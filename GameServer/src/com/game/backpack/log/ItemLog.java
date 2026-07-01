package com.game.backpack.log;

import com.game.player.structs.Player;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Date;

/**
 * 物品变化日志
 */
public class ItemLog extends CommonLogBean {

    private static final Logger logger = LogManager.getLogger("ItemLog");

    private long itemId;            //物品ID
    private int modelId;            //物品模型ID
    private int changeNum;          //变化的数量
    private int reason;             //原因码
    private long actionId;          //关联ID
    private String changeAction;    //变更类型
    private int type; //道具类型
    private Date createDate;//创建时间

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.DAY;
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

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "create_date", fieldType = "datetime", index = "0")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }
}
