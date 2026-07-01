package com.kits.project.serverListConfig.channel.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 渠道信息对象 sdk_channel
 * 
 * @author gm
 * @date 2021-04-28
 */
public class SdkChannel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** 渠道ID */
    @Excel(name = "渠道ID")
    private Long channelId;

    /** 渠道名 */
    @Excel(name = "渠道名")
    private String channelName;

    /** 是否被占用 0-未占用  1-已占用 */
    private Long isUse;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setChannelName(String channelName)
    {
        this.channelName = channelName;
    }

    public String getChannelName()
    {
        return channelName;
    }
    public void setIsUse(Long isUse)
    {
        this.isUse = isUse;
    }

    public Long getIsUse()
    {
        return isUse;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("channelId", getChannelId())
            .append("channelName", getChannelName())
            .append("isUse", getIsUse())
            .toString();
    }
}
