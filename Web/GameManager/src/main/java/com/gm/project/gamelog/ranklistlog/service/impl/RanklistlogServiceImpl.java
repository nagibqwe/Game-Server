package com.gm.project.gamelog.ranklistlog.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.ranklistlog.domain.Ranklistlog;
import com.gm.project.gamelog.ranklistlog.service.IRanklistlogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 排行榜日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class RanklistlogServiceImpl implements IRanklistlogService 
{


    /**
     * 查询排行榜日志列表
     * 
     * @param ranklistlog 排行榜日志
     * @return 排行榜日志
     */
    @Override
    public List<Ranklistlog> selectRanklistlogList(Ranklistlog ranklistlog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(ranklistlog.getRankKind() != null){
            wheresql.append(" and rankKind = " + ranklistlog.getRankKind());
        }
        wheresql.append(" ");
        //自定义查询条件
        param.put("tableName","ranklistlog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Ranklistlog.class,param);
    }
}
