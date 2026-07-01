package com.kits.project.serverListConfig.loginServer.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 登录服信息对象 sdk_login_server
 * 
 * @author gm
 * @date 2021-10-20
 */
public class SdkLoginServer extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    @Excel(name = "id")
    private Long id;

    /** 登录服务器名称 */
    @Excel(name = "登录服务器名称")
    private String loginServerName;

    /** 登录服务器IP */
    @Excel(name = "登录服务器IP")
    private String loginServerIp;

    /** 登录服端口 */
    @Excel(name = "登录服端口")
    private Integer loginServerPort;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setLoginServerName(String loginServerName)
    {
        this.loginServerName = loginServerName;
    }

    public String getLoginServerName()
    {
        return loginServerName;
    }
    public void setLoginServerIp(String loginServerIp)
    {
        this.loginServerIp = loginServerIp;
    }

    public String getLoginServerIp()
    {
        return loginServerIp;
    }
    public void setLoginServerPort(Integer loginServerPort)
    {
        this.loginServerPort = loginServerPort;
    }

    public Integer getLoginServerPort()
    {
        return loginServerPort;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("loginServerName", getLoginServerName())
            .append("loginServerIp", getLoginServerIp())
            .append("loginServerPort", getLoginServerPort())
            .toString();
    }
}
