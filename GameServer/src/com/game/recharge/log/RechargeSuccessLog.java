/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.recharge.log;

import com.game.db.DBErrorToFile;
import com.game.player.structs.PlayerWorldInfo;
import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.CommonLogBean;

/**
 * @author soko <xuchangming@haowan123.com>
 */
public class RechargeSuccessLog extends CommonLogBean {

    public String orderId;                  //订单id
    public String amount = "";              //支付金额
    public String currency = "";            //币种 RMB,KER,USD,TWD,VND
    public int gold;                        //充值获得的元宝数
    public int gift;                        //赠送的绑定元宝
    public int type;
    public long actionId;                   //关联id
    public String parm = "";                //额外的数据
    private int reason;//理由编码
    private String oid;//渠道订单号
    private String os;//操作系统
    private int level;//玩家的等级
    private String uuid;//funcellID 
    private String itemId;//充值的档次名字
    private int isMoon;//是否月卡

    public void setPlayer(PlayerWorldInfo pwi) {
        this.setPlayerInfo(pwi.getPlat(), pwi.getCsid(), pwi.getUserId(), pwi.getRoleid(), pwi.getRolename());
    }

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

    @Log(logField = "oid", fieldType = "varchar(200)", index = "0")
    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Log(logField = "os", fieldType = "varchar(50)", index = "0")
    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.YEAR;
    }

    @Override
    public void logToFile() {
        DBErrorToFile.error(buildSql());
    }

    @Log(logField = "level", fieldType = "int", index = "0")
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Log(logField = "uuid", fieldType = "varchar(100)", index = "0")
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Log(logField = "itemId", fieldType = "varchar(100)", index = "0")
    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    @Log(logField = "isMoon", fieldType = "int", index = "0")
    public int getIsMoon() {
        return isMoon;
    }

    public void setIsMoon(int isMoon) {
        this.isMoon = isMoon;
    }

}
