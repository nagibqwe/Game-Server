package common.attribute;

import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.godbook.struct.ConditionInfo;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class AmuletTaskAttributeScript implements IAttributeScript {

    private static final Logger logger = LogManager.getLogger(AmuletTaskAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.AmuletTask;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        //检查功能开关
//        if (!Manager.controlManager.FunctionOpen(player, FunctionEnum.GoddessMedalForm)) {
//            return att;
//        }

        final List<Integer> infos = new ArrayList<>();
        player.getGodBookInfo().values().forEach(
                n -> n.getAmuletInfo().values().stream()
                        .filter(m -> m.getStatus() == ConditionInfo.HAS_DRAW)
                        .forEach(k -> infos.add(k.getId()))
        );
       // for (Integer id : infos) {
       //     Cfg_AmuletCondition_Bean bean = CfgManager.getCfg_AmuletCondition_Container().getValueByKey(id);
       //     if (bean == null) {
       //         logger.error("Cfg_AmuletConditionBean配置表不存在：" + id);
       //         continue;
       //     }
       //     for (ReadArray<Integer> array : bean.getProperty().getValuees()) {
       //         if (array.size() < 2) {
       //             logger.error("Cfg_AmuletConditionBean配置表属性异常：" + id);
       //         }
       //         if (array.get(0) < 1 || array.get(0) > AttributeType.ATTR_MAX) {
       //             continue;
       //         }
       //         att.addAttribute(array.get(0), array.get(1));
       //     }
       // }
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
        return ScriptEnum.AmuletTaskAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
