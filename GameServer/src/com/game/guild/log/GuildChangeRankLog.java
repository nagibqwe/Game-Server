package com.game.guild.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 公会职位变更
 */
public class GuildChangeRankLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger("GuildChangeRankLog");

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    private long guildId;
    //操作玩家
    private long roleId1;
    //操作玩家的权限等级
    private int rank;
    //被设置的玩家
    private long roleId2;
    //设置成现在的权限
    private int currRank;

    @Log(fieldType = "bigint", index = "0", logField = "guildId")
    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "roleId1")
    public long getRoleId1() {
        return roleId1;
    }

    public void setRoleId1(long roleId1) {
        this.roleId1 = roleId1;
    }

    @Log(fieldType = "int", index = "0", logField = "rank")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Log(fieldType = "bigint", index = "0", logField = "roleId2")
    public long getRoleId2() {
        return roleId2;
    }

    public void setRoleId2(long roleId2) {
        this.roleId2 = roleId2;
    }

    @Log(fieldType = "int", index = "0", logField = "currRank")
    public int getCurrRank() {
        return currRank;
    }

    public void setCurrRank(int currRank) {
        this.currRank = currRank;
    }

}
