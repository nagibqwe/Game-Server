package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内获得/消耗货币时的信息。
 * @time Created on 2020/3/26 17:04.
 * @author: tc
 */
public class tbllog_gold extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 平台账号ID
	private String account_name;
	// 等级
	private int dim_level;
	// 职业ID
	private int dim_prof;
	// 货币类型（1=钻石，2=绑定钻石，3=金币，4=绑定金币，5=礼券，6=积分/荣誉, 7=兑换）
	private int money_type;
	// 货币数量
	private int amount;
	// 剩余货币数量（行为完成后）
	private int money_remain;
	// 涉及的道具ID
	private int item_id;
	// 货币加减 （1=增加，-1=减少）
	private int opt;
	// 行为分类1 （一级消费点）对应(dict_action.action_id)
	private int action_1;
	// 若存在一级消费点,不存在二级消费点,则将二级消费点设置为一级消费点的值
	private int action_2;
	// 物品数量
	private int item_number;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_gold() {}

	public tbllog_gold(String platform, String device, long role_id, String account_name, int dim_level, int dim_prof, int money_type, int amount, int money_remain, int item_id, int opt, int action_1, int action_2, int item_number, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.dim_level = dim_level;
		this.dim_prof = dim_prof;
		this.money_type = money_type;
		this.amount = amount;
		this.money_remain = money_remain;
		this.item_id = item_id;
		this.opt = opt;
		this.action_1 = action_1;
		this.action_2 = action_2;
		this.item_number = item_number;
		this.happend_time = happend_time;
	}

	@Log(logField = "platform", fieldType = "varchar(100)", index = "0")
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	@Log(logField = "device", fieldType = "varchar(100)", index = "0")
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	@Log(logField = "role_id", fieldType = "bigint", index = "0")
	public long getRole_id() {
		return role_id;
	}

	public void setRole_id(long role_id) {
		this.role_id = role_id;
	}

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Log(logField = "dim_level", fieldType = "int", index = "0")
	public int getDim_level() {
		return dim_level;
	}

	public void setDim_level(int dim_level) {
		this.dim_level = dim_level;
	}

	@Log(logField = "dim_prof", fieldType = "int", index = "0")
	public int getDim_prof() {
		return dim_prof;
	}

	public void setDim_prof(int dim_prof) {
		this.dim_prof = dim_prof;
	}

	@Log(logField = "money_type", fieldType = "int", index = "0")
	public int getMoney_type() {
		return money_type;
	}

	public void setMoney_type(int money_type) {
		this.money_type = money_type;
	}

	@Log(logField = "amount", fieldType = "int", index = "0")
	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	@Log(logField = "money_remain", fieldType = "int", index = "0")
	public int getMoney_remain() {
		return money_remain;
	}

	public void setMoney_remain(int money_remain) {
		this.money_remain = money_remain;
	}

	@Log(logField = "item_id", fieldType = "int", index = "0")
	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	@Log(logField = "opt", fieldType = "int", index = "0")
	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	@Log(logField = "action_1", fieldType = "int", index = "0")
	public int getAction_1() {
		return action_1;
	}

	public void setAction_1(int action_1) {
		this.action_1 = action_1;
	}

	@Log(logField = "action_2", fieldType = "int", index = "0")
	public int getAction_2() {
		return action_2;
	}

	public void setAction_2(int action_2) {
		this.action_2 = action_2;
	}

	@Log(logField = "item_number", fieldType = "int", index = "0")
	public int getItem_number() {
		return item_number;
	}

	public void setItem_number(int item_number) {
		this.item_number = item_number;
	}

	@Log(logField = "happend_time", fieldType = "int", index = "1")
	public int getHappend_time() {
		return happend_time;
	}

	public void setHappend_time(int happend_time) {
		this.happend_time = happend_time;
	}

	/**
	 * 日志多长时间建一次表
	 *
	 * @return
	 */
	@Override
	public TableCheckStepEnum getRollingStep() {
		return TableCheckStepEnum.UNROLL;
	}

	@Override
	public void logToFile() {
		logger.error(buildSql());
	}
}
