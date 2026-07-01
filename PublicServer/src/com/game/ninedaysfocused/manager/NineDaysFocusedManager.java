package com.game.ninedaysfocused.manager;

import com.game.manager.Manager;
import com.game.ninedaysfocused.script.INineDaysFocused;
import com.game.ninedaysfocused.structs.NineDaysCrossPlayer;
import com.game.ninedaysfocused.structs.NineDaysQueuer;
import com.game.script.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by 542 on 2019/7/22.
 */
public class NineDaysFocusedManager {
    private static final Logger log = LogManager.getLogger(NineDaysFocusedManager.class);

    private boolean isOpen = false;

    private  ConcurrentHashMap<Integer, ConcurrentHashMap<Long, NineDaysCrossPlayer>> playerMatchList = new ConcurrentHashMap<>(); //玩家报名数据 服务器组ID，玩家列表

    private  ConcurrentHashMap<Integer, ConcurrentHashMap<Long, NineDaysQueuer>> readyqueue = new ConcurrentHashMap<>(); //准备阶段

    private  ConcurrentHashMap<Integer, ConcurrentHashMap<Long, NineDaysQueuer>> p2pqueue = new ConcurrentHashMap<>(); //匹配阶段





    private enum Singleton {

        INSTANCE;
        NineDaysFocusedManager manager;

        Singleton() {
            this.manager = new NineDaysFocusedManager();
        }

        NineDaysFocusedManager getProcessor() {
            return manager;
        }
    }

    public INineDaysFocused deal(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NineDaysFocusedScript);
        if (is instanceof INineDaysFocused) {
            return (INineDaysFocused) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    //SoulAnimalForestManager
    public static NineDaysFocusedManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    public void  setOpen(boolean isOpen){this.isOpen = isOpen;}

    public boolean getOpen(){return  isOpen;}

    public ConcurrentHashMap<Integer,  ConcurrentHashMap<Long, NineDaysCrossPlayer> > getPlayerMatchList(){return playerMatchList;}

    public ConcurrentHashMap<Integer,ConcurrentHashMap<Long, NineDaysQueuer> >getReadyqueue() {
        return readyqueue;
    }

    public ConcurrentHashMap<Integer,ConcurrentHashMap<Long, NineDaysQueuer> >getP2pqueue() {
        return p2pqueue;
    }
}
