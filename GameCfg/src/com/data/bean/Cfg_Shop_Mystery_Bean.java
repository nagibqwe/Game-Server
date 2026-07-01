/**
 * Auto generated, do not edit it
 *
 * shop_Mystery配置表
 */
package com.data.bean;

	
public class Cfg_Shop_Mystery_Bean{
    /**
     * 商品ID
     */
    private final int ID;
    /**
     * 商品ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 物品ID
     */
    private final int itemID;
    /**
     * 物品ID
     * @return
     */
    public final int getItemID(){
        return itemID;
    }
    /**
     * 所属档次ID（同ID的为同一档商品库内商品）
     */
    private final int libraryID;
    /**
     * 所属档次ID（同ID的为同一档商品库内商品）
     * @return
     */
    public final int getLibraryID(){
        return libraryID;
    }
    /**
     * 每组商品数量
     */
    private final int itemNum;
    /**
     * 每组商品数量
     * @return
     */
    public final int getItemNum(){
        return itemNum;
    }
    /**
     * 角色职业限制
     */
    private final int occupation;
    /**
     * 角色职业限制
     * @return
     */
    public final int getOccupation(){
        return occupation;
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
     * 购买后是否绑定（0不绑定；1绑定）
     */
    private final int bind;
    /**
     * 购买后是否绑定（0不绑定；1绑定）
     * @return
     */
    public final int getBind(){
        return bind;
    }
    /**
     * 促销标签
     */
    private final int promotion;
    /**
     * 促销标签
     * @return
     */
    public final int getPromotion(){
        return promotion;
    }

    public Cfg_Shop_Mystery_Bean(int ID,int itemID,int libraryID,int itemNum,int occupation,int currencyID,int price,int sort,int bind,int promotion){
        this.ID = ID;
        this.itemID = itemID;
        this.libraryID = libraryID;
        this.itemNum = itemNum;
        this.occupation = occupation;
        this.currencyID = currencyID;
        this.price = price;
        this.sort = sort;
        this.bind = bind;
        this.promotion = promotion;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("itemID:").append(itemID).append(";");
        str.append("libraryID:").append(libraryID).append(";");
        str.append("itemNum:").append(itemNum).append(";");
        str.append("occupation:").append(occupation).append(";");
        str.append("currencyID:").append(currencyID).append(";");
        str.append("price:").append(price).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("bind:").append(bind).append(";");
        str.append("promotion:").append(promotion).append(";");
        return str.toString();
    }
}
