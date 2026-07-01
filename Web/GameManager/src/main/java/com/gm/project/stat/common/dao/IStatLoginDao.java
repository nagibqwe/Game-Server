package com.gm.project.stat.common.dao;

import java.util.List;
import java.util.Map;

public interface IStatLoginDao {
    /**
     * 活跃玩家

     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param stime
     * @param etime
     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getActiveUserDataList(String channelNames, String selectServerIds, String blackUserStr, long stime, long etime, boolean isBlack);


    /**
     * 活跃设备
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr

     * @param isBlack
     * @return
     */
    public List<Map<String, Object>> getActiveDeviceDataList(String channelNames, String selectServerIds, String blackUserStr, long stime, long etime,boolean isBlack);

}
