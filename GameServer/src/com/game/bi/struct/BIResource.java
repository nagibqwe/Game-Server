package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [资源流水]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIResource {
	/**
	 * 资源类型说明，1-经验,2-荣耀点等资源 提供相关【字典】
	 */
	private String resource_type = "";
	/**
	 * 0减少1增加
	 */
	private String change_type = "";
	/**
	 * 变化数量
	 */
	private String resource_num = "";
	/**
	 * 变化前的资源数量
	 */
	private String before_resource = "";
	/**
	 * 变化后的资源数量
	 */
	private String after_resource = "";
	/**
	 * 资源产出/消耗途径分类，枚举类型，由字典表管理翻译内容，资源增加的获得点；例：经验获得->任务获得经验 提供相关【字典】
	 */
	private String resource_op_type = "";
	/**
	 * 资源获得或者消耗的对象类型 如 1:道具 2:任务;任务获得经验时，该值为2 提供相关【字典】 
	 */
	private String resource_op_target_type = "";
	/**
	 * 资源变化途径对象的ID 例：任务获得资源时，该值设为对应的任务索引ID 提供相关【字典】
	 */
	private String resource_op_target_value = "";
	/**
	 * 资源获得或者消耗对象的类型的数量,如果没有则设为空
	 */
	private String resource_op_target_count = "";
	/**
	 * 资源获得或者消耗对象的属性，例如等级/品阶等
	 */
	private String resource_op_target_attr = "";

	public BIResource() {}

	public BIResource(
			String resource_type,
			String change_type,
			String resource_num,
			String before_resource,
			String after_resource,
			String resource_op_type,
			String resource_op_target_type,
			String resource_op_target_value,
			String resource_op_target_count,
			String resource_op_target_attr
	) {
		setResource_type(resource_type);
		setChange_type(change_type);
		setResource_num(resource_num);
		setBefore_resource(before_resource);
		setAfter_resource(after_resource);
		setResource_op_type(resource_op_type);
		setResource_op_target_type(resource_op_target_type);
		setResource_op_target_value(resource_op_target_value);
		setResource_op_target_count(resource_op_target_count);
		setResource_op_target_attr(resource_op_target_attr);
	}

	public String getResource_type() {
		return resource_type;
	}

	public void setResource_type(String resource_type) {
		if (resource_type == null || resource_type.equals(""))
			this.resource_type = "";
		else
			this.resource_type = resource_type;
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

	public String getResource_num() {
		return resource_num;
	}

	public void setResource_num(String resource_num) {
		if (resource_num == null || resource_num.equals(""))
			this.resource_num = "";
		else
			this.resource_num = resource_num;
	}

	public String getBefore_resource() {
		return before_resource;
	}

	public void setBefore_resource(String before_resource) {
		if (before_resource == null || before_resource.equals(""))
			this.before_resource = "";
		else
			this.before_resource = before_resource;
	}

	public String getAfter_resource() {
		return after_resource;
	}

	public void setAfter_resource(String after_resource) {
		if (after_resource == null || after_resource.equals(""))
			this.after_resource = "";
		else
			this.after_resource = after_resource;
	}

	public String getResource_op_type() {
		return resource_op_type;
	}

	public void setResource_op_type(String resource_op_type) {
		if (resource_op_type == null || resource_op_type.equals(""))
			this.resource_op_type = "";
		else
			this.resource_op_type = resource_op_type;
	}

	public String getResource_op_target_type() {
		return resource_op_target_type;
	}

	public void setResource_op_target_type(String resource_op_target_type) {
		if (resource_op_target_type == null || resource_op_target_type.equals(""))
			this.resource_op_target_type = "";
		else
			this.resource_op_target_type = resource_op_target_type;
	}

	public String getResource_op_target_value() {
		return resource_op_target_value;
	}

	public void setResource_op_target_value(String resource_op_target_value) {
		if (resource_op_target_value == null || resource_op_target_value.equals(""))
			this.resource_op_target_value = "";
		else
			this.resource_op_target_value = resource_op_target_value;
	}

	public String getResource_op_target_count() {
		return resource_op_target_count;
	}

	public void setResource_op_target_count(String resource_op_target_count) {
		if (resource_op_target_count == null || resource_op_target_count.equals(""))
			this.resource_op_target_count = "";
		else
			this.resource_op_target_count = resource_op_target_count;
	}

	public String getResource_op_target_attr() {
		return resource_op_target_attr;
	}

	public void setResource_op_target_attr(String resource_op_target_attr) {
		if (resource_op_target_attr == null || resource_op_target_attr.equals(""))
			this.resource_op_target_attr = "";
		else
			this.resource_op_target_attr = resource_op_target_attr;
	}

}
