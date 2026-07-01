package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [交易日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BITrade {
	/**
	 * 对方账号ID
	 */
	private String t_account_id = "";
	/**
	 * 对方游戏角色ID
	 */
	private String t_role_id = "";
	/**
	 * 对方游戏角色名
	 */
	private String t_role_name = "";
	/**
	 * 对方交易时ip
	 */
	private String t_ip = "";
	/**
	 * 物品id
	 */
	private String item_id = "";
	/**
	 * 物品类型
	 */
	private String item_type = "";
	/**
	 * 交易数量
	 */
	private String item_num = "";
	/**
	 * 交易前的物品数量
	 */
	private String before_num = "";
	/**
	 * 交易后的物品数量
	 */
	private String after_num = "";
	/**
	 * 交易金额
	 */
	private String order_money = "";
	/**
	 * 交易类型 1:交易进 2:交易出
	 */
	private String order_type = "";
	/**
	 * 交易流水号
	 */
	private String order_sn = "";

	public BITrade() {}

	public BITrade(
			String t_account_id,
			String t_role_id,
			String t_role_name,
			String t_ip,
			String item_id,
			String item_type,
			String item_num,
			String before_num,
			String after_num,
			String order_money,
			String order_type,
			String order_sn
	) {
		setT_account_id(t_account_id);
		setT_role_id(t_role_id);
		setT_role_name(t_role_name);
		setT_ip(t_ip);
		setItem_id(item_id);
		setItem_type(item_type);
		setItem_num(item_num);
		setBefore_num(before_num);
		setAfter_num(after_num);
		setOrder_money(order_money);
		setOrder_type(order_type);
		setOrder_sn(order_sn);
	}

	public String getT_account_id() {
		return t_account_id;
	}

	public void setT_account_id(String t_account_id) {
		if (t_account_id == null || t_account_id.equals(""))
			this.t_account_id = "";
		else
			this.t_account_id = t_account_id;
	}

	public String getT_role_id() {
		return t_role_id;
	}

	public void setT_role_id(String t_role_id) {
		if (t_role_id == null || t_role_id.equals(""))
			this.t_role_id = "";
		else
			this.t_role_id = t_role_id;
	}

	public String getT_role_name() {
		return t_role_name;
	}

	public void setT_role_name(String t_role_name) {
		if (t_role_name == null || t_role_name.equals(""))
			this.t_role_name = "";
		else
			this.t_role_name = t_role_name;
	}

	public String getT_ip() {
		return t_ip;
	}

	public void setT_ip(String t_ip) {
		if (t_ip == null || t_ip.equals(""))
			this.t_ip = "";
		else
			this.t_ip = t_ip;
	}

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		if (item_id == null || item_id.equals(""))
			this.item_id = "";
		else
			this.item_id = item_id;
	}

	public String getItem_type() {
		return item_type;
	}

	public void setItem_type(String item_type) {
		if (item_type == null || item_type.equals(""))
			this.item_type = "";
		else
			this.item_type = item_type;
	}

	public String getItem_num() {
		return item_num;
	}

	public void setItem_num(String item_num) {
		if (item_num == null || item_num.equals(""))
			this.item_num = "";
		else
			this.item_num = item_num;
	}

	public String getBefore_num() {
		return before_num;
	}

	public void setBefore_num(String before_num) {
		if (before_num == null || before_num.equals(""))
			this.before_num = "";
		else
			this.before_num = before_num;
	}

	public String getAfter_num() {
		return after_num;
	}

	public void setAfter_num(String after_num) {
		if (after_num == null || after_num.equals(""))
			this.after_num = "";
		else
			this.after_num = after_num;
	}

	public String getOrder_money() {
		return order_money;
	}

	public void setOrder_money(String order_money) {
		if (order_money == null || order_money.equals(""))
			this.order_money = "";
		else
			this.order_money = order_money;
	}

	public String getOrder_type() {
		return order_type;
	}

	public void setOrder_type(String order_type) {
		if (order_type == null || order_type.equals(""))
			this.order_type = "";
		else
			this.order_type = order_type;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		if (order_sn == null || order_sn.equals(""))
			this.order_sn = "";
		else
			this.order_sn = order_sn;
	}

}
