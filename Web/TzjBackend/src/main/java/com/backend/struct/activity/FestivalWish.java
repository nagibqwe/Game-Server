package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.ActivityUtil;
import com.backend.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 节日许愿
 */
public class FestivalWish extends Activity implements IActivityCustom {

    final int poolSize = 36;

    public FestivalWish() {
    }

    public FestivalWish(Activity activity) {
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

        String[] keyId = paramMap.get("keyId");
        String[] oneCostKey = paramMap.get("oneCostKey");
        String[] tenCostKey = paramMap.get("tenCostKey");
        String[] oneCostGold = paramMap.get("oneCostGold");
        String[] tenCostGold = paramMap.get("tenCostGold");

        String[] addWish = paramMap.get("addWish");
        String[] addScore = paramMap.get("addScore");
        String[] wishMax = paramMap.get("wishMax");
        String[] wasteGoldCount = paramMap.get("wasteGoldCount");
        //保底奖励
        String[] scores = paramMap.get("score");
        String[] baoDiRewards = paramMap.get("baoDiReward");
        //奖池奖励
        String[] keyWeights = paramMap.get("keyWeight");
        String[] goldWeights = paramMap.get("goldWeight");
        String[] isBigs = paramMap.get("isBig");
        String[] isShows = paramMap.get("isShow");
        String[] rewardDatas = paramMap.get("rewardData");
        //抽奖幸运值
        String[] oneLuckyValue = paramMap.get("oneLuckyValue");
        String[] luckyAwardGift = paramMap.get("luckyAwardGift");
        //保底次数
        String[] i_baoDi_min_num = paramMap.get("i_baoDi_min_num");
        String[] i_baoDi_max_num = paramMap.get("i_baoDi_max_num");
        String[] i_baoDiReward = paramMap.get("i_baoDiReward");
        String[] i_baoDi_range_count = paramMap.get("i_baoDi_range_count");
        String[] i_baoDi_range_min = paramMap.get("i_baoDi_range_min");
        String[] i_baoDi_range_max = paramMap.get("i_baoDi_range_max");
        String[] i_baoDi_range_pro = paramMap.get("i_baoDi_range_pro");

        if (scores.length != baoDiRewards.length) {
            throw new RuntimeException("===数据错误");
        }

        if (goldWeights.length != keyWeights.length || isBigs.length != keyWeights.length || rewardDatas.length != keyWeights.length || isShows.length != keyWeights.length) {
            throw new RuntimeException("==数据错误,当前" + keyWeights.length);
        }

        List<HashMap<String, Object>> lowestRewards = new ArrayList<>();
        List<HashMap<String, Object>> lowestRewardsClient = new ArrayList<>();
        for (int i = 0; i < scores.length; i++) {
            int score = Integer.parseInt(scores[i]);
            String rewardData = baoDiRewards[i];

            List<ItemBean> itemBeans = ItemBean.split(rewardData);

            HashMap<String, Object> item = new HashMap<>();
            item.put("score", score);
            item.put("rewardData", itemBeans.get(0));
            lowestRewards.add(item);

            HashMap<String, Object> itemClient = new HashMap<>();
            itemClient.put("b", itemBeans.get(0).getB());
            itemClient.put("i", itemBeans.get(0).getI());
            itemClient.put("c", itemBeans.get(0).getC());
            itemClient.put("n", itemBeans.get(0).getN());
            itemClient.put("s", score);
            lowestRewardsClient.add(itemClient);
        }

        List<HashMap<String, Object>> rewardPool = new ArrayList<>();
        List<HashMap<String, Object>> rewardPoolClient = new ArrayList<>();
        for (int i = 0; i < keyWeights.length; i++) {
            int keyWeight = Integer.parseInt(keyWeights[i]);
            int goldWeight = Integer.parseInt(goldWeights[i]);
            int isBig = Integer.parseInt(isBigs[i]);
            int isShow = Integer.parseInt(isShows[i]);
            String rewardData = rewardDatas[i];

            List<ItemBean> itemBeans = ItemBean.split(rewardData);

            HashMap<String, Object> item = new HashMap<>();
            item.put("keyWeight", keyWeight);
            item.put("goldWeight", goldWeight);
            item.put("isBig", isBig==1?true:false);
            item.put("rewardData", itemBeans.get(0));
            rewardPool.add(item);

            HashMap<String, Object> itemClient = new HashMap<>();
            itemClient.put("b", itemBeans.get(0).getB());
            itemClient.put("i", itemBeans.get(0).getI());
            itemClient.put("c", itemBeans.get(0).getC());
            itemClient.put("n", itemBeans.get(0).getN());
            itemClient.put("isS", isShow);
            itemClient.put("isB", isBig);
            rewardPoolClient.add(itemClient);
        }

        HashMap<Integer, Object> lowestData = new HashMap<>();
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("keyId", Integer.parseInt(keyId[0]));
        resultMap.put("oneCostKey", Integer.parseInt(oneCostKey[0]));
        resultMap.put("tenCostKey", Integer.parseInt(tenCostKey[0]));
        resultMap.put("oneCostGold", Integer.parseInt(oneCostGold[0]));
        resultMap.put("tenCostGold", Integer.parseInt(tenCostGold[0]));

        resultMap.put("addWish", Integer.parseInt(addWish[0]));
        resultMap.put("addScore", Integer.parseInt(addScore[0]));
        resultMap.put("wishMax", Integer.parseInt(wishMax[0]));
        resultMap.put("wasteGoldCount", Integer.parseInt(wasteGoldCount[0]));

        resultMap.put("lowestRewards", lowestRewards);
        resultMap.put("rewardPool", rewardPool);
        resultMap.put("lowestData",lowestData);

        HashMap<String, Object> resultClientMap = new HashMap<>();
        resultClientMap.put("keyId", Integer.parseInt(keyId[0]));
        resultClientMap.put("oneCostKey", Integer.parseInt(oneCostKey[0]));
        resultClientMap.put("tenCostKey", Integer.parseInt(tenCostKey[0]));
        resultClientMap.put("oneCostGold", Integer.parseInt(oneCostGold[0]));
        resultClientMap.put("tenCostGold", Integer.parseInt(tenCostGold[0]));
        resultClientMap.put("wishMax", Integer.parseInt(wishMax[0]));
        resultClientMap.put("lowestRewards", lowestRewardsClient);
        resultClientMap.put("rewardPool", rewardPoolClient);
        resultClientMap.put("lowestData", lowestData);

        resultMap.put("client", JsonUtils.toJSONString(resultClientMap));

        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
