package com.backend.manager;

import com.backend.bean.Channel;
import com.backend.bean.Server;
import org.apache.log4j.Logger;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 登录服务器相关数据获取
 */
public class LoginServerManager {

    private final static Logger log = Logger.getLogger(LoginServerManager.class);

    private Dao dao;

    private Dao loginDao;

    private Dao loginLogDao;

    private List<String> channelList = new ArrayList<>();

    private Server loginServer;

    /**
     * 获取登录库连接
     */
    public Dao getLoginDao() {
        return loginDao;
    }

    /**
     * 获取登录库日志库连接
     */
    public Dao getLoginLogDao() {
        return loginLogDao;
    }

    /**
     * 获取相关平台的渠道信息
     */
    public List<String> getChannel() {
        return channelList;
    }

    /**
     * 取得登录服信息
     */
    public Server getLoginServer() {
        return loginServer;
    }

    public void init(Dao dao, Dao loginDao, Dao loginLogDao) {
        log.info("初始化登录服务器相关信息...");
        this.dao = dao;
        this.loginDao = loginDao;
        this.loginLogDao = loginLogDao;
        loadAll();
    }

    public void loadAll() {
        reloadChannels();
        List<Server> loginList = dao.query(Server.class, Cnd.where("serverType", "=", "2"));
        if (loginList == null || loginList.isEmpty()) {
            log.error("登录服配置不存在！！！");
            return;
        }
        if (loginList.size() > 1) {
            log.error("存在多个登录服配置！！！");
        }
        loginServer = loginList.get(0);
    }

    private void reloadChannels() {
        channelList.clear();
        List<Channel> channels = dao.query(Channel.class, null);
        channels.forEach(n -> channelList.add(n.getName()));
    }

    private enum Singleton {

        INSTANCE;
        LoginServerManager manager;

        Singleton() {
            this.manager = new LoginServerManager();
        }

        LoginServerManager getProcessor() {
            return manager;
        }
    }

    public static LoginServerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
