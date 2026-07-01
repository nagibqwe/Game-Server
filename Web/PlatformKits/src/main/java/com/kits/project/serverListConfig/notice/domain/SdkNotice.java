package com.kits.project.serverListConfig.notice.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;

import java.util.Date;


/**
 * 公告管理对象 sdk_notice
 * 
 * @author gm
 * @date 2021-06-22
 */
public class SdkNotice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 公告ID */
    private Long noticeId;

    /** 公告名 */
    @Excel(name = "公告名")
    private String noticeName;

    /** 公告类型(0:更新公告 1:登陆公告 2:活动公告) */
    @Excel(name = "公告类型(0:更新公告 1:登陆公告 2:活动公告)")
    private Long noticeType;

    /** 公告内容 */
    @Excel(name = "公告内容")
    private String noticeContent;

    /** 渠道 */
    @Excel(name = "渠道")
    private String channel;

    /** 是否自动弹出 0:不自动弹出 1:自动弹出 */
    @Excel(name = "是否自动弹出 0:不自动弹出 1:自动弹出")
    private Long auto;

    /** 开始时间 */
    @Excel(name = "开始时间")
    private Date startTime;

    /** 结束时间 */
    @Excel(name = "结束时间")
    private Date endTime;

    /** 0:停用 1:启用 */
    @Excel(name = "0:停用 1:启用")
    private Long status;

    public void setNoticeId(Long noticeId)
    {
        this.noticeId = noticeId;
    }

    public Long getNoticeId()
    {
        return noticeId;
    }
    public void setNoticeName(String noticeName)
    {
        this.noticeName = noticeName;
    }

    public String getNoticeName()
    {
        return noticeName;
    }
    public void setNoticeType(Long noticeType)
    {
        this.noticeType = noticeType;
    }

    public Long getNoticeType()
    {
        return noticeType;
    }
    public void setNoticeContent(String noticeContent)
    {
        this.noticeContent = noticeContent;
    }

    public String getNoticeContent()
    {
        return noticeContent;
    }
    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    public String getChannel()
    {
        return channel;
    }
    public void setAuto(Long auto)
    {
        this.auto = auto;
    }

    public Long getAuto()
    {
        return auto;
    }
    public void setStartTime(Date startTime)
    {
        this.startTime = startTime;
    }

    public Date getStartTime()
    {
        return startTime;
    }
    public void setEndTime(Date endTime)
    {
        this.endTime = endTime;
    }

    public Date getEndTime()
    {
        return endTime;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("noticeId", getNoticeId())
            .append("noticeName", getNoticeName())
            .append("noticeType", getNoticeType())
            .append("noticeContent", getNoticeContent())
            .append("channel", getChannel())
            .append("remark", getRemark())
            .append("auto", getAuto())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("status", getStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
