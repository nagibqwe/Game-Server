/**
 * Auto generated, do not edit it
 *
 * buff配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Buff_Bean{
    /**
     * buffid
     */
    private final int id;
    /**
     * buffid
     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * buff名称
     */
    private final String name;
    /**
     * buff名称
     * @return
     */
    public final String getName(){
        return name;
    }
    /**
     * Type_None = 0; //0：无效果
Type_Attribute = 1;// 1：属性
Type_HpPool = 2; //2：血池
Type_DecHp = 3;//3：掉血 
Type_DecAllHpRate = 4;// 4：掉血总值万分比 
Type_DecCurHpRate = 5;// 5：掉血当前值万分比 
Type_AddHp = 6;// 6：治疗
Type_addAllHpRate = 7; //7：治疗总值万分比 
Type_addCurHpRate = 8; //7：治疗当前值万分比
Type_SuperMan = 9; //7：霸体状态
Type_MoneyRate = 10; //7：金币倍率 param1：倍率万分比
Type_ExpRate = 11; //7：经验倍率 param1：倍率万分比
Type_Guiying = 12; //鬼影buff
SuperPveBuff = 13; //击杀周围怪物
Type_RoleInvisible = 14;//角色隐身
Type_DING = 15;//定身BUFF
Type_MiaoKang = 16;//免控BUFF
Type_ReDamageFromBoss = 17;// boss收到的伤害按比例施加到玩家身上
Type_BigBoom = 18;// 给player或者monster挂一个,结束时候炸周围的人固定伤害
Type_PosTriggerBuff = 19;// 位置触发事件的buff
Type_TriggerSummonBuff = 20;// 触发召唤物的buff
Type_Dizziness = 21;//眩晕的BUFF
Type_Bianshen = 22; //变身buff
Type_Chuandao = 23; //传道buff
Type_FeiJian = 24; //飞剑buff
Type_jinliao = 25; //禁疗buff
     */
    private final int type;
    /**
     * Type_None = 0; //0：无效果
Type_Attribute = 1;// 1：属性
Type_HpPool = 2; //2：血池
Type_DecHp = 3;//3：掉血 
Type_DecAllHpRate = 4;// 4：掉血总值万分比 
Type_DecCurHpRate = 5;// 5：掉血当前值万分比 
Type_AddHp = 6;// 6：治疗
Type_addAllHpRate = 7; //7：治疗总值万分比 
Type_addCurHpRate = 8; //7：治疗当前值万分比
Type_SuperMan = 9; //7：霸体状态
Type_MoneyRate = 10; //7：金币倍率 param1：倍率万分比
Type_ExpRate = 11; //7：经验倍率 param1：倍率万分比
Type_Guiying = 12; //鬼影buff
SuperPveBuff = 13; //击杀周围怪物
Type_RoleInvisible = 14;//角色隐身
Type_DING = 15;//定身BUFF
Type_MiaoKang = 16;//免控BUFF
Type_ReDamageFromBoss = 17;// boss收到的伤害按比例施加到玩家身上
Type_BigBoom = 18;// 给player或者monster挂一个,结束时候炸周围的人固定伤害
Type_PosTriggerBuff = 19;// 位置触发事件的buff
Type_TriggerSummonBuff = 20;// 触发召唤物的buff
Type_Dizziness = 21;//眩晕的BUFF
Type_Bianshen = 22; //变身buff
Type_Chuandao = 23; //传道buff
Type_FeiJian = 24; //飞剑buff
Type_jinliao = 25; //禁疗buff
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 分组(同组高等级顶替低等级)
     */
    private final int group;
    /**
     * 分组(同组高等级顶替低等级)
     * @return
     */
    public final int getGroup(){
        return group;
    }
    /**
     * 叠加次数 (额外规则-1同buff顶替)
     */
    private final int overlap;
    /**
     * 叠加次数 (额外规则-1同buff顶替)
     * @return
     */
    public final int getOverlap(){
        return overlap;
    }
    /**
     * 等级
     */
    private final int level;
    /**
     * 等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * buff图标
     */
    private final int icon;
    /**
     * buff图标
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 特效路径
     */
    private final int buffVfx;
    /**
     * 特效路径
     * @return
     */
    public final int getBuffVfx(){
        return buffVfx;
    }
    /**
     * 特效缩放
     */
    private final int vfxScal;
    /**
     * 特效缩放
     * @return
     */
    public final int getVfxScal(){
        return vfxScal;
    }
    /**
     * 特效挂载点
     */
    private final int vfxSlot;
    /**
     * 特效挂载点
     * @return
     */
    public final int getVfxSlot(){
        return vfxSlot;
    }
    /**
     * 间隔时间(毫秒) client ignore
     */
    private final int CDTime;
    /**
     * 间隔时间(毫秒) client ignore
     * @return
     */
    public final int getCDTime(){
        return CDTime;
    }
    /**
     * 持续时间(毫秒) client ignore
     */
    private final int allTime;
    /**
     * 持续时间(毫秒) client ignore
     * @return
     */
    public final int getAllTime(){
        return allTime;
    }
    /**
     * 触发间隔()client ignore
     */
    private final int time;
    /**
     * 触发间隔()client ignore
     * @return
     */
    public final int getTime(){
        return time;
    }
    /**
     * 触发次数 client ignore
     */
    private final int trigger;
    /**
     * 触发次数 client ignore
     * @return
     */
    public final int getTrigger(){
        return trigger;
    }
    /**
     * 参数1
     */
    private final int param1;
    /**
     * 参数1
     * @return
     */
    public final int getParam1(){
        return param1;
    }
    /**
     * 参数2 
     */
    private final int param2;
    /**
     * 参数2 
     * @return
     */
    public final int getParam2(){
        return param2;
    }
    /**
     * 参数3
     */
    private final int param3;
    /**
     * 参数3
     * @return
     */
    public final int getParam3(){
        return param3;
    }
    /**
     * 参数4
     */
    private final int param4;
    /**
     * 参数4
     * @return
     */
    public final int getParam4(){
        return param4;
    }
    /**
     * 影响的属性(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs porperty;
    /**
     * 影响的属性(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getPorperty(){
        return porperty;
    }
    /**
     * 作用对象（0所有 1怪物 2玩家）
     */
    private final int targetType;
    /**
     * 作用对象（0所有 1怪物 2玩家）
     * @return
     */
    public final int getTargetType(){
        return targetType;
    }
    /**
     * 脱离战斗是否清除(0不清除，1清除) client ignore
     */
    private final int leaveClean;
    /**
     * 脱离战斗是否清除(0不清除，1清除) client ignore
     * @return
     */
    public final int getLeaveClean(){
        return leaveClean;
    }
    /**
     * 死亡是否清除 client ignore
     */
    private final int dieClean;
    /**
     * 死亡是否清除 client ignore
     * @return
     */
    public final int getDieClean(){
        return dieClean;
    }
    /**
     * buff保存 0下线不保存 1下线不计时 2下线要计时 client ignore
     */
    private final int buffstore;
    /**
     * buff保存 0下线不保存 1下线不计时 2下线要计时 client ignore
     * @return
     */
    public final int getBuffstore(){
        return buffstore;
    }
    /**
     * 增0/减1益
     */
    private final int addsub;
    /**
     * 增0/减1益
     * @return
     */
    public final int getAddsub(){
        return addsub;
    }
    /**
     * 是否有文字显示（0，不显示；1，显示）
     */
    private final int wenzi;
    /**
     * 是否有文字显示（0，不显示；1，显示）
     * @return
     */
    public final int getWenzi(){
        return wenzi;
    }
    /**
     * 延迟时间（毫秒）
     */
    private final int d_time;
    /**
     * 延迟时间（毫秒）
     * @return
     */
    public final int getD_time(){
        return d_time;
    }
    /**
     * 特殊的条件 条件类型_条件参数;(@;@_@)
     */
    private final ReadIntegerArrayEs condi;
    /**
     * 特殊的条件 条件类型_条件参数;(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getCondi(){
        return condi;
    }
    /**
     * 服务器是否发送给客户端显示（0，显示；1，不显示）
     */
    private final int if_send;
    /**
     * 服务器是否发送给客户端显示（0，显示；1，不显示）
     * @return
     */
    public final int getIf_send(){
        return if_send;
    }
    /**
     * 改地图是否清除（0，不清；1，清除）
     */
    private final int if_changemap;
    /**
     * 改地图是否清除（0，不清；1，清除）
     * @return
     */
    public final int getIf_changemap(){
        return if_changemap;
    }
    /**
     * 客户端是否显示该BUFF在BUFF栏中（0，显示；1，不显示）
     */
    private final int if_show;
    /**
     * 客户端是否显示该BUFF在BUFF栏中（0，显示；1，不显示）
     * @return
     */
    public final int getIf_show(){
        return if_show;
    }

    public Cfg_Buff_Bean(int id,String name,int type,int group,int overlap,int level,int icon,int buffVfx,int vfxScal,int vfxSlot,int CDTime,int allTime,int time,int trigger,int param1,int param2,int param3,int param4,String porpertyStr,int targetType,int leaveClean,int dieClean,int buffstore,int addsub,int wenzi,int d_time,String condiStr,int if_send,int if_changemap,int if_show){
        this.id = id;
        this.name = name;
        this.type = type;
        this.group = group;
        this.overlap = overlap;
        this.level = level;
        this.icon = icon;
        this.buffVfx = buffVfx;
        this.vfxScal = vfxScal;
        this.vfxSlot = vfxSlot;
        this.CDTime = CDTime;
        this.allTime = allTime;
        this.time = time;
        this.trigger = trigger;
        this.param1 = param1;
        this.param2 = param2;
        this.param3 = param3;
        this.param4 = param4;
        this.porperty = new ReadIntegerArrayEs(porpertyStr,"}",",");
        this.targetType = targetType;
        this.leaveClean = leaveClean;
        this.dieClean = dieClean;
        this.buffstore = buffstore;
        this.addsub = addsub;
        this.wenzi = wenzi;
        this.d_time = d_time;
        this.condi = new ReadIntegerArrayEs(condiStr,"}",",");
        this.if_send = if_send;
        this.if_changemap = if_changemap;
        this.if_show = if_show;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("type:").append(type).append(";");
        str.append("group:").append(group).append(";");
        str.append("overlap:").append(overlap).append(";");
        str.append("level:").append(level).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("buffVfx:").append(buffVfx).append(";");
        str.append("vfxScal:").append(vfxScal).append(";");
        str.append("vfxSlot:").append(vfxSlot).append(";");
        str.append("CDTime:").append(CDTime).append(";");
        str.append("allTime:").append(allTime).append(";");
        str.append("time:").append(time).append(";");
        str.append("trigger:").append(trigger).append(";");
        str.append("param1:").append(param1).append(";");
        str.append("param2:").append(param2).append(";");
        str.append("param3:").append(param3).append(";");
        str.append("param4:").append(param4).append(";");
        str.append("porperty:").append(porperty).append(";");
        str.append("targetType:").append(targetType).append(";");
        str.append("leaveClean:").append(leaveClean).append(";");
        str.append("dieClean:").append(dieClean).append(";");
        str.append("buffstore:").append(buffstore).append(";");
        str.append("addsub:").append(addsub).append(";");
        str.append("wenzi:").append(wenzi).append(";");
        str.append("d_time:").append(d_time).append(";");
        str.append("condi:").append(condi).append(";");
        str.append("if_send:").append(if_send).append(";");
        str.append("if_changemap:").append(if_changemap).append(";");
        str.append("if_show:").append(if_show).append(";");
        return str.toString();
    }
}
