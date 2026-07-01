package com.backend.utils;

import com.backend.struct.ItemBean;
import com.backend.struct.LowestPro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ActivityUtil {

    //保底次数配置的信息组成
    public static HashMap<Integer, Object> getBaoDiDataInfo(HashMap<Integer, Object> lowestData, String[] i_baoDi_min_num, String[] i_baoDi_max_num, String[] i_baoDiReward,String[] i_baoDi_range_count,String[] i_baoDi_range_min,String[] i_baoDi_range_max,String[] i_baoDi_range_pro){
        int count = 0;
        for (int i = 0; i < i_baoDi_min_num.length; i++){
            List<LowestPro> lowestProList = new ArrayList<>();
            String i_baoDi_min_num_str = i_baoDi_min_num[i];
            String i_baoDi_max_num_str = i_baoDi_max_num[i];
            String i_baoDiReward_str = i_baoDiReward[i];

            Integer baoDi_min_num = Integer.parseInt(i_baoDi_min_num_str);
            Integer baoDi_max_num = Integer.parseInt(i_baoDi_max_num_str);
            List<ItemBean> list = ItemBean.split(i_baoDiReward_str);

            HashMap<String, Object> baodi = new HashMap<>();
            baodi.put("index",i);
            baodi.put("min",baoDi_min_num);
            baodi.put("max",baoDi_max_num);
            baodi.put("rewardData",list);
            for (int j = 0; j < Integer.parseInt(i_baoDi_range_count[i]); j++){
                LowestPro lowestPro = new LowestPro();
                int min = Integer.parseInt(i_baoDi_range_min[count]);
                int max = Integer.parseInt(i_baoDi_range_max[count]);
                int pro = Integer.parseInt(i_baoDi_range_pro[count]);
                count+=1;
                lowestPro.setMin(min);
                lowestPro.setMax(max);
                lowestPro.setPro(pro);
                lowestProList.add(lowestPro);
            }
            baodi.put("proList",lowestProList);
            lowestData.put(i,baodi);
        }

        return lowestData;
    }
}
