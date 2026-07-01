package com.backend.struct.log.entity.dailyactive;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 领取活跃度奖励
 */
@Table(name = "receiveactiverewardlog", tableType = TableType.Month)
public class ReceiveActiveRewardLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int cfgId;                  //领取的奖励id

    @FieldDesc
    private int dailyActiveValue;       //玩家领取时当前的活跃值

    @FieldDesc
    private String activeReward;        //获得的奖励

    @FieldDesc
    private long actionId;              //日志关联ID

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getCfgId() {
        return cfgId;
    }

    public void setCfgId(int cfgId) {
        this.cfgId = cfgId;
    }

    public int getDailyActiveValue() {
        return dailyActiveValue;
    }

    public void setDailyActiveValue(int dailyActiveValue) {
        this.dailyActiveValue = dailyActiveValue;
    }

    public String getActiveReward() {
        return activeReward;
    }

    public void setActiveReward(String activeReward) {
        this.activeReward = activeReward;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
