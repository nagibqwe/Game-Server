package com.gm.project.stat.stat_career_distribute.service;

import java.util.List;
import com.gm.project.stat.stat_career_distribute.domain.StatCareerDistribute;

/**
 * 职业分布Service接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IStatCareerDistributeService 
{

    /**
     * 查询职业分布列表
     * 
     * @param statCareerDistribute 职业分布
     * @return 职业分布集合
     */
    public List<StatCareerDistribute> selectStatCareerDistributeList(StatCareerDistribute statCareerDistribute,String channelNames,Integer selectServerId);

}
