/**
 * Auto generated, do not edit it
 *
 * daily配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Daily_Bean{
    /**
     * 活跃行为ID
     */
    private final int id;
    /**
     * 活跃行为ID
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 活跃行为名字
     */
    private final String name;
    /**
     * 活跃行为名字
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * 是否是开启的
0关；1开
client ignore
     */
    private final int if_open;
    /**
     * 是否是开启的
0关；1开
client ignore
     * @return
     */
    public final int getIf_open(){
        return if_open;
    }
    /**
     * 是否在列表中显示(0否1是)
     */
    private final int canshow;
    /**
     * 是否在列表中显示(0否1是)
     * @return
     */
    public final int getCanshow(){
        return canshow;
    }
    /**
     * 进度完成所加活跃值
     */
    private final int activeValue;
    /**
     * 进度完成所加活跃值
     * @return
     */
    public final int getActiveValue(){
        return activeValue;
    }
    /**
     * 参与次数
     */
    private final int times;
    /**
     * 参与次数
     * @return
     */
    public final int getTimes(){
        return times;
    }
    /**
     * 是否隐藏参与次数(0,不隐藏；1，隐藏）
     */
    private final int times_hide;
    /**
     * 是否隐藏参与次数(0,不隐藏；1，隐藏）
     * @return
     */
    public final int getTimes_hide(){
        return times_hide;
    }
    /**
     * 是否可以增加次数（0，不行；1可以）
     */
    private final int if_add_count;
    /**
     * 是否可以增加次数（0，不行；1可以）
     * @return
     */
    public final int getIf_add_count(){
        return if_add_count;
    }
    /**
     * 达到指定开服天数开启玩法，为空则表示和天数无关（达到等级要求并且达到开服天数要求才显示参加按钮）
     */
    private final int openday;
    /**
     * 达到指定开服天数开启玩法，为空则表示和天数无关（达到等级要求并且达到开服天数要求才显示参加按钮）
     * @return
     */
    public final int getOpenday(){
        return openday;
    }
    /**
     * 功能可参与等级
     */
    private final int openLevel;
    /**
     * 功能可参与等级
     * @return
     */
    public final int getOpenLevel(){
        return openLevel;
    }
    /**
     * 活动开放时间（0表示每天开放，1-7表示周一到周日的某一天）(@;@)
     */
    private final ReadIntegerArray openTime;
    /**
     * 活动开放时间（0表示每天开放，1-7表示周一到周日的某一天）(@;@)
     * @return
     */
    public final ReadIntegerArray getOpenTime(){
        return openTime;
    }
    /**
     * 是否是全天开放(0不处理、1不检查开放时间)
     */
    private final int allDayOpen;
    /**
     * 是否是全天开放(0不处理、1不检查开放时间)
     * @return
     */
    public final int getAllDayOpen(){
        return allDayOpen;
    }
    /**
     * 开放时间
     */
    private final ReadIntegerArrayEs time;
    /**
     * 开放时间
     * @return
     */
    public final ReadIntegerArrayEs getTime(){
        return time;
    }
    /**
     * 特殊关闭时间，本服福地专用，
其他功能慎用
开服天数_时间点（分钟）
     */
    private final ReadIntegerArray CloseTime;
    /**
     * 特殊关闭时间，本服福地专用，
其他功能慎用
开服天数_时间点（分钟）
     * @return
     */
    public final ReadIntegerArray getCloseTime(){
        return CloseTime;
    }
    /**
     * 境界等级增加的次数（境界下限_境界上限_增加次数）
     */
    private final ReadIntegerArrayEs state_power_count;
    /**
     * 境界等级增加的次数（境界下限_境界上限_增加次数）
     * @return
     */
    public final ReadIntegerArrayEs getState_power_count(){
        return state_power_count;
    }
    /**
     * 购买次数时消耗的元宝
     */
    private final ReadIntegerArray buy_need_gold;
    /**
     * 购买次数时消耗的元宝
     * @return
     */
    public final ReadIntegerArray getBuy_need_gold(){
        return buy_need_gold;
    }
    /**
     * 0.日刷新；1.周刷新
     */
    private final int refresh;
    /**
     * 0.日刷新；1.周刷新
     * @return
     */
    public final int getRefresh(){
        return refresh;
    }
    /**
     * clone_map的id
     */
    private final ReadIntegerArray cloneID;
    /**
     * clone_map的id
     * @return
     */
    public final ReadIntegerArray getCloneID(){
        return cloneID;
    }
    /**
     * 是否可以扫荡，0为不处理；1为可扫荡
     */
    private final int sweep;
    /**
     * 是否可以扫荡，0为不处理；1为可扫荡
     * @return
     */
    public final int getSweep(){
        return sweep;
    }
    /**
     * 扫荡等级，0为不处理；大于0为扫荡等级
     */
    private final int sweepLevel;
    /**
     * 扫荡等级，0为不处理；大于0为扫荡等级
     * @return
     */
    public final int getSweepLevel(){
        return sweepLevel;
    }
    /**
     * 是否根据开服时间确定活动开启，0为不处理按照统一时间来；>0为第几天开，开服那天算第1天
(@;@)
     */
    private final ReadIntegerArray delayDays;
    /**
     * 是否根据开服时间确定活动开启，0为不处理按照统一时间来；>0为第几天开，开服那天算第1天
(@;@)
     * @return
     */
    public final ReadIntegerArray getDelayDays(){
        return delayDays;
    }
    /**
     * 开服天数控制后是否还会再次开启，0为不开启，1为开启
     */
    private final int ifGono;
    /**
     * 开服天数控制后是否还会再次开启，0为不开启，1为开启
     * @return
     */
    public final int getIfGono(){
        return ifGono;
    }
    /**
     * 活动阶段client ignore

     */
    private final ReadIntegerArrayEs stage;
    /**
     * 活动阶段client ignore

     * @return
     */
    public final ReadIntegerArrayEs getStage(){
        return stage;
    }
    /**
     * 控制是否在活动准备阶段打开活动面板
0，不能打开
1，可以打开
     */
    private final int ready;
    /**
     * 控制是否在活动准备阶段打开活动面板
0，不能打开
1，可以打开
     * @return
     */
    public final int getReady(){
        return ready;
    }
    /**
     * 功能开启类型（0无;1打开功能界面;2前往与NPC对话;3直接参加活动不打开界面也不与Npc谈话. 注:打开界面,与Npc谈话 等等一切以此参数为准；4，直接进入副本；5，寻路日常任务）
     */
    private final int openType;
    /**
     * 功能开启类型（0无;1打开功能界面;2前往与NPC对话;3直接参加活动不打开界面也不与Npc谈话. 注:打开界面,与Npc谈话 等等一切以此参数为准；4，直接进入副本；5，寻路日常任务）
     * @return
     */
    public final int getOpenType(){
        return openType;
    }
    /**
     * 开启界面
     */
    private final ReadIntegerArray openUI;
    /**
     * 开启界面
     * @return
     */
    public final ReadIntegerArray getOpenUI(){
        return openUI;
    }
    /**
     * 寻路NPC的ID(阵营ID_职业ID_NPCID;阵营ID_职业ID_NPCID)
     */
    private final ReadIntegerArrayEs npcID;
    /**
     * 寻路NPC的ID(阵营ID_职业ID_NPCID;阵营ID_职业ID_NPCID)
     * @return
     */
    public final ReadIntegerArrayEs getNpcID(){
        return npcID;
    }
    /**
     * 是否线下推送，0不进行推送，1推送
     */
    private final int ifPush;
    /**
     * 是否线下推送，0不进行推送，1推送
     * @return
     */
    public final int getIfPush(){
        return ifPush;
    }
    /**
     * 推送类型，0就是不处理；1线上面板；2线上跑马灯，对应表notice
     */
    private final int pushType;
    /**
     * 推送类型，0就是不处理；1线上面板；2线上跑马灯，对应表notice
     * @return
     */
    public final int getPushType(){
        return pushType;
    }
    /**
     * 推送提前时间，分钟 0为不推送，>0为分钟数
     */
    private final int pushAdvance;
    /**
     * 推送提前时间，分钟 0为不推送，>0为分钟数
     * @return
     */
    public final int getPushAdvance(){
        return pushAdvance;
    }
    /**
     * 是否显示到顶部第三排菜单，0代表否，1代表是
     */
    private final int addOnMenu;
    /**
     * 是否显示到顶部第三排菜单，0代表否，1代表是
     * @return
     */
    public final int getAddOnMenu(){
        return addOnMenu;
    }
    /**
     * 服务器脚本ignore
     */
    private final int scriptId;
    /**
     * 服务器脚本ignore
     * @return
     */
    public final int getScriptId(){
        return scriptId;
    }
    /**
     * 跨服模式:0:所有的服务器都需要满足对应的开服天数，否则活动都不能进入。1:达到要求的开服天数的服务器可以进入。2:达到开服天数后，对应的跨服的所有服务器的玩家均可进入。
     */
    private final int crosstype;
    /**
     * 跨服模式:0:所有的服务器都需要满足对应的开服天数，否则活动都不能进入。1:达到要求的开服天数的服务器可以进入。2:达到开服天数后，对应的跨服的所有服务器的玩家均可进入。
     * @return
     */
    public final int getCrosstype(){
        return crosstype;
    }
    /**
     * 服务器跨服数量_显示开服时间

     */
    private final ReadIntegerArrayEs crossMatch;
    /**
     * 服务器跨服数量_显示开服时间

     * @return
     */
    public final ReadIntegerArrayEs getCrossMatch(){
        return crossMatch;
    }
    /**
     * 是否为跨服
0非跨服，
1跨服
2本服+跨服
     */
    private final int ifcross;
    /**
     * 是否为跨服
0非跨服，
1跨服
2本服+跨服
     * @return
     */
    public final int getIfcross(){
        return ifcross;
    }
    /**
     * 任务开放ID(@_@)完成这个ID得任务就开启改活动
     */
    private final ReadIntegerArray task;
    /**
     * 任务开放ID(@_@)完成这个ID得任务就开启改活动
     * @return
     */
    public final ReadIntegerArray getTask(){
        return task;
    }
    /**
     * 活动开启条件类型。1代表等级、2代表任务、3代表尚未定义
     */
    private final int type;
    /**
     * 活动开启条件类型。1代表等级、2代表任务、3代表尚未定义
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 开服第N天特殊开启一次活动
     */
    private final int specialOpen;
    /**
     * 开服第N天特殊开启一次活动
     * @return
     */
    public final int getSpecialOpen(){
        return specialOpen;
    }
    /**
     * 活动期间是否可中途退出公会
0不可以
1可以
默认为1
     */
    private final int isSignOut;
    /**
     * 活动期间是否可中途退出公会
0不可以
1可以
默认为1
     * @return
     */
    public final int getIsSignOut(){
        return isSignOut;
    }

    public Cfg_Daily_Bean(int id,String name,int if_open,int canshow,int activeValue,int times,int times_hide,int if_add_count,int openday,int openLevel,String openTimeStr,int allDayOpen,String timeStr,String CloseTimeStr,String state_power_countStr,String buy_need_goldStr,int refresh,String cloneIDStr,int sweep,int sweepLevel,String delayDaysStr,int ifGono,String stageStr,int ready,int openType,String openUIStr,String npcIDStr,int ifPush,int pushType,int pushAdvance,int addOnMenu,int scriptId,int crosstype,String crossMatchStr,int ifcross,String taskStr,int type,int specialOpen,int isSignOut){
        this.id = id;
        this.name = name;
        this.if_open = if_open;
        this.canshow = canshow;
        this.activeValue = activeValue;
        this.times = times;
        this.times_hide = times_hide;
        this.if_add_count = if_add_count;
        this.openday = openday;
        this.openLevel = openLevel;
        this.openTime = new ReadIntegerArray(openTimeStr,",");
        this.allDayOpen = allDayOpen;
        this.time = new ReadIntegerArrayEs(timeStr,"}",",");
        this.CloseTime = new ReadIntegerArray(CloseTimeStr,",");
        this.state_power_count = new ReadIntegerArrayEs(state_power_countStr,"}",",");
        this.buy_need_gold = new ReadIntegerArray(buy_need_goldStr,",");
        this.refresh = refresh;
        this.cloneID = new ReadIntegerArray(cloneIDStr,",");
        this.sweep = sweep;
        this.sweepLevel = sweepLevel;
        this.delayDays = new ReadIntegerArray(delayDaysStr,",");
        this.ifGono = ifGono;
        this.stage = new ReadIntegerArrayEs(stageStr,"}",",");
        this.ready = ready;
        this.openType = openType;
        this.openUI = new ReadIntegerArray(openUIStr,",");
        this.npcID = new ReadIntegerArrayEs(npcIDStr,"}",",");
        this.ifPush = ifPush;
        this.pushType = pushType;
        this.pushAdvance = pushAdvance;
        this.addOnMenu = addOnMenu;
        this.scriptId = scriptId;
        this.crosstype = crosstype;
        this.crossMatch = new ReadIntegerArrayEs(crossMatchStr,"}",",");
        this.ifcross = ifcross;
        this.task = new ReadIntegerArray(taskStr,",");
        this.type = type;
        this.specialOpen = specialOpen;
        this.isSignOut = isSignOut;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("if_open:").append(if_open).append(";");
        str.append("canshow:").append(canshow).append(";");
        str.append("activeValue:").append(activeValue).append(";");
        str.append("times:").append(times).append(";");
        str.append("times_hide:").append(times_hide).append(";");
        str.append("if_add_count:").append(if_add_count).append(";");
        str.append("openday:").append(openday).append(";");
        str.append("openLevel:").append(openLevel).append(";");
        str.append("openTime:").append(openTime).append(";");
        str.append("allDayOpen:").append(allDayOpen).append(";");
        str.append("time:").append(time).append(";");
        str.append("CloseTime:").append(CloseTime).append(";");
        str.append("state_power_count:").append(state_power_count).append(";");
        str.append("buy_need_gold:").append(buy_need_gold).append(";");
        str.append("refresh:").append(refresh).append(";");
        str.append("cloneID:").append(cloneID).append(";");
        str.append("sweep:").append(sweep).append(";");
        str.append("sweepLevel:").append(sweepLevel).append(";");
        str.append("delayDays:").append(delayDays).append(";");
        str.append("ifGono:").append(ifGono).append(";");
        str.append("stage:").append(stage).append(";");
        str.append("ready:").append(ready).append(";");
        str.append("openType:").append(openType).append(";");
        str.append("openUI:").append(openUI).append(";");
        str.append("npcID:").append(npcID).append(";");
        str.append("ifPush:").append(ifPush).append(";");
        str.append("pushType:").append(pushType).append(";");
        str.append("pushAdvance:").append(pushAdvance).append(";");
        str.append("addOnMenu:").append(addOnMenu).append(";");
        str.append("scriptId:").append(scriptId).append(";");
        str.append("crosstype:").append(crosstype).append(";");
        str.append("crossMatch:").append(crossMatch).append(";");
        str.append("ifcross:").append(ifcross).append(";");
        str.append("task:").append(task).append(";");
        str.append("type:").append(type).append(";");
        str.append("specialOpen:").append(specialOpen).append(";");
        str.append("isSignOut:").append(isSignOut).append(";");
        return str.toString();
    }
}
