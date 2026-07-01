package com.game.db.bean;

public class RechargeTotalMoneyBean {

    private long userId; // 游戏生成的账号id

    private int rechargeTotalMoney; //充值

    private String userName;            //553平台生成的账号名字;

    private String platformAccount;     //平台生成的账号


    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public int getRechargeTotalMoney() {
        return rechargeTotalMoney;
    }

    public void setRechargeTotalMoney(int rechargeTotalMoney) {
        this.rechargeTotalMoney = rechargeTotalMoney;
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
