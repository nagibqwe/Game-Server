package com.gm.project.stat.stat_recharge_counts.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.utils.StringUtils;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_recharge_counts.dao.IStatRechargeCountsDao;
import com.gm.project.stat.stat_recharge_counts.domain.RechargeCountsBean;
import com.gm.project.stat.stat_recharge_counts.service.IStatRechargeCountsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 付费充值统计 Service业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatRechargeCountsServiceImpl implements IStatRechargeCountsService
{
    @Autowired
    private IStatRechargeCountsDao statRechargeCountsDao;
    /**
     * 付费次数统计
     * @param selectGroupName
     * @param selectServerIds
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     */
    public List<RechargeCountsBean> rechargeCountsStat(String selectGroupName, String selectServerIds, String channelNames, String startDate, String endDate, Boolean isBlack){
        List<RechargeCountsBean> rechargeCountsMap = new ArrayList<>();
       // List<Map<String, Object>> rechargeCountsMapList = new ArrayList<>();
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
        //获取数据
        List<Map<String, Object>> rechargeCountsMapList = this.statRechargeCountsDao.getRechargeCountsDataList("stat_recharge",selectServerIds,channelNames,startDate,endDate,blackUsers);
        //处理得到的数据
        int[] count = new int[]{1, 2, 5, 10, 20, 30, 31};
        Map<Integer, Float> rechargeTimesMap = new TreeMap<>();//付费次数与人数的map
        Map<Integer, Float> rechargeAmountsMap = new TreeMap<>();//付费次数与付费总额的map
        for (int value : count) {
            rechargeTimesMap.put(value, 0f);
            rechargeAmountsMap.put(value, 0f);
        }
        if (rechargeCountsMapList.size() != 0) {
            for (Map<String, Object> rechargeMap : rechargeCountsMapList) {
                float rechargeCount = Float.parseFloat(rechargeMap.get("counts").toString());
                float amount = Float.parseFloat(rechargeMap.get("amount").toString());
                for (int i = 0; i < count.length; i++) {
                    float roleCounts = 0;
                    float payment;
                    if (i == 0 || i == 1 || i == (count.length - 1)) {
                        if (rechargeCount == count[i]) {
                            roleCounts++;
                            if (rechargeTimesMap.containsKey(count[i])) {
                                roleCounts += rechargeTimesMap.get(count[i]);
                            }
                            payment = amount;
                            if (rechargeAmountsMap.containsKey(count[i])) {
                                payment = rechargeAmountsMap.get(count[i]) + amount;
                            }
                            rechargeTimesMap.put(count[i], roleCounts);
                            rechargeAmountsMap.put(count[i], payment);
                        }
                    } else {
                        if (rechargeCount <= count[i] && rechargeCount > count[i - 1]) {
                            roleCounts++;
                            if (rechargeTimesMap.containsKey(count[i])) {
                                roleCounts += rechargeTimesMap.get(count[i]);
                            }
                            payment = amount;
                            if (rechargeAmountsMap.containsKey(count[i])) {
                                payment = rechargeAmountsMap.get(count[i]) + amount;
                            }
                            rechargeTimesMap.put(count[i], roleCounts);
                            rechargeAmountsMap.put(count[i], payment);
                        }
                    }
                }

            }
            for (int key1 : rechargeTimesMap.keySet()) {
               // Map<String, Object> rechargeTimesResMap = new TreeMap<>();//付费次数与人数最终的map
                RechargeCountsBean rechargeCountsBean = new RechargeCountsBean();
                String rechargeTimes = getRechargeTimesName(key1);
                float rechargeRoles = rechargeTimesMap.get(key1);
                for (int key2 : rechargeAmountsMap.keySet()) {
                    float rechargeAmounts = rechargeAmountsMap.get(key2);
                    if (key1 == key2) {
                        rechargeCountsBean.setRechargeAmounts(rechargeAmounts);
                        rechargeCountsBean.setRechargeTimes(rechargeTimes);
                        rechargeCountsBean.setRechargeRoles(rechargeRoles);
//                        rechargeTimesResMap.put("rechargeTimes", rechargeTimes);
//                        rechargeTimesResMap.put("rechargeRoles", rechargeRoles);
//                        rechargeTimesResMap.put("rechargeAmounts", rechargeAmounts);
                        rechargeCountsMap.add(rechargeCountsBean);
                        break;
                    }
                }
            }
        }
        return  rechargeCountsMap;
    }
    private String getRechargeTimesName(int counts) {
        String rechargeTimesName = "";
        switch (counts) {
            case 1:
                rechargeTimesName = "1次";
                break;
            case 2:
                rechargeTimesName = "2次";
                break;
            case 5:
                rechargeTimesName = "3~5次";
                break;
            case 10:
                rechargeTimesName = "6~10次";
                break;
            case 20:
                rechargeTimesName = "11~20次";
                break;
            case 30:
                rechargeTimesName = "21~30次";
                break;
            case 31:
                rechargeTimesName = "31次以上";
                break;
        }
        return rechargeTimesName;
    }
}
