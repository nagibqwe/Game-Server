package com.gm.project.monitor.job.domain;

import java.io.Serializable;
import java.util.Date;
import javax.validation.constraints.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.common.constant.ScheduleConstants;
import com.gm.common.utils.StringUtils;
import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.aspectj.lang.annotation.Excel.ColumnType;
import com.gm.framework.web.domain.BaseEntity;
import com.gm.project.monitor.job.util.CronUtils;

/**
 * Планировщик задач调度Информация sys_job
 * 
 * @author ruoyi
 */
public class Job extends BaseEntity implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 任务ID */
    @Excel(name = "任务№", cellType = ColumnType.NUMERIC)
    private Long jobId;

    /** Название задачи */
    @Excel(name = "Название задачи")
    private String jobName;

    /** 任务组名 */
    @Excel(name = "任务组名")
    private String jobGroup;

    /** Строка вызова */
    @Excel(name = "Строка вызова")
    private String invokeTarget;

    /** cron执行表达式 */
    @Excel(name = "执行表达式 ")
    private String cronExpression;

    /** cron计划策略 */
    @Excel(name = "计划策略 ", readConverterExp = "0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行")
    private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

    /** ДаНет并发执行（0允许 1禁止） */
    @Excel(name = "并发执行", readConverterExp = "0=允许,1=禁止")
    private String concurrent;

    /** 任务Статус（0Норма 1暂停） */
    @Excel(name = "任务Статус", readConverterExp = "0=Норма,1=暂停")
    private String status;

    public Long getJobId()
    {
        return jobId;
    }

    public void setJobId(Long jobId)
    {
        this.jobId = jobId;
    }

    @NotBlank(message = "Название задачи不能为空")
    @Size(min = 0, max = 64, message = "Название задачи不能超过64个字符")
    public String getJobName()
    {
        return jobName;
    }

    public void setJobName(String jobName)
    {
        this.jobName = jobName;
    }

    public String getJobGroup()
    {
        return jobGroup;
    }

    public void setJobGroup(String jobGroup)
    {
        this.jobGroup = jobGroup;
    }

    @NotBlank(message = "Строка вызова不能为空")
    @Size(min = 0, max = 1000, message = "Строка вызова长度不能超过500个字符")
    public String getInvokeTarget()
    {
        return invokeTarget;
    }

    public void setInvokeTarget(String invokeTarget)
    {
        this.invokeTarget = invokeTarget;
    }

    @NotBlank(message = "Cron执行表达式不能为空")
    @Size(min = 0, max = 255, message = "Cron执行表达式不能超过255个字符")
    public String getCronExpression()
    {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression)
    {
        this.cronExpression = cronExpression;
    }

    public Date getNextValidTime()
    {
        if (StringUtils.isNotEmpty(cronExpression))
        {
            return CronUtils.getNextExecution(cronExpression);
        }
        return null;
    }

    public String getMisfirePolicy()
    {
        return misfirePolicy;
    }

    public void setMisfirePolicy(String misfirePolicy)
    {
        this.misfirePolicy = misfirePolicy;
    }

    public String getConcurrent()
    {
        return concurrent;
    }

    public void setConcurrent(String concurrent)
    {
        this.concurrent = concurrent;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("jobId", getJobId())
            .append("jobName", getJobName())
            .append("jobGroup", getJobGroup())
            .append("cronExpression", getCronExpression())
            .append("nextValidTime", getNextValidTime())
            .append("misfirePolicy", getMisfirePolicy())
            .append("concurrent", getConcurrent())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .toString();
    }
}