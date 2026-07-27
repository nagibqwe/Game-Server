package com.gm.project.stat.stat_online.dao;

import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_online.domain.AllOnlineCountBean;

import java.util.List;


public interface IStatOnlineDao {
    public List<AllOnlineCountBean> allOnlineCountList(DBClient dbClient);
}
