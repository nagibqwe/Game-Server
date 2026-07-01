package com.kits.project.serverListConfig.x8channel.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 渠道-包含游戏各个渠道商的配置对象 t_x8_channel
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8Channel extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 渠道号 */
    private Long chnId;

    /** 逻辑渠道号 */
    @Excel(name = "逻辑渠道号")
    private Long logicChnId;

    /** app编号 */
    @Excel(name = "app编号")
    private Long appId;

    /** 渠道商号 */
    @Excel(name = "渠道商号")
    private Long chnmId;

    /** 游戏应用在渠道商申请的appid */
    @Excel(name = "游戏应用在渠道商申请的appid")
    private String cpAppId;

    /** 游戏应用在渠道商申请的appkey */
    @Excel(name = "游戏应用在渠道商申请的appkey")
    private String cpAppKey;

    /** 游戏应用在渠道商申请的appsecret */
    @Excel(name = "游戏应用在渠道商申请的appsecret")
    private String cpAppSecret;

    /** 是否开启充值;0-关闭，1-开启 */
    @Excel(name = "是否开启充值;0-关闭，1-开启")
    private Integer enablePay;

    /** 是否开启登录;0-关闭，1-开启 */
    @Excel(name = "是否开启登录;0-关闭，1-开启")
    private Integer enableLogin;

    /** 游戏应用在渠道商申请的支付id */
    @Excel(name = "游戏应用在渠道商申请的支付id")
    private String cpPayId;

    /** 游戏应用在渠道商申请的支付key */
    @Excel(name = "游戏应用在渠道商申请的支付key")
    private String cpPayKey;

    /** 游戏应用在渠道商申请的支付私钥 */
    @Excel(name = "游戏应用在渠道商申请的支付私钥")
    private String cpPayPriKey;

    /** 渠道商自定义配置json结构{k1:v1,k2:v2} */
    @Excel(name = "渠道商自定义配置json结构{k1:v1,k2:v2}")
    private String cpConfig;

    /** 渠道商认证url,此项会覆盖t_x8_channel_master中的对应值 */
    @Excel(name = "渠道商认证url,此项会覆盖t_x8_channel_master中的对应值")
    private String authUrl;

    /** 渠道商回调x8地址 */
    @Excel(name = "渠道商回调x8地址")
    private String payCallbackUrl;

    /**  */
    @Excel(name = "")
    private String orderUrl;

    /**  */
    @Excel(name = "")
    private String createUser;

    /**  */
    @Excel(name = "")
    private String updateUser;

    /** SDK打包参数，jsonObject格式 */
    @Excel(name = "SDK打包参数，jsonObject格式")
    private String sdkParams;

    /**  */
    @Excel(name = "")
    private String notes;

    public void setChnId(Long chnId)
    {
        this.chnId = chnId;
    }

    public Long getChnId()
    {
        return chnId;
    }
    public void setLogicChnId(Long logicChnId)
    {
        this.logicChnId = logicChnId;
    }

    public Long getLogicChnId()
    {
        return logicChnId;
    }
    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setChnmId(Long chnmId)
    {
        this.chnmId = chnmId;
    }

    public Long getChnmId()
    {
        return chnmId;
    }
    public void setCpAppId(String cpAppId)
    {
        this.cpAppId = cpAppId;
    }

    public String getCpAppId()
    {
        return cpAppId;
    }
    public void setCpAppKey(String cpAppKey)
    {
        this.cpAppKey = cpAppKey;
    }

    public String getCpAppKey()
    {
        return cpAppKey;
    }
    public void setCpAppSecret(String cpAppSecret)
    {
        this.cpAppSecret = cpAppSecret;
    }

    public String getCpAppSecret()
    {
        return cpAppSecret;
    }
    public void setEnablePay(Integer enablePay)
    {
        this.enablePay = enablePay;
    }

    public Integer getEnablePay()
    {
        return enablePay;
    }
    public void setEnableLogin(Integer enableLogin)
    {
        this.enableLogin = enableLogin;
    }

    public Integer getEnableLogin()
    {
        return enableLogin;
    }
    public void setCpPayId(String cpPayId)
    {
        this.cpPayId = cpPayId;
    }

    public String getCpPayId()
    {
        return cpPayId;
    }
    public void setCpPayKey(String cpPayKey)
    {
        this.cpPayKey = cpPayKey;
    }

    public String getCpPayKey()
    {
        return cpPayKey;
    }
    public void setCpPayPriKey(String cpPayPriKey)
    {
        this.cpPayPriKey = cpPayPriKey;
    }

    public String getCpPayPriKey()
    {
        return cpPayPriKey;
    }
    public void setCpConfig(String cpConfig)
    {
        this.cpConfig = cpConfig;
    }

    public String getCpConfig()
    {
        return cpConfig;
    }
    public void setAuthUrl(String authUrl)
    {
        this.authUrl = authUrl;
    }

    public String getAuthUrl()
    {
        return authUrl;
    }
    public void setPayCallbackUrl(String payCallbackUrl)
    {
        this.payCallbackUrl = payCallbackUrl;
    }

    public String getPayCallbackUrl()
    {
        return payCallbackUrl;
    }
    public void setOrderUrl(String orderUrl)
    {
        this.orderUrl = orderUrl;
    }

    public String getOrderUrl()
    {
        return orderUrl;
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
    public void setSdkParams(String sdkParams)
    {
        this.sdkParams = sdkParams;
    }

    public String getSdkParams()
    {
        return sdkParams;
    }
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("chnId", getChnId())
            .append("logicChnId", getLogicChnId())
            .append("appId", getAppId())
            .append("chnmId", getChnmId())
            .append("cpAppId", getCpAppId())
            .append("cpAppKey", getCpAppKey())
            .append("cpAppSecret", getCpAppSecret())
            .append("enablePay", getEnablePay())
            .append("enableLogin", getEnableLogin())
            .append("cpPayId", getCpPayId())
            .append("cpPayKey", getCpPayKey())
            .append("cpPayPriKey", getCpPayPriKey())
            .append("cpConfig", getCpConfig())
            .append("authUrl", getAuthUrl())
            .append("payCallbackUrl", getPayCallbackUrl())
            .append("orderUrl", getOrderUrl())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("createUser", getCreateUser())
            .append("updateUser", getUpdateUser())
            .append("sdkParams", getSdkParams())
            .append("notes", getNotes())
            .toString();
    }
}
