package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [死亡日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIDeath {
	/**
	 * 副本ID
	 */
	private String instance_id = "";
	/**
	 * 副本名字
	 */
	private String instance_name = "";
	/**
	 * 副本类型
	 */
	private String instance_type = "";
	/**
	 * 副本等级
	 */
	private String instance_level = "";
	/**
	 * 击杀者类型 如：1:玩家 2:NPC
	 */
	private String killer_type = "";
	/**
	 * 击杀者ID 如PVP击杀记录玩家角色id，如PVE击杀记录NPCid
	 */
	private String killer_id = "";
	/**
	 * 击杀者角色名 如PVP击杀记录玩家角色名，如PVE击杀记录NPC名字
	 */
	private String killer_name = "";
	/**
	 * 击杀者等级
	 */
	private String killer_level = "";
	/**
	 * 击杀者当时战力值
	 */
	private String killer_combat = "";

	public BIDeath() {}

	public BIDeath(
			String instance_id,
			String instance_name,
			String instance_type,
			String instance_level,
			String killer_type,
			String killer_id,
			String killer_name,
			String killer_level,
			String killer_combat
	) {
		setInstance_id(instance_id);
		setInstance_name(instance_name);
		setInstance_type(instance_type);
		setInstance_level(instance_level);
		setKiller_type(killer_type);
		setKiller_id(killer_id);
		setKiller_name(killer_name);
		setKiller_level(killer_level);
		setKiller_combat(killer_combat);
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

	public String getInstance_level() {
		return instance_level;
	}

	public void setInstance_level(String instance_level) {
		if (instance_level == null || instance_level.equals(""))
			this.instance_level = "";
		else
			this.instance_level = instance_level;
	}

	public String getKiller_type() {
		return killer_type;
	}

	public void setKiller_type(String killer_type) {
		if (killer_type == null || killer_type.equals(""))
			this.killer_type = "";
		else
			this.killer_type = killer_type;
	}

	public String getKiller_id() {
		return killer_id;
	}

	public void setKiller_id(String killer_id) {
		if (killer_id == null || killer_id.equals(""))
			this.killer_id = "";
		else
			this.killer_id = killer_id;
	}

	public String getKiller_name() {
		return killer_name;
	}

	public void setKiller_name(String killer_name) {
		if (killer_name == null || killer_name.equals(""))
			this.killer_name = "";
		else
			this.killer_name = killer_name;
	}

	public String getKiller_level() {
		return killer_level;
	}

	public void setKiller_level(String killer_level) {
		if (killer_level == null || killer_level.equals(""))
			this.killer_level = "";
		else
			this.killer_level = killer_level;
	}

	public String getKiller_combat() {
		return killer_combat;
	}

	public void setKiller_combat(String killer_combat) {
		if (killer_combat == null || killer_combat.equals(""))
			this.killer_combat = "";
		else
			this.killer_combat = killer_combat;
	}

}
