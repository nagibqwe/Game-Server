package com.backend.bean;

import org.nutz.dao.entity.annotation.*;

//充值配置表操作日志
@Table("t_recharge_item_log")
@TableIndexes(value={@Index(fields={"userId"}, name="userId_index",unique=false)})
public class RechargeItemLog {
    @Id
    private int id;
    /**
     * 修改人id
     */
    @Column
    @Comment("修改人id")
    private int userId;
    /**
     * 修改人IP
     */
    @Column
    @Comment("修改人IP")
    private String ip;
    /**
     * 修改人名
     */
    @Column
    @Comment("修改人名")
    private String userName;
    /**
     * 修改时间
     */
    @Column
    @Comment("修改时间 ")
    private long time;
    /**
     * 操作表名
     */
    @Column
    @Comment("操作表名")
    private String tableName;
    /**
     * 操作内容(详情)
     */
    @Column
    @Comment("操作内容(详情)")
    @ColDefine(customType="LONGTEXT",notNull=false)
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public RechargeItemLog() {
    }

    public RechargeItemLog(User user, String content,String ip,String tableName) {
        super();
        if(user==null){
            this.userId = -1;
            this.userName = "未知用户";
        }else{
            this.userId = user.getId();
            this.userName = user.getName();
        }
        this.content = content;
        this.time = System.currentTimeMillis();
        this.ip = ip;
        this.tableName=tableName;
    }
}
