package com.game.activity.struct;

import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;

/**
 * Created by cxl on 2020/8/19.
 */
public class RewardData {

    private int i;//道具ID

    private int n;//数量

    private int b;//是否绑定0，1

    private int c;//职业

    public int getI() {
        return i;
    }

    public RewardData setI(int i) {
        this.i = i;
        return this;
    }

    public int getN() {
        return n;
    }

    public RewardData setN(int n) {
        this.n = n;
        return this;
    }

    public int getB() {
        return b;
    }

    public RewardData setB(int b) {
        this.b = b;
        return this;
    }

    public int getC() {
        return c;
    }

    public RewardData setC(int c) {
        this.c = c;
        return this;
    }

    public ReadArray<Integer> parse() {
        Integer[] params = {i,n,b,c};
        return new ReadIntegerArray(params);
    }

    @Override
    public String toString() {
        return "RewardData{" +
                "i=" + i +
                ", n=" + n +
                ", b=" + b +
                ", c=" + c +
                '}';
    }
}
