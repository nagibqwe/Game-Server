package com.gm.project.stat.stat_ltv.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.TableDataInfoUtil;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.common.dao.IStatRoleStateDao;
import com.gm.project.stat.stat_ltv.dao.StatLtvDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 统计-ltvService业务层处理
 *
 * @author gm
 * @date 2021-04-27
 */
@Service
public class StatLtvServiceImpl
{
    /**
     * 数据库相关操作
     */
    @Autowired
    public StatLtvDaoImpl statLtvDaoImpl;


    @Autowired
    public IStatRoleStateDao statRoleStateDao;

    Integer[] ltvDays = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28, 29,44,59,119};
    public TableDataInfo caclLtv(String selectGroupName, String selectServerIdList,String channelNames,String startDate, String endDate,Boolean isBlack){


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

        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        //获取时间列表
        List<String> dateList = DateUtils.getDateList(startDate,endDate);
        //所有记录 key 天 value 该天留存数据
        Map<String, Map<String,Object>> allResult = new HashMap<>();
        //留存列表
        List<Map<String,String>> statLtvList = new ArrayList<>();
        caclLtvCommon(dateList,statLtvList,dbClientGM,selectServerIdList,allResult);
        //最终发给客户端展示数据
        List<Map<String,String>> rows = new ArrayList<>();
        Map<String,String> statLtvMap = null;
        //当前日期
        String currDay =  DateUtils.getDate();
        for(int i = 0;i<statLtvList.size();i++){
            statLtvMap = statLtvList.get(i);
            String caclStartTime = statLtvMap.get("caclStartTime");
            int userRegCount = Integer.parseInt(statLtvMap.get("caclNewCount"));
            Map<String,Object> dayResult = allResult.get(caclStartTime);
            Map<String,Integer> ltvDayList  = (Map<String,Integer>)dayResult.get("ltvRechargeSumList");
            if(ltvDayList == null){
               //
                for(int m = 0;m<ltvDays.length;m++){
                    String ltvDay = DateUtils.getNewDateForMinute2(caclStartTime,ltvDays[m]*1440);
                    Date keepDaydate = DateUtils.parseDate(ltvDay);
                    Date currDaydate = DateUtils.parseDate(currDay);
                    if(keepDaydate.getTime() <= currDaydate.getTime()){
                        statLtvMap.put("ltv"+(ltvDays[m]+1), "0.00");
                    }
                }
            }else{
                for(int m = 0;m<ltvDays.length;m++){
                    String ltvDay = DateUtils.getNewDateForMinute2(caclStartTime,ltvDays[m]*1440);
                    if(ltvDayList == null ){
                        continue;
                    }else {
                        if(ltvDayList.containsKey(ltvDay)){ //
                            // Map<String,Integer> ltvRechargeSumList  =  (Map<String,Integer>)ltvList.get(ltvDay);
                            float ltvValue = ltvDayList.get(ltvDay)*1f/userRegCount*1f;
                            statLtvMap.put("ltv"+(ltvDays[m]+1), String.format("%.2f", ltvValue));
                        }else {
//                            Date keepDaydate = DateUtils.parseDate(ltvDay);
//                            Date currDaydate = DateUtils.parseDate(currDay);
//                            if(keepDaydate.getTime() <= currDaydate.getTime()){
//                                statLtvMap.put("ltv"+(ltvDays[m]+1), "0.00");
//                            }
                        }
                    }
                }
            }

            rows.add(statLtvMap);
        }
        return TableDataInfoUtil.getDataTable(rows);
    }

    public void caclLtvCommon(List<String> dateList,List<Map<String,String>> statLtvList,DBClient dbClientGM,String serverList,Map<String, Map<String,Object>> allResult){
        //当前日期
        String currDay =  DateUtils.getDate();

        if(dateList!= null && dateList.size() > 0){
            for(int i = 0;i< dateList.size();i++){
                //当前计算基准的某天
                String caclStartDay = dateList.get(i);
                //相差多少天
                int diffDay = DateUtils.differentDaysByMillisecond(caclStartDay,currDay);
                //留存记录
                Map<String,String> statLtvBeanMap  = new HashMap<>();
                statLtvList.add(statLtvBeanMap);
                //日期
                statLtvBeanMap.put("caclStartTime",caclStartDay);
                statLtvBeanMap.put("diffDay",diffDay+"");
                //需要计算日期 对应某天留存集合
                Map<String,Object> dayResult = new HashMap<>();
                //登录数量 为0 就不用检查留存了
                Set<String> userIdRegAddList = this.statRoleStateDao.getUserIdRegAddSet(dbClientGM,caclStartDay,serverList);
                String statLtvSql = this.statLtvDaoImpl.getUserStatLtvSql(caclStartDay,caclStartDay,DateUtils.getNewDateForMinute2(caclStartDay,119*1440),serverList, StringUtils.join(userIdRegAddList,","));
                int caclNewCount = userIdRegAddList.size();
                statLtvBeanMap.put("caclNewCount",caclNewCount+"");
                Map<String,Integer> ltvRechargeSumList = new HashMap<>();
               if(caclNewCount > 0){
                    List<Map<String, Object>>  roleStatLtvList = dbClientGM.selectList(statLtvSql);
                    if(roleStatLtvList != null){
                        int sum = 0;
//                        for(int j = 0;j<roleStatLtvList.size();j++){
//                            int recharge_sum =  Integer.parseInt(roleStatLtvList.get(j).get("recharge_sum").toString());
//                            sum+=recharge_sum;
//                            ltvRechargeSumList.put(roleStatLtvList.get(j).get("charge_date").toString(),sum);
//                        }
//                        for(int j = 0;j<dateList.size();j++){
//                            for(int m = 0;m<roleStatLtvList.size();m++){
//                                if(roleStatLtvList.get(m).get("charge_date").toString().equals(dateList.get(j))){
//                                    int recharge_sum =  Integer.parseInt(roleStatLtvList.get(m).get("recharge_sum").toString());
//                                    sum+=recharge_sum;
//                                }
//                            }
//                            ltvRechargeSumList.put(dateList.get(j),sum);
//                        }
                        //计算ltv
                        for(int n = 0;n<ltvDays.length;n++){
                            String ltvDay = DateUtils.getNewDateForMinute2(caclStartDay,ltvDays[n]*1440);
                            Date keepDaydate = DateUtils.parseDate(ltvDay);
                            Date currDaydate = DateUtils.parseDate(currDay);
                            if(keepDaydate.getTime() <= currDaydate.getTime()){
                                for(int m = 0;m<roleStatLtvList.size();m++){
                                    if(roleStatLtvList.get(m).get("charge_date").toString().equals(ltvDay)){
                                        int recharge_sum =  Integer.parseInt(roleStatLtvList.get(m).get("recharge_sum").toString());
                                        sum+=recharge_sum;
                                    }
                                }
                                ltvRechargeSumList.put(ltvDay,sum);
                            }
                        }
                        dayResult.put("ltvRechargeSumList",ltvRechargeSumList);
                    }
               }
                //将每天的留存数据放入结果集合
                allResult.put(caclStartDay,dayResult);
            }
        }
    }
}
