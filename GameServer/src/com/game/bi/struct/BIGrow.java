package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [成长日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIGrow {
	/**
	 * 成长目标id
	 */
	private String grow_target_id = "";
	/**
	 * 成长目标唯一ID
	 */
	private String grow_instance_id = "";
	/**
	 * 成长目标类型 1:道具 2:法宝 3:坐骑 4:翅膀等 提供相关【字典】
	 */
	private String grow_target_type = "";
	/**
	 * 成长目标子类型 详见【字典】
	 */
	private String grow_target_subtype = "";
	/**
	 * 成长操作类型 1激活2升级3培养4升阶5蜕变6进化 提供相关【字典】
	 */
	private String grow_act_type = "";
	/**
	 * 0:减少 1:增加
	 */
	private String change_type = "";
	/**
	 * 操作状态 1:成功 2:失败
	 */
	private String grow_status = "";
	/**
	 * 变更值
	 */
	private String change_value = "";
	/**
	 * 变更前值  例如：成长操作类型是升级，则记录等级数，如果是操作类型是进化，则记录成长目标id
	 */
	private String before_value = "";
	/**
	 * 变更后值 例如：成长操作类型是升级，则记录等级数，如果是操作类型是进化，则记录成长目标id
	 */
	private String after_value = "";
	/**
	 * 变更战力值
	 */
	private String change_combat = "";
	/**
	 * 变更前战力值
	 */
	private String before_combat = "";
	/**
	 * 变更后战力值
	 */
	private String after_combat = "";

	public BIGrow() {}

	public BIGrow(
			String grow_target_id,
			String grow_instance_id,
			String grow_target_type,
			String grow_target_subtype,
			String grow_act_type,
			String change_type,
			String grow_status,
			String change_value,
			String before_value,
			String after_value,
			String change_combat,
			String before_combat,
			String after_combat
	) {
		setGrow_target_id(grow_target_id);
		setGrow_instance_id(grow_instance_id);
		setGrow_target_type(grow_target_type);
		setGrow_target_subtype(grow_target_subtype);
		setGrow_act_type(grow_act_type);
		setChange_type(change_type);
		setGrow_status(grow_status);
		setChange_value(change_value);
		setBefore_value(before_value);
		setAfter_value(after_value);
		setChange_combat(change_combat);
		setBefore_combat(before_combat);
		setAfter_combat(after_combat);
	}

	public String getGrow_target_id() {
		return grow_target_id;
	}

	public void setGrow_target_id(String grow_target_id) {
		if (grow_target_id == null || grow_target_id.equals(""))
			this.grow_target_id = "";
		else
			this.grow_target_id = grow_target_id;
	}

	public String getGrow_instance_id() {
		return grow_instance_id;
	}

	public void setGrow_instance_id(String grow_instance_id) {
		if (grow_instance_id == null || grow_instance_id.equals(""))
			this.grow_instance_id = "";
		else
			this.grow_instance_id = grow_instance_id;
	}

	public String getGrow_target_type() {
		return grow_target_type;
	}

	public void setGrow_target_type(String grow_target_type) {
		if (grow_target_type == null || grow_target_type.equals(""))
			this.grow_target_type = "";
		else
			this.grow_target_type = grow_target_type;
	}

	public String getGrow_target_subtype() {
		return grow_target_subtype;
	}

	public void setGrow_target_subtype(String grow_target_subtype) {
		if (grow_target_subtype == null || grow_target_subtype.equals(""))
			this.grow_target_subtype = "";
		else
			this.grow_target_subtype = grow_target_subtype;
	}

	public String getGrow_act_type() {
		return grow_act_type;
	}

	public void setGrow_act_type(String grow_act_type) {
		if (grow_act_type == null || grow_act_type.equals(""))
			this.grow_act_type = "";
		else
			this.grow_act_type = grow_act_type;
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

	public String getGrow_status() {
		return grow_status;
	}

	public void setGrow_status(String grow_status) {
		if (grow_status == null || grow_status.equals(""))
			this.grow_status = "";
		else
			this.grow_status = grow_status;
	}

	public String getChange_value() {
		return change_value;
	}

	public void setChange_value(String change_value) {
		if (change_value == null || change_value.equals(""))
			this.change_value = "";
		else
			this.change_value = change_value;
	}

	public String getBefore_value() {
		return before_value;
	}

	public void setBefore_value(String before_value) {
		if (before_value == null || before_value.equals(""))
			this.before_value = "";
		else
			this.before_value = before_value;
	}

	public String getAfter_value() {
		return after_value;
	}

	public void setAfter_value(String after_value) {
		if (after_value == null || after_value.equals(""))
			this.after_value = "";
		else
			this.after_value = after_value;
	}

	public String getChange_combat() {
		return change_combat;
	}

	public void setChange_combat(String change_combat) {
		if (change_combat == null || change_combat.equals(""))
			this.change_combat = "";
		else
			this.change_combat = change_combat;
	}

	public String getBefore_combat() {
		return before_combat;
	}

	public void setBefore_combat(String before_combat) {
		if (before_combat == null || before_combat.equals(""))
			this.before_combat = "";
		else
			this.before_combat = before_combat;
	}

	public String getAfter_combat() {
		return after_combat;
	}

	public void setAfter_combat(String after_combat) {
		if (after_combat == null || after_combat.equals(""))
			this.after_combat = "";
		else
			this.after_combat = after_combat;
	}

}
