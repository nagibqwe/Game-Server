package com.backend.struct.log;

import com.backend.annotation.log.FieldDesc;

public class CommonLogBean{

    @FieldDesc(show = false)
    private long userId;

    @FieldDesc(selectKey = true, desc = "logentity.commonlogbean.roleId")
    private long roleId;

    @FieldDesc(desc = "logentity.commonlogbean.roleName")
    private String roleName;

    @FieldDesc(show = false)
    private int createSid;

    @FieldDesc(show = false)
    private String platform;

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public int getCreateSid() {
        return createSid;
    }

    public void setCreateSid(int createSid) {
        this.createSid = createSid;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
