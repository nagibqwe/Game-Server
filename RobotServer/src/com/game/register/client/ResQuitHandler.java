package com.game.register.client;

import com.game.player.structs.Player;
import com.game.structs.SessionAttribute;
import game.core.command.Handler;
import game.core.message.RMessage;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ResQuit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.mina.core.session.IoSession;

/**
 * makehandler v1.5 退出游戏
 */
public class ResQuitHandler extends Handler {

    private static final Logger log = LogManager.getLogger(ResQuitHandler.class);
    private static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResQuit messInfo = (ResQuit) mess.getData();
            if (!iosession.containsAttribute(SessionAttribute.PLAYER.getValue())) {
                log.error(" 服务器发送断开！ reason=" + messInfo.getReason());
                return;
            }
            Player player = (Player) iosession.getAttribute(SessionAttribute.PLAYER.getValue());
            player.setSession(null);
            player.setHeart();


            String str = player.getUserId() + " ," + player.getId() + " 服务器发送断开！ reason=" + messInfo.getReason();
            log.error(str);
            SessionUtils.closeSession(player.getSession(), str, true);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResQuitHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e, e);
        }
    }

}
