package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

@Table
public class DbGame {

    @Id
    @Column
    private int id;

    @Column
    @Comment("服务器ID")
    private int serverId;

    @Name
    @Column
    @Comment("服务器名称")
    private String serverName;

    @Column
    @Comment("平台名")
    private String groupName;

    @Column
    @Comment("服务器IP及端口")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String serverIpPort;

    @Column
    @Comment("数据库名称")
    private String dbname;

    @Column
    @Comment("数据库用户名")
    private String dbuser;

    @Column
    @Comment("数据库密码")
    @ColDefine(type = ColType.VARCHAR, width = 100)
    private String dbpassword;

    @Column
    @Comment("合服列表")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String owerlist;

    @Column
    @Comment("合服标识 0:未合服 1:已合服")
    @ColDefine(customType="TINYINT",notNull=true)
    @Default("0")
    private int isHeFu;

    @Column
    @Comment("合服目标服ID")
    @Default("0")
    private int hefuServerID;

    @Column
    @Comment("合服时间")
    private Date hefuTime;

    @Column
    @Comment("服务器类型 0:测试服 1:正式服 2:登录服 3:跨服")
    @ColDefine(customType="TINYINT",notNull=true)
    @Default("0")
    private int serverType;

    @Column
    @Comment("是否删除 0:启用 1:删除 ")
    @ColDefine(customType="TINYINT",notNull=true)
    @Default("0")
    private int isDeleted;

    //签名算法计算
    private String sign;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getServerIpPort() {
        return serverIpPort;
    }

    public void setServerIpPort(String serverIpPort) {
        this.serverIpPort = serverIpPort;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getDbuser() {
        return dbuser;
    }

    public void setDbuser(String dbuser) {
        this.dbuser = dbuser;
    }

    public String getDbpassword() {
        return dbpassword;
    }

    public void setDbpassword(String dbpassword) {
        this.dbpassword = dbpassword;
    }

    public String getOwerlist() {
        return owerlist;
    }

    public void setOwerlist(String owerlist) {
        this.owerlist = owerlist;
    }

    public int getIsHeFu() {
        return isHeFu;
    }

    public void setIsHeFu(int isHeFu) {
        this.isHeFu = isHeFu;
    }

    public int getHefuServerID() {
        return hefuServerID;
    }

    public void setHefuServerID(int hefuServerID) {
        this.hefuServerID = hefuServerID;
    }

    public Date getHefuTime() {
        return hefuTime;
    }

    public void setHefuTime(Date hefuTime) {
        this.hefuTime = hefuTime;
    }

    public int getServerType() {
        return serverType;
    }

    public void setServerType(int serverType) {
        this.serverType = serverType;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
