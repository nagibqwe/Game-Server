/**
 * Auto generated, do not edit it
 *
 * FallingSky_Level配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_FallingSky_Level_Bean{
    /**
     * 
     */
    private final int id;
    /**
     * 
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 最大轮数由global进行处理，修改配置后必须对应修改global的最大轮数
1930 FallingSky_Round_Limit
     */
    private final int goup;
    /**
     * 最大轮数由global进行处理，修改配置后必须对应修改global的最大轮数
1930 FallingSky_Round_Limit
     * @return
     */
    public final int getGoup(){
        return goup;
    }
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 对应的材料和数量
     */
    private final ReadIntegerArray exp;
    /**
     * 对应的材料和数量
     * @return
     */
    public final ReadIntegerArray getExp(){
        return exp;
    }
    /**
     * 免费版奖励
     */
    private final ReadIntegerArrayEs FreeReward;
    /**
     * 免费版奖励
     * @return
     */
    public final ReadIntegerArrayEs getFreeReward(){
        return FreeReward;
    }
    /**
     * 付费版奖励
     */
    private final ReadIntegerArrayEs PayReward;
    /**
     * 付费版奖励
     * @return
     */
    public final ReadIntegerArrayEs getPayReward(){
        return PayReward;
    }

    public Cfg_FallingSky_Level_Bean(int id,int goup,int level,String expStr,String FreeRewardStr,String PayRewardStr){
        this.id = id;
        this.goup = goup;
        this.level = level;
        this.exp = new ReadIntegerArray(expStr,",");
        this.FreeReward = new ReadIntegerArrayEs(FreeRewardStr,"}",",");
        this.PayReward = new ReadIntegerArrayEs(PayRewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("goup:").append(goup).append(";");
        str.append("level:").append(level).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("FreeReward:").append(FreeReward).append(";");
        str.append("PayReward:").append(PayReward).append(";");
        return str.toString();
    }
}
