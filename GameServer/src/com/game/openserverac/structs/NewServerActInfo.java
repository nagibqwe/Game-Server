package com.game.openserverac.structs;

import java.util.HashSet;
import java.util.Set;

/**
 * @author gsj
 * @create 2020/7/17 15:24
 */
public class NewServerActInfo {

    private int type;

    private Set<Integer> overList = new HashSet<>();//已经领取的列表

    private Set<Integer> completList = new HashSet<>();//完成列表

    public NewServerActInfo() {
    }

    public NewServerActInfo(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Set<Integer> getOverList() {
        return overList;
    }

    public void setOverList(Set<Integer> overList) {
        this.overList = overList;
    }

    public Set<Integer> getCompletList() {
        return completList;
    }

    public void setCompletList(Set<Integer> completList) {
        this.completList = completList;
    }
}
