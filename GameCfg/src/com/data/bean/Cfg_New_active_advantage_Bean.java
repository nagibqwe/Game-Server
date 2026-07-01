/**
 * Auto generated, do not edit it
 *
 * new_active_advantage配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_New_active_advantage_Bean{
    /**
     * 排序ID（保证不重复即可）
     */
    private final int id;
    /**
     * 排序ID（保证不重复即可）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 描述（策划用，无实际意义）
     */
    private final String designDesc;
    /**
     * 描述（策划用，无实际意义）
     * @return
     */
    public final String getDesignDesc(){
        return designDesc;
    }
    /**
     * 1：新服优势
2：完美情缘
     */
    private final int activeType;
    /**
     * 1：新服优势
2：完美情缘
     * @return
     */
    public final int getActiveType(){
        return activeType;
    }
    /**
     * 类型
1：子任务
2：总任务
总任务是子任务完成数量集合标识
     */
    private final int type;
    /**
     * 类型
1：子任务
2：总任务
总任务是子任务完成数量集合标识
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 对应FunctionVariable表的值
     */
    private final ReadIntegerArray value;
    /**
     * 对应FunctionVariable表的值
     * @return
     */
    public final ReadIntegerArray getValue(){
        return value;
    }
    /**
     * 奖励
ItemID_num_bind_occ
bind:0不绑定1绑定
occ：0男1女9通用
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励
ItemID_num_bind_occ
bind:0不绑定1绑定
occ：0男1女9通用
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_New_active_advantage_Bean(int id,String designDesc,int activeType,int type,String valueStr,String rewardStr){
        this.id = id;
        this.designDesc = designDesc;
        this.activeType = activeType;
        this.type = type;
        this.value = new ReadIntegerArray(valueStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("designDesc:").append(designDesc).append(";");
        str.append("activeType:").append(activeType).append(";");
        str.append("type:").append(type).append(";");
        str.append("value:").append(value).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
