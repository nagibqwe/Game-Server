package com.game.db.bean;

public class RechargeReturnBean {

    private long userId;                // 游戏生成的账号id
    private long roleId;                // 返还的角色唯一id
    private int  createSid;             // 领取返还的角色创建服
    private int  rechargeTotalMoney;    // 账号删档测试期间充值总数'
    private int  returnGold;            // 返还等量的元宝数
    private long returnTime;            // 领取返还的时间;
    private String userName;            //553平台生成的账号名字;
    private String platformAccount;     //平台生成的账号


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getCreateSid() {
        return createSid;
    }

    public void setCreateSid(int createSid) {
        this.createSid = createSid;
    }

    public int getRechargeTotalMoney() {
        return rechargeTotalMoney;
    }

    public void setRechargeTotalMoney(int rechargeTotalMoney) {
        this.rechargeTotalMoney = rechargeTotalMoney;
    }

    public int getReturnGold() {
        return returnGold;
    }

    public void setReturnGold(int returnGold) {
        this.returnGold = returnGold;
    }

    public long getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(long returnTime) {
        this.returnTime = returnTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPlatformAccount() {
        return platformAccount;
    }

    public void setPlatformAccount(String platformAccount) {
        this.platformAccount = platformAccount;
    }
}
