package common.attribute;

import com.game.attribute.AttributeUtils;
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
public class ShiHaiAttributeScript implements IAttributeScript {
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.ShiHai;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        AttributeUtils.addAttribute(att, player.getShiHaiData().getShiHaiAttr());

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
        return ScriptEnum.ShiHaiAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
