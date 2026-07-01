/**
 * Auto generated, do not edit it
 *
 * Cross_devil_card_Synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Cross_devil_card_Synthesis_Bean{
    /**
     * ID
camp*1000+quality
     */
    private final int id;
    /**
     * ID
camp*1000+quality
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 所属阵营
对应Cross_devil_card_Camp表主键
     */
    private final int camp;
    /**
     * 所属阵营
对应Cross_devil_card_Camp表主键
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 所需装备品质
(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final int quality;
    /**
     * 所需装备品质
(：1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final int getQuality(){
        return quality;
    }
    /**
     * 目标装备ID组
     */
    private final ReadIntegerArray equip;
    /**
     * 目标装备ID组
     * @return
     */
    public final ReadIntegerArray getEquip(){
        return equip;
    }

    public Cfg_Cross_devil_card_Synthesis_Bean(int id,int camp,int quality,String equipStr){
        this.id = id;
        this.camp = camp;
        this.quality = quality;
        this.equip = new ReadIntegerArray(equipStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("equip:").append(equip).append(";");
        return str.toString();
    }
}
