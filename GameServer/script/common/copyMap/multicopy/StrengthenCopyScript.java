package common.copyMap.multicopy;

import com.data.CfgManager;
import com.data.FunctionVariable;
import com.data.Global;
import com.data.MessageString;
import com.data.bean.Cfg_Clone_level_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Clone_monster_Bean;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.*;
import com.game.backpack.structs.ExpValueItem;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.StrengthenCopyData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.data.ItemChangeReason;
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

public class StrengthenCopyScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(StrengthenCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.StrengthenCopyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);

        StrengthenCopyData zone = new StrengthenCopyData();
        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());
        zone.setStage(1);

        mapObject.setZone(zone);

        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapObject.getZoneModelId());

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(zone.getZoneId() * 100 + zone.getLevel());

        int bossNum = 0;
        for (int cloneMonsterId: clone_level_bean.getClonemonster_id().getValue()) {
            zone.getClone_monster_list().add(cloneMonsterId);
            Cfg_Clone_monster_Bean monster = CfgManager.getCfg_Clone_monster_Container().getValueByKey(cloneMonsterId);
            if (monster == null) {
                logger.error("Cfg_Clone_monster_Bean配置表不存在：" + cloneMonsterId);
                return;
            }
            for (ReadArray<Integer> array : monster.getMonster_information().getValuees()) {
                bossNum += array.get(1);
            }
            if (monster.getIf_end() == 1) {
                List<Monster> bossList = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), monster.getMonsterWave());
                for (Monster boss : bossList) {
                    boss.setCamp(0, true);
                }
                break;
            }
        }
        zone.setRemainBossNum(bossNum);
        zone.setStartTime((int) ((TimeUtils.Time() / 1000) + 3));
        zone.setEndTime((int) ((TimeUtils.Time() + bean.getExist_time()) / 1000));

        //3秒后开始进入第一阶段刷新怪物
        mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", 3 * 1000);

        //副本结束计时器
        mapObject.addMapOnceScriptEventTimer(getId(), "copyEnd", bean.getExist_time());

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return Manager.copyMapManager.logic().checkEnterTimes(player, model);
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {

        StrengthenCopyData data = map.getZone();
        //获取倍率
        if (!data.getMerge().containsKey(player.getId())) {
            int mergeCount = Manager.copyMapManager.logic().getMergeCount(player, map.getZoneModelId());
            data.getMerge().put(player.getId(), mergeCount);
            player.getCloneSetting().remove(map.getZoneModelId());

            //扣次数
            Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(map.getZoneModelId());
            if (bean == null) {
                return;
            }
            Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.StrengthenCopy, mergeCount);

            Manager.retrieveResManager.getScript().count(player, RetrieveType.FiveCopy, mergeCount);
            //记录永久通关次数
            Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.Five_lineCopy, Count.RefreshType.CountType_Forever, mergeCount);
            Manager.controlManager.operate(player, FunctionVariable.Five_lineCopy, mergeCount);
        }
        player.setCamp(0, true);

        CopyMapMessage.ResManyCopy.Builder builder = CopyMapMessage.ResManyCopy.newBuilder();
        builder.setStartTime(data.getStartTime() - (int) (TimeUtils.Time() / 1000));
        builder.setEndTime(data.getEndTime() - (int) (TimeUtils.Time() / 1000));
        builder.setMonsterNum(data.getRemainBossNum());
        builder.setStage(data.getStage());
        MessageUtils.send_to_player(player, CopyMapMessage.ResManyCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {

    }

    @Override
    public void onDamage(MapObject mapObject, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject mapObject, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject mapObject, Monster monster, Fighter attacker) {

        StrengthenCopyData data = mapObject.getZone();

        data.setRemainBossNum(data.getRemainBossNum() - 1);

        if (mapObject.getMonsters().size() <= 1) {

            if (data.getClone_monster_list().isEmpty()) {
                finish(mapObject, true, 1);
                return;
            }

            mapObject.addMapOnceScriptEventTimer(getId(), "refreshMonster", 5000);

            data.setStage(data.getStage() + 1);
            //五行副本，第6阶段是boss，打开阻挡
            MapManager.getInstance().setBlockDoor(mapObject, "DynamicBlocker", data.getStage() == 6);

            //同步怪物数量阶段
            CopyMapMessage.ResSyncManyCopy.Builder builder = CopyMapMessage.ResSyncManyCopy.newBuilder();
            builder.setMonsterNum(data.getRemainBossNum());
            builder.setStage(data.getStage());
            MessageUtils.send_to_map(mapObject, CopyMapMessage.ResSyncManyCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
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
            case "refreshMonster":
                refreshMonster(mapObject);
                break;
            case "copyEnd":
                finish(mapObject, false, 3);
                break;
        }
    }

    private void refreshMonster(MapObject mapObject) {

        StrengthenCopyData data = mapObject.getZone();
        if (data.getClone_monster_list().isEmpty()){
            return;
        }
        int cloneMonsterId = data.getClone_monster_list().remove(0);

        Cfg_Clone_monster_Bean monster = CfgManager.getCfg_Clone_monster_Container().getValueByKey(cloneMonsterId);

        //如果是最后一波Boss，把第一波就刷出的boss设置为不同阵营
        if (monster.getIf_end() == 1) {
            for (Monster boss : mapObject.getMonsters().values()) {
                boss.setCamp(1, true);
            }
            return;
        }
        Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), monster.getMonsterWave());
    }

    private void finish(MapObject mapObject, boolean success, int type) {
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setAutoRemove(true);
        mapObject.setStop(true);

        StrengthenCopyData zone = mapObject.getZone();

        int starNum = 0;
        //挑战成功，计算星数
        if (success) {
            starNum = calcStarNum(zone.getEndTime());
        }

        Cfg_Clone_level_Bean bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(zone.getZoneId() * 100 + zone.getLevel());

        //获取星数奖励
        ReadLongArrayEs rewardMap = bean.getExtra_reward();

        List<ReadArray<Long>> rewardList = new ArrayList<>();
        for (int i = 0; i < rewardMap.size(); i++) {
            ReadArray<Long> array = rewardMap.get(i);
            if (array.get(2) == starNum) {
                rewardList.add(new ReadLongArray(new Long[]{array.get(0), array.get(1)}));
            }
        }
        //成功奖励
        rewardList.addAll(Arrays.asList(bean.getSuccess_reward().getValuees()));

        boolean isTeam = zone.getMerge().size() > 1;
        for (Map.Entry<Long, Integer> entry : zone.getMerge().entrySet()) {
            Player player = mapObject.getPlayers().get(entry.getKey());
            if (player == null) {
                continue;
            }
            int rate = entry.getValue();

            for (int i = 0; i< rate; i++){
                Manager.activityManager.cloneDropTrigger(player, mapObject.getZoneModelId());
            }

            if (isTeam) {
                Manager.countManager.addCount(player, BaseCountType.GroupCopyMap, mapObject.getZoneModelId(), Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.GroupCopymap, 0);
            }
            //加物品
            List<Item> items = Item.createItems( new ReadLongArrayEs(rewardList), rate);

            List<Item> marryActivityItems = Manager.marriageManager.activity().triggerDrop(player.getCareer(), -1, zone.getZoneId(), rate);
            items.addAll(marryActivityItems);


            if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                        , MessageString.System, MessageString.BAGISSPACETOMAIL
                        , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet, mapObject.getZoneModelId());
            }

            //返回结算面板消息
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

            Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, zone.getLevel(), true);
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
        ReadIntegerArray level = Global.WuxingCloneNum;
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
