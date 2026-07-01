package common.chum;

import com.data.*;
import com.data.bean.Cfg_Mapsetting_Bean;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.chum.inter.IChumScript;
import com.game.chum.struct.*;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.map.script.IChumRewardHandler;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.robot.manager.RobotManager;
import com.game.script.structs.ScriptEnum;
import com.game.setting.struct.SettingType;
import com.game.task.structs.Task;
import com.game.team.structs.TeamInfo;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.script.IScript;
import game.core.util.BeanUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.ChumMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

/**
 * @explain: desc
 * @time Created on 2019/10/22 17:17.
 * @author: tc
 */
public class ChumScript implements IChumScript {
	private static final Logger log = LogManager.getLogger(ChumScript.class);

	/**
	 * 获取scriptId
	 * 九 零 一起玩 www.9017 5.com
	 * @return
	 */
	@Override
	public int getId() {
		return ScriptEnum.ChumScript;
	}

	/**
	 * 调用脚本
	 *
	 * @param args 参数
	 * @return
	 */
	@Override
	public Object call(Object... args) {
		return null;
	}

	/**
	 * 使用古道热肠道具效果
	 *
	 * @param player
	 * @param typ
	 */
	@Override
	public void useItem(Player player, ChumPrivilege typ) {
		if (typ == ChumPrivilege.NONE)
			return;

		switch (typ) {
			case FB_TASK:
				Manager.taskManager.deal().quickFinish(player, Task.DAILY_TASK,0, false, false,0);
				break;
			case SB_TASK:
				Manager.taskManager.deal().quickFinish(player, Task.DAILY_TASK,1, false, false,0);
				break;
			case DNY:
				IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.StarCopyActivityScript);
				if (is instanceof IChumRewardHandler) {
					IChumRewardHandler handler = (IChumRewardHandler) is;
					handler.useChumItemCompleteCloneMap(player);
				}
				break;
			case TIA:
				IScript is2 = Manager.scriptManager.GetScriptClass(ScriptEnum.FairyLandActivityScript);
				if (is2 instanceof IChumRewardHandler) {
					IChumRewardHandler handler = (IChumRewardHandler) is2;
					handler.useChumItemCompleteCloneMap(player);
				}
				break;
		}
	}

	/**
	 * 添加活跃点
	 *
	 * @param player
	 * @param value
	 */
	@Override
	public void addActiveValue(Player player, int value) {
		if (value <= 0)
			return;

		ChumBeanExt ext = Manager.chumManager.query(player.getId());
		if (ext == null)
			return;

		ChumMember member = Manager.chumManager.member(ext, player.getId());
		if (member == null)
			return;

		member.setExp(member.getExp() + value);
		addExp(ext, value);
		Manager.chumManager.saveChumBeanExt(ext);
	}

	/**
	 * 获取同袍同泽次数与战力继承比
	 * index=0:次数
	 * index=1:战力继承万分比
	 *
	 * @param player
	 * @return
	 */
	@Override
	public List<Integer> getCallSoul(Player player) {
	//List<Integer> list = new ArrayList<>();
	//list.add(0); // 每日可召次数
	//list.add(0); // 每次召的机器人，属性继承比例

	//ChumBeanExt ext = Manager.chumManager.query(player.getId());
	//if (ext != null) {
	//	Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(ext.getLvl());
	//	if (bean != null && bean.getRight1_parm().size() == 2) {
	//		list.set(0, bean.getRight1_parm().get(0));
	//		list.set(1, bean.getRight1_parm().get(1));
	//	}
	//}
	//return list;
		return null;
	}

	/**
	 * 古道热肠奖励
	 *
	 * @param player
	 * @param typ
	 */
	@Override
	public void helpReward(Player player, ChumPrivilege typ) {
		//if (typ == ChumPrivilege.NONE)
		//	return;
//
		//// 检测该玩家当日该类型是否已经互助
		//ChumData data = player.getChumData();
		//if (data == null)
		//	return;
//
		//if (data.checkIsSend(typ.getValue()))
		//	return;
//
		//ChumBeanExt ext = Manager.chumManager.query(player.getId());
		//if (ext == null)
		//	return;
//
		//if (ext.getMembers().size() == 1)
		//	return;
//
		//int cntId = -1;
		//switch (typ) {
		//	case FB_TASK:
		//		cntId = MessageString.ChumTaskFB;
		//		break;
		//	case SB_TASK:
		//		cntId = MessageString.ChumTaskSB;
		//		break;
		//	case DNY:
		//		cntId = MessageString.ChumDNY;
		//		break;
		//	case TIA:
		//		cntId = MessageString.ChumTIA;
		//		break;
		//}
		//String content = cntId + "@_@" + player.getName();
//
		//// 当前等级的挚友是否包含该特权
		//for (int i = 1; i <= ext.getLvl(); i++) {
		//	Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(i);
		//	if (bean != null && bean.getRight2_parm().size() == 2 && bean.getRight2_parm().get(0) == typ.getValue()) {
		//		for (ChumMember member : ext.getMembers()) {
		//			if (player.getId() == member.getRoleID())
		//				continue;
//
		//			data.setSend(typ.getValue());
//
		//			// 邮件发送道具
		//			List<Item> items = Item.createItems(bean.getRight2_parm().get(1), 1, true);
		//			mailAm(member.getRoleID(), MessageString.ChumTaskReward, content, items);
		//		}
		//		break;
		//	}
		//}
	}

	/**
	 * 挚友组队BUFF
	 *
	 * @param teamInfo
	 */
	@Override
	public void checkChumBuff(TeamInfo teamInfo) {
		if (teamInfo == null || teamInfo.getMembers().size() <= 1)
			return;

		boolean add = true;
		ChumBeanExt cb = null;
		for (long m : teamInfo.getMembers()) {
			Player player = Manager.playerManager.getPlayer(m);
			if (player == null) {
				add = false;
				continue;
			}

			ChumBeanExt ext = Manager.chumManager.query(m);
			if (ext != null)
				removeChumBuff(player, ext.getLvl());
			else
				add = false;

			if (cb == null)
				cb = ext;

			if (cb != null && ext != null && !cb.getId().equals(ext.getId()))
				add = false;
		}

		if (add && cb != null) {
			List<Integer> buffs = queryBuff(cb.getLvl());
			for (long m : teamInfo.getMembers()) {
				Player player = Manager.playerManager.getPlayerCache(m);
				for (int buffId : buffs)
					Manager.buffManager.deal().onAddBuff(player, player, buffId);
			}
		}
	}

	/**
	 * 移除组队BUFF
	 *
	 * @param player
	 */
	@Override
	public void removeChumBuff(Player player) {
		if (player == null)
			return;
		ChumBeanExt ext = Manager.chumManager.query(player.getId());
		if (ext == null)
			return;
		removeChumBuff(player, ext.getLvl());
	}

	private void removeChumBuff(Player player, int lvl) {
		List<Integer> buffs = queryBuff(lvl);
		for (int buffId : buffs)
			Manager.buffManager.deal().onRemoveBuff(player, buffId);
	}

	/**
	 * 退出挚友组
	 *
	 * @param playerID
	 * @param lvl      退出时挚友组织的等级
	 */
	@Override
	public void exitChum(long playerID, int lvl) {
		Player player = Manager.playerManager.getPlayer(playerID);
		if (player != null) {
			removeTitle(player, lvl);
			removeChumBuff(player, lvl);
		}
	}

	private void joinChum(long playerID) {
		Player player = Manager.playerManager.getPlayer(playerID);
		if (player == null)
			return;

		ChumBeanExt ext = Manager.chumManager.query(playerID);
		if (ext == null)
			return;

		addTitle(player, ext.getLvl());
	}

	private void addExp(ChumBeanExt ext, int value) {
		//if (value <= 0)
		//	return;
//
		//Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(ext.getLvl());
		//if (bean == null || bean.getExp() == 0)
		//	return;
//
		//int all = ext.getExp() + value;
		//if (all < bean.getExp()) {
		//	ext.setExp(all);
		//} else {
		//	// 升级
		//	ext.setLvl(ext.getLvl() + 1);
		//	ext.setExp(0);
		//	chumLvlUp(ext);
		//	addExp(ext, all - bean.getExp());
		//}
	}

	/**
	 * 挚友组升级
	 *
	 * @param ext
	 */
	private void chumLvlUp(ChumBeanExt ext) {
		for (ChumMember member : ext.getMembers()) {
			Player pl = Manager.playerManager.getPlayer(member.getRoleID());

			removeTitle(pl, ext.getLvl() - 1);
			addTitle(pl, ext.getLvl());
		}
	}

	/**
	 * 移除老称号
	 *
	 * @param player
	 * @param lvl
	 */
	private void removeTitle(Player player, int lvl) {
		List<Integer> titleList = queryTitle(lvl);
		for (int title : titleList) {
			Manager.titleManager.deal().uninstallTitle(player, title);
		}
	}

	/**
	 * 添加称号
	 *
	 * @param player
	 * @param lvl
	 */
	private void addTitle(Player player, int lvl) {
		List<Integer> titleList = queryTitle(lvl);
		for (int title : titleList) {
			Manager.titleManager.deal().useTitleItem(player, title, 1, ItemChangeReason.IntimateLevelUpGet);
		}

		if (titleList.size() > 0) {
			String content = MessageString.ChumTitleDesc + "@_@" + lvl;
			mail(player.getId(), MessageString.ChumTitleReward, content);
		}
	}

	private List<Integer> queryBuff(int lvl) {
		List<Integer> list = new ArrayList<>();
		//if (lvl <= 0) {
		//	log.error("获取BUFF错误, lvl=" + lvl + " stack:" + BeanUtil.getStack());
		//	return list;
		//}
//
		//for (int i = 1; i <= lvl; i++) {
		//	Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(i);
		//	if (bean.getRight4_parm() == 0)
		//		continue;
//
		//	if (!list.contains(bean.getRight4_parm()))
		//		list.add(bean.getRight4_parm());
		//}
		return list;
	}

	/**
	 * 查询称号列表
	 *
	 * @param lvl
	 * @return
	 */
	private List<Integer> queryTitle(int lvl) {
		List<Integer> list = new ArrayList<>();
		//if (lvl <= 0) {
		//	log.error("获取称号错误, lvl=" + lvl + " stack:" + BeanUtil.getStack());
		//	return list;
		//}
//
		//for (int i = 1; i <= lvl; i++) {
		//	Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(i);
//
		//	for (int title : bean.getRight3_parm().getValue()) {
		//		if (!list.contains(title))
		//			list.add(title);
		//	}
		//}
		return list;
	}

	/**
	 * 请求挚友主面板信息
	 *
	 * @param player
	 */
	@Override
	public void onReqChum(Player player) {
		ChumBeanExt ext = Manager.chumManager.query(player.getId());
		ChumMessage.ResChum.Builder builder = ChumMessage.ResChum.newBuilder();
		builder.setChum(packChum(player, ext));
		MessageUtils.send_to_player(player, ChumMessage.ResChum.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 请求挚友排行
	 *
	 * @param player
	 */
	@Override
	public void onReqRank(Player player) {
		List<ChumBeanExt> list = Manager.chumManager.getExtList();
		List<ChumMessage.Rank> ranks = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			ChumMessage.Rank.Builder rb = ChumMessage.Rank.newBuilder();
			rb.setC1(packPlayer(list.get(i).getRid1()));
			rb.setC2(packPlayer(list.get(i).getRid2()));
			rb.setName(list.get(i).getName());
			rb.setLvl(list.get(i).getLvl());
			ranks.add(rb.build());
		}
		ChumMessage.ResRank.Builder builder = ChumMessage.ResRank.newBuilder();
		builder.addAllRanks(ranks);
		MessageUtils.send_to_player(player, ChumMessage.ResRank.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 双向好友挚友信息
	 *
	 * @param player
	 */
	@Override
	public void onReqFriend(Player player) {
		List<Long> friends = Manager.friendManager.getMyIntimateFriend(player);
		List<ChumMessage.Friend> friendList = new ArrayList<>();
		for (long id : friends) {
			Player p = Manager.playerManager.getPlayer(id);
			if (p == null) {
				log.error("好友模块返回的数据，找不到该玩家：" + id);
				continue;
			}

			ChumMessage.Friend.Builder builder = ChumMessage.Friend.newBuilder();
			builder.setPlayer(packPlayer(id));

			ChumBeanExt ext = Manager.chumManager.query(id);
			if (ext != null) {
				builder.setName(ext.getName());
				builder.setId(ext.getId());
				Player c = Manager.playerManager.getPlayer(ext.getRid1());
				if (c != null)
					builder.setCreatePName(c.getName());
			} else {
				builder.setName("");
				builder.setId(0);
				builder.setCreatePName("");
			}

			friendList.add(builder.build());
		}
		ChumMessage.ResFriend.Builder builder = ChumMessage.ResFriend.newBuilder();
		builder.addAllFriends(friendList);
		MessageUtils.send_to_player(player, ChumMessage.ResFriend.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 邀请目标加入挚友
	 *
	 * @param player
	 * @param targetID
	 */
	@Override
	public void onReqInvite(Player player, long targetID) {
		if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Intimate)) {
			return;
		}

		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt != null) {
			if (player.getId() != selfExt.getRid1() && player.getId() != selfExt.getRid2())
				return;

			// 判断人数是否已满
			if (!canAdd(selfExt.getLvl(), selfExt.getMembers().size())) {
				MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumTargetMaxNumber);
				return;
			}
		}

		ChumBeanExt targetExt = Manager.chumManager.query(targetID);
		if (targetExt != null) {
			// 对方已经有挚友组织了
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumTargetHave);
			return;
		}

		Player target = Manager.playerManager.getPlayerOnline(targetID);
		if (target == null) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.GuildInviteFailed_OppNotOnLine);
			return;
		}

		if (!Manager.controlManager.deal().isOpenFunction(target, FunctionStart.Intimate)) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.C_MAIN_GONGNENGWEIKAIQI, target.getName());
			return;
		}

		// 目标是否设置自动拒绝
//		if (target.getSetting(SettingType.Auto_ChumInviteRefuse)) {
//			inviteMail(player.getId(), targetID, true, false);
//			return;
//		}

		ChumInvite invite = Manager.chumManager.getChumInvite(player.getId());
		if (invite.isCD()) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.DEAL_REPLAY_CDING);
			return;
		}

		// 目标是否已经在流程中
		ChumInvite invTar = Manager.chumManager.getChumInvite(targetID);
		if (invTar.isCD()) {
			// 玩家正在建立挚友组织
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumTargetCreating);
			return;
		}

		invTar.newCDTime();

		invite.setTargetID(targetID);
		invite.setInviteID(Manager.chumManager.getInviteID());
		invite.newCDTime();

		Manager.chumManager.getInviteMap().put(invite.getInviteID(), invite);

		// 通知目标
		ChumMessage.ResTarget.Builder builder = ChumMessage.ResTarget.newBuilder();
		builder.setInviteID(invite.getInviteID());
		builder.setName(player.getName());
		MessageUtils.send_to_player(target, ChumMessage.ResTarget.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 邀请确认
	 *
	 * @param player
	 * @param inviteID
	 * @param agree
	 */
	@Override
	public void onReqInviteConfirm(Player player, int inviteID, boolean agree) {
		ChumInvite invite = Manager.chumManager.getInviteMap().get(inviteID);
		if (invite == null)
			return;

		// 邀请者
		long invitePID = invite.getRoleID();
		// 被邀请者，自己
		long targetPID = invite.getTargetID();

		if (player.getId() != targetPID)
			return;

		// 移除邀请信息
		invite.reset();

		// 移除自己的锁定信息
		ChumInvite invTar = Manager.chumManager.getChumInvite(player.getId());
		invTar.reset();

		Manager.chumManager.getInviteMap().remove(inviteID);

		if (!agree) {
			// 拒绝
			inviteMail(invitePID, targetPID, false, agree);
			return;
		}

		// 同意
		ChumBeanExt selfExt = Manager.chumManager.query(targetPID);
		if (selfExt != null) {
			inviteMail(invitePID, targetPID, false, false);
			return;
		}

		ChumBeanExt invPID = Manager.chumManager.query(invitePID);
		if (invPID == null) {
			// 邀请者没有挚友组织
			long id = IDConfigUtil.getId();
			invPID = new ChumBeanExt(id, invitePID, targetPID);
			Manager.chumManager.getPlayerChumMap().put(invitePID, id);
			Manager.chumManager.getPlayerChumMap().put(targetPID, id);
			Manager.chumManager.insertChumBeanExt(invPID);

			joinChum(inviteID);
		} else {
			// 邀请者有挚友组织
			// 判断人数是否已满
			if (!canAdd(invPID.getLvl(), invPID.getMembers().size())) {
				MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumTargetMaxNumber);
				return;
			}
			invPID.getMembers().add(new ChumMember(targetPID, 0));
			Manager.chumManager.getPlayerChumMap().put(targetPID, invPID.getId());
			Manager.chumManager.saveChumBeanExt(invPID);
		}

		joinChum(targetPID);
		inviteMail(invitePID, targetPID, false, true);
	}

	/**
	 * 改名
	 *
	 * @param player
	 * @param name
	 */
	@Override
	public void onReqChangeName(Player player, String name) {
		if (name.equals(""))
			return;

		if (!checkName(name)) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveWords);
			return;
		}

		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt == null || (selfExt.getRid1() != player.getId() && selfExt.getRid2() != player.getId()))
			return;

		boolean succ = false;
		if (selfExt.getFreet() > 0) {
			// 有免费改名次数
			selfExt.setFreet((short) (selfExt.getFreet() - 1));
			succ = true;
		} else {
			// 判断道具是否足够
			int itemID = Global.Newguild_change_name_item.get(0);
			int num = Global.Newguild_change_name_item.get(1);

			if (!Manager.backpackManager.manager().canDeleteItemNum(player, itemID, num)) {
				return;
			}

			// 扣道具
			Manager.backpackManager.manager().onRemoveItem(player, itemID, num, ItemChangeReason.ChumChangeNameDec, IDConfigUtil.getLogId());
		}

		// 改名
		selfExt.setName(name);
		Manager.chumManager.saveChumBeanExt(selfExt);
		ChumMessage.ResChangeName.Builder builder = ChumMessage.ResChangeName.newBuilder();
		builder.setName(name);
		builder.setSucc(succ);
		builder.setFreeT(selfExt.getFreet());
		MessageUtils.send_to_player(player, ChumMessage.ResChangeName.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 改公告
	 *
	 * @param player
	 * @param anno
	 */
	@Override
	public void onReqChangeAnno(Player player, String anno) {
		if (!checkName(anno)) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.SensitiveWords);
			return;
		}

		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt == null || (selfExt.getRid1() != player.getId() && selfExt.getRid2() != player.getId()))
			return;
		selfExt.setAnno(anno);
		Manager.chumManager.saveChumBeanExt(selfExt);
		ChumMessage.ResChangeAnno.Builder builder = ChumMessage.ResChangeAnno.newBuilder();
		builder.setAnno(anno);
		builder.setSucc(true);
		MessageUtils.send_to_player(player, ChumMessage.ResChangeAnno.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 踢人
	 *
	 * @param player
	 * @param targetID
	 */
	@Override
	public void onReqKick(Player player, long targetID) {
		if (player.getId() == targetID)
			return;

		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt == null || (selfExt.getRid1() != player.getId() && selfExt.getRid2() != player.getId()))
			return;

		if (targetID == selfExt.getRid1() || targetID == selfExt.getRid2())
			return;

		boolean succ = false;
		Iterator<ChumMember> iterator = selfExt.getMembers().iterator();
		while (iterator.hasNext()) {
			ChumMember m = iterator.next();
			if (m.getRoleID() == targetID) {
				iterator.remove();
				Manager.chumManager.getPlayerChumMap().remove(targetID);
				Manager.chumManager.saveChumBeanExt(selfExt);
				exitChum(targetID, selfExt.getLvl());
				succ = true;
				break;
			}
		}
		ChumMessage.ResKick.Builder builder = ChumMessage.ResKick.newBuilder();
		builder.setTID(targetID);
		builder.setSucc(succ);
		MessageUtils.send_to_player(player, ChumMessage.ResKick.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 退出
	 *
	 * @param player
	 */
	@Override
	public void onReqExit(Player player) {
		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt == null)
			return;

		boolean succ = false;
		Iterator<ChumMember> iterator = selfExt.getMembers().iterator();
		while (iterator.hasNext()) {
			ChumMember m = iterator.next();
			if (m.getRoleID() == player.getId()) {
				iterator.remove();
				Manager.chumManager.saveChumBeanExt(selfExt);
				Manager.chumManager.getPlayerChumMap().remove(player.getId());
				exitChum(player.getId(), selfExt.getLvl());
				succ = true;
				break;
			}
		}

		if (succ) {
			// 退出成功
			if (selfExt.getMembers().size() == 0) {
				Manager.chumManager.deleteChumBeanExt(selfExt.getId());
			} else {
				Manager.chumManager.saveChumBeanExt(selfExt);
			}
		}

		ChumMessage.ResExit.Builder builder = ChumMessage.ResExit.newBuilder();
		builder.setSucc(succ);
		MessageUtils.send_to_player(player, ChumMessage.ResExit.MsgID.eMsgID_VALUE, builder.build().toByteArray());
	}

	/**
	 * 召唤元神
	 *
	 * @param player
	 */
	@Override
	public void onReqCallSoul(Player player) {
		// 检查能否召唤
		MapObject map = Manager.mapManager.getMap(player.gainMapId());
		Cfg_Mapsetting_Bean bean = CfgManager.getCfg_Mapsetting_Container().getValueByKey(map.getMapModelId());
		if (bean == null)
			return;

		if (bean.getIf_Newguild_Call() != 1)
			return;

		long now = TimeUtils.Time();
		if (now - player.getLastCallChumTime() <= Global.Newguild_BOSS_Clone_Times * 1000) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumCallCool);
			return;
		}

		ChumBeanExt selfExt = Manager.chumManager.query(player.getId());
		if (selfExt == null || selfExt.getMembers().size() <= 1) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumNoJoin);
			return;
		}

		List<Integer> soulProp = getCallSoul(player);
		if (soulProp.get(0) <= 0) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumCallSoulMax);
			return;
		}

		long count = Manager.countManager.getCount(player, BaseCountType.ChumCall, 0L);
		if (count >= soulProp.get(0)) {
			MessageUtils.notify_player(player, Notify.ERROR, MessageString.ChumCallSoulMax);
			return;
		}

		// 按照战力排序，每次最多召前三个
		List<ChumSoul> souls = new ArrayList<>();
		for (ChumMember member : selfExt.getMembers()) {
			if (member.getRoleID() == player.getId())
				continue;

			Player p = Manager.playerManager.getPlayer(member.getRoleID());
			if (p == null)
				continue;
			souls.add(new ChumSoul(p.getId(), p.getFightPoint()));
		}

		if (souls.size() == 0)
			return;

		player.setLastCallChumTime(now);

		Collections.sort(souls, (v1, v2) -> {
			if (v2.getFight() > v1.getFight()) return 1;
			else return -1;
		});

		Manager.countManager.addCount(player, BaseCountType.ChumCall, 0L, Count.RefreshType.CountType_Day, 1);
		int n = Math.min(souls.size(), Global.Newguild_BOSS_Clone_Num);
		List<Long> roleIDs = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			roleIDs.add(souls.get(i).getRoleID());
		}
		RobotManager.getInstance().deal().OnRobotHelpBattle(player, roleIDs,
				TimeUtils.Time() + Global.Newguild_BOSS_Clone_Times * 1000,
				soulProp.get(1) / 10000f, Global.Newguild_BOSS_BUFF);

		// 返回剩余次数
		ChumMessage.ResCallSoul.Builder builder = ChumMessage.ResCallSoul.newBuilder();
		builder.setNum(soulProp.get(0) - (int) count - 1);
		MessageUtils.send_to_player(player, ChumMessage.ResCallSoul.MsgID.eMsgID_VALUE, builder.build().toByteArray());
		MessageUtils.notify_player(player, Notify.NORMAL, MessageString.ChumCallCoolCD);
	}

	private boolean canAdd(int lvl, int have) {
		//Cfg_Newguild_Bean bean = CfgManager.getCfg_Newguild_Container().getValueByKey(lvl);
		//if (bean == null)
		//	return false;
//
		//return bean.getMaxpeople() >= have + 1;
		return false;
	}

	private void inviteMail(long invitePID, long targetPID, boolean auto, boolean agree) {
		Player player = Manager.playerManager.getPlayerCache(invitePID);
		Player target = Manager.playerManager.getPlayerCache(targetPID);
		if (agree) {
			// 同意
			String cnt1 = MessageString.ChumCreateSuccMail + "@_@" + target.getName();
			mail(invitePID, MessageString.ChumCreateSucc, cnt1);

			String cnt2 = MessageString.ChumCreateSuccMail + "@_@" + player.getName();
			mail(targetPID, MessageString.ChumCreateSucc, cnt2);
		} else {
			String cnt1 = MessageString.ChumPlayerRefuse + "@_@" + target.getName();
			mail(invitePID, MessageString.ChumCreateFailed, cnt1);

			// 自动拒绝，不发给被邀请者
			if (!auto) {
				String cnt2 = MessageString.ChumRefuseInviteMail + "@_@" + player.getName();
				mail(targetPID, MessageString.ChumRefuseInvite, cnt2);
			}
		}
	}

	private void mail(long pid, int title, String content) {
		mailAm(pid, title, content);
	}

	private void mailAm(long pid, int title, String content) {
		Manager.mailManager.sendMailToPlayer(pid, MailType.SysCommonRewardMail, MessageString.System, title, content);
	}

	private ChumMessage.Chum.Builder packChum(Player player, ChumBeanExt beanExt) {
		ChumMessage.Chum.Builder chum = ChumMessage.Chum.newBuilder();
		if (beanExt == null) {
			chum.setId(0);
			chum.setC1(packPlayer(0));
			chum.setC2(packPlayer(0));
			chum.setName("");
			chum.setAnno("");
			chum.setLvl(0);
			chum.setExp(0);
			chum.setFreeT(0);
			chum.setNum(0);
			List<ChumMessage.Member> memberList = new ArrayList<>();
			chum.addAllMembers(memberList);
			return chum;
		}

		chum.setId(beanExt.getId());
		chum.setC1(packPlayer(beanExt.getRid1()));
		chum.setC2(packPlayer(beanExt.getRid2()));
		chum.setName(beanExt.getName());
		chum.setAnno(beanExt.getAnno());
		chum.setLvl(beanExt.getLvl());
		chum.setExp(beanExt.getExp());
		chum.setFreeT(beanExt.getFreet());

		List<Integer> soulProp = getCallSoul(player);
		chum.setNum(soulProp.get(0) - (int) Manager.countManager.getCount(player, BaseCountType.ChumCall, 0L));

		// 成员
		List<ChumMessage.Member> memberList = new ArrayList<>();
		for (ChumMember member : beanExt.getMembers()) {
			memberList.add(packMember(member));
		}
		chum.addAllMembers(memberList);
		return chum;
	}

	private ChumMessage.Member packMember(ChumMember member) {
		ChumMessage.Member.Builder mm = ChumMessage.Member.newBuilder();
		ChumMessage.Player.Builder pb = packPlayer(member.getRoleID());
		mm.setPlayer(pb);
		if (pb.getRoleID() > 0) {
			Player player = Manager.playerManager.getPlayer(member.getRoleID());
			if (player != null)
				mm.setPower(player.getFightPoint());
			else
				mm.setPower(0);
		}
		mm.setExp(member.getExp());
		mm.setOnline(Manager.playerManager.getPlayerOnline(member.getRoleID()) != null);
		return mm.build();
	}

	private ChumMessage.Player.Builder packPlayer(long playerID) {
		ChumMessage.Player.Builder builder = ChumMessage.Player.newBuilder();
		builder.setRoleID(0);
		builder.setName("");
		builder.setLevel(0);
		builder.setVipLvl(0);
		builder.setCareer(0);
		if (playerID == 0)
			return builder;

		Player player = Manager.playerManager.getPlayer(playerID);
		if (player == null)
			return builder;

		builder.setRoleID(playerID);
		builder.setName(player.getName());
		builder.setLevel(player.getLevel());
		builder.setVipLvl(player.getStateVip().getLv());
		builder.setCareer(player.getCareer());
		return builder;
	}

	private boolean checkName(String name) {
		if (Utils.isContainsShielding_symbol(name)) return false;
		if (Utils.isForbiddenStr(name)) return false;
		return true;
	}
}
