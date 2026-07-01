package com.gm.project.stat.stat_recharge_accumulate.service;



import com.gm.project.stat.stat_recharge_accumulate.domain.RechargeAccumulateBean;

import java.util.List;
import java.util.Map;

/**
 * 充值统计Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatRechargeAccumulateService
{


    public List<RechargeAccumulateBean> statRechargeAccumulate(String selectGroupName, String selectServerIdList,String channelNames,String startDate, String endDate,Boolean isBlack);

}
