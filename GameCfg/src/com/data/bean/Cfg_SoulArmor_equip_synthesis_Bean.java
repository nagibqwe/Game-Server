/**
 * Auto generated, do not edit it
 *
 * SoulArmor_equip_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_SoulArmor_equip_synthesis_Bean{
    /**
     * 身上穿的装备ID
     */
    private final int Id;
    /**
     * 身上穿的装备ID
     * @return
     */
    public final int getId(){
        return Id;
    }
    /**
     * 合成目标装备ID
     */
    private final int Equip_ID;
    /**
     * 合成目标装备ID
     * @return
     */
    public final int getEquip_ID(){
        return Equip_ID;
    }
    /**
     * 参与合成魂印等级
     */
    private final int synthesis_level;
    /**
     * 参与合成魂印等级
     * @return
     */
    public final int getSynthesis_level(){
        return synthesis_level;
    }
    /**
     * 放入装备的部位限制，不填表示不限制
     */
    private final ReadIntegerArray join_part;
    /**
     * 放入装备的部位限制，不填表示不限制
     * @return
     */
    public final ReadIntegerArray getJoin_part(){
        return join_part;
    }
    /**
     * 材料装备需求的职业id
     */
    private final int professional;
    /**
     * 材料装备需求的职业id
     * @return
     */
    public final int getProfessional(){
        return professional;
    }
    /**
     * 材料装备放入品质(1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     */
    private final ReadIntegerArray quality;
    /**
     * 材料装备放入品质(1.白 2.绿 3.蓝 4.紫 5.橙 6.金 7.红,8粉,9暗金.10幻彩)
     * @return
     */
    public final ReadIntegerArray getQuality(){
        return quality;
    }
    /**
     * 材料装备的概率（同阶装备基础概率_比本阶装备高的装备基础概率_低一阶装备基础概率_低两阶装备以下基础概率）
没有阶的概念，但计算概率需要用到
     */
    private final int join_num_probability;
    /**
     * 材料装备的概率（同阶装备基础概率_比本阶装备高的装备基础概率_低一阶装备基础概率_低两阶装备以下基础概率）
没有阶的概念，但计算概率需要用到
     * @return
     */
    public final int getJoin_num_probability(){
        return join_num_probability;
    }
    /**
     * 品质的概率翻倍系数
     */
    private final ReadIntegerArray quality_Number;
    /**
     * 品质的概率翻倍系数
     * @return
     */
    public final ReadIntegerArray getQuality_Number(){
        return quality_Number;
    }
    /**
     * 可以放入的星级的多少
     */
    private final ReadIntegerArray diamond;
    /**
     * 可以放入的星级的多少
     * @return
     */
    public final ReadIntegerArray getDiamond(){
        return diamond;
    }
    /**
     * 星级对应的概率翻倍系数
     */
    private final ReadIntegerArray diamond_Number;
    /**
     * 星级对应的概率翻倍系数
     * @return
     */
    public final ReadIntegerArray getDiamond_Number(){
        return diamond_Number;
    }
    /**
     * 材料装备是否能放入道具（数量:1以后为必放材料,道具ID_数量）(@_@)
     */
    private final ReadIntegerArray join_item;
    /**
     * 材料装备是否能放入道具（数量:1以后为必放材料,道具ID_数量）(@_@)
     * @return
     */
    public final ReadIntegerArray getJoin_item(){
        return join_item;
    }
    /**
     * 是否进行跑马灯公告(0或空不公告,1进行公告)
     */
    private final int notice;
    /**
     * 是否进行跑马灯公告(0或空不公告,1进行公告)
     * @return
     */
    public final int getNotice(){
        return notice;
    }

    public Cfg_SoulArmor_equip_synthesis_Bean(int Id,int Equip_ID,int synthesis_level,String join_partStr,int professional,String qualityStr,int join_num_probability,String quality_NumberStr,String diamondStr,String diamond_NumberStr,String join_itemStr,int notice){
        this.Id = Id;
        this.Equip_ID = Equip_ID;
        this.synthesis_level = synthesis_level;
        this.join_part = new ReadIntegerArray(join_partStr,",");
        this.professional = professional;
        this.quality = new ReadIntegerArray(qualityStr,",");
        this.join_num_probability = join_num_probability;
        this.quality_Number = new ReadIntegerArray(quality_NumberStr,",");
        this.diamond = new ReadIntegerArray(diamondStr,",");
        this.diamond_Number = new ReadIntegerArray(diamond_NumberStr,",");
        this.join_item = new ReadIntegerArray(join_itemStr,",");
        this.notice = notice;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("Id:").append(Id).append(";");
        str.append("Equip_ID:").append(Equip_ID).append(";");
        str.append("synthesis_level:").append(synthesis_level).append(";");
        str.append("join_part:").append(join_part).append(";");
        str.append("professional:").append(professional).append(";");
        str.append("quality:").append(quality).append(";");
        str.append("join_num_probability:").append(join_num_probability).append(";");
        str.append("quality_Number:").append(quality_Number).append(";");
        str.append("diamond:").append(diamond).append(";");
        str.append("diamond_Number:").append(diamond_Number).append(";");
        str.append("join_item:").append(join_item).append(";");
        str.append("notice:").append(notice).append(";");
        return str.toString();
    }
}
