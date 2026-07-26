package com.gm.project.gmtool.activity.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * Игровые события对象 t_activity
 * 
 * @author gm
 * @date 2021-09-07
 */
public class Activity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** ID события */
    @Excel(name = "ID события")
    private Integer id;

    /** Тип события */
    @Excel(name = "Тип события")
    private Integer type;

    /** Тип праздника */
    @Excel(name = "Тип праздника")
    private Integer subType;

    /** 最小开放Уровень */
    @Excel(name = "最小开放Уровень")
    private Integer minLv;

    /** 最大开放Уровень */
    @Excel(name = "最大开放Уровень")
    private Integer maxLv;

    /** 标签(用于区分展示在哪个Метка события下) */
    @Excel(name = "标签(用于区分展示在哪个Метка события下)")
    private Integer tag;

    /** 活动Сортировка */
    @Excel(name = "活动Сортировка")
    private Integer sort;

    /** Название события */
    @Excel(name = "Название события")
    private String name;

    /** 活动说明 */
    @Excel(name = "活动说明")
    private String description;

    /** Тип времени 0固定Время（配置Время）1Время открытия сервера变量（根据Время открытия сервера+Время变量计算） */
    @Excel(name = "Тип времени 0固定Время", readConverterExp = "配=置Время")
    private Integer timeType;

    /** 距离开服多少День */
    @Excel(name = "距离开服多少День")
    private Integer openServerOffsetBegin;

    /** 活动День数 */
    @Excel(name = "活动День数")
    private Integer openServerOffset;

    /** 活动Время начала */
    @Excel(name = "活动Время начала")
    private String beginTime;

    /** 活动Время окончания */
    @Excel(name = "活动Время окончания")
    private String endTime;

    /** 记录距离开服多少День */
    @Excel(name = "记录距离开服多少День")
    private Integer openServerRecordOffsetBegin;

    /** 活动记录持续День数 */
    @Excel(name = "活动记录持续День数")
    private Integer openServerRecordOffset;

    /** 开始记录Время */
    @Excel(name = "开始记录Время")
    private String startRecordTime;

    /** 结束记录Время */
    @Excel(name = "结束记录Время")
    private String endRecordTime;

    /** Статус события，0：未验证(Тестовый、Удалить)，1：已验证(发布、Удалить)，2：已发布(Удалить)，     //已过期(Удалить)这个通过活动Время окончания去判断 */
    @Excel(name = "Статус события，0：未验证(Тестовый、Удалить)，1：已验证(发布、Удалить)，2：已发布(Удалить)，     //已过期(Удалить)这个通过活动Время окончания去判断")
    private Integer state;

    /** 活动要发布到的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动要发布到的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..])")
    private String toSidList;

    /** 活动发布Успешно的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动发布Успешно的Игровой сервер列表(List JSON化后的字串[sid1,sid2,..])")
    private String okSidList;

    /** 活动ДаНет被Удалить，0：Нет，1：Да */
    @Excel(name = "活动ДаНет被Удалить，0：Нет，1：Да")
    private Integer isDeleted;

    /** Автопубликация при открытии活动标识，0：Нет，1：Да */
    @Excel(name = "Автопубликация при открытии活动标识，0：Нет，1：Да")
    private Integer autoSend;

    /** ДаНетДаСобытие нового сервера */
    @Excel(name = "ДаНетДаСобытие нового сервера")
    private Integer isOpenServer;

    /** 自定义参数 */
    @Excel(name = "自定义参数")
    private String custom;

    /** 活动ДаНет被覆盖正在进行的活动，0：Нет，1：Да */
    @Excel(name = "活动ДаНет被覆盖正在进行的活动，0：Нет，1：Да")
    private Integer cover;

    /** 配置参数，用于GM后台反解析 */
    @Excel(name = "配置参数，用于GM后台反解析")
    private String configData;

    public Activity() {

    }

    public Activity(Activity activity) {
        this.setId(activity.getId());
        this.setType(activity.getType());
        this.setSubType(activity.getSubType());
        this.setMinLv(activity.getMinLv());
        this.setMaxLv(activity.getMaxLv());
        this.setTag(activity.getTag());
        this.setSort(activity.getSort());
        this.setName(activity.getName());
        this.setDescription(activity.getDescription());
        this.setTimeType(activity.getTimeType());
        this.setOpenServerOffsetBegin(activity.getOpenServerOffsetBegin());
        this.setOpenServerOffset(activity.getOpenServerOffset());
        this.setBeginTime(activity.getBeginTime());
        this.setEndTime(activity.getEndTime());
        this.setOpenServerRecordOffsetBegin(activity.getOpenServerRecordOffsetBegin());
        this.setOpenServerRecordOffset(activity.getOpenServerRecordOffset());
        this.setStartRecordTime(activity.getStartRecordTime());
        this.setEndRecordTime(activity.getEndRecordTime());
        this.setState(activity.getState());
        this.setToSidList(activity.getToSidList());
        this.setOkSidList(activity.getOkSidList());
        this.setIsDeleted(activity.getIsDeleted());
        this.setAutoSend(activity.getAutoSend());
        this.setIsOpenServer(activity.getIsOpenServer());
        this.setCover(activity.getCover());
        this.setConfigData(activity.getConfigData());
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
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
    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDescription()
    {
        return description;
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
    public void setState(Integer state)
    {
        this.state = state;
    }

    public Integer getState()
    {
        return state;
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
    public void setCover(Integer cover)
    {
        this.cover = cover;
    }

    public Integer getCover()
    {
        return cover;
    }
    public void setConfigData(String configData)
    {
        this.configData = configData;
    }

    public String getConfigData()
    {
        return configData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("type", getType())
            .append("subType", getSubType())
            .append("minLv", getMinLv())
            .append("maxLv", getMaxLv())
            .append("tag", getTag())
            .append("sort", getSort())
            .append("name", getName())
            .append("description", getDescription())
            .append("timeType", getTimeType())
            .append("openServerOffsetBegin", getOpenServerOffsetBegin())
            .append("openServerOffset", getOpenServerOffset())
            .append("beginTime", getBeginTime())
            .append("endTime", getEndTime())
            .append("openServerRecordOffsetBegin", getOpenServerRecordOffsetBegin())
            .append("openServerRecordOffset", getOpenServerRecordOffset())
            .append("startRecordTime", getStartRecordTime())
            .append("endRecordTime", getEndRecordTime())
            .append("state", getState())
            .append("toSidList", getToSidList())
            .append("okSidList", getOkSidList())
            .append("isDeleted", getIsDeleted())
            .append("autoSend", getAutoSend())
            .append("isOpenServer", getIsOpenServer())
            .append("custom", getCustom())
            .append("cover", getCover())
            .append("configData", getConfigData())
            .toString();
    }
}
