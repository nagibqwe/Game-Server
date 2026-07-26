package com.gm.project.monitor.logininfor.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.gm.framework.web.domain.BaseEntity;
import java.util.Date;

/**
 * 系统访问记录表 sys_logininfor
 * 
 * @author ruoyi
 */
public class Logininfor extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "№", cellType = ColumnType.NUMERIC)
    private Long infoId;

    /** 用户账号 */
    @Excel(name = "用户账号")
    private String loginName;

    /** 登录Статус 0Успешно 1Ошибка */
    @Excel(name = "登录Статус", readConverterExp = "0=Успешно,1=Ошибка")
    private String status;

    /** 登录IP地址 */
    @Excel(name = "登录地址")
    private String ipaddr;

    /** Место входа */
    @Excel(name = "Место входа")
    private String loginLocation;

    /** БраузерТип */
    @Excel(name = "Браузер")
    private String browser;

    /** Операционная система */
    @Excel(name = "Операционная система")
    private String os;

    /** Подсказка消息 */
    @Excel(name = "Подсказка消息")
    private String msg;

    /** 访问Время */
    @Excel(name = "访问Время", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date loginTime;

    public Long getInfoId()
    {
        return infoId;
    }

    public void setInfoId(Long infoId)
    {
        this.infoId = infoId;
    }

    public String getLoginName()
    {
        return loginName;
    }

    public void setLoginName(String loginName)
    {
        this.loginName = loginName;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIpaddr()
    {
        return ipaddr;
    }

    public void setIpaddr(String ipaddr)
    {
        this.ipaddr = ipaddr;
    }

    public String getLoginLocation()
    {
        return loginLocation;
    }

    public void setLoginLocation(String loginLocation)
    {
        this.loginLocation = loginLocation;
    }

    public String getBrowser()
    {
        return browser;
    }

    public void setBrowser(String browser)
    {
        this.browser = browser;
    }

    public String getOs()
    {
        return os;
    }

    public void setOs(String os)
    {
        this.os = os;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public Date getLoginTime()
    {
        return loginTime;
    }

    public void setLoginTime(Date loginTime)
    {
        this.loginTime = loginTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("infoId", getInfoId())
            .append("loginName", getLoginName())
            .append("ipaddr", getIpaddr())
            .append("loginLocation", getLoginLocation())
            .append("browser", getBrowser())
            .append("os", getOs())
            .append("status", getStatus())
            .append("msg", getMsg())
            .append("loginTime", getLoginTime())
            .toString();
    }
}