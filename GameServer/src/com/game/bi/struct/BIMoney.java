package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [货币流水]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIMoney {
	/**
	 * 货币类型，1=钻石1，2-钻石2，3=绑定钻石，4=金币，5=绑定金币 提供相关【字典】
	 */
	private String money_type = "";
	/**
	 * 变化类型 1:增加 0:减少
	 */
	private String change_type = "";
	/**
	 * 变化数量
	 */
	private String amount = "";
	/**
	 * 变化前余额
	 */
	private String before_amount = "";
	/**
	 * 变化后余额
	 */
	private String after_amount = "";
	/**
	 * 货币产出/消耗途径的分类，枚举类型，由字典表管理翻译内容，增加货币的获得点；例：充值获得元宝，任务获得金币。减少货币的消耗点；例：元宝购买道具等 提供相关【字典】
	 */
	private String money_op_type = "";
	/**
	 * 货币获得或者消耗的对象类型，如 1=道具，2=任务;元宝购买道具时，该值为1，任务获得金币时，该值为2 提供相关【字典】
	 */
	private String money_op_target_type = "";
	/**
	 * 货币获得或者消耗对象的值，例：元宝购买道具时，该值设为道具的索引ID; 例：任务获得金币时，该值设为对应的任务ID 提供相关【字典】
	 */
	private String money_op_target_value = "";
	/**
	 * 货币获得或者消耗对象的类型的数量，例：元宝购买道具时，该值设为道具的数量; 如果没有则设为空
	 */
	private String money_op_target_count = "";
	/**
	 * 货币获得或者消耗的对象属性，例如等级/品阶等
	 */
	private String money_op_target_attr = "";

	public BIMoney() {}

	public BIMoney(
			String money_type,
			String change_type,
			String amount,
			String before_amount,
			String after_amount,
			String money_op_type,
			String money_op_target_type,
			String money_op_target_value,
			String money_op_target_count,
			String money_op_target_attr
	) {
		setMoney_type(money_type);
		setChange_type(change_type);
		setAmount(amount);
		setBefore_amount(before_amount);
		setAfter_amount(after_amount);
		setMoney_op_type(money_op_type);
		setMoney_op_target_type(money_op_target_type);
		setMoney_op_target_value(money_op_target_value);
		setMoney_op_target_count(money_op_target_count);
		setMoney_op_target_attr(money_op_target_attr);
	}

	public String getMoney_type() {
		return money_type;
	}

	public void setMoney_type(String money_type) {
		if (money_type == null || money_type.equals(""))
			this.money_type = "";
		else
			this.money_type = money_type;
	}

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		if (change_type == null || change_type.equals(""))
			this.change_type = "";
		else
			this.change_type = change_type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		if (amount == null || amount.equals(""))
			this.amount = "";
		else
			this.amount = amount;
	}

	public String getBefore_amount() {
		return before_amount;
	}

	public void setBefore_amount(String before_amount) {
		if (before_amount == null || before_amount.equals(""))
			this.before_amount = "";
		else
			this.before_amount = before_amount;
	}

	public String getAfter_amount() {
		return after_amount;
	}

	public void setAfter_amount(String after_amount) {
		if (after_amount == null || after_amount.equals(""))
			this.after_amount = "";
		else
			this.after_amount = after_amount;
	}

	public String getMoney_op_type() {
		return money_op_type;
	}

	public void setMoney_op_type(String money_op_type) {
		if (money_op_type == null || money_op_type.equals(""))
			this.money_op_type = "";
		else
			this.money_op_type = money_op_type;
	}

	public String getMoney_op_target_type() {
		return money_op_target_type;
	}

	public void setMoney_op_target_type(String money_op_target_type) {
		if (money_op_target_type == null || money_op_target_type.equals(""))
			this.money_op_target_type = "";
		else
			this.money_op_target_type = money_op_target_type;
	}

	public String getMoney_op_target_value() {
		return money_op_target_value;
	}

	public void setMoney_op_target_value(String money_op_target_value) {
		if (money_op_target_value == null || money_op_target_value.equals(""))
			this.money_op_target_value = "";
		else
			this.money_op_target_value = money_op_target_value;
	}

	public String getMoney_op_target_count() {
		return money_op_target_count;
	}

	public void setMoney_op_target_count(String money_op_target_count) {
		if (money_op_target_count == null || money_op_target_count.equals(""))
			this.money_op_target_count = "";
		else
			this.money_op_target_count = money_op_target_count;
	}

	public String getMoney_op_target_attr() {
		return money_op_target_attr;
	}

	public void setMoney_op_target_attr(String money_op_target_attr) {
		if (money_op_target_attr == null || money_op_target_attr.equals(""))
			this.money_op_target_attr = "";
		else
			this.money_op_target_attr = money_op_target_attr;
	}

}
