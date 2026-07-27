package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:18
 * @Auth ZUncle
 */
public class GroupBuy extends Activity implements IActivityCustom {

    public GroupBuy() {
    }

    public GroupBuy(Activity activity) {
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

        String[] career0 = paramMap.get("career0");             //职业对应的道具ID
        String[] career1 = paramMap.get("career1");             //职业对应的道具ID
        String[] career2 = paramMap.get("career2");             //职业对应的道具ID
        String[] career3 = paramMap.get("career3");             //职业对应的道具ID
        String[] itemNum = paramMap.get("targetItemNum");       //团购道具数量
        String[] costCoinType = paramMap.get("costCoinType");   //团购货币
        String[] oriPrice = paramMap.get("oriPrice");           //团购原价
        String[] buy_player = paramMap.get("buy_player");   //团购折扣数据  人数 <折扣，价格>
        String[] buy_discount = paramMap.get("buy_discount");   //团购折扣数据  人数 <折扣，价格>
        String[] buy_price = paramMap.get("buy_price");   //团购折扣数据  人数 <折扣，价格>

        HashMap<Integer, Object> items = new HashMap<>();
        items.put(0, Integer.parseInt(career0[0]));
        items.put(1, Integer.parseInt(career1[0]));
        items.put(2, Integer.parseInt(career2[0]));
        items.put(3, Integer.parseInt(career3[0]));

        HashMap<Integer, Object[]> cdmap = new HashMap<>();
        for (int i = 0; i < buy_player.length; i++) {
            int num = Integer.parseInt(buy_player[i]);
            if (cdmap.containsKey(num)) {
                throw new RuntimeException("重复的团购数量 num=" + num);
            }
            cdmap.put(num, new Object[]{Float.parseFloat(buy_discount[i]), Integer.parseInt(buy_price[i])});
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("itemId", items);
        resultMap.put("itemNum", Integer.parseInt(itemNum[0]));
        resultMap.put("costCoinType", Integer.parseInt(costCoinType[0]));
        resultMap.put("oriPrice", Integer.parseInt(oriPrice[0]));
        resultMap.put("countDiscountList", cdmap);

        resultMap.put("client", JsonUtils.toJSONString(resultMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
