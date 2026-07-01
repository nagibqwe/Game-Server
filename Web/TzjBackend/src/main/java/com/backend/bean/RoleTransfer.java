package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 角色转移实体
 * @author Administrator
 *
 */
@Table("t_role_transfer")
public class RoleTransfer {
	
	@Column
	@Comment("被转移的角色ID")
	private String roleId;
	
	@Column
	@Comment("被转移角色的原始帐号ID")
	private String srcUserId;
	
	@Column
	@Comment("转移目标帐号ID")
	private String targetUserId;
	
	@Column
	@Comment("区服")
	private int serverId;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=300, notNull=true)
	@Comment("转移原因")
	private String reason;

	@Column
	@ColDefine(customType="TINYINT",notNull=true)
	@Comment("是否生效,0为生效 1为无效")
	private int isDeleted;

	@Column
    @Comment("操作时间")
	private int time;

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getSrcUserId() {
		return srcUserId;
	}

	public void setSrcUserId(String srcUserId) {
		this.srcUserId = srcUserId;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
