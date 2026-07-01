/**
 * Auto generated, do not edit it
 *
 * Cross_devil_Group_Copy配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_Group_Copy_Bean{
    /**
     * id
（和clonemap配置主键保持一致）
     */
    private final int id;
    /**
     * id
（和clonemap配置主键保持一致）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 开启副本消耗道具
     */
    private final ReadIntegerArray Open_Item;
    /**
     * 开启副本消耗道具
     * @return
     */
    public final ReadIntegerArray getOpen_Item(){
        return Open_Item;
    }
    /**
     * 团长奖励
     */
    private final ReadIntegerArrayEs cap_Reward;
    /**
     * 团长奖励
     * @return
     */
    public final ReadIntegerArrayEs getCap_Reward(){
        return cap_Reward;
    }
    /**
     * 团员奖励
     */
    private final ReadIntegerArrayEs member_Reward;
    /**
     * 团员奖励
     * @return
     */
    public final ReadIntegerArrayEs getMember_Reward(){
        return member_Reward;
    }
    /**
     * 副本刷新怪物Id和刷新坐标
怪物ID_坐标X_坐标Z
对应Cross_devil_Group_Boss和monster表的主键
     */
    private final ReadIntegerArrayEs boss;
    /**
     * 副本刷新怪物Id和刷新坐标
怪物ID_坐标X_坐标Z
对应Cross_devil_Group_Boss和monster表的主键
     * @return
     */
    public final ReadIntegerArrayEs getBoss(){
        return boss;
    }

    public Cfg_Cross_devil_Group_Copy_Bean(int id,String Open_ItemStr,String cap_RewardStr,String member_RewardStr,String bossStr){
        this.id = id;
        this.Open_Item = new ReadIntegerArray(Open_ItemStr,",");
        this.cap_Reward = new ReadIntegerArrayEs(cap_RewardStr,"}",",");
        this.member_Reward = new ReadIntegerArrayEs(member_RewardStr,"}",",");
        this.boss = new ReadIntegerArrayEs(bossStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("Open_Item:").append(Open_Item).append(";");
        str.append("cap_Reward:").append(cap_Reward).append(";");
        str.append("member_Reward:").append(member_Reward).append(";");
        str.append("boss:").append(boss).append(";");
        return str.toString();
    }
}
