/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_suit配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_signet_suit_Bean{
    /**
     * 套装id
     */
    private final int id;
    /**
     * 套装id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 部位需求
     */
    private final ReadIntegerArrayEs part;
    /**
     * 部位需求
     * @return
     */
    public final ReadIntegerArrayEs getPart(){
        return part;
    }
    /**
     * 最低星级需求
（≥该星都可激活）
     */
    private final int star;
    /**
     * 最低星级需求
（≥该星都可激活）
     * @return
     */
    public final int getStar(){
        return star;
    }
    /**
     * 最低品质要求（3.蓝 4.紫6.金 7.红,8粉9暗金.10幻彩）
     */
    private final int quality;
    /**
     * 最低品质要求（3.蓝 4.紫6.金 7.红,8粉9暗金.10幻彩）
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 套装预览默认装备
     */
    private final ReadIntegerArrayEs suit;
    /**
     * 套装预览默认装备
     * @return
     */
    public final ReadIntegerArrayEs getSuit(){
        return suit;
    }
    /**
     * 组合名称
     */
    private final String name;
    /**
     * 组合名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 2件增加属性
     */
    private final ReadIntegerArrayEs valueOf2;
    /**
     * 2件增加属性
     * @return
     */
    public final ReadIntegerArrayEs getValueOf2(){
        return valueOf2;
    }
    /**
     * 3件增加属性
     */
    private final ReadIntegerArrayEs valueOf3;
    /**
     * 3件增加属性
     * @return
     */
    public final ReadIntegerArrayEs getValueOf3(){
        return valueOf3;
    }
    /**
     * 4件增加属性
     */
    private final ReadIntegerArrayEs valueOf4;
    /**
     * 4件增加属性
     * @return
     */
    public final ReadIntegerArrayEs getValueOf4(){
        return valueOf4;
    }
    /**
     * 6件增加属性
     */
    private final ReadIntegerArrayEs valueOf6;
    /**
     * 6件增加属性
     * @return
     */
    public final ReadIntegerArrayEs getValueOf6(){
        return valueOf6;
    }

    public Cfg_SoulArmor_signet_suit_Bean(int id,String partStr,int star,int quality,String suitStr,String name,String valueOf2Str,String valueOf3Str,String valueOf4Str,String valueOf6Str){
        this.id = id;
        this.part = new ReadIntegerArrayEs(partStr,"}",",");
        this.star = star;
        this.quality = quality;
        this.suit = new ReadIntegerArrayEs(suitStr,"}",",");
        this.name = name;
        this.valueOf2 = new ReadIntegerArrayEs(valueOf2Str,"}",",");
        this.valueOf3 = new ReadIntegerArrayEs(valueOf3Str,"}",",");
        this.valueOf4 = new ReadIntegerArrayEs(valueOf4Str,"}",",");
        this.valueOf6 = new ReadIntegerArrayEs(valueOf6Str,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("part:").append(part).append(";");
        str.append("star:").append(star).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("suit:").append(suit).append(";");
        str.append("name:").append(name).append(";");
        str.append("valueOf2:").append(valueOf2).append(";");
        str.append("valueOf3:").append(valueOf3).append(";");
        str.append("valueOf4:").append(valueOf4).append(";");
        str.append("valueOf6:").append(valueOf6).append(";");
        return str.toString();
    }
}
