package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("t_user_role")
public class UserRole {
	
	@Column("userid")
	@Comment("用户ID")
	private int userid;
	
	@Column("roleid")
	@Comment("角色id")
	private int roleid;

	public int getUserid() {
		return userid;
	}

	public void setUserid(int userid) {
		this.userid = userid;
	}

	public int getRoleid() {
		return roleid;
	}

	public void setRoleid(int roleid) {
		this.roleid = roleid;
	}
	
	
}
