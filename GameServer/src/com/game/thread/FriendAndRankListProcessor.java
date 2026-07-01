/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.thread;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 好友与排行榜的所有消息要走的线程， 防止出现多线程处理帮会与王府的数据问题
 */
public class FriendAndRankListProcessor extends CommandProcessor {

    private static final Logger LOG = LogManager.getLogger("GuildProcessor");

    public FriendAndRankListProcessor() {
        super(FriendAndRankListProcessor.class.getSimpleName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        FriendAndRankListProcessor processor;

        Singleton() {
            this.processor = new FriendAndRankListProcessor();
        }

        FriendAndRankListProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static FriendAndRankListProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    @Override
    protected void doCommand(ICommand command) {
        command.action();
    }

    @Override
    public void writeError(String message) {
        LOG.error("处理帮会相关的协议时:" + message);
    }

    @Override
    public void writeError(String message, Throwable t) {
        LOG.error(message, t);
    }

}
