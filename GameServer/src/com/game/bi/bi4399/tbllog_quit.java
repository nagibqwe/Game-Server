package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内退出时的信息。
 * @time Created on 2020/3/26 16:58.
 * @author: tc
 */
public class tbllog_quit extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 平台账号ID
	private String account_name;
	// 登录等级
	private int login_level;
	// 登出等级
	private int logout_level;
	// 登出IP
	private String logout_ip;
	// 登录时间
	private int login_time;
	// 退出时间（索引）
	private int logout_time;
	// 在线时长
	private int time_duration;
	// 退出地图ID
	private int logout_map_id;
	// 退出异常或者原因，reason 对应字典表(0表示正常退出)
	private int reason_id;
	// 特殊信息
	private String msg;
	// 用户设备ID
	private String did;
	// 游戏版本号
	private String game_version;

	public tbllog_quit() {}

	public tbllog_quit(String platform, String device, long role_id, String account_name, int login_level, int logout_level, String logout_ip, int login_time, int logout_time, int time_duration, int logout_map_id, int reason_id, String msg, String did, String game_version) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.login_level = login_level;
		this.logout_level = logout_level;
		this.logout_ip = logout_ip;
		this.login_time = login_time;
		this.logout_time = logout_time;
		this.time_duration = time_duration;
		this.logout_map_id = logout_map_id;
		this.reason_id = reason_id;
		this.msg = msg;
		this.did = did;
		this.game_version = game_version;
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

	@Log(logField = "role_id", fieldType = "bigint", index = "0")
	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Log(logField = "login_level", fieldType = "int", index = "0")
	public int getLogin_level() {
		return login_level;
	}

	public void setLogin_level(int login_level) {
		this.login_level = login_level;
	}

	@Log(logField = "logout_level", fieldType = "int", index = "0")
	public int getLogout_level() {
		return logout_level;
	}

	public void setLogout_level(int logout_level) {
		this.logout_level = logout_level;
	}

	@Log(logField = "logout_ip", fieldType = "varchar(100)", index = "0")
	public String getLogout_ip() {
		return logout_ip;
	}

	public void setLogout_ip(String logout_ip) {
		this.logout_ip = logout_ip;
	}

	@Log(logField = "login_time", fieldType = "int", index = "0")
	public int getLogin_time() {
		return login_time;
	}

	public void setLogin_time(int login_time) {
		this.login_time = login_time;
	}

	@Log(logField = "logout_time", fieldType = "int", index = "1")
	public int getLogout_time() {
		return logout_time;
	}

	public void setLogout_time(int logout_time) {
		this.logout_time = logout_time;
	}

	@Log(logField = "time_duration", fieldType = "int", index = "0")
	public int getTime_duration() {
		return time_duration;
	}

	public void setTime_duration(int time_duration) {
		this.time_duration = time_duration;
	}

	@Log(logField = "logout_map_id", fieldType = "int", index = "0")
	public int getLogout_map_id() {
		return logout_map_id;
	}

	public void setLogout_map_id(int logout_map_id) {
		this.logout_map_id = logout_map_id;
	}

	@Log(logField = "reason_id", fieldType = "int", index = "0")
	public int getReason_id() {
		return reason_id;
	}

	public void setReason_id(int reason_id) {
		this.reason_id = reason_id;
	}

	@Log(logField = "msg", fieldType = "varchar(100)", index = "0")
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Log(logField = "did", fieldType = "varchar(100)", index = "0")
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Log(logField = "game_version", fieldType = "varchar(100)", index = "0")
	public String getGame_version() {
		return game_version;
	}

	public void setGame_version(String game_version) {
		this.game_version = game_version;
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
