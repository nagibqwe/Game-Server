package com.gm.project.stat.stat_daily_data.service.impl;

import com.game.util.ListToStringUtil;
import com.game.util.ThreeTuple;
import com.game.util.TwoTuple;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.common.dao.IStatLoginDao;
import com.gm.project.stat.common.dao.IStatRechargeDao;
import com.gm.project.stat.common.dao.IStatRoleStateDao;
import com.gm.project.stat.stat_daily_data.dao.IStatDailyDataDao;
import com.gm.project.stat.stat_daily_data.dao.impl.StatDailyDataDaoImpl;
import com.gm.project.stat.stat_daily_data.domain.StatDailyDataBean;
import com.gm.project.stat.stat_daily_data.service.IStatDailyDataService;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.text.ParseException;

import java.util.*;


@Service
public class StatDailyDataServiceImpl implements IStatDailyDataService {

    /**
     * 数据库相关操作
     */
    @Autowired
    public IStatDailyDataDao statDailyDataDao;


    @Autowired
    public IStatRoleStateDao statRoleStateDao;


    @Autowired
    public IStatRechargeDao statRechargeDao;


    @Autowired
    public IStatLoginDao statLoginDao;
    /**
     *
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     */
    public List<StatDailyDataBean> statDailyData(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack){
        /**
         * 天数对应的 统计数据
         */
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        String mapKey = "day";
        long stime =  StatUtil.getDataTime(startDate,endDate).first;
        long etime =  StatUtil.getDataTime(startDate,endDate).second;
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
        List<String> exFields = new ArrayList<>();
        exFields.add(mapKey);
        //激活用户激活
        List<Map<String, Object>>  activeUserList = this.statLoginDao.getActiveUserDataList(channelNames,selectServerIdList,blackUsers,stime,etime,isBlack);

        //新增设备
        List<Map<String, Object>> newDeviceList = this.statRoleStateDao.getNewDeviceDataList(channelNames,selectServerIdList,blackUsers,startDate + " 00:00:00",endDate + " 23:59:59",isBlack);
        //活跃设备
        List<Map<String, Object>>  activeDeviceList = this.statLoginDao.getActiveDeviceDataList(channelNames,selectServerIdList,blackUsers,stime,etime,isBlack);


      //  return new ThreeTuple<>(newUserList,rechargeMap,newRechargeUserList);

        ThreeTuple< List<Map<String, Object>>,List<Map<String, Object>>,List<Map<String, Object>>>  statDailyDataCommon = this.statDailyDataCommon(selectGroupName,selectServerIdList,channelNames,startDate,endDate,blackUsers,isBlack);

        List<Map<String, Object>> newUserList = statDailyDataCommon.first;
        //充值列表
        List<Map<String, Object>> rechargeMap =  statDailyDataCommon.second;
        //新增付费玩家人数   新增付费率=新增付费玩家人数/新增玩家人数
        List<Map<String, Object>> newRechargeUserList = statDailyDataCommon.third;



        // 在线人数backend
//        paraMap.put("startDate", startDate);
//        paraMap.put("endDate", endDate);


        List<Map<String, Object>> onlineList = this.statDailyDataDao.getOnlineList(selectServerIdList,startDate,endDate);//  QueryUtil.getInstance().query(dao, sqlStr, table, paraMap);
        List<String> initKeys = new ArrayList<>();// 初始化没有的数据
        initKeys.add("avgnum");
        if (!onlineList.isEmpty()) {
            List<String> addOnlineList = addOnlineResult(resultMap, onlineList, mapKey);
            if (onlineList.size() < resultMap.size()) {
                for (Map.Entry<String, Map<String, String>> entry : resultMap
                        .entrySet()) {
                    if (!addOnlineList.contains(entry.getKey())) {
                        for (String initKey : initKeys) {
                            entry.getValue().put(initKey, "0");
                        }
                    }
                }
            }
        } else {
            for (Map.Entry<String, Map<String, String>> entry : resultMap
                    .entrySet()) {
                for (String initKey : initKeys) {
                    if (!entry.getValue().containsKey(initKey)) {
                        entry.getValue().put(initKey, "0");
                    }
                }
            }
        }

        //统计充值总额和充值人数
        // 组装查询统计结果
        //System.out.println("rechargeMap===="+rechargeMap.toString());
        if (!rechargeMap.isEmpty()) {
            Map<String, List<Map<String, Object>>> dataMap = new TreeMap<>();
            Map<String, Map<String, String>> data = new TreeMap<>();
            for (Map<String, Object> map : rechargeMap) {
                if (map.containsKey("day")) {
                    String day = map.get("day").toString();
                    map.remove("day");
                    List<Map<String, Object>> list = new ArrayList<>();
                    list.add(map);
                    if (dataMap.containsKey(day)) {
                        list.addAll(dataMap.get(day));
                    }
                    dataMap.put(day, list);
                }
            }
            for (String key : dataMap.keySet()) {
                float totalmoney = 0f;
                float totalgold = 0f;
                int totaltimes = 0;
//                float totalbindgold = 0f;
                Set<String> userSet = new HashSet<>();
                for (Map<String, Object> map : dataMap.get(key)) {
                    totalmoney += Float.parseFloat(map.get("totalmoney").toString());
                    totalgold += Float.parseFloat(map.get("totalgold").toString());
                    totaltimes += Integer.parseInt(map.get("totaltimes").toString());
//                    totalbindgold += Float.parseFloat(map.get("totalbindgold"));
                    userSet.add(map.get("userId").toString());
                }
                int totaluser = userSet.size();
                Map<String, String> map = new HashMap<>();
                map.put("totalmoney", String.valueOf(totalmoney));
                map.put("totalgold", String.valueOf(totalgold));
                map.put("totaltimes", String.valueOf(totaltimes));
//                map.put("totalbindgold", totalbindgold + "");
                map.put("totaluser", String.valueOf(totaluser));
                if (data.containsKey(key)) {
                    map.put("totalmoney", (totalmoney + Float.parseFloat(data.get(key).get("totalmoney"))) + "");
                    map.put("totalgold", (totalgold + Float.parseFloat(data.get(key).get("totalgold"))) + "");
                    map.put("totaltimes", (totaltimes + Integer.parseInt(data.get(key).get("totaltimes"))) + "");
//                    map.put("totalbindgold", (totalbindgold + Float.parseFloat(data.get(key).get("totalbindgold"))) + "");
                    map.put("totaluser", (totaluser + Integer.parseInt(data.get(key).get("totaluser"))) + "");
                }
                data.put(key, map);
            }
            addStatResult(resultMap, data, exFields);
        }


        //统计活跃玩家、活跃设备
        String userKey = "userId";
        String activeNum = "activenum";
        String deviceKey = "machineCode";
        String deviceNum = "deviceNum";
        Map<String, Set<Object>> activeNumMap;
        Map<String, Set<Object>> deviceNumMap;
        activeNumMap = getAssembleMap(activeUserList, mapKey, userKey);
        getRMap(resultMap, activeNumMap, activeNum);
        deviceNumMap = getAssembleMap(activeDeviceList, mapKey, deviceKey);
        getRMap(resultMap, deviceNumMap, deviceNum);

        Map<String, Set<Object>> newUserNumMap;
        Map<String, Set<Object>> newDeviceNumMap;

        Map<String, Set<Object>> newRechargeUserMap;
        //统计新增玩家、新增设备
        if (!newUserList.isEmpty()) {

            newUserNumMap = getAssembleMap(newUserList, mapKey, userKey);
            getRMap(resultMap, newUserNumMap, "addnum");

            //addResult(resultMap, single, mapKey,"addnum");
        }
        if (!newDeviceList.isEmpty()) {
            newDeviceNumMap = getAssembleMap(newDeviceList, mapKey, deviceKey);
            getRMap(resultMap, newDeviceNumMap, "deviceaddnum");
        }

        //统计新增付费玩家人数
        if (!newRechargeUserList.isEmpty()){
            newRechargeUserMap = getAssembleMap(newRechargeUserList,mapKey,userKey);
            getRMap(resultMap, newRechargeUserMap, "addRechargeNum");
        }
        List<StatDailyDataBean>  listData = new ArrayList<>();
        List<Date> dateList = StatUtil.getDateList(startDate,endDate);
        if(dateList!=null && dateList.size()>0){
            for(int i = 0;i<dateList.size();i++){
                String dates = StatUtil.sdf.format(dateList.get(i));
                StatDailyDataBean statDailyDataBean = new StatDailyDataBean();
                statDailyDataBean.setDay(dates);

                Map<String, String> day = resultMap.get(dates);
                if(day!=null && day.size()>0){

//                    statDailyDataBean.setTotaluser(0);
//                    statDailyDataBean.setActivenum(0);
//                    statDailyDataBean.setAddRechargeNum(0);
//                    statDailyDataBean.setAddnum(0);
                     statDailyDataBean.setTotalmoney(0f);
                    statDailyDataBean.setTotalgold(0f);

                    if(day.containsKey("totalmoney")){
                        statDailyDataBean.setTotalmoney(Float.parseFloat(day.get("totalmoney")));
                    }

                    if(day.containsKey("totalgold")){
                        statDailyDataBean.setTotalgold(Float.parseFloat(day.get("totalgold")));
                    }

                    if(day.containsKey("totaltimes")){
                        statDailyDataBean.setTotaltimes(day.get("totaltimes"));
                    }

                    if(day.containsKey("totaluser")){
                        statDailyDataBean.setTotaluser(day.get("totaluser"));
                    }

                    if(day.containsKey("deviceaddnum")){
                        statDailyDataBean.setDeviceaddnum(day.get("deviceaddnum"));
                    }
                    if(day.containsKey("activenum")){
                        statDailyDataBean.setActivenum(day.get("activenum"));
                    }

                    if(day.containsKey("addRechargeNum")){
                        statDailyDataBean.setAddRechargeNum(day.get("addRechargeNum"));
                    }

                    if(day.containsKey("addnum")){
                        statDailyDataBean.setAddnum(day.get("addnum"));
                    }

                    if(day.containsKey("deviceNum")){
                        statDailyDataBean.setDeviceNum(day.get("deviceNum"));
                    }

                    if(day.containsKey("avgnum")){
                        statDailyDataBean.setAvgnum(Float.parseFloat(day.get("avgnum")));
                    }

                    if(day.containsKey("maxnum")){
                        statDailyDataBean.setMaxnum(Integer.parseInt(day.get("maxnum")));
                    }
                    if(!StringUtils.isBlank(statDailyDataBean.getActivenum())){
                       // float activpayrate = statDailyDataBean.getTotaluser()/statDailyDataBean.getActivenum();

                        float totaluser = 0f;
                        if(!StringUtils.isBlank(statDailyDataBean.getTotaluser())){
                            totaluser =  Float.parseFloat(statDailyDataBean.getTotaluser());
                        }


                        float activpayrate   = (float) StatUtil.div(totaluser, Float.parseFloat(statDailyDataBean.getActivenum()),4);
                        statDailyDataBean.setActivpayrate(activpayrate);
                    }

                    if(!StringUtils.isBlank(statDailyDataBean.getAddnum())){
                       // float addpayrate = statDailyDataBean.getAddRechargeNum()/statDailyDataBean.getAddnum();
                        float addRechargeNum = 0f;
                        if(!StringUtils.isBlank(statDailyDataBean.getAddRechargeNum())){
                            addRechargeNum =  Float.parseFloat(statDailyDataBean.getAddRechargeNum());
                        }

                        float addpayrate   = (float) StatUtil.div(addRechargeNum, Float.parseFloat(statDailyDataBean.getAddnum()),4);
                        statDailyDataBean.setAddpayrate(addpayrate);
                    }

                    if(!StringUtils.isBlank(statDailyDataBean.getActivenum())){

                        // float addpayrate = statDailyDataBean.getAddRechargeNum()/statDailyDataBean.getAddnum();
                        float arpu   = (float) StatUtil.div(statDailyDataBean.getTotalmoney(), Float.parseFloat(statDailyDataBean.getActivenum()),2);
                        statDailyDataBean.setArpu(arpu);
                    }

                    if(!StringUtils.isBlank(statDailyDataBean.getTotaluser())){

                        float arppu   = (float) StatUtil.div(statDailyDataBean.getTotalmoney(), Float.parseFloat(statDailyDataBean.getTotaluser()),2);
                        statDailyDataBean.setArppu(arppu);
                    }



                    listData.add(statDailyDataBean);
                }
            }
        }

        return listData;
    }
    public ThreeTuple< List<Map<String, Object>>,List<Map<String, Object>>,List<Map<String, Object>>> statDailyDataCommon(String groupName, String selectServerIdList, String channelNames, String startDate, String endDate, String blackUsers, Boolean isBlack){

        long stime =  StatUtil.getDataTime(startDate,endDate).first;
        long etime =  StatUtil.getDataTime(startDate,endDate).second;

        Map<String, Map<String, String>> resultMap = new HashMap<>();
        String mapKey = "day";
        String userKey = "userId";
        List<String> exFields = new ArrayList<>();
        exFields.add(mapKey);
        List<Map<String, Object>> newUserList = new ArrayList<>();//新增玩家
        //新注册用户
        List<Map<String, Object>> userRegisterDataList = this.statRoleStateDao.getUserRegisterDataList(channelNames,selectServerIdList,startDate,endDate);
        Map<String,String> createUsers = new HashMap<>();
        if(userRegisterDataList != null && userRegisterDataList.size()>0){
            for (Map<String, Object> firstMap : userRegisterDataList) {
                String uid = firstMap.get("userId").toString();
                String createTime = firstMap.get("createTime").toString();
                Integer idx = createTime.indexOf(' ');
                createTime = createTime.substring(0,idx);
                if(createUsers.containsKey(uid)){
                    if(createUsers.get(uid).compareTo(createTime) > 0) {
                        createUsers.put(uid,createTime);
                    }
                }
                else{
                    createUsers.put(uid,createTime);
                }
            }
        }
        if(createUsers != null && createUsers.size()>0){
            for(String key : createUsers.keySet()){
                Map<String, Object> dayUser = new HashMap<>();
                dayUser.put("userId",key);
                dayUser.put("day",createUsers.get(key));

                newUserList.add(dayUser);
            }
        }
        //充值列表
        List<Map<String, Object>> rechargeMap =  this.statRechargeDao.getRechargeMapDataList(channelNames,selectServerIdList,blackUsers,stime,etime,isBlack);
        //新增付费玩家人数   新增付费率=新增付费玩家人数/新增玩家人数
        List<Map<String, Object>> newRechargeUserList = new ArrayList<>();//新增付费玩家人数
        if (rechargeMap !=null && !rechargeMap.isEmpty()) {
            for (Map<String, Object> rechargeUserMap : rechargeMap) {
                String rechargeUserId = rechargeUserMap.get("userId").toString();
                String rechargeDay = rechargeUserMap.get("day").toString();
                for (Map<String, Object> newUserMap :newUserList){
                    String newUserId = newUserMap.get("userId").toString();
                    String newUserDay = newUserMap.get("day").toString();
                    if (rechargeUserId.equals(newUserId) && rechargeDay.equals(newUserDay)){
                        Map<String, Object> map = new LinkedHashMap<>();
                        map.put("userId",newUserId);
                        map.put("day",newUserDay);
                        newRechargeUserList.add(map);
                    }else {
                        continue;
                    }
                }
            }
        }


        return new ThreeTuple<>(newUserList,rechargeMap,newRechargeUserList);
    }



    // 组装查询统计结果
    public Map<String, Set<Object>> getAssembleMap(List<Map<String, Object>> dataMap, String mapKey, String key) {
        Map<String, Set<Object>> activeNumMap = new HashMap<>();

        if(dataMap == null){
            return activeNumMap;
        }
        Set<Object> activeSet;
        for (Map<String, Object> map : dataMap) {
            if (activeNumMap.containsKey(map.get(mapKey))) {
                if (!activeNumMap.get(map.get(mapKey)).contains(map.get(key))) {
                    activeNumMap.get(map.get(mapKey)).add(String.valueOf(map.get(key)));
                }
            } else {
                activeSet = new HashSet<>();
                activeSet.add(String.valueOf(map.get(key)));
                activeNumMap.put(map.get(mapKey).toString(), activeSet);
            }
        }
        return activeNumMap;
    }



    //统计活跃玩家和活跃设备\新增玩家，新增设备
    private void getRMap(Map<String, Map<String, String>> resultMap, Map<String, Set<Object>> activeNumMap, String activeKey) {
        Map<String, String> rMap;
        for (Map.Entry<String, Set<Object>> entry : activeNumMap.entrySet()) {
            if (resultMap.containsKey(entry.getKey())) {
                if (resultMap.get(entry.getKey()).containsKey(activeKey)) {
                    String temp = Integer.parseInt(resultMap.get(entry.getKey()).get(activeKey)) + entry.getValue().size() + "";
                    resultMap.get(entry.getKey()).put(activeKey, temp);
                } else {
                    resultMap.get(entry.getKey()).put(activeKey, entry.getValue().size() + "");
                }
            } else {
                rMap = new HashMap<>();
                rMap.put(activeKey, entry.getValue().size() + "");
                resultMap.put(entry.getKey(), rMap);
            }
        }
    }
    private void addStatResult(Map<String, Map<String, String>> resultMap, Map<String, Map<String, String>> singleMap, List<String> exFields) {
        Map<String, String> map;
        for (Map.Entry<String, Map<String, String>> entry : singleMap.entrySet()) {
            if (resultMap.containsKey(entry.getKey())) {
                map = resultMap.get(entry.getKey());
                for (Map.Entry<String, String> sline : entry.getValue().entrySet()) {
                    if (exFields.contains(sline.getKey())) {
                        continue;
                    }
                    map.put(sline.getKey(), getSum(map.get(sline.getKey()), sline.getValue()).toString());
                }
                resultMap.put(entry.getKey(), map);
            } else {
                resultMap.put(entry.getKey(), singleMap.get(entry.getKey()));
            }
        }
    }
    private Object getSum(Object r, Object s) {
        if (r == null && s != null) {
            return Float.parseFloat(s.toString());
        } else if (r != null && s == null) {
            return Float.parseFloat(r.toString());
        } else if (r == null && s == null) {
            return null;
        } else {
            return Float.parseFloat(r.toString()) + Float.parseFloat(s.toString());
        }
    }



    private List<String> addOnlineResult(
            Map<String, Map<String, String>> resultMap,
            List<Map<String, Object>> list, String key) {
        List<String> addList = new ArrayList<>();
        String avgNum = "avgnum";
        String maxNum = "maxnum";
        for (Map<String, Object> dataMap : list) {
            Map<String, String> map;
            if (resultMap.containsKey(dataMap.get(key))) {
                map = resultMap.get(dataMap.get(key));
            } else {
                map = new HashMap<>();
            }
            for (Map.Entry<String, Object> entry : dataMap.entrySet()) {
                if (entry.getKey().equals(avgNum) && map.containsKey(avgNum)) {
                    map.put(entry.getKey(), getSum(map.get(entry.getKey()), entry.getValue()).toString());
                } else if (entry.getKey().equals(maxNum) && map.containsKey(maxNum)) {
                    map.put(entry.getKey(), getSum(map.get(entry.getKey()), entry.getValue()).toString());
                } else {
                    map.put(entry.getKey(), entry.getValue().toString());
                }
            }
            resultMap.put(dataMap.get(key).toString(), map);
            addList.add(dataMap.get(key).toString());
        }
        return addList;
    }

}
