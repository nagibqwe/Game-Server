/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.thread;

import game.core.command.CommandProcessor;
import game.core.command.ICommand;
import game.core.message.MessageNumber;
import game.core.message.MsgSourceEnum;
import game.core.message.RMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <b>登陆消息分发线程</b>
 *
 * @author ChenLong
 */
public class RegisterProcessor extends CommandProcessor {

    private static final Logger log = LogManager.getLogger(RegisterProcessor.class);

    private RegisterProcessor() {
        super(RegisterProcessor.class.getSimpleName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        RegisterProcessor processor;

        Singleton() {
            this.processor = new RegisterProcessor();
        }

        RegisterProcessor getProcessor() {
            return processor;
        }
    }

    /**
     * 获取DispatchProcessor的实例对象.
     *
     * @return
     */
    public static RegisterProcessor getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 命令处理入口，这里并不执行命令，而是根据命令类型或属性为其分派对应的线程Processor.
     *
     * @param command 待处理的命令
     */
    @Override
    protected void doCommand(ICommand command) {
        if (command instanceof RMessage) {
            RMessage handler = (RMessage) command;
            try {
                int msgid = handler.getId();

                int sourceId = MessageNumber.getSource(msgid);

                if (sourceId != MsgSourceEnum.ClientToGameServerr) {
                    log.error("收到了不是gameServer应该处理 消息ID是：" + msgid);
                    return;
                }

                handler.action();
            } catch (Exception e) {
                log.error(e, e);
            }
        } else {
            log.error("不支持的命令类型！command class: " + command.getClass().getName());
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
