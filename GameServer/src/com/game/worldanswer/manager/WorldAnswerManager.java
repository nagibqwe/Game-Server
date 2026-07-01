package com.game.worldanswer.manager;

import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.worldanswer.script.IWorldAnswer;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Created by cxl on 2019/7/15.
 */
public class WorldAnswerManager {

    private static final Logger log = LogManager.getLogger(WorldAnswerManager.class);

    private int answerStage = 0; ///答题的阶段

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        WorldAnswerManager manager;

        Singleton() {
            this.manager = new WorldAnswerManager();
        }

        WorldAnswerManager getProcessor() {
            return manager;
        }
    }

    public IWorldAnswer getIWorldAnswer(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.AnswerQuestionsBaseScript);
        if (is instanceof IWorldAnswer) {
            return (IWorldAnswer) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    //SoulAnimalForestManager
    public static WorldAnswerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void setAnswerStage(int answerStage){this.answerStage = answerStage;}

    public int getAnswerStage(){return  answerStage;}

}
