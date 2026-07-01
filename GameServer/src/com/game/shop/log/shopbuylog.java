/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.shop.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 购买物品的日志记录
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class shopbuylog extends BaseLogBean {

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    private static final Logger logger = LogManager.getLogger("shopbuylog");

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    private int shopId;
    private int sellId;
    private int itemModelId;
    private int buyTimes;//购买的多少手
    private int realNum;//购买的实际数量值
    private long action;
    private int moneyType;
    private long moneyNum;
    private String platfrom;
    private long roleId;
    private String roleName;
    private long userId;
    private int rolelevel;
    private int sid;
    private int reason;//购买原由
    private int srcId;//来源目标记录

    @Log(logField = "shopId", fieldType = "int", index = "0")
    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    @Log(logField = "sellId", fieldType = "int", index = "0")
    public int getSellId() {
        return sellId;
    }

    public void setSellId(int sellId) {
        this.sellId = sellId;
    }

    @Log(logField = "itemModelId", fieldType = "int", index = "0")
    public int getItemModelId() {
        return itemModelId;
    }

    public void setItemModelId(int itemModelId) {
        this.itemModelId = itemModelId;
    }

    @Log(logField = "realNum", fieldType = "int", index = "0")
    public int getRealNum() {
        return realNum;
    }

    public void setRealNum(int realNum) {
        this.realNum = realNum;
    }

    @Log(logField = "action", fieldType = "bigint", index = "0")
    public long getAction() {
        return action;
    }

    public void setAction(long action) {
        this.action = action;
    }

    @Log(logField = "moneyType", fieldType = "int", index = "0")
    public int getMoneyType() {
        return moneyType;
    }

    public void setMoneyType(int moneyType) {
        this.moneyType = moneyType;
    }

    @Log(logField = "moneyNum", fieldType = "bigint", index = "0")
    public long getMoneyNum() {
        return moneyNum;
    }

    public void setMoneyNum(long moneyNum) {
        this.moneyNum = moneyNum;
    }

    @Log(logField = "platformName", fieldType = "varchar(100)", index = "4")
    public String getPlatfrom() {
        return platfrom;
    }

    public void setPlatfrom(String platfrom) {
        this.platfrom = platfrom;
    }

    @Log(logField = "roleId", fieldType = "bigint", index = "3")
    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    @Log(logField = "userId", fieldType = "bigint", index = "2")
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Log(logField = "rolelevel", fieldType = "int", index = "0")
    public int getRolelevel() {
        return rolelevel;
    }

    public void setRolelevel(int rolelevel) {
        this.rolelevel = rolelevel;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "buyTimes", fieldType = "int", index = "0")
    public int getBuyTimes() {
        return buyTimes;
    }

    public void setBuyTimes(int buyTimes) {
        this.buyTimes = buyTimes;
    }

    @Log(logField = "FromSrc", fieldType = "int", index = "0")
    public int getSrcId() {
        return srcId;
    }

    public void setSrcId(int srcId) {
        this.srcId = srcId;
    }

    @Log(logField = "reasonCode", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }
    
    @Log(logField = "roleName", fieldType = "varchar(100)", index = "0")
    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    

}
