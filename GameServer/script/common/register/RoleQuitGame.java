package common.register;

import com.game.behavior.manager.BehaviorManager;
import com.game.bi.biqq.QQLogType;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapDefine;
import com.game.peak.timer.PeakCancelMatchEvent;
import com.game.peak.timer.PeakCloneEvent;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SessionAttribute;
import com.game.register.script.IQuitGame;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import game.core.message.SMessage;
import game.core.script.IScript;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCopyMapOut;
import game.message.CrossServerMessage.G2FSynPlayerOut;
import game.message.PlayerMessage;
import game.message.RegisterMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
public class RoleQuitGame implements IScript, IQuitGame {
    private static final Logger logger = LogManager.getLogger(RoleQuitGame.class);

    @Override
    public int getId() {
        return ScriptEnum.QuitGameBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void QuitGame(ChannelHandlerContext context, int reason, boolean isQuit,boolean isSendMsg) {

        if(isSendMsg) {
            sendQuitGameInfo(context, reason);
        }

        Player player = context.channel().attr(SessionAttribute.PLAYER).get();
        if (player == null) {
            Manager.registerManager.deal().tickSession(context);
            return;
        }
        logger.warn(" 开始退出游戏 player={} sessionId:{} reason={} ", player, context.channel(), reason);
        Manager.registerManager.deal().tickSession(context);
        if (isQuit) {
            logger.warn("玩家主动退出游戏！player={}", player);
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map != null && map.getType() == MapDefine.COPY_MAP) {
                Manager.copyMapManager.outZone(player);//退出副本
            }
            if (player.playerCrossData.isToFightServer()) {
                ReqCopyMapOut.Builder msg = ReqCopyMapOut.newBuilder();
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), ReqCopyMapOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
        try {
            //玩家下线BI
            boolean canRes = Manager.retrieveResManager.getScript().canRetrieveRes(player);
            Manager.biManager.getScript().biLogout(player, reason, canRes ? 1 : 0);
            Manager.biManager.getScript().biRole_info(player);

            Manager.biManager.get4399Script().updatePlayer(player);
            Manager.biManager.get4399Script().quitBiTo4399(player, reason, "");
            Manager.biManager.getQQScript().log(player, QQLogType.logout);
            QuitGame(player);

        } catch (Exception e) {
            logger.error("sessionId:" + context.channel() + "出现异常了退出了游戏。退出原因:" + reason, e);
        }
    }

    //发送退出游戏到客户端
    void sendQuitGameInfo(ChannelHandlerContext context, int reason) {
        try {
            RegisterMessage.ResQuit.Builder msg = RegisterMessage.ResQuit.newBuilder();
            msg.setReason(reason);
            ChannelFuture cf = context.writeAndFlush(new SMessage(RegisterMessage.ResQuit.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
            cf.awaitUninterruptibly(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    void QuitGame(Player player) {
        //如果账号已经退出了， 则去掉此逻辑
        if (EntityState.ExitGame.compare(player.getState())) {
            return;
        }
        player.dealOffLine();//先设置退出标记
        //如果是战斗服， 则返回， 不处理退出信息
        if (GameServer.getInstance().IsFightServer()) {
            logger.error("玩家离线的时候在战斗服---" + player.getId());
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        player.addState(EntityState.ExitGame);
        //下线清理战斗状态
        player.setFightState(0);
        player.getFightEnums().clear();
        player.clearHatred();
        player.getEnemys().clear();
        player.getBeEnemys().clear();
        player.getPklist().clear();

        BehaviorManager.CancelAllBehavior(player);

        //计算buff
        Manager.buffManager.deal().offline(player);
        //队伍下线处理
        Manager.teamManager.playerOffLine(player);
        //宠物
        Manager.petManager.offLine(player);
        //退出公会处理
        Manager.guildsManager.playerOffLine(player);
        //离线挂机处理
        Manager.playerHookManager.deal().enterOfflineHook(player);
        Manager.worldHelpManager.playerOffline(player);
        Manager.retrieveResManager.getScript().switchDay(player);

        //同乘时下线处理
        if (player.getHorse().isRideOther() || Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            Manager.horseManager.deal().onReqChangeRideState(player, 0);
        }

        Manager.leaderPreachManager.getScript().offline(player);
        Manager.registerManager.deal().writeRoleLoginLog(player);
        Manager.saveThreadManager.getSavePlayerThread().addRole(Manager.playerManager.manager().makeRoleBeanByPlayer(player));
        //保存物品数据到表中
        RoleUpdateLogService.getInstance().updateRoleItemData(player.getId());
        //保存玩家的最新数据到rolestate
        RoleUpdateLogService.getInstance().updateRoleDate(player.getId());

        //从地图退出
        Manager.mapManager.manager().onQuitMap(map, player, true);
        //同步消息到世界服
        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if (pwi != null) {
            pwi.setLastOffTime((int) (TimeUtils.Time() / 1000));
        }
        Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
        Manager.playerManager.manager().changeLoginFight(player.getId(), player.getFightPoint());

        if (player.playerCrossData.isToFightServer()) {
            //向战斗服发送玩家离线
            G2FSynPlayerOut.Builder msg = G2FSynPlayerOut.newBuilder();
            msg.setRoleId(player.getId());
            MessageUtils.send_to_public(G2FSynPlayerOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            MessageUtils.send_to_fight(player, G2FSynPlayerOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            //退出匹配
            Manager.copyMapManager.manager().onReqCancelMatch(player);
            player.playerCrossData.isReqFight = false;
            logger.error("通知已经跨服中已经离线！");
        }
        //退出巅峰竞技匹配
        Manager.peakManager.addCommand(new PeakCancelMatchEvent(player));

        //退出玩家，则删除语音房间的数据
        Manager.chatManager.deal().playerLogout(player);

        Manager.cooldownManager.cleanAllCooldown(player);
        Manager.cooldownManager.cleanAllCooldown(Manager.petManager.getBattlePet(player));

        //玩家退出仙女护送
        Manager.couplefightManager.getCoupleEscort().onLeaveGame(player);
        //同步社交服务器
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
        PlayerMessage.G2SSynPlayerSocialInfo.Builder mPlayer = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
        mPlayer.setGlobalPlayerWorldInfo(playerWorldInfo.toGlobalPlayerWorldInfo());
        mPlayer.setType(1);
        MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, mPlayer.build().toByteArray());
        //仙侣对决
        Manager.couplefightManager.getScript().playerOffline(player);
        logger.info("退出游戏完成 player:" + player);
    }

}
