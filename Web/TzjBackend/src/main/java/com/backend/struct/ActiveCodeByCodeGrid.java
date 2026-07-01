package com.backend.struct;

public class ActiveCodeByCodeGrid {
    private String id;
    /**
     * 激活码
     */
    private String activeCode;
    /**
     * 平台名
     */
    private String platformName;
    /**
     * 区服
     */
    private String sid;
    /**
     * 角色id
     */
    private String roleId;
    /**
     * 账号id
     */
    private String userId;
    /**
     * 物品列表
     */
    private String itemList;
    /**
     * 唯一标识
     */
    private String actionId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActiveCode() {
        return activeCode;
    }

    public void setActiveCode(String activeCode) {
        this.activeCode = activeCode;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getItemList() {
        return itemList;
    }

    public void setItemList(String itemList) {
        this.itemList = itemList;
    }

    public String getActionId() {
        return actionId;
    }

    public void setActionId(String actionId) {
        this.actionId = actionId;
    }
}
