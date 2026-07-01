package com.game.server.timer;

import com.game.server.SocialServer;
import com.game.server.manager.ServerManager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/8/2 16:04
 * @Auth ZUncle
 */
public class ServerHeartTimer extends TimerEvent {

    static Logger logger = LogManager.getLogger(ServerHeartTimer.class);

    /**
     * 循环事件
     */
    public ServerHeartTimer() {
        super(-1, 5000, 1000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
//        logger.info("hello world!");
        try {
            SocialServer.getInstance().server().doHeart();
        } catch (Exception e) {
            logger.error(e, e);
        }

    }
}
