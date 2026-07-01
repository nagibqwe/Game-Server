/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.login;

import com.game.db.dao.UserloginDao;
import game.core.util.LoginVerifySignCal;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class LoginDataUpdate {

    private final static Logger logger = LogManager.getLogger(LoginDataUpdate.class);

    //格式http://ip:port/changelogindata?userId=12&sign=fjsfok&accessToken=jsoi&machineCode=fjio&time=123354&platformName=123354&enterServerId=1&addServerId=1&deleteServerId=1&roleId=1
    public static void changeLoginData(HttpRequest httpRequest) {
        QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
        Map<String, List<String>> rMap = qsd.parameters();
        String userId = rMap.get("userId").get(0);
        String sign = rMap.get("sign").get(0);
        String accessToken = rMap.get("accessToken").get(0);
        String machineCode = rMap.get("machineCode").get(0);
        String time = rMap.get("time").get(0);
        String platformName = rMap.get("platformName").get(0);
        if (userId == null || sign == null || accessToken == null || machineCode == null || time == null
                || !LoginVerifySignCal.calSign(userId, accessToken, machineCode, time,platformName).equals(sign)) {
            logger.error("改变logindata数据失败 参数错误." + qsd.toString());
            return;
        }

        LoginDataBean user = UserCacheManager.getInstance().getByUserId(Long.parseLong(userId));
        if (user == null) {
            logger.error("改变logindata数据失败 账号不存在.userId:" + userId);
            return;
        }

        boolean isChange = false;
        //更新进入的服务器id
        isChange |= updateEnterServerId(user, rMap.get("enterServerId").get(0));
        //新增创建角色的服务器id
        isChange |= addCreateRoleServerId(user, rMap.get("addServerId").get(0));
        //删除了角色的服务器id
        isChange |= deleteServerId(user, rMap.get("deleteServerId").get(0));
        //更新进入服务器的角色
        isChange |= updateEnterRoleId(user, rMap.get("roleId").get(0));
        if (!isChange) {
            return;
        }
        UserloginDao userDao = new UserloginDao();
        userDao.update(user.toUserloginBean());
    }

    private static boolean updateEnterRoleId(LoginDataBean user, String roleId) {
        if (roleId == null) {
            return false;
        }
        Long lastEnterRoleId = Long.parseLong(roleId);
        if(user.getLastEnterRoleId() == lastEnterRoleId) {
            return false;
        }
        user.setLastEnterRoleId(lastEnterRoleId);
        return true;
    }

    //更新进入的服务器id
    private static boolean updateEnterServerId(LoginDataBean user, String enterServerId) {
        if (enterServerId == null) {
            return false;
        }
        user.setLastEnterServerId(Integer.parseInt(enterServerId));
        return true;
    }

    //添加创建了角色的服务器id
    private static boolean addCreateRoleServerId(LoginDataBean user, String addServerId) {
        if (addServerId == null) {
            return false;
        }
        Integer serverId = Integer.parseInt(addServerId);
        if (user.getSIdList().contains(serverId)) {
            return false;
        }
        user.getSIdList().add(serverId);
        return true;
    }

    //移除删除了角色的服务器id
    private static boolean deleteServerId(LoginDataBean user, String deleteServerId) {

        if (deleteServerId == null) {
            return false;
        }

        Integer serverId = Integer.parseInt(deleteServerId);
        if (!user.getSIdList().contains(serverId)) {
            return false;
        }
        user.getSIdList().remove(serverId);
        return true;
    }
}
