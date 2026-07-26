package com.gm.project.gmtool.rechargeItem.domain;

import java.util.TreeMap;

/**
 * Пополнение商品Информация
 */
public class RechargeItemInfo {
	/**
	 * ID пополнения
	 */
	private int goods_id;
	/**
	 * Внутренний ID конфигурации
	 */
	private int goods_system_cfg_id;
	/**
	 * 商品名字描述（主要用于BI后台Данные）
	 */
	private String goods_name;
	/**
	 * Название канала
	 */
	private String goods_pay_channel;
	/**
	 * SDK支付Тип
	 */
	private int goods_pay_type;
	/**
	 * Тип пополнения
	 */
	private int goods_type;
	/**
	 * Подтип пополнения
	 */
	private int goods_subtype;
	/**
	 * Количество пополнений（当前轮每个挡位对应Пополнение的次数)
	 */
	private int goods_limit;
	/**
	 * ID отображаемой иконки
	 */
	private int goods_icon;
	/**
	 * URL изображения товара
	 */
	private String goodsurl;
	/**
	 * Пополнение档位对应消耗的真实货币
	 */
	private TreeMap<String, TreeMap<String,String>> goods_price = new TreeMap<>();
	/**
	 * Пополнение计费点。运营配置
	 */
	private TreeMap<String,String> goods_price_point = new TreeMap<>();
	/**
	 * 界面默认Показывать的货币 例如:THB
	 */
	private String goods_show_price;
	/**
	 * Награда за пополнение
	 */
	private String goods_reward;
	/**
	 * Множитель награды
	 */
	private String goods_multiple;
	/**
	 * Дополнительная награда
	 */
	private String goods_extra_reward;
	/**
	 * Количество дополнительных наград
	 */
	private int goods_extra_reward_limit;
	/**
	 * 商品扩展字段
	 */
	private String goods_ext;
	/**
	 * ДаНет计入到游戏累充活动
	 */
	private int isTotalRecharge;
	/**
	 * ДаНет增加VIP经验
	 */
	private int totalVipPower;

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public int getGoods_system_cfg_id() {
		return goods_system_cfg_id;
	}

	public void setGoods_system_cfg_id(int goods_system_cfg_id) {
		this.goods_system_cfg_id = goods_system_cfg_id;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public String getGoods_pay_channel() {
		return goods_pay_channel;
	}

	public void setGoods_pay_channel(String goods_pay_channel) {
		this.goods_pay_channel = goods_pay_channel;
	}

	public int getGoods_pay_type() {
		return goods_pay_type;
	}

	public void setGoods_pay_type(int goods_pay_type) {
		this.goods_pay_type = goods_pay_type;
	}

	public int getGoods_type() {
		return goods_type;
	}

	public void setGoods_type(int goods_type) {
		this.goods_type = goods_type;
	}

	public int getGoods_subtype() {
		return goods_subtype;
	}

	public void setGoods_subtype(int goods_subtype) {
		this.goods_subtype = goods_subtype;
	}

	public int getGoods_limit() {
		return goods_limit;
	}

	public void setGoods_limit(int goods_limit) {
		this.goods_limit = goods_limit;
	}

	public int getGoods_icon() {
		return goods_icon;
	}

	public void setGoods_icon(int goods_icon) {
		this.goods_icon = goods_icon;
	}

	public String getGoodsurl() {
		return goodsurl;
	}

	public void setGoodsurl(String goodsurl) {
		this.goodsurl = goodsurl;
	}

	public TreeMap<String, TreeMap<String, String>> getGoods_price() {
		return goods_price;
	}

	public void setGoods_price(TreeMap<String, TreeMap<String, String>> goods_price) {
		this.goods_price = goods_price;
	}

	public TreeMap<String, String> getGoods_price_point() {
		return goods_price_point;
	}

	public void setGoods_price_point(TreeMap<String, String> goods_price_point) {
		this.goods_price_point = goods_price_point;
	}

	public String getGoods_show_price() {
		return goods_show_price;
	}

	public void setGoods_show_price(String goods_show_price) {
		this.goods_show_price = goods_show_price;
	}

	public String getGoods_reward() {
		return goods_reward;
	}

	public void setGoods_reward(String goods_reward) {
		this.goods_reward = goods_reward;
	}

	public String getGoods_multiple() {
		return goods_multiple;
	}

	public void setGoods_multiple(String goods_multiple) {
		this.goods_multiple = goods_multiple;
	}

	public String getGoods_extra_reward() {
		return goods_extra_reward;
	}

	public void setGoods_extra_reward(String goods_extra_reward) {
		this.goods_extra_reward = goods_extra_reward;
	}

	public int getGoods_extra_reward_limit() {
		return goods_extra_reward_limit;
	}

	public void setGoods_extra_reward_limit(int goods_extra_reward_limit) {
		this.goods_extra_reward_limit = goods_extra_reward_limit;
	}

	public String getGoods_ext() {
		return goods_ext;
	}

	public void setGoods_ext(String goods_ext) {
		this.goods_ext = goods_ext;
	}

	public int getIsTotalRecharge() {
		return isTotalRecharge;
	}

	public void setIsTotalRecharge(int isTotalRecharge) {
		this.isTotalRecharge = isTotalRecharge;
	}

	public int getTotalVipPower() {
		return totalVipPower;
	}

	public void setTotalVipPower(int totalVipPower) {
		this.totalVipPower = totalVipPower;
	}
}
