/**
 * Auto generated, do not edit it
 *
 * VIPWeekReward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_VIPWeekReward_Bean{
    /**
     * 奖励编号
     */
    private final int id;
    /**
     * 奖励编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 组标识
     */
    private final int type;
    /**
     * 组标识
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 需要达到的领取条件(领取不扣除）
     */
    private final int condition;
    /**
     * 需要达到的领取条件(领取不扣除）
     * @return
     */
    public final int getCondition(){
        return condition;
    }
    /**
     * ID_num_bind_occ(物品ID_数量_绑定_职业）
bind  0不绑定  1绑定
occ   0男剑  1女枪  9通用
     */
    private final ReadIntegerArrayEs item;
    /**
     * ID_num_bind_occ(物品ID_数量_绑定_职业）
bind  0不绑定  1绑定
occ   0男剑  1女枪  9通用
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }
    /**
     * 可领取的VIP点（用于增加VIP经验）
     */
    private final int VIPMoney;
    /**
     * 可领取的VIP点（用于增加VIP经验）
     * @return
     */
    public final int getVIPMoney(){
        return VIPMoney;
    }
    /**
     * 激活周卡额外领取的奖励
ID_num_bind_occ(物品ID_数量_绑定_职业）
bind  0不绑定  1绑定
occ   0男剑  1女枪  9通用
     */
    private final ReadIntegerArrayEs item_weekCard;
    /**
     * 激活周卡额外领取的奖励
ID_num_bind_occ(物品ID_数量_绑定_职业）
bind  0不绑定  1绑定
occ   0男剑  1女枪  9通用
     * @return
     */
    public final ReadIntegerArrayEs getItem_weekCard(){
        return item_weekCard;
    }
    /**
     * 激活周卡额外领取的奖励
可领取的VIP点（用于增加VIP经验）
     */
    private final int VIPMoney_weekCard;
    /**
     * 激活周卡额外领取的奖励
可领取的VIP点（用于增加VIP经验）
     * @return
     */
    public final int getVIPMoney_weekCard(){
        return VIPMoney_weekCard;
    }

    public Cfg_VIPWeekReward_Bean(int id,int type,int condition,String itemStr,int VIPMoney,String item_weekCardStr,int VIPMoney_weekCard){
        this.id = id;
        this.type = type;
        this.condition = condition;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.VIPMoney = VIPMoney;
        this.item_weekCard = new ReadIntegerArrayEs(item_weekCardStr,"}",",");
        this.VIPMoney_weekCard = VIPMoney_weekCard;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("item:").append(item).append(";");
        str.append("VIPMoney:").append(VIPMoney).append(";");
        str.append("item_weekCard:").append(item_weekCard).append(";");
        str.append("VIPMoney_weekCard:").append(VIPMoney_weekCard).append(";");
        return str.toString();
    }
}
