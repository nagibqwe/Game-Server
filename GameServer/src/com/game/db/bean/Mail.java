/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author Administrator
 */
public class Mail extends BaseBean {

    private int type;                  //邮件类型，1：系统，2：后台 7:公会邮件(主要是1天删除)

    private long mailId;               //邮件唯一Id

    private long receiveTime;          //邮件收到时间，单位ms

    private String sender;             //邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)

    private long receiverId;           //邮件收件人角色Id

    private byte isRead;               //是否已读，0：未读，1：已读

    private byte hasAttachment;        //是否有附件，0：无，1：有

    private byte isAttachReceived;     //附件是否已领取，0：未领取，1：已领取

    private String mailData;           //整个邮件数据(JSON化存储，包含邮件全部信息[邮件标题、内容、附件等会在内])
    
    private int readTable;//是否是参数需要翻译

    private int source;                //邮件来源 com.data.ItemChangeReason

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getMailId() {
        return mailId;
    }

    public void setMailId(long mailId) {
        this.mailId = mailId;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(long receiverId) {
        this.receiverId = receiverId;
    }

    public byte getIsRead() {
        return isRead;
    }

    public void setIsRead(byte isRead) {
        this.isRead = isRead;
    }

    public byte getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(byte hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public byte getIsAttachReceived() {
        return isAttachReceived;
    }

    public void setIsAttachReceived(byte isAttachReceived) {
        this.isAttachReceived = isAttachReceived;
    }

    public String getMailData() {
        return mailData;
    }

    public void setMailData(String mailData) {
        this.mailData = mailData;
    }

    public int getReadTable() {
        return readTable;
    }

    public void setReadTable(int readTable) {
        this.readTable = readTable;
    }

    /**
     * 邮件来源 {@link com.data.ItemChangeReason}
     */
    public int getSource() {
        return source;
    }

    /**
     * 邮件来源 {@link com.data.ItemChangeReason}
     * @param source
     */
    public void setSource(int source) {
        this.source = source;
    }
}
