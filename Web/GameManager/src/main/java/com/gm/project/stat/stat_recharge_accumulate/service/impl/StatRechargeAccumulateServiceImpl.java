package com.gm.project.stat.stat_recharge_accumulate.service.impl;

import com.game.util.ListToStringUtil;
import com.gm.common.utils.StringUtils;
import com.gm.project.common.utils.StatUtil;
import com.gm.project.gmtool.manager.BlackListManager;
import com.gm.project.stat.stat_recharge_accumulate.dao.IStatRechargeAccumulateDao;
import com.gm.project.stat.stat_recharge_accumulate.domain.RechargeAccumulateBean;
import com.gm.project.stat.stat_recharge_accumulate.service.IStatRechargeAccumulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 充值统计Service业务层处理
 * 
 * @author gm
 * @date 2021-09-13
 */
@Service
public class StatRechargeAccumulateServiceImpl implements IStatRechargeAccumulateService
{

    @Autowired
    private IStatRechargeAccumulateDao statRechargeAccumulateDao;


    /**
     * 累计充值统计
     * @param channelNames
     * @param selectServerIdList
     * @param startDate
     * @param endDate
     * @return
     */
    public List<RechargeAccumulateBean> statRechargeAccumulate(String selectGroupName, String selectServerIdList,String channelNames,String startDate, String endDate,Boolean isBlack){

        if (!StringUtils.isBlank(channelNames)) {
            channelNames = "'" + channelNames + "'";
            channelNames = channelNames.replace(",", "','");
        }
        //黑名单排除
        String blackUsers = "";
        if (isBlack!=null && isBlack) {
            List<Object> blackList = BlackListManager.getInstance().getBlackListUsers(selectGroupName);
            if (blackList.isEmpty()) {
                isBlack = false;
            } else {
                blackUsers = ListToStringUtil.listToString(blackList);
            }
        }

        List<RechargeAccumulateBean> dataList = new ArrayList<>();
        //Map<String, Object> accumulateRechargeMap = new LinkedHashMap<>();
        List<Map<String, Object>> accumulateRechargeMapList = this.statRechargeAccumulateDao.getRechargeAccumulateList(selectServerIdList,channelNames,startDate,endDate,blackUsers);
        int[] amountValue = new int[]{0, 6000, 9800, 19800, 49800, 99800, 199800, 499800, 999800, 1999800, 2999800};
        Map<Integer, RechargeAccumulateBean> rechargeAmountMap = new TreeMap<>();
        for (int value : amountValue) {
            RechargeAccumulateBean accumulateRechargeBean = new RechargeAccumulateBean();
            accumulateRechargeBean.setAmount(value);

            accumulateRechargeBean.setAccumulateAmount(getAmountName(value));
            rechargeAmountMap.put(value, accumulateRechargeBean);
        }
        if (accumulateRechargeMapList.size() != 0) {
            for (Map<String, Object> map : accumulateRechargeMapList) {
                double amount = Float.parseFloat(map.get("amount").toString());
                for (int i = 0; i < amountValue.length; i++) {
                    int count = 0;
                    float sum = 0;
                    if (i != (amountValue.length - 1)) {
                        if (amount > amountValue[i] && amount <= amountValue[i + 1]) {
                            count++;
                            sum += amount;
                            if (rechargeAmountMap.containsKey(amountValue[i])) {
                                count += rechargeAmountMap.get(amountValue[i]).getRoleNum();
                                sum += rechargeAmountMap.get(amountValue[i]).getRechargeAmount();
                            }
                            rechargeAmountMap.get(amountValue[i]).setRoleNum(count);
                            rechargeAmountMap.get(amountValue[i]).setRechargeAmount(sum);
                            //rechargeAmountMap.put(amountValue[i], count + "," + sum);
                        }
                    } else {
                        if (amount > amountValue[i]) {
                            count++;
                            sum += amount;
                            if (rechargeAmountMap.containsKey(amountValue[i])) {
                                count += rechargeAmountMap.get(amountValue[i]).getRoleNum();
                                sum += rechargeAmountMap.get(amountValue[i]).getRechargeAmount();
                            }
                            rechargeAmountMap.get(amountValue[i]).setRoleNum(count);
                            rechargeAmountMap.get(amountValue[i]).setRechargeAmount(sum);
                        }
                    }
                }
            }
            for (int key : rechargeAmountMap.keySet()) {

                float roleNumRate   = (float) StatUtil.div(rechargeAmountMap.get(key).getRoleNum()*100, accumulateRechargeMapList.size(),2);
               // float newRoleNumRate  = roleNumRate*100;
                rechargeAmountMap.get(key).setRoleNumRate(roleNumRate+"%");


                dataList.add(rechargeAmountMap.get(key));
            }
        }
        return dataList;
    }

    private String getAmountName(int amount) {
        //   int[] amountValue = new int[]{0, 6000, 9800, 19800, 49800, 99800, 199800, 499800, 999800, 1999800, 2999800};
        String amountName = "";
        switch (amount) {
            case 0:
                amountName = "0~60(大于0，小于等于60)";
                break;
            case 6000:
                amountName = "60~98(大于60，小于等于98)";
                break;
            case 9800:
                amountName = "98~198(大于98，小于等于198)";
                break;
            case 19800:
                amountName = "198~498(大于198，小于等于498)";
                break;
            case 49800:
                amountName = "498~998(大于498，小于等于998)";
                break;
            case 99800:
                amountName = "998~1998(大于998，小于等于1998)";
                break;
            case 199800:
                amountName = "1998~4998(大于1998，小于等于4998)";
                break;
            case 499800:
                amountName = "4998~9998(大于4998，小于等于9998)";
                break;
            case 999800:
                amountName = "9998~19998(大于9998，小于等于19998)";
                break;
            case 1999800:
                amountName = "19998~29998(大于19998，小于等于29998)";
                break;
            case 2999800:
                amountName = ">29998(大于29998)";
                break;
        }
        return amountName;
    }
}
