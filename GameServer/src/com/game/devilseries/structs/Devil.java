package com.game.devilseries.structs;

import com.game.shop.structs.LimitShop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 魔魂信息
 */
public class Devil {

    private Map<Integer, DevilCamp> camps = new HashMap<>();
    //勾选了自动分解为true
    private boolean autoSet = false;
    //自动分解的品质
    private int autoColor;
    //自动分解的星级
    private int autoStar;
    //抽奖热度值
    private int devilHotValue;

    private List<Integer> followDevilCopyList  = new ArrayList<>();

    public Map<Integer, DevilCamp> getCamps() {
        return camps;
    }

    public void setCamps(Map<Integer, DevilCamp> camps) {
        this.camps = camps;
    }

    public boolean isAutoSet() {
        return autoSet;
    }

    public void setAutoSet(boolean autoSet) {
        this.autoSet = autoSet;
    }

    public int getAutoColor() {
        return autoColor;
    }

    public void setAutoColor(int autoColor) {
        this.autoColor = autoColor;
    }

    public int getAutoStar() {
        return autoStar;
    }

    public void setAutoStar(int autoStar) {
        this.autoStar = autoStar;
    }

    public int getDevilHotValue() {
        return devilHotValue;
    }

    public void setDevilHotValue(int devilHotValue) {
        this.devilHotValue = devilHotValue;
    }

    public List<Integer> getFollowDevilCopyList() {
        return followDevilCopyList;
    }

    public void setFollowDevilCopyList(List<Integer> followDevilCopyList) {
        this.followDevilCopyList = followDevilCopyList;
    }
}
