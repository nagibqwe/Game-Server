/**
 * Auto generated, do not edit it
 *
 * characters配置表
 */
package com.data.bean;

import com.data.struct.ReadIntegerArrayEs; 
import com.data.struct.ReadLongArrayEs; 
import com.data.struct.ReadIntegerArray; 
import com.data.struct.ReadLongArray; 
	
public class Cfg_Characters_Bean{
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
     * 升级所需经验(当前级别升下级的经验)
     */
    private final long exp;
    /**
     * 升级所需经验(当前级别升下级的经验)
     * @return
     */
    public final long getExp(){
        return exp;
    }
    /**
     * 本级属性(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs AttributeValue;
    /**
     * 本级属性(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getAttributeValue(){
        return AttributeValue;
    }
    /**
     * 升级自动获取分支技能（0为女1为男）格式为：职业_分支技能；职业_分支技能(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs skill;
    /**
     * 升级自动获取分支技能（0为女1为男）格式为：职业_分支技能；职业_分支技能(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getSkill(){
        return skill;
    }
    /**
     * 升级自动获得的心法
     */
    private final int OccMental;
    /**
     * 升级自动获得的心法
     * @return
     */
    public final int getOccMental(){
        return OccMental;
    }
    /**
     * 升级自动获取技能（0为男剑；1为女枪）格式为：职业_技能；职业_技能 client ignore
     */
    private final ReadIntegerArrayEs OccSkill;
    /**
     * 升级自动获取技能（0为男剑；1为女枪）格式为：职业_技能；职业_技能 client ignore
     * @return
     */
    public final ReadIntegerArrayEs getOccSkill(){
        return OccSkill;
    }
    /**
     * 离线经验(每分钟) client ignore
     */
    private final int lixianxp;
    /**
     * 离线经验(每分钟) client ignore
     * @return
     */
    public final int getLixianxp(){
        return lixianxp;
    }
    /**
     * 打坐获得真气client ignore
     */
    private final int dazuozq;
    /**
     * 打坐获得真气client ignore
     * @return
     */
    public final int getDazuozq(){
        return dazuozq;
    }
    /**
     * 打坐回复生命值client ignore
     */
    private final int dazuohp;
    /**
     * 打坐回复生命值client ignore
     * @return
     */
    public final int getDazuohp(){
        return dazuohp;
    }
    /**
     * 夜晚打坐系数client ignore
     */
    private final int dazuo_night_coefficient;
    /**
     * 夜晚打坐系数client ignore
     * @return
     */
    public final int getDazuo_night_coefficient(){
        return dazuo_night_coefficient;
    }
    /**
     * 打坐最大收益次数client ignore
     */
    private final int dazuo_max_times;
    /**
     * 打坐最大收益次数client ignore
     * @return
     */
    public final int getDazuo_max_times(){
        return dazuo_max_times;
    }
    /**
     * 自动回复生命值 client ignore
     */
    private final int auto_recover_hp;
    /**
     * 自动回复生命值 client ignore
     * @return
     */
    public final int getAuto_recover_hp(){
        return auto_recover_hp;
    }
    /**
     * 崇拜经验奖励client ignore
     */
    private final int worship_exp;
    /**
     * 崇拜经验奖励client ignore
     * @return
     */
    public final int getWorship_exp(){
        return worship_exp;
    }
    /**
     * 崇拜体力奖励client ignore
     */
    private final int worship_soul;
    /**
     * 崇拜体力奖励client ignore
     * @return
     */
    public final int getWorship_soul(){
        return worship_soul;
    }
    /**
     * 篝火经验
client ignore
     */
    private final long bonfire;
    /**
     * 篝火经验
client ignore
     * @return
     */
    public final long getBonfire(){
        return bonfire;
    }
    /**
     * 双倍经验累积上限（client ignore）
     */
    private final long expPool;
    /**
     * 双倍经验累积上限（client ignore）
     * @return
     */
    public final long getExpPool(){
        return expPool;
    }
    /**
     * 推荐战斗力（client ignore）
     */
    private final int power;
    /**
     * 推荐战斗力（client ignore）
     * @return
     */
    public final int getPower(){
        return power;
    }
    /**
     * 超赞发题奖励(枚举_值;枚举_值)(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs send_praise_reward;
    /**
     * 超赞发题奖励(枚举_值;枚举_值)(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getSend_praise_reward(){
        return send_praise_reward;
    }
    /**
     * 普通发题奖励(枚举_值;枚举_值(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs send_ordinary_reward;
    /**
     * 普通发题奖励(枚举_值;枚举_值(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getSend_ordinary_reward(){
        return send_ordinary_reward;
    }
    /**
     * 坑爹发题奖励(枚举_值;枚举_值)(@;@_@) client ignore
     */
    private final ReadIntegerArrayEs send_low_reward;
    /**
     * 坑爹发题奖励(枚举_值;枚举_值)(@;@_@) client ignore
     * @return
     */
    public final ReadIntegerArrayEs getSend_low_reward(){
        return send_low_reward;
    }
    /**
     * 正确答题奖励(@;@_@)client ignore
     */
    private final ReadIntegerArrayEs correct_reward;
    /**
     * 正确答题奖励(@;@_@)client ignore
     * @return
     */
    public final ReadIntegerArrayEs getCorrect_reward(){
        return correct_reward;
    }
    /**
     * 错误答题奖励(@;@_@)correct_reward
     */
    private final ReadIntegerArrayEs error_reward;
    /**
     * 错误答题奖励(@;@_@)correct_reward
     * @return
     */
    public final ReadIntegerArrayEs getError_reward(){
        return error_reward;
    }
    /**
     * 竞技场胜利奖励经验[@;@_@] client ignore
     */
    private final ReadLongArrayEs jjc_reward_victory;
    /**
     * 竞技场胜利奖励经验[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getJjc_reward_victory(){
        return jjc_reward_victory;
    }
    /**
     * 竞技场失败奖励经验[@;@_@] client ignore
     */
    private final ReadLongArrayEs jjc_reward_fail;
    /**
     * 竞技场失败奖励经验[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getJjc_reward_fail(){
        return jjc_reward_fail;
    }
    /**
     * 公会战胜利奖励经验[@;@_@] client ignore
     */
    private final ReadLongArrayEs guild_fight_reward_victory;
    /**
     * 公会战胜利奖励经验[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getGuild_fight_reward_victory(){
        return guild_fight_reward_victory;
    }
    /**
     * 公会战失败奖励经验[@;@_@] client ignore
     */
    private final ReadLongArrayEs guild_fight_reward_fail;
    /**
     * 公会战失败奖励经验[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getGuild_fight_reward_fail(){
        return guild_fight_reward_fail;
    }
    /**
     * 经验副本每只怪经验 client ignore
     */
    private final long expclone_monster_exp;
    /**
     * 经验副本每只怪经验 client ignore
     * @return
     */
    public final long getExpclone_monster_exp(){
        return expclone_monster_exp;
    }
    /**
     * 经验副本怪物属性翻倍系数 （万分比）client ignore
     */
    private final int expclone_monster_att;
    /**
     * 经验副本怪物属性翻倍系数 （万分比）client ignore
     * @return
     */
    public final int getExpclone_monster_att(){
        return expclone_monster_att;
    }
    /**
     * 大能遗府怪物属性翻倍系数 （万分比）BOSS_小怪
client ignore
     */
    private final ReadIntegerArray Starclone_monster_att;
    /**
     * 大能遗府怪物属性翻倍系数 （万分比）BOSS_小怪
client ignore
     * @return
     */
    public final ReadIntegerArray getStarclone_monster_att(){
        return Starclone_monster_att;
    }
    /**
     * 王者对决失败奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs kingfight_lose_award;
    /**
     * 王者对决失败奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getKingfight_lose_award(){
        return kingfight_lose_award;
    }
    /**
     * 天芒鬼城经验奖励（9层，从1层到9层顺序填写）[@;@_@] client ignore
     */
    private final ReadLongArray YZZD_EXP_award;
    /**
     * 天芒鬼城经验奖励（9层，从1层到9层顺序填写）[@;@_@] client ignore
     * @return
     */
    public final ReadLongArray getYZZD_EXP_award(){
        return YZZD_EXP_award;
    }
    /**
     * 神魔战场积分经验奖励(5个，按照分数从低到高顺序填写）[@;@_@]
     */
    private final ReadLongArray SZZQ_EXP_award;
    /**
     * 神魔战场积分经验奖励(5个，按照分数从低到高顺序填写）[@;@_@]
     * @return
     */
    public final ReadLongArray getSZZQ_EXP_award(){
        return SZZQ_EXP_award;
    }
    /**
     * 神魔战场排行经验奖励（5个，按照排名从前到后的排名填写）[@;@_@]
     */
    private final ReadLongArray SZZQ_EXP_rank_award;
    /**
     * 神魔战场排行经验奖励（5个，按照排名从前到后的排名填写）[@;@_@]
     * @return
     */
    public final ReadLongArray getSZZQ_EXP_rank_award(){
        return SZZQ_EXP_rank_award;
    }
    /**
     * 世界答题经验奖励client ignore
     */
    private final long world_question_exp;
    /**
     * 世界答题经验奖励client ignore
     * @return
     */
    public final long getWorld_question_exp(){
        return world_question_exp;
    }
    /**
     * 公会怪物攻城小怪client ignore
     */
    private final long guild_monster_Intrusion_exp;
    /**
     * 公会怪物攻城小怪client ignore
     * @return
     */
    public final long getGuild_monster_Intrusion_exp(){
        return guild_monster_Intrusion_exp;
    }
    /**
     * 公会怪物攻城BOSS  client ignore
     */
    private final long guild_BOSSmonster_Intrusion_exp;
    /**
     * 公会怪物攻城BOSS  client ignore
     * @return
     */
    public final long getGuild_BOSSmonster_Intrusion_exp(){
        return guild_BOSSmonster_Intrusion_exp;
    }
    /**
     * 参加初级婚宴奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs dinner_1;
    /**
     * 参加初级婚宴奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getDinner_1(){
        return dinner_1;
    }
    /**
     * 参加中级婚宴奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs dinner_2;
    /**
     * 参加中级婚宴奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getDinner_2(){
        return dinner_2;
    }
    /**
     * 参加高级婚宴奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs dinner_3;
    /**
     * 参加高级婚宴奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getDinner_3(){
        return dinner_3;
    }
    /**
     * 魂兽森林每30秒增加经验client ignore
     */
    private final long bossSoulBeasts_exp;
    /**
     * 魂兽森林每30秒增加经验client ignore
     * @return
     */
    public final long getBossSoulBeasts_exp(){
        return bossSoulBeasts_exp;
    }
    /**
     * 王者联盟胜利奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs kingAlliance_win_award;
    /**
     * 王者联盟胜利奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getKingAlliance_win_award(){
        return kingAlliance_win_award;
    }
    /**
     * 王者联盟失败奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs kingAlliance_lose_award;
    /**
     * 王者联盟失败奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getKingAlliance_lose_award(){
        return kingAlliance_lose_award;
    }
    /**
     * 跨服炼丹提交丹药基础经验奖励[@;@_@] client ignore
     */
    private final ReadLongArrayEs kuafu_Alchemy_award;
    /**
     * 跨服炼丹提交丹药基础经验奖励[@;@_@] client ignore
     * @return
     */
    public final ReadLongArrayEs getKuafu_Alchemy_award(){
        return kuafu_Alchemy_award;
    }
    /**
     * 大能遗府星级副本扫荡需要战斗力，根据等级变化（client ignore）
     */
    private final int StarCopyNeedFightPower;
    /**
     * 大能遗府星级副本扫荡需要战斗力，根据等级变化（client ignore）
     * @return
     */
    public final int getStarCopyNeedFightPower(){
        return StarCopyNeedFightPower;
    }
    /**
     * 天界之门的怪物难度参数[@_@]client ignore
     */
    private final int sky_door_parm;
    /**
     * 天界之门的怪物难度参数[@_@]client ignore
     * @return
     */
    public final int getSky_door_parm(){
        return sky_door_parm;
    }
    /**
     * 当前等级的限制转职阶段（client ignore）
     */
    private final int changejob_limit;
    /**
     * 当前等级的限制转职阶段（client ignore）
     * @return
     */
    public final int getChangejob_limit(){
        return changejob_limit;
    }
    /**
     * 掌门传道每次经验奖励（4个难度依次填写）[@_@] 
     */
    private final long Leader_Preach_award;
    /**
     * 掌门传道每次经验奖励（4个难度依次填写）[@_@] 
     * @return
     */
    public final long getLeader_Preach_award(){
        return Leader_Preach_award;
    }
    /**
     * 婚姻副本每10秒增加经验 client ignore
     */
    private final long marry_single_add_exp;
    /**
     * 婚姻副本每10秒增加经验 client ignore
     * @return
     */
    public final long getMarry_single_add_exp(){
        return marry_single_add_exp;
    }
    /**
     * 婚姻副本使用物品增加经验配置 （可配置多个婚姻副本使用物品加经验的）
itemid_经验;itemid_经验
client ignore
     */
    private final ReadLongArrayEs marry_use_item_add_exp;
    /**
     * 婚姻副本使用物品增加经验配置 （可配置多个婚姻副本使用物品加经验的）
itemid_经验;itemid_经验
client ignore
     * @return
     */
    public final ReadLongArrayEs getMarry_use_item_add_exp(){
        return marry_use_item_add_exp;
    }
    /**
     * 升级获得得货币client ignore
     */
    private final ReadIntegerArray level_up_money;
    /**
     * 升级获得得货币client ignore
     * @return
     */
    public final ReadIntegerArray getLevel_up_money(){
        return level_up_money;
    }
    /**
     * 神女ID_护送经验;
     */
    private final ReadLongArrayEs Convoy_Exp;
    /**
     * 神女ID_护送经验;
     * @return
     */
    public final ReadLongArrayEs getConvoy_Exp(){
        return Convoy_Exp;
    }

    public Cfg_Characters_Bean(int level,long exp,String AttributeValueStr,String skillStr,int OccMental,String OccSkillStr,int lixianxp,int dazuozq,int dazuohp,int dazuo_night_coefficient,int dazuo_max_times,int auto_recover_hp,int worship_exp,int worship_soul,long bonfire,long expPool,int power,String send_praise_rewardStr,String send_ordinary_rewardStr,String send_low_rewardStr,String correct_rewardStr,String error_rewardStr,String jjc_reward_victoryStr,String jjc_reward_failStr,String guild_fight_reward_victoryStr,String guild_fight_reward_failStr,long expclone_monster_exp,int expclone_monster_att,String Starclone_monster_attStr,String kingfight_lose_awardStr,String YZZD_EXP_awardStr,String SZZQ_EXP_awardStr,String SZZQ_EXP_rank_awardStr,long world_question_exp,long guild_monster_Intrusion_exp,long guild_BOSSmonster_Intrusion_exp,String dinner_1Str,String dinner_2Str,String dinner_3Str,long bossSoulBeasts_exp,String kingAlliance_win_awardStr,String kingAlliance_lose_awardStr,String kuafu_Alchemy_awardStr,int StarCopyNeedFightPower,int sky_door_parm,int changejob_limit,long Leader_Preach_award,long marry_single_add_exp,String marry_use_item_add_expStr,String level_up_moneyStr,String Convoy_ExpStr){
        this.level = level;
        this.exp = exp;
        this.AttributeValue = new ReadIntegerArrayEs(AttributeValueStr,"}",",");
        this.skill = new ReadIntegerArrayEs(skillStr,"}",",");
        this.OccMental = OccMental;
        this.OccSkill = new ReadIntegerArrayEs(OccSkillStr,"}",",");
        this.lixianxp = lixianxp;
        this.dazuozq = dazuozq;
        this.dazuohp = dazuohp;
        this.dazuo_night_coefficient = dazuo_night_coefficient;
        this.dazuo_max_times = dazuo_max_times;
        this.auto_recover_hp = auto_recover_hp;
        this.worship_exp = worship_exp;
        this.worship_soul = worship_soul;
        this.bonfire = bonfire;
        this.expPool = expPool;
        this.power = power;
        this.send_praise_reward = new ReadIntegerArrayEs(send_praise_rewardStr,"}",",");
        this.send_ordinary_reward = new ReadIntegerArrayEs(send_ordinary_rewardStr,"}",",");
        this.send_low_reward = new ReadIntegerArrayEs(send_low_rewardStr,"}",",");
        this.correct_reward = new ReadIntegerArrayEs(correct_rewardStr,"}",",");
        this.error_reward = new ReadIntegerArrayEs(error_rewardStr,"}",",");
        this.jjc_reward_victory = new ReadLongArrayEs(jjc_reward_victoryStr,"}",",");
        this.jjc_reward_fail = new ReadLongArrayEs(jjc_reward_failStr,"}",",");
        this.guild_fight_reward_victory = new ReadLongArrayEs(guild_fight_reward_victoryStr,"}",",");
        this.guild_fight_reward_fail = new ReadLongArrayEs(guild_fight_reward_failStr,"}",",");
        this.expclone_monster_exp = expclone_monster_exp;
        this.expclone_monster_att = expclone_monster_att;
        this.Starclone_monster_att = new ReadIntegerArray(Starclone_monster_attStr,",");
        this.kingfight_lose_award = new ReadLongArrayEs(kingfight_lose_awardStr,"}",",");
        this.YZZD_EXP_award = new ReadLongArray(YZZD_EXP_awardStr,",");
        this.SZZQ_EXP_award = new ReadLongArray(SZZQ_EXP_awardStr,",");
        this.SZZQ_EXP_rank_award = new ReadLongArray(SZZQ_EXP_rank_awardStr,",");
        this.world_question_exp = world_question_exp;
        this.guild_monster_Intrusion_exp = guild_monster_Intrusion_exp;
        this.guild_BOSSmonster_Intrusion_exp = guild_BOSSmonster_Intrusion_exp;
        this.dinner_1 = new ReadLongArrayEs(dinner_1Str,"}",",");
        this.dinner_2 = new ReadLongArrayEs(dinner_2Str,"}",",");
        this.dinner_3 = new ReadLongArrayEs(dinner_3Str,"}",",");
        this.bossSoulBeasts_exp = bossSoulBeasts_exp;
        this.kingAlliance_win_award = new ReadLongArrayEs(kingAlliance_win_awardStr,"}",",");
        this.kingAlliance_lose_award = new ReadLongArrayEs(kingAlliance_lose_awardStr,"}",",");
        this.kuafu_Alchemy_award = new ReadLongArrayEs(kuafu_Alchemy_awardStr,"}",",");
        this.StarCopyNeedFightPower = StarCopyNeedFightPower;
        this.sky_door_parm = sky_door_parm;
        this.changejob_limit = changejob_limit;
        this.Leader_Preach_award = Leader_Preach_award;
        this.marry_single_add_exp = marry_single_add_exp;
        this.marry_use_item_add_exp = new ReadLongArrayEs(marry_use_item_add_expStr,"}",",");
        this.level_up_money = new ReadIntegerArray(level_up_moneyStr,",");
        this.Convoy_Exp = new ReadLongArrayEs(Convoy_ExpStr,"}",",");
    }


    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("level:").append(level).append(";");
        str.append("exp:").append(exp).append(";");
        str.append("AttributeValue:").append(AttributeValue).append(";");
        str.append("skill:").append(skill).append(";");
        str.append("OccMental:").append(OccMental).append(";");
        str.append("OccSkill:").append(OccSkill).append(";");
        str.append("lixianxp:").append(lixianxp).append(";");
        str.append("dazuozq:").append(dazuozq).append(";");
        str.append("dazuohp:").append(dazuohp).append(";");
        str.append("dazuo_night_coefficient:").append(dazuo_night_coefficient).append(";");
        str.append("dazuo_max_times:").append(dazuo_max_times).append(";");
        str.append("auto_recover_hp:").append(auto_recover_hp).append(";");
        str.append("worship_exp:").append(worship_exp).append(";");
        str.append("worship_soul:").append(worship_soul).append(";");
        str.append("bonfire:").append(bonfire).append(";");
        str.append("expPool:").append(expPool).append(";");
        str.append("power:").append(power).append(";");
        str.append("send_praise_reward:").append(send_praise_reward).append(";");
        str.append("send_ordinary_reward:").append(send_ordinary_reward).append(";");
        str.append("send_low_reward:").append(send_low_reward).append(";");
        str.append("correct_reward:").append(correct_reward).append(";");
        str.append("error_reward:").append(error_reward).append(";");
        str.append("jjc_reward_victory:").append(jjc_reward_victory).append(";");
        str.append("jjc_reward_fail:").append(jjc_reward_fail).append(";");
        str.append("guild_fight_reward_victory:").append(guild_fight_reward_victory).append(";");
        str.append("guild_fight_reward_fail:").append(guild_fight_reward_fail).append(";");
        str.append("expclone_monster_exp:").append(expclone_monster_exp).append(";");
        str.append("expclone_monster_att:").append(expclone_monster_att).append(";");
        str.append("Starclone_monster_att:").append(Starclone_monster_att).append(";");
        str.append("kingfight_lose_award:").append(kingfight_lose_award).append(";");
        str.append("YZZD_EXP_award:").append(YZZD_EXP_award).append(";");
        str.append("SZZQ_EXP_award:").append(SZZQ_EXP_award).append(";");
        str.append("SZZQ_EXP_rank_award:").append(SZZQ_EXP_rank_award).append(";");
        str.append("world_question_exp:").append(world_question_exp).append(";");
        str.append("guild_monster_Intrusion_exp:").append(guild_monster_Intrusion_exp).append(";");
        str.append("guild_BOSSmonster_Intrusion_exp:").append(guild_BOSSmonster_Intrusion_exp).append(";");
        str.append("dinner_1:").append(dinner_1).append(";");
        str.append("dinner_2:").append(dinner_2).append(";");
        str.append("dinner_3:").append(dinner_3).append(";");
        str.append("bossSoulBeasts_exp:").append(bossSoulBeasts_exp).append(";");
        str.append("kingAlliance_win_award:").append(kingAlliance_win_award).append(";");
        str.append("kingAlliance_lose_award:").append(kingAlliance_lose_award).append(";");
        str.append("kuafu_Alchemy_award:").append(kuafu_Alchemy_award).append(";");
        str.append("StarCopyNeedFightPower:").append(StarCopyNeedFightPower).append(";");
        str.append("sky_door_parm:").append(sky_door_parm).append(";");
        str.append("changejob_limit:").append(changejob_limit).append(";");
        str.append("Leader_Preach_award:").append(Leader_Preach_award).append(";");
        str.append("marry_single_add_exp:").append(marry_single_add_exp).append(";");
        str.append("marry_use_item_add_exp:").append(marry_use_item_add_exp).append(";");
        str.append("level_up_money:").append(level_up_money).append(";");
        str.append("Convoy_Exp:").append(Convoy_Exp).append(";");
        return str.toString();
    }
}
