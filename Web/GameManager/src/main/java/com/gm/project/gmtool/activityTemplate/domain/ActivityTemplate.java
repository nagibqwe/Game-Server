package com.gm.project.gmtool.activityTemplate.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Шаблоны событий对象 t_activity_template
 * 
 * @author gm
 * @date 2021-09-07
 */
public class ActivityTemplate
{
    private static final long serialVersionUID = 1L;

    /** 活动模板ID */
//    @Excel(name = "id")
    private Integer id;

    /** 模版Название */
    @Excel(name = "templateName")
    private String templateName;

    /** 活动模板Время создания */
    @Excel(name = "createTime")
    private String createTime;

    /** Тип события */
    @Excel(name = "type")
    private Integer type;

    /** 活动子Тип */
    @Excel(name = "subType")
    private Integer subType;

    /** 最小开放Уровень */
    @Excel(name = "minLv")
    private Integer minLv;

    /** 最大开放Уровень */
    @Excel(name = "maxLv")
    private Integer maxLv;

    /** 标签(用于区分展示在哪个Метка события下) */
    @Excel(name = "tag")
    private Integer tag;

    /** 活动Сортировка */
    @Excel(name = "sort")
    private Integer sort;

    /** Название события */
    @Excel(name = "name")
    private String name;

    /** Тип времени 0固定Время（配置Время） 1Время открытия сервера变量（根据Время открытия сервера+Время变量计算） */
    @Excel(name = "timeType")
    private Integer timeType;

    /** 距离开服多少День */
    @Excel(name = "openServerOffsetBegin")
    private Integer openServerOffsetBegin;

    /** 活动День数 */
    @Excel(name = "openServerOffset")
    private Integer openServerOffset;

    /** 活动Время начала */
    @Excel(name = "beginTime")
    private String beginTime;

    /** 活动Время окончания */
    @Excel(name = "endTime")
    private String endTime;

    /** 记录距离开服多少День */
    @Excel(name = "openServerRecordOffsetBegin")
    private Integer openServerRecordOffsetBegin;

    /** 活动记录持续День数 */
    @Excel(name = "openServerRecordOffset")
    private Integer openServerRecordOffset;

    /** 开始记录Время */
    @Excel(name = "startRecordTime")
    private String startRecordTime;

    /** 结束记录Время */
    @Excel(name = "endRecordTime")
    private String endRecordTime;

    /** Автопубликация при открытии活动标识，0：Нет，1：Да */
    @Excel(name = "autoSend")
    private Integer autoSend;

    /** ДаНетДаСобытие нового сервера */
    @Excel(name = "isOpenServer")
    private Integer isOpenServer;

    /** 自定义参数 */
    @Excel(name = "custom")
    private String custom;

    /** 模板描述 */
    @Excel(name = "templateDec")
    private String templateDec;

    /** 活动说明 */
    @Excel(name = "description")
    private String description;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setTemplateName(String templateName)
    {
        this.templateName = templateName;
    }

    public String getTemplateName()
    {
        return templateName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setSubType(Integer subType)
    {
        this.subType = subType;
    }

    public Integer getSubType()
    {
        return subType;
    }
    public void setMinLv(Integer minLv)
    {
        this.minLv = minLv;
    }

    public Integer getMinLv()
    {
        return minLv;
    }
    public void setMaxLv(Integer maxLv)
    {
        this.maxLv = maxLv;
    }

    public Integer getMaxLv()
    {
        return maxLv;
    }
    public void setTag(Integer tag)
    {
        this.tag = tag;
    }

    public Integer getTag()
    {
        return tag;
    }
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }

    public Integer getSort()
    {
        return sort;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setTimeType(Integer timeType)
    {
        this.timeType = timeType;
    }

    public Integer getTimeType()
    {
        return timeType;
    }
    public void setOpenServerOffsetBegin(Integer openServerOffsetBegin)
    {
        this.openServerOffsetBegin = openServerOffsetBegin;
    }

    public Integer getOpenServerOffsetBegin()
    {
        return openServerOffsetBegin;
    }
    public void setOpenServerOffset(Integer openServerOffset)
    {
        this.openServerOffset = openServerOffset;
    }

    public Integer getOpenServerOffset()
    {
        return openServerOffset;
    }
    public void setBeginTime(String beginTime)
    {
        this.beginTime = beginTime;
    }

    public String getBeginTime()
    {
        return beginTime;
    }
    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getEndTime()
    {
        return endTime;
    }
    public void setOpenServerRecordOffsetBegin(Integer openServerRecordOffsetBegin)
    {
        this.openServerRecordOffsetBegin = openServerRecordOffsetBegin;
    }

    public Integer getOpenServerRecordOffsetBegin()
    {
        return openServerRecordOffsetBegin;
    }
    public void setOpenServerRecordOffset(Integer openServerRecordOffset)
    {
        this.openServerRecordOffset = openServerRecordOffset;
    }

    public Integer getOpenServerRecordOffset()
    {
        return openServerRecordOffset;
    }
    public void setStartRecordTime(String startRecordTime)
    {
        this.startRecordTime = startRecordTime;
    }

    public String getStartRecordTime()
    {
        return startRecordTime;
    }
    public void setEndRecordTime(String endRecordTime)
    {
        this.endRecordTime = endRecordTime;
    }

    public String getEndRecordTime()
    {
        return endRecordTime;
    }
    public void setAutoSend(Integer autoSend)
    {
        this.autoSend = autoSend;
    }

    public Integer getAutoSend()
    {
        return autoSend;
    }
    public void setIsOpenServer(Integer isOpenServer)
    {
        this.isOpenServer = isOpenServer;
    }

    public Integer getIsOpenServer()
    {
        return isOpenServer;
    }
    public void setCustom(String custom)
    {
        this.custom = custom;
    }

    public String getCustom()
    {
        return custom;
    }

    public String getTemplateDec() {
        return templateDec;
    }

    public void setTemplateDec(String templateDec) {
        this.templateDec = templateDec;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("templateName", getTemplateName())
            .append("createTime", getCreateTime())
            .append("type", getType())
            .append("subType", getSubType())
            .append("minLv", getMinLv())
            .append("maxLv", getMaxLv())
            .append("tag", getTag())
            .append("sort", getSort())
            .append("name", getName())
            .append("timeType", getTimeType())
            .append("openServerOffsetBegin", getOpenServerOffsetBegin())
            .append("openServerOffset", getOpenServerOffset())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("openServerRecordOffsetBegin", getOpenServerRecordOffsetBegin())
            .append("openServerRecordOffset", getOpenServerRecordOffset())
            .append("startRecordTime", getStartRecordTime())
            .append("endRecordTime", getEndRecordTime())
            .append("autoSend", getAutoSend())
            .append("isOpenServer", getIsOpenServer())
            .append("custom", getCustom())
            .append("templateDec", getTemplateDec())
            .append("description", getDescription())
            .toString();
    }
}
