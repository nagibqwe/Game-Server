package com.game.openserverac.structs;

import java.util.HashSet;
import java.util.Set;

/**
 * 返利宝箱数据
 * @Auther: gouzhongliang
 * @Date: 2021/12/3 9:43
 */
public class RebateBoxData {

    /**每日消耗的灵玉数量*/
    private int[] boxs = new int[7];
    /**领取了的天数*/
    private Set<Integer> boxDays = new HashSet<>();
    /**是否已发送提醒*/
    private int[] boxRemind = new int[7];

    public int[] getBoxs() {
        return boxs;
    }

    public void setBoxs(int[] boxs) {
        this.boxs = boxs;
    }

    public Set<Integer> getBoxDays() {
        return boxDays;
    }

    public void setBoxDays(Set<Integer> boxDays) {
        this.boxDays = boxDays;
    }

    public int[] getBoxRemind() {
        return boxRemind;
    }

    public void setBoxRemind(int[] boxRemind) {
        this.boxRemind = boxRemind;
    }
}
