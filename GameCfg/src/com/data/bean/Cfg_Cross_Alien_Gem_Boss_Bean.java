/**
 * Auto generated, do not edit it
 *
 * Cross_Alien_Gem_Boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_Alien_Gem_Boss_Bean{
    /**
     * 编号ID
     */
    private final int id;
    /**
     * 编号ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 怪物ID
对应monster表的主键
     */
    private final int monsterId;
    /**
     * 怪物ID
对应monster表的主键
     * @return
     */
    public final int getMonsterId(){
        return monsterId;
    }
    /**
     * BOSS类型
1：宝库副本首领
2：宝库副本精英
     */
    private final int type;
    /**
     * BOSS类型
1：宝库副本首领
2：宝库副本精英
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * BOSS所属副本
对应Cross_Alien_Gem_Copy的主键
     */
    private final int ConnectType;
    /**
     * BOSS所属副本
对应Cross_Alien_Gem_Copy的主键
     * @return
     */
    public final int getConnectType(){
        return ConnectType;
    }
    /**
     * 开服时间区间
     */
    private final ReadIntegerArray day;
    /**
     * 开服时间区间
     * @return
     */
    public final ReadIntegerArray getDay(){
        return day;
    }
    /**
     * 世界等级区间
     */
    private final ReadIntegerArray level;
    /**
     * 世界等级区间
     * @return
     */
    public final ReadIntegerArray getLevel(){
        return level;
    }
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     */
    private final ReadIntegerArray pos;
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     * @return
     */
    public final ReadIntegerArray getPos(){
        return pos;
    }
    /**
     * 怪物死亡后获得归属的服务器的所有玩家共同分的掉落
 （client ignore）
     */
    private final ReadIntegerArray drop;
    /**
     * 怪物死亡后获得归属的服务器的所有玩家共同分的掉落
 （client ignore）
     * @return
     */
    public final ReadIntegerArray getDrop(){
        return drop;
    }
    /**
     * 怪物死亡后的归属阵营方伤害排名掉落
名次下限_名次上限_对应掉落包
 （client ignore）
     */
    private final ReadIntegerArrayEs specialDrop;
    /**
     * 怪物死亡后的归属阵营方伤害排名掉落
名次下限_名次上限_对应掉落包
 （client ignore）
     * @return
     */
    public final ReadIntegerArrayEs getSpecialDrop(){
        return specialDrop;
    }

    public Cfg_Cross_Alien_Gem_Boss_Bean(int id,int monsterId,int type,int ConnectType,String dayStr,String levelStr,String posStr,String dropStr,String specialDropStr){
        this.id = id;
        this.monsterId = monsterId;
        this.type = type;
        this.ConnectType = ConnectType;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
        this.pos = new ReadIntegerArray(posStr,",");
        this.drop = new ReadIntegerArray(dropStr,",");
        this.specialDrop = new ReadIntegerArrayEs(specialDropStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("monsterId:").append(monsterId).append(";");
        str.append("type:").append(type).append(";");
        str.append("ConnectType:").append(ConnectType).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("drop:").append(drop).append(";");
        str.append("specialDrop:").append(specialDrop).append(";");
        return str.toString();
    }
}
