package com.game.player.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 转移角色Log
 */
public class TranRoleLog extends BaseLogBean {

    private static final Logger logger = LogManager.getLogger("TranRoleLog");

    private long roleId;		         //玩家ID
    private long oldUserId;              //老的账号ID
    private long newUserId;              //新的账号ID

    @Log(fieldType = "bigint", index = "0", logField = "roleid")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "oldUserId")
    public long getOldUserId() {
        return oldUserId;
    }

    public void setOldUserId(long oldUserId) {
        this.oldUserId = oldUserId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "newUserId")
    public long getNewUserId() {
        return newUserId;
    }

    public void setNewUserId(long newUserId) {
        this.newUserId = newUserId;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

}
