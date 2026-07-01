/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package game.core.util;

import game.core.net.Config.ServerConfig;

/**
 *
 * @author Administrator
 */
public class LoginVerifySignCal
{

    //sign计算
    public static String calSign(long userId, String accessToken, String machineCode, long time,String platform)
    {
        return CodedUtil.Md5(userId + ServerConfig.getPrivateKey() + accessToken + machineCode + time + platform);
    }

    public static String calSign(String userId, String accessToken, String machineCode, String time,String platform)
    {
        return CodedUtil.Md5(userId + ServerConfig.getPrivateKey() + accessToken + machineCode + time + platform);
    }

    public static String calSign(String token ,String user_id, int svr_id, String secretKey)
    {
        return CodedUtil.Md5("token=" + token + "&user_id=" + user_id + "&svr_id=" + svr_id + secretKey);
    }

    public static String calSignV2(String token ,String user_id, String secretKey)
    {
        return CodedUtil.Md5("token=" + token + "&user_id=" + user_id + secretKey);
    }

}
