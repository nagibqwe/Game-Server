package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Table("t_activity")
@TableIndexes(value={@Index(fields={"state","isDeleted","endTime"}, name="state_is_end",unique=false),
		@Index(fields={"type","isDeleted","endTime"}, name="type_is_end", unique=false),
		@Index(fields={"type","isDeleted"}, name="type_is", unique=false)})
public class Activity implements Cloneable{

    @Id
	@Column 
	@Comment("活动ID")
	private int id;

	@Column
	@Comment("活动逻辑类型")
	private int type;

    @Column
    @Comment("节日类型")
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
	@Comment("活动说明")
	@ColDefine(type=ColType.VARCHAR, width=200)
	private String description;

	@Column
	@Comment("时间类型 0固定时间（配置时间） 1开服时间变量（根据开服时间+时间变量计算）")
	private int timeType;

	@Column
	@Comment("距离开服多少天")
	private int openServerOffsetBegin;

    @Column
    @Comment("活动持续天数")
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
	@Comment("活动状态，0：未验证(测试、删除)，1：已验证(发布、删除)，2：已发布(删除)，     //已过期(删除)这个通过活动结束时间去判断")
	private int state;

	@Column
	@Comment("活动发布平台(groupName)(List JSON化后的字串[plat1,plat2,..])")
	@ColDefine(type=ColType.VARCHAR, width=300)
	private String platform = "";
	
	@Column
	@Comment("活动要发布到的区服列表(List JSON化后的字串[sid1,sid2,..])")
	@ColDefine(type=ColType.VARCHAR, width=500)
	private String toSidList = "";

	@Column
	@Comment("活动发布成功的区服列表(List JSON化后的字串[sid1,sid2,..])")
	@ColDefine(type=ColType.VARCHAR, width=500)
	private String okSidList = "";
		
	@Column
	@Comment("活动是否被删除，0：否，1：是")
	private byte isDeleted;

    @Column
    @Comment("开服自动发布活动标识，0：否，1：是")
    @Default("0")
    private int autoSend;

    @Column
    @Comment("是否是新服活动，0：否，1：是")
    @Default("0")
    private int isOpenServer;

    @Column
	@Comment("提交开始时间")
	private String submitBeginTime;
	
	@Column
	@Comment("提交结束时间")
	private String submitEndTime;

    @Column
    @Comment("自定义参数")
    @ColDefine(type=ColType.TEXT)
	private String custom;

    @Column
    @Comment("活动是否被覆盖正在进行的活动，0：否，1：是")
    private int cover;

    @Column
    @Comment("配置参数，用于GM后台反解析")
    @ColDefine(type=ColType.TEXT)
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
		this.setPlatform(activity.getPlatform());
		this.setToSidList(activity.getToSidList());
		this.setOkSidList(activity.getOkSidList());
		this.setIsDeleted(activity.getIsDeleted());
		this.setAutoSend(activity.getAutoSend());
		this.setIsOpenServer(activity.getIsOpenServer());
		this.setSubmitBeginTime(activity.getSubmitBeginTime());
		this.setSubmitEndTime(activity.getSubmitEndTime());
		this.setCover(activity.getCover());
		this.setConfigData(activity.getConfigData());
	}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getToSidList() {
        return toSidList;
    }

    public void setToSidList(String toSidList) {
        this.toSidList = toSidList;
    }

    public String getOkSidList() {
        return okSidList;
    }

    public void setOkSidList(String okSidList) {
        this.okSidList = okSidList;
    }

    public byte getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(byte isDeleted) {
        this.isDeleted = isDeleted;
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

    public String getSubmitBeginTime() {
        return submitBeginTime;
    }

    public void setSubmitBeginTime(String submitBeginTime) {
        this.submitBeginTime = submitBeginTime;
    }

    public String getSubmitEndTime() {
        return submitEndTime;
    }

    public void setSubmitEndTime(String submitEndTime) {
        this.submitEndTime = submitEndTime;
    }

    public String getCustom() {
        return custom;
    }

    public void setCustom(String custom) {
        this.custom = custom;
    }

    public int getCover() {
        return cover;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public String getConfigData() {
        return configData;
    }

    public void setConfigData(String configData) {
        this.configData = configData;
    }
}
