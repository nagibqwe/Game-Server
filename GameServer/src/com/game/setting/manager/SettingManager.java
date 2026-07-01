package com.game.setting.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.setting.script.ISettingScript;
import com.game.setting.struct.FeedBackInfo;
import game.core.script.IScript;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class SettingManager {

    /**
     * 缓存GM发送的反馈信息
     * TODO 存储
     */
    private ConcurrentHashMap<Long, List<FeedBackInfo>> feedbackMap = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, List<FeedBackInfo>> getFeedbackMap() {
        return feedbackMap;
    }

    public void setFeedbackMap(ConcurrentHashMap<Long, List<FeedBackInfo>> feedbackMap) {
        this.feedbackMap = feedbackMap;
    }

    public ISettingScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SettingBaseScript);
        if (is instanceof ISettingScript) {
            return (ISettingScript) is;
        } else {
            return null;
        }
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        SettingManager processor;

        Singleton() {
            this.processor = new SettingManager();
        }

        SettingManager getProcessor() {
            return processor;
        }
    }

    public static SettingManager getInstance() {
        return SettingManager.Singleton.INSTANCE.getProcessor();
    }
}
