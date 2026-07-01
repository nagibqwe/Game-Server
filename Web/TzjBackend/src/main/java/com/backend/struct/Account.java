package com.backend.struct;

public class Account {
	/**
	 * 游戏账号id
	 */
	private String userid;
	/**
	 * 553平台生成的账号名字
	 */
	private String userName;
	/**
	 * 渠道的账号
	 */
	private String platformAccount;
	/**
	 * 平台名
	 */
	private String platformName;
	/**
	 * 创建时间
	 */
	private int createTime;
	/**
	 * 上次登录时间
	 */
	private int time;
	/**
	 * 上次登录ip
	 */
	private String lastLoginIp;
	/**
	 * 机器唯一码,客户端生成的uuid，即使删除了游戏还是不变的，必须不为空
	 */
	private String machineCode;
	/**
	 * imei
	 */
	private String imei;
	/**
	 * mac地址
	 */
	private String mac;

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPlatformAccount() {
		return platformAccount;
	}
	public void setPlatformAccount(String platformAccount) {
		this.platformAccount = platformAccount;
	}
	public String getPlatformName() {
		return platformName;
	}
	public void setPlatformName(String platformName) {
		this.platformName = platformName;
	}
	public int getCreateTime() {
		return createTime;
	}
	public void setCreateTime(int createTime) {
		this.createTime = createTime;
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getLastLoginIp() {
		return lastLoginIp;
	}
	public void setLastLoginIp(String lastLoginIp) {
		this.lastLoginIp = lastLoginIp;
	}
	public String getMachineCode() {
		return machineCode;
	}
	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}
	public String getImei() {
		return imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
}
