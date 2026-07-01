/**
 * Auto generated, do not edit it
 *
 * monster配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadIntegerArrayEs; 
	
public class Cfg_Monster_Bean{
    /**
     * 怪物id
     */
    private final int id;
    /**
     * 怪物id
     * @return
     */
    public final int getId(){
        return id;
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
     * 怪物气泡对白
     */
    private final String dialog;
    /**
     * 怪物气泡对白
     * @return
     */
    public final String getDialog(){
        return dialog;
    }
    /**
     * 阵营(0玩家 3怪物)
     */
    private final int camp;
    /**
     * 阵营(0玩家 3怪物)
     * @return
     */
    public final int getCamp(){
        return camp;
    }
    /**
     * 怪物类型(1普通小怪,2精英,3BOSS
     */
    private final int monster_type;
    /**
     * 怪物类型(1普通小怪,2精英,3BOSS
     * @return
     */
    public final int getMonster_type(){
        return monster_type;
    }
    /**
     * BOSS类型（0或者空，不分类；1，世界BOSS；2，BOSS之家；3，套装BOSS（晶甲和域）；4，宝石BOSS；5，本服神兽岛（废弃）；6，跨服神兽岛；7，跨服领地战，8，境界BOSS（个人首领），9，天墟战场BOSS，10，无限层世界BOSS，11新手BOSS、12晶甲精英，13境界特殊BOSS引导用，14荒古神坛 15古魔封印BOSS，16新BOSS之家，17混沌虚空BOSS，18，须弥宝库BOSS，19除魔团BOSS）client ignore
     */
    private final int BOSS_type;
    /**
     * BOSS类型（0或者空，不分类；1，世界BOSS；2，BOSS之家；3，套装BOSS（晶甲和域）；4，宝石BOSS；5，本服神兽岛（废弃）；6，跨服神兽岛；7，跨服领地战，8，境界BOSS（个人首领），9，天墟战场BOSS，10，无限层世界BOSS，11新手BOSS、12晶甲精英，13境界特殊BOSS引导用，14荒古神坛 15古魔封印BOSS，16新BOSS之家，17混沌虚空BOSS，18，须弥宝库BOSS，19除魔团BOSS）client ignore
     * @return
     */
    public final int getBOSS_type(){
        return BOSS_type;
    }
    /**
     * 怪物攻击模式（0不反击，1主动，2被动）
     */
    private final int attack_type;
    /**
     * 怪物攻击模式（0不反击，1主动，2被动）
     * @return
     */
    public final int getAttack_type(){
        return attack_type;
    }
    /**
     * 怪物超过多少范围后清除仇恨 client ignore
     */
    private final int monster_pos;
    /**
     * 怪物超过多少范围后清除仇恨 client ignore
     * @return
     */
    public final int getMonster_pos(){
        return monster_pos;
    }
    /**
     * 是否使用玩家模型(0不使用，1使用)
     */
    private final int PlayerModel;
    /**
     * 是否使用玩家模型(0不使用，1使用)
     * @return
     */
    public final int getPlayerModel(){
        return PlayerModel;
    }
    /**
     * 身体ID_武器head_武器Body_武器特效_翅膀ID
     */
    private final ReadIntegerArray PlayerModelRes;
    /**
     * 身体ID_武器head_武器Body_武器特效_翅膀ID
     * @return
     */
    public final ReadIntegerArray getPlayerModelRes(){
        return PlayerModelRes;
    }
    /**
     * 资源
     */
    private final int res;
    /**
     * 资源
     * @return
     */
    public final int getRes(){
        return res;
    }
    /**
     * 模型缩放（百分比）
     */
    private final int size_scale;
    /**
     * 模型缩放（百分比）
     * @return
     */
    public final int getSize_scale(){
        return size_scale;
    }
    /**
     * 受击半径（厘米）
     */
    private final int strike_distance;
    /**
     * 受击半径（厘米）
     * @return
     */
    public final int getStrike_distance(){
        return strike_distance;
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
     * 死亡时音效编号
     */
    private final int die_soundid;
    /**
     * 死亡时音效编号
     * @return
     */
    public final int getDie_soundid(){
        return die_soundid;
    }
    /**
     * 等级_x000D_
-1表示显示玩家等级_x000D_
0表示为世界等级_x000D_
>0表示具体的等级
     */
    private final int level;
    /**
     * 等级_x000D_
-1表示显示玩家等级_x000D_
0表示为世界等级_x000D_
>0表示具体的等级
     * @return
     */
    public final int getLevel(){
        return level;
    }
    /**
     * 怪物境界等级，用于威压
     */
    private final int state_level;
    /**
     * 怪物境界等级，用于威压
     * @return
     */
    public final int getState_level(){
        return state_level;
    }
    /**
     * 怪物战斗力（用于判断是否进行战斗力的压制，空或零都不进行战斗力压制）
     */
    private final int score;
    /**
     * 怪物战斗力（用于判断是否进行战斗力的压制，空或零都不进行战斗力压制）
     * @return
     */
    public final int getScore(){
        return score;
    }
    /**
     * 本级属性(@;@_@) （client ignore）
     */
    private final ReadIntegerArrayEs AttributeValue;
    /**
     * 本级属性(@;@_@) （client ignore）
     * @return
     */
    public final ReadIntegerArrayEs getAttributeValue(){
        return AttributeValue;
    }
    /**
     * 血量
     */
    private final long maxHp;
    /**
     * 血量
     * @return
     */
    public final long getMaxHp(){
        return maxHp;
    }
    /**
     * 0：走常规流程；
1：固定伤害，用hurtValue字段，显示固定值；
2：固定伤害，用hurtValue字段，显示属性计算造成的值
3：受伤固定掉血：被攻击则每秒掉血，不受攻击则不每秒掉血，用hurtValue字段，显示属性计算造成的值
4：时间固定掉血：出生后无论被攻击否都每秒掉血，用hurtValue字段，显示属性计算造成的值
5：单位时间内的掉血上限(时间填写在global) 实列:当beHurtType 字段填写5时,beHurtValue填写的数值为单位时间内的掉血上限

     */
    private final int beHurtType;
    /**
     * 0：走常规流程；
1：固定伤害，用hurtValue字段，显示固定值；
2：固定伤害，用hurtValue字段，显示属性计算造成的值
3：受伤固定掉血：被攻击则每秒掉血，不受攻击则不每秒掉血，用hurtValue字段，显示属性计算造成的值
4：时间固定掉血：出生后无论被攻击否都每秒掉血，用hurtValue字段，显示属性计算造成的值
5：单位时间内的掉血上限(时间填写在global) 实列:当beHurtType 字段填写5时,beHurtValue填写的数值为单位时间内的掉血上限

     * @return
     */
    public final int getBeHurtType(){
        return beHurtType;
    }
    /**
     * 固定伤害的具体值
     */
    private final int beHurtValue;
    /**
     * 固定伤害的具体值
     * @return
     */
    public final int getBeHurtValue(){
        return beHurtValue;
    }
    /**
     * 护甲量
     */
    private final int Armor;
    /**
     * 护甲量
     * @return
     */
    public final int getArmor(){
        return Armor;
    }
    /**
     * 护甲恢复CD client ignore
     */
    private final int Armor_CD;
    /**
     * 护甲恢复CD client ignore
     * @return
     */
    public final int getArmor_CD(){
        return Armor_CD;
    }
    /**
     * 护甲是否会掉（0，会掉；1,不会掉）
     */
    private final int Armor_if;
    /**
     * 护甲是否会掉（0，会掉；1,不会掉）
     * @return
     */
    public final int getArmor_if(){
        return Armor_if;
    }
    /**
     * 掉落经验
     */
    private final long exp;
    /**
     * 掉落经验
     * @return
     */
    public final long getExp(){
        return exp;
    }
    /**
     * 掉落的血之精魄
     */
    private final int blood;
    /**
     * 掉落的血之精魄
     * @return
     */
    public final int getBlood(){
        return blood;
    }
    /**
     * 巡逻距离半径(厘米) client ignore
     */
    private final int patrol;
    /**
     * 巡逻距离半径(厘米) client ignore
     * @return
     */
    public final int getPatrol(){
        return patrol;
    }
    /**
     * 特杀类型 client ignore
     */
    private final int beAttackType;
    /**
     * 特杀类型 client ignore
     * @return
     */
    public final int getBeAttackType(){
        return beAttackType;
    }
    /**
     * 巡逻间隔时间（毫秒） client ignore
     */
    private final int patrol_cd;
    /**
     * 巡逻间隔时间（毫秒） client ignore
     * @return
     */
    public final int getPatrol_cd(){
        return patrol_cd;
    }
    /**
     * 追击距离半径(厘米)本距离值不得小于怪物的巡逻距离值 client ignore
     */
    private final int pursuit;
    /**
     * 追击距离半径(厘米)本距离值不得小于怪物的巡逻距离值 client ignore
     * @return
     */
    public final int getPursuit(){
        return pursuit;
    }
    /**
     * 脱战距离半径(厘米)本距离值不得小于怪物的巡逻距离值 client ignore
     */
    private final int recoversuit;
    /**
     * 脱战距离半径(厘米)本距离值不得小于怪物的巡逻距离值 client ignore
     * @return
     */
    public final int getRecoversuit(){
        return recoversuit;
    }
    /**
     * 怪物使用的技能ID(@;@)client ignore
     */
    private final ReadIntegerArray use_skills;
    /**
     * 怪物使用的技能ID(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getUse_skills(){
        return use_skills;
    }
    /**
     * 固定掉血(具体掉血量) client ignore
     */
    private final long fixDecHp;
    /**
     * 固定掉血(具体掉血量) client ignore
     * @return
     */
    public final long getFixDecHp(){
        return fixDecHp;
    }
    /**
     * 脱战回血（1回血 0不回血）client ignore
     */
    private final long recoverHp;
    /**
     * 脱战回血（1回血 0不回血）client ignore
     * @return
     */
    public final long getRecoverHp(){
        return recoverHp;
    }
    /**
     * 怪物的重生时间(单位：毫秒 0和负数代表不能重生) client ignore
     */
    private final long revive_time;
    /**
     * 怪物的重生时间(单位：毫秒 0和负数代表不能重生) client ignore
     * @return
     */
    public final long getRevive_time(){
        return revive_time;
    }
    /**
     * 关联的脚本ID client ignore
     */
    private final long script_id;
    /**
     * 关联的脚本ID client ignore
     * @return
     */
    public final long getScript_id(){
        return script_id;
    }
    /**
     * ai列表(@;@)client ignore
     */
    private final ReadIntegerArray ai;
    /**
     * ai列表(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getAi(){
        return ai;
    }
    /**
     * 普通掉落(@;@)client ignore
     */
    private final ReadIntegerArray normaldrop;
    /**
     * 普通掉落(@;@)client ignore
     * @return
     */
    public final ReadIntegerArray getNormaldrop(){
        return normaldrop;
    }
    /**
     * 职业掉落(@;@)client ignore
     */
    private final ReadIntegerArrayEs professional_drop;
    /**
     * 职业掉落(@;@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getProfessional_drop(){
        return professional_drop;
    }
    /**
     * 共享掉落（所有伤害排名上的玩家共享同一份掉落，几率完全随机）client ignore
     */
    private final ReadIntegerArray shareDrop;
    /**
     * 共享掉落（所有伤害排名上的玩家共享同一份掉落，几率完全随机）client ignore
     * @return
     */
    public final ReadIntegerArray getShareDrop(){
        return shareDrop;
    }
    /**
     * 掉落类型，0，所有人都有，1，伤害最高的单人   2，伤害最高的队伍共享 client ignore
     */
    private final int drop_belong;
    /**
     * 掉落类型，0，所有人都有，1，伤害最高的单人   2，伤害最高的队伍共享 client ignore
     * @return
     */
    public final int getDrop_belong(){
        return drop_belong;
    }
    /**
     * BOSS特殊掉落组client ignore
     */
    private final int specialdrop;
    /**
     * BOSS特殊掉落组client ignore
     * @return
     */
    public final int getSpecialdrop(){
        return specialdrop;
    }
    /**
     * 任务掉落client ignore
     */
    private final int taskdrop;
    /**
     * 任务掉落client ignore
     * @return
     */
    public final int getTaskdrop(){
        return taskdrop;
    }
    /**
     * 怪物隐藏（用于任务,任务类型_任务ID;任务类型_任务ID）(@;@_@)
     */
    private final ReadIntegerArrayEs taskShow;
    /**
     * 怪物隐藏（用于任务,任务类型_任务ID;任务类型_任务ID）(@;@_@)
     * @return
     */
    public final ReadIntegerArrayEs getTaskShow(){
        return taskShow;
    }
    /**
     * 转职任务掉落client ignore
     */
    private final int genderTaskdrop;
    /**
     * 转职任务掉落client ignore
     * @return
     */
    public final int getGenderTaskdrop(){
        return genderTaskdrop;
    }
    /**
     * 怪物任务是否共享client ignore
     */
    private final int share;
    /**
     * 怪物任务是否共享client ignore
     * @return
     */
    public final int getShare(){
        return share;
    }
    /**
     * 出生时间(毫秒)
     */
    private final int brithProtect;
    /**
     * 出生时间(毫秒)
     * @return
     */
    public final int getBrithProtect(){
        return brithProtect;
    }
    /**
     * 出生是否朝向玩家
     */
    private final int brithFowardPlayer;
    /**
     * 出生是否朝向玩家
     * @return
     */
    public final int getBrithFowardPlayer(){
        return brithFowardPlayer;
    }
    /**
     * 出生后给予的buff(client ignore)
     */
    private final int brithBUFF;
    /**
     * 出生后给予的buff(client ignore)
     * @return
     */
    public final int getBrithBUFF(){
        return brithBUFF;
    }
    /**
     * 出现篝火概率_ID(@_@)client ignore
     */
    private final ReadIntegerArray bonfire_ID;
    /**
     * 出现篝火概率_ID(@_@)client ignore
     * @return
     */
    public final ReadIntegerArray getBonfire_ID(){
        return bonfire_ID;
    }
    /**
     * yedai文件名(不带路径和后缀)client ignore
     */
    private final String yedai;
    /**
     * yedai文件名(不带路径和后缀)client ignore
     * @return
     */
    public final String getYedai(){
        return yedai;
    }
    /**
     * 脱战后是否重置AI  client ignore
     */
    private final int reset_ai;
    /**
     * 脱战后是否重置AI  client ignore
     * @return
     */
    public final int getReset_ai(){
        return reset_ai;
    }
    /**
     * 是否能够被选中
     */
    private final int can_be_select;
    /**
     * 是否能够被选中
     * @return
     */
    public final int getCan_be_select(){
        return can_be_select;
    }
    /**
     * 固定伤害中，自动掉血调用的系数
     */
    private final int monster_hp_change;
    /**
     * 固定伤害中，自动掉血调用的系数
     * @return
     */
    public final int getMonster_hp_change(){
        return monster_hp_change;
    }
    /**
     * 聚宝盆奖励（击杀后给与所有参加攻击的玩家给与的奖励数量）废弃
itemID_num
     */
    private final int magic_bowl_reward;
    /**
     * 聚宝盆奖励（击杀后给与所有参加攻击的玩家给与的奖励数量）废弃
itemID_num
     * @return
     */
    public final int getMagic_bowl_reward(){
        return magic_bowl_reward;
    }

    public Cfg_Monster_Bean(int id,String name,String dialog,int camp,int monster_type,int BOSS_type,int attack_type,int monster_pos,int PlayerModel,String PlayerModelResStr,int res,int size_scale,int strike_distance,int icon,int die_soundid,int level,int state_level,int score,String AttributeValueStr,long maxHp,int beHurtType,int beHurtValue,int Armor,int Armor_CD,int Armor_if,long exp,int blood,int patrol,int beAttackType,int patrol_cd,int pursuit,int recoversuit,String use_skillsStr,long fixDecHp,long recoverHp,long revive_time,long script_id,String aiStr,String normaldropStr,String professional_dropStr,String shareDropStr,int drop_belong,int specialdrop,int taskdrop,String taskShowStr,int genderTaskdrop,int share,int brithProtect,int brithFowardPlayer,int brithBUFF,String bonfire_IDStr,String yedai,int reset_ai,int can_be_select,int monster_hp_change,int magic_bowl_reward){
        this.id = id;
        this.name = name;
        this.dialog = dialog;
        this.camp = camp;
        this.monster_type = monster_type;
        this.BOSS_type = BOSS_type;
        this.attack_type = attack_type;
        this.monster_pos = monster_pos;
        this.PlayerModel = PlayerModel;
        this.PlayerModelRes = new ReadIntegerArray(PlayerModelResStr,",");
        this.res = res;
        this.size_scale = size_scale;
        this.strike_distance = strike_distance;
        this.icon = icon;
        this.die_soundid = die_soundid;
        this.level = level;
        this.state_level = state_level;
        this.score = score;
        this.AttributeValue = new ReadIntegerArrayEs(AttributeValueStr,"}",",");
        this.maxHp = maxHp;
        this.beHurtType = beHurtType;
        this.beHurtValue = beHurtValue;
        this.Armor = Armor;
        this.Armor_CD = Armor_CD;
        this.Armor_if = Armor_if;
        this.exp = exp;
        this.blood = blood;
        this.patrol = patrol;
        this.beAttackType = beAttackType;
        this.patrol_cd = patrol_cd;
        this.pursuit = pursuit;
        this.recoversuit = recoversuit;
        this.use_skills = new ReadIntegerArray(use_skillsStr,",");
        this.fixDecHp = fixDecHp;
        this.recoverHp = recoverHp;
        this.revive_time = revive_time;
        this.script_id = script_id;
        this.ai = new ReadIntegerArray(aiStr,",");
        this.normaldrop = new ReadIntegerArray(normaldropStr,",");
        this.professional_drop = new ReadIntegerArrayEs(professional_dropStr,"}",",");
        this.shareDrop = new ReadIntegerArray(shareDropStr,",");
        this.drop_belong = drop_belong;
        this.specialdrop = specialdrop;
        this.taskdrop = taskdrop;
        this.taskShow = new ReadIntegerArrayEs(taskShowStr,"}",",");
        this.genderTaskdrop = genderTaskdrop;
        this.share = share;
        this.brithProtect = brithProtect;
        this.brithFowardPlayer = brithFowardPlayer;
        this.brithBUFF = brithBUFF;
        this.bonfire_ID = new ReadIntegerArray(bonfire_IDStr,",");
        this.yedai = yedai;
        this.reset_ai = reset_ai;
        this.can_be_select = can_be_select;
        this.monster_hp_change = monster_hp_change;
        this.magic_bowl_reward = magic_bowl_reward;
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id:").append(id).append(";");
        str.append("name:").append(name).append(";");
        str.append("dialog:").append(dialog).append(";");
        str.append("camp:").append(camp).append(";");
        str.append("monster_type:").append(monster_type).append(";");
        str.append("BOSS_type:").append(BOSS_type).append(";");
        str.append("attack_type:").append(attack_type).append(";");
        str.append("monster_pos:").append(monster_pos).append(";");
        str.append("PlayerModel:").append(PlayerModel).append(";");
        str.append("PlayerModelRes:").append(PlayerModelRes).append(";");
        str.append("res:").append(res).append(";");
        str.append("size_scale:").append(size_scale).append(";");
        str.append("strike_distance:").append(strike_distance).append(";");
        str.append("icon:").append(icon).append(";");
        str.append("die_soundid:").append(die_soundid).append(";");
        str.append("level:").append(level).append(";");
        str.append("state_level:").append(state_level).append(";");
        str.append("score:").append(score).append(";");
        str.append("AttributeValue:").append(AttributeValue).append(";");
        str.append("maxHp:").append(maxHp).append(";");
        str.append("beHurtType:").append(beHurtType).append(";");
        str.append("beHurtValue:").append(beHurtValue).append(";");
        str.append("Armor:").append(Armor).append(";");
        str.append("Armor_CD:").append(Armor_CD).append(";");
        str.append("Armor_if:").append(Armor_if).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("blood:").append(blood).append(";");
        str.append("patrol:").append(patrol).append(";");
        str.append("beAttackType:").append(beAttackType).append(";");
        str.append("patrol_cd:").append(patrol_cd).append(";");
        str.append("pursuit:").append(pursuit).append(";");
        str.append("recoversuit:").append(recoversuit).append(";");
        str.append("use_skills:").append(use_skills).append(";");
        str.append("fixDecHp:").append(fixDecHp).append(";");
        str.append("recoverHp:").append(recoverHp).append(";");
        str.append("revive_time:").append(revive_time).append(";");
        str.append("script_id:").append(script_id).append(";");
        str.append("ai:").append(ai).append(";");
        str.append("normaldrop:").append(normaldrop).append(";");
        str.append("professional_drop:").append(professional_drop).append(";");
        str.append("shareDrop:").append(shareDrop).append(";");
        str.append("drop_belong:").append(drop_belong).append(";");
        str.append("specialdrop:").append(specialdrop).append(";");
        str.append("taskdrop:").append(taskdrop).append(";");
        str.append("taskShow:").append(taskShow).append(";");
        str.append("genderTaskdrop:").append(genderTaskdrop).append(";");
        str.append("share:").append(share).append(";");
        str.append("brithProtect:").append(brithProtect).append(";");
        str.append("brithFowardPlayer:").append(brithFowardPlayer).append(";");
        str.append("brithBUFF:").append(brithBUFF).append(";");
        str.append("bonfire_ID:").append(bonfire_ID).append(";");
        str.append("yedai:").append(yedai).append(";");
        str.append("reset_ai:").append(reset_ai).append(";");
        str.append("can_be_select:").append(can_be_select).append(";");
        str.append("monster_hp_change:").append(monster_hp_change).append(";");
        str.append("magic_bowl_reward:").append(magic_bowl_reward).append(";");
        return str.toString();
    }
}
