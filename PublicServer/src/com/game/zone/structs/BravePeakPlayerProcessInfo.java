package com.game.zone.structs;

import game.core.util.AutoIncrementLongArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 记录玩家勇者巅峰闯关信息
 * Created by zcd on 2018/2/6.
 */
public class BravePeakPlayerProcessInfo {

    /**
     * 最高层
     */
    private int floor;

    /**
     * 当前层是否已经通关 0，失败，1，表示成功
     */
    private int success;

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "BravePeakPlayerProcessInfo{" +
                "floor=" + floor +
                ", success=" + success +
                '}';
    }
}
