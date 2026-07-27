package com.gm.project.gmtool.activityFestivalType.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 节日类型对象 t_activity_festival_type
 * 
 * @author gm
 * @date 2021-09-09
 */
public class ActivityFestivalType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 节日ID */
    @Excel(name = "节日ID")
    private Integer id;

    /** 节日名 */
    @Excel(name = "节日名")
    private String name;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .toString();
    }
}
