package com.backend.struct.activity;

import com.backend.bean.Model;
import com.backend.manager.ActivityManager;
import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.bean.ActivityBossType;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首领狂欢（元旦）
 */
public class HolidayBoss extends Activity implements IActivityCustom {

    public HolidayBoss() {

    }

    public HolidayBoss(Activity activity) {
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
        //客户端数据
        String[] magicId = paramMap.get("magicId");//奖励展示
        String[] boxId = paramMap.get("boxId");//礼包奖励展示
        String[] showItems = paramMap.get("showItems");//奖励展示列表

        //出售商品数据
        String[] prices = paramMap.get("price");//单价
        String[] coins = paramMap.get("coin");//出售货币
        String[] limits = paramMap.get("limit");//单日限购
        String[] sellItems = paramMap.get("item");//出售道具

        String modelData = ActivityManager.getInstance().getModelData(Integer.parseInt(magicId[0]));

        if (prices.length != coins.length ||prices.length!=limits.length||prices.length!=sellItems.length) {
            throw new RuntimeException("===商品数据错误" );
        }
        //BOSS ID列表
        String[] bossLists = paramMap.get("bossList");
        if (bossLists.length<=0 ) {
            throw new RuntimeException("===数据错误" );
        }
        List<Integer> bossList = new ArrayList<>();
        for (String id:bossLists) {
                ActivityBossType activityBossType = ActivityManager.getInstance().getActivityBoss(Integer.parseInt(id));
                String[] bossIds = activityBossType.getBoss_id().split("_");
            for (String bossId:bossIds) {
                bossList.add(Integer.parseInt(bossId));
            }
        }

        //boss奖励权重
        String[] bossGiftWeights = paramMap.get("bossGiftWeight");
        //boss奖励串
        String[] bossGiftRewards = paramMap.get("bossGiftReward");

        if (bossGiftWeights.length != bossGiftRewards.length) {
            throw new RuntimeException("===boss奖励数据错误" );
        }

        //礼包ID
        String[] giftIds = paramMap.get("giftId");
        if (giftIds.length<=0 ) {
            throw new RuntimeException("===数据错误" );
        }

        String[] boxRewardCount = paramMap.get("boxRewardCount");

        //礼包奖励权重
        String[] boxGiftWeights = paramMap.get("boxGiftWeight");
        //礼包奖励串
        String[] boxGiftRewards = paramMap.get("boxGiftReward");

        if (boxGiftWeights.length != boxGiftRewards.length) {
            throw new RuntimeException("===礼包数据错误" );
        }

        HashMap<Integer, Object> goodData = new HashMap<>();
        for (int i = 0; i < prices.length; i++) {
            int id = i+1;
            int price = Integer.parseInt(prices[i]);
            int coin = Integer.parseInt(coins[i]);
            int limit = Integer.parseInt(limits[i]);
            String sellItem = sellItems[i];

            HashMap<String, Object> data = new HashMap<>();
            data.put("id", id);
            data.put("price", price);
            data.put("coin", coin);
            data.put("limit", limit);
            data.put("item", ItemBean.split(sellItem).get(0));

            goodData.put(id, data);
        }

        List<HashMap<String, Object>> bossDataList = new ArrayList<>();
        for (int i = 0; i < bossGiftWeights.length; i++) {
            HashMap<String, Object> data = new HashMap<>();
            data.put("weight", Integer.parseInt(bossGiftWeights[i]));
            data.put("gift", ItemBean.split(bossGiftRewards[i]));

            bossDataList.add(data);
        }

        int totalCount = 0;
        HashMap<Integer, Object> boxData = new HashMap<>();
        List<HashMap<String, Object>> boxDataList = new ArrayList<>();
        for (int i = 0; i < giftIds.length; i++) {
            int count = Integer.parseInt(boxRewardCount[i]);
            if(count < 0 ){
                throw new RuntimeException("===数据错误");
            }

            int start = totalCount;
            totalCount+=count;

            for (int j = start; j < totalCount; j++) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("weight", Integer.parseInt(boxGiftWeights[j]));
                data.put("gift", ItemBean.split(boxGiftRewards[j]));
                boxDataList.add(data);
            }
            boxData.put(Integer.parseInt(giftIds[i]), boxDataList);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("bossList", bossList);
        resultMap.put("bossGiftPool", bossDataList);
        resultMap.put("boxGiftPool", boxData);
        resultMap.put("goods", goodData);

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("magicId", modelData);
        clientMap.put("boxId", boxId[0]);
        clientMap.put("showItems", ItemBean.split(showItems[0]));
        clientMap.put("goods", goodData.values());

        resultMap.put("client", JsonUtils.toJSONString(clientMap));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
