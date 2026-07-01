package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录最近5分钟在线玩家总人数、总设备数信息（按 平台+设备 分组统计），至少每5分钟记录一次。
 * @time Created on 2020/3/26 17:02.
 * @author: tc
 */
public class tbllog_online extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 当前维度的在线玩家总人数
	private int people;
	// 当前维度的在线玩家总设备数
	private int device_cnt;
	// 当前维度的在线IP数
	private int ip_cnt;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_online() {}

	public tbllog_online(String platform, String device, int people, int device_cnt, int ip_cnt, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.people = people;
		this.device_cnt = device_cnt;
		this.ip_cnt = ip_cnt;
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

	@Log(logField = "people", fieldType = "int", index = "0")
	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	@Log(logField = "device_cnt", fieldType = "int", index = "0")
	public int getDevice_cnt() {
		return device_cnt;
	}

	public void setDevice_cnt(int device_cnt) {
		this.device_cnt = device_cnt;
	}

	@Log(logField = "ip_cnt", fieldType = "int", index = "0")
	public int getIp_cnt() {
		return ip_cnt;
	}

	public void setIp_cnt(int ip_cnt) {
		this.ip_cnt = ip_cnt;
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
