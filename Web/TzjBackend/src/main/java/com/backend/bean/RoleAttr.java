package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

import java.util.Date;

/**
 * 设置角色属性的bean
 */
@Table("t_role_attr")
public class RoleAttr {

	@Id
	@Comment("属性设置ID")
	private int id;
	
	@Column
	@Comment("服务器ID")
	private int serverId;

    @Column
    @Comment("角色ID")
    private String roleId;
	
	@Column
	@Comment("属性类型")
	private int attrType;
	
	@Column
	@Comment("设置的属性值")
	private int attrValue;

	@Column
	@Comment("真实的属性值")
	private int realValue;
	
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

    public int getAttrType() {
        return attrType;
    }

    public void setAttrType(int attrType) {
        this.attrType = attrType;
    }

    public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

    public int getAttrValue() {
        return attrValue;
    }

    public void setAttrValue(int attrValue) {
        this.attrValue = attrValue;
    }

    public int getRealValue() {
		return realValue;
	}

	public void setRealValue(int realValue) {
		this.realValue = realValue;
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
