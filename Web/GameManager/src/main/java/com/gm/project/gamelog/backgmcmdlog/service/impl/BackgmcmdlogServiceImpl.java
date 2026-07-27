package com.gm.project.gamelog.backgmcmdlog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.backgmcmdlog.domain.Backgmcmdlog;
import com.gm.project.gamelog.backgmcmdlog.service.IBackgmcmdlogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 后台指令日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-10
 */
@Service
public class BackgmcmdlogServiceImpl implements IBackgmcmdlogService 
{


    /**
     * 查询后台指令日志列表
     * 
     * @param backgmcmdlog 后台指令日志
     * @return 后台指令日志
     */
    @Override
    public List<Backgmcmdlog> selectBackgmcmdlogList(Backgmcmdlog backgmcmdlog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");

        //自定义查询条件
        param.put("tableName","backgmcmdlog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Backgmcmdlog.class,param);
    }
}
