package com.game.equip.log;

import com.game.common.dblog.DbLog;
import com.game.serialization.SerialCache;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;

/**
 * 装备合成日志
 */
public class EquipSynthesisLog extends DbLog{

    private int equipId;//合成装备id
    private String ids;//合成材料ids
    private long itemModeId;
    private long itemNumber;
    private int isSuccess; //0表示成功 其他表示失败

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(fieldType = "int", index = "0", logField = "equipId")
    public int getEquipId() {
        return equipId;
    }

    public void setEquipId(int equipId) {
        this.equipId = equipId;
    }

    @Log(fieldType = "text", index = "0", logField = "ids")
    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    @Log(fieldType = "int", index = "0", logField = "itemModeId")
    public long getItemModeId() {
        return itemModeId;
    }

    public void setItemModeId(long itemModeId) {
        this.itemModeId = itemModeId;
    }

    @Log(fieldType = "int", index = "0", logField = "itemNumber")
    public long getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(long itemNumber) {
        this.itemNumber = itemNumber;
    }

    @Log(fieldType = "int", index = "0", logField = "isSuccess")
    public int getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(int isSuccess) {
        this.isSuccess = isSuccess;
    }

    @Override
    public boolean serial(SerialCache cache) {
        equipId = cache.getInt();
        ids = cache.getString();
        itemModeId = cache.getInt();
        itemNumber = cache.getInt();
        isSuccess = cache.getInt();
        return true;
    }

}
