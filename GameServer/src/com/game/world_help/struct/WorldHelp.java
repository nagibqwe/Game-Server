package com.game.world_help.struct;

/**
 * @explain: desc
 * @time Created on 2019/12/11 15:38.
 * @author: tc
 */
public class WorldHelp {
	private int id; // 支援唯一ID
	private boolean isInitiate; // true发起者,false帮助者

	public WorldHelp(int id, boolean isInitiate) {
		this.id = id;
		this.isInitiate = isInitiate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isInitiate() {
		return isInitiate;
	}

	public void setInitiate(boolean initiate) {
		isInitiate = initiate;
	}

	@Override
	public String toString() {
		return "WorldHelp{" +
				"id=" + id +
				", isInitiate=" + isInitiate +
				'}';
	}
}
