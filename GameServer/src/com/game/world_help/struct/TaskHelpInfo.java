package com.game.world_help.struct;

import java.util.List;

/**
 * 任务支援信息
 */
public class TaskHelpInfo {
	private int id; // 支援唯一ID
	private int cloneMapId; //副本ID
	private int taskId; // 任务ID
	private long teamId; // 队伍唯一ID
	private long ownerId; // 发起者玩家ID
	private long time; // 发起时间
	private List<Long> helpIds; // 帮助的玩家IDs

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCloneMapId() {
		return cloneMapId;
	}

	public void setCloneMapId(int cloneMapId) {
		this.cloneMapId = cloneMapId;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public long getTeamId() {
		return teamId;
	}

	public void setTeamId(long teamId) {
		this.teamId = teamId;
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
