package com.gm.project.stat.stat_recharge_overview.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.utils.DateUtils;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_recharge_overview.dao.IStatRechargeOverviewDao;
import com.gm.project.stat.stat_recharge_overview.domain.StatRechargeOverviewBean;
import com.gm.project.stat.stat_recharge_overview.service.IStatRechargeOverviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 付费总览
 */
@Service
public class StatRechargeOverviewServiceImpl implements IStatRechargeOverviewService {


    private static DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private static DateFormat sdfhm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static long oneday = 1000 * 60 * 60 * 24;
    private DecimalFormat df = new DecimalFormat("0.00");
    /**
     * 查询选择日期内的每日登陆用户总数
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Date> getDateList(String startDate,String endDate){
        //查询选择日期内的每日登陆用户总数
        Date start = DateUtils.parseDate(startDate);
        Date end = DateUtils.parseDate(endDate);
        List<Date> dateList = new ArrayList<>();
        int dvalue = DateUtils.differentDaysByMillisecond(start, end);
        long dateTime = start.getTime();
        Date date;
        for (int i = 0; i < dvalue + 1; i++) {
            date = new Date(dateTime - oneday);
            dateList.add(date);
            dateTime += oneday;
        }
        return dateList;
    }

    /**
     * 数据库相关操作
     */
    @Autowired
    public IStatRechargeOverviewDao statRechargeOverviewDao;

    public List<StatRechargeOverviewBean> StatRechargeOverview(String selectGroupName,String selectServerIdList, String channelNames, String startDate, String endDate, Boolean isBlack) {

        List<StatRechargeOverviewBean> dataList = new ArrayList<>();

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

        List<Date> dateList = getDateList(startDate,endDate);

        List<Map<String, Object>> loginUsersMap;//当天登录账号
        List<Map<String, Object>> newUsersMap;//当天新增账号
        List<Map<String, Object>> rechargeUsersMap;//当天付费账号以及付费总额
        List<Map<String, Object>> newRechargeUsersMap;//当天新增付费账号
        List<Map<String, Object>> oldRechargeUsersMap;//老玩家付费账号
        for (Date d : dateList) {// 将数据按天查询
            loginUsersMap = new ArrayList<>();
            newUsersMap = new ArrayList<>();
            rechargeUsersMap = new ArrayList<>();
            newRechargeUsersMap = new ArrayList<>();
            oldRechargeUsersMap = new ArrayList<>();
            String s = sdf.format(d);

            loginUsersMap = this.statRechargeOverviewDao.getLoginUsersDataList("stat_login",selectServerIdList,channelNames,startDate,endDate,blackUsers);

            newUsersMap = this.statRechargeOverviewDao.getNewUsersDataList("stat_role",selectServerIdList,channelNames,startDate,endDate,blackUsers);

            rechargeUsersMap = this.statRechargeOverviewDao.getRechargeUsersDataList("stat_recharge",selectServerIdList,channelNames,startDate,endDate,blackUsers);

            newRechargeUsersMap = this.statRechargeOverviewDao.getRechargeUsersDataList("stat_recharge",selectServerIdList,channelNames,startDate,endDate,blackUsers);

            oldRechargeUsersMap = this.statRechargeOverviewDao.getOldRechargeUsersDataList("stat_recharge",selectServerIdList,channelNames,startDate,endDate,blackUsers);

            Set<String> loginUsersSet = new HashSet<>();
            Set<String> newUsersSet = new HashSet<>();
            Set<String> rechargeUsersSet = new HashSet<>();
            Set<String> newRechargeUsersSet = new HashSet<>();
            Set<String> oldRechargeUsersSet = new HashSet<>();

            if(loginUsersMap!=null && loginUsersMap.size()>0){
                for (Map<String, Object> loginUser : loginUsersMap) {
                    loginUsersSet.add(loginUser.get("userId").toString());
                }
            }

            if(newUsersMap != null && newUsersMap.size()>0){
                for (Map<String, Object> newUser : newUsersMap) {
                    newUsersSet.add(newUser.get("userId").toString());
                }
            }

            double rechargePayment = 0.0;

            if(rechargeUsersMap!=null && rechargeUsersMap.size()>0){
                for (Map<String, Object> rechargeUser : rechargeUsersMap) {
                    rechargeUsersSet.add(rechargeUser.get("userId").toString());
                    rechargePayment += Double.valueOf(rechargeUser.get("amount").toString());
                }
            }

            if(newRechargeUsersMap!=null && newRechargeUsersMap.size()>0){
                for (Map<String, Object> newRechargeUser : newRechargeUsersMap) {
                    newRechargeUsersSet.add(newRechargeUser.get("userId").toString());
                }
            }

            if(oldRechargeUsersMap!=null && oldRechargeUsersMap.size()>0){
                for (Map<String, Object> oldRechargeUser : oldRechargeUsersMap) {
                    oldRechargeUsersSet.add(oldRechargeUser.get("userId").toString());
                }
            }

            long loginUsersCount = loginUsersSet.size();
            long newUsersCount = newUsersSet.size();
            long rechargeUsersCount = rechargeUsersSet.size();

            long newRechargeUsersCount = newRechargeUsersSet.size();
            long oldRechargeUsersCount = oldRechargeUsersSet.size();

            StatRechargeOverviewBean bean = new StatRechargeOverviewBean();
            bean.setDate(s);
            bean.setTotalPayment(rechargePayment);
            bean.setDAU(loginUsersCount);
            bean.setNewUsers(newUsersCount);
            bean.setRechargeUsers(rechargeUsersCount);
            bean.setNewRechargeUsers(newRechargeUsersCount);
            bean.setOldRechargeUsers(oldRechargeUsersCount);
            bean.setNewRechargeRate(df.format((double) newRechargeUsersCount / (double) newUsersCount));
            bean.setOldRechargeRate(df.format((double) oldRechargeUsersCount / (double) (loginUsersCount - newUsersCount)));
            bean.setRechargeRate(df.format((double) rechargeUsersCount / (double) loginUsersCount));
            bean.setARPU(df.format((double) rechargePayment / (double) loginUsersCount));
            bean.setARPPU(df.format((double) rechargePayment / (double) rechargeUsersCount));
            dataList.add(bean);
        }
        return dataList;
    }

}
