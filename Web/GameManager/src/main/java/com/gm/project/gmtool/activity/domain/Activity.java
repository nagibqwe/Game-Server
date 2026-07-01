package com.gm.project.gmtool.activity.domain;

import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * 运营活动对象 t_activity
 * 
 * @author gm
 * @date 2021-09-07
 */
public class Activity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 活动ID */
    @Excel(name = "活动ID")
    private Integer id;

    /** 活动类型 */
    @Excel(name = "活动类型")
    private Integer type;

    /** 节日类型 */
    @Excel(name = "节日类型")
    private Integer subType;

    /** 最小开放等级 */
    @Excel(name = "最小开放等级")
    private Integer minLv;

    /** 最大开放等级 */
    @Excel(name = "最大开放等级")
    private Integer maxLv;

    /** 标签(用于区分展示在哪个活动标签下) */
    @Excel(name = "标签(用于区分展示在哪个活动标签下)")
    private Integer tag;

    /** 活动排序 */
    @Excel(name = "活动排序")
    private Integer sort;

    /** 活动名称 */
    @Excel(name = "活动名称")
    private String name;

    /** 活动说明 */
    @Excel(name = "活动说明")
    private String description;

    /** 时间类型 0固定时间（配置时间）1开服时间变量（根据开服时间+时间变量计算） */
    @Excel(name = "时间类型 0固定时间", readConverterExp = "配=置时间")
    private Integer timeType;

    /** 距离开服多少天 */
    @Excel(name = "距离开服多少天")
    private Integer openServerOffsetBegin;

    /** 活动天数 */
    @Excel(name = "活动天数")
    private Integer openServerOffset;

    /** 活动开始时间 */
    @Excel(name = "活动开始时间")
    private String beginTime;

    /** 活动结束时间 */
    @Excel(name = "活动结束时间")
    private String endTime;

    /** 记录距离开服多少天 */
    @Excel(name = "记录距离开服多少天")
    private Integer openServerRecordOffsetBegin;

    /** 活动记录持续天数 */
    @Excel(name = "活动记录持续天数")
    private Integer openServerRecordOffset;

    /** 开始记录时间 */
    @Excel(name = "开始记录时间")
    private String startRecordTime;

    /** 结束记录时间 */
    @Excel(name = "结束记录时间")
    private String endRecordTime;

    /** 活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断 */
    @Excel(name = "活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断")
    private Integer state;

    /** 活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])")
    private String toSidList;

    /** 活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..]) */
    @Excel(name = "活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])")
    private String okSidList;

    /** 活动是否被删除，0：否，1：是 */
    @Excel(name = "活动是否被删除，0：否，1：是")
    private Integer isDeleted;

    /** 开服自动发布活动标识，0：否，1：是 */
    @Excel(name = "开服自动发布活动标识，0：否，1：是")
    private Integer autoSend;

    /** 是否是新服活动 */
    @Excel(name = "是否是新服活动")
    private Integer isOpenServer;

    /** 自定义参数 */
    @Excel(name = "自定义参数")
    private String custom;

    /** 活动是否被覆盖正在进行的活动，0：否，1：是 */
    @Excel(name = "活动是否被覆盖正在进行的活动，0：否，1：是")
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
