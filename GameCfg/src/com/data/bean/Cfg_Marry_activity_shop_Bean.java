/**
 * Auto generated, do not edit it
 *
 * marry_activity_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_activity_shop_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 商品名字
     */
    private final String name;
    /**
     * 商品名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 原价
货币类型_价格
     */
    private final ReadIntegerArray originalPrice;
    /**
     * 原价
货币类型_价格
     * @return
     */
    public final ReadIntegerArray getOriginalPrice(){
        return originalPrice;
    }
    /**
     * 当前价格
     */
    private final ReadIntegerArray price;
    /**
     * 当前价格
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 个人限购次数
     */
    private final int limitBuy;
    /**
     * 个人限购次数
     * @return
     */
    public final int getLimitBuy(){
        return limitBuy;
    }
    /**
     * 礼包内容
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 礼包内容
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_Marry_activity_shop_Bean(int id,String name,String originalPriceStr,String priceStr,int limitBuy,String rewardStr){
        this.id = id;
        this.name = name;
        this.originalPrice = new ReadIntegerArray(originalPriceStr,",");
        this.price = new ReadIntegerArray(priceStr,",");
        this.limitBuy = limitBuy;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("originalPrice:").append(originalPrice).append(";");
        str.append("price:").append(price).append(";");
        str.append("limitBuy:").append(limitBuy).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
