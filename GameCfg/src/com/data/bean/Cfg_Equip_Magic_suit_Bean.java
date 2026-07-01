/**
 * Auto generated, do not edit it
 *
 * Equip_Magic_suit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Magic_suit_Bean{
    /**
     * 套装唯一ID
     */
    private final int Id;
    /**
     * 套装唯一ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 对应幻装阶数
     */
    private final int need_degree;
    /**
     * 对应幻装阶数
     * @return
     */
    public final int getNeed_degree(){
        return need_degree;
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
    /**
     * 8件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_8;
    /**
     * 8件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_8(){
        return attribute_8;
    }
    /**
     * 8件套百分比属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs elementAttribute_8;
    /**
     * 8件套百分比属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_8(){
        return elementAttribute_8;
    }
    /**
     * 10件套基础属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs attribute_10;
    /**
     * 10件套基础属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_10(){
        return attribute_10;
    }
    /**
     * 10件套百分比属性提升(@;@_@)
     */
    private final ReadIntegerArrayEs elementAttribute_10;
    /**
     * 10件套百分比属性提升(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getElementAttribute_10(){
        return elementAttribute_10;
    }

    public Cfg_Equip_Magic_suit_Bean(int Id,int need_degree,int score,String attribute_2Str,String elementAttribute_2Str,String attribute_4Str,String elementAttribute_4Str,String attribute_6Str,String elementAttribute_6Str,String attribute_8Str,String elementAttribute_8Str,String attribute_10Str,String elementAttribute_10Str){
        this.Id = Id;
        this.need_degree = need_degree;
        this.score = score;
        this.attribute_2 = new ReadIntegerArrayEs(attribute_2Str,"}",",");
        this.elementAttribute_2 = new ReadIntegerArrayEs(elementAttribute_2Str,"}",",");
        this.attribute_4 = new ReadIntegerArrayEs(attribute_4Str,"}",",");
        this.elementAttribute_4 = new ReadIntegerArrayEs(elementAttribute_4Str,"}",",");
        this.attribute_6 = new ReadIntegerArrayEs(attribute_6Str,"}",",");
        this.elementAttribute_6 = new ReadIntegerArrayEs(elementAttribute_6Str,"}",",");
        this.attribute_8 = new ReadIntegerArrayEs(attribute_8Str,"}",",");
        this.elementAttribute_8 = new ReadIntegerArrayEs(elementAttribute_8Str,"}",",");
        this.attribute_10 = new ReadIntegerArrayEs(attribute_10Str,"}",",");
        this.elementAttribute_10 = new ReadIntegerArrayEs(elementAttribute_10Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("need_degree:").append(need_degree).append(";");
        str.append("score:").append(score).append(";");
        str.append("attribute_2:").append(attribute_2).append(";");
        str.append("elementAttribute_2:").append(elementAttribute_2).append(";");
        str.append("attribute_4:").append(attribute_4).append(";");
        str.append("elementAttribute_4:").append(elementAttribute_4).append(";");
        str.append("attribute_6:").append(attribute_6).append(";");
        str.append("elementAttribute_6:").append(elementAttribute_6).append(";");
        str.append("attribute_8:").append(attribute_8).append(";");
        str.append("elementAttribute_8:").append(elementAttribute_8).append(";");
        str.append("attribute_10:").append(attribute_10).append(";");
        str.append("elementAttribute_10:").append(elementAttribute_10).append(";");
        return str.toString();
    }
}
