package com.game.questionnaire.manager;

import com.game.manager.Manager;
import com.game.questionnaire.script.IQuestionnaireScript;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

public class QuestionnaireManager {

    private enum Singleton {
        INSTANCE;
        QuestionnaireManager manager;

        Singleton() {
            this.manager = new QuestionnaireManager();
        }
        QuestionnaireManager getProcessor() {
            return manager;
        }
    }

    public static QuestionnaireManager getInstance() {
        return QuestionnaireManager.Singleton.INSTANCE.getProcessor();
    }


    public IQuestionnaireScript deal() {
        IScript script = Manager.scriptManager.GetScriptClass(ScriptEnum.QuestionnaireScript);
        if (script instanceof  IQuestionnaireScript) {
            return (IQuestionnaireScript) script;
        }
        return null;
    }
}
