package com.game.newfashion.structs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by cxl on 2020/3/23.
 */
public class PlayerNewFashion {

    private int bodyID = 0;

    private int weaponID = 0;

    private int wingId = 0;//翅膀穿戴ID


    //key类型 = type
    private HashMap<Integer,FashionData> wearDatas = new HashMap<>();

    //激活的时装 KEY 时装ID
    private HashMap<Integer,FashionData> activtyFsDatas = new HashMap<>();

    //激活的图鉴   key = id value = star
    private HashMap<Integer,Integer> actityTjDatas = new HashMap<>();

    public int getBodyID() {
        return bodyID;
    }

    public void setBodyID(int bodyID) {
        this.bodyID = bodyID;
    }

    public int getWeaponID() {
        return weaponID;
    }

    public void setWeaponID(int weaponID) {
        this.weaponID = weaponID;
    }

    public int getWingId() {
        return wingId;
    }

    public void setWingId(int wingId) {
        this.wingId = wingId;
    }

    public HashMap<Integer, FashionData> getWearDatas() {
        return wearDatas;
    }

    public void setWearDatas(HashMap<Integer, FashionData> wearDatas) {
        this.wearDatas = wearDatas;
    }

    public HashMap<Integer, FashionData> getActivtyFsDatas() {
        return activtyFsDatas;
    }

    public void setActivtyFsDatas(HashMap<Integer, FashionData> activtyFsDatas) {
        this.activtyFsDatas = activtyFsDatas;
    }

    public HashMap<Integer, Integer> getActityTjDatas() {
        return actityTjDatas;
    }

    public void setActityTjDatas(HashMap<Integer, Integer> actityTjDatas) {
        this.actityTjDatas = actityTjDatas;
    }
}
