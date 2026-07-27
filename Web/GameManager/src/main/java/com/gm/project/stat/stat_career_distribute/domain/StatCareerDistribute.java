package com.gm.project.stat.stat_career_distribute.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 职业分布对象 stat_career_distribute
 * 
 * @author gm
 * @date 2021-09-07
 */
public class StatCareerDistribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 职业 */
    @Excel(name = "职业")
    private Integer career;

    /** 人数 */
    @Excel(name = "人数")
    private Integer rolecount;

    public void setCareer(Integer career)
    {
        this.career = career;
    }

    public Integer getCareer()
    {
        return career;
    }
    public void setRolecount(Integer rolecount)
    {
        this.rolecount = rolecount;
    }

    public Integer getRolecount()
    {
        return rolecount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("career", getCareer())
            .append("rolecount", getRolecount())
            .toString();
    }
}
