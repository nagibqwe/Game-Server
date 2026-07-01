package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内的聊天信息。
 * @time Created on 2020/3/26 17:14.
 * @author: tc
 */
public class tbllog_chat extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 平台账号ID
	private String account_name;
	// 角色ID
	private long role_id;
	// 角色名
	private String role_name;
	// 等级
	private int dim_level;
	// 玩家IP
	private String user_ip;
	// 用户设备ID
	private String did;
	// 聊天频道（提供字典表）
	private int channel;
	// 聊天信息
	private String msg;
	// 内容类型(0代表语音,1代表文本)
	private int type;
	// 聊天对象ID
	private long target_role_id;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_chat() {}

	public tbllog_chat(String platform, String device, String account_name, long role_id, String role_name, int dim_level, String user_ip, String did, int channel, String msg, int type, long target_role_id, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.account_name = account_name;
		this.role_id = role_id;
		this.role_name = role_name;
		this.dim_level = dim_level;
		this.user_ip = user_ip;
		this.did = did;
		this.channel = channel;
		this.msg = msg;
		this.type = type;
		this.target_role_id = target_role_id;
		this.happend_time = happend_time;
	}

	@Log(logField = "platform", fieldType = "varchar(100)", index = "0")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Log(logField = "device", fieldType = "varchar(100)", index = "0")
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Log(logField = "role_id", fieldType = "bigint", index = "0")
	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	@Log(logField = "role_name", fieldType = "varchar(100)", index = "0")
	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	@Log(logField = "dim_level", fieldType = "int", index = "0")
	public int getDim_level() {
		return dim_level;
	}

	public void setDim_level(int dim_level) {
		this.dim_level = dim_level;
	}

	@Log(logField = "user_ip", fieldType = "varchar(100)", index = "0")
	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	@Log(logField = "did", fieldType = "varchar(100)", index = "0")
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Log(logField = "channel", fieldType = "int", index = "0")
	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	@Log(logField = "msg", fieldType = "varchar(100)", index = "0")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Log(logField = "type", fieldType = "int", index = "0")
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Log(logField = "target_role_id", fieldType = "bigint", index = "0")
	public long getTarget_role_id() {
		return target_role_id;
	}

	public void setTarget_role_id(long target_role_id) {
		this.target_role_id = target_role_id;
	}

	@Log(logField = "happend_time", fieldType = "int", index = "1")
	public int getHappend_time() {
		return happend_time;
	}

	public void setHappend_time(int happend_time) {
		this.happend_time = happend_time;
	}

	/**
	 * 日志多长时间建一次表
	 *
	 * @return
	 */
	@Override
	public TableCheckStepEnum getRollingStep() {
		return TableCheckStepEnum.UNROLL;
	}

	@Override
	public void logToFile() {
		logger.error(buildSql());
	}
}
