package com.gm.project.stat.stat_career_distribute.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.stat.stat_career_distribute.dao.IStatCareerDistributeDao;
import com.gm.project.stat.stat_career_distribute.domain.StatCareerDistribute;
import com.gm.project.stat.stat_career_distribute.service.IStatCareerDistributeService;

/**
 * 职业分布Service业务层处理
 * 
 * @author gm
 * @date 2021-09-07
 */
@Service
public class StatCareerDistributeServiceImpl implements IStatCareerDistributeService 
{
    @Autowired
    private IStatCareerDistributeDao statCareerDistributeDao;

    /**
     * 查询职业分布列表
     * 
     * @param statCareerDistribute 职业分布
     * @return 职业分布
     */
    @Override
    public List<StatCareerDistribute> selectStatCareerDistributeList(StatCareerDistribute statCareerDistribute,String channelNames,Integer selectServerId)
    {
        List<StatCareerDistribute> list = this.statCareerDistributeDao.selectStatCareerDistributeList(statCareerDistribute,channelNames,selectServerId);
        if(list == null){
            return new ArrayList<>();
        }
        return list;
    }


}
