package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [公会升级]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIGuild_upgrade {
	/**
	 * 行会ID
	 */
	private String guild_id = "";
	/**
	 * 公会名
	 */
	private String guild_name = "";
	/**
	 * 升级类型 1:仙盟大厅 2:仙盟商店 3:仙盟驻地
	 */
	private String level_type = "";
	/**
	 * 行会升级前等级
	 */
	private String before_guild_level = "";
	/**
	 * 行会升级后等级
	 */
	private String later_guild_level = "";

	public BIGuild_upgrade() {}

	public BIGuild_upgrade(
			String guild_id,
			String guild_name,
			String level_type,
			String before_guild_level,
			String later_guild_level
	) {
		setGuild_id(guild_id);
		setGuild_name(guild_name);
		setLevel_type(level_type);
		setBefore_guild_level(before_guild_level);
		setLater_guild_level(later_guild_level);
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

	public String getGuild_name() {
		return guild_name;
	}

	public void setGuild_name(String guild_name) {
		if (guild_name == null || guild_name.equals(""))
			this.guild_name = "";
		else
			this.guild_name = guild_name;
	}

	public String getLevel_type() {
		return level_type;
	}

	public void setLevel_type(String level_type) {
		if (level_type == null || level_type.equals(""))
			this.level_type = "";
		else
			this.level_type = level_type;
	}

	public String getBefore_guild_level() {
		return before_guild_level;
	}

	public void setBefore_guild_level(String before_guild_level) {
		if (before_guild_level == null || before_guild_level.equals(""))
			this.before_guild_level = "";
		else
			this.before_guild_level = before_guild_level;
	}

	public String getLater_guild_level() {
		return later_guild_level;
	}

	public void setLater_guild_level(String later_guild_level) {
		if (later_guild_level == null || later_guild_level.equals(""))
			this.later_guild_level = "";
		else
			this.later_guild_level = later_guild_level;
	}

}
