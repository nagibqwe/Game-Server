package com.game.bi.manager;

import com.game.bi.base.BI;
import com.game.bi.struct.*;
import game.core.util.IDConfigUtil;

/**
 * @explain: auto generate
 * @time Created on 2020-01-02 15:58:58
 * @author: tc
 */
public class BIString {
	/**
	 * 角色信息变化
	 * @param bi
	 * @param biRole_info
	 * @return
	 */
	public static String str(BI bi, BIRole_info biRole_info) {
		if (bi == null || biRole_info == null) return "";
		return "role_info:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biRole_info.getCreated_time() + '|' +
				biRole_info.getLast_login_time() + '|' +
				biRole_info.getTotal_online() + '|' +
				biRole_info.getExp() + '|' +
				biRole_info.getLast_updatetime() + '|' +
				biRole_info.getTask_id() + '|' +
				biRole_info.getMoney_balance() + '|' +
				biRole_info.getMoney2_balance() + '|' +
				biRole_info.getBmoney_balance() + '|' +
				biRole_info.getGold_balance() + '|' +
				biRole_info.getBgold_balance() + '|' +
				biRole_info.getMarry_status() + '|' +
				biRole_info.getMarry_partner_id() + '|' +
				biRole_info.getMarry_partner_name() + '|' +
				biRole_info.getMarry_partner_time() + '|' +
				biRole_info.getShengxian_order() + '\n';
	}

	/**
	 * 角色创建
	 * @param bi
	 * @param biCreate_role
	 * @return
	 */
	public static String str(BI bi, BICreate_role biCreate_role) {
		if (bi == null || biCreate_role == null) return "";
		return "create_role:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '\n';
	}

	/**
	 * 角色登录
	 * @param bi
	 * @param biLogin
	 * @return
	 */
	public static String str(BI bi, BILogin biLogin) {
		if (bi == null || biLogin == null) return "";
		return "login:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biLogin.getDevice_nm() + '|' +
				biLogin.getLogin_type() + '|' +
				biLogin.getIs_retrieve() + '\n';
	}

	/**
	 * 角色登出
	 * @param bi
	 * @param biLogout
	 * @return
	 */
	public static String str(BI bi, BILogout biLogout) {
		if (bi == null || biLogout == null) return "";
		return "logout:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biLogout.getLogin_time() + '|' +
				biLogout.getLogin_level() + '|' +
				biLogout.getLogin_vip_level() + '|' +
				biLogout.getOnline_time() + '|' +
				biLogout.getLogout_type() + '|' +
				biLogout.getMoney_balance() + '|' +
				biLogout.getMoney2_balance() + '|' +
				biLogout.getBmoney_balance() + '|' +
				biLogout.getGold_balance() + '|' +
				biLogout.getBgold_balance() + '|' +
				biLogout.getMaster_point() + '|' +
				biLogout.getIs_retrieve() + '\n';
	}

	/**
	 * 充值流水
	 * @param bi
	 * @param biPay
	 * @return
	 */
	public static String str(BI bi, BIPay biPay) {
		if (bi == null || biPay == null) return "";
		return "pay:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biPay.getOrder_id() + '|' +
				biPay.getPay_platform() + '|' +
				biPay.getPay_order_id() + '|' +
				biPay.getPay_type() + '|' +
				biPay.getPay_status() + '|' +
				biPay.getPay_money_ct() + '|' +
				biPay.getPay_money_amount() + '|' +
				biPay.getPay_gold_amount() + '|' +
				biPay.getProduct_id() + '|' +
				biPay.getProduct_name() + '|' +
				biPay.getProduct_amount() + '\n';
	}

	/**
	 * 货币流水
	 * @param bi
	 * @param biMoney
	 * @return
	 */
	public static String str(BI bi, BIMoney biMoney) {
		if (bi == null || biMoney == null) return "";
		return "money:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biMoney.getMoney_type() + '|' +
				biMoney.getChange_type() + '|' +
				biMoney.getAmount() + '|' +
				biMoney.getBefore_amount() + '|' +
				biMoney.getAfter_amount() + '|' +
				biMoney.getMoney_op_type() + '|' +
				biMoney.getMoney_op_target_type() + '|' +
				biMoney.getMoney_op_target_value() + '|' +
				biMoney.getMoney_op_target_count() + '|' +
				biMoney.getMoney_op_target_attr() + '\n';
	}

	/**
	 * 物品流水
	 * @param bi
	 * @param biItem
	 * @return
	 */
	public static String str(BI bi, BIItem biItem) {
		if (bi == null || biItem == null) return "";
		return "item:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biItem.getItem_id() + '|' +
				biItem.getItem_instance_id() + '|' +
				biItem.getItem_name() + '|' +
				biItem.getItem_type() + '|' +
				biItem.getChange_type() + '|' +
				biItem.getItem_num() + '|' +
				biItem.getBefore_num() + '|' +
				biItem.getAfter_num() + '|' +
				biItem.getItem_op_type() + '|' +
				biItem.getItem_op_target_type() + '|' +
				biItem.getItem_op_target_value() + '|' +
				biItem.getItem_op_target_count() + '|' +
				biItem.getItem_op_target_attr() + '\n';
	}

	/**
	 * 商城购买
	 * @param bi
	 * @param biMall
	 * @return
	 */
	public static String str(BI bi, BIMall biMall) {
		if (bi == null || biMall == null) return "";
		return "mall:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biMall.getMall_type() + '|' +
				biMall.getItem_type() + '|' +
				biMall.getItem_id() + '|' +
				biMall.getItem_num() + '|' +
				biMall.getMoney_type() + '|' +
				biMall.getAmount() + '|' +
				biMall.getPrice() + '|' +
				biMall.getLocation() + '\n';
	}

	/**
	 * 资源流水
	 * @param bi
	 * @param biResource
	 * @return
	 */
	public static String str(BI bi, BIResource biResource) {
		if (bi == null || biResource == null) return "";
		return "resource:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biResource.getResource_type() + '|' +
				biResource.getChange_type() + '|' +
				biResource.getResource_num() + '|' +
				biResource.getBefore_resource() + '|' +
				biResource.getAfter_resource() + '|' +
				biResource.getResource_op_type() + '|' +
				biResource.getResource_op_target_type() + '|' +
				biResource.getResource_op_target_value() + '|' +
				biResource.getResource_op_target_count() + '|' +
				biResource.getResource_op_target_attr() + '\n';
	}

	/**
	 * 任务日志
	 * @param bi
	 * @param biTask
	 * @return
	 */
	public static String str(BI bi, BITask biTask) {
		if (bi == null || biTask == null) return "";
		return "task:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biTask.getTask_id() + '|' +
				biTask.getTask_name() + '|' +
				biTask.getTask_type() + '|' +
				biTask.getTask_status() + '|' +
				biTask.getTask_used_time() + '|' +
				biTask.getTask_subtype() + '|' +
				biTask.getTask_level() + '\n';
	}

	/**
	 * 成长日志
	 * @param bi
	 * @param biGrow
	 * @return
	 */
	public static String str(BI bi, BIGrow biGrow) {
		if (bi == null || biGrow == null) return "";
		return "grow:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biGrow.getGrow_target_id() + '|' +
				biGrow.getGrow_instance_id() + '|' +
				biGrow.getGrow_target_type() + '|' +
				biGrow.getGrow_target_subtype() + '|' +
				biGrow.getGrow_act_type() + '|' +
				biGrow.getChange_type() + '|' +
				biGrow.getGrow_status() + '|' +
				biGrow.getChange_value() + '|' +
				biGrow.getBefore_value() + '|' +
				biGrow.getAfter_value() + '|' +
				biGrow.getChange_combat() + '|' +
				biGrow.getBefore_combat() + '|' +
				biGrow.getAfter_combat() + '\n';
	}

	/**
	 * 交易日志
	 * @param bi
	 * @param biTrade
	 * @return
	 */
	public static String str(BI bi, BITrade biTrade) {
		if (bi == null || biTrade == null) return "";
		return "trade:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biTrade.getT_account_id() + '|' +
				biTrade.getT_role_id() + '|' +
				biTrade.getT_role_name() + '|' +
				biTrade.getT_ip() + '|' +
				biTrade.getItem_id() + '|' +
				biTrade.getItem_type() + '|' +
				biTrade.getItem_num() + '|' +
				biTrade.getBefore_num() + '|' +
				biTrade.getAfter_num() + '|' +
				biTrade.getOrder_money() + '|' +
				biTrade.getOrder_type() + '|' +
				biTrade.getOrder_sn() + '\n';
	}

	/**
	 * 副本日志
	 * @param bi
	 * @param biInstance
	 * @return
	 */
	public static String str(BI bi, BIInstance biInstance) {
		if (bi == null || biInstance == null) return "";
		return "instance:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biInstance.getInstance_id() + '|' +
				biInstance.getInstance_name() + '|' +
				biInstance.getInstance_type() + '|' +
				biInstance.getInstance_subtype() + '|' +
				biInstance.getInstance_level() + '|' +
				biInstance.getInstance_diff() + '|' +
				biInstance.getInstance_status() + '|' +
				biInstance.getInstance_result() + '|' +
				biInstance.getTeam_id() + '|' +
				biInstance.getInstance_used_time() + '|' +
				biInstance.getInstance_floor() + '\n';
	}

	/**
	 * 怪物击杀日志
	 * @param bi
	 * @param biMonster_kill
	 * @return
	 */
	public static String str(BI bi, BIMonster_kill biMonster_kill) {
		if (bi == null || biMonster_kill == null) return "";
		return "monster_kill:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biMonster_kill.getInstance_id() + '|' +
				biMonster_kill.getInstance_name() + '|' +
				biMonster_kill.getInstance_type() + '|' +
				biMonster_kill.getInstance_level() + '|' +
				biMonster_kill.getMonster_id() + '|' +
				biMonster_kill.getMonster_name() + '|' +
				biMonster_kill.getMonster_type() + '|' +
				biMonster_kill.getMonster_level() + '|' +
				biMonster_kill.getDps() + '|' +
				biMonster_kill.getDps_rank() + '|' +
				biMonster_kill.getGuild_id() + '|' +
				biMonster_kill.getMonster_subtype() + '|' +
				biMonster_kill.getDps_guild_rank() + '\n';
	}

	/**
	 * 死亡日志
	 * @param bi
	 * @param biDeath
	 * @return
	 */
	public static String str(BI bi, BIDeath biDeath) {
		if (bi == null || biDeath == null) return "";
		return "death:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biDeath.getInstance_id() + '|' +
				biDeath.getInstance_name() + '|' +
				biDeath.getInstance_type() + '|' +
				biDeath.getInstance_level() + '|' +
				biDeath.getKiller_type() + '|' +
				biDeath.getKiller_id() + '|' +
				biDeath.getKiller_name() + '|' +
				biDeath.getKiller_level() + '|' +
				biDeath.getKiller_combat() + '\n';
	}

	/**
	 * 聊天日志
	 * @param bi
	 * @param biChat
	 * @return
	 */
	public static String str(BI bi, BIChat biChat) {
		if (bi == null || biChat == null) return "";
		return "chat:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biChat.getChat_source() + '|' +
				biChat.getSys_flag() + '|' +
				biChat.getChat_txt() + '|' +
				biChat.getObject_account_id() + '|' +
				biChat.getObject_role_id() + '|' +
				biChat.getObject_role_name() + '|' +
				biChat.getObject_ip() + '|' +
				biChat.getObject_device_id() + '\n';
	}

	/**
	 * 活动日志
	 * @param bi
	 * @param biActivity
	 * @return
	 */
	public static String str(BI bi, BIActivity biActivity) {
		if (bi == null || biActivity == null) return "";
		return "activity:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biActivity.getActivity_group_id() + '|' +
				biActivity.getActivity_group_name() + '|' +
				biActivity.getActivity_id() + '|' +
				biActivity.getActivity_name() + '|' +
				biActivity.getActivity_type() + '|' +
				biActivity.getReward_group_id() + '|' +
				biActivity.getReward_group_name() + '|' +
				biActivity.getReward_id() + '|' +
				biActivity.getReward_name() + '|' +
				biActivity.getActivity_status() + '|' +
				biActivity.getActivity_round() + '|' +
				biActivity.getReward_level() + '\n';
	}

	/**
	 * 在线人数(服务器)
	 * @param bi
	 * @param biOnline
	 * @return
	 */
	public static String str(BI bi, BIOnline biOnline) {
		if (biOnline == null) return "";
		return "online:1.2.2" + '|' +
				biOnline.getLog_id() + '|' +
				biOnline.getEvent_time() + '|' +
				biOnline.getZone_id() + '|' +
				biOnline.getServer_id() + '|' +
				biOnline.getOnline_num() + '|' +
				biOnline.getHang_up_num() + '|' +
				biOnline.getAccount_list() + '\n';
	}

	/**
	 * 在线人数(网关) 仅限采用网关架构的游戏
	 * @param bi
	 * @param biOnline_gate
	 * @return
	 */
	public static String str(BI bi, BIOnline_gate biOnline_gate) {
		if (bi == null || biOnline_gate == null) return "";
		return "online_gate:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biOnline_gate.getGate_server_id() + '|' +
				biOnline_gate.getOnline_num() + '\n';
	}

	/**
	 * 装备变化
	 * @param bi
	 * @param biEquip
	 * @return
	 */
	public static String str(BI bi, BIEquip biEquip) {
		if (bi == null || biEquip == null) return "";
		return "equip:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biEquip.getItem_id() + '|' +
				biEquip.getItem_name() + '|' +
				biEquip.getEquip_type() + '|' +
				biEquip.getPart() + '|' +
				biEquip.getEquip_act_type() + '|' +
				biEquip.getStar() + '|' +
				biEquip.getLevel() + '|' +
				biEquip.getColor() + '|' +
				biEquip.getStr_level() + '|' +
				biEquip.getBind() + '|' +
				biEquip.getSuit() + '|' +
				biEquip.getGem_id() + '|' +
				biEquip.getGem_num() + '|' +
				biEquip.getGem_pos() + '|' +
				biEquip.getGem_set() + '|' +
				biEquip.getGem_rating() + '|' +
				biEquip.getLock_num() + '|' +
				biEquip.getLock_type() + '|' +
				biEquip.getChange_score() + '|' +
				biEquip.getBefore_score() + '|' +
				biEquip.getAfter_score() + '|' +
				biEquip.getRefine_level() + '\n';
	}

	/**
	 * 称号/目标
	 * @param bi
	 * @param biRealm
	 * @return
	 */
	public static String str(BI bi, BIRealm biRealm) {
		if (bi == null || biRealm == null) return "";
		return "realm:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biRealm.getUp_type() + '|' +
				biRealm.getRealm_act_type() + '|' +
				biRealm.getTask_id() + '|' +
				biRealm.getProgress() + '\n';
	}

	/**
	 * 拍卖
	 * @param bi
	 * @param biAuction
	 * @return
	 */
	public static String str(BI bi, BIAuction biAuction) {
		if (biAuction == null) return "";
		return "auction:1.2.2" + '|' +
				biAuction.getLog_id() + '|' +
				biAuction.getEvent_time() + '|' +
				biAuction.getZone_id() + '|' +
				biAuction.getServer_id() + '|' +
				biAuction.getGame_version() + '|' +
				biAuction.getAccount_id() + '|' +
				biAuction.getRole_id() + '|' +
				biAuction.getRole_name() + '|' +
				biAuction.getRole_sex() + '|' +
				biAuction.getRole_prof() + '|' +
				biAuction.getRole_level() + '|' +
				biAuction.getRole_vip_level() + '|' +
				biAuction.getRole_combat() + '|' +
				biAuction.getApp_id() + '|' +
				biAuction.getChannel_id() + '|' +
				biAuction.getSource_id() + '|' +
				biAuction.getPlatform() + '|' +
				biAuction.getDevice_id() + '|' +
				biAuction.getDevice_name() + '|' +
				biAuction.getDevice_screen() + '|' +
				biAuction.getDevice_version() + '|' +
				biAuction.getUser_ip() + '|' +
				biAuction.getMap_id() + '|' +
				biAuction.getTarget_id() + '|' +
				biAuction.getTarget_type() + '|' +
				biAuction.getAuction_op_type() + '|' +
				biAuction.getItem_id() + '|' +
				biAuction.getItem_type() + '|' +
				biAuction.getItem_colour() + '|' +
				biAuction.getItem_lev() + '|' +
				biAuction.getItem_star() + '|' +
				biAuction.getItem_no() + '|' +
				biAuction.getItem_name() + '|' +
				biAuction.getItem_num() + '|' +
				biAuction.getBid_price() + '|' +
				biAuction.getFixed_price() + '|' +
				biAuction.getGuild_id() + '\n';
	}

	/**
	 * 公会战
	 * @param bi
	 * @param biGuild_war
	 * @return
	 */
	public static String str(BI bi, BIGuild_war biGuild_war) {
		if (biGuild_war == null) return "";
		return "guild_war:1.2.2" + '|' +
				biGuild_war.getLog_id() + '|' +
				biGuild_war.getEvent_time() + '|' +
				biGuild_war.getZone_id() + '|' +
				biGuild_war.getServer_id() + '|' +
				biGuild_war.getGame_version() + '|' +
				biGuild_war.getAccount_id() + '|' +
				biGuild_war.getRole_id() + '|' +
				biGuild_war.getRole_name() + '|' +
				biGuild_war.getRole_sex() + '|' +
				biGuild_war.getRole_prof() + '|' +
				biGuild_war.getRole_level() + '|' +
				biGuild_war.getRole_vip_level() + '|' +
				biGuild_war.getRole_combat() + '|' +
				biGuild_war.getApp_id() + '|' +
				biGuild_war.getChannel_id() + '|' +
				biGuild_war.getSource_id() + '|' +
				biGuild_war.getPlatform() + '|' +
				biGuild_war.getDevice_id() + '|' +
				biGuild_war.getDevice_name() + '|' +
				biGuild_war.getDevice_screen() + '|' +
				biGuild_war.getDevice_version() + '|' +
				biGuild_war.getUser_ip() + '|' +
				biGuild_war.getMap_id() + '|' +
				biGuild_war.getGuild_id() + '|' +
				biGuild_war.getGuild_name() + '|' +
				biGuild_war.getGuild_level() + '|' +
				biGuild_war.getRate_level() + '|' +
				biGuild_war.getType() + '|' +
				biGuild_war.getCamp() + '|' +
				biGuild_war.getRound() + '|' +
				biGuild_war.getSeries_win() + '\n';
	}

	/**
	 * 公会成员变化
	 * @param bi
	 * @param biGuild_member
	 * @return
	 */
	public static String str(BI bi, BIGuild_member biGuild_member) {
		if (bi == null || biGuild_member == null) return "";
		return "guild_member:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biGuild_member.getGuild_id() + '|' +
				biGuild_member.getGuild_level() + '|' +
				biGuild_member.getChange_type() + '|' +
				biGuild_member.getStatus() + '|' +
				biGuild_member.getMember() + '|' +
				biGuild_member.getGuild_power() + '\n';
	}

	/**
	 * 公会升级
	 * @param bi
	 * @param biGuild_upgrade
	 * @return
	 */
	public static String str(BI bi, BIGuild_upgrade biGuild_upgrade) {
		if (bi == null || biGuild_upgrade == null) return "";
		return "guild_upgrade:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biGuild_upgrade.getGuild_id() + '|' +
				biGuild_upgrade.getGuild_name() + '|' +
				biGuild_upgrade.getLevel_type() + '|' +
				biGuild_upgrade.getBefore_guild_level() + '|' +
				biGuild_upgrade.getLater_guild_level() + '\n';
	}

	/**
	 * 新手引导
	 * @param bi
	 * @param biNewbie_guide
	 * @return
	 */
	public static String str(BI bi, BINewbie_guide biNewbie_guide) {
		if (bi == null || biNewbie_guide == null) return "";
		return "newbie_guide:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biNewbie_guide.getStep_id() + '|' +
				biNewbie_guide.getStatus() + '\n';
	}

	/**
	 * 签到
	 * @param bi
	 * @param biSign
	 * @return
	 */
	public static String str(BI bi, BISign biSign) {
		if (bi == null || biSign == null) return "";
		return "sign:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biSign.getSign_type() + '|' +
				biSign.getSign_reward_type() + '|' +
				biSign.getSign_reward_id() + '|' +
				biSign.getSign_continue_day() + '|' +
				biSign.getSign_act_id() + '|' +
				biSign.getSign_act_day() + '\n';
	}

	/**
	 * 诸界远征
	 * @param bi
	 * @param biWorld_expedition
	 * @return
	 */
	public static String str(BI bi, BIWorld_expedition biWorld_expedition) {
		if (bi == null || biWorld_expedition == null) return "";
		return "world_expedition:1.2.2"  + '|' + IDConfigUtil.getLogId() + '|' + bi.toString() + '|' +
				biWorld_expedition.getExpedition_id() + '|' +
				biWorld_expedition.getCity_id() + '|' +
				biWorld_expedition.getCity_type() + '|' +
				biWorld_expedition.getAdd_score() + '|' +
				biWorld_expedition.getServer_score() + '|' +
				biWorld_expedition.getCity_owner_id() + '\n';
	}

	/**
	 * 游戏区服服务器状态变更
	 * @param biServer_op
	 * @return
	 */
	public static String str(BIServer_op biServer_op) {
		if (biServer_op == null) return "";
		return "server_op:1.2"  + '|' + IDConfigUtil.getLogId() + '|' +
				biServer_op.getEvent_time() + '|' +
				biServer_op.getZone_id() + '|' +
				biServer_op.getServer_id() + '|' +
				biServer_op.getGame_version() + '|' +
				biServer_op.getZone_name() + '|' +
				biServer_op.getServer_name() + '|' +
				biServer_op.getServer_open_time() + '|' +
				biServer_op.getServer_open_day() + '|' +
				biServer_op.getServer_op_type() + '\n';
	}

}
