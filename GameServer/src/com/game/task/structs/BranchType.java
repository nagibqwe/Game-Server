package com.game.task.structs;

/**
 * 支线任务的功能类型作者: 1 好友 2 技能升级 3 坐骑幻化 4 转职 5 强化 6 装备进阶 7 合成 8 称号激活 9 坐骑升阶 10 装备升星
 * 11 神兵进阶 12 伙伴谈心 13 【守卫六扇门】 14 【宣府大征兵】 15 【西厂叛军】 16 【PK之王】 17 【阵营战】 18 【挑战副本】
 * 19 【经验副本】 20 【金币副本 21 【幽谷探宝】 22 【守卫秘宝】 23 【名成八阵】 24 【天子讨伐令】 25 【勇闯海岛】 26
 * 【过关斩将】 27 【争锋相对】 28 【剑荡四方】 29 【跨服竞技】 30 【宝藏山谷】 31 【跨服阵营战】
 *
 * @author soko <xuchangming@haowan123.com>
 */
public enum BranchType {

    CHALLENGECLONE(18),     //挑战副本
    MONEYCLONE(20),         //金币副本
    ACTIVITYCOPYMAP_1(32), //活动副本一
    ACTIVITYCOPYMAP_2(33), //活动副本一
    THECHIEFTASK(34),        //首席任务
    EXP_COPY(35),       //经验副本
    HAVEAGUILD(36),          //加入或者创建一个帮会
    UP_ACTIVE_MEDAL(37),     //升级活跃勋章
    CHIEF_CHANLLAGE(38),     //首席挑战
    PET_CATCH(39),             //宠物捕捉
    BATTLE_TASK_FINISH(40),   //完成勇者战场任务
    MELTING_WEEK_COUNT(41),   //魔盒熔炼次数
    WEDDING_COUNT(42),         //结婚次数
    STORY_CLONE_DIFFICULTY(43),     //剧情副本困难难度三星
    SEND_OTHER_STAMINA(44),         //赠送其他人体力
    HUNT_FIGHT_SOUL(45),            //猎取战魂
    ACTIVE_MASTER_HOLLOW_HOLY(46),       //激活师门圣器
    WEDDING_DINNER_PARTY(47),           //婚宴
    TEMPLE_STONE_SUBMIT(48),            //创世水晶的提交
    USE_ITEM(49),                       //使用某个指定的道具
    Muti_Player_Clone(50),              //多人副本(惊悚乐园)
    OPEN_UI_TO_SUBMIT(51),              //打开ui完成任务(需要主动提交)
    MOUNT_FLY_UP(52),                   //飞行坐骑起飞完成任务
    SPECIAL_MONSTER(53),                //击杀精英boss
    EQUIP_FUSE(54),                     //装备融合
    GUILD_HOUSE_DONATE(55),             //公会仓库装备捐献
    VIP_STATE_LEVEL(56),             //境界达到X等级
    VIP_STATE_TASK_NUM(57),             //完成X个境界任务
    NULL_BRANCH_TYPE(10000);   //没有找到默认返回

    private final int value;

    BranchType(int v) {
        this.value = v;
    }

    public final int getValue() {
        return value;
    }
    
    public static BranchType getBranchType(int value){
        for(BranchType type : values()){
            if(type.getValue() == value){
                return type;
            }
        }
        return NULL_BRANCH_TYPE;
    }
}
