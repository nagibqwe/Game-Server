/**
 * Auto generated, do not edit it
 *
 * Occ_Skill配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Occ_Skill_Bean{
    /**
     * 技能ID(心法ID*100000+职业*10000+技能位置*1000+蜕变*100+被动等级）
     */
    private final int ID;
    /**
     * 技能ID(心法ID*100000+职业*10000+技能位置*1000+蜕变*100+被动等级）
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 心法ID（组ID）
     */
    private final int Mental_ID;
    /**
     * 心法ID（组ID）
     * @return
     */
    public final int getMental_ID(){
        return Mental_ID;
    }
    /**
     * 职业（0，男剑；1，女枪）
     */
    private final int occ;
    /**
     * 职业（0，男剑；1，女枪）
     * @return
     */
    public final int getOcc(){
        return occ;
    }
    /**
     * 技能位置（0，普攻；1-4从左至右的4个技能。）
     */
    private final int position;
    /**
     * 技能位置（0，普攻；1-4从左至右的4个技能。）
     * @return
     */
    public final int getPosition(){
        return position;
    }
    /**
     * 是否是蜕变（0，不蜕变；1，蜕变）
     */
    private final int if_tuibian;
    /**
     * 是否是蜕变（0，不蜕变；1，蜕变）
     * @return
     */
    public final int getIf_tuibian(){
        return if_tuibian;
    }
    /**
     * 被动等级
     */
    private final int Passive_level;
    /**
     * 被动等级
     * @return
     */
    public final int getPassive_level(){
        return Passive_level;
    }
    /**
     * 关联伤害技能
     */
    private final ReadIntegerArray skill_id;
    /**
     * 关联伤害技能
     * @return
     */
    public final ReadIntegerArray getSkill_id(){
        return skill_id;
    }
    /**
     * 关联被动技能
     */
    private final ReadIntegerArray Passive_skill_id;
    /**
     * 关联被动技能
     * @return
     */
    public final ReadIntegerArray getPassive_skill_id(){
        return Passive_skill_id;
    }
    /**
     * 蜕变消耗（物品ID_数量）
     */
    private final ReadIntegerArray tuibian_item;
    /**
     * 蜕变消耗（物品ID_数量）
     * @return
     */
    public final ReadIntegerArray getTuibian_item(){
        return tuibian_item;
    }
    /**
     * 升级消耗（物品ID_数量）
     */
    private final ReadIntegerArray level_item;
    /**
     * 升级消耗（物品ID_数量）
     * @return
     */
    public final ReadIntegerArray getLevel_item(){
        return level_item;
    }
    /**
     * 获得时提升战斗力 client ignore
     */
    private final int fight_num;
    /**
     * 获得时提升战斗力 client ignore
     * @return
     */
    public final int getFight_num(){
        return fight_num;
    }

    public Cfg_Occ_Skill_Bean(int ID,int Mental_ID,int occ,int position,int if_tuibian,int Passive_level,String skill_idStr,String Passive_skill_idStr,String tuibian_itemStr,String level_itemStr,int fight_num){
        this.ID = ID;
        this.Mental_ID = Mental_ID;
        this.occ = occ;
        this.position = position;
        this.if_tuibian = if_tuibian;
        this.Passive_level = Passive_level;
        this.skill_id = new ReadIntegerArray(skill_idStr,",");
        this.Passive_skill_id = new ReadIntegerArray(Passive_skill_idStr,",");
        this.tuibian_item = new ReadIntegerArray(tuibian_itemStr,",");
        this.level_item = new ReadIntegerArray(level_itemStr,",");
        this.fight_num = fight_num;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("Mental_ID:").append(Mental_ID).append(";");
        str.append("occ:").append(occ).append(";");
        str.append("position:").append(position).append(";");
        str.append("if_tuibian:").append(if_tuibian).append(";");
        str.append("Passive_level:").append(Passive_level).append(";");
        str.append("skill_id:").append(skill_id).append(";");
        str.append("Passive_skill_id:").append(Passive_skill_id).append(";");
        str.append("tuibian_item:").append(tuibian_item).append(";");
        str.append("level_item:").append(level_item).append(";");
        str.append("fight_num:").append(fight_num).append(";");
        return str.toString();
    }
}
