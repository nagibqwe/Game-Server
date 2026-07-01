package com.kits.project.serverListConfig.x8channelMaster.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 渠道商-实际的发行渠道商,360,小米等对象 t_x8_channel_master
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8ChannelMaster extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** x8内的发行渠道编号 */
    private Long chnmId;

    /** x8内渠道名称 */
    @Excel(name = "x8内渠道名称")
    private String chnmName;

    /** 渠道类型 iwgame表示表示官方，mix表示联运 */
    @Excel(name = "渠道类型 iwgame表示表示官方，mix表示联运")
    private String type;

    /** 渠道token认证url */
    @Excel(name = "渠道token认证url")
    private String authUrl;

    /** 渠道商的下单url地址 */
    @Excel(name = "渠道商的下单url地址")
    private String orderUrl;

    /** x8提供渠道的回调url */
    @Excel(name = "x8提供渠道的回调url")
    private String payCallbackUrl;

    /** 客户端参数模板 jsonArray格式 */
    @Excel(name = "客户端参数模板 jsonArray格式")
    private String sdkParamsTemplate;

    /** 扩展字段  jsonObject */
    @Excel(name = "扩展字段  jsonObject")
    private String extParams;

    /**  */
    @Excel(name = "")
    private String notes;

    /** 创建人 */
    @Excel(name = "创建人")
    private String createUser;

    /**  */
    @Excel(name = "")
    private String updateUser;

    public void setChnmId(Long chnmId)
    {
        this.chnmId = chnmId;
    }

    public Long getChnmId()
    {
        return chnmId;
    }
    public void setChnmName(String chnmName)
    {
        this.chnmName = chnmName;
    }

    public String getChnmName()
    {
        return chnmName;
    }
    public void setType(String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
    public void setAuthUrl(String authUrl)
    {
        this.authUrl = authUrl;
    }

    public String getAuthUrl()
    {
        return authUrl;
    }
    public void setOrderUrl(String orderUrl)
    {
        this.orderUrl = orderUrl;
    }

    public String getOrderUrl()
    {
        return orderUrl;
    }
    public void setPayCallbackUrl(String payCallbackUrl)
    {
        this.payCallbackUrl = payCallbackUrl;
    }

    public String getPayCallbackUrl()
    {
        return payCallbackUrl;
    }
    public void setSdkParamsTemplate(String sdkParamsTemplate)
    {
        this.sdkParamsTemplate = sdkParamsTemplate;
    }

    public String getSdkParamsTemplate()
    {
        return sdkParamsTemplate;
    }
    public void setExtParams(String extParams)
    {
        this.extParams = extParams;
    }

    public String getExtParams()
    {
        return extParams;
    }
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }
    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public String getCreateUser()
    {
        return createUser;
    }
    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("chnmId", getChnmId())
            .append("chnmName", getChnmName())
            .append("type", getType())
            .append("authUrl", getAuthUrl())
            .append("orderUrl", getOrderUrl())
            .append("payCallbackUrl", getPayCallbackUrl())
            .append("sdkParamsTemplate", getSdkParamsTemplate())
            .append("extParams", getExtParams())
            .append("notes", getNotes())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("createUser", getCreateUser())
            .append("updateUser", getUpdateUser())
            .toString();
    }
}
