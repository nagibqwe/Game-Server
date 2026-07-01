package com.gm.project.gmtool.activityBossType.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 运营活动boss类型对象 t_activity_boss_type
 * 
 * @author gm
 * @date 2021-09-14
 */
public class ActivityBossType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动BOSS分类配置ID */
    @Excel(name = "活动BOSS分类配置ID")
    private Integer id;

    /** 后台显示的BOSS类型 */
    @Excel(name = "后台显示的BOSS类型")
    private String name;

    /** 对应的BOSSID */
    @Excel(name = "对应的BOSSID")
    private String bossId;

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
    public void setBossId(String bossId)
    {
        this.bossId = bossId;
    }

    public String getBossId()
    {
        return bossId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .append("bossId", getBossId())
            .toString();
    }
}
