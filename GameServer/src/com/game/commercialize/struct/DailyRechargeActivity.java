package com.game.commercialize.struct;

import game.core.util.TimeUtils;

import java.util.List;

/**
 * 每日累充活动数据实体类
 *
 * @author luosv
 * Created on 2018/4/18 0018.
 */
public class DailyRechargeActivity {
    /**
     * 角色ID
     */
    private long roleId;

    // 上一次重置时间
    private long lastResetTime;

    // 每日累充版本号
    private int version;

    // 今日累计充值的元宝数量
    private int gold;

    // 奖励列表
    private List<DailyRechargeStage> stageList;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getLastResetTime() {
        return lastResetTime;
    }

    public void setLastResetTime(long lastResetTime) {
        this.lastResetTime = lastResetTime;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public List<DailyRechargeStage> getStageList() {
        return stageList;
    }

    public void setStageList(List<DailyRechargeStage> stageList) {
        this.stageList = stageList;
    }

    /**
     * 是否可以重置
     * @return
     */
    public boolean isNeedReset() {
        return !TimeUtils.isSameDay(this.lastResetTime, TimeUtils.Time());
    }
}
