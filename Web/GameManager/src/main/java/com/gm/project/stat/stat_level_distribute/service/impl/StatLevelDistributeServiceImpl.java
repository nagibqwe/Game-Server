package com.gm.project.stat.stat_level_distribute.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;

import com.gm.project.stat.stat_level_distribute.dao.IStatLevelDistributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gm.project.stat.stat_level_distribute.domain.StatLevelDistribute;
import com.gm.project.stat.stat_level_distribute.service.IStatLevelDistributeService;


/**
 * 角色等级分布Service业务层处理
 * 
 * @author gm
 * @date 2021-08-06
 */
@Service
public class StatLevelDistributeServiceImpl implements IStatLevelDistributeService 
{

    /**
     * 数据库相关操作
     */
    @Autowired
    public IStatLevelDistributeDao statLevelDistributeDao;
    /**
     * 查询角色等级分布列表
     * 
     * @param statLevelDistribute 角色等级分布
     * @return 角色等级分布
     */
    @Override
    public List<StatLevelDistribute> selectStatLevelDistributeList(String channelNames,  Integer condition, Integer level,StatLevelDistribute statLevelDistribute,String startDate,String endDate,Integer selectServerId)
    {
        List<StatLevelDistribute> statLevelDistributeList = null;
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
         if(condition == 0){   //离线时候的等级分布
             String tableName = "roleloginlog";
             List<String> goalTables = DBServerMgr.getInstance().getQueryTables(tableName, TableType.Month, startDate, endDate);
             Map<String, List<String>> hefuTableMap = new TreeMap<>(DBServerMgr.getInstance().getHefuTable(selectServerId, tableName,  DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate),  DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate)));
             for (String key : hefuTableMap.keySet()) {
                 List<String> tableList = hefuTableMap.get(key);
                 tableList.retainAll(goalTables);//过滤重复数据表
                 for (String table : tableList) {
                     DBClient logDBClient = DBServerMgr.getInstance().getLogDBClient(Integer.parseInt(key));
                     if (logDBClient == null) {
                         continue;
                     }
                     statLevelDistributeList = this.statLevelDistributeDao.getLevelDistributeListByCondition0(logDBClient,table,channelNames,selectServerId+"",level+"",startDate,endDate);
                 }
             }

         }else if(condition == 1) {   //离线后加上离线挂机的等级分布
             DBClient logDBClient = DBServerMgr.getInstance().getLogDBClient(selectServerId);
             statLevelDistributeList = this.statLevelDistributeDao.getLevelDistributeListByCondition1Sql(logDBClient,channelNames,selectServerId+"",level+"",startDate,endDate);
         }
         if(statLevelDistributeList == null){
             statLevelDistributeList = new ArrayList<>();
         }
         return statLevelDistributeList;
    }


}
