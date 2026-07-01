/**
 * Auto generated, do not edit it
 *
 * pet_rank配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Pet_rank_Bean{
    /**
     * 流水号 id*1000+阶数
     */
    private final int Id;
    /**
     * 流水号 id*1000+阶数
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 阶数
     */
    private final int level;
    /**
     * 阶数
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 宠物ID
     */
    private final int pet_id;
    /**
     * 宠物ID
     * @return
     */
    public final int getPet_id(){
        return pet_id;
    }
    /**
     * 消耗资源：资源ID_数量(@_@)
     */
    private final ReadIntegerArray rank_exp;
    /**
     * 消耗资源：资源ID_数量(@_@)
     * @return
     */
    public final ReadIntegerArray getRank_exp(){
        return rank_exp;
    }
    /**
     * 阶数附带辅助技能：技能位_技能ID;技能位_技能ID(@;@_@)
     */
    private final ReadIntegerArrayEs pet_skill;
    /**
     * 阶数附带辅助技能：技能位_技能ID;技能位_技能ID(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getPet_skill(){
        return pet_skill;
    }
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性：属性类型_数值，属性类型1_数值，(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }

    public Cfg_Pet_rank_Bean(int Id,int level,int pet_id,String rank_expStr,String pet_skillStr,String attributeStr){
        this.Id = Id;
        this.level = level;
        this.pet_id = pet_id;
        this.rank_exp = new ReadIntegerArray(rank_expStr,",");
        this.pet_skill = new ReadIntegerArrayEs(pet_skillStr,"}",",");
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("level:").append(level).append(";");
        str.append("pet_id:").append(pet_id).append(";");
        str.append("rank_exp:").append(rank_exp).append(";");
        str.append("pet_skill:").append(pet_skill).append(";");
        str.append("attribute:").append(attribute).append(";");
        return str.toString();
    }
}
