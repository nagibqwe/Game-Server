package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [充值流水]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIPay {
	/**
	 * 游戏内部订单号
	 */
	private String order_id = "";
	/**
	 * 外部关联充值平台名，如SDK
	 */
	private String pay_platform = "";
	/**
	 * 外部关联充值平台订单号，如果是接绿岸SDK，则为SDK返回的订单号
	 */
	private String pay_order_id = "";
	/**
	 * 充值类型 0:测试充值 1:正常充值 99:GM后台充值
	 */
	private String pay_type = "";
	/**
	 * 支付状态 1:未支付 2:支付前取消 3:支付成功 4:发货失败 5:确认发货订单完成
	 */
	private String pay_status = "";
	/**
	 * 实际充值货币类型，RMB:人民币 THB:泰铢 VND:越南盾 MYR:马来元 SGD:新加坡元 USD:美元
	 */
	private String pay_money_ct = "";
	/**
	 * 充值货币数量
	 */
	private String pay_money_amount = "";
	/**
	 * 充值获得元宝数量
	 */
	private String pay_gold_amount = "";
	/**
	 * 充值购买商品ID
	 */
	private String product_id = "";
	/**
	 * 充值购买商品名称
	 */
	private String product_name = "";
	/**
	 * 充值购买商品数量
	 */
	private String product_amount = "";

	public BIPay() {}

	public BIPay(
			String order_id,
			String pay_platform,
			String pay_order_id,
			String pay_type,
			String pay_status,
			String pay_money_ct,
			String pay_money_amount,
			String pay_gold_amount,
			String product_id,
			String product_name,
			String product_amount
	) {
		setOrder_id(order_id);
		setPay_platform(pay_platform);
		setPay_order_id(pay_order_id);
		setPay_type(pay_type);
		setPay_status(pay_status);
		setPay_money_ct(pay_money_ct);
		setPay_money_amount(pay_money_amount);
		setPay_gold_amount(pay_gold_amount);
		setProduct_id(product_id);
		setProduct_name(product_name);
		setProduct_amount(product_amount);
	}

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		if (order_id == null || order_id.equals(""))
			this.order_id = "";
		else
			this.order_id = order_id;
	}

	public String getPay_platform() {
		return pay_platform;
	}

	public void setPay_platform(String pay_platform) {
		if (pay_platform == null || pay_platform.equals(""))
			this.pay_platform = "";
		else
			this.pay_platform = pay_platform;
	}

	public String getPay_order_id() {
		return pay_order_id;
	}

	public void setPay_order_id(String pay_order_id) {
		if (pay_order_id == null || pay_order_id.equals(""))
			this.pay_order_id = "";
		else
			this.pay_order_id = pay_order_id;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		if (pay_type == null || pay_type.equals(""))
			this.pay_type = "";
		else
			this.pay_type = pay_type;
	}

	public String getPay_status() {
		return pay_status;
	}

	public void setPay_status(String pay_status) {
		if (pay_status == null || pay_status.equals(""))
			this.pay_status = "";
		else
			this.pay_status = pay_status;
	}

	public String getPay_money_ct() {
		return pay_money_ct;
	}

	public void setPay_money_ct(String pay_money_ct) {
		if (pay_money_ct == null || pay_money_ct.equals(""))
			this.pay_money_ct = "";
		else
			this.pay_money_ct = pay_money_ct;
	}

	public String getPay_money_amount() {
		return pay_money_amount;
	}

	public void setPay_money_amount(String pay_money_amount) {
		if (pay_money_amount == null || pay_money_amount.equals(""))
			this.pay_money_amount = "";
		else
			this.pay_money_amount = pay_money_amount;
	}

	public String getPay_gold_amount() {
		return pay_gold_amount;
	}

	public void setPay_gold_amount(String pay_gold_amount) {
		if (pay_gold_amount == null || pay_gold_amount.equals(""))
			this.pay_gold_amount = "";
		else
			this.pay_gold_amount = pay_gold_amount;
	}

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		if (product_id == null || product_id.equals(""))
			this.product_id = "";
		else
			this.product_id = product_id;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		if (product_name == null || product_name.equals(""))
			this.product_name = "";
		else
			this.product_name = product_name;
	}

	public String getProduct_amount() {
		return product_amount;
	}

	public void setProduct_amount(String product_amount) {
		if (product_amount == null || product_amount.equals(""))
			this.product_amount = "";
		else
			this.product_amount = product_amount;
	}

}
