/**
 * Auto generated, do not edit it
 *
 * xianmengzhengba配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Xianmengzhengba_Bean{
    /**
     * 排序id
     */
    private final int id;
    /**
     * 排序id
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
     * 1.仙盟发展2.仙盟征战3.仙盟争夺
     */
    private final int activeType;
    /**
     * 1.仙盟发展2.仙盟征战3.仙盟争夺
     * @return
     */
    public final int getActiveType(){
        return activeType;
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
    /**
     * 客户端用任务列表排序
     */
    private final int sort;
    /**
     * 客户端用任务列表排序
     * @return
     */
    public final int getSort(){
        return sort;
    }
    /**
     * 客户端显示用
     */
    private final String desc;
    /**
     * 客户端显示用
     * @return
     */
    public final String getDesc(){
        return desc;
    }
    /**
     * 对应跳转的功能ID
对应FunctionStart表主键
     */
    private final int functionId;
    /**
     * 对应跳转的功能ID
对应FunctionStart表主键
     * @return
     */
    public final int getFunctionId(){
        return functionId;
    }

    public Cfg_Xianmengzhengba_Bean(int id,String designDesc,int activeType,String valueStr,String rewardStr,int sort,String desc,int functionId){
        this.id = id;
        this.designDesc = designDesc;
        this.activeType = activeType;
        this.value = new ReadIntegerArray(valueStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.sort = sort;
        this.desc = desc;
        this.functionId = functionId;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("designDesc:").append(designDesc).append(";");
        str.append("activeType:").append(activeType).append(";");
        str.append("value:").append(value).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("desc:").append(desc).append(";");
        str.append("functionId:").append(functionId).append(";");
        return str.toString();
    }
}
