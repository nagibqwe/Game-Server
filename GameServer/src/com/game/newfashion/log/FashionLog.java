package com.game.newfashion.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 时装激活日志
 */
public class FashionLog extends CommonLogBean {

    private int fashionId;      //时装id
    private int fashionLv;      //时装等级
    private int actType;        //操作类型 0：激活

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(logField = "fashionId", fieldType = "int", index = "0")
    public int getFashionId() {
        return fashionId;
    }

    public void setFashionId(int fashionId) {
        this.fashionId = fashionId;
    }

    @Log(logField = "fashionLv", fieldType = "int", index = "0")
    public int getFashionLv() {
        return fashionLv;
    }

    public void setFashionLv(int fashionLv) {
        this.fashionLv = fashionLv;
    }

    @Log(logField = "actType", fieldType = "int", index = "0")
    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }

}
