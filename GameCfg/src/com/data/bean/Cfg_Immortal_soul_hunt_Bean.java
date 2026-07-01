/**
 * Auto generated, do not edit it
 *
 * immortal_soul_hunt配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Immortal_soul_hunt_Bean{
    /**
     * 灵魂抽取id
     */
    private final int id;
    /**
     * 灵魂抽取id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 抽奖的次数（区分是单抽还是10连抽）
     */
    private final int times;
    /**
     * 抽奖的次数（区分是单抽还是10连抽）
     * @return
     */
    public final int getTimes(){
        return times;
    }
    /**
     * 特殊次数的特殊奖池（填写对应的次数，如果填-1，则为一般奖池）
     */
    private final int special_times;
    /**
     * 特殊次数的特殊奖池（填写对应的次数，如果填-1，则为一般奖池）
     * @return
     */
    public final int getSpecial_times(){
        return special_times;
    }
    /**
     * 抽取类型判断（immortal_soul_attribute表的type_万分比概率）client igrone
     */
    private final ReadIntegerArrayEs type_probability;
    /**
     * 抽取类型判断（immortal_soul_attribute表的type_万分比概率）client igrone
     * @return
     */
    public final ReadIntegerArrayEs getType_probability(){
        return type_probability;
    }
    /**
     * 抽取品质判断（immortal_soul_attribute表的quality_权重）client igrone
     */
    private final ReadIntegerArrayEs quality_probability;
    /**
     * 抽取品质判断（immortal_soul_attribute表的quality_权重）client igrone
     * @return
     */
    public final ReadIntegerArrayEs getQuality_probability(){
        return quality_probability;
    }
    /**
     * 第X次走特殊掉落组client igrone
     */
    private final int special_times1;
    /**
     * 第X次走特殊掉落组client igrone
     * @return
     */
    public final int getSpecial_times1(){
        return special_times1;
    }
    /**
     * 抽取类型判断（immortal_soul_attribute表的type_万分比概率）client igrone
     */
    private final ReadIntegerArrayEs special_type_probability;
    /**
     * 抽取类型判断（immortal_soul_attribute表的type_万分比概率）client igrone
     * @return
     */
    public final ReadIntegerArrayEs getSpecial_type_probability(){
        return special_type_probability;
    }
    /**
     * 抽取品质判断（immortal_soul_attribute表的quality_权重）client igrone
     */
    private final ReadIntegerArrayEs special_quality_probability;
    /**
     * 抽取品质判断（immortal_soul_attribute表的quality_权重）client igrone
     * @return
     */
    public final ReadIntegerArrayEs getSpecial_quality_probability(){
        return special_quality_probability;
    }
    /**
     * 属性灵魄抽取属性类型判断（1，只能出单属性最大类型1-21；2，能够出双属性，最大类型1-28；3，能够出三属性，最大类型1-35）client igrone
     */
    private final ReadIntegerArray add_type_probability;
    /**
     * 属性灵魄抽取属性类型判断（1，只能出单属性最大类型1-21；2，能够出双属性，最大类型1-28；3，能够出三属性，最大类型1-35）client igrone
     * @return
     */
    public final ReadIntegerArray getAdd_type_probability(){
        return add_type_probability;
    }
    /**
     * 每次抽奖固定获得的物品client igrone
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 每次抽奖固定获得的物品client igrone
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 抽奖消耗的货币_数量
     */
    private final ReadIntegerArray basic_attributes;
    /**
     * 抽奖消耗的货币_数量
     * @return
     */
    public final ReadIntegerArray getBasic_attributes(){
        return basic_attributes;
    }
    /**
     * 货币不足时的单位购买花费元宝数量
     */
    private final int exchange_ranking;
    /**
     * 货币不足时的单位购买花费元宝数量
     * @return
     */
    public final int getExchange_ranking(){
        return exchange_ranking;
    }
    /**
     * 货币不足时的单位购买的货币
     */
    private final ReadIntegerArray exp;
    /**
     * 货币不足时的单位购买的货币
     * @return
     */
    public final ReadIntegerArray getExp(){
        return exp;
    }
    /**
     * 货币不足时的单位赠送的货币
     */
    private final ReadIntegerArray type;
    /**
     * 货币不足时的单位赠送的货币
     * @return
     */
    public final ReadIntegerArray getType(){
        return type;
    }

    public Cfg_Immortal_soul_hunt_Bean(int id,int times,int special_times,String type_probabilityStr,String quality_probabilityStr,int special_times1,String special_type_probabilityStr,String special_quality_probabilityStr,String add_type_probabilityStr,String rewardStr,String basic_attributesStr,int exchange_ranking,String expStr,String typeStr){
        this.id = id;
        this.times = times;
        this.special_times = special_times;
        this.type_probability = new ReadIntegerArrayEs(type_probabilityStr,"}",",");
        this.quality_probability = new ReadIntegerArrayEs(quality_probabilityStr,"}",",");
        this.special_times1 = special_times1;
        this.special_type_probability = new ReadIntegerArrayEs(special_type_probabilityStr,"}",",");
        this.special_quality_probability = new ReadIntegerArrayEs(special_quality_probabilityStr,"}",",");
        this.add_type_probability = new ReadIntegerArray(add_type_probabilityStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.basic_attributes = new ReadIntegerArray(basic_attributesStr,",");
        this.exchange_ranking = exchange_ranking;
        this.exp = new ReadIntegerArray(expStr,",");
        this.type = new ReadIntegerArray(typeStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("times:").append(times).append(";");
        str.append("special_times:").append(special_times).append(";");
        str.append("type_probability:").append(type_probability).append(";");
        str.append("quality_probability:").append(quality_probability).append(";");
        str.append("special_times1:").append(special_times1).append(";");
        str.append("special_type_probability:").append(special_type_probability).append(";");
        str.append("special_quality_probability:").append(special_quality_probability).append(";");
        str.append("add_type_probability:").append(add_type_probability).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("basic_attributes:").append(basic_attributes).append(";");
        str.append("exchange_ranking:").append(exchange_ranking).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("type:").append(type).append(";");
        return str.toString();
    }
}
