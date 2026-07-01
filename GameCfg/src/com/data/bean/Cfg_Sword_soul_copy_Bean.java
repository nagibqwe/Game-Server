/**
 * Auto generated, do not edit it
 *
 * sword_soul_copy配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Sword_soul_copy_Bean{
    /**
     * 波次
     */
    private final int num;
    /**
     * 波次
     * @return
     */
    public final int getNum(){
        return num;
    }
    /**
     * 通关奖励(@;@_@)
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 通关奖励(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 客户端用通关奖励得显示(@;@_@)
     */
    private final ReadIntegerArrayEs reward_show;
    /**
     * 客户端用通关奖励得显示(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getReward_show(){
        return reward_show;
    }
    /**
     * 解锁灵魄位置
     */
    private final int unlock_part;
    /**
     * 解锁灵魄位置
     * @return
     */
    public final int getUnlock_part(){
        return unlock_part;
    }
    /**
     * 服务器计算派发灵魄类型client ignore
     */
    private final int reward_type;
    /**
     * 服务器计算派发灵魄类型client ignore
     * @return
     */
    public final int getReward_type(){
        return reward_type;
    }
    /**
     * 需求战斗力
     */
    private final int need_fight_power;
    /**
     * 需求战斗力
     * @return
     */
    public final int getNeed_fight_power(){
        return need_fight_power;
    }
    /**
     * 跳过需要的战斗力
     */
    private final int pass_power;
    /**
     * 跳过需要的战斗力
     * @return
     */
    public final int getPass_power(){
        return pass_power;
    }
    /**
     * 挂机每次奖励的道具
     */
    private final ReadIntegerArrayEs mandate_single_reward;
    /**
     * 挂机每次奖励的道具
     * @return
     */
    public final ReadIntegerArrayEs getMandate_single_reward(){
        return mandate_single_reward;
    }
    /**
     * 通关灵魄奖励（数量_分类_品质）client ignore
     */
    private final ReadIntegerArrayEs immortal_soul_reward;
    /**
     * 通关灵魄奖励（数量_分类_品质）client ignore
     * @return
     */
    public final ReadIntegerArrayEs getImmortal_soul_reward(){
        return immortal_soul_reward;
    }
    /**
     * 通关灵魄奖励得类型枚举client ignore
     */
    private final ReadIntegerArrayEs immortal_soul_reward_exclusive;
    /**
     * 通关灵魄奖励得类型枚举client ignore
     * @return
     */
    public final ReadIntegerArrayEs getImmortal_soul_reward_exclusive(){
        return immortal_soul_reward_exclusive;
    }
    /**
     * 剑灵阁每层怪物ID_刷新坐标 client ignore
     */
    private final ReadIntegerArray moster_id_pos;
    /**
     * 剑灵阁每层怪物ID_刷新坐标 client ignore
     * @return
     */
    public final ReadIntegerArray getMoster_id_pos(){
        return moster_id_pos;
    }

    public Cfg_Sword_soul_copy_Bean(int num,String rewardStr,String reward_showStr,int unlock_part,int reward_type,int need_fight_power,int pass_power,String mandate_single_rewardStr,String immortal_soul_rewardStr,String immortal_soul_reward_exclusiveStr,String moster_id_posStr){
        this.num = num;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.reward_show = new ReadIntegerArrayEs(reward_showStr,"}",",");
        this.unlock_part = unlock_part;
        this.reward_type = reward_type;
        this.need_fight_power = need_fight_power;
        this.pass_power = pass_power;
        this.mandate_single_reward = new ReadIntegerArrayEs(mandate_single_rewardStr,"}",",");
        this.immortal_soul_reward = new ReadIntegerArrayEs(immortal_soul_rewardStr,"}",",");
        this.immortal_soul_reward_exclusive = new ReadIntegerArrayEs(immortal_soul_reward_exclusiveStr,"}",",");
        this.moster_id_pos = new ReadIntegerArray(moster_id_posStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("num:").append(num).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("reward_show:").append(reward_show).append(";");
        str.append("unlock_part:").append(unlock_part).append(";");
        str.append("reward_type:").append(reward_type).append(";");
        str.append("need_fight_power:").append(need_fight_power).append(";");
        str.append("pass_power:").append(pass_power).append(";");
        str.append("mandate_single_reward:").append(mandate_single_reward).append(";");
        str.append("immortal_soul_reward:").append(immortal_soul_reward).append(";");
        str.append("immortal_soul_reward_exclusive:").append(immortal_soul_reward_exclusive).append(";");
        str.append("moster_id_pos:").append(moster_id_pos).append(";");
        return str.toString();
    }
}
