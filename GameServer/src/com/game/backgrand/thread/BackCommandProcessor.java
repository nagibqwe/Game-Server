/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backgrand.thread;

import game.core.command.CommandProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class BackCommandProcessor extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(BackCommandProcessor.class);

    private BackCommandProcessor() {
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
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static BackCommandProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
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
