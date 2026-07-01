/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.register.client;

import com.game.structs.Config;
import game.core.util.CodedUtil;

/**
 *
 * @author Administrator
 */
public class LoginVerifySignCal
{

    //sign计算
    public static String calSign(long userId, String accessToken, String machineCode, long time,String plateformName)
    {
        return CodedUtil.Md5(userId + Config.getPrivateKey() + accessToken + machineCode + time + plateformName);
    }

    public static String calSign(long userId, String accessToken, String machineCode, String time)
    {
        return CodedUtil.Md5(userId + Config.getPrivateKey() + accessToken + machineCode + time);
    }

    public static String calSign(String userId, String accessToken, String machineCode, long time)
    {
        return CodedUtil.Md5(userId + Config.getPrivateKey() + accessToken + machineCode + time);
    }

    public static String calSign(String userId, String accessToken, String machineCode, String time)
    {
        return CodedUtil.Md5(userId + Config.getPrivateKey() + accessToken + machineCode + time);
    }
}
