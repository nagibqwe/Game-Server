package com.game.player.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class PlayerAttChangeLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("PlayerAttChangeLog");

    private long roleId;
    private String name;
    private String endAtt;    //结束属性点
    private long endFight;    //结束战斗力

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "name", fieldType = "varchar(50)", index = "0")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Log(logField = "endAtt", fieldType = "varchar(500)", index = "0")
    public String getEndAtt() {
        return endAtt;
    }

    public void setEndAtt(String endAtt) {
        this.endAtt = endAtt;
    }

    @Log(logField = "endFight", fieldType = "bigint", index = "0")
    public long getEndFight() {
        return endFight;
    }

    public void setEndFight(long endFight) {
        this.endFight = endFight;
    }

}
