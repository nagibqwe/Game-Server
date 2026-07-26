package com.gm.project.gmtool.db.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал库列对象 t_db
 * 
 * @author gm
 * @date 2021-09-08
 */
public class TDb extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** Название сервера */
    @Excel(name = "Название сервера")
    private String serverName;

    /** Тип1:Журнал库 */
    @Excel(name = "Тип1:Журнал库")
    private Integer type;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** Платформа */
    @Excel(name = "Платформа")
    private String groupName;

    /** IP базы данных */
    @Excel(name = "IP базы данных")
    private String dbIp;

    /** Порт базы данных */
    @Excel(name = "Порт базы данных")
    private Integer dbPort;

    /** Название базы данных */
    @Excel(name = "Название базы данных")
    private String dbname;

    /** Пользователь базы данных */
    @Excel(name = "Пользователь базы данных")
    private String dbuser;

    /** Пароль базы данных */
    @Excel(name = "Пароль базы данных")
    private String dbpassword;

    /** Список объединения */
    @Excel(name = "Список объединения")
    private String serverIdList;

    /** Объединение серверов标识 */
    @Excel(name = "Объединение серверов标识")
    private Integer isHeFu;

    /** ID целевого сервера */
    @Excel(name = "ID целевого сервера")
    private Integer hefuServerID;

    /** Время объединения серверов */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Время объединения серверов", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /** Тип сервера 0:Тестовый сервер 1:Основной сервер 2:Сервер входа 3:Общий сервер 4:Межсерверный */
    @Excel(name = "Тип сервера 0:Тестовый сервер 1:Основной сервер 2:Сервер входа 3:Общий сервер 4:Межсерверный")
    private Integer serverType;

    /** Время открытия сервера */
    @Excel(name = "Время открытия сервера")
    private String serverOpenTime;

    /** Время обновления */
    @Excel(name = "Время обновления")
    private Date updateDate;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setDbIp(String dbIp)
    {
        this.dbIp = dbIp;
    }

    public String getDbIp()
    {
        return dbIp;
    }
    public void setDbPort(Integer dbPort)
    {
        this.dbPort = dbPort;
    }

    public Integer getDbPort()
    {
        return dbPort;
    }
    public void setDbname(String dbname)
    {
        this.dbname = dbname;
    }

    public String getDbname()
    {
        return dbname;
    }
    public void setDbuser(String dbuser)
    {
        this.dbuser = dbuser;
    }

    public String getDbuser()
    {
        return dbuser;
    }
    public void setDbpassword(String dbpassword)
    {
        this.dbpassword = dbpassword;
    }

    public String getDbpassword()
    {
        return dbpassword;
    }
    public void setServerIdList(String serverIdList)
    {
        this.serverIdList = serverIdList;
    }

    public String getServerIdList()
    {
        return serverIdList;
    }
    public void setIsHeFu(Integer isHeFu)
    {
        this.isHeFu = isHeFu;
    }

    public Integer getIsHeFu()
    {
        return isHeFu;
    }
    public void setHefuServerID(Integer hefuServerID)
    {
        this.hefuServerID = hefuServerID;
    }

    public Integer getHefuServerID()
    {
        return hefuServerID;
    }
    public void setHefuTime(Date hefuTime)
    {
        this.hefuTime = hefuTime;
    }

    public Date getHefuTime()
    {
        return hefuTime;
    }
    public void setServerType(Integer serverType)
    {
        this.serverType = serverType;
    }

    public Integer getServerType()
    {
        return serverType;
    }
    public void setServerOpenTime(String serverOpenTime)
    {
        this.serverOpenTime = serverOpenTime;
    }

    public String getServerOpenTime()
    {
        return serverOpenTime;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverName", getServerName())
            .append("type", getType())
            .append("serverId", getServerId())
            .append("groupName", getGroupName())
            .append("dbIp", getDbIp())
            .append("dbPort", getDbPort())
            .append("dbname", getDbname())
            .append("dbuser", getDbuser())
            .append("dbpassword", getDbpassword())
            .append("serverIdList", getServerIdList())
            .append("isHeFu", getIsHeFu())
            .append("hefuServerID", getHefuServerID())
            .append("hefuTime", getHefuTime())
            .append("serverType", getServerType())
            .append("serverOpenTime", getServerOpenTime())
            .append("updateDate", getUpdateDate())
            .toString();
    }
}
