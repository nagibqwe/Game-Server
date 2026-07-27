package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activityBossType.domain.ActivityBossType;
import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.manager.ActivityManager;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author gaozhaoguang
 * @desc HolidayWords
 * @date Created on 2020/10/23 10:14
 **/
public class HolidayWords extends Activity implements IActivityCustom {

    public HolidayWords() {
    }

    public HolidayWords(Activity activity) {
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


        //0.数据准备

        //掉落副本配置
        String[] dropCloneMap = paramMap.get("dropCloneMap");
        String[] cloneIdxArray = paramMap.get("cloneIdx");

        //掉落boss配置

        String[] bossIdxArray = paramMap.get("bossIdx");

        //兑换处理
        String[] exChangeLimit = paramMap.get("exChangeLimit");
        String[] isShowRedPoint = paramMap.get("isShowRedPoint");
        String[] wordItems = paramMap.get("wordItems");
        String[] rewardBox = paramMap.get("rewardBox");

        //1.副本奖励池处理
        if (dropCloneMap.length != cloneIdxArray.length ) {
            throw new RuntimeException("===副本数据错误");
        }

        //1.1副本处理
        HashMap<Integer, List<HashMap<String, Object>>> cloneMap = new HashMap<>();
        for (int i = 0; i < dropCloneMap.length; i++) {

            String[] dropWeight = paramMap.get("clone_dropWeight_"+cloneIdxArray[i]);
            String[] dropRewardItems = paramMap.get("clone_dropRewardItems_"+cloneIdxArray[i]);
            //奖励池处理
            if (dropWeight.length != dropRewardItems.length) {
                throw new RuntimeException("===副本奖励池数据错误");
            }

            List<HashMap<String, Object>> pool = new ArrayList<>();
            for (int j = 0; j < dropRewardItems.length; j++) {
                String rd = dropRewardItems[j];
                List<ItemBean> dropItems = ItemBean.split(rd);
                HashMap<String, Object> clone = new HashMap<>();
                clone.put("weight",Integer.parseInt(dropWeight[j]));
                clone.put("gift",dropItems);
                pool.add(clone);
            }

            cloneMap.put(Integer.parseInt(dropCloneMap[i]),pool);
        }


        //2 Boss奖池的处理
        HashMap<Integer, List<HashMap<String, Object>>> bossPoolMap = new HashMap<>();
        HashMap<Integer,Integer> bossMap = new HashMap<>();
        for (int i = 0; i < bossIdxArray.length; i++) {

            //2.1 boss列表
            String[] dropBoss = paramMap.get("bossList_"+bossIdxArray[i]);
            if(dropBoss.length <= 0){
                throw new RuntimeException("===必须选择boss类型");
            }

            for (String id:dropBoss) {
                ActivityBossType activityBossType = ActivityManager.getInstance().getActivityBoss(Integer.parseInt(id));
                String[] bossIds = activityBossType.getBossId().split("_");
                for (String bossId:bossIds) {
                    bossMap.put(Integer.parseInt(bossId),i+1);
                }
            }

            //2.2 boss奖池的处理
            String[] dropWeight = paramMap.get("boss_dropWeight_"+bossIdxArray[i]);
            String[] dropRewardItems = paramMap.get("boss_dropRewardItems_"+bossIdxArray[i]);

            //奖励池处理
            if (dropWeight.length != dropRewardItems.length) {
                throw new RuntimeException("===Boss奖励池数据错误");
            }
            List<HashMap<String, Object>> pool = new ArrayList<>();
            for (int j = 0; j < dropRewardItems.length; j++) {
                String rd = dropRewardItems[j];
                List<ItemBean> dropItems = ItemBean.split(rd);
                HashMap<String, Object> clone = new HashMap<>();
                clone.put("weight",Integer.parseInt(dropWeight[j]));
                clone.put("gift",dropItems);
                pool.add(clone);
            }
            bossPoolMap.put(i+1,pool);
        }



        //3.兑换处理
        if (exChangeLimit.length != wordItems.length|| exChangeLimit.length != rewardBox.length) {
            throw new RuntimeException("===兑换数据错误");
        }
        HashMap<Integer, Object> giftDataMap = new HashMap<>();
        for (int i = 0; i < rewardBox.length; i++) {

            HashMap<String, Object> data = new HashMap<>();

            data.put("id", i+1);
            //兑换次数
            data.put("limit", Integer.parseInt(exChangeLimit[i]));

            //是否显示小红点
            data.put("isShowRedPoint",Integer.parseInt(isShowRedPoint[i]));
            //收集字体
            String rd = wordItems[i];
            List<ItemBean> words = ItemBean.split(rd);
            data.put("words", words);

            //兑换宝箱
            rd = rewardBox[i];
            List<ItemBean> rewardBoxes = ItemBean.split(rd);
            data.put("box", rewardBoxes);

            giftDataMap.put(i+1, data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("clonePool", cloneMap);
        resultMap.put("bossPool", bossPoolMap);
        resultMap.put("boss", bossMap);
        resultMap.put("gift", giftDataMap);
        resultMap.put("client", JsonUtils.toJSONString(giftDataMap.values()));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}

