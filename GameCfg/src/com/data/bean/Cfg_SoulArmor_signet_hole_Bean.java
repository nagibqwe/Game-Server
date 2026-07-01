/**
 * Auto generated, do not edit it
 *
 * SoulArmor_signet_hole配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_SoulArmor_signet_hole_Bean{
    /**
     * 镶嵌孔位id
     */
    private final int hole;
    /**
     * 镶嵌孔位id
     * @return
     */
    public final int getHole(){
        return hole;
    }
    /**
     * 解锁条件(FunctionVariable中的条件）
     */
    private final ReadIntegerArrayEs open;
    /**
     * 解锁条件(FunctionVariable中的条件）
     * @return
     */
    public final ReadIntegerArrayEs getOpen(){
        return open;
    }
    /**
     * 镶嵌部位类型
equip表中part字段
     */
    private final int equipType;
    /**
     * 镶嵌部位类型
equip表中part字段
     * @return
     */
    public final int getEquipType(){
        return equipType;
    }
    /**
     * 外圈或内圈，1外圈，2内圈
     */
    private final int circleType;
    /**
     * 外圈或内圈，1外圈，2内圈
     * @return
     */
    public final int getCircleType(){
        return circleType;
    }

    public Cfg_SoulArmor_signet_hole_Bean(int hole,String openStr,int equipType,int circleType){
        this.hole = hole;
        this.open = new ReadIntegerArrayEs(openStr,"}",",");
        this.equipType = equipType;
        this.circleType = circleType;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("hole:").append(hole).append(";");
        str.append("open:").append(open).append(";");
        str.append("equipType:").append(equipType).append(";");
        str.append("circleType:").append(circleType).append(";");
        return str.toString();
    }
}
