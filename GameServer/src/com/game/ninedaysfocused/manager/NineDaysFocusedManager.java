package com.game.ninedaysfocused.manager;

import com.game.manager.Manager;
import com.game.ninedaysfocused.script.INineDaysFocused;
import com.game.ninedaysfocused.structs.BossHurtData;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by CLc on 2019/7/22.
 */

public class NineDaysFocusedManager {



    private boolean isOpen = false;    //开启

    private ConcurrentHashMap<Long, ConcurrentHashMap<Long, BossHurtData>> bossHurtList = new ConcurrentHashMap<>();//每个地图对应的阵营伤害统计

    private enum Singleton {

        INSTANCE;
        NineDaysFocusedManager processor;

        Singleton() {
            this.processor = new NineDaysFocusedManager();
        }

        NineDaysFocusedManager getProcessor() {
            return processor;
        }
    }

    public static NineDaysFocusedManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public static INineDaysFocused deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NineDaysFocusedBaseScript);
        if (is instanceof INineDaysFocused) {
            return (INineDaysFocused) is;
        }
        throw new NullPointerException("没有找到九天争锋实现接口！");
    }


    public boolean getIsOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
    }

    public  ConcurrentHashMap<Long, ConcurrentHashMap<Long, BossHurtData>> getBossHurtList(){return bossHurtList;}
}
