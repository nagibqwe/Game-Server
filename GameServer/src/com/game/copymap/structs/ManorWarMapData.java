package com.game.copymap.structs;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 瞿冰冰
 * 2019/9/26
 */
public class ManorWarMapData {

    private Set<Integer> dieList = new HashSet<>();

    private int crossServerGroupId = 0;

    private int totalBossNum = 0;

    public Set<Integer> getDieList() {
        return dieList;
    }

    public void setDieList(Set<Integer> dieList) {
        this.dieList = dieList;
    }

    public int getCrossServerGroupId() {
        return crossServerGroupId;
    }

    public void setCrossServerGroupId(int crossServerGroupId) {
        this.crossServerGroupId = crossServerGroupId;
    }

    public int getTotalBossNum() {
        return totalBossNum;
    }

    public void setTotalBossNum(int totalBossNum) {
        this.totalBossNum = totalBossNum;
    }
}
