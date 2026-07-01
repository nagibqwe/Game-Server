package com.kits.project.serverListConfig.apiserver.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * APIServer地址管理对象 sdk_apiserver
 * 
 * @author gm
 * @date 2021-06-24
 */
public class SdkApiserver extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID */
    private Long id;

    /** APIServer地址 */
    @Excel(name = "APIServer地址")
    private String apiUrl;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setApiUrl(String apiUrl)
    {
        this.apiUrl = apiUrl;
    }

    public String getApiUrl()
    {
        return apiUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("apiUrl", getApiUrl())
            .append("remark", getRemark())
            .toString();
    }
}
