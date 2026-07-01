package com.backend.struct.log.entity.gm;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

/**
 * GM指令日志
 */
@Table(name = "gmcommandlog", tableType = TableType.Month)
public class GmCommandLog implements IConvertor {

    @FieldDesc
    private long userId;        //账号id

    @FieldDesc
    private String roleName;    //角色名

    @FieldDesc
    private long roleId;        //角色id

    @FieldDesc
    private int sid;            //角色区服

    @FieldDesc
    private int gmLevel;        //gm等级

    @FieldDesc
    private String command;     //GM命令

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;          //时间

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getGmLevel() {
        return gmLevel;
    }

    public void setGmLevel(int gmLevel) {
        this.gmLevel = gmLevel;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
