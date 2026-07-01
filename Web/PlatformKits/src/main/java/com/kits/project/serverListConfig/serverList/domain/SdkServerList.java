package com.kits.project.serverListConfig.serverList.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器列表对象 sdk_server_list
 * 
 * @author gm
 * @date 2021-04-26
 */
public class SdkServerList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 服务器列表名称 */
    @Excel(name = "服务器列表名称")
    private String name;

    /** 登陆服务器IP */
    @Excel(name = "登陆服务器IP")
    private String loginServerGroup;

    /** 渠道ID列表 */
    @Excel(name = "渠道ID列表")
    private String channelIds;

    /** 服务器ID列表 */
    @Excel(name = "服务器ID列表")
    private String serverIds;

    /** 状态 */
    @Excel(name = "状态")
    private Long status;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getLoginServerGroup() {
        return loginServerGroup;
    }

    public void setLoginServerGroup(String loginServerGroup) {
        this.loginServerGroup = loginServerGroup;
    }

    public void setChannelIds(String channelIds)
    {
        this.channelIds = channelIds;
    }

    public String getChannelIds()
    {
        return channelIds;
    }
    public void setServerIds(String serverIds)
    {
        this.serverIds = serverIds;
    }

    public String getServerIds()
    {
        return serverIds;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("loginServerGroup", getLoginServerGroup())
            .append("channelIds", getChannelIds())
            .append("serverIds", getServerIds())
            .append("status", getStatus())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
