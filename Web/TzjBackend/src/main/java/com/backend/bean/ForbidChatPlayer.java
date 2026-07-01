package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_forbidchat")
public class ForbidChatPlayer {

	@Id
	@Column
	private long id;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=200)
	@Comment("聊天禁言的账号")
	private String userId;

	@Column
	@Comment("违规类型 1黑色产业 2不良信息")
	private int crimeType;

	@Column
	@Comment("禁言类型 1:工作室禁言2:全文替换禁言3:关键字替换禁言4:常规禁言5:隐形禁言6:隔离禁言")
	private int forbidType;
	
	@Column
	@Comment("创建的时间")
	private String createTime;
	
	@Column
	@Comment("封号的结束时间")
	private String endTime;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=120)
	@Comment("后台那个管理人员添加的")
	private String backUserName;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=120)
	@Comment("后台那个管理人员修改")
	private String backMUserName;
	
	@Column
	@ColDefine(type = ColType.VARCHAR, width=600)
	@Comment("操作的理由")
	private String reason;

	@Column
	@ColDefine(type=ColType.VARCHAR, width=100)
	@Comment("发送到游戏服列表")
	private String serverIds;
	
	@Column
	@Comment("删除状态值")
	private int state;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getCrimeType() {
		return crimeType;
	}

	public void setCrimeType(int crimeType) {
		this.crimeType = crimeType;
	}

	public int getForbidType() {
		return forbidType;
	}

	public void setForbidType(int forbidType) {
		this.forbidType = forbidType;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getBackUserName() {
		return backUserName;
	}

	public void setBackUserName(String backUserName) {
		this.backUserName = backUserName;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

	public String getBackMUserName() {
		return backMUserName;
	}

	public void setBackMUserName(String backMUserName) {
		this.backMUserName = backMUserName;
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
