/**
 * Auto generated, do not edit it
 *
 * marry_battle_reward配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Marry_battle_reward_Bean{
    /**
     * 排序id
     */
    private final int id;
    /**
     * 排序id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 类型
1海选赛每场奖励；2小组赛每场奖励；3冠军赛每场奖励；4，海选赛场次奖励；5小组赛排名奖励；6冠军赛(地榜）排名奖励；7冠军赛(天榜）排名奖励
     */
    private final int type;
    /**
     * 类型
1海选赛每场奖励；2小组赛每场奖励；3冠军赛每场奖励；4，海选赛场次奖励；5小组赛排名奖励；6冠军赛(地榜）排名奖励；7冠军赛(天榜）排名奖励
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 参数：每场奖励没有参数，填0，场次奖励填对应的场次，排名奖励填写排名的闭区间
     */
    private final ReadIntegerArray parm;
    /**
     * 参数：每场奖励没有参数，填0，场次奖励填对应的场次，排名奖励填写排名的闭区间
     * @return
     */
    public final ReadIntegerArray getParm(){
        return parm;
    }
    /**
     * 具体奖励
     */
    private final ReadIntegerArrayEs reward_item;
    /**
     * 具体奖励
     * @return
     */
    public final ReadIntegerArrayEs getReward_item(){
        return reward_item;
    }

    public Cfg_Marry_battle_reward_Bean(int id,int type,String parmStr,String reward_itemStr){
        this.id = id;
        this.type = type;
        this.parm = new ReadIntegerArray(parmStr,",");
        this.reward_item = new ReadIntegerArrayEs(reward_itemStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("type:").append(type).append(";");
        str.append("parm:").append(parm).append(";");
        str.append("reward_item:").append(reward_item).append(";");
        return str.toString();
    }
}
