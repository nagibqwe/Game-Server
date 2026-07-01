package com.gm.project.stat.stat_gold_purpose.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.dbclient.TableType;
import com.gm.common.utils.DateUtils;
import com.gm.project.gmtool.changereason.service.impl.TChangereasonServiceImpl;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.monitor.server.domain.Sys;
import com.gm.project.stat.stat_gold_purpose.dao.IStatGoldPurposeDao;
import com.gm.project.stat.stat_gold_purpose.domain.GoldPurposeBean;
import com.gm.project.stat.stat_gold_purpose.service.IStatGoldPurposeService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  元宝消费统计 Service业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatGoldPurposeServiceImpl implements IStatGoldPurposeService
{
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    private IStatGoldPurposeDao statRechargeSecondDao;
    public List<GoldPurposeBean> statGoldPurpose(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack,Integer goldType){

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        try {
            start.setTime(sdf.parse(startDate));
            end.setTime(sdf.parse(endDate));
            end.add(Calendar.DAY_OF_MONTH, 1);
            end.setTimeInMillis(end.getTimeInMillis() - 1000L);
        } catch (ParseException e) {
           // log.error(e.getMessage(), e);
        }

        //渠道名称
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单排除
        String blackUsers = "";
        if (isBlack!=null && isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(selectGroupName);
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        String table = "goldchangelog";
        if (goldType == 2) {
            table = "bindgoldchangelog";
        }
        //获取时间列表
        List<String> queryTables = DBServerMgr.getInstance().getQueryTables(table, TableType.Month,start,end);
        String[] serverArray = selectServerIds.split(",");
        Map<String, List<String>> hefuTableMap = new HashMap<>();
        for(int i = 0;i< serverArray.length;i++){
            Map<String, List<String>> temphefuTableMap =  DBServerMgr.getInstance().getHefuTable(Integer.parseInt(serverArray[i]), table,  DateUtils.dateTime(DateUtils.YYYY_MM_DD,startDate),  DateUtils.dateTime(DateUtils.YYYY_MM_DD,endDate));
            hefuTableMap.putAll(temphefuTableMap);
        }
        List<String> tableList;
        List<GoldPurposeBean> allDataList = new ArrayList<>();
        for (String sid : hefuTableMap.keySet()) {
            tableList = hefuTableMap.get(sid);
            tableList.retainAll(queryTables);//过滤重复数据表
            for (String s : tableList) {

                DBClient dBClient = DBServerMgr.getInstance().getLogDBClient(Integer.parseInt(sid));
                if(dBClient == null){
                    continue;
                }
                List<GoldPurposeBean> dataList = this.statRechargeSecondDao.getGoldConsumeList(dBClient,s,sid,channelNames,startDate,endDate,blackUsers);
                if(dataList!=null && dataList.size()>0){
                    for(int i = 0;i<dataList.size();i++){
                        String reason = dataList.get(i).getReason();

                        dataList.get(i).setReason(TChangereasonServiceImpl.getInstance().getReason( reason));
                      //  map.put("reason", ReasonManager.getInstance().getReasonMap().get(reason) + "[" + reason + "]");
                        allDataList.add(dataList.get(i));
                    }
                }
            }
        }
        return  allDataList;
    }
}
