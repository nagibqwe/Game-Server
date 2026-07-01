/**
 * Auto generated, do not edit it
 *
 * Equip_shenpin_synthesis配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Equip_shenpin_synthesis_Bean{
    /**
     * ID（类型*10000000+身上的装备）
     */
    private final int ID;
    /**
     * ID（类型*10000000+身上的装备）
     * @return
     */
    public final int getID(){
        return ID;
    }
    /**
     * 合成类型（1.升星；2.升阶）
     */
    private final int synthesis_type;
    /**
     * 合成类型（1.升星；2.升阶）
     * @return
     */
    public final int getSynthesis_type(){
        return synthesis_type;
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
     * 身上穿的装备ID
     */
    private final int Equip_ID;
    /**
     * 身上穿的装备ID
     * @return
     */
    public final int getEquip_ID(){
        return Equip_ID;
    }
    /**
     * 合成需要的材料_数量
     */
    private final ReadIntegerArray join_item;
    /**
     * 合成需要的材料_数量
     * @return
     */
    public final ReadIntegerArray getJoin_item(){
        return join_item;
    }
    /**
     * 合成目标装备ID
     */
    private final int Target_Equip_ID;
    /**
     * 合成目标装备ID
     * @return
     */
    public final int getTarget_Equip_ID(){
        return Target_Equip_ID;
    }
    /**
     * 公告类型（10跑马灯）
     */
    private final int notice;
    /**
     * 公告类型（10跑马灯）
     * @return
     */
    public final int getNotice(){
        return notice;
    }
    /**
     * 聊天发送(0世界4系统14传闻）
     */
    private final ReadIntegerArray chatchannel;
    /**
     * 聊天发送(0世界4系统14传闻）
     * @return
     */
    public final ReadIntegerArray getChatchannel(){
        return chatchannel;
    }

    public Cfg_Equip_shenpin_synthesis_Bean(int ID,int synthesis_type,int synthesis_level,int Equip_ID,String join_itemStr,int Target_Equip_ID,int notice,String chatchannelStr){
        this.ID = ID;
        this.synthesis_type = synthesis_type;
        this.synthesis_level = synthesis_level;
        this.Equip_ID = Equip_ID;
        this.join_item = new ReadIntegerArray(join_itemStr,",");
        this.Target_Equip_ID = Target_Equip_ID;
        this.notice = notice;
        this.chatchannel = new ReadIntegerArray(chatchannelStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("ID:").append(ID).append(";");
        str.append("synthesis_type:").append(synthesis_type).append(";");
        str.append("synthesis_level:").append(synthesis_level).append(";");
        str.append("Equip_ID:").append(Equip_ID).append(";");
        str.append("join_item:").append(join_item).append(";");
        str.append("Target_Equip_ID:").append(Target_Equip_ID).append(";");
        str.append("notice:").append(notice).append(";");
        str.append("chatchannel:").append(chatchannel).append(";");
        return str.toString();
    }
}
