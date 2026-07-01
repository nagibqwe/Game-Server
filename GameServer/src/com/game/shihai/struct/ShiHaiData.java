package com.game.shihai.struct;

import com.game.attribute.BaseIntAttribute;
import com.game.structs.AttributeType;

public class ShiHaiData {
    /**
     * 境界，对应配置表id
     */
    private int cfgId;

    /**
     * 增加的属性
     */
    private BaseIntAttribute shiHaiAttr = new BaseIntAttribute(AttributeType.ATTR_MAX);

    /**
     * 获取 境界，对应配置表id
     *
     * @return cfgId 境界，对应配置表id
     */
    public int getCfgId() {
        return this.cfgId;
    }

    /**
     * 设置 境界，对应配置表id
     *
     * @param cfgId 境界，对应配置表id
     */
    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    /**
     * 获取 增加的属性
     *
     * @return shiHaiAttr 增加的属性
     */
    public BaseIntAttribute getShiHaiAttr() {
        return this.shiHaiAttr;
    }

    /**
     * 设置 增加的属性
     *
     * @param shiHaiAttr 增加的属性
     */
    public void setShiHaiAttr(BaseIntAttribute shiHaiAttr) {
        this.shiHaiAttr = shiHaiAttr;
    }
}
