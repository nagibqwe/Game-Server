package com.gm.project.gamelog.itemchangelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.itemchangelog.domain.Itemchangelog;

/**
 * 物品变化日志Service接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface IItemchangelogService 
{

    /**
     * 查询物品变化日志列表
     * 
     * @param itemchangelog 物品变化日志
     * @return 物品变化日志集合
     */
    public List<Itemchangelog> selectItemchangelogList(Itemchangelog itemchangelog,Map<String, Object> param);

}
