package com.gm.project.gamelog.maillog.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.gm.framework.aspectj.lang.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.gm.framework.web.domain.BaseEntity;


/**
 * ПисьмоЖурнал对象 log_maillog
 * 
 * @author gm
 * @date 2021-09-08
 */
public class Maillog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /**  */
    private Integer id;

    /** ДаНетДа多语言 0 不Да多语言 1Да多语言，需要翻译成本地语种 */
    private Integer messageStringId;

    /** 行为ID值 */
    private Long actionId;

    /** Текст письма */
    @Excel(name = "Текст письма")
    private String mailContend;

    /**  */
    private Integer source;

    public Long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    /** Письмо收到Время */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Письмо收到Время", width = 30, dateFormat = "yyyy-MM-dd")
    private Long receiveTime;

    /** ПисьмоДействияТип */
    @Excel(name = "ПисьмоДействияТип")
    private Integer mailAction;

    /**  */
    private Integer isAttachReceived;

    /** Вложение */
    @Excel(name = "Вложение")
    private String attachment;

    /** Время */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "Время", width = 30, dateFormat = "yyyy-MM-dd")
    private Long time;

    /**  */
    private Integer notBindGoldNum;

    /** 收件人ID */
    @Excel(name = "收件人ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long receiverId;

    /** ПисьмоТип */
    @Excel(name = "ПисьмоТип")
    private Integer type;

    /**  */
    private Integer receiverSid;

    /** Тема письма */
    @Excel(name = "Тема письма")
    private String mailTitle;

    /** 收件人 */
    @Excel(name = "收件人")
    private String receiverName;

    /**  */
    private Integer isRead;

    /** Письмо唯一Id */
    @Excel(name = "Письмо唯一Id")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long mailId;

    /** sender */
    @Excel(name = "sender")
    private String sender;

    /**  */
    private Integer hasAttachment;

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }
    public void setMessageStringId(Integer messageStringId)
    {
        this.messageStringId = messageStringId;
    }

    public Integer getMessageStringId()
    {
        return messageStringId;
    }
    public void setActionId(Long actionId)
    {
        this.actionId = actionId;
    }

    public Long getActionId()
    {
        return actionId;
    }
    public void setMailContend(String mailContend)
    {
        this.mailContend = mailContend;
    }

    public String getMailContend()
    {
        return mailContend;
    }
    public void setSource(Integer source)
    {
        this.source = source;
    }

    public Integer getSource()
    {
        return source;
    }

    public void setMailAction(Integer mailAction)
    {
        this.mailAction = mailAction;
    }

    public Integer getMailAction()
    {
        return mailAction;
    }
    public void setIsAttachReceived(Integer isAttachReceived)
    {
        this.isAttachReceived = isAttachReceived;
    }

    public Integer getIsAttachReceived()
    {
        return isAttachReceived;
    }
    public void setAttachment(String attachment)
    {
        this.attachment = attachment;
    }

    public String getAttachment()
    {
        return attachment;
    }

    public void setNotBindGoldNum(Integer notBindGoldNum)
    {
        this.notBindGoldNum = notBindGoldNum;
    }

    public Integer getNotBindGoldNum()
    {
        return notBindGoldNum;
    }
    public void setReceiverId(Long receiverId)
    {
        this.receiverId = receiverId;
    }

    public Long getReceiverId()
    {
        return receiverId;
    }
    public void setType(Integer type)
    {
        this.type = type;
    }

    public Integer getType()
    {
        return type;
    }
    public void setReceiverSid(Integer receiverSid)
    {
        this.receiverSid = receiverSid;
    }

    public Integer getReceiverSid()
    {
        return receiverSid;
    }
    public void setMailTitle(String mailTitle)
    {
        this.mailTitle = mailTitle;
    }

    public String getMailTitle()
    {
        return mailTitle;
    }
    public void setReceiverName(String receiverName)
    {
        this.receiverName = receiverName;
    }

    public String getReceiverName()
    {
        return receiverName;
    }
    public void setIsRead(Integer isRead)
    {
        this.isRead = isRead;
    }

    public Integer getIsRead()
    {
        return isRead;
    }
    public void setMailId(Long mailId)
    {
        this.mailId = mailId;
    }

    public Long getMailId()
    {
        return mailId;
    }
    public void setSender(String sender)
    {
        this.sender = sender;
    }

    public String getSender()
    {
        return sender;
    }
    public void setHasAttachment(Integer hasAttachment)
    {
        this.hasAttachment = hasAttachment;
    }

    public Integer getHasAttachment()
    {
        return hasAttachment;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("messageStringId", getMessageStringId())
            .append("actionId", getActionId())
            .append("mailContend", getMailContend())
            .append("source", getSource())
            .append("receiveTime", getReceiveTime())
            .append("mailAction", getMailAction())
            .append("isAttachReceived", getIsAttachReceived())
            .append("attachment", getAttachment())
            .append("time", getTime())
            .append("notBindGoldNum", getNotBindGoldNum())
            .append("receiverId", getReceiverId())
            .append("type", getType())
            .append("receiverSid", getReceiverSid())
            .append("mailTitle", getMailTitle())
            .append("receiverName", getReceiverName())
            .append("isRead", getIsRead())
            .append("mailId", getMailId())
            .append("sender", getSender())
            .append("hasAttachment", getHasAttachment())
            .toString();
    }
}
