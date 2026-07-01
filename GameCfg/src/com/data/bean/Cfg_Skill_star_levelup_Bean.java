/**
 * Auto generated, do not edit it
 *
 * skill_star_levelup配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Skill_star_levelup_Bean{
    /**
     * ID[(职业+1)*1000000+顺序*1000+星级]
     */
    private final int id;
    /**
     * ID[(职业+1)*1000000+顺序*1000+星级]
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 职业 client ignore
     */
    private final int occ;
    /**
     * 职业 client ignore
     * @return
     */
    public final int getOcc(){
        return occ;
    }
    /**
     * 顺序client ignore
     */
    private final int oder;
    /**
     * 顺序client ignore
     * @return
     */
    public final int getOder(){
        return oder;
    }
    /**
     * 星级（升星到的星级）client ignore
     */
    private final int star;
    /**
     * 星级（升星到的星级）client ignore
     * @return
     */
    public final int getStar(){
        return star;
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
     * 升级所需的物品ID_数量
     */
    private final ReadIntegerArray need_item;
    /**
     * 升级所需的物品ID_数量
     * @return
     */
    public final ReadIntegerArray getNeed_item(){
        return need_item;
    }
    /**
     * 增加的属性
     */
    private final ReadIntegerArrayEs att;
    /**
     * 增加的属性
     * @return
     */
    public final ReadIntegerArrayEs getAtt(){
        return att;
    }
    /**
     * 激活等级
     */
    private final int active_level;
    /**
     * 激活等级
     * @return
     */
    public final int getActive_level(){
        return active_level;
    }
    /**
     * 重置天赋退还的物品ID_数量
     */
    private final ReadIntegerArray return_item;
    /**
     * 重置天赋退还的物品ID_数量
     * @return
     */
    public final ReadIntegerArray getReturn_item(){
        return return_item;
    }

    public Cfg_Skill_star_levelup_Bean(int id,int occ,int oder,int star,String skill_idStr,String need_itemStr,String attStr,int active_level,String return_itemStr){
        this.id = id;
        this.occ = occ;
        this.oder = oder;
        this.star = star;
        this.skill_id = new ReadIntegerArray(skill_idStr,",");
        this.need_item = new ReadIntegerArray(need_itemStr,",");
        this.att = new ReadIntegerArrayEs(attStr,"}",",");
        this.active_level = active_level;
        this.return_item = new ReadIntegerArray(return_itemStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("occ:").append(occ).append(";");
        str.append("oder:").append(oder).append(";");
        str.append("star:").append(star).append(";");
        str.append("skill_id:").append(skill_id).append(";");
        str.append("need_item:").append(need_item).append(";");
        str.append("att:").append(att).append(";");
        str.append("active_level:").append(active_level).append(";");
        str.append("return_item:").append(return_item).append(";");
        return str.toString();
    }
}
