package com.gm.project.stat.stat_remain.service;

import com.game.util.ListToStringUtil;
import com.game.util.ThreeTuple;
import com.game.util.TwoTuple;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.StringUtils;
import com.gm.common.utils.TableDataInfoUtil;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.monitor.server.domain.Sys;
import com.gm.project.stat.common.dao.IStatRechargeDao;
import com.gm.project.stat.common.dao.IStatRoleStateDao;
import com.gm.project.stat.stat_daily_data.service.IStatDailyDataService;
import com.gm.project.stat.stat_remain.dao.StatRemainDaoImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.*;

/**
 * 留存服务
 * @author ruoyi
 */
@Service
public class StatRemainServiceImpl
{
    /**
     * 数据库相关操作
     */
    @Autowired
    public StatRemainDaoImpl statRemainDaoImpl;


    @Autowired
    public IStatDailyDataService statDailyDataService;

    @Autowired
    public IStatRoleStateDao statRoleStateDao;


    @Autowired
    public IStatRechargeDao statRechargeDao;
    /**
     * 留存统计天数
     */
    private static final int[] durDays = {1, 2, 3, 4, 5, 6, 7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,44,59,119};

    /**
     * 计算留存统计
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param selectServerIdList 服务器列表
     * @param remainType 留存类型
     * @return
     */
    public TableDataInfo caclRemain(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack,String remainType){

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

        //当前日期
        String currDay =  DateUtils.getDate();
        //所有记录 key 天 value 该天留存数据
        Map<String, Map<String,Object>> allResult = new HashMap<>();
        //留存列表
        List<Map<String,String>> statRemainList = new ArrayList<>();

        this.caclRemainCommon(startDate,endDate,selectServerIdList,remainType,allResult,statRemainList,selectGroupName,channelNames, blackUsers,isBlack);


        //最终发给客户端展示数据
        List<Map<String,String>> rows = new ArrayList<>();
        Map<String,String> statRemainMap = null;
        for(int i = 0;i<statRemainList.size();i++){
            statRemainMap = statRemainList.get(i);
            String caclStartTime = statRemainMap.get("caclStartTime");
            int userRegCount = Integer.parseInt(statRemainMap.get("caclNewCount"));
            if(userRegCount == 0){
                rows.add(statRemainMap);
                for(int m = 0;m<durDays.length;m++){
                   String keepDay = DateUtils.getNewDateForMinute2(caclStartTime,durDays[m]*1440);
                    Date keepDaydate = DateUtils.parseDate(keepDay);
                    Date currDaydate = DateUtils.parseDate(currDay);
                    if(keepDaydate.getTime() <= currDaydate.getTime()){
                        statRemainMap.put("keep"+(durDays[m]+1),"0(0%)");
                    }
                }
            }else {
                rows.add(statRemainMap);
                Map<String,Object> dayResult = allResult.get(caclStartTime);
                Map<String,Integer> keepLoginList  = (Map<String,Integer>)dayResult.get("keepLoginList");
                if(keepLoginList == null){
                    continue;
                }
                for(int m = 0;m<durDays.length;m++){
                    String keepDay = DateUtils.getNewDateForMinute2(caclStartTime,durDays[m]*1440);
                    if(keepLoginList == null ){
                        continue;
                    }else {
                        if(keepLoginList.containsKey(keepDay)){
                            statRemainMap.put("keep"+(durDays[m]+1),caclKeepRateView( keepLoginList,keepDay,userRegCount));
                        }else {
                            Date keepDaydate = DateUtils.parseDate(keepDay);
                            Date currDaydate = DateUtils.parseDate(currDay);
                            if(keepDaydate.getTime() <= currDaydate.getTime()){
                                //  statRemainMap.put("keep"+(durDays[m]+1),"");
                                statRemainMap.put("keep"+(durDays[m]+1),"0(0%)");
                            }
                        }
                    }
                }
                //当前天留存
                statRemainMap.put("currDayKeep",caclKeepRateView( keepLoginList,currDay,userRegCount));
            }
        }
        return TableDataInfoUtil.getDataTable(rows);
    }





    public void  caclRemainCommon(String startTime, String endTime, String serverList,String remainType,Map<String, Map<String,Object>> allResult, List<Map<String,String>> statRemainList,String groupName,String channelNames,  String blackUsers,Boolean isBlack){
        //获取每日数据 总览
       // TwoTuple<Map<String, Set<Object>>,Map<String, Set<Object>>> statDailyData = this.statDailyDataService.statDailyDataCommon(null,serverList,null,startTime,endTime,null,false);
        ThreeTuple< List<Map<String, Object>>,List<Map<String, Object>>,List<Map<String, Object>>>  statDailyDataCommon = this.statDailyDataService.statDailyDataCommon(groupName,serverList,channelNames,startTime,endTime,blackUsers,isBlack);
        List<Map<String, Object>> newUserList = statDailyDataCommon.first;
        //充值列表
        List<Map<String, Object>> rechargeMap =  statDailyDataCommon.second;
        //新增付费玩家人数   新增付费率=新增付费玩家人数/新增玩家人数
        List<Map<String, Object>> newRechargeUserList = statDailyDataCommon.third;
        String mapKey = "day";
        String userKey = "userId";
        Map<String, Set<Object>>  newRechargeUserMap = this.statDailyDataService.getAssembleMap(newRechargeUserList,mapKey,userKey);
        Map<String, Set<Object>> newUserNumMap = this.statDailyDataService.getAssembleMap(newUserList, mapKey, userKey);


        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        //获取时间列表
        List<String> dateList = DateUtils.getDateList(startTime,endTime);
        //当前日期
        String currDay =  DateUtils.getDate();
        if(dateList!= null && dateList.size() > 0){
            for(int i = 0;i< dateList.size();i++){
                //当前计算基准的某天
                String caclStartDay = dateList.get(i);
                //相差多少天
                int diffDay = DateUtils.differentDaysByMillisecond(caclStartDay,currDay);
                //留存记录
                Map<String,String> statRemain  = new HashMap<>();
                statRemainList.add(statRemain);
                //日期
                statRemain.put("caclStartTime",caclStartDay);
                statRemain.put("diffDay",diffDay+"");
                //需要计算日期 对应某天留存集合
                Map<String,Object> dayResult = new HashMap<>();
                //登录数量 为0 就不用检查留存了
                int caclNewCount = 0;
                //查询留存当天 到 当天120天之间的 所有角色数量
                String statRemainSql= "";
                //查询当天
                String currDayStatRemainSql = "";
                if("new_user_remain".equals(remainType)){ //新账号
                    //计算相关去重账号等逻辑
                    Set<Object> userIdRegAddList =   newUserNumMap.get(caclStartDay);  //this.statRoleStateDao.getUserIdRegAddSet(dbClientGM,caclStartDay,serverList);
                    if(userIdRegAddList!=null){
                        caclNewCount = userIdRegAddList.size();
                        statRemainSql = this.statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,caclStartDay,DateUtils.getNewDateForMinute2(caclStartDay,119*1440),serverList, StringUtils.join(userIdRegAddList,","));
                        currDayStatRemainSql = this.statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,currDay,DateUtils.getNewDateForMinute2(currDay,1440),serverList, StringUtils.join(userIdRegAddList,","));
                    }
                }else if("new_role_remain".equals(remainType)){ //新创建角色
                    caclNewCount = this.statRemainDaoImpl.getNewRoleNum(dbClientGM,caclStartDay,serverList);
                    statRemainSql= statRemainDaoImpl.getNewRoleStatRemainSqlSql(caclStartDay,caclStartDay,DateUtils.getNewDateForMinute2(caclStartDay,119*1440),serverList);
                    currDayStatRemainSql= statRemainDaoImpl.getNewRoleStatRemainSqlSql(caclStartDay,currDay,DateUtils.getNewDateForMinute2(currDay,1440),serverList);
                }else if("old_user_remain".equals(remainType)){ //老用户
                    Set<String>  oldUserIdRegAddList = this.statRemainDaoImpl.getOldUserIdRegAddSet(dbClientGM,caclStartDay,serverList);
                    caclNewCount = oldUserIdRegAddList.size();
                    statRemainSql= statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,caclStartDay,DateUtils.getNewDateForMinute2(caclStartDay,119*1440),serverList,StringUtils.join(oldUserIdRegAddList,","));
                    currDayStatRemainSql= statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,currDay,DateUtils.getNewDateForMinute2(currDay,1440),serverList,StringUtils.join(oldUserIdRegAddList,","));
                }else if("new_user_pay_remain".equals(remainType)){ //付费新增用户
                    //得到新注册的用户id
//                    Set<Object> userIdRegAddList = statDailyData.second.get(caclStartDay); //this.statRoleStateDao.getUserIdRegAddSet(dbClientGM,caclStartDay,serverList);
//                    Set<String> payNewUserIdRegAddList = null;
//                    String payUserIdRegAddListStr = "";
//                    if(userIdRegAddList!= null && userIdRegAddList.size()>0){
//                        payNewUserIdRegAddList = this.statRemainDaoImpl.gePayNewUserIdRegAddSet(dbClientGM,caclStartDay,serverList,userIdRegAddList);
//                        payUserIdRegAddListStr = StringUtils.join(payNewUserIdRegAddList,",");
//                        caclNewCount = payNewUserIdRegAddList.size();
//                    }
                    Set<Object> payNewUserIdRegAddList = newRechargeUserMap.get(caclStartDay);
                    String payUserIdRegAddListStr = "";
                    if(payNewUserIdRegAddList!=null){
                        payUserIdRegAddListStr = StringUtils.join(payNewUserIdRegAddList,",");
                        caclNewCount = payNewUserIdRegAddList.size();
                        statRemainSql= statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,caclStartDay,DateUtils.getNewDateForMinute2(caclStartDay,119*1440),serverList,payUserIdRegAddListStr);
                        currDayStatRemainSql= statRemainDaoImpl.getUserStatRemainSqlSql(caclStartDay,currDay,DateUtils.getNewDateForMinute2(currDay,1440),serverList,payUserIdRegAddListStr);
                    }


                }
                statRemain.put("caclNewCount",caclNewCount+"");
                //基准数量大于0 才去计算之后留存
                if(caclNewCount > 0){
                    Map<String,Integer> keepLoginList = new HashMap<>();
                    //执行结果集
                    this.keepLoginList(dbClientGM,keepLoginList,statRemainSql);
                    //计算当天
                    this.keepLoginList(dbClientGM,keepLoginList,currDayStatRemainSql);
                    dayResult.put("keepLoginList",keepLoginList);
                }
                //将每天的留存数据放入结果集合
                allResult.put(caclStartDay,dayResult);
            }
        }

    }


    /**
     *  计算留存显示
     * @param keepLoginList
     * @param keepDay
     * @param userRegCount
     * @return
     */
    public String caclKeepRateView(Map<String,Integer> keepLoginList,String keepDay,int userRegCount){
        if(keepLoginList == null || keepLoginList.size() == 0){
            return "";
        }
        if(!keepLoginList.containsKey(keepDay)){
            return "";
        }
        float keepRate = keepLoginList.get(keepDay)*1f/userRegCount;
        return keepLoginList.get(keepDay)+ "("+String.format("%.2f", keepRate*100)+ "%)";
    }
    /**
     * 计算某天的 登录个数列表
     * @param dbClientGM
     * @param keepLoginList
     * @param statRemainSql
     */
    public void keepLoginList(DBClient dbClientGM,Map<String,Integer> keepLoginList,String statRemainSql){
        List<Map<String, Object>>  roleStatRemainList = dbClientGM.selectList(statRemainSql);

        if(roleStatRemainList != null){
            for(int j = 0;j<roleStatRemainList.size();j++){
                keepLoginList.put(roleStatRemainList.get(j).get("date").toString(),Integer.parseInt(roleStatRemainList.get(j).get("count").toString()));
            }
        }
    }
}
