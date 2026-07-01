/**
 * Auto generated, do not edit it
 *
 * ConvoyGirl配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_ConvoyGirl_Bean{
    /**
     * 神女id
     */
    private final int id;
    /**
     * 神女id
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 神女名字
     */
    private final String Name;
    /**
     * 神女名字
     * @return
     */
    public final String getName(){
        return Name;
    }
    /**
     * 神女使用头像
     */
    private final int Icon;
    /**
     * 神女使用头像
     * @return
     */
    public final int getIcon(){
        return Icon;
    }
    /**
     * 物品奖励
物品id1_数量_绑定_职业
职业：0玄剑、1天英、2地藏、3罗刹、9通用
绑定：0绑定、1不绑定
     */
    private final ReadIntegerArrayEs Reward_ID;
    /**
     * 物品奖励
物品id1_数量_绑定_职业
职业：0玄剑、1天英、2地藏、3罗刹、9通用
绑定：0绑定、1不绑定
     * @return
     */
    public final ReadIntegerArrayEs getReward_ID(){
        return Reward_ID;
    }
    /**
     * 护送时间(单位：s)
     */
    private final int ConvoyTime;
    /**
     * 护送时间(单位：s)
     * @return
     */
    public final int getConvoyTime(){
        return ConvoyTime;
    }
    /**
     * 物品ID_需要数量
     */
    private final ReadIntegerArray UseItem;
    /**
     * 物品ID_需要数量
     * @return
     */
    public final ReadIntegerArray getUseItem(){
        return UseItem;
    }
    /**
     * 选择神女时使用的背景板
     */
    private final String ChooseBackground;
    /**
     * 选择神女时使用的背景板
     * @return
     */
    public final String getChooseBackground(){
        return ChooseBackground;
    }

    public Cfg_ConvoyGirl_Bean(int id,String Name,int Icon,String Reward_IDStr,int ConvoyTime,String UseItemStr,String ChooseBackground){
        this.id = id;
        this.Name = Name;
        this.Icon = Icon;
        this.Reward_ID = new ReadIntegerArrayEs(Reward_IDStr,"}",",");
        this.ConvoyTime = ConvoyTime;
        this.UseItem = new ReadIntegerArray(UseItemStr,",");
        this.ChooseBackground = ChooseBackground;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("Name:").append(Name).append(";");
        str.append("Icon:").append(Icon).append(";");
        str.append("Reward_ID:").append(Reward_ID).append(";");
        str.append("ConvoyTime:").append(ConvoyTime).append(";");
        str.append("UseItem:").append(UseItem).append(";");
        str.append("ChooseBackground:").append(ChooseBackground).append(";");
        return str.toString();
    }
}
