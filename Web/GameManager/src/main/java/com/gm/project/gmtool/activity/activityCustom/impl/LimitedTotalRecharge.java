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
 * @Date 2020/9/3 16:18
 * @Auth ZUncle
 */
public class LimitedTotalRecharge extends Activity implements IActivityCustom {

    public LimitedTotalRecharge() {
    }

    public LimitedTotalRecharge(Activity activity) {
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

        String[] i_reach = paramMap.get("i_reach");
        String[] i_select = paramMap.get("i_select");
        String[] i_fixRewardGroup = paramMap.get("i_fixRewardGroup");
        String[] i_SelectRewardGroup = paramMap.get("i_SelectRewardGroup");


        if(i_select != null&&i_SelectRewardGroup == null){
            throw new RuntimeException("==>可选奖励组 未配置");
        }

        HashMap<Integer, Object> data = new HashMap<>();

        for (int i = 0; i < i_reach.length; i++) {
            Integer target = Integer.parseInt(i_reach[i]);
            int slen = i_select == null ? 0 : "".equals(i_select[i]) ? 0 : Integer.parseInt(i_select[i]);

            if(slen>0){
                List<ItemBean> unfix = ItemBean.split(i_SelectRewardGroup[i]);
                if (slen > unfix.size()) {
                    throw new RuntimeException("==>可选奖励组 数量低于 可选数量 len" + slen+",index="+i);
                }
            }

            HashMap<String, Object> map = new HashMap<>();
            map.put("targetRcharge", target);            // 充值金额
            map.put("customlen", slen);                // 可选奖励数量

            map.put("fixedRewardMap", ItemBean.split(i_fixRewardGroup[i])); //固定奖励组
            map.put("customRewardMap", ItemBean.split(i_SelectRewardGroup[i])); //可选奖励组
            if (data.containsKey(target)) {
                throw new RuntimeException("==>重复的达成目标");
            }
            data.put(target, map);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("rechargeTargetMap", data);
        resultMap.put("client", JsonUtils.toJSONString(data));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
