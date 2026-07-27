package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.ActivityUtil;
import com.gm.project.gmtool.utils.JsonUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cornucopia extends Activity implements IActivityCustom {

    public Cornucopia() {
    }

    public Cornucopia(Activity activity){
        super(activity);
    }

    /**
     * 解析自定义参数
     * @param paramMap
     * @return
     * @throws ParseException
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) throws ParseException {
        String[] gridStrs = paramMap.get("grids");
        String[] i_big_limit = paramMap.get("i_big_limit");
        String[] i_one_limit = paramMap.get("i_one_limit");
        String[] i_two_limit = paramMap.get("i_two_limit");
        String[] i_three_limit = paramMap.get("i_three_limit");
        String[] i_costItem = paramMap.get("i_costItem");
        String[] i_oneCostItem = paramMap.get("i_oneCostItem");
        String[] i_oneCostGold = paramMap.get("i_oneCostGold");
        String[] i_tenCostItem = paramMap.get("i_tenCostItem");
        String[] i_tenCostGold = paramMap.get("i_tenCostGold");
        String[] i_GiveGift = paramMap.get("i_GiveGift");

        String[] i_item_probability = paramMap.get("i_item_probability");
        String[] i_gold_probability = paramMap.get("i_gold_probability");
        String[] i_get_probability = paramMap.get("i_get_probability");
        String[] i_gold_init = paramMap.get("i_gold_init");
        String[] i_gold_standard = paramMap.get("i_gold_standard");
        String[] i_gold_upper_limit = paramMap.get("i_gold_upper_limit");
        String[] i_gold_set = paramMap.get("i_gold_set");
        String[] i_baodi_scope_min = paramMap.get("i_baodi_scope_min");
        String[] i_baodi_scope_max = paramMap.get("i_baodi_scope_max");
        String[] i_gold_limit = paramMap.get("i_gold_limit");

        String[] i_total_limit = paramMap.get("i_total_limit");
        String[] i_one_reward_limit = paramMap.get("i_one_reward_limit");
        String[] i_day_reward_limit = paramMap.get("i_day_reward_limit");

        //奖品等级权重
        String[] i_item_big = paramMap.get("i_item_big");
        String[] i_item_one = paramMap.get("i_item_one");
        String[] i_item_two = paramMap.get("i_item_two");
        String[] i_item_three = paramMap.get("i_item_three");

        String[] i_gold_big = paramMap.get("i_gold_big");
        String[] i_gold_one = paramMap.get("i_gold_one");
        String[] i_gold_two = paramMap.get("i_gold_two");
        String[] i_gold_three = paramMap.get("i_gold_three");

        //奖池数据
        String[] i_item_weight_big = paramMap.get("i_item_weight_big");
        String[] i_gold_weight_big = paramMap.get("i_gold_weight_big");
        String[] i_reward_big = paramMap.get("i_reward_big");

        String[] i_item_weight_one = paramMap.get("i_item_weight_one");
        String[] i_gold_weight_one = paramMap.get("i_gold_weight_one");
        String[] i_reward_one = paramMap.get("i_reward_one");

        String[] i_item_weight_two = paramMap.get("i_item_weight_two");
        String[] i_gold_weight_two = paramMap.get("i_gold_weight_two");
        String[] i_reward_two = paramMap.get("i_reward_two");

        String[] i_item_weight_three = paramMap.get("i_item_weight_three");
        String[] i_gold_weight_three = paramMap.get("i_gold_weight_three");
        String[] i_reward_three = paramMap.get("i_reward_three");

        //抽奖幸运值
        String[] oneLuckyValue = paramMap.get("oneLuckyValue");
        String[] luckyAwardGift = paramMap.get("luckyAwardGift");

        //累计领奖
        String[] i_countReward_num = paramMap.get("i_countReward_num");
        String[] i_countReward = paramMap.get("i_countReward");

        //免费礼包
        String[] activeValue = paramMap.get("activeValue");
        String[] freeGiftReward = paramMap.get("freeGiftReward");

        //保底次数
        String[] i_baoDi_min_num = paramMap.get("i_baoDi_min_num");
        String[] i_baoDi_max_num = paramMap.get("i_baoDi_max_num");
        String[] i_baoDiReward = paramMap.get("i_baoDiReward");
        String[] i_baoDi_range_count = paramMap.get("i_baoDi_range_count");
        String[] i_baoDi_range_min = paramMap.get("i_baoDi_range_min");
        String[] i_baoDi_range_max = paramMap.get("i_baoDi_range_max");
        String[] i_baoDi_range_pro = paramMap.get("i_baoDi_range_pro");

        if (gridStrs.length <= 0) {
            throw new RuntimeException("===客户端奖励展示数据错误或数据为空");
        }

        if (Integer.parseInt(i_baodi_scope_min[0]) >= Integer.parseInt(i_baodi_scope_max[0])) {
            throw new RuntimeException("===保底范围数据错误");
        }

        List<List<ItemBean>> gridList = new ArrayList<>();
        for (int i = 0; i < gridStrs.length; i++) {
            String gridStr = gridStrs[i];
            List<ItemBean> list = ItemBean.split(gridStr);
            gridList.add(list);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("limitLv",Integer.parseInt(i_big_limit[0]));
        resultMap.put("limitLv1",Integer.parseInt(i_one_limit[0]));
        resultMap.put("limitLv2",Integer.parseInt(i_two_limit[0]));
        resultMap.put("limitLv3",Integer.parseInt(i_three_limit[0]));
        resultMap.put("itemId",Integer.parseInt(i_costItem[0]));
        resultMap.put("oneCostItem",Integer.parseInt(i_oneCostItem[0]));
        resultMap.put("oneCostGold",Integer.parseInt(i_oneCostGold[0]));
        resultMap.put("tenCostItem",Integer.parseInt(i_tenCostItem[0]));
        resultMap.put("tenCostGold",Integer.parseInt(i_tenCostGold[0]));
        resultMap.put("giftData", ItemBean.split(i_GiveGift[0]).get(0));

        resultMap.put("goldItemPro",Integer.parseInt(i_item_probability[0]));
        resultMap.put("goldPro",Integer.parseInt(i_gold_probability[0]));
        resultMap.put("goldPoolPer",Integer.parseInt(i_get_probability[0]));
        resultMap.put("goldInitCount",Integer.parseInt(i_gold_init[0]));
        resultMap.put("sysAddBaseValue",Integer.parseInt(i_gold_standard[0]));
        resultMap.put("sysAddLimit",Integer.parseInt(i_gold_upper_limit[0]));
        resultMap.put("sysAddCount",Integer.parseInt(i_gold_set[0]));
        resultMap.put("goldBigMin",Integer.parseInt(i_baodi_scope_min[0]));
        resultMap.put("goldBigMax",Integer.parseInt(i_baodi_scope_max[0]));
        resultMap.put("limitGold",Integer.parseInt(i_gold_limit[0]));

        resultMap.put("goldMaxCount",Integer.parseInt(i_total_limit[0]));
        resultMap.put("goldOneMaxCount",Integer.parseInt(i_one_reward_limit[0]));
        resultMap.put("goldDailyCount",Integer.parseInt(i_day_reward_limit[0]));

        //奖品等级权重 <奖品等级, 奖品等级权重>
        HashMap<Integer, Object> levelWeightMap = new HashMap<>();
        getLevelWeightMap(0,levelWeightMap,i_item_big,i_gold_big);
        getLevelWeightMap(1,levelWeightMap,i_item_one,i_gold_one);
        getLevelWeightMap(2,levelWeightMap,i_item_two,i_gold_two);
        getLevelWeightMap(3,levelWeightMap,i_item_three,i_gold_three);

        //奖池信息 <奖品等级, 奖励信息>
        HashMap<Integer, Object> rewardPoolMap = new HashMap<>();
        getRewardPoolInfo(0,rewardPoolMap,i_item_weight_big,i_gold_weight_big,i_reward_big);
        getRewardPoolInfo(1,rewardPoolMap,i_item_weight_one,i_gold_weight_one,i_reward_one);
        getRewardPoolInfo(2,rewardPoolMap,i_item_weight_two,i_gold_weight_two,i_reward_two);
        getRewardPoolInfo(3,rewardPoolMap,i_item_weight_three,i_gold_weight_three,i_reward_three);

        //保底奖励   达到保底次数之后随机从的对应的奖品等级的池子中取一个奖品
        HashMap<Integer, Object> lowestData = new HashMap<>();
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);

        //累计领奖 <累计领奖次数,累计奖励>
        HashMap<Integer, Object> accRewardMap = new HashMap<>();
        for (int i = 0; i < i_countReward_num.length; i++){
            String i_countReward_num_str = i_countReward_num[i];
            String i_countReward_str = i_countReward[i];
            Integer countReward_num = Integer.parseInt(i_countReward_num_str);
            List<ItemBean> list = ItemBean.split(i_countReward_str);
            accRewardMap.put(countReward_num,list);
        }

        //免费礼包
        HashMap<Integer, Object> freeGiftMap = new HashMap<>();
        for (int i = 0; i < activeValue.length; i++){
            String activeValuestr = activeValue[i];
            String freeGiftRewardstr = freeGiftReward[i];
            Integer activeValueRequire = Integer.parseInt(activeValuestr);
            List<ItemBean> list = ItemBean.split(freeGiftRewardstr);
            freeGiftMap.put(activeValueRequire,list);
        }

        resultMap.put("levelWeightMap",levelWeightMap);
        resultMap.put("rewardPoolMap",rewardPoolMap);
        resultMap.put("lowestData",lowestData);
        resultMap.put("accRewardMap",accRewardMap);
        resultMap.put("freeGiftMap",freeGiftMap);

        HashMap<String, Object> resultClientMap = new HashMap<>();
        resultClientMap.put("AwardList", gridList);
        resultClientMap.put("CostItemId", Integer.parseInt(i_costItem[0]));
        resultClientMap.put("CostGoldCount", Integer.parseInt(i_oneCostGold[0]));
//        resultClientMap.put("tenCostItemCount",Integer.parseInt(i_tenCostItem[0]));
//        resultClientMap.put("tenCostGoldCount",Integer.parseInt(i_tenCostGold[0]));
        resultClientMap.put("CountAwardList", accRewardMap);
        resultClientMap.put("freeGiftMap", freeGiftMap);
        resultClientMap.put("GoldAwardPercent", Integer.parseInt(i_get_probability[0]));
        resultClientMap.put("giftData", ItemBean.split(i_GiveGift[0]).get(0));
        resultClientMap.put("lowestData", lowestData);
        resultClientMap.put("goldOneMaxCount",Integer.parseInt(i_one_reward_limit[0]));
        resultMap.put("client", JsonUtils.toJSONString(resultClientMap));

        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }

    //奖池数据配置的奖励信息
    private HashMap<Integer, Object> getRewardPoolInfo(int level,HashMap<Integer, Object> rewardPoolMap,String[] item_weight,String[] gold_weight,String[] reward){
        List<HashMap<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < item_weight.length; i++) {
            String i_item_weight_str = item_weight[i];
            String i_gold_weight_str = gold_weight[i];
            String i_reward_str = reward[i];

            Integer item_weight_ = Integer.parseInt(i_item_weight_str);
            Integer gold_weight_ = Integer.parseInt(i_gold_weight_str);
            List<ItemBean> list = ItemBean.split(i_reward_str);

            HashMap<String, Object> item = new HashMap<>();
            item.put("itemWeight", item_weight_);
            item.put("goldWeight", gold_weight_);
            item.put("rewardData", list);
            mapList.add(item);
        }
        rewardPoolMap.put(level, mapList);
        return rewardPoolMap;
    }
    //奖品等级权重 <奖品等级, 奖品等级权重>
    private HashMap<Integer, Object> getLevelWeightMap(int level,HashMap<Integer, Object> levelWeightMap,String[] item,String[] gold){
        HashMap<String, Object> levelWeight = new HashMap<>();
        levelWeight.put("itemWeight",Integer.parseInt(item[0]));
        levelWeight.put("goldWeight",Integer.parseInt(gold[0]));
        levelWeightMap.put(level,levelWeight);

        return levelWeightMap;
    }
}
