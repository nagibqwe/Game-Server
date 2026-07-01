/**
 * Auto generated, do not edit it
 *
 * challenge_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadLongArrayEs; 
	
public class Cfg_Challenge_reward_Bean{
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
     * 需求玩家等级
     */
    private final int need_level;
    /**
     * 需求玩家等级
     * @return
     */
    public final int getNeed_level(){
        return need_level;
    }
    /**
     * 普通通关奖励(@;@_@)
     */
    private final ReadLongArrayEs normal_reward;
    /**
     * 普通通关奖励(@;@_@)
     * @return
     */
    public final ReadLongArrayEs getNormal_reward(){
        return normal_reward;
    }
    /**
     * 章节通关奖励奖励(@;@_@)
     */
    private final ReadLongArrayEs chapter_reward;
    /**
     * 章节通关奖励奖励(@;@_@)
     * @return
     */
    public final ReadLongArrayEs getChapter_reward(){
        return chapter_reward;
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

    public Cfg_Challenge_reward_Bean(int num,int need_level,String normal_rewardStr,String chapter_rewardStr,int need_fight_power){
        this.num = num;
        this.need_level = need_level;
        this.normal_reward = new ReadLongArrayEs(normal_rewardStr,"}",",");
        this.chapter_reward = new ReadLongArrayEs(chapter_rewardStr,"}",",");
        this.need_fight_power = need_fight_power;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("num:").append(num).append(";");
        str.append("need_level:").append(need_level).append(";");
        str.append("normal_reward:").append(normal_reward).append(";");
        str.append("chapter_reward:").append(chapter_reward).append(";");
        str.append("need_fight_power:").append(need_fight_power).append(";");
        return str.toString();
    }
}
