package com.game.map.client;
import game.core.command.Handler;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 
import game.core.util.TimeUtils;
import game.message.MapMessage.ResRelive;
import org.apache.mina.core.session.IoSession;


/**
* makehandler  v1.5
* 复活吧我的勇士
*/
public class ResReliveHandler extends Handler{

    private final Logger log = LogManager.getLogger(ResReliveHandler.class);
    private final Logger logger = LogManager.getLogger("HandlerDealTime");

    @Override
    public void action(RMessage mess) {
        try {
            long start = TimeUtils.Time();
            IoSession iosession = mess.getSession();
            ResRelive messInfo = (ResRelive) mess.getData();


            long dealtime = TimeUtils.Time() - start;
            if (dealtime > 300) {
                logger.error("ResReliveHandler deal long time:" + dealtime);
            }
        } catch (Exception e) {
            log.info(e,e);
        }
    }

}