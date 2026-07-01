/**
 * Auto generated, do not edit it
 *
 * week_welfare_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Week_welfare_reward_Bean{
    /**
     * 任务ID
     */
    private final int id;
    /**
     * 任务ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 1：特等奖
2：一等奖
3：二等奖
4：三等奖
     */
    private final int type;
    /**
     * 1：特等奖
2：一等奖
3：二等奖
4：三等奖
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 对应奖励个数（总数相加为8个，客户端只有8个显示位置）
     */
    private final int num;
    /**
     * 对应奖励个数（总数相加为8个，客户端只有8个显示位置）
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 可替换奖池（一个type只对应一个奖池），且只有特等奖和一等奖有奖池,系统默认选中奖池前1-2个道具
item_num_bind_occ_level
bind:0未绑定1绑定
occ：0男1女9通用
level:奖池物品兑换开启等级(0为默认奖励）
     */
    private final ReadIntegerArrayEs rewardPool;
    /**
     * 可替换奖池（一个type只对应一个奖池），且只有特等奖和一等奖有奖池,系统默认选中奖池前1-2个道具
item_num_bind_occ_level
bind:0未绑定1绑定
occ：0男1女9通用
level:奖池物品兑换开启等级(0为默认奖励）
     * @return
     */
    public final ReadIntegerArrayEs getRewardPool(){
        return rewardPool;
    }
    /**
     * 概率，每一个物品对应抽中的概率，总和必须等于100000
     */
    private final int probability;
    /**
     * 概率，每一个物品对应抽中的概率，总和必须等于100000
     * @return
     */
    public final int getProbability(){
        return probability;
    }
    /**
     * 触发幸运抽奖后的概率（抽奖一定次数后，必定抽中二等奖及以上），具体次数配置global表1882
     */
    private final int luckProbability;
    /**
     * 触发幸运抽奖后的概率（抽奖一定次数后，必定抽中二等奖及以上），具体次数配置global表1882
     * @return
     */
    public final int getLuckProbability(){
        return luckProbability;
    }

    public Cfg_Week_welfare_reward_Bean(int id,int type,int num,String rewardPoolStr,int probability,int luckProbability){
        this.id = id;
        this.type = type;
        this.num = num;
        this.rewardPool = new ReadIntegerArrayEs(rewardPoolStr,"}",",");
        this.probability = probability;
        this.luckProbability = luckProbability;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("num:").append(num).append(";");
        str.append("rewardPool:").append(rewardPool).append(";");
        str.append("probability:").append(probability).append(";");
        str.append("luckProbability:").append(luckProbability).append(";");
        return str.toString();
    }
}
