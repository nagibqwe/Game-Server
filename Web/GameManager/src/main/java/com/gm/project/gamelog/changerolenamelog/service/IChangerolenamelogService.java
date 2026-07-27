package com.gm.project.gamelog.changerolenamelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.changerolenamelog.domain.Changerolenamelog;

/**
 * 改名日志Service接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface IChangerolenamelogService 
{

    /**
     * 查询改名日志列表
     * 
     * @param changerolenamelog 改名日志
     * @return 改名日志集合
     */
    public List<Changerolenamelog> selectChangerolenamelogList(Changerolenamelog changerolenamelog,Map<String, Object> param);

}
