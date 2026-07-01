/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common.behavior;

import com.game.behavior.structs.BaseBehavior;
import com.game.behavior.structs.IBehavior;
import com.game.behavior.structs.type.JumpBehavior;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Entity;
import com.game.structs.EntityState;
import game.core.map.IMapObject;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 *
 * @author zenghai
 */
public class JumpBehaviorScript implements IScript, IBehavior {

    protected Logger log = LogManager.getLogger("BaseBehavior");

    @Override
    public int getId() {
        return ScriptEnum.JumpBehaviorCommonScript;
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

        if (behavior.IsOver()) {
            return true;
        }
        Entity mover = (Entity) owner;
        
        JumpBehavior jb = (JumpBehavior) behavior;
        mover.changeCurPos(jb.getEndTarPos(), true);

        mover.removeSate(EntityState.Jump);
        mover.addState(EntityState.Stand);
        behavior.Over();
        return true;
    }

    @Override
    public void action(BaseBehavior behavior) {
       
    }
}
