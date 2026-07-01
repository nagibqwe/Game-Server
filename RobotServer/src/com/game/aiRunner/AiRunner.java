package com.game.aiRunner;

import com.game.manager.YedMgr;
import com.game.yed.YedAI;

/**
 * Created by huhu on 2017/9/18.
 */
public abstract class AiRunner {
    private YedAI curai = null;

    protected Object[] ps = null;

    protected Object aiOwner = null;

    protected String aiName;

    public AiRunner(String ainame, Object owner, Object...params) {
        aiOwner = owner;
        curai = new YedAI(this);
        aiName = ainame;
        curai.Load(ainame, false);
        curai.addCommonTarget(YedMgr.getInstance());
        if(params.length > 0){
            ps = new Object[params.length];
            System.arraycopy(params, 0, ps, 0, ps.length);
        }
    }

    public void Update(){
        if (curai == null) {
            return;
        }

        long now = System.currentTimeMillis();
        if (curai.lastUpdateTime <= 0) {
            curai.lastUpdateTime = now;
        }
        curai.Update(now - curai.lastUpdateTime);
        curai.lastUpdateTime = now;
    }

    public void updateData(Object owner, Object...params){
        aiOwner = owner;
        if(params.length > 0){
            ps = new Object[params.length];
            System.arraycopy(params, 0, ps, 0, ps.length);
        }
        if(curai == null){
            curai = new YedAI(this);
            curai.addCommonTarget(YedMgr.getInstance());
        }
        curai.Load(aiName, true);
    }

    public boolean IsExit(){
        return curai.IsExit();
    }

    public abstract void OnRemove();
}
