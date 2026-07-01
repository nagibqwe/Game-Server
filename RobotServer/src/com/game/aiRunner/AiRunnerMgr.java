package com.game.aiRunner;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by huhu on 2017/9/18.
 */
public class AiRunnerMgr {

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        AiRunnerMgr processor;

        Singleton() {
            this.processor = new AiRunnerMgr();
        }

        AiRunnerMgr getProcessor() {
            return processor;
        }
    }

    /**
     * 获取PlayerManager的实例对象.
     *
     * @return
     */
    public static AiRunnerMgr getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    private LinkedList<AiRunner> runnerlist = new LinkedList<>();
    private LinkedList<AiRunner> removelist = new LinkedList<>();

    public void push(AiRunner runer) {
        runnerlist.push(runer);
    }

    public void pushRemove(AiRunner runner){
        removelist.push(runner);
    }

    public void Update(){
        Iterator<AiRunner> it = runnerlist.iterator();
        while (it.hasNext()) {
            AiRunner ai = it.next();
            ai.Update();
            if(ai.IsExit() || removelist.contains(ai)){
                it.remove();
                Remove(ai);
                removelist.remove(ai);
            }
        }
    }
    private void Remove(AiRunner runer){
        runnerlist.remove(runer);
        runer.OnRemove();
    }
}
