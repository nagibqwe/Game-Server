package com.backend.struct;

public class RechargeItemLogGrid {

    /**
     * id
     */
    private int id;
    /**
     * 修改人名
     */
    private String userName;
    /**
     * 修改时间
     */
    private String time;
    /**
     * 操作表名
     */
    private String tableName;
    /**
     * 操作内容(详情)
     */
    private String content;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
}
