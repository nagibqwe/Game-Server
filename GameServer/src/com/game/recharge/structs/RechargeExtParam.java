package com.game.recharge.structs;

/**
 * @explain: desc
 * @time Created on 2019/11/20 16:40.
 * @author: tc
 */
public class RechargeExtParam {
	private int srv_id;
	private long role_id;

	@Override
	public String toString() {
		return "RechargeExtParam{" +
				"srv_id=" + srv_id +
				", role_id=" + role_id +
				'}';
	}

	public int getSrv_id() {
		return srv_id;
	}

	public void setSrv_id(int srv_id) {
		this.srv_id = srv_id;
	}

	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}
}
