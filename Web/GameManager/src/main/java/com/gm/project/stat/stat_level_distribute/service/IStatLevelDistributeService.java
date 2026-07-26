package com.gm.project.stat.stat_level_distribute.service;

import java.util.List;
import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;

/**
 * Уровень персонажа分布Service接口
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatLevelDistributeService 
{


    /**
     * 查询Уровень персонажа分布列表
     * 
     * @param statLevelDistribute Уровень персонажа分布
     * @return Уровень персонажа分布集合
     */
    public List<StatLevelDistribute> selectStatLevelDistributeList(String channelNames,  Integer condition, Integer level,StatLevelDistribute statLevelDistribute,String startDate,String endDate,Integer selectServerId);


}
