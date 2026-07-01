package com.game.chum.manager;

import com.game.chum.inter.IChumScript;
import com.game.chum.struct.ChumBeanExt;
import com.game.chum.struct.ChumInvite;
import com.game.chum.struct.ChumMember;
import com.game.db.bean.ChumBean;
import com.game.db.dao.ChumDao;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @explain: 挚友管理类
 * @time Created on 2019/10/22 11:07.
 * @author: tc
 */
public class ChumManager {
	private static final Logger log = LogManager.getLogger(ChumManager.class);

	private enum Singleton {
		INSTANCE;
		ChumManager manager;

		Singleton() {
			this.manager = new ChumManager();
		}

		ChumManager getProcessor() {
			return manager;
		}
	}

	public static ChumManager getInstance() {
		return ChumManager.Singleton.INSTANCE.getProcessor();
	}

	private ChumManager() {
	}

	/**
	 * 脚本
	 *
	 * @return
	 */
	public IChumScript getScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.ChumScript);
		if (script == null) {
			log.error("没有找到脚本：" + ScriptEnum.ChumScript);
			return null;
		}
		return (IChumScript) script;
	}

	private final ChumDao chumDao = new ChumDao();
	/**
	 * 挚友列表
	 */
	private ConcurrentHashMap<Long, ChumBeanExt> chumMap = new ConcurrentHashMap<>();

	/**
	 * 玩家对应的挚友组
	 */
	private ConcurrentHashMap<Long, Long> playerChumMap = new ConcurrentHashMap<>();

	// 邀请挚友流程ID
	private AtomicInteger inviteID = new AtomicInteger();
	// 邀请者数据
	private ConcurrentHashMap<Long, ChumInvite> invitePlayerMap = new ConcurrentHashMap<>();
	// 流程数据
	private ConcurrentHashMap<Integer, ChumInvite> inviteMap = new ConcurrentHashMap<>();

	public ConcurrentHashMap<Long, Long> getPlayerChumMap() {
		return playerChumMap;
	}

	public int getInviteID() {
		return inviteID.incrementAndGet();
	}

	public void saveChumBeanExt(ChumBeanExt ext) {
		ext.setLastfreshtime(TimeUtils.Time());

		if (chumMap.containsKey(ext.getId()))
			update(ext);
		else
			insert(ext);

		chumMap.put(ext.getId(), ext);
	}

	public void insertChumBeanExt(ChumBeanExt ext) {
		ext.setLastfreshtime(TimeUtils.Time());
		insert(ext);
		chumMap.put(ext.getId(), ext);
	}

	public void deleteChumBeanExt(long id) {
		if (!chumMap.containsKey(id))
			return;

		delete(chumMap.get(id));
		chumMap.remove(id);
	}

	public List<ChumBeanExt> getExtList() {
		List<ChumBeanExt> list = new ArrayList<>(chumMap.values());
		list.sort((v1, v2) -> {
			if (v2.getLvl() > v1.getLvl())
				return 1;
			else if (v2.getLvl().equals(v1.getLvl()) && v2.getExp() > v1.getExp())
				return 1;
			else
				return -1;
		});
		return list;
	}

	/**
	 * 获取邀请临时数据
	 *
	 * @param playerID
	 * @return
	 */
	public ChumInvite getChumInvite(long playerID) {
		if (!invitePlayerMap.containsKey(playerID)) {
			ChumInvite invite = new ChumInvite();
			invite.setRoleID(playerID);
			invitePlayerMap.put(playerID, invite);
			return invite;
		}

		ChumInvite invite = invitePlayerMap.get(playerID);
		if (!invite.isCD() && invite.getInviteID() > 0) {
			inviteMap.remove(invite.getInviteID());
			invite.reset();
		}

		return invite;
	}

	public ConcurrentHashMap<Integer, ChumInvite> getInviteMap() {
		return inviteMap;
	}

	/**
	 * load
	 */
	public void load() {
		List<ChumBean> list = chumDao.selectAll();
		for (int i = 0; i < list.size(); i++) {
			ChumBeanExt beanExt = new ChumBeanExt(list.get(i));
			chumMap.put(beanExt.getId(), beanExt);

			for (ChumMember member : beanExt.getMembers()) {
				playerChumMap.put(member.getRoleID(), beanExt.getId());
			}
		}
	}

	/**
	 * 玩家上线
	 *
	 * @param player
	 */
	public void playerOnline(Player player) {
		ChumBeanExt ext = query(player.getUserId());
		if (ext == null)
			return;

		ext.setLastfreshtime(TimeUtils.Time());
		update(ext);
	}

	private void update(ChumBeanExt ext) {
		Manager.saveThreadManager.getOtherServerSave().deal(ext.getSave(), DbSqlName.CHUM_UPDATE, SaveServer.UPDATE);
	}

	private void insert(ChumBeanExt ext) {
		Manager.saveThreadManager.getOtherServerSave().deal(ext.getSave(), DbSqlName.CHUM_INSERT, SaveServer.INSERT);
	}

	private void delete(ChumBeanExt ext) {
		ChumBean bean = ext;
		bean.setWhere(ext.getId());
		Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.CHUM_DELETE, SaveServer.DELETE);
	}

	/**
	 * 查询挚友组
	 *
	 * @param playerID
	 * @return
	 */
	public ChumBeanExt query(long playerID) {
		if (!playerChumMap.containsKey(playerID))
			return null;

		long chumId = playerChumMap.get(playerID);
		ChumBeanExt ext = chumMap.getOrDefault(chumId, null);
		if (ext == null) {
			log.error("内部数据错误, playerID:" + playerID + " chumID:" + chumId);
			return null;
		}

		return ext;
	}

	/**
	 * 查询成员
	 *
	 * @param playerID
	 * @return
	 */
	public ChumMember member(long playerID) {
		ChumBeanExt ext = query(playerID);
		if (ext == null)
			return null;

		ChumMember member = member(ext, playerID);
		if (member == null)
			log.error("数据错误，玩家:" + playerID + " 不在挚友成员列表中:" + ext.getId());
		return member;
	}

	/**
	 * 查询成员
	 * @param ext
	 * @param playerID
	 * @return
	 */
	public ChumMember member(ChumBeanExt ext, long playerID) {
		for (int i = 0; i < ext.getMembers().size(); i++) {
			if (ext.getMembers().get(i).getRoleID() == playerID)
				return ext.getMembers().get(i);
		}
		return null;
	}
}
