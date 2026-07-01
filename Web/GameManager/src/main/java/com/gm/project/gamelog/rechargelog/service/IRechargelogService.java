package com.gm.project.gamelog.rechargelog.service;

import java.util.List;
import java.util.Map;

import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gamelog.rechargelog.domain.Rechargelog;

/**
 * 充值日志Service接口
 * 
 * @author gm
 * @date 2021-09-09
 */
public interface IRechargelogService 
{

    /**
     * 查询充值日志列表
     * 
     * @param rechargelog 充值日志
     * @return 充值日志集合
     */
    public List<Rechargelog> selectRechargelogList(Rechargelog rechargelog, Map<String, Object> param);

}
