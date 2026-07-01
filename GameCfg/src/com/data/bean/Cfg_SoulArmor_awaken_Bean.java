/**
 * Auto generated, do not edit it
 *
 * SoulArmor_awaken配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_awaken_Bean{
    /**
     * 流水id
     */
    private final int id;
    /**
     * 流水id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 觉醒等级
     */
    private final int level;
    /**
     * 觉醒等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 升级消耗 道具id_数量
     */
    private final ReadIntegerArrayEs consume;
    /**
     * 升级消耗 道具id_数量
     * @return
     */
    public final ReadIntegerArrayEs getConsume(){
        return consume;
    }
    /**
     * 觉醒等级属性
     */
    private final ReadIntegerArrayEs levelValue;
    /**
     * 觉醒等级属性
     * @return
     */
    public final ReadIntegerArrayEs getLevelValue(){
        return levelValue;
    }
    /**
     * 觉醒额外解锁属性
(激活技能需先获得三条属性）
     */
    private final ReadIntegerArrayEs extraValue;
    /**
     * 觉醒额外解锁属性
(激活技能需先获得三条属性）
     * @return
     */
    public final ReadIntegerArrayEs getExtraValue(){
        return extraValue;
    }
    /**
     * 解锁技能目标id
     */
    private final int skill;
    /**
     * 解锁技能目标id
     * @return
     */
    public final int getSkill(){
        return skill;
    }
    /**
     * 升到本级是否解锁技能
     */
    private final int judgeOpenSkill;
    /**
     * 升到本级是否解锁技能
     * @return
     */
    public final int getJudgeOpenSkill(){
        return judgeOpenSkill;
    }

    public Cfg_SoulArmor_awaken_Bean(int id,int level,String consumeStr,String levelValueStr,String extraValueStr,int skill,int judgeOpenSkill){
        this.id = id;
        this.level = level;
        this.consume = new ReadIntegerArrayEs(consumeStr,"}",",");
        this.levelValue = new ReadIntegerArrayEs(levelValueStr,"}",",");
        this.extraValue = new ReadIntegerArrayEs(extraValueStr,"}",",");
        this.skill = skill;
        this.judgeOpenSkill = judgeOpenSkill;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("level:").append(level).append(";");
        str.append("consume:").append(consume).append(";");
        str.append("levelValue:").append(levelValue).append(";");
        str.append("extraValue:").append(extraValue).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("judgeOpenSkill:").append(judgeOpenSkill).append(";");
        return str.toString();
    }
}
