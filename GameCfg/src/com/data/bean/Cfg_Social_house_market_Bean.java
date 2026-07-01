/**
 * Auto generated, do not edit it
 *
 * social_house_market配置表
 */
package com.data.bean;

	
public class Cfg_Social_house_market_Bean{
    /**
     * 商品ID
唯一ID
     */
    private final int ID;
    /**
     * 商品ID
唯一ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 家具ID
     */
    private final int furnitureID;
    /**
     * 家具ID
     * @return
     */
    public final int getFurnitureID(){
        return furnitureID;
    }
    /**
     * 所属商城ID
1.房屋商城
2.人气商城
3.珍宝商城
     */
    private final int shopID;
    /**
     * 所属商城ID
1.房屋商城
2.人气商城
3.珍宝商城
     * @return
     */
    public final int getShopID(){
        return shopID;
    }
    /**
     * 商城标签
1_2_3_4_5

对应ShopMenu里面的主键

     */
    private final int shopType;
    /**
     * 商城标签
1_2_3_4_5

对应ShopMenu里面的主键

     * @return
     */
    public final int getShopType(){
        return shopType;
    }
    /**
     * 购买需求聚灵盆等级
     */
    private final int level;
    /**
     * 购买需求聚灵盆等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 0.不限购；1.日限够；2.周限购；3.月限购；4.年限购；5.终身限购
     */
    private final int limitType;
    /**
     * 0.不限购；1.日限够；2.周限购；3.月限购；4.年限购；5.终身限购
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
    /**
     * 货币ID
     */
    private final int currencyID;
    /**
     * 货币ID
     * @return
     */
    public final int getCurrencyID(){
        return currencyID;
    }
    /**
     * 价格
     */
    private final int price;
    /**
     * 价格
     * @return
     */
    public final int getPrice(){
        return price;
    }
    /**
     * 促销标签
0.无
1.打折
2.推荐
3.新品
4.热卖
打折商品这个字段不能为0
     */
    private final int promotion;
    /**
     * 促销标签
0.无
1.打折
2.推荐
3.新品
4.热卖
打折商品这个字段不能为0
     * @return
     */
    public final int getPromotion(){
        return promotion;
    }
    /**
     * 排列优先级
     */
    private final int sort;
    /**
     * 排列优先级
     * @return
     */
    public final int getSort(){
        return sort;
    }

    public Cfg_Social_house_market_Bean(int ID,int furnitureID,int shopID,int shopType,int level,int limitType,int buyNum,int currencyID,int price,int promotion,int sort){
        this.ID = ID;
        this.furnitureID = furnitureID;
        this.shopID = shopID;
        this.shopType = shopType;
        this.level = level;
        this.limitType = limitType;
        this.buyNum = buyNum;
        this.currencyID = currencyID;
        this.price = price;
        this.promotion = promotion;
        this.sort = sort;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("furnitureID:").append(furnitureID).append(";");
        str.append("shopID:").append(shopID).append(";");
        str.append("shopType:").append(shopType).append(";");
        str.append("level:").append(level).append(";");
        str.append("limitType:").append(limitType).append(";");
        str.append("buyNum:").append(buyNum).append(";");
        str.append("currencyID:").append(currencyID).append(";");
        str.append("price:").append(price).append(";");
        str.append("promotion:").append(promotion).append(";");
        str.append("sort:").append(sort).append(";");
        return str.toString();
    }
}
