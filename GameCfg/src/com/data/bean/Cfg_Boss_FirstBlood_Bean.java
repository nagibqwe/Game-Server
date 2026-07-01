/**
 * Auto generated, do not edit it
 *
 * boss_FirstBlood配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Boss_FirstBlood_Bean{
    /**
     * 编号ID
     */
    private final int ID;
    /**
     * 编号ID
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 怪物ID
     */
    private final int monster_id;
    /**
     * 怪物ID
     * @return
     */
    public final int getMonster_id(){
        return monster_id;
    }
    /**
     * boss的类型（1，无极墟域；2晶甲和域；3神兽岛；4VIP首领）
     */
    private final int boss_type;
    /**
     * boss的类型（1，无极墟域；2晶甲和域；3神兽岛；4VIP首领）
     * @return
     */
    public final int getBoss_type(){
        return boss_type;
    }
    /**
     * 首杀的红包奖励（货币ID_数量）
     */
    private final ReadIntegerArrayEs first_blood_cash;
    /**
     * 首杀的红包奖励（货币ID_数量）
     * @return
     */
    public final ReadIntegerArrayEs getFirst_blood_cash(){
        return first_blood_cash;
    }
    /**
     * 首杀奖励（职业ID_奖励_数量_绑定)
     */
    private final ReadIntegerArrayEs first_blood_reward;
    /**
     * 首杀奖励（职业ID_奖励_数量_绑定)
     * @return
     */
    public final ReadIntegerArrayEs getFirst_blood_reward(){
        return first_blood_reward;
    }
    /**
     * 个人奖励（职业ID_奖励_数量_绑定)
     */
    private final ReadIntegerArrayEs personal_reward;
    /**
     * 个人奖励（职业ID_奖励_数量_绑定)
     * @return
     */
    public final ReadIntegerArrayEs getPersonal_reward(){
        return personal_reward;
    }

    public Cfg_Boss_FirstBlood_Bean(int ID,int monster_id,int boss_type,String first_blood_cashStr,String first_blood_rewardStr,String personal_rewardStr){
        this.ID = ID;
        this.monster_id = monster_id;
        this.boss_type = boss_type;
        this.first_blood_cash = new ReadIntegerArrayEs(first_blood_cashStr,"}",",");
        this.first_blood_reward = new ReadIntegerArrayEs(first_blood_rewardStr,"}",",");
        this.personal_reward = new ReadIntegerArrayEs(personal_rewardStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("monster_id:").append(monster_id).append(";");
        str.append("boss_type:").append(boss_type).append(";");
        str.append("first_blood_cash:").append(first_blood_cash).append(";");
        str.append("first_blood_reward:").append(first_blood_reward).append(";");
        str.append("personal_reward:").append(personal_reward).append(";");
        return str.toString();
    }
}
