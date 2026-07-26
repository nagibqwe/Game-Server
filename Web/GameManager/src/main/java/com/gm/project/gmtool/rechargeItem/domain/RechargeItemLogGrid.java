package com.gm.project.gmtool.rechargeItem.domain;

public class RechargeItemLogGrid {

    /**
     * id
     */
    private int id;
    /**
     * Изменить人名
     */
    private String userName;
    /**
     * Время изменения
     */
    private String time;
    /**
     * Действия表名
     */
    private String tableName;
    /**
     * ДействияСодержимое(Подробнее)
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
