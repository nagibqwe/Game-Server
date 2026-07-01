/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_lottery_object配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_signet_lottery_object_Bean{
    /**
     * 抽奖项id
     */
    private final int id;
    /**
     * 抽奖项id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 抽奖项名称
     */
    private final String name;
    /**
     * 抽奖项名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 宝箱图片资源id
     */
    private final int icon;
    /**
     * 宝箱图片资源id
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 抽奖钥匙id
     */
    private final int consumeItem;
    /**
     * 抽奖钥匙id
     * @return
     */
    public final int getConsumeItem(){
        return consumeItem;
    }
    /**
     * 双倍奖励消耗元宝
     */
    private final ReadIntegerArrayEs consumeMoney;
    /**
     * 双倍奖励消耗元宝
     * @return
     */
    public final ReadIntegerArrayEs getConsumeMoney(){
        return consumeMoney;
    }
    /**
     * 单倍奖励祈灵经验值
     */
    private final int exp;
    /**
     * 单倍奖励祈灵经验值
     * @return
     */
    public final int getExp(){
        return exp;
    }

    public Cfg_SoulArmor_signet_lottery_object_Bean(int id,String name,int icon,int consumeItem,String consumeMoneyStr,int exp){
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.consumeItem = consumeItem;
        this.consumeMoney = new ReadIntegerArrayEs(consumeMoneyStr,"}",",");
        this.exp = exp;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("consumeItem:").append(consumeItem).append(";");
        str.append("consumeMoney:").append(consumeMoney).append(";");
        str.append("exp:").append(exp).append(";");
        return str.toString();
    }
}
