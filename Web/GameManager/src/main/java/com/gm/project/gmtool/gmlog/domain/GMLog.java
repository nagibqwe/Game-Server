package com.gm.project.gmtool.gmlog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Журнал GM-панели对象 t_gm_log
 * 
 * @author gm
 * @date 2021-09-01
 */
public class GMLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Журнал主键 */
    private Long id;

    /** Оператор */
    @Excel(name = "Оператор")
    private String name;

    /** Название подразделения */
    @Excel(name = "Название подразделения")
    private String deptName;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String ip;

    /** ЖурналСодержимое */
    @Excel(name = "ЖурналСодержимое")
    private String content;

    /** Время операции */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Время операции", width = 30, dateFormat = "yyyy-MM-dd")
    private Date time;

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
    public void setDeptName(String deptName)
    {
        this.deptName = deptName;
    }

    public String getDeptName()
    {
        return deptName;
    }
    public void setIp(String ip)
    {
        this.ip = ip;
    }

    public String getIp()
    {
        return ip;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setTime(Date time)
    {
        this.time = time;
    }

    public Date getTime()
    {
        return time;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("deptName", getDeptName())
            .append("ip", getIp())
            .append("content", getContent())
            .append("time", getTime())
            .toString();
    }
}
