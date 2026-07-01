package com.game.recharge.manager;

import game.core.command.CommandProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 充值队列处理
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class RechargeProcessor extends CommandProcessor {

    private static final Logger log = LogManager.getLogger("RechargeManager");

    public RechargeProcessor(String threadName) {
        super(threadName);
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        RechargeProcessor processor;

        Singleton() {
            this.processor = new RechargeProcessor("RechargeManager");
        }

        RechargeProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     */
    public static RechargeProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    @Override
    public void writeError(String message) {
        log.error("充值进程时失败错了：" + message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        log.error("充值进程时失败错了：" + message, t);
    }

}
