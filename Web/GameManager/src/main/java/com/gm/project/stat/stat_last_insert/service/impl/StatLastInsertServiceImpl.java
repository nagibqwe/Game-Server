package com.gm.project.stat.stat_last_insert.service.impl;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.project.stat.stat_last_insert.dao.IStatLastInsertDao;
import com.gm.project.stat.stat_last_insert.domain.StatLastInsertBean;
import com.gm.project.stat.stat_last_insert.service.IStatLastInsertService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  日志拉取记录 Service业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatLastInsertServiceImpl implements IStatLastInsertService
{
    @Autowired
    private IStatLastInsertDao statLastInsertDao;
    public List<StatLastInsertBean> selectAllStatLastInsert(Integer serverId){
        List<StatLastInsertBean> list = this.statLastInsertDao.getStatLastInsertList(serverId);
        return list;
    }

    public int deleteStatLastInsertByIds(String ids){
        return  this.statLastInsertDao.deleteStatLastInsertByIds(ids);
    }
}
