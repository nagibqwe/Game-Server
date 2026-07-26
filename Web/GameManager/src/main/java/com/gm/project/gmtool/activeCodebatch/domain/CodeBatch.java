package com.gm.project.gmtool.activeCodebatch.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Пакет кодов активации对象 t_code_batch
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

    /** Платформа */
    @Excel(name = "Платформа")
    private String platform;

    /** ДаНет为万能码 */
    @Excel(name = "ДаНет为万能码")
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
