package com.game.server.struct;

import io.netty.util.AttributeKey;

/**
 * @Desc TODO
 * @Date 2021/6/10 20:29
 * @Auth ZUncle
 */
public class SessionAttribute {

    public static final AttributeKey<Integer> ServerId = AttributeKey.newInstance("ServerId");

    public static final AttributeKey<String> ServerName = AttributeKey.newInstance("ServerName");

    public static final AttributeKey<Integer> ServerType = AttributeKey.newInstance("ServerType");

}
