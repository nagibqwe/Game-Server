package com.game.bi.manager;

import com.game.bi.biqq.ReqMessage;
import com.game.bi.inter.IBi4399Script;
import com.game.bi.inter.IQQScript;
import com.game.bi.inter.IBiScript;
import com.game.bi.timer.BITimer;
import com.game.bi.timer.BiQQTimer;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import game.core.command.ICommand;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.thread.ServerThread;
import game.core.thread.TimerThread;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @explain: bi manager
 * @time Created on 2019/12/30 15:16.
 * @author: tc
 */
public class BIManager extends ServerThread {
	private final Logger log = LogManager.getLogger(BIManager.class);
	private static TimerThread timerThread = new TimerThread("bi_timer", 100);

	private BIFileLog biBak = new BIFileLog();
	private BIFileLog biSync = new BIFileLog();

	//BIQQ 需要重试的数据
	private List<ReqMessage> biqqReq = new ArrayList<>();

	public BIManager() {
		super(new ThreadGroup("biGroup"), "biThread", timerThread);

		biBak.setPath(SystemUtils.USER_DIR + "/bi");
		biBak.setPrefix("BiBak_");
		biBak.setSuffix(".log");
		biSync.setPath(ServerConfig.getBiConfig().getBiBakDir());

		biSync.setPrefix("BiSync_");
		biSync.setSuffix(".log");

		addTimerEvent(new BiQQTimer());
	}

	private enum Singleton {
		INSTANCE;
		BIManager manager;

		Singleton() {
			this.manager = new BIManager();
		}

		BIManager getProcessor() {
			return manager;
		}
	}

	public static BIManager getInstance() {
		return BIManager.Singleton.INSTANCE.getProcessor();
	}

	/**
	 * start1
	 */
	public void start1() {
		BIManager.getInstance().start();
		timerThread.start();
		BIManager.getInstance().addTimerEvent(new BITimer());
		getScript().init();
	}

	/**
	 * stop1
	 */
	public void stop1() {
		timerThread.stop();
		BIManager.getInstance().stop(true);
	}

	/**
	 * 获取普通BI脚本
	 * @return
	 */
	public IBiScript getScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.BIScript);
		if (script == null) {
			log.error("没有找到该bi类型的脚本, 脚本ID：" + ScriptEnum.BIScript);
			return null;
		}
		return (IBiScript) script;
	}

	/**
	 * 获取4399BI脚本
	 * @return
	 */
	public IBi4399Script get4399Script() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.BI4399Script);
		if (script == null) {
			log.error("没有找到该bi 4399类型的脚本, 脚本ID：" + ScriptEnum.BI4399Script);
			return null;
		}
		return (IBi4399Script) script;
	}

	/**
	 * 获取QQBI脚本
	 * @return
	 */
	public IQQScript getQQScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.QQScript);
		if (script == null) {
			log.error("没有找到该bi QQ类型的脚本, 脚本ID：" + ScriptEnum.QQScript);
			return null;
		}
		return (IQQScript) script;
	}

	private ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

	public ConcurrentLinkedQueue<String> getQueue() {
		return queue;
	}

	public BIFileLog getBiBak() {
		return biBak;
	}

	public BIFileLog getBiSync() {
		return biSync;
	}

	/**
	 * 玩家上线
	 * @param player
	 */
	public void playerOnline(Player player) {
		getScript().biUpdateBase(player);
	}

	public List<ReqMessage> getBiqqReq() {
		return biqqReq;
	}

	public void setBiqqReq(List<ReqMessage> biqqReq) {
		this.biqqReq = biqqReq;
	}

	@Override
	public boolean addCommand(ICommand command) {
		return super.addCommand(command);
	}
}
