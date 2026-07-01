package com.kits.project.serverListConfig.x8app.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 游戏应用对象 t_x8_app
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8App extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 游戏应用编号 */
    private Long appId;

    /** 应用名称 */
    @Excel(name = "应用名称")
    private String appName;

    /**  */
    @Excel(name = "")
    private String gameId;

    /** 游戏app端密钥 */
    @Excel(name = "游戏app端密钥")
    private String appKey;

    /** 游戏服务端密钥 */
    @Excel(name = "游戏服务端密钥")
    private String appSecret;

    /** 游戏应用rsa公钥 */
    @Excel(name = "游戏应用rsa公钥")
    private String appRsaPubKey;

    /** 游戏应用rsa私钥 */
    @Excel(name = "游戏应用rsa私钥")
    private String appRsaPriKey;

    /** 游戏默认回调地址-暂未用到 */
    @Excel(name = "游戏默认回调地址-暂未用到")
    private String payCallbackUrl;

    /**  */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dataOperateTime;

    /**  */
    @Excel(name = "")
    private String createUser;

    /**  */
    @Excel(name = "")
    private String updateUser;

    /**  */
    @Excel(name = "")
    private String notes;

    /** 额外参数 */
    @Excel(name = "额外参数")
    private String extParams;

    public void setAppId(Long appId)
    {
        this.appId = appId;
    }

    public Long getAppId()
    {
        return appId;
    }
    public void setAppName(String appName)
    {
        this.appName = appName;
    }

    public String getAppName()
    {
        return appName;
    }
    public void setGameId(String gameId)
    {
        this.gameId = gameId;
    }

    public String getGameId()
    {
        return gameId;
    }
    public void setAppKey(String appKey)
    {
        this.appKey = appKey;
    }

    public String getAppKey()
    {
        return appKey;
    }
    public void setAppSecret(String appSecret)
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret()
    {
        return appSecret;
    }
    public void setAppRsaPubKey(String appRsaPubKey)
    {
        this.appRsaPubKey = appRsaPubKey;
    }

    public String getAppRsaPubKey()
    {
        return appRsaPubKey;
    }
    public void setAppRsaPriKey(String appRsaPriKey)
    {
        this.appRsaPriKey = appRsaPriKey;
    }

    public String getAppRsaPriKey()
    {
        return appRsaPriKey;
    }
    public void setPayCallbackUrl(String payCallbackUrl)
    {
        this.payCallbackUrl = payCallbackUrl;
    }

    public String getPayCallbackUrl()
    {
        return payCallbackUrl;
    }
    public void setDataOperateTime(Date dataOperateTime)
    {
        this.dataOperateTime = dataOperateTime;
    }

    public Date getDataOperateTime()
    {
        return dataOperateTime;
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
    public void setNotes(String notes)
    {
        this.notes = notes;
    }

    public String getNotes()
    {
        return notes;
    }
    public void setExtParams(String extParams)
    {
        this.extParams = extParams;
    }

    public String getExtParams()
    {
        return extParams;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("appId", getAppId())
            .append("appName", getAppName())
            .append("gameId", getGameId())
            .append("appKey", getAppKey())
            .append("appSecret", getAppSecret())
            .append("appRsaPubKey", getAppRsaPubKey())
            .append("appRsaPriKey", getAppRsaPriKey())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("payCallbackUrl", getPayCallbackUrl())
            .append("dataOperateTime", getDataOperateTime())
            .append("createUser", getCreateUser())
            .append("updateUser", getUpdateUser())
            .append("notes", getNotes())
            .append("extParams", getExtParams())
            .toString();
    }
}
