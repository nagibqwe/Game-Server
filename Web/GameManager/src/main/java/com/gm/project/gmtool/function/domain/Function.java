package com.gm.project.gmtool.function.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 游戏功能列表对象 t_function
 * 
 * @author gm
 * @date 2021-10-26
 */
public class Function extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 功能Id */
    @Excel(name = "功能Id")
    private Integer funcId;

    /** 功能名 */
    @Excel(name = "功能名")
    private String funcName;

    /** 父Id */
    @Excel(name = "父Id")
    private Integer parentId;

    /** 开启状态 */
    @Excel(name = "开启状态")
    private Integer openState;

    public void setFuncId(Integer funcId)
    {
        this.funcId = funcId;
    }

    public Integer getFuncId()
    {
        return funcId;
    }
    public void setFuncName(String funcName)
    {
        this.funcName = funcName;
    }

    public String getFuncName()
    {
        return funcName;
    }
    public void setParentId(Integer parentId)
    {
        this.parentId = parentId;
    }

    public Integer getParentId()
    {
        return parentId;
    }
    public void setOpenState(Integer openState)
    {
        this.openState = openState;
    }

    public Integer getOpenState()
    {
        return openState;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("funcId", getFuncId())
            .append("funcName", getFuncName())
            .append("parentId", getParentId())
            .append("openState", getOpenState())
            .toString();
    }
}
