package common.copyMap.singlecopy;

import com.data.*;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Clone_daneng_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.container.Cfg_Characters_Container;
import com.data.container.Cfg_Clone_map_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.copymap.structs.CopyMapType;
import com.game.copymap.structs.StarCopyMapData;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IMapBaseScript;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapParam;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class StarCopyScript implements IMapBaseScript {

    private static final Logger logger = LogManager.getLogger(StarCopyScript.class);

    @Override
    public int getId() {
        return ScriptEnum.StarCopyActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        try {
            Player player = (Player) objects[0];
            String method = (String) objects[1];
            switch (method) {
                case "sweepCopy":
                    Cfg_Clone_map_Bean bean = (Cfg_Clone_map_Bean) objects[2];
                    sweepCopy(player, bean);
                    break;
                case "killMonster":
                    killMonster(player, (long)objects[2]);
                    break;
                default:
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
        return null;
    }

    private void killMonster(Player player, long monsterId) {
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map == null) {
            return;
        }
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(map.getZoneModelId());
        if (bean.getType() != CopyMapType.PowerHourse_CopyMap) {
            return;
        }
        StarCopyMapData data = MapParam.getStarCopyMapData(map);
        if (data.getStage() != 1) {
            return;
        }
        Monster monster = map.getMonsters().get(monsterId);
        if (monster == null) {
            logger.info(map.getMonsters().size());
            logger.error("不存在指定id的怪物：" + monsterId);
            return;
        }
        monster.setDie();
        monster.doDie(player);
        MapUtils.sendDead(player, monster);
    }


    @Override
    public void onCreate(MapObject mapObject, Object... objects) {

    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return Manager.copyMapManager.logic().checkEnterTimes(player, model);
    }

    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        StarCopyMapData data = MapParam.getStarCopyMapData(map);
        int firstStageTime = Global.Daneng_clone_frist_time.get(Global.Daneng_clone_frist_time.size() - 1);
        if (data.getStage() == 0) {
            data.setStage(1);
            data.setDiffLevel(getCopyDiffLevelBean(player));
            data.setStartTime((int) (TimeUtils.Time() / 1000 + 3));
            data.setEndTime(data.getStartTime() + firstStageTime);
            decCopyCount(player);

            map.addMapOnceScriptEventTimer(getId(), "changeStage", firstStageTime * 1000 + 3000, player);

        }
        if (data.getStage() == 1) {
            Manager.buffManager.deal().onAddBuff(player, player, Global.Daneng_clone_frist_Buff);
        }
        sendStarCopyInfo(player, data, map);
    }

    private void decCopyCount(Player player) {
        Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.StarCopy, 1);
        Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.DaNengYiFuNum, Count.RefreshType.CountType_Forever, 1);
        Manager.controlManager.operate(player, FunctionVariable.DaNengYiFuNum, 1);
        Manager.retrieveResManager.getScript().count(player, RetrieveType.StarCopy);
    }

    private void sendStarCopyInfo(Player player, StarCopyMapData data, MapObject mapObject) {
        int now = (int) (TimeUtils.Time() / 1000);
        CopyMapMessage.ResStartCopyInfo.Builder builder = CopyMapMessage.ResStartCopyInfo.newBuilder();
        builder.setStage(data.getStage());
        builder.setStartTime(Math.max(data.getStartTime() - now, 0));
        builder.setRemainTime(Math.max(data.getEndTime() - now, 0));
        builder.setMonsterNum(mapObject.getMonsters().size());
        if (data.getStage() == 2) {
            builder.setStarNum(data.getStar());
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResStartCopyInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        removeDaNengBuff(player);
    }

    private void removeDaNengBuff(Player player) {
        Manager.buffManager.deal().onRemoveBuff(player, Global.Daneng_clone_frist_Buff);
        logger.info("移除buff");
    }

    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        if (!(attacker instanceof Player)) {
            return;
        }
        Player player = (Player) attacker;
        StarCopyMapData data = MapParam.getStarCopyMapData(map);
        if (data.getStage() == 2 && map.getMonsters().isEmpty()) {
            finish(map, player, true, 1);
            return;
        }
        if (data.getStage() == 1) {
            if (map.getMonsters().isEmpty()) {
                changeStage(map, player);
                return;
            }
            data.setKillMonsterNum(data.getKillMonsterNum() + 1);

            CopyMapMessage.ResSyncMonsterNum.Builder builder = CopyMapMessage.ResSyncMonsterNum.newBuilder();
            builder.setMonsterNum(map.getMonsters().size());
            MessageUtils.send_to_player(player, CopyMapMessage.ResSyncMonsterNum.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {

    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "timeoutEnd":
                timeoutEnd(mapObject, params);
                break;
            case "changeStage":
                changeStage(mapObject, (Player) params[0]);
                break;
            case "refreshMonster":
                refreshMonster(mapObject, (Player) params[0]);
                break;
            default:
                break;
        }
    }

    /**
     * 第二阶段刷新boss
     */
    private void refreshMonster(MapObject mapObject, Player player) {
        StarCopyMapData data = MapParam.getStarCopyMapData(mapObject);
        Cfg_Clone_daneng_Bean bean = CfgManager.getCfg_Clone_daneng_Container().getValueByKey(data.getDiffLevel());
        if (bean == null) {
            logger.error("未找到大能遗府boss的战力配置：" + player.getLevel());
            return;
        }
        List<Monster> monsters = Manager.copyMapManager.manager().copyMapRefreshMonster(mapObject, mapObject.getZoneModelId(), 1);
        for (Monster monster : monsters) {
            monster.setScore(bean.getMonster_fight_num());
        }
    }

    /**
     * 获取玩家等级对应的难度和奖励配置
     */
    private int getCopyDiffLevelBean(Player player) {
        for (Cfg_Clone_daneng_Bean bean : CfgManager.getCfg_Clone_daneng_Container().getValuees()) {
            if (player.getLevel() >= bean.getMin_lv() && player.getLevel() <= bean.getMax_lv()) {
                return bean.getId();
            }
        }
        return 0;
    }

    /**
     * 切换阶段
     */
    private void changeStage(MapObject map, Player player) {
        StarCopyMapData data = MapParam.getStarCopyMapData(map);
        if (data.getStage() == 1 && map.getMonsters().size() > 0) {
            finish(map, player, false, 3);
            return;
        }
        if (data.getStage() == 2) {
            return;
        }
        removeDaNengBuff(player);
        Cfg_Clone_map_Bean bean = Cfg_Clone_map_Container.GetInstance().getValueByKey(map.getZoneModelId());
        int firstStageTime = Global.Daneng_clone_frist_time.get(Global.Daneng_clone_frist_time.size() - 1);
        int secondStageTime = bean.getExist_time() / 1000 - firstStageTime;
        data.setStage(2);
        data.setStar(calcStarNum(data.getStartTime()));
        data.setStartTime((int) (TimeUtils.Time() / 1000 + 3));
        data.setEndTime(data.getStartTime() + secondStageTime);
        sendStarCopyInfo(player, data, map);

        map.addMapOnceScriptEventTimer(getId(), "refreshMonster", 3000, player);
        map.addMapOnceScriptEventTimer(getId(), "timeoutEnd", secondStageTime * 1000 + 3000, player);
    }


    private void timeoutEnd(MapObject mapObject, Object[] params) {
        //超时，挑战失败处理
        Player player = (Player) params[0];
        if (!mapObject.getPlayers().containsKey(player.getId())) {
            return;
        }
        finish(mapObject, player, false, 3);
    }

    @Override
    public void removeMap(MapObject map) {

    }

    /**
     * 扫荡
     */
    private void sweepCopy(Player player, Cfg_Clone_map_Bean bean) {
        if (bean.getType() != CopyMapType.PowerHourse_CopyMap) {
            return;
        }
        if (!canEnterMap(player, bean.getId(), 0)) {
            return;
        }
        //是否能免费扫荡
        boolean canFreeSweep = Manager.controlManager.deal().checkFuncProgress(player, bean.getSweep_free());
        if (!canFreeSweep) {
            Cfg_Characters_Bean charactersBean = Cfg_Characters_Container.GetInstance().getValueByKey(player.getLevel());
            if (player.getFightPoint() < charactersBean.getStarCopyNeedFightPower()) {
                return;
            }
            //扣物品
            if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getSweep().get(0), bean.getSweep().get(1), ItemChangeReason.SweepCloneUseItemDec, bean.getId())) {
                return;
            }
        }

        //发送奖励
        reward(player, bean.getId(), getCopyDiffLevelBean(player), 5);

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.COPY_SWEEP_SUCCESS);
        Manager.copyMapManager.manager().writeEnterLog(bean.getId(), player, true);
        Manager.copyMapManager.logic().biInstance(player, bean.getId(), 2, 1, 0, false);
    }

    /**
     * 副本结算
     *
     * @param isWin 挑战成功或失败
     */
    private void finish(MapObject mapObject, Player player, boolean isWin, int type) {
        //防止重复结算
        if (mapObject.isStop()) {
            return;
        }
        mapObject.setStop(true);

        Manager.copyMapManager.logic().biInstance(player, mapObject.getZoneModelId(), 1, type, 0, false);

        StarCopyMapData data = MapParam.getStarCopyMapData(mapObject);
        int starNum = data.getStar();

        //返回消息
        CopyMapMessage.ResStartCopyResult.Builder builder = CopyMapMessage.ResStartCopyResult.newBuilder();
        builder.setStarNum(isWin ? starNum : 0);

        if (builder.getStarNum() != 0) {
            List<Item> items = reward(player, mapObject.getZoneModelId(), data.getDiffLevel(), starNum);
            if (items != null) {
                for (Item item : items) {
                    CopyMapMessage.cardItemInfo.Builder itemInfo = CopyMapMessage.cardItemInfo.newBuilder();
                    itemInfo.setItemId(item.getItemModelId());
                    itemInfo.setNum(item.getNum());
                    itemInfo.setBind(item.isBind());
                    builder.addRewardlist(itemInfo);
                }
            }
        }
        MessageUtils.send_to_player(player, CopyMapMessage.ResStartCopyResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取通关大能遗府奖励，包含发奖和通关逻辑
     *
     * @param starNum   通关星数
     * @return          奖励列表
     */
    private List<Item> reward(Player player, int model, int diffLevel, int starNum) {
        if (starNum <= 0) {
            return null;
        }

        //发送奖励
        Cfg_Clone_daneng_Bean bean = CfgManager.getCfg_Clone_daneng_Container().getValueByKey(diffLevel);
        if (bean == null) {
            logger.error("Cfg_Clone_daneng_Bean配置表不存在：" + diffLevel);
            return null;
        }
        ReadIntegerArrayEs rewardMap = bean.getExtra_reward();
        List<ReadArray<Integer>> reward = new ArrayList<>();
        for (int i = 0; i < rewardMap.size(); i ++) {
            if (rewardMap.get(i).get(2) == starNum) {
                reward.add(rewardMap.get(i));
            }
        }
        if (reward.isEmpty()) {
            logger.error("Cfg_Clone_mapBean配置表奖励异常： " + model + ",星数奖励不存在：" + starNum);
            return null;
        }
        List<Item> items = Item.createItems(Utils.toReadIntegerArrayEsByArray(reward));
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CopyMapGet, model)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail,
                    MessageString.System, MessageString.BAGISSPACETOMAIL,
                    MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet, model);
        }
        return items;
    }

    /**
     * 根据挑战时间判定星级
     *
     * @param startTime 结束时间
     * @return  通关星级
     */
    private int calcStarNum(int startTime) {
        ReadArray<Integer> level = Global.Daneng_clone_frist_time;
        int now = (int) (TimeUtils.Time() / 1000);
        int useTime = now - startTime;
        for (int i = 0; i < level.size(); i++) {
            if (useTime <= level.get(i)) {
                return level.size() - i;
            }
        }
        return 0;
    }
}
