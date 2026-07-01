/**
 * Auto generated, do not edit it
 *
 * share配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Share_Bean{
    /**
     * ID(type*10000+cond)
     */
    private final int id;
    /**
     * ID(type*10000+cond)
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型
     */
    private final int type;
    /**
     * 类型
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 次数
     */
    private final int count;
    /**
     * 次数
     * @return
     */
    public final int getCount(){
        return count;
    }
    /**
     * 条件
     */
    private final int condition;
    /**
     * 条件
     * @return
     */
    public final int getCondition(){
        return condition;
    }
    /**
     * 描述
     */
    private final String description;
    /**
     * 描述
     * @return
     */
    public final String getDescription(){
        return description;
    }
    /**
     * 奖励物品id_数量(@;@_@)
     */
    private final ReadIntegerArrayEs rewards;
    /**
     * 奖励物品id_数量(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRewards(){
        return rewards;
    }
    /**
     * 分享文本
     */
    private final String text;
    /**
     * 分享文本
     * @return
     */
    public final String getText(){
        return text;
    }
    /**
     * 模型ID
     */
    private final int modelId;
    /**
     * 模型ID
     * @return
     */
    public final int getModelId(){
        return modelId;
    }

    public Cfg_Share_Bean(int id,int type,int count,int condition,String description,String rewardsStr,String text,int modelId){
        this.id = id;
        this.type = type;
        this.count = count;
        this.condition = condition;
        this.description = description;
        this.rewards = new ReadIntegerArrayEs(rewardsStr,"}",",");
        this.text = text;
        this.modelId = modelId;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("count:").append(count).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("description:").append(description).append(";");
        str.append("rewards:").append(rewards).append(";");
        str.append("text:").append(text).append(";");
        str.append("modelId:").append(modelId).append(";");
        return str.toString();
    }
}
