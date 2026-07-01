package com.game.bigplayer.struct;

import java.util.HashMap;

public class BigPlayerInfo {

    private long weeksRefreshTime;//刷新时间

    private int curlevel;//当前大玩咖等级

    //key 大玩咖 等级
    private HashMap<Integer,BigPlayerData> bigPlayerDataHashMap = new HashMap<>();//大玩咖腾讯返回的数据

    public HashMap<Integer, BigPlayerData> getBigPlayerDataHashMap() {
        return bigPlayerDataHashMap;
    }

    public void setBigPlayerDataHashMap(HashMap<Integer, BigPlayerData> bigPlayerDataHashMap) {
        this.bigPlayerDataHashMap = bigPlayerDataHashMap;
    }

    public long getWeeksRefreshTime() {
        return weeksRefreshTime;
    }

    public void setWeeksRefreshTime(long weeksRefreshTime) {
        this.weeksRefreshTime = weeksRefreshTime;
    }
}
