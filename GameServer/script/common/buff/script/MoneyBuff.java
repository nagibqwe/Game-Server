package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @author zenghai
 */
public class MoneyBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        Player player = (Player) target;
        float rate = config.getParam1() / 10000f;
        player.setMoneyRate(rate + player.getMoneyRate());

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
    public int remove(Buff buff,Fighter owner) {
        if (!(owner instanceof Player)) {
            return 0;
        }
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
        Player player = (Player) owner;
        float rate = config.getParam1() / 10000f;
        player.setMoneyRate(player.getMoneyRate() - rate);
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.MoneyRateBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
