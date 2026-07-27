package com.gm.project.gmtool.activity.domain;

import com.gm.project.gmtool.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2020/9/3 20:18
 * @Auth ZUncle
 */
public class ItemBean {


    private int i;//道具ID

    private int n;//数量

    private int b;//是否绑定0，1

    private int c;//职业

    public int getI() {
        return i;
    }

    public void setI(int i) {
        this.i = i;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public int getB() {
        return b;
    }

    public void setB(int b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }

    public static List<ItemBean> split(String data) {
        List<ItemBean> items = new ArrayList<>();
        if (data == null|| data.equals("")) {
            return items;
        }
        String[] split = data.split(";");
        for (String str : split) {
            int[] s = Utils.splitInteger(str, "_");
            ItemBean c = ItemBean.builder().i(s[0]).n(s[1]);
            if (s.length > 2) {
                c.b(s[2]);
            } else {
                c.b(1);
            }
            if (s.length > 3) {
                c.c(s[3]);
            } else {
                c.c(9);
            }
            items.add(c);
        }
        return items;
    }

    public static HashMap<Integer, ItemBean> map(String data) {
        List<ItemBean> split = split(data);
        HashMap<Integer, ItemBean> map = new HashMap<>();
        for (ItemBean item : split) {
            map.put(item.getI(), item);
        }
        return map;
    }

    public static ItemBean builder() {
        return new ItemBean();
    }

    public ItemBean i(int i) {
        this.i = i;
        return this;
    }

    public ItemBean n(int n) {
        this.n = n;
        return this;
    }

    public ItemBean b(int b) {
        this.b = b;
        return this;
    }

    public ItemBean c(int c) {
        this.c = c;
        return this;
    }

}
