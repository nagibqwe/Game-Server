package com.game.mail.log;

import game.core.dblog.TableCheckStepEnum;
import game.core.dblog.base.Log;
import game.core.dblog.bean.BaseLogBean;

public class MailLog extends BaseLogBean {

    private int mailAction;                          //邮件变化动作，1：邮件收取，2：邮件读取，3：邮件附件领取，4：玩家一键删除邮件，5，邮件到期系统删除
    private int type;                                //邮件类型，1：系统，2：后台
    private long mailId;                             //邮件唯一Id
    private long receiveTime;                        //邮件收到时间，单位ms
    private String sender;                           //邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)
    private String mailTitle;                        //邮件标题
    private String mailContend;                      //邮件内容
    private long receiverId;                         //邮件收件人角色Id
    private String receiverName;                     //邮件收件人角色名
    private int receiverSid;                         //邮件收件人创建区服
    private byte isRead;                             //是否已读，0：未读，1：已读
    private byte hasAttachment;                      //是否有附件，0：无，1：有
    private String attachment;                       //JSON化后的字符串格式的附件物品列表(包括货币在内)
    private byte isAttachReceived;                   //有附件时附件是否已领取
    private int notBindGoldNum;                      //邮件附件中非绑元宝数量(没有附件、或附件中没非绑元宝、或附件被领取了，则此值为0)
    private long actionId;                           //行为ID值
    private int messageStringId;                     //是否是多语言 0 不是多语言 1是多语言，需要翻译成本地语种
    /**
     * 邮件来源 {@link com.data.ItemChangeReason}
     */
    private int source;                              //邮件物品来源

    @Override
    public TableCheckStepEnum getRollingStep() {
        return TableCheckStepEnum.MONTH;
    }

    @Override
    public void logToFile() {
        logger.error(buildSql());
    }

    @Log(logField = "mailAction", fieldType = "int", index = "0")
    public int getMailAction() {
        return mailAction;
    }

    public void setMailAction(int mailAction) {
        this.mailAction = mailAction;
    }

    @Log(logField = "type", fieldType = "int", index = "0")
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Log(logField = "mailId", fieldType = "bigint", index = "0")
    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    @Log(logField = "receiveTime", fieldType = "bigint", index = "0")
    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Log(logField = "sender", fieldType = "varchar(512)", index = "0")
    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    @Log(logField = "mailTitle", fieldType = "TEXT", index = "0")
    public String getMailTitle() {
        return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
        this.mailTitle = mailTitle;
    }

    @Log(logField = "mailContend", fieldType = "LONGTEXT", index = "0")
    public String getMailContend() {
        return mailContend;
    }

    public void setMailContend(String mailContend) {
        this.mailContend = mailContend;
    }

    @Log(logField = "receiverId", fieldType = "bigint", index = "2")
    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    @Log(logField = "receiverName", fieldType = "varchar(512)", index = "0")
    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    @Log(logField = "receiverSid", fieldType = "int", index = "0")
    public int getReceiverSid() {
        return receiverSid;
    }

    public void setReceiverSid(int receiverSid) {
        this.receiverSid = receiverSid;
    }

    @Log(logField = "isRead", fieldType = "tinyint", index = "0")
    public byte getIsRead() {
        return isRead;
    }

    public void setIsRead(byte isRead) {
        this.isRead = isRead;
    }

    @Log(logField = "hasAttachment", fieldType = "tinyint", index = "0")
    public byte getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(byte hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    @Log(logField = "attachment", fieldType = "TEXT", index = "0")
    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    @Log(logField = "isAttachReceived", fieldType = "tinyint", index = "0")
    public byte getIsAttachReceived() {
        return isAttachReceived;
    }

    public void setIsAttachReceived(byte isAttachReceived) {
        this.isAttachReceived = isAttachReceived;
    }

    @Log(logField = "notBindGoldNum", fieldType = "int", index = "0")
    public int getNotBindGoldNum() {
        return notBindGoldNum;
    }

    public void setNotBindGoldNum(int notBindGoldNum) {
        this.notBindGoldNum = notBindGoldNum;
    }

    @Log(logField = "actionId", fieldType = "bigint", index = "0")
    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    @Log(logField = "messageStringId", fieldType = "int", index = "0")
    public int getMessageStringId() {
        return messageStringId;
    }

    public void setMessageStringId(int messageStringId) {
        this.messageStringId = messageStringId;
    }

    /**
     * 邮件来源 {@link com.data.ItemChangeReason}
     */
    @Log(logField = "source", fieldType = "int", index = "0")
    public int getSource() {
        return source;
    }

    /**
     * 邮件来源 {@link com.data.ItemChangeReason}
     */
    public void setSource(int source) {
        this.source = source;
    }
}
