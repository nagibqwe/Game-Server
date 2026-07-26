package com.gm.project.system.post.domain;

import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.gm.framework.web.domain.BaseEntity;

/**
 * 岗位表 sys_post
 * 
 * @author ruoyi
 */
public class Post extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 岗位№ */
    @Excel(name = "岗位№", cellType = ColumnType.NUMERIC)
    private Long postId;

    /** 岗位编码 */
    @Excel(name = "岗位编码")
    private String postCode;

    /** 岗位Название */
    @Excel(name = "岗位Название")
    private String postName;

    /** 岗位Сортировка */
    @Excel(name = "岗位Сортировка")
    private String postSort;

    /** Статус（0Норма 1Отключено） */
    @Excel(name = "Статус", readConverterExp = "0=Норма,1=Отключено")
    private String status;

    /** 用户ДаНет存在此岗位标识 默认不存在 */
    private boolean flag = false;

    public Long getPostId()
    {
        return postId;
    }

    public void setPostId(Long postId)
    {
        this.postId = postId;
    }

    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    public String getPostCode()
    {
        return postCode;
    }

    public void setPostCode(String postCode)
    {
        this.postCode = postCode;
    }

    @NotBlank(message = "岗位Название不能为空")
    @Size(min = 0, max = 50, message = "岗位Название长度不能超过50个字符")
    public String getPostName()
    {
        return postName;
    }

    public void setPostName(String postName)
    {
        this.postName = postName;
    }

    @NotBlank(message = "Порядок отображения обязателен")
    public String getPostSort()
    {
        return postSort;
    }

    public void setPostSort(String postSort)
    {
        this.postSort = postSort;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("postId", getPostId())
            .append("postCode", getPostCode())
            .append("postName", getPostName())
            .append("postSort", getPostSort())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}
