/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.mail.structs;

import com.game.backpack.structs.Item;
import com.game.structs.GameObject;
import java.util.ArrayList;
import java.util.List;

public class  MailData extends GameObject implements Cloneable, Comparable<MailData> {
    
    private transient int saveType;                  //邮件保存类型，insert、update、delete
    
    private int type;                                //邮件类型，1：系统，2：后台，3，交易行(集市)
    
    private long mailId;                             //邮件唯一Id

    private long receiveTime;                        //邮件收到时间，单位ms
    
    private String sender;                           //邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)
    
    private String mailTitle;                        //邮件标题
    
    private String mailContend;                      //邮件内容
    
    private long receiverId;                         //邮件收件人角色Id
    
    private byte isRead;                             //是否已读，0：未读，1：已读
    
    private byte hasAttachment;                      //是否有附件，0：无，1：有
    
    private List<Item> itemList = new ArrayList<>(); //附件物品列表(包括货币在内)
    
    private byte isAttachReceived;                   //附件是否已领取，0：未领取，1：已领取
    
    private int readTable;                      //是否是语言包邮件 0 为非语言包， 1是需要读取语言包

    private int source;                         //物品来源
            
    public int getSaveType() {
	return saveType;
    }

    public void setSaveType(int saveType) {
	this.saveType = saveType;
    }
    
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

    public String getMailTitle() {
	return mailTitle;
    }

    public void setMailTitle(String mailTitle) {
	this.mailTitle = mailTitle;
    }

    public String getMailContend() {
	return mailContend;
    }

    public void setMailContend(String mailContend) {
	this.mailContend = mailContend;
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

    public List<Item> getItemList() {
	return itemList;
    }

    public void setItemList(List<Item> itemList) {
	this.itemList = itemList;
    }

    public byte getIsAttachReceived() {
	return isAttachReceived;
    }

    public void setIsAttachReceived(byte isAttachReceived) {
	this.isAttachReceived = isAttachReceived;
    }

    public int getReadTable() {
        return readTable;
    }

    public void setReadTable(int readTable) {
        this.readTable = readTable;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    @Override
    public MailData clone() throws CloneNotSupportedException {
	    return (MailData) super.clone();
    }

    @Override
    public int compareTo(MailData mail) {
        if (this.receiveTime < mail.getReceiveTime()) {
            return -1;
        }
        if (this.receiveTime > mail.getReceiveTime()) {
            return 1;
        }
        return 0;
    }

    @Override
    public void release() {

    }
}
