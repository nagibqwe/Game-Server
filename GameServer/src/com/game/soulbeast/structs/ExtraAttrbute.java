package com.game.soulbeast.structs;

/**
 * 魂兽装备额外属性
 * Created by zcd on 2018/5/10.
 */
public class ExtraAttrbute {
    private int key;
    private int value;
    //所属颜色
    private int color;

    public ExtraAttrbute(int key, int value, int color) {
        this.key = key;
        this.value = value;
        this.color = color;
    }

    public ExtraAttrbute(){

    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return "ExtraAttrbute{" +
                "key=" + key +
                ", value=" + value +
                ", color=" + color +
                '}';
    }
}
