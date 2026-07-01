/**
 * Auto generated, do not edit it
 *
 * Cross_fudi_boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Cross_fudi_boss_Bean{
    /**
     * 怪物ID
     */
    private final int id;
    /**
     * 怪物ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）
     */
    private final int position;
    /**
     * 福地等级（1，一级福地；2，二级福地；3，三级福地）
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
     * 用于副本内排名的编号
     */
    private final int sort;
    /**
     * 用于副本内排名的编号
     * @return
     */
    public final int getSort(){
        return sort;
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
     * 击杀怪物后获得怒气值
     */
    private final int rage;
    /**
     * 击杀怪物后获得怒气值
     * @return
     */
    public final int getRage(){
        return rage;
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
     * 占领分数
     */
    private final int score;
    /**
     * 占领分数
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 福地商城对应积分
     */
    private final ReadIntegerArray personal_score;
    /**
     * 福地商城对应积分
     * @return
     */
    public final ReadIntegerArray getPersonal_score(){
        return personal_score;
    }
    /**
     * 个人积分
     */
    private final int geren_score;
    /**
     * 个人积分
     * @return
     */
    public final int getGeren_score(){
        return geren_score;
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
     * 怪物死亡后的伤害排名掉落
     */
    private final ReadIntegerArrayEs special_drop;
    /**
     * 怪物死亡后的伤害排名掉落
     * @return
     */
    public final ReadIntegerArrayEs getSpecial_drop(){
        return special_drop;
    }
    /**
     * 是否在功能开启时默认关注
（0不关注，1关注）
     */
    private final int defaultFollowOpen;
    /**
     * 是否在功能开启时默认关注
（0不关注，1关注）
     * @return
     */
    public final int getDefaultFollowOpen(){
        return defaultFollowOpen;
    }

    public Cfg_Cross_fudi_boss_Bean(int id,int position,String dayStr,String levelStr,int sort,int type,String name,int icon,String posStr,int rage,String rewardStr,int score,String personal_scoreStr,int geren_score,String dropStr,String special_dropStr,int defaultFollowOpen){
        this.id = id;
        this.position = position;
        this.day = new ReadIntegerArray(dayStr,",");
        this.level = new ReadIntegerArray(levelStr,",");
        this.sort = sort;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.pos = new ReadIntegerArray(posStr,",");
        this.rage = rage;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.score = score;
        this.personal_score = new ReadIntegerArray(personal_scoreStr,",");
        this.geren_score = geren_score;
        this.drop = new ReadIntegerArray(dropStr,",");
        this.special_drop = new ReadIntegerArrayEs(special_dropStr,"}",",");
        this.defaultFollowOpen = defaultFollowOpen;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("position:").append(position).append(";");
        str.append("day:").append(day).append(";");
        str.append("level:").append(level).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("type:").append(type).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("rage:").append(rage).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("score:").append(score).append(";");
        str.append("personal_score:").append(personal_score).append(";");
        str.append("geren_score:").append(geren_score).append(";");
        str.append("drop:").append(drop).append(";");
        str.append("special_drop:").append(special_drop).append(";");
        str.append("defaultFollowOpen:").append(defaultFollowOpen).append(";");
        return str.toString();
    }
}
