/**
 * Auto generated, do not edit it
 *
 * guild_war_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_war_rank_Bean{
    /**
     * 编号
     */
    private final int id;
    /**
     * 编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 评级名称
     */
    private final String name;
    /**
     * 评级名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 每个评级对应的仙盟数量
     */
    private final int count;
    /**
     * 每个评级对应的仙盟数量
     * @return
     */
    public final int getCount(){
        return count;
    }
    /**
     * 评级方式
-1代表每次评级都按照战斗力排序
具体值代表选取的战斗力排名范围
     */
    private final ReadIntegerArray firstRankMethod;
    /**
     * 评级方式
-1代表每次评级都按照战斗力排序
具体值代表选取的战斗力排名范围
     * @return
     */
    public final ReadIntegerArray getFirstRankMethod(){
        return firstRankMethod;
    }
    /**
     * 第一名仙盟结算奖励
物品_数量_绑定
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定(需要上仙盟拍卖行的必须是未绑定状态）
     */
    private final ReadIntegerArrayEs guildReward1;
    /**
     * 第一名仙盟结算奖励
物品_数量_绑定
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定(需要上仙盟拍卖行的必须是未绑定状态）
     * @return
     */
    public final ReadIntegerArrayEs getGuildReward1(){
        return guildReward1;
    }
    /**
     * 第二名仙盟结算奖励
     */
    private final ReadIntegerArrayEs guildReward2;
    /**
     * 第二名仙盟结算奖励
     * @return
     */
    public final ReadIntegerArrayEs getGuildReward2(){
        return guildReward2;
    }
    /**
     * 第三名仙盟结算奖励
     */
    private final ReadIntegerArrayEs guildReward3;
    /**
     * 第三名仙盟结算奖励
     * @return
     */
    public final ReadIntegerArrayEs getGuildReward3(){
        return guildReward3;
    }
    /**
     * 第一名个人结算奖励
物品_数量_绑定_职业
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定
职业：0男剑1女枪9通用
     */
    private final ReadIntegerArrayEs personalReward1;
    /**
     * 第一名个人结算奖励
物品_数量_绑定_职业
物品：对应item表主键
数量：对应奖励的数量
绑定：0未绑定1绑定
职业：0男剑1女枪9通用
     * @return
     */
    public final ReadIntegerArrayEs getPersonalReward1(){
        return personalReward1;
    }
    /**
     * 第二名个人结算奖励
     */
    private final ReadIntegerArrayEs personalReward2;
    /**
     * 第二名个人结算奖励
     * @return
     */
    public final ReadIntegerArrayEs getPersonalReward2(){
        return personalReward2;
    }
    /**
     * 第三名个人结算奖励
     */
    private final ReadIntegerArrayEs personalReward3;
    /**
     * 第三名个人结算奖励
     * @return
     */
    public final ReadIntegerArrayEs getPersonalReward3(){
        return personalReward3;
    }

    public Cfg_Guild_war_rank_Bean(int id,String name,int count,String firstRankMethodStr,String guildReward1Str,String guildReward2Str,String guildReward3Str,String personalReward1Str,String personalReward2Str,String personalReward3Str){
        this.id = id;
        this.name = name;
        this.count = count;
        this.firstRankMethod = new ReadIntegerArray(firstRankMethodStr,",");
        this.guildReward1 = new ReadIntegerArrayEs(guildReward1Str,"}",",");
        this.guildReward2 = new ReadIntegerArrayEs(guildReward2Str,"}",",");
        this.guildReward3 = new ReadIntegerArrayEs(guildReward3Str,"}",",");
        this.personalReward1 = new ReadIntegerArrayEs(personalReward1Str,"}",",");
        this.personalReward2 = new ReadIntegerArrayEs(personalReward2Str,"}",",");
        this.personalReward3 = new ReadIntegerArrayEs(personalReward3Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("count:").append(count).append(";");
        str.append("firstRankMethod:").append(firstRankMethod).append(";");
        str.append("guildReward1:").append(guildReward1).append(";");
        str.append("guildReward2:").append(guildReward2).append(";");
        str.append("guildReward3:").append(guildReward3).append(";");
        str.append("personalReward1:").append(personalReward1).append(";");
        str.append("personalReward2:").append(personalReward2).append(";");
        str.append("personalReward3:").append(personalReward3).append(";");
        return str.toString();
    }
}
