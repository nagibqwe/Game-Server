/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author hewei@haowan123.com
 */
public class LogUser extends BaseLogBean{
    
    private static final Logger logger = LogManager.getLogger("LoginDBLog");

    private long userId; // 游戏生成的账号id
    private String userName; // 553平台生成的账号ID
    private String platformAccount; // 平台生成的账号
    private String platformName; // 平台名
    private long createTime;//创建时间
    private String lastLoginIp;//上次登录ip
    private String machineCode;//机器唯一码,客户端生成的uuid，即使删除了游戏还是不变的，必须不为空
    private String imei;
    private String mac;

    /**
     * get 游戏生成的账号id
     *
     * @return
     */
    @Log(logField = "userid", fieldType = "bigint", index = "0")
    public long getUserId() {
        return userId;
    }

    /**
     * set 游戏生成的账号id
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * get 553平台生成的账号ID
     *
     * @return
     */
    @Log(logField = "userName", fieldType = "varchar(64)", index = "0")
    public String getUserName() {
        return userName;
    }

    /**
     * set 553平台生成的账号ID
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * get 平台生成的账号
     *
     * @return
     */
    @Log(logField = "platformAccount", fieldType = "varchar(64)", index = "0")
    public String getPlatformAccount() {
        return platformAccount;
    }

    /**
     * set 平台生成的账号
     */
    public void setPlatformAccount(String platformAccount) {
        this.platformAccount = platformAccount;
    }

    /**
     * get 平台名
     *
     * @return
     */
    @Log(logField = "platformName", fieldType = "varchar(16)", index = "0")
    public String getPlatformName() {
        return platformName;
    }

    /**
     * set 平台名
     */
    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    /**
     * get 创建时间
     *
     * @return
     */
    @Log(logField = "createTime", fieldType = "int", index = "0")
    public long getCreateTime() {
        return createTime;
    }

    /**
     * set 创建时间
     */
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    
    /**
     * get 上次登录ip
     *
     * @return
     */
    @Log(logField = "lastLoginIp", fieldType = "varchar(32)", index = "0")
    public String getLastLoginIp() {
        return lastLoginIp;
    }

    /**
     * set 上次登录ip
     */
    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    @Log(logField = "machineCode", fieldType = "varchar(64)", index = "0")
    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    @Log(logField = "imei", fieldType = "varchar(64)", index = "0")
    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    @Log(logField = "mac", fieldType = "varchar(64)", index = "0")
    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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
