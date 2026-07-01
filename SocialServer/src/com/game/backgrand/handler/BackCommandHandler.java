package com.game.backgrand.handler;

import com.game.backgrand.script.IBackGrand;
import com.game.manager.Manager;
import com.game.script.struct.ScriptEnum;
import game.core.command.ICommand;
import game.core.script.IScript;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpRequest;

/**
 * @Desc TODO
 * @Date 2021/6/8 16:27
 * @Auth ZUncle
 */
public class BackCommandHandler implements ICommand {

    private ChannelHandlerContext session;
    private HttpRequest request;


    public BackCommandHandler(ChannelHandlerContext session, HttpRequest request) {
        this.session = session;
        this.request = request;
    }

    /**
     * 执行命令.
     */
    @Override
    public void action() {

        back().cmd(session, request);

    }

    public IBackGrand back() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.BackGrandScript);
        if (is == null) {
            throw new NullPointerException("没有找到具体的脚本实例！script=" + ScriptEnum.ServerScript);
        }
        return (IBackGrand) is;
    }
}
