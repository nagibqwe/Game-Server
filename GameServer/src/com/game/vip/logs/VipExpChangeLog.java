package com.game.vip.logs;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

public class VipExpChangeLog extends CommonLogBean {

    private long nowExp;

    private int addExp;

    private int reason;

    private long actionId;

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Log(logField = "nowExp", fieldType = "bigint", index = "0")
    public long getNowExp() {
        return nowExp;
    }

    public void setNowExp(long nowExp) {
        this.nowExp = nowExp;
    }


    @Log(logField = "addExp", fieldType = "int", index = "0")
    public int getAddExp() {
        return addExp;
    }

    public void setAddExp(int addExp) {
        this.addExp = addExp;
    }


    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }
}
