package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [物品流水]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIItem {
	/**
	 * 物品id
	 */
	private String item_id = "";
	/**
	 * 物品唯一id
	 */
	private String item_instance_id = "";
	/**
	 * 物品名称
	 */
	private String item_name = "";
	/**
	 * 物品类型
	 */
	private String item_type = "";
	/**
	 * 0:减少 1:增加
	 */
	private String change_type = "";
	/**
	 * 变更数量
	 */
	private String item_num = "";
	/**
	 * 变更前的数量
	 */
	private String before_num = "";
	/**
	 * 变更后的数量
	 */
	private String after_num = "";
	/**
	 * 物品变化途径枚举类型，由字典表管理翻译内容，物品增加的获得点；例：道具获得->任务获得道具 提供相关【字典】
	 */
	private String item_op_type = "";
	/**
	 * 获取物品途径的类型 如 1:任务 2:商城购买;任务获得道具时，该值为1 提供相关【字典】
	 */
	private String item_op_target_type = "";
	/**
	 * 获取物品途径对象的ID 例：任务获得道具时，该值设为对应的任务索引ID 提供相关【字典】
	 */
	private String item_op_target_value = "";
	/**
	 * 获得或者消耗对象的类型的数量,如果没有则设为空
	 */
	private String item_op_target_count = "";
	/**
	 * 获得或者消耗的对象属性，例如等级/品阶等
	 */
	private String item_op_target_attr = "";

	public BIItem() {}

	public BIItem(
			String item_id,
			String item_instance_id,
			String item_name,
			String item_type,
			String change_type,
			String item_num,
			String before_num,
			String after_num,
			String item_op_type,
			String item_op_target_type,
			String item_op_target_value,
			String item_op_target_count,
			String item_op_target_attr
	) {
		setItem_id(item_id);
		setItem_instance_id(item_instance_id);
		setItem_name(item_name);
		setItem_type(item_type);
		setChange_type(change_type);
		setItem_num(item_num);
		setBefore_num(before_num);
		setAfter_num(after_num);
		setItem_op_type(item_op_type);
		setItem_op_target_type(item_op_target_type);
		setItem_op_target_value(item_op_target_value);
		setItem_op_target_count(item_op_target_count);
		setItem_op_target_attr(item_op_target_attr);
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

	public String getItem_instance_id() {
		return item_instance_id;
	}

	public void setItem_instance_id(String item_instance_id) {
		if (item_instance_id == null || item_instance_id.equals(""))
			this.item_instance_id = "";
		else
			this.item_instance_id = item_instance_id;
	}

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		if (item_name == null || item_name.equals(""))
			this.item_name = "";
		else
			this.item_name = item_name;
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

	public String getChange_type() {
		return change_type;
	}

	public void setChange_type(String change_type) {
		if (change_type == null || change_type.equals(""))
			this.change_type = "";
		else
			this.change_type = change_type;
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

	public String getBefore_num() {
		return before_num;
	}

	public void setBefore_num(String before_num) {
		if (before_num == null || before_num.equals(""))
			this.before_num = "";
		else
			this.before_num = before_num;
	}

	public String getAfter_num() {
		return after_num;
	}

	public void setAfter_num(String after_num) {
		if (after_num == null || after_num.equals(""))
			this.after_num = "";
		else
			this.after_num = after_num;
	}

	public String getItem_op_type() {
		return item_op_type;
	}

	public void setItem_op_type(String item_op_type) {
		if (item_op_type == null || item_op_type.equals(""))
			this.item_op_type = "";
		else
			this.item_op_type = item_op_type;
	}

	public String getItem_op_target_type() {
		return item_op_target_type;
	}

	public void setItem_op_target_type(String item_op_target_type) {
		if (item_op_target_type == null || item_op_target_type.equals(""))
			this.item_op_target_type = "";
		else
			this.item_op_target_type = item_op_target_type;
	}

	public String getItem_op_target_value() {
		return item_op_target_value;
	}

	public void setItem_op_target_value(String item_op_target_value) {
		if (item_op_target_value == null || item_op_target_value.equals(""))
			this.item_op_target_value = "";
		else
			this.item_op_target_value = item_op_target_value;
	}

	public String getItem_op_target_count() {
		return item_op_target_count;
	}

	public void setItem_op_target_count(String item_op_target_count) {
		if (item_op_target_count == null || item_op_target_count.equals(""))
			this.item_op_target_count = "";
		else
			this.item_op_target_count = item_op_target_count;
	}

	public String getItem_op_target_attr() {
		return item_op_target_attr;
	}

	public void setItem_op_target_attr(String item_op_target_attr) {
		if (item_op_target_attr == null || item_op_target_attr.equals(""))
			this.item_op_target_attr = "";
		else
			this.item_op_target_attr = item_op_target_attr;
	}

}
