/**
 * Auto generated, do not edit it
 *
 * change_model配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Change_model_Bean{
    /**
     * 对应BUFF表的主键ID
     */
    private final int id;
    /**
     * 对应BUFF表的主键ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应modelconfig主键ID
     */
    private final int modelId;
    /**
     * 对应modelconfig主键ID
     * @return
     */
    public final int getModelId(){
        return modelId;
    }
    /**
     * 技能，对应skill表主键（占用原有主角技能栏，可能有多个技能，，第一个技能为普通攻击其中一个必须是变身回角色的技能，无需配置，程序写死
     */
    private final ReadIntegerArray skill;
    /**
     * 技能，对应skill表主键（占用原有主角技能栏，可能有多个技能，，第一个技能为普通攻击其中一个必须是变身回角色的技能，无需配置，程序写死
     * @return
     */
    public final ReadIntegerArray getSkill(){
        return skill;
    }
    /**
     * 原有属性的倍数（填写BUFFID）
     */
    private final int attribute;
    /**
     * 原有属性的倍数（填写BUFFID）
     * @return
     */
    public final int getAttribute(){
        return attribute;
    }
    /**
     * 是否可攻击玩家
（0否1是）
     */
    private final int isAttRole;
    /**
     * 是否可攻击玩家
（0否1是）
     * @return
     */
    public final int getIsAttRole(){
        return isAttRole;
    }
    /**
     * 是否可被玩家攻击
（0否1是）
     */
    private final int isRoleAtt;
    /**
     * 是否可被玩家攻击
（0否1是）
     * @return
     */
    public final int getIsRoleAtt(){
        return isRoleAtt;
    }
    /**
     * 是否可攻击其他载具
（0否1是）
     */
    private final int isAttChange;
    /**
     * 是否可攻击其他载具
（0否1是）
     * @return
     */
    public final int getIsAttChange(){
        return isAttChange;
    }
    /**
     * 是否可被其他载具攻击
（0否1是）
     */
    private final int isChangeAtt;
    /**
     * 是否可被其他载具攻击
（0否1是）
     * @return
     */
    public final int getIsChangeAtt(){
        return isChangeAtt;
    }
    /**
     * 只能在指定地图使用得变身（null代表没有限制），可以配置多个
     */
    private final ReadIntegerArray mapLimit;
    /**
     * 只能在指定地图使用得变身（null代表没有限制），可以配置多个
     * @return
     */
    public final ReadIntegerArray getMapLimit(){
        return mapLimit;
    }

    public Cfg_Change_model_Bean(int id,int modelId,String skillStr,int attribute,int isAttRole,int isRoleAtt,int isAttChange,int isChangeAtt,String mapLimitStr){
        this.id = id;
        this.modelId = modelId;
        this.skill = new ReadIntegerArray(skillStr,",");
        this.attribute = attribute;
        this.isAttRole = isAttRole;
        this.isRoleAtt = isRoleAtt;
        this.isAttChange = isAttChange;
        this.isChangeAtt = isChangeAtt;
        this.mapLimit = new ReadIntegerArray(mapLimitStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("modelId:").append(modelId).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("attribute:").append(attribute).append(";");
        str.append("isAttRole:").append(isAttRole).append(";");
        str.append("isRoleAtt:").append(isRoleAtt).append(";");
        str.append("isAttChange:").append(isAttChange).append(";");
        str.append("isChangeAtt:").append(isChangeAtt).append(";");
        str.append("mapLimit:").append(mapLimit).append(";");
        return str.toString();
    }
}
