package com.gm.project.gmtool.hefu.entiry;

import com.gm.project.gmtool.server.domain.TServer;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/9 14:09
 */
public class HefuServer {
    private int serverId;
    private TServer server;
    private DBInfo db;
    private DBInfo dblog;

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public TServer getServer() {
        return server;
    }

    public void setServer(TServer server) {
        this.server = server;
    }

    public DBInfo getDb() {
        return db;
    }

    public void setDb(DBInfo db) {
        this.db = db;
    }

    public DBInfo getDblog() {
        return dblog;
    }

    public void setDblog(DBInfo dblog) {
        this.dblog = dblog;
    }


}
