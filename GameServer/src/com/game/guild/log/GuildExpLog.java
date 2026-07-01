package com.game.guild.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 公会资金log
 */
public class GuildExpLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger("GuildExpLog");

    private long guildId;

    private long actionId;

    private long exp;

    private long changeExp;

    private int res;

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

    @Log(fieldType = "bigint", index = "0", logField = "actionId")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "exp")
    public long getExp() {
        return exp;
    }

    public void setExp(long exp) {
        this.exp = exp;
    }

    @Log(fieldType = "bigint", index = "0", logField = "changeExp")
    public long getChangeExp() {
        return changeExp;
    }

    public void setChangeExp(long changeExp) {
        this.changeExp = changeExp;
    }

    @Log(fieldType = "int", index = "0", logField = "res")
    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
