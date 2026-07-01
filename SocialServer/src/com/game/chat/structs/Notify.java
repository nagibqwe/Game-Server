package com.game.chat.structs;

/**
 * 通知类型枚举
 *
 * @author Somebody
 */

public enum Notify {

    /**
     * 1    错误通知 --飘字
     */
    ERROR(1),
    /**
     * 2	正常提示--飘字
     */
    NORMAL(2),
    /**
     * 3	成功通知--飘字
     */
    SUCCESS(3),
    /**
     * 4	滚动公告 --跑马灯
     */
    MARQUEE(4),
    /**
     * 5	切出 --弹二级提示框
     */
    SHOWBOX(5),
    /**
     * 6	聊天框系统提示 --这个只有聊天框
     */
    CHAT(6),
    /**
     * 7    系统栏公告  这个聊天框弹一次，飘字一次
     */
    CHAT_SYS_BULL(7),
    /**
     * 8    高阶公告 --没用
     */
    HIGH_ORDER_NOTIFY(8),
    /**
     * 9    跑马灯 --  和4一样的
     */
    CHAT_SYS_MARQUEE(9),
    /**
     * 10   可链接的跑马灯  --可以点击的跑马灯
     */
    CHAT_SYS_URL_MARQUEE(10),
    /**
     * 11	固定飘字停留3秒
     */
    FIXED(11),

    /**
     * 12	专属跑马灯
     */
    EXCLUSIVE_NOTIFY(12);

    private final byte value;

    Notify(int value) {
        this.value = (byte) value;
    }

    public byte getValue() {
        return value;
    }

    public boolean compare(int value) {
        return this.value == value;
    }

}
