package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [怪物击杀日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIMonster_kill {
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
	 * 怪物id
	 */
	private String monster_id = "";
	/**
	 * 怪物名字
	 */
	private String monster_name = "";
	/**
	 * 怪物类型
	 */
	private String monster_type = "";
	/**
	 * 怪物等级
	 */
	private String monster_level = "";
	/**
	 * 累计伤害
	 */
	private String dps = "";
	/**
	 * 伤害排名
	 */
	private String dps_rank = "";
	/**
	 * 公会ID
	 */
	private String guild_id = "";
	/**
	 * 怪物子类型【字典表定义】
	 */
	private String monster_subtype = "";
	/**
	 * 所属公会伤害排名
	 */
	private String dps_guild_rank = "";

	public BIMonster_kill() {}

	public BIMonster_kill(
			String instance_id,
			String instance_name,
			String instance_type,
			String instance_level,
			String monster_id,
			String monster_name,
			String monster_type,
			String monster_level,
			String dps,
			String dps_rank,
			String guild_id,
			String monster_subtype,
			String dps_guild_rank
	) {
		setInstance_id(instance_id);
		setInstance_name(instance_name);
		setInstance_type(instance_type);
		setInstance_level(instance_level);
		setMonster_id(monster_id);
		setMonster_name(monster_name);
		setMonster_type(monster_type);
		setMonster_level(monster_level);
		setDps(dps);
		setDps_rank(dps_rank);
		setGuild_id(guild_id);
		setMonster_subtype(monster_subtype);
		setDps_guild_rank(dps_guild_rank);
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

	public String getMonster_id() {
		return monster_id;
	}

	public void setMonster_id(String monster_id) {
		if (monster_id == null || monster_id.equals(""))
			this.monster_id = "";
		else
			this.monster_id = monster_id;
	}

	public String getMonster_name() {
		return monster_name;
	}

	public void setMonster_name(String monster_name) {
		if (monster_name == null || monster_name.equals(""))
			this.monster_name = "";
		else
			this.monster_name = monster_name;
	}

	public String getMonster_type() {
		return monster_type;
	}

	public void setMonster_type(String monster_type) {
		if (monster_type == null || monster_type.equals(""))
			this.monster_type = "";
		else
			this.monster_type = monster_type;
	}

	public String getMonster_level() {
		return monster_level;
	}

	public void setMonster_level(String monster_level) {
		if (monster_level == null || monster_level.equals(""))
			this.monster_level = "";
		else
			this.monster_level = monster_level;
	}

	public String getDps() {
		return dps;
	}

	public void setDps(String dps) {
		if (dps == null || dps.equals(""))
			this.dps = "";
		else
			this.dps = dps;
	}

	public String getDps_rank() {
		return dps_rank;
	}

	public void setDps_rank(String dps_rank) {
		if (dps_rank == null || dps_rank.equals(""))
			this.dps_rank = "";
		else
			this.dps_rank = dps_rank;
	}

	public String getGuild_id() {
		return guild_id;
	}

	public void setGuild_id(String guild_id) {
		if (guild_id == null || guild_id.equals(""))
			this.guild_id = "";
		else
			this.guild_id = guild_id;
	}

	public String getMonster_subtype() {
		return monster_subtype;
	}

	public void setMonster_subtype(String monster_subtype) {
		if (monster_subtype == null || monster_subtype.equals(""))
			this.monster_subtype = "";
		else
			this.monster_subtype = monster_subtype;
	}

	public String getDps_guild_rank() {
		return dps_guild_rank;
	}

	public void setDps_guild_rank(String dps_guild_rank) {
		if (dps_guild_rank == null || dps_guild_rank.equals(""))
			this.dps_guild_rank = "";
		else
			this.dps_guild_rank = dps_guild_rank;
	}

}
