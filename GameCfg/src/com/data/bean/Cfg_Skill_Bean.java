/**
 * Auto generated, do not edit it
 *
 * skill配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadIntegerArray; 
	
public class Cfg_Skill_Bean{
    /**
     * 技能ID

     */
    private final int id;
    /**
     * 技能ID

     * @return
     */
    public final int getId(){
        return id;
    }
    /**
     * 技能分支ID
     */
    private final int cell_id;
    /**
     * 技能分支ID
     * @return
     */
    public final int getCell_id(){
        return cell_id;
    }
    /**
     * 技能图标编号,任务技能图标使用的是skillicon的图集
     */
    private final int icon;
    /**
     * 技能图标编号,任务技能图标使用的是skillicon的图集
     * @return
     */
    public final int getIcon(){
        return icon;
    }
    /**
     * 技能可视化数据
     */
    private final String VisualDef;
    /**
     * 技能可视化数据
     * @return
     */
    public final String getVisualDef(){
        return VisualDef;
    }
    /**
     * 和服务器的同步类型（0客户端先表现；1等服务器返回了再表现）
     */
    private final int server_sync_type;
    /**
     * 和服务器的同步类型（0客户端先表现；1等服务器返回了再表现）
     * @return
     */
    public final int getServer_sync_type(){
        return server_sync_type;
    }
    /**
     * 0主动、 1被动、2特杀
     */
    private final int type;
    /**
     * 0主动、 1被动、2特杀
     * @return
     */
    public final int getType(){
        return type;
    }
    /**
     * 技能额外参数(0_怪物特杀类型_增伤万分比,1_属性_值;2_技能id_属性) 0是特杀1属性加成，2是技能加成(@;@_@)
     */
    private final ReadIntegerArrayEs params;
    /**
     * 技能额外参数(0_怪物特杀类型_增伤万分比,1_属性_值;2_技能id_属性) 0是特杀1属性加成，2是技能加成(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getParams(){
        return params;
    }
    /**
     * 增加百分比属性(属性ID_数值）(@;@_@)
     */
    private final ReadIntegerArrayEs params_pre_att;
    /**
     * 增加百分比属性(属性ID_数值）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getParams_pre_att(){
        return params_pre_att;
    }
    /**
     * 使用者（0-4，职业1,2,3,4,5技能；10：无限制；11怪物技能；12：宠物技能；13：婚姻技能；14：法宝技能
     */
    private final int user_type;
    /**
     * 使用者（0-4，职业1,2,3,4,5技能；10：无限制；11怪物技能；12：宠物技能；13：婚姻技能；14：法宝技能
     * @return
     */
    public final int getUser_type(){
        return user_type;
    }
    /**
     * 冷却时间（单位毫秒）
     */
    private final int cd;
    /**
     * 冷却时间（单位毫秒）
     * @return
     */
    public final int getCd(){
        return cd;
    }
    /**
     * 公共CD（单位毫秒）
     */
    private final int public_cd;
    /**
     * 公共CD（单位毫秒）
     * @return
     */
    public final int getPublic_cd(){
        return public_cd;
    }
    /**
     * 是否使用公CD（0不使用，1使用）
     */
    private final int use_public_cd;
    /**
     * 是否使用公CD（0不使用，1使用）
     * @return
     */
    public final int getUse_public_cd(){
        return use_public_cd;
    }
    /**
     * 需求人物等级
     */
    private final int need_player_level;
    /**
     * 需求人物等级
     * @return
     */
    public final int getNeed_player_level(){
        return need_player_level;
    }
    /**
     * 技能大类型
     */
    private final int element_type;
    /**
     * 技能大类型
     * @return
     */
    public final int getElement_type(){
        return element_type;
    }
    /**
     * 技能的等级（初中高）
     */
    private final int level;
    /**
     * 技能的等级（初中高）
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 伤害分担或加深 -1 分担 1 加深 0无效果 client ignore
     */
    private final int share;
    /**
     * 伤害分担或加深 -1 分担 1 加深 0无效果 client ignore
     * @return
     */
    public final int getShare(){
        return share;
    }
    /**
     * 升级战斗力增长 client ignore
     */
    private final int fight_num;
    /**
     * 升级战斗力增长 client ignore
     * @return
     */
    public final int getFight_num(){
        return fight_num;
    }
    /**
     * 是否获得就生效 client ignore
     */
    private final int if_get;
    /**
     * 是否获得就生效 client ignore
     * @return
     */
    public final int getIf_get(){
        return if_get;
    }
    /**
     * 获得生效的BUFFID  client ignore
     */
    private final int if_get_params;
    /**
     * 获得生效的BUFFID  client ignore
     * @return
     */
    public final int getIf_get_params(){
        return if_get_params;
    }
    /**
     * 伤害修正系数 client ignore
     */
    private final int damageOffset;
    /**
     * 伤害修正系数 client ignore
     * @return
     */
    public final int getDamageOffset(){
        return damageOffset;
    }
    /**
     * 伤害基础值
     */
    private final int damageBase;
    /**
     * 伤害基础值
     * @return
     */
    public final int getDamageBase(){
        return damageBase;
    }
    /**
     * 伤害提升值
     */
    private final int damageUp;
    /**
     * 伤害提升值
     * @return
     */
    public final int getDamageUp(){
        return damageUp;
    }
    /**
     * 护甲伤害基础值 client ignore
     */
    private final int ArmordamageBase;
    /**
     * 护甲伤害基础值 client ignore
     * @return
     */
    public final int getArmordamageBase(){
        return ArmordamageBase;
    }
    /**
     * 护甲伤害提升值 client ignore
     */
    private final int ArmordamageUp;
    /**
     * 护甲伤害提升值 client ignore
     * @return
     */
    public final int getArmordamageUp(){
        return ArmordamageUp;
    }
    /**
     * 获得的效果，填写server_Skill_Trigger表中的ID；如果是玩家技能填写，则该效果是专属于该技能的，如果是被动技能填写，该效果是全局的。client ignore
     */
    private final ReadIntegerArray Trigger;
    /**
     * 获得的效果，填写server_Skill_Trigger表中的ID；如果是玩家技能填写，则该效果是专属于该技能的，如果是被动技能填写，该效果是全局的。client ignore
     * @return
     */
    public final ReadIntegerArray getTrigger(){
        return Trigger;
    }
    /**
     * XP技能对于时长怪的伤害放大系数
     */
    private final int xp_skill_damage;
    /**
     * XP技能对于时长怪的伤害放大系数
     * @return
     */
    public final int getXp_skill_damage(){
        return xp_skill_damage;
    }

    public Cfg_Skill_Bean(int id,int cell_id,int icon,String VisualDef,int server_sync_type,int type,String paramsStr,String params_pre_attStr,int user_type,int cd,int public_cd,int use_public_cd,int need_player_level,int element_type,int level,int share,int fight_num,int if_get,int if_get_params,int damageOffset,int damageBase,int damageUp,int ArmordamageBase,int ArmordamageUp,String TriggerStr,int xp_skill_damage){
        this.id = id;
        this.cell_id = cell_id;
        this.icon = icon;
        this.VisualDef = VisualDef;
        this.server_sync_type = server_sync_type;
        this.type = type;
        this.params = new ReadIntegerArrayEs(paramsStr,"}",",");
        this.params_pre_att = new ReadIntegerArrayEs(params_pre_attStr,"}",",");
        this.user_type = user_type;
        this.cd = cd;
        this.public_cd = public_cd;
        this.use_public_cd = use_public_cd;
        this.need_player_level = need_player_level;
        this.element_type = element_type;
        this.level = level;
        this.share = share;
        this.fight_num = fight_num;
        this.if_get = if_get;
        this.if_get_params = if_get_params;
        this.damageOffset = damageOffset;
        this.damageBase = damageBase;
        this.damageUp = damageUp;
        this.ArmordamageBase = ArmordamageBase;
        this.ArmordamageUp = ArmordamageUp;
        this.Trigger = new ReadIntegerArray(TriggerStr,",");
        this.xp_skill_damage = xp_skill_damage;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("cell_id:").append(cell_id).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("VisualDef:").append(VisualDef).append(";");
        str.append("server_sync_type:").append(server_sync_type).append(";");
        str.append("type:").append(type).append(";");
        str.append("params:").append(params).append(";");
        str.append("params_pre_att:").append(params_pre_att).append(";");
        str.append("user_type:").append(user_type).append(";");
        str.append("cd:").append(cd).append(";");
        str.append("public_cd:").append(public_cd).append(";");
        str.append("use_public_cd:").append(use_public_cd).append(";");
        str.append("need_player_level:").append(need_player_level).append(";");
        str.append("element_type:").append(element_type).append(";");
        str.append("level:").append(level).append(";");
        str.append("share:").append(share).append(";");
        str.append("fight_num:").append(fight_num).append(";");
        str.append("if_get:").append(if_get).append(";");
        str.append("if_get_params:").append(if_get_params).append(";");
        str.append("damageOffset:").append(damageOffset).append(";");
        str.append("damageBase:").append(damageBase).append(";");
        str.append("damageUp:").append(damageUp).append(";");
        str.append("ArmordamageBase:").append(ArmordamageBase).append(";");
        str.append("ArmordamageUp:").append(ArmordamageUp).append(";");
        str.append("Trigger:").append(Trigger).append(";");
        str.append("xp_skill_damage:").append(xp_skill_damage).append(";");
        return str.toString();
    }
}
