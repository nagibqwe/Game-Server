package com.game.bi.struct;

/**
 * @explain: this file is auto generate!! [装备变化]
 * @time Created on 2020-01-18 15:58:58
 * @author: tc
 */
public class BIEquip {
	/**
	 * 装备id
	 */
	private String item_id = "";
	/**
	 * 装备名称
	 */
	private String item_name = "";
	/**
	 * 装备类型 1:角色装备 2:仙甲 3:圣装 4:神兽 5:灵体3阶~18:灵体16阶 19:第二套仙甲
	 */
	private String equip_type = "";
	/**
	 * 部位
	 */
	private String part = "";
	/**
	 * 操作类型 1:穿戴 2:合成 3:强化 4:洗练 5:宝石镶嵌 6:宝石升级 7:套装激活 8:宝石精炼
	 */
	private String equip_act_type = "";
	/**
	 * 操作后星级
	 */
	private String star = "";
	/**
	 * 操作后等阶
	 */
	private String level = "";
	/**
	 * 操作后颜色
	 */
	private String color = "";
	/**
	 * 操作后强化等级
	 */
	private String str_level = "";
	/**
	 * 是否绑定
	 */
	private String bind = "";
	/**
	 * 套装类型 1:传世 2:洞虚 3:破天
	 */
	private String suit = "";
	/**
	 * 宝石ID
	 */
	private String gem_id = "";
	/**
	 * 宝石孔位
	 */
	private String gem_num = "";
	/**
	 * 宝石位置
	 */
	private String gem_pos = "";
	/**
	 * 装备宝石数量
	 */
	private String gem_set = "";
	/**
	 * 宝石等级
	 */
	private String gem_rating = "";
	/**
	 * 在装备发生变化时锁定属性的数量
	 */
	private String lock_num = "";
	/**
	 * 用于锁定属性的消耗字段类型 0:无锁定 1:材料锁定 2:货币锁定
	 */
	private String lock_type = "";
	/**
	 * 变更总评分值
	 */
	private String change_score = "";
	/**
	 * 变更前总评分值
	 */
	private String before_score = "";
	/**
	 * 变更后总评分值
	 */
	private String after_score = "";
	/**
	 * 精炼等级
	 */
	private String refine_level = "";

	public BIEquip() {}

	public BIEquip(
			String item_id,
			String item_name,
			String equip_type,
			String part,
			String equip_act_type,
			String star,
			String level,
			String color,
			String str_level,
			String bind,
			String suit,
			String gem_id,
			String gem_num,
			String gem_pos,
			String gem_set,
			String gem_rating,
			String lock_num,
			String lock_type,
			String change_score,
			String before_score,
			String after_score,
			String refine_level
	) {
		setItem_id(item_id);
		setItem_name(item_name);
		setEquip_type(equip_type);
		setPart(part);
		setEquip_act_type(equip_act_type);
		setStar(star);
		setLevel(level);
		setColor(color);
		setStr_level(str_level);
		setBind(bind);
		setSuit(suit);
		setGem_id(gem_id);
		setGem_num(gem_num);
		setGem_pos(gem_pos);
		setGem_set(gem_set);
		setGem_rating(gem_rating);
		setLock_num(lock_num);
		setLock_type(lock_type);
		setChange_score(change_score);
		setBefore_score(before_score);
		setAfter_score(after_score);
		setRefine_level(refine_level);
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

	public String getItem_name() {
		return item_name;
	}

	public void setItem_name(String item_name) {
		if (item_name == null || item_name.equals(""))
			this.item_name = "";
		else
			this.item_name = item_name;
	}

	public String getEquip_type() {
		return equip_type;
	}

	public void setEquip_type(String equip_type) {
		if (equip_type == null || equip_type.equals(""))
			this.equip_type = "";
		else
			this.equip_type = equip_type;
	}

	public String getPart() {
		return part;
	}

	public void setPart(String part) {
		if (part == null || part.equals(""))
			this.part = "";
		else
			this.part = part;
	}

	public String getEquip_act_type() {
		return equip_act_type;
	}

	public void setEquip_act_type(String equip_act_type) {
		if (equip_act_type == null || equip_act_type.equals(""))
			this.equip_act_type = "";
		else
			this.equip_act_type = equip_act_type;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		if (star == null || star.equals(""))
			this.star = "";
		else
			this.star = star;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		if (level == null || level.equals(""))
			this.level = "";
		else
			this.level = level;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if (color == null || color.equals(""))
			this.color = "";
		else
			this.color = color;
	}

	public String getStr_level() {
		return str_level;
	}

	public void setStr_level(String str_level) {
		if (str_level == null || str_level.equals(""))
			this.str_level = "";
		else
			this.str_level = str_level;
	}

	public String getBind() {
		return bind;
	}

	public void setBind(String bind) {
		if (bind == null || bind.equals(""))
			this.bind = "";
		else
			this.bind = bind;
	}

	public String getSuit() {
		return suit;
	}

	public void setSuit(String suit) {
		if (suit == null || suit.equals(""))
			this.suit = "";
		else
			this.suit = suit;
	}

	public String getGem_id() {
		return gem_id;
	}

	public void setGem_id(String gem_id) {
		if (gem_id == null || gem_id.equals(""))
			this.gem_id = "";
		else
			this.gem_id = gem_id;
	}

	public String getGem_num() {
		return gem_num;
	}

	public void setGem_num(String gem_num) {
		if (gem_num == null || gem_num.equals(""))
			this.gem_num = "";
		else
			this.gem_num = gem_num;
	}

	public String getGem_pos() {
		return gem_pos;
	}

	public void setGem_pos(String gem_pos) {
		if (gem_pos == null || gem_pos.equals(""))
			this.gem_pos = "";
		else
			this.gem_pos = gem_pos;
	}

	public String getGem_set() {
		return gem_set;
	}

	public void setGem_set(String gem_set) {
		if (gem_set == null || gem_set.equals(""))
			this.gem_set = "";
		else
			this.gem_set = gem_set;
	}

	public String getGem_rating() {
		return gem_rating;
	}

	public void setGem_rating(String gem_rating) {
		if (gem_rating == null || gem_rating.equals(""))
			this.gem_rating = "";
		else
			this.gem_rating = gem_rating;
	}

	public String getLock_num() {
		return lock_num;
	}

	public void setLock_num(String lock_num) {
		if (lock_num == null || lock_num.equals(""))
			this.lock_num = "";
		else
			this.lock_num = lock_num;
	}

	public String getLock_type() {
		return lock_type;
	}

	public void setLock_type(String lock_type) {
		if (lock_type == null || lock_type.equals(""))
			this.lock_type = "";
		else
			this.lock_type = lock_type;
	}

	public String getChange_score() {
		return change_score;
	}

	public void setChange_score(String change_score) {
		if (change_score == null || change_score.equals(""))
			this.change_score = "";
		else
			this.change_score = change_score;
	}

	public String getBefore_score() {
		return before_score;
	}

	public void setBefore_score(String before_score) {
		if (before_score == null || before_score.equals(""))
			this.before_score = "";
		else
			this.before_score = before_score;
	}

	public String getAfter_score() {
		return after_score;
	}

	public void setAfter_score(String after_score) {
		if (after_score == null || after_score.equals(""))
			this.after_score = "";
		else
			this.after_score = after_score;
	}

	public String getRefine_level() {
		return refine_level;
	}

	public void setRefine_level(String refine_level) {
		if (refine_level == null || refine_level.equals(""))
			this.refine_level = "";
		else
			this.refine_level = refine_level;
	}

}
