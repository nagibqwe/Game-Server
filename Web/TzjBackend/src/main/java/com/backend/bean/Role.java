package com.backend.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

/**
 * 用户角色
 */
@Table("t_role")
public class Role {
	
	@Id
	@Comment("角色id")
	private int roleId;
	
	@Name
	@Comment("角色名称")
	private String roleName;
	
	@Comment("创建时间")
	private Date createTime;
	
	@Comment("描述")
	private String description;
	
	@Comment("生效标记 0:生效1:无效")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isDeleted;

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}



