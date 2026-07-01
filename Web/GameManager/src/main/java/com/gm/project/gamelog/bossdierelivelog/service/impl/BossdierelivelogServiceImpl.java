package com.gm.project.gamelog.bossdierelivelog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.bossdierelivelog.domain.Bossdierelivelog;
import com.gm.project.gamelog.bossdierelivelog.service.IBossdierelivelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 首领死亡复活日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-10
 */
@Service
public class BossdierelivelogServiceImpl implements IBossdierelivelogService 
{


    /**
     * 查询首领死亡复活日志列表
     * 
     * @param bossdierelivelog 首领死亡复活日志
     * @return 首领死亡复活日志
     */
    @Override
    public List<Bossdierelivelog> selectBossdierelivelogList(Bossdierelivelog bossdierelivelog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件

        if(bossdierelivelog.getType() != null){
            wheresql.append(" and type = " + bossdierelivelog.getType());
        }
        if(bossdierelivelog.getBossId() != null){
            wheresql.append(" and bossId = " + bossdierelivelog.getBossId());
        }
        if(bossdierelivelog.getMapId() != null){
            wheresql.append(" and mapId = " + bossdierelivelog.getMapId());
        }
        if(bossdierelivelog.getParam() != null){
            wheresql.append(" and param = " + bossdierelivelog.getParam());
        }
        //自定义查询条件
        param.put("tableName","bossdierelivelog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Bossdierelivelog.class,param);
    }
}
