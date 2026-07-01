package com.game.server.timer;

import com.game.server.SocialServer;
import com.game.server.publicClient.PublicClient;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/6/11 17:34
 * @Auth ZUncle
 */
public class PublicServerReconnectTimer extends TimerEvent {

    static Logger logger = LogManager.getLogger(PublicServerReconnectTimer.class);
    /**
     * 循环事件
     */
    public PublicServerReconnectTimer() {
        super(1, 5 * 1000, 0);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

//        logger.info("公共服重连++++++++++");

        SocialServer.getInstance().pc.connect();

    }
}
