package com.game.friend.struct;

import game.core.util.TimeUtils;

/**
 * 最近聊天
 */
public class LatelyPlayer extends RelationInfo{

    /**
     * 最近聊天时间
     */
    int time;

    public LatelyPlayer() {
        super();
        this.time = (int) (TimeUtils.Time() / 1000);
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
