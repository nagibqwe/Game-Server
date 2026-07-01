package com.game.recharge.structs;

import game.core.net.Config.ServerConfig;
import game.core.util.CodedUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/20 14:14.
 * @author: tc
 */
public class Recharge {
	/**
	 * 充值订单号
	 */
	private String order_no;
	/**
	 * 角色ID
	 */
	private long role_id;
	/**
	 * 商品ID
	 */
	private int goods_id;
	/**
	 * 商品类型
	 */
	private String goods_type;
	/**
	 * 商品扩展数据;商品管理后台如果配置了此参数,则将填充在此处
	 */
	private String goods_ext;
	/**
	 * 订单金额:单位分`
	 */
	private int total_fee;
	/**
	 * 待发放道具ID;商品管理后台如果配置了此参数,则将填充在此处
	 */
	private int item_id;
	/**
	 * 待发放游戏货币数;商品管理后台如果配置了此参数,则将填充在此处
	 */
	private int game_money;
	/**
	 * 透传扩展参数,客户端下单时如果传递了扩展参数,则将填充在此处
	 */
	private String ext_param;
	/**
	 * 签名算法，目前支持md5。 该字段不参与签名
	 */
	private String sign_type;
	/**
	 * md5签名（签名算法，见最后）签名使用的私钥是x8Server生成的，创建游戏的时候会生成，给到游戏那边。 该字段不参与签名
	 */
	private String sign;
	/**
	 * 商品扩展Code
	 */
	private String goods_code;
	/**
	 * 商品扩展数据
	 */
	private String goods_name;

	/**
	 * 货币类型 THB，HKD，CNY
	 */
	private String money_type;

	/**
	 * 异步通知时间
	 */
	private String notify_time;
	/**
	 * 异步通知ID
	 */
	private String notify_id;

	/**
	 *第三方支付订单
	 */
	private String trade_no;

	/**
	 *支付成功,目前就只有此类型
	 */
	private int trade_status;

	/**
	 * 计算到游戏累充值
	 */
	private int totalRecharge;

	/**
	 * vip经验加成
	 */
	private int totalVipPower;



	/**
	 * 计算签名
	 * @param map
	 * @return
	 */
	public static String sign(HashMap<String, String> map) {
		List<String> keys = new ArrayList<>();
		for (String key : map.keySet()) {
			if (key.equals("sign_type") || key.equals("sign"))
				continue;

			if (map.get(key).equals(""))
				continue;

			keys.add(key);
		}

		keys.sort(new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return o1.compareTo(o2);
			}
		});

		StringBuilder builder = new StringBuilder();
		int n = keys.size();
		for (int i = 0; i < n; i++) {
			builder.append(keys.get(i)).append("=").append(map.get(keys.get(i)));
			if (i != n - 1)
				builder.append("&");
		}
		builder.append(ServerConfig.getPrivateKey());
		return CodedUtil.Md5(builder.toString());
	}

	@Override
	public String toString() {
		return "Recharge{" +
				"order_no='" + order_no + '\'' +
				", role_id=" + role_id +
				", goods_id=" + goods_id +
				", goods_type='" + goods_type + '\'' +
				", goods_ext='" + goods_ext + '\'' +
				", total_fee=" + total_fee +
				", item_id=" + item_id +
				", game_money=" + game_money +
				", ext_param='" + ext_param + '\'' +
				", sign_type='" + sign_type + '\'' +
				", sign='" + sign + '\'' +
				", goods_code='" + goods_code + '\'' +
				", goods_name='" + goods_name + '\'' +
				", money_type='" + money_type + '\'' +
				", notify_time='" + notify_time + '\'' +
				", notify_id='" + notify_id + '\'' +
				", trade_no='" + trade_no + '\'' +
				", trade_status='" + trade_status + '\'' +
				", totalRecharge='" + totalRecharge + '\'' +
				", totalVipPower='" + totalVipPower + '\'' +
				'}';
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public String getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(String goods_type) {
		this.goods_type = goods_type;
	}

	public String getGoods_ext() {
		return goods_ext;
	}

	public void setGoods_ext(String goods_ext) {
		this.goods_ext = goods_ext;
	}

	public int getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(int total_fee) {
		this.total_fee = total_fee;
	}

	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	public int getGame_money() {
		return game_money;
	}

	public void setGame_money(int game_money) {
		this.game_money = game_money;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	public String getExt_param() {
		return ext_param;
	}

	public void setExt_param(String ext_param) {
		this.ext_param = ext_param;
	}

	public String getGoods_code() {
		return goods_code;
	}

	public void setGoods_code(String goods_code) {
		this.goods_code = goods_code;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getMoney_type() {
		return money_type;
	}

	public void setMoney_type(String money_type) {
		this.money_type = money_type;
	}

	public String getNotify_time() {
		return notify_time;
	}

	public void setNotify_time(String notify_time) {
		this.notify_time = notify_time;
	}

	public String getNotify_id() {
		return notify_id;
	}

	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}

	public String getTrade_no() {
		return trade_no;
	}

	public void setTrade_no(String trade_no) {
		this.trade_no = trade_no;
	}

	public int getTrade_status() {
		return trade_status;
	}

	public void setTrade_status(int trade_status) {
		this.trade_status = trade_status;
	}

	public int getTotalRecharge() {
		return totalRecharge;
	}

	public void setTotalRecharge(int totalRecharge) {
		this.totalRecharge = totalRecharge;
	}

	public int getTotalVipPower() {
		return totalVipPower;
	}

	public void setTotalVipPower(int totalVipPower) {
		this.totalVipPower = totalVipPower;
	}
}
