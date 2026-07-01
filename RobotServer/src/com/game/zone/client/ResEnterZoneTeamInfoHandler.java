package com.game.zone.client;

import game.core.command.Handler;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.ZoneMessage.ResEnterZoneTeamInfo;
import org.apache.mina.core.session.IoSession;
import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.message.RMessage;
import game.message.ZoneMessage.ReqReadyZone;
import game.message.ZoneMessage.cloneTeamInfo;

/**
 * makehandler v1.7 for netty 匹配队员情况通知
 */
public class ResEnterZoneTeamInfoHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResEnterZoneTeamInfoHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResEnterZoneTeamInfo messInfo = (ResEnterZoneTeamInfo) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            if (player.getTeamId() != messInfo.getTeamId()) {
                log.error(player.getId() + ", 收到组队同意进入的消息， 但不是自己的！");
                return;
            }

            boolean isLeader = false;
            for (cloneTeamInfo cti : messInfo.getInfolistList()) {
                if (cti.getLeader() && cti.getRoleId() == player.getId()) {
                    isLeader = true;
                    break;
                }
            }

            //自动发送同意
            if (!isLeader) {
                //1秒后再来同意
                Thread.sleep(2000);
                ReqReadyZone.Builder msg = ReqReadyZone.newBuilder();
                msg.setTeamId(messInfo.getTeamId());
                msg.setReady(true);
                player.sendMsg(ReqReadyZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResEnterZoneTeamInfoHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
