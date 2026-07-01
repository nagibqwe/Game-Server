package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Buff_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.buff.structs.Buff;
import com.game.buff.structs.BuffDefine;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;


/**
 * @author lw
 */
public class BuffAttributeScript implements IAttributeScript {

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.BUFF;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        for (Buff buff : player.getBuffs()) {

            Cfg_Buff_Bean config = CfgManager.getCfg_Buff_Container().getValueByKey(buff.getBuffId());
            if (config.getType() == BuffDefine.Type_Attribute) {
                for (ReadArray<Integer> porpert : config.getPorperty().getValuees()) {
                    int type = porpert.get(0);
                    int value = porpert.get(1);
                    if (type < 1 || type > AttributeType.ATTR_MAX) {
                        continue;
                    }
                    att.addAttribute(type, value * buff.getOverlap());
                }
//            } else if (config.getType() == BuffDefine.Type_ExpRate) {
//                att.addAttribute(AttributeType.ATTR_MonserExp, config.getParam1() * buff.getOverlap());
            }
        }
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
        return ScriptEnum.BuffAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
