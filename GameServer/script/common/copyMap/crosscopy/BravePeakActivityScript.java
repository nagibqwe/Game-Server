package common.copyMap.crosscopy;

import com.data.CfgManager;
import com.data.Global;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.data.struct.ReadLongArrayEs;
import com.game.backpack.structs.ItemCoinType;
import com.game.bravepeak.struct.BravePeakDefine;
import com.game.map.script.IMapBaseScript;
import com.game.copymap.structs.FightRoomState;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.monster.structs.Monster;
import com.game.pet.structs.Pet;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import game.core.map.Position;
import game.core.util.TimeUtils;
import game.message.BravePeakMessage;
import game.message.CommonMessage;
import game.message.CopyMapMessage;
import game.message.ZoneMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 勇者巅峰（天芒鬼城）活动处理脚本 Created by zcd on 2018/2/5.
 */
public class BravePeakActivityScript implements IMapBaseScript {
    private static final Logger log = LogManager.getLogger(BravePeakActivityScript.class);
    @Override
    public int getId() {
        return ScriptEnum.BravePeakCrossActivityScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onCreate(MapObject mapObject, Object... objects) {
        mapObject.setAutoRemove(false);
        List<CommonMessage.CrossAttribute> createParams = (List<CommonMessage.CrossAttribute>) objects[1];
        for (CommonMessage.CrossAttribute ca : createParams) {
            if (ca.getType() == 10009) {
                DailyActiveManager.bravePeakMapInfo.setActiveEndTime(TimeUtils.Time() + ca.getValue());
                mapObject.addMapOnceScriptEventTimer(getId(), "timeOutClose",ca.getValue());

                mapObject.addMapOnceScriptEventTimer(getId(), "KickPlayerOut", ca.getValue() + 10 * 1000);
            }
        }
    }

    @Override
    public boolean canEnterMap(Player player, int model, int level) {
        return true;
    }

    private void timeOutClose(MapObject mapObject) {
        normalFinish(mapObject);
    }

    /**
     * 正常结束，即时间到了做的一些处理
     *
     * @param mapObject
     */
    private void normalFinish(MapObject mapObject) {
        for (Player player : mapObject.getPlayers().values()) {
            showCompleteReward(player);
        }
    }

    private void showCompleteReward(Player player)
    {
        int modeID = 0;
        List<Integer> modeIDList = new ArrayList<>();
        if (DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().containsKey(player.getId()))
             modeIDList = DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().get(player.getId());
        Collections.sort(modeIDList);
        Collections.reverse(modeIDList);
        if (modeIDList.size()>0)
            modeID = modeIDList.get(0);
        int floor = modeID % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE;
        List<ReadIntegerArrayEs> result = new ArrayList<>();
        List<ReadLongArrayEs> resultExp = new ArrayList<>();
        result.addAll(copySettlement(modeID, true,player,resultExp));
        sendSettlement(player, floor, result,resultExp);
    }

    //进入点轮着选
    private Position roundPosition( MapObject map)
    {
        Position position = DailyActiveManager.bravePeakMapInfo.getPosIndex( map);
         return position;
    }
    @Override
    public void onEnterMap(Player player, MapObject map, boolean login) {
        Position pos = null ;
        if (DailyActiveManager.bravePeakMapInfo.getPlayerNewPos().containsKey(player.getId())){
            pos =   DailyActiveManager.bravePeakMapInfo.getPlayerNewPos().get(player.getId());
           player.changeCurPos(pos,true);
        }else {
            pos =   roundPosition(map);
           player.changeCurPos(pos,true);
        }
        Pet pet = Manager.petManager.getBattlePet(player);
        if (pet != null) {
            pet.changeCurPos(pos,true);
        }
        //设置玩家积分
        DailyActiveManager.bravePeakMapInfo.getPlayerScore().put(player.getId(), 0L);
        //发送通知
        int floor = map.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE;
        int maxScore = Global.BravePeakScoreLimit.get(floor - 1);
        sendRecord(player, floor, DailyActiveManager.bravePeakMapInfo.getPlayerScore().get(player.getId()), maxScore,map);

        sendEndTimeToClient(player,map);
        //BI
        Manager.copyMapManager.logic().biInstance(player, map.getZoneModelId(), 0, 1, floor, false);

        Manager.retrieveResManager.getScript().onSendResourceFindChangeToGame(player, RetrieveType.ArenaYZZD.type());
    }

    private void sendEndTimeToClient(Player player, MapObject map) {
        long endTime = DailyActiveManager.bravePeakMapInfo.getActiveEndTime()- TimeUtils.Time();
        CopyMapMessage.ResCopymapNeedTime.Builder msg = CopyMapMessage.ResCopymapNeedTime.newBuilder();
        msg.setEndTime((int) (endTime / 1000));
        msg.setModelId(map.getZoneModelId());
        msg.setWaitEndToStart(0);
        MessageUtils.send_to_player(player, CopyMapMessage.ResCopymapNeedTime.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
    @Override
    public void onQuitMap(Player player, MapObject map, boolean isQuit) {
        //finishDeal(map, player, !isQuit);
        DailyActiveManager.bravePeakMapInfo.getPlayerScore().remove(player.getId());
        Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, map.getId(), map.getZoneModelId());
    }

    @Override
    public void onDamage(MapObject map, Monster monster, long damage, Fighter attacker) {

    }

    @Override
    public void onMonsterDie(MapObject map, Monster monster, Fighter attacker) {

    }

    @Override
    public void onMonsterAfterDie(MapObject map, Monster monster, Fighter attacker) {
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            addScore(player, map, 1);
        }
    }

    @Override
    public void onLeaveBattle(MapObject map, Monster monster, Player attacker) {

    }

    @Override
    public void onPlayerDie(MapObject map, Fighter attacker, Player player) {
        if (attacker instanceof Player) {
            Player attackerPlayer = (Player) attacker;
            addScore(attackerPlayer, map, 1);
        }
        Position pos =   roundPosition(map);
        player.getCurGps().setPos(pos);
    }

    @Override
    public void action(MapObject mapObject, String method, Object[] params) {
        switch (method) {
            case "playerDieOutBossHome":
                playerDieOutBossHome(params);
                break;
            case "timeOutClose":
                timeOutClose(mapObject);
                break;
            case "waitToNext":
                waitToNext(mapObject, params);
                break;
            case "KickPlayerOut":
                KickPlayerOut(mapObject);
                break;
            default:
                break;
        }
    }

    private void waitToNext(MapObject mapObject, Object... params) {
        Player player = (Player) params[0];
        Manager.mapManager.manager().onQuitMap(mapObject,player,false);
        f2gSendBravePeakReward(player, mapObject, mapObject.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE == 9);
    }

    private void playerDieOutBossHome(Object[] params) {
        Player player = (Player) params[0];
        Manager.mapManager.manager().onCrossOutMap(player);
    }

    @Override
    public void removeMap(MapObject mapObject) {

    }

    /**
     * 给玩家加分
     *
     * @param player
     * @param map
     * @param addScore
     */
    private void addScore(Player player, MapObject map, int addScore) {
        Long score = DailyActiveManager.bravePeakMapInfo.getPlayerScore().get(player.getId());
        if (score == null) {
            score = 0L;
        }
        int floor = map.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE;
        //积分加满之后不在往下走,避免造成两次退出地图
        int maxScore = Global.BravePeakScoreLimit.get(floor - 1);
        if (score >=maxScore)
            return;
        //------
        score += addScore;
        DailyActiveManager.bravePeakMapInfo.getPlayerScore().put(player.getId(), score);
        //发战报更新最新消息
        sendRecord(player, floor, score, maxScore,map);
        if (score >= maxScore) {
//            Manager.mapManager.manager().onCrossOutMap(player);
            //最后一次就直接退出副本
            if (floor == BravePeakDefine.BRAVE_PEAK_MAX_FLOOR) {
                advanceFinish(map, player);
            } else {
                if (addScore > 0) {
                    thisFloorFinishDeal(player, map, true);
                } else {
                    thisFloorFinishDeal(player, map, false);
                }
            }

            Manager.copyMapManager.logic().biInstance(player, map.getZoneModelId(), 1, 1, floor, false);
        }
    }


    /**
     * 本层完了之后的处理
     *
     * @param player
     * @param map
     * @param isNormal 是否正常的结束
     */
    private void thisFloorFinishDeal(Player player, MapObject map, boolean isNormal) {
        if (isNormal) {
            //转移位置
            Position pos =   roundPosition(map);
            DailyActiveManager.bravePeakMapInfo.getPlayerNewPos().put(player.getId(),pos);
            Manager.mapManager.transport().ResCurMapTransport(player, map, pos, 0, 0);
            //加buff
//            Manager.buffManager.deal().onAddBuff(player, player, Global.DailyActivite2SuccessBuffID);
            //给客户端发送协议
            ZoneMessage.ResBravePeakSuccessOneFloorNotice.Builder msg = ZoneMessage.ResBravePeakSuccessOneFloorNotice.newBuilder();
            msg.setCurFloor(map.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE);
            msg.setRemainTime(BravePeakDefine.BRAVE_PEAK_FINISH_WAIT_TIME);
            MessageUtils.send_to_player(player, ZoneMessage.ResBravePeakSuccessOneFloorNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            //一个五秒的tick
            map.addMapOnceScriptEventTimer(getId(), "waitToNext", BravePeakDefine.BRAVE_PEAK_FINISH_WAIT_TIME * 1000, player);
        } else {
            waitToNext(map, player);
        }
    }

    /**
     * 发送副本结算面板信息
     *
     * @param player
     * @param maxFloor
     * @param arrays
     */
    private void sendSettlement(Player player, int maxFloor, List<ReadIntegerArrayEs> arrays, List<ReadLongArrayEs> resultExp) {
        ZoneMessage.ResBravePeakResultPanl.Builder builder = ZoneMessage.ResBravePeakResultPanl.newBuilder();
        builder.setMaxFloor(maxFloor);
        builder.addAllInfo(buildItemInfo(arrays,resultExp));
        MessageUtils.send_to_player(player, ZoneMessage.ResBravePeakResultPanl.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 发战报
     *
     * @param player
     * @param floor
     * @param curNum
     * @param needNum
     */
    private void sendRecord(Player player, int floor, long curNum, int needNum,MapObject mapObject) {
        ZoneMessage.ResBravePeakRecord.Builder builder = ZoneMessage.ResBravePeakRecord.newBuilder();
        builder.setFloor(floor);
        builder.setNowNum((int) curNum);
        builder.setNeedNum(needNum);
        if(!isSendRewardItemToClient(player,mapObject)){
            List<ReadIntegerArrayEs> result = new ArrayList<>();
            result.addAll(copyFloorlement(mapObject.getZoneModelId()));
            if (result != null && result.size() != 0)
                builder.addAllInfo(buildItemInfo(result, null));
        }
        MessageUtils.send_to_player(player, ZoneMessage.ResBravePeakRecord.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private boolean isSendRewardItemToClient(Player player,MapObject mapObject)
    {
        if ( DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().containsKey(player.getId())){
            List<Integer> rewardlist =   DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().get(player.getId());
            if (rewardlist.contains(mapObject.getZoneModelId())) {
                return true;
            }else {
                return false;
            }
        }
        return false;
    }


    /**
     * 统计玩家最后能获得的奖励
     *
     * @param copyMayId
     * @param isSuccess 这层是否通关了
     * @return
     */
    private List<ReadIntegerArrayEs> copySettlement(int copyMayId, boolean isSuccess,Player player,List<ReadLongArrayEs> resultExp) {
        List<ReadIntegerArrayEs> rewards = new ArrayList<>();
        int firstFloorId = BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE + 1;
        long allExp = 0;
        for (int i = firstFloorId; i < copyMayId || (i == copyMayId && isSuccess); i++) {
            Cfg_Clone_map_Bean cfg_clone_mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(i);
            if (cfg_clone_mapBean == null) {
                log.error(" 在勇者巅峰结算的时候，没有找到副本：" + i);
                continue;
            }
            Cfg_Characters_Bean bean = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
            if (bean == null) {
               continue;
            }
           // ItemCoinType.EXP;
            long expNum = bean.getYZZD_EXP_award().get(cfg_clone_mapBean.getMaterialLevel()) == null ?
                    0 : bean.getYZZD_EXP_award().get(cfg_clone_mapBean.getMaterialLevel());
            allExp +=expNum;
            rewards.add(cfg_clone_mapBean.getSuccess_reward());
        }
        if (allExp >0){
            List<List<Long>> base = new ArrayList<>();
            List<Long> ail = new ArrayList<>();
            ail.add((long)ItemCoinType.EXP);
            ail.add(allExp);
            base.add(ail);
            resultExp.add(Utils.toReadLongArrayEsByList(base));
        }
        return rewards;
    }

    private List<ReadIntegerArrayEs> copyFloorlement(int copyMayId) {
        List<ReadIntegerArrayEs> rewards = new ArrayList<>();
         Cfg_Clone_map_Bean cfg_clone_mapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(copyMayId);
         if (cfg_clone_mapBean == null) {
             log.error(" 在勇者巅峰结算的时候，没有找到副本："   + copyMayId);
             return  null;
         }
         rewards.add(cfg_clone_mapBean.getSuccess_reward());
        return rewards;
    }

    /**
     * 封装奖励物品数据
     *
     * @param arrays
     * @return
     */
    private List<ZoneMessage.itemInfo> buildItemInfo(List<ReadIntegerArrayEs> arrays,List<ReadLongArrayEs> resultExp) {
        List<ZoneMessage.itemInfo> result = new ArrayList<>();
        for (ReadIntegerArrayEs array : arrays) {
            for (ReadArray<Integer> value : array.getValuees()) {
                ZoneMessage.itemInfo.Builder builder = ZoneMessage.itemInfo.newBuilder();
                builder.setModelId(value.get(0));
                builder.setNum(value.get(1));
                result.add(builder.build());
            }
        }
        if (resultExp !=null && resultExp.size()>0){
            for (ReadLongArrayEs array : resultExp) {
                for (ReadArray<Long> value : array.getValuees()) {
                    ZoneMessage.itemInfo.Builder builder = ZoneMessage.itemInfo.newBuilder();
                    builder.setModelId(value.get(0).intValue());
                    builder.setNum(value.get(1));
                    result.add(builder.build());
                }
            }
        }
        return result;
    }

    /**
     * 提前结束活动处理
     *
     * @param mapObject
     * @param player
     */
    private void advanceFinish(MapObject mapObject, Player player) {
        Position pos =   roundPosition(mapObject);
        Manager.mapManager.transport().ResCurMapTransport(player, mapObject, pos, 0, 0);
        finishDeal(mapObject, player, true);
        f2gSendBravePeakReward(player, mapObject, false);
        mapObject.addMapOnceScriptEventTimer(getId(), "playerDieOutBossHome", 10 * 1000, player);

        //增加跨服副本次数
        Manager.copyMapManager.manager().sendF2GCloneEnterAddOne(mapObject, player);
    }

    /**
     * 发送结束的战报
     *
     * @param mapObject
     * @param player
     * @param isSuccess
     */
    private void finishDeal(MapObject mapObject, Player player, boolean isSuccess) {
        int floor = mapObject.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE;
        List<ReadIntegerArrayEs> result = new ArrayList<>();
        List<ReadLongArrayEs> resultExp = new ArrayList<>();
        result.addAll(copySettlement(mapObject.getZoneModelId(), isSuccess,player,resultExp));
        sendSettlement(player, floor, result, resultExp);
    }

    /**
     * 10秒结束副本
     *
     * @param mapObject 副本上下文
     */
    public void KickPlayerOut(MapObject mapObject) {
        FightClientManager.GetInstance().getRoomStateCache().put(mapObject.getId(), FightRoomState.FIGHTREWARDEND);//领奖结束了
        Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHTREWARDEND);
        for (Player player : mapObject.getPlayers().values()) {
            Manager.copyMapManager.manager().onReqCopyMapOut(player);
        }
        mapObject.setStop(true);
        DailyActiveManager.bravePeakMapInfo.clear();
        log.error("天芒鬼扯活动结束 踢人");
    }

    /**
     * 发向游戏服，给玩家发奖励
     *
     * @param player
     * @param mapObject
     */
    private void f2gSendBravePeakReward(Player player, MapObject mapObject, boolean needToNext) {

        Manager.crossServerManager.sendToPublicPlayerOutCrossFight(player, mapObject.getId(), mapObject.getZoneModelId());
        BravePeakMessage.F2GSendBravePeakReward.Builder builder = BravePeakMessage.F2GSendBravePeakReward.newBuilder();
        builder.setFloor(mapObject.getZoneModelId() % BravePeakDefine.BRAVE_PEAK_COPY_ID_BASE);
        builder.setRoleId(player.getId());
        builder.setCopyMapId(mapObject.getZoneModelId());
        builder.setNeedToNext(needToNext);
        builder.setIsGetReward(isGetReward(player,mapObject));
        FightClientManager.GetInstance().send_to_game(player.getIosession(), BravePeakMessage.F2GSendBravePeakReward.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private boolean isGetReward(Player player, MapObject mapObject)
    {
        if ( DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().containsKey(player.getId())){
           List<Integer> rewardlist =   DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().get(player.getId());
           if (rewardlist.contains(mapObject.getZoneModelId())) {
                return true;
           }else {
               rewardlist.add(mapObject.getZoneModelId());
               return false;
           }
        }
        else {
            List<Integer> rewardlist = new ArrayList<>();
            rewardlist.add(mapObject.getZoneModelId());
            DailyActiveManager.bravePeakMapInfo.getPlayerGetReward().put(player.getId(),rewardlist);
            return false;
        }
    }

}
