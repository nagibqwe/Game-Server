package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.manager.ActivityManager;
import com.gm.project.gmtool.utils.JsonUtils;
import com.gm.project.gmtool.utils.TimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO 节日祝福
 */
public class FestivalSign extends Activity implements IActivityCustom {

    public FestivalSign() {
    }

    public FestivalSign(Activity activity) {
        super(activity);
    }

    /**
     * 解析自定义参数
     *
     * @param paramMap
     * @return
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap){
        //补签道具ID
        String[] buqianid = paramMap.get("buqianid");
        //补签消耗
        String[] buqianCost = paramMap.get("buqianCost");

        //每日签到天数
        String[] days = paramMap.get("day");
        //每日签到模型ID
        String[] modelIds = paramMap.get("modelId");
        //每日签到奖励
        String[] dayRewards = paramMap.get("dayReward");

        if (buqianid.length <= 0 || buqianCost.length <= 0 || days.length <= 0 || modelIds.length <= 0 || dayRewards.length <= 0 ) {
            throw new RuntimeException("===数据错误或数据为空");
        }

        int timeType = Integer.parseInt(paramMap.get("timeType")[0]);
        if(timeType==0){
            String beginTimeStr = paramMap.get("beginTime")[0];
            String endTimeStr = paramMap.get("endTime")[0];
            int dayOff = 0;
            try {
                dayOff = TimeUtils.getBetweenDays(TimeUtils.getDateByString2(endTimeStr).getTime(), TimeUtils.getDateByString2(beginTimeStr).getTime());
                dayOff+=1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (days.length > dayOff ) {
                throw new RuntimeException("===配置的天数超过活动持续时间");
            }
        }else if(timeType==1){
            int opsOffset = Integer.parseInt(paramMap.get("openServerOffset")[0]);
            if (days.length > opsOffset ) {
                throw new RuntimeException("===配置的天数超过活动持续时间");
            }
        }else{
            throw new RuntimeException("===时间类型错误");
        }

        //全服宝箱ID
        String[] boxIds = paramMap.get("boxId");
        //全服需要进度
        String[] needs = paramMap.get("need");
        //全服宝箱奖励
        String[] serverRewards = paramMap.get("serverReward");

        int dayCount=0;
        List<HashMap<String, Object>> dayRewardList = new ArrayList<>();
        for (int i = dayCount; i < dayRewards.length; i++) {
            Integer day = Integer.parseInt(days[i]);
//            Integer modelId = Integer.parseInt(modelIds[i]);
            String modelData = ActivityManager.getInstance().getModelData(Integer.parseInt(modelIds[i]));
            List<ItemBean> rewards = ItemBean.split(dayRewards[i]);

            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("day", day);
            itemMap.put("modelId", modelData);
//            //这里处理模型的缩放,位置,旋转的信息,这里临时写一下,以后通过配置表来读取.
//            itemMap.put("s", 120);
//            itemMap.put("x", 0);
//            itemMap.put("y", 0);
//            itemMap.put("rx", 0);
//            itemMap.put("ry", 180);
//            itemMap.put("rz", 0);
            itemMap.put("rewards", rewards);
            dayRewardList.add(itemMap);
            dayCount++;
        }

        int serverCount=0;
        List<HashMap<String, Object>> serverRewardList = new ArrayList<>();
        for (int i = serverCount; i < serverRewards.length; i++) {
            Integer id = Integer.parseInt(boxIds[i]);
            Integer need = Integer.parseInt(needs[i]);
            List<ItemBean> rewards = ItemBean.split(serverRewards[i]);

            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("id", id);
            itemMap.put("need", need);
            itemMap.put("rewards", rewards);
            serverRewardList.add(itemMap);
            serverCount++;
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("buqianid", Integer.parseInt(buqianid[0]));
        resultMap.put("buqianCost", Integer.parseInt(buqianCost[0]));
        resultMap.put("daily", dayRewardList);
        resultMap.put("total", serverRewardList);

        resultMap.put("client", JsonUtils.toJSONString(resultMap));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
