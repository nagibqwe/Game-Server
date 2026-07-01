package com.game.huaxinflysword.manager;
import com.game.huaxinflysword.script.IHuaxinFlySword;
import com.game.huaxinflysword.script.ISwordSoulScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * Created by 542 on 2020/5/21.
 */
public class HuaxinFlySwordManager {

    private enum Singleton {

        INSTANCE;
        HuaxinFlySwordManager manager;

        Singleton() {
            this.manager = new HuaxinFlySwordManager();
        }

        HuaxinFlySwordManager getProcessor() {
            return manager;
        }
    }

    public static HuaxinFlySwordManager getInstance() {
        return HuaxinFlySwordManager.Singleton.INSTANCE.getProcessor();
    }

    public IHuaxinFlySword deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HuaxinFlySwordScript);
        return (IHuaxinFlySword) is;
    }

    public ISwordSoulScript swordSoulScript() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.SwordSoulTowerScript);
        return (ISwordSoulScript) is;
    }
}
