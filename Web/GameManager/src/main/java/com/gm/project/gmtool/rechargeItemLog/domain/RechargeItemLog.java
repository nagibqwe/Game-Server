package com.gm.project.gmtool.rechargeItemLog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 充值配置日志对象 t_recharge_item_log
 * 
 * @author gm
 * @date 2021-08-25
 */
public class RechargeItemLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** 修改人id */
    @Excel(name = "修改人id")
    private Integer userId;

    /** 修改人IP */
    @Excel(name = "修改人IP")
    private String ip;

    /** 修改人名 */
    @Excel(name = "修改人名")
    private String userName;

    /** 修改时间 */
    @Excel(name = "修改时间")
    private Long time;

    /** 操作表名 */
    @Excel(name = "操作表名")
    private String tableName;

    /** 操作内容(详情) */
    @Excel(name = "操作内容(详情)")
    private String content;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setUserId(Integer userId)
    {
        this.userId = userId;
    }

    public Integer getUserId()
    {
        return userId;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableName()
    {
        return tableName;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("ip", getIp())
            .append("userName", getUserName())
            .append("time", getTime())
            .append("tableName", getTableName())
            .append("content", getContent())
            .toString();
    }
}
