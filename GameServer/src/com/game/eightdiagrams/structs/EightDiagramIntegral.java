package com.game.eightdiagrams.structs;

import com.data.Global;

/**
 * Created by 542 on 2019/9/23.
 */
public class EightDiagramIntegral {

    private long roleID = 0;

    private String name = "";

    private long hurt = 0;

    private int hurtIntegral = 0;

    private int integral = 0;

    private String platSid = "";

    private int colorCamp = 0;

    private int serverid = 0;

    public int getColorCamp() {
        return colorCamp;
    }

    public void setColorCamp(int colorCamp) {
        this.colorCamp = colorCamp;
    }

    public void setRoleID(long roleID){this.roleID = roleID;}

    public long getRoleID(){return roleID;}

    public void setIntegral(int integral){this.integral = integral;}

    public int getIntegral(){return integral;}

    public void setName(String name){this.name = name;}

    public String getName(){return name;}

    public void setHurt(long hurt){
        this.hurt +=hurt;
        hurtIntegral = (int)(this.hurt/ Global.Eight_City_Count_Boss.get(0));

    }

    public long getHurt(){return hurt;}

    public void setPlatSid(String platSid){this.platSid = platSid;}

    public String getPlatSid(){return platSid;}

    public int getAllIntegral(){return hurtIntegral + integral;}

    public int getServerid() {
        return serverid;
    }

    public void setServerid(int serverid) {
        this.serverid = serverid;
    }
}
