package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [签到]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BISign {
	/**
	 * 签到类型 1:福利签到(内置活动) 2:节日祝福(运营活动)
	 */
	private String sign_type = "";
	/**
	 * 签到奖励类型 0:无奖励 1:签到当日奖励 2:累计签到多次奖励 3:全服签到次数奖励
	 */
	private String sign_reward_type = "";
	/**
	 * 签到奖励相关日志的唯一id，无奖励时可为空，有奖励时，可与物品流水日志中的op_target_value关联
	 */
	private String sign_reward_id = "";
	/**
	 * 累计连续签到天数，跨月不清零
	 */
	private String sign_continue_day = "";
	/**
	 * 当该签到参与全服累计活动时的活动id
	 */
	private String sign_act_id = "";
	/**
	 * 全服活动统计时的该签到的计算统计天数
	 */
	private String sign_act_day = "";

	public BISign() {}

	public BISign(
			String sign_type,
			String sign_reward_type,
			String sign_reward_id,
			String sign_continue_day,
			String sign_act_id,
			String sign_act_day
	) {
		setSign_type(sign_type);
		setSign_reward_type(sign_reward_type);
		setSign_reward_id(sign_reward_id);
		setSign_continue_day(sign_continue_day);
		setSign_act_id(sign_act_id);
		setSign_act_day(sign_act_day);
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		if (sign_type == null || sign_type.equals(""))
			this.sign_type = "";
		else
			this.sign_type = sign_type;
	}

	public String getSign_reward_type() {
		return sign_reward_type;
	}

	public void setSign_reward_type(String sign_reward_type) {
		if (sign_reward_type == null || sign_reward_type.equals(""))
			this.sign_reward_type = "";
		else
			this.sign_reward_type = sign_reward_type;
	}

	public String getSign_reward_id() {
		return sign_reward_id;
	}

	public void setSign_reward_id(String sign_reward_id) {
		if (sign_reward_id == null || sign_reward_id.equals(""))
			this.sign_reward_id = "";
		else
			this.sign_reward_id = sign_reward_id;
	}

	public String getSign_continue_day() {
		return sign_continue_day;
	}

	public void setSign_continue_day(String sign_continue_day) {
		if (sign_continue_day == null || sign_continue_day.equals(""))
			this.sign_continue_day = "";
		else
			this.sign_continue_day = sign_continue_day;
	}

	public String getSign_act_id() {
		return sign_act_id;
	}

	public void setSign_act_id(String sign_act_id) {
		if (sign_act_id == null || sign_act_id.equals(""))
			this.sign_act_id = "";
		else
			this.sign_act_id = sign_act_id;
	}

	public String getSign_act_day() {
		return sign_act_day;
	}

	public void setSign_act_day(String sign_act_day) {
		if (sign_act_day == null || sign_act_day.equals(""))
			this.sign_act_day = "";
		else
			this.sign_act_day = sign_act_day;
	}

}
