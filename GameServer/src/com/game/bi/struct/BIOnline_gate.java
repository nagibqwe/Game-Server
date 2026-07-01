package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [在线人数(网关) 仅限采用网关架构的游戏]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIOnline_gate {
	/**
	 * 网关服务器ID
	 */
	private String gate_server_id = "";
	/**
	 * 当前服务器在线人数
	 */
	private String online_num = "";

	public BIOnline_gate() {}

	public BIOnline_gate(
			String gate_server_id,
			String online_num
	) {
		setGate_server_id(gate_server_id);
		setOnline_num(online_num);
	}

	public String getGate_server_id() {
		return gate_server_id;
	}

	public void setGate_server_id(String gate_server_id) {
		if (gate_server_id == null || gate_server_id.equals(""))
			this.gate_server_id = "";
		else
			this.gate_server_id = gate_server_id;
	}

	public String getOnline_num() {
		return online_num;
	}

	public void setOnline_num(String online_num) {
		if (online_num == null || online_num.equals(""))
			this.online_num = "";
		else
			this.online_num = online_num;
	}

}
