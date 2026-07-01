package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 金币变化日志
 */
public class MoneyChangeLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("MoneyChangeLog");

    private int roleLevel;          //玩家等级
    private String loginIp;         //登录ip
    private int moneyType;          //货币类型
    private long changeNum;         //变化数量
    private int reason;             //原因码
    private long beforeNum;         //变化前数量
    private long afterNum;          //变化后数量
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
        logger.error(buildSql());
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

    @Log(logField = "monyeyType", fieldType = "int", index = "0")
    public int getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(int moneyType) {
        this.moneyType = moneyType;
    }

    @Log(logField = "changeNum", fieldType = "bigint", index = "0")
    public long getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(long changeNum) {
        this.changeNum = changeNum;
    }

    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(logField = "beforeNum", fieldType = "bigint", index = "0")
    public long getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(long beforeNum) {
        this.beforeNum = beforeNum;
    }

    @Log(logField = "afterNum", fieldType = "bigint", index = "0")
    public long getAfternum() {
        return afterNum;
    }

    public void setAfterNum(long afterNum) {
        this.afterNum = afterNum;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
