package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_announce")
public class Announce {

	@Id
	@Column
	private Integer id;
	
	@Column
	@Comment("公告创建时间")
	private Long createTime;

	@Column
	@Comment("创建时间的字符串式")
	private String createDate;
	
	@Column
	@Comment("创建者账号ID")
	private Integer userId;
	
	@Column
	@Comment("创建者名字")
	@ColDefine(type = ColType.CHAR, width=100)
	private String userName;
	
	@Column
	@Comment("服务器组")
	private String groupName;

	@Column
	@Comment("服务器id")
	@ColDefine(customType="TEXT")
	private String serverIds;

	@Column
	@Comment("类型")
	private Integer type;
	
	@Column
	@ColDefine(customType="LONGTEXT")
	@Comment("公告的内容")
	private String content;

	@Column
	@Comment("原因")
	private String reason;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
}
