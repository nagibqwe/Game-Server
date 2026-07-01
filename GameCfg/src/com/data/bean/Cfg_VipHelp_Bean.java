/**
 * Auto generated, do not edit it
 *
 * VipHelp配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_VipHelp_Bean{
    /**
     * 帮助ID
     */
    private final int id;
    /**
     * 帮助ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 助力类型
0.投资人
1.申请人
     */
    private final int HelpType;
    /**
     * 助力类型
0.投资人
1.申请人
     * @return
     */
    public final int getHelpType(){
        return HelpType;
    }
    /**
     * 物品奖励
物品id1_最小数量_最大数量_绑定_职业
职业：0玄剑、1天英、2地藏、3罗刹、9通用
绑定：0不绑定、1绑定
     */
    private final ReadIntegerArrayEs HelpReward;
    /**
     * 物品奖励
物品id1_最小数量_最大数量_绑定_职业
职业：0玄剑、1天英、2地藏、3罗刹、9通用
绑定：0不绑定、1绑定
     * @return
     */
    public final ReadIntegerArrayEs getHelpReward(){
        return HelpReward;
    }
    /**
     * 对应天数
     */
    private final int Day;
    /**
     * 对应天数
     * @return
     */
    public final int getDay(){
        return Day;
    }

    public Cfg_VipHelp_Bean(int id,int HelpType,String HelpRewardStr,int Day){
        this.id = id;
        this.HelpType = HelpType;
        this.HelpReward = new ReadIntegerArrayEs(HelpRewardStr,"}",",");
        this.Day = Day;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("HelpType:").append(HelpType).append(";");
        str.append("HelpReward:").append(HelpReward).append(";");
        str.append("Day:").append(Day).append(";");
        return str.toString();
    }
}
