package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.ActivityUtil;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/10 16:02
 * @Auth ZUncle
 */
public class DrawReward extends Activity implements IActivityCustom {

    final int poolSize = 36;

    public DrawReward() {
    }

    public DrawReward(Activity activity) {
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

        String[] p_round = paramMap.get("p_round");
        String[] p_reward = paramMap.get("p_reward");

        String[] g_s_reach = paramMap.get("g_s_reach");
        String[] g_p_reach = paramMap.get("g_p_reach");
        String[] g_reward = paramMap.get("g_reward");

        String[] i_weight = paramMap.get("i_weight");
        String[] i_big = paramMap.get("i_big");
        String[] i_reward = paramMap.get("i_reward");

        String[] i_big_limit = paramMap.get("i_big_limit");
        String[] i_costItem = paramMap.get("i_costItem");
        String[] i_costGold = paramMap.get("i_costGold");
        String[] i_GoldGift = paramMap.get("i_GoldGift");
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

        if (p_round.length != p_reward.length) {
            throw new RuntimeException("===轮次数据错误");
        }
        if (g_s_reach.length != g_p_reach.length || g_p_reach.length != g_reward.length) {
            throw new RuntimeException("===进度数据错误");
        }
        if (i_weight.length != poolSize || i_big.length != poolSize || i_reward.length != poolSize) {
            throw new RuntimeException("==奖池数据错误不是36组, 当前" + i_weight.length);
        }
        HashMap<Integer, Object> rounds = new HashMap<>();
        for (int i = 0; i < p_round.length; i++) {
            String reachStr = p_round[i];
            String reward = p_reward[i];

            Integer round = Integer.parseInt(reachStr);
            List<ItemBean> list = ItemBean.split(reward);
            if (rounds.containsKey(round)) {
                throw new RuntimeException("===重复的轮次" + round);
            }
            HashMap<String, Object> item = new HashMap<>();
            item.put("round", round);
            item.put("item", list);
            rounds.put(round, item);
        }
        HashMap<Integer, Object> prcs = new HashMap<>();
        for (int i = 0; i < g_s_reach.length; i++) {
            String s_reach = g_s_reach[i];
            String p_reach = g_p_reach[i];
            String reward = g_reward[i];

            Integer reach = Integer.parseInt(s_reach);
            List<ItemBean> list = ItemBean.split(reward);
            if (prcs.containsKey(reach)) {
                throw new RuntimeException("===重复的进度" + reach);
            }
            HashMap<String, Object> item = new HashMap<>();
            item.put("s_reach", reach);
            item.put("p_reach", Integer.parseInt(p_reach));
            item.put("item", list);
            prcs.put(reach, item);
        }

        HashMap<Integer, Object> draws = new HashMap<>();
        int bigCount = 0;
        for (int i = 0; i < i_weight.length; i++) {
            String weight = i_weight[i];
            String big = i_big[i];
            String reward = i_reward[i];
            int isbig = Integer.parseInt(big);
            if (isbig == 1) {
                bigCount += 1;
            }
            List<ItemBean> list = ItemBean.split(reward);

            HashMap<String, Object> item = new HashMap<>();
            item.put("id", i + 1);
            item.put("rate", Integer.parseInt(weight));
            item.put("big", isbig);
            item.put("item", list);
            draws.put(i + 1, item);
        }
        int big_limit = Integer.parseInt(i_big_limit[0]);
        if (big_limit > poolSize - bigCount) {
            throw  new RuntimeException("错误的大奖系数限制 超过普通奖励个数");
        }
        //保底奖励
        HashMap<Integer, Object> lowestData = new HashMap<>();
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("rounds", new ArrayList<>(rounds.values()));
        resultMap.put("prcs", new ArrayList<>(prcs.values()));
        resultMap.put("draws", new ArrayList<>(draws.values()));
        resultMap.put("costItem", Integer.parseInt(i_costItem[0]));
        resultMap.put("gold", Integer.parseInt(i_costGold[0]));
        resultMap.put("goldGift", ItemBean.split(i_GoldGift[0]).get(0));
        resultMap.put("lowestData",lowestData);

        resultMap.put("client", JsonUtils.toJSONString(resultMap));
        resultMap.put("bigLimit", big_limit);

        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
