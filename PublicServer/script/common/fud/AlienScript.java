package common.fud;

import com.data.CfgManager;
import com.data.MessageString;
import com.data.bean.*;
import com.game.alienboss.script.IAlienScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.structs.ServerInfo;
import com.game.guildcrossfud.struct.*;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.servermatch.manager.ServerMatchManager;
import com.game.structs.ServerType;
import com.game.structs.SessionKey;
import com.game.utils.MessageUtils;
import com.game.zone.structs.ZoneMapDefine;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.AlienBossMessage;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.GuildCrossFudMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2021/11/15 17:00
 * @Auth ZUncle
 */
public class AlienScript implements IAlienScript {

    final Logger logger = LogManager.getLogger(AlienScript.class);

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.AlienScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 活动开启
     */
    @Override
    public void activeBegin() {

        createCity();

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (AlienCity city : group.getAlien().values()) {
                if (city.getCanEnterServer().isEmpty() || city.getBossHashMap().isEmpty()) {
                    continue;
                }

                allocCityBoss(group, city);

                allocRoom(group, city);
            }
        }

    }

    /**
     * 活动关闭
     */
    @Override
    public void activeEnd() {

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (AlienCity city : group.getAlien().values()) {
                if (city.getCanEnterServer().isEmpty() || city.getBossHashMap().isEmpty()) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
                if (room == null) {
                    continue;
                }
                ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                //通知房间关闭
                GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
                message.setRoomId(city.getRoomId());
                message.setType(2);
                MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());

                logger.info("虚空幻境活动结束 city={} ", city);
            }
        }
    }

    void allocCityBoss(FudGroup group, AlienCity city) {

        ///根据开服天数和世界等级计算添Boss
        int total = 0;
        Cfg_Cross_Alien_Connect_Bean cityBean = CfgManager.getCfg_Cross_Alien_Connect_Container().getValueByKey(city.getCityId());
        for (Cfg_Cross_Alien_Boss_Bean bean : CfgManager.getCfg_Cross_Alien_Boss_Container().getValuees()) {
            if (cityBean.getType() != bean.getType()) {
                continue;
            }
            if (group.getOpenDay() >= bean.getDay().get(0)
                    && group.getOpenDay() <= bean.getDay().get(1)
                    && group.getWorldLevel() >= bean.getLevel().get(0)
                    && group.getWorldLevel() <= bean.getLevel().get(1)
            ) {
                FudBoss boss = new FudBoss();
                boss.setBossId(bean.getId());
                city.getBossHashMap().put(boss.getBossId(), boss);

                total += bean.getPoint();
            }
        }
        city.setTotalScore(total);
    }

    //分配福地房间服务器
    FightRoom allocRoom(FudGroup group, AlienCity city) {
        Cfg_Cross_Alien_Connect_Bean bean = CfgManager.getCfg_Cross_Alien_Connect_Container().getValueByKey(city.getCityId());

        Cfg_Clone_map_Bean map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(bean.getCopyId());
        //开始分配战斗服务器
        ServerInfo serverInfo = Manager.fightManager.deal().getFightServerId(0);
        if (serverInfo == null) {
            List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
            logger.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
            return null;
        }

        FightRoom room = createRoom(group, city, serverInfo, map_bean);
        city.setRoomId(room.getFid());

        List<CommonMessage.CrossAttribute> args = new ArrayList<>();
        //0=组ID 1=城市ID 2=boss数据 3=服务器
        CommonMessage.CrossAttribute.Builder mGroup = CommonMessage.CrossAttribute.newBuilder();
        mGroup.setType(0);
        mGroup.setParam1(group.getGroupId());
        args.add(mGroup.build());

        CommonMessage.CrossAttribute.Builder mCity = CommonMessage.CrossAttribute.newBuilder();
        mCity.setType(1);
        mCity.setParam1(city.getCityId());
        args.add(mCity.build());

        for (FudBoss boss : city.getBossHashMap().values()) {
            CommonMessage.CrossAttribute.Builder mBoss = CommonMessage.CrossAttribute.newBuilder();
            mBoss.setType(2);
            mBoss.setParam1(boss.getBossId());
            args.add(mBoss.build());
        }
        for (int serverId : city.getCanEnterServer().keySet()) {
            CommonMessage.CrossAttribute.Builder server = CommonMessage.CrossAttribute.newBuilder();
            server.setType(3);
            server.setParam1(serverId);
            args.add(server.build());
        }

        CrossFightMessage.P2FCreateCityMap.Builder msg = CrossFightMessage.P2FCreateCityMap.newBuilder();
        msg.setModelID(bean.getCopyId());
        msg.setRoomID(city.getRoomId());
        msg.addAllMapSetList(args);
        MessageUtils.send_to_game(serverInfo.getSession(), CrossFightMessage.P2FCreateCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return room;
    }

    FightRoom createRoom(FudGroup fud, AlienCity city, ServerInfo server, Cfg_Clone_map_Bean bean) {
        FightRoom room = new FightRoom();
        room.setpPlat(server.getPlatName());
        room.setServerId(server.getServerId());
        room.setServerGroupId(fud.getGroupId());
        room.setStageId(fud.getStage());
        room.setModelId(bean.getId());
        room.setCtime(TimeUtils.Time());
        room.setCrId(city.getCityId());
        room.setCname("混沌虚空");
        room.setFid(IDConfigUtil.getLogId());
        room.setAllReadyStart(true);
        room.setType(bean.getType());
        room.setWaitTime(room.getCtime());
        room.setEndwait(room.getCtime() + bean.getEnter_time());
        room.setFightTime(0);
        room.setAttackValue(0);
        room.setMinP(ZoneMapDefine.GM_ZONE_MAP_MIN_NUM);
        room.setRstate(FightRoomState.CREATEROOM);
        FightManager.getInstance().SaveRoomInfo(room, server.getPlatName(), server.getServerId());//保存并且写log
        return room;
    }

    /**
     * 创建虚空副本
     */
    @Override
    public void createCity() {

        Cfg_Cross_Alien_Connect_Bean[] beans = CfgManager.getCfg_Cross_Alien_Connect_Container().getValuees();

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            group.getAlien().clear();

            HashMap<Integer, Cfg_Cross_Alien_Connect_Bean> cc = new HashMap<>();
            //根据开服天数和世界等级计算开放虚空副本
            for (Cfg_Cross_Alien_Connect_Bean bean : beans) {
                AlienCity alien = new AlienCity();
                alien.setCityId(bean.getId());
                group.getAlien().put(alien.getCityId(), alien);

                for (int cityId : bean.getConnectCity().getValue()) {
                    cc.put(cityId, bean);
                }
            }
            //计算虚空城市进入权限
            for (Map.Entry<Integer, Cfg_Cross_Alien_Connect_Bean> entry : cc.entrySet()) {
                FudCity city = group.getCity().get(entry.getKey());
                AlienCity alien = group.getAlien().get(entry.getValue().getId());
                if (city == null || alien == null)
                    continue;
                FudCamp camp = group.getCamp().get(city.getCamp());
                if (camp == null)
                    continue;
                camp.getServerList().forEach(serverId -> alien.getCanEnterServer().put(serverId, 0));
            }
        }
    }

    /**
     * 进入虚空幻境
     *
     * @param context
     * @param mess
     */
    @Override
    public void G2PEnterCrossAlien(ChannelHandlerContext context, AlienBossMessage.G2PEnterCrossAlien mess) {

        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CROSS_Alien_Boss);

        CommonMessage.CrossRole role = mess.getRole();

        long dailyTime = Manager.dailyActiveManager.deal().getDailyNearlyEndTime(DailyActiveDefine.CROSS_Alien_Boss);

        if (dailyTime == 0) {
            MessageUtils.notify_player(context, role.getRoleId(), MessageString.DAILY_NOT_OPEN);
            return;
        }
        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器虚空幻境参与资格！！！role={}", role);
            return;
        }
        AlienCity city = group.getAlien().get(mess.getCityId());
        if (city == null || group.getAlienSelect().getOrDefault(role.getRoleId(), city.getCityId()) != city.getCityId()) {
            return;
        }
        //服务器是否有进入资格
        if (!city.getCanEnterServer().containsKey(role.getServerId())) {
            return;
        }
        group.getAlienSelect().put(role.getRoleId(), city.getCityId());

        FightRoom room = Manager.fightManager.getFrcache().get(city.getRoomId());
        if (room == null) {
            logger.info("虚空幻境已销毁 city={} role={}", city.getCityId(), role);
            return;
        }

        CrossFightMessage.roleAtt.Builder mRole = CrossFightMessage.roleAtt.newBuilder();
        mRole.setCampNo(role.getServerId());
        mRole.setRoleId(role.getRoleId());

        CrossFightMessage.P2GResFightStart.Builder msg = CrossFightMessage.P2GResFightStart.newBuilder();
        msg.setFightId(room.getFid());
        msg.setZoneModelId(room.getModelId());
        msg.setFightServerId(room.getServerId());
        msg.setMapModelId(room.getMapmodelId());
        msg.setOnlyJoin(true);
        msg.addRoleInfo(mRole);

        MessageUtils.send_to_game(context, CrossFightMessage.P2GResFightStart.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        logger.info("玩家进入虚空幻境 city={} role={}", city.getCityId(), role);
    }

    /**
     * 进入须弥宝库
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void G2PEnterCrossAlienGem(ChannelHandlerContext context, AlienBossMessage.G2PEnterCrossAlienGem messInfo) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CROSS_Alien_Boss);

        CommonMessage.CrossRole role = messInfo.getRole();

        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器虚空幻境参与资格！！！role={}", role);
            return;
        }
        AlienCity city = group.getAlien().get(messInfo.getCityId());
        if (city == null || group.getAlienSelect().getOrDefault(role.getRoleId(), city.getCityId()) != city.getCityId()) {
            return;
        }
        //服务器是否有进入资格
        if (!city.getCanEnterServer().containsKey(role.getServerId())) {
            return;
        }
        //是否占领
        if (city.getExtraServerId() != role.getServerId()) {
            return;
        }

        AlienBossMessage.P2FEnterCrossAlienGem.Builder message = AlienBossMessage.P2FEnterCrossAlienGem.newBuilder();

    }

    /**
     * 获取虚空幻境数据
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void G2PCrossAlienCity(ChannelHandlerContext context, AlienBossMessage.G2PCrossAlienCity messInfo) {
        String serverKey = context.channel().attr(SessionKey.SERVERPLATID).get();
        int groupID = ServerMatchManager.deal().getGroupIDForCurOpenDay(serverKey, DailyActiveDefine.CROSS_Alien_Boss);

        CommonMessage.CrossRole role = messInfo.getRole();

        FudGroup group = Manager.fudManager.getGroups().get(groupID);
        if (group == null) {
            logger.info("你所在服务器虚空幻境参与资格！！！role={}", role);
            return;
        }
        AlienCity city = group.getAlien().get(messInfo.getCityId());
        if (city == null) {
            return;
        }

        AlienBossMessage.ResCrossAlienCity.Builder message = AlienBossMessage.ResCrossAlienCity.newBuilder();
        message.setCity(builder(city));
        for (FudBoss boss : city.getBossHashMap().values()) {
            message.addBoss(builder(boss));
        }
        MessageUtils.send_to_player(context, role.getRoleId(), AlienBossMessage.ResCrossAlienCity.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    AlienBossMessage.AlienBoss.Builder builder(FudBoss boss) {
        AlienBossMessage.AlienBoss.Builder mBoss = AlienBossMessage.AlienBoss.newBuilder();
        mBoss.setBossId(boss.getBossId());
        mBoss.setHp(boss.getHp());
        mBoss.setKillServerId(boss.getKill());
        if (boss.getRefreshTime() > 0) {
            long curTime = TimeUtils.Time();
            mBoss.setTime((int) (boss.getRefreshTime() - curTime));
        } else {
            mBoss.setTime(0);
        }
        return mBoss;
    }

    AlienBossMessage.AlienCity.Builder builder(AlienCity city) {
        AlienBossMessage.AlienCity.Builder mCity = AlienBossMessage.AlienCity.newBuilder();
        mCity.setCityId(city.getCityId());
        mCity.addAllAuthEnterList(city.getCanEnterServer().keySet());
        mCity.setServerId(city.getExtraServerId());
        return mCity;
    }

    /**
     * boss数据刷新消息
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void F2PCrossAlienBoss(ChannelHandlerContext context, AlienBossMessage.F2PCrossAlienBoss messInfo) {
        FudGroup group = Manager.fudManager.getGroups().get(messInfo.getGroupId());
        if (group == null) {
            return;
        }
        AlienCity city = group.getAlien().get(messInfo.getCityId());
        if (city == null) {
            return;
        }
        if (messInfo.getRoomId() != city.getRoomId()) {
            return;
        }
        for (AlienBossMessage.AlienBoss mBoss : messInfo.getBossList()) {
            FudBoss boss = city.getBossHashMap().get(mBoss.getBossId());
            if (mBoss.getHp() > 0) {
                boss.setHp(mBoss.getHp());
            }
            if (mBoss.getHp() <= 0 && mBoss.getTime() > 0) {
                boss.setRefreshTime(mBoss.getTime());
            }
        }
    }

    /**
     * 怪物死亡消息
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void F2PCrossAlienBossDie(ChannelHandlerContext context, AlienBossMessage.F2PCrossAlienBossDie messInfo) {
        FudGroup group = Manager.fudManager.getGroups().get(messInfo.getGroupId());
        if (group == null) {
            return;
        }
        int kill = messInfo.getServerKill();
        AlienBossMessage.AlienBoss die = messInfo.getBoss();
        AlienCity city = group.getAlien().get(messInfo.getCityId());
        if (city == null) {
            return;
        }
        if (messInfo.getRoomId() != city.getRoomId()) {
            return;
        }
        if (!city.getCanEnterServer().containsKey(kill)) {
            return;
        }
        Cfg_Cross_Alien_Boss_Bean bean = CfgManager.getCfg_Cross_Alien_Boss_Container().getValueByKey(die.getBossId());
        if (bean == null) {
            return;
        }

        FudBoss boss = city.getBossHashMap().get(die.getBossId());
        boss.setHp(0);
        boss.setRefreshTime(0);
        boss.setKill(kill);
        int score = city.getCanEnterServer().get(kill);
        city.getCanEnterServer().put(kill, score + bean.getPoint());
        if ((score + bean.getPoint()) * 2 > city.getTotalScore()) {
            city.setExtraServerId(kill);
        }
        logger.info("混沌虚空sever={}添加积分{}+{} 占领={}", kill, score, bean.getPoint(), city.getExtraServerId());
    }

}
