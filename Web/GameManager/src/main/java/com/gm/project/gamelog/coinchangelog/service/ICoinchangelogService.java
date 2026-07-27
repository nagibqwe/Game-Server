package com.gm.project.gamelog.coinchangelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.coinchangelog.domain.Coinchangelog;

/**
 * 货币变化日志Service接口
 * 
 * @author gm
 * @date 2021-11-08
 */
public interface ICoinchangelogService 
{

    /**
     * 查询货币变化日志列表
     * 
     * @param coinchangelog 货币变化日志
     * @return 货币变化日志集合
     */
    public List<Coinchangelog> selectCoinchangelogList(Coinchangelog coinchangelog,Map<String, Object> param);

}
