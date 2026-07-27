package com.gm.project.stat.stat_recharge_overview.service;

import com.gm.project.stat.stat_recharge_overview.domain.StatRechargeOverviewBean;

import java.util.List;

public interface IStatRechargeOverviewService {
    public List<StatRechargeOverviewBean> StatRechargeOverview(String selectGroupName,String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack);
}
