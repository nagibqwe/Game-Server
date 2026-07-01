/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.monster.structs.Monster;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.map.IMapObject;
import game.core.map.Position;
import game.core.script.IScript;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class RunBackBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.RunBackBehaviorCommonScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public boolean Cancel(BaseBehavior behavior) {
        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return behavior.Over();
        }
        if (!(owner instanceof Entity)) {
            return behavior.Over();
        }

        action(behavior);

        Entity mover = (Entity) owner;
        if (mover instanceof Monster) {
            Monster monster = (Monster) mover;
            if (monster.getSpeedRate() > 0.5) {
                monster.setSpeedRate(0.5f);
                sendEntitySpeed(mover);
            }
        }
        mover.clearRoads();
        mover.removeSate(EntityState.Move);
        mover.removeSate(EntityState.Run);
        mover.removeSate(EntityState.RunBack);
        mover.addState(EntityState.Stand);
        behavior.Over();
        sendMonsterRun(mover);
        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {
        IMapObject owner = behavior.getOwner();
        if (owner == null) {
            return;
        }
        if (!(owner instanceof Entity)) {
            return;
        }
        Entity mover = (Entity) owner;

        if (mover.getRoads().isEmpty()) {

            if (mover instanceof Monster) {
                Monster monster = (Monster) mover;
                if (monster.getSpeedRate() > 0.5) {
                    monster.setSpeedRate(0.5f);
                    sendEntitySpeed(mover);
                }
//                LOGGER.error("[" + mover.getName() + "]移动行为完毕" + mover.gainCurPos() + "出生点：" +monster.getInitPos());
            }

            mover.removeSate(EntityState.Move);
            mover.removeSate(EntityState.Run);
            mover.removeSate(EntityState.RunBack);
            mover.addState(EntityState.Stand);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            sendMonsterRun(mover);
            return;
        }

        if (behavior.getOffset() <= 0) {
            //LOGGER.error("[" + mover.getName() + "]移动行为执行 时间为0 ");
            return;
        }

        float speed = mover.gainFinalMoveSpeed() / 100.0f;
        
        if (speed <= 0) {
            mover.clearRoads();
            return;
        }

        float moveDis = speed * behavior.getOffset() / 1000f;

        while (!mover.getRoads().isEmpty()) {
            Position pos = mover.getRoads().get(0);
            float dis = Utils.getDistance(pos, mover.gainCurPos());

            if (moveDis > dis) {
                Position old = mover.gainCurPos();
                mover.changeCurPos(pos, true);
                moveDis -= dis;
                mover.getRoads().remove(0);
//                LOGGER.error("[" + mover.getName() + "]方向移动中途行为执行old=" + old + "new=" + pos + "dis=" + dis + "speed=" + speed + "time=" + behavior.getOffset());
            } else {
                Position dirPos = Utils.getDirPos(mover.gainCurPos(), pos, moveDis);
                Position old = mover.gainCurPos();
                mover.changeCurPos(dirPos, true);
//                LOGGER.error("[" + mover.getName() + "]方向移动目标方向："+pos+"，行为dis="+dis+", 执行old=" + old + "new=" + dirPos + "moveDis=" + moveDis + "speed=" + speed + "time=" + behavior.getOffset());
                return;
            }
        }
    }

    //移动速度变化
    private void sendEntitySpeed(Entity entity) {
        MapMessage.ResMoveSpeedChange.Builder msg = MapMessage.ResMoveSpeedChange.newBuilder();
        msg.setObjectId(entity.getId());
        msg.setValue(entity.gainFinalMoveSpeed());
        MessageUtils.send_to_roundPlayer(entity, MapMessage.ResMoveSpeedChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //怪物是否跑动
    private void sendMonsterRun(Entity entity) {
        MapMessage.ResUpdateMoveState.Builder msg = MapMessage.ResUpdateMoveState.newBuilder();
        msg.setId(entity.getId());
        msg.setIsRun(EntityState.Run.compare(entity.getState()));
        MessageUtils.send_to_roundPlayer(entity, MapMessage.ResUpdateMoveState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }
}
