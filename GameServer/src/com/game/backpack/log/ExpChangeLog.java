package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExpChangeLog extends CommonLogBean {

    protected static final Logger log = LogManager.getLogger("ExpChangeLog");

    private int roleLevel;          //玩家等级
    private long changeNum;         //变化值
    private int reason;             //原因码
    private long beforeNum;         //变化前值
    private long afterNum;          //变化后值
    private long actionId;          //关联ID
    private long upLevelExp;        //升级经验

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
    public long getAfterNum() {
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

    @Log(logField = "upLevelExp", fieldType = "bigint", index = "0")
    public long getUpLevelExp() {
        return upLevelExp;
    }

    public void setUpLevelExp(long upLevelExp) {
        this.upLevelExp = upLevelExp;
    }

}
