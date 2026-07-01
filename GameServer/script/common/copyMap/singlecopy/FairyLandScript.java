package common.copyMap.singlecopy;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.*;
import com.game.backpack.structs.ExpValueItem;
import com.game.backpack.structs.Item;
import com.game.copymap.structs.CopyMapType;
import com.game.copymap.structs.FailyLandCopyData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
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
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 天禁之门
 */
public class FairyLandScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(FairyLandScript.class);

    @Override
    public int getId() {
        return ScriptEnum.FairyLandActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        Player player = (Player) objects[0];
        String method = (String) objects[1];

        int copyId;
        switch (method) {
            case "openPanel":
                copyId = (int) objects[2];
                openPanel(player, copyId);
                break;
            case "sweepCopy":
                Cfg_Clone_map_Bean bean = (Cfg_Clone_map_Bean) objects[2];
                sweepCopy(player, bean.getId());
                break;
            default:
                logger.error("错误的调用脚本方法：" + method);
        }
        return null;
    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

        FailyLandCopyData zone = new FailyLandCopyData();

        zone.setZoneId(mapObject.zone.getZoneId());
        zone.setLevel(mapObject.zone.getLevel());
        mapObject.setZone(zone);

        Player player = (Player) objects[0];

        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(zone.getZoneId() * 100 + zone.getLevel());

        Cfg_Characters_Bean charactersBean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        int rate = charactersBean.getSky_door_parm();

        for (int cloneMonsterId : clone_level_bean.getClonemonster_id().getValue()) {

            Cfg_Clone_monster_Bean monsterBean = CfgManager.getCfg_Clone_monster_Container().getValueByKey(cloneMonsterId);

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
        int challengeTime = Global.Skydoor_Finish_Time.get(Global.Skydoor_Finish_Time.size() - 1) * 1000;

        mapObject.addMapOnceScriptEventTimer(getId(), "timeoutEnd", challengeTime, player);
        zone.setEndTime(TimeUtils.Time() + challengeTime);

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.FairyLand.getValue());
        if (dailyBean == null) {
            logger.error("Cfg_Clone_map_Bean或Cfg_Daily_Bean配置表不存在：" + model);
            return false;
        }
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyBean.getId());
        return remainCount != 0;
    }

    @Override
    public void onEnterMap(Player player, MapObject mapObject, boolean login) {

        FailyLandCopyData data = mapObject.getZone();

        CopyMapMessage.ResEnterFairyCopy.Builder builder = CopyMapMessage.ResEnterFairyCopy.newBuilder();
        builder.setEndTime((int) ((data.getEndTime() - TimeUtils.Time()) / 1000));
        builder.setLevel(data.getLevel());
        MessageUtils.send_to_player(player, CopyMapMessage.ResEnterFairyCopy.MsgID.eMsgID_VALUE, builder.build().toByteArray());

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
        if (mapObject.getMonsters().isEmpty()) {
            if (attacker instanceof Player) {
                finish(mapObject, (Player) attacker, true, 1);
            }
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject mapObject, Fighter attacker, Player player) {
        finish(mapObject, player, false, 2);
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        if ("timeoutEnd".equals(method)) {
            timeoutEnd(mapObject, params);
        }
    }

    private void timeoutEnd(MapObject mapObject, Object[] params) {
        Player player = (Player) params[0];
        if (!mapObject.getPlayers().containsKey(player.getId())) {
            return;
        }
        finish(mapObject, player, false, 3);
    }

    /**
     * 副本结算
     *
     * @param type 退出副本类型
     */
    private void finish(MapObject mapObject, Player player, boolean win, int type) {

        if (mapObject.isStop()) {
            return;
        }
        mapObject.setStop(true);

        FailyLandCopyData zone = mapObject.getZone();


        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, zone.getLevel(), win);
        int rewardIndex = 0;
        List<Item> items = new ArrayList<>();
        if (win) {
            rewardIndex = getRewardIndex(mapObject);
            items = sendChallengeReward(player, zone.getZoneId(), zone.getLevel(), rewardIndex);
        }

        //返回结算面板消息
        CopyMapMessage.ResFairyCopyResult.Builder builder = CopyMapMessage.ResFairyCopyResult.newBuilder();
        builder.setScore(rewardIndex);
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
        MessageUtils.send_to_player(player, CopyMapMessage.ResFairyCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 请求面板信息
     */
    private void openPanel(Player player, int cloneId) {
        Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(cloneId);
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.FairyLand.getValue());
        if (bean == null || dailyBean == null) {
            logger.error("Cfg_Clone_map_Bean：" + cloneId + ",或Cfg_Daily_Bean:" + DailyActiveDefine.FairyLand + "不存在");
            return;
        }

        int dailyRemainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyBean.getId());
        int dailyMaxCount = Manager.dailyActiveManager.deal().getDailyMaxCount(player, dailyBean.getId());
        int dailyCanBuyCount = Manager.dailyActiveManager.deal().getDailyCanBuyCount(player, dailyBean.getId());
        int buyCount = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyBean.getId(), 0);

        int mergeCount = (int) Manager.countManager.getCount(player, BaseCountType.COPY_Merge_Count, bean.getId());

        CopyMapMessage.ResOpenFairyCopyPanel.Builder builder = CopyMapMessage.ResOpenFairyCopyPanel.newBuilder();
        builder.setRemainCount(dailyRemainCount);
        builder.setMaxCount(dailyMaxCount);
        builder.setCanBuyCount(dailyCanBuyCount);
        builder.setBuyCount(buyCount);
        builder.setMergeCount(mergeCount);
        MessageUtils.send_to_player(player, CopyMapMessage.ResOpenFairyCopyPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 扫荡副本
     */
    private void sweepCopy(Player player, int copyId) {
        Cfg_Daily_Bean dailyBean = CfgManager.getCfg_Daily_Container().getValueByKey(DailyActiveDefine.FairyLand.getValue());
        Cfg_Clone_map_Bean cloneMapBean = Cfg_Clone_map_Container.GetInstance().getValueByKey(copyId);
        if (cloneMapBean.getType() != CopyMapType.Heave_CopyMap) {
            logger.error("Cfg_Clone_map_Bean配置表配置类型出错：" + copyId);
            return;
        }
        int remainCount = Manager.dailyActiveManager.deal().getDailyRemainCount(player, dailyBean.getId());
        if (remainCount == 0) {
            logger.error("扫荡次数不足");
            return;
        }
        boolean free = Manager.controlManager.deal().checkFuncProgress(player, cloneMapBean.getSweep_free());
        if (!free) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, cloneMapBean.getSweep().get(0), cloneMapBean.getSweep().get(1),
                    ItemChangeReason.SweepCloneUseItemDec, cloneMapBean.getId())) {
                logger.info("扫荡道具不足");
                return;
            }
        }
        sendChallengeReward(player, copyId, 1, 1);
        Manager.copyMapManager.logic().biInstance(player, copyId, 2, 1, 0, true);

        //记录永久通关次数
        Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GateOfHeaven, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.GateOfHeaven, 1);

        openPanel(player, copyId);
    }

    /**
     * 挑战成功或完成扫荡发送奖励
     *
     * @return 获得的物品奖励
     */
    private List<Item> sendChallengeReward(Player player, int copyId, int level, int rewardIndex) {

        int mergeCount = Manager.copyMapManager.logic().getMergeCount(player, copyId);
        player.getCloneSetting().remove(copyId);

        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.FairyLand, mergeCount);
        Manager.countManager.addVariant(player, VariantType.Daily_Enter_TJZM_Times, mergeCount);
        Manager.retrieveResManager.getScript().count(player, RetrieveType.FairyLand, mergeCount);

        //记录永久通关次数
        Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GateOfHeaven, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.GateOfHeaven, mergeCount);


        Cfg_Clone_level_Bean clone_level_bean = CfgManager.getCfg_Clone_level_Container().getValueByKey(copyId * 100 + level);

        //获取星数奖励
        ReadLongArrayEs rewardMap = clone_level_bean.getExtra_reward();

        List<ReadArray<Long>> rewardList = new ArrayList<>();
        for (int i = 0; i < rewardMap.size(); i++) {
            ReadArray<Long> array = rewardMap.get(i);
            if (array.get(2) == rewardIndex) {
                rewardList.add(new ReadLongArray(new Long[]{array.get(0), array.get(1)}));
            }
        }
        //成功奖励
        rewardList.addAll(Arrays.asList(clone_level_bean.getSuccess_reward().getValuees()));

        //发送奖励
        List<Item> items = Item.createItems(new ReadLongArrayEs(rewardList), mergeCount);

        List<Item> marryActivityItems = Manager.marriageManager.activity().triggerDrop(player.getCareer(), -1, copyId, mergeCount);
        items.addAll(marryActivityItems);

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, copyId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet, copyId);
        }
        for (int i = 0; i < mergeCount; i++) {
            Manager.activityManager.cloneDropTrigger(player, copyId);
        }
        //返回展示列表
        return Item.createItems(new ReadLongArrayEs(rewardList), mergeCount);
    }

    private int getRewardIndex(MapObject mapObject) {

        FailyLandCopyData data = mapObject.getZone();

        long statTime = data.getEndTime() - Global.Skydoor_Finish_Time.get(Global.Skydoor_Finish_Time.size() - 1) * 1000;
        int useTime = (int) ((TimeUtils.Time() - statTime) / 1000);

        for (int i = 0; i < Global.Skydoor_Finish_Time.size(); i++) {
            if (useTime <= Global.Skydoor_Finish_Time.get(i)) {
                return i + 1;
            }
        }
        return 0;
    }

}
