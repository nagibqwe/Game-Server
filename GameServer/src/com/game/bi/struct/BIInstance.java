package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [副本日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIInstance {
	/**
	 * 副本id
	 */
	private String instance_id = "";
	/**
	 * 副本名字
	 */
	private String instance_name = "";
	/**
	 * 副本类型 提供相关【字典】
	 */
	private String instance_type = "";
	/**
	 * 副本子类型 提供相关【字典】
	 */
	private String instance_subtype = "";
	/**
	 * 副本等级
	 */
	private String instance_level = "";
	/**
	 * 副本难度 1:D 2:C 3:B 4:A 5:S
	 */
	private String instance_diff = "";
	/**
	 * 副本状态 0:进入,1:退出等
	 */
	private String instance_status = "";
	/**
	 * 副本结果 1:成功 2:失败 3:超时 4:放弃
	 */
	private String instance_result = "";
	/**
	 * 队伍ID 保证全局唯一性
	 */
	private String team_id = "";
	/**
	 * 完成副本时记录消耗时间(单位:秒)
	 */
	private String instance_used_time = "";
	/**
	 * 副本层级，副本为剑灵阁时为楼层
	 */
	private String instance_floor = "";

	public BIInstance() {}

	public BIInstance(
			String instance_id,
			String instance_name,
			String instance_type,
			String instance_subtype,
			String instance_level,
			String instance_diff,
			String instance_status,
			String instance_result,
			String team_id,
			String instance_used_time,
			String instance_floor
	) {
		setInstance_id(instance_id);
		setInstance_name(instance_name);
		setInstance_type(instance_type);
		setInstance_subtype(instance_subtype);
		setInstance_level(instance_level);
		setInstance_diff(instance_diff);
		setInstance_status(instance_status);
		setInstance_result(instance_result);
		setTeam_id(team_id);
		setInstance_used_time(instance_used_time);
		setInstance_floor(instance_floor);
	}

	public String getInstance_id() {
		return instance_id;
	}

	public void setInstance_id(String instance_id) {
		if (instance_id == null || instance_id.equals(""))
			this.instance_id = "";
		else
			this.instance_id = instance_id;
	}

	public String getInstance_name() {
		return instance_name;
	}

	public void setInstance_name(String instance_name) {
		if (instance_name == null || instance_name.equals(""))
			this.instance_name = "";
		else
			this.instance_name = instance_name;
	}

	public String getInstance_type() {
		return instance_type;
	}

	public void setInstance_type(String instance_type) {
		if (instance_type == null || instance_type.equals(""))
			this.instance_type = "";
		else
			this.instance_type = instance_type;
	}

	public String getInstance_subtype() {
		return instance_subtype;
	}

	public void setInstance_subtype(String instance_subtype) {
		if (instance_subtype == null || instance_subtype.equals(""))
			this.instance_subtype = "";
		else
			this.instance_subtype = instance_subtype;
	}

	public String getInstance_level() {
		return instance_level;
	}

	public void setInstance_level(String instance_level) {
		if (instance_level == null || instance_level.equals(""))
			this.instance_level = "";
		else
			this.instance_level = instance_level;
	}

	public String getInstance_diff() {
		return instance_diff;
	}

	public void setInstance_diff(String instance_diff) {
		if (instance_diff == null || instance_diff.equals(""))
			this.instance_diff = "";
		else
			this.instance_diff = instance_diff;
	}

	public String getInstance_status() {
		return instance_status;
	}

	public void setInstance_status(String instance_status) {
		if (instance_status == null || instance_status.equals(""))
			this.instance_status = "";
		else
			this.instance_status = instance_status;
	}

	public String getInstance_result() {
		return instance_result;
	}

	public void setInstance_result(String instance_result) {
		if (instance_result == null || instance_result.equals(""))
			this.instance_result = "";
		else
			this.instance_result = instance_result;
	}

	public String getTeam_id() {
		return team_id;
	}

	public void setTeam_id(String team_id) {
		if (team_id == null || team_id.equals(""))
			this.team_id = "";
		else
			this.team_id = team_id;
	}

	public String getInstance_used_time() {
		return instance_used_time;
	}

	public void setInstance_used_time(String instance_used_time) {
		if (instance_used_time == null || instance_used_time.equals(""))
			this.instance_used_time = "";
		else
			this.instance_used_time = instance_used_time;
	}

	public String getInstance_floor() {
		return instance_floor;
	}

	public void setInstance_floor(String instance_floor) {
		if (instance_floor == null || instance_floor.equals(""))
			this.instance_floor = "";
		else
			this.instance_floor = instance_floor;
	}

}
