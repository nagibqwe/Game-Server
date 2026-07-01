/**
 * Auto generated, do not edit it
 *
 * retrieveRes配置表
 */
package com.data.bean;

import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_RetrieveRes_Bean{
    /**
     * 唯一ID
     */
    private final int id;
    /**
     * 唯一ID
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
     * 等级min
     */
    private final int minLevel;
    /**
     * 等级min
     * @return
     */
    public final int getMinLevel(){
        return minLevel;
    }
    /**
     * 等级max
     */
    private final int maxLevel;
    /**
     * 等级max
     * @return
     */
    public final int getMaxLevel(){
        return maxLevel;
    }
    /**
     * 对应FunctionStart表里的主键ID
用于程序判断功能是否开启
     */
    private final int openVariables;
    /**
     * 对应FunctionStart表里的主键ID
用于程序判断功能是否开启
     * @return
     */
    public final int getOpenVariables(){
        return openVariables;
    }
    /**
     * 对应daily表主键，
用于程序定位功能
填0代表无日常项
     */
    private final int dailyId;
    /**
     * 对应daily表主键，
用于程序定位功能
填0代表无日常项
     * @return
     */
    public final int getDailyId(){
        return dailyId;
    }
    /**
     * 对应vipPower主键
用于定位额外购买次数
填0代表无VIP购买次数
     */
    private final int vipPower;
    /**
     * 对应vipPower主键
用于定位额外购买次数
填0代表无VIP购买次数
     * @return
     */
    public final int getVipPower(){
        return vipPower;
    }
    /**
     * 找回类型名字，客户端调用界面显示
     */
    private final String name;
    /**
     * 找回类型名字，客户端调用界面显示
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 部分找回奖励
ID_Num_Bind_Occ
ID_数量_绑定_职业
绑定：0未绑定 1绑定
职业：0男 1女 9通用
部分50%比例（活跃25%）
     */
    private final ReadLongArrayEs reward_part;
    /**
     * 部分找回奖励
ID_Num_Bind_Occ
ID_数量_绑定_职业
绑定：0未绑定 1绑定
职业：0男 1女 9通用
部分50%比例（活跃25%）
     * @return
     */
    public final ReadLongArrayEs getReward_part(){
        return reward_part;
    }
    /**
     * 部分找回消耗
物品ID_数量
     */
    private final ReadIntegerArray cost_part;
    /**
     * 部分找回消耗
物品ID_数量
     * @return
     */
    public final ReadIntegerArray getCost_part(){
        return cost_part;
    }
    /**
     * 完美找回奖励
ID_Num_Bind_Occ
ID_数量_绑定_职业
绑定：0未绑定 1绑定
职业：0男 1女 9通用
完美100%比例(活跃50%）
     */
    private final ReadLongArrayEs reward_perfect;
    /**
     * 完美找回奖励
ID_Num_Bind_Occ
ID_数量_绑定_职业
绑定：0未绑定 1绑定
职业：0男 1女 9通用
完美100%比例(活跃50%）
     * @return
     */
    public final ReadLongArrayEs getReward_perfect(){
        return reward_perfect;
    }
    /**
     * 部分找回经验值奖励
（为了减少客户端字符串数量独立）
     */
    private final long rewardexp_part;
    /**
     * 部分找回经验值奖励
（为了减少客户端字符串数量独立）
     * @return
     */
    public final long getRewardexp_part(){
        return rewardexp_part;
    }
    /**
     * 完美找回经验值奖励
（为了减少客户端字符串数量独立）
     */
    private final long rewardexp_perfect;
    /**
     * 完美找回经验值奖励
（为了减少客户端字符串数量独立）
     * @return
     */
    public final long getRewardexp_perfect(){
        return rewardexp_perfect;
    }
    /**
     * 完美找回消耗
物品ID_数量
     */
    private final ReadIntegerArray cost_perfect;
    /**
     * 完美找回消耗
物品ID_数量
     * @return
     */
    public final ReadIntegerArray getCost_perfect(){
        return cost_perfect;
    }
    /**
     * 次数(每天系统提供的参与次数)
活跃值代表可找回上限
     */
    private final int max;
    /**
     * 次数(每天系统提供的参与次数)
活跃值代表可找回上限
     * @return
     */
    public final int getMax(){
        return max;
    }
    /**
     * 找回组（每次点击找回能够找回多少资源）
比如：max=20；group=10
表示每次找回10次奖励，找回2次就找回满了
（废弃，保持和max相等即可）
     */
    private final int group;
    /**
     * 找回组（每次点击找回能够找回多少资源）
比如：max=20；group=10
表示每次找回10次奖励，找回2次就找回满了
（废弃，保持和max相等即可）
     * @return
     */
    public final int getGroup(){
        return group;
    }

    public Cfg_RetrieveRes_Bean(int id,int type,int minLevel,int maxLevel,int openVariables,int dailyId,int vipPower,String name,String reward_partStr,String cost_partStr,String reward_perfectStr,long rewardexp_part,long rewardexp_perfect,String cost_perfectStr,int max,int group){
        this.id = id;
        this.type = type;
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.openVariables = openVariables;
        this.dailyId = dailyId;
        this.vipPower = vipPower;
        this.name = name;
        this.reward_part = new ReadLongArrayEs(reward_partStr,"}",",");
        this.cost_part = new ReadIntegerArray(cost_partStr,",");
        this.reward_perfect = new ReadLongArrayEs(reward_perfectStr,"}",",");
        this.rewardexp_part = rewardexp_part;
        this.rewardexp_perfect = rewardexp_perfect;
        this.cost_perfect = new ReadIntegerArray(cost_perfectStr,",");
        this.max = max;
        this.group = group;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("minLevel:").append(minLevel).append(";");
        str.append("maxLevel:").append(maxLevel).append(";");
        str.append("openVariables:").append(openVariables).append(";");
        str.append("dailyId:").append(dailyId).append(";");
        str.append("vipPower:").append(vipPower).append(";");
        str.append("name:").append(name).append(";");
        str.append("reward_part:").append(reward_part).append(";");
        str.append("cost_part:").append(cost_part).append(";");
        str.append("reward_perfect:").append(reward_perfect).append(";");
        str.append("rewardexp_part:").append(rewardexp_part).append(";");
        str.append("rewardexp_perfect:").append(rewardexp_perfect).append(";");
        str.append("cost_perfect:").append(cost_perfect).append(";");
        str.append("max:").append(max).append(";");
        str.append("group:").append(group).append(";");
        return str.toString();
    }
}
