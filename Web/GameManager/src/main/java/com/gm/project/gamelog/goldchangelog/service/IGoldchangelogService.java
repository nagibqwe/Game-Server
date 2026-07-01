package com.gm.project.gamelog.goldchangelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.goldchangelog.domain.Goldchangelog;

/**
 * 元宝变化日志Service接口
 * 
 * @author gm
 * @date 2021-09-11
 */
public interface IGoldchangelogService 
{

    /**
     * 查询元宝变化日志列表
     * 
     * @param goldchangelog 元宝变化日志
     * @return 元宝变化日志集合
     */
    public List<Goldchangelog> selectGoldchangelogList(Goldchangelog goldchangelog,Map<String, Object> param);

}
