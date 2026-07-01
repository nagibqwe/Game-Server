package com.backend.struct.log.entity.backpack;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.CommonLogBean;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * 元宝变化日志
 */
@Table(name = "goldchangelog", tableType = TableType.Month)
public class GoldChangeLog extends CommonLogBean implements IConvertor {

    @FieldDesc
    private int roleLevel;          //玩家等级

    @FieldDesc
    private String loginIp;         //登录IP

    @FieldDesc
    private int changeNum;          //变化值

    @FieldDesc(selectKey = true)
    private int reason;             //原因码

    @FieldDesc
    private int beforeNum;          //变化前值

    @FieldDesc
    private int afterNum;           //变化后值

    @FieldDesc
    private long actionId;          //关联ID

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

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public int getChangeNum() {
        return changeNum;
    }

    public void setChangeNum(int changeNum) {
        this.changeNum = changeNum;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public int getBeforeNum() {
        return beforeNum;
    }

    public void setBeforeNum(int beforeNum) {
        this.beforeNum = beforeNum;
    }

    public int getAfterNum() {
        return afterNum;
    }

    public void setAfterNum(int afterNum) {
        this.afterNum = afterNum;
    }

    public long getActionId() {
        return actionId;
    }

    public void setActionId(long actionId) {
        this.actionId = actionId;
    }
}
