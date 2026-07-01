package common.cross;

import com.data.*;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_HuaxingFlySword_Bean;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.enums.ResourceType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.copymap.structs.FightRoomState;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.crossfight.script.ICrossFightScript;
import com.game.crossserver.manager.CrossServerManager;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightserver.manager.FightClientManager;
import com.game.fightserver.struct.FightClient;
import com.game.gold.structs.Gold;
import com.game.horse.structs.Horse;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.nature.structs.HuaxinEntity;
import com.game.pet.structs.Pet;
import com.game.player.manager.PlayerManager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.robot.ai.RobotAi;
import com.game.robot.struct.Robot;
import com.game.script.structs.ScriptEnum;
import com.game.server.structs.SynToFightType;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.structs.EntityState;
import com.game.task.structs.Task;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.welfare.struct.RetrieveType;
import com.google.protobuf.ByteString;
import game.core.json.TypeReference;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.util.*;
import game.message.*;
import game.message.CrossFightMessage.*;
import game.message.CrossServerMessage.F2GCloneEnterAddOne;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static game.core.map.Position.ZEROPOS;

/**
 * 战场的协议处理
 *
 * @author admin
 */
public class CrossFightScript implements ICrossFightScript {

    private static final Logger LOG = LogManager.getLogger(CrossFightScript.class);

    @Override
    public int getId() {
        return ScriptEnum.CrossFightBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //跨服战斗开始
    @Override
    public void OnP2GResFightStart(ChannelHandlerContext context, P2GResFightStart mess) {
        //检查是否有机器人
        List<Integer> robots = new ArrayList<>();
        for (roleAtt ratt : mess.getRoleInfoList()) {
            if (ratt.getIsRobot()) {
                robots.add(ratt.getCarear());
            }
        }
        int[] robot = new int[robots.size()];
        if (robot.length != 0) {
            for (int i = 0; i < robot.length; i++) {
                robot[i] = robots.get(i);
            }
        }
        for (roleAtt ratt : mess.getRoleInfoList()) {
            if (ratt.getIsRobot()) {
                continue;
            }
            long roleId = ratt.getRoleId();
            Player player = Manager.playerManager.getPlayerOnline(roleId);
            if (player == null) {
                player = Manager.playerManager.getPlayerCache(roleId);//刚离开,进行缓存， 可以让玩家上线后直接进战斗副本
            }
            if (player == null) {
                LOG.info("跨服战场已经开始了，但玩家" + roleId + "已经不在线了！");
                continue;
            }
            LOG.info("跨服战场已经开始了，玩家" + roleId + "离线，设置上线参数！");
            player.playerCrossData.setToFightServer(true);
            player.playerCrossData.isReqFight = false;//这个控制开关把它关闭
            player.playerCrossData.toFightId = mess.getFightId();
            player.playerCrossData.toFightSid = mess.getFightServerId();
            player.playerCrossData.toZoneModelId = mess.getZoneModelId();
            player.playerCrossData.crossState = CrossState.PCS_FIGHT;
            player.setMatchRobot(robot);


            //同步玩家的数据 & 掉线的玩家也同步数据
            Manager.playerManager.managerExt().OnSynPlayerInfoToFight(mess.getFightServerId(), player, mess.getFightId(), mess.getZoneModelId(), ratt, mess.getMapModelId(), mess.getLevel(), mess.getMapSetListList(), mess.getOnlyJoin());
            LOG.info("第一步：跨服战场已经开始了，但玩家" + roleId + "开始同步数据！");
        }
    }

    @Override
    public void OnF2GCloneEnterAddOne(ChannelHandlerContext context, F2GCloneEnterAddOne mess) {
        int modelid = mess.getModelId();
        List<Integer> scriptIds = mess.getScriptIdList();
        long fightId = mess.getFightId();
        for (Long roleId : mess.getRoleIdsList()) {
            LOG.info("收到跨服战斗ID：" + fightId + " , 副本ID：" + modelid + ", 脚本ID：" + scriptIds + ", 的玩家roleId：" + roleId + " 的报到次数");
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (player == null) {
                LOG.info("玩家roleId：" + roleId + " 的报到次数时，没有任何缓存数据， 不是本服的");
                continue;
            }
            if (scriptIds.contains(ScriptEnum.GodDevilWarCrossActivityScript)) {
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.GhostBattlefieldNum, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.GhostBattlefieldNum, 1);
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.GodDevilWar, 1);
                //Manager.retrieveResManager.getScript().count(player, RetrieveType.GodDevilWar);
            }
            if (scriptIds.contains(ScriptEnum.BravePeakCrossActivityScript)) {
                Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.ForbiddenAreaNum, Count.RefreshType.CountType_Forever, 1);
                Manager.controlManager.operate(player, FunctionVariable.ForbiddenAreaNum, 1);
                Manager.dailyActiveManager.deal().addDailyProgress(player, DailyActiveDefine.YZZD, 1);
                //Manager.retrieveResManager.getScript().count(player, RetrieveType.ArenaYZZD);
            }
            if (scriptIds.contains(ScriptEnum.UniverseWarActivityScript)) {
                Manager.countManager.addCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue(), Count.RefreshType.CountType_Forever, mess.getValue());
                int anger = (int) Manager.countManager.getCount(player, BaseCountType.UniverseAnger, DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR.getValue());
                MSG_UniverseMessage.ResSynAnger.Builder msg = MSG_UniverseMessage.ResSynAnger.newBuilder();
                msg.setAnger(anger);
                MessageUtils.send_to_player(player, MSG_UniverseMessage.ResSynAnger.MsgID.eMsgID_VALUE, msg.build().toByteArray());
                Manager.biManager.getScript().biEvent(player, 1, (int) mess.getValue(), anger);
                Manager.controlManager.operate(player, FunctionVariable.Kill_Guild_Territorial_Boss, 1);
            }
            if (scriptIds.contains(ScriptEnum.SoulAnimalForestCrossCloneCrossActivityScript)) {
                if(mess.getValue() > 0  ){
                    Manager.controlManager.operate(player, FunctionVariable.Kill_Cross_Boss_Num, 1);
                    Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.Kill_Cross_Boss_Num, Count.RefreshType.CountType_Forever, 1);
                    Manager.controlManager.operate(player, FunctionVariable.Kill_SoulBeast_Boss_Num, 1);
                    Manager.countManager.addCount(player, BaseCountType.Event_TIMES, FunctionVariable.Kill_SoulBeast_Boss_Num, Count.RefreshType.CountType_Forever, 1);
                }else {
                    Manager.retrieveResManager.getScript().count(player, RetrieveType.GodIsland);
                }
            }
            if (scriptIds.contains(ScriptEnum.PeakCloneScript)){
                Manager.controlManager.operate(player, FunctionVariable.ArenaTop_Win, 1);
            }
            if(scriptIds.contains(ScriptEnum.WorldBonfireActivityScript)){
                Manager.controlManager.operate(player, FunctionVariable.Join_Bon_Fire, 1);
            }
            if (scriptIds.contains(ScriptEnum.CrossHorseBossActivityScript)){
                long count =  Manager.countManager.getCount(player,BaseCountType.KaoShangLingScore_Horse_Day,DailyActiveDefine.CrossHosreBoss.getValue());
                int max = Global.HorseBoss_score_limit;
                if (count >= max){
                    return;
                }
                long add =  mess.getValue() + count > max ? max -count:mess.getValue();
                Manager.countManager.addCount(player, BaseCountType.KaoShangLingScore_Horse_Day, DailyActiveDefine.CrossHosreBoss.getValue(), Count.RefreshType.CountType_Day, add);
                Manager.countManager.addCount(player, BaseCountType.KaoShangLingScore_Horse_Total, DailyActiveDefine.CrossHosreBoss.getValue(), Count.RefreshType.CountType_Forever, add);

                //BI 犒赏令积分
                BigInteger bigChangeExp = BigInteger.valueOf(add);
                long totalCount = Manager.countManager.getCount(player,BaseCountType.KaoShangLingScore_Horse_Total,DailyActiveDefine.CrossHosreBoss.getValue());
                BigInteger beforeExp = BigInteger.valueOf(totalCount);
                BigInteger afterExp = beforeExp.add(bigChangeExp);
                //经验产出BI
                Manager.biManager.getScript().biResource(player, 1, ResourceType.KaoShangLingScore.getId(), bigChangeExp, beforeExp, afterExp, 0, 0, ItemChangeReason.KaoShangLingHorseGet, IDConfigUtil.getLogId());
            }

            LOG.info("玩家roleId：" + roleId + " 的报到次数时 + 1");
        }
    }

    @Override
    public void OnF2GCloneCDRecordAdd(ChannelHandlerContext context, CrossServerMessage.F2GCloneCDRecordAdd messInfo) {
        long fightId = messInfo.getFightId();
        String cdKey = messInfo.getCdkey();
        long userKey = messInfo.getDefinekey();
        int times = messInfo.getCdTimes();
        int cdType = messInfo.getCdType();
        int cdHour = messInfo.getCdHour();
        for (Long roleId : messInfo.getRoleIdsList()) {
            LOG.info("收到跨服战斗ID：" + fightId + " ,玩家roleId：" + roleId + " 的CD" + userKey + "(" + userKey + ")报到次数 num=" + times);
            Player player = Manager.playerManager.getPlayerCache(roleId);
            if (player == null) {
                LOG.info("玩家roleId：" + roleId + " 的报到次数时，没有任何缓存数据， 不是本服的");
                continue;
            }

            Manager.countManager.addCount(player, BaseCountType.convert(cdKey), userKey, Count.RefreshType.convert(cdType, cdHour), times);

            if (cdKey.equals(BaseCountType.SOULANIMALFORESTGATHERNUM.getValue())) {
                Manager.controlManager.operate(player, FunctionVariable.CollectionCrystal, times);
            }
            int afterValue = (int) Manager.countManager.getCount(player, BaseCountType.SOULANIMALFORESTGATHERNUM, userKey);
            if (userKey == 1) {
                Manager.biManager.getScript().biEvent(player, 3, times, afterValue);
            } else if (userKey == 2) {
                Manager.biManager.getScript().biEvent(player, 2, times, afterValue);
            }
        }
    }

    //公共服通知有玩家离开了房间！
    @Override
    public void OnP2GOutFightRoom(P2GOutFightRoom mess) {
        long roleId = mess.getRoleid();

        Player player = Manager.playerManager.getPlayerCache(roleId);
        LOG.info("玩家roleId：" + roleId + " 的离开了房间" + mess.getFightId());
        if (player == null) {
            return;
        }
        LOG.info("玩家roleId：" + roleId + " 在线， 把状态清空的离开了房间" + mess.getFightId());
        player.playerCrossData.isReqFight = false;

    }

    @Override
    public void OnReqOutFightRoom(ChannelHandlerContext context, Player player, ReqOutFightRoom mess) {
        G2PReqOutFightRoom.Builder msg = G2PReqOutFightRoom.newBuilder();
        msg.setFightId(mess.getFightId());
        msg.setRoleid(player.getId());
        MessageUtils.send_to_public(G2PReqOutFightRoom.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }


    @Override
    public void OnG2FNoticeSynRoleInfo(ChannelHandlerContext context, CrossFightMessage.G2FNoticeSynRoleInfo messInfo) {
        ByteString messData = null;
        if (messInfo.getMsgId() > 0)
            messData = messInfo.getMessData();
        Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (player == null) {//角色不在线
            return;
        }
        HashMap<Integer, Object> valueMap = null;
        if (messInfo.hasValue()) {
            valueMap = JsonUtils.parseObject(messInfo.getValue(), new TypeReference<HashMap<Integer, Object>>() {
            });
        }
        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        if (map == null) {
            LOG.error(player.nameIdString() + " 发送公告的时候 ， 地图已经不存在了！");
            return;
        }

        switch (messInfo.getType()) {
            case SynToFightType.WingChange://翅膀外观切换广播
                if (valueMap != null && !valueMap.isEmpty()) {
                    player.getWing().setCurrentId((int) valueMap.get(1));
                }
                MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray(), true);
//                FightClientManager.GetInstance().send_to_players(new ArrayList<>(map.getPlayers()), messInfo.getMsgId(), messData.toByteArray(), map.getId());
                break;
            case SynToFightType.HorseChange://坐骑外观切换广播
                if (valueMap != null && !valueMap.isEmpty()) {
                    player.getHorse().setCurLayer((int) valueMap.get(1));
                    int state = (int) valueMap.get(2);
                    int speed = (int) valueMap.get(3);
                    player.getHorse().setRideState(HorseRideStateEnum.getStateEnumByInt(state));
                    player.getAttribute().setAttribute(AttributeType.ATTR_Speed, speed);
                }
                MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray(), true);
//                FightClientManager.GetInstance().send_to_players(new ArrayList<>(map.getPlayers()), messInfo.getMsgId(), messData.toByteArray(), map.getid);

                break;
            case SynToFightType.FashionChange://时装切换广播
                if (valueMap != null && !valueMap.isEmpty()) {
                    if (valueMap.get(1) != null) {
                        player.getNewFashionData().setBodyID((int) valueMap.get(1));
                    } else if (valueMap.get(2) != null) {
                        player.getNewFashionData().setWeaponID((int) valueMap.get(2));
                    }
                }
                MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray(), true);
//                FightClientManager.GetInstance().send_to_players(new ArrayList<>(map.getPlayers()), messInfo.getMsgId(), messData.toByteArray());
                break;
            case SynToFightType.PetLvChange://宠物升级广播
            {
                if (valueMap == null || valueMap.isEmpty()) {
                    return;
                }

                int level = (int) valueMap.get(1);
                int petmodelId = (int) valueMap.get(3);
                int oldf = player.getActivePet().getFightPet();
                player.getActivePet().setFightPet(petmodelId);
                Pet pet = player.getActivePet().getPets().get(petmodelId);
                if (pet == null) {
                    return;
                }
                boolean isEnter = false;
                if (pet.getLevel() != level) {
                    isEnter = true;
                }

                if (oldf != petmodelId) {
                    isEnter = true;
                }

                if (isEnter) {
                    Manager.mapManager.manager().onEnterMap(pet);
                }
                MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray(), true);

            }
            break;
            case SynToFightType.TitleChange:
                if (!Manager.universeManager.deal().canOptTitle(player)) {
                    return;
                }
                if (valueMap != null && !valueMap.isEmpty()) {
                    if (valueMap.get(1) != null) {
                        player.getTitleData().setWearId((int) valueMap.get(1));
                    }
                }
                MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray());
                break;
            case SynToFightType.SkillChange:
                if (valueMap != null && !valueMap.isEmpty()) {
                    int type = (int) valueMap.get(1);
                    int skillID = (int) valueMap.get(2);
                    if (type == 1) {
                        player.getSkills().remove(skillID);
                    } else {
                        Skill skill = new Skill();
                        skill.setSkillId(skillID);
                        skill.setLevel(1);
                        player.getSkills().put(skillID, skill);
                    }
                }
                MessageUtils.send_to_player(player, messInfo.getMsgId(), messData.toByteArray());
                break;
            case SynToFightType.FightPointChange:
                if (valueMap != null && !valueMap.isEmpty()) {
                    long newFightPon = (long) valueMap.get(1);
                    player.setFightPoint(newFightPon);
                    String attstring = (String) valueMap.get(2);
                    Manager.crossServerManager.ParseAttCals(player, attstring);
                }
            case SynToFightType.HuaxingFlySwordChange:
                if (valueMap != null && !valueMap.isEmpty()) {
                    int exID = (int) valueMap.get(1);
                    int skillID = (int) valueMap.get(2);
                    long huaxingID = (long) valueMap.get(3);

                    HuaxinEntity huaxinEntity = null;
                    if (player.getCurHuaxinEntity() == null) {
                        huaxinEntity = new HuaxinEntity();
                        player.setCurHuaxinEntity(huaxinEntity);
                    } else {
                        huaxinEntity = player.getCurHuaxinEntity();
                    }

                    Cfg_HuaxingFlySword_Bean newSkillBean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(skillID);
                    if (newSkillBean == null) {
                        LOG.info("Cfg_HuaxingFlySword_Bean  == null  " + skillID);
                        return;
                    }
                    if (player.getFlyswordAllInfo().getCurFlySwordSkillId() != skillID) {
                        ConcurrentHashMap<Integer, Skill> baseSkills = huaxinEntity.getBaseSkills();
                        Skill skill;
                        if (!baseSkills.containsKey(newSkillBean.getUse_Skill())){
                            skill = new Skill();
                            skill.setSkillId(newSkillBean.getUse_Skill());
                            baseSkills.put(newSkillBean.getUse_Skill(), skill);
                        }
                        if (newSkillBean.getNormal_Skill() > 0){
                            if (!baseSkills.containsKey(newSkillBean.getNormal_Skill())){
                                skill = new Skill();
                                skill.setSkillId(newSkillBean.getNormal_Skill());
                                baseSkills.put(newSkillBean.getNormal_Skill(), skill);
                            }
                        }
                    }
                    huaxinEntity.setExcelId(exID);
                    huaxinEntity.setOwnerId(player.getId());
                    huaxinEntity.setId(huaxingID);
                    player.getFlyswordAllInfo().setCurUseFlySwordId(exID);
                    player.getFlyswordAllInfo().setCurFlySwordSkillId(skillID);
                    MessageUtils.send_to_roundPlayer(player, messInfo.getMsgId(), messData.toByteArray());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void OnF2GPlayerOutCrossWorldMap(ChannelHandlerContext context, CrossFightMessage.F2GPlayerOutCrossWorldMap messInfo) {
        //跨服过来的地图切换命令处理
        long roleId = messInfo.getRoleId();

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            //玩家已经不在线了
            player = Manager.playerManager.getPlayerCache(roleId);
            if (player != null) {
                player.playerCrossData.setToFightServer(false);//清除跨服标识
                player.playerCrossData.toFightSid = 0;
                player.playerCrossData.toZoneModelId = 0;
                player.playerCrossData.toFightId = 0;
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            }
            //返回上级地图
            Manager.copyMapManager.outZone(player);
            return;
        }

        int tomapId = messInfo.getToMapId();

        Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(tomapId);

        if (bean == null) {
            return;
        }
        //如果目标还是跨服
        if (bean.getType() == MapDefine.CROSS_WORLD_MAP) {
            //通知进入跨服野图
            if (ConnectFightManager.GetInstance().getMapIdInFight().containsKey(tomapId)) {
                int fid = ConnectFightManager.GetInstance().getMapIdInFight().get(tomapId);
                CrossFightMessage.roleAtt.Builder ratt = CrossFightMessage.roleAtt.newBuilder();
                ratt.setRoleId(roleId);
                if (Manager.playerManager.managerExt().OnSynPlayerInfoToFight(fid, player, 0, 0, ratt.build(), tomapId, 0, new ArrayList<>(), false)) {
                    return;
                }
            }
            //同步切换结果
            MapUtils.sendChangeMap(player, 0, 0, ZEROPOS, MapDefine.CHANGE_MAP_RESULT_FAILED_CROSS_MAP_NOT_SERVER, -1, -1);
            return;
        }

        //检查跨服状态，并去掉标志
        player.playerCrossData.setToFightServer(false);//清除跨服标识
        player.playerCrossData.toFightSid = 0;
        player.playerCrossData.toZoneModelId = 0;
        player.playerCrossData.toFightId = 0;
        player.playerCrossData.crossState = CrossState.PCS_LOCAL;

        //从休息室里面离开，回到目标要去的地图
        Manager.mapManager.transport().moveToOtherWorldMap(player, tomapId, MapManager.getPos(messInfo.getTox(), messInfo.getToy()), messInfo.getTransId());
    }


    @Override
    public void OnG2FSynPowerAttAndFace(ChannelHandlerContext context, CrossFightMessage.G2FSynPowerAttAndFace messInfo) {

        Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (player == null) {//角色不在线
            return;
        }
        player.setFightPoint(messInfo.getFightPower());
        Manager.crossServerManager.ParseAttCals(player, messInfo.getAttlist());
    }

    /**
     * 从game发到fight的玩家数据同步
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FSynPlayerInfo(ChannelHandlerContext context, G2FSynPlayerInfo mess) {
        int state = 0;
        String data = mess.getPlayerData();
        try {
            FightClient fc = context.channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).get();
            if (fc != null) {
                Player player = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(data), Player.class);
                player.setIsOnline((byte) 1);//在线
                player.dealOnLine();
                player.changeMapId(0);
                player.changeMapModelId(0);
                player.changeLine(0);
                player.setGuildId(mess.getGuildId());
                //设置玩家的服务器id防止切换场景时bi中的服务器id被置零
                player.setCreateServerID(player.getBi().getServer_id());
                player.setLoginIP(player.getBi().getUser_ip());

                String newName = mess.getRoleName();

                if (player.getGold() == null) {
                    player.setGold(new Gold());
                }
                player.playerCrossData.fightCampNo = mess.getCross().getCampNo();
                player.playerCrossData.platSid = fc.getPlat() + "_" + fc.getSid();
                player.playerCrossData.toFightId = mess.getFightId();//设置战斗ID值
                Manager.registerManager.addRoleName(mess.getRoleId(), newName);
                Manager.registerManager.deal().writeRoleLoginLog(player);
                Manager.playerManager.cachePlayer(player);

                //设置坐骑为下马状态
                Horse horse = player.getHorse();
                if (horse != null) {
                    horse.setRideState(HorseRideStateEnum.UnRide);
                }
                player.getEnemys().clear();
                player.getBeEnemys().clear();
                Manager.crossServerManager.ParseAttCals(player, mess.getAttlist());
                player.getAttribute().calFinalAttackSpeed();
                player.getAttribute().calFinalMoveSpeed();
                player.setCurHp(player.getAttribute().MaxHP());
                player.removeSate(EntityState.Dead);
                player.resetState();
                player.getCooldowns().clear();
                player.setIosession(context);//设置玩家的连接点来源
                player.setBrithProtect(TimeUtils.Time() + 8000);
                player.onHpChange(null);
                Pet pet = Manager.petManager.getBattlePet(player);
                if (pet != null) {
                    Manager.petManager.deal().calPetAttribute(pet);
                }
                //进入跨服初始化 飞剑
                initCrossFlySword(player, mess.getMapSetListList());
                LOG.info("传输玩家" + mess.getRoleId() + " (" + mess.getRoleName() + ")数据成功，玩家数据：" + "-----    len=" + data.length() + "玩家的移动速度：" + player.getAttribute().gainFinalMoveSpeed());
                if (TimeUtils.isIDEEnvironment()) {
                    MessageUtils.notify_player(player, Notify.CHAT, MessageString.SynDataTofight);
                }
            } else {
                state = 2;
            }
        } catch (Exception e) {
            LOG.error(e, e);
            if (TimeUtils.isIDEEnvironment()) {
                File no = new File("player" + mess.getRoleId() + ".properties");
                try {
                    Utils.write(data, no);
                } catch (IOException ex) {
                    LOG.error(ex, ex);
                }
            }
            LOG.error("传输玩家" + mess.getRoleId() + " (" + mess.getRoleName() + ")数据出错，玩家数据：" + "-----    len=" + data.length());
            state = 1;
        }
        F2GSynPlayerInfoResult.Builder msg = F2GSynPlayerInfoResult.newBuilder();
        msg.setRoleId(mess.getRoleId());
        msg.setState(state);
        msg.setFid(ServerConfig.getServerId());
        msg.setZoneModelId(mess.getZoneModelId());
        msg.setZoneLevel(mess.getZoneLevel());
        msg.setFightId(mess.getFightId());
        msg.setCross(mess.getCross());
        msg.setMapModelId(mess.getMapModelId());
        msg.setOnlyJoin(mess.getOnlyJoin());
        msg.addAllMapSetList(mess.getMapSetListList());
        FightClientManager.GetInstance().send_to_game(context, F2GSynPlayerInfoResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    private void initCrossFlySword(Player player, List<CommonMessage.CrossAttribute> cloneAtt) {

        if (player.getFlyswordAllInfo().getFlyswordDataMap().size() <= 0)
            return;
        if (player.getFlyswordAllInfo().getCurUseFlySwordId() <= 0)
            return;
        if (player.getFlyswordAllInfo().getCurFlySwordSkillId() <= 0)
            return;
        Cfg_HuaxingFlySword_Bean bean = CfgManager.getCfg_HuaxingFlySword_Container().getValueByKey(player.getFlyswordAllInfo().getCurFlySwordSkillId());
        if (bean == null)
            return;

        long hxUid = 0L;
        for (CommonMessage.CrossAttribute huaxin : cloneAtt) {
            if (huaxin.getType() == player.getFlyswordAllInfo().getCurUseFlySwordId()) {
                hxUid = huaxin.getValue();
                break;
            }
        }
        //初始化当前使用技能
        HuaxinEntity huaxinEntity = new HuaxinEntity();
        huaxinEntity.setId(hxUid);
        huaxinEntity.setExcelId(player.getFlyswordAllInfo().getCurUseFlySwordId());
        huaxinEntity.setOwnerId(player.getId());
        Skill skill = new Skill();
        skill.setSkillId(bean.getUse_Skill());
        huaxinEntity.getBaseSkills().put(bean.getUse_Skill(), skill);
        if (bean.getNormal_Skill() > 0) {
            skill = new Skill();
            skill.setSkillId(bean.getNormal_Skill());
            huaxinEntity.getBaseSkills().put(bean.getNormal_Skill(), skill);
        }
        player.setCurHuaxinEntity(huaxinEntity);

        //跨服进入也要初始化
        Manager.huaxinFlySwordManager.deal().onlieInitflySwordSkill(player);
    }

    @Override
    public void OnP2GCheckCrossInfoRes(ChannelHandlerContext context, CrossFightMessage.P2GCheckCrossInfoRes messInfo) {
        Player player = Manager.playerManager.getPlayerOnline(messInfo.getRoleId());
        if (player == null) {
            LOG.info(messInfo.getRoleId() + "进已经不在线了， 请求进入跨服时！");
            player = Manager.playerManager.getPlayerCache(messInfo.getRoleId());
            if (player != null) {
                player.playerCrossData.toFightId = 0;
                player.playerCrossData.isReqFight = false;
                player.playerCrossData.setToFightServer(false);
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            }
            return;
        }
        if (messInfo.getIsCanEnter()) {
            //可以进跨服的
            G2FEnterCloneMap.Builder crossmsg = G2FEnterCloneMap.newBuilder();
            crossmsg.setCloneId(messInfo.getCloneID());
            crossmsg.setRoomId(player.playerCrossData.toFightId);
            crossmsg.setRoleId(player.getId());
            crossmsg.setCross(messInfo.getCross());
            crossmsg.setOnlyJoin(true);
            crossmsg.addAllMapSetList(messInfo.getMapSetListList());
            ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FEnterCloneMap.MsgID.eMsgID_VALUE, crossmsg.build().toByteArray());
            LOG.info(player.nameIdString() + "玩家断线续传进入跨服请求！");
        } else {
            LOG.info(player.nameIdString() + "玩家断线续传进入跨服请求！ 跨服已经失败了， 不能进入了， 进本地地图！");
            //跨服副本结算的回调用原来流程
            player.playerCrossData.toFightId = 0;
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            MapObject unknowMap = Manager.mapManager.getMap(player.gainMapId());
            if (unknowMap == null) {
                LOG.info("跨服返回，跨服休息室世界地图不见了");
            } else {
                Manager.mapManager.manager().clearPlayer(unknowMap, player);
            }
            Manager.mapManager.changeMap(player, player.getOld().getModelId(), player.getOld().getPos(), -1, true);
            //如果是重新登录，则返回本地地图
            // if (EntityState.LoginGame.compare(player.getState())) {
            //     MapUtils.sendPlayerLoadingMapID(player,player.gainMapModelId(),player.gainLine(),player.getId(),player.gainCurPos());
            // } else {
//       //   ScriptManager.getInstance().call(ScriptEnum.Enter_Game, player);
            //     Manager.mapManager.manager().onEnterMap(player, Manager.mapManager.getMap(player.gainMapId()), player.gainCurPos());
            // }
        }
    }

    /**
     * 同步玩家角色的返回结果
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GSynPlayerInfoResult(ChannelHandlerContext context, F2GSynPlayerInfoResult mess) {
        long roleId = mess.getRoleId();
        LOG.info("第二步：跨服战场已经开始了，玩家" + roleId + "数据同步完成！");

        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            LOG.info("第二步：跨服战场已经失败，玩家" + roleId + "在这期间掉线！");
            player = Manager.playerManager.getPlayerCache(roleId);
            if (player != null) {
                player.playerCrossData.toFightId = mess.getFightId();
                player.playerCrossData.toFightSid = mess.getFid();
                player.playerCrossData.toZoneModelId = mess.getZoneModelId();
                actionTask(player, mess.getZoneModelId());
                return;
            }
            return;
        }
        //商量一下怎么切换流程
        player.playerCrossData.toFightId = mess.getFightId();
        player.playerCrossData.toFightSid = mess.getFid();
        player.playerCrossData.toZoneModelId = mess.getZoneModelId();

        //开始向战斗服注册战斗战场初始化及切换场景
        G2FEnterCloneMap.Builder msg = G2FEnterCloneMap.newBuilder();
        msg.setRoleId(roleId);
        msg.setCloneId(mess.getZoneModelId());
        msg.setZoneLevel(mess.getZoneLevel());
        msg.setRoomId(mess.getFightId());
        msg.setCross(mess.getCross());
        msg.setOnlyJoin(mess.getOnlyJoin());
        msg.addAllMapSetList(mess.getMapSetListList());
        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, -1, G2FEnterCloneMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        actionTask(player, mess.getZoneModelId());
        LOG.info("第二步：跨服同步数据完成，玩家" + player.getName() + "_" + roleId + "数据同步完成！");
    }

    private void actionTask(Player player, int modelId) {
        try {
            Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
            if (bean == null) {
                return;
            }
            int type = -1;
            if (type > 0) {
                Manager.taskManager.deal().action(player, Task.ACTION_TYPE_FUNCTION, type, 1);
            }
        } catch (Exception e) {
            LOG.error(e, e);
        }
    }

    public void P2FCreateCityMap(ChannelHandlerContext context, P2FCreateCityMap mess) {
        MapObject mapObject = Manager.mapManager.getMap(mess.getRoomID());
        if (mapObject == null) {
            mapObject = Manager.mapManager.createCopyMap(mess.getModelID(), 1, MapManager.CopyMapOwnerSystemId, mess.getRoomID(), mess.getMapSetListList());
            Manager.crossServerManager.getCrossServer().SendFightStateToPublic(mapObject.getId(), FightRoomState.FIGHT_WAIT);
        }
    }

    /**
     * 机器人跨服助战
     */
    public void G2FSynRobotInfoToHelpBattle(ChannelHandlerContext context, G2FSynRobotInfoToHelpBattle mess) {
        Player player = PlayerManager.getInstance().getPlayerCache(mess.getRoleId());
        if (player == null) {
            LOG.info("玩家不在跨服");
            return;
        }
        MapObject mapObject = Manager.mapManager.getMap(player.gainMapId());
        if (mapObject == null) {
            LOG.info("玩家不再地图里面");
            return;
        }
        try {
            Robot robot = JsonUtils.parseObject(VersionUpdateUtil.dataLoad(mess.getRobotData()), Robot.class);
            robot.setAi(RobotAi.Help);
            CrossServerManager.getInstance().ParseAttCals(robot, mess.getAttlist());
            robot.setCamp(player.getCamp());
            robot.getAttribute().setAttribute(AttributeType.ATTR_Speed, Global.MoveSpeed);
            robot.getAttribute().calFinalMoveSpeed();
            robot.getAttribute().gainFinalMoveSpeed();
            MapGpsUtil.CopyGPS(player.getCurGps(), robot.getCurGps());
            // 随机坐标
            Position randomPos = Utils.getRandomPos(mapObject, robot.getCurGps().getPos(), 5);
            robot.getCurGps().setPos(randomPos);
            Manager.mapManager.manager().onEnterMap(robot);
        } catch (Exception e) {
            LOG.error(e);
        }

    }

    @Override
    public void sendF2GSynRoleFVInfo(Player player, int fvType, int fvValue, int type, long con, int resetType, long value) {
        CrossFightMessage.F2GSynRoleFVInfo.Builder msg = CrossFightMessage.F2GSynRoleFVInfo.newBuilder();
        msg.setRoleId(player.getId());
        msg.setFvType(fvType);
        msg.setFvValue(fvValue);
        msg.setType(type);
        msg.setCon(con);
        msg.setResetType(resetType);
        msg.setValue(value);
        FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossFightMessage.F2GSynRoleFVInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onF2GSynRoleFVInfo(ChannelHandlerContext context, F2GSynRoleFVInfo mess) {
        long roleId = mess.getRoleId();
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            return;
        }
        if(mess.getFvType() == FunctionVariable.KillfudiBoss_type){
            Manager.controlManager.operate(player, mess.getFvType(), mess.getType(), mess.getFvValue());
            return;
        }else if(mess.getFvType() == FunctionVariable.TypeBoss){
            Manager.countManager.addCount(player, BaseCountType.convert(String.valueOf(mess.getType())), mess.getCon(), Count.RefreshType.convert(mess.getResetType()), mess.getValue());
            Manager.controlManager.operate(player, mess.getFvType(), (int)mess.getCon(), mess.getFvValue());
            return;
        }
        Manager.countManager.addCount(player, BaseCountType.convert(String.valueOf(mess.getType())), mess.getCon(), Count.RefreshType.convert(mess.getResetType()), mess.getValue());
        Manager.controlManager.operate(player, mess.getFvType(), mess.getFvValue());
    }

    @Override
    public void closeMap(long mapId) {
        MapObject map = Manager.mapManager.getMap(mapId);
        Manager.mapManager.deleteMap(map);
    }


    @Override
    public void sendF2GSendPersonalNotice(Player player,ChatMessage.PersonalNotice.Builder personalNotice) {
        CrossServerMessage.F2GSendPersonalNotice.Builder msg =  CrossServerMessage.F2GSendPersonalNotice.newBuilder();
        msg.setType(personalNotice.getType());
        msg.setContent(personalNotice.getContent());
        msg.addAllValue(personalNotice.getValueList());
        msg.addAllChatChannelList(personalNotice.getChatChannelListList());

        FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossServerMessage.F2GSendPersonalNotice.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
