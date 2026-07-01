package com.game.leaderpreach.manager;

import com.game.leaderpreach.inter.ILeaderPreachScript;
import com.game.leaderpreach.struct.LPPlayerData;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @explain: desc
 * @time Created on 2019/11/8 15:27.
 * @author: tc
 */
public class LeaderPreachManager {
	private static final Logger log = LogManager.getLogger(LeaderPreachManager.class);

	private enum Singleton {
		INSTANCE;
		LeaderPreachManager manager;

		Singleton() {
			this.manager = new LeaderPreachManager();
		}

		LeaderPreachManager getProcessor() {
			return manager;
		}
	}

	public static LeaderPreachManager getInstance() {
		return LeaderPreachManager.Singleton.INSTANCE.getProcessor();
	}


	/**
	 * 脚本
	 *
	 * @return
	 */
	public ILeaderPreachScript getScript() {
		IScript script = ScriptManager.getInstance().GetScriptClass(ScriptEnum.LeaderPreachScript);
		if (script == null) {
			log.error("没有找到脚本：" + ScriptEnum.LeaderPreachScript);
			return null;
		}
		return (ILeaderPreachScript) script;
	}

	// 等待进入的玩家列表
	private List<LPPlayerData> waitPIds = new ArrayList<>();
	// 所有副本地图列表
	private long lastResetDataTime = TimeUtils.Time();


	public List<LPPlayerData> getWaitPIds() {
		return waitPIds;
	}

	public long getLastResetDataTime() {
		return lastResetDataTime;
	}

	public void setLastResetDataTime(long lastResetDataTime) {
		this.lastResetDataTime = lastResetDataTime;
	}
}
