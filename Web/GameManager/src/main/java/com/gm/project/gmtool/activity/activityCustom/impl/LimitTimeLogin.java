package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:17
 * @Auth ZUncle
 */
public class LimitTimeLogin extends Activity implements IActivityCustom {

    int limitDay = 28;

    public LimitTimeLogin() {
    }

    public LimitTimeLogin(Activity activity) {
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
        String[] i_RewardGroup = paramMap.get("i_RewardGroup");
        String[] i_BuyRewardGroup = paramMap.get("i_BuyRewardGroup");
        String[] buyMoneyType = paramMap.get("coinType"); //购买货币类型
        String[] buyMoneyNum = paramMap.get("coinCount"); //购买货币数量

        if (i_day.length > limitDay || i_RewardGroup.length != i_day.length || i_BuyRewardGroup.length != i_day.length) {
            throw new RuntimeException("===数据错误");
        }

        HashMap<Integer, List<ItemBean>> nHashMap = new HashMap<>();
        HashMap<Integer, List<ItemBean>> bHashMap = new HashMap<>();

        for (int i = 0; i< i_day.length; i++ ) {
            int day = Integer.parseInt(i_day[i]);
            String i_reward =  i_RewardGroup[i];
            nHashMap.put(day,ItemBean.split(i_reward));

            String i_buyReward =  i_BuyRewardGroup[i];
            bHashMap.put(day,ItemBean.split(i_buyReward));
        }
        List<List<ItemBean>> normalAward = new ArrayList<>();
        List<List<ItemBean>> buyAward = new ArrayList<>();
        for (int i = 1; i<= nHashMap.size(); i++) {
            List<ItemBean> itemBeans = nHashMap.get(i);
            if (itemBeans == null) {
                throw new RuntimeException("===缺失 day="+i);
            }
            normalAward.add(itemBeans);

            itemBeans = bHashMap.get(i);
            if (itemBeans == null) {
                throw new RuntimeException("===缺失 day="+i);
            }
            buyAward.add(itemBeans);
        }

        HashMap<String, Object> hm = new HashMap();
        hm.put("normalAwardList", normalAward);  //普通奖励【道具ID_数量_是否绑定_职业;道具ID_数量_是否绑定_职业】
        hm.put("buyAwardList", buyAward);    //购买奖励
        hm.put("buyMoneyType", Integer.parseInt(buyMoneyType[0]));  //购买的货币ID
        hm.put("buyMoneyNum", Integer.parseInt(buyMoneyNum[0]));  //购买的货币num

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("normalAwardList", normalAward);
        clientMap.put("buyAwardList", buyAward);
        clientMap.put("buyMoneyType", Integer.parseInt(buyMoneyType[0]));
        clientMap.put("buyMoneyNum", Integer.parseInt(buyMoneyNum[0]));


        hm.put("client", JsonUtils.toJSONString(clientMap));

        this.setCustom(JsonUtils.toJSONString(hm));
        return this;
    }
}
