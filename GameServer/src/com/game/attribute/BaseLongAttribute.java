package com.game.attribute;

import com.game.structs.AttributeType;
import game.core.util.AutoIncrementLongArray;

/**
 *
 */
public class BaseLongAttribute extends BaseAttribute {

    protected AutoIncrementLongArray attributes;
    private int size;

    @Deprecated
    public BaseLongAttribute() {

    }

    public BaseLongAttribute(int size) {
        this.size = size;
        this.attributes = new AutoIncrementLongArray(size);
    }

    public AutoIncrementLongArray getAttributes() {
        return attributes;
    }

    public void setAttributes(AutoIncrementLongArray attributes) {
        this.attributes = attributes;
    }

    public void setAttribute(int attri, long value) {
        attributes.set(attri, value);
    }

    public void addAttribute(int attri, long value) {
        attributes.add(attri, value);
    }

    public long getAdditionValue(int i) {
        return attributes.get(i);
    }

    @Override
    public int getLength() {
        return attributes.length();
    }

    public void setAttributeArray(long[] value) {
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

    @Override
    public void clean() {
        if (attributes == null) {
            attributes = new AutoIncrementLongArray(size);
        }
        attributes.clean();
    }

    @Override
    public String toString() {
        return attributes.toString();
    }

}
