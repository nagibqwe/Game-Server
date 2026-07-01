package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_forbiduser")
public class Forbiduser {

	@Id
	@Column
	private long id;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=200)
	@Comment("封号的条件")
	private String userId;
	
	
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
	@Comment("删除状态值")
	private int state;
	
	@Column
	@ColDefine(type=ColType.VARCHAR ,width=600)
	@Comment("登录返回的结果")
	private String backStr;

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

	public String getBackStr() {
		return backStr;
	}

	public void setBackStr(String backStr) {
		this.backStr = backStr;
	}
	
}
