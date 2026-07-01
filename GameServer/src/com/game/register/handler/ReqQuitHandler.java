package com.game.register.handler;

import com.game.manager.Manager;
import com.game.player.structs.QuitGameDefine;
import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.RegisterMessage.ReqQuit;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
* @Desc //退出游戏
* @Desc TODO Auto Create
* @Auth Tool
*/

@Message(id = ReqQuit.MsgID.eMsgID_VALUE, clazz = ReqQuit.class)

public class ReqQuitHandler extends Handler<ReqQuit> {

    static final Logger log = LogManager.getLogger(ReqQuitHandler.class);
    static final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess, ReqQuit messInfo) {
        try {
            long start = TimeUtils.Time();
            ChannelHandlerContext context = mess.getContext();
            Manager.playerManager.iQuitGame().QuitGame(context, QuitGameDefine.Normal, true, true);

            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ReqQuitHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
