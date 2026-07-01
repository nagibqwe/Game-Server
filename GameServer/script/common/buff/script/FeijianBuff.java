package common.buff.script;

import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @author gsj
 * @create 2020/5/22 10:30
 */
public class FeijianBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
        return 0;
    }

    @Override
    public void overlap(Buff buff, Fighter owner) {
    }

    @Override
    public int remove(Buff buff,Fighter owner) {
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        return 0;
    }

    @Override
    public int timeout(Buff buff,Fighter target) {
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.FeijianBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
