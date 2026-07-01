/**
 * Auto generated, do not edit it
 *
 * Equip_xianjia_suit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_xianjia_suit_Bean{
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
     * 是第几套仙甲
     */
    private final int oder;
    /**
     * 是第几套仙甲
     * @return
     */
    public final int getOder(){
        return oder;
    }
    /**
     * 套装类型（0仙佩；1副装上装；2副装下装;3仙甲;4阴阳;5八卦）
     */
    private final int type;
    /**
     * 套装类型（0仙佩；1副装上装；2副装下装;3仙甲;4阴阳;5八卦）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 需要的部位（34仙甲左佩，35仙甲右佩，36仙甲头冠，37仙甲肩饰，38仙甲护腕，39仙甲手套，40仙甲内衬，41仙甲腰带，42仙甲裤子，43仙甲鞋履）
     */
    private final ReadIntegerArray part;
    /**
     * 需要的部位（34仙甲左佩，35仙甲右佩，36仙甲头冠，37仙甲肩饰，38仙甲护腕，39仙甲手套，40仙甲内衬，41仙甲腰带，42仙甲裤子，43仙甲鞋履）
     * @return
     */
    public final ReadIntegerArray getPart(){
        return part;
    }
    /**
     * 套装中最低的仙甲等级
     */
    private final int level;
    /**
     * 套装中最低的仙甲等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 属性
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 属性
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 4件套属性
     */
    private final ReadIntegerArrayEs attribute1;
    /**
     * 4件套属性
     * @return
     */
    public final ReadIntegerArrayEs getAttribute1(){
        return attribute1;
    }

    public Cfg_Equip_xianjia_suit_Bean(int Id,int oder,int type,String partStr,int level,String attributeStr,String attribute1Str){
        this.Id = Id;
        this.oder = oder;
        this.type = type;
        this.part = new ReadIntegerArray(partStr,",");
        this.level = level;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.attribute1 = new ReadIntegerArrayEs(attribute1Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("oder:").append(oder).append(";");
        str.append("type:").append(type).append(";");
        str.append("part:").append(part).append(";");
        str.append("level:").append(level).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("attribute1:").append(attribute1).append(";");
        return str.toString();
    }
}
