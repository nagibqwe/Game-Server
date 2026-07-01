package com.game.structs;

import java.util.List;

/**
 * Created by huhu on 2017/9/22.
 * 给ISkillTargetChecker脚本保存上下文
 */
public class STCheckerMaker {
    public int checkerScriptID;
    public List<Fighter> enemys;
    public int maxTargetCount;

    private STCheckerMaker(){

    }

    public static STCheckerMaker createChecker(int scriptid, List<Fighter> ens, int maxc){
        STCheckerMaker ret = new STCheckerMaker();
        ret.checkerScriptID = scriptid;
        ret.enemys = ens;
        ret.maxTargetCount = maxc;
        return ret;
    }
}
