package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 节日特惠
 */
public class FestivalPreference extends Activity implements IActivityCustom {

    public FestivalPreference() {
    }

    public FestivalPreference(Activity activity) {
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

        String[] rechargeIds = paramMap.get("rechargeId");
        String[] rewards = paramMap.get("reward");

        if (rechargeIds.length != rewards.length) {
            throw new RuntimeException("===奖励数据错误" );
        }

        HashMap<Integer, Object> awardData = new HashMap<>();
        for (int i = 0; i < rechargeIds.length; i++) {
            String rechargeIdStr = rechargeIds[i];
            String reward = rewards[i];

            Integer rechargeId = Integer.parseInt(rechargeIdStr);
            if (awardData.containsKey(rechargeId)) {
                throw new RuntimeException("===重复的消耗目标" + rechargeId);
            }
            awardData.put(rechargeId, ItemBean.split(reward));
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("festivalRewardList", awardData);
        resultMap.put("client", JsonUtils.toJSONString(awardData));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
