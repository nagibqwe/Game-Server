package common.buff.script;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.game.buff.script.IBuffBehavior;
import com.game.buff.structs.Buff;
import com.game.manager.Manager;
import com.game.map.structs.MapUtils;
import com.game.script.structs.ScriptEnum;
import com.game.structs.Fighter;
import game.core.script.IScript;

/**
 * @Desc TODO
 * @Date 2020/7/31 16:13
 * @Auth ZUncle
 */
public class AddHpBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff, Fighter source, Fighter target) {
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
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        if (owner.isDie()) {
            return 0;
        }
        if (owner.getCurHp() >= owner.getAttribute().MaxHP()) {
            return 0;
        }
        //TODO 是否禁疗
        if (Manager.buffManager.deal().haveJinLiao(owner)) {
            return 0;
        }
        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        long addValue = config.getParam1();
        if (config.getParam2() > 0) {
            addValue = attacker.getLevel() * config.getParam2();
        }

        MapUtils.sendBuffHp(attacker, owner, (int) addValue);

        long need = owner.getAttribute().MaxHP() - owner.getCurHp();

        long min = Math.min(addValue, need);

        owner.setCurHp(owner.getCurHp() + min);

        owner.onHpChange(owner);

        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.AddHpBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
