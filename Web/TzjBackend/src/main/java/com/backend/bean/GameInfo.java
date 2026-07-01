package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

@Table("game_info")
public class GameInfo {
	@Id(auto = false)
	@Comment("游戏ID")
	private int gameId;

	@Column
	@Comment("第三方充值密钥")
	@ColDefine(type = ColType.VARCHAR, width = 50)
	@Default("0")
	private String rechargeSecretkey;

	@Column
	@Comment("自动开服起始服务器ID")
	@Default("0")
	private int autoFirstServerId = 0;

	@Column
	@Comment("检查用户人数,自动开服条件,如果不按注册人数开服则填0")
	@Default("0")
	private int autoUserCount = 0;

	@Column
	@Comment("自动开服完成截止服务器ID")
	@Default("0")
	private int autoServerId = 0;

	@Column
	@Comment("修改时间")
	private long time;

	public int getGameId() {
		return gameId;
	}

	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	public String getRechargeSecretkey() {
		return rechargeSecretkey;
	}

	public void setRechargeSecretkey(String rechargeSecretkey) {
		this.rechargeSecretkey = rechargeSecretkey;
	}

	public int getAutoFirstServerId() {
		return autoFirstServerId;
	}

	public void setAutoFirstServerId(int autoFirstServerId) {
		this.autoFirstServerId = autoFirstServerId;
	}

	public int getAutoUserCount() {
		return autoUserCount;
	}

	public void setAutoUserCount(int autoUserCount) {
		this.autoUserCount = autoUserCount;
	}

	public int getAutoServerId() {
		return autoServerId;
	}

	public void setAutoServerId(int autoServerId) {
		this.autoServerId = autoServerId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
