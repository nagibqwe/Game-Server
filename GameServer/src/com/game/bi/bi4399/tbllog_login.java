package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内登录时的信息。
 * @time Created on 2020/3/26 16:56.
 * @author: tc
 */
public class tbllog_login extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 平台账号ID
	private String account_name;
	// 等级
	private int dim_level;
	// 玩家IP
	private String user_ip;
	// 登录地图ID
	private int login_map_id;
	// 用户设备ID
	private String did;
	// 游戏版本号
	private String game_version;
	// 手机操作系统，如：android、iOS
	private String os;
	// 操作系统版本号，如：2.3.4
	private String os_version;
	// 设备名称，如：三星GT-S5830
	private String device_name;
	// 屏幕分辨率，如：480*800
	private String screen;
	// 移动网络运营商(mobile network operators)，如：中国移动、中国联通
	private String mno;
	// 联网方式(Networking mode)，如：3G、WIFI
	private String nm;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_login() {}

	public tbllog_login(String platform, String device, long role_id, String account_name, int dim_level, String user_ip, int login_map_id, String did, String game_version, String os, String os_version, String device_name, String screen, String mno, String nm, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.dim_level = dim_level;
		this.user_ip = user_ip;
		this.login_map_id = login_map_id;
		this.did = did;
		this.game_version = game_version;
		this.os = os;
		this.os_version = os_version;
		this.device_name = device_name;
		this.screen = screen;
		this.mno = mno;
		this.nm = nm;
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

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
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

	@Log(logField = "login_map_id", fieldType = "int", index = "0")
	public int getLogin_map_id() {
		return login_map_id;
	}

	public void setLogin_map_id(int login_map_id) {
		this.login_map_id = login_map_id;
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

	@Log(logField = "os", fieldType = "varchar(100)", index = "0")
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Log(logField = "os_version", fieldType = "varchar(100)", index = "0")
	public String getOs_version() {
		return os_version;
	}

	public void setOs_version(String os_version) {
		this.os_version = os_version;
	}

	@Log(logField = "device_name", fieldType = "varchar(100)", index = "0")
	public String getDevice_name() {
		return device_name;
	}

	public void setDevice_name(String device_name) {
		this.device_name = device_name;
	}

	@Log(logField = "screen", fieldType = "varchar(100)", index = "0")
	public String getScreen() {
		return screen;
	}

	public void setScreen(String screen) {
		this.screen = screen;
	}

	@Log(logField = "mno", fieldType = "varchar(100)", index = "0")
	public String getMno() {
		return mno;
	}

	public void setMno(String mno) {
		this.mno = mno;
	}

	@Log(logField = "nm", fieldType = "varchar(100)", index = "0")
	public String getNm() {
		return nm;
	}

	public void setNm(String nm) {
		this.nm = nm;
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
