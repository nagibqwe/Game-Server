/**
 * Auto generated, do not edit it
 *
 * VIPTrueRecharge配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_VIPTrueRecharge_Bean{
    /**
     * 充值档位编号
     */
    private final int id;
    /**
     * 充值档位编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 充值类型
1低级充值
2高级充值
     */
    private final int type;
    /**
     * 充值类型
1低级充值
2高级充值
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 充值条件（单位：灵玉）
     */
    private final int rechargeLimit;
    /**
     * 充值条件（单位：灵玉）
     * @return
     */
    public final int getRechargeLimit(){
        return rechargeLimit;
    }
    /**
     * 物品奖励，
ItemId_num_bind_occ
物品ID_数量_绑定状态_职业
绑定状态：0未绑定1绑定
职业：0男1女9通用
     */
    private final ReadIntegerArrayEs rechargeReward;
    /**
     * 物品奖励，
ItemId_num_bind_occ
物品ID_数量_绑定状态_职业
绑定状态：0未绑定1绑定
职业：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getRechargeReward(){
        return rechargeReward;
    }
    /**
     * 累计充值特权ID；对应vipPower表的主键
     */
    private final ReadIntegerArray trueRewardPowerId;
    /**
     * 累计充值特权ID；对应vipPower表的主键
     * @return
     */
    public final ReadIntegerArray getTrueRewardPowerId(){
        return trueRewardPowerId;
    }
    /**
     * 特权对应参数
     */
    private final ReadIntegerArrayEs trueRewardPowerPra;
    /**
     * 特权对应参数
     * @return
     */
    public final ReadIntegerArrayEs getTrueRewardPowerPra(){
        return trueRewardPowerPra;
    }

    public Cfg_VIPTrueRecharge_Bean(int id,int type,int rechargeLimit,String rechargeRewardStr,String trueRewardPowerIdStr,String trueRewardPowerPraStr){
        this.id = id;
        this.type = type;
        this.rechargeLimit = rechargeLimit;
        this.rechargeReward = new ReadIntegerArrayEs(rechargeRewardStr,"}",",");
        this.trueRewardPowerId = new ReadIntegerArray(trueRewardPowerIdStr,",");
        this.trueRewardPowerPra = new ReadIntegerArrayEs(trueRewardPowerPraStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("rechargeLimit:").append(rechargeLimit).append(";");
        str.append("rechargeReward:").append(rechargeReward).append(";");
        str.append("trueRewardPowerId:").append(trueRewardPowerId).append(";");
        str.append("trueRewardPowerPra:").append(trueRewardPowerPra).append(";");
        return str.toString();
    }
}
