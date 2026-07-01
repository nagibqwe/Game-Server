package com.game.dailyactive.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by cxl on 2019/9/17.
 */
public class EnterDailyActivityLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(EnterDailyActivityLog.class);

    private int dailyId;
    private int modelId;
    private long roleId;
    private int  level;
    private int sid;
    private String plat;

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }

    @Log(fieldType = "int", index = "0", logField = "dailyId")
    public int getDailyId() {return dailyId;}

    public void setDailyId(int dailyId) {this.dailyId = dailyId;}

    @Log(fieldType = "int", index = "0", logField = "modelId")
    public int getModelId(){return modelId;}

    public void setModelId(int modelId){this.modelId = modelId;}

    @Log(fieldType = "bigint", index = "0", logField = "roleId")
    public long getRoleId(){return roleId;}

    public void setRoleId(long roleId){this.roleId = roleId;}

    @Log(fieldType = "int", index = "0", logField = "level")
    public int getLevel(){return level;}

    public void setLevel(int level){this.level = level;}

    @Log(fieldType = "int", index = "0", logField = "sid")
    public int getSid(){return sid;}

    public void setSid(int sid){this.sid =sid;}

    @Log(fieldType = "varchar(100)",  index = "0" , logField = "plat")
    public String getPlat(){return plat;}

    public void  setPlat(String plat){this.plat = plat;}

}
