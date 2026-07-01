package com.game.count.structs;

/**
 *
 * @author admin
 */
public enum BooleanDay implements BooleanCount {
    /**
     * 婚姻系统宝匣每日领取
     */
    MarryBoxDailyReward(0),

    /**
     * 公会每日领取
     */
    GuildDailyReward(1),

    /**
     * vip礼包每日领取
     */
    VipDailyReward(2),

    /**
     * vip点数据每日领取
     */
    VipDailyExp(3),

    /**
     * 今日登陆
     */
    DailyLogin(4),
    ;
    /**
     * key值，从0一次增加，小于64
     */
    private final long key;

    BooleanDay(long key) {
        this.key = key;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public VariantType getVariantType() {
        return VariantType.BooleanDayValue;
    }

}
