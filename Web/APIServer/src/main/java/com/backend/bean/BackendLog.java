package com.backend.bean;

import com.backend.struct.StaticField;
import org.nutz.dao.entity.annotation.*;
import org.nutz.mvc.Mvcs;

@Table("t_backend_log")
@TableIndexes(value={@Index(fields={"userId"}, name="userId_index",unique=false)})
public class BackendLog {
	@Id
	private int id;
	/**
	 * 用户ID
	 */
	@Column
	@Comment("后台用户ID")
	private int userId;
	/**
	 * 用户名
	 */
	@Column
	@Comment("后台用户名")
	private String userName;
	/**
	 * 操作内容
	 */
	@Column
	@Comment("操作内容")
	@ColDefine(type=ColType.TEXT)
	private String content;
	/**
	 * 操作时间
	 */
	@Column
	@Comment("操作时间 ")
	private long time;
	/**
	 * 使用者IP
	 */
	@Column
	@Comment("操作用户IP")
	private String ip;
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public BackendLog() {
	}

	public BackendLog(int userId, String userName, String content, long time,String ip) {
		super();
		this.userId = userId;
		this.userName = userName;
		this.content = content;
		this.time = time;
		this.ip = ip;
	}
	
	public BackendLog(User user, String content,String ip) {
		super();
		if(user==null){
			this.userId = -1;
			this.userName = "未知用户";
		}else{
			this.userId = user.getId();
			this.userName = user.getName();
		}
		this.content = content;
		this.time = System.currentTimeMillis();
		this.ip = ip;
	}
	
	public BackendLog(String content) {
		super();
		
		User user=(User)Mvcs.getReq().getSession().getAttribute("USER");
		if(user==null){
			this.userId = -1;
			this.userName = "未知用户";
		}else{
			this.userId = user.getId();
			this.userName = user.getName();
		}
		this.content = content;
		this.time = System.currentTimeMillis();
		this.ip = Mvcs.getReq().getHeader(StaticField.USER_REAL_IP);
	}
}
