package com.game.server.social;

import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO
 * @Date 2021/6/22 20:11
 * @Auth ZUncle
 */
public class SocialServerReconnectTimer extends TimerEvent {

    static Logger logger = LogManager.getLogger(SocialServerReconnectTimer.class);

    /**
     * 默认1秒后执行1次的timer
     */
    public SocialServerReconnectTimer() {
        super(1, 5 * 1000, 0);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
//        logger.info("开始重连+++++ hash={}", this);
        SocialServerClient.getInstance().connect();
    }
}
