/**
 * Auto generated, do not edit it
 *
 * task_gender配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadLongArrayEs; 
	
public class Cfg_Task_gender_Bean{
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
     * 职业限制（0-玄剑1-天英9-通用）
     */
    private final int gender;
    /**
     * 职业限制（0-玄剑1-天英9-通用）
     * @return
     */
    public final int getGender(){
        return gender;
    }
    /**
     * 领取任务所需洗髓阶数
     */
    private final int genderClass;
    /**
     * 领取任务所需洗髓阶数
     * @return
     */
    public final int getGenderClass(){
        return genderClass;
    }
    /**
     * 任务开放等级
     */
    private final int level;
    /**
     * 任务开放等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 任务顺序
     */
    private final int sequence;
    /**
     * 任务顺序
     * @return
     */
    public final int getSequence(){
        return sequence;
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
     * NPC接任务对白接
     */
    private final int task_talk_start;
    /**
     * NPC接任务对白接
     * @return
     */
    public final int getTask_talk_start(){
        return task_talk_start;
    }
    /**
     * NPC交任务对白接
     */
    private final int task_talk_end;
    /**
     * NPC交任务对白接
     * @return
     */
    public final int getTask_talk_end(){
        return task_talk_end;
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
     * 进行的战力限制
     */
    private final long score_limit;
    /**
     * 进行的战力限制
     * @return
     */
    public final long getScore_limit(){
        return score_limit;
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
     * 是否为真掉落（0否1是）
     */
    private final int trueDrop;
    /**
     * 是否为真掉落（0否1是）
     * @return
     */
    public final int getTrueDrop(){
        return trueDrop;
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
     * 任务寻径地图ID
     */
    private final int pathMap;
    /**
     * 任务寻径地图ID
     * @return
     */
    public final int getPathMap(){
        return pathMap;
    }
    /**
     * 提交路径(地图id;ncpID)(@_@)
     */
    private final ReadIntegerArray endpath;
    /**
     * 提交路径(地图id;ncpID)(@_@)
     * @return
     */
    public final ReadIntegerArray getEndpath(){
        return endpath;
    }
    /**
     * 完成后接取任务ID
     */
    private final int post_task_id;
    /**
     * 完成后接取任务ID
     * @return
     */
    public final int getPost_task_id(){
        return post_task_id;
    }
    /**
     * 是否自动任务，NPC对话后下一个任务需要自动
     */
    private final int auto_task;
    /**
     * 是否自动任务，NPC对话后下一个任务需要自动
     * @return
     */
    public final int getAuto_task(){
        return auto_task;
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
     * 跳过消耗
     */
    private final int SkipCost;
    /**
     * 跳过消耗
     * @return
     */
    public final int getSkipCost(){
        return SkipCost;
    }
    /**
     * 是否可以跳过（0否1是）
     */
    private final int canSkip;
    /**
     * 是否可以跳过（0否1是）
     * @return
     */
    public final int getCanSkip(){
        return canSkip;
    }
    /**
     * 是否提示跳过（0否1是）
     */
    private final int skipPrompt;
    /**
     * 是否提示跳过（0否1是）
     * @return
     */
    public final int getSkipPrompt(){
        return skipPrompt;
    }
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     */
    private final String planes_show_enter;
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     * @return
     */
    public final String getPlanes_show_enter(){
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

    public Cfg_Task_gender_Bean(int id,int gender,int genderClass,int level,int sequence,String task_name,String conditions_describe,int task_talk_start,int task_talk_end,int conditions_npc,long score_limit,int task_type,int trueDrop,String task_x_zStr,String goal_npcStr,String show_npcStr,String show_monsterStr,String show_gatherStr,int pathMap,String endpathStr,int post_task_id,int auto_task,int auto_commit,String rewardsStr,int SkipCost,int canSkip,int skipPrompt,String planes_show_enter,int isSyncPos){
        this.id = id;
        this.gender = gender;
        this.genderClass = genderClass;
        this.level = level;
        this.sequence = sequence;
        this.task_name = task_name;
        this.conditions_describe = conditions_describe;
        this.task_talk_start = task_talk_start;
        this.task_talk_end = task_talk_end;
        this.conditions_npc = conditions_npc;
        this.score_limit = score_limit;
        this.task_type = task_type;
        this.trueDrop = trueDrop;
        this.task_x_z = new ReadIntegerArray(task_x_zStr,",");
        this.goal_npc = new ReadIntegerArray(goal_npcStr,",");
        this.show_npc = new ReadIntegerArray(show_npcStr,",");
        this.show_monster = new ReadIntegerArray(show_monsterStr,",");
        this.show_gather = new ReadIntegerArray(show_gatherStr,",");
        this.pathMap = pathMap;
        this.endpath = new ReadIntegerArray(endpathStr,",");
        this.post_task_id = post_task_id;
        this.auto_task = auto_task;
        this.auto_commit = auto_commit;
        this.rewards = new ReadLongArrayEs(rewardsStr,"}",",");
        this.SkipCost = SkipCost;
        this.canSkip = canSkip;
        this.skipPrompt = skipPrompt;
        this.planes_show_enter = planes_show_enter;
        this.isSyncPos = isSyncPos;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("gender:").append(gender).append(";");
        str.append("genderClass:").append(genderClass).append(";");
        str.append("level:").append(level).append(";");
        str.append("sequence:").append(sequence).append(";");
        str.append("task_name:").append(task_name).append(";");
        str.append("conditions_describe:").append(conditions_describe).append(";");
        str.append("task_talk_start:").append(task_talk_start).append(";");
        str.append("task_talk_end:").append(task_talk_end).append(";");
        str.append("conditions_npc:").append(conditions_npc).append(";");
        str.append("score_limit:").append(score_limit).append(";");
        str.append("task_type:").append(task_type).append(";");
        str.append("trueDrop:").append(trueDrop).append(";");
        str.append("task_x_z:").append(task_x_z).append(";");
        str.append("goal_npc:").append(goal_npc).append(";");
        str.append("show_npc:").append(show_npc).append(";");
        str.append("show_monster:").append(show_monster).append(";");
        str.append("show_gather:").append(show_gather).append(";");
        str.append("pathMap:").append(pathMap).append(";");
        str.append("endpath:").append(endpath).append(";");
        str.append("post_task_id:").append(post_task_id).append(";");
        str.append("auto_task:").append(auto_task).append(";");
        str.append("auto_commit:").append(auto_commit).append(";");
        str.append("rewards:").append(rewards).append(";");
        str.append("SkipCost:").append(SkipCost).append(";");
        str.append("canSkip:").append(canSkip).append(";");
        str.append("skipPrompt:").append(skipPrompt).append(";");
        str.append("planes_show_enter:").append(planes_show_enter).append(";");
        str.append("isSyncPos:").append(isSyncPos).append(";");
        return str.toString();
    }
}
