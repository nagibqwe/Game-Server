package com.gm.project.gmtool.changereason.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Код причины对象 t_changereason
 * 
 * @author gm
 * @date 2021-12-21
 */
public class TChangereason extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** Код причиныid */
    private Long id;

    /** Код причины名字 */
    @Excel(name = "Код причины名字")
    private String name;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("name", getName())
            .toString();
    }
}
