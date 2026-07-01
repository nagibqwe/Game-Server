package com.gm.project.gmtool.activityLuckyValue.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 抽奖幸运值对象 t_activity_lucky_value
 * 
 * @author gm
 * @date 2021-09-16
 */
public class ActivityLuckyValue extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Integer id;

    /** 总幸运值 */
    @Excel(name = "总幸运值")
    private Integer totalLuckyValue;

    /** 备注说明 */
    @Excel(name = "备注说明")
    private String tips;

    /** 活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断 */
    @Excel(name = "活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断")
    private Integer state;

    /** 活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..]) */
    @Excel(name = "活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])")
    private String platform;

    /** 活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])")
    private String toSidList;

    /** 活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])")
    private String okSidList;

    /** 活动是否被删除，0：否，1：是 */
    @Excel(name = "活动是否被删除，0：否，1：是")
    private Integer isDeleted;

    /** 活动是否被覆盖正在进行的活动，0：否，1：是 */
    @Excel(name = "活动是否被覆盖正在进行的活动，0：否，1：是")
    private Integer cover;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setTotalLuckyValue(Integer totalLuckyValue)
    {
        this.totalLuckyValue = totalLuckyValue;
    }

    public Integer getTotalLuckyValue()
    {
        return totalLuckyValue;
    }
    public void setTips(String tips)
    {
        this.tips = tips;
    }

    public String getTips()
    {
        return tips;
    }
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
    }
    public void setPlatform(String platform)
    {
        this.platform = platform;
    }

    public String getPlatform()
    {
        return platform;
    }
    public void setToSidList(String toSidList)
    {
        this.toSidList = toSidList;
    }

    public String getToSidList()
    {
        return toSidList;
    }
    public void setOkSidList(String okSidList)
    {
        this.okSidList = okSidList;
    }

    public String getOkSidList()
    {
        return okSidList;
    }
    public void setIsDeleted(Integer isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public Integer getIsDeleted()
    {
        return isDeleted;
    }
    public void setCover(Integer cover)
    {
        this.cover = cover;
    }

    public Integer getCover()
    {
        return cover;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("totalLuckyValue", getTotalLuckyValue())
            .append("tips", getTips())
            .append("state", getState())
            .append("platform", getPlatform())
            .append("toSidList", getToSidList())
            .append("okSidList", getOkSidList())
            .append("isDeleted", getIsDeleted())
            .append("cover", getCover())
            .toString();
    }
}
