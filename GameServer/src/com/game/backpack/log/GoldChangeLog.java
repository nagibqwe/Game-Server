package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 元宝变化日志
 */
public class GoldChangeLog extends CommonLogBean {

    protected static final Logger log = LogManager.getLogger("GoldChangeLog");

    private int roleLevel;          //玩家等级
    private String loginIp;         //登录IP
    private int changeNum;          //变化值
    private int reason;             //原因码
    private int beforeNum;          //变化前值
    private int afterNum;           //变化后值
    private long actionId;          //关联ID

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "roleLevel", fieldType = "int", index = "0")
    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    @Log(logField = "loginIp", fieldType = "varchar(50)", index = "0")
    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @Log(logField = "changeNum", fieldType = "int", index = "0")
    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(logField = "beforeNum", fieldType = "int", index = "0")
    public int getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(int beforeNum) {
        this.beforeNum = beforeNum;
    }

    @Log(logField = "afterNum", fieldType = "int", index = "0")
    public int getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(int afterNum) {
        this.afterNum = afterNum;
    }

    public long setActionId(long action) {
        return actionId = action;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

}
