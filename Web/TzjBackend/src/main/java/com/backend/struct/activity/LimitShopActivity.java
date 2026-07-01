package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 限时商城
 */
public class LimitShopActivity extends Activity implements IActivityCustom {

    public LimitShopActivity() {

    }

    public LimitShopActivity(Activity activity) {
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
        String[] i_server_limit = paramMap.get("i_server_limit");
        String[] i_discount = paramMap.get("i_discount");
        String[] i_coin = paramMap.get("i_coin");
        String[] i_coin_count = paramMap.get("i_coin_count");
        String[] i_RewardGroup = paramMap.get("i_RewardGroup");

        if (i_id.length != i_name.length || i_id.length != i_limit.length || i_id.length != i_server_limit.length|| i_id.length != i_discount.length
                || i_id.length != i_coin.length ||i_id.length != i_coin_count.length||i_id.length != i_RewardGroup.length) {
            throw new RuntimeException("===数据格式错误");
        }

        if(i_RewardGroup[0].split(";").length>3){
            throw new RuntimeException("===礼包内道具上限为3");
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
            map.put("serverBuyNum", Integer.parseInt(i_server_limit[i]));  //全服限购次数
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
