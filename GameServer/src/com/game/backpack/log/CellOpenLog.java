package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * 背包仓库格子开启日志
 */
public class CellOpenLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("CellOpenLog");

    private byte type;              //类型：1包裹、2仓库
    private int resumeGold;         //花费元宝
    private byte actionType;        //开启类型：1 元宝开启  0时间开启
    private int beforeCells;        //开启前格子数
    private int afterCells;         //开启后格子数
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

    @Log(fieldType = "bigint", logField = "roleid", index = "0")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "int", logField = "celltype", index = "0")
    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    @Log(fieldType = "int", logField = "resumgold", index = "0")
    public int getResumeGold() {
        return resumeGold;
    }

    public void setResumeGold(int resumeGold) {
        this.resumeGold = resumeGold;
    }

    @Log(fieldType = "int", logField = "actionType", index = "0")
    public byte getActionType() {
        return actionType;
    }

    public void setActionType(byte actionType) {
        this.actionType = actionType;
    }

    @Log(fieldType = "int", logField = "beforeCells", index = "0")
    public int getBeforeCells() {
        return beforeCells;
    }

    public void setBeforeCells(int beforeCells) {
        this.beforeCells = beforeCells;
    }

    @Log(fieldType = "int", logField = "afterCells", index = "0")
    public int getAfterCells() {
        return afterCells;
    }

    public void setAfterCells(int afterCells) {
        this.afterCells = afterCells;
    }

    @Log(fieldType = "bigint", logField = "actionId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

}
