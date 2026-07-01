/**
 * Auto generated, do not edit it
 *
 * Equip_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_synthesis_Bean{
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
     * 参与合成等级
     */
    private final int synthesis_level;
    /**
     * 参与合成等级
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
     * 材料装备的概率（同阶装备基础概率_比本阶装备高的装备基础概率_低一阶装备基础概率_低两阶装备以下基础概率）
     */
    private final ReadIntegerArray join_num_probability;
    /**
     * 材料装备的概率（同阶装备基础概率_比本阶装备高的装备基础概率_低一阶装备基础概率_低两阶装备以下基础概率）
     * @return
     */
    public final ReadIntegerArray getJoin_num_probability(){
        return join_num_probability;
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
     * 单个道具的货币id_数量(填写后道具不足时会自动扣除对应数量的货币,假如不填写就不会自动扣除)
     */
    private final ReadIntegerArray item_Price;
    /**
     * 单个道具的货币id_数量(填写后道具不足时会自动扣除对应数量的货币,假如不填写就不会自动扣除)
     * @return
     */
    public final ReadIntegerArray getItem_Price(){
        return item_Price;
    }
    /**
     * 特殊装备材料1所有装备ID(1~99阶)(@_@)
     */
    private final ReadIntegerArray join_EquipID1;
    /**
     * 特殊装备材料1所有装备ID(1~99阶)(@_@)
     * @return
     */
    public final ReadIntegerArray getJoin_EquipID1(){
        return join_EquipID1;
    }
    /**
     * 材料1放入数量(最小_最大)(@_@)
     */
    private final ReadIntegerArray join_num1;
    /**
     * 材料1放入数量(最小_最大)(@_@)
     * @return
     */
    public final ReadIntegerArray getJoin_num1(){
        return join_num1;
    }
    /**
     * 特殊装备材料2所有装备ID(1~99阶)(@_@)
     */
    private final ReadIntegerArray join_EquipID2;
    /**
     * 特殊装备材料2所有装备ID(1~99阶)(@_@)
     * @return
     */
    public final ReadIntegerArray getJoin_EquipID2(){
        return join_EquipID2;
    }
    /**
     * 材料2放入数量(最小_最大)(@_@)
     */
    private final ReadIntegerArray join_num2;
    /**
     * 材料2放入数量(最小_最大)(@_@)
     * @return
     */
    public final ReadIntegerArray getJoin_num2(){
        return join_num2;
    }
    /**
     * 身上装备失败不扣除,其他失败后返还的道具id和数量
     */
    private final ReadIntegerArray failure_type;
    /**
     * 身上装备失败不扣除,其他失败后返还的道具id和数量
     * @return
     */
    public final ReadIntegerArray getFailure_type(){
        return failure_type;
    }
    /**
     * 进行跑马灯公告(10为跑马灯)
     */
    private final int notice;
    /**
     * 进行跑马灯公告(10为跑马灯)
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 掉落物品后聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Equip_synthesis_Bean(int Id,int Equip_ID,int synthesis_level,String join_partStr,int professional,String qualityStr,String quality_NumberStr,String join_num_probabilityStr,String diamondStr,String diamond_NumberStr,String join_itemStr,String item_PriceStr,String join_EquipID1Str,String join_num1Str,String join_EquipID2Str,String join_num2Str,String failure_typeStr,int notice,String chatchannelStr){
        this.Id = Id;
        this.Equip_ID = Equip_ID;
        this.synthesis_level = synthesis_level;
        this.join_part = new ReadIntegerArray(join_partStr,",");
        this.professional = professional;
        this.quality = new ReadIntegerArray(qualityStr,",");
        this.quality_Number = new ReadIntegerArray(quality_NumberStr,",");
        this.join_num_probability = new ReadIntegerArray(join_num_probabilityStr,",");
        this.diamond = new ReadIntegerArray(diamondStr,",");
        this.diamond_Number = new ReadIntegerArray(diamond_NumberStr,",");
        this.join_item = new ReadIntegerArray(join_itemStr,",");
        this.item_Price = new ReadIntegerArray(item_PriceStr,",");
        this.join_EquipID1 = new ReadIntegerArray(join_EquipID1Str,",");
        this.join_num1 = new ReadIntegerArray(join_num1Str,",");
        this.join_EquipID2 = new ReadIntegerArray(join_EquipID2Str,",");
        this.join_num2 = new ReadIntegerArray(join_num2Str,",");
        this.failure_type = new ReadIntegerArray(failure_typeStr,",");
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
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
        str.append("quality_Number:").append(quality_Number).append(";");
        str.append("join_num_probability:").append(join_num_probability).append(";");
        str.append("diamond:").append(diamond).append(";");
        str.append("diamond_Number:").append(diamond_Number).append(";");
        str.append("join_item:").append(join_item).append(";");
        str.append("item_Price:").append(item_Price).append(";");
        str.append("join_EquipID1:").append(join_EquipID1).append(";");
        str.append("join_num1:").append(join_num1).append(";");
        str.append("join_EquipID2:").append(join_EquipID2).append(";");
        str.append("join_num2:").append(join_num2).append(";");
        str.append("failure_type:").append(failure_type).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
