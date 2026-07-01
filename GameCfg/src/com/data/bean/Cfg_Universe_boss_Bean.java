/**
 * Auto generated, do not edit it
 *
 * universe_boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Universe_boss_Bean{
    /**
     * key=怪物ID
     */
    private final int id;
    /**
     * key=怪物ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 阵营（程序用于区分各个服务器）
     */
    private final int camp;
    /**
     * 阵营（程序用于区分各个服务器）
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 世界等级对应的怪
     */
    private final ReadIntegerArray worldLevel;
    /**
     * 世界等级对应的怪
     * @return
     */
    public final ReadIntegerArray getWorldLevel(){
        return worldLevel;
    }
    /**
     * 表示拥有领地的宗派排名。
1表示大本营守关BOSS；
2表示战场中心BOSS
     */
    private final int group;
    /**
     * 表示拥有领地的宗派排名。
1表示大本营守关BOSS；
2表示战场中心BOSS
     * @return
     */
    public final int getGroup(){
        return group;
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
     * 创建副本的时候需要推送给玩家的怪物
0不推送（不填默认为0）
1推送
原则上推送数量不超过2
     */
    private final int createTips;
    /**
     * 创建副本的时候需要推送给玩家的怪物
0不推送（不填默认为0）
1推送
原则上推送数量不超过2
     * @return
     */
    public final int getCreateTips(){
        return createTips;
    }
    /**
     * 怪物ID,对应monster表
     */
    private final int monsterID;
    /**
     * 怪物ID,对应monster表
     * @return
     */
    public final int getMonsterID(){
        return monsterID;
    }
    /**
     * 怪物刷新的地图.clone_map的ID
     */
    private final int mapID;
    /**
     * 怪物刷新的地图.clone_map的ID
     * @return
     */
    public final int getMapID(){
        return mapID;
    }
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     */
    private final ReadIntegerArrayEs pos;
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getPos(){
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
     * 死亡后复活时间，单位分钟
0表示走后面的特殊时间点刷新
     */
    private final int resp_time;
    /**
     * 死亡后复活时间，单位分钟
0表示走后面的特殊时间点刷新
     * @return
     */
    public final int getResp_time(){
        return resp_time;
    }
    /**
     * 固定复活的时间点和前面的死亡复活互斥，与0点的时间，单位分钟 client ignore
     */
    private final ReadIntegerArray special_time;
    /**
     * 固定复活的时间点和前面的死亡复活互斥，与0点的时间，单位分钟 client ignore
     * @return
     */
    public final ReadIntegerArray getSpecial_time(){
        return special_time;
    }
    /**
     * 怒气，击杀怪物后获得的怒气
     */
    private final int score;
    /**
     * 怒气，击杀怪物后获得的怒气
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 积分，击杀怪物后获得的积分，所有参与攻击的都会获得
     */
    private final int point;
    /**
     * 积分，击杀怪物后获得的积分，所有参与攻击的都会获得
     * @return
     */
    public final int getPoint(){
        return point;
    }
    /**
     * 击杀后在对应场景中为其他服务器的玩家增加的buff
     */
    private final int kill_buff;
    /**
     * 击杀后在对应场景中为其他服务器的玩家增加的buff
     * @return
     */
    public final int getKill_buff(){
        return kill_buff;
    }
    /**
     * 对应名次
1_3（对应1-3名）
     */
    private final ReadIntegerArrayEs rankLimit;
    /**
     * 对应名次
1_3（对应1-3名）
     * @return
     */
    public final ReadIntegerArrayEs getRankLimit(){
        return rankLimit;
    }
    /**
     * 奖励（初步预计为奖励礼包）
ID_数量
     */
    private final ReadIntegerArrayEs rankReward;
    /**
     * 奖励（初步预计为奖励礼包）
ID_数量
     * @return
     */
    public final ReadIntegerArrayEs getRankReward(){
        return rankReward;
    }
    /**
     * 服务器排名倍数
对应1,2,3,4名
     */
    private final ReadIntegerArray serverRankReward;
    /**
     * 服务器排名倍数
对应1,2,3,4名
     * @return
     */
    public final ReadIntegerArray getServerRankReward(){
        return serverRankReward;
    }

    public Cfg_Universe_boss_Bean(int id,int camp,String worldLevelStr,int group,int sort,int type,int createTips,int monsterID,int mapID,String posStr,int rage,int resp_time,String special_timeStr,int score,int point,int kill_buff,String rankLimitStr,String rankRewardStr,String serverRankRewardStr){
        this.id = id;
        this.camp = camp;
        this.worldLevel = new ReadIntegerArray(worldLevelStr,",");
        this.group = group;
        this.sort = sort;
        this.type = type;
        this.createTips = createTips;
        this.monsterID = monsterID;
        this.mapID = mapID;
        this.pos = new ReadIntegerArrayEs(posStr,"}",",");
        this.rage = rage;
        this.resp_time = resp_time;
        this.special_time = new ReadIntegerArray(special_timeStr,",");
        this.score = score;
        this.point = point;
        this.kill_buff = kill_buff;
        this.rankLimit = new ReadIntegerArrayEs(rankLimitStr,"}",",");
        this.rankReward = new ReadIntegerArrayEs(rankRewardStr,"}",",");
        this.serverRankReward = new ReadIntegerArray(serverRankRewardStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("worldLevel:").append(worldLevel).append(";");
        str.append("group:").append(group).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("type:").append(type).append(";");
        str.append("createTips:").append(createTips).append(";");
        str.append("monsterID:").append(monsterID).append(";");
        str.append("mapID:").append(mapID).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("rage:").append(rage).append(";");
        str.append("resp_time:").append(resp_time).append(";");
        str.append("special_time:").append(special_time).append(";");
        str.append("score:").append(score).append(";");
        str.append("point:").append(point).append(";");
        str.append("kill_buff:").append(kill_buff).append(";");
        str.append("rankLimit:").append(rankLimit).append(";");
        str.append("rankReward:").append(rankReward).append(";");
        str.append("serverRankReward:").append(serverRankReward).append(";");
        return str.toString();
    }
}
