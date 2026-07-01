/**
 * Auto generated, do not edit it
 *
 * EightCity配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_EightCity_Bean{
    /**
     * 城市ID
     */
    private final int id;
    /**
     * 城市ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 城市名字
     */
    private final String name;
    /**
     * 城市名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 城市等级
     */
    private final int level;
    /**
     * 城市等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 占领之后的奖励配置
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 占领之后的奖励配置
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 守城boss的ID
     */
    private final int bossID;
    /**
     * 守城boss的ID
     * @return
     */
    public final int getBossID(){
        return bossID;
    }
    /**
     * 守城BOSS的刷新位置
     */
    private final ReadIntegerArray bossPos;
    /**
     * 守城BOSS的刷新位置
     * @return
     */
    public final ReadIntegerArray getBossPos(){
        return bossPos;
    }
    /**
     * 相邻的城市ID
     */
    private final ReadIntegerArray canAttackCity;
    /**
     * 相邻的城市ID
     * @return
     */
    public final ReadIntegerArray getCanAttackCity(){
        return canAttackCity;
    }
    /**
     * 线ID
     */
    private final ReadIntegerArray canAttackCityLine;
    /**
     * 线ID
     * @return
     */
    public final ReadIntegerArray getCanAttackCityLine(){
        return canAttackCityLine;
    }
    /**
     * 副本ID
     */
    private final int modleID;
    /**
     * 副本ID
     * @return
     */
    public final int getModleID(){
        return modleID;
    }

    public Cfg_EightCity_Bean(int id,String name,int level,String rewardStr,int bossID,String bossPosStr,String canAttackCityStr,String canAttackCityLineStr,int modleID){
        this.id = id;
        this.name = name;
        this.level = level;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.bossID = bossID;
        this.bossPos = new ReadIntegerArray(bossPosStr,",");
        this.canAttackCity = new ReadIntegerArray(canAttackCityStr,",");
        this.canAttackCityLine = new ReadIntegerArray(canAttackCityLineStr,",");
        this.modleID = modleID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("level:").append(level).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("bossID:").append(bossID).append(";");
        str.append("bossPos:").append(bossPos).append(";");
        str.append("canAttackCity:").append(canAttackCity).append(";");
        str.append("canAttackCityLine:").append(canAttackCityLine).append(";");
        str.append("modleID:").append(modleID).append(";");
        return str.toString();
    }
}
