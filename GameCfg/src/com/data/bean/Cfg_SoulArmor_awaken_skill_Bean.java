/**
 * Auto generated, do not edit it
 *
 * SoulArmor_awaken_skill配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_awaken_skill_Bean{
    /**
     * 技能id（对应skill表中id）
     */
    private final int id;
    /**
     * 技能id（对应skill表中id）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 技能位置
     */
    private final int skillPosition;
    /**
     * 技能位置
     * @return
     */
    public final int getSkillPosition(){
        return skillPosition;
    }
    /**
     * 技能等级
     */
    private final int skillLevel;
    /**
     * 技能等级
     * @return
     */
    public final int getSkillLevel(){
        return skillLevel;
    }
    /**
     * 技能升级消耗 道具id_数量
     */
    private final ReadIntegerArrayEs consumeSkill;
    /**
     * 技能升级消耗 道具id_数量
     * @return
     */
    public final ReadIntegerArrayEs getConsumeSkill(){
        return consumeSkill;
    }
    /**
     * 升级目标技能id
     */
    private final int nextSkill;
    /**
     * 升级目标技能id
     * @return
     */
    public final int getNextSkill(){
        return nextSkill;
    }

    public Cfg_SoulArmor_awaken_skill_Bean(int id,int skillPosition,int skillLevel,String consumeSkillStr,int nextSkill){
        this.id = id;
        this.skillPosition = skillPosition;
        this.skillLevel = skillLevel;
        this.consumeSkill = new ReadIntegerArrayEs(consumeSkillStr,"}",",");
        this.nextSkill = nextSkill;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("skillPosition:").append(skillPosition).append(";");
        str.append("skillLevel:").append(skillLevel).append(";");
        str.append("consumeSkill:").append(consumeSkill).append(";");
        str.append("nextSkill:").append(nextSkill).append(";");
        return str.toString();
    }
}
