package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * 设置评价开关
 */
@Table("t_evaluate")
public class Evaluate {

	@Id
	@Comment("评价ID")
	private int id;
	
	@Column
	@Comment("服务器ID")
	private int serverId;
	
	@Column
	@Comment("评价类型")
	private int eType;
	
	@Column
	@Comment("开关状态")
	private boolean state;
	
	@Column
	@Comment("执行时间")
	private Date actionTime;
	
	@Column
	@Comment("设置原因")
	private String reason;
	
	@Column
	@Comment("是否删除，0 ：不删除， 1： 删除")
	private int isDelete;

	@Column
	@Comment("操作者名字")
	private String actionUser;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public int geteType() {
		return eType;
	}

	public void seteType(int eType) {
		this.eType = eType;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	public Date getActionTime() {
        return actionTime;
    }

    public void setActionTime(Date actionTime) {
        this.actionTime = actionTime;
    }

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(int isDelete) {
		this.isDelete = isDelete;
	}

	public String getActionUser() {
		return actionUser;
	}

	public void setActionUser(String actionUser) {
		this.actionUser = actionUser;
	}
}
