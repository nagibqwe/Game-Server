package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Marry_childAtt_Bean;
import com.data.bean.Cfg_Marry_child_Bean;
import com.data.bean.Cfg_Marry_lock_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.marriage.struct.MarryChild;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author lw
 */
public class MarriageAttributeScript implements IAttributeScript {

    private final static Logger log = LogManager.getLogger(MarriageAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.MARRIAGE;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        if (player.getMarryLock().isOpen()) {
            //TODO 心锁基本属性
            int marryLockLevel = Manager.marriageManager.manager().calcMarryLockLevelId(player);
            Cfg_Marry_lock_Bean config = CfgManager.getCfg_Marry_lock_Container().getValueByKey(marryLockLevel);
            for (ReadArray<Integer> attribute : config.getAttribute().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
            //TODO 心锁仙侣属性
            if (player.getMarriageUid() != 0) {
                for (ReadArray<Integer> attribute : config.getMarryAttribute().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
            }
        }

        // 仙娃属性
        BaseIntAttribute childAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
        for (MarryChild child : player.getChilds().values()) {
            int levelId = Manager.marriageManager.manager().calcChildLevelId(child);
            Cfg_Marry_childAtt_Bean childAtt_bean = CfgManager.getCfg_Marry_childAtt_Container().getValueByKey(levelId);
            if (childAtt_bean == null) {
                log.error("Cfg_Marry_childAtt_Bean无法找到指定数据，婚姻系统计算属性有误，id = " + levelId);
                continue;
            }
            for (ReadArray<Integer> attribute : childAtt_bean.getAttributes().getValuees()) {
                att.addAttribute(attribute.get(0), attribute.get(1));
                childAtt.addAttribute(attribute.get(0), attribute.get(1));
            }
        }

        if(sycRank){
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(childAtt);
            Manager.rankListManager.deal().setMarryChildPower(player, power);
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
        return ScriptEnum.MarriageAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
