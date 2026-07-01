package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:16
 * @Auth ZUncle
 */
public class GetActive extends Activity implements IActivityCustom {

    public GetActive() {
    }

    public GetActive(Activity activity) {
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

        String[] coinType = paramMap.get("coinType");
        String[] coinCv = paramMap.get("coinCv");

        String[] reachs = paramMap.get("reach");
        String[] rewards = paramMap.get("reward");
        String[] showRewards = paramMap.get("showReward");

        if (coinType.length != coinCv.length) {
            throw new RuntimeException("===兑换数据错误" );
        }

        if (reachs.length != rewards.length || reachs.length != showRewards.length) {
            throw new RuntimeException("===奖励数据错误" );
        }

        HashMap<Integer, Integer> coinRatio = new HashMap<>();
        for (int i = 0; i < coinType.length; i++) {
            String ct = coinType[i];
            String cc = coinCv[i];
            Integer coin = Integer.parseInt(ct);
            if (coinRatio.containsKey(coin)) {
                throw new RuntimeException("===重复的货币类型" + coin);
            }
            coinRatio.put(coin, Integer.parseInt(cc));
        }

        HashMap<Integer, Object> awardData = new HashMap<>();
        for (int i = 0; i < reachs.length; i++) {
            String reachStr = reachs[i];
            String reward = rewards[i];
            String showReward = showRewards[i];

            HashMap<String, Object> data = new HashMap<>();
            data.put("showItem", Integer.parseInt(showReward));
            data.put("awardList", ItemBean.split(reward));

            Integer reach = Integer.parseInt(reachStr);
            if (awardData.containsKey(reach)) {
                throw new RuntimeException("===重复的消耗目标" + reach);
            }
            awardData.put(reach, data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("awardData", awardData);
        resultMap.put("coinRatio", coinRatio);
        resultMap.put("client", JsonUtils.toJSONString(awardData));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
