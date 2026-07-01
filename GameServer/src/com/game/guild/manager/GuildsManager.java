package com.game.guild.manager;

import com.game.db.bean.GuildBean;
import com.game.db.bean.GuildMemberBean;
import com.game.db.dao.GuildDao;
import com.game.db.dao.GuildMemberDao;
import com.game.guild.command.*;
import com.game.guild.script.*;
import com.game.guild.structs.Guild;
import com.game.guild.structs.GuildMember;
import com.game.guild.structs.GuildSysConfig;
import com.game.guild.timer.GuildTimer;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;


public class GuildsManager {

    private static final Logger log = LogManager.getLogger(GuildsManager.class);

    private enum Singleton {
        INSTANCE;
        GuildsManager manager;

        Singleton() {
            this.manager = new GuildsManager();
        }

        GuildsManager getProcessor() {
            return manager;
        }
    }

    public static GuildsManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    /**
     * 公会名称对应的公会Id
     */
    private HashMap<String, Long> guildName = new HashMap<>();

    /**
     * 已成立的公会
     */
    private ConcurrentHashMap<Long, Guild> guildMap = new ConcurrentHashMap<>();

    /**
     * 所有公会成员
     */
    private ConcurrentHashMap<Long, GuildMember> guildMembers = new ConcurrentHashMap<>();

    public void loadAllGuildData() throws Exception {
        log.info("开始加载所有公会数据开始...");
        long beginTime = TimeUtils.Time();
        GuildDao guildDao = new GuildDao();
        List<GuildBean> guildList = guildDao.selectAll();
        for (GuildBean bean : guildList) {
            Guild guild = bean.toGuild(bean);

            guildMap.put(guild.getId(), guild);
            guildName.put(guild.getName(), guild.getId());
        }
        log.info("加载所有公会数据结束 所用时间：" + (TimeUtils.Time() - beginTime));

        log.info("开始加载所有公会成员数据开始...");
        long beginTime1 = TimeUtils.Time();
        GuildMemberDao guildMemberDao = new GuildMemberDao();
        List<GuildMemberBean> guildMemberList = guildMemberDao.selectAll();
        for (GuildMemberBean bean : guildMemberList) {
            GuildMember guildMember = bean.toGuildMember();
            PlayerWorldInfo worldInfo = Manager.playerManager.getPlayerWorldInfo(guildMember.getId());
            if (worldInfo != null) {
                guildMember.setPlayerWorldInfo(worldInfo);
                guildMembers.put(guildMember.getId(), guildMember);
                Guild guild = guildMap.get(guildMember.getGuildId());
                if (guild != null) {
                    guild.getMembers().put(guildMember.getId(), guildMember);
                    if (guildMember.getPosition() == GuildSysConfig.TYPE_MASTER) {
                        guild.setChairMan(guildMember);
                    }
                    Player player = Manager.playerManager.getPlayer(guildMember.getId());
                    if (player != null){
                        player.setGuildId(guild.getId());
                    }
                } else {
                    if (guildMember.getGuildId() != 0) {
                        log.error("加载数据异常,公会不存在,需要解散:" + guildMember.getGuildId());
                        guildMember.setGuildId(0);
                        guildMember.setJoinTime(0);
                        guildMember.setContribute(0);
                        guildMember.setPosition(GuildSysConfig.TYPE_MEMBER);
                        Player player = Manager.playerManager.getPlayer(guildMember.getId());
                        if (player != null) {
                            player.setGuildId(0);
                            player.setGuildName("");
                        }
                    }
                }
            } else {
                log.error("加载数据异常,玩家不存在:" + guildMember.getId());
            }
        }
        log.info("加载所有公会成员数据结束 所用时间：" + (TimeUtils.Time() - beginTime1));

        Calendar calendar = Calendar.getInstance();
        int second = calendar.get(Calendar.SECOND);
        int millis = calendar.get(Calendar.MILLISECOND);
        long delay = 60 * 1000 - second * 1000 - millis;
        GameServer.getInstance().getAssistThread().addTimerEvent(new GuildTimer(delay));
    }

    public IGuildManagerScript manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GuildManagerBaseScript);
        if (is instanceof IGuildManagerScript) {
            return (IGuildManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public void createGuild(Player player, String name, int icon, String notice) {
        GameServer.getInstance().getAssistThread().addCommand(new CreateGuildCommand(player, name, icon, notice));
    }

    public void reqApplyGuild(Player player, List<Long> guildIds) {
        GameServer.getInstance().getAssistThread().addCommand(new ApplyGuildCommand(player, guildIds));
    }

    public void reqApplyDelGuild(Player player, List<Long> playerIds, boolean flag) {
        GameServer.getInstance().getAssistThread().addCommand(new DealApplyGuildCommand(player, playerIds, flag));
    }

    public void reqChangeGuildName(Player player, String name) {
        GameServer.getInstance().getAssistThread().addCommand(new ChangeGuildNameCommand(player, name));
    }

    public void reqChangeGuildRank(Player player, long roleId, int rank) {
        GameServer.getInstance().getAssistThread().addCommand(new ChangeGuildRankCommand(player, roleId, rank));
    }

    public void reqGuildConstructUp(Player player, int type) {
        GameServer.getInstance().getAssistThread().addCommand(new DealGuildConstructUpCommand(player, type));
    }

    public void reqImpeachMaster(Player player) {
        GameServer.getInstance().getAssistThread().addCommand(new ImpeachMasterCommand(player));
    }

    public void changeGuildExpCommand(Guild guild, long exp, int res, long actionId) {
        GameServer.getInstance().getAssistThread().addCommand(new ChangeGuildExpCommand(guild, exp, res, actionId));
    }

    public void playerOnLine(Player player) {
        manager().playerOnLine(player);
    }

    public void playerOffLine(Player player) {

    }

    public Guild getGuildById(long id) {
        return guildMap.get(id);
    }

    public Guild GetGuildByPlayer(Player p) {
        return guildMap.get(p.getGuildId());
    }


    public ConcurrentHashMap<Long, Guild> getGuildMap() {
        return guildMap;
    }

    public void setGuildMap(ConcurrentHashMap<Long, Guild> GuildMap) {
        this.guildMap = GuildMap;
    }

    public ConcurrentHashMap<Long, GuildMember> getGuildMembers() {
        return guildMembers;
    }

    public void setGuildMembers(ConcurrentHashMap<Long, GuildMember> guildMembers) {
        this.guildMembers = guildMembers;
    }

    public boolean isHaveName(String name) {
        return this.guildName.containsKey(name);
    }

    public HashMap<String, Long> getGuildName() {
        return guildName;
    }

    public void setGuildName(HashMap<String, Long> guildName) {
        this.guildName = guildName;
    }


}
