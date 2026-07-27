package com.gm.project.stat.stat_level_distribute.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 角色等级分布对象 stat_level_distribute
 * 
 * @author gm
 * @date 2021-08-06
 */
public class StatLevelDistribute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 等级 */
    @Excel(name = "等级")
    private Integer level;

    /** 人数 */
    @Excel(name = "人数")
    private Integer rolecount;

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Integer getLevel()
    {
        return level;
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
            .append("level", getLevel())
            .append("rolecount", getRolecount())
            .toString();
    }
}
