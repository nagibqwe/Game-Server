package com.backend.struct;

public class ActiveCode {
	private int id;
	/**
	 * 激活码
	 */
	private String code;
	/**
	 * 激活码类型名
	 */
	private String activeName;
	/**
	 * 批次号
	 */
	private String batch;
	/**
	 * 物品列表
	 */
	private String itemList;
	/**
	 * 参数（默认=0）,1表示万能码
	 */
	private int param;
	/**
	 * 有效开始时间（NULL表示无）
	 */
	private String valide_time_begin;
	/**
	 * 有效结束时间（NULL表示无）
	 */
	private String valide_time_end;
	/**
	 * 平台名（大）
	 */
	private String plateform_name_big;
	/**
	 * 平台名（小）
	 */
	private String plateform_name_small;
	/**
	 * 仅某些服有效（[]表示通用），例如：2、3、4服=[2,3,4]
	 */
	private String valide_server_id_list;
	/**
	 * 创建时间
	 */
	private int create_time;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getActiveName() {
		return activeName;
	}

	public void setActiveName(String activeName) {
		this.activeName = activeName;
	}

	public String getBatch() {
		return batch;
	}

	public void setBatch(String batch) {
		this.batch = batch;
	}

	public String getItemList() {
		return itemList;
	}

	public void setItemList(String itemList) {
		this.itemList = itemList;
	}

	public int getParam() {
		return param;
	}

	public void setParam(int param) {
		this.param = param;
	}

	public String getValide_time_begin() {
		return valide_time_begin;
	}

	public void setValide_time_begin(String valide_time_begin) {
		this.valide_time_begin = valide_time_begin;
	}

	public String getValide_time_end() {
		return valide_time_end;
	}

	public void setValide_time_end(String valide_time_end) {
		this.valide_time_end = valide_time_end;
	}

	public String getPlateform_name_big() {
		return plateform_name_big;
	}

	public void setPlateform_name_big(String plateform_name_big) {
		this.plateform_name_big = plateform_name_big;
	}

	public String getPlateform_name_small() {
		return plateform_name_small;
	}

	public void setPlateform_name_small(String plateform_name_small) {
		this.plateform_name_small = plateform_name_small;
	}

	public String getValide_server_id_list() {
		return valide_server_id_list;
	}

	public void setValide_server_id_list(String valide_server_id_list) {
		this.valide_server_id_list = valide_server_id_list;
	}

	public int getCreate_time() {
		return create_time;
	}

	public void setCreate_time(int create_time) {
		this.create_time = create_time;
	}
}
