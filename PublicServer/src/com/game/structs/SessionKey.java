/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.structs;

import io.netty.util.AttributeKey;

/**
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class SessionKey {

    //服务器ID标识
    public static final AttributeKey<Integer> SERVERID = AttributeKey.newInstance("server_id");
    public static final AttributeKey<String> SERVERPLATID = AttributeKey.newInstance("server_plat_id");
    public static final AttributeKey<String> SERVERPLAT = AttributeKey.newInstance("server_plat");
    public static final AttributeKey<String> SERVERNAMEPREFIX = AttributeKey.newInstance("server_Prefix");
    public static final AttributeKey<String> SERVERNAME = AttributeKey.newInstance("serverName");
}
