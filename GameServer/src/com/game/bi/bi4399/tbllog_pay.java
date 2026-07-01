package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏内充值时的信息。请设置pay_type字段，用于区别测试订单以及正式订单，并注意在测试服中不进行过大金额测试。
 * @time Created on 2020/3/26 17:17.
 * @author: tc
 */
public class tbllog_pay extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 平台账号ID
	private String account_name;
	// 玩家IP
	private String user_ip;
	// 等级
	private int dim_level;
	// 充值类型, 测试订单记 0（不计入流水部分）, 其他为正式订单(如1)
	private int pay_type;
	// 订单号（设唯一键确保订单不重复，varchar不得超过255）（唯一键）
	private String order_id;
	// 充值金额（总充值金额）
	private float pay_money;
	// 充值获得的元宝/金币数
	private int pay_gold;
	// 用户设备ID
	private String did;
	// 游戏版本号
	private String game_version;
	// 事件发生时间（索引）
	private int happend_time;

	public tbllog_pay() {}

	public tbllog_pay(String platform, String device, long role_id, String account_name, String user_ip, int dim_level, int pay_type, String order_id, float pay_money, int pay_gold, String did, String game_version, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.user_ip = user_ip;
		this.dim_level = dim_level;
		this.pay_type = pay_type;
		this.order_id = order_id;
		this.pay_money = pay_money;
		this.pay_gold = pay_gold;
		this.did = did;
		this.game_version = game_version;
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

	@Log(logField = "user_ip", fieldType = "varchar(100)", index = "0")
	public String getUser_ip() {
		return user_ip;
	}

	public void setUser_ip(String user_ip) {
		this.user_ip = user_ip;
	}

	@Log(logField = "dim_level", fieldType = "int", index = "0")
	public int getDim_level() {
		return dim_level;
	}

	public void setDim_level(int dim_level) {
		this.dim_level = dim_level;
	}

	@Log(logField = "pay_type", fieldType = "int", index = "0")
	public int getPay_type() {
		return pay_type;
	}

	public void setPay_type(int pay_type) {
		this.pay_type = pay_type;
	}

	@Log(logField = "order_id", fieldType = "varchar(100)", index = "1")
	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	@Log(logField = "pay_money", fieldType = "float", index = "0")
	public float getPay_money() {
		return pay_money;
	}

	public void setPay_money(float pay_money) {
		this.pay_money = pay_money;
	}

	@Log(logField = "pay_gold", fieldType = "int", index = "0")
	public int getPay_gold() {
		return pay_gold;
	}

	public void setPay_gold(int pay_gold) {
		this.pay_gold = pay_gold;
	}

	@Log(logField = "did", fieldType = "varchar(100)", index = "0")
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Log(logField = "game_version", fieldType = "varchar(100)", index = "0")
	public String getGame_version() {
		return game_version;
	}

	public void setGame_version(String game_version) {
		this.game_version = game_version;
	}

	@Log(logField = "happend_time", fieldType = "int", index = "2")
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
