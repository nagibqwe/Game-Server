package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.activity.domain.ItemBean;
import com.gm.project.gmtool.utils.ActivityUtil;
import com.gm.project.gmtool.utils.JsonUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class xianJingTreasureHunt extends Activity implements IActivityCustom {

    public xianJingTreasureHunt(){

    }

    public xianJingTreasureHunt(Activity activity){ super(activity);}

    /**
     * 解析自定义参数
     * @param paramMap
     * @return
     * @throws ParseException
     */
    @Override
    public Activity parseCustom(Map<String, String[]> paramMap) throws ParseException {
        //消耗配置
        String[] lotteryKeyItemId = paramMap.get("lotteryKeyItemId");
        String[] oneCostGemCoin = paramMap.get("oneCostGemCoin");
        String[] scoreID = paramMap.get("scoreID");
        String[] giveCoinNum = paramMap.get("giveCoinNum");
        //奖池配置
        String[] lotteryTabName = paramMap.get("lotteryTabName");
        String[] lotteryNeedKey = paramMap.get("lotteryNeedKey");
        String[] lotteryGetScore = paramMap.get("lotteryGetScore");
        String[] leftTopLotteryMapName = paramMap.get("leftTopLotteryMapName");
        String[] rightTopLotteryMapName = paramMap.get("rightTopLotteryMapName");
        String[] leftBottomLotteryMapName = paramMap.get("leftBottomLotteryMapName");
        String[] rightBottomLotteryMapName = paramMap.get("rightBottomLotteryMapName");
        String[] treasureChestId = paramMap.get("treasureChestId");

        String weightName = "weight";
        String[] weight;
        String isShowName = "isShow";
        String[] isShow;
        String weightRewardName = "weightReward";
        String[] weightReward;

        String boxWeightName = "boxWeight";
        String[] boxWeigh;
        String boxWeightRewardName = "boxWeightReward";
        String[] boxWeightReward;
        List<HashMap<String,Object>> XJTBLotteryPoolBeanList = new ArrayList<>();
        List<HashMap<String,Object>> ClientXJTBLotteryPoolBeanList = new ArrayList<>();
        for (int i=0;i < lotteryTabName.length;i++){
            List<HashMap<String,Object>> XJTBLotteryMapRewardList = new ArrayList<>();
            List<HashMap<String,Object>> XJTBTreasureChestRewardBeanList = new ArrayList<>();
            HashMap<String,Object> ClientXJTBLotteryPoolBean = new HashMap<>();
            HashMap<String,Object> XJTBLotteryPoolBean = new HashMap<>();
            XJTBLotteryPoolBean.put("lotteryTabName",lotteryTabName[i]);
            XJTBLotteryPoolBean.put("lotteryNeedKey",Integer.parseInt(lotteryNeedKey[i]));
            XJTBLotteryPoolBean.put("lotteryGetScore",Integer.parseInt(lotteryGetScore[i]));
            XJTBLotteryPoolBean.put("leftTopLotteryMapName",leftTopLotteryMapName[i]);
            XJTBLotteryPoolBean.put("rightTopLotteryMapName",rightTopLotteryMapName[i]);
            XJTBLotteryPoolBean.put("leftBottomLotteryMapName",leftBottomLotteryMapName[i]);
            XJTBLotteryPoolBean.put("rightBottomLotteryMapName",rightBottomLotteryMapName[i]);

            ClientXJTBLotteryPoolBean.put("lotteryTabName",lotteryTabName[i]);
            ClientXJTBLotteryPoolBean.put("lotteryNeedKey",lotteryNeedKey[i]);
            ClientXJTBLotteryPoolBean.put("lotteryGetScore",lotteryGetScore[i]);
            ClientXJTBLotteryPoolBean.put("treasureChestId",Integer.parseInt(treasureChestId[i]));
            ClientXJTBLotteryPoolBean.put("leftTopLotteryMapName",leftTopLotteryMapName[i]);
            ClientXJTBLotteryPoolBean.put("rightTopLotteryMapName",rightTopLotteryMapName[i]);
            ClientXJTBLotteryPoolBean.put("leftBottomLotteryMapName",leftBottomLotteryMapName[i]);
            ClientXJTBLotteryPoolBean.put("rightBottomLotteryMapName",rightBottomLotteryMapName[i]);
            ClientXJTBLotteryPoolBeanList.add(ClientXJTBLotteryPoolBean);

            weight = paramMap.get(weightName + (i+1));
            isShow = paramMap.get(isShowName + (i+1));
            weightReward = paramMap.get(weightRewardName + (i+1));
            for (int j=0;j < weight.length;j++){
                HashMap<String,Object> XJTBLotteryMapReward = new HashMap<>();
                XJTBLotteryMapReward.put("weight",Integer.parseInt(weight[j]));
                XJTBLotteryMapReward.put("isShow",Integer.parseInt(isShow[j]));
                XJTBLotteryMapReward.put("reward", ItemBean.split(weightReward[j]));
                XJTBLotteryMapRewardList.add(XJTBLotteryMapReward);
            }
            XJTBLotteryPoolBean.put("XJTBLotteryMapRewardList",XJTBLotteryMapRewardList);

            XJTBLotteryPoolBean.put("treasureChestId",Integer.parseInt(treasureChestId[i]));
            boxWeigh = paramMap.get(boxWeightName + (i+1));
            boxWeightReward = paramMap.get(boxWeightRewardName + (i+1));
            for (int k=0;k < boxWeigh.length;k++){
                HashMap<String,Object> XJTBTreasureChestRewardBean = new HashMap<>();
                XJTBTreasureChestRewardBean.put("weight",Integer.parseInt(boxWeigh[k]));
                XJTBTreasureChestRewardBean.put("reward",ItemBean.split(boxWeightReward[k]));
                XJTBTreasureChestRewardBeanList.add(XJTBTreasureChestRewardBean);
            }
            XJTBLotteryPoolBean.put("XJTBTreasureChestRewardBeanList",XJTBTreasureChestRewardBeanList);
            XJTBLotteryPoolBeanList.add(XJTBLotteryPoolBean);
        }
        //兑换商城
        List<HashMap<String,Object>> shopList = new ArrayList<>();
        String[] goodsId = paramMap.get("goodsId");
        String[] needScore = paramMap.get("needScore");
        String[] limitBuy = paramMap.get("limitBuy");
        String[] reward = paramMap.get("reward");
        for (int i=0;i < goodsId.length;i++){
            HashMap<String, Object> XJTBShopBean = new HashMap<>();
            XJTBShopBean.put("goodsId",Integer.parseInt(goodsId[i]));
            XJTBShopBean.put("needScore",Integer.parseInt(needScore[i]));
            XJTBShopBean.put("limitBuy",Integer.parseInt(limitBuy[i]));
            XJTBShopBean.put("reward",ItemBean.split(reward[i]));
            shopList.add(XJTBShopBean);
        }
        //保底次数配置
        HashMap<Integer, Object> lowestData = new HashMap<>();
        String[] i_baoDi_min_num = paramMap.get("i_baoDi_min_num");
        String[] i_baoDi_max_num = paramMap.get("i_baoDi_max_num");
        String[] i_baoDiReward = paramMap.get("i_baoDiReward");
        String[] i_baoDi_range_count = paramMap.get("i_baoDi_range_count");
        String[] i_baoDi_range_min = paramMap.get("i_baoDi_range_min");
        String[] i_baoDi_range_max = paramMap.get("i_baoDi_range_max");
        String[] i_baoDi_range_pro = paramMap.get("i_baoDi_range_pro");
        ActivityUtil.getBaoDiDataInfo(lowestData,i_baoDi_min_num,i_baoDi_max_num,i_baoDiReward,i_baoDi_range_count,i_baoDi_range_min,i_baoDi_range_max,i_baoDi_range_pro);

        //抽奖幸运值配置
        String[] oneLuckyValue = paramMap.get("oneLuckyValue");
        String[] luckyAwardGift = paramMap.get("luckyAwardGift");

        //最终结果(服务器数据)
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("lotteryKeyItemId",Integer.parseInt(lotteryKeyItemId[0]));
        resultMap.put("oneCostGemCoin",Integer.parseInt(oneCostGemCoin[0]));
        resultMap.put("giveCoinId",3);//固定赠送铜钱id
        resultMap.put("giveCoinNum",Integer.parseInt(giveCoinNum[0]));
        resultMap.put("scoreID",Integer.parseInt(scoreID[0]));
        resultMap.put("XJTBLotteryPoolBeanList",XJTBLotteryPoolBeanList);
        resultMap.put("shopList",shopList);
        resultMap.put("lowestData",lowestData);
        resultMap.put("luckyAwardList", ItemBean.split(luckyAwardGift[0]));
        resultMap.put("luckyValue", Integer.parseInt(oneLuckyValue[0]));

        //客户端数据
        HashMap<String, Object> resultClientMap = new HashMap<>();
        resultClientMap.put("lotteryKeyItemId",Integer.parseInt(lotteryKeyItemId[0]));
        resultClientMap.put("oneCostGemCoin",Integer.parseInt(oneCostGemCoin[0]));
        resultClientMap.put("giveCoinId",3);//固定赠送铜钱
        resultClientMap.put("giveCoinNum",Integer.parseInt(giveCoinNum[0]));
        resultClientMap.put("scoreID",Integer.parseInt(scoreID[0]));//积分ID
        resultClientMap.put("ClientXJTBLotteryPoolBeanList",ClientXJTBLotteryPoolBeanList);
        resultClientMap.put("lowestData",lowestData);
        resultClientMap.put("shopList",shopList);

        resultMap.put("client", JsonUtils.toJSONString(resultClientMap));

        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }
}
