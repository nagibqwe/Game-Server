package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [角色登出]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BILogout {
	/**
	 * 本次会话登录时间
	 */
	private String login_time = "";
	/**
	 * 角色登录等级
	 */
	private String login_level = "";
	/**
	 * 角色登录时vip等级，如果没有则设为空
	 */
	private String login_vip_level = "";
	/**
	 * 本次会话在线时长,单位：秒
	 */
	private String online_time = "";
	/**
	 * 退出类型 1:正常退出 2:顶号退出 3:报错退出
	 */
	private String logout_type = "";
	/**
	 * 退出时剩余钻石数
	 */
	private String money_balance = "";
	/**
	 * 退出时剩余钻石2数
	 */
	private String money2_balance = "";
	/**
	 * 退出时剩余绑定钻石数
	 */
	private String bmoney_balance = "";
	/**
	 * 退出时剩余金币数
	 */
	private String gold_balance = "";
	/**
	 * 退出时剩余绑定金币数
	 */
	private String bgold_balance = "";
	/**
	 * 大师点，活动用，每轮活动都需要清空一次
	 */
	private String master_point = "";
	/**
	 * 是否有找回资源资格,0:没有,1:有
	 */
	private String is_retrieve = "";

	public BILogout() {}

	public BILogout(
			String login_time,
			String login_level,
			String login_vip_level,
			String online_time,
			String logout_type,
			String money_balance,
			String money2_balance,
			String bmoney_balance,
			String gold_balance,
			String bgold_balance,
			String master_point,
			String is_retrieve
	) {
		setLogin_time(login_time);
		setLogin_level(login_level);
		setLogin_vip_level(login_vip_level);
		setOnline_time(online_time);
		setLogout_type(logout_type);
		setMoney_balance(money_balance);
		setMoney2_balance(money2_balance);
		setBmoney_balance(bmoney_balance);
		setGold_balance(gold_balance);
		setBgold_balance(bgold_balance);
		setMaster_point(master_point);
		setIs_retrieve(is_retrieve);
	}

	public String getLogin_time() {
		return login_time;
	}

	public void setLogin_time(String login_time) {
		if (login_time == null || login_time.equals(""))
			this.login_time = "";
		else
			this.login_time = login_time;
	}

	public String getLogin_level() {
		return login_level;
	}

	public void setLogin_level(String login_level) {
		if (login_level == null || login_level.equals(""))
			this.login_level = "";
		else
			this.login_level = login_level;
	}

	public String getLogin_vip_level() {
		return login_vip_level;
	}

	public void setLogin_vip_level(String login_vip_level) {
		if (login_vip_level == null || login_vip_level.equals(""))
			this.login_vip_level = "";
		else
			this.login_vip_level = login_vip_level;
	}

	public String getOnline_time() {
		return online_time;
	}

	public void setOnline_time(String online_time) {
		if (online_time == null || online_time.equals(""))
			this.online_time = "";
		else
			this.online_time = online_time;
	}

	public String getLogout_type() {
		return logout_type;
	}

	public void setLogout_type(String logout_type) {
		if (logout_type == null || logout_type.equals(""))
			this.logout_type = "";
		else
			this.logout_type = logout_type;
	}

	public String getMoney_balance() {
		return money_balance;
	}

	public void setMoney_balance(String money_balance) {
		if (money_balance == null || money_balance.equals(""))
			this.money_balance = "";
		else
			this.money_balance = money_balance;
	}

	public String getMoney2_balance() {
		return money2_balance;
	}

	public void setMoney2_balance(String money2_balance) {
		if (money2_balance == null || money2_balance.equals(""))
			this.money2_balance = "";
		else
			this.money2_balance = money2_balance;
	}

	public String getBmoney_balance() {
		return bmoney_balance;
	}

	public void setBmoney_balance(String bmoney_balance) {
		if (bmoney_balance == null || bmoney_balance.equals(""))
			this.bmoney_balance = "";
		else
			this.bmoney_balance = bmoney_balance;
	}

	public String getGold_balance() {
		return gold_balance;
	}

	public void setGold_balance(String gold_balance) {
		if (gold_balance == null || gold_balance.equals(""))
			this.gold_balance = "";
		else
			this.gold_balance = gold_balance;
	}

	public String getBgold_balance() {
		return bgold_balance;
	}

	public void setBgold_balance(String bgold_balance) {
		if (bgold_balance == null || bgold_balance.equals(""))
			this.bgold_balance = "";
		else
			this.bgold_balance = bgold_balance;
	}

	public String getMaster_point() {
		return master_point;
	}

	public void setMaster_point(String master_point) {
		if (master_point == null || master_point.equals(""))
			this.master_point = "";
		else
			this.master_point = master_point;
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
