/**
 * Auto generated, do not edit it
 *
 * extra_rewards配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Extra_rewards_Bean{
    /**
     * 额外奖励编号
     */
    private final int extra_rewards_id;
    /**
     * 额外奖励编号
     * @return
     */
    public final int getExtra_rewards_id(){
        return extra_rewards_id;
    }
    /**
     * 任务类型（0 主线 1 日常 2 讨伐）
     */
    private final int type;
    /**
     * 任务类型（0 主线 1 日常 2 讨伐）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 玩家等级min
     */
    private final int level_min;
    /**
     * 玩家等级min
     * @return
     */
    public final int getLevel_min(){
        return level_min;
    }
    /**
     * 玩家等级max
     */
    private final int level_max;
    /**
     * 玩家等级max
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 任务奖励(奖励类型1_普通奖励1_满星奖励1;奖励类型2_普通奖励2_满星奖励2;奖励类型N_普通奖励N_满星奖励N)(@;@_@)
     */
    private final ReadIntegerArrayEs rewards;
    /**
     * 任务奖励(奖励类型1_普通奖励1_满星奖励1;奖励类型2_普通奖励2_满星奖励2;奖励类型N_普通奖励N_满星奖励N)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRewards(){
        return rewards;
    }

    public Cfg_Extra_rewards_Bean(int extra_rewards_id,int type,int level_min,int level_max,String rewardsStr){
        this.extra_rewards_id = extra_rewards_id;
        this.type = type;
        this.level_min = level_min;
        this.level_max = level_max;
        this.rewards = new ReadIntegerArrayEs(rewardsStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("extra_rewards_id:").append(extra_rewards_id).append(";");
        str.append("type:").append(type).append(";");
        str.append("level_min:").append(level_min).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("rewards:").append(rewards).append(";");
        return str.toString();
    }
}
