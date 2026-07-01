/**
 * Auto generated, do not edit it
 *
 * Treasure_Recovery配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Treasure_Recovery_Bean{
    /**
     * 编号，主键
     */
    private final int id;
    /**
     * 编号，主键
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 物品id_职业
     */
    private final ReadIntegerArrayEs item;
    /**
     * 物品id_职业
     * @return
     */
    public final ReadIntegerArrayEs getItem(){
        return item;
    }
    /**
     * 对应FunctionStart表，功能开放则显示出来
空代表默认开放
     */
    private final int condition;
    /**
     * 对应FunctionStart表，功能开放则显示出来
空代表默认开放
     * @return
     */
    public final int getCondition(){
        return condition;
    }
    /**
     * 回收单个物品的价格
     */
    private final ReadIntegerArray reward;
    /**
     * 回收单个物品的价格
     * @return
     */
    public final ReadIntegerArray getReward(){
        return reward;
    }

    public Cfg_Treasure_Recovery_Bean(int id,String itemStr,int condition,String rewardStr){
        this.id = id;
        this.item = new ReadIntegerArrayEs(itemStr,"}",",");
        this.condition = condition;
        this.reward = new ReadIntegerArray(rewardStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("item:").append(item).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
