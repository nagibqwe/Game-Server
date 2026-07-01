package com.game.ninedaysfocused.structs;

/**
 * Created by 542 on 2019/7/23.
 */
public class NineDaysCrossPlayer {

    private String plat;                //平台标识
    private int sid;                    //服务器编号
    public long roleID = 0;
    private String serverName; 		//服务器名字
    private int career; 		//职业
    private int degree; 		//转职阶位
    private int lv; 			//转职等级
    private int fightPoint;		//战斗力
    private int weaponId;		//骑兵id
    private int wingId;			//翅膀id
    private int equipMinStar;		//穿戴装备最低星级
    private int fashionBodyId;		//时装身体ID
    private int fashionWeaponId;	//时装武器ID
    private String name; 		//角色名


    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public void setRoleID(long roleID){this.roleID = roleID;}

    public long getRoleID(){return  roleID;}

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

    public int getLv() {
        return lv;
    }

    public void setLv(int lv) {
        this.lv = lv;
    }

    public int getFightPoint() {
        return fightPoint;
    }

    public void setFightPoint(int fightPoint) {
        this.fightPoint = fightPoint;
    }

    public int getWeaponId() {
        return weaponId;
    }

    public void setWeaponId(int weaponId) {
        this.weaponId = weaponId;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public int getEquipMinStar() {
        return equipMinStar;
    }

    public void setEquipMinStar(int equipMinStar) {
        this.equipMinStar = equipMinStar;
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

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
