package com.gm.project.stat.stat_last_insert.dao.impl;


import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.stat.common.dao.BaseDao;
import com.gm.project.stat.stat_last_insert.dao.IStatLastInsertDao;
import com.gm.project.stat.stat_last_insert.domain.StatLastInsertBean;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;


/**
 * 日志收集记录dao
 * 
 * @author gm
 * @date 2021-08-06
 */

@Service
public class StatLastInsertDaoImpl extends BaseDao implements IStatLastInsertDao
{
    private static final long serialVersionUID = 1L;


    public List<StatLastInsertBean> getStatLastInsertList(Integer serverId){
        StringBuilder str = new StringBuilder();
        str.append("select * from stat_last_insert where 1 = 1");
        str.append(" and sid = "+serverId+"");
        //str.append("group by sid");
        DBClient dbClientStat = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientStat == null){
            return null;
        }
        List<StatLastInsertBean> mapList = dbClientStat.selectList(str.toString(), StatLastInsertBean.class);
        return mapList;
    }
    public int deleteStatLastInsertByIds(String ids){
        StringBuilder str = new StringBuilder();
        str.append("DELETE FROM stat_last_insert WHERE 1 = 1");
        str.append(" and id = "+ids+"");

        DBClient dbClientStat = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        if(dbClientStat == null){
            return 0;
        }
        try {
            return dbClientStat.executeUpdate(str.toString());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return 0;
    }

}
