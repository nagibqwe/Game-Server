package com.gm.project.stat.stat_daily_data.dao;


import java.util.List;
import java.util.Map;

public interface IStatDailyDataDao {
    /**
     *  获取在线列表
     * @param selectServerIdList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> getOnlineList(String selectServerIdList,String startDate,String endDate);



}
