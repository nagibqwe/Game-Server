package com.gm.project.gmtool.cyAnnounce.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 循环公告对象 t_cyannounce
 * 
 * @author gm
 * @date 2021-10-27
 */
public class CyAnnounce
{
    private static final long serialVersionUID = 1L;

    /** 公告的编号 */
    private Integer id;

    /** 公告的平台分组 */
    @Excel(name = "公告的平台分组")
    private String groupName;

    /** 公告的发送的服务器列表 */
    @Excel(name = "公告的发送的服务器列表")
    private String serverIds;

    /** 公告的标识 */
    @Excel(name = "公告的标识")
    private String batchTag;

    /** 公告的内容 */
    @Excel(name = "公告的内容")
    private String content;

    /** 公告的创建时间 */
    @Excel(name = "公告的创建时间")
    private Long createTime;

    /** 公告的创建时间字符格式化 */
    @Excel(name = "公告的创建时间字符格式化")
    private String createDate;

    /** 公告的添加者ID */
    @Excel(name = "公告的添加者ID")
    private Integer createUserId;

    /** 公告的添加者名字 */
    @Excel(name = "公告的添加者名字")
    private String createUserName;

    /** 公告的开始时间 */
    @Excel(name = "公告的开始时间")
    private Long fromTime;

    /** 公告的开始字符格式化 */
    @Excel(name = "公告的开始字符格式化")
    private String fromDate;

    /** 公告的结束时间 */
    @Excel(name = "公告的结束时间")
    private Long toTime;

    /** 公告的结束时间字符格式化 */
    @Excel(name = "公告的结束时间字符格式化")
    private String toDate;

    /** 公告发送的总次数 */
    @Excel(name = "公告发送的总次数")
    private Integer totalTimes;

    /** 公告的当前已经发送的次数 */
    @Excel(name = "公告的当前已经发送的次数")
    private Long nowTimes;

    /** 公告的下一次发送的时间 */
    @Excel(name = "公告的下一次发送的时间")
    private Long nextTimes;

    /** 公告的下一次发送时间字符格式化 */
    @Excel(name = "公告的下一次发送时间字符格式化")
    private String nextDate;

    /** 公告的当前状态，启用还是禁用 */
    @Excel(name = "公告的当前状态，启用还是禁用")
    private Integer state;

    /** 公告发送的频率 */
    @Excel(name = "公告发送的频率")
    private Integer cycleInterval;

    /** 公告发送的位置 */
    @Excel(name = "公告发送的位置")
    private Integer type;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setGroupName(String groupName)
    {
        this.groupName = groupName;
    }

    public String getGroupName()
    {
        return groupName;
    }
    public void setServerIds(String serverIds)
    {
        this.serverIds = serverIds;
    }

    public String getServerIds()
    {
        return serverIds;
    }
    public void setBatchTag(String batchTag)
    {
        this.batchTag = batchTag;
    }

    public String getBatchTag()
    {
        return batchTag;
    }
    public void setContent(String content)
    {
        this.content = content;
    }

    public String getContent()
    {
        return content;
    }
    public void setCreateDate(String createDate)
    {
        this.createDate = createDate;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate()
    {
        return createDate;
    }
    public void setCreateUserId(Integer createUserId)
    {
        this.createUserId = createUserId;
    }

    public Integer getCreateUserId()
    {
        return createUserId;
    }
    public void setCreateUserName(String createUserName)
    {
        this.createUserName = createUserName;
    }

    public String getCreateUserName()
    {
        return createUserName;
    }
    public void setFromTime(Long fromTime)
    {
        this.fromTime = fromTime;
    }

    public Long getFromTime()
    {
        return fromTime;
    }
    public void setFromDate(String fromDate)
    {
        this.fromDate = fromDate;
    }

    public String getFromDate()
    {
        return fromDate;
    }
    public void setToTime(Long toTime)
    {
        this.toTime = toTime;
    }

    public Long getToTime()
    {
        return toTime;
    }
    public void setToDate(String toDate)
    {
        this.toDate = toDate;
    }

    public String getToDate()
    {
        return toDate;
    }
    public void setTotalTimes(Integer totalTimes)
    {
        this.totalTimes = totalTimes;
    }

    public Integer getTotalTimes()
    {
        return totalTimes;
    }
    public void setNowTimes(Long nowTimes)
    {
        this.nowTimes = nowTimes;
    }

    public Long getNowTimes()
    {
        return nowTimes;
    }
    public void setNextTimes(Long nextTimes)
    {
        this.nextTimes = nextTimes;
    }

    public Long getNextTimes()
    {
        return nextTimes;
    }
    public void setNextDate(String nextDate)
    {
        this.nextDate = nextDate;
    }

    public String getNextDate()
    {
        return nextDate;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }
    public void setCycleInterval(Integer cycleInterval)
    {
        this.cycleInterval = cycleInterval;
    }

    public Integer getCycleInterval()
    {
        return cycleInterval;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("groupName", getGroupName())
            .append("serverIds", getServerIds())
            .append("batchTag", getBatchTag())
            .append("content", getContent())
            .append("createTime", getCreateTime())
            .append("createDate", getCreateDate())
            .append("createUserId", getCreateUserId())
            .append("createUserName", getCreateUserName())
            .append("fromTime", getFromTime())
            .append("fromDate", getFromDate())
            .append("toTime", getToTime())
            .append("toDate", getToDate())
            .append("totalTimes", getTotalTimes())
            .append("nowTimes", getNowTimes())
            .append("nextTimes", getNextTimes())
            .append("nextDate", getNextDate())
            .append("state", getState())
            .append("cycleInterval", getCycleInterval())
            .append("type", getType())
            .toString();
    }
}
