package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [公会成员变化]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIGuild_member {
	/**
	 * 行会ID
	 */
	private String guild_id = "";
	/**
	 * 行会等级
	 */
	private String guild_level = "";
	/**
	 * 操作类型 1:创建 2:加入 3:主动解散工会 4:工会解散被踢出 5:被管理员踢出 6:主动退出 7:人数不足自动解散 8:会长丢失解散公会 9:会长不存在解散公会 10:职位变化
	 */
	private String change_type = "";
	/**
	 * 行会成员身份 1:成员 2:长老 3:副会长 4:会长
	 */
	private String status = "";
	/**
	 * 操作后行会人数
	 */
	private String member = "";
	/**
	 * 操作后行会战斗力（游戏设定为取战斗力前10名平均战力）
	 */
	private String guild_power = "";

	public BIGuild_member() {}

	public BIGuild_member(
			String guild_id,
			String guild_level,
			String change_type,
			String status,
			String member,
			String guild_power
	) {
		setGuild_id(guild_id);
		setGuild_level(guild_level);
		setChange_type(change_type);
		setStatus(status);
		setMember(member);
		setGuild_power(guild_power);
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

	public String getGuild_level() {
		return guild_level;
	}

	public void setGuild_level(String guild_level) {
		if (guild_level == null || guild_level.equals(""))
			this.guild_level = "";
		else
			this.guild_level = guild_level;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		if (change_type == null || change_type.equals(""))
			this.change_type = "";
		else
			this.change_type = change_type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		if (status == null || status.equals(""))
			this.status = "";
		else
			this.status = status;
	}

	public String getMember() {
		return member;
	}

	public void setMember(String member) {
		if (member == null || member.equals(""))
			this.member = "";
		else
			this.member = member;
	}

	public String getGuild_power() {
		return guild_power;
	}

	public void setGuild_power(String guild_power) {
		if (guild_power == null || guild_power.equals(""))
			this.guild_power = "";
		else
			this.guild_power = guild_power;
	}

}
