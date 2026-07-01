package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [角色登录]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BILogin {
	/**
	 * 联网方式(networking mode),如:3G、4G、WIFI
	 */
	private String device_nm = "";
	/**
	 * 登录类型 1:普通登录 2:掉线重连 3:跨天登记
	 */
	private String login_type = "";
	/**
	 * 是否有找回资源资格,0:没有,1:有
	 */
	private String is_retrieve = "";

	public BILogin() {}

	public BILogin(
			String device_nm,
			String login_type,
			String is_retrieve
	) {
		setDevice_nm(device_nm);
		setLogin_type(login_type);
		setIs_retrieve(is_retrieve);
	}

	public String getDevice_nm() {
		return device_nm;
	}

	public void setDevice_nm(String device_nm) {
		if (device_nm == null || device_nm.equals(""))
			this.device_nm = "";
		else
			this.device_nm = device_nm;
	}

	public String getLogin_type() {
		return login_type;
	}

	public void setLogin_type(String login_type) {
		if (login_type == null || login_type.equals(""))
			this.login_type = "";
		else
			this.login_type = login_type;
	}

	public String getIs_retrieve() {
		return is_retrieve;
	}

	public void setIs_retrieve(String is_retrieve) {
		if (is_retrieve == null || is_retrieve.equals(""))
			this.is_retrieve = "";
		else
			this.is_retrieve = is_retrieve;
	}

}
