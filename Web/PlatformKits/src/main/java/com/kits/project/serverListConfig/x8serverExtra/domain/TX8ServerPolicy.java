package com.kits.project.serverListConfig.x8serverExtra.domain;

import com.kits.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.kits.framework.web.domain.BaseEntity;


/**
 * 渠道区服明细对象 t_x8_server_policy
 * 
 * @author gm
 * @date 2021-07-08
 */
public class TX8ServerPolicy extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 策略编号 */
    private Long policyId;

    /** 游戏ID */
    @Excel(name = "游戏ID")
    private Long gameId;

    /** 渠道编号 */
    @Excel(name = "渠道编号")
    private Long chnId;

    /** 区服编号 */
    @Excel(name = "区服编号")
    private Long svrId;

    /** 排序号 */
    @Excel(name = "排序号")
    private Long sortId;

    /** 状态 */
    @Excel(name = "状态")
    private Long svrStatus;

    /** 热度标签 */
    @Excel(name = "热度标签")
    private String svrTag;

    /** 场景ID */
    @Excel(name = "场景ID")
    private String sceId;

    /** 预变更状态 */
    @Excel(name = "预变更状态")
    private Long svrPreStatus;

    /** 预置更新热度标签 */
    @Excel(name = "预置更新热度标签")
    private String svrPreTag;

    /** 预置状态更新时间cron */
    @Excel(name = "预置状态更新时间cron")
    private String svrPreStatusDate;

    /** 预置标签更新时间cron */
    @Excel(name = "预置标签更新时间cron")
    private String svrPreTagDate;

    public void setPolicyId(Long policyId)
    {
        this.policyId = policyId;
    }

    public Long getPolicyId()
    {
        return policyId;
    }
    public void setGameId(Long gameId)
    {
        this.gameId = gameId;
    }

    public Long getGameId()
    {
        return gameId;
    }
    public void setChnId(Long chnId)
    {
        this.chnId = chnId;
    }

    public Long getChnId()
    {
        return chnId;
    }
    public void setSvrId(Long svrId)
    {
        this.svrId = svrId;
    }

    public Long getSvrId()
    {
        return svrId;
    }
    public void setSortId(Long sortId)
    {
        this.sortId = sortId;
    }

    public Long getSortId()
    {
        return sortId;
    }
    public void setSvrStatus(Long svrStatus)
    {
        this.svrStatus = svrStatus;
    }

    public Long getSvrStatus()
    {
        return svrStatus;
    }
    public void setSvrTag(String svrTag)
    {
        this.svrTag = svrTag;
    }

    public String getSvrTag()
    {
        return svrTag;
    }
    public void setSceId(String sceId)
    {
        this.sceId = sceId;
    }

    public String getSceId()
    {
        return sceId;
    }
    public void setSvrPreStatus(Long svrPreStatus)
    {
        this.svrPreStatus = svrPreStatus;
    }

    public Long getSvrPreStatus()
    {
        return svrPreStatus;
    }
    public void setSvrPreTag(String svrPreTag)
    {
        this.svrPreTag = svrPreTag;
    }

    public String getSvrPreTag()
    {
        return svrPreTag;
    }
    public void setSvrPreStatusDate(String svrPreStatusDate)
    {
        this.svrPreStatusDate = svrPreStatusDate;
    }

    public String getSvrPreStatusDate()
    {
        return svrPreStatusDate;
    }
    public void setSvrPreTagDate(String svrPreTagDate)
    {
        this.svrPreTagDate = svrPreTagDate;
    }

    public String getSvrPreTagDate()
    {
        return svrPreTagDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("policyId", getPolicyId())
            .append("gameId", getGameId())
            .append("chnId", getChnId())
            .append("svrId", getSvrId())
            .append("sortId", getSortId())
            .append("svrStatus", getSvrStatus())
            .append("svrTag", getSvrTag())
            .append("updateTime", getUpdateTime())
            .append("createTime", getCreateTime())
            .append("sceId", getSceId())
            .append("svrPreStatus", getSvrPreStatus())
            .append("svrPreTag", getSvrPreTag())
            .append("svrPreStatusDate", getSvrPreStatusDate())
            .append("svrPreTagDate", getSvrPreTagDate())
            .toString();
    }
}
