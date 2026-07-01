package com.game.guildactivity.struct;

import java.util.HashMap;
import java.util.List;

/**
 * 仙盟驻地地图数据
 */
public class GuildBaseMapData {

    /**
     * 仙盟Boss刷新小怪波数
     */
    private int progress;

    /**
     * 每波小怪列表
     */
    private HashMap<Integer, List<Long>> monsterInfo = new HashMap<>();

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public HashMap<Integer, List<Long>> getMonsterInfo() {
        return monsterInfo;
    }

    public void setMonsterInfo(HashMap<Integer, List<Long>> monsterInfo) {
        this.monsterInfo = monsterInfo;
    }
}
