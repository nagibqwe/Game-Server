/**
 * Auto generated, do not edit it
 *
 * clone_xinmo配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Clone_xinmo_Bean{
    /**
     * id
     */
    private final int id;
    /**
     * id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 第一波刷怪的波数 client ignore
     */
    private final int clone_monster_id;
    /**
     * 第一波刷怪的波数 client ignore
     * @return
     */
    public final int getClone_monster_id(){
        return clone_monster_id;
    }
    /**
     * 进入所需最小等级
     */
    private final int min_lv;
    /**
     * 进入所需最小等级
     * @return
     */
    public final int getMin_lv(){
        return min_lv;
    }
    /**
     * 最高等级进入限制
     */
    private final int max_lv;
    /**
     * 最高等级进入限制
     * @return
     */
    public final int getMax_lv(){
        return max_lv;
    }
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@] client ignore
     */
    private final ReadIntegerArrayEs extra_reward;
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@] client ignore
     * @return
     */
    public final ReadIntegerArrayEs getExtra_reward(){
        return extra_reward;
    }

    public Cfg_Clone_xinmo_Bean(int id,int clone_monster_id,int min_lv,int max_lv,String extra_rewardStr){
        this.id = id;
        this.clone_monster_id = clone_monster_id;
        this.min_lv = min_lv;
        this.max_lv = max_lv;
        this.extra_reward = new ReadIntegerArrayEs(extra_rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("clone_monster_id:").append(clone_monster_id).append(";");
        str.append("min_lv:").append(min_lv).append(";");
        str.append("max_lv:").append(max_lv).append(";");
        str.append("extra_reward:").append(extra_reward).append(";");
        return str.toString();
    }
}
