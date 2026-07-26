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

    /** ID события */
    @Excel(name = "ID события")
    private Integer id;

    /** 总幸运值 */
    @Excel(name = "总幸运值")
    private Integer totalLuckyValue;

    /** Примечание说明 */
    @Excel(name = "Примечание说明")
    private String tips;

    /** Статус события，0：未验证(Тестовый、Удалить)，1：已验证(发布、Удалить)，2：已发布(Удалить)，     //已过期(Удалить)这个通过活动Время окончания去判断 */
    @Excel(name = "Статус события，0：未验证(Тестовый、Удалить)，1：已验证(发布、Удалить)，2：已发布(Удалить)，     //已过期(Удалить)这个通过活动Время окончания去判断")
    private Integer state;

    /** 活动发布Платформа(groupName)(List JSON化后的字串[plat1,plat2,..]) */
    @Excel(name = "活动发布Платформа(groupName)(List JSON化后的字串[plat1,plat2,..])")
    private String platform;

    /** 活动要发布到的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动要发布到的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..])")
    private String toSidList;

    /** 活动发布Успешно的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动发布Успешно的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..])")
    private String okSidList;

    /** 活动ДаНет被Удалить，0：Нет，1：Да */
    @Excel(name = "活动ДаНет被Удалить，0：Нет，1：Да")
    private Integer isDeleted;

    /** 活动ДаНет被覆盖正在进行的活动，0：Нет，1：Да */
    @Excel(name = "活动ДаНет被覆盖正在进行的活动，0：Нет，1：Да")
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
