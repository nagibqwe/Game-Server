package com.gm.project.gmtool.cmd.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал операций горячего обновления对象 t_cmd_log
 * 
 * @author gm
 * @date 2021-07-30
 */
public class CmdLog
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** Действия命令 */
    @Excel(name = "Действия命令")
    private String action;

    /** 参数 */
    @Excel(name = "参数")
    private String params;

    /** Название сервера */
    @Excel(name = "Название сервера")
    private String serverName;

    /** ID сервера */
    @Excel(name = "ID сервера")
    private Integer serverId;

    /** ДействияРезультат */
    @Excel(name = "ДействияРезультат")
    private Integer isOk;

    /** 处理Результат */
    @Excel(name = "处理Результат")
    private String result;

    /** Время операции */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "Время операции", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operDate;

    /** 用户 */
    @Excel(name = "用户")
    private String user;

    /** IP оператора */
    @Excel(name = "IP оператора")
    private String ip;

    /** GM命令Тип 0:游戏服GM(socket) 1:Общий сервер或Сервер входаGM(http) */
    @Excel(name = "GM命令Тип 0:游戏服GM(socket) 1:Общий сервер或Сервер входаGM(http)")
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
