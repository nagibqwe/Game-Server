package com.gm.project.stat.stat_recharge_second.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_recharge_second.dao.IStatRechargeSecondDao;
import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondItemBean;
import com.gm.project.stat.stat_recharge_second.domain.RechargeSecondTimeIntervaBean;
import com.gm.project.stat.stat_recharge_second.service.IStatRechargeSecondService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 二次付费统计 Service业务层处理
 *
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatRechargeSecondServiceImpl implements IStatRechargeSecondService {
    @Autowired
    private IStatRechargeSecondDao statRechargeSecondDao;

    /**
     * 二次付费间隔统计
     *
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     */
    public List<RechargeSecondTimeIntervaBean> statRechargeSecondTimeInterval(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack) {

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

        List<Map<String, Object>> secondRechargeMapList = this.statRechargeSecondDao.getFirstAndSecondRechargeList("stat_recharge", selectServerIdList, channelNames, startDate, endDate, blackUsers);
        List<Map<String, Object>> secondRechargeUserMap = this.getSecondRechargeUserMap(secondRechargeMapList, startDate, endDate);

        //按照间隔天数来获取数据(新增15,16,17,18,19分别对应间隔10分钟，30分钟，1小时，2-3小时，3小时)
        int[] timeGap = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 14, 30, 15, 16, 17, 18, 19};
        Map<Integer, Integer> timeGapMap = new TreeMap<>();
        for (int value : timeGap) {//初始化
            timeGapMap.put(value, 0);
        }
        for (Map<String, Object> dataMap : secondRechargeUserMap) {
            long time = Long.parseLong(dataMap.get("timesec").toString());
            String itemId = dataMap.get("itemId").toString();
            float amount = Float.parseFloat(dataMap.get("totalFee").toString());
            for (int value : timeGap) {
                int count = 0;
                if (value == 8) {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 6) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 14) {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 16) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 30) {
                    if (time >= (value) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 15) {
                    if (time >= 10 * 60 && time < 30 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 16) {
                    if (time >= 30 * 60 && time < 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 17) {
                    if (time >= 60 * 60 && time < 2 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 18) {
                    if (time >= 2 * 60 * 60 && time < 3 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else if (value == 19) {
                    if (time >= 3 * 60 * 60 && time < 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        }
                        timeGapMap.put(value, count);
                    }
                } else {
                    if (time >= (value) * 24 * 60 * 60 && time < (value + 1) * 24 * 60 * 60) {
                        count++;
                        if (timeGapMap.containsKey(value)) {
                            timeGapMap.put(value, count + timeGapMap.get(value));
                        } else {
                            timeGapMap.put(value, count);
                        }
                    }
                }
            }
        }
        List<RechargeSecondTimeIntervaBean> rechargeSecondTimeIntervaBeanList = new ArrayList<>();
        for (int key : timeGapMap.keySet()) {
            String timeGapName = getTimeGapName(key);
            RechargeSecondTimeIntervaBean rechargeSecondTimeIntervaBean = new RechargeSecondTimeIntervaBean();
            rechargeSecondTimeIntervaBean.setIntervaDay(timeGapName);
            rechargeSecondTimeIntervaBean.setRoles(timeGapMap.get(key));
            rechargeSecondTimeIntervaBeanList.add(rechargeSecondTimeIntervaBean);
        }


        return rechargeSecondTimeIntervaBeanList;
    }

    /**
     * 获取二次付费的map
     *
     * @param secondRechargeMapList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<Map<String, Object>> getSecondRechargeUserMap(List<Map<String, Object>> secondRechargeMapList, String startDate, String endDate) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date start = null;
        Date end = null;
        try {
            start = format.parse(startDate + " 00:00:00");
            end = format.parse(endDate + " 23:59:59");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long startTime = start.getTime() / 1000;
        long endTime = end.getTime() / 1000;

        List<Map<String, Object>> secondRechargeUserMap = new ArrayList<>();//获取二次付费的map
        Map<String, String> rechargeMap = new TreeMap<>();
        for (Map<String, Object> map : secondRechargeMapList) {
            String userId = map.get("userId").toString();

            //充值时间
            long time = Long.parseLong(map.get("timesec").toString());
            float amount = Float.parseFloat(map.get("totalFee").toString());
//            int isMoon = 0;
//            if (map.get("isMoon") != null) {
//                isMoon = Integer.parseInt(map.get("isMoon").toString());
//            }
            String goodsId = map.get("goodsId").toString();
            if (rechargeMap.containsKey(userId)) {
                String[] str = rechargeMap.get(userId).split(",");
                Map<String, Object> dataMap = new HashMap<>();
                long timeTemp = Long.parseLong(str[0]);
                float amountTemp = Float.parseFloat(str[1]);
//                int isMoonTemp = Integer.parseInt(str[2]);
                String goodsIdTemp = str[2];
                long timeGap;
                if (timeTemp < time) {
                    if (time >= startTime && time <= endTime) {
                        timeGap = time - timeTemp;
                        dataMap.put("userId", userId);
                        dataMap.put("timesec", timeGap);
                        dataMap.put("totalFee", amount);
//                        dataMap.put("isMoon", isMoon);
                        dataMap.put("goodsId", goodsId);
                        secondRechargeUserMap.add(dataMap);
                    }
                } else {
                    if (timeTemp >= startTime && timeTemp <= endTime) {
                        timeGap = timeTemp - time;
                        dataMap.put("userId", userId);
                        dataMap.put("timesec", timeGap);
                        dataMap.put("totalFee", amountTemp);
//                        dataMap.put("isMoon", isMoonTemp);
                        dataMap.put("goodsId", goodsIdTemp);
                        secondRechargeUserMap.add(dataMap);
                    }
                }
                rechargeMap = new TreeMap<>();
            } else {
                rechargeMap.put(userId, time + "," + amount + "," + goodsId);
            }
        }

        return secondRechargeUserMap;

    }


    private String getTimeGapName(int time) {
        String timeGapName = "";
        switch (time) {
            case 0:
                timeGapName = "间隔0天";
                break;
            case 1:
                timeGapName = "间隔1天";
                break;
            case 2:
                timeGapName = "间隔2天";
                break;
            case 3:
                timeGapName = "间隔3天";
                break;
            case 4:
                timeGapName = "间隔4天";
                break;
            case 5:
                timeGapName = "间隔5天";
                break;
            case 6:
                timeGapName = "间隔6天";
                break;
            case 7:
                timeGapName = "间隔7天";
                break;
            case 8:
                timeGapName = "间隔8~14天";
                break;
            case 14:
                timeGapName = "间隔14~29天";
                break;
            case 15:
                timeGapName = "间隔10分钟";
                break;
            case 16:
                timeGapName = "间隔30分钟";
                break;
            case 17:
                timeGapName = "间隔1小时";
                break;
            case 18:
                timeGapName = "间隔2-3小时";
                break;
            case 19:
                timeGapName = "间隔3小时以上";
                break;
            case 30:
                timeGapName = "间隔30天以上";
                break;
        }
        return timeGapName;
    }

    private String getProjectName(int project) {
        String projectName = "";
        switch (project) {
            case 10:
                projectName = "0元每日礼包";
                break;
            case 11:
                projectName = "6元每日礼包";
                break;
            case 12:
                projectName = "12元每日礼包";
                break;
            case 13:
                projectName = "18元每日礼包";
                break;
            case 14:
                projectName = "30元每日礼包";
                break;
            case 15:
                projectName = "68元每日礼包";
                break;
        }
        return projectName;
    }

    /**
     * 二次付费项目统计
     *
     * @param selectGroupName
     * @param selectServerIdList
     * @param channelNames
     * @param startDate
     * @param endDate
     * @param isBlack
     * @return
     */
    public List<RechargeSecondItemBean> statRechargeSecondItem(String selectGroupName,String selectServerIdList, String channelNames,String startDate, String endDate, Boolean isBlack) {
        //渠道名称
        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单排除
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

        List<Map<String, Object>> secondRechargeMapList = this.statRechargeSecondDao.getFirstAndSecondRechargeList("stat_recharge", selectServerIdList, channelNames, startDate, endDate, blackUsers);
        List<Map<String, Object>> secondRechargeUserMap = this.getSecondRechargeUserMap(secondRechargeMapList, startDate, endDate);
        int[] project = new int[]{10, 11, 12, 13, 14, 15};
        Map<Integer, Integer> projectMap = new TreeMap<>();
        for (int value : project) {
            projectMap.put(value, 0);
        }
        //二次项目统计
        for (Map<String, Object> dataMap : secondRechargeUserMap) {
            long time = Long.parseLong(dataMap.get("timesec").toString());
            String goodsId = dataMap.get("goodsId").toString();
            int itemId = Integer.parseInt(goodsId);
           // float amount = Float.parseFloat(dataMap.get("totalFee").toString());
            for (int value : project) {
                int count = 0;

                if (value == itemId) {
                    count++;
                    if (projectMap.containsKey(value)) {
                        projectMap.put(value, count + projectMap.get(value));
                    } else {
                        projectMap.put(value, count);
                    }
                }
            }

        }
        List<RechargeSecondItemBean> rechargeSecondItemBeanList = new ArrayList<>();
        for (int key : projectMap.keySet()) {
            RechargeSecondItemBean rechargeSecondItemBean = new RechargeSecondItemBean();
            String projectName = getProjectName(key);
            rechargeSecondItemBean.setSecondItem(projectName);
            rechargeSecondItemBean.setRoles(projectMap.get(key));
            rechargeSecondItemBeanList.add(rechargeSecondItemBean);
        }
        return rechargeSecondItemBeanList;
    }
}
