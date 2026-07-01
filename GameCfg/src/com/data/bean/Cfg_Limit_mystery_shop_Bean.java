/**
 * Auto generated, do not edit it
 *
 * limit_mystery_shop配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Limit_mystery_shop_Bean{
    /**
     * 商城编号

     */
    private final int id;
    /**
     * 商城编号

     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
同一组奖励必须配置相同
     */
    private final ReadIntegerArrayEs reward;
    /**
     * ItemId_num_bind_occ
bind：0未绑定，1绑定
occ：0男1女9通用
同一组奖励必须配置相同
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 关联FunctionVariable

     */
    private final ReadIntegerArray condition;
    /**
     * 关联FunctionVariable

     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 价格
type_value
type:-1=内购，type=其他值的时候对应item表的货币ID
     */
    private final ReadIntegerArray price;
    /**
     * 价格
type_value
type:-1=内购，type=其他值的时候对应item表的货币ID
     * @return
     */
    public final ReadIntegerArray getPrice(){
        return price;
    }
    /**
     * 有效购买时间（秒）
-1代表无限制时间
     */
    private final int time;
    /**
     * 有效购买时间（秒）
-1代表无限制时间
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 组ID，代表哪些商品为同一组（rechargeItem的ID保持一致）
     */
    private final int group;
    /**
     * 组ID，代表哪些商品为同一组（rechargeItem的ID保持一致）
     * @return
     */
    public final int getGroup(){
        return group;
    }

    public Cfg_Limit_mystery_shop_Bean(int id,String rewardStr,String conditionStr,String priceStr,int time,int group){
        this.id = id;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.price = new ReadIntegerArray(priceStr,",");
        this.time = time;
        this.group = group;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("price:").append(price).append(";");
        str.append("time:").append(time).append(";");
        str.append("group:").append(group).append(";");
        return str.toString();
    }
}
