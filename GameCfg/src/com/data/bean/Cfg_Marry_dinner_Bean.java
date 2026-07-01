/**
 * Auto generated, do not edit it
 *
 * marry_dinner配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_dinner_Bean{
    /**
     * 宴席等级
     */
    private final int level;
    /**
     * 宴席等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 名称
     */
    private final String name;
    /**
     * 名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 消耗的货币(@_@)
     */
    private final ReadIntegerArray need_type;
    /**
     * 消耗的货币(@_@)
     * @return
     */
    public final ReadIntegerArray getNeed_type(){
        return need_type;
    }
    /**
     * 给的物品列表(@;@_@)
     */
    private final ReadIntegerArrayEs rewardItemList;
    /**
     * 给的物品列表(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRewardItemList(){
        return rewardItemList;
    }
    /**
     * 赠送婚宴ID
     */
    private final int dinner;
    /**
     * 赠送婚宴ID
     * @return
     */
    public final int getDinner(){
        return dinner;
    }
    /**
     * 结婚所需所需好友度
     */
    private final int Friends;
    /**
     * 结婚所需所需好友度
     * @return
     */
    public final int getFriends(){
        return Friends;
    }
    /**
     * 可获得的婚宴次数
     */
    private final int orderTime;
    /**
     * 可获得的婚宴次数
     * @return
     */
    public final int getOrderTime(){
        return orderTime;
    }

    public Cfg_Marry_dinner_Bean(int level,String name,String need_typeStr,String rewardItemListStr,int dinner,int Friends,int orderTime){
        this.level = level;
        this.name = name;
        this.need_type = new ReadIntegerArray(need_typeStr,",");
        this.rewardItemList = new ReadIntegerArrayEs(rewardItemListStr,"}",",");
        this.dinner = dinner;
        this.Friends = Friends;
        this.orderTime = orderTime;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("name:").append(name).append(";");
        str.append("need_type:").append(need_type).append(";");
        str.append("rewardItemList:").append(rewardItemList).append(";");
        str.append("dinner:").append(dinner).append(";");
        str.append("Friends:").append(Friends).append(";");
        str.append("orderTime:").append(orderTime).append(";");
        return str.toString();
    }
}
