package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 节日礼包
 */
public class HolidayDailyGift extends Activity implements IActivityCustom {

    final int maxLimitDay = 30;

    public HolidayDailyGift() {
    }

    public HolidayDailyGift(Activity activity) {
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
        String[] i_day = paramMap.get("i_day");
        String[] currencyType = paramMap.get("currencyType");
        String[] price = paramMap.get("price");
        String[] limitTimes = paramMap.get("limitTimes");
        String[] reward = paramMap.get("rewards");

        if (i_day.length > maxLimitDay || reward.length != i_day.length) {
            throw new RuntimeException("===数据错误");
        }

        TreeMap<Integer, Object> data = new TreeMap<>();

        for (int i = 0; i < i_day.length; i++) {
            int day = Integer.parseInt(i_day[i]);
            HashMap<String, Object> map = new HashMap<>();
            map.put("day", day);
            map.put("currencyType", currencyType[i]);
            map.put("price", price[i]);
            map.put("limitTimes", limitTimes[i]);
            map.put("reward", ItemBean.split(reward[i]));

            if (data.containsKey(day)) {
                throw new RuntimeException(" 重复的天 day=" + day);
            }
            data.put(day, map);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("server", data);
        resultMap.put("client", JsonUtils.toJSONString(data));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
