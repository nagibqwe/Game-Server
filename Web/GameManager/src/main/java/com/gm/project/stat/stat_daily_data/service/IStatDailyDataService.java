package com.gm.project.stat.stat_daily_data.service;

import com.game.util.ThreeTuple;
import com.game.util.TwoTuple;
import com.gm.project.stat.stat_daily_data.domain.StatDailyDataBean;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IStatDailyDataService {
    public List<StatDailyDataBean> statDailyData(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack);

    public ThreeTuple< List<Map<String, Object>>,List<Map<String, Object>>,List<Map<String, Object>>> statDailyDataCommon(String groupName, String selectServerIdList, String channelNames, String startDate, String endDate , String blackUsers, Boolean isBlack);
    public Map<String, Set<Object>> getAssembleMap(List<Map<String, Object>> dataMap, String mapKey, String key);
}
