/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.thread;

import game.core.command.CommandProcessor;
import game.core.command.Handler;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>命令分发线程</b>
 *
 * @author ChenLong
 */
public class InnerServerProcessor extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(InnerServerProcessor.class);

    private InnerServerProcessor() {
        super(InnerServerProcessor.class.getSimpleName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        InnerServerProcessor processor;

        Singleton() {
            this.processor = new InnerServerProcessor();
        }

        InnerServerProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static InnerServerProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 命令处理入口，这里并不执行命令，而是根据命令类型或属性为其分派对应的线程Processor.
     *
     * @param command 待处理的命令
     */
    @Override
    protected void doCommand(ICommand command) {
        try {
            command.action();
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void writeError(String message) {
        log.error(message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        log.error(message, t);
    }

}
