package com.gm.project.gamelog.backgmcmdlog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.backgmcmdlog.domain.Backgmcmdlog;

/**
 * 后台指令日志Service接口
 * 
 * @author gm
 * @date 2021-09-10
 */
public interface IBackgmcmdlogService 
{

    /**
     * 查询后台指令日志列表
     * 
     * @param backgmcmdlog 后台指令日志
     * @return 后台指令日志集合
     */
    public List<Backgmcmdlog> selectBackgmcmdlogList(Backgmcmdlog backgmcmdlog,Map<String, Object> param);

}
