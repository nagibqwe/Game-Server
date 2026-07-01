/**
 * Auto generated, do not edit it
 * <p>
 * roleActivityData
 */
package com.game.db.bean;

import game.core.db.BaseBean;

public class RoleActivityDataBean extends BaseBean {

    private long roleId; // 角色ID
    private String actData; // 角色活动相关数据

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getActData() {
        return actData;
    }

    public void setActData(String actData) {
        this.actData = actData;
    }
}
