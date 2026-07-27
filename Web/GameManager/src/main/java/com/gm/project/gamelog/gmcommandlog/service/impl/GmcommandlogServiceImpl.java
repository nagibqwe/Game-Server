package com.gm.project.gamelog.gmcommandlog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.gmcommandlog.domain.Gmcommandlog;
import com.gm.project.gamelog.gmcommandlog.service.IGmcommandlogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * gm命令日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class GmcommandlogServiceImpl implements IGmcommandlogService 
{


    /**
     * 查询gm命令日志列表
     * 
     * @param gmcommandlog gm命令日志
     * @return gm命令日志
     */
    @Override
    public List<Gmcommandlog> selectGmcommandlogList(Gmcommandlog gmcommandlog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        param.put("tableName","gmcommandlog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Gmcommandlog.class,param);
    }
}
