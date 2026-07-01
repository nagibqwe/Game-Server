package com.game.eightdiagrams.structs;

/**
 * Created by cxl on 2019/9/21.
 */
public class EightDaramsMapData {


    public int ciytID;

    public int groupID;

    public int birthSid ;//城市出生的 服务器ID

    public int curSid;//当前占领这个城市的服务器ID

    public int curCamp;//BOSS阵营

    public int bossID;//

    public void setCiytID(int ciytID){this.ciytID = ciytID;}

    public int getCiytID(){return ciytID;}

    public void setGroupID(int groupID){this.groupID = groupID;}

    public int getGroupID(){return groupID;}

    public void setBirthSid(int birthSid){this.birthSid = birthSid;}

    public int getBirthSid(){return birthSid;}

    public void setCurSid(int curSid){this.curSid = curSid;}

    public int getCurSid(){return curSid;}

    public void setCurCamp(int curCamp){this.curCamp = curCamp;}

    public int getCurCamp(){return curCamp;}

    public void setBossID(int bossID){this.bossID = bossID;}

    public int getBossID(){return bossID;}
}
