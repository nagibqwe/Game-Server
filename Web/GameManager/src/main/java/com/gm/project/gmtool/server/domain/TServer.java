package com.gm.project.gmtool.server.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.common.utils.MessageUtils;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;

import javax.validation.constraints.Min;


/**
 * 服务器列对象 t_server
 * 
 * @author gm
 * @date 2021-07-14
 */
public class TServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 服务器名称
     */
    @Excel(name = "服务器名称")
    private String serverName;

    /**
     * 服务器ID
     */
    @Excel(name = "服务器ID")
    private Integer serverId;

    /**
     * 平台名
     */
    @Excel(name = "分组名")
    private String groupName;

    /**
     * 服务器IP
     */
    @Excel(name = "服务器IP")
    private String serverIP;

    /**
     * 服务器端口
     */
    @Excel(name = "服务器端口")
    private Integer serverPort;

    /**
     * 日志库IP
     */
    @Excel(name = "日志库IP")
    private String dblogIp;

    /**
     * 日志库端口
     */
    @Excel(name = "日志库端口")
    private Integer dblogPort;

    /**
     * 日志库名称
     */
    @Excel(name = "日志库名称")
    private String dblogName;

    /**
     * 日志库用户名
     */
    @Excel(name = "日志库用户名")
    private String dblogUser;

    /**
     * 日志库密码
     */
    @Excel(name = "日志库密码")
    private String dblogPwd;

    /**
     * 是否合服
     */
    @Excel(name = "是否合服")
    private Integer isHeFu;

    /**
     * 合服时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合服时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /**
     * 合服目标服ID
     */
    @Excel(name = "合服目标服ID")
    private Integer hefuServerID;

    /**
     * 合服列表
     */
    @Excel(name = "合服列表")
    private String serverIdList;

    /**
     * 服务器类型 0:测试服 1:正式服 2:登录服 3:公共服 4:战斗服
     */
    @Excel(name = "服务器类型 0:测试服 1:正式服 2:登录服 3:公共服 4:战斗服")
    private Integer serverType;

    /**
     * 是否删除
     */
    @Excel(name = "是否删除")
    private Integer isDeleted;

    /**
     * 0为展示，1为不展示
     */
    @Excel(name = "0为展示，1为不展示")
    private Integer isShow;

    /**
     * 开服时间
     */
    @Excel(name = "开服时间")
    private String serverOpenTime;

    /**
     * 服务器状态 0:备服状态 1:开服状态
     */
    @Excel(name = "服务器状态 0:备服状态 1:开服状态")
    private Long openState;

    /**
     * 服务器最新心跳时间
     */
    @Excel(name = "服务器最新心跳时间")
    private String heartTime;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerName() {
        return serverName;
    }

    public Integer getServerId() {
        return serverId;
    }

    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setServerIP(String serverIP) {
        this.serverIP = serverIP;
    }

    public String getServerIP() {
        return serverIP;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public void setDblogIp(String dblogIp) {
        this.dblogIp = dblogIp;
    }

    public String getDblogIp() {
        return dblogIp;
    }

    public Integer getDblogPort() {
        return dblogPort;
    }

    public void setDblogPort(Integer dblogPort) {
        this.dblogPort = dblogPort;
    }

    public void setDblogName(String dblogName) {
        this.dblogName = dblogName;
    }

    public String getDblogName() {
        return dblogName;
    }

    public void setDblogUser(String dblogUser) {
        this.dblogUser = dblogUser;
    }

    public String getDblogUser() {
        return dblogUser;
    }

    public void setDblogPwd(String dblogPwd) {
        this.dblogPwd = dblogPwd;
    }

    public String getDblogPwd() {
        return dblogPwd;
    }

    public void setIsHeFu(Integer isHeFu) {
        this.isHeFu = isHeFu;
    }

    public Integer getIsHeFu() {
        return isHeFu;
    }

    public void setHefuTime(Date hefuTime) {
        this.hefuTime = hefuTime;
    }

    public Date getHefuTime() {
        return hefuTime;
    }

    public Integer getHefuServerID() {
        return hefuServerID;
    }

    public void setHefuServerID(Integer hefuServerID) {
        this.hefuServerID = hefuServerID;
    }

    public String getServerIdList() {
        return serverIdList;
    }

    public void setServerIdList(String serverIdList) {
        this.serverIdList = serverIdList;
    }

    public void setServerType(Integer serverType) {
        this.serverType = serverType;
    }

    public Integer getServerType() {
        return serverType;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsShow(Integer isShow) {
        this.isShow = isShow;
    }

    public Integer getIsShow() {
        return isShow;
    }

    public void setServerOpenTime(String serverOpenTime) {
        this.serverOpenTime = serverOpenTime;
    }

    public String getServerOpenTime() {
        return serverOpenTime;
    }

    public void setOpenState(Long openState) {
        this.openState = openState;
    }

    public Long getOpenState() {
        return openState;
    }

    public void setHeartTime(String heartTime) {
        this.heartTime = heartTime;
    }

    public String getHeartTime() {
        return heartTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("serverName", getServerName())
                .append("serverId", getServerId())
                .append("groupName", getGroupName())
                .append("serverIP", getServerIP())
                .append("serverPort", getServerPort())
                .append("dblogIp", getDblogIp())
                .append("dblogPort", getDblogPort())
                .append("dblogName", getDblogName())
                .append("dblogUser", getDblogUser())
                .append("dblogPwd", getDblogPwd())
                .append("isHeFu", getIsHeFu())
                .append("hefuTime", getHefuTime())
                .append("hefuServerID", getHefuServerID())
                .append("serverIdList", getServerIdList())
                .append("serverType", getServerType())
                .append("isDeleted", getIsDeleted())
                .append("isShow", getIsShow())
                .append("serverOpenTime", getServerOpenTime())
                .append("openState", getOpenState())
                .append("heartTime", getHeartTime())
                .toString();
    }
}
