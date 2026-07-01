package com.game.achievement.manager;

import com.game.achievement.script.IAchievementScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

public class AchievementManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        AchievementManager processor;

        Singleton() {
            this.processor = new AchievementManager();
        }

        AchievementManager getProcessor() {
            return processor;
        }
    }

    public static AchievementManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public IAchievementScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.AchievementBaseScript);
        return (IAchievementScript) script;
    }

}
