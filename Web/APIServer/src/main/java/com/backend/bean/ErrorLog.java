package com.backend.bean;

import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.dao.entity.annotation.TableIndexes;

@Table("t_errorlog")
@TableIndexes(value = {@Index(fields = {"serverId", "receTime"}, name = "s_re", unique = false),
        @Index(fields = {"type", "receTime"}, name = "type_re", unique = false),
        @Index(fields = {"serverId", "type", "mKey", "receTime"}, name = "s_t_k_re", unique = false),
        @Index(fields = {"type", "mKey", "receTime"}, name = "t_k_re", unique = false),
        @Index(fields = {"receTime"}, name = "receTime", unique = false)})
public class ErrorLog {

    @Id
    @Column
    private int id;

    @Column
    @Comment("接收错误日志的时间")
    private String receTime;

    @Column
    @Comment("接收的服务器ID")
    private int serverId;

    @Column
    @Comment("接收的平台编号")
    private String platform;

    @Column
    @Comment("错误类型数字编号")
    private int type;

    @Column
    @Comment("错误关键字")
    @ColDefine(type = ColType.VARCHAR, width = 200)
    private String mKey;

    @Column
    @Comment("错误的具体 信息")
    @ColDefine(type = ColType.TEXT)
    private String content;

    @Column
    @Comment("错误产生的数量")
    private long lastValue;


    @Column
    @Comment("备注，用于记录此条有没有做过其它操作")
    @ColDefine(type = ColType.TEXT)
    private String demo;

    //签名KEy
    private String sign;

    @Column
    @Comment("当前标志位")
    private int state;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReceTime() {
        return receTime;
    }

    public void setReceTime(String receTime) {
        this.receTime = receTime;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getLastValue() {
        return lastValue;
    }

    public void setLastValue(long lastValue) {
        this.lastValue = lastValue;
    }

    public String getDemo() {
        return demo;
    }

    public void setDemo(String demo) {
        this.demo = demo;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

}
 