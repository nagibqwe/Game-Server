/**
 * Auto generated, do not edit it
 *
 * guildBoss_Reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_GuildBoss_Reward_Bean{
    /**
     * ID
     */
    private final int ID;
    /**
     * ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 世界等级段
     */
    private final ReadIntegerArray world_level;
    /**
     * 世界等级段
     * @return
     */
    public final ReadIntegerArray getWorld_level(){
        return world_level;
    }
    /**
     * 排名区间
     */
    private final ReadIntegerArrayEs rank;
    /**
     * 排名区间
     * @return
     */
    public final ReadIntegerArrayEs getRank(){
        return rank;
    }
    /**
     * 进入竞拍得奖励
     */
    private final ReadIntegerArrayEs guild_auction_reward;
    /**
     * 进入竞拍得奖励
     * @return
     */
    public final ReadIntegerArrayEs getGuild_auction_reward(){
        return guild_auction_reward;
    }
    /**
     * 个人奖励
     */
    private final ReadIntegerArrayEs personal_reward;
    /**
     * 个人奖励
     * @return
     */
    public final ReadIntegerArrayEs getPersonal_reward(){
        return personal_reward;
    }
    /**
     * 小怪奖励
     */
    private final ReadIntegerArrayEs monster_reward;
    /**
     * 小怪奖励
     * @return
     */
    public final ReadIntegerArrayEs getMonster_reward(){
        return monster_reward;
    }

    public Cfg_GuildBoss_Reward_Bean(int ID,String world_levelStr,String rankStr,String guild_auction_rewardStr,String personal_rewardStr,String monster_rewardStr){
        this.ID = ID;
        this.world_level = new ReadIntegerArray(world_levelStr,",");
        this.rank = new ReadIntegerArrayEs(rankStr,"}",",");
        this.guild_auction_reward = new ReadIntegerArrayEs(guild_auction_rewardStr,"}",",");
        this.personal_reward = new ReadIntegerArrayEs(personal_rewardStr,"}",",");
        this.monster_reward = new ReadIntegerArrayEs(monster_rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("world_level:").append(world_level).append(";");
        str.append("rank:").append(rank).append(";");
        str.append("guild_auction_reward:").append(guild_auction_reward).append(";");
        str.append("personal_reward:").append(personal_reward).append(";");
        str.append("monster_reward:").append(monster_reward).append(";");
        return str.toString();
    }
}
