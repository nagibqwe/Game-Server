package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [称号/目标]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIRealm {
	/**
	 * 类型 1:称号 2:目标
	 */
	private String up_type = "";
	/**
	 * 操作类型 0:未达成 1:达成 2:领取 3:使用
	 */
	private String realm_act_type = "";
	/**
	 * 对应称号/目标id
	 */
	private String task_id = "";
	/**
	 * 完成进度 1/3 如类型为境界任务则记录完成进度，如类型为其他则为空
	 */
	private String progress = "";

	public BIRealm() {}

	public BIRealm(
			String up_type,
			String realm_act_type,
			String task_id,
			String progress
	) {
		setUp_type(up_type);
		setRealm_act_type(realm_act_type);
		setTask_id(task_id);
		setProgress(progress);
	}

	public String getUp_type() {
		return up_type;
	}

	public void setUp_type(String up_type) {
		if (up_type == null || up_type.equals(""))
			this.up_type = "";
		else
			this.up_type = up_type;
	}

	public String getRealm_act_type() {
		return realm_act_type;
	}

	public void setRealm_act_type(String realm_act_type) {
		if (realm_act_type == null || realm_act_type.equals(""))
			this.realm_act_type = "";
		else
			this.realm_act_type = realm_act_type;
	}

	public String getTask_id() {
		return task_id;
	}

	public void setTask_id(String task_id) {
		if (task_id == null || task_id.equals(""))
			this.task_id = "";
		else
			this.task_id = task_id;
	}

	public String getProgress() {
		return progress;
	}

	public void setProgress(String progress) {
		if (progress == null || progress.equals(""))
			this.progress = "";
		else
			this.progress = progress;
	}

}
