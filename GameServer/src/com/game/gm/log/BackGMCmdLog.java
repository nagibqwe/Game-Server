package com.game.gm.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * Created by 瞿冰冰
 * 2019/7/9
 */
public class BackGMCmdLog  extends BaseLogBean {

    private String backUser;        //后台用户
    private String cmd;             //指令
    private String result;          //执行结果

    @Log(logField = "backUser", fieldType = "varchar(50)", index = "0")
    public String getBackUser() {
        return backUser;
    }

    public void setBackUser(String backUser) {
        this.backUser = backUser;
    }

    @Log(logField = "cmd", fieldType = "LONGTEXT", index = "0")
    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    @Log(logField = "result", fieldType = "text", index = "0")
    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
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
