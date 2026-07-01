package com.gm.project.gmtool.gmlog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * GM后台日志记录对象 t_gm_log
 * 
 * @author gm
 * @date 2021-09-01
 */
public class GMLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 日志主键 */
    private Long id;

    /** 操作人员 */
    @Excel(name = "操作人员")
    private String name;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String deptName;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String ip;

    /** 日志内容 */
    @Excel(name = "日志内容")
    private String content;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
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
