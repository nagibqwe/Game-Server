package com.game.count.structs;

/**
 *
 * @author admin
 */
public enum BooleanYear implements BooleanCount {

    ;
    /**
     * key值，从0一次增加
     */
    public final long key;

    BooleanYear(long key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.BooleanYearValue;
    }

}
