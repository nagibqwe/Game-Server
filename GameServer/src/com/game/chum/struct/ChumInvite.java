package com.game.chum.struct;

import com.data.Global;
import game.core.util.TimeUtils;

/**
 * @explain: desc
 * @time Created on 2019/10/23 15:22.
 * @author: tc
 */
public class ChumInvite {
	private long roleID;
	private long targetID;
	private int inviteID;
	private long time;

	public ChumInvite() {
		this.roleID = 0;
		this.targetID = 0;
		this.inviteID = 0;
		this.time = 0;
	}

	public ChumInvite(long roleID, long targetID, int inviteID, long time) {
		this.roleID = roleID;
		this.targetID = targetID;
		this.inviteID = inviteID;
		this.time = time;
	}

	public void reset() {
		this.targetID = 0;
		this.inviteID = 0;
		this.time = 0;
	}

	/**
	 * 是否在CD中
	 * @return
	 */
	public boolean isCD() {
		if (time == 0)
			return false;

		return TimeUtils.Time() - time < Global.Newguild_inveterate_CD * 1000;
	}

	public void newCDTime() {
		this.time = TimeUtils.Time();
	}

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public long getTargetID() {
		return targetID;
	}

	public void setTargetID(long targetID) {
		this.targetID = targetID;
	}

	public int getInviteID() {
		return inviteID;
	}

	public void setInviteID(int inviteID) {
		this.inviteID = inviteID;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}
}
