/**
 * Auto generated, do not edit it
 *
 * FirstRecharge配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_FirstRecharge_Bean{
    /**
     * 奖励ID
     */
    private final int id;
    /**
     * 奖励ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 需要充值元宝
     */
    private final int needRecharge;
    /**
     * 需要充值元宝
     * @return
     */
    public final int getNeedRecharge(){
        return needRecharge;
    }
    /**
     * 装备奖励（ID_数量_是否绑定（0否1是）_职业），没有不填(@;@_@)
     */
    private final ReadIntegerArrayEs equipAward;
    /**
     * 装备奖励（ID_数量_是否绑定（0否1是）_职业），没有不填(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getEquipAward(){
        return equipAward;
    }
    /**
     * 物品奖励ID_数量_是否绑定（0否1是）(@;@_@)
     */
    private final ReadIntegerArrayEs itemAward;
    /**
     * 物品奖励ID_数量_是否绑定（0否1是）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getItemAward(){
        return itemAward;
    }
    /**
     * 是否公告（0不公告；1公告）
     */
    private final int radio;
    /**
     * 是否公告（0不公告；1公告）
     * @return
     */
    public final int getRadio(){
        return radio;
    }
    /**
     * 背景图片ID
     */
    private final int textureId;
    /**
     * 背景图片ID
     * @return
     */
    public final int getTextureId(){
        return textureId;
    }
    /**
     * 展示资源ID
     */
    private final int modleId;
    /**
     * 展示资源ID
     * @return
     */
    public final int getModleId(){
        return modleId;
    }

    public Cfg_FirstRecharge_Bean(int id,int needRecharge,String equipAwardStr,String itemAwardStr,int radio,int textureId,int modleId){
        this.id = id;
        this.needRecharge = needRecharge;
        this.equipAward = new ReadIntegerArrayEs(equipAwardStr,"}",",");
        this.itemAward = new ReadIntegerArrayEs(itemAwardStr,"}",",");
        this.radio = radio;
        this.textureId = textureId;
        this.modleId = modleId;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("needRecharge:").append(needRecharge).append(";");
        str.append("equipAward:").append(equipAward).append(";");
        str.append("itemAward:").append(itemAward).append(";");
        str.append("radio:").append(radio).append(";");
        str.append("textureId:").append(textureId).append(";");
        str.append("modleId:").append(modleId).append(";");
        return str.toString();
    }
}
