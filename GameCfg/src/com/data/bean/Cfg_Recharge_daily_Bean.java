/**
 * Auto generated, do not edit it
 *
 * recharge_daily配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Recharge_daily_Bean{
    /**
     * ID
     */
    private final int ID;
    /**
     * ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 档位
     */
    private final int position;
    /**
     * 档位
     * @return
     */
    public final int getPosition(){
        return position;
    }
    /**
     * 类型（1，充值；2消费）
     */
    private final int type;
    /**
     * 类型（1，充值；2消费）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 金额
     */
    private final int money;
    /**
     * 金额
     * @return
     */
    public final int getMoney(){
        return money;
    }
    /**
     * 奖励物品(物品ID_数量_绑定)(@;@_@)
     */
    private final ReadIntegerArrayEs award;
    /**
     * 奖励物品(物品ID_数量_绑定)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAward(){
        return award;
    }

    public Cfg_Recharge_daily_Bean(int ID,int position,int type,int money,String awardStr){
        this.ID = ID;
        this.position = position;
        this.type = type;
        this.money = money;
        this.award = new ReadIntegerArrayEs(awardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("position:").append(position).append(";");
        str.append("type:").append(type).append(";");
        str.append("money:").append(money).append(";");
        str.append("award:").append(award).append(";");
        return str.toString();
    }
}
