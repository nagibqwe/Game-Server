package com.gm.project.stat.stat_recharge_distribute.service.impl;

import java.util.*;

import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmtool.rechargeItem.domain.RechargeItem;
import com.gm.project.gmtool.rechargeItem.service.IRechargeItemService;
import com.gm.project.stat.stat_recharge_distribute.dao.IStatRechargeDistributeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.stat.stat_recharge_distribute.service.IStatRechargeDistributeService;

/**
 * 充值统计Service业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatRechargeDistributeServiceImpl implements IStatRechargeDistributeService
{

    @Autowired
    private IStatRechargeDistributeDao statPayDao;
    @Autowired
    private IRechargeItemService rechargeItemService;
    /**
     * 充值等级统计相关
     * @param startDate
     * @param endDate
     * @param selectServerIds
     * @param channelNames
     * @return
     */
    public List<Map<String, Object>> payLevelStat(String selectServerIds,String channelNames,String startDate, String endDate){
       return this.statPayDao.payLevelStat(selectServerIds,channelNames,startDate,endDate);
    }

    public List<Map<String, Object>> payDaylStat(String selectServerIds,String channelNames,String startDate){
        List<Map<String, Object>> payDayDataMap = this.statPayDao.payDaylStat(selectServerIds,channelNames,startDate);
        List<Map<String, Object>> dataMapList = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            int countSum = 0;
            float amountSum = 0;
            for (Map<String, Object> dataMap : payDayDataMap) {
                //String currency = dataMap.get("currency").toString();
                int timeHour = Integer.parseInt(dataMap.get("timeHour").toString());
                if (timeHour == i) {
                    countSum++;
                    amountSum += Float.parseFloat(dataMap.get("totalFee").toString()) ;
                }
            }
            Map<String, Object> map = new HashMap<>();
            String time = i + ":00" + "~" + (i + 1) + ":00";
            map.put("time", time);
            map.put("countSum", countSum);
            map.put("amountSum", amountSum);
            dataMapList.add(map);
        }

        return dataMapList;
    }

    public List<Map<String, Object>> payGoodIdslStat(String selectServerIds,String channelNames,String startDate, String endDate){
        List<Map<String, Object>> dataList = this.statPayDao.payGoodIdslStat(selectServerIds,channelNames,startDate,endDate);

        List<Map<String, Object>> payCoundataList = this.statPayDao.payCountStat(selectServerIds,channelNames,startDate,endDate);


        int countSum = 0;
        if(payCoundataList!=null){
            countSum = payCoundataList.size();
        }

        if(dataList!=null && dataList.size()>0){

            for(int i = 0;i<dataList.size();i++){
                Map<String, Object> dataBeanMap = dataList.get(i);


                int goodsId = Integer.parseInt(dataList.get(i).get("goodsId").toString());

                int count = Integer.parseInt(dataList.get(i).get("count").toString());
                RechargeItem selectRechargeItem = new RechargeItem();
                selectRechargeItem.setGoodsSystemCfgId(goodsId);


                List<RechargeItem> rechargeItemList =this.rechargeItemService.selectRechargeItemList(selectRechargeItem);
                if(rechargeItemList!=null && rechargeItemList.size()>0){
                    dataBeanMap.put("goodsName",rechargeItemList.get(0).getGoodsName());
                }
                float activpayrate   = (float) StatUtil.div(count*100, countSum,4);
                dataBeanMap.put("countRate",activpayrate);
            }

        }


        return dataList;
    }
}
