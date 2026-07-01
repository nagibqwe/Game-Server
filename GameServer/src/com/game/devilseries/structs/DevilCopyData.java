package com.game.devilseries.structs;

/**
 * 除魔团副本数据
 * Created by cxl on 2021/5/19.
 */
public class DevilCopyData {

    private String headName;//团长名

    private long endTime;//结束时间

    private long mapId;//唯一实列ID

    private int cloneId;//副本ID

    private long roleId ;//角色id

    private int career ;//职业

    private int fashionHead;//时装头像

    private int  fashionFrame;//时装头像框

    private String customHeadPath; //自定义头像路径
    private boolean useCustomHead; // 是否使用自定义头像 1 表示使用

    public String getHeadName() {
        return headName;
    }

    public void setHeadName(String headName) {
        this.headName = headName;
    }

    public long getEndTime() {return endTime;}

    public void setEndTime(long endTime) {this.endTime = endTime;}

    public long getMapId() {
        return mapId;
    }

    public void setMapId(long mapId) {
        this.mapId = mapId;
    }

    public int getCloneId() {
        return cloneId;
    }

    public void setCloneId(int cloneId) {
        this.cloneId = cloneId;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getFashionHead() {
        return fashionHead;
    }

    public void setFashionHead(int fashionHead) {
        this.fashionHead = fashionHead;
    }

    public int getFashionFrame() {
        return fashionFrame;
    }

    public void setFashionFrame(int fashionFrame) {
        this.fashionFrame = fashionFrame;
    }

    public String getCustomHeadPath() {
        return customHeadPath;
    }

    public void setCustomHeadPath(String customHeadPath) {
        this.customHeadPath = customHeadPath;
    }

    public boolean isUseCustomHead() {
        return useCustomHead;
    }

    public void setUseCustomHead(boolean useCustomHead) {
        this.useCustomHead = useCustomHead;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }
}
