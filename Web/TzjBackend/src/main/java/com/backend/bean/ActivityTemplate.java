package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

@Table("t_activity_template")
@TableIndexes(value = {@Index(fields = {"type", "createTime"})})
public class ActivityTemplate {

    @Id
    @Column
    @Comment("活动模板ID")
    private int id;

    @Column
    @Comment("模版名称")
    @ColDefine(type = ColType.VARCHAR, width = 50)
    private String templateName;

    @Column
    @Comment("活动模板创建时间")
    private String createTime;

    @Column
    @Comment("活动类型")
    private int type;

    @Column
    @Comment("活动子类型")
    private int subType;

    @Column
    @Comment("最小开放等级")
    private int minLv;

    @Column
    @Comment("最大开放等级")
    private int maxLv;

    @Column
    @Comment("标签(用于区分展示在哪个活动标签下)")
    private int tag;

    @Column
    @Comment("活动排序")
    private int sort;

    @Column
    @Comment("活动名称")
    private String name;

    @Column
    @Comment("时间类型 0固定时间（配置时间） 1开服时间变量（根据开服时间+时间变量计算）")
    private int timeType;

    @Column
    @Comment("距离开服多少天")
    private int openServerOffsetBegin;

    @Column
    @Comment("活动天数")
    private int openServerOffset;

    @Column
    @Comment("活动开始时间")
    private String beginTime;

    @Column
    @Comment("活动结束时间")
    private String endTime;

    @Column
    @Comment("记录距离开服多少天")
    private int openServerRecordOffsetBegin;

    @Column
    @Comment("活动记录持续天数")
    private int openServerRecordOffset;

    @Column
    @Comment("开始记录时间")
    private String startRecordTime;

    @Column
    @Comment("结束记录时间")
    private String endRecordTime;

    @Column
    @Comment("开服自动发布活动标识，0：否，1：是")
    @Default("0")
    private int autoSend;

    @Column
    @Comment("是否是新服活动，0：否，1：是")
    @Default("0")
    private int isOpenServer;

    @Column
    @Comment("自定义参数")
    @ColDefine(type=ColType.TEXT)
    private String custom;

    @Column
    @Comment("模板描述")
    @ColDefine(type=ColType.TEXT)
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSubType() {
        return subType;
    }

    public void setSubType(int subType) {
        this.subType = subType;
    }

    public int getMinLv() {
        return minLv;
    }

    public void setMinLv(int minLv) {
        this.minLv = minLv;
    }

    public int getMaxLv() {
        return maxLv;
    }

    public void setMaxLv(int maxLv) {
        this.maxLv = maxLv;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTimeType() {
        return timeType;
    }

    public void setTimeType(int timeType) {
        this.timeType = timeType;
    }

    public int getOpenServerOffsetBegin() {
        return openServerOffsetBegin;
    }

    public void setOpenServerOffsetBegin(int openServerOffsetBegin) {
        this.openServerOffsetBegin = openServerOffsetBegin;
    }

    public int getOpenServerOffset() {
        return openServerOffset;
    }

    public void setOpenServerOffset(int openServerOffset) {
        this.openServerOffset = openServerOffset;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getOpenServerRecordOffsetBegin() {
        return openServerRecordOffsetBegin;
    }

    public void setOpenServerRecordOffsetBegin(int openServerRecordOffsetBegin) {
        this.openServerRecordOffsetBegin = openServerRecordOffsetBegin;
    }

    public int getOpenServerRecordOffset() {
        return openServerRecordOffset;
    }

    public void setOpenServerRecordOffset(int openServerRecordOffset) {
        this.openServerRecordOffset = openServerRecordOffset;
    }

    public String getStartRecordTime() {
        return startRecordTime;
    }

    public void setStartRecordTime(String startRecordTime) {
        this.startRecordTime = startRecordTime;
    }

    public String getEndRecordTime() {
        return endRecordTime;
    }

    public void setEndRecordTime(String endRecordTime) {
        this.endRecordTime = endRecordTime;
    }

    public int getAutoSend() {
        return autoSend;
    }

    public void setAutoSend(int autoSend) {
        this.autoSend = autoSend;
    }

    public int getIsOpenServer() {
        return isOpenServer;
    }

    public void setIsOpenServer(int isOpenServer) {
        this.isOpenServer = isOpenServer;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
