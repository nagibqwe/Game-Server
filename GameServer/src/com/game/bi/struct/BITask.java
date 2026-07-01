package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [任务日志]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BITask {
	/**
	 * 任务ID
	 */
	private String task_id = "";
	/**
	 * 任务名字
	 */
	private String task_name = "";
	/**
	 * 任务类型 1:主线 2:支线等,提供相关【字典】
	 */
	private String task_type = "";
	/**
	 * 操作类型 1:接收任务 2:完成任务 3:放弃任务 4:完成并领取奖励
	 */
	private String task_status = "";
	/**
	 * 完成任务时记录该任务消耗时间(单位:秒)
	 */
	private String task_used_time = "";
	/**
	 * 任务子类型
	 */
	private String task_subtype = "";
	/**
	 * 任务难度等级 S A B C D
	 */
	private String task_level = "";

	public BITask() {}

	public BITask(
			String task_id,
			String task_name,
			String task_type,
			String task_status,
			String task_used_time,
			String task_subtype,
			String task_level
	) {
		setTask_id(task_id);
		setTask_name(task_name);
		setTask_type(task_type);
		setTask_status(task_status);
		setTask_used_time(task_used_time);
		setTask_subtype(task_subtype);
		setTask_level(task_level);
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

	public String getTask_name() {
		return task_name;
	}

	public void setTask_name(String task_name) {
		if (task_name == null || task_name.equals(""))
			this.task_name = "";
		else
			this.task_name = task_name;
	}

	public String getTask_type() {
		return task_type;
	}

	public void setTask_type(String task_type) {
		if (task_type == null || task_type.equals(""))
			this.task_type = "";
		else
			this.task_type = task_type;
	}

	public String getTask_status() {
		return task_status;
	}

	public void setTask_status(String task_status) {
		if (task_status == null || task_status.equals(""))
			this.task_status = "";
		else
			this.task_status = task_status;
	}

	public String getTask_used_time() {
		return task_used_time;
	}

	public void setTask_used_time(String task_used_time) {
		if (task_used_time == null || task_used_time.equals(""))
			this.task_used_time = "";
		else
			this.task_used_time = task_used_time;
	}

	public String getTask_subtype() {
		return task_subtype;
	}

	public void setTask_subtype(String task_subtype) {
		if (task_subtype == null || task_subtype.equals(""))
			this.task_subtype = "";
		else
			this.task_subtype = task_subtype;
	}

	public String getTask_level() {
		return task_level;
	}

	public void setTask_level(String task_level) {
		if (task_level == null || task_level.equals(""))
			this.task_level = "";
		else
			this.task_level = task_level;
	}

}
