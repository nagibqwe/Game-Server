package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [诸界远征]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIWorld_expedition {
	/**
	 * 远征轮次唯一id
	 */
	private String expedition_id = "";
	/**
	 * 城市id
	 */
	private String city_id = "";
	/**
	 * 城市类型 1: 2:  
	 */
	private String city_type = "";
	/**
	 * 角色个人增加占领值
	 */
	private String add_score = "";
	/**
	 * 区服占领值
	 */
	private String server_score = "";
	/**
	 * 城市所属服务器id
	 */
	private String city_owner_id = "";

	public BIWorld_expedition() {}

	public BIWorld_expedition(
			String expedition_id,
			String city_id,
			String city_type,
			String add_score,
			String server_score,
			String city_owner_id
	) {
		setExpedition_id(expedition_id);
		setCity_id(city_id);
		setCity_type(city_type);
		setAdd_score(add_score);
		setServer_score(server_score);
		setCity_owner_id(city_owner_id);
	}

	public String getExpedition_id() {
		return expedition_id;
	}

	public void setExpedition_id(String expedition_id) {
		if (expedition_id == null || expedition_id.equals(""))
			this.expedition_id = "";
		else
			this.expedition_id = expedition_id;
	}

	public String getCity_id() {
		return city_id;
	}

	public void setCity_id(String city_id) {
		if (city_id == null || city_id.equals(""))
			this.city_id = "";
		else
			this.city_id = city_id;
	}

	public String getCity_type() {
		return city_type;
	}

	public void setCity_type(String city_type) {
		if (city_type == null || city_type.equals(""))
			this.city_type = "";
		else
			this.city_type = city_type;
	}

	public String getAdd_score() {
		return add_score;
	}

	public void setAdd_score(String add_score) {
		if (add_score == null || add_score.equals(""))
			this.add_score = "";
		else
			this.add_score = add_score;
	}

	public String getServer_score() {
		return server_score;
	}

	public void setServer_score(String server_score) {
		if (server_score == null || server_score.equals(""))
			this.server_score = "";
		else
			this.server_score = server_score;
	}

	public String getCity_owner_id() {
		return city_owner_id;
	}

	public void setCity_owner_id(String city_owner_id) {
		if (city_owner_id == null || city_owner_id.equals(""))
			this.city_owner_id = "";
		else
			this.city_owner_id = city_owner_id;
	}

}
