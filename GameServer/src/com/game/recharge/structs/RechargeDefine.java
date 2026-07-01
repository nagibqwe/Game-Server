package com.game.recharge.structs;

/**
 * @explain: desc
 * @time Created on 2019/11/21 11:43.
 * @author: tc
 */
public class RechargeDefine {
	/**
	 * 浏览器图标
	 */
	public static final String FAVICON = "/favicon.ico";

	/**
	 * 新订单
	 */
	public static final Byte STA_NEW = 0;
	/**
	 * 已发货
	 */
	public static final Byte STA_COMPLETE = 1;
	/**
	 * 订单异常
	 */
	public static final Byte STA_ERROR = 2;

	// 订单状态原因
	public static final int STA_REASON_None = 0;
	/**
	 * 玩家不存在
	 */
	public static final int STA_REASON_PlayerNotExist = 1;
	/**
	 * 订单重复
	 */
	public static final int STA_REASON_Repeated = 2;
	/**
	 * 发货失败，回滚状态
	 */
	public static final int STA_REASON_Rollback = 3;
	/**
	 * 充值成功，但不允许发奖
	 */
	public static final int STA_REASON_NoReward = 4;
	/**
	 * 普通正常充值成功
	 */
	public static final int STA_REASON_RewardNormal = 5;
	/**
	 * 充值成功，但没有充值配置
	 */
	public static final int STA_REASON_RewardNoConfig = 6;
	/**
	 * 充值流程走完
	 */
	public static final int STA_REASON_Complete = 7;
	/**
	 * 发奖过程中异常
	 */
	public static final int STA_REASON_RewardExcept = 8;



	/**
	 * 正常订单
	 */
	public static final Byte SRC_NORMAL = 0;
	/**
	 * 后台
	 */
	public static final Byte SRC_BACKEND = 1;
	/**
	 * GM指令
	 */
	public static final Byte SRC_GM = 2;
	/**
	 * 免费礼包
	 */
	public static final Byte SRC_INTERNAL = 3;



	/**
	 * 普通充值类型
	 */
	public static final int RECHARGE_TYPE_NORMAL = 1;
	/**
	 * 每日礼包充值
	 */
	public static final int RECHARGE_TYPE_DAY_GIFT = 2;
	/**
	 * 周卡充值
	 */
	public static final int RECHARGE_TYPE_WEEK_CARD = 3;
	/**
	 * 月卡充值
	 */
	public static final int RECHARGE_TYPE_MONTH_CARD = 4;
	/**
	 * 尊享卡充值
	 */
	public static final int RECHARGE_TYPE_EXCLUSIVE_CARD = 5;
	/**
	 * 成长基金
	 */
	public static final int RECHARGE_TYPE_GROWTH_FUND = 6;

	/**
	 * 神秘商店
	 */
	public static final int RECHARGE_TYPE_MYSTERY_SHOP = 7;

	/**
	 * 零元够
	 */
	public static final int RECHARGE_TYPE_FREE_SHOP = 8;
	/**
	 * 超值折扣
	 */
	public static final int RECHARGE_TYPE_DISCOUNT = 9;

	/**
	 * 狂欢周礼包
	 */
	public static final int RECHARGE_TYPE_CRAZYSAT = 10;

	/**
	 * 运营活动礼包
	 */
	public static final int RECHARGE_TYPE_ACTIVITY = 11;

	/**
	 * 天禁令高级令牌购买
	 */
	public static final int FALLSKY_TYPE_ACTIVITY = 12;

	/**
	 * 巅峰基金
	 */
	public static final int RECHARGE_TYPE_INVESTPEAK = 13;

	/**
	 * 福利每日礼包一键购买
	 */
	public static final int RECHARGE_TYPE_DAY_ONEKEY_GIFT  = 14;

	/**
	 * 核心功能任务
	 */
	public static final int RECHARGE_TYPE_FUNCTION_TASK  = 15;


	/**
	 * 第三方支付特殊处理
	 */
	public static final int RECHARGE_TYPE_THIRDPAY = 99;


	/**
	 * 普通充值 -- 充值次数不做处理
	 */
	public static final int RECHARGE_REFRESH_TYPE_NORMAL = 1;
	/**
	 * 新手充值 -- 一生只有一次
	 */
	public static final int RECHARGE_REFRESH_TYPE_NEWBIE = 2;
	/**
	 * 周充值 -- 一周只有一次
	 */
	public static final int RECHARGE_REFRESH_TYPE_WEEK = 3;
	/**
	 * 日充值 -- 一天只有一次
	 */
	public static final int RECHARGE_REFRESH_TYPE_DAY = 4;
}
