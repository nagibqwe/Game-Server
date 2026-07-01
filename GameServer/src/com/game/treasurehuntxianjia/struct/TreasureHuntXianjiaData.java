package com.game.treasurehuntxianjia.struct;

import com.game.backpack.structs.Item;
import com.game.treasurehunt.struct.TreasureHuntRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2020/2/25.
 */
public class TreasureHuntXianjiaData {

    //轮
    private int round = -1;
    //仙甲寻宝次数
    private int huntXianjiaCount = 0;
    //密保已经免费抽过的次数
    private int mibaoCount = 0;
    //抽多少次以内必中的 奖励索引
    private int alreadyIndex = 0;


    //玩家的仓库
    private Map<Integer,Map<Long,Item>> xianjiaStoreHouse = new HashMap<>();
    //密宝奖池1
    private ConcurrentHashMap<Integer,Boolean> mibaoreward_1 = new ConcurrentHashMap<>();
    //密宝奖池2
    private ConcurrentHashMap<Integer,Boolean> mibaoreward_2  = new ConcurrentHashMap<>();

    public void init(int round,ConcurrentHashMap<Integer,Boolean> mibaoreward_1,ConcurrentHashMap<Integer,Boolean> mibaoreward_2){
        this.round = round;
        huntXianjiaCount = 0;
        mibaoCount = 0;
        alreadyIndex = 0;
        this.mibaoreward_1 = mibaoreward_1;
        this.mibaoreward_2 = mibaoreward_2;
    }

    public int getAlreadyIndex() {
        return alreadyIndex;
    }
    public void setAlreadyIndex(int alreadyIndex) {
        this.alreadyIndex = alreadyIndex;
    }
    public int getMibaoCount() {
        return mibaoCount;
    }

    public void setMibaoCount(int mibaoCount) {
        this.mibaoCount = mibaoCount;
    }

    public ConcurrentHashMap<Integer, Boolean> getMibaoreward_1() {
        return mibaoreward_1;
    }

    public void setMibaoreward_1(ConcurrentHashMap<Integer, Boolean> mibaoreward_1) {
        this.mibaoreward_1 = mibaoreward_1;
    }

    public ConcurrentHashMap<Integer, Boolean> getMibaoreward_2() {
        return mibaoreward_2;
    }

    public void setMibaoreward_2(ConcurrentHashMap<Integer, Boolean> mibaoreward_2) {
        this.mibaoreward_2 = mibaoreward_2;
    }


    public Map<Integer, Map<Long, Item>> getXianjiaStoreHouse() {
        return xianjiaStoreHouse;
    }

    public void setXianjiaStoreHouse(Map<Integer, Map<Long, Item>> xianjiaStoreHouse) {
        this.xianjiaStoreHouse = xianjiaStoreHouse;
    }


    public int getHuntXianjiaCount() {
        return huntXianjiaCount;
    }

    public void setHuntXianjiaCount(int huntXianjiaCount) {
        this.huntXianjiaCount = huntXianjiaCount;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }
}
