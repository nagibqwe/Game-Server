package com.backend.struct.activity;

import com.backend.module.activity.script.IActivityCustom;
import com.backend.bean.Activity;
import com.backend.struct.ItemBean;
import com.backend.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 积分排行
 */
public class HolidayScoreRank extends Activity implements IActivityCustom {

    public HolidayScoreRank() {

    }

    public HolidayScoreRank(Activity activity) {
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

        String[] starts = paramMap.get("start");
        String[] tails = paramMap.get("tail");
        String[] rankScores = paramMap.get("rankScore");
        String[] rankRewards = paramMap.get("rankRewards");

        String[] scores = paramMap.get("score");
        String[] rewards = paramMap.get("rewards");

        String[] limit = paramMap.get("limit");
        String[] gold = paramMap.get("gold");

        if (starts.length != tails.length ||starts.length!=rankScores.length||starts.length!=rankRewards.length) {
            throw new RuntimeException("===排名数据错误" );
        }

        if (scores.length != rewards.length) {
            throw new RuntimeException("===积分数据错误" );
        }

        List<HashMap<String, Object>> rankDataList = new ArrayList<>();
        for (int i = 0; i < starts.length; i++) {
            String start = starts[i];
            String tail = tails[i];
            String score = rankScores[i];
            HashMap<String, Object> data = new HashMap<>();
            data.put("start", Integer.parseInt(start));
            data.put("tail", Integer.parseInt(tail));
            data.put("score", Integer.parseInt(score));
            data.put("items", ItemBean.split(rankRewards[i]));

            rankDataList.add(data);
        }

        HashMap<Integer, Object> awardData = new HashMap<>();
        for (int i = 0; i < scores.length; i++) {
            String score = scores[i];
            String reward = rewards[i];

            HashMap<String, Object> data = new HashMap<>();
            data.put("score", score);
            data.put("items", ItemBean.split(reward));

            awardData.put(Integer.parseInt(score), data);
        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("ranks", rankDataList);
        resultMap.put("scores", awardData);
        resultMap.put("limit", Integer.parseInt(limit[0]));
        resultMap.put("gold", Integer.parseInt(gold[0]));

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("ranks", rankDataList);
        clientMap.put("scores", awardData.values());
        resultMap.put("client", JsonUtils.toJSONString(clientMap));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
