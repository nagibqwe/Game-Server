package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [角色信息变化]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIRole_info {
	/**
	 * 角色创建时间
	 */
	private String created_time = "";
	/**
	 * 角色最近一次登录时间
	 */
	private String last_login_time = "";
	/**
	 * 角色累计在线时长,单位:秒
	 */
	private String total_online = "";
	/**
	 * 当前经验值
	 */
	private String exp = "";
	/**
	 * 最后一次更新时间
	 */
	private String last_updatetime = "";
	/**
	 * 当前未完成主线任务ID ，若含多个主线任务时，按逗号分隔任务id记录，如：任务A,任务B
	 */
	private String task_id = "";
	/**
	 * 当前钻石余额
	 */
	private String money_balance = "";
	/**
	 * 当前钻石2余额
	 */
	private String money2_balance = "";
	/**
	 * 当前绑定钻石余额
	 */
	private String bmoney_balance = "";
	/**
	 * 当前金币余额
	 */
	private String gold_balance = "";
	/**
	 * 当前绑定金币余额
	 */
	private String bgold_balance = "";
	/**
	 * 仙缘状态 0:未婚 1:已婚
	 */
	private String marry_status = "";
	/**
	 * 仙缘对象角色ID
	 */
	private String marry_partner_id = "";
	/**
	 * 仙缘对象名称
	 */
	private String marry_partner_name = "";
	/**
	 * 仙缘发生时间
	 */
	private String marry_partner_time = "";
	/**
	 * 升仙令 1:未激活 2:限时升仙令 3:永久升仙令
	 */
	private String shengxian_order = "";

	public BIRole_info() {}

	public BIRole_info(
			String created_time,
			String last_login_time,
			String total_online,
			String exp,
			String last_updatetime,
			String task_id,
			String money_balance,
			String money2_balance,
			String bmoney_balance,
			String gold_balance,
			String bgold_balance,
			String marry_status,
			String marry_partner_id,
			String marry_partner_name,
			String marry_partner_time,
			String shengxian_order
	) {
		setCreated_time(created_time);
		setLast_login_time(last_login_time);
		setTotal_online(total_online);
		setExp(exp);
		setLast_updatetime(last_updatetime);
		setTask_id(task_id);
		setMoney_balance(money_balance);
		setMoney2_balance(money2_balance);
		setBmoney_balance(bmoney_balance);
		setGold_balance(gold_balance);
		setBgold_balance(bgold_balance);
		setMarry_status(marry_status);
		setMarry_partner_id(marry_partner_id);
		setMarry_partner_name(marry_partner_name);
		setMarry_partner_time(marry_partner_time);
		setShengxian_order(shengxian_order);
	}

	public String getCreated_time() {
		return created_time;
	}

	public void setCreated_time(String created_time) {
		if (created_time == null || created_time.equals(""))
			this.created_time = "";
		else
			this.created_time = created_time;
	}

	public String getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(String last_login_time) {
		if (last_login_time == null || last_login_time.equals(""))
			this.last_login_time = "";
		else
			this.last_login_time = last_login_time;
	}

	public String getTotal_online() {
		return total_online;
	}

	public void setTotal_online(String total_online) {
		if (total_online == null || total_online.equals(""))
			this.total_online = "";
		else
			this.total_online = total_online;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		if (exp == null || exp.equals(""))
			this.exp = "";
		else
			this.exp = exp;
	}

	public String getLast_updatetime() {
		return last_updatetime;
	}

	public void setLast_updatetime(String last_updatetime) {
		if (last_updatetime == null || last_updatetime.equals(""))
			this.last_updatetime = "";
		else
			this.last_updatetime = last_updatetime;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		if (task_id == null || task_id.equals(""))
			this.task_id = "";
		else
			this.task_id = task_id;
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

	public String getMarry_status() {
		return marry_status;
	}

	public void setMarry_status(String marry_status) {
		if (marry_status == null || marry_status.equals(""))
			this.marry_status = "";
		else
			this.marry_status = marry_status;
	}

	public String getMarry_partner_id() {
		return marry_partner_id;
	}

	public void setMarry_partner_id(String marry_partner_id) {
		if (marry_partner_id == null || marry_partner_id.equals(""))
			this.marry_partner_id = "";
		else
			this.marry_partner_id = marry_partner_id;
	}

	public String getMarry_partner_name() {
		return marry_partner_name;
	}

	public void setMarry_partner_name(String marry_partner_name) {
		if (marry_partner_name == null || marry_partner_name.equals(""))
			this.marry_partner_name = "";
		else
			this.marry_partner_name = marry_partner_name;
	}

	public String getMarry_partner_time() {
		return marry_partner_time;
	}

	public void setMarry_partner_time(String marry_partner_time) {
		if (marry_partner_time == null || marry_partner_time.equals(""))
			this.marry_partner_time = "";
		else
			this.marry_partner_time = marry_partner_time;
	}

	public String getShengxian_order() {
		return shengxian_order;
	}

	public void setShengxian_order(String shengxian_order) {
		if (shengxian_order == null || shengxian_order.equals(""))
			this.shengxian_order = "";
		else
			this.shengxian_order = shengxian_order;
	}

}
