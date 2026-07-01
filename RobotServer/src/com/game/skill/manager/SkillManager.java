package com.game.skill.manager;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.skill.script.ISkillScript;
import game.core.script.IScript;

public class SkillManager {
    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        SkillManager processor;

        Singleton() {
            this.processor = new SkillManager();
        }

        SkillManager getProcessor() {
            return processor;
        }
    }

    public static SkillManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ISkillScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SkillScript);
        if (is instanceof ISkillScript) {
            return (ISkillScript) is;
        }
        return null;
    }

}
