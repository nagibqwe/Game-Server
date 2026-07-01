package com.game.backgrand.thread;

import game.core.command.CommandProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @Desc TODO  GM 后台消息执行线程
 * @Date 2021/6/8 16:30
 * @Auth ZUncle
 */
public class BackCommandProcessor extends CommandProcessor {

    final Logger logger = LogManager.getLogger(BackCommandProcessor.class);

    public BackCommandProcessor() {
        super(BackCommandProcessor.class.getSimpleName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        BackCommandProcessor processor;

        Singleton() {
            this.processor = new BackCommandProcessor();
        }

        BackCommandProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * @return
     */
    public static BackCommandProcessor Instance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     */
    @Override
    public void writeError(String message) {
        logger.error(message);
    }

    /**
     * 如果处理过程中发生异常，记录错误日志信息.
     *
     * @param message 错误信息描述
     * @param t       产生错误的异常类
     */
    @Override
    public void writeError(String message, Throwable t) {
        logger.error(message, t);
    }
}
