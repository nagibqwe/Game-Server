package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_State_power_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;

/**
 * @author lw
 */
public class StateVipAttributeScript implements IAttributeScript {


    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.StateVip;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }

        int stateVip = player.getStateVip().getLv();
        if (stateVip == 0) {
            return att;
        }

        Cfg_State_power_Bean bean = CfgManager.getCfg_State_power_Container().getValueByKey(stateVip);
        if (bean == null) {
            return att;
        }

        att.clean();
        for (ReadArray<Integer> attri : bean.getValue().getValuees()) {
            int type = attri.get(0);
            int value = attri.get(1);
            if (type < 1 || type > AttributeType.ATTR_MAX) {
                continue;
            }
            att.addAttribute(type, value);
        }
        att.calFinalAttackSpeed();
        return att;
    }

    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();
        return att;
    }

    @Override
    public int getId() {
        return ScriptEnum.StateVipAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
