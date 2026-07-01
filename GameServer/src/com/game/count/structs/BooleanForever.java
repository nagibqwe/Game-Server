package com.game.count.structs;

/**
 *
 * @author admin
 */
public enum BooleanForever implements BooleanCount {
    /**
     *
     */
    AuctionPur(0),

    /**
     *  自动关注福地boss
     */
    AutoFollowFudBoss(1),

    /**
     *  自动关注跨服福地boss
     */
    AutoFollowCrossFudBoss(2),

    /**
     * 每日分享
     */
    PlatformEvaluateEveryDayShare(3),
    ;
    /**
     * key值，从0一次增加
     */
    public final long key;

    BooleanForever(long key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.BooleanForeverValue;
    }
}
