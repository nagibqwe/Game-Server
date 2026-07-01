package com.gm.project.stat.stat_dau.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import com.gm.framework.web.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;


/**
 *
 *
 * @author gm
 * @date 2021-08-06
 */
public class StatDauBean extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 等级 */
    @Excel(name = "日期")
    private String day;

    /** dau */
    @Excel(name = "dau")
    private Integer dauNum;

    public StatDauBean() {
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("day", getDay())
            .append("dauNum", getDauNum())
            .toString();
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Integer getDauNum() {
        return dauNum;
    }

    public void setDauNum(Integer dauNum) {
        this.dauNum = dauNum;
    }
}
