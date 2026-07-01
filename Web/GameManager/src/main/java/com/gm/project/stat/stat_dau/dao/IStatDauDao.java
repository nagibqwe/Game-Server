package com.gm.project.stat.stat_dau.dao;

import java.util.List;
import java.util.Map;

public interface IStatDauDao {
    /**
     * 获取DAU的数据
     * @param table
     * @param channelNames
     * @param selectServerIds
     * @param blackUserStr
     * @param startDate
     * @param endDate
     * @param level
     * @return
     */
    public List<Map<String, Object>> getDayDAUDataList(String table, String channelNames, String selectServerIds, String blackUserStr, String startDate, String endDate, int level);
}
