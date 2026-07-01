package com.game.player.structs;

public class PlayerCommunityInfoSettingInfo {
    //社区信息相关
    private int decorate; // 装饰
    private int pendan; //挂件
    private String sign; // 个性签名
    private String brith;//生日
    private boolean isNotFriendLeaveMsg ;//是否允许非好友留言

    public int getDecorate() {
        return decorate;
    }

    public void setDecorate(int decorate) {
        this.decorate = decorate;
    }

    public int getPendan() {
        return pendan;
    }

    public void setPendan(int pendan) {
        this.pendan = pendan;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getBrith() {
        return brith;
    }

    public void setBrith(String brith) {
        this.brith = brith;
    }

    public boolean isNotFriendLeaveMsg() {
        return isNotFriendLeaveMsg;
    }

    public void setNotFriendLeaveMsg(boolean notFriendLeaveMsg) {
        isNotFriendLeaveMsg = notFriendLeaveMsg;
    }
}
