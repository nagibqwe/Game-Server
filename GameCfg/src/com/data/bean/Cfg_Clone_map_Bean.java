/**
 * Auto generated, do not edit it
 *
 * clone_map配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Clone_map_Bean{
    /**
     * 副本编号
     */
    private final int id;
    /**
     * 副本编号
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 对应的日常ID(用于购买次数）
     */
    private final int dailyid;
    /**
     * 对应的日常ID(用于购买次数）
     * @return
     */
    public final int getDailyid(){
        return dailyid;
    }
    /**
     * 总类型（1，副本；2，活动；3，位面）
     */
    private final int main_type;
    /**
     * 总类型（1，副本；2，活动；3，位面）
     * @return
     */
    public final int getMain_type(){
        return main_type;
    }
    /**
     * 副本类型：1：多人副本;2：个人挑战副本;3首席副本;4婚宴地图;5Boss之家;6个人BOSS;7幻境BOSS;8上古战场;9勇者之巅;10神兽岛;11位面副本；12宗派福地；13福地宝藏；14大能遗府；15天界之门；16境界boss；17特殊无限层；18本服领地战；19跨服领地战；20八极阵图；21世界篝火；22掌门传道；23天墟战场；24仙盟任务副本；25仙盟战；26仙盟驻地;27剑主试炼；28剑灵阁；29情缘副本；30巅峰竞技；31福地论剑；32荒古神坛；33魔王缝隙；34魔王缝隙除魔团；35仙侣对决；36家园地图；37混沌虚空；38混沌虚空宝库
     */
    private final int type;
    /**
     * 副本类型：1：多人副本;2：个人挑战副本;3首席副本;4婚宴地图;5Boss之家;6个人BOSS;7幻境BOSS;8上古战场;9勇者之巅;10神兽岛;11位面副本；12宗派福地；13福地宝藏；14大能遗府；15天界之门；16境界boss；17特殊无限层；18本服领地战；19跨服领地战；20八极阵图；21世界篝火；22掌门传道；23天墟战场；24仙盟任务副本；25仙盟战；26仙盟驻地;27剑主试炼；28剑灵阁；29情缘副本；30巅峰竞技；31福地论剑；32荒古神坛；33魔王缝隙；34魔王缝隙除魔团；35仙侣对决；36家园地图；37混沌虚空；38混沌虚空宝库
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 副本组队中的名称
     */
    private final String type_name;
    /**
     * 副本组队中的名称
     * @return
     */
    public final String getType_name(){
        return type_name;
    }
    /**
     * 副本关联地图编号
     */
    private final int mapid;
    /**
     * 副本关联地图编号
     * @return
     */
    public final int getMapid(){
        return mapid;
    }
    /**
     * 副本名称（支持HTML）
     */
    private final String duplicate_name;
    /**
     * 副本名称（支持HTML）
     * @return
     */
    public final String getDuplicate_name(){
        return duplicate_name;
    }
    /**
     * 额外参数
     */
    private final String params;
    /**
     * 额外参数
     * @return
     */
    public final String getParams(){
        return params;
    }
    /**
     * 购买次数时消耗的元宝，优先消耗绑定元宝(@_@)
     */
    private final ReadIntegerArray buy_need_gold;
    /**
     * 购买次数时消耗的元宝，优先消耗绑定元宝(@_@)
     * @return
     */
    public final ReadIntegerArray getBuy_need_gold(){
        return buy_need_gold;
    }
    /**
     * 副本进入类型：1.本服;2.跨服
     */
    private final int enter_type;
    /**
     * 副本进入类型：1.本服;2.跨服
     * @return
     */
    public final int getEnter_type(){
        return enter_type;
    }
    /**
     * 进入子类型(1.单人；2.组队；3.单人+组队）
     */
    private final int enter_sub_type;
    /**
     * 进入子类型(1.单人；2.组队；3.单人+组队）
     * @return
     */
    public final int getEnter_sub_type(){
        return enter_sub_type;
    }
    /**
     * 是否需要匹配（0，不需要；1，需要）
     */
    private final int if_match;
    /**
     * 是否需要匹配（0，不需要；1，需要）
     * @return
     */
    public final int getIf_match(){
        return if_match;
    }
    /**
     * 副本难度(0：不需要填写;1：简单;2：困难;3：地狱）
     */
    private final int clone_story;
    /**
     * 副本难度(0：不需要填写;1：简单;2：困难;3：地狱）
     * @return
     */
    public final int getClone_story(){
        return clone_story;
    }
    /**
     * 进入所需最小等级
     */
    private final int min_lv;
    /**
     * 进入所需最小等级
     * @return
     */
    public final int getMin_lv(){
        return min_lv;
    }
    /**
     * 最高等级进入限制
     */
    private final int max_lv;
    /**
     * 最高等级进入限制
     * @return
     */
    public final int getMax_lv(){
        return max_lv;
    }
    /**
     * 每日手动挑战次数(-1表示未开放,0表示不限制)
     */
    private final int manual_num;
    /**
     * 每日手动挑战次数(-1表示未开放,0表示不限制)
     * @return
     */
    public final int getManual_num(){
        return manual_num;
    }
    /**
     * 副本报名时间（毫秒）
     */
    private final int sign_up_time;
    /**
     * 副本报名时间（毫秒）
     * @return
     */
    public final int getSign_up_time(){
        return sign_up_time;
    }
    /**
     * 进入副本后准备阶段时间（毫秒）
     */
    private final int enter_time;
    /**
     * 进入副本后准备阶段时间（毫秒）
     * @return
     */
    public final int getEnter_time(){
        return enter_time;
    }
    /**
     * 副本时间（毫秒）
     */
    private final int exist_time;
    /**
     * 副本时间（毫秒）
     * @return
     */
    public final int getExist_time(){
        return exist_time;
    }
    /**
     * 副本成功奖励：物品ID_数量;物品ID_数量[@;@_@]
     */
    private final ReadIntegerArrayEs success_reward;
    /**
     * 副本成功奖励：物品ID_数量;物品ID_数量[@;@_@]
     * @return
     */
    public final ReadIntegerArrayEs getSuccess_reward(){
        return success_reward;
    }
    /**
     * 副本失败奖励：物品ID_数量;物品ID_数量[@;@_@]
     */
    private final ReadIntegerArrayEs fail_reward;
    /**
     * 副本失败奖励：物品ID_数量;物品ID_数量[@;@_@]
     * @return
     */
    public final ReadIntegerArrayEs getFail_reward(){
        return fail_reward;
    }
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@]
     */
    private final ReadIntegerArrayEs extra_reward;
    /**
     * 副本特殊奖励【积分】：物品ID_数量_参数;物品ID_数量_参数[@;@_@]
     * @return
     */
    public final ReadIntegerArrayEs getExtra_reward(){
        return extra_reward;
    }
    /**
     * 任务位面是否同步位置
（0表示不同步，默认为0，1表示同步）
     */
    private final int isSyPos;
    /**
     * 任务位面是否同步位置
（0表示不同步，默认为0，1表示同步）
     * @return
     */
    public final int getIsSyPos(){
        return isSyPos;
    }
    /**
     * BOSS击杀大奖描述（物品ID_数量;物品ID_数量）(@;@_@)
     */
    private final ReadIntegerArrayEs Random_Description;
    /**
     * BOSS击杀大奖描述（物品ID_数量;物品ID_数量）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRandom_Description(){
        return Random_Description;
    }
    /**
     * 推荐战斗力
     */
    private final int combat_power;
    /**
     * 推荐战斗力
     * @return
     */
    public final int getCombat_power(){
        return combat_power;
    }
    /**
     * 能否鼓舞
     */
    private final int canUpMorale;
    /**
     * 能否鼓舞
     * @return
     */
    public final int getCanUpMorale(){
        return canUpMorale;
    }
    /**
     * 扫荡要求物品(@_@)
     */
    private final ReadIntegerArray sweep;
    /**
     * 扫荡要求物品(@_@)
     * @return
     */
    public final ReadIntegerArray getSweep(){
        return sweep;
    }
    /**
     * 免费扫荡条件(@_@)
     */
    private final ReadIntegerArray sweep_free;
    /**
     * 免费扫荡条件(@_@)
     * @return
     */
    public final ReadIntegerArray getSweep_free(){
        return sweep_free;
    }
    /**
     * 需要完成的主线任务id(@_@)
     */
    private final ReadIntegerArray needTaskId;
    /**
     * 需要完成的主线任务id(@_@)
     * @return
     */
    public final ReadIntegerArray getNeedTaskId(){
        return needTaskId;
    }
    /**
     * 排序（小的再前，大的灾后）
     */
    private final int equipLevel;
    /**
     * 排序（小的再前，大的灾后）
     * @return
     */
    public final int getEquipLevel(){
        return equipLevel;
    }
    /**
     * 给予经验的索引
     */
    private final int materialLevel;
    /**
     * 给予经验的索引
     * @return
     */
    public final int getMaterialLevel(){
        return materialLevel;
    }
    /**
     * 副本开场跑的ai
     */
    private final String runningyedai;
    /**
     * 副本开场跑的ai
     * @return
     */
    public final String getRunningyedai(){
        return runningyedai;
    }
    /**
     * 副本组，每一组代表同一个副本
     */
    private final int group_id;
    /**
     * 副本组，每一组代表同一个副本
     * @return
     */
    public final int getGroup_id(){
        return group_id;
    }
    /**
     * 是否可以上坐骑（同时需要再mapsetting配置可上坐骑才会生效）
0不允许
1允许
     */
    private final int isRide;
    /**
     * 是否可以上坐骑（同时需要再mapsetting配置可上坐骑才会生效）
0不允许
1允许
     * @return
     */
    public final int getIsRide(){
        return isRide;
    }

    public Cfg_Clone_map_Bean(int id,int dailyid,int main_type,int type,String type_name,int mapid,String duplicate_name,String params,String buy_need_goldStr,int enter_type,int enter_sub_type,int if_match,int clone_story,int min_lv,int max_lv,int manual_num,int sign_up_time,int enter_time,int exist_time,String success_rewardStr,String fail_rewardStr,String extra_rewardStr,int isSyPos,String Random_DescriptionStr,int combat_power,int canUpMorale,String sweepStr,String sweep_freeStr,String needTaskIdStr,int equipLevel,int materialLevel,String runningyedai,int group_id,int isRide){
        this.id = id;
        this.dailyid = dailyid;
        this.main_type = main_type;
        this.type = type;
        this.type_name = type_name;
        this.mapid = mapid;
        this.duplicate_name = duplicate_name;
        this.params = params;
        this.buy_need_gold = new ReadIntegerArray(buy_need_goldStr,",");
        this.enter_type = enter_type;
        this.enter_sub_type = enter_sub_type;
        this.if_match = if_match;
        this.clone_story = clone_story;
        this.min_lv = min_lv;
        this.max_lv = max_lv;
        this.manual_num = manual_num;
        this.sign_up_time = sign_up_time;
        this.enter_time = enter_time;
        this.exist_time = exist_time;
        this.success_reward = new ReadIntegerArrayEs(success_rewardStr,"}",",");
        this.fail_reward = new ReadIntegerArrayEs(fail_rewardStr,"}",",");
        this.extra_reward = new ReadIntegerArrayEs(extra_rewardStr,"}",",");
        this.isSyPos = isSyPos;
        this.Random_Description = new ReadIntegerArrayEs(Random_DescriptionStr,"}",",");
        this.combat_power = combat_power;
        this.canUpMorale = canUpMorale;
        this.sweep = new ReadIntegerArray(sweepStr,",");
        this.sweep_free = new ReadIntegerArray(sweep_freeStr,",");
        this.needTaskId = new ReadIntegerArray(needTaskIdStr,",");
        this.equipLevel = equipLevel;
        this.materialLevel = materialLevel;
        this.runningyedai = runningyedai;
        this.group_id = group_id;
        this.isRide = isRide;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("dailyid:").append(dailyid).append(";");
        str.append("main_type:").append(main_type).append(";");
        str.append("type:").append(type).append(";");
        str.append("type_name:").append(type_name).append(";");
        str.append("mapid:").append(mapid).append(";");
        str.append("duplicate_name:").append(duplicate_name).append(";");
        str.append("params:").append(params).append(";");
        str.append("buy_need_gold:").append(buy_need_gold).append(";");
        str.append("enter_type:").append(enter_type).append(";");
        str.append("enter_sub_type:").append(enter_sub_type).append(";");
        str.append("if_match:").append(if_match).append(";");
        str.append("clone_story:").append(clone_story).append(";");
        str.append("min_lv:").append(min_lv).append(";");
        str.append("max_lv:").append(max_lv).append(";");
        str.append("manual_num:").append(manual_num).append(";");
        str.append("sign_up_time:").append(sign_up_time).append(";");
        str.append("enter_time:").append(enter_time).append(";");
        str.append("exist_time:").append(exist_time).append(";");
        str.append("success_reward:").append(success_reward).append(";");
        str.append("fail_reward:").append(fail_reward).append(";");
        str.append("extra_reward:").append(extra_reward).append(";");
        str.append("isSyPos:").append(isSyPos).append(";");
        str.append("Random_Description:").append(Random_Description).append(";");
        str.append("combat_power:").append(combat_power).append(";");
        str.append("canUpMorale:").append(canUpMorale).append(";");
        str.append("sweep:").append(sweep).append(";");
        str.append("sweep_free:").append(sweep_free).append(";");
        str.append("needTaskId:").append(needTaskId).append(";");
        str.append("equipLevel:").append(equipLevel).append(";");
        str.append("materialLevel:").append(materialLevel).append(";");
        str.append("runningyedai:").append(runningyedai).append(";");
        str.append("group_id:").append(group_id).append(";");
        str.append("isRide:").append(isRide).append(";");
        return str.toString();
    }
}
