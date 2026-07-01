package com.game.register.script;

import com.game.player.structs.Player;
import game.message.BIMessage;
import game.message.RegisterMessage.ReqDeleteRole;
import game.message.RegisterMessage.ReqLoginGame;
import game.message.RegisterMessage.ReqRegainRole;
import io.netty.channel.ChannelHandlerContext;

/**
 * 登录接口处理
 */
public interface IRegisterScript {

    void OnReqCreateCharacter(ChannelHandlerContext context, BIMessage.Device device, String name, int career);
    
    void OnReqDeleteRole(ChannelHandlerContext context, ReqDeleteRole messInfo);
        
    void OnReqLoginGame(ChannelHandlerContext context, ReqLoginGame messInfo);
    
    void OnReqRegainRole(ChannelHandlerContext context, ReqRegainRole messInfo);
    
    void OnReqSelectCharacter(ChannelHandlerContext context, long roleId, boolean isReconnect);

    /**
     * 踢出连接
     */
    void tickSession( ChannelHandlerContext session);

    /**
     * 注册连接
     * @param session
     */
    void registerSession(ChannelHandlerContext session);


    void replaceLogin(ChannelHandlerContext old,boolean isSendMsg);

    /**
     * 加载封号和白名单数据
     */
    void loadForbidAndWhite();

    void writeRoleLoginLog(Player player);
    void writeRoleLoginLog(Player player, int type, boolean isTrue);


    /**
     * 开服登录异常检测
     */
    void loginCheckException();

}
