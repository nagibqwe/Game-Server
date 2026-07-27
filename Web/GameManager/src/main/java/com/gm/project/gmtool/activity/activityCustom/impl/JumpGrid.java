package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.ActivityUtil;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO 掷骰子
 */
public class JumpGrid extends Activity implements IActivityCustom {

    public JumpGrid() {
    }

    public JumpGrid(Activity activity) {
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

        //消耗元宝
        String[] costGoldStr = paramMap.get("costGold");
        //消耗色子
        String[] costStr = paramMap.get("cost");
        //每日凌晨获得色子
        String[] dailyGainStrs = paramMap.get("dailyGain");

        //格子奖励
        String[] gridStrs = paramMap.get("grids");

        //大奖奖励
        String[] bigGiftWeights = paramMap.get("bigGiftWeight");
        String[] bigGiftRewards = paramMap.get("bigGiftReward");

        if (bigGiftWeights.length != bigGiftRewards.length) {
            throw new RuntimeException("===大奖奖励数据错误" );
        }

        //个人通关进度
        String[] playerProc = paramMap.get("playerProc");
        //个人通关进度奖励
        String[] playerTimes = paramMap.get("playerTimes");
        //全服通关进度
        String[] serverProc = paramMap.get("serverProc");
        //全服通关进度奖励
        String[] serverTimes = paramMap.get("serverTimes");
        //抽奖幸运值
        String[] oneLuckyValue = paramMap.get("oneLuckyValue");
        String[] luckyAwardGift = paramMap.get("luckyAwardGift");
        //保底次数
        String[] i_baoDi_min_num = paramMap.get("i_baoDi_min_num");
        String[] i_baoDi_max_num = paramMap.get("i_baoDi_max_num");
        String[] i_baoDiReward = paramMap.get("i_baoDiReward");
        String[] i_baoDi_range_count = paramMap.get("i_baoDi_range_count");
        String[] i_baoDi_range_min = paramMap.get("i_baoDi_range_min");
        String[] i_baoDi_range_max = paramMap.get("i_baoDi_range_max");
        String[] i_baoDi_range_pro = paramMap.get("i_baoDi_range_pro");

        if (costGoldStr.length <= 0 || costStr.length <= 0 || dailyGainStrs.length <= 0 || gridStrs.length <= 0 ||
                playerProc.length <= 0 || playerTimes.length <= 0 || serverProc.length <= 0 || serverTimes.length <= 0) {
            throw new RuntimeException("===数据错误或数据为空");
        }
        if (playerProc.length != playerTimes.length) {
            throw new RuntimeException("===个人进度数据错误");
        }
        if (serverProc.length != serverTimes.length) {
            throw new RuntimeException("===服务器进度数据错误");
        }

        List<ItemBean> dailyGains = ItemBean.split(dailyGainStrs[0]);

        List<List<ItemBean>> gridList = new ArrayList<>();
        for (int i = 0; i < gridStrs.length; i++) {
            String gridStr = gridStrs[i];
            List<ItemBean> list = ItemBean.split(gridStr);
            gridList.add(list);
        }

        List<ItemBean> bigList = new ArrayList<>();
        List<HashMap<String, Object>> bigDataList = new ArrayList<>();
        for (int i = 0; i < bigGiftWeights.length; i++) {
            List<ItemBean> beanList = ItemBean.split(bigGiftRewards[i]);
            HashMap<String, Object> data = new HashMap<>();
            data.put("bigGiftWeight", Integer.parseInt(bigGiftWeights[i]));
            data.put("gift", beanList);
            bigList.addAll(beanList);
            bigDataList.add(data);
        }
        gridList.add(bigList);

        int playerCount=0;
        HashMap<Integer, Object> playerReward = new HashMap<>();
        for (int i = playerCount; i < playerTimes.length; i++) {
            String procStr = playerProc[i];
            String rewardStr = playerTimes[i];

            Integer proc = Integer.parseInt(procStr);
            List<ItemBean> list = ItemBean.split(rewardStr);

            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("proc", proc);
            itemMap.put("items", list);
            playerReward.put(proc, itemMap);
            playerCount++;
        }

        int serverCount=0;
        HashMap<Integer, Object> serverReward = new HashMap<>();
        for (int i = serverCount; i < serverTimes.length; i++) {
            String procStr = serverProc[i];
            String rewardStr = serverTimes[i];

            Integer proc = Integer.parseInt(procStr);
            List<ItemBean> list = ItemBean.split(rewardStr);

            HashMap<String, Object> itemMap = new HashMap<>();
            itemMap.put("proc", proc);
            itemMap.put("items", list);
            serverReward.put(proc, itemMap);
            serverCount++;
        }

        //保底奖励
        HashMap<Integer, Object> lowestData = new HashMap<>();
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);

        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("costGold", Integer.parseInt(costGoldStr[0]));
        resultMap.put("cost", ItemBean.split(costStr[0]).get(0));
        resultMap.put("grids", gridList);
        resultMap.put("playerTimes", playerReward);
        resultMap.put("serverTimes", serverReward);
        resultMap.put("lowestData",lowestData);

        resultMap.put("client", JsonUtils.toJSONString(resultMap));

        resultMap.put("dailyGain", dailyGains);
        resultMap.put("bigGiftPool", bigDataList);
        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
