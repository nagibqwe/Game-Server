package com.backend.struct.log.entity.chat;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 聊天日志
 */
@Table(name = "chatlog", tableType = TableType.Month)
public class ChatLog extends CommonLogBean implements IConvertor {

    @FieldDesc(selectKey = true)
    private int channel;        //发送类型

    @FieldDesc(selectKey = true)
    private long receRoleId;    //接受者id

    @FieldDesc
    private String content;     //内容

    @FieldDesc(selectKey = true)
    private int level;          //等级

    @FieldDesc
    private String ip;          //IP

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public long getReceRoleId() {
        return receRoleId;
    }

    public void setReceRoleId(long receRoleId) {
        this.receRoleId = receRoleId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
