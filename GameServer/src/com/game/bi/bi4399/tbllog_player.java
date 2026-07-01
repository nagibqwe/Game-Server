package com.game.bi.bi4399;

import com.game.db.DBErrorToFile;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @explain: 用户信息表记录玩家在游戏内的关键信息，该表是状态表，是每个角色一条数据的，当该玩家状态发生改变时，例如玩家有 创角 登录 充值 升级 下线 等重大事件时候，需要及时对该表的相应数据进行更新。
 * @time Created on 2020/3/26 16:47.
 * @author: tc
 */
public class tbllog_player extends BaseLogBean {
	// TODO 特殊处理该表
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 角色ID
	private long role_id;
	// 角色名
	private String role_name;
	// 平台账号ID
	private String account_name;
	// 平台帐号名
	private String user_name;
	// 阵营
	private String dim_nation;
	// 职业ID
	private int dim_prof;
	// 性别(0=女，1=男，2=未知) tinyint
	private int dim_sex;
	// 注册时间（索引）
	private int reg_time;
	// 注册IP
	private String reg_ip;
	// 用户设备ID,用户设备ID，Android系统取IMEI码；iOS6.0以前系统取设备号，iOS6.0及以后的系统取广告标示符（IDFA -Identifier For Advertising）, PC可以采用mac地址。请注意不要用MD5加密did字段
	private String did;
	// 等级
	private int dim_level;
	// VIP等级
	private int dim_vip_level;
	// 用户段位ID
	private int dim_grade;
	// 当前经验
	private long dim_exp;
	// 帮派名称
	private String dim_guild;
	// 战斗力
	private long dim_power;
	// 剩余元宝数（充值兑换货币）
	private int gold_number;
	// 剩余绑定元宝数（非充值兑换货币）
	private int bgold_number;
	// 剩余金币数
	private int coin_number;
	// 剩余绑定金币数
	private int bcoin_number;
	// 总充值
	private int pay_money;
	// 首充时间
	private int first_pay_time;
	// 最后充值时间
	private int last_pay_time;
	// 最后登录时间
	private int last_login_time;
	// 变动时间
	private int happend_time;

	public tbllog_player() { }

	public tbllog_player(String platform, String device, long role_id, String role_name, String account_name, String user_name, String dim_nation, int dim_prof, int dim_sex, int reg_time, String reg_ip, String did, int dim_level, int dim_vip_level, int dim_grade, long dim_exp, String dim_guild, long dim_power, int gold_number, int bgold_number, int coin_number, int bcoin_number, int pay_money, int first_pay_time, int last_pay_time, int last_login_time, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.role_id = role_id;
		this.role_name = role_name;
		this.account_name = account_name;
		this.user_name = user_name;
		this.dim_nation = dim_nation;
		this.dim_prof = dim_prof;
		this.dim_sex = dim_sex;
		this.reg_time = reg_time;
		this.reg_ip = reg_ip;
		this.did = did;
		this.dim_level = dim_level;
		this.dim_vip_level = dim_vip_level;
		this.dim_grade = dim_grade;
		this.dim_exp = dim_exp;
		this.dim_guild = dim_guild;
		this.dim_power = dim_power;
		this.gold_number = gold_number;
		this.bgold_number = bgold_number;
		this.coin_number = coin_number;
		this.bcoin_number = bcoin_number;
		this.pay_money = pay_money;
		this.first_pay_time = first_pay_time;
		this.last_pay_time = last_pay_time;
		this.last_login_time = last_login_time;
		this.happend_time = happend_time;
	}

	/**
	 * 更新数据
	 *
	 * @param connection
	 * @return
	 */
	public int update(Connection connection) {
		String sql = "UPDATE tbllog_player " +
				"SET" +
				"  happend_time = ?" +
				" ,coin_number = ?" +
				" ,device = ?" +
				" ,dim_exp = ?" +
				" ,pay_money = ?" +
				" ,role_name = ?" +
//				" ,role_id = ?" +
				" ,platform = ?" +
				" ,user_name = ?" +
				" ,dim_vip_level = ?" +
				" ,dim_nation = ?" +
				" ,reg_ip = ?" +
				" ,account_name = ?" +
				" ,last_pay_time = ?" +
				" ,gold_number = ?" +
				" ,bgold_number = ?" +
				" ,reg_time = ?" +
				" ,did = ?" +
				" ,last_login_time = ?" +
				" ,dim_sex = ?" +
				" ,dim_grade = ?" +
				" ,first_pay_time = ?" +
				" ,bcoin_number = ?" +
				" ,dim_level = ?" +
				" ,dim_prof = ?" +
				" ,dim_power = ?" +
				" ,dim_guild = ?" +
				" WHERE role_id = ?";
		try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
			int i = 1;
			updateStmt.setInt(i++, this.getHappend_time());
			updateStmt.setInt(i++, this.getCoin_number());
			updateStmt.setString(i++, this.getDevice());
			updateStmt.setLong(i++, this.getDim_exp());
			updateStmt.setInt(i++, this.getPay_money());
			updateStmt.setString(i++, this.getRole_name());
			updateStmt.setString(i++, this.getPlatform());
			updateStmt.setString(i++, this.getUser_name());
			updateStmt.setInt(i++, this.getDim_vip_level());
			updateStmt.setString(i++, this.getDim_nation());
			updateStmt.setString(i++, this.getReg_ip());
			updateStmt.setString(i++, this.getAccount_name());
			updateStmt.setInt(i++, this.getLast_pay_time());
			updateStmt.setInt(i++, this.getGold_number());
			updateStmt.setInt(i++, this.getBgold_number());
			updateStmt.setInt(i++, this.getReg_time());
			updateStmt.setString(i++, this.getDid());
			updateStmt.setInt(i++, this.getLast_login_time());
			updateStmt.setInt(i++, this.getDim_sex());
			updateStmt.setInt(i++, this.getDim_grade());
			updateStmt.setInt(i++, this.getFirst_pay_time());
			updateStmt.setInt(i++, this.getBcoin_number());
			updateStmt.setInt(i++, this.getDim_level());
			updateStmt.setInt(i++, this.getDim_prof());
			updateStmt.setLong(i++, this.getDim_power());
			updateStmt.setString(i++, this.getDim_guild());
			updateStmt.setLong(i++, this.getRole_id());
			return updateStmt.executeUpdate();
		} catch (SQLException ex) {
			DBErrorToFile.error(ex, ex);
		}
		return 0;
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

	@Log(logField = "role_name", fieldType = "varchar(100)", index = "0")
	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	@Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
	public String getAccount_name() {
		return account_name;
	}

	public void setAccount_name(String account_name) {
		this.account_name = account_name;
	}

	@Log(logField = "user_name", fieldType = "varchar(100)", index = "0")
	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	@Log(logField = "dim_nation", fieldType = "varchar(100)", index = "0")
	public String getDim_nation() {
		return dim_nation;
	}

	public void setDim_nation(String dim_nation) {
		this.dim_nation = dim_nation;
	}

	@Log(logField = "dim_prof", fieldType = "int", index = "0")
	public int getDim_prof() {
		return dim_prof;
	}

	public void setDim_prof(int dim_prof) {
		this.dim_prof = dim_prof;
	}

	@Log(logField = "dim_sex", fieldType = "tinyint", index = "0")
	public int getDim_sex() {
		return dim_sex;
	}

	public void setDim_sex(int dim_sex) {
		this.dim_sex = dim_sex;
	}

	@Log(logField = "reg_time", fieldType = "int", index = "1")
	public int getReg_time() {
		return reg_time;
	}

	public void setReg_time(int reg_time) {
		this.reg_time = reg_time;
	}

	@Log(logField = "reg_ip", fieldType = "varchar(100)", index = "0")
	public String getReg_ip() {
		return reg_ip;
	}

	public void setReg_ip(String reg_ip) {
		this.reg_ip = reg_ip;
	}

	@Log(logField = "did", fieldType = "varchar(100)", index = "0")
	public String getDid() {
		return did;
	}

	public void setDid(String did) {
		this.did = did;
	}

	@Log(logField = "dim_level", fieldType = "int", index = "0")
	public int getDim_level() {
		return dim_level;
	}

	public void setDim_level(int dim_level) {
		this.dim_level = dim_level;
	}

	@Log(logField = "dim_vip_level", fieldType = "int", index = "0")
	public int getDim_vip_level() {
		return dim_vip_level;
	}

	public void setDim_vip_level(int dim_vip_level) {
		this.dim_vip_level = dim_vip_level;
	}

	@Log(logField = "dim_grade", fieldType = "int", index = "0")
	public int getDim_grade() {
		return dim_grade;
	}

	public void setDim_grade(int dim_grade) {
		this.dim_grade = dim_grade;
	}

	@Log(logField = "dim_exp", fieldType = "bigint", index = "0")
	public long getDim_exp() {
		return dim_exp;
	}

	public void setDim_exp(long dim_exp) {
		this.dim_exp = dim_exp;
	}

	@Log(logField = "dim_guild", fieldType = "varchar(100)", index = "0")
	public String getDim_guild() {
		return dim_guild;
	}

	public void setDim_guild(String dim_guild) {
		this.dim_guild = dim_guild;
	}

	@Log(logField = "dim_power", fieldType = "bigint", index = "0")
	public long getDim_power() {
		return dim_power;
	}

	public void setDim_power(long dim_power) {
		this.dim_power = dim_power;
	}

	@Log(logField = "gold_number", fieldType = "int", index = "0")
	public int getGold_number() {
		return gold_number;
	}

	public void setGold_number(int gold_number) {
		this.gold_number = gold_number;
	}

	@Log(logField = "bgold_number", fieldType = "int", index = "0")
	public int getBgold_number() {
		return bgold_number;
	}

	public void setBgold_number(int bgold_number) {
		this.bgold_number = bgold_number;
	}

	@Log(logField = "coin_number", fieldType = "int", index = "0")
	public int getCoin_number() {
		return coin_number;
	}

	public void setCoin_number(int coin_number) {
		this.coin_number = coin_number;
	}

	@Log(logField = "bcoin_number", fieldType = "int", index = "0")
	public int getBcoin_number() {
		return bcoin_number;
	}

	public void setBcoin_number(int bcoin_number) {
		this.bcoin_number = bcoin_number;
	}

	@Log(logField = "pay_money", fieldType = "int", index = "0")
	public int getPay_money() {
		return pay_money;
	}

	public void setPay_money(int pay_money) {
		this.pay_money = pay_money;
	}

	@Log(logField = "first_pay_time", fieldType = "int", index = "0")
	public int getFirst_pay_time() {
		return first_pay_time;
	}

	public void setFirst_pay_time(int first_pay_time) {
		this.first_pay_time = first_pay_time;
	}

	@Log(logField = "last_pay_time", fieldType = "int", index = "0")
	public int getLast_pay_time() {
		return last_pay_time;
	}

	public void setLast_pay_time(int last_pay_time) {
		this.last_pay_time = last_pay_time;
	}

	@Log(logField = "last_login_time", fieldType = "int", index = "0")
	public int getLast_login_time() {
		return last_login_time;
	}

	public void setLast_login_time(int last_login_time) {
		this.last_login_time = last_login_time;
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
