/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.server.filter;

import com.game.server.MainServer;
import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger; 

/**
 * 网络消息处理接口逻辑
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class InnerMsgImpl extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(InnerMsgImpl.class);

    private InnerMsgImpl() {
        super(InnerMsgImpl.class.getSimpleName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        InnerMsgImpl processor;

        Singleton() {
            this.processor = new InnerMsgImpl();
        }

        InnerMsgImpl getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static InnerMsgImpl getInstance() {
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

    /**
     * 分配任务入口
     *
     * @param command
     */
    @Override
    protected void doCommand(ICommand command) {
        try {
            boolean isTrue = MainServer.getInstance().getwServerThread().addCommand(command);//压入线程去处理， 保存线程操作
            //如果没有成功， 则进行自处理
            if (!isTrue) {
                command.action();
            }
        } catch (Exception e) {
            log.error(e, e);
            command.action();
        }
    }

    @Override
    protected void doCommandStatistic(long useTime, ICommand command) {
        if (useTime > 300) {
            if (command instanceof RMessage) {
                RMessage handler = (RMessage) command;
                log.info("处理消息：" + handler.getId() + " 超时：" + useTime);
            } else {
                log.error("处理了未知的协议信息!超时：" + useTime);
            }
        }
    }
}
