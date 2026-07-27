package com.gm.project.gmtool.white.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 白名单管理对象 t_white
 * 
 * @author gm
 * @date 2021-11-22
 */
public class White extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** 白名单条件 */
    @Excel(name = "白名单条件")
    private String con;

    /** 状态:0=生效1=失效 */
    @Excel(name = "状态:0=生效1=失效")
    private Integer state;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCon(String con)
    {
        this.con = con;
    }

    public String getCon()
    {
        return con;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("con", getCon())
            .append("state", getState())
            .toString();
    }
}
