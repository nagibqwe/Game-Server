package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内道具产出和消耗日志
 */
public class tbllog_items extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 平台账号ID
	private String account_name;
	// 玩家等级
	private int dim_level;
	//操作类型( -1是使用【数量减少】，1是增加【数量增加】，0是修改【数量不变，状态变化】)
	private int opt;
	//对应各自项目组的道具消耗项目字典,行为类型（dict_action.action_id）
	private int action_id;
	//道具ID
	private int item_id;
	//道具获得/消耗数量
	private int item_number;
	//物品产出所在地图ID(dict_action.action_id)
	private int map_id;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_items() {}

	public tbllog_items(String platform, String device, long role_id, String account_name, int dim_level, int opt, int action_id, int item_id, int item_number, int map_id, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.dim_level = dim_level;
		this.opt = opt;
		this.action_id = action_id;
		this.item_id = item_id;
		this.item_number = item_number;
		this.map_id = map_id;
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

	@Log(logField = "opt", fieldType = "int", index = "0")
	public int getOpt() {
		return opt;
	}

	public void setOpt(int opt) {
		this.opt = opt;
	}

	@Log(logField = "action_id", fieldType = "int", index = "0")
	public int getAction_id() {
		return action_id;
	}

	public void setAction_id(int action_id) {
		this.action_id = action_id;
	}

	@Log(logField = "item_id", fieldType = "int", index = "0")
	public int getItem_id() {
		return item_id;
	}

	public void setItem_id(int item_id) {
		this.item_id = item_id;
	}

	@Log(logField = "item_number", fieldType = "int", index = "0")
	public int getItem_number() {
		return item_number;
	}

	public void setItem_number(int item_number) {
		this.item_number = item_number;
	}

	@Log(logField = "map_id", fieldType = "int", index = "0")
	public int getMap_id() {
		return map_id;
	}

	public void setMap_id(int map_id) {
		this.map_id = map_id;
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
