/**
 * Auto generated, do not edit it
 *
 * Leader_Preach_value配置表
 */
package com.data.bean;

import com.data.struct.ReadLongArray; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Leader_Preach_value_Bean{
    /**
     * ID（最低等级）
     */
    private final int id;
    /**
     * ID（最低等级）
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 传道显示名字
     */
    private final String name;
    /**
     * 传道显示名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 标准战斗力[@_@]
     */
    private final ReadLongArray Leader_Preach_fight;
    /**
     * 标准战斗力[@_@]
     * @return
     */
    public final ReadLongArray getLeader_Preach_fight(){
        return Leader_Preach_fight;
    }
    /**
     * 每次经验奖励（4个难度依次填写）[@_@] 
     */
    private final ReadLongArray Leader_Preach_award;
    /**
     * 每次经验奖励（4个难度依次填写）[@_@] 
     * @return
     */
    public final ReadLongArray getLeader_Preach_award(){
        return Leader_Preach_award;
    }
    /**
     * 最小等级
     */
    private final int min_level;
    /**
     * 最小等级
     * @return
     */
    public final int getMin_level(){
        return min_level;
    }
    /**
     * 最大等级
     */
    private final int max_level;
    /**
     * 最大等级
     * @return
     */
    public final int getMax_level(){
        return max_level;
    }
    /**
     * 传道的地图ID
     */
    private final int mapid;
    /**
     * 传道的地图ID
     * @return
     */
    public final int getMapid(){
        return mapid;
    }
    /**
     * 传道区域中心点
     */
    private final ReadIntegerArray point;
    /**
     * 传道区域中心点
     * @return
     */
    public final ReadIntegerArray getPoint(){
        return point;
    }
    /**
     * 传道区域半径
     */
    private final int Radius;
    /**
     * 传道区域半径
     * @return
     */
    public final int getRadius(){
        return Radius;
    }

    public Cfg_Leader_Preach_value_Bean(int id,String name,String Leader_Preach_fightStr,String Leader_Preach_awardStr,int min_level,int max_level,int mapid,String pointStr,int Radius){
        this.id = id;
        this.name = name;
        this.Leader_Preach_fight = new ReadLongArray(Leader_Preach_fightStr,",");
        this.Leader_Preach_award = new ReadLongArray(Leader_Preach_awardStr,",");
        this.min_level = min_level;
        this.max_level = max_level;
        this.mapid = mapid;
        this.point = new ReadIntegerArray(pointStr,",");
        this.Radius = Radius;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("Leader_Preach_fight:").append(Leader_Preach_fight).append(";");
        str.append("Leader_Preach_award:").append(Leader_Preach_award).append(";");
        str.append("min_level:").append(min_level).append(";");
        str.append("max_level:").append(max_level).append(";");
        str.append("mapid:").append(mapid).append(";");
        str.append("point:").append(point).append(";");
        str.append("Radius:").append(Radius).append(";");
        return str.toString();
    }
}
