/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.zone;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.fightroom.structs.FightRoom;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.structs.ServerType;
import com.game.utils.MessageUtils;
import com.game.zone.manager.ZoneManager;
import com.game.zone.script.IZoneHandlerScript;
import com.game.zone.structs.CopyMapType;
import com.game.zone.structs.TeamPlayerInfo;
import com.game.zone.structs.ZoneTeam;
import game.message.BravePeakMessage;
import game.message.ZoneMessage;
import game.message.ZoneMessage.P2GReqCancelCrossTag;
import game.message.ZoneMessage.P2GResEnterZone;
import game.message.ZoneMessage.cloneTeamInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 跨服进入的处理协议集合
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class ZoneDealHandlerScript implements IZoneHandlerScript {

    private static final Logger LOG = LogManager.getLogger("ZoneDealHandlerScript");

    @Override
    public void OnG2PReqCrossZoneReadyZone(ChannelHandlerContext context, ZoneMessage.G2PReqCrossZoneReadyZone mess) {
        boolean isReady = mess.getReady();
        long roleId = mess.getRoleId();
        FightRoom fr = Manager.fightManager.GetRoomByFightId(mess.getTeamId());
        if (fr == null) {
            LOG.error("玩家同意的时候，房间已经不存在了RoleID = " + roleId);
            return;
        }
        if (fr.isAllReadyStart()) {
            return;
        }
        if (!isReady) {//这个玩家不同意进入
            //这个房间的人全部结束

            ZoneTeam zt = searchRoleTeam(fr.getTeam(), roleId);

//            if (zt == null) {
//                LOG.error("玩家同意的时候，没有相应的队伍信息RoleID = " + roleId);
//                return;
//            }
            //移除队伍匹配，别的队伍从新加入匹配
            TeamPlayerInfo info = zt.getPlist().get(roleId);
            Set<ZoneTeam> zts = new HashSet<>(fr.getTeam());
            for (ZoneTeam z : zts) {
                cancelMatch(info.getRoleId(), info.getName(), z, 2);
            }
//            if (zts.remove(zt)) {
//                if (zts.size() > 0) {
//                    //加入匹配
//                    int modeId = fr.getModelId();
//                    for (ZoneTeam z : zts) {
//                        ZoneManager.put(modeId, z);
//                    }
//                }
//            }
            //删除创建的房间
            ZoneManager.countdown.remove(fr.getFid());
            Manager.fightManager.getFrcache().remove(fr.getFid());
            //发送回GameServer取消跨服标志
//            cancelMatch(roleId, zt, 2);
        } else {
            //同意了，是不是最后一个
            Set<ZoneTeam> zts = fr.getTeam();
            agreed(zts, roleId);
            for (ZoneTeam zt : zts) {
                for (TeamPlayerInfo info : zt.getPlist().values()) {
                    if (!info.isReady()) {
                        try {
                            //还有没准备好的
                            //通知玩家
                            Manager.zoneManager.manager().sendTeamInfo(fr);
                            return;
                        } catch (Exception ex) {
                            LOG.error(ex, ex);
                        }
                    }
                }
            }
            //全部准备好了进入副本
            List<ZoneTeam> list = new ArrayList<>(fr.getTeam());
            try {
                for (ZoneTeam zt : list) {
                    Manager.zoneManager.deleteRoleKey(zt);
                }
                ZoneManager.countdown.remove(fr.getFid());
                fr.setAllReadyStart(true);//准备好了
                Manager.fightManager.deal().fightStart(fr, list);
            } catch (Exception ex) {
                LOG.error(ex, ex);
            }
        }
    }

    //同意放入
    private void agreed(Set<ZoneTeam> set, long roleId) {
        for (ZoneTeam zt : set) {
            if (zt.getPlist().containsKey(roleId)) {
                zt.getPlist().get(roleId).setReady(true);
            }
        }
    }

    //搜索玩家在那个队伍里
    private ZoneTeam searchRoleTeam(Set<ZoneTeam> set, long roleId) {
        for (ZoneTeam zt : set) {
            if (zt.getPlist().containsKey(roleId)) {
                return zt;
            }
        }
        return null;
    }

    //玩家取消跨服
    @Override
    public void OnG2PReqCancelMatch(ChannelHandlerContext context, ZoneMessage.G2PReqCancelMatch mess) {
        cancelMatch(mess.getRoleId(), mess.getName(), null, 1);
    }

    @Override
    public void cancelMatch(long roleId, String name, ZoneTeam zt, int type) {
        if (zt == null) {
            zt = Manager.zoneManager.getAndDelete(roleId);
        }
        if (zt != null) {
            sendCancelMatchInfo(zt, roleId, type, name);
            //清除玩家Key及队列
            Manager.zoneManager.deleteRoleKey(zt);
        }
    }

    //发送取消玩家服的标记
    private void sendCancelMatchInfo(ZoneTeam zt, long roleId, int type, String name) {
        P2GReqCancelCrossTag.Builder msg = P2GReqCancelCrossTag.newBuilder();
        msg.setCulpritId(roleId);
        msg.setType(type);
        msg.setRoleName(name);
        for (TeamPlayerInfo info : zt.getPlist().values()) {
            try {
                msg.addInfolist(Manager.zoneManager.manager().buildTeamInfo(info));
            } catch (Exception ex) {
                LOG.error(ex, ex);
            }
        }

        ChannelHandlerContext socket = Manager.gameServerManager.GetSession(zt.getPlat(), zt.getsId());
        MessageUtils.send_to_game(socket, ZoneMessage.P2GReqCancelCrossTag.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void OnG2PReqEnterZone(ChannelHandlerContext context, ZoneMessage.G2PReqEnterZone mess) {
        int modelId = mess.getModelId();
        List<Long> rids = new ArrayList<>();
        long createRoleId = mess.getRoleId();
        for (cloneTeamInfo cti : mess.getListList()) {
            rids.add(cti.getRoleId());
            if (cti.getLeader()) {
                createRoleId = cti.getRoleId();
            }
        }
        //检查有没有战斗服
        List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        if (list.size() < 1) {
            LOG.error("没有收到有战斗服的注册信息！");
            sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 1, mess.getEnterType());//创建失败了
            return;
        }
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(modelId);
        if (bean == null) {
            sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 1, mess.getEnterType());//创建失败了
            return;
        }

        ZoneTeam zt = new ZoneTeam();
        zt.setPlat(mess.getPlat());
        zt.setsId(mess.getSid());
        zt.setBirthGroup(mess.getBirthGroup());

        TeamPlayerInfo tpi;
        for (cloneTeamInfo cti : mess.getListList()) {
            tpi = new TeamPlayerInfo();
            tpi.setCareer(cti.getCarear());
            tpi.setLeader(cti.getLeader());
            tpi.setName(cti.getRoleName());
            tpi.setReady(cti.getReady());
            tpi.setRoleId(cti.getRoleId());
            tpi.setServerId(cti.getServerId());
            tpi.setF(cti.getPower());
            tpi.setLv(cti.getLevel());
            zt.getPlist().put(cti.getRoleId(), tpi);
        }

        switch (bean.getType()) {
            case CopyMapType.House_CopyMap:
                Manager.fightManager.deal().enterHouseMap(context,  mess.getModelId(), mess.getLevel(), mess.getRoleId(), zt, bean);
                break;
            case CopyMapType.GodDevilWar_CopyMap:
                if (!Manager.godDevilWarManager.deal().enterFightRoom(context,zt, bean)) {
                    sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 2, mess.getEnterType());//创建房间失败了
                    return;
                }
                break;
            case CopyMapType.Brave_CopyMap:
                if (!Manager.bravePeakManager.deal().enterBravePeakRoom(context, zt, mess.getModelId(), bean)) {
                    sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 2, mess.getEnterType());//创建房间失败了
                    return;
                }
                break;
            case CopyMapType.MonsterSoul_CopyMap:
                long fightId = Manager.fightManager.deal().EnterSoulAnimalForestClone(context, zt, modelId, bean);
                if (fightId < 1) {
                    sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 7, mess.getEnterType());
                    return;
                }
                break;
            default: {
                sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 6, mess.getEnterType());
            }
            return;
        }
        sendFailureEnterZone(context, mess.getModelId(), createRoleId, rids, 0, mess.getEnterType());
    }

    private void sendFailureEnterZone(ChannelHandlerContext context, int modelId, long roleId, List<Long> roleids, int state, int type) {
        P2GResEnterZone.Builder msg = P2GResEnterZone.newBuilder();
        msg.setModelId(modelId);
        msg.setRoleId(roleId);
        msg.addAllRoleIds(roleids);
        msg.setState(state);
        msg.setEnterType(type);
        MessageUtils.send_to_game(context, P2GResEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public int getId() {
        return ScriptEnum.ZoneDealHandlerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    /**
     * 游戏服过来获取玩家勇者巅峰信息
     *
     * @param context
     * @param mess
     */
    @Override
    public void onG2PGetPlayerBravePeakInfo(ChannelHandlerContext context, BravePeakMessage.G2PGetPlayerBravePeakInfo mess) {
        int floor = 1;
        boolean success = false;
        if (Manager.zoneManager.getBravePeakInfo().getMaxFloor().containsKey(mess.getRoleId())) {
            floor = Manager.zoneManager.getBravePeakInfo().getMaxFloor().get(mess.getRoleId()).getFloor();
            success = Manager.zoneManager.getBravePeakInfo().getMaxFloor().get(mess.getRoleId()).getSuccess() > 0;
        }
        BravePeakMessage.P2GPlayerBravePeakInfoResult.Builder msg = BravePeakMessage.P2GPlayerBravePeakInfoResult.newBuilder();
        msg.setFloor(floor);
        msg.setRoleId(mess.getRoleId());
        msg.setSuccess(success);
        MessageUtils.send_to_game(context, BravePeakMessage.P2GPlayerBravePeakInfoResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

}
