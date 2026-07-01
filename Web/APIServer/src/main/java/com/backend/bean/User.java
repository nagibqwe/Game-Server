package com.backend.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.*;

/**
 * 后台用户信息
 * @author Administrator
 *
 */
@Table("t_user")
public class User {
	@Id
	@Comment("用户ID")
	private int id;
	
	@Name
	@Column
	@Comment("用户名")
	private String name;
	
	@Column("passwd")
	@Comment("密码")
	private String password;
	
	@Column
	@Comment("加盐算法")
	private String salt;
	
	@Column("ct")
	@Comment("创建时间")
	private Date createTime;
	
	@Column("ut")
	@Comment("最后修改时间")
	private Date updateTime;
	
	@Column("ip")
	@Comment("最后登录IP")
	private String ip;
	
	@Column("language")
	@Comment("语言")
	@ColDefine(type = ColType.VARCHAR, width = 20)
	private String language;

	@Column("isDeleted")
	@Comment("生效标记 0:生效1:无效")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isDeleted;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}
	
	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}
}
