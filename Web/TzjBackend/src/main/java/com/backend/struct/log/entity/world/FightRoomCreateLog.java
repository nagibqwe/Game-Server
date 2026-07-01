package com.backend.struct.log.entity.world;

import com.backend.annotation.log.FieldDesc;
import com.backend.annotation.log.Table;
import com.backend.struct.log.IConvertor;
import com.backend.struct.log.TableType;

import java.util.Map;

@Table(name = "fightroomcreatelog", tableType = TableType.Month, crossLogType = 2)
public class FightRoomCreateLog implements IConvertor {

    @FieldDesc
    private long roleId;            //角色ID

    @FieldDesc
    private String roleName;        //角色名字

    @FieldDesc
    private String plat;            //平台

    @FieldDesc
    private long fid;               //房间战

    @FieldDesc
    private int modelId;            //跨服战的副本ID

    @FieldDesc
    private int sid;                //创建服的来源服务器Id

    @FieldDesc
    private int power;              //战斗力

    @FieldDesc
    private int isAuto;             //是否自动开始

    @FieldDesc(desc = "logentity.commonlogbean.time")
    private long time;

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

    public String getPlat() {
        return plat;
    }

    public void setPlat(String plat) {
        this.plat = plat;
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getSid() {
        return sid;
    }

    public void setSid(int sid) {
        this.sid = sid;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(int isAuto) {
        this.isAuto = isAuto;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public Map<String, String> convert(Map<String, String> data) {
        return data;
    }
}
