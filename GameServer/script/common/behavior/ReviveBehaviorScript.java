package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.BehaviorStatus;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.ReviveBehavior;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.monster.structs.Monster;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import game.core.map.IMapObject;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import game.message.MapMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ReviveBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.ReviveBehaviorCommonScript;
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
        //已经结束了，就不要再执行了
        if (behavior.IsOver()) {
            return true;
        }

        Entity mover = (Entity) owner;

        behavior.Over();

        if (!mover.isDie()) {
            return true;
        }
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
        ReviveBehavior rb = (ReviveBehavior) behavior;
        Entity mover = (Entity) owner;
        if (TimeUtils.Time() < rb.getReviveTime()) {
            return;
        }
        if (!mover.isDie()) {
            mover.removeSate(EntityState.Dead);
            MapMessage.ResRelive.Builder msg = MapMessage.ResRelive.newBuilder();
            msg.setPlayerId(mover.getId());
            msg.setMapId(mover.getCurGps().getModelId());
            msg.setCurPos(MapUtils.getPos(mover.gainCurPos()));
            MessageUtils.send_to_roundPlayer(mover, MapMessage.ResRelive.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
            behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
            return;
        }

        if (mover instanceof Player) {
            Player player = (Player) mover;
            Manager.playerManager.deal(ScriptEnum.PlayerReliveBaseScript).doHomeRelive(player);
            log.info("[" + mover.getName() + "]自动复活 pos= " + mover.gainCurPos() + "血量=" + player.getCurHp() + "自动复活结束时间:" + rb.getReviveTime() + ", 当前=" + TimeUtils.Time());
        }

        if (owner instanceof Monster) {
            mover.reset();
            Manager.mapManager.manager().onEnterMap(owner);
        }
        behavior.SetState(BehaviorStatus.ACTIVITYSTATUS_OVERED);
    }

}
