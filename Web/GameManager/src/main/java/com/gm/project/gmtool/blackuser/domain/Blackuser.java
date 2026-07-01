package com.gm.project.gmtool.blackuser.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 黑名单对象 t_blackuser
 * 
 * @author gm
 * @date 2021-11-04
 */
public class Blackuser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Integer id;

    /** 黑名单用户 */
    @Excel(name = "黑名单用户Id")
    private Long userId;

    /** 平台名 */
    @Excel(name = "平台名")
    private String platform;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPlatform()
    {
        return platform;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userId", getUserId())
            .append("platform", getPlatform())
            .toString();
    }
}
