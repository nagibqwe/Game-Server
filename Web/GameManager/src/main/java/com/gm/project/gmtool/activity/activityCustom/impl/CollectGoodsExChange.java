package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/10 22:54
 * @Auth ZUncle
 */
public class CollectGoodsExChange extends Activity implements IActivityCustom {

    public CollectGoodsExChange() {
    }

    public CollectGoodsExChange(Activity activity) {
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
        String[] exChangeMaterialsId = paramMap.get("exChangeMaterialsId");
        String[] returnMoneyCoinType = paramMap.get("returnMoneyCoinType");
        String[] returnMoneyCoinNum = paramMap.get("returnMoneyCoinNum");

        String[] exChangeTimes = paramMap.get("exChangeTimes");
        String[] exChangePrice = paramMap.get("exChangePrice");
        String[] rewardData = paramMap.get("reward");

        if (exChangeTimes.length != exChangePrice.length|| exChangeTimes.length != rewardData.length) {
            throw new RuntimeException("===兑换数据错误");
        }


        HashMap<Integer, Object> exChangeDataMap = new HashMap<>();
        for (int i = 0; i < rewardData.length; i++) {

            String rd = rewardData[i];
            List<ItemBean> rewards = ItemBean.split(rd);
            ItemBean reward = rewards.get(0);
            HashMap<String, Object> data = new HashMap<>();
            data.put("exChangeTimes", Integer.parseInt(exChangeTimes[i]));
            data.put("exChangePrice", Integer.parseInt(exChangePrice[i]));
            data.put("rewardData", reward);

            if (exChangeDataMap.containsKey(reward.getI())) {
                throw new RuntimeException("===重复的兑换道具" + reward.getI());
            }
            exChangeDataMap.put(i+1, data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("exChangeMaterialsId", Integer.parseInt(exChangeMaterialsId[0]));
        resultMap.put("returnMoneyCoinType", Integer.parseInt(returnMoneyCoinType[0]));
        resultMap.put("returnMoneyCoinNum", Integer.parseInt(returnMoneyCoinNum[0]));
        resultMap.put("exChangeDataMap", exChangeDataMap);
        resultMap.put("client", JsonUtils.toJSONString(resultMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
