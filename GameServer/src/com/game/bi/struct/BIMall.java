package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [商城购买]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIMall {
	/**
	 * 用于区分钻石/金币/成就等商城类型,1=钻石商店，2=金币商店
	 */
	private String mall_type = "";
	/**
	 * 商城购买的商品类型
	 */
	private String item_type = "";
	/**
	 * 商城购买的商品id
	 */
	private String item_id = "";
	/**
	 * 单次购买的商品数量
	 */
	private String item_num = "";
	/**
	 * 货币类型，1:钻石1 2:钻石2 3:绑定钻石 4:金币，5:绑定金币 提供相关【字典】
	 */
	private String money_type = "";
	/**
	 * 支付货币数量
	 */
	private String amount = "";
	/**
	 * 商城出售的单个物品原价
	 */
	private String price = "";
	/**
	 * 物品上架的位置编号
	 */
	private String location = "";

	public BIMall() {}

	public BIMall(
			String mall_type,
			String item_type,
			String item_id,
			String item_num,
			String money_type,
			String amount,
			String price,
			String location
	) {
		setMall_type(mall_type);
		setItem_type(item_type);
		setItem_id(item_id);
		setItem_num(item_num);
		setMoney_type(money_type);
		setAmount(amount);
		setPrice(price);
		setLocation(location);
	}

	public String getMall_type() {
		return mall_type;
	}

	public void setMall_type(String mall_type) {
		if (mall_type == null || mall_type.equals(""))
			this.mall_type = "";
		else
			this.mall_type = mall_type;
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

	public String getItem_id() {
		return item_id;
	}

	public void setItem_id(String item_id) {
		if (item_id == null || item_id.equals(""))
			this.item_id = "";
		else
			this.item_id = item_id;
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

	public String getMoney_type() {
		return money_type;
	}

	public void setMoney_type(String money_type) {
		if (money_type == null || money_type.equals(""))
			this.money_type = "";
		else
			this.money_type = money_type;
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

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		if (price == null || price.equals(""))
			this.price = "";
		else
			this.price = price;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if (location == null || location.equals(""))
			this.location = "";
		else
			this.location = location;
	}

}
