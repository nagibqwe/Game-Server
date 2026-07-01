package com.backend.struct.log.entity.guild;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

@Table(name = "guildbaselog", tableType = TableType.Year)
public class GuildBaseLog implements IConvertor {

    @FieldDesc(selectKey = true)
    private long guildId;       //公会Id

    @FieldDesc
    private String guildName;   //公会名称

    @FieldDesc
    private byte type;          //操作类型 1:公会创建2:公会解散3:加入公会4:主动退会5:被踢出公会6：公会信息设置7：公会改名8：弹劾会长

    @FieldDesc
    private long actId;         //被操作玩家

    @FieldDesc
    private String actName;     //被操作玩家名称

    @FieldDesc
    private String param;       //其他参数

    @FieldDesc
    private long actionId;      //关联其他日志Id

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public long getActId() {
        return actId;
    }

    public void setActId(long actId) {
        this.actId = actId;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }
}
