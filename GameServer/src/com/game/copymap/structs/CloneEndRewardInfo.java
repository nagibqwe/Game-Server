package com.game.copymap.structs;

import game.core.util.AutoIncrementLongArray;

import java.util.ArrayList;
import java.util.List;

/**
 * 最后副本结算时，发奖的参数集合
 *
 * @author xuchangming <xysoko@qq.com>
 */
public class CloneEndRewardInfo {

    private List<Long> roleIds = new ArrayList<>();
    private long zoneId;
    private int modelId;
    private int sort;
    private long score;
    private int time;
    private int star;
    private int dieNum;
    private boolean success;
    List<AutoIncrementLongArray> reward;

    public List<Long> getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(List<Long> roleIds) {
        this.roleIds = roleIds;
    }

    public long getZoneId() {
        return zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public int getDieNum() {
        return dieNum;
    }

    public void setDieNum(int dieNum) {
        this.dieNum = dieNum;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<AutoIncrementLongArray> getReward() {
        return reward;
    }

    public void setReward(List<AutoIncrementLongArray> reward) {
        this.reward = reward;
    }

    @Override
    public String toString() {
        return "CloneEndRewardInfo{" + "roleIds=" + roleIds + ", zoneId=" + zoneId + ", modelId=" + modelId + ", sort=" + sort + ", score=" + score + ", time=" + time + ", star=" + star + ", dieNum=" + dieNum + '}';
    }
}
