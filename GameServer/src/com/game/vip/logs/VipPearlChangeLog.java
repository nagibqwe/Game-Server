package com.game.vip.logs;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

public class VipPearlChangeLog extends CommonLogBean {

    private int opType;//0：过期卸下 1：没有装备直接佩戴 2永久替换限时 3永久佩戴永久

    private int itemId;//使用的道具ID

    private int deadLine;//过期时间

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }

    @Log(logField = "opType", fieldType = "int", index = "0")
    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    @Log(logField = "itemId", fieldType = "int", index = "0")
    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    @Log(logField = "deadLine", fieldType = "int", index = "0")
    public int getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(int deadLine) {
        this.deadLine = deadLine;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }
}
