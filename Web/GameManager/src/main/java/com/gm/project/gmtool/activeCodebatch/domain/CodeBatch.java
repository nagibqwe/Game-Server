package com.gm.project.gmtool.activeCodebatch.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 激活码批次号对象 t_code_batch
 * 
 * @author gm
 * @date 2021-09-22
 */
public class CodeBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** 批次号 */
    @Excel(name = "批次号")
    private Integer batchId;

    /** 账号ID */
    @Excel(name = "账号ID")
    private Long userId;

    /** 时间 */
    @Excel(name = "时间")
    private Long time;

    /** 平台名 */
    @Excel(name = "平台名")
    private String platform;

    /** 是否为万能码 */
    @Excel(name = "是否为万能码")
    private Integer isUniversal;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setBatchId(Integer batchId)
    {
        this.batchId = batchId;
    }

    public Integer getBatchId()
    {
        return batchId;
    }
    public void setUserId(Long userId)
    {
        this.userId = userId;
    }

    public Long getUserId()
    {
        return userId;
    }
    public void setTime(Long time)
    {
        this.time = time;
    }

    public Long getTime()
    {
        return time;
    }
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPlatform()
    {
        return platform;
    }
    public void setIsUniversal(Integer isUniversal)
    {
        this.isUniversal = isUniversal;
    }

    public Integer getIsUniversal()
    {
        return isUniversal;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("batchId", getBatchId())
            .append("userId", getUserId())
            .append("time", getTime())
            .append("platform", getPlatform())
            .append("isUniversal", getIsUniversal())
            .toString();
    }
}
