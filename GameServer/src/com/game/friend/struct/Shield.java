/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.friend.struct;

import game.core.util.TimeUtils;

/**
 * 屏蔽关系
 */
public class Shield extends RelationInfo {

    /**
     * 屏蔽时间
     */
    int time;

    public Shield() {
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
