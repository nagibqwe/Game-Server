package com.game.copymap.structs;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/9/29 15:15
 * @Auth ZUncle
 */
public class ExpNoteData extends  ZoneCache{

    /**
     * 巅峰竞技经验统计
     */
    ConcurrentHashMap<Long, Long> expNote = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, Long> getExpNote() {
        return expNote;
    }

    public void setExpNote(ConcurrentHashMap<Long, Long> expNote) {
        this.expNote = expNote;
    }
}
