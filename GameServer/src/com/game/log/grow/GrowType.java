package com.game.log.grow;

/**
 * 玩家成长类型
 */
public enum GrowType {
    //坐骑 1
    horse_level_up                  (1,ActType.levelUp,"坐骑升级"),
    horse_soul_up                   (9,ActType.levelUp,"坐骑御魂升级"),
    horse_soul_stageUp              (9,ActType.stageUp,"坐骑御魂升阶"),
    horse_active                    (12,ActType.active,"坐骑激活"),
    horse_star_up                   (12,ActType.star_up,"坐骑升星"),
    //坐骑装备 1
    horse_equip_active              (26,ActType.active,"坐骑脉轮激活"),
    horse_equip_wear                (60,ActType.equip_wear, BiType.equip,"坐骑装备穿戴"),
    horse_equip_synthesis           (60,ActType.equip_synthesis, BiType.equip,"坐骑装备合成"),
    horse_equip_intensify           (60,ActType.equip_intensify, BiType.equip,"坐骑装备强化"),
    horse_equip_soul                (60,ActType.equip_soul, BiType.equip,"坐骑装备附魂"),
    horse_equip_intensify_active    (26,ActType.intensify_active,"坐骑装备强化激活"),
    horse_equip_soul_active         (26,ActType.soul_active,"坐骑装备附魂激活"),

    //古籍 1
    book_active                     (4,ActType.active,"古籍激活"),

    //剑灵 1
    flySword_active                 (20,ActType.active,"剑灵激活"),
    flySword_level_up               (20,ActType.levelUp,"剑灵升级"),
    flySword_stageUp                (20,ActType.stageUp,"剑灵升阶"),

    //灵魂 1
    soul_wear                       (22,ActType.active,"灵魄镶嵌"),
//    soul_change                     (21,ActType.active,"灵魄替换"),
    soul_level_up                   (22,ActType.levelUp,"灵魄升级"),
//    soul_                           (21,ActType.active,"灵魄分解"),
//    soul_                           (21,ActType.active,"灵魄兑换"),
//    soul_                           (21,ActType.active,"灵魄合成"),

    //仙甲 1
    immortalEquip_wear              (2,ActType.equip_wear, BiType.equip,"仙甲穿戴"),
    immortalEquip_synthesis         (2,ActType.equip_synthesis, BiType.equip,"仙甲合成"),

    //技能成长 0
    skill_active                    (6,ActType.active,"技能激活"),
    skill_up                        (6,ActType.levelUp,"技能升级"),
    skill_star                      (6,ActType.star_up,"技能升星"),
    skill_channel_up                (28,ActType.levelUp,"经脉升级"),
    skill_channel_reset             (28,ActType.reset,"经脉重置"),
//    skill_setting                   (6,ActType.active,"技能装配"),
    //心法
    skill_mental_active             (8,ActType.active,"技能心法选择"),
    skill_mental_reset              (8,ActType.reset,"技能心法重置"),

//    nature_active                   (2,ActType.active,"造化激活"),
    //法宝 1
    stifle_active                   (14,ActType.active,"法宝激活"),
    stifle_star_up                  (14,ActType.levelUp,"法宝升星"),
    stifle_level_up                 (3,ActType.levelUp,"法宝升级"),
    stifle_soul_up                  (42,ActType.levelUp,"法宝御魂升级"),
    stifle_soul_stageUp             (42,ActType.stageUp,"法宝御魂升阶"),
    stifle_spirit_evolution         (15,ActType.evolution,"法宝灵器进化"),
    stifle_spirit_active            (15,ActType.active,"法宝灵器激活"),

    //宠物 1
    pet_active                      (24,ActType.active,"宠物化形激活"),
    pet_star_up                     (24,ActType.star_up,"宠物化形升星"),
    pet_level_up                    (7,ActType.levelUp,"宠物升级"),
    pet_soul_up                     (11,ActType.levelUp,"宠物御魂升级"),
    pet_soul_stageUp                (11,ActType.stageUp,"宠物御魂升阶"),
    pet_assistant_unlock            (25,ActType.unlock,"宠物助阵解锁"),
    pet_assistant_active            (25,ActType.active,"宠物助阵上阵"),
    //宠物装备 1
    pet_equip_wear                  (50,ActType.equip_wear, BiType.equip,"宠物装备穿戴"),
    pet_equip_intensify             (50,ActType.equip_intensify, BiType.equip,"宠物装备强化"),
    pet_equip_soul                  (50,ActType.equip_soul, BiType.equip,"宠物装备附魂"),
    pet_equip_synthesis             (50,ActType.equip_synthesis, BiType.equip,"宠物装备合成"),
    pet_equip_intensify_active      (25,ActType.intensify_active,"宠物装备强化激活"),
    pet_equip_soul_active           (25,ActType.soul_active,"宠物装备附魂激活"),

    //炼器  1
    equip_wear                      (1,ActType.equip_wear, BiType.equip,"装备穿戴"),
    equip_intensify                 (1,ActType.equip_intensify, BiType.equip,"装备强化"),
    equip_synthesis                 (1,ActType.equip_synthesis, BiType.equip,"装备合成"),
    equip_wash                      (1,ActType.equip_wash, BiType.equip,"装备洗练"),
    equip_suit_active               (1,ActType.equip_suit_active, BiType.equip,"装备套装激活"),
    gem_inlay                       (1,ActType.gem_inlay, BiType.equip,"宝石镶嵌"),
    gem_level_up                    (1,ActType.gem_level_up, BiType.equip,"宝石升级"),
    gem_refine                      (1,ActType.gem_refine, BiType.equip,"宝石精炼"),
    

    //助战(神兽) 1
    soulBeast_active                (17,ActType.active,"神兽激活"),
    soulBeast_goBack                (17,ActType.goBack,"神兽收回"),
    soulBeast_equip_wear            (4,ActType.equip_wear, BiType.equip,"神兽装备穿戴"),
    soulBeast_equip_synthesis       (4,ActType.equip_synthesis, BiType.equip,"神兽装备合成"),
    soulBeast_equip_intensify       (4,ActType.equip_intensify, BiType.equip,"神兽装备强化"),

    //婚姻 1
    marry_child_active              (18,ActType.active,"婚姻仙娃激活"),
    marry_child_levelUp             (18,ActType.levelUp,"婚姻仙娃升级"),
    marry_lock_active               (19,ActType.active,"婚姻心锁激活"),
    marry_lock_levelUp              (19,ActType.levelUp,"婚姻心锁升级"),

    //圣装 1
    holyEquipInlay                  (3,ActType.equip_wear, BiType.equip,"圣装穿戴"),
//    holyEquip_resolve               (3,ActType.resolve, BiType.equip,"圣装分解"),
    holyEquip_intensify             (3,ActType.equip_intensify, BiType.equip,"圣装强化"),
    holyEquip_synthesis             (3,ActType.equip_synthesis, BiType.equip,"圣装觉醒(合成)"),
    holyEquip_useSoul               (23,ActType.levelUp,"圣装圣魂使用"),

    //时装  1
    fashion_body_active             (30,ActType.active,"时装衣服激活"),
    fashion_body_starUp             (30,ActType.star_up,"时装衣服升星"),
    fashion_weapon_active           (31,ActType.active,"时装武器激活"),
    fashion_weapon_starUp           (31,ActType.star_up,"时装武器升星"),
    fashion_horse_active            (32,ActType.active,"时装坐骑激活"),
    fashion_horse_starUp            (32,ActType.star_up,"时装坐骑升星"),
    fashion_pet_active              (33,ActType.active,"时装宠物激活"),
    fashion_pet_starUp              (33,ActType.star_up,"时装宠物升星"),
    fashion_stifle_active           (34,ActType.active,"时装法宝激活"),
    fashion_stifle_starUp           (34,ActType.star_up,"时装法宝升星"),
    fashion_chat_active             (37,ActType.active,"聊天气泡激活"),
    fashion_chat_starUp             (37,ActType.star_up,"聊天气泡升星"),
    fashion_head_active             (35,ActType.active,"头像激活"),
    fashion_head_starUp             (35,ActType.star_up,"头像升星"),
    fashion_headFrame_active        (36,ActType.active,"头像框激活"),
    fashion_headFrame_starUp        (36,ActType.star_up,"头像框升星"),
    fashion_tj_active               (29,ActType.active,"时装图鉴激活"),
    fashion_tj_starUp               (29,ActType.star_up,"时装图鉴升星"),
    //魂甲  1
    soulArmor_level_up              (27,ActType.levelUp,"魂甲升级"),
    soulArmor_equip_wear            (21,ActType.equip_wear, BiType.equip,"魂印镶嵌(激活)"),
    soulArmor_equip_intensify       (21,ActType.equip_intensify, BiType.equip,"魂印强化"),
//    soulArmor_equip_resolve         (32,ActType.resolve,"魂印分解"),
    soulArmor_evolution             (27,ActType.evolution,"魂甲突破"),
    soulArmor_wake                  (27,ActType.wake,"魂甲觉醒"),

    //灵体  1
    spirit_starUp                   (21,ActType.star_up,"灵星激活"),
    spirit_equip_wear               (5,ActType.equip_wear, BiType.equip,"灵体装备穿戴"),
    spirit_equip_synthesis          (5,ActType.equip_synthesis, BiType.equip,"灵体装备合成"),

    //识海  1
    shihai_levelUp                  (5,ActType.levelUp,"识海升级"),
    //称号
//    title_active                    (1,ActType.active,"称号激活"),

    //魔魂
    devil_equip_wear                (70,ActType.equip_wear, BiType.equip,"魔魂装备穿戴"),
    devil_equip_synthesis           (70,ActType.equip_synthesis, BiType.equip,"魔魂装备合成"),
    devil_card_unlock               (38,ActType.unlock,"魔魂解锁"),
    devil_card_levelUp              (38,ActType.levelUp,"魔魂升级"),
    devil_card_stageUp              (38,ActType.stageUp,"魔魂升阶"),
    devil_card_evolution            (38,ActType.evolution,"魔魂突破"),

    //背饰(翅膀)
    wing_active                     (13,ActType.active,"翅膀化形激活"),
    wing_star_up                    (13,ActType.star_up,"翅膀化形升星"),
    wing_level_up                   (2,ActType.levelUp,"翅膀升级"),
    wing_soul_up                    (10,ActType.levelUp,"翅膀御魂升级"),
    wing_soul_stageUp               (10,ActType.stageUp,"翅膀御魂升阶"),

    //神兵
    godWeapon_active                (41,ActType.active,"神兵化形激活"),
    godWeapon_star_up               (41,ActType.star_up,"神兵化形升星"),
    godWeapon_level_up              (39,ActType.levelUp,"神兵升级"),
    godWeapon_soul_up               (40,ActType.levelUp,"神兵御魂升级"),
    godWeapon_soul_stageUp          (40,ActType.stageUp,"神兵御魂升阶"),
    ;
    //类型
    private int type;
    //子类型
    private int subType;
    //操作类型
    private int act_type;
    //bi类型（装备、成长）
    private int biType;
    //描述
    private String desc;

    GrowType(int type, ActType act_type, BiType biType, String desc){
        this.type = type;
        this.act_type = act_type.type;
        this.biType = biType.getType();
        this.desc = desc;
    }

    GrowType(int type, ActType act_type, String desc){
        this.type = type;
        this.act_type = act_type.type;
        this.biType = BiType.grow.getType();
        this.desc = desc;
    }

    GrowType(int type, int subType, ActType act_type, String desc){
        this.type = type;
        this.subType = subType;
        this.act_type = act_type.type;
        this.biType = BiType.grow.getType();
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public int getAct_type() {
        return act_type;
    }

    public String getDesc() {
        return desc;
    }

    public int getBiType() {
        return biType;
    }

    public int getSubType() {
        return subType;
    }

    public static GrowType get(int type, int subType, int actType, BiType biType){
        if(biType == BiType.equip){
            if(type >= 5 && type <= 18){
                type = 5;
            }else if(type >= 50 && type < 60){
                type = 50;
            }else if(type >= 60 && type < 70){
                type = 60;
            }
        }
        GrowType v = null;
        for(GrowType t : GrowType.values()){
            if(t.biType == biType.getType() && t.getType() == type && t.getAct_type() == actType){
                v = t;
                if(t.getSubType() == subType){
                    return t;
                }
            }
        }
        return v;
    }
}
