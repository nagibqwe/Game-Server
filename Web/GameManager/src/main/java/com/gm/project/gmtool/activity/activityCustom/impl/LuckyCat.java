package com.gm.project.gmtool.activity.activityCustom.impl;

import com.gm.project.gmtool.activity.activityCustom.IActivityCustom;
import com.gm.project.gmtool.activity.domain.Activity;
import com.gm.project.gmtool.utils.JsonUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LuckyCat extends Activity implements IActivityCustom {

    public LuckyCat() {}

    public LuckyCat(Activity activity) {
        super(activity);
    }

    public Activity parseCustom( Map<String, String[]> paramMap) {
        String[] rateStrs = paramMap.get("rate");
        String[] weightStr = paramMap.get("weight");
        String[] consumeStr = paramMap.get("consume");
        String[] recharge = paramMap.get("recharge");
        String[] minIndex = paramMap.get("minIndex");
        String[] maxIndex = paramMap.get("maxIndex");
        String[] serverRestNum = paramMap.get("serverRestNum");

        Integer[] rate = toIntArray(rateStrs);
        Integer[] weight = toIntArray(weightStr);

        List<Integer[]> gearList = new ArrayList<>();

        StringBuilder gearStr = new StringBuilder();
        Integer[] rateArray;
        for (int i = 0; i < consumeStr.length; i++) {
            rateArray = new Integer[5];
            rateArray[0] = Integer.parseInt(consumeStr[i]);
            rateArray[1] = Integer.parseInt(recharge[i]);
            rateArray[2] = Integer.parseInt(minIndex[i]);
            rateArray[3] = Integer.parseInt(maxIndex[i]);
            rateArray[4] = Integer.parseInt(serverRestNum[i]);
            gearList.add(rateArray);

            //传给客户端的字符串
            gearStr.append(consumeStr[i]).append("_").append(recharge[i]).append("_").append(minIndex[i]).append("_").append(maxIndex[i]).append("_").append(serverRestNum[i]).append("_").append(";");
        }

        HashMap<String,Object> resultMap = new HashMap<>();
        //服务器参数
        resultMap.put("rate", rate);
        resultMap.put("weight", weight);
        resultMap.put("gear", gearList);

        //客户端需要的参数
        StringBuilder rateStr = new StringBuilder();
        for(String r:rateStrs){
            rateStr.append(r).append("_");
        }

        HashMap<String,Object> clientMap = new HashMap<>();
        clientMap.put("rate", rateStr.toString());
        clientMap.put("gear", gearStr.toString());

        resultMap.put("client", JsonUtils.toJSONString(clientMap));
        this.setCustom(JsonUtils.toJSONString(resultMap));
        return this;
    }

    private Integer[] toIntArray(String[] sA){
        Integer[] ints = new Integer[sA.length];
        for (int i = 0; i < sA.length; i++) {
            ints[i] = Integer.parseInt(sA[i]);
        }
        return ints;
    }
}
