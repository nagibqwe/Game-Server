package com.game.world_help.struct;

import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/12/11 15:30.
 * @author: tc
 */
public class WorldHelpInfo {
	private int id; // 支援唯一ID
	private int bossId;
	private int mapId; // 地图ID
	private long mapUid; // 地图唯一ID
	private long bossCode;
	private long ownerId; // 发起者玩家ID
	private long time; // 发起时间
	private List<Long> helpIds; // 帮助的玩家IDs

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getBossId() {
		return bossId;
	}

	public void setBossId(int bossId) {
		this.bossId = bossId;
	}

	public int getMapId() {
		return mapId;
	}

	public void setMapId(int mapId) {
		this.mapId = mapId;
	}

	public long getMapUid() {
		return mapUid;
	}

	public void setMapUid(long mapUid) {
		this.mapUid = mapUid;
	}

	public long getBossCode() {
		return bossCode;
	}

	public void setBossCode(long bossCode) {
		this.bossCode = bossCode;
	}

	public long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(long ownerId) {
		this.ownerId = ownerId;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public List<Long> getHelpIds() {
		return helpIds;
	}

	public void setHelpIds(List<Long> helpIds) {
		this.helpIds = helpIds;
	}
}
