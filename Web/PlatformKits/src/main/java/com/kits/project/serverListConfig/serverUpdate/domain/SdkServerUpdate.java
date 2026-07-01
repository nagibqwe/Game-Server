package com.kits.project.serverListConfig.serverUpdate.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 服务器信息修改时间记录对象 sdk_server_update
 * 
 * @author gm
 * @date 2021-12-08
 */
public class SdkServerUpdate
{
    private static final long serialVersionUID = 1L;

    private Long updateTime;

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
