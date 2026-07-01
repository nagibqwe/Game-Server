package common.attribute;

import com.data.CfgManager;
import com.data.bean.Cfg_Changejob_Bean;
import com.data.bean.Cfg_State_xisui_acupoint_Bean;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;

/**
 * @explain: desc
 * @time Created on 2020/2/4 15:44.
 * @author: tc
 */
public class XiSuiAttributeScript implements IAttributeScript {
    /**
     * @return
     */
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.XiSui;
    }

    /**
     * 获取玩家属性
     *
     * @param player
     * @param sycRank
     * @return
     */
    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        Cfg_Changejob_Bean cfg_changejob_bean = CfgManager.getCfg_Changejob_Container().getValueByKey(player.getXsGrade());
        if (cfg_changejob_bean != null) {
            for (ReadArray<Integer> aii : cfg_changejob_bean.getContribute_describe().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
            }
        }

        Cfg_State_xisui_acupoint_Bean bean = CfgManager.getCfg_State_xisui_acupoint_Container().getValueByKey(player.getXsLevel());
        if (bean != null) {
            for (ReadArray<Integer> aii : bean.getProp_all().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
            }
        }
        return att;
    }

    /**
     * 获取玩家系统属性
     *
     * @param player
     * @return
     */
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

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.XiSuiAttributeScript;
    }

    /**
     * 调用脚本
     *
     * @param args 参数
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }
}
