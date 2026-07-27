package com.gm.project.gamelog.bossdierelivelog.service;

import java.util.List;
import java.util.Map;
import com.gm.project.gamelog.bossdierelivelog.domain.Bossdierelivelog;

/**
 * 首领死亡复活日志Service接口
 * 
 * @author gm
 * @date 2021-09-10
 */
public interface IBossdierelivelogService 
{

    /**
     * 查询首领死亡复活日志列表
     * 
     * @param bossdierelivelog 首领死亡复活日志
     * @return 首领死亡复活日志集合
     */
    public List<Bossdierelivelog> selectBossdierelivelogList(Bossdierelivelog bossdierelivelog,Map<String, Object> param);

}
