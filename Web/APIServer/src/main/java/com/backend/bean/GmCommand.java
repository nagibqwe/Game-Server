package com.backend.bean;

import java.util.Date;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gm_log")
public class GmCommand {

    @Id
    private int id;
    
    //操作命令
    @Column
    @ColDefine(width=100)
    private String action;
    
    //参数
    @Column
    @ColDefine(width=500)
    private String params;
    
    //服务器名
    @Column
    @ColDefine(width=100)
    private String serverName;
    
    //服务器ID
    @Column
    private int serverId;
    
    //成功失败
    @Column
    private boolean isOk;
    
    //处理结果
    @Column
    @ColDefine(type = ColType.TEXT)
    private String result;
    
    //操作时间
    @Column
    private Date operDate;
    
    @Column
    private String user;
    
    @Column
    private String ip;
    
    @Column
    @Comment("GM命令类型 0:游戏服GM(socket) 1:跨服或登录服GM(http)")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
    private int gmType;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public boolean isOk() {
		return isOk;
	}

	public void setOk(boolean isOk) {
		this.isOk = isOk;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Date getOperDate() {
		return operDate;
	}

	public void setOperDate(Date operDate) {
		this.operDate = operDate;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getGmType() {
		return gmType;
	}

	public void setGmType(int gmType) {
		this.gmType = gmType;
	}
}
