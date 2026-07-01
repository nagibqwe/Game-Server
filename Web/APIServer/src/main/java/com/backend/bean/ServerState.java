package com.backend.bean;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

/**
 * 服务器状态标志收取
 * 
 * @author Administrator
 *
 */
@Table("t_serverstate")
@TableIndexes(value = { @Index(fields = { "serverId" }, name = "serverId_index") })
public class ServerState {

	@Id
	@Column
	private int id;

	@Column
	@Comment("当前服务器Id")
	private int serverId;

	@Column
	@Comment("服务器的状态")
	private int state;

	@Column
	@Comment("当前服务器IP")
	private String ip;

	@Column
	@Comment("是否连接好world")
	private int isConnectWord;

	@Column
	@Comment("当前服务器更新人数")
	private int currentNum;

	@Column
	@Comment("最后一次更新的时间")
	private String updateTime;

	
	
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

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getIsConnectWord() {
		return isConnectWord;
	}

	public void setIsConnectWord(int isConnectWord) {
		this.isConnectWord = isConnectWord;
	}

	public int getCurrentNum() {
		return currentNum;
	}

	public void setCurrentNum(int currentNum) {
		this.currentNum = currentNum;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

}
