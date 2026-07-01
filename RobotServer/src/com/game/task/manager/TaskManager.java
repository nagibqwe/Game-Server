package com.game.task.manager;

import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.task.script.IMainTaskScript;
import com.game.task.script.ITaskScript;
import game.core.script.IScript;

public class TaskManager {

    private enum Singleton {

        INSTANCE;
        TaskManager processor;

        Singleton() {
            this.processor = new TaskManager();
        }

        TaskManager getProcessor() {
            return processor;
        }
    }

    public static TaskManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public ITaskScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.TaskBaseScript);
        if (is instanceof ITaskScript) {
            return (ITaskScript) is;
        }
        return null;
    }

    public IMainTaskScript mainTask() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.MainTaskBaseScript);
        if (is instanceof IMainTaskScript) {
            return (IMainTaskScript) is;
        }
        return null;
    }

}
