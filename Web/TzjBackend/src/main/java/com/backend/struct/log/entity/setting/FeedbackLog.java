package com.backend.struct.log.entity.setting;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;
/**
 * 反馈日志
 */
@Table(name = "feedbacklog", tableType = TableType.Month)
public class FeedbackLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private long sender;            //发送者,0自己,1系统

    @FieldDesc
    private int type;               //反馈类型

    @FieldDesc
    private long sendTime;          //发送时间

    @FieldDesc
    private String content;         //内容

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getSender() {
        return sender;
    }

    public void setSender(long sender) {
        this.sender = sender;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
