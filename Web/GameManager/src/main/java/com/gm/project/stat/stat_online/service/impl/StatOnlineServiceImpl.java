package com.gm.project.stat.stat_online.service.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.stat.stat_online.dao.IStatOnlineDao;
import com.gm.project.stat.stat_online.domain.AllOnlineCountBean;
import com.gm.project.stat.stat_online.service.IStatOnlineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 商店统计 服务
 * @author ruoyi
 */
@Service
public class StatOnlineServiceImpl implements IStatOnlineService
{
    @Autowired
    public IStatOnlineDao statOnlineDao;
    public  List<AllOnlineCountBean> statAllOnlineCountList(){
        DBClient dbClient = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.GM);
        return  this.statOnlineDao.allOnlineCountList(dbClient);
    }
}
