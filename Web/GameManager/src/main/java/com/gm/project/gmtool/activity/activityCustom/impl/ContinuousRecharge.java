package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 连续累充
 */
public class ContinuousRecharge extends Activity implements IActivityCustom {

    //每档最大10个
    final int maxLimitDay = 60;

    public ContinuousRecharge() {
    }

    public ContinuousRecharge(Activity activity) {
        super(activity);
    }

    /**
     * 解析自定义参数
     *
     * @param paramMap
     * @return
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) {
        String[] i_reachStrs = paramMap.get("i_reach");
        if (i_reachStrs.length <= 0) {
            throw new RuntimeException("===数据错误");
        }

        String[] dayCountStrs = paramMap.get("dayCount");
        String[] i_day = paramMap.get("i_day");
        if (i_day.length > maxLimitDay) {
            throw new RuntimeException("===数据错误,天数超过最大上限值");
        }
        String[] i_fixRewardGroup = paramMap.get("i_fixRewardGroup");
        String[] i_totalRewardGroup = paramMap.get("i_totalRewardGroup");
        int totalDayCount = 0;

        HashMap<Integer, HashMap<Integer, Object>> dataMap = new HashMap<>();

        for (int i = 0; i < i_reachStrs.length; i++) {
            if(i_reachStrs[i] == null || i_reachStrs[i].equals("")){
                continue;
            }
            int i_reach = Integer.parseInt(i_reachStrs[i]);
            if (i_reach < 0) {
                throw new RuntimeException("===数据错误");
            }else if(i_reach == 0){
                continue;
            }

            int dayCount = Integer.parseInt(dayCountStrs[i]);
            if(dayCount < 0 ){
                throw new RuntimeException("===数据错误");
            }

            int start = totalDayCount;
            totalDayCount+=dayCount;

            HashMap<Integer, Object> data = new HashMap<>();

            for (int j = start; j < totalDayCount; j++) {
                int day = Integer.parseInt(i_day[j]);
                HashMap<String, Object> map = new HashMap<>();
                map.put("day", day);
                map.put("rewardDatas", ItemBean.split(i_fixRewardGroup[j]));
                map.put("totalRewardDatas", ItemBean.split(i_totalRewardGroup == null ? null : i_totalRewardGroup[j]));

                if (data.containsKey(day)) {
                    throw new RuntimeException(" 重复的天 day=" + day);
                }
                data.put(day, map);
            }
            dataMap.put(i_reach, data);
        }

        if(dataMap.isEmpty()){
            throw new RuntimeException("===数据错误");
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("targetDataMap", dataMap);
        resultMap.put("client", JsonUtils.toJSONString(dataMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
