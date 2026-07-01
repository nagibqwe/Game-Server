package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.Cfg_Characters_Bean;
import com.data.bean.Cfg_Equip_Collection_start_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * @author lw
 */
public class BaseAttributeScript implements IAttributeScript {

    private final static Logger logger = LogManager.getLogger(BaseAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.BASE;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        //基本加成
        Cfg_Characters_Bean cfg = CfgManager.getCfg_Characters_Container().getValueByKey(player.getLevel());
        if (cfg == null) {
            logger.error("BaseAttributeScript error nocfg level:" + player.getLevel());
            return att;
        }
        for (ReadArray<Integer> aii : cfg.getAttributeValue().getValuees()) {
            att.addAttribute(aii.get(0), aii.get(1));
        }

        if (Manager.controlManager.deal().isOpenFunction(player, FunctionStart.LingtiFanTai)) {
            //蕴养属性
            for (Cfg_Equip_Collection_start_Bean bean : CfgManager.getCfg_Equip_Collection_start_Container().getValuees()) {
                if (bean.getId() > player.getSpiritData().getCfgId()) {
                    continue;
                }
                for (int i = 0; i < bean.getAttribute().size(); i++) {
                    att.addAttribute(bean.getAttribute().get(i).get(0), bean.getAttribute().get(i).get(1));
                }
            }
        }

//        //转职加成，只针对基础属性加成
//        Cfg_Changejob_Bean bean = CfgManager.getCfg_Changejob_Container().getValueByKey((player.getCareer() + 1) * 1000 + player.getGrade());
//        if (bean == null) {
//            return att;
//        }
//        for (ReadArray<Integer> aii : bean.getAttributeValue().getValuees()) {
//            att.addAttribute(aii.get(0), aii.get(1));
//        }
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
        return ScriptEnum.BaseAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
