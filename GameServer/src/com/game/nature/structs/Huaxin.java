package com.game.nature.structs;

public class Huaxin {
    /**
     * 配置表id
     * */
    private int excelId;
    /**
     * 当前等级
     * */
    private int level;

    public Huaxin() {}

    public Huaxin(int excelId, int level) {
        this.excelId = excelId;
        this.level = level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getExcelId() {
        return excelId;
    }

    public void setExcelId(int excelId) {
        this.excelId = excelId;
    }
}
