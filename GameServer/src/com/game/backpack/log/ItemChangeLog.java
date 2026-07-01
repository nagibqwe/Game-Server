package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger;

/**
 * 物品变化日志
 */
public class ItemChangeLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("ItemChangeLog");

    private int roleLevel;          //角色等级
    private long itemId;            //物品ID
    private int modelId;            //物品模型ID
    private int changeNum;          //变化的数量
    private int oldNum;             //变化前的数量
    private int newNum;             //变化后的数量
    private int reason;             //原因码
    private long actionId;          //关联ID
    private String changeAction;    //变更类型
    private int coinType;           //消耗货币类型
    private float costNum;          //消耗货币数量
    private int cellId;             //格子位置
    private int type;               //道具类型

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    public void setPlayer(Player p) {
        this.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
    }
    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Log(logField = "roleLevel", fieldType = "int", index = "0")
    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    @Log(logField = "itemId", fieldType = "bigint", index = "0")
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Log(logField = "modelId", fieldType = "int", index = "0")
    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    @Log(logField = "changeNum", fieldType = "int", index = "0")
    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    @Log(logField = "oldNum", fieldType = "int", index = "0")
    public int getOldNum() {
        return oldNum;
    }

    public void setOldNum(int oldNum) {
        this.oldNum = oldNum;
    }

    @Log(logField = "newNum", fieldType = "int", index = "0")
    public int getNewNum() {
        return newNum;
    }

    public void setNewNum(int newNum) {
        this.newNum = newNum;
    }

    @Log(logField = "cellId", fieldType = "int", index = "0")
    public int getCellId() {
        return cellId;
    }

    public void setCellId(int cellId) {
        this.cellId = cellId;
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

    @Log(logField = "changeAction", fieldType = "varchar(20)", index = "0")
    public String getChangeAction() {
        return changeAction;
    }

    public void setChangeAction(String changeAction) {
        this.changeAction = changeAction;
    }

    @Log(logField = "coinType", fieldType = "int", index = "0")
    public int getCoinType() {
        return coinType;
    }

    public void setCoinType(int coinType) {
        this.coinType = coinType;
    }

    @Log(logField = "costNum", fieldType = "float", index = "0")
    public float getCostNum() {
        return costNum;
    }

    public void setCostNum(float costNum) {
        this.costNum = costNum;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
