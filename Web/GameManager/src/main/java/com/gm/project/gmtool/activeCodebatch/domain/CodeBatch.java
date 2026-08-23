package com.gm.project.gmtool.activeCodebatch.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Объект пакетов кодов активации t_code_batch
 * 
 * @author gm
 * @date 2021-09-22
 */
public class CodeBatch extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Integer id;

    /** Номер пакета */
    @Excel(name = "Номер пакета")
    private Integer batchId;

    /** ID аккаунта */
    @Excel(name = "ID аккаунта")
    private Long userId;

    /** Время */
    @Excel(name = "Время")
    private Long time;

    /** Название платформы */
    @Excel(name = "Название платформы")
    private String platform;

    /** Универсальный код */
    @Excel(name = "Универсальный код")
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