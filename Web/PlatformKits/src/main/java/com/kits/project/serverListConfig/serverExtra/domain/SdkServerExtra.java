package com.kits.project.serverListConfig.serverExtra.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器额外信息对象 sdk_server_extra
 *
 * @author gm
 * @date 2021-05-07
 */
public class SdkServerExtra extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 服务器列表ID */
    private Long serverListId;

    /** 服务器ID */
    private Long serverId;

    /** 排序值 */
    @Excel(name = "排序值")
    private Long sortId;

    /** 状态 0-未开放 1-开放中 2-维护中 */
    @Excel(name = "状态 0-未开放 1-开放中 2-维护中")
    private Long serverStatus;

    /** 游戏服类型 0-测试 1-先锋 2-正式 */
    @Excel(name = "游戏服类型 0-测试 1-先锋 2-正式")
    private Long groupType;

    /** 热度标签 0-正常  1-爆满  2-推荐  3-新服 */
    @Excel(name = "热度标签 0-正常  1-爆满  2-推荐  3-新服")
    private String serverLabel;

    /** 场景ID */
    @Excel(name = "场景ID")
    private String sceId;

    /** 客户端版本ID */
    @Excel(name = "客户端版本ID")
    private String appVersion;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setServerListId(Long serverListId)
    {
        this.serverListId = serverListId;
    }

    public Long getServerListId()
    {
        return serverListId;
    }
    public void setServerId(Long serverId)
    {
        this.serverId = serverId;
    }

    public Long getServerId()
    {
        return serverId;
    }
    public void setSortId(Long sortId)
    {
        this.sortId = sortId;
    }

    public Long getSortId()
    {
        return sortId;
    }
    public void setServerStatus(Long serverStatus)
    {
        this.serverStatus = serverStatus;
    }

    public Long getServerStatus()
    {
        return serverStatus;
    }
    public void setGroupType(Long groupType)
    {
        this.groupType = groupType;
    }

    public Long getGroupType()
    {
        return groupType;
    }
    public void setServerLabel(String serverLabel)
    {
        this.serverLabel = serverLabel;
    }

    public String getServerLabel()
    {
        return serverLabel;
    }
    public void setSceId(String sceId)
    {
        this.sceId = sceId;
    }

    public String getSceId()
    {
        return sceId;
    }
    public void setAppVersion(String appVersion)
    {
        this.appVersion = appVersion;
    }

    public String getAppVersion()
    {
        return appVersion;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("serverListId", getServerListId())
                .append("serverId", getServerId())
                .append("sortId", getSortId())
                .append("serverStatus", getServerStatus())
                .append("groupType", getGroupType())
                .append("serverLabel", getServerLabel())
                .append("sceId", getSceId())
                .append("appVersion", getAppVersion())
                .append("updateTime", getUpdateTime())
                .append("createTime", getCreateTime())
                .toString();
    }
}