package common.fud;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.data.bean.Cfg_Cross_devil_boss_Bean;
import com.data.bean.Cfg_Cross_fudi_main_Bean;
import com.game.fightroom.manager.FightManager;
import com.game.fightroom.structs.FightRoom;
import com.game.fightroom.structs.FightRoomState;
import com.game.gameserver.structs.ServerInfo;
import com.game.guildcrossfud.script.IDevilFudScript;
import com.game.guildcrossfud.struct.FudBoss;
import com.game.guildcrossfud.struct.FudCity;
import com.game.guildcrossfud.struct.FudGroup;
import com.game.guildcrossfud.struct.FudRole;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.structs.ServerType;
import com.game.utils.MessageUtils;
import com.game.zone.structs.ZoneMapDefine;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CommonMessage;
import game.message.CrossFightMessage;
import game.message.GuildCrossFudMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/4/27 20:54
 * @Auth ZUncle
 */
public class DevilFudScript implements IDevilFudScript {

    final Logger logger = LogManager.getLogger(DevilFudScript.class);

    final int cloneId = 20008;  //魔王缝隙副本ID

    /**
     * 战斗服 同步福地数据到公共服
     *
     * @param context
     * @param mess
     */
    @Override
    public void F2PCrossFudInfo(ChannelHandlerContext context, GuildCrossFudMessage.F2PCrossFudInfo mess) {

        FudGroup group = Manager.fudManager.getGroups().get(mess.getGroupId());
        if (group == null) {
            return;
        }
        FudCity city = group.getCity().get(mess.getCityId());
        if (city == null) {
            return;
        }

        //更新boss
        for (GuildCrossFudMessage.CrossFudBoss mBoss : mess.getBossList()) {
            FudBoss boss = city.getDevilBoss().get(mBoss.getBossId());
            if (boss == null) {
                continue;
            }
            boss.setHp(mBoss.getHp());
            if (mBoss.getTime() > 0) {
                boss.setRefreshTime(TimeUtils.Time() + mBoss.getTime() * 1000);
            }
        }
        logger.info("魔王缝隙副本 更新boss数据 group={} city={}", mess.getGroupId(), mess.getCityId());
    }

    /**
     * 活动开启
     */
    @Override
    public void activeBegin() {

        logger.info("魔王缝隙副本开启！！！1");

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }

                allocCityBoss(group, city);

                allocRoom(group, city);
            }
        }
    }

    //计算魔王缝隙怪物
    void allocCityBoss(FudGroup group, FudCity city) {
        Cfg_Cross_fudi_main_Bean cityBean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        for (Cfg_Cross_devil_boss_Bean bean : CfgManager.getCfg_Cross_devil_boss_Container().getValuees()) {
            if (cityBean.getPosition() != bean.getPosition()) {
                continue;
            }
            if (group.getOpenDay() >= bean.getDay().get(0)
                    && group.getOpenDay() <= bean.getDay().get(1)
                    && group.getWorldLevel() >= bean.getLevel().get(0)
                    && group.getWorldLevel() <= bean.getLevel().get(1)
            ) {
                FudBoss boss = new FudBoss();
                boss.setBossId(bean.getId());
                city.getDevilBoss().put(boss.getBossId(), boss);
            }
        }
    }

    //分配福地房间服务器
    FightRoom allocRoom(FudGroup group, FudCity city) {
        Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
        if (bean.getPosition() == 0) {
            return null;
        }
        if (city.getDevilBoss().isEmpty()) {
//            logger.error("魔王缝隙 分配失败 boss size=0 group={} city={}", group, city);
            return null;
        }
        Cfg_Clone_map_Bean map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(cloneId);
        //开始分配战斗服务器
        ServerInfo serverInfo = Manager.fightManager.deal().getFightServerId(0);
        if (serverInfo == null) {
            List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
            logger.error("没有战斗服连接在线， 请运维检查一下战斗服是否有！ 战斗服个数：" + list.size());
            return null;
        }

        FightRoom room = createRoom(group, city, serverInfo, map_bean);
        city.setDevilRoomId(room.getFid());

        List<CommonMessage.CrossAttribute> args = new ArrayList<>();
        //0=组ID 1=城市ID 2=boss数据 3=服务器阵营
        CommonMessage.CrossAttribute.Builder info = CommonMessage.CrossAttribute.newBuilder();
        info.setType(0);
        info.setParam1(group.getGroupId());
        args.add(info.build());
        CommonMessage.CrossAttribute.Builder info1 = CommonMessage.CrossAttribute.newBuilder();
        info1.setType(1);
        info1.setParam1(city.getCityId());
        args.add(info1.build());
        for (FudBoss boss : city.getDevilBoss().values()) {
            CommonMessage.CrossAttribute.Builder mBoss = CommonMessage.CrossAttribute.newBuilder();
            mBoss.setType(2);
            mBoss.setParam1(boss.getBossId());
            args.add(mBoss.build());
        }

        CrossFightMessage.P2FCreateCityMap.Builder msg = CrossFightMessage.P2FCreateCityMap.newBuilder();
        msg.setModelID(cloneId);
        msg.setRoomID(city.getDevilRoomId());
        msg.addAllMapSetList(args);
        MessageUtils.send_to_game(serverInfo.getSession(), CrossFightMessage.P2FCreateCityMap.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return room;
    }

    FightRoom createRoom(FudGroup fud, FudCity city, ServerInfo server, Cfg_Clone_map_Bean bean) {
        FightRoom room = new FightRoom();
        room.setpPlat(server.getPlatName());
        room.setServerId(server.getServerId());
        room.setServerGroupId(fud.getGroupId());
        room.setStageId(fud.getStage());
        room.setModelId(bean.getId());
        room.setCtime(TimeUtils.Time());
        room.setCrId(city.getCityId());
        room.setCname("魔王缝隙");
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
     * 活动关闭
     */
    @Override
    public void activeEnd() {

        for (FudGroup group : Manager.fudManager.getGroups().values()) {
            for (FudCity city : group.getCity().values()) {
                Cfg_Cross_fudi_main_Bean bean = CfgManager.getCfg_Cross_fudi_main_Container().getValueByKey(city.getCityId());
                if (bean.getPosition() == 0) {
                    continue;
                }
                FightRoom room = Manager.fightManager.getFrcache().get(city.getDevilRoomId());
                if (room == null) {
                    continue;
                }
                ChannelHandlerContext socket = Manager.gameServerManager.GetSession(room.getPlat(), room.getServerId());
                //通知房间关闭
                GuildCrossFudMessage.P2GCrossFudProcess.Builder message = GuildCrossFudMessage.P2GCrossFudProcess.newBuilder();
                message.setRoomId(city.getDevilRoomId());
                message.setType(2);
                MessageUtils.send_to_game(socket, GuildCrossFudMessage.P2GCrossFudProcess.MsgID.eMsgID_VALUE, message.build().toByteArray());
                //清理魔王数据
                city.setDevilRoomId(0);
                city.getDevilBoss().clear();
                logger.info("魔王缝隙活动结束 city={} ", city);
            }
        }
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.DevilFudScript;
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
}
