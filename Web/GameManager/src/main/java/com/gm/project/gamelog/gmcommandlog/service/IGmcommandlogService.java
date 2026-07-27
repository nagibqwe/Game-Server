package com.gm.project.gamelog.gmcommandlog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.gmcommandlog.domain.Gmcommandlog;

/**
 * gm命令日志Service接口
 * 
 * @author gm
 * @date 2021-09-08
 */
public interface IGmcommandlogService 
{

    /**
     * 查询gm命令日志列表
     * 
     * @param gmcommandlog gm命令日志
     * @return gm命令日志集合
     */
    public List<Gmcommandlog> selectGmcommandlogList(Gmcommandlog gmcommandlog,Map<String, Object> param);

}
