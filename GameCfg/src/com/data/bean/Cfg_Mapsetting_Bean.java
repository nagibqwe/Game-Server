/**
 * Auto generated, do not edit it
 *
 * Mapsetting配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Mapsetting_Bean{
    /**
     * 地图名
     */
    private final String name;
    /**
     * 地图名
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 地图id
细则见备注
     */
    private final int map_id;
    /**
     * 地图id
细则见备注
     * @return
     */
    public final int getMap_id(){
        return map_id;
    }
    /**
     * 地图类型-1登录场景，0世界地图，1副本地图，2竞技场，3跨服副本，5位面
     */
    private final int type;
    /**
     * 地图类型-1登录场景，0世界地图，1副本地图，2竞技场，3跨服副本，5位面
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 阵营模式匹配方式：0阵营id不一样就可以攻击；1根据敌方的阵营id进行位运算匹配
     */
    private final int scene_came_match_type;
    /**
     * 阵营模式匹配方式：0阵营id不一样就可以攻击；1根据敌方的阵营id进行位运算匹配
     * @return
     */
    public final int getScene_came_match_type(){
        return scene_came_match_type;
    }
    /**
     * 宠物在地图是否出战
0表示不限制，1表示宠物
     */
    private final int hide_mode;
    /**
     * 宠物在地图是否出战
0表示不限制，1表示宠物
     * @return
     */
    public final int getHide_mode(){
        return hide_mode;
    }
    /**
     * 地图内是否允许组队（0为不允许，1为允许）
     */
    private final int can_team;
    /**
     * 地图内是否允许组队（0为不允许，1为允许）
     * @return
     */
    public final int getCan_team(){
        return can_team;
    }
    /**
     * 是否允许组队传送（0为不允许，1为允许）
     */
    private final int team_sent;
    /**
     * 是否允许组队传送（0为不允许，1为允许）
     * @return
     */
    public final int getTeam_sent(){
        return team_sent;
    }
    /**
     * 脚本ID
     */
    private final int isscript;
    /**
     * 脚本ID
     * @return
     */
    public final int getIsscript(){
        return isscript;
    }
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     */
    private final ReadIntegerArrayEs bornPosition;
    /**
     * 出生位置，（x_y 表示地图坐标)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getBornPosition(){
        return bornPosition;
    }
    /**
     * 复活位置，（地图ID_坐标；地图填0为在当前地图复活)(@;@_@)
     */
    private final ReadIntegerArrayEs relivePosition;
    /**
     * 复活位置，（地图ID_坐标；地图填0为在当前地图复活)(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getRelivePosition(){
        return relivePosition;
    }
    /**
     * 用于位面离开时的位置，（x_y 表示地图坐标)(@_@)
不填表示从位面原地出
坐标表示指定位置出来
     */
    private final ReadIntegerArray leavePosition;
    /**
     * 用于位面离开时的位置，（x_y 表示地图坐标)(@_@)
不填表示从位面原地出
坐标表示指定位置出来
     * @return
     */
    public final ReadIntegerArray getLeavePosition(){
        return leavePosition;
    }
    /**
     * 消息分级类型0=普通，1=？
     */
    private final int filter;
    /**
     * 消息分级类型0=普通，1=？
     * @return
     */
    public final int getFilter(){
        return filter;
    }
    /**
     * 消息分级的可见人数
     */
    private final int filter_num;
    /**
     * 消息分级的可见人数
     * @return
     */
    public final int getFilter_num(){
        return filter_num;
    }
    /**
     * 区域的高
     */
    private final int area_high;
    /**
     * 区域的高
     * @return
     */
    public final int getArea_high(){
        return area_high;
    }
    /**
     * 区域的宽
     */
    private final int area_width;
    /**
     * 区域的宽
     * @return
     */
    public final int getArea_width(){
        return area_width;
    }
    /**
     * 进入所需VIP等级（0-9）
     */
    private final int needVip;
    /**
     * 进入所需VIP等级（0-9）
     * @return
     */
    public final int getNeedVip(){
        return needVip;
    }
    /**
     * 进入所需境界等级（0为无限制，不填则为0）
     */
    private final int needState;
    /**
     * 进入所需境界等级（0为无限制，不填则为0）
     * @return
     */
    public final int getNeedState(){
        return needState;
    }
    /**
     * 进入最小等级 -1表示无限制
     */
    private final int level_min;
    /**
     * 进入最小等级 -1表示无限制
     * @return
     */
    public final int getLevel_min(){
        return level_min;
    }
    /**
     * 进入最大等级 -1表示无限制
     */
    private final int level_max;
    /**
     * 进入最大等级 -1表示无限制
     * @return
     */
    public final int getLevel_max(){
        return level_max;
    }
    /**
     * 是否安全(0不能pk, 1常规pk， 2无惩罚pk)
     */
    private final int pkState;
    /**
     * 是否安全(0不能pk, 1常规pk， 2无惩罚pk)
     * @return
     */
    public final int getPkState(){
        return pkState;
    }
    /**
     * 是否可以使用药品。0为可以使用，1为不可使用
     */
    private final int use_drug;
    /**
     * 是否可以使用药品。0为可以使用，1为不可使用
     * @return
     */
    public final int getUse_drug(){
        return use_drug;
    }
    /**
     * 复活模式 读取relive表的类型
     */
    private final int relive_type;
    /**
     * 复活模式 读取relive表的类型
     * @return
     */
    public final int getRelive_type(){
        return relive_type;
    }
    /**
     * 复活倒计时（毫秒
     */
    private final int relive_time;
    /**
     * 复活倒计时（毫秒
     * @return
     */
    public final int getRelive_time(){
        return relive_time;
    }
    /**
     * 最大分线数量（此地图的最大分线数量，当线路中人数满了之后，不会新开线路，只会增加线路可容纳人数上限）
     */
    private final int lines;
    /**
     * 最大分线数量（此地图的最大分线数量，当线路中人数满了之后，不会新开线路，只会增加线路可容纳人数上限）
     * @return
     */
    public final int getLines(){
        return lines;
    }
    /**
     * 每条线路每批次最大增加人数（例：分线20条，最大人数50。当20条线路都50人后，会再往20条线路中分别投入50人，如果又满了，再加50，依此循环……）
     */
    private final int online;
    /**
     * 每条线路每批次最大增加人数（例：分线20条，最大人数50。当20条线路都50人后，会再往20条线路中分别投入50人，如果又满了，再加50，依此循环……）
     * @return
     */
    public final int getOnline(){
        return online;
    }
    /**
     * 场景资源名字
     */
    private final String level_name;
    /**
     * 场景资源名字
     * @return
     */
    public final String getLevel_name(){
        return level_name;
    }
    /**
     * 场景资源名字2
     */
    private final String level_name_2;
    /**
     * 场景资源名字2
     * @return
     */
    public final String getLevel_name_2(){
        return level_name_2;
    }
    /**
     * 地图阻挡数据
     */
    private final String map_grid;
    /**
     * 地图阻挡数据
     * @return
     */
    public final String getMap_grid(){
        return map_grid;
    }
    /**
     * 地图怪物NPC采集物寻路点等等
     */
    private final String map_info;
    /**
     * 地图怪物NPC采集物寻路点等等
     * @return
     */
    public final String getMap_info(){
        return map_info;
    }
    /**
     * 摄像机默认视线的距离
     */
    private final int cam_layer_cull_distance;
    /**
     * 摄像机默认视线的距离
     * @return
     */
    public final int getCam_layer_cull_distance(){
        return cam_layer_cull_distance;
    }
    /**
     * 地图掉落[几率_掉落ID；几率_掉落ID]
     */
    private final ReadIntegerArrayEs dropMap;
    /**
     * 地图掉落[几率_掉落ID；几率_掉落ID]
     * @return
     */
    public final ReadIntegerArrayEs getDropMap(){
        return dropMap;
    }
    /**
     * 使用的摄像机名字
     */
    private final String camera_name;
    /**
     * 使用的摄像机名字
     * @return
     */
    public final String getCamera_name(){
        return camera_name;
    }
    /**
     * 使用的摄像机名字
     */
    private final String camera_name_night;
    /**
     * 使用的摄像机名字
     * @return
     */
    public final String getCamera_name_night(){
        return camera_name_night;
    }
    /**
     * 是否受到设置影响，0表示不会，即设置中的画质改变时不会改变摄像机，1表示会，即设置中画质改变时会改变摄像机
     */
    private final int change_by_setting;
    /**
     * 是否受到设置影响，0表示不会，即设置中的画质改变时不会改变摄像机，1表示会，即设置中画质改变时会改变摄像机
     * @return
     */
    public final int getChange_by_setting(){
        return change_by_setting;
    }
    /**
     * 是否能跳（0否 1是）
     */
    private final int canJump;
    /**
     * 是否能跳（0否 1是）
     * @return
     */
    public final int getCanJump(){
        return canJump;
    }
    /**
     * 是否能飞（0否 1是）
     */
    private final int canFly;
    /**
     * 是否能飞（0否 1是）
     * @return
     */
    public final int getCanFly(){
        return canFly;
    }
    /**
     * 能否上坐骑（0否 1是）
     */
    private final int canRiding;
    /**
     * 能否上坐骑（0否 1是）
     * @return
     */
    public final int getCanRiding(){
        return canRiding;
    }
    /**
     * 飞行最低高度
     */
    private final int fly_min_height;
    /**
     * 飞行最低高度
     * @return
     */
    public final int getFly_min_height(){
        return fly_min_height;
    }
    /**
     * 飞行最高高度
     */
    private final int fly_max_height;
    /**
     * 飞行最高高度
     * @return
     */
    public final int getFly_max_height(){
        return fly_max_height;
    }
    /**
     * 地图里边是否可以挂机
     */
    private final int can_mandate;
    /**
     * 地图里边是否可以挂机
     * @return
     */
    public final int getCan_mandate(){
        return can_mandate;
    }
    /**
     * 地图篝火上限
     */
    private final int bonfire_num;
    /**
     * 地图篝火上限
     * @return
     */
    public final int getBonfire_num(){
        return bonfire_num;
    }
    /**
     * 加队自动召唤（0不召唤，1召唤）
     */
    private final int teamAuto;
    /**
     * 加队自动召唤（0不召唤，1召唤）
     * @return
     */
    public final int getTeamAuto(){
        return teamAuto;
    }
    /**
     * 自动寻路的坐标（世界BOSS用，别的不用；阵营ID_x_z）
     */
    private final ReadIntegerArray xunlu;
    /**
     * 自动寻路的坐标（世界BOSS用，别的不用；阵营ID_x_z）
     * @return
     */
    public final ReadIntegerArray getXunlu(){
        return xunlu;
    }
    /**
     * 公会死亡是否发送消息
     */
    private final int guild_kill;
    /**
     * 公会死亡是否发送消息
     * @return
     */
    public final int getGuild_kill(){
        return guild_kill;
    }
    /**
     * 是否显示挂机经验收益（0否，1是）
     */
    private final int exp_efficiency;
    /**
     * 是否显示挂机经验收益（0否，1是）
     * @return
     */
    public final int getExp_efficiency(){
        return exp_efficiency;
    }
    /**
     * 地图默认挂机方式（0.全地图；1.当前屏）
     */
    private final int auto_fight_set;
    /**
     * 地图默认挂机方式（0.全地图；1.当前屏）
     * @return
     */
    public final int getAuto_fight_set(){
        return auto_fight_set;
    }
    /**
     * 聊天框点击坐标的超链接是否弹出提示（0否，1是）
     */
    private final int enterPrompt;
    /**
     * 聊天框点击坐标的超链接是否弹出提示（0否，1是）
     * @return
     */
    public final int getEnterPrompt(){
        return enterPrompt;
    }
    /**
     * 该地图是否开启地图经验(0，不开启；大于1为on_hook的map_exp字段中数组序列,1表示第1个
     */
    private final int map_exp;
    /**
     * 该地图是否开启地图经验(0，不开启；大于1为on_hook的map_exp字段中数组序列,1表示第1个
     * @return
     */
    public final int getMap_exp(){
        return map_exp;
    }
    /**
     * 开启地图经验的条件（不填限制）(FunctionVariable的条件）
     */
    private final ReadIntegerArrayEs map_exp_condition;
    /**
     * 开启地图经验的条件（不填限制）(FunctionVariable的条件）
     * @return
     */
    public final ReadIntegerArrayEs getMap_exp_condition(){
        return map_exp_condition;
    }
    /**
     * 控制小地图整体缩放大小得比例，原始尺寸为1
     */
    private final int mini_map_scale;
    /**
     * 控制小地图整体缩放大小得比例，原始尺寸为1
     * @return
     */
    public final int getMini_map_scale(){
        return mini_map_scale;
    }
    /**
     * 是否屏蔽升级和任务完成特效
默认为0表示不屏蔽；1表示屏蔽
     */
    private final int vfx_show;
    /**
     * 是否屏蔽升级和任务完成特效
默认为0表示不屏蔽；1表示屏蔽
     * @return
     */
    public final int getVfx_show(){
        return vfx_show;
    }
    /**
     * 是否可以挚友召唤（0，不行；1.可以）
     */
    private final int if_Newguild_Call;
    /**
     * 是否可以挚友召唤（0，不行；1.可以）
     * @return
     */
    public final int getIf_Newguild_Call(){
        return if_Newguild_Call;
    }
    /**
     * 默认战斗模式（0，和平；1，全体；2，本服；3，强制）
     */
    private final int fight_type;
    /**
     * 默认战斗模式（0，和平；1，全体；2，本服；3，强制）
     * @return
     */
    public final int getFight_type(){
        return fight_type;
    }
    /**
     * 能否切换战斗模式（0，不行；1，可以）
     */
    private final int fight_change;
    /**
     * 能否切换战斗模式（0，不行；1，可以）
     * @return
     */
    public final int getFight_change(){
        return fight_change;
    }
    /**
     * 当前是否可以现在打坐（0或空，不行；1.可以）
     */
    private final int meditation_whether;
    /**
     * 当前是否可以现在打坐（0或空，不行；1.可以）
     * @return
     */
    public final int getMeditation_whether(){
        return meditation_whether;
    }
    /**
     * 是否可以世界支援（0，不行；1.可以）
     */
    private final int if_World_Support;
    /**
     * 是否可以世界支援（0，不行；1.可以）
     * @return
     */
    public final int getIf_World_Support(){
        return if_World_Support;
    }
    /**
     * 退出副本进入的地图ID
     */
    private final int leave_mapid;
    /**
     * 退出副本进入的地图ID
     * @return
     */
    public final int getLeave_mapid(){
        return leave_mapid;
    }
    /**
     * 
     */
    private final int MultiLevel;
    /**
     * 
     * @return
     */
    public final int getMultiLevel(){
        return MultiLevel;
    }
    /**
     * 脚本控制限时NPC(client ignore)
     */
    private final ReadIntegerArray Limit_Npc;
    /**
     * 脚本控制限时NPC(client ignore)
     * @return
     */
    public final ReadIntegerArray getLimit_Npc(){
        return Limit_Npc;
    }

    public Cfg_Mapsetting_Bean(String name,int map_id,int type,int scene_came_match_type,int hide_mode,int can_team,int team_sent,int isscript,String bornPositionStr,String relivePositionStr,String leavePositionStr,int filter,int filter_num,int area_high,int area_width,int needVip,int needState,int level_min,int level_max,int pkState,int use_drug,int relive_type,int relive_time,int lines,int online,String level_name,String level_name_2,String map_grid,String map_info,int cam_layer_cull_distance,String dropMapStr,String camera_name,String camera_name_night,int change_by_setting,int canJump,int canFly,int canRiding,int fly_min_height,int fly_max_height,int can_mandate,int bonfire_num,int teamAuto,String xunluStr,int guild_kill,int exp_efficiency,int auto_fight_set,int enterPrompt,int map_exp,String map_exp_conditionStr,int mini_map_scale,int vfx_show,int if_Newguild_Call,int fight_type,int fight_change,int meditation_whether,int if_World_Support,int leave_mapid,int MultiLevel,String Limit_NpcStr){
        this.name = name;
        this.map_id = map_id;
        this.type = type;
        this.scene_came_match_type = scene_came_match_type;
        this.hide_mode = hide_mode;
        this.can_team = can_team;
        this.team_sent = team_sent;
        this.isscript = isscript;
        this.bornPosition = new ReadIntegerArrayEs(bornPositionStr,"}",",");
        this.relivePosition = new ReadIntegerArrayEs(relivePositionStr,"}",",");
        this.leavePosition = new ReadIntegerArray(leavePositionStr,",");
        this.filter = filter;
        this.filter_num = filter_num;
        this.area_high = area_high;
        this.area_width = area_width;
        this.needVip = needVip;
        this.needState = needState;
        this.level_min = level_min;
        this.level_max = level_max;
        this.pkState = pkState;
        this.use_drug = use_drug;
        this.relive_type = relive_type;
        this.relive_time = relive_time;
        this.lines = lines;
        this.online = online;
        this.level_name = level_name;
        this.level_name_2 = level_name_2;
        this.map_grid = map_grid;
        this.map_info = map_info;
        this.cam_layer_cull_distance = cam_layer_cull_distance;
        this.dropMap = new ReadIntegerArrayEs(dropMapStr,"}",",");
        this.camera_name = camera_name;
        this.camera_name_night = camera_name_night;
        this.change_by_setting = change_by_setting;
        this.canJump = canJump;
        this.canFly = canFly;
        this.canRiding = canRiding;
        this.fly_min_height = fly_min_height;
        this.fly_max_height = fly_max_height;
        this.can_mandate = can_mandate;
        this.bonfire_num = bonfire_num;
        this.teamAuto = teamAuto;
        this.xunlu = new ReadIntegerArray(xunluStr,",");
        this.guild_kill = guild_kill;
        this.exp_efficiency = exp_efficiency;
        this.auto_fight_set = auto_fight_set;
        this.enterPrompt = enterPrompt;
        this.map_exp = map_exp;
        this.map_exp_condition = new ReadIntegerArrayEs(map_exp_conditionStr,"}",",");
        this.mini_map_scale = mini_map_scale;
        this.vfx_show = vfx_show;
        this.if_Newguild_Call = if_Newguild_Call;
        this.fight_type = fight_type;
        this.fight_change = fight_change;
        this.meditation_whether = meditation_whether;
        this.if_World_Support = if_World_Support;
        this.leave_mapid = leave_mapid;
        this.MultiLevel = MultiLevel;
        this.Limit_Npc = new ReadIntegerArray(Limit_NpcStr,",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("name:").append(name).append(";");
        str.append("map_id:").append(map_id).append(";");
        str.append("type:").append(type).append(";");
        str.append("scene_came_match_type:").append(scene_came_match_type).append(";");
        str.append("hide_mode:").append(hide_mode).append(";");
        str.append("can_team:").append(can_team).append(";");
        str.append("team_sent:").append(team_sent).append(";");
        str.append("isscript:").append(isscript).append(";");
        str.append("bornPosition:").append(bornPosition).append(";");
        str.append("relivePosition:").append(relivePosition).append(";");
        str.append("leavePosition:").append(leavePosition).append(";");
        str.append("filter:").append(filter).append(";");
        str.append("filter_num:").append(filter_num).append(";");
        str.append("area_high:").append(area_high).append(";");
        str.append("area_width:").append(area_width).append(";");
        str.append("needVip:").append(needVip).append(";");
        str.append("needState:").append(needState).append(";");
        str.append("level_min:").append(level_min).append(";");
        str.append("level_max:").append(level_max).append(";");
        str.append("pkState:").append(pkState).append(";");
        str.append("use_drug:").append(use_drug).append(";");
        str.append("relive_type:").append(relive_type).append(";");
        str.append("relive_time:").append(relive_time).append(";");
        str.append("lines:").append(lines).append(";");
        str.append("online:").append(online).append(";");
        str.append("level_name:").append(level_name).append(";");
        str.append("level_name_2:").append(level_name_2).append(";");
        str.append("map_grid:").append(map_grid).append(";");
        str.append("map_info:").append(map_info).append(";");
        str.append("cam_layer_cull_distance:").append(cam_layer_cull_distance).append(";");
        str.append("dropMap:").append(dropMap).append(";");
        str.append("camera_name:").append(camera_name).append(";");
        str.append("camera_name_night:").append(camera_name_night).append(";");
        str.append("change_by_setting:").append(change_by_setting).append(";");
        str.append("canJump:").append(canJump).append(";");
        str.append("canFly:").append(canFly).append(";");
        str.append("canRiding:").append(canRiding).append(";");
        str.append("fly_min_height:").append(fly_min_height).append(";");
        str.append("fly_max_height:").append(fly_max_height).append(";");
        str.append("can_mandate:").append(can_mandate).append(";");
        str.append("bonfire_num:").append(bonfire_num).append(";");
        str.append("teamAuto:").append(teamAuto).append(";");
        str.append("xunlu:").append(xunlu).append(";");
        str.append("guild_kill:").append(guild_kill).append(";");
        str.append("exp_efficiency:").append(exp_efficiency).append(";");
        str.append("auto_fight_set:").append(auto_fight_set).append(";");
        str.append("enterPrompt:").append(enterPrompt).append(";");
        str.append("map_exp:").append(map_exp).append(";");
        str.append("map_exp_condition:").append(map_exp_condition).append(";");
        str.append("mini_map_scale:").append(mini_map_scale).append(";");
        str.append("vfx_show:").append(vfx_show).append(";");
        str.append("if_Newguild_Call:").append(if_Newguild_Call).append(";");
        str.append("fight_type:").append(fight_type).append(";");
        str.append("fight_change:").append(fight_change).append(";");
        str.append("meditation_whether:").append(meditation_whether).append(";");
        str.append("if_World_Support:").append(if_World_Support).append(";");
        str.append("leave_mapid:").append(leave_mapid).append(";");
        str.append("MultiLevel:").append(MultiLevel).append(";");
        str.append("Limit_Npc:").append(Limit_Npc).append(";");
        return str.toString();
    }
}
