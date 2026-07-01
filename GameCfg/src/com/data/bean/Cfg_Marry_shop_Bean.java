/**
 * Auto generated, do not edit it
 *
 * marry_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Marry_shop_Bean{
    /**
     * 列表ID
     */
    private final int Id;
    /**
     * 列表ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 对应价格
     */
    private final ReadIntegerArray price;
    /**
     * 对应价格
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 单个增加的热度
     */
    private final int hot;
    /**
     * 单个增加的热度
     * @return
     */
    public final int getHot(){
        return hot;
    }
    /**
     * 每场使用上限
     */
    private final int use_max;
    /**
     * 每场使用上限
     * @return
     */
    public final int getUse_max(){
        return use_max;
    }
    /**
     * 获取经验的索引
     */
    private final int exp_index;
    /**
     * 获取经验的索引
     * @return
     */
    public final int getExp_index(){
        return exp_index;
    }
    /**
     * 使用时对应调用的掉落包ID（对应server_drop_item表主键）
     */
    private final int drop_item;
    /**
     * 使用时对应调用的掉落包ID（对应server_drop_item表主键）
     * @return
     */
    public final int getDrop_item(){
        return drop_item;
    }

    public Cfg_Marry_shop_Bean(int Id,String priceStr,int hot,int use_max,int exp_index,int drop_item){
        this.Id = Id;
        this.price = new ReadIntegerArray(priceStr,",");
        this.hot = hot;
        this.use_max = use_max;
        this.exp_index = exp_index;
        this.drop_item = drop_item;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("price:").append(price).append(";");
        str.append("hot:").append(hot).append(";");
        str.append("use_max:").append(use_max).append(";");
        str.append("exp_index:").append(exp_index).append(";");
        str.append("drop_item:").append(drop_item).append(";");
        return str.toString();
    }
}
