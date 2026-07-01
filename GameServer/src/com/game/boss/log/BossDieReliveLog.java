package com.game.boss.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

public class BossDieReliveLog extends BaseLogBean {

    private long bossId;        //bossID

    private long mapId;         //地图ID

    private int type;           //0死亡 1复活

    private long param;         //参数，死亡为击杀者ID

    @Log(logField = "bossId", fieldType = "bigint", index = "0")
    public long getBossId() {
        return bossId;
    }

    public void setBossId(long bossId) {
        this.bossId = bossId;
    }

    @Log(logField = "mapId", fieldType = "bigint", index = "0")
    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "param", fieldType = "bigint", index = "0")
    public long getParam() {
        return param;
    }

    public void setParam(long param) {
        this.param = param;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
