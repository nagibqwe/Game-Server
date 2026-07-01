/**
 * Auto generated, do not edit it
 *
 * Skill_Trigger配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Skill_Trigger_Bean{
    /**
     * 触发Id
     */
    private final int id;
    /**
     * 触发Id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 触发的条件（1.常态,2.被攻击，3.被暴击，4.被追击，5.被连击,6.被会心一击,7.攻击，8.暴击，9.追击，10.连击，11.会心一击)
     */
    private final int Trigger_Conditions;
    /**
     * 触发的条件（1.常态,2.被攻击，3.被暴击，4.被追击，5.被连击,6.被会心一击,7.攻击，8.暴击，9.追击，10.连击，11.会心一击)
     * @return
     */
    public final int getTrigger_Conditions(){
        return Trigger_Conditions;
    }
    /**
     * 触发目标类型（0，通用；1，玩家；2，怪物）
     */
    private final int Trigger_type;
    /**
     * 触发目标类型（0，通用；1，玩家；2，怪物）
     * @return
     */
    public final int getTrigger_type(){
        return Trigger_type;
    }
    /**
     * 作用目标（自己，目标,主人）
     */
    private final int Target;
    /**
     * 作用目标（自己，目标,主人）
     * @return
     */
    public final int getTarget(){
        return Target;
    }
    /**
     * 作用目标血量检测(0:少于；1：大于；-1：不检测）
     */
    private final ReadIntegerArray Hp;
    /**
     * 作用目标血量检测(0:少于；1：大于；-1：不检测）
     * @return
     */
    public final ReadIntegerArray getHp(){
        return Hp;
    }
    /**
     * 被动检测（填对应被动技能 ID，不填则不生效，填写后在玩家本身拥有该BUFF时才能生效）
     */
    private final int skill;
    /**
     * 被动检测（填对应被动技能 ID，不填则不生效，填写后在玩家本身拥有该BUFF时才能生效）
     * @return
     */
    public final int getSkill(){
        return skill;
    }
    /**
     * 效果检测（填对应BUFF ID，不填则不生效，填写后在作用目标有该BUFF时才能生效）
     */
    private final int buff;
    /**
     * 效果检测（填对应BUFF ID，不填则不生效，填写后在作用目标有该BUFF时才能生效）
     * @return
     */
    public final int getBuff(){
        return buff;
    }
    /**
     * 效果数量监测（0_增益BUFF数量；1_减益BUFF数量）
     */
    private final ReadIntegerArray buff_num;
    /**
     * 效果数量监测（0_增益BUFF数量；1_减益BUFF数量）
     * @return
     */
    public final ReadIntegerArray getBuff_num(){
        return buff_num;
    }
    /**
     * 作用效果 1_属性ID_数值_几率；2_增加BUFFID_几率_CD【只参与计算，不是给人加的；3_清除自身负面buff的几率_CD；4_使用技能ID_几率_CD
     */
    private final ReadIntegerArrayEs effect;
    /**
     * 作用效果 1_属性ID_数值_几率；2_增加BUFFID_几率_CD【只参与计算，不是给人加的；3_清除自身负面buff的几率_CD；4_使用技能ID_几率_CD
     * @return
     */
    public final ReadIntegerArrayEs getEffect(){
        return effect;
    }

    public Cfg_Skill_Trigger_Bean(int id,int Trigger_Conditions,int Trigger_type,int Target,String HpStr,int skill,int buff,String buff_numStr,String effectStr){
        this.id = id;
        this.Trigger_Conditions = Trigger_Conditions;
        this.Trigger_type = Trigger_type;
        this.Target = Target;
        this.Hp = new ReadIntegerArray(HpStr,",");
        this.skill = skill;
        this.buff = buff;
        this.buff_num = new ReadIntegerArray(buff_numStr,",");
        this.effect = new ReadIntegerArrayEs(effectStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("Trigger_Conditions:").append(Trigger_Conditions).append(";");
        str.append("Trigger_type:").append(Trigger_type).append(";");
        str.append("Target:").append(Target).append(";");
        str.append("Hp:").append(Hp).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("buff:").append(buff).append(";");
        str.append("buff_num:").append(buff_num).append(";");
        str.append("effect:").append(effect).append(";");
        return str.toString();
    }
}
