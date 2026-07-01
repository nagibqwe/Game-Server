package com.game.bi.bi4399;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

/**
 * @explain: 该日志记录玩家在游戏升级时的信息。
 * @time Created on 2020/3/26 16:56.
 * @author: cxl
 */
public class tbllog_level_up  extends BaseLogBean {

    // 所属平台，记录SDK platformID_gameID
    private String platform;
    // 设备端：android、ios、web、pc
    private String device;
    // 角色ID
    private long role_id;
    // 平台账号ID
    private String account_name;
    // 角色名字
    private String role_name;
    //上一等级
    private int last_level;
    //当前等级
    private int current_level;
    //上一级经验值
    private long last_exp;
    //当前经验值i
    private long current_exp;
    // 事件发生时间（索引）
    private int happend_time;

    public tbllog_level_up() {}
    public tbllog_level_up(String platform, String device, long role_id, String account_name, String role_name, int last_level, int current_level, long last_exp, long current_exp, int happend_time) {
        this.platform = platform;
        this.device = device;
        this.role_id = role_id;
        this.account_name = account_name;
        this.role_name = role_name;
        this.last_level = last_level;
        this.current_level = current_level;
        this.last_exp = last_exp;
        this.current_exp = current_exp;
        this.happend_time = happend_time;
    }

    @Log(logField = "platform", fieldType = "varchar(100)", index = "0")
    public String getPlatform() {return platform;}

    public void setPlatform(String platform) {this.platform = platform;}

    @Log(logField = "device", fieldType = "varchar(100)", index = "0")
    public String getDevice() {return device;}

    public void setDevice(String device) {
        this.device = device;
    }

    @Log(logField = "role_id", fieldType = "bigint", index = "0")
    public long getRole_id() {
        return role_id;
    }

    public void setRole_id(long role_id) {
        this.role_id = role_id;
    }

    @Log(logField = "account_name", fieldType = "varchar(100)", index = "0")
    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    @Log(logField = "role_name", fieldType = "varchar(100)", index = "0")
    public String getRole_name() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name = role_name;
    }

    @Log(logField = "last_level", fieldType = "int", index = "0")
    public int getLast_level() {
        return last_level;
    }

    public void setLast_level(int last_level) {
        this.last_level = last_level;
    }

    @Log(logField = "current_level", fieldType = "int", index = "0")
    public int getCurrent_level() {
        return current_level;
    }

    public void setCurrent_level(int current_level) {
        this.current_level = current_level;
    }

    @Log(logField = "last_exp", fieldType = "bigint", index = "0")
    public long getLast_exp() {
        return last_exp;
    }

    public void setLast_exp(long last_exp) {
        this.last_exp = last_exp;
    }

    @Log(logField = "current_exp", fieldType = "bigint", index = "0")
    public long getCurrent_exp() {
        return current_exp;
    }

    public void setCurrent_exp(long current_exp) {
        this.current_exp = current_exp;
    }
    @Log(logField = "happend_time", fieldType = "int", index = "1")
    public int getHappend_time() {
        return happend_time;
    }

    public void setHappend_time(int happend_time) {
        this.happend_time = happend_time;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.UNROLL;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }
}
