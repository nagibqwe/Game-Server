/**
 * Auto generated, do not edit it
 *
 * Equip_Collection_star配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Equip_Collection_star_Bean{
    /**
     * ID（灵星星级）
     */
    private final int Id;
    /**
     * ID（灵星星级）
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 需要灵体装备的星级总数
     */
    private final int StarNum;
    /**
     * 需要灵体装备的星级总数
     * @return
     */
    public final int getStarNum(){
        return StarNum;
    }
    /**
     * 本级累计属性(@;@_@)
     */
    private final ReadIntegerArrayEs attribute;
    /**
     * 本级累计属性(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getAttribute(){
        return attribute;
    }
    /**
     * 本级累计百分比属性(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs attribute_pre;
    /**
     * 本级累计百分比属性(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getAttribute_pre(){
        return attribute_pre;
    }
    /**
     * 对应的ICON（填0为默认的小图标，填其他为对应的icon）
     */
    private final int icon;
    /**
     * 对应的ICON（填0为默认的小图标，填其他为对应的icon）
     * @return
     */
    public final int getIcon(){
        return icon;
    }

    public Cfg_Equip_Collection_star_Bean(int Id,int StarNum,String attributeStr,String attribute_preStr,int icon){
        this.Id = Id;
        this.StarNum = StarNum;
        this.attribute = new ReadIntegerArrayEs(attributeStr,"}",",");
        this.attribute_pre = new ReadIntegerArrayEs(attribute_preStr,"}",",");
        this.icon = icon;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("StarNum:").append(StarNum).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("attribute_pre:").append(attribute_pre).append(";");
        str.append("icon:").append(icon).append(";");
        return str.toString();
    }
}
