package com.game.db.bean;

import game.core.db.BaseBean;

/**
 * @author luosv
 * Created on 2018/4/18 0018.
 */
public class DailyAccRechargeBean extends BaseBean {

    /**
     * 角色ID
     */
    private long roleId;

    /**
     * 每日累充数据
     */
    private String DailyAccRechargeData;

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getDailyAccRechargeData() {
        return DailyAccRechargeData;
    }

    public void setDailyAccRechargeData(String dailyAccRechargeData) {
        DailyAccRechargeData = dailyAccRechargeData;
    }

}
