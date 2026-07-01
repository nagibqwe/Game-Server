/**
 * Auto generated, do not edit it
 *
 * shop_Maket配置表
 */
package com.data.bean;

	
public class Cfg_Shop_Maket_Bean{
    /**
     * 商品ID
唯一ID，不可重复.不可删除更改,只能添加
101开头表示游戏每日特惠商品
102开头表示游戏常用道具商品
20001开头表示游戏积分兑换商品
20002开头表示游戏寻宝积分商品
20003开头表示游戏阵道兑换商品
20004开头表示游戏帮贡兑换商品
20005开头表示游戏绑元商城商品
20006开头表示巅峰晋级荣誉币商品
30001开头表示公会福地兑换商品
40001开头表示公会商店兑换商品
50001开头表示混沌之境商店
     */
    private final int ID;
    /**
     * 商品ID
唯一ID，不可重复.不可删除更改,只能添加
101开头表示游戏每日特惠商品
102开头表示游戏常用道具商品
20001开头表示游戏积分兑换商品
20002开头表示游戏寻宝积分商品
20003开头表示游戏阵道兑换商品
20004开头表示游戏帮贡兑换商品
20005开头表示游戏绑元商城商品
20006开头表示巅峰晋级荣誉币商品
30001开头表示公会福地兑换商品
40001开头表示公会商店兑换商品
50001开头表示混沌之境商店
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 1：商城（GM后台配置，运营控制）
0：商店（配置表控制）
     */
    private final int type;
    /**
     * 1：商城（GM后台配置，运营控制）
0：商店（配置表控制）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 道具ID
     */
    private final int itemID;
    /**
     * 道具ID
     * @return
     */
    public final int getItemID(){
        return itemID;
    }
    /**
     * 所属商城ID
1.元宝商城
2.兑换商城
3.福地积分商店
4.仙盟贡献商店
5.混沌之境商店
9:婚姻商城（只用于埋点，不可增加配置）

     */
    private final int shopID;
    /**
     * 所属商城ID
1.元宝商城
2.兑换商城
3.福地积分商店
4.仙盟贡献商店
5.混沌之境商店
9:婚姻商城（只用于埋点，不可增加配置）

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
    private final String shopType;
    /**
     * 商城标签
1_2_3_4_5

对应ShopMenu里面的主键
     * @return
     */
    public final String getShopType(){
        return shopType;
    }
    /**
     * 商品标签页ID
区别不同的商城标签页
对应functionstart中ID
     */
    private final int labelID;
    /**
     * 商品标签页ID
区别不同的商城标签页
对应functionstart中ID
     * @return
     */
    public final int getLabelID(){
        return labelID;
    }
    /**
     * 购买需求等级
     */
    private final int level;
    /**
     * 购买需求等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 购买需求军衔
     */
    private final int militaryLevel;
    /**
     * 购买需求军衔
     * @return
     */
    public final int getMilitaryLevel(){
        return militaryLevel;
    }
    /**
     * 购买需求帮会等级
     */
    private final int guildLevel;
    /**
     * 购买需求帮会等级
     * @return
     */
    public final int getGuildLevel(){
        return guildLevel;
    }
    /**
     * 购买需求仙盟商店起始等级
     */
    private final int guildShopLvlStart;
    /**
     * 购买需求仙盟商店起始等级
     * @return
     */
    public final int getGuildShopLvlStart(){
        return guildShopLvlStart;
    }
    /**
     * 购买需求仙盟商店结束等级
     */
    private final int guildShopLvlEND;
    /**
     * 购买需求仙盟商店结束等级
     * @return
     */
    public final int getGuildShopLvlEND(){
        return guildShopLvlEND;
    }
    /**
     * 购买需求境界等级
     */
    private final int vipLevel;
    /**
     * 购买需求境界等级
     * @return
     */
    public final int getVipLevel(){
        return vipLevel;
    }
    /**
     * 角色职业限制
-1.通用无限制
0.玄剑
1.天英
     */
    private final int occupation;
    /**
     * 角色职业限制
-1.通用无限制
0.玄剑
1.天英
     * @return
     */
    public final int getOccupation(){
        return occupation;
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
1、元宝
2、绑元
3、金币
10、成就点
11、帮贡
29、巅峰竞技荣誉币
     */
    private final int currencyID;
    /**
     * 货币ID
1、元宝
2、绑元
3、金币
10、成就点
11、帮贡
29、巅峰竞技荣誉币
     * @return
     */
    public final int getCurrencyID(){
        return currencyID;
    }
    /**
     * 打折前价格
     */
    private final int price;
    /**
     * 打折前价格
     * @return
     */
    public final int getPrice(){
        return price;
    }
    /**
     * 打折后价格
     */
    private final int discountPrice;
    /**
     * 打折后价格
     * @return
     */
    public final int getDiscountPrice(){
        return discountPrice;
    }
    /**
     * 打折数
0为不打折
>0为具体打折数
     */
    private final int discount;
    /**
     * 打折数
0为不打折
>0为具体打折数
     * @return
     */
    public final int getDiscount(){
        return discount;
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
    /**
     * 上架时间
物品上架的时间：年_月_日_时_分_秒，0则为即刻上架
     */
    private final String upTime;
    /**
     * 上架时间
物品上架的时间：年_月_日_时_分_秒，0则为即刻上架
     * @return
     */
    public final String getUpTime(){
        return upTime;
    }
    /**
     * 下架时间
物品下架的时间：年_月_日_时_分_秒，0为永不下架
     */
    private final String downTime;
    /**
     * 下架时间
物品下架的时间：年_月_日_时_分_秒，0为永不下架
     * @return
     */
    public final String getDownTime(){
        return downTime;
    }
    /**
     * 过期日期
物品过期时刻：年_月_日_时_分_秒，0则为即刻上架
     */
    private final String overdue;
    /**
     * 过期日期
物品过期时刻：年_月_日_时_分_秒，0则为即刻上架
     * @return
     */
    public final String getOverdue(){
        return overdue;
    }
    /**
     * 持续时间
购买后的使用倒计时
     */
    private final int duration;
    /**
     * 持续时间
购买后的使用倒计时
     * @return
     */
    public final int getDuration(){
        return duration;
    }
    /**
     * 是否绑定
（0否 1是）
     */
    private final int bind;
    /**
     * 是否绑定
（0否 1是）
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 刷新使用货币类型，-1为不能刷新
     */
    private final int refreshCurrency;
    /**
     * 刷新使用货币类型，-1为不能刷新
     * @return
     */
    public final int getRefreshCurrency(){
        return refreshCurrency;
    }
    /**
     * 刷新货币消耗数量
     */
    private final int refreshNum;
    /**
     * 刷新货币消耗数量
     * @return
     */
    public final int getRefreshNum(){
        return refreshNum;
    }
    /**
     * 购买需求最低世界等级
     */
    private final int worldLvlStart;
    /**
     * 购买需求最低世界等级
     * @return
     */
    public final int getWorldLvlStart(){
        return worldLvlStart;
    }
    /**
     * 购买需求结束世界等级
     */
    private final int worldLvlEND;
    /**
     * 购买需求结束世界等级
     * @return
     */
    public final int getWorldLvlEND(){
        return worldLvlEND;
    }
    /**
     * 上架时间
根据开服天数
以当天0点为准
     */
    private final int openDay;
    /**
     * 上架时间
根据开服天数
以当天0点为准
     * @return
     */
    public final int getOpenDay(){
        return openDay;
    }
    /**
     * 下架时间
根据开服天数
以当天24点为准
     */
    private final int closeDay;
    /**
     * 下架时间
根据开服天数
以当天24点为准
     * @return
     */
    public final int getCloseDay(){
        return closeDay;
    }
    /**
     * 开通修神锻体后是否打折，具体折扣读取修神锻体特权表
0代表不打折
1代表打折
     */
    private final int isDiscount;
    /**
     * 开通修神锻体后是否打折，具体折扣读取修神锻体特权表
0代表不打折
1代表打折
     * @return
     */
    public final int getIsDiscount(){
        return isDiscount;
    }
    /**
     * 商品价格（价格约卖越高），不配置则代表不会随着购买次数增加价格
例：1_50_5;51_100_5(第一次购买到第五十次购买都是在原有价格上增加5，51次到100次就是增加10），如果可购买数量超过了配置数量，则读取最后一个值
     */
    private final String countDiscount;
    /**
     * 商品价格（价格约卖越高），不配置则代表不会随着购买次数增加价格
例：1_50_5;51_100_5(第一次购买到第五十次购买都是在原有价格上增加5，51次到100次就是增加10），如果可购买数量超过了配置数量，则读取最后一个值
     * @return
     */
    public final String getCountDiscount(){
        return countDiscount;
    }

    public Cfg_Shop_Maket_Bean(int ID,int type,int itemID,int shopID,String shopType,int labelID,int level,int militaryLevel,int guildLevel,int guildShopLvlStart,int guildShopLvlEND,int vipLevel,int occupation,int limitType,int buyNum,int currencyID,int price,int discountPrice,int discount,int promotion,int sort,String upTime,String downTime,String overdue,int duration,int bind,int refreshCurrency,int refreshNum,int worldLvlStart,int worldLvlEND,int openDay,int closeDay,int isDiscount,String countDiscount){
        this.ID = ID;
        this.type = type;
        this.itemID = itemID;
        this.shopID = shopID;
        this.shopType = shopType;
        this.labelID = labelID;
        this.level = level;
        this.militaryLevel = militaryLevel;
        this.guildLevel = guildLevel;
        this.guildShopLvlStart = guildShopLvlStart;
        this.guildShopLvlEND = guildShopLvlEND;
        this.vipLevel = vipLevel;
        this.occupation = occupation;
        this.limitType = limitType;
        this.buyNum = buyNum;
        this.currencyID = currencyID;
        this.price = price;
        this.discountPrice = discountPrice;
        this.discount = discount;
        this.promotion = promotion;
        this.sort = sort;
        this.upTime = upTime;
        this.downTime = downTime;
        this.overdue = overdue;
        this.duration = duration;
        this.bind = bind;
        this.refreshCurrency = refreshCurrency;
        this.refreshNum = refreshNum;
        this.worldLvlStart = worldLvlStart;
        this.worldLvlEND = worldLvlEND;
        this.openDay = openDay;
        this.closeDay = closeDay;
        this.isDiscount = isDiscount;
        this.countDiscount = countDiscount;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("type:").append(type).append(";");
        str.append("itemID:").append(itemID).append(";");
        str.append("shopID:").append(shopID).append(";");
        str.append("shopType:").append(shopType).append(";");
        str.append("labelID:").append(labelID).append(";");
        str.append("level:").append(level).append(";");
        str.append("militaryLevel:").append(militaryLevel).append(";");
        str.append("guildLevel:").append(guildLevel).append(";");
        str.append("guildShopLvlStart:").append(guildShopLvlStart).append(";");
        str.append("guildShopLvlEND:").append(guildShopLvlEND).append(";");
        str.append("vipLevel:").append(vipLevel).append(";");
        str.append("occupation:").append(occupation).append(";");
        str.append("limitType:").append(limitType).append(";");
        str.append("buyNum:").append(buyNum).append(";");
        str.append("currencyID:").append(currencyID).append(";");
        str.append("price:").append(price).append(";");
        str.append("discountPrice:").append(discountPrice).append(";");
        str.append("discount:").append(discount).append(";");
        str.append("promotion:").append(promotion).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("upTime:").append(upTime).append(";");
        str.append("downTime:").append(downTime).append(";");
        str.append("overdue:").append(overdue).append(";");
        str.append("duration:").append(duration).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("refreshCurrency:").append(refreshCurrency).append(";");
        str.append("refreshNum:").append(refreshNum).append(";");
        str.append("worldLvlStart:").append(worldLvlStart).append(";");
        str.append("worldLvlEND:").append(worldLvlEND).append(";");
        str.append("openDay:").append(openDay).append(";");
        str.append("closeDay:").append(closeDay).append(";");
        str.append("isDiscount:").append(isDiscount).append(";");
        str.append("countDiscount:").append(countDiscount).append(";");
        return str.toString();
    }
}
