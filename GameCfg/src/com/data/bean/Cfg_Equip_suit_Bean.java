/**
 * Auto generated, do not edit it
 *
 * Equip_suit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_suit_Bean{
    /**
     * 套装唯一ID  套装等级*10000+阶数*100+顺序id(1~99)
     */
    private final int Id;
    /**
     * 套装唯一ID  套装等级*10000+阶数*100+顺序id(1~99)
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 套装前缀
     */
    private final String prefix;
    /**
     * 套装前缀
     * @return
     */
    public final String getPrefix(){
        return prefix;
    }
    /**
     * 套装名字
     */
    private final String name;
    /**
     * 套装名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 套装等级
     */
    private final int level;
    /**
     * 套装等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 需求装备阶数(@_@)
     */
    private final ReadIntegerArray need_degree;
    /**
     * 需求装备阶数(@_@)
     * @return
     */
    public final ReadIntegerArray getNeed_degree(){
        return need_degree;
    }
    /**
     * 需求的品质
     */
    private final int need_quality;
    /**
     * 需求的品质
     * @return
     */
    public final int getNeed_quality(){
        return need_quality;
    }
    /**
     * 需求钻石数量
     */
    private final int need_diamonds;
    /**
     * 需求钻石数量
     * @return
     */
    public final int getNeed_diamonds(){
        return need_diamonds;
    }
    /**
     * 职业限制
0-执笔者；1-拳师；2-大锤；3-太刀；4-卡牌；5-枪手(@_@)
     */
    private final ReadIntegerArray need_gender;
    /**
     * 职业限制
0-执笔者；1-拳师；2-大锤；3-太刀；4-卡牌；5-枪手(@_@)
     * @return
     */
    public final ReadIntegerArray getNeed_gender(){
        return need_gender;
    }
    /**
     * 需求部位(@_@)
     */
    private final ReadIntegerArray need_parts;
    /**
     * 需求部位(@_@)
     * @return
     */
    public final ReadIntegerArray getNeed_parts(){
        return need_parts;
    }
    /**
     * 锻造所需材料（部位_ID_num）(@;@_@)
     */
    private final ReadIntegerArrayEs need_items;
    /**
     * 锻造所需材料（部位_ID_num）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getNeed_items(){
        return need_items;
    }
    /**
     * 套装属性1(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_1;
    /**
     * 套装属性1(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_1(){
        return attribute_1;
    }
    /**
     * 套装属性2(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_2;
    /**
     * 套装属性2(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_2(){
        return attribute_2;
    }
    /**
     * 套装属性4(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_4;
    /**
     * 套装属性4(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_4(){
        return attribute_4;
    }
    /**
     * 套装属性6(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_6;
    /**
     * 套装属性6(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_6(){
        return attribute_6;
    }
    /**
     * 父类ID，填写神怒套上层天怒套ID用于套装石返还
     */
    private final int parent_ID;
    /**
     * 父类ID，填写神怒套上层天怒套ID用于套装石返还
     * @return
     */
    public final int getParent_ID(){
        return parent_ID;
    }

    public Cfg_Equip_suit_Bean(int Id,String prefix,String name,int level,String need_degreeStr,int need_quality,int need_diamonds,String need_genderStr,String need_partsStr,String need_itemsStr,String attribute_1Str,String attribute_2Str,String attribute_4Str,String attribute_6Str,int parent_ID){
        this.Id = Id;
        this.prefix = prefix;
        this.name = name;
        this.level = level;
        this.need_degree = new ReadIntegerArray(need_degreeStr,",");
        this.need_quality = need_quality;
        this.need_diamonds = need_diamonds;
        this.need_gender = new ReadIntegerArray(need_genderStr,",");
        this.need_parts = new ReadIntegerArray(need_partsStr,",");
        this.need_items = new ReadIntegerArrayEs(need_itemsStr,"}",",");
        this.attribute_1 = new ReadIntegerArrayEs(attribute_1Str,"}",",");
        this.attribute_2 = new ReadIntegerArrayEs(attribute_2Str,"}",",");
        this.attribute_4 = new ReadIntegerArrayEs(attribute_4Str,"}",",");
        this.attribute_6 = new ReadIntegerArrayEs(attribute_6Str,"}",",");
        this.parent_ID = parent_ID;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("prefix:").append(prefix).append(";");
        str.append("name:").append(name).append(";");
        str.append("level:").append(level).append(";");
        str.append("need_degree:").append(need_degree).append(";");
        str.append("need_quality:").append(need_quality).append(";");
        str.append("need_diamonds:").append(need_diamonds).append(";");
        str.append("need_gender:").append(need_gender).append(";");
        str.append("need_parts:").append(need_parts).append(";");
        str.append("need_items:").append(need_items).append(";");
        str.append("attribute_1:").append(attribute_1).append(";");
        str.append("attribute_2:").append(attribute_2).append(";");
        str.append("attribute_4:").append(attribute_4).append(";");
        str.append("attribute_6:").append(attribute_6).append(";");
        str.append("parent_ID:").append(parent_ID).append(";");
        return str.toString();
    }
}
