package common.copyMap.multicopy;

import com.data.*;
import com.data.bean.Cfg_Clone_level_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.*;
import com.game.backpack.structs.ExpValueItem;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.EquipCopyData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.welfare.struct.RetrieveType;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EquipCopyScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(EquipCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.EquipCopyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        try {
            Player player = (Player) objects[0];
            String method = (String) objects[1];
            switch (method) {
                case "refreshMonster":
                    MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
                    if (mapObject == null) {
                        logger.error("mapObject is null!!");
                        return null;
                    }
                    refreshMonster(mapObject);
                    break;
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        Player leader = (Player) objects[0];

        EquipCopyData zone = new EquipCopyData();
        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());
        zone.setStage(1);
        zone.setLeaderSex(leader.getSex());
        mapObject.setZone(zone);

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(zone.getZoneId() * 100 + zone.getLevel());

        int bossNum = 0;
        for (int cloneMonsterId : clone_level_bean.getClonemonster_id().getValue()) {
            zone.getClone_monster_list().add(cloneMonsterId);
            Cfg_Clone_monster_Bean monsterBean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(cloneMonsterId);
            if (monsterBean == null) {
                logger.error("Cfg_Clone_monster_Bean配置表不存在：" + cloneMonsterId);
                return;
            }
            if (monsterBean.getIf_end() == 1) {
                monsterBean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(cloneMonsterId + zone.getLeaderSex());
            }
            for (ReadArray<Integer> array : monsterBean.getMonster_information().getValuees()) {
                bossNum += array.get(1);
            }
            if (monsterBean.getIf_end() == 1) {
                break;
            }
        }
        zone.setRemainBossNum(bossNum);
        zone.setStartTime((int) ((TimeUtils.Time() / 1000) + 6));
        zone.setEndTime((int) (TimeUtils.Time() / 1000) + bean.getExist_time() / 1000);

        //副本结束计时器
        mapObject.addMapOnceScriptEventTimer(getId(), "copyEnd", bean.getExist_time());
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return Manager.copyMapManager.logic().checkEnterTimes(player, model);
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        //设置倍率
        EquipCopyData data = mapObject.getZone();
        if (!data.getMerge().containsKey(player.getId())) {
            int mergeCount = Manager.copyMapManager.logic().getMergeCount(player, mapObject.getZoneModelId());
            data.getMerge().put(player.getId(), mergeCount);
            player.getCloneSetting().remove(mapObject.getZoneModelId());

            //扣次数
            Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(mapObject.getZoneModelId());
            if (bean == null) {
                return;
            }
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.EquipCopy, mergeCount);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.XinMoCopy, mergeCount);
            //记录永久通关次数
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.CopyOfHeartDevi, Count.RefreshType.CountType_Forever, mergeCount);
            Manager.controlManager.operate(player, FunctionVariable.CopyOfHeartDevi, mergeCount);
        }
        CopyMapMessage.ResManyCopy.Builder builder = CopyMapMessage.ResManyCopy.newBuilder();
        builder.setStartTime(data.getStartTime() - (int) (TimeUtils.Time() / 1000));
        builder.setEndTime(data.getEndTime() - (int) (TimeUtils.Time() / 1000));
        builder.setMonsterNum(data.getRemainBossNum());
        builder.setStage(data.getStage());
        MessageUtils.send_to_player(player, CopyMapMessage.ResManyCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject mapObject, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {
    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        EquipCopyData data = mapObject.getZone();

        if (data.getMonsterIds().contains(monster.getId())) {
            data.getMonsterIds().remove(monster.getId());
            data.setRemainBossNum(data.getRemainBossNum() - 1);

            //同步怪物数量
            CopyMapMessage.ResSyncManyCopy.Builder builder = CopyMapMessage.ResSyncManyCopy.newBuilder();
            builder.setMonsterNum(data.getRemainBossNum());
            builder.setStage(data.getStage());
            MessageUtils.send_to_map(mapObject, CopyMapMessage.ResSyncManyCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());

            if (data.getMonsterIds().isEmpty()) {
                //强制小怪死亡
                forceMonsterDie(mapObject, attacker);

                //是否最后一波
                if (data.getClone_monster_list().isEmpty()) {
                    finish(mapObject, true, 1);
                    return;
                }
                //刷新下一波怪
                data.setStage(data.getStage() + 1);
            }
        }

    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    /**
     * 强制地图上的怪物死亡
     */
    private void forceMonsterDie(MapObject mapObject, Fighter attacker) {
        ConcurrentHashMap<Long, Monster> monsters = mapObject.getMonsters();
        for (Monster monster : monsters.values()) {
            if (monster.getCamp() != 0) {
                continue;
            }
            monster.setDie();
            monster.doDie(attacker);
            MapUtils.sendDead(attacker, monster);
        }
    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "copyEnd":
                finish(mapObject, false, 3);
                break;
        }
    }

    /**
     * 刷怪，一波怪物中包含boss和小怪
     * 前面几波，小怪是观众，不能被攻击。BOSS死亡小怪强制死亡
     * 最后一波刷队长模型放大的怪
     */
    private void refreshMonster(MapObject mapObject) {

        EquipCopyData zone = mapObject.getZone();

        if (mapObject.isStop() || mapObject.getMonsters().size() > 0) {
            return;
        }
        if (zone.getClone_monster_list().isEmpty()) {
            return;
        }

        for (Player player : mapObject.getPlayers().values()) {
            if (zone.getStage() == 1) {
                break;
            }
            Manager.mapManager.changeMap(player, mapObject.getId(), mapObject.getBrithPos(), false);
        }

        if (zone.getClone_monster_list().size() == 1) {
            int bossId = zone.getClone_monster_list().remove(0);
            Cfg_Clone_monster_Bean boss = CfgManager.getCfg_Clone_monster_Container().getValueByKey(bossId + zone.getLeaderSex());

            List<Monster> bossList = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), boss.getMonsterWave());
            bossList.forEach(n -> zone.getMonsterIds().add(n.getId()));

        } else {
            int bossId = zone.getClone_monster_list().remove(0);
            Cfg_Clone_monster_Bean boss = CfgManager.getCfg_Clone_monster_Container().getValueByKey(bossId);
            List<Monster> bossList = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), boss.getMonsterWave());
            bossList.forEach(n -> zone.getMonsterIds().add(n.getId()));

            int monsterId = zone.getClone_monster_list().remove(0);
            Cfg_Clone_monster_Bean monster = CfgManager.getCfg_Clone_monster_Container().getValueByKey(monsterId);
            List<Monster> normalList = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), monster.getMonsterWave());
            normalList.forEach(n -> n.setCamp(0, true));
        }
    }

    /**
     * 副本结算奖励
     *
     * @param success 挑战成功/失败
     */
    private void finish(MapObject mapObject, boolean success, int type) {
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setAutoRemove(true);
        mapObject.setStop(true);

        EquipCopyData data = mapObject.getZone();

        int starNum = 0;
        //挑战成功，计算星数
        if (success) {
            starNum = calcStarNum(data.getEndTime());
        }
        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(data.getZoneId() * 100 + data.getLevel());

        //获取星数奖励
        ReadLongArrayEs rewardMap = clone_level_bean.getExtra_reward();

        List<ReadArray<Long>> rewardList = new ArrayList<>();
        for (int i = 0; i < rewardMap.size(); i++) {
            ReadArray<Long> array = rewardMap.get(i);
            if (array.get(2) == starNum) {
                rewardList.add(new ReadLongArray(new Long[]{array.get(0), array.get(1)}));
            }
        }
        //成功奖励
        rewardList.addAll(Arrays.asList(clone_level_bean.getSuccess_reward().getValuees()));

        boolean isTeam = data.getMerge().size() > 1;
        for (Map.Entry<Long, Integer> entry : data.getMerge().entrySet()) {
            Player player = mapObject.getPlayers().get(entry.getKey());
            if (player == null) {
                continue;
            }
            Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, data.getLevel(), false);

            int rate = entry.getValue();
            if (isTeam) {
                Manager.countManager.addCount(player, BaseCountType.GroupCopyMap, mapObject.getZoneModelId(), Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.GroupCopymap, 0);
            }
            //加物品
            List<Item> items = Item.createItems(new ReadLongArrayEs(rewardList), rate);

            List<Item> marryActivityItems = Manager.marriageManager.activity().triggerDrop(player.getCareer(), -1, data.getZoneId(), rate);
            items.addAll(marryActivityItems);

            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                        MessageString.System, MessageString.BAGISSPACETOMAIL,
                        MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId());
            }
            for (int i = 0; i < rate; i++) {
                Manager.activityManager.cloneDropTrigger(player, mapObject.getZoneModelId());
            }
            //结算面板消息
            CopyMapMessage.ResManyCopyResult.Builder builder = CopyMapMessage.ResManyCopyResult.newBuilder();
            builder.setStarNum(starNum);
            for (Item item : items) {
                CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                itemInfo.setItemId(item.getItemModelId());
                itemInfo.setNum(item.getNum());
                if (item instanceof ExpValueItem) {
                    itemInfo.setNum(((ExpValueItem) item).getExpNum());
                }
                itemInfo.setBind(item.isBind());
                builder.addRewardlist(itemInfo);
            }
            MessageUtils.send_to_player(player, CopyMapMessage.ResManyCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }


    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 根据挑战时间判定星级
     *
     * @param endTime 开始时间
     * @return 通关星级
     */
    private int calcStarNum(int endTime) {
        ReadIntegerArray level = Global.XinmoCloneNum;
        if (level == null || level.size() < 2) {
            logger.error("global配置表配置错误：" + Global.XinmoCloneNum);
            return 0;
        }
        int now = (int) (TimeUtils.Time() / 1000);
        int remainTime = endTime - now;
        if (remainTime > level.get(1)) {
            return 3;
        } else if (remainTime > level.get(0)) {
            return 2;
        } else {
            return 1;
        }
    }

}
