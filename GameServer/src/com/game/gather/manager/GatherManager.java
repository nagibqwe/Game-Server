package com.game.gather.manager;

import com.game.gather.script.IGatherScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

/**
 * 采集物管理
 * Created by zcd on 2018/2/8.
 */
public class GatherManager {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        //一个枚举的元素，它就代表了Singleton的一个实例//一个枚举的元素，它就代表了Singleton的一个实例
        INSTANCE;
        GatherManager manager;

        Singleton() {
            this.manager = new GatherManager();
        }

        GatherManager getProcessor() {
            return manager;
        }
    }

    public static GatherManager getInstance() {
        return GatherManager.Singleton.INSTANCE.getProcessor();
    }

    public IGatherScript deal(){
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.GatherManagerBaseScript);
        if(script instanceof IGatherScript){
            return (IGatherScript)script;
        }
        return null;
    }
}
