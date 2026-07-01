package common.horse;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Equip_Container;
import com.data.container.Cfg_Horse_equip_score_Container;
import com.data.container.Cfg_Horse_equip_unlock_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.backpack.manager.BackpackManager;
import com.game.backpack.structs.Item;
import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.FlyBehavior;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.horse.script.IHorseScript;
import com.game.horse.structs.*;
import com.game.log.grow.GrowType;
import com.game.log.db.RoleGrowLog;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.nature.structs.Nature;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.structs.SynToFightType;
import com.game.structs.AttributeType;
import com.game.structs.EntityState;
import com.game.task.structs.TaskHelp;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.HorseMessage;
import game.message.HorseMessage.ResChangeHorse;
import game.message.HorseMessage.ResChangeRideState;
import game.message.HorseMessage.ResFlyActionRes;
import game.message.HorseMessage.ResUpdateHightRes;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author admin
 */
public class HorseScript implements IScript, IHorseScript {

    private static final Logger log = LogManager.getLogger(HorseScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HorseBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqChangeHorse(Player player, int horseLayer) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Mount)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MAIN_GONGNENGWEIKAIQI, "2&_坐骑");
            return;
        }

        ResChangeHorse.Builder resMsg = ResChangeHorse.newBuilder();

        Horse horse = player.getHorse();
        Nature nature = horse.getNature();
//
//        if (!horse.getMythicalMap().containsKey(horseLayer)) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HORSE_NOT_ACTIVE);
//            return;
//        }
//
//        Cfg_Horse_mythicalBean bean = Manager.gameDataManager.Cfg_Horse_mythicalContainer.GetValueByKey(horseLayer);
//        if (bean == null) {
//            log.error("horse_mythical 中不存在：" + horseLayer);
//            return;
//        }
//
//        if (EntityState.Fly.compare(player.getState()) && bean.getCan_fly() != 1) {//判断玩家是否在飞行状态
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Fly_CannotChangeHorse);//飞行中，普通坐骑不能出战!
//            log.error("玩家在飞行状态，不能起飞！roleId=" + player.getId());
//            resMsg.setIsChangeSuccess(false);
//            MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//            return;
//        }

        //判断是否满足坐骑更换条件
//        if (!manager.gameDataManager.Cfg_Horse_upContainer.getMap().containsKey(horseLayer)) {
//            log.error("请求更换乘骑的horseLayer错误！horseLayer=" + horseLayer);
//            resMsg.setIsChangeSuccess(false);
//            MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
//            return;
//        }
        Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(nature.getCurrentId());
        if (horse.getCurLayer() == horseLayer || horseBean.getSteps() < horseLayer) {
            log.error("请求更换的horseLayer不符合要求！horseLayer=" + horseLayer);
            resMsg.setIsChangeSuccess(false);
            MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
            return;
        }

        horse.setCurLayer(horseLayer);
        resMsg.setIsChangeSuccess(true);
        resMsg.setAfterHorseLayer(horseLayer);
        MessageUtils.send_to_player(player, ResChangeHorse.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        if (horse.getRideState() == HorseRideStateEnum.Ride) {//更换坐骑后向周围玩家发送广播
            sendRideStateInfo(player);
        }
        Manager.playerManager.savePlayer(player, SavePlayerLevel.HalfMinLater);
    }

    /**
     * 改变客户端外观
     *
     * @param player 玩家
     * @param state  状态
     * @return
     */
    @Override
    public boolean onReqChangeRideState(Player player, int state) {
        Horse horse = player.getHorse();

        int steps = horse.getHorseSteps();
        if (steps <= 0 && !horse.isRideOther()) {
            log.error(TaskHelp.getPlayerInfo(player) + "没有坐骑");
            return false;
        }

        if (GameServer.getInstance().IsFightServer()) {
            dealChangeRideState(player, state);
            return false;
        }

        MapObject curmap = Manager.mapManager.getMap(player.gainMapId());
        if (curmap == null) {
            log.error(TaskHelp.getPlayerInfo(player) + "切换坐骑时找不到地图");
            return false;
        }
        if (curmap.getZoneModelId() > 0) {
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(curmap.getZoneModelId());
            //副本中不能骑乘坐骑
            if (bean.getIsRide() == 0 && state == 1) {
//            MessageUtils.notify_player(player, Notify.CHAT, MessageString.Horse_InstanceCanNotRide);
                return false;
            }
        }
        //上马有条件
        if (state == 1 && player.isInBattle()) {
            MessageUtils.notify_player(player, Notify.CHAT, MessageString.Horse_BattleRideFobid); //脱离战斗状态才能骑乘坐骑
            return false;
        }
        //去掉隐身BUFF
        Manager.buffManager.deal().removeBuffInvisible(player);

        dealChangeRideState(player, state);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater); //保存玩家数据 
        return true;
    }

    /**
     * 用来处理玩家骑行状态变更
     *
     * @param player
     * @param state
     */
    private void dealChangeRideState(Player player, int state) {
        Horse horse = player.getHorse();
        if (state == HorseRideStateEnum.UnRide.getState()) {//true:下坐骑
            horse.setRideState(HorseRideStateEnum.UnRide);
            if (horse.isRideOther()) {
                /**
                 * TODO：等多人坐骑出来，修改此处
                 * */
//                horse.setRideOther(false);
//                Long invitedPalyerId = horse.getCurMythical().getOwnerId();
//                Player invitedPlayer = Manager.playerManager.getOnLinePlayer(invitedPalyerId);
//                List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(invitedPalyerId);
//                if (invitedPlayer != null) {
//                    MessageUtils.notify_same_horse_palyer(invitedPlayer, true, Notify.ERROR, MessageString.Player_Accord_UnRide, player.getName());
//                }
//                if (passengers == null) {
//                    log.error(TaskHelp.getPlayerInfo(player) + "作为乘客下马时，发现passenger列表为null！driverId：" + invitedPalyerId);
//                    return;
//                } else if (!passengers.contains(player.getId())) {
//                    log.error(TaskHelp.getPlayerInfo(player) + "作为乘客下马时，发现passenger列表为没有自己！driverId：" + invitedPalyerId);
//                } else {
//                    passengers.remove(player.getId());
//                }
//                horse.setCurMythical(null);
//                //如果车上还有其他的乘客，就发送乘客
//                if (passengers.size() > 0) {
//                    noticeAllSameRideDownInfo(player);
//                } else {
//                    //没有其他的乘客就发送司机的id
//                    noticeAllSameRideDownInfo(invitedPlayer);
//                    Manager.horseManager.getMultiPlayerHashMap().remove(invitedPalyerId);
//                }
//            } else {
//                MessageUtils.notify_same_horse_palyer(player, false, Notify.ERROR, MessageString.Host_Accord_Finish_This_Ride, player.getName());
//                //司机下马，设置乘客的状态为不是乘骑别人
//                List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
//                if (passengers != null) {
//                    for (Long passengerID : passengers) {
//                        Player passengerPlayer = Manager.playerManager.getOnLinePlayer(passengerID);
//                        if (passengerPlayer != null) {
//                            passengerPlayer.getHorse().setRideOther(false);
//                            passengerPlayer.getHorse().setCurMythical(null);
//                        }
//                    }
//                }
//                Manager.horseManager.getMultiPlayerHashMap().remove(player.getId());
//                noticeAllSameRideDownInfo(player);
            }
        } else {//上坐骑
            //player.removeSate(EntityState.Sitting);
            horse.setRideState(HorseRideStateEnum.Ride);
        }
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE); //上下马后重新计算坐骑加成属性
        sendRideStateInfo(player);
        //刷新对应的坐骑装备BUFF
        updateHorseEquipBuff(player);
    }

    /**
     * 处理玩家起飞和降落
     *
     * @param player
     * @param isFly
     * @param x
     * @param y
     */
    @Override
    public void onReqFlyActionHandler(Player player, boolean isFly, float x, float y) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Mount)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MAIN_GONGNENGWEIKAIQI, "2&_坐骑");
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config.getCanFly() == 0) {
            log.info("地图不能飞 " + player);
            return;
        }

        if (isFly) {
            if (player.getHorse().getRideState() != HorseRideStateEnum.Ride) {
                log.info("未骑坐骑 " + player);
                return;
            }
        } else {
            if (player.getHorse().getRideState() != HorseRideStateEnum.Fly) {
                log.info("不是飞行状态，不需要下降" + player);
                return;
            }
        }
//        Cfg_Horse_mythicalBean curHorse = Manager.gameDataManager.Cfg_Horse_mythicalContainer.GetValueByKey(player.getHorse().getCurLayer());
//        if (curHorse == null) {
//            log.info("配置表horse_mythical中未找到id=" + player.getHorse().getCurLayer());
//            return;
//        }
//        if (curHorse.getCan_fly() != 1) {
//            return;
//        }

        if (isFly) {//起飞
            //添加飞行行为
            if (!BehaviorManager.HasBehavior(player, BehaviorType.Fly)) {
                BehaviorManager.InsertBehavior(player, new FlyBehavior(player));
            }
            player.addState(EntityState.Fly);
            tuckAndReleasePet(player, 1);
            player.setHeight(config.getFly_min_height());
            player.getHorse().setRideState(HorseRideStateEnum.Fly);
        } else {//降落
            //检测降落点
            Position pos = new Position(x, y);
            //超过距离就回原点
            float defLen = Utils.getDistance(player.gainCurPos(), pos);
            if (defLen > 20) {
                log.error(player.nameIdString() + " 飞的距离落地超过了20！");
                return;
            }
            if (!Utils.isCanMove(map, pos)) {
                log.error(player.nameIdString() + " 飞的地点不能降落！");
                return;
            }
            log.error(player.nameIdString() + ",当前" + player.gainCurPos() + " 飞的地点降落坐标（" + x + "," + y + "）！");

            //去掉飞行行为
            if (BehaviorManager.HasBehavior(player, BehaviorType.Fly)) {
                BehaviorManager.CancelBehaviorByType(player, BehaviorType.Fly);
            }
            player.changeCurPos(pos, true);

            tuckAndReleasePet(player, 0);
            player.getHorse().setRideState(HorseRideStateEnum.Ride);
        }

        //TODO 更新
        ResFlyActionRes.Builder msg = ResFlyActionRes.newBuilder();
        msg.setRoleId(player.getId());
        msg.setFly(isFly);
        msg.setX(x);
        msg.setY(y);
        MessageUtils.send_to_roundPlayer(player, ResFlyActionRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 收拢或者释放宠物，在起飞和降落时调用
     *
     * @param player
     * @param action
     */
    private void tuckAndReleasePet(Player player, int action) {
        //大于0就是起飞,然后收起宠物
        if (action > 0) {
            tuckPet(player);
            List<Long> passenggers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            if (passenggers != null) {
                for (Long passengger : passenggers) {
                    Player otherPlayer = Manager.playerManager.getPlayerCache(passengger);
                    if (otherPlayer != null) {
                        tuckPet(otherPlayer);
                    }
                }
            }
        } else {
            releasePet(player);
            List<Long> passenggers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
            if (passenggers != null) {
                for (Long passengger : passenggers) {
                    Player otherPlayer = Manager.playerManager.getPlayerCache(passengger);
                    if (otherPlayer != null) {
                        releasePet(otherPlayer);
                    }
                }
            }
        }
    }

    /**
     * 收回宠物
     *
     * @param player
     */
    private void tuckPet(Player player) {
        int oldPet = player.getActivePet().getFightPet();
        if (oldPet > 0) {
            Manager.petManager.deal().petAction(player, 4, oldPet, false);      //让宠物休息中！
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.FLY_AUTO_RESET_PET);
        }
    }

    private void releasePet(Player player) {
        int oldPet = player.getActivePet().getFightPet();
        if (oldPet > 0) {
            Manager.petManager.deal().petAction(player, 3, oldPet, false);      //让宠物出站！
        }
    }

    @Override
    public void onReqUpdateHightHandler(Player player, float high) {

        //TODO 条件检测
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        Cfg_Mapsetting_Bean config = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
        if (config.getFly_min_height() > high || config.getFly_max_height() < high) {
            log.info("位置不对" + player);
            return;
        }
        //TODO 更新
        player.setHeight(high);

        ResUpdateHightRes.Builder msg = ResUpdateHightRes.newBuilder();
        msg.setRoleId(player.getId());
        msg.setHigh(high);
        MessageUtils.send_to_roundPlayer(player, ResUpdateHightRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void sendRideStateInfo(Player player) {
        ResChangeRideState.Builder rMsg = ResChangeRideState.newBuilder();
        if (player.getHorse().getRideState() == HorseRideStateEnum.UnRide) {//true:下坐骑
            rMsg.setRideId(0);
        } else if (player.getHorse().getRideState() == HorseRideStateEnum.Ride) {
            if (player.getHorse().isRideOther()) {
                rMsg.setRideId(0);
            } else {
                rMsg.setRideId(player.getHorse().getHorseModelId());
            }
        } else if (player.getHorse().getRideState() == HorseRideStateEnum.Fly) {
            if (player.getHorse().isRideOther()) {
                rMsg.setRideId(0);
            } else {
                rMsg.setRideId(player.getHorse().getHorseModelId());
            }
        }
        rMsg.setPlayerId(player.getId());

        if (player.playerCrossData.isToFightServer()) {
            java.util.Map<Integer, Object> map = new HashMap<>();
            map.put(1, player.getHorse().getCurLayer());
            map.put(2, player.getHorse().getRideState().getState());
            map.put(3, player.getAttribute().getAdditionValue(AttributeType.ATTR_Speed));
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.HorseChange, map, ResChangeRideState.MsgID.eMsgID_VALUE, rMsg.build().toByteString());
        } else {
            MessageUtils.send_to_roundPlayer(player, ResChangeRideState.MsgID.eMsgID_VALUE, rMsg.build().toByteArray()); //更换坐骑后向周围玩家发送广播     
        }
    }


    /**
     * 邀请他人同乘坐骑
     *
     * @param player
     * @param invitedPlayerId 被邀请的玩家Id
     */
    @Override
    public void onReqInviteOtherPlayer(Player player, long invitedPlayerId) {
//        Horse horse = player.getHorse();
//        //条件检查
//        int nowHorseId = horse.getCurLayer();
//        if (nowHorseId == 0) {
//            log.error(TaskHelp.getPlayerInfo(player) + "还没有开启坐骑系统！");
//            return;
//        }
//        Mythical mythical = horse.getMythicalMap().get(nowHorseId);
//        if (mythical == null) {
//            log.error(TaskHelp.getPlayerInfo(player) + "未获得此坐骑(" + nowHorseId + "),当前已经拥有的坐骑为：" + horse.getMythicalMap());
//            return;
//        }
//        if (mythical.getType() != HorseLevelEnum.Multi_Player_Horse.getType()) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Not_Ride_Special_Horse, HorseLevelEnum.Multi_Player_Horse.getName(), "邀请");
//            return;
//        }
//        Cfg_Horse_mythicalBean bean = Manager.gameDataManager.Cfg_Horse_mythicalContainer.GetValueByKey(nowHorseId);
//        if (bean == null) {
//            log.error("horse_mythical 中不存在 ：" + nowHorseId);
//            return;
//        }
//        List<Long> passenggers = Manager.horseManager.getMultiPlayerHashMap().get(player.getId());
//        if (passenggers != null) {
//            if (passenggers.contains(invitedPlayerId)) {
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Oppisite_Have_On_This_Mythical);
//                return;
//            }
//        }
//        if (passenggers != null) {
//            if (passenggers.size() + 1 >= bean.getRide_num()) {
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Cur_Multi_Player_Horse_Ride_Max);
//                return;
//            }
//        }
//        Player invitedPlayer = Manager.playerManager.getOnLinePlayer(invitedPlayerId);
//        if (invitedPlayer == null) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildInviteFailed_OppNotOnLine);
//            return;
//        }
//
//        int maxDistance = Global.GetIntValue(Global.Muti_Horse_Invite_Distance, 15);
//        double distance = Utils.getDistance(invitedPlayer.gainCurPos(), player.gainCurPos());
//        if (distance > maxDistance) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.You_Too_Far_With_Invited);
//            return;
//        }
//
//        //请求的转发
//        ResInviteMessageDisPatcher.Builder builder = ResInviteMessageDisPatcher.newBuilder();
//        builder.setInviteName(player.getName());
//        builder.setInvitePlayerId(player.getId());
//        MessageUtils.send_to_player(invitedPlayer, ResInviteMessageDisPatcher.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 被邀请的玩家响应别人的邀请
     *
     * @param player
     * @param invitePlayerId 邀请者玩家Id
     * @param agreeOrRefuse  同意或者拒绝
     */
    @Override
    public void onReqResponseInvite(Player player, long invitePlayerId, boolean agreeOrRefuse) {

//        Player invitePlayer = Manager.playerManager.getOnLinePlayer(invitePlayerId);
//        if (invitePlayer == null) {
//            log.error(TaskHelp.getPlayerInfo(player) + "响应别人的同乘邀请时，邀请人为null");
//            return;
//        }
//        if (agreeOrRefuse) {
//            Horse horse = invitePlayer.getHorse();
//            if (horse.getCurLayer() <= 0) {
//                log.error(TaskHelp.getPlayerInfo(invitePlayer) + "还没有坐骑！");
//                return;
//            }
//            Cfg_Horse_mythicalBean bean = Manager.gameDataManager.Cfg_Horse_mythicalContainer.GetValueByKey(horse.getCurLayer());
//            if (bean == null) {
//                log.error("horse_mythical 配置配置表中，不存在" + horse.getCurLayer());
//                return;
//            }
//            if (bean.getType() != HorseLevelEnum.Multi_Player_Horse.getType()) {
//                log.error(TaskHelp.getPlayerInfo(invitePlayer) + "不是乘坐的多人坐骑！");
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Opposite_State_Cant_Multi_Ride);
//                return;
//            }
//            if (horse.getRideState() == HorseRideStateEnum.UnRide) {
//                log.error(TaskHelp.getPlayerInfo(invitePlayer) + "邀请" + TaskHelp.getPlayerInfo(player) + "时已经下马！");
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Opposite_State_Cant_Multi_Ride);
//                return;
//            }
//            if (horse.getRideState() == HorseRideStateEnum.Fly) {
//                log.error(TaskHelp.getPlayerInfo(invitePlayer) + "邀请" + TaskHelp.getPlayerInfo(player) + "时已经起飞！");
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.Plyaer_Is_Fly_You_Cant_Ride);
//                return;
//            }
//            List<Long> passengers = Manager.horseManager.getMultiPlayerHashMap().get(invitePlayer.getId());
//            if (passengers != null) {
//                if (passengers.contains(player.getId())) {
//                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.You_Have_On_This_Mythical);
//                    return;
//                }
//            }
//            if (passengers != null) {
//                if (passengers.size() + 1 >= bean.getRide_num()) {
//                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.Cur_Multi_Player_Horse_Ride_Max);
//                    return;
//                }
//            }
//
//            int maxDistance = Global.GetIntValue(Global.Muti_Horse_Invite_Distance, 15);
//            double distance = Utils.getDistance(invitePlayer.gainCurPos(), player.gainCurPos());
//            if (distance > maxDistance) {
//                MessageUtils.notify_player(player, Notify.ERROR, MessageString.You_Too_Far_With_Inviter);
//                return;
//            }
//
//            if (player.getHorse().getRideState() == HorseRideStateEnum.Ride) {
//                Manager.horseManager.autoUnRide(player);
//            }
//
//            player.getHorse().setRideOther(true);
//            player.getHorse().setCurMythical(horse.getMythicalMap().get(horse.getCurLayer()));
//            // player.getHorse().setRideState(HorseRideStateEnum.Ride);
//            Manager.horseManager.addOneToMultiPlayerHorse(invitePlayerId, player.getId());
//            noticeAllSameRideOnInfo(invitePlayer);
//            player.changeCurPos(invitePlayer.getCurGps().getPos(), false);
//            // sendRideStateInfo(player);
//        } else {
//            MessageUtils.notify_player(invitePlayer, Notify.ERROR, MessageString.Team_RefuseInviteInfo, player.getName());
//        }
    }

    @Override
    public void initPlayerHorseEquip(Player player) {
        for (Cfg_Horse_equip_unlock_Bean bean : Cfg_Horse_equip_unlock_Container.GetInstance().getValuees()) {
            HorseEquip equip = player.getHorse().getEquips().get(bean.getSite());
            if (equip == null) {
                equip = new HorseEquip(bean.getSite());
                player.getHorse().getEquips().put(bean.getSite(), equip);
            }
            if (!equip.isActive() && Manager.controlManager.deal().checkFuncProgress(player, bean.getSiteUnlock())) {
                equip.setActive(true);
                Manager.controlManager.operate(player, FunctionVariable.Horse_equip_pos, bean.getSite(), 1);
                if (player.getHorse().getHorseEquipactiveId() == 0) {
                    player.getHorse().setHorseEquipactiveId(equip.getEquipId());
                }
                equip.setStrengthActiveId(bean.getSite() * 10000);
                equip.setSoulActiveId(bean.getSite() * 10000);
            }
            HorseEquipPart part = equip.getParts().get(bean.getPartId());
            if (part == null) {
                part = new HorseEquipPart();
                equip.getParts().put(bean.getPartId(), part);
            }
            if (!part.isActive() && Manager.controlManager.deal().checkFuncProgress(player, bean.getPartUnlock())) {
                part.setActive(true);
            }
        }
    }

    /**
     * @param player
     * @param horseId
     * @return
     */
    private MultiPlayerHorse createMultiPlayerHorse(Player player, int horseId) {
        MultiPlayerHorse horse = new MultiPlayerHorse();
        horse.setModelId(horseId);
        horse.setTrainCount(0);
        horse.setOwnerId(player.getId());
        return horse;
    }

    public void online(Player player) {
        //发送脉轮信息
        sendAllHorseEquipInfo(player);
        //更新buff
        updateHorseEquipBuff(player);
        //发送背包信息
        backpackMessage.ResHorseEquipBagInfos.Builder bagInfo = backpackMessage.ResHorseEquipBagInfos.newBuilder();
        player.getHorseEquipPackItems().values().forEach(item -> bagInfo.addItemInfoList(Manager.backpackManager.manager().buildItemInfo(item).build()));
        MessageUtils.send_to_player(player, backpackMessage.ResHorseEquipBagInfos.MsgID.eMsgID_VALUE, bagInfo.build().toByteArray());
    }

    @Override
    public void changeHorseAssiant(Player player, int assistantId, int id) {
        HorseEquip horseEquip = player.getHorse().getEquips().get(assistantId);
        //助阵位找不到
        if (horseEquip == null || !horseEquip.isActive()) {
            log.error("前端传来的助阵位id非法assistantId={} p={}", assistantId, player);
            return;
        }
        //该助阵位上本来就是这个宠物
        if (player.getHorse().getHorseEquipactiveId() == assistantId) {
            return;
        }
        //失效
        HorseEquip oldEquip = player.getHorse().getEquips().get(player.getHorse().getHorseEquipactiveId());
        if (oldEquip != null) {
            //移除buf
            if (oldEquip.getBuff() != 0) {
                Manager.buffManager.deal().onRemoveBuff(player, oldEquip.getBuff());
            }
        }
        //激活
        player.getHorse().setHorseEquipactiveId(assistantId);
        //加上buf
        if (horseEquip.getBuff() != 0) {
            Manager.buffManager.deal().onAddBuff(player, player, horseEquip.getBuff());
        }
        //推送客户端该助战位成功换上了宠物
        HorseMessage.ResMountChangeAssi.Builder pb = HorseMessage.ResMountChangeAssi.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setPetModelId(assistantId);
        MessageUtils.send_to_player(player, HorseMessage.ResMountChangeAssi.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    @Override
    public void wearEquip(Player player, long equipId, int assistantId, int cellId) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑脉轮数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        if (!assistant.isActive()) {
            log.error("坐骑脉轮未激活" + player.getId() + ", " + assistantId);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Nonactivated);
            return;
        }
        HorseEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            log.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        Item equip = player.getHorseEquipPackItems().get(equipId);
        if (equip == null) {
            log.error("坐骑装备找不到");
            return;
        }
        Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (eqBean == null) {
            log.error("装备表中没有找到该装备,id=" + equip.getItemModelId());
            return;
        }
        if (eqBean.getLevel() > player.getLevel()) {
            log.error("等级不足，无法穿戴");
            return;
        }
        if (eqBean.getPart() != cellId) {
            log.error("该装备不能穿戴到这个位置");
            return;
        }
        long action = IDConfigUtil.getLogId();
        //背包扣除将要穿戴的装备
        Item equipInBag = removeHorseEquip(player, equipId, ItemChangeReason.HorseEquipWearDec, action);
        if (equipInBag == null) {
            log.error("扣除背包中装备失败");
            return;
        }
        Item oldEquip = part.getItem();
        //该位置已经有装备，旧装备到背包
        if (oldEquip != null) {
            addHorseEquip(player, oldEquip, ItemChangeReason.HorseEquipWearGet, action);
        }
        part.setItem(equipInBag);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        //坐骑装备评分改变计算
        horseEquipUpdateScore(player, assistant);
        //通知客户端
        HorseMessage.ResMountEquipWear.Builder pb = HorseMessage.ResMountEquipWear.newBuilder();
        pb.setEquipId(equipId);
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setEquipModelId(equipInBag.getItemModelId());
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipWear.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player, GrowType.horse_equip_wear, eqBean, part.getStrengthLv(), assistantId);
    }

    @Override
    public void intenHorseEquip(Player player, int assistantId, int cellId) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑装备数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        HorseEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            log.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getItem() == null) {
            log.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        int oldIntenLv = part.getStrengthLv();
        //当前强化等级的配置
        Cfg_Horse_equip_inten_Bean nowIntenConfig = CfgManager.getCfg_Horse_equip_inten_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldIntenLv);
        //下一级强化等级的配置
        Cfg_Horse_equip_inten_Bean nextIntenConfig = CfgManager.getCfg_Horse_equip_inten_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldIntenLv + 1);
        if (nextIntenConfig == null) {
            log.error("已强化到最高等级");
            return;
        }
        if (nowIntenConfig == null) {
            log.error("强化配置表找不到");
            return;
        }
        Cfg_Equip_Bean equip = Cfg_Equip_Container.GetInstance().getValueByKey(part.getItem().getItemModelId());
        if (equip == null) {
            log.error("装备配置表找不到equipId:{}", equip.getId());
            return;
        }
        //物品阶数要求
        if (nextIntenConfig.getGrade() > equip.getGrade()) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Equip_Intensify_Grade_Failure, nextIntenConfig.getGrade());
            return;
        }
        //强化所需物品不足
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, nowIntenConfig.getConsume(), IDConfigUtil.getLogId(), ItemChangeReason.HorseEquipIntenDec)) {
            return;
        }
        part.setStrengthLv(oldIntenLv + 1);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        //返回结果
        HorseMessage.ResMountEquipStrength.Builder pb = HorseMessage.ResMountEquipStrength.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setStrengthLv(part.getStrengthLv());
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipStrength.MsgID.eMsgID_VALUE, pb.build().toByteArray());
//        Manager.controlManager.operate(player, FunctionVariable.Horse_Equip_Strengthen_Level, 1);

        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getItem().getItemModelId());
        RoleGrowLog.create(player, GrowType.horse_equip_intensify, targetBean, part.getStrengthLv(), assistantId);
    }

    @Override
    public void soulHorseEquip(Player player, int assistantId, int cellId) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑装备数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        HorseEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            log.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getItem() == null) {
            log.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        int oldSoulLv = part.getSoulLv();
        //当前附魂等级的配置
        Cfg_Horse_equip_soulbound_Bean nowSoulConfig = CfgManager.getCfg_Horse_equip_soulbound_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldSoulLv);
        //下一级附魂等级的配置
        Cfg_Horse_equip_soulbound_Bean nextSoulConfig = CfgManager.getCfg_Horse_equip_soulbound_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldSoulLv + 1);
        if (nextSoulConfig == null) {
            log.error("已附魂到最高等级");
            return;
        }
        if (nowSoulConfig == null) {
            log.error("附魂消耗数据找不到");
            return;
        }
        //附魂所需物品不足
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, nowSoulConfig.getConsume(), IDConfigUtil.getLogId(), ItemChangeReason.HorseEquipSoulDec)) {
            return;
        }
        part.setSoulLv(oldSoulLv + 1);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        //返回结果
        HorseMessage.ResMountEquipSoul.Builder pb = HorseMessage.ResMountEquipSoul.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setSoulLv(part.getSoulLv());
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipSoul.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getItem().getItemModelId());
        RoleGrowLog.create(player, GrowType.horse_equip_soul, targetBean, part.getSoulLv(), assistantId);
    }

    @Override
    public void horseEquipIntenActive(Player player, int assistantId, int levelId) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑装备数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        if (assistant.getStrengthActiveId() >= levelId) {
            log.error("该等级已经激活了" + player.getId() + ", " + assistantId);
            return;
        }
        Cfg_Horse_equip_inten_class_Bean config = CfgManager.getCfg_Horse_equip_inten_class_Container().getValueByKey(levelId);
        if (config == null) {
            log.error("配置表找不到" + levelId);
            return;
        }
        int totalLv = 0;
        for (HorseEquipPart part : assistant.getParts().values()) {
            totalLv += part.getStrengthLv();
        }
        if (totalLv < config.getSuitLevel()) {
            log.error("还没有达到全身强等级");
            return;
        }
        int oldLevel = assistant.getStrengthActiveId();
        assistant.setStrengthActiveId(levelId);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        //返回结果
        HorseMessage.ResMountEquipActiveInten.Builder pb = HorseMessage.ResMountEquipActiveInten.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setStrengthActiveId(assistant.getStrengthActiveId());
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipActiveInten.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player, GrowType.horse_equip_intensify_active, 0, assistantId, oldLevel, levelId, null);
    }

    @Override
    public void horseEquipSoulActive(Player player, int assistantId, int levelId) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑脉轮装备数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        if (assistant.getSoulActiveId() >= levelId) {
            log.error("该等级已经激活了" + player.getId() + ", " + assistantId);
            return;
        }
        Cfg_Horse_equip_soulbound_class_Bean config = CfgManager.getCfg_Horse_equip_soulbound_class_Container().getValueByKey(levelId);
        if (config == null) {
            log.error("配置表找不到" + levelId);
            return;
        }
        int totalLv = 0;
        for (HorseEquipPart part : assistant.getParts().values()) {
            totalLv += part.getSoulLv();
        }
        if (totalLv < config.getSuitLevel()) {
            log.error("全身附魂等级没有全部达到");
            return;
        }
        int oldLevel = assistant.getSoulActiveId();
        assistant.setSoulActiveId(levelId);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        //返回结果
        HorseMessage.ResMountEquipActiveSoul.Builder pb = HorseMessage.ResMountEquipActiveSoul.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setSoulActiveId(assistant.getSoulActiveId());
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipActiveSoul.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player, GrowType.horse_equip_soul_active, 0, assistantId, oldLevel, levelId, null);
    }

    @Override
    public void horseEquipSynthetic(Player player, int assistantId, int cellId, List<Long> eqs) {
        HorseEquip assistant = player.getHorse().getEquips().get(assistantId);
        if (assistant == null) {
            log.error("坐骑装备数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        HorseEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            log.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getItem() == null) {
            log.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }

        //升级前装备
        Cfg_Equip_Bean srcBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getItem().getItemModelId());
        //升级的配置数据
        Cfg_Horse_equip_synthesis_Bean synthesis_Bean = CfgManager.getCfg_Horse_equip_synthesis_Container().getValueByKey(part.getItem().getItemModelId());
        //升级后装备
        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(synthesis_Bean.getEquip_ID());
        if (srcBean == null || targetBean == null) {
            log.error("升级前后的装备bean竟然为空");
            return;
        }

        List<Item> equips = new ArrayList<>();
        long totalProbability = 0L;
        int qualityIndex, diamondIndex, join_num_index;
        for (Long eid : eqs) {
            //检测前端传来的即将消耗的装备存不存在
            Item equip = player.getHorseEquipPackItems().get(eid);
            if (equip == null) {
                log.error("坐骑装备找不到");
                return;
            }
            //要消耗的装备模板数据
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (!synthesis_Bean.getJoin_part().isEmpty() && !synthesis_Bean.getJoin_part().contains(bean.getPart())) {
                log.error("不能消耗该位置的装备");
                return;
            }
            if (!synthesis_Bean.getQuality().contains(bean.getQuality())) {
                log.error("不能消耗该品质的装备");
                return;
            }
            if (!synthesis_Bean.getDiamond().contains(bean.getDiamond_Number())) {
                log.error("不能消耗该星级的装备");
                return;
            }
            qualityIndex = getArrayIndex(synthesis_Bean.getQuality(), bean.getQuality());
            diamondIndex = getArrayIndex(synthesis_Bean.getDiamond(), bean.getDiamond_Number());
            join_num_index = getJoinNumIndex(targetBean.getGrade(), bean.getGrade());
            totalProbability += (long) synthesis_Bean.getQuality_Number().get(qualityIndex) * synthesis_Bean.getDiamond_Number().get(diamondIndex) * synthesis_Bean.getJoin_num_probability().get(join_num_index);
            equips.add(equip);
        }

        long action = IDConfigUtil.getLogId();
        //要扣除的道具
        if (synthesis_Bean.getJoin_item().size() == 2) {
            int itemNumber = synthesis_Bean.getJoin_item().get(1);
            int itemModeId = synthesis_Bean.getJoin_item().get(0);
            if (!Manager.backpackManager.manager().onRemoveItem(player, itemModeId, itemNumber, ItemChangeReason.HorseEquipComposeDec, action)) {
                log.error("要扣除的道具不足");
                return;
            }
        }

        //移除消耗的装备
        for (Long eid : eqs) {
            Item removed = removeHorseEquip(player, eid, ItemChangeReason.HorseEquipComposeDec, action);
            if (removed == null) {
                log.error("要扣除的装备不存在");
            }
        }

        //概率计算成功与否
        boolean success = RandomUtils.defaultIsGenerate((int) (totalProbability / 10000_0000));

        HorseMessage.ResMountEquipSynthesis.Builder res = HorseMessage.ResMountEquipSynthesis.newBuilder();
        res.setSuccess(success);
        res.setAssistantId(assistantId);
        res.setCellId(cellId);

        if (success) {
            //合成了新装备，直接替换掉源装备
            Item tarEquip = Item.createOwnedItem(targetBean.getId(), 1, true);
            Item srcEquip = part.getItem();
            //物品消耗日志
            Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, srcEquip, ItemChangeReason.HorseEquipComposeDec, action);
            part.setItem(tarEquip);
            res.setNewEquip(BackpackManager.getInstance().manager().buildItemInfo(tarEquip));
            //物品添加日志
            Manager.backpackManager.manager().writeItemLogAndBI(player, 0, 1, tarEquip, ItemChangeReason.HorseEquipComposeGet, action);
            if (synthesis_Bean.getNotice() !=0 || synthesis_Bean.getChatchannel() != null) {


                List<Item> itemList = new ArrayList<>();
                itemList.add(tarEquip);

                String rewardStr = Utils.makeItemsStr(itemList);

                //TODO: 公告
                MessageUtils.notify_allOnlinePlayer(synthesis_Bean.getNotice(),synthesis_Bean.getChatchannel(), MessageString.EquipSynthetic,
                        player.getId()+"",  player.getName(), rewardStr,
                        Utils.makeUrlStr(MessageString.EquipSynthetic));
            }

            RoleGrowLog.create(player, GrowType.horse_equip_synthesis, targetBean, part.getStrengthLv(), assistantId);
        }
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
        horseEquipUpdateScore(player, assistant);
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipSynthesis.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    private int getJoinNumIndex(int target, int stuff) {
        if (target > stuff) {
            int v = target - stuff;
            if (v == 1) {
                return 2;
            } else {
                return 3;
            }
        } else if (target < stuff) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public void horseEquipDecompose(Player player, List<Long> equipId) {
        //奖励id与数量，用于奖励合并
        HashMap<Integer, Integer> map = new HashMap<>();
        long action = IDConfigUtil.getLogId();
        //成功分解的装备数
        int count = 0;
        for (Long id : equipId) {
            //背包扣除将要分解的装备
            Item equipInBag = removeHorseEquip(player, id, ItemChangeReason.HorseEquipDecomposeDec, action);
            if (equipInBag == null) {
                log.error("扣除背包中装备失败");
                continue;
            }
            Cfg_Horse_equip_resolve_Bean bean = CfgManager.getCfg_Horse_equip_resolve_Container().getValueByKey(equipInBag.getItemModelId());
            if (bean == null) {
                log.error("配置数据找不到");
                continue;
            }
            for (ReadArray<Integer> value : bean.getResolve_rewards().getValuees()) {
                if (RandomUtils.random(10000) < value.get(0)) {
                    Integer num = map.getOrDefault(value.get(1), 0);
                    map.put(value.get(1), num + value.get(2));
                }
            }
            count++;
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            BackpackManager.getInstance().manager().addItem(player, Item.createItem(entry.getKey(), entry.getValue(), true), ItemChangeReason.HorseEquipDecomposeGet, action);
        }
        Manager.countManager.addCount(player, BaseCountType.Horse_Equip_Resolve, 0, Count.RefreshType.CountType_Forever, count);
//        Manager.controlManager.operate(player, FunctionVariable.Horse_Equip_Resolve, count);
    }

    @Override
    public boolean addHorseEquip(Player player, Item item, int reason, long action) {
        if (item == null) {
            log.error("传入的道具为空！");
            return false;
        }
        Cfg_Equip_Bean cfg = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
        if (cfg == null) {
            log.error("道具的模板数据找不到！");
            return false;
        }

        //添加日志
        if (reason != ItemChangeReason.HorseEquipWearGet) {
            Manager.backpackManager.manager().writeItemLogAndBI(player, 0, item.getNum(), item, reason, action);
        }
        //添加物品
        player.getHorseEquipPackItems().put(item.getId(), item);
        backpackMessage.ResHorseEquipAdd.Builder builder = backpackMessage.ResHorseEquipAdd.newBuilder();
        builder.setItemInfo(Manager.backpackManager.manager().buildItemInfo(item).build());
        builder.setReason(reason);
        MessageUtils.send_to_player(player, backpackMessage.ResHorseEquipAdd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //设置了装备自动分解
        if (player.getHorse().isAutoDecompose()) {
            //满足自动分解的条件
            if (6 > cfg.getQuality() && 1 >= cfg.getDiamond_Number()) {
                Cfg_Horse_equip_resolve_Bean bean = CfgManager.getCfg_Horse_equip_resolve_Container().getValueByKey(item.getItemModelId());
                if (bean == null) {
                    log.error("配置数据找不到");
                } else {
                    removeHorseEquip(player, item.getId(), ItemChangeReason.HorseEquipDecomposeDec, action);
                    for (ReadArray<Integer> value : bean.getResolve_rewards().getValuees()) {
                        if (RandomUtils.random(10000) < value.get(0)) {
                            BackpackManager.getInstance().manager().addItem(player, Item.createItem(value.get(1), value.get(2), true), ItemChangeReason.HorseEquipAutoDecomposeGet, action);
                        }
                    }
                }
            }
        }

        Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
        return true;
    }

    @Override
    public boolean canAdd(Player player, int horseEquipNum) {
        return horseEquipNum + player.getHorseEquipPackItems().size() <= 200;
    }

    @Override
    public void autoEquipDecomposeSet(Player player, boolean set) {
        player.getHorse().setAutoDecompose(set);
        HorseMessage.ResMountEquipDecomposeSetting.Builder pb = HorseMessage.ResMountEquipDecomposeSetting.newBuilder();
        pb.setSet(set);
        MessageUtils.send_to_player(player, HorseMessage.ResMountEquipDecomposeSetting.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    @Override
    public void activeHorseEquip(Player player, int slotId) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MountEquip)) {
            return;
        }
        if (slotId != 0) {
            HorseEquip site = player.getHorse().getEquips().get(slotId);
            if (site == null || site.isActive()) {
                return;
            }
            Map.Entry<Integer, HorseEquipPart> cell = Utils.findOne(site.getParts().entrySet(), et -> true);

            Cfg_Horse_equip_unlock_Bean bean = CfgManager.getCfg_Horse_equip_unlock_Container().getValueByKey(slotId * 10000 + cell.getKey());
            if (Manager.backpackManager.manager().onRemoveItem(
                    player, bean.getSiteUnlockItem().get(0), bean.getSiteUnlockItem().get(1), ItemChangeReason.HorseEquipActiveDec, IDConfigUtil.getLogId())) {
                site.setActive(true);
                sendAllHorseEquipInfo(player);
                horseEquipUpdateScore(player, site);
                Manager.controlManager.operate(player, FunctionVariable.Horse_equip_pos, slotId, 1);
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.ActiveSuccess);
                log.info("道具激活脉轮槽位 slotId={} P={}", bean.getSite(), player);
                RoleGrowLog.create(player, GrowType.horse_equip_active, 0, slotId, 0, slotId, null);
            }
            return;
        }
        boolean isActive = false;
        for (Cfg_Horse_equip_unlock_Bean bean : CfgManager.getCfg_Horse_equip_unlock_Container().getValuees()) {
            HorseEquip site = player.getHorse().getEquips().get(bean.getSite());
            if (site == null) {
                site = new HorseEquip(bean.getSite());
                player.getHorse().getEquips().put(bean.getSite(), site);
            }
            //检查槽位装备栏
            HorseEquipPart part = site.getParts().get(bean.getPartId());
            if (part == null) {
                part = new HorseEquipPart();
                site.getParts().put(bean.getPartId(), part);
            }
            if (!part.isActive()) {
                if (Manager.controlManager.deal().checkFuncProgress(player, bean.getPartUnlock())) {
                    part.setActive(true);
                    isActive = true;
                    log.info("条件激活脉轮部位 slotId={} part={} P={}", bean.getSite(), bean.getPartId(), player);
                }
            }
            if (site.isActive()) {
                continue;
            }
            if (bean.getSiteUnlock().size() <= 0) {
                continue;
            }
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getSiteUnlock())) {
                site.setActive(true);
                Manager.controlManager.operate(player, FunctionVariable.Horse_equip_pos, slotId, 1);
                isActive = true;
                horseEquipUpdateScore(player, site);
                log.info("条件激活脉轮槽位 slotId={} P={}", bean.getSite(), player);
                RoleGrowLog.create(player, GrowType.horse_equip_active, 0, bean.getSite(), 0, bean.getSite(), null);
            }
        }
        if (isActive) {
            sendAllHorseEquipInfo(player);
//            Manager.controlManager.operate(player, FunctionVariable.Pet_Equip_Inten, 1);
        }
    }

    @Override
    public void horseEquipUpdateScore(Player player, HorseEquip horseEquip) {
        int total = 0;
        for (HorseEquipPart part : horseEquip.getParts().values()) {
            Item item = part.getItem();
            if (item != null && part.isActive()) {
                Cfg_Equip_Bean equip = Cfg_Equip_Container.GetInstance().getValueByKey(item.getItemModelId());
                if (equip != null) {
                    total += equip.getScore();
                }
            }
        }
        horseEquip.setScore(total);

        Manager.controlManager.operate(player, FunctionVariable.Horse_equip_score, horseEquip.getEquipId(), total);
        //计算等级和buf
        Cfg_Horse_equip_score_Bean bean = null;
        int buff = 0;
        for (Cfg_Horse_equip_score_Bean score_bean : Cfg_Horse_equip_score_Container.GetInstance().getValuees()) {
            if (score_bean.getSite() == horseEquip.getEquipId() && total >= score_bean.getNeed_score()) {
                buff = score_bean.getVFX();
                bean = score_bean;
            }
        }
        //更新buff
        if (horseEquip.getBuff() != buff) {
            if (horseEquip.getBuff() != 0) {
                Manager.buffManager.deal().onRemoveBuff(player, horseEquip.getBuff());
            }
            horseEquip.setBuff(buff);
            if (player.getHorse().getRideState() != HorseRideStateEnum.UnRide) {
                Manager.buffManager.deal().onAddBuff(player, player, buff);
            }
        }

        //公告发送
        if(bean != null){
            if (bean.getNotice() != 0 || bean.getChatchannel() != null) {
                //脉轮名称
                Cfg_Horse_equip_unlock_Bean unlock_bean = null;
                for(Cfg_Horse_equip_unlock_Bean b : Cfg_Horse_equip_unlock_Container.GetInstance().getValuees()){
                    if(b.getSite() == horseEquip.getEquipId()){
                        unlock_bean = b;
                        break;
                    }
                }
                if(unlock_bean == null){
                    return;
                }

                MessageUtils.notify_allOnlinePlayer(bean.getNotice() , bean.getChatchannel(), MessageString.HORSE_EQUIP_SCORE_NOTICE1,
                        player.getId()+"", player.getName(), unlock_bean.getName(), bean.getName(),
                        Utils.makeUrlStr(MessageString.HORSE_EQUIP_SCORE_NOTICE1));
            }
        }
    }

    /**
     * 获取指定值在数组中的索引
     */
    private int getArrayIndex(ReadIntegerArray array, int target) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    private Item removeHorseEquip(Player player, long equipId, int reason, long action) {
        Item item = player.getHorseEquipPackItems().remove(equipId);
        if (item == null) {
            return null;
        }
        backpackMessage.ResHorseEquipDelete.Builder res = backpackMessage.ResHorseEquipDelete.newBuilder();
        res.setReason(reason);
        res.setItemId(equipId);
        MessageUtils.send_to_player(player, backpackMessage.ResHorseEquipDelete.MsgID.eMsgID_VALUE, res.build().toByteArray());
        Manager.backpackManager.manager().writeItemLogAndBI(player, 1, 0, item, reason, action);
        return item;
    }

    /**
     * 发送所有坐骑脉轮信息
     *
     * @param player
     */
    private void sendAllHorseEquipInfo(Player player) {

        if (player.getHorse().getEquips().size() == 0) {
            initPlayerHorseEquip(player);
        }
        HorseMessage.ResHorseEquipList.Builder res = HorseMessage.ResHorseEquipList.newBuilder();
        player.getHorse().getEquips().entrySet().stream().forEach(e -> {
            e.getValue().setEquipId(e.getKey());
            res.addAssistantList(e.getValue().bytesWriteToPb());
        });
        res.setCurLevel(1);
        res.setCurExp(1);
        res.setBattleId(player.getHorse().getHorseEquipactiveId());
        res.setFuncOpen(true);
        res.setAutoSet(player.getHorse().isAutoDecompose());

        MessageUtils.send_to_player(player, HorseMessage.ResHorseEquipList.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 刷新坐骑装备BUFF
     *
     * @param player 玩家
     */
    private void updateHorseEquipBuff(Player player) {
        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MountEquip)) {
            Horse horse = player.getHorse();
            if (horse.getRideState() == HorseRideStateEnum.UnRide) {
                //下坐骑取消所有buff
                for (HorseEquip eq : horse.getEquips().values()) {
                    if (eq.isActive() && eq.getBuff() != 0) {
                        Manager.buffManager.deal().onRemoveBuff(player, eq.getBuff());
                    }
                }
            } else {
                //添加所有buff
                for (HorseEquip eq : horse.getEquips().values()) {
                    if (eq.isActive() && eq.getBuff() != 0) {
                        Manager.buffManager.deal().onAddBuff(player, player, eq.getBuff());
                    }
                }
            }
        }
    }

    @Override
    public int isHorseEquipActived(Player player, Integer id) {
        if(id == null){
            return 0;
        }
        HorseEquip horseEquip = player.getHorse().getEquips().get(id);
        if(horseEquip == null){
            return 0;
        }
        if(horseEquip.isActive()){
            return 1;
        }
        return 0;
    }
}
