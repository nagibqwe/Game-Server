package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:16
 * @Auth ZUncle
 */
public class DailyRecharge extends Activity implements IActivityCustom {

    final int maxLimitDay = 7;

    public DailyRecharge() {
    }

    public DailyRecharge(Activity activity) {
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
        String[] i_reach = paramMap.get("i_reach");
        String[] i_fixRewardGroup = paramMap.get("i_fixRewardGroup");
        String[] i_totalRewardGroup = paramMap.get("i_totalRewardGroup");

        if (i_day.length > maxLimitDay || i_reach.length != i_day.length || i_fixRewardGroup.length != i_day.length) {
            throw new RuntimeException("===数据错误");
        }

        HashMap<Integer, Object> data = new HashMap<>();

        for (int i = 0; i < i_day.length; i++) {
            int day = Integer.parseInt(i_day[i]);
            int reach = Integer.parseInt(i_reach[i]);

            HashMap<String, Object> map = new HashMap<>();
            map.put("day", day);
            map.put("rechargeTarget", reach);
            map.put("rewardDatas", ItemBean.split(i_fixRewardGroup[i]));
            map.put("totalRewardDatas", ItemBean.split(i_totalRewardGroup == null ? null : i_totalRewardGroup[i]));

            if (data.containsKey(day)) {
                throw new RuntimeException(" 重复的天 day=" + day);
            }
            data.put(day, map);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("targetDataMap", data);
        resultMap.put("client", JsonUtils.toJSONString(data));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
