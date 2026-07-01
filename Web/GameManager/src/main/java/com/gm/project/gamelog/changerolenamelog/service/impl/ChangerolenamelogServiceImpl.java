package com.gm.project.gamelog.changerolenamelog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.changerolenamelog.domain.Changerolenamelog;
import com.gm.project.gamelog.changerolenamelog.service.IChangerolenamelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 改名日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class ChangerolenamelogServiceImpl implements IChangerolenamelogService 
{


    /**
     * 查询改名日志列表
     * 
     * @param changerolenamelog 改名日志
     * @return 改名日志
     */
    @Override
    public List<Changerolenamelog> selectChangerolenamelogList(Changerolenamelog changerolenamelog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        param.put("tableName","changerolenamelog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Changerolenamelog.class,param);
    }
}
