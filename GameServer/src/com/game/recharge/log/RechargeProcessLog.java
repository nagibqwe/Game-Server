/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.recharge.log;

import com.game.db.DBErrorToFile;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 *
 * @author hewei@haowan123.com
 */
public class RechargeProcessLog extends CommonLogBean {
    public String orderId;                  //订单id
    public String amount = "";              //支付金额
    public String currency = "";            //币种 RMB,KER,USD,TWD,VND
    public int gold;                        //充值获得的元宝数
    public int gift;                        //赠送的绑定元宝
    public int type;
    public long actionId;                   //关联id
    public String parm = "";                //额外的数据
    private int reason;//理由编码

    @Log(logField = "orderId", fieldType = "varchar(64)", index = "0")
    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Log(logField = "amount", fieldType = "varchar(32)", index = "0")
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Log(logField = "currency", fieldType = "varchar(16)", index = "0")
    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    @Log(logField = "gold", fieldType = "int", index = "0")
    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    @Log(logField = "gift", fieldType = "int", index = "0")
    public int getGift() {
        return gift;
    }

    public void setGift(int gift) {
        this.gift = gift;
    }

    @Log(logField = "type", fieldType = "tinyint", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(logField = "parm", fieldType = "varchar(128)", index = "0")
    public String getParm() {
        return parm;
    }

    public void setParm(String parm) {
        this.parm = parm;
    }

    @Log(logField = "reason", fieldType = "int", index = "0")
    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        DBErrorToFile.error(buildSql());
    }

}
