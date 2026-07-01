package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2020/9/3 16:17
 * @Auth ZUncle
 */
public class LimitGiftBag extends Activity implements IActivityCustom {

    public LimitGiftBag() {
    }

    public LimitGiftBag(Activity activity) {
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

        String[] i_id = paramMap.get("i_id");
        String[] i_name = paramMap.get("i_name");
        String[] i_limit = paramMap.get("i_limit");
        String[] i_discount = paramMap.get("i_discount");
        String[] i_coin = paramMap.get("i_coin");
        String[] i_coin_count = paramMap.get("i_coin_count");
        String[] i_RewardGroup = paramMap.get("i_RewardGroup");

        if (i_id.length != i_name.length || i_id.length != i_limit.length || i_id.length != i_discount.length
                || i_id.length != i_coin.length ||i_id.length != i_coin_count.length||i_id.length != i_RewardGroup.length) {
            throw new RuntimeException("===数据格式错误");
        }

        HashMap<Integer, Object> data = new HashMap<>();
        for (int i= 0; i < i_id.length; i++) {

            int id = Integer.parseInt(i_id[i]);
            HashMap<String, Object> map = new HashMap<>();
            map.put("id", id);            //礼包ID
            map.put("price", Integer.parseInt(i_coin_count[i]));         //礼包价格
            map.put("costCoinType", Integer.parseInt(i_coin[i]));  //礼包出售货币类型
            map.put("discount", Float.valueOf(i_discount[i]));      //礼包折扣
            map.put("giftName", i_name[i]);      //礼包名字
            map.put("buyNum", Integer.parseInt(i_limit[i]));        //限购
            map.put("rewardDatas", ItemBean.split(i_RewardGroup[i]));

            if (data.containsKey(id)) {
                throw new RuntimeException("重复 礼包ID");
            }
            data.put(id, map);
        }

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("limitGiftDataHashMap", data);
        resultMap.put("client", JsonUtils.toJSONString(data));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
