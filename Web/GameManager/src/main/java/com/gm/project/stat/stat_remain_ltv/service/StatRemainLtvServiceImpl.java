package com.gm.project.stat.stat_remain_ltv.service;

import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.DateUtils;
import com.gm.common.utils.TableDataInfoUtil;
import com.gm.framework.web.page.TableDataInfo;
import com.gm.project.stat.stat_ltv.service.impl.StatLtvServiceImpl;
import com.gm.project.stat.stat_remain.dao.StatRemainDaoImpl;
import com.gm.project.stat.stat_remain.service.StatRemainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StatRemainLtvServiceImpl {

    /**
     * 数据库相关操作
     */
    @Autowired
    public StatRemainDaoImpl statRemainDaoImpl;
    @Autowired
    private StatRemainServiceImpl statRemainService;
    @Autowired
    private StatLtvServiceImpl statLtvServiceImpl;

    /**
     * 留存统计天数
     */
    private static final int[] durDays = {1, 2, 3, 4, 5, 6, 7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,43,44,58,59,118,119};

    /**
     * Ltv统计天数
     */
    private static final int[] ltvDays = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,43,44,58,59,118,119};
    private static final String[] numType = {"平均LTV","当日LTV较前日增长率((今日-昨日)/昨日)","平均LTV增长倍率","当日新增LTV对比首日((今日-昨日)/首日)","当日增长LTV对比前日增长LTV趋势((今日-昨日)/(昨日-前日))",
            "平均留存率","平均留存衰减趋势(今日/昨日)","平均付费留存","平均付费留存衰减趋势(今日/昨日)"};

    public TableDataInfo caclRemainLtv(String startDate, String endDate, String serverList,String remainType,String remainType2){

        //region(留存相关数据)
        //当前日期
        String currDay =  DateUtils.getDate();
        //所有记录 key 天 value 该天留存数据
        Map<String, Map<String,Object>> allStatNewUserRemainResult = new HashMap<>();
        //新用户留存
        List<Map<String,String>> statNewUserRemainList = new ArrayList<>();
        statRemainService.caclRemainCommon(startDate,endDate,serverList,remainType,allStatNewUserRemainResult,statNewUserRemainList,"","","",false);

        //所有记录 key 天 value 该天留存数据
        Map<String, Map<String,Object>> allStatUserPayRemainResult = new HashMap<>();
        //付费用户留存
        List<Map<String,String>> statUserPayRemainList = new ArrayList<>();
        statRemainService.caclRemainCommon(startDate,endDate,serverList,remainType2,allStatUserPayRemainResult,statUserPayRemainList,"","","",false);
        //endregion


        //region(LTV相关数据)
        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        //获取时间列表
        List<String> dateList = DateUtils.getDateList(startDate,endDate);
        //所有记录 key 天 value 该天留存数据
        Map<String, Map<String,Object>> allStatLtvResult = new HashMap<>();
        //留存列表
        List<Map<String,String>> statLtvList = new ArrayList<>();

        statLtvServiceImpl.caclLtvCommon(dateList,statLtvList,dbClientGM,serverList,allStatLtvResult);
        //endregion

        //最终发给客户端展示数据
        List<Map<String,String>> resultRows = new ArrayList<>();
        Map<String,String> avgLtv = getAvgLtv(allStatLtvResult,statLtvList);//平均ltv
        Map<String,String> currentLtvRate = getCurrentLtvRate(avgLtv,numType[1]);//当日LTV较前日增长率((今日-昨日)/昨日)
        Map<String,String> avgLtvIncreaseRate = getAvgLtvIncreaseRate(avgLtv,numType[2]);//平均LTV增长倍率(今日/首日)
        Map<String,String> currentIncreaseLtv = getCurrentIncreaseLtv(avgLtv,numType[3]);//当日新增LTV对比首日((今日-昨日)/首日)
        Map<String,String> currentIncreaseLtv2 = getCurrentIncreaseLtv2(avgLtv,numType[4]);//当日增长LTV对比前日增长LTV趋势((今日-昨日)/(昨日-前日))
        Map<String,String> avgRemain = getAvgRemain(allStatNewUserRemainResult,statNewUserRemainList,numType[5]);//平均留存率
        Map<String,String> avgRemainTrend = getAvgRemainTrend(avgRemain,numType[6]);//平均留存衰减趋势(今日/昨日)
        Map<String,String> avgPayRemain = getAvgRemain(allStatUserPayRemainResult,statUserPayRemainList,numType[7]);//平均付费留存
        Map<String,String> avgPayRemainTrend = getAvgRemainTrend(avgPayRemain,numType[8]);//平均付费留存衰减趋势(今日/昨日)

        resultRows.add(avgLtv);
        resultRows.add(currentLtvRate);
        resultRows.add(avgLtvIncreaseRate);
        resultRows.add(currentIncreaseLtv);
        resultRows.add(currentIncreaseLtv2);
        resultRows.add(avgRemain);
        resultRows.add(avgRemainTrend);
        resultRows.add(avgPayRemain);
        resultRows.add(avgPayRemainTrend);

        return TableDataInfoUtil.getDataTable(resultRows);

    }


    /**
     * 平均LTV
     * @param allStatLtvResult
     * @param statLtvList
     */
    private Map<String,String> getAvgLtv(Map<String, Map<String,Object>> allStatLtvResult,List<Map<String,String>> statLtvList){
        List<Map<String,String>> rows = new ArrayList<>();
        //当前日期
        String currDay =  DateUtils.getDate();
        Map<String,String> statLtvMap = null;
        for(int i = 0;i<statLtvList.size();i++){
            statLtvMap = statLtvList.get(i);
            String caclStartTime = statLtvMap.get("caclStartTime");
            int userRegCount = Integer.parseInt(statLtvMap.get("caclNewCount"));
            Map<String,Object> dayResult = allStatLtvResult.get(caclStartTime);
            Map<String,Integer> ltvDayList  = (Map<String,Integer>)dayResult.get("ltvRechargeSumList");
            if(ltvDayList == null){
                continue;
            }
            for(int m = 0;m<ltvDays.length;m++){
                String ltvDay = DateUtils.getNewDateForMinute2(caclStartTime,ltvDays[m]*1440);
                if(ltvDayList == null ){
                    continue;
                }else {
//                    Date ltvDaydate = DateUtils.parseDate(ltvDay);
//                    Date currDaydate = DateUtils.parseDate(currDay);
//                    if(ltvDaydate.getTime() >= currDaydate.getTime()){
//                        //  statRemainMap.put("keep"+(durDays[m]+1),"");
//                    }else {
                        if(ltvDayList.containsKey(ltvDay)){ //
                            // Map<String,Integer> ltvRechargeSumList  =  (Map<String,Integer>)ltvList.get(ltvDay);
                            float ltvValue = ltvDayList.get(ltvDay)*1f/userRegCount*1f;
                            statLtvMap.put("keep"+(ltvDays[m]+1), String.format("%.2f", ltvValue));
                        }
//                    }
                }
            }
            rows.add(statLtvMap);//ltv统计的原始数据
        }

        Map<String,String> ltvMap = new HashMap<>();
        Map<String,Integer> countMap = new HashMap<>();
        Map<String,String> resultMap = new HashMap<>();
        Integer preCount = 0;
        getRemainLtvMap(ltvMap,countMap,preCount,rows);
        //平均LTV = 每日ltv总值/对应的个数
        String avgLtv = numType[0];
        resultMap.put("numType",avgLtv);
        for (Map.Entry<String,String> entry:ltvMap.entrySet()){
            if (entry.getKey().startsWith("keep")){
                float totalLtv = Float.valueOf(entry.getValue());
                int count = countMap.get(entry.getKey());
                resultMap.put(entry.getKey(),String.format("%.2f",totalLtv/count));
            }
        }
        return resultMap;
    }

    private Map<String,String> getCurrentLtvRate(Map<String,String> avgLtv,String numType){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:avgLtv.entrySet()){
            if (entry.getKey().startsWith("keep")){
                int today = Integer.parseInt(entry.getKey().split("keep")[1]);//当前天
                if (today > 1){
                    int preDay = today - 1;//前一天
                    if (entry.getValue() != null && avgLtv.get("keep"+preDay) != null){
                        float preDayKeep = Float.parseFloat(avgLtv.get("keep"+preDay));
                        float todayKeep = Float.parseFloat(entry.getValue());
                        if (preDayKeep != 0){
                            resultMap.put(entry.getKey(),String.format("%.2f",(todayKeep-preDayKeep)/preDayKeep*100)+"%");
                        }else {
                            resultMap.put(entry.getKey(),"0.00%");
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    private Map<String,String> getAvgLtvIncreaseRate(Map<String,String> avgLtv,String numType){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:avgLtv.entrySet()){
            if (entry.getKey().startsWith("keep")){
                int today = Integer.parseInt(entry.getKey().split("keep")[1]);//当前天
                if (today > 1){
                    float todayKeep = Float.parseFloat(entry.getValue());
                    float firstDayKeep = Float.parseFloat(avgLtv.get("keep1"));
                    resultMap.put(entry.getKey(),String.format("%.2f", (todayKeep/firstDayKeep)*100) + "%");
                }
            }
        }
        return resultMap;
    }

    private Map<String,String> getCurrentIncreaseLtv(Map<String,String> avgLtv,String numType){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:avgLtv.entrySet()){
            if (entry.getKey().startsWith("keep")){
                int today = Integer.parseInt(entry.getKey().split("keep")[1]);//当前天
                if (today > 1){
                    int preDay = today - 1;//前一天
                    if (entry.getValue() != null && avgLtv.get("keep"+preDay) != null){
                        float todayKeep = Float.parseFloat(entry.getValue());
                        float preDayKeep = Float.parseFloat(avgLtv.get("keep"+preDay));
                        float firstDayKeep = Float.parseFloat(avgLtv.get("keep1"));
                        resultMap.put(entry.getKey(),String.format("%.2f", ((todayKeep-preDayKeep)/firstDayKeep)*100) + "%");
                    }
                }
            }
        }
        return resultMap;
    }

    private Map<String,String> getCurrentIncreaseLtv2(Map<String,String> avgLtv,String numType){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:avgLtv.entrySet()){
            if (entry.getKey().startsWith("keep")){
                int today = Integer.parseInt(entry.getKey().split("keep")[1]);//当前天(今天)
                if (today > 2){
                    int preDay = today - 1;//前一天(昨天)
                    int pre2Day = preDay - 1;//前两天(前天)
                    if (entry.getValue() != null && avgLtv.get("keep"+preDay) != null && avgLtv.get("keep"+pre2Day) != null){
                        float todayKeep = Float.parseFloat(entry.getValue());
                        float preDayKeep = Float.parseFloat(avgLtv.get("keep"+preDay));
                        float pre2DayKeep = Float.parseFloat(avgLtv.get("keep"+pre2Day));
                        if ((preDayKeep-pre2DayKeep) != 0){
                            resultMap.put(entry.getKey(),String.format("%.2f", ((todayKeep-preDayKeep)/(preDayKeep-pre2DayKeep))*100) + "%");
                        }else {
                            resultMap.put(entry.getKey(),"0.00%");
                        }
                    }
                }
            }
        }
        return resultMap;
    }

    /**
     * 平均留存率和平均付费留存
     * @param allStatNewUserRemainResult
     * @param statNewUserRemainList
     */
    private Map<String,String> getAvgRemain(Map<String, Map<String,Object>> allStatNewUserRemainResult,List<Map<String,String>> statNewUserRemainList,String numType){
        List<Map<String,String>> rows = new ArrayList<>();
        //当前日期
        String currDay =  DateUtils.getDate();
        Map<String,String> statRemainMap = null;
        for(int i = 0;i<statNewUserRemainList.size();i++){
            statRemainMap = statNewUserRemainList.get(i);
            String caclStartTime = statRemainMap.get("caclStartTime");
            int userRegCount = Integer.parseInt(statRemainMap.get("caclNewCount"));
            rows.add(statRemainMap);//留存统计的原始数据
            Map<String,Object> dayResult = allStatNewUserRemainResult.get(caclStartTime);
            Map<String,Integer> keepLoginList  = (Map<String,Integer>)dayResult.get("keepLoginList");
            if(keepLoginList == null){
                continue;
            }
            for(int m = 0;m<durDays.length;m++){
                String keepDay = DateUtils.getNewDateForMinute2(caclStartTime,durDays[m]*1440);
                if(keepLoginList == null ){
                    continue;
                }else {
                    Date keepDaydate = DateUtils.parseDate(keepDay);
                    Date currDaydate = DateUtils.parseDate(currDay);
                    if(keepDaydate.getTime() >= currDaydate.getTime()){
                        //  statRemainMap.put("keep"+(durDays[m]+1),"");
                    }else {
                        if(keepLoginList.containsKey(keepDay)){
                            statRemainMap.put("keep"+(durDays[m]+1),caclKeepRateView( keepLoginList,keepDay,userRegCount));
                        }else {
                            statRemainMap.put("keep"+(durDays[m]+1),"0.0000");
                        }
                    }
                }
            }
        }
        Map<String,String> remainMap = new HashMap<>();
        Map<String,Integer> countMap = new HashMap<>();
        Map<String,String> resultMap = new HashMap<>();
        Integer preCount = 0;
        getRemainLtvMap(remainMap,countMap,preCount,rows);
        //平均留存率 = 每日留存总值/对应的个数
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:remainMap.entrySet()){
            if (entry.getKey().startsWith("keep")){
                float totalLtv = Float.valueOf(entry.getValue());
                int count = countMap.get(entry.getKey());
                resultMap.put(entry.getKey(),String.format("%.2f", (totalLtv/count)*100) + "%");
            }
        }
        return resultMap;
    }

    /**
     * 获取留存或者LTV的总值和对应的个数值
     * @param Map
     * @param countMap
     * @param preCount
     * @param rows
     */
    private void getRemainLtvMap(Map<String,String> Map,Map<String,Integer> countMap,Integer preCount,List<Map<String,String>> rows){
        for (int i = 0; i < rows.size(); i++){
            Map<String,String> row = rows.get(i);
            for (Map.Entry<String,String> entry:row.entrySet()){
                if (entry.getKey().startsWith("keep")){//只需要取keep的数据
                    if (null == Map.get(entry.getKey())){
                        Map.put(entry.getKey(),entry.getValue());
                        preCount = 1;
                        countMap.put(entry.getKey(),preCount);//初始计数为1
                    }else{
                        float preLtv = Float.valueOf(Map.get(entry.getKey()));
                        preLtv += Float.valueOf(entry.getValue());
                        Map.put(entry.getKey(),String.valueOf(preLtv));
                        preCount = countMap.get(entry.getKey());
                        preCount += 1;
                    }
                    countMap.put(entry.getKey(),preCount);
                }
            }
        }
    }
    public String caclKeepRateView(Map<String,Integer> keepLoginList,String keepDay,int userRegCount){
        if(keepLoginList == null || keepLoginList.size() == 0){
            return "";
        }
        if(!keepLoginList.containsKey(keepDay)){
            return "";
        }
        float keepRate = keepLoginList.get(keepDay)*1f/userRegCount;
        return String.format("%.4f", keepRate);
    }

    private Map<String,String> getAvgRemainTrend(Map<String,String> avgRemain,String numType){
        Map<String,String> resultMap = new HashMap<>();
        resultMap.put("numType",numType);
        for (Map.Entry<String,String> entry:avgRemain.entrySet()){
            if (entry.getKey().startsWith("keep")){
                int today = Integer.parseInt(entry.getKey().split("keep")[1]);//当前天
                if (today > 2){//留存从第三天开始才有可以计算的数据
                    int preDay = today - 1;//前一天
                    if (entry.getValue() != null && avgRemain.get("keep"+preDay) != null){
                        float preDayKeep = Float.parseFloat(avgRemain.get("keep"+preDay).split("%")[0])/100;
                        float todayKeep = Float.parseFloat(entry.getValue().split("%")[0])/100;
                        if (preDayKeep != 0){
                            resultMap.put(entry.getKey(),String.format("%.2f", (todayKeep/preDayKeep)*100) + "%");
                        }else {
                            resultMap.put(entry.getKey(),"0.00%");
                        }
                    }
                }
            }
        }
        return resultMap;
    }
}
