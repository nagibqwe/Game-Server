package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

import java.util.Date;

/**
 * 服务器的地址
 * @author Administrator
 *
 */
@Table("t_server")
public class Server implements Comparable<Server> {

	@Id
	@Column
	private int id;

	@Column
	@Comment("服务器ID")
	private int serverId;

	@Column
	@Comment("服务器名称")
	private String serverName = "";

	@Column
	@Comment("平台名")
	private String groupName = "";

	@Column
	@Comment("游戏服IP")
	private String WorldIP = "";

	@Column
	@Comment("游戏服监听GM后台消息端口")
	private int worldPort;

	@Column
	@Comment("合服标识0:未合服1:合服")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isHeFu;

	@Column
	@Comment("合服时间")
	private Date hefuTime;

	@Column
	@Comment("合服目标服ID")
	@Default("0")
	private int hefuServerID;

	@Column
	@Comment("服务器标识 0:测试服1:正式服2:登录服3:世界服4:跨服")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private byte serverType; //是否是正式服，1：正式服，0：测试服

	@Column
	@Comment("是否删除 0:启用 1:删除 ")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isDeleted;

	@Column
	@Comment("0为展示，1为不展示 ")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int isShow;

	@Column
	@Comment("开服时间")
	private String serverOpenTime = "";

	@Column
	@Comment("服务器状态 0:备服状态 1:开服状态")
	private int openState;

	@Column
	@Comment("服务器最新心跳时间")
	private String heartTime;

	@Column
	@Comment("注册人数")
	@ColDefine(customType="TINYINT",notNull=true)
	@Default("0")
	private int registerNum;

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

	public String getWorldIP() {
		return WorldIP;
	}

	public void setWorldIP(String worldIP) {
		WorldIP = worldIP;
	}

	public int getWorldPort() {
		return worldPort;
	}

	public void setWorldPort(int worldPort) {
		this.worldPort = worldPort;
	}

	public int getIsHeFu() {
		return isHeFu;
	}

	public void setIsHeFu(int isHeFu) {
		this.isHeFu = isHeFu;
	}

	public Date getHefuTime() {
		return hefuTime;
	}

	public void setHefuTime(Date hefuTime) {
		this.hefuTime = hefuTime;
	}

	public int getHefuServerID() {
		return hefuServerID;
	}

	public void setHefuServerID(int hefuServerID) {
		this.hefuServerID = hefuServerID;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public boolean check() {
		return false;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public byte getServerType() {
		return serverType;
	}

	public void setServerType(byte serverType) {
		this.serverType = serverType;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public int getIsShow() {
		return isShow;
	}

	public void setIsShow(int isShow) {
		this.isShow = isShow;
	}

	public String getServerOpenTime() {
		return serverOpenTime;
	}

	public void setServerOpenTime(String serverOpenTime) {
		this.serverOpenTime = serverOpenTime;
	}

	public int getOpenState() {
		return openState;
	}

	public void setOpenState(int openState) {
		this.openState = openState;
	}

	public String getHeartTime() {
		return heartTime;
	}

	public void setHeartTime(String heartTime) {
		this.heartTime = heartTime;
	}

	public int getRegisterNum() {
		return registerNum;
	}

	public void setRegisterNum(int registerNum) {
		this.registerNum = registerNum;
	}

	@Override
	public int compareTo(Server o) {
		return this.getServerId() - o.getServerId();
	}
}
