package com.gm.project.gmquerydata.rechargerank.service;

import com.gm.project.gmquerydata.rechargerank.domain.RechargeRankBean;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

public interface IRechargeRankService {
    public List<RechargeRankBean> rechargeRank(String groupName, String selectServerIdList, String channelNames, String startDate, String endDate, Integer type, Map<String,Object> param) throws ParseException;
}
