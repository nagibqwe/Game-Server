package com.gm.project.stat.stat_churn_rate.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.dbclient.DBClient;
import com.gm.common.dbclient.DBServerMgr;
import com.gm.common.utils.StringUtils;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_churn_rate.dao.IStatChurnRateDao;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveAmountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveCountBean;
import com.gm.project.stat.stat_churn_rate.domain.PlayerLeaveRankBean;
import com.gm.project.stat.stat_churn_rate.service.IStatChurnRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * 用户流失统计 服务
 * @author ruoyi
 */
@Service
public class StatChurnRateServiceImpl implements IStatChurnRateService
{

    @Autowired
    public IStatChurnRateDao statChurnRateDao;
    public  List<PlayerLeaveCountBean> playerLeaveCount(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isBlack,
                                                        String select_condition, String minPay, String maxPay) throws ParseException {
        //渠道筛选
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
        List<PlayerLeaveCountBean> dataList = new ArrayList<>();
        this.StatChurnRateCommon(dataList,null,null,selectGroupName,selectServerIdList,channelNames,startDate,endDate,select_condition,minPay,maxPay);
        return dataList;
    }


    /**
     * 留存统计公共部分
     * @param dataList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param select_condition
     * @throws ParseException
     */
    public void StatChurnRateCommon(List<PlayerLeaveCountBean> dataList,Set<String> vipUserIdList,Set<String> ordUserIdList,String groupName, String selectServerIdList, String channelNames,String startDate, String endDate,String select_condition,String minPay, String maxPay) throws ParseException {
        List<Date> dateList = StatUtil.getDateList(startDate,endDate);
        String endTime = endDate + " 23:59:59";
        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        //得到统计时间内的所有用户数据
        List<Map<String, Object>> allUserDataList = this.statChurnRateDao.getAllUserDataList(dbClientGM,channelNames,"stat_login",startDate,endDate);
        //得到统计时间内的所有付费用户数据
       // List<Map<String, Object>> rechargeDataMap = new ArrayList<>();

        List<Map<String, Object>> rechargeDataMap = this.statChurnRateDao.getRechargeDataMap(dbClientGM,channelNames,selectServerIdList,"stat_recharge",minPay,maxPay,allUserDataList);
        for (Date d : dateList) {
            String dates = StatUtil.sdf.format(d) + " 00:00:00";
            String daten = StatUtil.sdf.format(d) + " 23:59:59";
            String datess = StatUtil.sdf.format(d.getTime() + StatUtil.oneday) + " 00:00:00";
            /*
             * 分别获取统计日的登录玩家和第二天未登录且到截止日都未登录的玩家
             */
            Set<String> firstLoginUserIdList = new HashSet<>();//统计日的登录用户
            Set<String> lastLoginUserIdList = new HashSet<>();//统计日后到截止日的登录用户
            for (Map<String, Object> loginUserMap : allUserDataList) {
                String ltime = loginUserMap.get("date").toString() + " 00:00:01";
                String userId = loginUserMap.get("userId").toString();
                if (StatUtil.sdfhm.parse(ltime).before(StatUtil.sdfhm.parse(daten)) && StatUtil.sdfhm.parse(ltime).after(StatUtil.sdfhm.parse(dates))) {
                    firstLoginUserIdList.add(userId);
                }
                if (StatUtil.sdfhm.parse(ltime).before(StatUtil.sdfhm.parse(endTime)) && StatUtil.sdfhm.parse(ltime).after(StatUtil.sdfhm.parse(datess))) {
                    lastLoginUserIdList.add(userId);
                }
            }
            /*
             * 分别获取统计日的付费玩家和统计日后一周的付费玩家
             */
            Set<String> firstVipUserIdList = new HashSet<>();//统计日的付费用户
            Set<String> lastVipUserIdList = new HashSet<>();//统计日后一周的付费用户
            Set<String> needVipUserIdList = new HashSet<>();//存放付费用户id
            for (Map<String, Object> rechargeUserMap : rechargeDataMap) {
                String userId = rechargeUserMap.get("userId").toString();
                firstVipUserIdList.add(userId);//统计日中所有登录过的付费玩家id
                lastVipUserIdList.add(userId);//
                needVipUserIdList.add(userId);
            }
            //流失用户统计需要
            if(dataList!=null){
                //统计流失率
                firstVipUserIdList.retainAll(firstLoginUserIdList);//当日总登录付费玩家
                int firstLoginCount = firstLoginUserIdList.size();//总登录玩家
                firstLoginUserIdList.removeAll(lastLoginUserIdList);//第二天到截止日登录过的玩家
                int laterLoginCount = firstLoginUserIdList.size();//没登录过的玩家--流失玩家数
                float ordrate = 0;
                if (firstLoginCount != 0) {
                    ordrate = (float) laterLoginCount / firstLoginCount;
                }
                lastVipUserIdList.retainAll(firstLoginUserIdList);//第二天到截止日登陆过的付费玩家
                int laterVipLoginCount = firstVipUserIdList.size() - lastVipUserIdList.size();//没登录过的付费玩家--流失玩家
                float viprate = 0;
                if (firstVipUserIdList.size() != 0) {
                    viprate = (float) laterVipLoginCount / firstVipUserIdList.size();
                }
                PlayerLeaveCountBean playerLeaveCountBean = new PlayerLeaveCountBean();
                playerLeaveCountBean.setDate(StatUtil.sdf.format(d));
                if(select_condition.equals("0")){ //普通用户
                    playerLeaveCountBean.setRoleLoginCount(firstLoginCount);
                    playerLeaveCountBean.setRoleLostCount(laterLoginCount);
                    playerLeaveCountBean.setRate(ordrate);
                }else if(select_condition.equals("1")){  //vip用户
                    playerLeaveCountBean.setRoleLoginCount(firstVipUserIdList.size());
                    playerLeaveCountBean.setRoleLostCount(laterVipLoginCount);
                    playerLeaveCountBean.setRate(viprate);
                }
                dataList.add(playerLeaveCountBean);
            }

            if(vipUserIdList != null && ordUserIdList!=null){
                firstVipUserIdList.retainAll(firstLoginUserIdList);//当日登录过的付费玩家
                firstLoginUserIdList.removeAll(lastLoginUserIdList);//流失普通玩家的id
                needVipUserIdList.retainAll(lastLoginUserIdList);//第二天到截止日期登录过的玩家
                lastVipUserIdList.removeAll(needVipUserIdList);//第二天到截止日期登录过的付费玩家
                firstVipUserIdList.removeAll(lastVipUserIdList);//流失付费玩家的id
                ordUserIdList.addAll(firstLoginUserIdList);
                vipUserIdList.addAll(firstVipUserIdList);
            }

        }
    }


    /**
     * 流失用户付费金额与次数统计
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isblack
     * @param select_condition
     * @param minPay
     * @param maxPay
     * @throws ParseException
     */
    public List<PlayerLeaveAmountBean> playerLeaveAmount(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isblack,
                                                         String select_condition, String minPay, String maxPay) throws ParseException {
        //渠道筛选
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单
        String blackUsers = "";
        if (isblack!=null && isblack) {
            List<Object> blackList = null;//BlackListManager.getInstance().getBlackListUsers(groupName);
            if (blackList.isEmpty()) {
                isblack = false;
            } else {
                //blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        Set<String> vipUserIdList = new HashSet<>();//存放付费流失用户的userid
        Set<String> ordUserIdList = new HashSet<>();//存放普通流失用户的userid
        this.StatChurnRateCommon(null,vipUserIdList,ordUserIdList,selectGroupName,selectServerIdList,channelNames,startDate,endDate,select_condition,minPay,maxPay);
        String frontVipUsers = vipUserIdList.toString();
        String vipUsers = frontVipUsers.substring(1, frontVipUsers.length() - 1);
        String vipUserId = vipUsers;//保存付费流失用户
        int[] amountSum = new int[]{0, 10, 20, 30, 70, 150, 300, 500, 1000, 1500, 2000, 5000, 10000};
        String[] amountSums = new String[]{"0~10", "10~20", "20~30", "30~70", "70~150", "150~300", "300~500", "500~1000", "1000~1500", "1500~2000", "2000~5000", "5000~10000", ">=10000"};
        List<PlayerLeaveAmountBean> dataList = new ArrayList<>();
        for (int i = 0; i < amountSum.length; i++) {
            PlayerLeaveAmountBean playerLeaveAmountBean  = new PlayerLeaveAmountBean();
            playerLeaveAmountBean.setKey(amountSums[i]);
            List<Map<String, Object>> dataMap = this.statChurnRateDao.getAmountDataList(dbClientGM,"stat_recharge",channelNames,vipUserId,amountSum[i]);
            if (!dataMap.isEmpty()) {
                for (Map<String, Object> map : dataMap) {
                    if (map.containsKey("counts")) {
                        playerLeaveAmountBean.setPaylcount(Integer.parseInt(map.get("counts").toString()));
                    }
                    if (map.containsKey("amount") && map.get("amount") != null) {
                        playerLeaveAmountBean.setRechargeSum(Integer.parseInt(map.get("amount").toString()));
                    } else {
                        playerLeaveAmountBean.setRechargeSum(0);
                    }
                }
            }
            dataList.add(playerLeaveAmountBean);
        }
        return dataList;
    }


    /**
     * 流失用户等级分布
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isblack
     * @param select_condition
     * @param minPay
     * @param maxPay
     * @return
     * @throws ParseException
     */
    public List<PlayerLeaveRankBean> playerLeaveRank(String selectGroupName, String selectServerIdList, String channelNames, String startDate, String endDate, Boolean  isblack,
                                                     String select_condition, String minPay, String maxPay) throws ParseException {

        //渠道筛选
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单
        String blackUsers = "";
        if (isblack!=null && isblack) {
            List<Object> blackList = null;//BlackListManager.getInstance().getBlackListUsers(groupName);
            if (blackList.isEmpty()) {
                isblack = false;
            } else {
                //blackUsers = ListToStringUtil.listToString(blackList);
            }
        }
        //获取统计服日志
        DBClient dbClientGM = DBServerMgr.getInstance().getDBClient(DBServerMgr.DBServer.STAT_LOG);
        Set<String> vipUserIdList = new HashSet<>();//存放付费流失用户的userid
        Set<String> ordUserIdList = new HashSet<>();//存放普通流失用户的userid
        this.StatChurnRateCommon(null,vipUserIdList,ordUserIdList,selectGroupName,selectServerIdList,channelNames,startDate,endDate,select_condition,minPay,maxPay);

        Map<String, Integer> ordcMap = new LinkedHashMap<>();//统计普通用户等级数据
        Map<String, Integer> vipcMap = new LinkedHashMap<>();//统计付费用户等级数据
        String frontOrdUsers = ordUserIdList.toString();
        String ordUsers = frontOrdUsers.substring(1, frontOrdUsers.length() - 1);
        String frontVipUsers = vipUserIdList.toString();
        String vipUsers = frontVipUsers.substring(1, frontVipUsers.length() - 1);
         String vipUserId = vipUsers;//保存付费流失用户
        List<Map<String, Object>> ordDataMap = new ArrayList<>();
        List<Map<String, Object>> vipDataMap = new ArrayList<>();
        if (ordUsers != null && !"".equals(ordUsers)) {
            ordDataMap = this.statChurnRateDao.getUserLevelDataList(dbClientGM,selectServerIdList,channelNames,ordUsers);
        }
        if (vipUsers != null && !"".equals(vipUsers)) {
            vipDataMap = this.statChurnRateDao.getUserLevelDataList(dbClientGM,selectServerIdList,channelNames,vipUsers);
        }
        if (ordDataMap != null && !"".equals(ordDataMap)) {
            for (Map<String, Object> map : ordDataMap) {
                String level = map.get("level").toString();
                if (ordcMap.containsKey(level)) {
                    ordcMap.put(level, ordcMap.get(level) + 1);
                } else {
                    ordcMap.put(level, 1);
                }
            }
        }
        if (vipDataMap != null && !"".equals(vipDataMap)) {
            for (Map<String, Object> map : vipDataMap) {
                String level = map.get("level").toString();
                if (vipcMap.containsKey(level)) {
                    vipcMap.put(level, vipcMap.get(level) + 1);
                } else {
                    vipcMap.put(level, 1);
                }
            }
        }
        Map<Integer, Integer> ordMap = new LinkedHashMap<>();//统计等级对应用户数
        Map<Integer, Integer> vipMap = new LinkedHashMap<>();//统计等级对应付费用户数
//            for (int i = 1; i <= 800; i++) {
//                ordMap.put(i, 0);
//                vipMap.put(i, 0);
//            }

        Map<Integer, PlayerLeaveRankBean> dataMap = new LinkedHashMap<>();//统计等级对应用户数

        //将新的等级对应的用户数覆盖
        for (String key : ordcMap.keySet()) {
            PlayerLeaveRankBean bean = new PlayerLeaveRankBean();
            bean.setRank(Integer.parseInt(key));
            bean.setUserLeaveCount(ordcMap.get(key));
            dataMap.put(Integer.parseInt(key),bean);
        }
        for (String key : vipcMap.keySet()) {
            if(dataMap.containsKey(Integer.parseInt(key))){
                dataMap.get(Integer.parseInt(key)).setVipUserLeaveCount(vipcMap.get(key));
            }else {
                PlayerLeaveRankBean bean = new PlayerLeaveRankBean();
                bean.setRank(Integer.parseInt(key));
                bean.setUserLeaveCount(ordcMap.get(key));
                dataMap.put(Integer.parseInt(key),bean);
            }
        }
        List<PlayerLeaveRankBean> dataList = new ArrayList<>();
        if(dataMap!=null && dataMap.size()>0){
            for(PlayerLeaveRankBean bean : dataMap.values()){
                dataList.add(bean);
            }
        }
        return dataList;
    }
}
