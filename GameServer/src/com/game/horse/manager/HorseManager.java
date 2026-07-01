/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.horse.manager;

/**
 * @author 兰香<lanxiang@haowan123.com>
 */

import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.horse.script.IHorseScript;
import com.game.horse.structs.Horse;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.task.structs.TaskHelp;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class HorseManager {

    private static final Logger log = LogManager.getLogger(HorseManager.class);

    //当前多人坐骑的搭载情况，key 为司机，value 为 乘客，乘客中不包含司机
    public ConcurrentHashMap<Long, List<Long>> multiPlayerHashMap = new ConcurrentHashMap<>();

    //请求通知坐骑系统开放了，给予首只坐骑  妈的又要改回完成任务开放[2016.6.24]
//    public void reqHorseOpen(Player player) {
//        if(player.getLevel() < Global.HorseOpenLevel){ //坐骑开放全由客户端判断,nn的又说是由特定任务完成发放。。。
//            return;
//        }
//        if(player.getHorse().getHighestLayer() > 0){
//            //已经有了
//            return;
//        }
//        //还没的坐骑，给坐骑
//	giveFirstHorse(player);
//    }

//    //请求打开坐骑主面板
//    public void reqOpenHorsePanel(Player player) {
//        Horse horse = player.getHorse();
//        ResOpenHorsePanel.Builder resMsg = ResOpenHorsePanel.newBuilder();
////        resMsg.setHorseInfo(buildHorseInfo(horse));
//
//        List<UpItemInfo.Builder> nextItemInfoList;
//        if (horse.getCurStarLv() >= getHorseMaxStarLv()) { //幻化等级已满，则应返进阶所需材料信息，否则返幻化的
//            nextItemInfoList = manager.horseManager.getUpItemInfo(player, HORSE_UP, 0);
//        } else {
//            nextItemInfoList = manager.horseManager.getUpItemInfo(player, HORSE_STAR_UP, 0);
//        }
//        if (nextItemInfoList != null && !nextItemInfoList.isEmpty()) {
//            for (UpItemInfo.Builder itemInfo : nextItemInfoList) {
//                resMsg.addItemInfoList(itemInfo);
//            }
//        }
//
//        MessageUtils.send_to_player(player, ResOpenHorsePanel.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//    }

    //请求坐骑进阶
//    public void reqHorseUpDeal(Player player, boolean isAutoConsume) {
//        MapObject<String, Object> map = new HashMap<>();
//        map.put("operate", "horseUp");
//        map.put("player", player);
//        map.put("isAutoConsume", isAutoConsume);
//        
//        ScriptManager.getInstance().call(ScriptEnum.HorseUpIllusionScript, map);
//    }
    //请求坐骑幻化
//    public void reqHorseIllusionDeal(Player player, boolean isAutoConsume) {
//        MapObject<String, Object> map = new HashMap<>();
//        map.put("operate", "horseIllusion");
//        map.put("player", player);
//        map.put("isAutoConsume", isAutoConsume);
//        ScriptManager.getInstance().call(ScriptEnum.HorseUpIllusionScript, map);
//    }
    //打开技能升阶面板，返给客户端技能升阶所需材料数据信息(确认到所有技能升阶所消耗的材料和数量是一样的，只需从表中读取一个返回即可)
    //后又改了技能升阶消耗材料和数量是变化的，打开面板时表示默认选中第一个技能，即打开面板时需要读取第一个技能所对应消耗的材料和数量
//    public void reqOpenSkillUpPanel(Player player, int skillId) {
//        ResOpenHorseSkillUpPanel.Builder resMsg = ResOpenHorseSkillUpPanel.newBuilder();
//        List<UpItemInfo.Builder> itemInfoList = manager.horseManager.getUpItemInfo(player, HORSE_SKILL_UP, skillId);
//        if (itemInfoList != null && !itemInfoList.isEmpty()) {
//            for (UpItemInfo.Builder itemInfo : itemInfoList) {
//                resMsg.addItemInfoList(itemInfo);
//            }
//        }
//        MessageUtils.send_to_player(player, ResOpenHorseSkillUpPanel.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//    }

    //请求坐骑技能升阶
//    public void reqHorseSkillUpDeal(Player player, int upSkillId, boolean isAutoConsume) {
//        MapObject<String, Object> map = new HashMap<>();
//        map.put("operate", "horseSkillUp");
//        map.put("player", player);
//        map.put("upSkillId", upSkillId);
//        map.put("isAutoConsume", isAutoConsume);
//        ScriptManager.getInstance().call(ScriptEnum.HorseUpIllusionScript, map);
//    }
    //查看其它玩家坐骑信息(目前为在线的其他玩家可查)
//    public void reqQueryOtherPlayerHorse(Player player, long otherPlayerId) {
//        Player otherPlayer = manager.playerManager.getOnLinePlayer(otherPlayerId);
//        if (otherPlayer == null) {
//            LOGGER.error("otherPlayer == null");
//            return;
//        }
//
//        Horse horse = otherPlayer.getHorse();
//        ResOtherPlayerHorse.Builder resMsg = ResOtherPlayerHorse.newBuilder();
//        resMsg.setOtherPlayerId(otherPlayerId);
//        resMsg.setHorseInfo(buildHorseInfo(horse));
//        MessageUtils.send_to_player(player, ResOtherPlayerHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//    }

//    //请求换成坐骑
//    //public void reqChangeHorse(Player player, int horseLayer) {
//    public void reqChangeHorse(Player player, int rideId, int rideType) {
//        Horse horse = player.getHorse();
//        if (horse.getCurLayer() == rideId && horse.getRideType() == rideType) {
//            return;
//        }
//
//        if (rideType == 0) {
//            ResChangeHorse.Builder resMsg = ResChangeHorse.newBuilder();
//            if (!manager.gameDataManager.Cfg_Horse_upContainer.getMap().containsKey(rideId)) {
//                LOGGER.error("请求换乘坐骑的rideId错误！rideId=" + rideId);
//                resMsg.setIsChangeSuccess(false);
//                MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//                return;
//            }
//            horse.setRideType(rideType);
//            horse.setCurLayer(rideId);
//            resMsg.setIsChangeSuccess(true);
//            resMsg.setAfterHorseLayer(rideId);
//            MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//        } else if (rideType == 1) {
////            ResFightMythical.Builder resMsg = ResFightMythical.newBuilder();
////            if (!manager.gameDataManager.Cfg_MythicalContainer.getMap().containsKey(rideId)) {
////                LOGGER.error("请求换乘神兽的rideId错误！rideId=" + rideId);
////                resMsg.setIsSuccess(false);
////                MessageUtils.send_to_player(player, ResFightMythical.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
////                return;
////            }
////            horse.setRideType(rideType);
////            horse.setCurLayer(rideId);
////            resMsg.setIsSuccess(true);
////            resMsg.setFightMythicalId(rideId);
////            MessageUtils.send_to_player(player, ResFightMythical.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//        }
//
//        if (horse.getRideState() == RIDE) { //骑乘状态换乘坐骑后要广播骑乘新坐骑消息
//            ResChangeRideState.Builder rMsg = ResChangeRideState.newBuilder();
//            rMsg.setPlayerId(player.getId());
////            rMsg.setRideType(horse.getRideType());
//            rMsg.setRideId(horse.getCurLayer());
////            rMsg.setRideState(RIDE);
//            MessageUtils.send_to_roundPlayer(player, ResChangeRideState.MsgID.eMsgID_VALUE, rMsg.build().toByteArray()); //上下马后广播骑乘状态            
//        }
//
//        manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater);
//    }

    //请求改变坐骑骑乘状态
//    public void reqChangeRideState(Player player, int rideState) {
//        //2015年12月21日 16:26:02 副本不能上坐骑
//        com.game.map.structs.MapObject curmap = manager.mapManager.getMap(player);
//        if (curmap.getZoneId() > 0 && rideState == RIDE) {
//            //MessageUtils.notify_player(player, Notify.CHAT, StringData.CopyMapCanNotRide);
//            return;
//        }
//
//        if (rideState != UNRIDE && rideState != RIDE) {
//            LOGGER.error("错误的rideState=" + rideState);
//            return;
//        }
//
//        Horse horse = player.getHorse();
//        if (horse == null || horse.getCurLayer() < 1) {
//            manager.horseManager.giveFirstHorse(player, RandomUtils.randomValue(10, 1));//随机一个坐骑出来
//            horse = player.getHorse();
//        }
//        //2016年3月26日 12:02:40 如果状态一样在请求，服务器主动广播一次
//        if (horse.getRideState() == rideState) {
//            ResChangeRideState.Builder resMsg = ResChangeRideState.newBuilder();
//            resMsg.setPlayerId(player.getId());
//            resMsg.setRideType(horse.getRideType());
//            resMsg.setRideId(horse.getCurLayer());
//            resMsg.setRideState(rideState);
//            MessageUtils.send_to_roundPlayer(player, ResChangeRideState.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()); //上下马后广播骑乘状态
//            return;
//        }
//        //上马有条件
//        if (rideState == RIDE && EntityState.Battle.compare(player.getState())) {
//            MessageUtils.notify_player(player, Notify.CHAT, StringData.BattleRideFobid); //脱离战斗状态才能骑乘坐骑
//            return;
//        }
//        LOGGER.info(player + " 坐骑的" + horse.getCurLayer() + " 当前UP状态是：" + rideState);
//        horse.setRideState(rideState);
//
//        ResChangeRideState.Builder resMsg = ResChangeRideState.newBuilder();
//        resMsg.setPlayerId(player.getId());
//        resMsg.setRideType(horse.getRideType());
//        resMsg.setRideId(horse.getCurLayer());
//        resMsg.setRideState(rideState);
//        MessageUtils.send_to_roundPlayer(player, ResChangeRideState.MsgID.eMsgID_VALUE, resMsg.build().toByteArray()); //上下马后广播骑乘状态
//
//        manager.playerAttAttributeManager.calPlayerAttributs(player, PlayerAttributeType.HORSE); //上下马后重新计算坐骑加成属性
//        manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
//    }

//如下其他内部方法
    //自动上马(自动寻路时自动上马)(由客户端请求)
//    public void autoRide(Player player) { 
//        reqChangeRideState(player, RIDE);
//    }

    public ConcurrentHashMap<Long, List<Long>> getMultiPlayerHashMap() {
        return multiPlayerHashMap;
    }

    public void setMultiPlayerHashMap(ConcurrentHashMap<Long, List<Long>> multiPlayerHashMap) {
        this.multiPlayerHashMap = multiPlayerHashMap;
    }

    public void addOneToMultiPlayerHorse(long master, long invited) {
        if (multiPlayerHashMap.containsKey(master)) {
            if (!multiPlayerHashMap.get(master).contains(invited)) {
                multiPlayerHashMap.get(master).add(invited);
            }
        } else {
            List<Long> list = new ArrayList<>();
            list.add(invited);
            multiPlayerHashMap.put(master, list);
        }
    }

    //自动下马(进入战斗时则自动下马) (也由客户端请求了[释放技能时则请求])
    public void autoUnRide(Player player) {
        Horse horse = player.getHorse();
        if (horse == null) {
            log.error(TaskHelp.getPlayerInfo(player) + "的 horse 为空");
            return;
        }
        //如果是飞着的，那么先落地，在下马
        if (horse.getRideState() == HorseRideStateEnum.Fly) {
            //去掉飞行行为
            if (BehaviorManager.HasBehavior(player, BehaviorType.Fly)) {
                BehaviorManager.CancelBehaviorByType(player, BehaviorType.Fly);
            }
            player.getHorse().setRideState(HorseRideStateEnum.Ride);
            // deal().onReqFlyActionHandler(player, false, player.gainCurPos().getX(), player.gainCurPos().getY());
        }
        deal().onReqChangeRideState(player, 0);
    }

    public void online(Player player) {
        deal().online(player);
    }

//    //获取技能升级所需材料信息
//    public UpItemInfo.Builder getSkillUpItemInfo(Player player, int upSkillId) { //upSkillId为玩家主动选择要升级的技能Id
//	Cfg_Horse_skillBean skillBean = manager.gameDataManager.Cfg_Horse_skillContainer.GetValueByKey(upSkillId);
//	if (skillBean == null) {
//	    LOGGER.error("获取技能数据出错！skillId=" + upSkillId);
//	    return null;
//	}
//	
//	int upItemId = skillBean.getUp_item_id();
//	int upItemNum = skillBean.getUp_item_num();
//	int ownedNum = manager.backpackManager.manager().getItemNum(player, upItemId);
//	
//	UpItemInfo.Builder itemInfo = UpItemInfo.newBuilder();
//	itemInfo.setItemModelId(upItemId);
//	itemInfo.setItemNum(upItemNum);
//	itemInfo.setOwnedNum(ownedNum);
//	
//	return itemInfo;
//    }

    //获取目前开放的最高技能上限[修改为各技能可有不同的最高等级，增加至horse_skill表中，@2015-12-29]
//    public int getHorseSkillLevelMax() {        
//        return Global.GetIntValue(Global.HorseSkillLevelMax);
//    public int getHorseSkillLevelMax(int skillId) {
//        Cfg_Horse_skillBean skillBean = manager.gameDataManager.Cfg_Horse_skillContainer.getMap().get(skillId);
//        if (skillBean == null) {
//            LOGGER.error("获取技能数据出错！skillId=" + skillId);
//            return -1;
//        }
//        return skillBean.getMax_level();
//    }

    //用枚举来实现单例
    private enum Singleton {

        INSTANCE;
        HorseManager manager;

        Singleton() {
            this.manager = new HorseManager();
        }

        HorseManager getProcessor() {
            return manager;
        }
    }

    //获取HorseManager的实例对象
    public static HorseManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IHorseScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HorseBaseScript);
        if (is instanceof IHorseScript) {
            return (IHorseScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
