package com.gm.project.gamelog.ranklistlog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.ranklistlog.domain.Ranklistlog;

/**
 * 排行榜日志Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface IRanklistlogService 
{

    /**
     * 查询排行榜日志列表
     * 
     * @param ranklistlog 排行榜日志
     * @return 排行榜日志集合
     */
    public List<Ranklistlog> selectRanklistlogList(Ranklistlog ranklistlog,Map<String, Object> param);

}
