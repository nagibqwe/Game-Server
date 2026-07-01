package com.game.couplefight.structs;

/**
 * 粉丝数据
 * @Auther: gouzhongliang
 * @Date: 2021/7/21 16:48
 */
public class Fans {

    /**玩家id*/
    private long id;
    /**姓名*/
    private String name;
    /**等级*/
    private int level;
    /**战力*/
    private long power;
    /**钱*/
    private int money;
    /**排名*/
    private int rank;
    /**服务器平台*/
    private String serverKey;

    public Fans(){}

    public Fans(long rid, String name, int level, long power, String serverKey){
        this.id = rid;
        this.name = name;
        this.level = level;
        this.power = power;
        this.serverKey = serverKey;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getPower() {
        return power;
    }

    public void setPower(long power) {
        this.power = power;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public void update(Fans obj) {
        this.name = obj.getName();
        this.level = obj.getLevel();
        this.power = obj.getPower();
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    @Override
    public String toString() {
        return "Fans{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", power=" + power +
                ", money=" + money +
                ", rank=" + rank +
                ", serverKey='" + serverKey + '\'' +
                '}';
    }
}
