/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.recharge.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * 月卡增加时的日志
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class MoonCardLog extends CommonLogBean {

    private String rechargeItemId; //充值商品ID
    private int prevValue; //之前的天数
    private int curValue; //当前天数
    private int nextValue; //到期天数
    private int gold; //充值元宝数
    private int actType; //操作类型，1：月卡，2：终身特权卡
    private long actionId; 

    @Log(fieldType = "varchar(100)", index = "0", logField = "rechargeItemId")
    public String getRechargeItemId() {
        return rechargeItemId;
    }

    public void setRechargeItemId(String rechargeItemId) {
        this.rechargeItemId = rechargeItemId;
    }

    @Log(fieldType = "int", index = "0", logField = "prevValue")
    public int getPrevValue() {
        return prevValue;
    }

    public void setPrevValue(int prevValue) {
        this.prevValue = prevValue;
    }

    @Log(fieldType = "int", index = "0", logField = "curValue")
    public int getCurValue() {
        return curValue;
    }

    public void setCurValue(int curValue) {
        this.curValue = curValue;
    }

    @Log(fieldType = "int", index = "0", logField = "nextValue")
    public int getNextValue() {
        return nextValue;
    }

    public void setNextValue(int nextValue) {
        this.nextValue = nextValue;
    }

    @Log(fieldType = "int", index = "0", logField = "gold")
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Log(fieldType = "int", index = "0", logField = "actType")
    public int getActType() {
        return actType;
    }

    public void setActType(int actType) {
        this.actType = actType;
    }

    @Log(fieldType = "bigint", index = "0", logField = "actionId")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
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
