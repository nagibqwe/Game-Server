package com.gm.project.stat.stat_last_insert.dao;


import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_last_insert.domain.StatLastInsertBean;

import java.util.List;

/**
 * 日志收集记录
 * 
 * @author gm
 * @date 2021-08-06
 */
public interface IStatLastInsertDao
{

    /**
     * 日志收集记录 获取数据
     * @return
     */
    public List<StatLastInsertBean> getStatLastInsertList(Integer serverId);


    public int deleteStatLastInsertByIds(String ids);
}
