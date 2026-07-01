package com.game.player.ai;

import com.game.aiRunner.AiRunner;
import com.game.manager.YedMgr;
import com.game.map.structs.BaseNpc;
import com.game.player.structs.Player;
import com.game.structs.Position;
import com.game.utils.MapUtils;
import com.game.yed.YedAI;
import org.apache.logging.log4j.Logger;

/**
 * Created by huhu on 2017/9/18.
 */
public class AiMoveTo extends AiRunner {
    private long lastMoveTick = 0;
    private final Logger ailogger = YedMgr.getInstance().getAiLogger();

    public AiMoveTo(Object owner, Object... params) {
        super("move", owner, params);
        //ailogger.debug(String.format("moveai debug %s", owner), new Throwable());
    }

    private void Sa_MoveAi_MoveTo(){
        if(aiOwner == null){
            return;
        }
        Position endpos = (Position)ps[1];
        if(aiOwner instanceof BaseNpc){
            ((BaseNpc)aiOwner).moveAi_MoveToPos(endpos);
        }
        ailogger.warn(String.format("moveai debug role:%d Sa_MoveAi_MoveTo endpos:%s", getId(), endpos));
    }

    private long getId() {
        if(aiOwner instanceof BaseNpc){
            return ((BaseNpc)aiOwner).getId();
        }
        return 0;
    }

    private void Sa_SendChangeMapMsg(){
        int targetmapid = (int)ps[0];
        if(aiOwner instanceof Player){
            ((Player)aiOwner).sendChangeMapMsg(targetmapid);
        }
        ailogger.debug(String.format("moveai debug role:%d Sa_SendChangeMapMsg targetmapid:%d", getId(), targetmapid));
    }

    private boolean Sa_IsArrival(){
        if(aiOwner instanceof BaseNpc){
            int targetmapid = (int)ps[0];
            Position endpos = (Position)ps[1];
            int mapid = ((BaseNpc)aiOwner).getMapModelId();
            if(mapid != targetmapid){
                //ailogger.info(String.format("moveai debug role:%d Sa_IsArrival mapid[%d] != targetmapid[%d]", getId(), mapid, targetmapid));
                return false;
            }
            double dis = MapUtils.getDistance(endpos, ((BaseNpc)aiOwner).getCurPos());

            if(dis < getParamDis()){
                ailogger.warn(String.format("moveai debug role:%d Sa_IsArrival dis[%d] < getParamDis[%d]", getId(), (int)dis, (int)getParamDis()));
            }
            return dis < getParamDis();

        }
        ailogger.info(String.format("moveai debug role:%d Sa_IsArrival true", getId()));
        return true;
    }

    private double getParamDis() {
        if(ps.length > 2)
            return (double)ps[2];
        return 2;
    }

    private boolean Sa_IsSameMap(){
        if(aiOwner instanceof BaseNpc){
            int targetmapid = (int)ps[0];
            int mapid = ((BaseNpc)aiOwner).getMapModelId();
            ailogger.debug(String.format("moveai debug role:%d Sa_IsSameMap mapid[%d] == targetmapid[%d]", getId(), mapid, targetmapid));
            return mapid == targetmapid;
        }
        ailogger.debug(String.format("moveai debug role:%d Sa_IsSameMap false", getId()));
        return false;
    }

    private void Sa_ChangePos(YedAI ai){
        long now = System.currentTimeMillis();
        if (lastMoveTick <= 0) {
            lastMoveTick = now;
        }
        long dt = now - lastMoveTick;
        lastMoveTick = now;

        if(aiOwner instanceof BaseNpc){
            BaseNpc o = (BaseNpc)aiOwner;
            o.moveTick(dt);
        }
        ailogger.debug(String.format("moveai debug role:%d Sa_ChangePos %s", getId(), ((BaseNpc)aiOwner).getCurPos()));
    }

    @Override
    public void OnRemove() {
        BaseNpc n = ((BaseNpc)aiOwner);
        n.onMoveAiRemove();
    }
}
