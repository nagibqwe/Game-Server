/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.activity.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * @author hewei@haowan123.com
 */
public class ActivityDonateLog extends BaseLogBean {

    private long sendId;    //赠送方角色id
    private long receiveId; //收取方角色id
    private int itemModelid;//赠送物品id
    private int num;        //赠送数量
    private int sid;        //服务器id
    private long actionId;  //关联id

    @Log(logField = "sendId", fieldType = "bigint", index = "0")
    public long getSendId() {
        return sendId;
    }

    public void setSendId(long sendId) {
        this.sendId = sendId;
    }

    @Log(logField = "receiveId", fieldType = "bigint", index = "0")
    public long getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(long receiveId) {
        this.receiveId = receiveId;
    }

    @Log(logField = "itemModelid", fieldType = "int", index = "0")
    public int getItemModelid() {
        return itemModelid;
    }

    public void setItemModelid(int itemModelid) {
        this.itemModelid = itemModelid;
    }

    @Log(logField = "num", fieldType = "int", index = "0")
    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    @Log(logField = "sid", fieldType = "int", index = "0")
    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    private static final Logger logger = LogManager.getLogger("ActivityDonateLog");
}
