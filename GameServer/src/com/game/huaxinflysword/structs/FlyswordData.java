package com.game.huaxinflysword.structs;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by cxl on 2020/7/2.
 */
public class FlyswordData {

    //等级
    private int level;

    //当前进阶
    private int steps;

    //当前系列类型
    private int type ;

    //当前类型激活的剑灵
    private List<Integer>  activateList = new ArrayList<>();

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public List<Integer> getActivateList() {
        return activateList;
    }

    public void setActivateList(List<Integer> activateList) {
        this.activateList = activateList;
    }

}
