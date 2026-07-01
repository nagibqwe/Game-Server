package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [活动日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIActivity {
	/**
	 * 活动组id(一级活动)
	 */
	private String activity_group_id = "";
	/**
	 * 活动组名称(一级活动)
	 */
	private String activity_group_name = "";
	/**
	 * 活动id(二级活动)
	 */
	private String activity_id = "";
	/**
	 * 活动id(二级活动)
	 */
	private String activity_name = "";
	/**
	 * 活动类型 1:日常活动 2:商城活动 3:福利活动 4:抽奖活动 5:跨服活动 6:签到
	 */
	private String activity_type = "";
	/**
	 * 奖励组id(一级奖励)
	 */
	private String reward_group_id = "";
	/**
	 * 奖励组名称(一级奖励)
	 */
	private String reward_group_name = "";
	/**
	 * 奖励id(二级奖励)
	 */
	private String reward_id = "";
	/**
	 * 奖励名称(二级奖励)
	 */
	private String reward_name = "";
	/**
	 * 活动状态 1:活动接取 2:完成活动 3:放弃活动 4:完成并领取奖励
	 */
	private String activity_status = "";
	/**
	 * 抽奖活动时为轮次数，划拳活动时为次数
	 */
	private String activity_round = "";
	/**
	 * 祈灵玩法抽奖时的祈灵等级
	 */
	private String reward_level = "";

	public BIActivity() {}

	public BIActivity(
			String activity_group_id,
			String activity_group_name,
			String activity_id,
			String activity_name,
			String activity_type,
			String reward_group_id,
			String reward_group_name,
			String reward_id,
			String reward_name,
			String activity_status,
			String activity_round,
			String reward_level
	) {
		setActivity_group_id(activity_group_id);
		setActivity_group_name(activity_group_name);
		setActivity_id(activity_id);
		setActivity_name(activity_name);
		setActivity_type(activity_type);
		setReward_group_id(reward_group_id);
		setReward_group_name(reward_group_name);
		setReward_id(reward_id);
		setReward_name(reward_name);
		setActivity_status(activity_status);
		setActivity_round(activity_round);
		setReward_level(reward_level);
	}

	public String getActivity_group_id() {
		return activity_group_id;
	}

	public void setActivity_group_id(String activity_group_id) {
		if (activity_group_id == null || activity_group_id.equals(""))
			this.activity_group_id = "";
		else
			this.activity_group_id = activity_group_id;
	}

	public String getActivity_group_name() {
		return activity_group_name;
	}

	public void setActivity_group_name(String activity_group_name) {
		if (activity_group_name == null || activity_group_name.equals(""))
			this.activity_group_name = "";
		else
			this.activity_group_name = activity_group_name;
	}

	public String getActivity_id() {
		return activity_id;
	}

	public void setActivity_id(String activity_id) {
		if (activity_id == null || activity_id.equals(""))
			this.activity_id = "";
		else
			this.activity_id = activity_id;
	}

	public String getActivity_name() {
		return activity_name;
	}

	public void setActivity_name(String activity_name) {
		if (activity_name == null || activity_name.equals(""))
			this.activity_name = "";
		else
			this.activity_name = activity_name;
	}

	public String getActivity_type() {
		return activity_type;
	}

	public void setActivity_type(String activity_type) {
		if (activity_type == null || activity_type.equals(""))
			this.activity_type = "";
		else
			this.activity_type = activity_type;
	}

	public String getReward_group_id() {
		return reward_group_id;
	}

	public void setReward_group_id(String reward_group_id) {
		if (reward_group_id == null || reward_group_id.equals(""))
			this.reward_group_id = "";
		else
			this.reward_group_id = reward_group_id;
	}

	public String getReward_group_name() {
		return reward_group_name;
	}

	public void setReward_group_name(String reward_group_name) {
		if (reward_group_name == null || reward_group_name.equals(""))
			this.reward_group_name = "";
		else
			this.reward_group_name = reward_group_name;
	}

	public String getReward_id() {
		return reward_id;
	}

	public void setReward_id(String reward_id) {
		if (reward_id == null || reward_id.equals(""))
			this.reward_id = "";
		else
			this.reward_id = reward_id;
	}

	public String getReward_name() {
		return reward_name;
	}

	public void setReward_name(String reward_name) {
		if (reward_name == null || reward_name.equals(""))
			this.reward_name = "";
		else
			this.reward_name = reward_name;
	}

	public String getActivity_status() {
		return activity_status;
	}

	public void setActivity_status(String activity_status) {
		if (activity_status == null || activity_status.equals(""))
			this.activity_status = "";
		else
			this.activity_status = activity_status;
	}

	public String getActivity_round() {
		return activity_round;
	}

	public void setActivity_round(String activity_round) {
		if (activity_round == null || activity_round.equals(""))
			this.activity_round = "";
		else
			this.activity_round = activity_round;
	}

	public String getReward_level() {
		return reward_level;
	}

	public void setReward_level(String reward_level) {
		if (reward_level == null || reward_level.equals(""))
			this.reward_level = "";
		else
			this.reward_level = reward_level;
	}

}
