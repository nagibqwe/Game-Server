package com.gm.project.gmtool.activityFestivalRelation.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 运营活动节日关系对象 t_activity_festival_relation
 * 
 * @author gm
 * @date 2021-11-08
 */
public class ActivityFestivalRelation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 运营活动type的ID */
    @Excel(name = "运营活动type的ID")
    private Integer logicId;

    /** 节日ID */
    @Excel(name = "节日ID")
    private Integer festivalId;

    public void setLogicId(Integer logicId)
    {
        this.logicId = logicId;
    }

    public Integer getLogicId()
    {
        return logicId;
    }
    public void setFestivalId(Integer festivalId)
    {
        this.festivalId = festivalId;
    }

    public Integer getFestivalId()
    {
        return festivalId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("logicId", getLogicId())
            .append("festivalId", getFestivalId())
            .toString();
    }
}
