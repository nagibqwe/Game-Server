package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

/**
 * 计时频率公告的数据结构表
 */
@Table("t_cyannounce")
public class CyAnnounce {

	@Id
	@Column
	@Comment("公告的编号")
	private int id;
	
	@Column
	@Comment("公告的平台分组")
	private String groupName;
	
	@Column
	@ColDefine(type = ColType.VARCHAR , width=100)
	@Comment("公告的发送的服务器列表")
	private String serverIds;
	
	@Column
	@Comment("公告的标识")
	private String batchTag;
	
	@Column
	@ColDefine(type=ColType.TEXT)
	@Comment("公告的内容")
	private String content;

	@Column
	@Comment("公告的创建时间")
	private long createTime;

	@Column
	@Comment("公告的创建时间字符格式化")
	private String createDate;

	@Column
	@Comment("公告的添加者ID")
	private int createUserId;

	@Column
	@Comment("公告的添加者名字")
	private String createUserName;

	@Column
	@Comment("公告的开始时间")
	private long fromTime;

	@Column
	@Comment("公告的开始字符格式化")
	private String fromDate;

	@Column
	@Comment("公告的结束时间")
	private long toTime;

	@Column
	@Comment("公告的结束时间字符格式化")
	private String toDate;

	@Column
	@Comment("公告发送的总次数")
	private int totalTimes;

	@Column
	@Comment("公告的当前已经发送的次数")
	private long nowTimes;

	@Column
	@Comment("公告的下一次发送的时间")
	private long nextTimes;

	@Column
	@Comment("公告的下一次发送时间字符格式化")
	private String nextDate;

	@Column
	@Comment("公告的当前状态，启用还是禁用")
	private int state;

	@Column
	@Comment("公告发送的频率")
	private int cycleInterval;
	
	@Column
	@Comment("公告发送的位置")
	private int type;

	private String stateStr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

	public String getBatchTag() {
		return batchTag;
	}

	public void setBatchTag(String batchTag) {
		this.batchTag = batchTag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public long getFromTime() {
		return fromTime;
	}

	public void setFromTime(long fromTime) {
		this.fromTime = fromTime;
	}

	public String getFromDate() {
		return fromDate;
	}

	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}

	public long getToTime() {
		return toTime;
	}

	public void setToTime(long toTime) {
		this.toTime = toTime;
	}

	public String getToDate() {
		return toDate;
	}

	public void setToDate(String toDate) {
		this.toDate = toDate;
	}

	public int getTotalTimes() {
		return totalTimes;
	}

	public void setTotalTimes(int totalTimes) {
		this.totalTimes = totalTimes;
	}

	public long getNowTimes() {
		return nowTimes;
	}

	public void setNowTimes(long nowTimes) {
		this.nowTimes = nowTimes;
	}

	public long getNextTimes() {
		return nextTimes;
	}

	public void setNextTimes(long nextTimes) {
		this.nextTimes = nextTimes;
	}

	public String getNextDate() {
		return nextDate;
	}

	public void setNextDate(String nextDate) {
		this.nextDate = nextDate;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getCycleInterval() {
		return cycleInterval;
	}

	public void setCycleInterval(int cycleInterval) {
		this.cycleInterval = cycleInterval;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStateStr() {
		return stateStr;
	}

	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
}
