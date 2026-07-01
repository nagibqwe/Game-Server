package com.kits.project.serverListConfig.server.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器配置信息对象 sdk_server
 * 
 * @author gm
 * @date 2021-04-25
 */
public class SdkServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private Long serverId;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 服务器IP */
    @Excel(name = "服务器IP")
    private String serverIp;

    /** 端口 */
    @Excel(name = "端口")
    private Long serverPort;

    /** 服务器状态 0:备服状态 1:开服状态 */
    @Excel(name = "服务器状态 0:备服状态 1:开服状态")
    private Long openState;

    /** 是否为备服状态 */
    @Excel(name = "是否为备服状态")
    private Long isBackup;

    /** 扩展数据 */
    @Excel(name = "扩展数据")
    private String serverExtconfig;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setServerId(Long serverId)
    {
        this.serverId = serverId;
    }

    public Long getServerId()
    {
        return serverId;
    }
    public void setServerName(String serverName)
    {
        this.serverName = serverName;
    }

    public String getServerName()
    {
        return serverName;
    }
    public void setServerIp(String serverIp)
    {
        this.serverIp = serverIp;
    }

    public String getServerIp()
    {
        return serverIp;
    }
    public void setServerPort(Long serverPort)
    {
        this.serverPort = serverPort;
    }

    public Long getServerPort()
    {
        return serverPort;
    }
    public void setServerExtconfig(String serverExtconfig)
    {
        this.serverExtconfig = serverExtconfig;
    }

    public Long getOpenState() {
        return openState;
    }

    public void setOpenState(Long openState) {
        this.openState = openState;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getIsBackup() {
        return isBackup;
    }

    public void setIsBackup(Long isBackup) {
        this.isBackup = isBackup;
    }

    public String getServerExtconfig()
    {
        return serverExtconfig;
    }

    @Override
    public String toString() {
        return "SdkServer{" +
                "id=" + id +
                ", serverId=" + serverId +
                ", serverName='" + serverName + '\'' +
                ", serverIp='" + serverIp + '\'' +
                ", serverPort=" + serverPort +
                ", openState=" + openState +
                ", isBackup=" + isBackup +
                ", serverExtconfig='" + serverExtconfig + '\'' +
                '}';
    }
}
