package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Title_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gsj
 */
public class TitleAttributeScript implements IAttributeScript {

    private static final Logger logger = LogManager.getLogger(TitleAttributeScript.class);


    @Override
    public int getId() {
        return ScriptEnum.TitleAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.TITLE;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        Cfg_Title_Bean bean;
        ConcurrentHashMap<Integer, Integer> titleList = player.getTitleData().getTitleList();
        for (Map.Entry<Integer, Integer> entry : titleList.entrySet()) {
            if (entry.getValue() != 0 && TimeUtils.Time() / 1000 > entry.getValue()) {
                continue;
            }
            bean = CfgManager.getCfg_Title_Container().getValueByKey(entry.getKey());
            if (bean == null) {
                logger.error("Cfg_TitleBean配置表不存在：" + entry.getKey());
                continue;
            }
            for (ReadArray<Integer> array : bean.getProperty().getValuees()) {
                att.addAttribute(array.get(0), array.get(1));
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

}
