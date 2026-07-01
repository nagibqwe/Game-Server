/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.player.structs;

import com.game.register.structs.UserInfo;
import com.game.fightserver.struct.FightClient;
import io.netty.util.AttributeKey;

/**
 *
 * @author Administrator
 */
public class SessionAttribute {

    /**
     * 1	账号数据
     */
    public static final AttributeKey<UserInfo> USER_INFO = AttributeKey.newInstance("user_info");
    /**
     * 2	账号状态
     */
    public static final AttributeKey<Integer> USER_STATE = AttributeKey.newInstance("user_state");
    /**
     * 3	角色id
     */
    public static final AttributeKey<Long> ROLE_ID = AttributeKey.newInstance("roleId");
    /**
     * 4	服务器id
     */
    public static final AttributeKey<Integer> SERVER_ID = AttributeKey.newInstance("serverId");
    /**
     * 5	渠道名
     */
    public static final AttributeKey<String> PLATFORMNAME = AttributeKey.newInstance("platformName");
    /**
     * 6	ip
     */
    public static final AttributeKey<String> IP = AttributeKey.newInstance("ip");
    /**
     * 7	客户端发送加速检查包的时间
     */
    public static final AttributeKey<Integer> LASTCLIENTSENDSPEEDUP = AttributeKey.newInstance("LASTCLIENTSENDSPEEDUP");
    /**
     * 8	心跳次数
     */
//  public static final AttributeKey<Integer> HEARTCOUNT=AttributeKey.newInstance("heartcount");
    /**
     * 9	玩家
     */
    public static final AttributeKey<Player> PLAYER = AttributeKey.newInstance("player");
    /**
     * 10	上次加速判断发送时间
     */
    public static final AttributeKey<Long> SpeedUpCheckSendTime = AttributeKey.newInstance("LastHeartSendTime");
    /**
     * 11	客户端心跳发送时间
     */
    public static final AttributeKey<Long> HeartSendTime = AttributeKey.newInstance("HeartSendTime");
    /**
     * 5	funcell生成的UUid
     */
    public static final AttributeKey<String> FUNCELLUUID = AttributeKey.newInstance("funcellUUid");

    /**
     * 5	MACHINECODE
     */
    public static final AttributeKey<String> MACHINECODE = AttributeKey.newInstance("MACHINECODE");

    /**
     * 5	PLATUSERID
     */
    public static final AttributeKey<String> PLATUSERID = AttributeKey.newInstance("PLATUSERID");

    /**
     * 5	客户端的系统
     */
    public static final AttributeKey<String> CLIENTOS = AttributeKey.newInstance("CLIENTOS");

    public final static AttributeKey<Integer> CONNECT_SERVER_ID = AttributeKey.newInstance("connect-server-id");
    public final static AttributeKey<String> CONNECT_SERVER_IP = AttributeKey.newInstance("connect-server-ip");
    public final static AttributeKey<Integer> CONNECT_SERVER_PORT = AttributeKey.newInstance("connect-server-port");

    public final static AttributeKey<FightClient> FIGHT_CLIENT_INFO = AttributeKey.newInstance("FIGHT_CLIENT_INFO");

    //登录时选择的语言类型
    public static final AttributeKey<Integer> LANGUAGE_TYPE = AttributeKey.newInstance("languageType");

    //发送无效消息次数
    public static final AttributeKey<Integer> INVALIDMSGCOUNT = AttributeKey.newInstance("invalidmsgcount");

}
