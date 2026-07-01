package com.game.bi.bi4399;

import com.game.db.DBErrorToFile;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @explain: 帮派信息表记录帮派详情，该表是状态表，是每个帮派一条数据的，当该帮派状态发生改变时需要对该表的相应数据进行及时更新。
 * @time Created on 2020/3/26 17:21.
 * @author: tc
 */
public class tbllog_guild extends BaseLogBean {
	// 所属平台，记录SDK platformID_gameID
	private String platform;
	// 设备端：android、ios、web、pc
	private String device;
	// 帮派ID
	private long guild_id;
	// 帮派名称
	private String guild_name;
	// 帮派等级
	private int guild_level;
	// 帮派经验
	private long guild_exp;
	// 帮派排名
	private int guild_rank;
	// 帮派人数
	private int guild_member;
	// 帮主ID FIXME 文档上为int类型
	private long guild_leader_id;
	// 帮主名称
	private String guild_leader_name;
	// 帮主ID战力
	private long guild_leader_power;
	// 帮主VIP等级
	private int guild_leader_vip;
	// 帮派公告(可选)
	private String guild_notice;
	// 帮派总战力，全部角色战力累加(可选)
	private long guild_power;
	// 帮派资金(可选)
	private int guild_money;
	// 帮派创建时间（索引）
	private int happend_time;

	public tbllog_guild() {}

	public tbllog_guild(String platform, String device, long guild_id, String guild_name, int guild_level, long guild_exp, int guild_rank, int guild_member, long guild_leader_id, String guild_leader_name, long guild_leader_power, int guild_leader_vip, String guild_notice, long guild_power, int guild_money, int happend_time) {
		this.platform = platform;
		this.device = device;
		this.guild_id = guild_id;
		this.guild_name = guild_name;
		this.guild_level = guild_level;
		this.guild_exp = guild_exp;
		this.guild_rank = guild_rank;
		this.guild_member = guild_member;
		this.guild_leader_id = guild_leader_id;
		this.guild_leader_name = guild_leader_name;
		this.guild_leader_power = guild_leader_power;
		this.guild_leader_vip = guild_leader_vip;
		this.guild_notice = guild_notice;
		this.guild_power = guild_power;
		this.guild_money = guild_money;
		this.happend_time = happend_time;
	}
    public int update(Connection connection) {
        String sql = "UPDATE tbllog_guild " +
                "SET" +
                "  platform = ?" +
                " ,device = ?" +
                " ,guild_name = ?" +
                " ,guild_level = ?" +
                " ,guild_exp = ?" +
                " ,guild_rank = ?" +
                " ,guild_member = ?" +
                " ,guild_leader_id = ?" +
                " ,guild_leader_name = ?" +
                " ,guild_leader_power = ?" +
                " ,guild_leader_vip = ?" +
                " ,guild_notice = ?" +
                " ,guild_power = ?" +
                " ,guild_money = ?" +
                " ,happend_time = ?" +
                " WHERE guild_id = ?";
        try (PreparedStatement updateStmt = connection.prepareStatement(sql)) {
            int i = 1;
            updateStmt.setString(i++, this.getPlatform());
            updateStmt.setString(i++, this.getDevice());
            updateStmt.setString(i++, this.getGuild_name());
            updateStmt.setInt(i++, this.getGuild_level());
            updateStmt.setLong(i++, this.getGuild_exp());
            updateStmt.setInt(i++, this.getGuild_rank());
            updateStmt.setInt(i++, this.getGuild_member());
            updateStmt.setLong(i++, this.getGuild_leader_id());
            updateStmt.setString(i++, this.getGuild_leader_name());
            updateStmt.setLong(i++, this.getGuild_leader_power());
            updateStmt.setInt(i++, this.getGuild_leader_vip());
            updateStmt.setString(i++, this.getGuild_notice());
            updateStmt.setLong(i++, this.getGuild_power());
            updateStmt.setInt(i++, this.getGuild_money());
            updateStmt.setInt(i++, this.getHappend_time());
            updateStmt.setLong(i, this.getGuild_id());
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

	@Log(logField = "guild_id", fieldType = "bigint", index = "0")
	public long getGuild_id() {
		return guild_id;
	}

	public void setGuild_id(long guild_id) {
		this.guild_id = guild_id;
	}

	@Log(logField = "guild_name", fieldType = "varchar(100)", index = "0")
	public String getGuild_name() {
		return guild_name;
	}

	public void setGuild_name(String guild_name) {
		this.guild_name = guild_name;
	}

	@Log(logField = "guild_level", fieldType = "int", index = "0")
	public int getGuild_level() {
		return guild_level;
	}

	public void setGuild_level(int guild_level) {
		this.guild_level = guild_level;
	}

	@Log(logField = "guild_exp", fieldType = "bigint", index = "0")
	public long getGuild_exp() {
		return guild_exp;
	}

	public void setGuild_exp(long guild_exp) {
		this.guild_exp = guild_exp;
	}

	@Log(logField = "guild_rank", fieldType = "int", index = "0")
	public int getGuild_rank() {
		return guild_rank;
	}

	public void setGuild_rank(int guild_rank) {
		this.guild_rank = guild_rank;
	}

	@Log(logField = "guild_member", fieldType = "int", index = "0")
	public int getGuild_member() {
		return guild_member;
	}

	public void setGuild_member(int guild_member) {
		this.guild_member = guild_member;
	}

	@Log(logField = "guild_leader_id", fieldType = "bigint", index = "0")
	public long getGuild_leader_id() {
		return guild_leader_id;
	}

	public void setGuild_leader_id(long guild_leader_id) {
		this.guild_leader_id = guild_leader_id;
	}

	@Log(logField = "guild_leader_name", fieldType = "varchar(100)", index = "0")
	public String getGuild_leader_name() {
		return guild_leader_name;
	}

	public void setGuild_leader_name(String guild_leader_name) {
		this.guild_leader_name = guild_leader_name;
	}

	@Log(logField = "guild_leader_power", fieldType = "bigint", index = "0")
	public long getGuild_leader_power() {
		return guild_leader_power;
	}

	public void setGuild_leader_power(long guild_leader_power) {
		this.guild_leader_power = guild_leader_power;
	}

	@Log(logField = "guild_leader_vip", fieldType = "int", index = "0")
	public int getGuild_leader_vip() {
		return guild_leader_vip;
	}

	public void setGuild_leader_vip(int guild_leader_vip) {
		this.guild_leader_vip = guild_leader_vip;
	}

	@Log(logField = "guild_notice", fieldType = "varchar(255)", index = "0")
	public String getGuild_notice() {
		return guild_notice;
	}

	public void setGuild_notice(String guild_notice) {
		this.guild_notice = guild_notice;
	}

	@Log(logField = "guild_power", fieldType = "bigint", index = "0")
	public long getGuild_power() {
		return guild_power;
	}

	public void setGuild_power(long guild_power) {
		this.guild_power = guild_power;
	}

	@Log(logField = "guild_money", fieldType = "int", index = "0")
	public int getGuild_money() {
		return guild_money;
	}

	public void setGuild_money(int guild_money) {
		this.guild_money = guild_money;
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
