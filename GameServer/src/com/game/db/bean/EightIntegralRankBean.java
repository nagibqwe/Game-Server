package com.game.db.bean;

import com.data.Global;

/**
 * Created by cxl on 2020/12/18.
 */
public class EightIntegralRankBean {

    private long roleID ;

    private String name ;

    private long hurt ;

    private int integral ;

    private String platSid;

    private int colorCamp ;

    private int serverid ;

    private int groupId;


    public int getAllIntegral(){
        return (int)(this.hurt/ Global.Eight_City_Count_Boss.get(0)) + integral;
    }


    public long getRoleID() {
        return roleID;
    }

    public void setRoleID(long roleID) {
        this.roleID = roleID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getHurt() {
        return hurt;
    }

    public void setHurt(long hurt) {
        this.hurt = hurt;
    }

    public int getIntegral() {
        return integral;
    }

    public void setIntegral(int integral) {
        this.integral = integral;
    }

    public String getPlatSid() {
        return platSid;
    }

    public void setPlatSid(String platSid) {
        this.platSid = platSid;
    }

    public int getColorCamp() {
        return colorCamp;
    }

    public void setColorCamp(int colorCamp) {
        this.colorCamp = colorCamp;
    }

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
