package com.gm.project.gamelog.ranklistlog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 排行榜日志对象 log_ranklistlog
 * 
 * @author gm
 * @date 2021-09-08
 */
public class Ranklistlog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Long id;

    /** date */
    @Excel(name = "date")
    private String date;

    /** 排行榜类型 */
    @Excel(name = "排行榜类型")
    private Integer rankKind;

    /** 排名 */
    @Excel(name = "排名")
    private Integer rank;

    /** 角色Id */
    @Excel(name = "角色Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    /** 角色名 */
    @Excel(name = "角色名")
    private String roleName;

    /** 排行数据 */
    @Excel(name = "排行数据")
    private String rankData;

    /** 渠道 */
    @Excel(name = "渠道")
    private String platformName;

    /** 时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "时间")
    private Long time;


    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setDate(String date)
    {
        this.date = date;
    }

    public String getDate()
    {
        return date;
    }
    public void setRankKind(Integer rankKind)
    {
        this.rankKind = rankKind;
    }

    public Integer getRankKind()
    {
        return rankKind;
    }
    public void setRank(Integer rank)
    {
        this.rank = rank;
    }

    public Integer getRank()
    {
        return rank;
    }
    public void setRoleId(Long roleId)
    {
        this.roleId = roleId;
    }

    public Long getRoleId()
    {
        return roleId;
    }
    public void setRoleName(String roleName)
    {
        this.roleName = roleName;
    }

    public String getRoleName()
    {
        return roleName;
    }
    public void setRankData(String rankData)
    {
        this.rankData = rankData;
    }

    public String getRankData()
    {
        return rankData;
    }
    public void setPlatformName(String platformName)
    {
        this.platformName = platformName;
    }

    public String getPlatformName()
    {
        return platformName;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("date", getDate())
            .append("rankKind", getRankKind())
            .append("rank", getRank())
            .append("roleId", getRoleId())
            .append("roleName", getRoleName())
            .append("rankData", getRankData())
            .append("platformName", getPlatformName())
            .append("time", getTime())
            .toString();
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
