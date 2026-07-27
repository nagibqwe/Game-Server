package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaozhaoguang
 * @desc FBShareNewYear  FB分享元旦版
 * @date Created on 2020/10/21 10:59
 **/
public class FBShare extends Activity implements IActivityCustom {

    public FBShare() {
    }

    public FBShare(Activity activity) {
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

        String[] i_RewardItems = paramMap.get("reward");

        if (i_RewardItems.length == 0 ) {
            throw new RuntimeException("===数据错误");
        }

        //奖励【道具ID_数量_是否绑定_职业;道具ID_数量_是否绑定_职业】
        String i_reward = i_RewardItems[0];
        List<ItemBean> awards = ItemBean.split(i_reward);

        HashMap<String, Object> hm = new HashMap();
        hm.put("awardList", awards);

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("awardList", awards);

        hm.put("client", JsonUtils.toJSONString(clientMap));

        this.setCustom(JsonUtils.toJSONString(hm));
        return this;
    }
}