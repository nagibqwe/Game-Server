package com.game.copymap.structs;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/8/5 14:56
 * @Auth ZUncle
 */
public class StrengthenCopyData extends ZoneCache{
    /**
     * 怪物波数
     */
    private int stage;

    /**
     * 剩余boss数量
     */
    private int remainBossNum;

    /**
     * 副本刷怪列表
     */
    private List<Integer> clone_monster_list = new ArrayList<>();

    /**
     * 副本开始时间（秒）
     */
    private int startTime;

    /**
     * 副本结束时间（秒）
     */
    private int endTime;

    public int getStage() {
        return stage;
    }

    public void setStage(int stage) {
        this.stage = stage;
    }

    public int getRemainBossNum() {
        return remainBossNum;
    }

    public void setRemainBossNum(int remainBossNum) {
        this.remainBossNum = remainBossNum;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public List<Integer> getClone_monster_list() {
        return clone_monster_list;
    }
}
