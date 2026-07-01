package com.backend.struct.log.entity.mail;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;
import com.backend.utils.LogUtil;
import com.backend.manager.ItemManager;
import com.backend.utils.JsonUtils;
import com.backend.utils.TypeReference;
import org.nutz.lang.Strings;

import java.util.*;

/**
 * 邮件日志
 */
@Table(name = "maillog", tableType = TableType.Month)
public class MailLog implements IConvertor {

    @FieldDesc(selectKey = true)
    private int mailAction;                          //邮件变化动作，1：邮件收取，2：邮件读取，3：邮件附件领取，4：玩家一键删除邮件，5，邮件到期系统删除

    @FieldDesc
    private int type;                                //邮件类型，1：系统，2：后台

    @FieldDesc
    private long mailId;                             //邮件唯一Id

    @FieldDesc
    private long receiveTime;                        //邮件收到时间，单位ms

    @FieldDesc
    private String sender;                           //邮件发件人，1：系统(集市、某副本、商城等)，2：后台(后台发件人)

    @FieldDesc(selectKey = true)
    private String mailTitle;                        //邮件标题

    @FieldDesc
    private String mailContend;                      //邮件内容

    @FieldDesc(selectKey = true)
    private long receiverId;                         //邮件收件人角色Id

    @FieldDesc(selectKey = true)
    private String receiverName;                     //邮件收件人角色名

    @FieldDesc(show = false)
    private int receiverSid;                         //邮件收件人创建区服

    @FieldDesc(show = false)
    private byte isRead;                             //是否已读，0：未读，1：已读

    @FieldDesc(show = false)
    private byte hasAttachment;                      //是否有附件，0：无，1：有

    @FieldDesc
    private String attachment;                       //JSON化后的字符串格式的附件物品列表(包括货币在内)

    @FieldDesc(show = false)
    private byte isAttachReceived;                   //有附件时附件是否已领取

    @FieldDesc(show = false)
    private int notBindGoldNum;                      //邮件附件中非绑元宝数量(没有附件、或附件中没非绑元宝、或附件被领取了，则此值为0)

    @FieldDesc(show = false)
    private long actionId;                           //行为ID值

    @FieldDesc(show = false)
    private int messageStringId;                     //是否是多语言 0 不是多语言 1是多语言，需要翻译成本地语种

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        String mailAction = data.get("mailAction");
        String action = "";
        if (!Strings.isBlank(mailAction)) {
            switch (mailAction) {
                case "1":
                    action = "邮件收取";
                    break;
                case "2":
                    action = "邮件读取";
                    break;
                case "3":
                    action = "邮件附件领取";
                    break;
                case "4":
                    action = "玩家一键删除邮件";
                    break;
                case "5":
                    action = "邮件到期系统删除";
                    break;
            }
            action += "[" + mailAction + "]";
            data.put("mailAction", action);
        }
        String receiveTime = data.get("receiveTime");
        long time = Long.valueOf(receiveTime);
        data.put("receiveTime", LogUtil.sdfhms.format(new Date(time)));
        StringBuilder items = new StringBuilder("[");
        String attachment = data.get("attachment");
        List<HashMap<String, Object>> array = JsonUtils.parseObject(attachment, new TypeReference<ArrayList<HashMap<String, Object>>>() {});
        if(array.size()>0) {
            for (HashMap<String, Object> object : array) {

                String itemName = ItemManager.getInstance().getItemName(Integer.valueOf(object.get("itemModelId").toString()));
                items.append(itemName).append("*").append(object.get("num").toString()).append(";");
            }
        }
        items.append("]");
        data.put("attachment",items.toString());
        return data;
    }

    public int getMailAction() {
        return mailAction;
    }

    public void setMailAction(int mailAction) {
        this.mailAction = mailAction;
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

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public int getReceiverSid() {
        return receiverSid;
    }

    public void setReceiverSid(int receiverSid) {
        this.receiverSid = receiverSid;
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

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
    }

    public byte getIsAttachReceived() {
        return isAttachReceived;
    }

    public void setIsAttachReceived(byte isAttachReceived) {
        this.isAttachReceived = isAttachReceived;
    }

    public int getNotBindGoldNum() {
        return notBindGoldNum;
    }

    public void setNotBindGoldNum(int notBindGoldNum) {
        this.notBindGoldNum = notBindGoldNum;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public int getMessageStringId() {
        return messageStringId;
    }

    public void setMessageStringId(int messageStringId) {
        this.messageStringId = messageStringId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
