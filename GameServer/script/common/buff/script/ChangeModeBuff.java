package common.buff.script;

import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2020/7/31 16:13
 * @Auth ZUncle
 */
public class ChangeModeBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff, Fighter source, Fighter target) {
        target.setChangeModelID(buff.getBuffId());
        Manager.buffManager.deal().addChangeModeSkill(target, buff.getBuffId());
        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int timeout(Buff buff, Fighter target) {
        return 0;
    }

    @Override
    public int remove(Buff buff, Fighter owner) {
        owner.setChangeModelID(0);
        Manager.buffManager.deal().removeChangeModeSkill(owner, buff.getBuffId());
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {

        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.BianshenBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
