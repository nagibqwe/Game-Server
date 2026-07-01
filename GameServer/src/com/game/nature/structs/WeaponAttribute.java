package com.game.nature.structs;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class WeaponAttribute {
    /**
     * 属性id
     * */
    private int attributeId = 0;
    /**
     * 当前属性值
     * */
    private int value = 0;
    /**
     * 当前等级下，每次附灵增加的属性值
     * 请注意，该变量有特定的使用场景，请勿在其他场景中意图通过该变量获取当前等级增加的属性值，在其他场景中不保证也不会保证该变量的值的正确性
     * */
    @JsonIgnore
    private transient int addValue = 0;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void addValue(int value) {
        this.value += value;
    }

    public int getAttributeId() {
        return attributeId;
    }

    public void setAttributeId(int attributeId) {
        this.attributeId = attributeId;
    }

    public int getAddValue() {
        return addValue;
    }

    public void setAddValue(int addValue) {
        this.addValue = addValue;
    }
}
