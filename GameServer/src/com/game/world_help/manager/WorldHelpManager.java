package com.game.world_help.manager;

import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.world_help.inter.IWorldHelpScript;
import com.game.world_help.struct.TaskHelpInfo;
import com.game.world_help.struct.WorldHelp;
import com.game.world_help.struct.WorldHelpInfo;
import game.core.command.CommandProcessor;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @explain: 世界支援管理类
 * @time Created on 2019/12/11 15:07.
 * @author: tc
 */
public class WorldHelpManager extends CommandProcessor {
	private final Logger log = LogManager.getLogger(WorldHelpManager.class);

	private enum Singleton {
		INSTANCE;
		WorldHelpManager manager;

		Singleton() {
			this.manager = new WorldHelpManager("worldHelp");
		}

		WorldHelpManager getProcessor() {
			return manager;
		}
	}

	public static WorldHelpManager getInstance() {
		return WorldHelpManager.Singleton.INSTANCE.getProcessor();
	}

	public WorldHelpManager(String name) {
		super(name);
	}

	/**
	 * 脚本
	 *
	 * @return
	 */
	public IWorldHelpScript getScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.WorldHelpScript);
		if (script == null) {
			log.error("没有找到脚本：" + ScriptEnum.WorldHelpScript);
			return null;
		}
		return (IWorldHelpScript) script;
	}

	// 支援ID
	private AtomicInteger atoId = new AtomicInteger();
	// 参与列表
	private ConcurrentHashMap<Long, WorldHelp> join = new ConcurrentHashMap<>();
	// 帮助列表
	private ConcurrentHashMap<Integer, WorldHelpInfo> help = new ConcurrentHashMap<>();
	// 历史支援信息
	private ConcurrentHashMap<Integer, WorldHelpInfo> history = new ConcurrentHashMap<>();
	// BossCode
	private ConcurrentHashMap<Long, List<Integer>> boss = new ConcurrentHashMap<>();

	// 任务帮助列表
	private ConcurrentHashMap<Integer, TaskHelpInfo> taskHelp = new ConcurrentHashMap<>();

	// 任务支援参与列表
	private ConcurrentHashMap<Long, WorldHelp> taskJoin = new ConcurrentHashMap<>();

	// 任务历史支援信息
	private ConcurrentHashMap<Integer, TaskHelpInfo> taskHistory = new ConcurrentHashMap<>();

	/**
	 * get key id
	 * @return
	 */
	public int newID() {
		return atoId.incrementAndGet();
	}

	public ConcurrentHashMap<Long, WorldHelp> getJoin() {
		return join;
	}

	public ConcurrentHashMap<Integer, WorldHelpInfo> getHelp() {
		return help;
	}

	public ConcurrentHashMap<Integer, WorldHelpInfo> getHistory() {
		return history;
	}

	public ConcurrentHashMap<Long, List<Integer>> getBoss() {
		return boss;
	}

	public ConcurrentHashMap<Integer, TaskHelpInfo> getTaskHelp() {
		return taskHelp;
	}

	public ConcurrentHashMap<Long, WorldHelp> getTaskJoin() {
		return taskJoin;
	}

	public ConcurrentHashMap<Integer, TaskHelpInfo> getTaskHistory() {
		return taskHistory;
	}

	/**
	 * 玩家上线
	 * @param player
	 */
	public void playerOnline(Player player) {
		getScript().online(player);
	}

	/**
	 * 玩家下线
	 * @param player
	 */
	public void playerOffline(Player player) {
		getScript().offline(player);
	}

	/**
	 * 如果处理过程中发生异常，记录错误日志信息.
	 *
	 * @param message 错误信息描述
	 */
	@Override
	public void writeError(String message) {
		log.error("世界支援失败：" + message);
	}

	/**
	 * 如果处理过程中发生异常，记录错误日志信息.
	 *
	 * @param message 错误信息描述
	 * @param t       产生错误的异常类
	 */
	@Override
	public void writeError(String message, Throwable t) {
		log.error("世界支援失败：" + message, t);
	}
}
