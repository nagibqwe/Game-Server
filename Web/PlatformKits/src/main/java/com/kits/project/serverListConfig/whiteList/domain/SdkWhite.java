package com.kits.project.serverListConfig.whiteList.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 白名单对象 sdk_white
 * 
 * @author gm
 * @date 2021-04-26
 */
public class SdkWhite extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    @Excel(name = "ID")
    private Long id;

    /** 白名单账号 */
    @Excel(name = "白名单账号")
    private String whiteName;

    /** 备注 */
    @Excel(name = "备注")
    private String tips;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setWhiteName(String whiteName)
    {
        this.whiteName = whiteName;
    }

    public String getWhiteName()
    {
        return whiteName;
    }
    public void setTips(String tips)
    {
        this.tips = tips;
    }

    public String getTips()
    {
        return tips;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("whiteName", getWhiteName())
            .append("tips", getTips())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .toString();
    }
}
