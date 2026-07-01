/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.background;

import com.game.login.LoginVerify;
import com.game.login.UserCacheManager;
import com.game.db.bean.ForbidBean;
import com.game.db.bean.LogUserDoBean;
import com.game.db.dao.ForbidDao;
import com.game.db.dao.UserloginDao;
import com.game.db.dao.WhiteDao;
import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.util.TimeUtils;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author Administrator
 */
public class BackgroundDoUser {

    private final static Logger logger = LogManager.getLogger(BackgroundDoUser.class);

    private final static int deleteuserType = 0;//删除账号
    private final static int recoveruserType = 1;//恢复已删除账号

    //格式http://ip:port/deleteuser?secret_key=12&userId=214
    public static String deleteuser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台删除账号失败 私密key错误");
                return "failed";
            }
            String userId = pMap.get("userId").get(0);
            if (userId == null) {
                logger.error("后台删除账号失败 传入账号id或者账号名为空");
                return "failed";
            }
            deleteuser(Long.parseLong(userId));
            return "ok";
        } catch (Exception numberFormatException) {
            logger.error("后台删除账号失败 参数错误", numberFormatException);
            return "failed";
        }
    }

    //格式http://ip:port/recoveruser?secret_key=12&userId=214&userName=5465
    public static String recoveruser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台恢复账号失败 私密key错误");
                return "failed";
            }
            String userId = pMap.get("userId").get(0);
            String userName = pMap.get("userName").get(0);
            if (userId == null || userName == null) {
                logger.error("后台恢复账号失败 传入账号id或者账号名为空");
                return "failed";
            }
            recoveruser(Long.parseLong(userId), userName);
            return "ok";
        } catch (Exception numberFormatException) {
            logger.error("后台恢复账号失败 参数错误", numberFormatException);
            return "failed";
        }
    }

    //格式http://ip:port/forbiddenuser?secret_key=12&whiteStr=214&forbiddenTime=1990943(unix时间，单位s)
    public static String forbiddenUser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台屏蔽账号失败 私密key错误");
                return "failed";
            }
            String forbidStr = pMap.get("forbidStr").get(0);
            String forbiddenTime = pMap.get("forbiddenTime").get(0);
            if (forbidStr == null || forbiddenTime == null) {
                logger.error("后台屏蔽账号失败 传入forbidStr为空");
                return "failed";
            }
            if (LoginVerify.getInstance().getForbids().containsKey(forbidStr)) {
                ForbidBean bean = new ForbidBean();
                bean.setStr(forbidStr);
                bean.setTime(Integer.parseInt(forbiddenTime));
                LoginVerify.getInstance().getForbids().put(forbidStr, bean);
                ForbidDao dao = new ForbidDao();
                dao.delete(forbidStr);
                dao.insert(bean);
                logger.error("后台屏蔽账号失败 传入forbidStr:" + forbidStr + "已存在");
                return "ok";
            }
            ForbidBean bean = new ForbidBean();
            bean.setStr(forbidStr);
            bean.setTime(Integer.parseInt(forbiddenTime));
            LoginVerify.getInstance().getForbids().put(forbidStr, bean);
            ForbidDao dao = new ForbidDao();
            dao.insert(bean);
            logger.error("后台屏蔽账号 传入forbidStr:" + forbidStr + "成功， 封号的结束日期是：" + TimeUtils.format2string(bean.getTime() * 1000L));
            return "ok";
        } catch (Exception e) {
            logger.error("后台屏蔽账号失败 参数错误", e);
            return "failed";
        }
    }

    //格式http://ip:port/cancelforbiddenuser?secret_key=12&whiteStr=214
    public static String cancelForbiddenUser(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台取消屏蔽失败 私密key错误");
                return "failed";
            }
            String forbidStr = pMap.get("forbidStr").get(0);
            if (forbidStr == null) {
                logger.error("后台取消屏蔽失败 传入forbidStr为空");
                return "failed";
            }
            LoginVerify.getInstance().getForbids().remove(forbidStr);
            ForbidDao dao = new ForbidDao();
            dao.delete(forbidStr);
            return "ok";
        } catch (Exception e) {
            logger.error("后台取消屏蔽失败 参数错误", e);
            return "failed";
        }
    }

    //格式http://ip:port/whiteadd?secret_key=12&whiteStr=214
    public static String whiteAdd(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台设置白名单账号失败 私密key错误");
                return "failed";
            }
            String whiteStr = pMap.get("whiteStr").get(0);
            if (whiteStr == null) {
                logger.error("后台设置白名单账号失败 传入whiteStr为空");
                return "falied";
            }
            //如果已经有了则返回
            if (LoginVerify.getInstance().isWhite(whiteStr)) {
                return "ok";
            }

            WhiteDao whitedao = new WhiteDao();
            whitedao.insert(whiteStr);
            LoginVerify.getInstance().getWhites().add(whiteStr);

        } catch (Exception e) {
            logger.error("后台设置白名单账号失败 传入参数错误", e);
            return "falied";
        }
        return "ok";
    }

    //格式http://ip:port/whitecancel?secret_key=12&whiteStr=214
    public static String whiteCancel(HttpRequest httpRequest) {
        try {
            QueryStringDecoder qsd = new QueryStringDecoder(httpRequest.uri());
            Map<String, List<String>> pMap = qsd.parameters();
            List<String> param = qsd.parameters().get("secret_key");
            if (!param.get(0).equals(ServerConfig.getPrivateKey())) {
                logger.error("后台取消白名单账号失败 私密key错误");
                return "failed";
            }
            String whiteStr = pMap.get("whiteStr").get(0);
            if (whiteStr == null) {
                logger.error("后台取消白名单账号失败 传入whiteStr为空");
                return "falied";
            }
            WhiteDao whitedao = new WhiteDao();
            whitedao.delete(whiteStr);
            LoginVerify.getInstance().getWhites().remove(whiteStr);

        } catch (Exception e) {
            logger.error("后台取消白名单账号失败 传入参数错误", e);
            return "falied";
        }
        return "ok";
    }

    //恢复已删除的账号
    private static void recoveruser(long userId, String userName) {
        UserloginDao userDao = new UserloginDao();
        userDao.deleteUserByUserName(userName);
        UserCacheManager.getInstance().remove(userName);
        userDao.recoverUser(userId);
        writeUserDoLog(userId, recoveruserType);
    }

    //删除账号
    private static void deleteuser(long userId) {
        UserloginDao userDao = new UserloginDao();
        userDao.deleteUser(userId);
        UserCacheManager.getInstance().removeByUserId(userId);
        writeUserDoLog(userId, deleteuserType);
    }

    //写后台操作log
    private static void writeUserDoLog(long userId, int type) {
        try {
            LogUserDoBean logUserDoBean = new LogUserDoBean();
            logUserDoBean.setUserId(userId);
            logUserDoBean.setType(type);
            LogService.getInstance().execute(logUserDoBean);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

}
