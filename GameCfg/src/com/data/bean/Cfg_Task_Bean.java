/**
 * Auto generated, do not edit it
 *
 * task配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadFloatArrayEs; 
	
public class Cfg_Task_Bean{
    /**
     * 任务id(第1位为章节编号后面4位为任务编号)
     */
    private final int task_id;
    /**
     * 任务id(第1位为章节编号后面4位为任务编号)
     * @return
     */
    public final int getTask_id(){
        return task_id;
    }
    /**
     * 领取阵营（0为所有阵营）
     */
    private final int camp;
    /**
     * 领取阵营（0为所有阵营）
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 徽章进度(徽章ID_当前进度)(@_@)
     */
    private final ReadIntegerArray medalJD;
    /**
     * 徽章进度(徽章ID_当前进度)(@_@)
     * @return
     */
    public final ReadIntegerArray getMedalJD(){
        return medalJD;
    }
    /**
     * NPC交任务对白接：

     */
    private final int task_talk_end;
    /**
     * NPC交任务对白接：

     * @return
     */
    public final int getTask_talk_end(){
        return task_talk_end;
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
     * 领取条件：类型_值(@;@_@)
（类型读FunctionVariable表：1为等级，2为任务,3为开服时间,等级；不需要则留空)
     */
    private final ReadIntegerArrayEs conditions_value;
    /**
     * 领取条件：类型_值(@;@_@)
（类型读FunctionVariable表：1为等级，2为任务,3为开服时间,等级；不需要则留空)
     * @return
     */
    public final ReadIntegerArrayEs getConditions_value(){
        return conditions_value;
    }
    /**
     * 完成后接取任务ID（-1为新手村任务做完，0为主线做完）
     */
    private final int post_task_id;
    /**
     * 完成后接取任务ID（-1为新手村任务做完，0为主线做完）
     * @return
     */
    public final int getPost_task_id(){
        return post_task_id;
    }
    /**
     * 任务的前置任务的ID用于处理位面同步位置问题
     */
    private final int pre_task_id;
    /**
     * 任务的前置任务的ID用于处理位面同步位置问题
     * @return
     */
    public final int getPre_task_id(){
        return pre_task_id;
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
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX）15播放动作（0角色1法宝）
     */
    private final int type;
    /**
     * 任务类型（0NPC对话 1地图杀怪 2采集(不进背包） 3使用道具 4提交道具 5护送 6功能操作 7卡等级 8副本通关 9到达指定坐标 10收集道具（不进背包） 11收集道具（进背包） 12位面杀怪 13完成X个境界任务 14境界到达XX）15播放动作（0角色1法宝）
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 任务目标(怪物ID_数量/NPCid_数量/等级/采集物品ID_数量_采集物ID/收集物品ID_数量_怪物ID/怪物ID_数量_NPCID_副本Id/飞行ID_飞行次数)(@_@)
     */
    private final ReadIntegerArray target;
    /**
     * 任务目标(怪物ID_数量/NPCid_数量/等级/采集物品ID_数量_采集物ID/收集物品ID_数量_怪物ID/怪物ID_数量_NPCID_副本Id/飞行ID_飞行次数)(@_@)
     * @return
     */
    public final ReadIntegerArray getTarget(){
        return target;
    }
    /**
     * (法宝，宠物)位移坐标，用于15类型任务
     */
    private final ReadIntegerArray move;
    /**
     * (法宝，宠物)位移坐标，用于15类型任务
     * @return
     */
    public final ReadIntegerArray getMove(){
        return move;
    }
    /**
     * 玩家进位面面向，用于15类型任务
     */
    private final int turn;
    /**
     * 玩家进位面面向，用于15类型任务
     * @return
     */
    public final int getTurn(){
        return turn;
    }
    /**
     * 表演动作
     */
    private final String animation;
    /**
     * 表演动作
     * @return
     */
    public final String getAnimation(){
        return animation;
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
不配置的时候表示自动提交
     */
    private final ReadIntegerArray endpath;
    /**
     * 提交路径(地图id;ncpID)(@_@)
不配置的时候表示自动提交
     * @return
     */
    public final ReadIntegerArray getEndpath(){
        return endpath;
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
     * 任务奖励(奖励1;奖励2;奖励N) 奖励模型_数量_是否绑定[@;@_@]
     */
    private final ReadLongArrayEs rewards;
    /**
     * 任务奖励(奖励1;奖励2;奖励N) 奖励模型_数量_是否绑定[@;@_@]
     * @return
     */
    public final ReadLongArrayEs getRewards(){
        return rewards;
    }
    /**
     * 奖励装备（格式：职业_装备ID_是否绑定）多个用；间隔(@;@_@)
     */
    private final ReadLongArrayEs Equip;
    /**
     * 奖励装备（格式：职业_装备ID_是否绑定）多个用；间隔(@;@_@)
     * @return
     */
    public final ReadLongArrayEs getEquip(){
        return Equip;
    }
    /**
     * 神兵展示(@_@)
     */
    private final ReadIntegerArray Show;
    /**
     * 神兵展示(@_@)
     * @return
     */
    public final ReadIntegerArray getShow(){
        return Show;
    }
    /**
     * 装备强化属性（强化等级）
     */
    private final ReadIntegerArray Equip_strengthening;
    /**
     * 装备强化属性（强化等级）
     * @return
     */
    public final ReadIntegerArray getEquip_strengthening(){
        return Equip_strengthening;
    }
    /**
     * 任务共享类型（0为不共享，1为共享）
     */
    private final int Share;
    /**
     * 任务共享类型（0为不共享，1为共享）
     * @return
     */
    public final int getShare(){
        return Share;
    }
    /**
     * 怪物是否是隐藏的
     */
    private final int monsterhide;
    /**
     * 怪物是否是隐藏的
     * @return
     */
    public final int getMonsterhide(){
        return monsterhide;
    }
    /**
     * 接受到任务时进行飞行传送的ID
     */
    private final int flyteleport;
    /**
     * 接受到任务时进行飞行传送的ID
     * @return
     */
    public final int getFlyteleport(){
        return flyteleport;
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
     * 服务器副本ID值，服务器机器人用于完成副本任务处理
     */
    private final int servercloneId;
    /**
     * 服务器副本ID值，服务器机器人用于完成副本任务处理
     * @return
     */
    public final int getServercloneId(){
        return servercloneId;
    }
    /**
     * 是否传送（0传送，1不传送）
     */
    private final int isTransport;
    /**
     * 是否传送（0传送，1不传送）
     * @return
     */
    public final int getIsTransport(){
        return isTransport;
    }
    /**
     * 是否自动交接任务（0自动，1不自动）
     */
    private final int isAuto;
    /**
     * 是否自动交接任务（0自动，1不自动）
     * @return
     */
    public final int getIsAuto(){
        return isAuto;
    }
    /**
     * 激活技能（职业ID_技能ID；职业ID_技能ID）(@;@_@)
     */
    private final ReadIntegerArrayEs set_act_skill;
    /**
     * 激活技能（职业ID_技能ID；职业ID_技能ID）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getSet_act_skill(){
        return set_act_skill;
    }
    /**
     * 激活分支（职业ID_分支ID_分支索引；职业ID_分支ID_分支索引）(@;@_@)
     */
    private final ReadIntegerArrayEs set_act_branch;
    /**
     * 激活分支（职业ID_分支ID_分支索引；职业ID_分支ID_分支索引）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getSet_act_branch(){
        return set_act_branch;
    }
    /**
     * 是否加载小飞鞋特效（0否1是）
     */
    private final int isFly;
    /**
     * 是否加载小飞鞋特效（0否1是）
     * @return
     */
    public final int getIsFly(){
        return isFly;
    }
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     */
    private final ReadFloatArrayEs planes_show_enter;
    /**
     * 当为位面时，进位面的表现效果
0表示：无效果直接切
具体参数表示进入退出效果
     * @return
     */
    public final ReadFloatArrayEs getPlanes_show_enter(){
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
     * 完成任务后获得的buff
     */
    private final int buff;
    /**
     * 完成任务后获得的buff
     * @return
     */
    public final int getBuff(){
        return buff;
    }
    /**
     * 是否显示引导提示
     */
    private final int IsShowPrompt;
    /**
     * 是否显示引导提示
     * @return
     */
    public final int getIsShowPrompt(){
        return IsShowPrompt;
    }
    /**
     * 引导提示的文本
     */
    private final String PromptText;
    /**
     * 引导提示的文本
     * @return
     */
    public final String getPromptText(){
        return PromptText;
    }

    public Cfg_Task_Bean(int task_id,int camp,String medalJDStr,int task_talk_end,String task_name,String conditions_describe,String conditions_valueStr,int post_task_id,int pre_task_id,int task_talk_start,String task_x_zStr,int type,String targetStr,String moveStr,int turn,String animation,String show_npcStr,String show_monsterStr,String show_gatherStr,int pathMap,String endpathStr,int auto_commit,String rewardsStr,String EquipStr,String ShowStr,String Equip_strengtheningStr,int Share,int monsterhide,int flyteleport,int open_panel_param,int servercloneId,int isTransport,int isAuto,String set_act_skillStr,String set_act_branchStr,int isFly,String planes_show_enterStr,int isSyncPos,int buff,int IsShowPrompt,String PromptText){
        this.task_id = task_id;
        this.camp = camp;
        this.medalJD = new ReadIntegerArray(medalJDStr,",");
        this.task_talk_end = task_talk_end;
        this.task_name = task_name;
        this.conditions_describe = conditions_describe;
        this.conditions_value = new ReadIntegerArrayEs(conditions_valueStr,"}",",");
        this.post_task_id = post_task_id;
        this.pre_task_id = pre_task_id;
        this.task_talk_start = task_talk_start;
        this.task_x_z = new ReadIntegerArray(task_x_zStr,",");
        this.type = type;
        this.target = new ReadIntegerArray(targetStr,",");
        this.move = new ReadIntegerArray(moveStr,",");
        this.turn = turn;
        this.animation = animation;
        this.show_npc = new ReadIntegerArray(show_npcStr,",");
        this.show_monster = new ReadIntegerArray(show_monsterStr,",");
        this.show_gather = new ReadIntegerArray(show_gatherStr,",");
        this.pathMap = pathMap;
        this.endpath = new ReadIntegerArray(endpathStr,",");
        this.auto_commit = auto_commit;
        this.rewards = new ReadLongArrayEs(rewardsStr,"}",",");
        this.Equip = new ReadLongArrayEs(EquipStr,"}",",");
        this.Show = new ReadIntegerArray(ShowStr,",");
        this.Equip_strengthening = new ReadIntegerArray(Equip_strengtheningStr,",");
        this.Share = Share;
        this.monsterhide = monsterhide;
        this.flyteleport = flyteleport;
        this.open_panel_param = open_panel_param;
        this.servercloneId = servercloneId;
        this.isTransport = isTransport;
        this.isAuto = isAuto;
        this.set_act_skill = new ReadIntegerArrayEs(set_act_skillStr,"}",",");
        this.set_act_branch = new ReadIntegerArrayEs(set_act_branchStr,"}",",");
        this.isFly = isFly;
        this.planes_show_enter = new ReadFloatArrayEs(planes_show_enterStr,"}",",");
        this.isSyncPos = isSyncPos;
        this.buff = buff;
        this.IsShowPrompt = IsShowPrompt;
        this.PromptText = PromptText;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("task_id:").append(task_id).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("medalJD:").append(medalJD).append(";");
        str.append("task_talk_end:").append(task_talk_end).append(";");
        str.append("task_name:").append(task_name).append(";");
        str.append("conditions_describe:").append(conditions_describe).append(";");
        str.append("conditions_value:").append(conditions_value).append(";");
        str.append("post_task_id:").append(post_task_id).append(";");
        str.append("pre_task_id:").append(pre_task_id).append(";");
        str.append("task_talk_start:").append(task_talk_start).append(";");
        str.append("task_x_z:").append(task_x_z).append(";");
        str.append("type:").append(type).append(";");
        str.append("target:").append(target).append(";");
        str.append("move:").append(move).append(";");
        str.append("turn:").append(turn).append(";");
        str.append("animation:").append(animation).append(";");
        str.append("show_npc:").append(show_npc).append(";");
        str.append("show_monster:").append(show_monster).append(";");
        str.append("show_gather:").append(show_gather).append(";");
        str.append("pathMap:").append(pathMap).append(";");
        str.append("endpath:").append(endpath).append(";");
        str.append("auto_commit:").append(auto_commit).append(";");
        str.append("rewards:").append(rewards).append(";");
        str.append("Equip:").append(Equip).append(";");
        str.append("Show:").append(Show).append(";");
        str.append("Equip_strengthening:").append(Equip_strengthening).append(";");
        str.append("Share:").append(Share).append(";");
        str.append("monsterhide:").append(monsterhide).append(";");
        str.append("flyteleport:").append(flyteleport).append(";");
        str.append("open_panel_param:").append(open_panel_param).append(";");
        str.append("servercloneId:").append(servercloneId).append(";");
        str.append("isTransport:").append(isTransport).append(";");
        str.append("isAuto:").append(isAuto).append(";");
        str.append("set_act_skill:").append(set_act_skill).append(";");
        str.append("set_act_branch:").append(set_act_branch).append(";");
        str.append("isFly:").append(isFly).append(";");
        str.append("planes_show_enter:").append(planes_show_enter).append(";");
        str.append("isSyncPos:").append(isSyncPos).append(";");
        str.append("buff:").append(buff).append(";");
        str.append("IsShowPrompt:").append(IsShowPrompt).append(";");
        str.append("PromptText:").append(PromptText).append(";");
        return str.toString();
    }
}
