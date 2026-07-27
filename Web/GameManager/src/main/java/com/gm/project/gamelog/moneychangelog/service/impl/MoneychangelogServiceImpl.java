package com.gm.project.gamelog.moneychangelog.service.impl;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.moneychangelog.domain.Moneychangelog;
import com.gm.project.gamelog.moneychangelog.service.IMoneychangelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 货币变化日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class MoneychangelogServiceImpl implements IMoneychangelogService 
{


    /**
     * 查询货币变化日志列表
     * 
     * @param moneychangelog 货币变化日志
     * @return 货币变化日志
     */
    @Override
    public List<Moneychangelog> selectMoneychangelogList(Moneychangelog moneychangelog,Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(moneychangelog.getRoleId() != null){
            wheresql.append(" and roleId = " + moneychangelog.getRoleId());
        }
        if(moneychangelog.getMonyeyType() != null){
            wheresql.append(" and monyeyType = " + moneychangelog.getMonyeyType());
        }

        if(moneychangelog.getReason() != null){
            wheresql.append(" and reason = " + moneychangelog.getReason());
        }
        //自定义查询条件
        param.put("tableName","moneychangelog");
        param.put("where",wheresql);
        return GameLogUtil.getLogDataList(Moneychangelog.class,param);
    }
}
