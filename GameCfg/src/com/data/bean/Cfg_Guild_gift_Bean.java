/**
 * Auto generated, do not edit it
 *
 * guild_gift配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_gift_Bean{
    /**
     * 宝箱ID
     */
    private final int ID;
    /**
     * 宝箱ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 名字（策划用）
     */
    private final String name;
    /**
     * 名字（策划用）
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 宝箱类型（0.普通宝箱，1，特殊宝箱）
     */
    private final int type;
    /**
     * 宝箱类型（0.普通宝箱，1，特殊宝箱）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 奖励展示的使用的物品ID
     */
    private final int show_item;
    /**
     * 奖励展示的使用的物品ID
     * @return
     */
    public final int getShow_item(){
        return show_item;
    }
    /**
     * 条件（填写FunctionVariable表配置）
     */
    private final ReadIntegerArray Variable_ID;
    /**
     * 条件（填写FunctionVariable表配置）
     * @return
     */
    public final ReadIntegerArray getVariable_ID(){
        return Variable_ID;
    }
    /**
     * 奖励内容（物品_数量_权重；）
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励内容（物品_数量_权重；）
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 刷新类型（0，每日任务；1，终身任务）
     */
    private final int Refresh_type;
    /**
     * 刷新类型（0，每日任务；1，终身任务）
     * @return
     */
    public final int getRefresh_type(){
        return Refresh_type;
    }
    /**
     * 刷新时间（从凌晨0点开始的分钟数）
     */
    private final int Refresh_times;
    /**
     * 刷新时间（从凌晨0点开始的分钟数）
     * @return
     */
    public final int getRefresh_times(){
        return Refresh_times;
    }
    /**
     * 失效时间（完成条件后X分钟）
     */
    private final int invalid_times;
    /**
     * 失效时间（完成条件后X分钟）
     * @return
     */
    public final int getInvalid_times(){
        return invalid_times;
    }

    public Cfg_Guild_gift_Bean(int ID,String name,int type,int show_item,String Variable_IDStr,String rewardStr,int Refresh_type,int Refresh_times,int invalid_times){
        this.ID = ID;
        this.name = name;
        this.type = type;
        this.show_item = show_item;
        this.Variable_ID = new ReadIntegerArray(Variable_IDStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.Refresh_type = Refresh_type;
        this.Refresh_times = Refresh_times;
        this.invalid_times = invalid_times;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("show_item:").append(show_item).append(";");
        str.append("Variable_ID:").append(Variable_ID).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("Refresh_type:").append(Refresh_type).append(";");
        str.append("Refresh_times:").append(Refresh_times).append(";");
        str.append("invalid_times:").append(invalid_times).append(";");
        return str.toString();
    }
}
