package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [聊天日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIChat {
	/**
	 * 聊天频道 1:系统频道 2:队伍频道 3:世界频道等
	 */
	private String chat_source = "";
	/**
	 * 是否为系统消息，1:系统消息 2:玩家手动输入消息
	 */
	private String sys_flag = "";
	/**
	 * 聊天内容
	 */
	private String chat_txt = "";
	/**
	 * 聊天对象账号id
	 */
	private String object_account_id = "";
	/**
	 * 聊天对象角色id
	 */
	private String object_role_id = "";
	/**
	 * 聊天对象角色名
	 */
	private String object_role_name = "";
	/**
	 * 聊天对象ip
	 */
	private String object_ip = "";
	/**
	 * 聊天对象设备号
	 */
	private String object_device_id = "";

	public BIChat() {}

	public BIChat(
			String chat_source,
			String sys_flag,
			String chat_txt,
			String object_account_id,
			String object_role_id,
			String object_role_name,
			String object_ip,
			String object_device_id
	) {
		setChat_source(chat_source);
		setSys_flag(sys_flag);
		setChat_txt(chat_txt);
		setObject_account_id(object_account_id);
		setObject_role_id(object_role_id);
		setObject_role_name(object_role_name);
		setObject_ip(object_ip);
		setObject_device_id(object_device_id);
	}

	public String getChat_source() {
		return chat_source;
	}

	public void setChat_source(String chat_source) {
		if (chat_source == null || chat_source.equals(""))
			this.chat_source = "";
		else
			this.chat_source = chat_source;
	}

	public String getSys_flag() {
		return sys_flag;
	}

	public void setSys_flag(String sys_flag) {
		if (sys_flag == null || sys_flag.equals(""))
			this.sys_flag = "";
		else
			this.sys_flag = sys_flag;
	}

	public String getChat_txt() {
		return chat_txt;
	}

	public void setChat_txt(String chat_txt) {
		if (chat_txt == null || chat_txt.equals(""))
			this.chat_txt = "";
		else
			this.chat_txt = chat_txt;
	}

	public String getObject_account_id() {
		return object_account_id;
	}

	public void setObject_account_id(String object_account_id) {
		if (object_account_id == null || object_account_id.equals(""))
			this.object_account_id = "";
		else
			this.object_account_id = object_account_id;
	}

	public String getObject_role_id() {
		return object_role_id;
	}

	public void setObject_role_id(String object_role_id) {
		if (object_role_id == null || object_role_id.equals(""))
			this.object_role_id = "";
		else
			this.object_role_id = object_role_id;
	}

	public String getObject_role_name() {
		return object_role_name;
	}

	public void setObject_role_name(String object_role_name) {
		if (object_role_name == null || object_role_name.equals(""))
			this.object_role_name = "";
		else
			this.object_role_name = object_role_name;
	}

	public String getObject_ip() {
		return object_ip;
	}

	public void setObject_ip(String object_ip) {
		if (object_ip == null || object_ip.equals(""))
			this.object_ip = "";
		else
			this.object_ip = object_ip;
	}

	public String getObject_device_id() {
		return object_device_id;
	}

	public void setObject_device_id(String object_device_id) {
		if (object_device_id == null || object_device_id.equals(""))
			this.object_device_id = "";
		else
			this.object_device_id = object_device_id;
	}

}
