package com.game.zone.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
* 通知玩家退出位面副本
*/
public class ResCopyMapBitFinishHandler extends Handler{

    private static final Logger log = LogManager.getLogger(ResCopyMapBitFinishHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            CopyMapMessage.ResCopyMapBitFinish messInfo = (CopyMapMessage.ResCopyMapBitFinish) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            if (player == null) {
                return;
            }

            player.reqCopyMapOut();
            player.waitDoTime(1000);

            log.info("ResCopyMapBitFinish"+player.getInfo()+"收到位面副本完成消息");
            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResTeamEnterZoneISOKHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}