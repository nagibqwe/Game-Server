package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [在线人数(服务器)]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIOnline {
	/**
	 * 日志流水号，在一个游戏区服下，每条日志必须保证全局唯一性
	 */
	private String log_id = "";
	/**
	 * 发送时间
	 */
	private String event_time = "";
	/**
	 * 游戏大区ID
	 */
	private String zone_id = "";
	/**
	 * 游戏区服ID
	 */
	private String server_id = "";
	/**
	 * 当前服务器在线人数
	 */
	private String online_num = "";
	/**
	 * 当前服务器挂机人数
	 */
	private String hang_up_num = "";
	/**
	 * 当前在线玩家账号ID列表，账号ID通过逗号分隔，账号ID为外部平台账号ID
	 */
	private String account_list = "";

	public BIOnline() {}

	public BIOnline(
			String log_id,
			String event_time,
			String zone_id,
			String server_id,
			String online_num,
			String hang_up_num,
			String account_list
	) {
		setLog_id(log_id);
		setEvent_time(event_time);
		setZone_id(zone_id);
		setServer_id(server_id);
		setOnline_num(online_num);
		setHang_up_num(hang_up_num);
		setAccount_list(account_list);
	}

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		if (log_id == null || log_id.equals(""))
			this.log_id = "";
		else
			this.log_id = log_id;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		if (event_time == null || event_time.equals(""))
			this.event_time = "";
		else
			this.event_time = event_time;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		if (zone_id == null || zone_id.equals(""))
			this.zone_id = "";
		else
			this.zone_id = zone_id;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		if (server_id == null || server_id.equals(""))
			this.server_id = "";
		else
			this.server_id = server_id;
	}

	public String getOnline_num() {
		return online_num;
	}

	public void setOnline_num(String online_num) {
		if (online_num == null || online_num.equals(""))
			this.online_num = "";
		else
			this.online_num = online_num;
	}

	public String getHang_up_num() {
		return hang_up_num;
	}

	public void setHang_up_num(String hang_up_num) {
		if (hang_up_num == null || hang_up_num.equals(""))
			this.hang_up_num = "";
		else
			this.hang_up_num = hang_up_num;
	}

	public String getAccount_list() {
		return account_list;
	}

	public void setAccount_list(String account_list) {
		if (account_list == null || account_list.equals(""))
			this.account_list = "";
		else
			this.account_list = account_list;
	}

}
