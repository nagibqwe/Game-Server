package com.kits.project.serverListConfig.channelids.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器列中的渠道ID和渠道ID列关系对应对象 sdk_channelid_channelids
 * 
 * @author gm
 * @date 2021-05-10
 */
public class SdkChannelidChannelids extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 渠道ID */
    @Excel(name = "渠道ID")
    private Long channelId;

    /** 渠道ID列表 */
    @Excel(name = "渠道ID列表")
    private String channelIds;

    public void setChannelId(Long channelId)
    {
        this.channelId = channelId;
    }

    public Long getChannelId()
    {
        return channelId;
    }
    public void setChannelIds(String channelIds)
    {
        this.channelIds = channelIds;
    }

    public String getChannelIds()
    {
        return channelIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("channelId", getChannelId())
            .append("channelIds", getChannelIds())
            .toString();
    }
}
