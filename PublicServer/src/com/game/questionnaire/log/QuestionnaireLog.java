package com.game.questionnaire.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class QuestionnaireLog extends BaseLogBean {

    private static final Logger log = LogManager.getLogger(QuestionnaireLog.class);

    private long userId;        //用户id

    private long roleId;        //角色id

    private String data;        //答题数据

    @Log(fieldType = "bigint", index = "0", logField = "userId")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(fieldType = "bigint", index = "0", logField = "roleId")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(fieldType = "varchar(1024)", index = "0", logField = "data")
    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        log.error(buildSql());
    }
}
