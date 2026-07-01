/**
 * Auto generated, do not edit it
 *
 * Equip_holy_suit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_holy_suit_Bean{
    /**
     * 套装唯一ID
阶数*10000+品质*100+type字段
程度也是算的（不能修改ID规则）
     */
    private final int Id;
    /**
     * 套装唯一ID
阶数*10000+品质*100+type字段
程度也是算的（不能修改ID规则）
     * @return
     */
    public final int getId(){
        return Id;
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
     * 对应圣装阶数
     */
    private final int need_degree;
    /**
     * 对应圣装阶数
     * @return
     */
    public final int getNeed_degree(){
        return need_degree;
    }
    /**
     * 装备品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final int quality;
    /**
     * 装备品质(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 套装参考评分
     */
    private final int score;
    /**
     * 套装参考评分
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 1.衣服套装
2.首饰套装
1，2对应第一套圣装
3，4对应第二套圣装
5，6对应第三套圣装
     */
    private final int type;
    /**
     * 1.衣服套装
2.首饰套装
1，2对应第一套圣装
3，4对应第二套圣装
5，6对应第三套圣装
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 2件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_2;
    /**
     * 2件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_2(){
        return attribute_2;
    }
    /**
     * 2件套百分比属性提升(@;@_@)

     */
    private final ReadIntegerArrayEs elementAttribute_2;
    /**
     * 2件套百分比属性提升(@;@_@)

     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_2(){
        return elementAttribute_2;
    }
    /**
     * 4件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_4;
    /**
     * 4件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_4(){
        return attribute_4;
    }
    /**
     * 4件套百分比属性提升(@;@_@)

     */
    private final ReadIntegerArrayEs elementAttribute_4;
    /**
     * 4件套百分比属性提升(@;@_@)

     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_4(){
        return elementAttribute_4;
    }
    /**
     * 5件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_5;
    /**
     * 5件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_5(){
        return attribute_5;
    }
    /**
     * 5件套百分比属性提升(@;@_@)

     */
    private final ReadIntegerArrayEs elementAttribute_5;
    /**
     * 5件套百分比属性提升(@;@_@)

     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_5(){
        return elementAttribute_5;
    }
    /**
     * 6件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_6;
    /**
     * 6件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_6(){
        return attribute_6;
    }
    /**
     * 6件套百分比属性提升(@;@_@)

     */
    private final ReadIntegerArrayEs elementAttribute_6;
    /**
     * 6件套百分比属性提升(@;@_@)

     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_6(){
        return elementAttribute_6;
    }

    public Cfg_Equip_holy_suit_Bean(int Id,int level,int need_degree,int quality,int score,int type,String attribute_2Str,String elementAttribute_2Str,String attribute_4Str,String elementAttribute_4Str,String attribute_5Str,String elementAttribute_5Str,String attribute_6Str,String elementAttribute_6Str){
        this.Id = Id;
        this.level = level;
        this.need_degree = need_degree;
        this.quality = quality;
        this.score = score;
        this.type = type;
        this.attribute_2 = new ReadIntegerArrayEs(attribute_2Str,"}",",");
        this.elementAttribute_2 = new ReadIntegerArrayEs(elementAttribute_2Str,"}",",");
        this.attribute_4 = new ReadIntegerArrayEs(attribute_4Str,"}",",");
        this.elementAttribute_4 = new ReadIntegerArrayEs(elementAttribute_4Str,"}",",");
        this.attribute_5 = new ReadIntegerArrayEs(attribute_5Str,"}",",");
        this.elementAttribute_5 = new ReadIntegerArrayEs(elementAttribute_5Str,"}",",");
        this.attribute_6 = new ReadIntegerArrayEs(attribute_6Str,"}",",");
        this.elementAttribute_6 = new ReadIntegerArrayEs(elementAttribute_6Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("level:").append(level).append(";");
        str.append("need_degree:").append(need_degree).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("score:").append(score).append(";");
        str.append("type:").append(type).append(";");
        str.append("attribute_2:").append(attribute_2).append(";");
        str.append("elementAttribute_2:").append(elementAttribute_2).append(";");
        str.append("attribute_4:").append(attribute_4).append(";");
        str.append("elementAttribute_4:").append(elementAttribute_4).append(";");
        str.append("attribute_5:").append(attribute_5).append(";");
        str.append("elementAttribute_5:").append(elementAttribute_5).append(";");
        str.append("attribute_6:").append(attribute_6).append(";");
        str.append("elementAttribute_6:").append(elementAttribute_6).append(";");
        return str.toString();
    }
}
