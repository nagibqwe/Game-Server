package com.gm.project.gamelog.rechargelog.service.impl;

import java.util.List;
import java.util.Map;

import com.gm.common.utils.StringUtils;
import com.gm.framework.web.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.gamelog.rechargelog.domain.Rechargelog;
import com.gm.project.gamelog.rechargelog.service.IRechargelogService;
import com.gm.common.utils.text.Convert;
import com.gm.project.common.utils.GameLogUtil;
/**
 * 充值日志Service业务层处理
 * 
 * @author gm
 * @date 2021-09-09
 */
@Service
public class RechargelogServiceImpl implements IRechargelogService 
{


    /**
     * 查询充值日志列表
     * 
     * @param rechargelog 充值日志
     * @return 充值日志
     */
    @Override
    public List<Rechargelog> selectRechargelogList(Rechargelog rechargelog, Map<String, Object> param)
    {
        StringBuilder wheresql = new StringBuilder(" where 1 = 1");
        //自定义查询条件
        if(!StringUtils.isEmpty(rechargelog.getOrderNo())){
            wheresql.append(" and orderNo = " + rechargelog.getOrderNo());
        }
        if(rechargelog.getRoleId() != null){
            wheresql.append(" and roleId = " + rechargelog.getRoleId());
        }
        //自定义查询条件
        param.put("tableName","rechargelog");
        param.put("where",wheresql);
        return GameLogUtil.getHeFuLogDataList(Rechargelog.class,param);
    }
}
