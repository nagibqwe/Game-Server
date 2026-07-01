package com.game.guild.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 公会建筑升级
 */
public class GuildBuildingUpLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger("GuildBuildingUpLog");

    private long guildId;
    //操作玩家
    private long roleId1;
    //建筑类型
    private int type;
    //建筑等级
    private int level;
    //升级消耗
    private int expend;
    //消耗后剩余
    private int remaining;
    //关联其他日志Id
    private long actionId;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

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

    @Log(fieldType = "int", index = "0", logField = "type")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(fieldType = "int", index = "0", logField = "level")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(fieldType = "int", index = "0", logField = "expend")
    public int getExpend() {
        return expend;
    }

    public void setExpend(int expend) {
        this.expend = expend;
    }

    @Log(fieldType = "int", index = "0", logField = "remaining")
    public int getRemaining() {
        return remaining;
    }

    public void setRemaining(int remaining) {
        this.remaining = remaining;
    }

    @Log(fieldType = "bigint", index = "0", logField = "actionId")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
