package com.gm.project.stat.stat_online.dao.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.project.stat.stat_online.dao.IStatOnlineDao;
import com.gm.project.stat.stat_online.domain.AllOnlineCountBean;
import com.gm.project.stat.stat_shop_item.domain.ShopItemBean;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatOnlineDaoImpl implements IStatOnlineDao {

    /**
     * 所有服务器统计
     * @param dbClient
     * @return
     */
    public  List<AllOnlineCountBean> allOnlineCountList(DBClient dbClient){
        String sqlStr = "SELECT a.serverId,a.day,a.hour,a.min,a.num,a.time FROM t_servernum a,(SELECT b.serverid,MAX(b.time) as maxtime FROM t_servernum b where b.time>unix_timestamp(now()) - 3600*1 GROUP BY b.serverid) c WHERE a.serverid = c.serverid AND a.time = c.maxtime order by a.num desc";
        if(dbClient == null){
            return null;
        }
        List<AllOnlineCountBean> mapList = dbClient.selectList(sqlStr.toString(), AllOnlineCountBean.class);
        return mapList;
    }

}
