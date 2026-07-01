package com.game.chum.struct;

/**
 * @explain: desc
 * @time Created on 2019/10/22 17:54.
 * @author: tc
 */
public class ChumMember {
	private long roleID;
	// 累计的经验值
	private int exp;

	public long getRoleID() {
		return roleID;
	}

	public void setRoleID(long roleID) {
		this.roleID = roleID;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}
	private ChumMember(){}

	public ChumMember(long roleID, int exp) {
		this.roleID = roleID;
		this.exp = exp;
	}
}
