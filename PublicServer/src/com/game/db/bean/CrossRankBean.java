package com.game.db.bean;

/**
 * Created by CXL on 2020/4/9.
 */
public class CrossRankBean {

    private long roleId;

    private String roleName ;

    private int serverId ;

    private int career;

    private int stateVip;

    private int level;

    private long fightPower;

    private int fashionBodyId;

    private int fashionWeaponId;

    private int wingModel;

    private int fashionHalo  ;              //时装光环

    private int fashionMatrix;              //时装阵法

    private int spiritId;                   //灵体外观

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getServerId() {
        return serverId;
    }

    public void setServerId(int serverId) {
        this.serverId = serverId;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getStateVip() {
        return stateVip;
    }

    public void setStateVip(int stateVip) {
        this.stateVip = stateVip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public long getFightPower() {
        return fightPower;
    }

    public void setFightPower(long fightPower) {
        this.fightPower = fightPower;
    }

    public int getFashionBodyId() {
        return fashionBodyId;
    }

    public void setFashionBodyId(int fashionBodyId) {
        this.fashionBodyId = fashionBodyId;
    }

    public int getFashionWeaponId() {
        return fashionWeaponId;
    }

    public void setFashionWeaponId(int fashionWeaponId) {
        this.fashionWeaponId = fashionWeaponId;
    }

    public int getWingModel() {
        return wingModel;
    }

    public void setWingModel(int wingModel) {
        this.wingModel = wingModel;
    }

    public int getFashionHalo() {
        return fashionHalo;
    }

    public void setFashionHalo(int fashionHalo) {
        this.fashionHalo = fashionHalo;
    }

    public int getFashionMatrix() {
        return fashionMatrix;
    }

    public void setFashionMatrix(int fashionMatrix) {
        this.fashionMatrix = fashionMatrix;
    }

    public int getSpiritId() {
        return spiritId;
    }

    public void setSpiritId(int spiritId) {
        this.spiritId = spiritId;
    }
}
