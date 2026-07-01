package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_whitelist")
public class WhiteList {

	@Id
	@Column
	private int id;
	
	@Column
	@Comment("登录服ID")
	private int lsId;
	
	@Column
	@ColDefine(type=ColType.VARCHAR, width=200)
	@Comment("白名单条件")
	private String whiteCon;
	
	@Column
	@Comment("是添加还是删除")
	private int ctype;
	
	@Column
	@Comment("创建时间")
	private String createtime;
	
	@Column
	@Comment("创建人")
	private String userName;
	
	@Column
	@Comment("创建人的IP")
	private String userIP;
	
	@Column
	@Comment("登陆服返回的结果")
	@ColDefine(type=ColType.VARCHAR, width=500)
	private String backStr;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getLsId() {
		return lsId;
	}

	public void setLsId(int lsId) {
		this.lsId = lsId;
	}

	public String getWhiteCon() {
		return whiteCon;
	}

	public void setWhiteCon(String whiteCon) {
		this.whiteCon = whiteCon;
	}

	public int getCtype() {
		return ctype;
	}

	public void setCtype(int ctype) {
		this.ctype = ctype;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserIP() {
		return userIP;
	}

	public void setUserIP(String userIP) {
		this.userIP = userIP;
	}

	public String getBackStr() {
		return backStr;
	}

	public void setBackStr(String backStr) {
		this.backStr = backStr;
	}

}
