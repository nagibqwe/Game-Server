package com.game.eightdiagrams.structs;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/9/20.
 */
public class EightDiagramCity {


    //城市ID
    private int cityId = 0;
    //城市当前所属服务器
    private int curSid  = 0;
    //这个城市最初出生服务器ID
    private int birthSid = 0;
    //当前城市对应副本ID
    private int modelID = 0;
    //当前城市对应房间ID
    private long roomID = 0;

    private int bossID = 0;

    private long curBossHp = 0 ;

    private long maxBossHp =0;

    private String serverName  = "";

    private String platName = "";

    //服务器ID 对应参与信息，多少人，对BOSS的伤害等等
    private ConcurrentHashMap<Integer, CityBattleProgress> cityBattleProgressList = new ConcurrentHashMap<>();
    //城市正营
    private int colorCamp = 0;



    public void setCityId(int cityId){this.cityId = cityId;}

    public int getCityId(){return cityId;}

    public void setCurSid(int curSid){this.curSid = curSid;}

    public int getCurSid(){return curSid;}

    public void setBirthSid(int birthSid){this.birthSid =birthSid;}

    public int getBirthSid(){return birthSid;}

    public void setModelID(int modelID){this.modelID = modelID;}

    public  int getModelID(){return modelID;}

    public void setRoomID(long roomID){this.roomID = roomID;}

    public long getRoomID(){return roomID;}

    public void setBossID(int bossID){this.bossID = bossID;}

    public int getBossID(){return bossID;}

    public void setServerName(String serverName){this.serverName = serverName;}

    public String getServerName(){return serverName;}

    public void setPlatName(String platName){this.platName = platName;}

    public String getPlatName(){return platName;}

    public void setCurBossHp(long curBossHp){this.curBossHp = curBossHp;}

    public long getCurBossHp(){return curBossHp;}

    public void setMaxBossHp(long maxBossHp){this.maxBossHp = maxBossHp;}

    public long getMaxBossHp(){return maxBossHp;}

    public ConcurrentHashMap<Integer, CityBattleProgress> getCityBattleProgressList(){return  cityBattleProgressList;}

    public void setCityBattleProgressList(ConcurrentHashMap<Integer, CityBattleProgress> cityBattleProgressList) {
        this.cityBattleProgressList = cityBattleProgressList;
    }

    public int getColorCamp() {
        return colorCamp;
    }

    public void setColorCamp(int colorCamp) {
        this.colorCamp = colorCamp;
    }
}
