/**
 * Auto generated, do not edit it
 *
 * state_stifle配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_State_stifle_Bean{
    /**
     * 等级*100+星级
     */
    private final int id;
    /**
     * 等级*100+星级
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 境界名字
     */
    private final String name;
    /**
     * 境界名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 星级
     */
    private final int star;
    /**
     * 星级
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 背景特效id
     */
    private final int back_vfx;
    /**
     * 背景特效id
     * @return
     */
    public final int getBack_vfx(){
        return back_vfx;
    }
    /**
     * 升级需要条件(@_@)
     */
    private final ReadIntegerArray condition;
    /**
     * 升级需要条件(@_@)
     * @return
     */
    public final ReadIntegerArray getCondition(){
        return condition;
    }
    /**
     * 升级需求物品(@_@)
     */
    private final ReadIntegerArray need_item;
    /**
     * 升级需求物品(@_@)
     * @return
     */
    public final ReadIntegerArray getNeed_item(){
        return need_item;
    }
    /**
     * 额外伤害
     */
    private final int ex_damage;
    /**
     * 额外伤害
     * @return
     */
    public final int getEx_damage(){
        return ex_damage;
    }
    /**
     * 额外伤害CD(单位毫秒)
     */
    private final int ex_damage_cd;
    /**
     * 额外伤害CD(单位毫秒)
     * @return
     */
    public final int getEx_damage_cd(){
        return ex_damage_cd;
    }
    /**
     * 额外属性
     */
    private final ReadIntegerArrayEs att;
    /**
     * 额外属性
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 免疫境界（0不免疫，非0表示小于等于的境界都免疫）
     */
    private final int exempt_state;
    /**
     * 免疫境界（0不免疫，非0表示小于等于的境界都免疫）
     * @return
     */
    public final int getExempt_state(){
        return exempt_state;
    }
    /**
     * 是否激活新外观的外观ID
     */
    private final int ModelID;
    /**
     * 是否激活新外观的外观ID
     * @return
     */
    public final int getModelID(){
        return ModelID;
    }

    public Cfg_State_stifle_Bean(int id,String name,int level,int star,int back_vfx,String conditionStr,String need_itemStr,int ex_damage,int ex_damage_cd,String attStr,int exempt_state,int ModelID){
        this.id = id;
        this.name = name;
        this.level = level;
        this.star = star;
        this.back_vfx = back_vfx;
        this.condition = new ReadIntegerArray(conditionStr,",");
        this.need_item = new ReadIntegerArray(need_itemStr,",");
        this.ex_damage = ex_damage;
        this.ex_damage_cd = ex_damage_cd;
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.exempt_state = exempt_state;
        this.ModelID = ModelID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("level:").append(level).append(";");
        str.append("star:").append(star).append(";");
        str.append("back_vfx:").append(back_vfx).append(";");
        str.append("condition:").append(condition).append(";");
        str.append("need_item:").append(need_item).append(";");
        str.append("ex_damage:").append(ex_damage).append(";");
        str.append("ex_damage_cd:").append(ex_damage_cd).append(";");
        str.append("att:").append(att).append(";");
        str.append("exempt_state:").append(exempt_state).append(";");
        str.append("ModelID:").append(ModelID).append(";");
        return str.toString();
    }
}
