package com.gm.project.stat.stat_role.dao;


import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_role.domain.RoleInfoBean;

import java.util.List;

public interface IStatRoleDao {
    public List<RoleInfoBean> getRoleStateList(DBClient dbClient, String table, String channelNames, String sortType, int pageIndex, int pageSize);
    public int getRoleStateCount(DBClient dbClient,String table, String channelNames);
}
