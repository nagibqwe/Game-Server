package com.game.welfare.manager;

import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.welfare.script.IRetrieveResScript;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @explain: 资源找回
 * @time Created on 2020/1/6 17:41.
 * @author: tc
 */
public class RetrieveResManager {
	private final Logger log = LogManager.getLogger(RetrieveResManager.class);

	private enum Singleton {
		INSTANCE;
		RetrieveResManager manager;

		Singleton() {
			this.manager = new RetrieveResManager();
		}

		RetrieveResManager getProcessor() {
			return manager;
		}
	}

	public static RetrieveResManager getInstance() {
		return RetrieveResManager.Singleton.INSTANCE.getProcessor();
	}

	private RetrieveResManager() { }

	/**
	 * 玩家上线
	 * @param player
	 */
	public void playerOnline(Player player) {
		getScript().online(player);
	}

	/**
	 * script
	 * @return
	 */
	public IRetrieveResScript getScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.RetrieveResScript);
		if (script == null) {
			log.error("没有找到该福利类型的脚本,脚本ID：" + ScriptEnum.RetrieveResScript);
			return null;
		}
		return (IRetrieveResScript) script;
	}
}
