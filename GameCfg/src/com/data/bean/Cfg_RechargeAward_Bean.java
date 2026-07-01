/**
 * Auto generated, do not edit it
 *
 * RechargeAward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_RechargeAward_Bean{
    /**
     * 首充奖励表
     */
    private final int id;
    /**
     * 首充奖励表
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 奖励类型
(0首充奖励；
1续充奖励;
2高档位首充)
     */
    private final int awardType;
    /**
     * 奖励类型
(0首充奖励；
1续充奖励;
2高档位首充)
     * @return
     */
    public final int getAwardType(){
        return awardType;
    }
    /**
     * 需要充值数量（单位，灵玉）
     */
    private final int needRecharge;
    /**
     * 需要充值数量（单位，灵玉）
     * @return
     */
    public final int getNeedRecharge(){
        return needRecharge;
    }
    /**
     * 
玄剑职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     */
    private final ReadIntegerArrayEs occ_0;
    /**
     * 
玄剑职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     * @return
     */
    public final ReadIntegerArrayEs getOcc_0(){
        return occ_0;
    }
    /**
     * 
天英职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     */
    private final ReadIntegerArrayEs occ_1;
    /**
     * 
天英职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     * @return
     */
    public final ReadIntegerArrayEs getOcc_1(){
        return occ_1;
    }
    /**
     * 
地藏职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     */
    private final ReadIntegerArrayEs occ_2;
    /**
     * 
地藏职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     * @return
     */
    public final ReadIntegerArrayEs getOcc_2(){
        return occ_2;
    }
    /**
     * 
罗刹职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     */
    private final ReadIntegerArrayEs occ_3;
    /**
     * 
罗刹职业首充Tips武器配置
类型_modelcfgId
类型详情见批注
     * @return
     */
    public final ReadIntegerArrayEs getOcc_3(){
        return occ_3;
    }
    /**
     * 奖励物品名称_描述
     */
    private final String rewardDes;
    /**
     * 奖励物品名称_描述
     * @return
     */
    public final String getRewardDes(){
        return rewardDes;
    }
    /**
     * 奖励区分职业，图标也是显示出来（ID_数量_是否绑定（0否1是）_职业），没有不填(@;@_@)
     */
    private final ReadIntegerArrayEs equipAward;
    /**
     * 奖励区分职业，图标也是显示出来（ID_数量_是否绑定（0否1是）_职业），没有不填(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getEquipAward(){
        return equipAward;
    }
    /**
     * 物品奖励ID_数量_是否绑定（0否1是）(@;@_@)
     */
    private final ReadIntegerArrayEs itemAward;
    /**
     * 物品奖励ID_数量_是否绑定（0否1是）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getItemAward(){
        return itemAward;
    }
    /**
     * 公告类型（10跑马灯）
     */
    private final int radio;
    /**
     * 公告类型（10跑马灯）
     * @return
     */
    public final int getRadio(){
        return radio;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_RechargeAward_Bean(int id,int awardType,int needRecharge,String occ_0Str,String occ_1Str,String occ_2Str,String occ_3Str,String rewardDes,String equipAwardStr,String itemAwardStr,int radio,String chatchannelStr){
        this.id = id;
        this.awardType = awardType;
        this.needRecharge = needRecharge;
        this.occ_0 = new ReadIntegerArrayEs(occ_0Str,"}",",");
        this.occ_1 = new ReadIntegerArrayEs(occ_1Str,"}",",");
        this.occ_2 = new ReadIntegerArrayEs(occ_2Str,"}",",");
        this.occ_3 = new ReadIntegerArrayEs(occ_3Str,"}",",");
        this.rewardDes = rewardDes;
        this.equipAward = new ReadIntegerArrayEs(equipAwardStr,"}",",");
        this.itemAward = new ReadIntegerArrayEs(itemAwardStr,"}",",");
        this.radio = radio;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("awardType:").append(awardType).append(";");
        str.append("needRecharge:").append(needRecharge).append(";");
        str.append("occ_0:").append(occ_0).append(";");
        str.append("occ_1:").append(occ_1).append(";");
        str.append("occ_2:").append(occ_2).append(";");
        str.append("occ_3:").append(occ_3).append(";");
        str.append("rewardDes:").append(rewardDes).append(";");
        str.append("equipAward:").append(equipAward).append(";");
        str.append("itemAward:").append(itemAward).append(";");
        str.append("radio:").append(radio).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
