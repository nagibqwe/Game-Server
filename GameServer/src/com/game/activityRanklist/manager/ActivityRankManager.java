package com.game.activityRanklist.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_RankAwardItem_Bean;
import com.data.bean.Cfg_RankAwardType_Bean;
import com.game.activityRanklist.timer.ActivityRankTimer;
import com.game.manager.Manager;
import com.game.activityRanklist.script.IActivityRankScript;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import game.core.script.IScript;
import java.util.ArrayList;
import java.util.HashMap;

public class ActivityRankManager {

    //活动结束后服务器统一发放奖励的排行榜的配置数据缓存。防止每次ontimer的时候都计算。key是rankType
    public HashMap<Integer, ArrayList<Cfg_RankAwardItem_Bean>> timeEndRankAward = new HashMap<>();

    //玩家主动领取奖励的排行榜配置数据缓存。用于检测奖励红点。key是rankType
    public HashMap<Integer, ArrayList<Cfg_RankAwardItem_Bean>> initiativeRankAward = new HashMap<>();

    //排行榜结束天数 key是rankType
    public HashMap<Integer, Integer> endDay = new HashMap<>();

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ActivityRankManager processor;

        Singleton() {
            this.processor = new ActivityRankManager();
        }

        ActivityRankManager getProcessor() {
            return processor;
        }
    }

    private ActivityRankManager() {
        initTimeEndRankAward();
        GameServer.getInstance().getAssistThread().addTimerEvent(new ActivityRankTimer());
    }

    public static ActivityRankManager getInstance() {
        return ActivityRankManager.Singleton.INSTANCE.getProcessor();
    }

    public IActivityRankScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.ActivityRankScript);
        return (IActivityRankScript) script;
    }

    private void initTimeEndRankAward() {
        for (Cfg_RankAwardItem_Bean bean : CfgManager.getCfg_RankAwardItem_Container().getValuees()) {
            Cfg_RankAwardType_Bean typeBean = CfgManager.getCfg_RankAwardType_Container().getValueByKey(bean.getOwner_id());
            int rankType = typeBean.getLink_rank_id();

            if (bean.getAward_type() == 1) {
                ArrayList<Cfg_RankAwardItem_Bean> list = initiativeRankAward.get(rankType);
                if (list == null) {
                    list = new ArrayList<>();
                    initiativeRankAward.put(rankType, list);
                }
                list.add(bean);
            }
            else {
                ArrayList<Cfg_RankAwardItem_Bean> list = timeEndRankAward.get(rankType);
                if (list == null) {
                    list = new ArrayList<>();
                    timeEndRankAward.put(rankType, list);
                }
                list.add(bean);
            }
            endDay.put(rankType, typeBean.getEnd_day());
        }
    }
}
