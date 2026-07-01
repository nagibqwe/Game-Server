package com.game.copymap.structs;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 跨服神魔战场
 */
public class GodDevilWarCopyData {

    /**
     * 阵营信息
     * Map<roleId, camp>
     */
    private Map<Long, Integer> campMap = new ConcurrentHashMap<>();

    /**
     * 战场分数数据
     * Map<camp, Map<roleId, score>>
     */
    private Map<Integer, Map<Long, Integer>> scoreMap = new ConcurrentHashMap<>();

    /**
     * 战场伤害数据
     * Map<roleId, damage>
     */
    private Map<Long, Long> damageMap = new ConcurrentHashMap<>();

    /**
     * 分数奖励
     * Map<roleId, 配置表要求奖励分数值>
     */
    private Map<Long, Integer> scoreReward = new ConcurrentHashMap<>();

    public Map<Long, Integer> getCampMap() {
        return campMap;
    }

    public void setCampMap(Map<Long, Integer> campMap) {
        this.campMap = campMap;
    }

    public Map<Integer, Map<Long, Integer>> getScoreMap() {
        return scoreMap;
    }

    public void setScoreMap(Map<Integer, Map<Long, Integer>> scoreMap) {
        this.scoreMap = scoreMap;
    }

    public Map<Long, Long> getDamageMap() {
        return damageMap;
    }

    public void setDamageMap(Map<Long, Long> damageMap) {
        this.damageMap = damageMap;
    }

    public Map<Long, Integer> getScoreReward() {
        return scoreReward;
    }

    public void setScoreReward(Map<Long, Integer> scoreReward) {
        this.scoreReward = scoreReward;
    }
}
