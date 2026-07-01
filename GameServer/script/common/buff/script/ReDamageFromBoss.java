package common.buff.script;

import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author huhu
 */
public class ReDamageFromBoss implements IScript, IBuffBehavior {

    protected static final Logger log = LogManager.getLogger(ReDamageFromBoss.class);

    @Override
    public int add(Buff buff,Fighter src, Fighter target) {

        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int timeout(Buff buff,Fighter target) {

        return 0;
    }

    @Override
    public int remove(Buff buff,Fighter target) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.ReDamageFromBossBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
