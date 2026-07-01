package com.game.home.manager;

import com.game.home.script.IHomeScript;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.script.struct.ScriptEnum;
import game.core.script.IScript;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/7/12 18:05
 * @Auth ZUncle
 */
public class HomeManager {

    int turn;       //赛季状态
    /**
     * 家装大赛排名
     */
    final List<GlobalPlayerWorldInfo > rank = new ArrayList<>();

    public List<GlobalPlayerWorldInfo> getRank() {
        return rank;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        HomeManager manager;

        Singleton() {
            this.manager = new HomeManager();
        }

        HomeManager getProcessor() {
            return manager;
        }
    }

    public static HomeManager getInstance() {
        return HomeManager.Singleton.INSTANCE.getProcessor();
    }


    /**
     * 获取逻辑脚本
     * @return
     */
    public IHomeScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.HomeScript);
        return (IHomeScript) is;
    }

}
