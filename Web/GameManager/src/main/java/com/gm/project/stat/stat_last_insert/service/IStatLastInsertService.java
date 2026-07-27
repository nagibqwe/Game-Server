package com.gm.project.stat.stat_last_insert.service;



import com.gm.project.stat.stat_last_insert.domain.StatLastInsertBean;

import java.util.List;

/**
 *
 * 
 * @author gm
 * @date 2021-09-13
 */
public interface IStatLastInsertService
{
    public List<StatLastInsertBean> selectAllStatLastInsert(Integer serverId);

    public int deleteStatLastInsertByIds(String ids);
}
