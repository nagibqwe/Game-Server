package com.game.attribute;


public class BaseSystemIntAttribute extends BaseIntAttribute {

    public BaseSystemIntAttribute() {
    }

    public BaseSystemIntAttribute(int size) {
        super(size);
    }

    public void addSystemAttribute(int attri, int value) {
        //TODO 副属性百分百, 索引和配置表偏移1000
        int index = attri > 1000 ? attri - 1000 : attri;
        attributes.add(index, value);
    }

    public int getAttribute(int i) {
        int index = i > 1000 ? i - 1000 : i;
        return attributes.get(index);
    }

}
