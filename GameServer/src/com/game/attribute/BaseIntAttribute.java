package com.game.attribute;

import com.game.structs.AttributeType;
import game.core.util.AutoIncrementIntArray;

/**
 * 整型属性值处理
 *
 */
public class BaseIntAttribute extends BaseAttribute {

    protected AutoIncrementIntArray attributes;
    private int size;

    @Deprecated
    public BaseIntAttribute() {

    }

    @Override
    public String toString(){
        return attributes.toString();
    }

    public BaseIntAttribute(int size) {
        this.size = size;
        this.attributes = new AutoIncrementIntArray(size);
    }

    @Override
    public void clean(){
        if(attributes == null){
            attributes = new AutoIncrementIntArray(size);
        }
        attributes.clean();
    }

    @Deprecated
    public AutoIncrementIntArray getAttributes(){
        return attributes;
    }

    @Deprecated
    public void setAttributes(AutoIncrementIntArray attributes) {
        this.attributes = attributes;
    }

    public void setAttribute(int attri, int value){
        attributes.set(attri, value);
    }

    public void addAttribute(int attri, int value){
        attributes.add(attri, value);
    }

    public int getAdditionValue(int i) {
        return attributes.get(i);
    }

    @Override
    public int getLength() {
        return attributes.length();
    }

    public void setAttributeArray(int[] value) {
        attributes.setArray(value);
    }

    @Override
    protected long gainMoveSpeed() {
        return attributes.get(AttributeType.ATTR_Speed);
    }

    @Override
    protected long gainAttackSpeed() {
        return attributes.get(AttributeType.ATTR_AtkSpeed);
    }
}
