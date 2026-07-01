package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.JsonUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FangZeTreasureHunt extends Activity implements IActivityCustom {

    public FangZeTreasureHunt(){

    }

    public FangZeTreasureHunt(Activity activity){ super(activity);}


    /**
     * 解析自定义参数
     * @param paramMap
     * @return
     * @throws ParseException
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) throws ParseException {
        //探宝道具
        String[] drawItemIdArr = paramMap.get("drawItemId");
        String[] drawItemNeedLingyuArr = paramMap.get("drawItemNeedLingyu");

        //探宝设置
        String[] allFinishDrawItemNumArr = paramMap.get("allFinishDrawItemNum");
        String[] openNextMapDrawNumArr = paramMap.get("openNextMapDrawNum");
        String[] resetNeedlingyuArr = paramMap.get("resetNeedlingyu");

        List<HashMap<String,Object>> FZTBMapList = new ArrayList<>();

        for (int i = 0; i < allFinishDrawItemNumArr.length; i++){
            HashMap<String, Object> FZTBMapBean = new HashMap<>();
            String allFinishDrawItemNumStr = allFinishDrawItemNumArr[i];
            Integer allFinishDrawItemNum = Integer.parseInt(allFinishDrawItemNumStr);
            String openNextMapDrawNumStr = openNextMapDrawNumArr[i];
            Integer openNextMapDrawNum = Integer.parseInt(openNextMapDrawNumStr);
            String resetNeedlingyuStr = resetNeedlingyuArr[i];
            Integer resetNeedlingyu = Integer.parseInt(resetNeedlingyuStr);

            //奖励设置
            String[] weightArr = paramMap.get("weight"+(i+1));
            String[] minDrawNumArr = paramMap.get("minDrawNum"+(i+1));
            String[] i_RewardGroupArr = paramMap.get("i_RewardGroup"+(i+1));

            //消耗设置
            String[] costNumArr = paramMap.get("costNum"+(i+1));

            List<HashMap<String,Object>> FZTBMapRewardBeanList = new ArrayList<>();
            List<Integer> costNumList = new ArrayList<>();
            for (int j = 0; j < allFinishDrawItemNumArr.length; j++){
                HashMap<String,Object> FZTBMapRewardBean = new HashMap<>();
                String weightStr = weightArr[j];
                Integer weight = Integer.parseInt(weightStr);
                String minDrawNumStr = minDrawNumArr[j];
                Integer minDrawNum = Integer.parseInt(minDrawNumStr);
                String i_RewardGroupStr = i_RewardGroupArr[j];
                List<ItemBean> reward = ItemBean.split(i_RewardGroupStr);

                FZTBMapRewardBean.put("weight",weight);
                FZTBMapRewardBean.put("minDrawNum",minDrawNum);
                FZTBMapRewardBean.put("reward",reward);
                FZTBMapRewardBeanList.add(FZTBMapRewardBean);

                String costNumStr = costNumArr[j];
                Integer costNum = Integer.parseInt(costNumStr);
                costNumList.add(costNum);
            }

            FZTBMapBean.put("allFinishDrawItemNum",allFinishDrawItemNum);
            FZTBMapBean.put("openNextMapDrawNum",openNextMapDrawNum);
            FZTBMapBean.put("resetNeedlingyu",resetNeedlingyu);
            FZTBMapBean.put("FZTBMapRewardBeanList",FZTBMapRewardBeanList);
            FZTBMapBean.put("costNumList",costNumList);
            FZTBMapList.add(FZTBMapBean);

        }
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("drawItemId",Integer.parseInt(drawItemIdArr[0]));
        resultMap.put("drawItemNeedLingyu",Integer.parseInt(drawItemNeedLingyuArr[0]));
        resultMap.put("FZTBMapList",FZTBMapList);

        resultMap.put("client", JsonUtils.toJSONString(resultMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));

        return this;
    }
}
