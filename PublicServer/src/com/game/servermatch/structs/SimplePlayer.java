/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.servermatch.structs;

/**
 * 雕像数据,玩家展示等
 * @author zhaibiao
 */
public class SimplePlayer {
    private String name = "";
    private int career; //职业
    private int degree; //阶
    private int wingId; //翅膀ID
    private int clothesEquipId;//衣服装备ID
    private int weaponsEquipId;//武器装备Id
    private int clothesStar;//衣服部位的星级；
    private int weaponStar;//武器部位的星级
    private long id ;//关联的角色ID
    private long guildId;//公会Id
    private String guildName;//公会名称
    private int level;//玩家等级
    private int vip;//玩家vip等级
    private int fashionBodyId;
    private int fashionStar;
    private int fashionWeaponId;
    private int godHeadId;
    private int textTitleId;
    
    private int cityId;//    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCareer() {
        return career;
    }

    public void setCareer(int career) {
        this.career = career;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getClothesEquipId() {
        return clothesEquipId;
    }

    public void setClothesEquipId(int clothesEquipId) {
        this.clothesEquipId = clothesEquipId;
    }

    public int getWeaponsEquipId() {
        return weaponsEquipId;
    }

    public void setWeaponsEquipId(int weaponsEquipId) {
        this.weaponsEquipId = weaponsEquipId;
    }

    public int getClothesStar() {
        return clothesStar;
    }

    public void setClothesStar(int clothesStar) {
        this.clothesStar = clothesStar;
    }

    public int getWeaponStar() {
        return weaponStar;
    }

    public void setWeaponStar(int weaponStar) {
        this.weaponStar = weaponStar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public String getGuildName() {
        return guildName;
    }

    public void setGuildName(String guildName) {
        this.guildName = guildName;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getVip() {
        return vip;
    }

    public void setVip(int vip) {
        this.vip = vip;
    }

    public int getFashionBodyId() {
        return fashionBodyId;
    }

    public void setFashionBodyId(int fashionBodyId) {
        this.fashionBodyId = fashionBodyId;
    }

    public int getFashionStar() {
        return fashionStar;
    }

    public void setFashionStar(int fashionStar) {
        this.fashionStar = fashionStar;
    }

    public int getFashionWeaponId() {
        return fashionWeaponId;
    }

    public void setFashionWeaponId(int fashionWeaponId) {
        this.fashionWeaponId = fashionWeaponId;
    }

    public int getGodHeadId() {
        return godHeadId;
    }

    public void setGodHeadId(int godHeadId) {
        this.godHeadId = godHeadId;
    }

    public int getTextTitleId() {
        return textTitleId;
    }

    public void setTextTitleId(int textTitleId) {
        this.textTitleId = textTitleId;
    }
    
    
}
