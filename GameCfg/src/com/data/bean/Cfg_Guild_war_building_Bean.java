/**
 * Auto generated, do not edit it
 *
 * guild_war_building配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Guild_war_building_Bean{
    /**
     * 对应怪物表ID
     */
    private final int id;
    /**
     * 对应怪物表ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 建筑名字
     */
    private final String name;
    /**
     * 建筑名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 建筑类型
0上古意志
1内城建筑
2中城建筑
3外城建筑
     */
    private final int type;
    /**
     * 建筑类型
0上古意志
1内城建筑
2中城建筑
3外城建筑
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 建筑对应坐标（X,Z）
Y由系统自动判断
     */
    private final ReadIntegerArray pos;
    /**
     * 建筑对应坐标（X,Z）
Y由系统自动判断
     * @return
     */
    public final ReadIntegerArray getPos(){
        return pos;
    }
    /**
     * 世界等级（小于等于配置的值）
     */
    private final int worldLevel;
    /**
     * 世界等级（小于等于配置的值）
     * @return
     */
    public final int getWorldLevel(){
        return worldLevel;
    }
    /**
     * 存活时为上古意志减伤，加法叠加，最大不可超过100%（万分比）
     */
    private final int reduceHurt;
    /**
     * 存活时为上古意志减伤，加法叠加，最大不可超过100%（万分比）
     * @return
     */
    public final int getReduceHurt(){
        return reduceHurt;
    }
    /**
     * 控制的空气墙ID（由地图配置的时候产生的ID）
     */
    private final String airWall;
    /**
     * 控制的空气墙ID（由地图配置的时候产生的ID）
     * @return
     */
    public final String getAirWall(){
        return airWall;
    }
    /**
     * 计入统计的得分（达到要求，则计分+1）
     */
    private final int rankPoint;
    /**
     * 计入统计的得分（达到要求，则计分+1）
     * @return
     */
    public final int getRankPoint(){
        return rankPoint;
    }
    /**
     * 被摧毁时增加的积分

当事人_盟友增加
     */
    private final ReadIntegerArray destroyPoint;
    /**
     * 被摧毁时增加的积分

当事人_盟友增加
     * @return
     */
    public final ReadIntegerArray getDestroyPoint(){
        return destroyPoint;
    }
    /**
     * 被载具摧毁增加的积分

当事人_盟友增加
     */
    private final ReadIntegerArray carryDestroyPoint;
    /**
     * 被载具摧毁增加的积分

当事人_盟友增加
     * @return
     */
    public final ReadIntegerArray getCarryDestroyPoint(){
        return carryDestroyPoint;
    }
    /**
     * 被修复时增加的积分

当事人_盟友增加
     */
    private final ReadIntegerArray repairPoint;
    /**
     * 被修复时增加的积分

当事人_盟友增加
     * @return
     */
    public final ReadIntegerArray getRepairPoint(){
        return repairPoint;
    }
    /**
     * 被载具修复增加的积分

当事人_盟友增加
     */
    private final ReadIntegerArray carryRepiarPoint;
    /**
     * 被载具修复增加的积分

当事人_盟友增加
     * @return
     */
    public final ReadIntegerArray getCarryRepiarPoint(){
        return carryRepiarPoint;
    }
    /**
     * 复活建筑时对应需要采集的物品
     */
    private final int gather;
    /**
     * 复活建筑时对应需要采集的物品
     * @return
     */
    public final int getGather(){
        return gather;
    }
    /**
     * 被修复时的CD时间（秒）
     */
    private final int repairCD;
    /**
     * 被修复时的CD时间（秒）
     * @return
     */
    public final int getRepairCD(){
        return repairCD;
    }

    public Cfg_Guild_war_building_Bean(int id,String name,int type,String posStr,int worldLevel,int reduceHurt,String airWall,int rankPoint,String destroyPointStr,String carryDestroyPointStr,String repairPointStr,String carryRepiarPointStr,int gather,int repairCD){
        this.id = id;
        this.name = name;
        this.type = type;
        this.pos = new ReadIntegerArray(posStr,",");
        this.worldLevel = worldLevel;
        this.reduceHurt = reduceHurt;
        this.airWall = airWall;
        this.rankPoint = rankPoint;
        this.destroyPoint = new ReadIntegerArray(destroyPointStr,",");
        this.carryDestroyPoint = new ReadIntegerArray(carryDestroyPointStr,",");
        this.repairPoint = new ReadIntegerArray(repairPointStr,",");
        this.carryRepiarPoint = new ReadIntegerArray(carryRepiarPointStr,",");
        this.gather = gather;
        this.repairCD = repairCD;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("worldLevel:").append(worldLevel).append(";");
        str.append("reduceHurt:").append(reduceHurt).append(";");
        str.append("airWall:").append(airWall).append(";");
        str.append("rankPoint:").append(rankPoint).append(";");
        str.append("destroyPoint:").append(destroyPoint).append(";");
        str.append("carryDestroyPoint:").append(carryDestroyPoint).append(";");
        str.append("repairPoint:").append(repairPoint).append(";");
        str.append("carryRepiarPoint:").append(carryRepiarPoint).append(";");
        str.append("gather:").append(gather).append(";");
        str.append("repairCD:").append(repairCD).append(";");
        return str.toString();
    }
}
