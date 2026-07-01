package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 更新公告实体
 */
@Table("t_update_notice")
public class UpdateNotice {

	@Id
	@Comment("ID")
	private int id;
	
	@Column
	@Comment("服务器ID")
	private String serverIds;
	
	@Column
	@ColDefine(type=ColType.TEXT)
	@Comment("公告内容")
	private String content;

	@Column
	@Comment("公告奖励")
	private String reward;
	
	@Column
	@Comment("操作类型，0 ：只更新公告， 1： 重置奖励")
	private int type;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getServerIds() {
		return serverIds;
	}

	public void setServerIds(String serverIds) {
		this.serverIds = serverIds;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReward() {
		return reward;
	}

	public void setReward(String reward) {
		this.reward = reward;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
