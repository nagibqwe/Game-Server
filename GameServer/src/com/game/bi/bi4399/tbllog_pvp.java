package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志整合原来的tbllog_pvp以及tbllog_battle，通过action_type区分，如果无区分则记为0，记录玩家与玩家之间的交互日志
 * @time Created on 2020/3/26 17:07.
 * @author: tc
 */
public class tbllog_pvp extends BaseLogBean {
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
	// 类型id（对应dict_action.action_type_id）,现pvp日志分为PVP字典(12)以及战场字典(10), 如果游戏没有区分，则记为0
	private int action_type;
	// pvp 类型，1=>1v1, 2=>2v2, 3=>3v3, 4=>4v4, 5=>5v5, 10=>10v10，帮派对战记999，其他从1000开始
	private int pvp_type;
	// PVP地图ID，对应dict_action中的action_id，表示该pvp行为所在的地图
	private int pvp_id;
	// 连续战斗局数，从1开始
	private int continuous;
	// 事件发生时间
	private int begin_time;
	// 事件结束时间（索引）
	private int end_time;
	// PVP战斗时长
	private int time_duration;
	// 战斗力
	private long dim_power;
	// 游戏场次或者记录成room_id
	private int game_id;
	// 状态 (0=提前退出，1=完成比赛，2=开始匹配，-1=退出匹配或匹配失败)
	private int status;
	// 结果（1=战胜，2=战败，3=战平，4=无胜负（类似虫虫、球球的自由模式需要用到4））
	private int result;
	// 事件发生时间
	private int happend_time;

	public tbllog_pvp() {}

	public tbllog_pvp(String platform, String device, long role_id, String account_name, int dim_level, int action_type, int pvp_type, int pvp_id, int continuous, int begin_time, int end_time, int time_duration, long dim_power, int game_id, int status, int result, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.account_name = account_name;
		this.dim_level = dim_level;
		this.action_type = action_type;
		this.pvp_type = pvp_type;
		this.pvp_id = pvp_id;
		this.continuous = continuous;
		this.begin_time = begin_time;
		this.end_time = end_time;
		this.time_duration = time_duration;
		this.dim_power = dim_power;
		this.game_id = game_id;
		this.status = status;
		this.result = result;
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

	@Log(logField = "action_type", fieldType = "int", index = "0")
	public int getAction_type() {
		return action_type;
	}

	public void setAction_type(int action_type) {
		this.action_type = action_type;
	}

	@Log(logField = "pvp_type", fieldType = "int", index = "0")
	public int getPvp_type() {
		return pvp_type;
	}

	public void setPvp_type(int pvp_type) {
		this.pvp_type = pvp_type;
	}

	@Log(logField = "pvp_id", fieldType = "int", index = "0")
	public int getPvp_id() {
		return pvp_id;
	}

	public void setPvp_id(int pvp_id) {
		this.pvp_id = pvp_id;
	}

	@Log(logField = "continuous", fieldType = "int", index = "0")
	public int getContinuous() {
		return continuous;
	}

	public void setContinuous(int continuous) {
		this.continuous = continuous;
	}

	@Log(logField = "begin_time", fieldType = "int", index = "0")
	public int getBegin_time() {
		return begin_time;
	}

	public void setBegin_time(int begin_time) {
		this.begin_time = begin_time;
	}

	@Log(logField = "end_time", fieldType = "int", index = "1")
	public int getEnd_time() {
		return end_time;
	}

	public void setEnd_time(int end_time) {
		this.end_time = end_time;
	}

	@Log(logField = "time_duration", fieldType = "int", index = "0")
	public int getTime_duration() {
		return time_duration;
	}

	public void setTime_duration(int time_duration) {
		this.time_duration = time_duration;
	}

	@Log(logField = "dim_power", fieldType = "bigint", index = "0")
	public long getDim_power() {
		return dim_power;
	}

	public void setDim_power(long dim_power) {
		this.dim_power = dim_power;
	}

	@Log(logField = "game_id", fieldType = "int", index = "0")
	public int getGame_id() {
		return game_id;
	}

	public void setGame_id(int game_id) {
		this.game_id = game_id;
	}

	@Log(logField = "status", fieldType = "int", index = "0")
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	@Log(logField = "result", fieldType = "int", index = "0")
	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	@Log(logField = "happend_time", fieldType = "int", index = "0")
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
