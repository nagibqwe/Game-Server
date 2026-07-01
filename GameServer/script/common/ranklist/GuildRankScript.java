package common.ranklist;

import com.data.Global;
import com.game.chat.structs.Notify;
import com.game.db.bean.RankPlayer;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.ranklist.manager.RankListManager;
import com.game.ranklist.script.IGuildRankScript;
import com.game.ranklist.structs.RankType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.message.RankListMessage;
import game.message.RankListMessage.RankInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 帮会排行榜
 */
public class GuildRankScript implements IGuildRankScript {

    private static final Logger log = LogManager.getLogger(GuildRankScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GuildRankScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public int getRankType() {
        return RankType.GUILD_RANK;
    }

    @Override
    public List<RankInfo.Builder> getRankInfo() {
        List<RankListMessage.RankInfo.Builder> rankInfoList = new ArrayList<>();
        ConcurrentHashMap<Long, Guild> guildIdMap = Manager.guildsManager.getGuildMap();
        ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(getRankType());
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            int rank = entry.getKey();
            long guildId = entry.getValue();
            Guild guild = guildIdMap.get(guildId);
            if (guild == null) {
                log.error("找不到帮会Id=" + guildId + " 的帮会，其排名rank=" + rank);
                continue;
            }
            RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(guildId);
            if (rankPlayer == null) {
                return null;
            }
            Manager.rankListManager.deal().buildRankInfo(entry.getKey(), rankPlayer, false, String.valueOf(guild.gainGuildPower()), rankInfoList,-1);
        }
        return rankInfoList;
    }

    @Override
    public boolean canRank(RankPlayer rankPlayer) {
        return getCompareValue(rankPlayer) > 0;
    }

    @Override
    public int compareRankPlayer(RankPlayer p1, RankPlayer p2) {
        return 0;
    }

    @Override
    public long getCompareValue(RankPlayer rankPlayer) {
        return rankPlayer.getLevel();
    }

    /**
     * 只对宗派做排序
     * 按照宗派成员top10战力的平均值排
     */
    @Override
    public List<Guild> sortGuildRankOnly() {
        List<Guild> list = new ArrayList<>(Manager.guildsManager.getGuildMap().values());
        list.sort(Comparator.comparingLong(Guild::gainGuildPower).thenComparingInt(Guild::getLevel).thenComparingInt(Guild::getCreateTime).reversed());
        return list;
    }

    //帮会排行数据发生变化则重新排行
    @Override
    public void sortGuildRank() {
        List<Guild> guildList = sortGuildRankOnly();
        ConcurrentHashMap<Long, RankPlayer> rankPlayerMap = RankListManager.getRankPlayerMap();
        ConcurrentHashMap<Integer, Long> guildRankMap = RankListManager.getTempRankMap().get(getRankType());
        guildRankMap.clear();

        int N = Math.min(Manager.rankListManager.getRankMax(RankType.GUILD_RANK), guildList.size());
        for (int i = 0; i < N; i++) {
            Guild guild = guildList.get(i);

            // 判断帮主是否存在于rankPlayerMap中，不存在要放入(因为要获取帮主外观形象和进行崇拜)
            long chairmanId = guild.getChairMan().getId();
            if (!rankPlayerMap.containsKey(guild.getId())) {
                PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(chairmanId);
                if (player == null) {
                    log.error("guild rank 找不到帮主玩家，Id=" + guild.getId());
                    continue;
                }
                Manager.rankListManager.deal().initGuild(guild.getId(), player, guild.getName());
            }
            guildRankMap.put(i + 1, guild.getId());
        }
    }

    /**
     * 福地特殊排序
     *
     * @return
     */
    @Override
    public List<Guild> sortFuDiGuildRank() {
        List<Guild> list = new ArrayList<>(Manager.guildsManager.getGuildMap().values());
        list.sort((o1, o2) -> {
            long o1p = o1.gainGuildPower();
            long o2p = o2.gainGuildPower();
            if (o2p > o1p)
                return 1;
            else if (o2p == o1p && o2.getCreateTime() > o1.getCreateTime())
                return 1;
            return -1;
        });
        return list;
    }

    /**
     * gm查看公会的战斗力
     *
     * @param player
     * @param guildName
     * @param isFuDi
     * @return
     */
    @Override
    public void gmFuDiPower(Player player, String guildName, boolean isFuDi) {
        for (Guild guild : Manager.guildsManager.getGuildMap().values()) {
            if (isFuDi && guild.getTitleRankMap().isEmpty()) {
                continue;
            }
            if (guildName.equals("") || guildName.equals(guild.getName())) {
                gmFuDiPower(player, guild);
            }
        }
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, "======fuDi success======");
    }

    public void gmFuDiPower(Player player, Guild guild) {
        // 宗派内部需要重新排名
        StringBuilder sb = new StringBuilder();
        List<GuildMember> memberList = new ArrayList<>(guild.getMembers().values());
        long all = 0;
        int count = 0;
        int max = Math.min(Global.GuildFightLimit, memberList.size());
        for (int j = 0; j < max; j++) {
            GuildMember guildMember = memberList.get(j);
            Player p = Manager.playerManager.getPlayer(guildMember.getId());
            sb.append(p.getName()).append(":");

            long power = guildMember.gainPower();
            sb.append(power).append("/");
            if (power < 0) continue;
            all += power;
            count++;
        }

        long avg = all;
        if (count != 0) avg = all / count;
        String str = guild.getName() + " sort_num:" + max + " all_power:" + all + " avg_power:" + avg + " /// " + sb.toString();
        MessageUtils.notify_player(player, Notify.CHAT_SYS_BULL, str);
    }
}
