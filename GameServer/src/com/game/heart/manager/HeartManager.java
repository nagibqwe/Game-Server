package com.game.heart.manager;

import com.game.player.structs.QuitGameDefine;
import com.game.register.manager.RegisterManager;
import com.game.register.structs.UserInfo;
import com.game.register.structs.UserState;
import com.game.heart.log.RoleKickOutLog;
import com.game.heart.script.IHeartScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.message.SMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.util.CodedUtil;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import game.message.heartMessage.ResHeart;
import game.message.heartMessage.ResHeartFailed;
import game.message.heartMessage.ResReconnectSign;
import io.netty.channel.ChannelFuture;
import java.net.InetSocketAddress;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.TimeUnit;

public class HeartManager {

    private static final Logger log = LogManager.getLogger(HeartManager.class);

    //是否封停账号
    public static volatile boolean isForbid = false;

    //断线重联sign有效最大时间
    private static final int RECONNECTMAXTIME = 10 * 60 * 1000;

    //断线重联失败原因
    private static final int RECONNECSUCCESS = 0;//验证成功
    private static final int RECONNECTFAILED_TIMEOFF = -1;//时间过期
    private static final int RECONNECTFAILED_SIGNERROR = -2;//sign错误
    private static final int RECONNECTFAILED_NOTOFFLINE = -3;//角色并未下线

    //心跳检查
    private static final int HEART_STOPSENDTIME = 3 * 60 * 1000;//3分钟客户端没有发送心跳消息就断开连接

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        HeartManager processor;

        Singleton() {
            this.processor = new HeartManager();
        }

        HeartManager getProcessor() {
            return processor;
        }
    }

    public static HeartManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //加速包检查
//    public void heartReceive(ChannelHandlerContext session, heartMessage.ReqHeart heartInfo) {
//        manager.scriptManager.call(ScriptEnum.HeartScript, "OnHeartReceive", session, heartInfo);
//    }
    /**
     * 发送加速包检查协议
     *
     * @param player
     */
    public void sendSpeedUpCheckMsg(Player player) {
        ChannelHandlerContext session = player.getIosession();
        if (session == null) {
            return;
        }
        long time = System.currentTimeMillis();
        session.channel().attr(SessionAttribute.SpeedUpCheckSendTime).set(time);
        ResHeart.Builder msg = ResHeart.newBuilder();

        if (TimeUtils.isTimeGMSet() && ServerConfig.isTestServer()) {
            time = TimeUtils.Time();
        }

        msg.setServerTime((int) (time / 1000));
        session.writeAndFlush(new SMessage(ResHeart.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
    }

    //玩家心跳检查,是否心跳停止
    public void heartCheckAll() {
        try {
            long now = System.currentTimeMillis();
            for (Map.Entry<Long, ChannelHandlerContext> entry : Manager.registerManager.getAllsessions().entrySet()) {
                ChannelHandlerContext session = (ChannelHandlerContext) entry.getValue();

                if (session.channel().attr(SessionAttribute.PLAYER).get() == null) {
                    //还未进入游戏场景
                    continue;
                }
                if (session.channel().attr(SessionAttribute.HeartSendTime).get() == null) {
                    session.channel().attr(SessionAttribute.HeartSendTime).set(now);
                    continue;
                }
                // huhu debug 心跳超时踢掉暂时先不要
                long lastSendTime = session.channel().attr(SessionAttribute.HeartSendTime).get();
                if (now - lastSendTime > HEART_STOPSENDTIME) {
                    Player player = (Player) session.channel().attr(SessionAttribute.PLAYER).get();
                    log.error(session + "-->close [because] NoHeart " + " sessionId:" + session + " roleId:" + player.getId() + " name:" + player.getName() + " 心跳超过了3分钟被踏下线了");
                    Manager.playerManager.iQuitGame().QuitGame(session, QuitGameDefine.HeartTooQuick, false, true);
                    writeKickOutLog(player, 1, " roleId:" + player.getId() + " name:" + player.getName() + " 心跳超过了3分钟被踏下线了");
                }
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //客户端返回设置sign成功
    public void setReconnectSignSuccess(Player player) {
        player.setReconnectTime(player.getLastRefreshSignTime());
    }

    //每10分钟刷新下断线重联sign值
    public void sendReconnectSignInfo(Player player) {
        long now = System.currentTimeMillis();
        if (player.getLastRefreshSignTime() != 0 && now - player.getLastRefreshSignTime() < RECONNECTMAXTIME) {
            return;
        }
        sendSingInfo(player, now);
    }

    private void sendSingInfo(Player player, long now) {
        player.setLastRefreshSignTime(now);
        ResReconnectSign.Builder msg = ResReconnectSign.newBuilder();
        String sign = calReconnectSign(player.getId(), player.getUserId(), now);
        //LOGGER.info("更细断线重联sign值 roleId:" + player.getId() + " sign:" + sign);
        msg.setSign(sign);
        MessageUtils.send_to_player(player, ResReconnectSign.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private static int AAA = 0;
    //断线重联,sign = MD5(roleId + userId + time);sign每10分钟会刷新，所以时效为10分钟
    public void reconnect(ChannelHandlerContext session, long roleId, String sign) {
        log.error("断线重联开始 roleId：" + roleId + " sign:" + sign + " session:" + session.channel());

        InetSocketAddress remoteAddress = (InetSocketAddress) session.channel().remoteAddress();
        if (remoteAddress == null) {
            String str = String.format("remoteAddress error. Session is :%s;roleId is %d", session, roleId);
            log.error(str);
            SessionUtils.closeSession(session, str);
            return;
        }
        String ip = remoteAddress.getAddress().getHostAddress();
        if (ip == null) {
            String str = String.format("getIP error. Session is :%s;roleId is %d", session, roleId);
            SessionUtils.closeSession(session, str);
            return;
        }
        Player player = Manager.playerManager.getPlayerCache(roleId);
        if (player == null) {
            String str = String.format("断线重联验证失败,获取到玩家信息失败. Session is :%s;roleId is %d", session, roleId);
            log.error(str);
            SessionUtils.closeSession(session, str);
            return;
        }

        UserInfo info = Manager.registerManager.getUserInfoByUserId(player.getUserId());
        if (info == null) {
            String str = String.format("断线重联验证失败,获取到玩家userInfo数据失败. Session is :%s;userid is %d;roleId is %d", session, player.getUserId(), roleId);
            log.error(str);
            SessionUtils.closeSession(session, str);
            return;
        }

        //判断session是否相同,如果不同,表示原来的Session已经无效了,干掉就可以了.
        String userKey = info.getUserId() + "_" + ServerConfig.getServerId();
        ChannelHandlerContext old = Manager.registerManager.getUserEnterRoleID().get(userKey);
        if (old != null && !old.equals(session)){
            Manager.registerManager.deal().replaceLogin(old,false);
        }

        //时效过期
        if (TimeUtils.Time() - player.getReconnectTime() > (RECONNECTMAXTIME + 60000)) {
            sendReconnectFailedInfo(session, player, RECONNECTFAILED_TIMEOFF);
            log.error("断线重联验证失败 玩家sign时效过期roleId：" + roleId);
            return;
        }
        //sign错误
        if (!calReconnectSign(roleId, player.getUserId(), player.getReconnectTime()).equals(sign)) {
            sendReconnectFailedInfo(session, player, RECONNECTFAILED_SIGNERROR);
            log.error("断线重联验证失败 玩家sign错误roleId：" + roleId);
            return;
        }

        session.channel().attr(SessionAttribute.USER_INFO).set(info);
        session.channel().attr(SessionAttribute.SERVER_ID).set(player.getCurServerId());
        session.channel().attr(SessionAttribute.PLATFORMNAME).set(player.getPlatformName());
        session.channel().attr(SessionAttribute.FUNCELLUUID).set(player.getUuid());
        session.channel().attr(SessionAttribute.CLIENTOS).set(player.getOs());
        session.channel().attr(SessionAttribute.MACHINECODE).set(player.getMaCode());
        session.channel().attr(SessionAttribute.PLATUSERID).set(player.getPlatUserId());
        session.channel().attr(SessionAttribute.LANGUAGE_TYPE).set(player.getLanguageType());
        session.channel().attr(SessionAttribute.IP).set(ip);
        session.channel().attr(SessionAttribute.ROLE_ID).set(roleId);
        session.channel().attr(SessionAttribute.PLAYER).set(player);//把PLAYER注册进来
        session.channel().attr(SessionAttribute.USER_STATE).set(UserState.SELECTING.getValue());


        Manager.registerManager.deal().OnReqSelectCharacter(session, roleId, true);
        sendReconnectFailedInfo(session, player, RECONNECSUCCESS);
        log.error(player.getName() + "(" + player.getId() + ") 断线续传成功了！" + session.channel());
        sendSpeedUpCheckMsg(player);
        long time = System.currentTimeMillis();
        //构建心跳处理
        session.channel().attr(SessionAttribute.SpeedUpCheckSendTime).set(time - 10000);
        sendSingInfo(player, time);//重新发心跳计时处理

    }

    //发送断线重联失败消息
    private void sendReconnectFailedInfo(ChannelHandlerContext session, Player player, int reason) {
        ResHeartFailed.Builder msg = ResHeartFailed.newBuilder();
        msg.setReason(reason);
        msg.setMapModelId(-1);
        msg.setLineId(1);
        if (reason == RECONNECSUCCESS) {
            if ( player.playerCrossData.isToFightServer()) {
                msg.setMapModelId(-1);
            } else {
                msg.setMapModelId(player.gainMapModelId());
                msg.setLineId(player.gainLine());
            }
            session.writeAndFlush(new SMessage(ResHeartFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
            log.error("断线重联验证成功roleId：" + player.getId() + ", 地图ID值=" + msg.getMapModelId());
            return;
        }
        ChannelFuture cf = session.writeAndFlush(new SMessage(ResHeartFailed.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
        cf.awaitUninterruptibly(60, TimeUnit.SECONDS);
        String str = String.format("%s-->close [because] ReconnectFailed  reason=%d", session.channel(), reason);
        log.error(str);
        SessionUtils.closeSession(session, str);
    }

    //计算断线重联需要的sign
    private String calReconnectSign(long roleId, long userId, long time) {
        return CodedUtil.Md5("" + roleId + userId + time);
    }

    //记录封停角色log
    public void writeKickOutLog(Player player, int reason, String str) {
        try {
            RoleKickOutLog roleKickOutLog = new RoleKickOutLog();
            roleKickOutLog.setPlayer(player);
            roleKickOutLog.setReason(reason);
            roleKickOutLog.setContext(str);
            LogService.getInstance().execute(roleKickOutLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public IHeartScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HeartBaseScript);
        if (is instanceof IHeartScript) {
            return (IHeartScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
