package com.gm.project.stat.stat_dau.service;

import com.gm.project.stat.stat_dau.domain.StatDauBean;

import java.util.List;

public interface IStatDauService {
    /**
     * 日活统计
     * @param selectGroupName
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @param level
     * @return
     */
    public List<StatDauBean> statDau(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack, int level);
}
