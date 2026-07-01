/**
 * Auto generated, do not edit it
 *
 * clone_daneng配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Clone_daneng_Bean{
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
     * 大BOSS的战斗力
     */
    private final int monster_fight_num;
    /**
     * 大BOSS的战斗力
     * @return
     */
    public final int getMonster_fight_num(){
        return monster_fight_num;
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
     * 普通怪血量
     */
    private final long monster_max_hp1;
    /**
     * 普通怪血量
     * @return
     */
    public final long getMonster_max_hp1(){
        return monster_max_hp1;
    }
    /**
     * 精英怪血量
     */
    private final long monster_max_hp2;
    /**
     * 精英怪血量
     * @return
     */
    public final long getMonster_max_hp2(){
        return monster_max_hp2;
    }
    /**
     * boss血量
     */
    private final long monster_max_hp3;
    /**
     * boss血量
     * @return
     */
    public final long getMonster_max_hp3(){
        return monster_max_hp3;
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

    public Cfg_Clone_daneng_Bean(int id,int monster_fight_num,int min_lv,int max_lv,long monster_max_hp1,long monster_max_hp2,long monster_max_hp3,String extra_rewardStr){
        this.id = id;
        this.monster_fight_num = monster_fight_num;
        this.min_lv = min_lv;
        this.max_lv = max_lv;
        this.monster_max_hp1 = monster_max_hp1;
        this.monster_max_hp2 = monster_max_hp2;
        this.monster_max_hp3 = monster_max_hp3;
        this.extra_reward = new ReadIntegerArrayEs(extra_rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("monster_fight_num:").append(monster_fight_num).append(";");
        str.append("min_lv:").append(min_lv).append(";");
        str.append("max_lv:").append(max_lv).append(";");
        str.append("monster_max_hp1:").append(monster_max_hp1).append(";");
        str.append("monster_max_hp2:").append(monster_max_hp2).append(";");
        str.append("monster_max_hp3:").append(monster_max_hp3).append(";");
        str.append("extra_reward:").append(extra_reward).append(";");
        return str.toString();
    }
}
