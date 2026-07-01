package com.game.cooldown.structs;

/**
 * @author Administrator
 */
public enum CooldownTypes {

    //总公共冷却
    PUBLIC("PUBLIC"),
    //技能公共冷却
    SKILL_PUBLIC("SKILL_PUBLIC"),
    //技能冷却
    SKILL("SKILL"),
    //宠物手动技能技能冷却
    Player_Pet_CD_ManualSkill("Player_Pet_CD_ManualSkill"),
    //宠物技能冷却
    Player_Pet_CD_Skill("Player_Pet_CD_Skill"),
    //宠物技能冷却
    Player_Pet_Skill("Player_Pet_Skill"),
    //飞剑技能冷却
    Player_FlySowrd_CD_Skill("Player_FlySowrd_CD_Skill"),
    //聊天
    Chat_CD("Chat_CD"),
    //宠物召唤冷却
    PetCallCD("PetCallCD"),
    //pk值清理ce
    PkCleanCd("PkCleanCd"),
    //玩家翻滚
    RollMove("RollMove"),
    //翻滚CD
    RollCD("RollCD"),
    //怪物巡逻CD
    MonsterPatrol("MonsterPatrol"),
    //怪物愤怒cd
    MonsterAngerCD("MonsterAngerCD"),
    //怪物攻击CD
    MonsterSkill("MonsterSkill"),
    //怪物攻击动作
    MonsterAttackCD("MonsterAttackCD"),
    //怪物追击检测
    MonsterPursue("MonsterPursue"),
    //怪物检测仇恨对象脱战
    MonsterCheckHatred("MonsterCheckHatred"),
    //宠物移动Cd
    PetMove("PetMove"),
    //宠物复活
    PetRelive("PetRelive"),
    //移动
    Move("Move"),
    //切换地图
    ChangeMap("ChangeMap"),
    //技能触发CD
    SkillTrigger("SkillTrigger"),
    //跳出阻挡
    JumpBlock("JumpBlock"),
    //竞技场更换对手
    JJCChange("JJCChange"),
    //竞技场领奖CD
    JJCrewardCd("JJCrewardCd"),
    //Vip传送CD
    TransportCD("TransportCD"),
    //复活CD
    ReliveCD("ReliveCD"),
    //包裹整理冷确
    BAG_CLEAR("BAG_CLEAR"),
    //仓库整理冷确
    STORE_CLEAR("STORE_CLEAR"),
    //组队队长召唤冷却时间
    TEAM_CALL_CD("TEAM_CALL_CD"),
    //组队发布消息
    TEAM_NOTICE_CD("TEAM_NOTICE_CD"),
    //回血技能cd
    PLAYER_HP_CD("PLAYER_HP_CD"),
    //清除减益buff
    PLAYER_HARMFUL_BUFF("PLAYER_HARMFUL_BUFF"),
    // 离线挂机AI攻击频率CD
    OFFLINE_AI_ATK_CD("OFFLINE_AI_ATK_CD"),
    //提示冷却
    NOTIFY_CD("NOTIFY_CD"),
    //被动触发技能使用
    DOUBLE_SKILL_CD("NOTIFY_CD"),
    //公会喊话
    GUILD_RECRUIT_CD("GUILD_RECRUIT_CD"),
    //仙缘. 求婚
    MARRY_CD("MARRY_CD"),
    //仙缘. 请求购买冷却
    MARRY_BUY_CD("MARRY_BUY_CD"),
    //仙缘.离婚申请
    MARRY_DIVORCE_CD("MARRY_DIVORCE_CD"),
    //灵压冷却时间
    STATESTIFLE_CD("STATESTIFLE_CD"),
    //工会一键招人CD
    Guild_Recruit_BbSub_cd("Guild_Recruit_BbSub_cd"),
    //死亡求援CD
    DIE_CALL_HELP_CD("DIE_CALL_HELP_CD"),
    //公会战召集CD
    GUILDBATTLECALL_CD("GUILDBATTLECALL_CD"),
    //buff命中后CD
    BUFFHIT_CD("BUFFHIT_CD"),
    //灵力受击CD
    LINGLI_CD("LINGLI_CD");

    private final String value;

    CooldownTypes(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
