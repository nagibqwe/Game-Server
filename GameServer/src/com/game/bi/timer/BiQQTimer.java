package com.game.bi.timer;

import com.game.bi.manager.BIManager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 定时处理需要重试的数据
 * @Auther: gouzhongliang
 * @Date: 2021/12/21 11:24
 */
public class BiQQTimer extends TimerEvent {

    private final Logger log = LogManager.getLogger(BITimer.class);

    public BiQQTimer() {
        super(-1, 0, 5000);
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {
        try {
            BIManager.getInstance().getQQScript().retry();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
