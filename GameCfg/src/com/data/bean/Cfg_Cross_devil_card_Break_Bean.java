/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Break配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_devil_card_Break_Bean{
    /**
     * ID
=card*1000+Break_Level
     */
    private final int id;
    /**
     * ID
=card*1000+Break_Level
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 所属卡片
对应Cross_devil_card_Main表主键
     */
    private final int card;
    /**
     * 所属卡片
对应Cross_devil_card_Main表主键
     * @return
     */
    public final int getCard(){
        return card;
    }
    /**
     * 突破所需材料
配置必须为4个
     */
    private final ReadIntegerArrayEs condition;
    /**
     * 突破所需材料
配置必须为4个
     * @return
     */
    public final ReadIntegerArrayEs getCondition(){
        return condition;
    }
    /**
     * 突破激活技能
     */
    private final ReadIntegerArray skill;
    /**
     * 突破激活技能
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 级数
     */
    private final int Break_Level;
    /**
     * 级数
     * @return
     */
    public final int getBreak_Level(){
        return Break_Level;
    }
    /**
     * 属性类型对应attributeAdd表
     */
    private final ReadIntegerArrayEs att;
    /**
     * 属性类型对应attributeAdd表
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }

    public Cfg_Cross_devil_card_Break_Bean(int id,int card,String conditionStr,String skillStr,int Break_Level,String attStr){
        this.id = id;
        this.card = card;
        this.condition = new ReadIntegerArrayEs(conditionStr,"}",",");
        this.skill = new ReadIntegerArray(skillStr,",");
        this.Break_Level = Break_Level;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("card:").append(card).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("Break_Level:").append(Break_Level).append(";");
        str.append("att:").append(att).append(";");
        return str.toString();
    }
}
