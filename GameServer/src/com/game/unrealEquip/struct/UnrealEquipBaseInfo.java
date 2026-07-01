package com.game.unrealEquip.struct;

import com.game.holyEquip.struct.HolyEquipItem;
import com.game.holyEquip.struct.HolyEquipPart;

import java.util.concurrent.ConcurrentHashMap;

public class UnrealEquipBaseInfo {
    //幻装背包
    private ConcurrentHashMap<Integer,Integer> unrealSoulInfoList = new ConcurrentHashMap<>();

    //幻装信息
    private ConcurrentHashMap<Long, UnrealEquipItem> unrealEquipItemList = new ConcurrentHashMap<>();

    //幻装信息
    private ConcurrentHashMap<Integer, UnrealEquipPart> unrealEquipPartList = new ConcurrentHashMap<>();


    public ConcurrentHashMap<Integer, Integer> getUnrealSoulInfoList() {
        return unrealSoulInfoList;
    }

    public void setUnrealSoulInfoList(ConcurrentHashMap<Integer, Integer> unrealSoulInfoList) {
        this.unrealSoulInfoList = unrealSoulInfoList;
    }

    public ConcurrentHashMap<Long, UnrealEquipItem> getUnrealEquipItemList() {
        return unrealEquipItemList;
    }

    public void setUnrealEquipItemList(ConcurrentHashMap<Long, UnrealEquipItem> unrealEquipItemList) {
        this.unrealEquipItemList = unrealEquipItemList;
    }

    public ConcurrentHashMap<Integer, UnrealEquipPart> getUnrealEquipPartList() {
        return unrealEquipPartList;
    }

    public void setUnrealEquipPartList(ConcurrentHashMap<Integer, UnrealEquipPart> unrealEquipPartList) {
        this.unrealEquipPartList = unrealEquipPartList;
    }
}
