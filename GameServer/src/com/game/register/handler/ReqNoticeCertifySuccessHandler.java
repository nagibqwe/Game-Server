package com.game.register.handler;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqNoticeCertifySuccess;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //客户端通知游戏服实名认证成功
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqNoticeCertifySuccess.MsgID.eMsgID_VALUE, clazz = ReqNoticeCertifySuccess.class)

public class ReqNoticeCertifySuccessHandler extends Handler<ReqNoticeCertifySuccess> {

    static final Logger log = LogManager.getLogger(ReqNoticeCertifySuccessHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqNoticeCertifySuccess messInfo) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) mess.getExecutor();
            Manager.playerManager.certifyScript().onReqNoticeCertifySuccess(player);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqNoticeCertifySuccessHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
