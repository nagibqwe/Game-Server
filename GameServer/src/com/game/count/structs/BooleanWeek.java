package com.game.count.structs;

/**
 *
 * @author admin
 */
public enum BooleanWeek implements BooleanCount {

    ;
    /**
     * key值，从0一次增加
     */
    public final long key;

    BooleanWeek(long key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.BooleanWeekValue;
    }
}
