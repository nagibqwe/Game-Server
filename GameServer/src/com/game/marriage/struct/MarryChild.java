package com.game.marriage.struct;

/**
 * @Desc TODO
 * @Date 2020/8/11 20:30
 * @Auth ZUncle
 */
public class MarryChild {
    int id;         //仙娃ID
    String name;
    int level;
    int exp;
    int show;       //是否出战

    public int getShow() {
        return show;
    }

    public void setShow(int show) {
        this.show = show;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }
}
