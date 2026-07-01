package com.game.equip.log;

import com.game.common.dblog.DbLog;
import com.game.serialization.SerialCache;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 装备培养日志
 */
public class EquipWashLog extends DbLog {

    private static final Logger logger = LogManager.getLogger("EquipWashLog");

    private long equipId;               //装备ID
    private int part;                   //部位ID
    private String wash;                //洗练结果
    private String pos;                 //洗练锁定位置
    private long actionId;              //关联ID

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(fieldType = "bigint", logField = "equipId", index = "0")
    public long getEquipId() {
        return equipId;
    }

    public void setEquipId(long equipId) {
        this.equipId = equipId;
    }

    @Log(fieldType = "int", logField = "part", index = "0")
    public int getPart() {
        return part;
    }

    public void setPart(int part) {
        this.part = part;
    }

    @Log(fieldType = "varchar(200)", logField = "wash", index = "0")
    public String getWash() {
        return wash;
    }

    public void setWash(String wash) {
        this.wash = wash;
    }

    @Log(fieldType = "varchar(200)", logField = "pos", index = "0")
    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }

    @Log(fieldType = "bigint", logField = "actioinId", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public boolean serial(SerialCache cache) {
        equipId = cache.getLong();
        part = cache.getInt();
        wash = cache.getString();
        actionId = cache.getLong();
        return true;
    }

}
