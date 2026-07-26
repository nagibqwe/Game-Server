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
 * Сервер列对象 t_server
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
     * Название сервера
     */
    @Excel(name = "Название сервера")
    private String serverName;

    /**
     * ID сервера
     */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /**
     * Платформа
     */
    @Excel(name = "分组名")
    private String groupName;

    /**
     * СерверIP
     */
    @Excel(name = "СерверIP")
    private String serverIP;

    /**
     * Сервер端口
     */
    @Excel(name = "Сервер端口")
    private Integer serverPort;

    /**
     * Журнал库IP
     */
    @Excel(name = "Журнал库IP")
    private String dblogIp;

    /**
     * Журнал库端口
     */
    @Excel(name = "Журнал库端口")
    private Integer dblogPort;

    /**
     * Журнал库Название
     */
    @Excel(name = "Журнал库Название")
    private String dblogName;

    /**
     * Журнал库Имя пользователя
     */
    @Excel(name = "Журнал库Имя пользователя")
    private String dblogUser;

    /**
     * Журнал库Пароль
     */
    @Excel(name = "Журнал库Пароль")
    private String dblogPwd;

    /**
     * Объединён
     */
    @Excel(name = "Объединён")
    private Integer isHeFu;

    /**
     * Время объединения серверов
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Время объединения серверов", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /**
     * ID целевого сервера
     */
    @Excel(name = "ID целевого сервера")
    private Integer hefuServerID;

    /**
     * Список объединения
     */
    @Excel(name = "Список объединения")
    private String serverIdList;

    /**
     * Тип сервера 0:Тестовый сервер 1:Основной сервер 2:Сервер входа 3:Общий сервер 4:战斗服
     */
    @Excel(name = "Тип сервера 0:Тестовый сервер 1:Основной сервер 2:Сервер входа 3:Общий сервер 4:战斗服")
    private Integer serverType;

    /**
     * Удалён
     */
    @Excel(name = "Удалён")
    private Integer isDeleted;

    /**
     * 0为展示，1为不展示
     */
    @Excel(name = "0为展示，1为不展示")
    private Integer isShow;

    /**
     * Время открытия сервера
     */
    @Excel(name = "Время открытия сервера")
    private String serverOpenTime;

    /**
     * Статус сервера 0:Резервный 1:Открыт
     */
    @Excel(name = "Статус сервера 0:Резервный 1:Открыт")
    private Long openState;

    /**
     * Сервер最新心跳Время
     */
    @Excel(name = "Сервер最新心跳Время")
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
