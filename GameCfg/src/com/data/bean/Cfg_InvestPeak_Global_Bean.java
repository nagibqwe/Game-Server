/**
 * Auto generated, do not edit it
 *
 * investPeak_Global配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_InvestPeak_Global_Bean{
    /**
     * ID=gear*10000+times
     */
    private final int ID;
    /**
     * ID=gear*10000+times
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 27、第一档
28、第二档
29、第三档
     */
    private final int gear;
    /**
     * 27、第一档
28、第二档
29、第三档
     * @return
     */
    public final int getGear(){
        return gear;
    }
    /**
     * 领取需要达到的等级
     */
    private final int level;
    /**
     * 领取需要达到的等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 成长基金全服奖励达到次数
     */
    private final int times;
    /**
     * 成长基金全服奖励达到次数
     * @return
     */
    public final int getTimes(){
        return times;
    }
    /**
     * ID_num_是否绑定(0否 1是)
     */
    private final ReadIntegerArray reward;
    /**
     * ID_num_是否绑定(0否 1是)
     * @return
     */
    public final ReadIntegerArray getReward(){
        return reward;
    }

    public Cfg_InvestPeak_Global_Bean(int ID,int gear,int level,int times,String rewardStr){
        this.ID = ID;
        this.gear = gear;
        this.level = level;
        this.times = times;
        this.reward = new ReadIntegerArray(rewardStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("gear:").append(gear).append(";");
        str.append("level:").append(level).append(";");
        str.append("times:").append(times).append(";");
        str.append("reward:").append(reward).append(";");
        return str.toString();
    }
}
