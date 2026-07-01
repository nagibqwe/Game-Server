package common.copyMap.crosscopy;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.bean.*;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.type.ReviveBehavior;
import com.game.bi.enums.ResourceType;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.copymap.structs.FightRoomState;
import com.game.copymap.structs.GodDevilWarCopyData;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.ReliveType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.structs.Hatred;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 神魔战场（三界战场）
 */
public class GodDevilWarScript implements ICopyReliveScript, IMapBaseScript {

    private static final int Damage = 100000;//每伤害10W数值增加积分
    private static final int DamageScore = 1;//伤害增加的分数
    private static final int AssistsScore = 10;//助攻击杀增加积分
    private static final int KillScore = 20;//击杀增加积分
    private static final int CampKillScore = 50;//阵营击杀怪物增加积分
    private static final int HookScore = 40;//挂机每分钟增加积分

    private static final int camp1 = 0;
    private static final int camp2 = 1;
    private static final int camp3 = 2;

    private static final Logger log = LogManager.getLogger(GodDevilWarScript.class);

    @Override
    public int getId() {
        return ScriptEnum.GodDevilWarCrossActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        Player player = (Player) objects[0];
        String method = (String) objects[1];
        if ("onReqCloneFightInfo".equals(method)) {
            onReqCloneFightInfo(player);
        }
        return null;
    }

    //积分榜
    private void onReqCloneFightInfo(Player player) {
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        CopyMapMessage.ResTeamCampWarRank.Builder builder = CopyMapMessage.ResTeamCampWarRank.newBuilder();
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);

        TreeMap<Integer, Long> rankMap = new TreeMap<>(Comparator.comparingInt(n -> (int) n).reversed());
        //积分排名
        for (Map<Long, Integer> scoreMap : data.getScoreMap().values()) {
            for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
                rankMap.put(entry.getValue(), entry.getKey());
            }
        }
        int index = 1;
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            CopyMapMessage.RankInfo.Builder info = CopyMapMessage.RankInfo.newBuilder();
            Player cachePlayer = Manager.playerManager.getPlayerCache(entry.getValue());
            if (cachePlayer == null) {
                log.error("查看积分榜，玩家已不在缓存中！,id = " + entry.getValue());
                continue;
            }
            info.setCamp(data.getCampMap().get(entry.getValue()));
            info.setName(cachePlayer.getName());
            info.setLv(cachePlayer.getLevel());
            info.setFight(cachePlayer.getFightPoint());
            info.setPoints(entry.getKey());
            if (entry.getValue() == player.getId()) {
                builder.setSelfScore(entry.getKey());
                builder.setSelfRank(index);
            }
            index++;
            builder.addRankInfo(info);
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResTeamCampWarRank.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        data.getScoreMap().put(camp1, new ConcurrentHashMap<>());
        data.getScoreMap().put(camp2, new ConcurrentHashMap<>());
        data.getScoreMap().put(camp3, new ConcurrentHashMap<>());

        //每秒中同步战场数据
        mapObject.addMapLoopScriptEventTimer(getId(), "syncCloneInfo", -1, 0, 1000);

        //每分钟增加战场积分
        mapObject.addMapLoopScriptEventTimer(getId(), "addHookScore", -1, 0, 60 * 1000);

        //结束倒计时
        long endTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.GodDevilWar.getValue());
        mapObject.addMapOnceScriptEventTimer(getId(), "copyEnd", endTime - TimeUtils.Time());

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        long endTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.GodDevilWar.getValue());
        return endTime != 0;
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(map);
        int minCamp = data.getScoreMap().get(camp1).size() <= data.getScoreMap().get(camp2).size() ? camp1 : camp2;
        minCamp = data.getScoreMap().get(minCamp).size() <= data.getScoreMap().get(camp3).size() ? minCamp : camp3;

        //设置战场数据
        if (!data.getCampMap().containsKey(player.getId())) {
            data.getCampMap().put(player.getId(), minCamp);
            data.getScoreMap().get(minCamp).put(player.getId(), 0);
            data.getDamageMap().put(player.getId(), 0L);
            data.getScoreReward().put(player.getId(), 0);
        }
        int camp = data.getCampMap().get(player.getId());
        log.info(String.format("玩家[%s]进入神魔战场地图, camp：%s, score: %s, damage: %s",
                player.getName(), camp, data.getScoreMap().get(camp).get(player.getId()), data.getDamageMap().get(player.getId())));

        player.setCamp(camp, true);
        Manager.playerManager.manager().onUpdatePkState(player, PlayerDefine.PkStateCamp, true);

        //切换地图
        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        ReadIntegerArrayEs born = bean.getBornPosition();
        for (int i = 0; i < born.size(); i++) {
            if (i != camp) {
                continue;
            }
            Position pos = MapManager.getPos(born.get(i).get(0), born.get(i).get(1));
            Manager.mapManager.changeMap(player, map.getId(), pos, false);
        }

        //发送倒计时
        CopyMapMessage.ResCopymapNeedTime.Builder msg = CopyMapMessage.ResCopymapNeedTime.newBuilder();

        long endTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.GodDevilWar.getValue());
        msg.setEndTime((int) ((endTime - TimeUtils.Time()) / 1000));
        log.error(msg.getEndTime());
        msg.setModelId(map.getZoneModelId());
        msg.setWaitEndToStart(0);
        MessageUtils.send_to_player(player, CopyMapMessage.ResCopymapNeedTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.GodDevilWar.type());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
    }

    @Override
    public void onDamage(MapObject map, Monster defense, long damage, Fighter attacker) {
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(map);
            damage += data.getDamageMap().get(player.getId());
            data.getDamageMap().put(player.getId(), damage);

            //10w伤害=1积分
            int score = (int) (damage / Damage) * DamageScore;
            if (score > 0) {
                addScore(map, player, score);
                data.getDamageMap().put(player.getId(), damage % Damage);
            }
        }
    }

    /**
     * 增加积分
     */
    private void addScore(MapObject mapObject, Player player, int score) {
        if (player == null) {
            return;
        }
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        int camp = data.getCampMap().get(player.getId());
        int lastScore = data.getScoreMap().get(camp).get(player.getId());
        data.getScoreMap().get(camp).put(player.getId(), lastScore + score);
        //BI
        BigInteger bigChange = BigInteger.valueOf(score);
        BigInteger beforeNum = BigInteger.valueOf(lastScore);
        BigInteger afterNum = BigInteger.valueOf(lastScore + score);
        Manager.biManager.getScript().biResource(player, 1, ResourceType.GodDevilWarScore.getId(),bigChange,beforeNum,afterNum,0,0, ItemChangeReason.GodDevilRewardGet,IDConfigUtil.getLogId());

        checkScoreReward(mapObject, player, lastScore + score);
    }

    /**
     * 检查分数是否满足奖励条件
     */
    private void checkScoreReward(MapObject mapObject, Player player, int score) {
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        Cfg_Characters_Bean charBean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        for (Cfg_SZZQScoreAward_Bean bean : CfgManager.getCfg_SZZQScoreAward_Container().getValuees()) {
            if (score >= bean.getScore() && bean.getScore() > data.getScoreReward().get(player.getId())) {
                List<Item> items = new ArrayList<>(Item.createItems(bean.getAward()));
                //经验奖励
                long num = charBean.getSZZQ_EXP_award().get(bean.getUes_exp_index());
                Manager.currencyManager.manager().addEXP(player, num, ItemChangeReason.GodDevilRewardGet, IDConfigUtil.getLogId());

                log.info(String.format("玩家积分：%s, 获得积分奖励：%s", score, bean.getScore()));
                Manager.crossServerManager.getCrossServer().sendReward(player, items, ItemChangeReason.GodDevilRewardGet);
                //设置领取标志
                data.getScoreReward().put(player.getId(), bean.getScore());
            }
        }
    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        HashMap<Integer, Long> campDamage = new HashMap<>();
        //计算各阵营伤害量
        List<Hatred> hatredList = monster.getHatreds();
        for (Hatred hatred : hatredList) {
            if (!data.getCampMap().containsKey(hatred.getTarget().getId())) {
                continue;
            }
            if (!data.getCampMap().containsKey(hatred.getTarget().getId())) {
                continue;
            }
            int camp = data.getCampMap().get(hatred.getTarget().getId());
            long lastDamage = campDamage.getOrDefault(camp, 0L);
            campDamage.put(camp, lastDamage + hatred.getHatred());
        }
        long maxDamage = 0L;
        long maxDamageCamp = 0;
        for (Map.Entry<Integer, Long> entry : campDamage.entrySet()) {
            if (entry.getValue() > maxDamage) {
                maxDamageCamp = entry.getKey();
                maxDamage = entry.getValue();
            }
        }
        log.info("怪物死亡，最大伤害阵营：" + maxDamageCamp);
        //最大伤害的阵营全员加积分
        for (Map.Entry<Long, Integer> entry : data.getCampMap().entrySet()) {
            if (entry.getValue() == maxDamageCamp) {
                Player player = mapObject.getPlayers().get(entry.getKey());
                addScore(mapObject, player, CampKillScore);
            }
        }
    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        if (attacker instanceof Player) {
            Player attackPlayer = (Player) attacker;
            GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
            //击杀者积分奖励
            addScore(mapObject, attackPlayer, KillScore);
            log.info(String.format("玩家[%s]直接击杀[%s],获得积分：%s", attackPlayer.getName(), player.getName(), KillScore));

            //助攻者积分奖励
            for (Hatred hatred : player.getHatreds()) {
                if (!data.getCampMap().containsKey(hatred.getTarget().getId())) {
                    continue;
                }
                if (hatred.getTarget().getId() != attacker.getId()) {
                    Player fighter = mapObject.getPlayers().get(hatred.getTarget().getId());
                    addScore(mapObject, fighter, AssistsScore);
                    log.info(String.format("玩家[%s]助攻击杀[%s],获得积分：%s", fighter == null ? "null" : fighter.getName(), player.getName(), AssistsScore));
                }
            }
            Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(mapObject.getMapModelId());
            ReadIntegerArrayEs born = bean.getBornPosition();
            Integer camp = data.getCampMap().get(player.getId());
            Position pos = MapManager.getPos(born.get(camp).get(0), born.get(camp).get(1));
            player.changeCurPos(pos,true);
            //复活处理
            ReviveBehavior rb = new ReviveBehavior(player);
            rb.setReviveTime(TimeUtils.Time() + 30000);
            BehaviorManager.InsertBehavior(player, rb);
        }
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "syncCloneInfo":
                syncCloneInfo(mapObject);
                break;
            case "addHookScore":
                hookAddScore(mapObject);
                break;
            case "copyEnd":
                copyEnd(mapObject);
                break;
        }
    }

    /**
     * 同步战场数据
     */
    private void syncCloneInfo(MapObject mapObject) {
        CopyMapMessage.ResTeamCampWar.Builder builder = CopyMapMessage.ResTeamCampWar.newBuilder();

        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        TreeMap<Integer, Long> rankMap = new TreeMap<>(Comparator.comparingInt(n -> (int) n).reversed());

        //计算阵营总积分和个人积分排名
        for (Integer camp : data.getScoreMap().keySet()) {
            Map<Long, Integer> scoreMap = data.getScoreMap().get(camp);
            int totalScore = 0;
            for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
                rankMap.put(entry.getValue(), entry.getKey());
                totalScore += entry.getValue();
            }

            //阵营积分信息
            CopyMapMessage.CampInfo.Builder campInfo = CopyMapMessage.CampInfo.newBuilder();
            campInfo.setCamp(camp);
            campInfo.setCount(scoreMap.size());
            campInfo.setPoints(totalScore);
            builder.addCampInfo(campInfo);
        }

        //积分排名信息
        int rank = 0;
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            rank++;
            Player player = mapObject.getPlayers().get(entry.getValue());
            if (player == null) {
                continue;
            }
            builder.setSelfRank(rank);
            builder.setSelfScore(entry.getKey());
            MessageUtils.send_to_player(player, CopyMapMessage.ResTeamCampWar.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    /**
     * 每分钟地图挂机玩家增加积分
     */
    private void hookAddScore(MapObject mapObject) {
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        Player player;
        for (Long playerId : data.getCampMap().keySet()) {
            player = mapObject.getPlayers().get(playerId);
            if (player == null) {
                continue;
            }
            addScore(mapObject, player, HookScore);
            log.info(String.format("玩家[%s]每分钟挂机增加积分 %s", player.getName(), HookScore));
        }
    }

    /**
     * 副本结算
     */
    private void copyEnd(MapObject mapObject) {
        mapObject.setStop(true);

        //结算时玩家复活
        for (Player player : mapObject.getPlayers().values()) {
            if (player.isDie()) {
                Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).OnPlayerRelive(player, ReliveType.Gm, false, player.gainCurPos());
            }
        }

        //计算胜利阵营和积分排名
        GodDevilWarCopyData data = MapParam.getGodDevilWarCopyData(mapObject);
        int[] campScore = {0, 0, 0};
        TreeMap<Integer, Long> rankMap = new TreeMap<>(Comparator.comparingInt(n -> (int) n).reversed());
        for (Integer camp : data.getScoreMap().keySet()) {
            Map<Long, Integer> scoreMap = data.getScoreMap().get(camp);
            int totalScore = 0;
            for (Map.Entry<Long, Integer> entry : scoreMap.entrySet()) {
                rankMap.put(entry.getValue(), entry.getKey());
                totalScore += entry.getValue();
            }
            campScore[camp] = totalScore;
        }
        int topCamp = campScore[camp1] > campScore[camp2] ? camp1 : camp2;
        topCamp = campScore[topCamp] > campScore[camp3] ? topCamp : camp3;

        log.info("神魔战场胜利阵营：" + topCamp);

        //发送奖励
        int rank = 0;
        Player player;
        for (Map.Entry<Integer, Long> entry : rankMap.entrySet()) {
            rank++;
            player = Manager.playerManager.getPlayerCache(entry.getValue());
            if (player == null) {
                continue;
            }
            int camp = data.getCampMap().get(entry.getValue());
            log.info(String.format("玩家[%s]在神魔战场中阵营为：%s, 获得排名：%s, 是否胜利：%s", player.getName(), camp, rank, camp == topCamp));
            sendCloneEndReward(mapObject, player, rank, entry.getKey(), camp == topCamp);
        }

        //通知公共服副本结束
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
    }

    /**
     * 发送结算奖励
     *
     * @param rank  排名
     * @param score 积分
     * @param isWin 是否胜利
     */
    private void sendCloneEndReward(MapObject mapObject, Player player, int rank, int score, boolean isWin) {
        CopyMapMessage.ResTeamCampWarEndInfo.Builder builder = CopyMapMessage.ResTeamCampWarEndInfo.newBuilder();
        builder.setSelfRank(rank);
        builder.setSelfScore(score);

        List<Item> rewardItems = new ArrayList<>();
        //胜利阵营奖励
        Cfg_Clone_map_Bean cloneBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());
        if (isWin) {
            if (cloneBean == null) {
                log.error("Cfg_Clone_map_Bean配置表不存在:" + mapObject.getZoneModelId());
                return;
            }
            rewardItems.addAll(Item.createItems(cloneBean.getSuccess_reward()));
        }

        //积分排名奖励
        Cfg_SZZQAward_Bean awardBean = null;
        for (Cfg_SZZQAward_Bean bean : CfgManager.getCfg_SZZQAward_Container().getValuees()) {
            if (bean.getRank_max() >= rank && bean.getRank_min() <= rank) {
                awardBean = bean;
                break;
            }
        }
        if (awardBean != null) {
            Cfg_Characters_Bean charactersBean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (charactersBean == null) {
                log.error("Cfg_Characters_Bean配置表不存在：" + player.getLevel());
                return;
            }
            rewardItems.addAll(Item.createItems(awardBean.getAward()));
            //经验奖励
            long num = charactersBean.getSZZQ_EXP_rank_award().get(awardBean.getUes_exp_index());
            Manager.currencyManager.manager().addEXP(player, num, ItemChangeReason.GodDevilRewardGet, IDConfigUtil.getLogId());

            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(ItemCoinType.EXP);
            itemInfo.setNum(num);
            itemInfo.setBind(false);
            builder.addRewardlist(itemInfo);
        }
        Manager.crossServerManager.getCrossServer().sendReward(player, rewardItems, ItemChangeReason.GodDevilRewardGet);
        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(mapObject, player);

        for (Item item : rewardItems) {
            CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
            itemInfo.setItemId(item.getItemModelId());
            itemInfo.setNum(item.getNum());
            itemInfo.setBind(item.isBind());
            builder.addRewardlist(itemInfo);
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResTeamCampWarEndInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 计算副本复活点
     *
     * @param map
     * @param player
     * @return
     */
    @Override
    public Position doCreateRelivePosition(MapObject map, Player player) {
        return map.getRandomRelivePos();
    }
}
