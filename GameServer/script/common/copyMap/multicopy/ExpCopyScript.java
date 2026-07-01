package common.copyMap.multicopy;

import com.data.*;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Clone_level_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.behavior.manager.BehaviorManager;
import com.game.copymap.structs.ExpCopyData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * 凌云妖塔
 */
public class ExpCopyScript implements IMapBaseScript {

    final Logger logger = LogManager.getLogger(ExpCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ExpCopyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        ExpCopyData zone = new ExpCopyData();
        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());

        mapObject.setZone(zone);

        int mergeCount;
        int level = 0;
        List<Player> playerList = (List<Player>) objects[1];
        for (Player player : playerList) {
            //计算平均等级
            level += player.getLevel();

            //获取玩家进入副本的合并次数倍率
            mergeCount = Manager.copyMapManager.logic().getMergeCount(player, mapObject.getZoneModelId());
            player.getCloneSetting().remove(mapObject.getZoneModelId());
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.ExpCopy, mergeCount);

            //设置副本次数倍率
            zone.getMemberLevel().put(player.getId(), player.getLevel());
            zone.getTotalExp().put(player.getId(), 0L);
            zone.getMerge().put(player.getId(), mergeCount);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.ExpCopy, mergeCount);
            //记录永久通关次数
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.CopiesOfExperience, Count.RefreshType.CountType_Forever, mergeCount);
            Manager.controlManager.operate(player, FunctionVariable.CopiesOfExperience, mergeCount);
        }
        zone.setMonsterLv(level / playerList.size());

        Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(mapObject.getZoneModelId());

        //3秒后刷怪
        zone.setStartTime((int) (TimeUtils.Time() / 1000) + 3);
        zone.setEndTime((int) ((TimeUtils.Time() + bean.getExist_time()) / 1000));

        Cfg_Characters_Bean charactersBean = CfgManager.getCfg_Characters_Container().getValueByKey(zone.getMonsterLv());
        if (charactersBean == null) {
            logger.error("Cfg_Characters_Bean配置表不存在" + zone.getMonsterLv());
            return;
        }

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(zone.getZoneId() * 100 + zone.getLevel());
        if (clone_level_bean == null) {
            logger.error("clone_level_bean配置表不存在" + zone);
            return;
        }

        //3秒后开始刷怪
        mapObject.addMapOnceScriptEventTimer(getId(), "startRefresh", 3 * 1000);

        //副本结束计时器
        mapObject.addMapOnceScriptEventTimer(getId(), "copyEnd", bean.getExist_time());

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return Manager.copyMapManager.logic().checkEnterTimes(player, model);
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        ExpCopyData data = mapObject.getZone();

        int now = (int) (TimeUtils.Time() / 1000);
        CopyMapMessage.ResExpCopy.Builder builder = CopyMapMessage.ResExpCopy.newBuilder();
        builder.setStartTime(data.getStartTime() - now);
        builder.setEndTime(data.getEndTime() - now);
        builder.setMonsterNum(data.getKillNum());
        builder.setTotalExp(data.getTotalExp().get(player.getId()));
        MessageUtils.send_to_player(player, CopyMapMessage.ResExpCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject mapObject, boolean isQuit) {
        if (isQuit) {
            //主动退出清除鼓舞buff
            for (int i = 0; i < Global.Exp_Clone_Power_Up.size(); i++) {
                int buffId = Global.Exp_Clone_Power_Up.get(i).get(1);
                Manager.buffManager.deal().onRemoveBuff(player, buffId);
            }
            ExpCopyData data = mapObject.getZone();
            if (!mapObject.isStop()) {
                Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, 2, data == null ? 0 : data.getLevel(), false);
            }
        }
    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        ExpCopyData data = mapObject.getZone();
        if (data.isEnd()) {
            return;
        }
        data.setKillNum(data.getKillNum() + 1);

        boolean starChange = false;
        for (int i = 0; i < Global.Exp_Clone_Kill_num.size(); i++) {
            ReadArray<Integer> array = Global.Exp_Clone_Kill_num.get(i);
            if (array.get(1) <= data.getStar() || array.get(0) > data.getKillNum()) {
                continue;
            }
            data.setStar(array.get(1));
            starChange = true;
            //最后一个说明星数满了，副本结束
            if (i == Global.Exp_Clone_Kill_num.size() - 1) {
                data.setEnd(true);
            }
            break;
        }

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(data.getZoneId() * 100 + data.getLevel());

        //加普通经验
        Player player;
        Cfg_Characters_Bean bean;
        for (Map.Entry<Long, Long> entry : data.getTotalExp().entrySet()) {
            player = Manager.playerManager.getPlayerCache(entry.getKey());
            if (player == null || !mapObject.getPlayers().containsKey(player.getId())) {
                logger.info("玩家不存在或不在地图中");
                continue;
            }
            int multi = data.getMerge().get(player.getId());
            if (multi <= 0) {
                logger.error("玩家经验副本倍率为: " + multi);
            }
            int level = data.getMemberLevel().get(player.getId());
            bean = CfgManager.getCfg_Characters_Container().getValueByKey(level);
            if (bean == null) {
                logger.error("Cfg_Characters_Bean配置表不存在：" + data.getMonsterLv());
                return;
            }
            long finalExp = (long) (bean.getExpclone_monster_exp() * player.gainExpRate() * multi);
            Manager.currencyManager.manager().onChangeFinalExp(player, finalExp, ItemChangeReason.ExpCopyGet, IDConfigUtil.getLogId());

            data.getTotalExp().put(player.getId(), data.getTotalExp().get(player.getId()) + finalExp);

            //达到星数给百分比经验
            if (starChange) {
                if (clone_level_bean.getExtra_reward().get(0) == null || clone_level_bean.getExtra_reward().get(0).size() < data.getStar()) {
                    logger.error("Cfg_Clone_map_Bean配置表经验副本星数倍率配置错误：" + data.getStar());
                    continue;
                }
                long exp = (long) (bean.getExp() * (clone_level_bean.getExtra_reward().get(0).get(data.getStar() - 1) / 10000.));
                long starFinalExp = (long) (exp * player.gainExpRate() * multi);
                Manager.currencyManager.manager().onChangeFinalExp(player, starFinalExp, ItemChangeReason.ExpCopyGet, IDConfigUtil.getLogId());

                data.getTotalExp().put(player.getId(), data.getTotalExp().get(player.getId()) + starFinalExp);
            }
        }

        //满星结束
        if (data.isEnd()) {
            //同步一次消息再结算面板
            syncCopyData(mapObject);
            copyEnd(mapObject, true);
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "startRefresh":
                startRefresh(mapObject);
                break;
            case "syncCopyData":
                syncCopyData(mapObject);
                break;
            case "copyEnd":
                copyEnd(mapObject, false);
                break;
            case "finish":
                finish(mapObject);
                break;
            default:
        }
    }

    /**
     * 副本开始
     */
    private void startRefresh(MapObject mapObject) {
        //获取难度系数
        ExpCopyData data = mapObject.getZone();

        Cfg_Characters_Bean charactersBean = CfgManager.getCfg_Characters_Container().getValueByKey(data.getMonsterLv());
        int rate = charactersBean.getExpclone_monster_att();

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(data.getZoneId() * 100 + data.getLevel());

        for (int clone_monster : clone_level_bean.getClonemonster_id().getValue()) {
            Cfg_Clone_monster_Bean monsterBean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(clone_monster);
            if (monsterBean == null || monsterBean.getMonster_information().size() < 1) {
                logger.error("Cfg_Clone_monster_Bean配置表配置异常: " + clone_monster);
                continue;
            }
            for (int i = 0; i < monsterBean.getMonster_information().size(); i++) {
                ReadArray<Integer> info = monsterBean.getMonster_information().get(i);
                int x = info.get(2);
                int y = info.get(3);
                for (int j = 0; j < info.get(1); j++) {
                    if (monsterBean.getMonster_information().get(i).size() > 5) {
                        x = RandomUtils.random(Math.min(info.get(2), info.get(4)), Math.max(info.get(2), info.get(4)));
                        y = RandomUtils.random(Math.min(info.get(3), info.get(5)), Math.max(info.get(3), info.get(5)));
                    }
                    Monster monster = Manager.monsterManager.createMonster(mapObject, new Position(x, y), info.get(0), -3, rate);
                    monster.setScore(monster.getScore() * rate);
                }
            }
        }
        mapObject.addMapLoopScriptEventTimer(getId(), "syncCopyData", -1, 0, 1000);
    }

    /**
     * 副本结束
     */
    private void copyEnd(MapObject mapObject, boolean win) {

        //清除地图上的怪物
        for (Monster monster : mapObject.getMonsters().values()) {
            BehaviorManager.CancelAllBehavior(monster);
            Manager.mapManager.manager().onQuitMap(mapObject, monster, true);
        }

        if (win) {
            //策划要求提前打完两秒后才弹结算面板
            mapObject.addMapOnceScriptEventTimer(getId(), "finish", 2000);
            return;
        }

        finish(mapObject);
    }

    /**
     * 同步经验副本数据
     */
    private void syncCopyData(MapObject mapObject) {
        ExpCopyData data = mapObject.getZone();

        if (data.getLastKillNum() == data.getKillNum() || mapObject.getPlayers().isEmpty()) {
            return;
        }
        data.setLastKillNum(data.getKillNum());

        for (Map.Entry<Long, Long> entry : data.getTotalExp().entrySet()) {
            Player player = Manager.playerManager.getPlayerCache(entry.getKey());
            //能进副本一般缓存里是有的，一般不会为null
            if (player == null) {
                logger.error("玩家不存在");
                continue;
            }
            syncExpData(player, data, false, null);
        }
    }

    /**
     * 副本结算
     */
    private void finish(MapObject mapObject) {
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setStop(true);
        mapObject.setAutoRemove(true);

        ExpCopyData data = mapObject.getZone();

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(data.getZoneId() * 100 + data.getLevel());

        boolean isTeam = data.getMerge().size() > 1;
        for (Map.Entry<Long, Long> entry : data.getTotalExp().entrySet()) {
            int rate = data.getMerge().get(entry.getKey());
            List<Item> items = Item.createItems(clone_level_bean.getSuccess_reward(), rate);
            Player player = Manager.playerManager.getPlayerCache(entry.getKey());

            List<Item> marryActivityItems = Manager.marriageManager.activity().triggerDrop(player.getCareer(), -1, data.getZoneId(), rate);
            items.addAll(marryActivityItems);

            //结束清除鼓舞buff
//            for (int i = 0; i < Global.Exp_Clone_Power_Up.size(); i++) {
//                int buffId = Global.Exp_Clone_Power_Up.get(i).get(1);
//                Manager.buffManager.deal().onRemoveBuff(player, buffId);
//            }
            //能进副本一般缓存里是有的，一般不会为null
            if (player == null || !mapObject.getPlayers().containsKey(player.getId())) {
                logger.error("玩家不存在，或不在地图中");
                continue;
            }
            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet);
            }

            syncExpData(player, data, true, items);
            if (isTeam) {
                Manager.countManager.addCount(player, BaseCountType.GroupCopyMap, mapObject.getZoneModelId(), Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.GroupCopymap, 0);
            }
            for (int i = 0; i < rate; i++) {
                Manager.activityManager.cloneDropTrigger(player, mapObject.getZoneModelId());
            }
            Manager.currencyManager.manager().writeLog(player, 0, 0, data.getTotalExp().get(player.getId()), 0, ItemChangeReason.ExpCopyGet, 0, ItemCoinType.EXP);
            Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, 1, data.getLevel(), true);
        }
    }

    /**
     * 同步经验副本数据
     *
     * @param isEnd 副本是否结束
     */
    private void syncExpData(Player player, ExpCopyData data, boolean isEnd, List<Item> items) {
        CopyMapMessage.ResSyncMonsterExp.Builder builder = CopyMapMessage.ResSyncMonsterExp.newBuilder();
        builder.setIsEnd(isEnd);
        builder.setMonsterNum(data.getKillNum());
        builder.setTotalExp(data.getTotalExp().get(player.getId()));
        if (items != null) {
            for (Item item : items) {
                CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                itemInfo.setItemId(item.getItemModelId());
                itemInfo.setNum(item.getNum());
                itemInfo.setBind(item.isBind());
                builder.addList(itemInfo);
            }
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResSyncMonsterExp.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject mapObject) {
    }

}
