package com.kits.project.gmtool.server.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器列对象 t_server
 * 
 * @author gm
 * @date 2021-04-28
 */
public class TServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 服务器ID */
    @Excel(name = "服务器ID")
    private Long serverId;

    /** 服务器名称 */
    @Excel(name = "服务器名称")
    private String serverName;

    /** 平台名 */
    @Excel(name = "平台名")
    private String groupName;

    /** 游戏服IP */
    @Excel(name = "游戏服IP")
    private String WorldIP;

    /** 后台端口 */
    @Excel(name = "后台端口")
    private Long worldPort;

    /** 合服标识 */
    @Excel(name = "合服标识")
    private Integer isHeFu;

    /** 合服时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "合服时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date hefuTime;

    /** 合服目标服ID */
    @Excel(name = "合服目标服ID")
    private Long hefuServerID;

    /** 服务器标识 */
    @Excel(name = "服务器标识")
    private Integer serverType;

    /** 是否删除 */
    @Excel(name = "是否删除")
    private Integer isDeleted;

    /** 0为展示，1为不展示 */
    @Excel(name = "0为展示，1为不展示")
    private Integer isShow;

    /** 开服时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开服时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date serverOpenTime;

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
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setWorldIP(String WorldIP)
    {
        this.WorldIP = WorldIP;
    }

    public String getWorldIP()
    {
        return WorldIP;
    }
    public void setWorldPort(Long worldPort)
    {
        this.worldPort = worldPort;
    }

    public Long getWorldPort()
    {
        return worldPort;
    }
    public void setIsHeFu(Integer isHeFu)
    {
        this.isHeFu = isHeFu;
    }

    public Integer getIsHeFu()
    {
        return isHeFu;
    }
    public void setHefuTime(Date hefuTime)
    {
        this.hefuTime = hefuTime;
    }

    public Date getHefuTime()
    {
        return hefuTime;
    }
    public void setHefuServerID(Long hefuServerID)
    {
        this.hefuServerID = hefuServerID;
    }

    public Long getHefuServerID()
    {
        return hefuServerID;
    }
    public void setServerType(Integer serverType)
    {
        this.serverType = serverType;
    }

    public Integer getServerType()
    {
        return serverType;
    }
    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }
    public void setIsShow(Integer isShow)
    {
        this.isShow = isShow;
    }

    public Integer getIsShow()
    {
        return isShow;
    }
    public void setServerOpenTime(Date serverOpenTime)
    {
        this.serverOpenTime = serverOpenTime;
    }

    public Date getServerOpenTime()
    {
        return serverOpenTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("serverId", getServerId())
            .append("serverName", getServerName())
            .append("groupName", getGroupName())
            .append("WorldIP", getWorldIP())
            .append("worldPort", getWorldPort())
            .append("isHeFu", getIsHeFu())
            .append("hefuTime", getHefuTime())
            .append("hefuServerID", getHefuServerID())
            .append("serverType", getServerType())
            .append("isDeleted", getIsDeleted())
            .append("isShow", getIsShow())
            .append("serverOpenTime", getServerOpenTime())
            .toString();
    }
}
