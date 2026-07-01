/**
 * Auto generated, do not edit it
 *
 * guild_battle_boss配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Guild_battle_boss_Bean{
    /**
     * key=group*100+level
     */
    private final int id;
    /**
     * key=group*100+level
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 表示拥有领地的宗派排名。1表示1名，2表示2名，3表示3名。每日11点决定
     */
    private final int group;
    /**
     * 表示拥有领地的宗派排名。1表示1名，2表示2名，3表示3名。每日11点决定
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
     * 1：世界等级刷新
2：开服天数刷新
关联monsterID和reward字段，两个字段需要保持格式一致
     */
    private final int refreshType;
    /**
     * 1：世界等级刷新
2：开服天数刷新
关联monsterID和reward字段，两个字段需要保持格式一致
     * @return
     */
    public final int getRefreshType(){
        return refreshType;
    }
    /**
     * 怪物ID根据世界等级取；<=180取180 ；>=220取220
开服天数配置方式同理
     */
    private final ReadIntegerArrayEs monsterID;
    /**
     * 怪物ID根据世界等级取；<=180取180 ；>=220取220
开服天数配置方式同理
     * @return
     */
    public final ReadIntegerArrayEs getMonsterID(){
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
     */
    private final int resp_time;
    /**
     * 死亡后复活时间，单位分钟
     * @return
     */
    public final int getResp_time(){
        return resp_time;
    }
    /**
     * 展示奖励,大于等级显示最后等级
     */
    private final ReadIntegerArrayEs reward;
    /**
     * 展示奖励,大于等级显示最后等级
     * @return
     */
    public final ReadIntegerArrayEs getReward(){
        return reward;
    }
    /**
     * 掉落拍卖物品奖励（根据怪物当前等级计算；等级_几率_道具ID；多个的时候，相同的等级）
     */
    private final ReadIntegerArrayEs drop;
    /**
     * 掉落拍卖物品奖励（根据怪物当前等级计算；等级_几率_道具ID；多个的时候，相同的等级）
     * @return
     */
    public final ReadIntegerArrayEs getDrop(){
        return drop;
    }
    /**
     * 伤害排名第一奖励
     */
    private final ReadIntegerArrayEs first_reward;
    /**
     * 伤害排名第一奖励
     * @return
     */
    public final ReadIntegerArrayEs getFirst_reward(){
        return first_reward;
    }
    /**
     * 参与奖励
     */
    private final ReadIntegerArrayEs participation_reward;
    /**
     * 参与奖励
     * @return
     */
    public final ReadIntegerArrayEs getParticipation_reward(){
        return participation_reward;
    }
    /**
     * 共享掉落，怪物归属方的同仙盟成员都可以拾取,后面跟的是掉落包ID
天数_掉落包ID（天数还是世界等级由refreshType决定）
     */
    private final ReadIntegerArrayEs guildTeam_reward;
    /**
     * 共享掉落，怪物归属方的同仙盟成员都可以拾取,后面跟的是掉落包ID
天数_掉落包ID（天数还是世界等级由refreshType决定）
     * @return
     */
    public final ReadIntegerArrayEs getGuildTeam_reward(){
        return guildTeam_reward;
    }
    /**
     * 与type关联，当为1表示百分比；其他表示固定值
     */
    private final int score;
    /**
     * 与type关联，当为1表示百分比；其他表示固定值
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 个人积分获取值
     */
    private final int personal_score;
    /**
     * 个人积分获取值
     * @return
     */
    public final int getPersonal_score(){
        return personal_score;
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

    public Cfg_Guild_battle_boss_Bean(int id,int group,int sort,int type,String name,int icon,int refreshType,String monsterIDStr,int mapID,String posStr,int rage,int resp_time,String rewardStr,String dropStr,String first_rewardStr,String participation_rewardStr,String guildTeam_rewardStr,int score,int personal_score,int defaultFollowOpen){
        this.id = id;
        this.group = group;
        this.sort = sort;
        this.type = type;
        this.name = name;
        this.icon = icon;
        this.refreshType = refreshType;
        this.monsterID = new ReadIntegerArrayEs(monsterIDStr,"}",",");
        this.mapID = mapID;
        this.pos = new ReadIntegerArrayEs(posStr,"}",",");
        this.rage = rage;
        this.resp_time = resp_time;
        this.reward = new ReadIntegerArrayEs(rewardStr,"}",",");
        this.drop = new ReadIntegerArrayEs(dropStr,"}",",");
        this.first_reward = new ReadIntegerArrayEs(first_rewardStr,"}",",");
        this.participation_reward = new ReadIntegerArrayEs(participation_rewardStr,"}",",");
        this.guildTeam_reward = new ReadIntegerArrayEs(guildTeam_rewardStr,"}",",");
        this.score = score;
        this.personal_score = personal_score;
        this.defaultFollowOpen = defaultFollowOpen;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("group:").append(group).append(";");
        str.append("sort:").append(sort).append(";");
        str.append("type:").append(type).append(";");
        str.append("name:").append(name).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("refreshType:").append(refreshType).append(";");
        str.append("monsterID:").append(monsterID).append(";");
        str.append("mapID:").append(mapID).append(";");
        str.append("pos:").append(pos).append(";");
        str.append("rage:").append(rage).append(";");
        str.append("resp_time:").append(resp_time).append(";");
        str.append("reward:").append(reward).append(";");
        str.append("drop:").append(drop).append(";");
        str.append("first_reward:").append(first_reward).append(";");
        str.append("participation_reward:").append(participation_reward).append(";");
        str.append("guildTeam_reward:").append(guildTeam_reward).append(";");
        str.append("score:").append(score).append(";");
        str.append("personal_score:").append(personal_score).append(";");
        str.append("defaultFollowOpen:").append(defaultFollowOpen).append(";");
        return str.toString();
    }
}
