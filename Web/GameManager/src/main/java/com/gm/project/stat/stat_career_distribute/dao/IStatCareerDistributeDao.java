package com.gm.project.stat.stat_career_distribute.dao;

import com.gm.project.stat.stat_career_distribute.domain.StatCareerDistribute;

import java.util.List;

/**
 * 职业分布Mapper接口
 * 
 * @author gm
 * @date 2021-09-07
 */
public interface IStatCareerDistributeDao
{

    /**
     * 查询职业分布列表
     *
     * @param statCareerDistribute 职业分布
     * @return 职业分布集合
     */
    public List<StatCareerDistribute> selectStatCareerDistributeList(StatCareerDistribute statCareerDistribute, String channelNames, Integer selectServerId);
}
