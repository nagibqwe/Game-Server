package com.gm.project.stat.common.dao;

import java.util.List;
import java.util.Map;

public interface IStatRechargeDao {
    /**
     * 得到充值充值数据
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param stime
     * @param etime
     * @param isBlack
     * @return
     */
    public List<Map<String,Object>> getRechargeMapDataList(String channelNames, String selectServerIds, String blackUserStr, long stime, long etime,boolean isBlack);

}
