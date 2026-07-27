package com.gm.project.gamelog.moneychangelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.moneychangelog.domain.Moneychangelog;

/**
 * 货币变化日志Service接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface IMoneychangelogService 
{

    /**
     * 查询货币变化日志列表
     * 
     * @param moneychangelog 货币变化日志
     * @return 货币变化日志集合
     */
    public List<Moneychangelog> selectMoneychangelogList(Moneychangelog moneychangelog,Map<String, Object> param);

}
