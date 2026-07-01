package com.game.server.script;

import game.core.message.RMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * 协议处理接口
 *
 * @author soko <xuchangming@haowan123.com>
 */
public interface IhandlerScript {

    /**
     * 过渡网络协议
     *
     * @param mess
     * @return
     */
    boolean Filte_Handler(RMessage mess);

    boolean CheckFightServerId(int msgId);

    boolean CheckFilterMsgId(int msgId, RMessage mess);

    boolean OnOtherDealMsg(int msgId, RMessage mess, ChannelHandlerContext session);

    boolean FightSendMsgID_Filter(int msgId);

    /**
     * 消息派发
     * @param mess
     */
    void dispatch(RMessage mess);

}
