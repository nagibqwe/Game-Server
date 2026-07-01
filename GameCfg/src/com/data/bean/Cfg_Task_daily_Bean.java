/**
 * Auto generated, do not edit it
 *
 * task_daily配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Task_daily_Bean{
    /**
     * 任务ID
     */
    private final int id;
    /**
     * 任务ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 任务名称
     */
    private final String task_name;
    /**
     * 任务名称
     * @return
     */
    public final String getTask_name(){
        return task_name;
    }
    /**
     * 是否固定首发任务（0不是 1是）
     */
    private final int guide_type;
    /**
     * 是否固定首发任务（0不是 1是）
     * @return
     */
    public final int getGuide_type(){
        return guide_type;
    }
    /**
     * 日常任务子类型（0 经验日常；1 银币日常）
     */
    private final int daily_subtype;
    /**
     * 日常任务子类型（0 经验日常；1 银币日常）
     * @return
     */
    public final int getDaily_subtype(){
        return daily_subtype;
    }
    /**
     * 阵营 client ignore
     */
    private final int changejob;
    /**
     * 阵营 client ignore
     * @return
     */
    public final int getChangejob(){
        return changejob;
    }
    /**
     * 职业client ignore
     */
    private final int professional;
    /**
     * 职业client ignore
     * @return
     */
    public final int getProfessional(){
        return professional;
    }
    /**
     * 等级min client ignore
     */
    private final int level_min;
    /**
     * 等级min client ignore
     * @return
     */
    public final int getLevel_min(){
        return level_min;
    }
    /**
     * 等级max client ignore
     */
    private final int level_max;
    /**
     * 等级max client ignore
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 任务目标描述
     */
    private final String conditions_describe;
    /**
     * 任务目标描述
     * @return
     */
    public final String getConditions_describe(){
        return conditions_describe;
    }
    /**
     * 领取任务的NPC
     */
    private final int conditions_npc;
    /**
     * 领取任务的NPC
     * @return
     */
    public final int getConditions_npc(){
        return conditions_npc;
    }
    /**
     * 领取任务对话
     */
    private final int task_talk_start;
    /**
     * 领取任务对话
     * @return
     */
    public final int getTask_talk_start(){
        return task_talk_start;
    }
    /**
     * 领取任务的NPC地图
     */
    private final int conditions_map;
    /**
     * 领取任务的NPC地图
     * @return
     */
    public final int getConditions_map(){
        return conditions_map;
    }
    /**
     * 提交任务NPC
     */
    private final int over_npc;
    /**
     * 提交任务NPC
     * @return
     */
    public final int getOver_npc(){
        return over_npc;
    }
    /**
     * 完成任务是否为雕像client ignore
     */
    private final int statue_ID;
    /**
     * 完成任务是否为雕像client ignore
     * @return
     */
    public final int getStatue_ID(){
        return statue_ID;
    }
    /**
     * 完成任务对话
     */
    private final int task_talk_over;
    /**
     * 完成任务对话
     * @return
     */
    public final int getTask_talk_over(){
        return task_talk_over;
    }
    /**
     * 完成任务的NPC地图 client ignore
     */
    private final int over_map;
    /**
     * 完成任务的NPC地图 client ignore
     * @return
     */
    public final int getOver_map(){
        return over_map;
    }
    /**
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX）
     */
    private final int task_type;
    /**
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX）
     * @return
     */
    public final int getTask_type(){
        return task_type;
    }
    /**
     * 对话的npc_id
     */
    private final int npc_id;
    /**
     * 对话的npc_id
     * @return
     */
    public final int getNpc_id(){
        return npc_id;
    }
    /**
     * 坐标
用于位面，走到具体的坐标
(@_@)
     */
    private final ReadIntegerArray task_x_z;
    /**
     * 坐标
用于位面，走到具体的坐标
(@_@)
     * @return
     */
    public final ReadIntegerArray getTask_x_z(){
        return task_x_z;
    }
    /**
     * 任务目标(任务模型/NPC_数量)(@_@)
     */
    private final ReadIntegerArray goal_npc;
    /**
     * 任务目标(任务模型/NPC_数量)(@_@)
     * @return
     */
    public final ReadIntegerArray getGoal_npc(){
        return goal_npc;
    }
    /**
     * 仅在任务显示NPC（@_@)
     */
    private final ReadIntegerArray show_npc;
    /**
     * 仅在任务显示NPC（@_@)
     * @return
     */
    public final ReadIntegerArray getShow_npc(){
        return show_npc;
    }
    /**
     * 仅在任务显示怪物（@_@)
     */
    private final ReadIntegerArray show_monster;
    /**
     * 仅在任务显示怪物（@_@)
     * @return
     */
    public final ReadIntegerArray getShow_monster(){
        return show_monster;
    }
    /**
     * 仅在任务显示采集物（@_@)
     */
    private final ReadIntegerArray show_gather;
    /**
     * 仅在任务显示采集物（@_@)
     * @return
     */
    public final ReadIntegerArray getShow_gather(){
        return show_gather;
    }
    /**
     * 参数
     */
    private final int open_panel_param;
    /**
     * 参数
     * @return
     */
    public final int getOpen_panel_param(){
        return open_panel_param;
    }
    /**
     * 目标地图id client ignore
     */
    private final int goal_map;
    /**
     * 目标地图id client ignore
     * @return
     */
    public final int getGoal_map(){
        return goal_map;
    }
    /**
     * 传送位置，x_z(@_@) daily_task_x_z
     */
    private final ReadIntegerArray daily_task_x_z;
    /**
     * 传送位置，x_z(@_@) daily_task_x_z
     * @return
     */
    public final ReadIntegerArray getDaily_task_x_z(){
        return daily_task_x_z;
    }
    /**
     * 自动提交的任务
0.服务器自动完成
1.左边任务面板点击提交
     */
    private final int auto_commit;
    /**
     * 自动提交的任务
0.服务器自动完成
1.左边任务面板点击提交
     * @return
     */
    public final int getAuto_commit(){
        return auto_commit;
    }
    /**
     * 星级概率（0星概率_1星概率_2星概率_3星概率…..）(@_@) daily_task_x_z
     */
    private final ReadIntegerArray star;
    /**
     * 星级概率（0星概率_1星概率_2星概率_3星概率…..）(@_@) daily_task_x_z
     * @return
     */
    public final ReadIntegerArray getStar(){
        return star;
    }
    /**
     * 刷星消耗(@_@)
     */
    private final ReadIntegerArray fillStarcost;
    /**
     * 刷星消耗(@_@)
     * @return
     */
    public final ReadIntegerArray getFillStarcost(){
        return fillStarcost;
    }
    /**
     * 完成单次货币(完成任务货币类型_值)(@_@)
     */
    private final ReadIntegerArray over_currency;
    /**
     * 完成单次货币(完成任务货币类型_值)(@_@)
     * @return
     */
    public final ReadIntegerArray getOver_currency(){
        return over_currency;
    }
    /**
     * 双倍奖励(双倍奖励货币类型_值)(@_@)
     */
    private final ReadIntegerArray double_currency;
    /**
     * 双倍奖励(双倍奖励货币类型_值)(@_@)
     * @return
     */
    public final ReadIntegerArray getDouble_currency(){
        return double_currency;
    }
    /**
     * 0星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_0;
    /**
     * 0星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_0(){
        return rewards_0;
    }
    /**
     * 1星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_1;
    /**
     * 1星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_1(){
        return rewards_1;
    }
    /**
     * 2星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_2;
    /**
     * 2星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_2(){
        return rewards_2;
    }
    /**
     * 3星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_3;
    /**
     * 3星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_3(){
        return rewards_3;
    }
    /**
     * 4星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_4;
    /**
     * 4星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_4(){
        return rewards_4;
    }
    /**
     * 5星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards_5;
    /**
     * 5星任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards_5(){
        return rewards_5;
    }
    /**
     * 任务类型
     */
    private final String typeName;
    /**
     * 任务类型
     * @return
     */
    public final String getTypeName(){
        return typeName;
    }
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     */
    private final ReadIntegerArrayEs planes_show_enter;
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     * @return
     */
    public final ReadIntegerArrayEs getPlanes_show_enter(){
        return planes_show_enter;
    }
    /**
     * 任务位面是否同步位置
（0表示不同步，默认为0，1表示同步）
     */
    private final int isSyncPos;
    /**
     * 任务位面是否同步位置
（0表示不同步，默认为0，1表示同步）
     * @return
     */
    public final int getIsSyncPos(){
        return isSyncPos;
    }
    /**
     * 服务器是否自动派发（0自动派到任务栏 1需要玩家主动接取）
     */
    private final int recieve;
    /**
     * 服务器是否自动派发（0自动派到任务栏 1需要玩家主动接取）
     * @return
     */
    public final int getRecieve(){
        return recieve;
    }

    public Cfg_Task_daily_Bean(int id,String task_name,int guide_type,int daily_subtype,int changejob,int professional,int level_min,int level_max,String conditions_describe,int conditions_npc,int task_talk_start,int conditions_map,int over_npc,int statue_ID,int task_talk_over,int over_map,int task_type,int npc_id,String task_x_zStr,String goal_npcStr,String show_npcStr,String show_monsterStr,String show_gatherStr,int open_panel_param,int goal_map,String daily_task_x_zStr,int auto_commit,String starStr,String fillStarcostStr,String over_currencyStr,String double_currencyStr,String rewards_0Str,String rewards_1Str,String rewards_2Str,String rewards_3Str,String rewards_4Str,String rewards_5Str,String typeName,String planes_show_enterStr,int isSyncPos,int recieve){
        this.id = id;
        this.task_name = task_name;
        this.guide_type = guide_type;
        this.daily_subtype = daily_subtype;
        this.changejob = changejob;
        this.professional = professional;
        this.level_min = level_min;
        this.level_max = level_max;
        this.conditions_describe = conditions_describe;
        this.conditions_npc = conditions_npc;
        this.task_talk_start = task_talk_start;
        this.conditions_map = conditions_map;
        this.over_npc = over_npc;
        this.statue_ID = statue_ID;
        this.task_talk_over = task_talk_over;
        this.over_map = over_map;
        this.task_type = task_type;
        this.npc_id = npc_id;
        this.task_x_z = new ReadIntegerArray(task_x_zStr,",");
        this.goal_npc = new ReadIntegerArray(goal_npcStr,",");
        this.show_npc = new ReadIntegerArray(show_npcStr,",");
        this.show_monster = new ReadIntegerArray(show_monsterStr,",");
        this.show_gather = new ReadIntegerArray(show_gatherStr,",");
        this.open_panel_param = open_panel_param;
        this.goal_map = goal_map;
        this.daily_task_x_z = new ReadIntegerArray(daily_task_x_zStr,",");
        this.auto_commit = auto_commit;
        this.star = new ReadIntegerArray(starStr,",");
        this.fillStarcost = new ReadIntegerArray(fillStarcostStr,",");
        this.over_currency = new ReadIntegerArray(over_currencyStr,",");
        this.double_currency = new ReadIntegerArray(double_currencyStr,",");
        this.rewards_0 = new ReadLongArrayEs(rewards_0Str,"}",",");
        this.rewards_1 = new ReadLongArrayEs(rewards_1Str,"}",",");
        this.rewards_2 = new ReadLongArrayEs(rewards_2Str,"}",",");
        this.rewards_3 = new ReadLongArrayEs(rewards_3Str,"}",",");
        this.rewards_4 = new ReadLongArrayEs(rewards_4Str,"}",",");
        this.rewards_5 = new ReadLongArrayEs(rewards_5Str,"}",",");
        this.typeName = typeName;
        this.planes_show_enter = new ReadIntegerArrayEs(planes_show_enterStr,"}",",");
        this.isSyncPos = isSyncPos;
        this.recieve = recieve;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("task_name:").append(task_name).append(";");
        str.append("guide_type:").append(guide_type).append(";");
        str.append("daily_subtype:").append(daily_subtype).append(";");
        str.append("changejob:").append(changejob).append(";");
        str.append("professional:").append(professional).append(";");
        str.append("level_min:").append(level_min).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("conditions_describe:").append(conditions_describe).append(";");
        str.append("conditions_npc:").append(conditions_npc).append(";");
        str.append("task_talk_start:").append(task_talk_start).append(";");
        str.append("conditions_map:").append(conditions_map).append(";");
        str.append("over_npc:").append(over_npc).append(";");
        str.append("statue_ID:").append(statue_ID).append(";");
        str.append("task_talk_over:").append(task_talk_over).append(";");
        str.append("over_map:").append(over_map).append(";");
        str.append("task_type:").append(task_type).append(";");
        str.append("npc_id:").append(npc_id).append(";");
        str.append("task_x_z:").append(task_x_z).append(";");
        str.append("goal_npc:").append(goal_npc).append(";");
        str.append("show_npc:").append(show_npc).append(";");
        str.append("show_monster:").append(show_monster).append(";");
        str.append("show_gather:").append(show_gather).append(";");
        str.append("open_panel_param:").append(open_panel_param).append(";");
        str.append("goal_map:").append(goal_map).append(";");
        str.append("daily_task_x_z:").append(daily_task_x_z).append(";");
        str.append("auto_commit:").append(auto_commit).append(";");
        str.append("star:").append(star).append(";");
        str.append("fillStarcost:").append(fillStarcost).append(";");
        str.append("over_currency:").append(over_currency).append(";");
        str.append("double_currency:").append(double_currency).append(";");
        str.append("rewards_0:").append(rewards_0).append(";");
        str.append("rewards_1:").append(rewards_1).append(";");
        str.append("rewards_2:").append(rewards_2).append(";");
        str.append("rewards_3:").append(rewards_3).append(";");
        str.append("rewards_4:").append(rewards_4).append(";");
        str.append("rewards_5:").append(rewards_5).append(";");
        str.append("typeName:").append(typeName).append(";");
        str.append("planes_show_enter:").append(planes_show_enter).append(";");
        str.append("isSyncPos:").append(isSyncPos).append(";");
        str.append("recieve:").append(recieve).append(";");
        return str.toString();
    }
}
