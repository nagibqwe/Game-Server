package com.game.ninedaysfocused.script;

import game.core.script.IScript;
import game.message.NineDaysFocusedMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by 542 on 2019/7/22.
 */
public interface INineDaysFocused extends IScript {

    public void onStart();

    public void onOver();

    /**
     * 报名
     */
    public void onG2PMatchEnterMap(ChannelHandlerContext context,NineDaysFocusedMessage.G2PReqApplyNieDaysFocused messInfo);

    /**
     * 取消报名
     */
    public void onG2PCancelMatchEnterMap(ChannelHandlerContext context);

    public void tick(long time);


}
