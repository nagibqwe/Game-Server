/**
 * Auto generated, do not edit it
 *
 * vip配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Vip_Bean{
    /**
     * vip等级
     */
    private final int vipLevel;
    /**
     * vip等级
     * @return
     */
    public final int getVipLevel(){
        return vipLevel;
    }
    /**
     * vip升级经验
     */
    private final int vipLevelUp;
    /**
     * vip升级经验
     * @return
     */
    public final int getVipLevelUp(){
        return vipLevelUp;
    }
    /**
     * 称号奖励对应的item表ID
     */
    private final int titleReward;
    /**
     * 称号奖励对应的item表ID
     * @return
     */
    public final int getTitleReward(){
        return titleReward;
    }
    /**
     * VIP等级礼包
     */
    private final ReadIntegerArrayEs vipReward;
    /**
     * VIP等级礼包
     * @return
     */
    public final ReadIntegerArrayEs getVipReward(){
        return vipReward;
    }
    /**
     * 现价
ItemId_num
     */
    private final ReadIntegerArray vipRewardPriceNow;
    /**
     * 现价
ItemId_num
     * @return
     */
    public final ReadIntegerArray getVipRewardPriceNow(){
        return vipRewardPriceNow;
    }
    /**
     * vip每日礼包
     */
    private final ReadIntegerArrayEs vipRewardPer;
    /**
     * vip每日礼包
     * @return
     */
    public final ReadIntegerArrayEs getVipRewardPer(){
        return vipRewardPer;
    }
    /**
     * VIP特权ID；对应vipPower表的主键
     */
    private final ReadIntegerArray vipPowerId;
    /**
     * VIP特权ID；对应vipPower表的主键
     * @return
     */
    public final ReadIntegerArray getVipPowerId(){
        return vipPowerId;
    }
    /**
     * 特权对应参数（X_Y_Z；X对应VIPPower主键，Y对应服务器用字段，Z客户端对应字段）
     */
    private final ReadIntegerArrayEs vipPowerPra;
    /**
     * 特权对应参数（X_Y_Z；X对应VIPPower主键，Y对应服务器用字段，Z客户端对应字段）
     * @return
     */
    public final ReadIntegerArrayEs getVipPowerPra(){
        return vipPowerPra;
    }

    public Cfg_Vip_Bean(int vipLevel,int vipLevelUp,int titleReward,String vipRewardStr,String vipRewardPriceNowStr,String vipRewardPerStr,String vipPowerIdStr,String vipPowerPraStr){
        this.vipLevel = vipLevel;
        this.vipLevelUp = vipLevelUp;
        this.titleReward = titleReward;
        this.vipReward = new ReadIntegerArrayEs(vipRewardStr,"}",",");
        this.vipRewardPriceNow = new ReadIntegerArray(vipRewardPriceNowStr,",");
        this.vipRewardPer = new ReadIntegerArrayEs(vipRewardPerStr,"}",",");
        this.vipPowerId = new ReadIntegerArray(vipPowerIdStr,",");
        this.vipPowerPra = new ReadIntegerArrayEs(vipPowerPraStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("vipLevel:").append(vipLevel).append(";");
        str.append("vipLevelUp:").append(vipLevelUp).append(";");
        str.append("titleReward:").append(titleReward).append(";");
        str.append("vipReward:").append(vipReward).append(";");
        str.append("vipRewardPriceNow:").append(vipRewardPriceNow).append(";");
        str.append("vipRewardPer:").append(vipRewardPer).append(";");
        str.append("vipPowerId:").append(vipPowerId).append(";");
        str.append("vipPowerPra:").append(vipPowerPra).append(";");
        return str.toString();
    }
}
