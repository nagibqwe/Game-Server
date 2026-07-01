package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [游戏区服服务器状态变更]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIServer_op {
	/**
	 * 全局公共
	 */
	private String event_time = "";
	/**
	 * 服务器游戏大区id
	 */
	private String zone_id = "";
	/**
	 * 服务器游戏区服id
	 */
	private String server_id = "";
	/**
	 * 服务器公共:服务器版本号
	 */
	private String game_version = "";
	/**
	 * 服务器游戏大区名称
	 */
	private String zone_name = "";
	/**
	 * 服务器游戏区服名称
	 */
	private String server_name = "";
	/**
	 * 服务器现在的开服时间
	 */
	private String server_open_time = "";
	/**
	 * 服务器现在的开服天数
	 */
	private String server_open_day = "";
	/**
	 * 游戏服务器操作类型:1=开服(由备服开启) 2=维护开服 3=维护关服 101=进程启动 102=进程关闭 103 开服时间变化
	 */
	private String server_op_type = "";

	public BIServer_op() {}

	public BIServer_op(
			String zone_name,
			String server_name,
			String server_open_time,
			String server_open_day,
			String server_op_type
	) {
		setZone_name(zone_name);
		setServer_name(server_name);
		setServer_open_time(server_open_time);
		setServer_open_day(server_open_day);
		setServer_op_type(server_op_type);
	}

	public String getZone_name() {
		return zone_name;
	}

	public void setZone_name(String zone_name) {
		if (zone_name == null || zone_name.equals(""))
			this.zone_name = "";
		else
			this.zone_name = zone_name;
	}

	public String getServer_name() {
		return server_name;
	}

	public void setServer_name(String server_name) {
		if (server_name == null || server_name.equals(""))
			this.server_name = "";
		else
			this.server_name = server_name;
	}

	public String getServer_open_time() {
		return server_open_time;
	}

	public void setServer_open_time(String server_open_time) {
		if (server_open_time == null || server_open_time.equals(""))
			this.server_open_time = "";
		else
			this.server_open_time = server_open_time;
	}

	public String getServer_open_day() {
		return server_open_day;
	}

	public void setServer_open_day(String server_open_day) {
		if (server_open_day == null || server_open_day.equals(""))
			this.server_open_day = "";
		else
			this.server_open_day = server_open_day;
	}

	public String getServer_op_type() {
		return server_op_type;
	}

	public void setServer_op_type(String server_op_type) {
		if (server_op_type == null || server_op_type.equals(""))
			this.server_op_type = "";
		else
			this.server_op_type = server_op_type;
	}

	public String getEvent_time() {
		return event_time;
	}

	public void setEvent_time(String event_time) {
		this.event_time = event_time;
	}

	public String getZone_id() {
		return zone_id;
	}

	public void setZone_id(String zone_id) {
		this.zone_id = zone_id;
	}

	public String getServer_id() {
		return server_id;
	}

	public void setServer_id(String server_id) {
		this.server_id = server_id;
	}

	public String getGame_version() {
		return game_version;
	}

	public void setGame_version(String game_version) {
		this.game_version = game_version;
	}

}
