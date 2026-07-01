package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内创建角色时的信息。
 * @time Created on 2020/3/26 16:48.
 * @author: tc
 */
public class tbllog_role extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 角色名称
	private String role_name;
	// 平台账号ID
	private String account_name;
	// 玩家IP
	private String user_ip;
	// 职业ID
	private int dim_prof;
	// 性别(0=女，1=男，2=未知) tinyint
	private int dim_sex;
	// 用户设备ID
	private String did;
	// 游戏版本号
	private String game_version;
	// 事件发生时间（索引）
	private int happend_time;
	public tbllog_role() {}
	public tbllog_role(String platform, String device, long role_id, String role_name, String account_name, String user_ip, int dim_prof, int dim_sex, String did, String game_version, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.role_name = role_name;
		this.account_name = account_name;
		this.user_ip = user_ip;
		this.dim_prof = dim_prof;
		this.dim_sex = dim_sex;
		this.did = did;
		this.game_version = game_version;
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

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Log(logField = "user_ip", fieldType = "varchar(100)", index = "0")
	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	@Log(logField = "dim_prof", fieldType = "int", index = "0")
	public int getDim_prof() {
		return dim_prof;
	}

	public void setDim_prof(int dim_prof) {
		this.dim_prof = dim_prof;
	}

	@Log(logField = "dim_sex", fieldType = "int", index = "0")
	public int getDim_sex() {
		return dim_sex;
	}

	public void setDim_sex(int dim_sex) {
		this.dim_sex = dim_sex;
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
