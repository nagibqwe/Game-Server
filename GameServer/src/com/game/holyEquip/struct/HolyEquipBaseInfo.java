package com.game.holyEquip.struct;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/10/24.
 */
public class HolyEquipBaseInfo {


    private boolean isAutoResolve = true;//默认值

    private int quilty = 3;//默认值

    private int grade = 3;//默认值


    //圣装背包
    private ConcurrentHashMap<Integer,Integer> holySoulInfoList = new ConcurrentHashMap<>();

    //圣魂信息
    private ConcurrentHashMap<Long,HolyEquipItem> holyEquipItemList = new ConcurrentHashMap<>();

    //部位信息
    private ConcurrentHashMap<Integer,HolyEquipPart> holyEquipPartList = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer,Integer> getHolySoulInfoList(){return holySoulInfoList;}

    public void setHolySoulInfoList( ConcurrentHashMap<Integer,Integer> holySoulInfoList){
        this.holySoulInfoList = holySoulInfoList;
    }
    public ConcurrentHashMap<Long,HolyEquipItem> getHolyEquipItemList(){return holyEquipItemList;}

    public void setHolyEquipItemList( ConcurrentHashMap<Long,HolyEquipItem> holyEquipItemList) {
        this.holyEquipItemList = holyEquipItemList;
    }
    public ConcurrentHashMap<Integer,HolyEquipPart> getHolyEquipPartList(){return holyEquipPartList;}

    public void setHolyEquipPartList(ConcurrentHashMap<Integer,HolyEquipPart> holyEquipPartList){
        this.holyEquipPartList  = holyEquipPartList;
    }

    public void setAutoResolve(boolean isAutoResolve){this.isAutoResolve = isAutoResolve;}

    public boolean getIsAutoResolve(){return isAutoResolve;}

    public void setQuilty(int quilty){this.quilty = quilty;}

    public int getQuilty(){return quilty;}

    public void setGrade(int grade){this.grade = grade;}

    public int getGrade(){return grade;}
}
