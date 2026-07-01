package com.gm.project.stat.stat_gold_purpose.service;



import com.gm.project.stat.stat_gold_purpose.domain.GoldPurposeBean;

import java.util.List;

/**
 * 二次付费统计Service接口
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatGoldPurposeService
{
    /**
     * 二次付费项目统计
     * @param selectGroupName
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<GoldPurposeBean> statGoldPurpose(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack,Integer goldType);
}
