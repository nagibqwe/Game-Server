/**
 * Auto generated, do not edit it
 *
 * task_conquer配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Task_conquer_Bean{
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
     * 仙盟任务子类型（0 仙盟周常；1 宗派日常；2 仙盟日常）
     */
    private final int conquer_subtype;
    /**
     * 仙盟任务子类型（0 仙盟周常；1 宗派日常；2 仙盟日常）
     * @return
     */
    public final int getConquer_subtype(){
        return conquer_subtype;
    }
    /**
     * char
     */
    private final String subtype_describe;
    /**
     * char
     * @return
     */
    public final String getSubtype_describe(){
        return subtype_describe;
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
     * 职业(100为所有职业) client ignore
     */
    private final int professional;
    /**
     * 职业(100为所有职业) client ignore
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
     * 任务的品级（1_C，2_B,3_A,4_S）
     */
    private final int task_grade;
    /**
     * 任务的品级（1_C，2_B,3_A,4_S）
     * @return
     */
    public final int getTask_grade(){
        return task_grade;
    }
    /**
     * 任务的权重（万分比）
     */
    private final int task_weight;
    /**
     * 任务的权重（万分比）
     * @return
     */
    public final int getTask_weight(){
        return task_weight;
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
     * 任务内容描述
     */
    private final String content_describe;
    /**
     * 任务内容描述
     * @return
     */
    public final String getContent_describe(){
        return content_describe;
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
     * 领取任务的NPC地图 client ignore
     */
    private final int conditions_map;
    /**
     * 领取任务的NPC地图 client ignore
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
     * 完成任务是否为雕像 client ignore
     */
    private final int statue_ID;
    /**
     * 完成任务是否为雕像 client ignore
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
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX  15仙盟宣扬  16仙盟副本）
     */
    private final int task_type;
    /**
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX  15仙盟宣扬  16仙盟副本）
     * @return
     */
    public final int getTask_type(){
        return task_type;
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
     * 传送位置，x_z(@_@) client ignore
     */
    private final ReadIntegerArray daily_task_x_z;
    /**
     * 传送位置，x_z(@_@) client ignore
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
     * 任务奖励(奖励_数量;奖励_数量）[@;@_@]
     */
    private final ReadLongArrayEs rewards;
    /**
     * 任务奖励(奖励_数量;奖励_数量）[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards(){
        return rewards;
    }
    /**
     * 协助奖励
     */
    private final ReadIntegerArrayEs assistrewards;
    /**
     * 协助奖励
     * @return
     */
    public final ReadIntegerArrayEs getAssistrewards(){
        return assistrewards;
    }
    /**
     * 副本id
     */
    private final int clonemap;
    /**
     * 副本id
     * @return
     */
    public final int getClonemap(){
        return clonemap;
    }
    /**
     * 是否可以求援
     */
    private final int GuildSupport;
    /**
     * 是否可以求援
     * @return
     */
    public final int getGuildSupport(){
        return GuildSupport;
    }
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     */
    private final ReadIntegerArray planes_show_enter;
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     * @return
     */
    public final ReadIntegerArray getPlanes_show_enter(){
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

    public Cfg_Task_conquer_Bean(int id,String task_name,int conquer_subtype,String subtype_describe,int changejob,int professional,int level_min,int level_max,int task_grade,int task_weight,String conditions_describe,String content_describe,int conditions_npc,int task_talk_start,int conditions_map,int over_npc,int statue_ID,int task_talk_over,int over_map,int task_type,String task_x_zStr,String goal_npcStr,String show_npcStr,String show_monsterStr,String show_gatherStr,int goal_map,String daily_task_x_zStr,int auto_commit,String over_currencyStr,String double_currencyStr,String rewardsStr,String assistrewardsStr,int clonemap,int GuildSupport,String planes_show_enterStr,int isSyncPos,int recieve){
        this.id = id;
        this.task_name = task_name;
        this.conquer_subtype = conquer_subtype;
        this.subtype_describe = subtype_describe;
        this.changejob = changejob;
        this.professional = professional;
        this.level_min = level_min;
        this.level_max = level_max;
        this.task_grade = task_grade;
        this.task_weight = task_weight;
        this.conditions_describe = conditions_describe;
        this.content_describe = content_describe;
        this.conditions_npc = conditions_npc;
        this.task_talk_start = task_talk_start;
        this.conditions_map = conditions_map;
        this.over_npc = over_npc;
        this.statue_ID = statue_ID;
        this.task_talk_over = task_talk_over;
        this.over_map = over_map;
        this.task_type = task_type;
        this.task_x_z = new ReadIntegerArray(task_x_zStr,",");
        this.goal_npc = new ReadIntegerArray(goal_npcStr,",");
        this.show_npc = new ReadIntegerArray(show_npcStr,",");
        this.show_monster = new ReadIntegerArray(show_monsterStr,",");
        this.show_gather = new ReadIntegerArray(show_gatherStr,",");
        this.goal_map = goal_map;
        this.daily_task_x_z = new ReadIntegerArray(daily_task_x_zStr,",");
        this.auto_commit = auto_commit;
        this.over_currency = new ReadIntegerArray(over_currencyStr,",");
        this.double_currency = new ReadIntegerArray(double_currencyStr,",");
        this.rewards = new ReadLongArrayEs(rewardsStr,"}",",");
        this.assistrewards = new ReadIntegerArrayEs(assistrewardsStr,"}",",");
        this.clonemap = clonemap;
        this.GuildSupport = GuildSupport;
        this.planes_show_enter = new ReadIntegerArray(planes_show_enterStr,",");
        this.isSyncPos = isSyncPos;
        this.recieve = recieve;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("task_name:").append(task_name).append(";");
        str.append("conquer_subtype:").append(conquer_subtype).append(";");
        str.append("subtype_describe:").append(subtype_describe).append(";");
        str.append("changejob:").append(changejob).append(";");
        str.append("professional:").append(professional).append(";");
        str.append("level_min:").append(level_min).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("task_grade:").append(task_grade).append(";");
        str.append("task_weight:").append(task_weight).append(";");
        str.append("conditions_describe:").append(conditions_describe).append(";");
        str.append("content_describe:").append(content_describe).append(";");
        str.append("conditions_npc:").append(conditions_npc).append(";");
        str.append("task_talk_start:").append(task_talk_start).append(";");
        str.append("conditions_map:").append(conditions_map).append(";");
        str.append("over_npc:").append(over_npc).append(";");
        str.append("statue_ID:").append(statue_ID).append(";");
        str.append("task_talk_over:").append(task_talk_over).append(";");
        str.append("over_map:").append(over_map).append(";");
        str.append("task_type:").append(task_type).append(";");
        str.append("task_x_z:").append(task_x_z).append(";");
        str.append("goal_npc:").append(goal_npc).append(";");
        str.append("show_npc:").append(show_npc).append(";");
        str.append("show_monster:").append(show_monster).append(";");
        str.append("show_gather:").append(show_gather).append(";");
        str.append("goal_map:").append(goal_map).append(";");
        str.append("daily_task_x_z:").append(daily_task_x_z).append(";");
        str.append("auto_commit:").append(auto_commit).append(";");
        str.append("over_currency:").append(over_currency).append(";");
        str.append("double_currency:").append(double_currency).append(";");
        str.append("rewards:").append(rewards).append(";");
        str.append("assistrewards:").append(assistrewards).append(";");
        str.append("clonemap:").append(clonemap).append(";");
        str.append("GuildSupport:").append(GuildSupport).append(";");
        str.append("planes_show_enter:").append(planes_show_enter).append(";");
        str.append("isSyncPos:").append(isSyncPos).append(";");
        str.append("recieve:").append(recieve).append(";");
        return str.toString();
    }
}
