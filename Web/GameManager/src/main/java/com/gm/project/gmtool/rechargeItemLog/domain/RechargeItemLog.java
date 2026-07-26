package com.gm.project.gmtool.rechargeItemLog.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал настроек пополнения对象 t_recharge_item_log
 * 
 * @author gm
 * @date 2021-08-25
 */
public class RechargeItemLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** Изменить人id */
    @Excel(name = "Изменить人id")
    private Integer userId;

    /** Изменить人IP */
    @Excel(name = "Изменить人IP")
    private String ip;

    /** Изменить人名 */
    @Excel(name = "Изменить人名")
    private String userName;

    /** Время изменения */
    @Excel(name = "Время изменения")
    private Long time;

    /** Действия表名 */
    @Excel(name = "Действия表名")
    private String tableName;

    /** ДействияСодержимое(Подробнее) */
    @Excel(name = "ДействияСодержимое(Подробнее)")
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
