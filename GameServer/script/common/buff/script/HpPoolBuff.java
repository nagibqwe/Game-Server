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
 * @author zenghai
 */
public class HpPoolBuff implements IScript, IBuffBehavior {

    @Override
    public int add(Buff buff,Fighter source, Fighter target) {
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
        return 0;
    }

    @Override
    public int action(Buff buff, Fighter attacker, Fighter owner) {
        if (owner.isDie()) {
            return 0;
        }
        //TODO 是否禁疗
        if (Manager.buffManager.deal().haveJinLiao(owner)) {
            return 0;
        }

        Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());

        long need = owner.getAttribute().MaxHP() - owner.getCurHp();
        if (need <= 0 ) {
            return 0;
        }
        long remain = config.getParam1() * buff.getOverlap() - buff.getPar1();
        //TODO 断奶了
        if (remain <= 0) {
            buff.setDelete(true);
            return 0;
        }
        need = Math.min(need, config.getParam2());
        Long min = Math.min(need,remain);

        buff.setPar1(buff.getPar1() + min.intValue());
        owner.setCurHp(min + owner.getCurHp());
        MapUtils.sendBuffHp(attacker, owner, min.intValue());

        owner.onHpChange(owner);
        return 0;
    }

    @Override
    public int getId() {
        return ScriptEnum.HpPoolBuff;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }
}
