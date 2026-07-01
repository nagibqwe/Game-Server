/**
 * Auto generated, do not edit it
 *
 * JJCRank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_JJCRank_Bean{
    /**
     * 奖励的唯一ID
     */
    private final int id;
    /**
     * 奖励的唯一ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 段位的最小名次
     */
    private final int pos_mix;
    /**
     * 段位的最小名次
     * @return
     */
    public final int getPos_mix(){
        return pos_mix;
    }
    /**
     * 段位的最大名词，用于首次
     */
    private final int pos_max;
    /**
     * 段位的最大名词，用于首次
     * @return
     */
    public final int getPos_max(){
        return pos_max;
    }
    /**
     * 敌人1比我低最大
     */
    private final int enemy1;
    /**
     * 敌人1比我低最大
     * @return
     */
    public final int getEnemy1(){
        return enemy1;
    }
    /**
     * 敌人1比我高最大
     */
    private final int enemy1r;
    /**
     * 敌人1比我高最大
     * @return
     */
    public final int getEnemy1r(){
        return enemy1r;
    }
    /**
     * 敌人2比我低最大
     */
    private final int enemy2;
    /**
     * 敌人2比我低最大
     * @return
     */
    public final int getEnemy2(){
        return enemy2;
    }
    /**
     * 敌人2比我高最大
     */
    private final int enemy2r;
    /**
     * 敌人2比我高最大
     * @return
     */
    public final int getEnemy2r(){
        return enemy2r;
    }
    /**
     * 敌人3比我高最小
     */
    private final int enemy3;
    /**
     * 敌人3比我高最小
     * @return
     */
    public final int getEnemy3(){
        return enemy3;
    }
    /**
     * 敌人3比我高最大
     */
    private final int enemy3r;
    /**
     * 敌人3比我高最大
     * @return
     */
    public final int getEnemy3r(){
        return enemy3r;
    }
    /**
     * 首次到底排名奖励(@;@_@)
     */
    private final ReadIntegerArrayEs first_reward_item;
    /**
     * 首次到底排名奖励(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getFirst_reward_item(){
        return first_reward_item;
    }
    /**
     * 排名区间(@_@)
废弃
     */
    private final ReadIntegerArray rank;
    /**
     * 排名区间(@_@)
废弃
     * @return
     */
    public final ReadIntegerArray getRank(){
        return rank;
    }
    /**
     * 奖励(@;@_@)
废弃
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 奖励(@;@_@)
废弃
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }

    public Cfg_JJCRank_Bean(int id,int pos_mix,int pos_max,int enemy1,int enemy1r,int enemy2,int enemy2r,int enemy3,int enemy3r,String first_reward_itemStr,String rankStr,String rewardStr){
        this.id = id;
        this.pos_mix = pos_mix;
        this.pos_max = pos_max;
        this.enemy1 = enemy1;
        this.enemy1r = enemy1r;
        this.enemy2 = enemy2;
        this.enemy2r = enemy2r;
        this.enemy3 = enemy3;
        this.enemy3r = enemy3r;
        this.first_reward_item = new ReadIntegerArrayEs(first_reward_itemStr,"}",",");
        this.rank = new ReadIntegerArray(rankStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("pos_mix:").append(pos_mix).append(";");
        str.append("pos_max:").append(pos_max).append(";");
        str.append("enemy1:").append(enemy1).append(";");
        str.append("enemy1r:").append(enemy1r).append(";");
        str.append("enemy2:").append(enemy2).append(";");
        str.append("enemy2r:").append(enemy2r).append(";");
        str.append("enemy3:").append(enemy3).append(";");
        str.append("enemy3r:").append(enemy3r).append(";");
        str.append("first_reward_item:").append(first_reward_item).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
