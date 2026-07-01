package com.game.chum.struct;

/**
 * @explain: desc
 * @time Created on 2019/11/5 17:48.
 * @author: tc
 */
public class ChumSoul {
	private long roleID;
	private long fight; // 战力

	public ChumSoul(long roleID, long fight) {
		this.roleID = roleID;
		this.fight = fight;
	}

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public long getFight() {
		return fight;
	}

	public void setFight(long fight) {
		this.fight = fight;
	}
}
