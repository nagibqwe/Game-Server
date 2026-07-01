package com.backend.utils;

import com.backend.bean.RechargeItemLog;
import com.backend.bean.User;
import com.backend.struct.StaticField;
import org.nutz.dao.Dao;

import com.backend.bean.BackendLog;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 后台操作行为日志
 */
public class BackendLogUtil {

    private Dao dao;

    private static BackendLogUtil instance = new BackendLogUtil();

    private BackendLogUtil() {
    }

    public static BackendLogUtil getInstance() {
        return instance;
    }

    public void init(Dao dao) {
        this.dao = dao;
    }

    public void log(BackendLog operationLog) {
        dao.insert(operationLog);
    }

    public void log(HttpServletRequest request, String content) {
        User user = (User) request.getSession().getAttribute("USER");
        String ip = Toolkit.getIp(request);
        BackendLog backendLog = new BackendLog(user, content, ip);
        log(backendLog);
    }
    public void log(RechargeItemLog rechargeItemLog) {
        dao.insert(rechargeItemLog);
    }
    public void rechargeItemlog(HttpServletRequest request, String content, String tableName) {
        User user = (User) request.getSession().getAttribute("USER");
        String ip = Toolkit.getIp(request);
        RechargeItemLog rechargeItemLog = new RechargeItemLog(user, content, ip, tableName);
        log(rechargeItemLog);
    }
}
