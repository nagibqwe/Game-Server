/**
 * Auto generated, do not edit it
 *
 * Cross_devil_boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_devil_boss_Bean{
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
     */
    private final int monsterId;
    /**
     * 怪物ID
     * @return
     */
    public final int getMonsterId(){
        return monsterId;
    }
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）1级福地为最低级福地
0为特殊展示，用于客户端界面展示用
     */
    private final int position;
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）1级福地为最低级福地
0为特殊展示，用于客户端界面展示用
     * @return
     */
    public final int getPosition(){
        return position;
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
     * 副本开启后间隔多久后刷新出怪物
单位（秒）

     */
    private final int wait_time;
    /**
     * 副本开启后间隔多久后刷新出怪物
单位（秒）

     * @return
     */
    public final int getWait_time(){
        return wait_time;
    }
    /**
     * 1.领主；2.精英；3.侍卫
     */
    private final int type;
    /**
     * 1.领主；2.精英；3.侍卫
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 怪物名
     */
    private final String name;
    /**
     * 怪物名
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 头像
     */
    private final int icon;
    /**
     * 头像
     * @return
     */
    public final int getIcon(){
        return icon;
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
     * 展示奖励,
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 展示奖励,
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 怪物死亡后获得归属的服务器的所有玩家共同分的掉落
     */
    private final ReadIntegerArray drop;
    /**
     * 怪物死亡后获得归属的服务器的所有玩家共同分的掉落
     * @return
     */
    public final ReadIntegerArray getDrop(){
        return drop;
    }
    /**
     * 怪物死亡后的归属阵营方伤害排名掉落
名次下限_名次上限_对应掉落包
     */
    private final ReadIntegerArrayEs special_drop;
    /**
     * 怪物死亡后的归属阵营方伤害排名掉落
名次下限_名次上限_对应掉落包
     * @return
     */
    public final ReadIntegerArrayEs getSpecial_drop(){
        return special_drop;
    }

    public Cfg_Cross_devil_boss_Bean(int id,int monsterId,int position,String dayStr,String levelStr,int wait_time,int type,String name,int icon,String posStr,String rewardStr,String dropStr,String special_dropStr){
        this.id = id;
        this.monsterId = monsterId;
        this.position = position;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
        this.wait_time = wait_time;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.pos = new ReadIntegerArray(posStr,",");
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.drop = new ReadIntegerArray(dropStr,",");
        this.special_drop = new ReadIntegerArrayEs(special_dropStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("monsterId:").append(monsterId).append(";");
        str.append("position:").append(position).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        str.append("wait_time:").append(wait_time).append(";");
        str.append("type:").append(type).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("drop:").append(drop).append(";");
        str.append("special_drop:").append(special_drop).append(";");
        return str.toString();
    }
}
