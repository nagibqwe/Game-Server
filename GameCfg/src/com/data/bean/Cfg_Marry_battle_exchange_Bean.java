/**
 * Auto generated, do not edit it
 *
 * marry_battle_exchange配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Marry_battle_exchange_Bean{
    /**
     * 排序id
     */
    private final int id;
    /**
     * 排序id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 物品奖励ID
     */
    private final int itemID;
    /**
     * 物品奖励ID
     * @return
     */
    public final int getItemID(){
        return itemID;
    }
    /**
     * 兑换需要的货币ID_数量
     */
    private final ReadIntegerArray pay;
    /**
     * 兑换需要的货币ID_数量
     * @return
     */
    public final ReadIntegerArray getPay(){
        return pay;
    }
    /**
     * 0.不限购；1.周限购；2.终身限购
     */
    private final int limitType;
    /**
     * 0.不限购；1.周限购；2.终身限购
     * @return
     */
    public final int getLimitType(){
        return limitType;
    }
    /**
     * 可购买次数
商品可购买次数:-1为无限
     */
    private final int buyNum;
    /**
     * 可购买次数
商品可购买次数:-1为无限
     * @return
     */
    public final int getBuyNum(){
        return buyNum;
    }

    public Cfg_Marry_battle_exchange_Bean(int id,int itemID,String payStr,int limitType,int buyNum){
        this.id = id;
        this.itemID = itemID;
        this.pay = new ReadIntegerArray(payStr,",");
        this.limitType = limitType;
        this.buyNum = buyNum;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("itemID:").append(itemID).append(";");
        str.append("pay:").append(pay).append(";");
        str.append("limitType:").append(limitType).append(";");
        str.append("buyNum:").append(buyNum).append(";");
        return str.toString();
    }
}
