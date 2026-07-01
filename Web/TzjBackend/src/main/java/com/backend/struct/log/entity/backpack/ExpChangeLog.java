package com.backend.struct.log.entity.backpack;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 经验变化日志
 */
@Table(name = "expchangelog", tableType = TableType.Month)
public class ExpChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int roleLevel;          //玩家等级

    @FieldDesc
    private long changeNum;         //变化值

    @FieldDesc(selectKey = true)
    private int reason;             //原因码

    @FieldDesc
    private long beforeNum;         //变化前值

    @FieldDesc
    private long afterNum;          //变化后值

    @FieldDesc
    private long actionId;          //关联ID

    @FieldDesc
    private long upLevelExp;        //升级经验

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public int getRoleLevel() {
        return roleLevel;
    }

    public void setRoleLevel(int roleLevel) {
        this.roleLevel = roleLevel;
    }

    public long getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(long changeNum) {
        this.changeNum = changeNum;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public long getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(long beforeNum) {
        this.beforeNum = beforeNum;
    }

    public long getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(long afterNum) {
        this.afterNum = afterNum;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }

    public long getUpLevelExp() {
        return upLevelExp;
    }

    public void setUpLevelExp(long upLevelExp) {
        this.upLevelExp = upLevelExp;
    }
}
