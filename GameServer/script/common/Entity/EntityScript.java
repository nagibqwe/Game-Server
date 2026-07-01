package common.Entity;

import com.game.behavior.manager.BehaviorManager;
import com.game.behavior.structs.BehaviorType;
import com.game.behavior.structs.type.MoveBehavior;
import com.game.entity.script.IEntityScript;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.structs.Fighter;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.Position;
import game.core.script.IScript;
import game.message.MapMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 2017/9/26.
 */
public class EntityScript implements IScript, IEntityScript {
    @Override
    public int getId() {
        return ScriptEnum.EntityCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void runToTarget(Entity ths, Fighter target, float attackDis) {
        MapObject map = MapManager.getInstance().getMap(ths.gainMapId());
        Position pursuePos = target.getPursuePos(ths, attackDis);
        // 删除掉之前的
        BehaviorManager.CancelBehaviorByType(ths, BehaviorType.Move);
        List<Position> roads;
        if (!ths.isNeedCheckCanMove() || Utils.isCanMove(map, ths.gainCurPos(), pursuePos)) {
            roads = new ArrayList<>();
            roads.add(ths.gainCurPos());
            roads.add(pursuePos);
        } else {
            roads = MapUtils.findRoads(map, ths.gainCurPos(), pursuePos, 500);
        }

        ths.setRoads(roads);
//        StringBuilder roadsinfo = new StringBuilder();
//        for(Position r : roads){
//            roadsinfo.append(String.format(" x:%f y:%f\n", r.getX(), r.getY()));
//        }
//        YedMgr.getInstance().getAiLogger().info(String.format("debug move runToTarget this:%d this pos:%s roads:%s", ths.getId(), ths.gainCurPos()
//        , roadsinfo.toString()));
        ths.addState(EntityState.Move);
        //构建移动消息
        MapMessage.ResMoveTo.Builder moveMsg = MapMessage.ResMoveTo.newBuilder();
        moveMsg.setObjectId(ths.getId());
        for (Position pos : roads) {
            moveMsg.addPosList(MapUtils.getPos(pos));
        }
        MessageUtils.send_to_roundPlayer(ths, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), false);
        //插入移动行为
        BehaviorManager.InsertOnlyBehavior(ths, new MoveBehavior(ths));
    }

    @Override
    public void runToPosition(Entity ths, Position targetpos) {
        MapObject map = MapManager.getInstance().getMap(ths.gainMapId());

        List<Position> roads;
        if (!ths.isNeedCheckCanMove() || Utils.isCanMove(map, ths.gainCurPos(), targetpos)) {
            roads = new ArrayList<>();
            roads.add(ths.gainCurPos());
            roads.add(targetpos);
        } else {
            roads = MapUtils.findRoads(map, ths.gainCurPos(), targetpos, 500);
        }


        ths.setRoads(roads);
        ths.addState(EntityState.Move);
        //构建移动消息
        MapMessage.ResMoveTo.Builder moveMsg = MapMessage.ResMoveTo.newBuilder();
        moveMsg.setObjectId(ths.getId());
        for (Position pos : roads) {
            moveMsg.addPosList(MapUtils.getPos(pos));
        }
        MessageUtils.send_to_roundPlayer(ths, MapMessage.ResMoveTo.MsgID.eMsgID_VALUE, moveMsg.build().toByteArray(), false);

        //插入移动行为
        BehaviorManager.CancelBehaviorByType(ths, BehaviorType.Move);
        BehaviorManager.InsertBehavior(ths, new MoveBehavior(ths));
    }
}
