package com.game.nature.structs;

public class HuaxinModelInfo {
    /**
     * 配置表id
     */
    private int id;
    /**
     * 等级
     * */
    private int level;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString() {
        return "HuaxinModelInfo{" +
                "id=" + id +
                ", level=" + level +
                '}';
    }
}
