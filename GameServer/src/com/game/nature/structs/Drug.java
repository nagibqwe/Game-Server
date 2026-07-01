package com.game.nature.structs;

public class Drug {
    /**
     * 物品配置表id
     * */
    private int itemId;
    /**
     * 物品当前等级
     * */
    private int level;
    /**
     * 物品当前已消耗个数
     * */
    private int useNumber;
    /**
     * 物品所在位置
     * */
    private int pos;
    /**
     * 物品归属类型
     * */
    private int belongType;

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getUseNumber() {
        return useNumber;
    }

    public void setUseNumber(int useNumber) {
        this.useNumber = useNumber;
    }

    public int getBelongType() {
        return belongType;
    }

    public void setBelongType(int belongType) {
        this.belongType = belongType;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getExcelId() {
        return  belongType * 1000 + pos * 100 + level;
    }

    public void addUseNumber(int value) {
        this.useNumber += value;
    }

    public void addLevel(int value) {
        this.level += value;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int excelId = getExcelId();
        stringBuilder.append(excelId);
        stringBuilder.append(",");
        stringBuilder.append(itemId);
        stringBuilder.append(",");
        stringBuilder.append(useNumber);
        stringBuilder.append(";");
        return stringBuilder.toString();
    }
}
