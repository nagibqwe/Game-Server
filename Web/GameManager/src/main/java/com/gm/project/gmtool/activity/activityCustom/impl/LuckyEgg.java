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

public class LuckyEgg extends Activity implements IActivityCustom {

    public LuckyEgg() {
    }

    public LuckyEgg(Activity activity) {
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
        //客户端展示奖励
        String[] gridStrs = paramMap.get("grids");
        //砸蛋道具
        String[] i_costItem = paramMap.get("i_costItem");
        String[] i_oneCostItem = paramMap.get("i_oneCostItem");
        String[] i_oneCostGold = paramMap.get("i_oneCostGold");
        String[] dailyLimitCount = paramMap.get("dailyLimitCount");
        String[] i_GiveGift = paramMap.get("i_GiveGift");

        //刷新彩蛋权重 <彩蛋类型, 权重> 1彩蛋 2金蛋 3银蛋
        String[] i_item_one = paramMap.get("i_item_one");
        String[] i_item_two = paramMap.get("i_item_two");
        String[] i_item_three = paramMap.get("i_item_three");

        String[] i_gold_one = paramMap.get("i_gold_one");
        String[] i_gold_two = paramMap.get("i_gold_two");
        String[] i_gold_three = paramMap.get("i_gold_three");

        //奖池数据配置(彩蛋奖池数据)
        String[] i_item_weight_one = paramMap.get("i_item_weight_one");
        String[] i_gold_weight_one = paramMap.get("i_gold_weight_one");
        String[] isShow_one = paramMap.get("isShow_one");
        String[] i_reward_one = paramMap.get("i_reward_one");

        //奖池数据配置(金蛋奖池数据)
        String[] i_item_weight_two = paramMap.get("i_item_weight_two");
        String[] i_gold_weight_two = paramMap.get("i_gold_weight_two");
        String[] isShow_two = paramMap.get("isShow_two");
        String[] i_reward_two = paramMap.get("i_reward_two");

        //奖池数据配置(银蛋奖池数据)
        String[] i_item_weight_three = paramMap.get("i_item_weight_three");
        String[] i_gold_weight_three = paramMap.get("i_gold_weight_three");
        String[] isShow_three = paramMap.get("isShow_three");
        String[] i_reward_three = paramMap.get("i_reward_three");

        //保底次数配置
        String[] i_baoDi_min_num = paramMap.get("i_baoDi_min_num");
        String[] i_baoDi_max_num = paramMap.get("i_baoDi_max_num");
        String[] i_baoDiReward = paramMap.get("i_baoDiReward");
        String[] i_baoDi_range_count = paramMap.get("i_baoDi_range_count");
        String[] i_baoDi_range_min = paramMap.get("i_baoDi_range_min");
        String[] i_baoDi_range_max = paramMap.get("i_baoDi_range_max");
        String[] i_baoDi_range_pro = paramMap.get("i_baoDi_range_pro");

        //抽奖幸运值配置
        String[] oneLuckyValue = paramMap.get("oneLuckyValue");
        String[] luckyAwardGift = paramMap.get("luckyAwardGift");

        //免费礼包
        String[] onLineTime = paramMap.get("onLineTime");
        String[] timesLimit = paramMap.get("timesLimit");
        String[] freeGift = paramMap.get("freeGift");

        //彩蛋刷新配置
        String[] i_refresh_item = paramMap.get("i_refresh_item");
        String[] i_refresh_item_num = paramMap.get("i_refresh_item_num");
        String[] i_refresh_gold_num = paramMap.get("i_refresh_gold_num");
        String[] i_refresh = paramMap.get("i_refresh");

        //累计奖励
        String[] i_countReward_num = paramMap.get("i_countReward_num");
        String[] i_countReward = paramMap.get("i_countReward");

        List<List<ItemBean>> gridList = new ArrayList<>();
        for (int i = 0; i < gridStrs.length; i++) {
            String gridStr = gridStrs[i];
            List<ItemBean> list = ItemBean.split(gridStr);
            gridList.add(list);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        //彩蛋刷新
        resultMap.put("refreshItem",Integer.parseInt(i_refresh_item[0]));
        resultMap.put("refreshItemCost",1);
        resultMap.put("refreshGoldCost",Integer.parseInt(i_refresh_gold_num[0]));
        resultMap.put("refreshLowest",Integer.parseInt(i_refresh[0]));

        //砸蛋道具
        resultMap.put("costItemId",Integer.parseInt(i_costItem[0]));
        resultMap.put("oneCostItem",Integer.parseInt(i_oneCostItem[0]));
        resultMap.put("oneCostGold",Integer.parseInt(i_oneCostGold[0]));
        resultMap.put("dailyLimitCount",Integer.parseInt(dailyLimitCount[0]));
        resultMap.put("giftData", ItemBean.split(i_GiveGift[0]).get(0));

        //刷新彩蛋权重 <彩蛋类型, 权重> 1彩蛋 2金蛋 3银蛋
        HashMap<Integer, Object> eggWeightMap = new HashMap<>();
        getEggWeightMap(eggWeightMap,1,i_item_one,i_gold_one);
        getEggWeightMap(eggWeightMap,2,i_item_two,i_gold_two);
        getEggWeightMap(eggWeightMap,3,i_item_three,i_gold_three);
        resultMap.put("eggWeightMap",eggWeightMap);

        //彩蛋奖池信息 <彩蛋类型, 奖励信息>
        HashMap<Integer, Object> rewardPoolMap = new HashMap<>();
        getRewardPoolData(1,rewardPoolMap,i_item_weight_one,i_gold_weight_one,isShow_one,i_reward_one);
        getRewardPoolData(2,rewardPoolMap,i_item_weight_two,i_gold_weight_two,isShow_two,i_reward_two);
        getRewardPoolData(3,rewardPoolMap,i_item_weight_three,i_gold_weight_three,isShow_three,i_reward_three);
        resultMap.put("rewardPoolMap",rewardPoolMap);

        //保底奖励
        HashMap<Integer, Object> lowestData = new HashMap<>();
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);
//        for (int i = 0; i < i_baoDi_min_num.length; i++){
//            Integer baodiReward_num = Integer.parseInt(i_baoDi_min_num[i]);
//            List<ItemBean> baodiRewardlist = ItemBean.split(i_baodiReward[i]);
//            lowestMap.put(baodiReward_num,baodiRewardlist);
//        }
        resultMap.put("lowestMap",lowestData);

        //累计次数领奖 <累计领奖次数,累计奖励>
        HashMap<Integer, Object> countRewardMap = new HashMap<>();
        for (int i = 0; i < i_countReward_num.length; i++){
            Integer countReward_num = Integer.parseInt(i_countReward_num[i]);
            List<ItemBean> countRewardlist = ItemBean.split(i_countReward[i]);
            countRewardMap.put(countReward_num,countRewardlist);
        }
        resultMap.put("countRewardMap",countRewardMap);
        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));

        resultMap.put("onLineTime", Integer.parseInt(onLineTime[0]));
        resultMap.put("timesLimit", Integer.parseInt(timesLimit[0]));
        resultMap.put("freeGift", ItemBean.split(freeGift[0]));

        HashMap<String, Object> resultClientMap = new HashMap<>();
        resultClientMap.put("baodiMap",lowestData);

        resultClientMap.put("AwardList", gridList);
        resultClientMap.put("costItemId",Integer.parseInt(i_costItem[0]));
        resultClientMap.put("oneCostGold",Integer.parseInt(i_oneCostGold[0]));
        resultClientMap.put("giftData",ItemBean.split(i_GiveGift[0]).get(0));
        resultClientMap.put("dailyLimitCount",Integer.parseInt(dailyLimitCount[0]));
        resultClientMap.put("countRewardMap",countRewardMap);
        resultClientMap.put("refreshItem",Integer.parseInt(i_refresh_item[0]));
//        resultClientMap.put("refreshItemCost",Integer.parseInt(i_refresh_item_num[0]));
        resultClientMap.put("refreshGoldCost",Integer.parseInt(i_refresh_gold_num[0]));
        resultClientMap.put("onLineTime", Integer.parseInt(onLineTime[0]));
        resultClientMap.put("timesLimit", Integer.parseInt(timesLimit[0]));
        resultClientMap.put("freeGift", ItemBean.split(freeGift[0]));
        resultMap.put("client", JsonUtils.toJSONString(resultClientMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }

    //保底次数配置的信息组成
    private HashMap<Integer, Object> getBaoDiDataInfo(HashMap<Integer, Object> lowestMap, String[] i_baoDi_min_num, String[] i_baoDi_max_num, String[] i_baodiReward) {
        for (int i = 0; i < i_baoDi_min_num.length; i++){
            String i_baoDi_min_num_str = i_baoDi_min_num[i];
            String i_baoDi_max_num_str = i_baoDi_max_num[i];
            String i_baoDiReward_str = i_baodiReward[i];

            Integer baoDi_min_num = Integer.parseInt(i_baoDi_min_num_str);
            Integer baoDi_max_num = Integer.parseInt(i_baoDi_max_num_str);
            List<ItemBean> list = ItemBean.split(i_baoDiReward_str);

            HashMap<String, Object> baodi = new HashMap<>();
            baodi.put("index",i);
            baodi.put("min",baoDi_min_num);
            baodi.put("max",baoDi_max_num);
            baodi.put("rewardData",list);
            lowestMap.put(i,baodi);
        }
        return lowestMap;
    }

    private HashMap<Integer, Object> getEggWeightMap(HashMap<Integer, Object> eggWeightMap,int eggType, String[] itemWeight, String[] goldWeight) {
        HashMap<String, Object> eggWeight = new HashMap<>();
        eggWeight.put("itemWeight",Integer.parseInt(itemWeight[0]));
        eggWeight.put("goldWeight",Integer.parseInt(goldWeight[0]));
        eggWeightMap.put(eggType,eggWeight);

        return eggWeightMap;
    }
    private void getRewardPoolData(int eggType,HashMap<Integer, Object> rewardPoolMap,String[] item_weight,String[] gold_weight,String[] isShow,String[] i_reward){
        List<HashMap<String, Object>> RewardPoolData = new ArrayList<>();
        for (int i = 0; i < item_weight.length; i++){
            String i_item_weight_str = item_weight[i];
            String i_gold_weight_str = gold_weight[i];
            String i_isShow_str = isShow[i];
            String i_reward_str = i_reward[i];


            Integer item_weight_ = Integer.parseInt(i_item_weight_str);
            Integer gold_weight_ = Integer.parseInt(i_gold_weight_str);
            Integer is_Show = Integer.parseInt(i_isShow_str);
            List<ItemBean> rewardData = ItemBean.split(i_reward_str);

            HashMap<String, Object> RewardData = new HashMap<>();
            RewardData.put("itemWeight",item_weight_);
            RewardData.put("goldWeight",gold_weight_);
            RewardData.put("isShow",is_Show);
            RewardData.put("rewardData",rewardData);
            RewardPoolData.add(RewardData);
        }
        rewardPoolMap.put(eggType,RewardPoolData);
    }
}
