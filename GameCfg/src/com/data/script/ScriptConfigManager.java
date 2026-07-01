/**
 * Auto generated, do not edit it
 *
 * 配置表脚本管理
 */
package com.data.script;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Timer;
import java.util.TimerTask;
import com.data.ConfigBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import com.data.CfgManager;
import java.util.Map;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.container.Cfg_Item_Container;

	
public final class ScriptConfigManager{

    private final String classpath; // javac classpath
    private final String javaFilePath; // .java文件路径
    private final String classFilePath; // javac编译输出路径，内部ClassLoader查找脚本class文件路径

    /**
     * 根据包名获取config容器
     *
     * @param base 传入null返回容易数据，不为null设置数据
     * @param className
     */
    @SuppressWarnings("rawtypes")
    public ConfigBase getScriptConfigBase(String className) {
        switch (className) {
            case "config.Cfg_Global_Load":
                return CfgManager.Global_Container;
            case "config.Cfg_Drop_item_Load":
                return CfgManager.getCfg_Drop_item_Container();
            case "config.Cfg_Drop_package_Load":
                return CfgManager.getCfg_Drop_package_Container();
            case "config.Cfg_MessageString_Load":
                return CfgManager.MessageString_Container;
                        case "config.Cfg_Activity_task_type_Load":
                return CfgManager.getCfg_Activity_task_type_Container();
            case "config.Cfg_Activity_yunying_Load":
                return CfgManager.getCfg_Activity_yunying_Container();
            case "config.Cfg_AttributeAdd_Load":
                return CfgManager.getCfg_AttributeAdd_Container();
            case "config.Cfg_Bag_grid_Load":
                return CfgManager.getCfg_Bag_grid_Container();
            case "config.Cfg_Bi_Load":
                return CfgManager.getCfg_Bi_Container();
            case "config.Cfg_BossHome_Load":
                return CfgManager.getCfg_BossHome_Container();
            case "config.Cfg_Bossnew_HorseBoss_Load":
                return CfgManager.getCfg_Bossnew_HorseBoss_Container();
            case "config.Cfg_Bossnew_SoulBeasts_Load":
                return CfgManager.getCfg_Bossnew_SoulBeasts_Container();
            case "config.Cfg_Bossnew_world_Load":
                return CfgManager.getCfg_Bossnew_world_Container();
            case "config.Cfg_Bossstate_Load":
                return CfgManager.getCfg_Bossstate_Container();
            case "config.Cfg_Boss_FirstBlood_Load":
                return CfgManager.getCfg_Boss_FirstBlood_Container();
            case "config.Cfg_Buff_Load":
                return CfgManager.getCfg_Buff_Container();
            case "config.Cfg_Challenge_reward_Load":
                return CfgManager.getCfg_Challenge_reward_Container();
            case "config.Cfg_Changejob_Load":
                return CfgManager.getCfg_Changejob_Container();
            case "config.Cfg_Change_model_Load":
                return CfgManager.getCfg_Change_model_Container();
            case "config.Cfg_Characters_Load":
                return CfgManager.getCfg_Characters_Container();
            case "config.Cfg_Clone_daneng_Load":
                return CfgManager.getCfg_Clone_daneng_Container();
            case "config.Cfg_Clone_level_Load":
                return CfgManager.getCfg_Clone_level_Container();
            case "config.Cfg_Clone_map_Load":
                return CfgManager.getCfg_Clone_map_Container();
            case "config.Cfg_Clone_xinmo_Load":
                return CfgManager.getCfg_Clone_xinmo_Container();
            case "config.Cfg_ConvoyGirl_Load":
                return CfgManager.getCfg_ConvoyGirl_Container();
            case "config.Cfg_Cross_Alien_Boss_Load":
                return CfgManager.getCfg_Cross_Alien_Boss_Container();
            case "config.Cfg_Cross_Alien_Connect_Load":
                return CfgManager.getCfg_Cross_Alien_Connect_Container();
            case "config.Cfg_Cross_Alien_Connect_Show_Load":
                return CfgManager.getCfg_Cross_Alien_Connect_Show_Container();
            case "config.Cfg_Cross_Alien_Gem_Boss_Load":
                return CfgManager.getCfg_Cross_Alien_Gem_Boss_Container();
            case "config.Cfg_Cross_Alien_Gem_Copy_Load":
                return CfgManager.getCfg_Cross_Alien_Gem_Copy_Container();
            case "config.Cfg_Cross_devil_boss_Load":
                return CfgManager.getCfg_Cross_devil_boss_Container();
            case "config.Cfg_Cross_devil_card_Break_Load":
                return CfgManager.getCfg_Cross_devil_card_Break_Container();
            case "config.Cfg_Cross_devil_card_Camp_Load":
                return CfgManager.getCfg_Cross_devil_card_Camp_Container();
            case "config.Cfg_Cross_devil_card_Main_Load":
                return CfgManager.getCfg_Cross_devil_card_Main_Container();
            case "config.Cfg_Cross_devil_card_Synthesis_Load":
                return CfgManager.getCfg_Cross_devil_card_Synthesis_Container();
            case "config.Cfg_Cross_devil_card_Train_Load":
                return CfgManager.getCfg_Cross_devil_card_Train_Container();
            case "config.Cfg_Cross_devil_Group_Boss_Load":
                return CfgManager.getCfg_Cross_devil_Group_Boss_Container();
            case "config.Cfg_Cross_devil_Group_Copy_Load":
                return CfgManager.getCfg_Cross_devil_Group_Copy_Container();
            case "config.Cfg_Cross_devil_Group_Rank_Load":
                return CfgManager.getCfg_Cross_devil_Group_Rank_Container();
            case "config.Cfg_Cross_devil_hunt_Hot_Load":
                return CfgManager.getCfg_Cross_devil_hunt_Hot_Container();
            case "config.Cfg_Cross_devil_hunt_Pool_Load":
                return CfgManager.getCfg_Cross_devil_hunt_Pool_Container();
            case "config.Cfg_Cross_devil_hunt_Pop_Load":
                return CfgManager.getCfg_Cross_devil_hunt_Pop_Container();
            case "config.Cfg_Cross_fudi_boss_Load":
                return CfgManager.getCfg_Cross_fudi_boss_Container();
            case "config.Cfg_Cross_fudi_hold_reward_Load":
                return CfgManager.getCfg_Cross_fudi_hold_reward_Container();
            case "config.Cfg_Cross_fudi_main_Load":
                return CfgManager.getCfg_Cross_fudi_main_Container();
            case "config.Cfg_Cross_fudi_score_reward_Load":
                return CfgManager.getCfg_Cross_fudi_score_reward_Container();
            case "config.Cfg_Daily_Load":
                return CfgManager.getCfg_Daily_Container();
            case "config.Cfg_Daily_reward_Load":
                return CfgManager.getCfg_Daily_reward_Container();
            case "config.Cfg_EightCity_Load":
                return CfgManager.getCfg_EightCity_Container();
            case "config.Cfg_EightCityReward_Load":
                return CfgManager.getCfg_EightCityReward_Container();
            case "config.Cfg_Equip_Load":
                return CfgManager.getCfg_Equip_Container();
            case "config.Cfg_Equip_Collection_Load":
                return CfgManager.getCfg_Equip_Collection_Container();
            case "config.Cfg_Equip_Collection_star_Load":
                return CfgManager.getCfg_Equip_Collection_star_Container();
            case "config.Cfg_Equip_Collection_start_Load":
                return CfgManager.getCfg_Equip_Collection_start_Container();
            case "config.Cfg_Equip_holy_levelup_Load":
                return CfgManager.getCfg_Equip_holy_levelup_Container();
            case "config.Cfg_Equip_holy_resolve_Load":
                return CfgManager.getCfg_Equip_holy_resolve_Container();
            case "config.Cfg_Equip_holy_suit_Load":
                return CfgManager.getCfg_Equip_holy_suit_Container();
            case "config.Cfg_Equip_holy_synthesis_Load":
                return CfgManager.getCfg_Equip_holy_synthesis_Container();
            case "config.Cfg_Equip_holy_type_Load":
                return CfgManager.getCfg_Equip_holy_type_Container();
            case "config.Cfg_Equip_inten_class_Load":
                return CfgManager.getCfg_Equip_inten_class_Container();
            case "config.Cfg_Equip_inten_main_Load":
                return CfgManager.getCfg_Equip_inten_main_Container();
            case "config.Cfg_Equip_Magic_resolve_Load":
                return CfgManager.getCfg_Equip_Magic_resolve_Container();
            case "config.Cfg_Equip_Magic_suit_Load":
                return CfgManager.getCfg_Equip_Magic_suit_Container();
            case "config.Cfg_Equip_Magic_synthesis_Load":
                return CfgManager.getCfg_Equip_Magic_synthesis_Container();
            case "config.Cfg_Equip_resolve_Load":
                return CfgManager.getCfg_Equip_resolve_Container();
            case "config.Cfg_Equip_shenpin_synthesis_Load":
                return CfgManager.getCfg_Equip_shenpin_synthesis_Container();
            case "config.Cfg_Equip_suit_Load":
                return CfgManager.getCfg_Equip_suit_Container();
            case "config.Cfg_Equip_synthesis_Load":
                return CfgManager.getCfg_Equip_synthesis_Container();
            case "config.Cfg_Equip_xianjia_exchange_Load":
                return CfgManager.getCfg_Equip_xianjia_exchange_Container();
            case "config.Cfg_Equip_xianjia_suit_Load":
                return CfgManager.getCfg_Equip_xianjia_suit_Container();
            case "config.Cfg_Equip_xianjia_synthesis_Load":
                return CfgManager.getCfg_Equip_xianjia_synthesis_Container();
            case "config.Cfg_FallingSky_Level_Load":
                return CfgManager.getCfg_FallingSky_Level_Container();
            case "config.Cfg_FallingSky_Task_Load":
                return CfgManager.getCfg_FallingSky_Task_Container();
            case "config.Cfg_Fashion_Load":
                return CfgManager.getCfg_Fashion_Container();
            case "config.Cfg_Fashion_link_Load":
                return CfgManager.getCfg_Fashion_link_Container();
            case "config.Cfg_Fashion_total_Load":
                return CfgManager.getCfg_Fashion_total_Container();
            case "config.Cfg_FlySword_Grave_Load":
                return CfgManager.getCfg_FlySword_Grave_Container();
            case "config.Cfg_Free_newshop_Load":
                return CfgManager.getCfg_Free_newshop_Container();
            case "config.Cfg_Free_shop_Load":
                return CfgManager.getCfg_Free_shop_Container();
            case "config.Cfg_FunctionOpenTips_Load":
                return CfgManager.getCfg_FunctionOpenTips_Container();
            case "config.Cfg_FunctionStart_Load":
                return CfgManager.getCfg_FunctionStart_Container();
            case "config.Cfg_FunctionVariable_Load":
                return CfgManager.getCfg_FunctionVariable_Container();
            case "config.Cfg_Gather_Load":
                return CfgManager.getCfg_Gather_Container();
            case "config.Cfg_GemGrade_Load":
                return CfgManager.getCfg_GemGrade_Container();
            case "config.Cfg_GemRefining_Load":
                return CfgManager.getCfg_GemRefining_Container();
            case "config.Cfg_GemstoneInlay_Load":
                return CfgManager.getCfg_GemstoneInlay_Container();
            case "config.Cfg_GodWeaponQuality_Load":
                return CfgManager.getCfg_GodWeaponQuality_Container();
            case "config.Cfg_GodWeaponSuit_Load":
                return CfgManager.getCfg_GodWeaponSuit_Container();
            case "config.Cfg_GroundBuff_Load":
                return CfgManager.getCfg_GroundBuff_Container();
            case "config.Cfg_GuildBoss_Reward_Load":
                return CfgManager.getCfg_GuildBoss_Reward_Container();
            case "config.Cfg_Guild_battle_boss_Load":
                return CfgManager.getCfg_Guild_battle_boss_Container();
            case "config.Cfg_Guild_battle_final_add_Load":
                return CfgManager.getCfg_Guild_battle_final_add_Container();
            case "config.Cfg_Guild_battle_final_reward_Load":
                return CfgManager.getCfg_Guild_battle_final_reward_Container();
            case "config.Cfg_Guild_battle_score_Load":
                return CfgManager.getCfg_Guild_battle_score_Container();
            case "config.Cfg_Guild_gift_Load":
                return CfgManager.getCfg_Guild_gift_Container();
            case "config.Cfg_Guild_official_Load":
                return CfgManager.getCfg_Guild_official_Container();
            case "config.Cfg_Guild_title_Load":
                return CfgManager.getCfg_Guild_title_Container();
            case "config.Cfg_Guild_treasure_Load":
                return CfgManager.getCfg_Guild_treasure_Container();
            case "config.Cfg_Guild_up_Load":
                return CfgManager.getCfg_Guild_up_Container();
            case "config.Cfg_Guild_war_building_Load":
                return CfgManager.getCfg_Guild_war_building_Container();
            case "config.Cfg_Guild_war_contkill_Load":
                return CfgManager.getCfg_Guild_war_contkill_Container();
            case "config.Cfg_Guild_war_points_Load":
                return CfgManager.getCfg_Guild_war_points_Container();
            case "config.Cfg_Guild_war_rank_Load":
                return CfgManager.getCfg_Guild_war_rank_Container();
            case "config.Cfg_Guild_war_reward_Load":
                return CfgManager.getCfg_Guild_war_reward_Container();
            case "config.Cfg_Hall_Fame_Load":
                return CfgManager.getCfg_Hall_Fame_Container();
            case "config.Cfg_HappyWeek_Load":
                return CfgManager.getCfg_HappyWeek_Container();
            case "config.Cfg_Horse_equip_inten_Load":
                return CfgManager.getCfg_Horse_equip_inten_Container();
            case "config.Cfg_Horse_equip_inten_class_Load":
                return CfgManager.getCfg_Horse_equip_inten_class_Container();
            case "config.Cfg_Horse_equip_resolve_Load":
                return CfgManager.getCfg_Horse_equip_resolve_Container();
            case "config.Cfg_Horse_equip_score_Load":
                return CfgManager.getCfg_Horse_equip_score_Container();
            case "config.Cfg_Horse_equip_soulbound_Load":
                return CfgManager.getCfg_Horse_equip_soulbound_Container();
            case "config.Cfg_Horse_equip_soulbound_class_Load":
                return CfgManager.getCfg_Horse_equip_soulbound_class_Container();
            case "config.Cfg_Horse_equip_synthesis_Load":
                return CfgManager.getCfg_Horse_equip_synthesis_Container();
            case "config.Cfg_Horse_equip_unlock_Load":
                return CfgManager.getCfg_Horse_equip_unlock_Container();
            case "config.Cfg_Huaxingfabao_Load":
                return CfgManager.getCfg_Huaxingfabao_Container();
            case "config.Cfg_HuaxingFlySword_Load":
                return CfgManager.getCfg_HuaxingFlySword_Container();
            case "config.Cfg_HuaxingFlySword_Advanced_Load":
                return CfgManager.getCfg_HuaxingFlySword_Advanced_Container();
            case "config.Cfg_HuaxingFlySword_levelup_Load":
                return CfgManager.getCfg_HuaxingFlySword_levelup_Container();
            case "config.Cfg_HuaxingFlySword_skill_Load":
                return CfgManager.getCfg_HuaxingFlySword_skill_Container();
            case "config.Cfg_HuaxingHorse_Load":
                return CfgManager.getCfg_HuaxingHorse_Container();
            case "config.Cfg_HuaxingMagic_Load":
                return CfgManager.getCfg_HuaxingMagic_Container();
            case "config.Cfg_HuaxingTalisman_Load":
                return CfgManager.getCfg_HuaxingTalisman_Container();
            case "config.Cfg_HuaxingWeapon_Load":
                return CfgManager.getCfg_HuaxingWeapon_Container();
            case "config.Cfg_HuaxingWing_Load":
                return CfgManager.getCfg_HuaxingWing_Container();
            case "config.Cfg_Immortal_soul_attribute_Load":
                return CfgManager.getCfg_Immortal_soul_attribute_Container();
            case "config.Cfg_Immortal_soul_core_Load":
                return CfgManager.getCfg_Immortal_soul_core_Container();
            case "config.Cfg_Immortal_soul_core_att_Load":
                return CfgManager.getCfg_Immortal_soul_core_att_Container();
            case "config.Cfg_Immortal_soul_exp_Load":
                return CfgManager.getCfg_Immortal_soul_exp_Container();
            case "config.Cfg_Immortal_soul_hunt_Load":
                return CfgManager.getCfg_Immortal_soul_hunt_Container();
            case "config.Cfg_Immortal_soul_iattice_Load":
                return CfgManager.getCfg_Immortal_soul_iattice_Container();
            case "config.Cfg_Immortal_soul_synthesis_Load":
                return CfgManager.getCfg_Immortal_soul_synthesis_Container();
            case "config.Cfg_Invest_Load":
                return CfgManager.getCfg_Invest_Container();
            case "config.Cfg_InvestPeak_Load":
                return CfgManager.getCfg_InvestPeak_Container();
            case "config.Cfg_InvestPeak_Global_Load":
                return CfgManager.getCfg_InvestPeak_Global_Container();
            case "config.Cfg_InvestPeak_Level_Load":
                return CfgManager.getCfg_InvestPeak_Level_Container();
            case "config.Cfg_Invest_Global_Load":
                return CfgManager.getCfg_Invest_Global_Container();
            case "config.Cfg_Invest_Level_Load":
                return CfgManager.getCfg_Invest_Level_Container();
            case "config.Cfg_Item_Load":
                return CfgManager.getCfg_Item_Container();
            case "config.Cfg_ItemChangeReason_Load":
                return CfgManager.getCfg_ItemChangeReason_Container();
            case "config.Cfg_Item_gift_Load":
                return CfgManager.getCfg_Item_gift_Container();
            case "config.Cfg_Item_special_gift_Load":
                return CfgManager.getCfg_Item_special_gift_Container();
            case "config.Cfg_JJCAeward_Load":
                return CfgManager.getCfg_JJCAeward_Container();
            case "config.Cfg_JJCRank_Load":
                return CfgManager.getCfg_JJCRank_Container();
            case "config.Cfg_KaoShangLing_Horse_Load":
                return CfgManager.getCfg_KaoShangLing_Horse_Container();
            case "config.Cfg_Leader_Preach_Load":
                return CfgManager.getCfg_Leader_Preach_Container();
            case "config.Cfg_Leader_Preach_value_Load":
                return CfgManager.getCfg_Leader_Preach_value_Container();
            case "config.Cfg_Level_reward_Load":
                return CfgManager.getCfg_Level_reward_Container();
            case "config.Cfg_Limit_direct_shop_Load":
                return CfgManager.getCfg_Limit_direct_shop_Container();
            case "config.Cfg_Limit_gold_shop_Load":
                return CfgManager.getCfg_Limit_gold_shop_Container();
            case "config.Cfg_Limit_mystery_shop_Load":
                return CfgManager.getCfg_Limit_mystery_shop_Container();
            case "config.Cfg_Limit_shop_Load":
                return CfgManager.getCfg_Limit_shop_Container();
            case "config.Cfg_Manor_score_Load":
                return CfgManager.getCfg_Manor_score_Container();
            case "config.Cfg_Mapsetting_Load":
                return CfgManager.getCfg_Mapsetting_Container();
            case "config.Cfg_Marry_activity_bless_Load":
                return CfgManager.getCfg_Marry_activity_bless_Container();
            case "config.Cfg_Marry_activity_rank_Load":
                return CfgManager.getCfg_Marry_activity_rank_Container();
            case "config.Cfg_Marry_activity_shop_Load":
                return CfgManager.getCfg_Marry_activity_shop_Container();
            case "config.Cfg_Marry_activity_task_Load":
                return CfgManager.getCfg_Marry_activity_task_Container();
            case "config.Cfg_Marry_battle_exchange_Load":
                return CfgManager.getCfg_Marry_battle_exchange_Container();
            case "config.Cfg_Marry_battle_reward_Load":
                return CfgManager.getCfg_Marry_battle_reward_Container();
            case "config.Cfg_Marry_battle_time_Load":
                return CfgManager.getCfg_Marry_battle_time_Container();
            case "config.Cfg_Marry_child_Load":
                return CfgManager.getCfg_Marry_child_Container();
            case "config.Cfg_Marry_childAtt_Load":
                return CfgManager.getCfg_Marry_childAtt_Container();
            case "config.Cfg_Marry_dinner_Load":
                return CfgManager.getCfg_Marry_dinner_Container();
            case "config.Cfg_Marry_lock_Load":
                return CfgManager.getCfg_Marry_lock_Container();
            case "config.Cfg_Marry_order_Load":
                return CfgManager.getCfg_Marry_order_Container();
            case "config.Cfg_Marry_shop_Load":
                return CfgManager.getCfg_Marry_shop_Container();
            case "config.Cfg_Marry_show_Load":
                return CfgManager.getCfg_Marry_show_Container();
            case "config.Cfg_Marry_title_Load":
                return CfgManager.getCfg_Marry_title_Container();
            case "config.Cfg_Marry_wall_Load":
                return CfgManager.getCfg_Marry_wall_Container();
            case "config.Cfg_Monster_Load":
                return CfgManager.getCfg_Monster_Container();
            case "config.Cfg_Month_card_Load":
                return CfgManager.getCfg_Month_card_Container();
            case "config.Cfg_NatureHorse_Load":
                return CfgManager.getCfg_NatureHorse_Container();
            case "config.Cfg_NatureMagic_Load":
                return CfgManager.getCfg_NatureMagic_Container();
            case "config.Cfg_NatureTalisman_Load":
                return CfgManager.getCfg_NatureTalisman_Container();
            case "config.Cfg_NatureWeapon_Load":
                return CfgManager.getCfg_NatureWeapon_Container();
            case "config.Cfg_NatureWing_Load":
                return CfgManager.getCfg_NatureWing_Container();
            case "config.Cfg_Nature_att_Load":
                return CfgManager.getCfg_Nature_att_Container();
            case "config.Cfg_NewRedPacket_Load":
                return CfgManager.getCfg_NewRedPacket_Container();
            case "config.Cfg_New_active_advantage_Load":
                return CfgManager.getCfg_New_active_advantage_Container();
            case "config.Cfg_New_sever_active_Load":
                return CfgManager.getCfg_New_sever_active_Container();
            case "config.Cfg_New_sever_exchange_Load":
                return CfgManager.getCfg_New_sever_exchange_Container();
            case "config.Cfg_New_sever_growup_Load":
                return CfgManager.getCfg_New_sever_growup_Container();
            case "config.Cfg_New_sever_growuprew_Load":
                return CfgManager.getCfg_New_sever_growuprew_Container();
            case "config.Cfg_New_sever_luckcard_Load":
                return CfgManager.getCfg_New_sever_luckcard_Container();
            case "config.Cfg_New_sever_rank_Load":
                return CfgManager.getCfg_New_sever_rank_Container();
            case "config.Cfg_New_sever_rankrew_Load":
                return CfgManager.getCfg_New_sever_rankrew_Container();
            case "config.Cfg_Npc_Load":
                return CfgManager.getCfg_Npc_Container();
            case "config.Cfg_Npc_friend_Load":
                return CfgManager.getCfg_Npc_friend_Container();
            case "config.Cfg_Occ_Skill_Load":
                return CfgManager.getCfg_Occ_Skill_Container();
            case "config.Cfg_Offstring_Load":
                return CfgManager.getCfg_Offstring_Container();
            case "config.Cfg_On_hook_c_Load":
                return CfgManager.getCfg_On_hook_c_Container();
            case "config.Cfg_PeakBattleJoinReward_Load":
                return CfgManager.getCfg_PeakBattleJoinReward_Container();
            case "config.Cfg_PeakBattleRank_Load":
                return CfgManager.getCfg_PeakBattleRank_Container();
            case "config.Cfg_PeakBattleStage_Load":
                return CfgManager.getCfg_PeakBattleStage_Container();
            case "config.Cfg_Pet_Load":
                return CfgManager.getCfg_Pet_Container();
            case "config.Cfg_Pet_equip_inten_Load":
                return CfgManager.getCfg_Pet_equip_inten_Container();
            case "config.Cfg_Pet_equip_inten_class_Load":
                return CfgManager.getCfg_Pet_equip_inten_class_Container();
            case "config.Cfg_Pet_equip_resolve_Load":
                return CfgManager.getCfg_Pet_equip_resolve_Container();
            case "config.Cfg_Pet_equip_soulbound_Load":
                return CfgManager.getCfg_Pet_equip_soulbound_Container();
            case "config.Cfg_Pet_equip_soulbound_class_Load":
                return CfgManager.getCfg_Pet_equip_soulbound_class_Container();
            case "config.Cfg_Pet_equip_synthesis_Load":
                return CfgManager.getCfg_Pet_equip_synthesis_Container();
            case "config.Cfg_Pet_equip_unlock_Load":
                return CfgManager.getCfg_Pet_equip_unlock_Container();
            case "config.Cfg_Pet_level_Load":
                return CfgManager.getCfg_Pet_level_Container();
            case "config.Cfg_Pet_rank_Load":
                return CfgManager.getCfg_Pet_rank_Container();
            case "config.Cfg_Pet_soul_Load":
                return CfgManager.getCfg_Pet_soul_Container();
            case "config.Cfg_PlayerOccupation_Load":
                return CfgManager.getCfg_PlayerOccupation_Container();
            case "config.Cfg_PlayerShiHai_Load":
                return CfgManager.getCfg_PlayerShiHai_Container();
            case "config.Cfg_Pray_Load":
                return CfgManager.getCfg_Pray_Container();
            case "config.Cfg_PrayCost_Load":
                return CfgManager.getCfg_PrayCost_Container();
            case "config.Cfg_RankAwardItem_Load":
                return CfgManager.getCfg_RankAwardItem_Container();
            case "config.Cfg_RankAwardType_Load":
                return CfgManager.getCfg_RankAwardType_Container();
            case "config.Cfg_Rank_base_Load":
                return CfgManager.getCfg_Rank_base_Container();
            case "config.Cfg_Rank_compare_Load":
                return CfgManager.getCfg_Rank_compare_Container();
            case "config.Cfg_RechargeAward_Load":
                return CfgManager.getCfg_RechargeAward_Container();
            case "config.Cfg_Recharge_daily_Load":
                return CfgManager.getCfg_Recharge_daily_Container();
            case "config.Cfg_Recharge_daily_cangzhenge_Load":
                return CfgManager.getCfg_Recharge_daily_cangzhenge_Container();
            case "config.Cfg_Recharge_daily_duibaodian_Load":
                return CfgManager.getCfg_Recharge_daily_duibaodian_Container();
            case "config.Cfg_Recharge_daily_superreward_Load":
                return CfgManager.getCfg_Recharge_daily_superreward_Container();
            case "config.Cfg_Relive_Load":
                return CfgManager.getCfg_Relive_Container();
            case "config.Cfg_RetrieveRes_Load":
                return CfgManager.getCfg_RetrieveRes_Container();
            case "config.Cfg_RollDodge_Load":
                return CfgManager.getCfg_RollDodge_Container();
            case "config.Cfg_Sdkplatform_Load":
                return CfgManager.getCfg_Sdkplatform_Container();
            case "config.Cfg_Sevenday_login_Load":
                return CfgManager.getCfg_Sevenday_login_Container();
            case "config.Cfg_Share_Load":
                return CfgManager.getCfg_Share_Container();
            case "config.Cfg_Shop_Maket_Load":
                return CfgManager.getCfg_Shop_Maket_Container();
            case "config.Cfg_Sign_reward_Load":
                return CfgManager.getCfg_Sign_reward_Container();
            case "config.Cfg_Sign_rewardCumulative_Load":
                return CfgManager.getCfg_Sign_rewardCumulative_Container();
            case "config.Cfg_Sign_rewardsupplement_Load":
                return CfgManager.getCfg_Sign_rewardsupplement_Container();
            case "config.Cfg_Skill_Load":
                return CfgManager.getCfg_Skill_Container();
            case "config.Cfg_Skill_meridian_new_Load":
                return CfgManager.getCfg_Skill_meridian_new_Container();
            case "config.Cfg_Skill_meridian_pos_Load":
                return CfgManager.getCfg_Skill_meridian_pos_Container();
            case "config.Cfg_Skill_position_levelup_Load":
                return CfgManager.getCfg_Skill_position_levelup_Container();
            case "config.Cfg_Skill_star_levelup_Load":
                return CfgManager.getCfg_Skill_star_levelup_Container();
            case "config.Cfg_Social_decorate_Load":
                return CfgManager.getCfg_Social_decorate_Container();
            case "config.Cfg_Social_house_Load":
                return CfgManager.getCfg_Social_house_Container();
            case "config.Cfg_Social_house_furniture_Load":
                return CfgManager.getCfg_Social_house_furniture_Container();
            case "config.Cfg_Social_house_gift_Load":
                return CfgManager.getCfg_Social_house_gift_Container();
            case "config.Cfg_Social_house_level_Load":
                return CfgManager.getCfg_Social_house_level_Container();
            case "config.Cfg_Social_house_market_Load":
                return CfgManager.getCfg_Social_house_market_Container();
            case "config.Cfg_Social_house_rank_Load":
                return CfgManager.getCfg_Social_house_rank_Container();
            case "config.Cfg_Social_house_task_Load":
                return CfgManager.getCfg_Social_house_task_Container();
            case "config.Cfg_SoulArmor_awaken_Load":
                return CfgManager.getCfg_SoulArmor_awaken_Container();
            case "config.Cfg_SoulArmor_awaken_skill_Load":
                return CfgManager.getCfg_SoulArmor_awaken_skill_Container();
            case "config.Cfg_SoulArmor_breach_Load":
                return CfgManager.getCfg_SoulArmor_breach_Container();
            case "config.Cfg_SoulArmor_equip_synthesis_Load":
                return CfgManager.getCfg_SoulArmor_equip_synthesis_Container();
            case "config.Cfg_SoulArmor_level_up_Load":
                return CfgManager.getCfg_SoulArmor_level_up_Container();
            case "config.Cfg_SoulArmor_signet_hole_Load":
                return CfgManager.getCfg_SoulArmor_signet_hole_Container();
            case "config.Cfg_SoulArmor_signet_intensify_Load":
                return CfgManager.getCfg_SoulArmor_signet_intensify_Container();
            case "config.Cfg_SoulArmor_signet_lottery_Load":
                return CfgManager.getCfg_SoulArmor_signet_lottery_Container();
            case "config.Cfg_SoulArmor_signet_lottery_object_Load":
                return CfgManager.getCfg_SoulArmor_signet_lottery_object_Container();
            case "config.Cfg_SoulArmor_signet_suit_Load":
                return CfgManager.getCfg_SoulArmor_signet_suit_Container();
            case "config.Cfg_SoulBeasts_Load":
                return CfgManager.getCfg_SoulBeasts_Container();
            case "config.Cfg_SoulBeastsEquip_Load":
                return CfgManager.getCfg_SoulBeastsEquip_Container();
            case "config.Cfg_SoulBeastsEquipLevel_Load":
                return CfgManager.getCfg_SoulBeastsEquipLevel_Container();
            case "config.Cfg_State_Load":
                return CfgManager.getCfg_State_Container();
            case "config.Cfg_State_manage_Load":
                return CfgManager.getCfg_State_manage_Container();
            case "config.Cfg_State_power_Load":
                return CfgManager.getCfg_State_power_Container();
            case "config.Cfg_State_stifle_Load":
                return CfgManager.getCfg_State_stifle_Container();
            case "config.Cfg_State_stifle_add_Load":
                return CfgManager.getCfg_State_stifle_add_Container();
            case "config.Cfg_State_stifle_add_level_Load":
                return CfgManager.getCfg_State_stifle_add_level_Container();
            case "config.Cfg_State_stifle_add_level_all_Load":
                return CfgManager.getCfg_State_stifle_add_level_all_Container();
            case "config.Cfg_State_xisui_Load":
                return CfgManager.getCfg_State_xisui_Container();
            case "config.Cfg_State_xisui_acupoint_Load":
                return CfgManager.getCfg_State_xisui_acupoint_Container();
            case "config.Cfg_Statue_model_Load":
                return CfgManager.getCfg_Statue_model_Container();
            case "config.Cfg_Sword_soul_copy_Load":
                return CfgManager.getCfg_Sword_soul_copy_Container();
            case "config.Cfg_SZZQAward_Load":
                return CfgManager.getCfg_SZZQAward_Container();
            case "config.Cfg_SZZQScoreAward_Load":
                return CfgManager.getCfg_SZZQScoreAward_Container();
            case "config.Cfg_Task_Load":
                return CfgManager.getCfg_Task_Container();
            case "config.Cfg_Task_branch_Load":
                return CfgManager.getCfg_Task_branch_Container();
            case "config.Cfg_Task_conquer_Load":
                return CfgManager.getCfg_Task_conquer_Container();
            case "config.Cfg_Task_daily_Load":
                return CfgManager.getCfg_Task_daily_Container();
            case "config.Cfg_Task_gender_Load":
                return CfgManager.getCfg_Task_gender_Container();
            case "config.Cfg_Task_target_reward_Load":
                return CfgManager.getCfg_Task_target_reward_Container();
            case "config.Cfg_Thai_score_Load":
                return CfgManager.getCfg_Thai_score_Container();
            case "config.Cfg_Title_Load":
                return CfgManager.getCfg_Title_Container();
            case "config.Cfg_Today_function_Load":
                return CfgManager.getCfg_Today_function_Container();
            case "config.Cfg_Today_function_task_Load":
                return CfgManager.getCfg_Today_function_task_Container();
            case "config.Cfg_Treasure_Hunt_Load":
                return CfgManager.getCfg_Treasure_Hunt_Container();
            case "config.Cfg_Treasure_Pop_Load":
                return CfgManager.getCfg_Treasure_Pop_Container();
            case "config.Cfg_Treasure_Recovery_Load":
                return CfgManager.getCfg_Treasure_Recovery_Container();
            case "config.Cfg_Treasure_xianjiaSecret_Load":
                return CfgManager.getCfg_Treasure_xianjiaSecret_Container();
            case "config.Cfg_Universe_boss_Load":
                return CfgManager.getCfg_Universe_boss_Container();
            case "config.Cfg_Universe_command_Load":
                return CfgManager.getCfg_Universe_command_Container();
            case "config.Cfg_Universe_rank_Load":
                return CfgManager.getCfg_Universe_rank_Container();
            case "config.Cfg_Universe_Task_Load":
                return CfgManager.getCfg_Universe_Task_Container();
            case "config.Cfg_Vip_Load":
                return CfgManager.getCfg_Vip_Container();
            case "config.Cfg_VipHelp_Load":
                return CfgManager.getCfg_VipHelp_Container();
            case "config.Cfg_VipPower_Load":
                return CfgManager.getCfg_VipPower_Container();
            case "config.Cfg_VipRebate_Load":
                return CfgManager.getCfg_VipRebate_Container();
            case "config.Cfg_VIPTrueRecharge_Load":
                return CfgManager.getCfg_VIPTrueRecharge_Container();
            case "config.Cfg_VIPWeekReward_Load":
                return CfgManager.getCfg_VIPWeekReward_Container();
            case "config.Cfg_Wash_Load":
                return CfgManager.getCfg_Wash_Container();
            case "config.Cfg_Wash_best_Load":
                return CfgManager.getCfg_Wash_best_Container();
            case "config.Cfg_Week_welfare_Load":
                return CfgManager.getCfg_Week_welfare_Container();
            case "config.Cfg_Week_welfare_reward_Load":
                return CfgManager.getCfg_Week_welfare_reward_Container();
            case "config.Cfg_World_bonfire_Load":
                return CfgManager.getCfg_World_bonfire_Container();
            case "config.Cfg_World_question_Load":
                return CfgManager.getCfg_World_question_Container();
            case "config.Cfg_World_question_reward_Load":
                return CfgManager.getCfg_World_question_reward_Container();
            case "config.Cfg_World_Support_Load":
                return CfgManager.getCfg_World_Support_Container();
            case "config.Cfg_Xianmengzhengba_Load":
                return CfgManager.getCfg_Xianmengzhengba_Container();
            case "config.Cfg_AchievementRune_Load":
                return CfgManager.getCfg_AchievementRune_Container();
            case "config.Cfg_Across_Load":
                return CfgManager.getCfg_Across_Container();
            case "config.Cfg_Activity_festival_Load":
                return CfgManager.getCfg_Activity_festival_Container();
            case "config.Cfg_Bonfire_Load":
                return CfgManager.getCfg_Bonfire_Container();
            case "config.Cfg_Bossnew_drop_Load":
                return CfgManager.getCfg_Bossnew_drop_Container();
            case "config.Cfg_Card_Load":
                return CfgManager.getCfg_Card_Container();
            case "config.Cfg_Clonerobot_Load":
                return CfgManager.getCfg_Clonerobot_Container();
            case "config.Cfg_Clone_monster_Load":
                return CfgManager.getCfg_Clone_monster_Container();
            case "config.Cfg_Clone_story_Load":
                return CfgManager.getCfg_Clone_story_Container();
            case "config.Cfg_Cross_fudi_point_gift_reward_Load":
                return CfgManager.getCfg_Cross_fudi_point_gift_reward_Container();
            case "config.Cfg_Extra_rewards_Load":
                return CfgManager.getCfg_Extra_rewards_Container();
            case "config.Cfg_FirstRecharge_Load":
                return CfgManager.getCfg_FirstRecharge_Container();
            case "config.Cfg_GMitem_Load":
                return CfgManager.getCfg_GMitem_Container();
            case "config.Cfg_Guild_war_closereward_Load":
                return CfgManager.getCfg_Guild_war_closereward_Container();
            case "config.Cfg_Item_warning_Load":
                return CfgManager.getCfg_Item_warning_Container();
            case "config.Cfg_Jjcrobot_Load":
                return CfgManager.getCfg_Jjcrobot_Container();
            case "config.Cfg_Marrybattlerobot_Load":
                return CfgManager.getCfg_Marrybattlerobot_Container();
            case "config.Cfg_MonsterAi_Load":
                return CfgManager.getCfg_MonsterAi_Container();
            case "config.Cfg_MysBuShop_Load":
                return CfgManager.getCfg_MysBuShop_Container();
            case "config.Cfg_On_hook_Load":
                return CfgManager.getCfg_On_hook_Container();
            case "config.Cfg_Rank_Load":
                return CfgManager.getCfg_Rank_Container();
            case "config.Cfg_Recharge_daily_total_Load":
                return CfgManager.getCfg_Recharge_daily_total_Container();
            case "config.Cfg_RedPacket_Load":
                return CfgManager.getCfg_RedPacket_Container();
            case "config.Cfg_Robotrandomname_Load":
                return CfgManager.getCfg_Robotrandomname_Container();
            case "config.Cfg_Scuffle_king_Load":
                return CfgManager.getCfg_Scuffle_king_Container();
            case "config.Cfg_Shielding_symbol_Load":
                return CfgManager.getCfg_Shielding_symbol_Container();
            case "config.Cfg_Shop_Mystery_Load":
                return CfgManager.getCfg_Shop_Mystery_Container();
            case "config.Cfg_Skill_meridian_Load":
                return CfgManager.getCfg_Skill_meridian_Container();
            case "config.Cfg_Skill_Trigger_Load":
                return CfgManager.getCfg_Skill_Trigger_Container();
            case "config.Cfg_StarAttribute_Load":
                return CfgManager.getCfg_StarAttribute_Container();
            case "config.Cfg_Synthetic_Load":
                return CfgManager.getCfg_Synthetic_Container();
            case "config.Cfg_TimingMail_Load":
                return CfgManager.getCfg_TimingMail_Container();
            case "config.Cfg_Today_function_recharge_Load":
                return CfgManager.getCfg_Today_function_recharge_Container();
            case "config.Cfg_Treasure_Load":
                return CfgManager.getCfg_Treasure_Container();
            case "config.Cfg_UrlMarquee_Load":
                return CfgManager.getCfg_UrlMarquee_Container();
            default:
                break;
        }
        return null;
    }
    
    /**
     * 配置初始化
     */
    private ScriptConfigManager() {
        javaFilePath = "script";
        classFilePath = "bin";
        File file = new File(classFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString();
        load();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ScriptConfigManager processor;

        Singleton() {
            this.processor = new ScriptConfigManager();
        }

        ScriptConfigManager getProcessor() {
            return processor;
        }
    }

    /**
     * 获取ScriptConfigManager的实例对象.
     *
     * @return
     */
    public static ScriptConfigManager GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 加载配置
     */
    private void load() {
        try {
            initScript();
            String val = System.getProperty("ideDebug");
            boolean isDebug = (val != null && val.equals("true"));
            if (isDebug) {
                timerCheckReload();
            }
        } catch (Exception e) {
            System.out.println("load script fail:" + e);
            System.exit(-4);
        }
    }

    /**
     * 初始化配置脚本数据
     */
    @SuppressWarnings("rawtypes")
    private void initScript() throws Exception {
        String val = System.getProperty("ideDebug");
        boolean isDebug = (val != null && val.equals("true"));
        Set<Class<IScriptConfig>> scriptList = ClassUtil.GetSubClasses("config", IScriptConfig.class);
        for (Class<IScriptConfig> cls : scriptList) {
            IScriptConfig script = cls.newInstance();
            String className = script.getClass().getName();
            ConfigBase config = getScriptConfigBase(className);
            if (config == null) {
                continue;
            }
            config.setJavaFileTimestamp(0);
            String filename = javaFilePath + "/" + className.replace('.', '/') + ".java";
            config.setFileName(filename);
            if (!isDebug) {
                File f = new File(filename);
                if (f.isFile() && f.canRead()) {
                    reloadConfigScript(config, className, f);
                }
            }
        }
    }

    /**
     * 重新加载配置表，传入参数类似为：load.Cfg_Mapsetting_Load
     *
     * @param className
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean reloadConfigScript(String className) throws Exception {
        ConfigBase config = getScriptConfigBase(className);
        if (config == null) {
            throw new Exception("配置表：" + className + "reload失败，没有该配置文件");
        }
        File f = new File(config.getFileName());
        if (!f.isFile() || !f.canRead()) {
            throw new Exception("配置表：" + className + "reload失败，配置文件错误");
        }
        return reloadConfigScript(config, className, f);
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    private boolean reloadConfigScript(ConfigBase config, String className, File f) throws Exception {
        Class<?> clazz;
        try (InputStream inStream = new FileInputStream(f)) {
            byte[] bytes = new byte[(int) f.length()];
            inStream.read(bytes);
            clazz = ClassUtil.JavaCodeToObject(className, new String(bytes, "UTF-8"), classpath, classFilePath);
        }
        if (clazz == null) {
            throw new Exception("配置表：" + className + "reload失败，读取class失败");
        }
        if (!ClassUtil.IsInterface(clazz, IScriptConfig.class)) {
            throw new Exception("配置表：" + className + "reload失败，配置文件load不是继承于IScriptConfig");
        }
        IScriptConfig load = (IScriptConfig) clazz.newInstance();
        Map m = config.newMapValuees();
        load.load(m);
        config.initValue(m);
        config.setJavaFileTimestamp(f.lastModified());
        System.out.println("reload config:" + className);
        return true;

    }
    @SuppressWarnings({"rawtypes", "deprecation"})
   public void reloadCofigItem(){
        Map m = Cfg_Item_Container.GetInstance().newMapValuees();
        for (Cfg_Item_Bean bean : CfgManager.getCfg_Item_Container().getValuees()) {
             m.put(bean.getId(), bean);
        }
        for (Cfg_Equip_Bean bean : CfgManager.getCfg_Equip_Container().getValuees()) {
            int type = 2;
            //400万-500万之间是圣装，类型为14
            if ( bean.getId() >= 4000000 &&  bean.getId() <= 5000000){
                    type = 14;
               }
            //500万-600万之间是仙甲，类型为22
             if ( bean.getId() >= 5000000 &&  bean.getId() <= 6000000){
                    type = 22;
              }
            //700万-800万之间是宠物装备，类型为23
            if ( bean.getId() >= 7000000 &&  bean.getId() <= 8000000){
                    type = 23;
            }
            //魂印
            if ( bean.getPart() >= 211 && bean.getPart() <= 220){
                    type = 24;
            }
            //坐骑装备
            if ( bean.getPart() >= 301 && bean.getPart() <= 304){
                type = 25;
            }
            //魔魂装备
            if ( bean.getPart() >= 305 && bean.getPart() <= 340){
                type = 26;
            }
            //幻装装备
            if ( bean.getPart() >= 441 && bean.getPart() <= 450){
                type = 28;
            }
            Cfg_Item_Bean itemBean = new Cfg_Item_Bean(
            bean.getId(),
            bean.getName(), 0, type, 0, bean.getTrade_recom(), "", bean.getQuality(),
            bean.getLevel(),0,2, bean.getGender().getValueString(), bean.getBind(), 1,
            "", bean.getConfirm(), 0, 0, "", 0,0,
            0, 0, 0, 0, "", 0, 0, 0
            , 0, "", "",
            bean.getDrop_notice(),
			bean.getChatchannel().getValueString(),
			bean.getAuction_price_type(),
            bean.getAuction_use_coin(),
            bean.getAuction_min_price(),
            bean.getAuction_max_price(),
            bean.getAuction_single_type(),
            bean.getAuction_single_price(),
            bean.getAuction_countdown(),
            bean.getAuction_all_time(),
            bean.getAuction_guild_all_time(),
            bean.getDead_time());
            m.put(bean.getId(), itemBean);
        }
        Cfg_Item_Container.GetInstance().initValue(m);
   }


    /**
     * 启动一个timer，每秒自动编译最新脚本，开发版本时才调用
     */
    private void timerCheckReload() {
        new Timer("ScriptCheckReload-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    check();
                } catch (Exception e) {

                }
            }

            private void check() {
                try {
                    File dir = new File(javaFilePath + "/config");
                    checkScript(dir, "config");
                } catch (Exception e) {

                }
            }

            /**
             * 检查脚本
             */
            @SuppressWarnings("rawtypes")
            private void checkScript(File dir, String pakckageName) throws IllegalAccessException, IOException, ClassNotFoundException, InstantiationException, Exception {
                for (File f : dir.listFiles()) {
                    if (f.isDirectory()) {
                        if (pakckageName.isEmpty()) {
                            checkScript(f, f.getName());
                        } else {
                            checkScript(f, pakckageName + "." + f.getName());
                        }
                        continue;
                    }
                    if (f.isFile() && f.canRead()) {
                        String className;
                        if (pakckageName.isEmpty()) {
                            className = f.getName();
                        } else {
                            className = pakckageName + "." + f.getName();
                        }
                        className = className.replace(".java", "");
                        ConfigBase config = getScriptConfigBase(className);
                        if (config == null) {
                            continue;
                        }
                        if (config.getJavaFileTimestamp() == 0) {
                            config.setJavaFileTimestamp(f.lastModified());
                            continue;
                        }
                        if (f.lastModified() == config.getJavaFileTimestamp()) {
                            continue;
                        }
                        //重新加载
                        reloadConfigScript(config, className, f);
                    }
                }
            }
        }, 1000, 1000);
    }
}
