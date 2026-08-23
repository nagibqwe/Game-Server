package com.gm.project.gmtool.cmd.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Объект логов операций горячего обновления сервера t_cmd_log
 * 
 * @author gm
 * @date 2021-07-30
 */
public class CmdLog
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** Команда */
    @Excel(name = "Команда")
    private String action;

    /** Параметры */
    @Excel(name = "Параметры")
    private String params;

    /** Имя сервера */
    @Excel(name = "Имя сервера")
    private String serverName;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** Результат операции */
    @Excel(name = "Результат операции")
    private Integer isOk;

    /** Результат обработки */
    @Excel(name = "Результат обработки")
    private String result;

    /** Время операции */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Время операции", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operDate;

    /** Пользователь */
    @Excel(name = "Пользователь")
    private String user;

    /** IP оператора */
    @Excel(name = "IP оператора")
    private String ip;

    /** Тип GM команды 0: игровой сервер GM (socket) 1: публичный сервер или сервер входа GM (http) */
    @Excel(name = "Тип GM команды 0: игровой сервер(socket) 1: публичный/сервер входа(http)")
    private Integer gmType;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setAction(String action)
    {
        this.action = action;
    }

    public String getAction()
    {
        return action;
    }
    public void setParams(String params)
    {
        this.params = params;
    }

    public String getParams()
    {
        return params;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setServerId(Integer serverId)
    {
        this.serverId = serverId;
    }

    public Integer getServerId()
    {
        return serverId;
    }
    public void setIsOk(Integer isOk)
    {
        this.isOk = isOk;
    }

    public Integer getIsOk()
    {
        return isOk;
    }
    public void setResult(String result)
    {
        this.result = result;
    }

    public String getResult()
    {
        return result;
    }
    public void setOperDate(Date operDate)
    {
        this.operDate = operDate;
    }

    public Date getOperDate()
    {
        return operDate;
    }
    public void setUser(String user)
    {
        this.user = user;
    }

    public String getUser()
    {
        return user;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setGmType(Integer gmType)
    {
        this.gmType = gmType;
    }

    public Integer getGmType()
    {
        return gmType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("action", getAction())
            .append("params", getParams())
            .append("serverName", getServerName())
            .append("serverId", getServerId())
            .append("isOk", getIsOk())
            .append("result", getResult())
            .append("operDate", getOperDate())
            .append("user", getUser())
            .append("ip", getIp())
            .append("gmType", getGmType())
            .toString();
    }
}