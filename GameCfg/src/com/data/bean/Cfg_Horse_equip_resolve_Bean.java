/**
 * Auto generated, do not edit it
 *
 * Horse_equip_resolve配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Horse_equip_resolve_Bean{
    /**
     * 装备id
     */
    private final int Id;
    /**
     * 装备id
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 分解出来的道具,概率为万分比
概率_物品id_数量；概率_物品id_数量
     */
    private final ReadIntegerArrayEs Resolve_rewards;
    /**
     * 分解出来的道具,概率为万分比
概率_物品id_数量；概率_物品id_数量
     * @return
     */
    public final ReadIntegerArrayEs getResolve_rewards(){
        return Resolve_rewards;
    }

    public Cfg_Horse_equip_resolve_Bean(int Id,String Resolve_rewardsStr){
        this.Id = Id;
        this.Resolve_rewards = new ReadIntegerArrayEs(Resolve_rewardsStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Resolve_rewards:").append(Resolve_rewards).append(";");
        return str.toString();
    }
}
