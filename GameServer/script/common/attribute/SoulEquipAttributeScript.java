package common.attribute;

import com.data.CfgManager;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.attribute.script.IAttributeScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.soulArmor.struct.SoulArmorSlot;
import com.game.structs.AttributeType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;

/**
 * @Desc TODO
 * @Date 2020/12/29 18:15
 * @Auth ZUncle
 */
public class SoulEquipAttributeScript implements IAttributeScript {

    final Logger log = LogManager.getLogger(SoulEquipAttributeScript.class);

    /**
     * @return
     */
    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.SoulEquip;
    }

    /**
     * 获取属性
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

        //淬炼属性
        if (player.getSoulArmor().getLevel() > 0) {
            Cfg_SoulArmor_level_up_Bean config = CfgManager.getCfg_SoulArmor_level_up_Container().getValueByKey(player.getSoulArmor().getLevel());
            for (ReadArray<Integer> attribute : config.getLevelValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
            for (ReadArray<Integer> attribute : config.getExtraValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
        }
        //突破属性
        if (player.getSoulArmor().gainQualityLevel() > 0) {
            Cfg_SoulArmor_breach_Bean config = CfgManager.getCfg_SoulArmor_breach_Container().getValueByKey(player.getSoulArmor().getQualityLevel());
            for (ReadArray<Integer> attribute : config.getQualityValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
            for (ReadArray<Integer> attribute : config.getExtraValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
        }
        //觉醒属性
        if (player.getSoulArmor().getSkillLevel() > 0) {
            Cfg_SoulArmor_awaken_Bean config = CfgManager.getCfg_SoulArmor_awaken_Container().getValueByKey(player.getSoulArmor().getSkillLevel());
            for (ReadArray<Integer> attribute : config.getLevelValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
            for (ReadArray<Integer> attribute : config.getExtraValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
        }
        HashMap<Integer, Cfg_Equip_Bean> quality = new HashMap<>();
        //魂印属性
        for (SoulArmorSlot slot : player.getSoulArmor().getSlots().values()) {
            //强化属性
            int index = slot.getSlotId() * 10000 + slot.getLevel();
            Cfg_SoulArmor_signet_intensify_Bean config = CfgManager.getCfg_SoulArmor_signet_intensify_Container().getValueByKey(index);
            for (ReadArray<Integer> attribute : config.getValue().getValuees()) {
                if (attribute.size() >= 2) {
                    att.addAttribute(attribute.get(0), attribute.get(1));
                }
            }
            //魂印属性
            if (slot.getBall() != null) {
                Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(slot.getBall().getItemModelId());
                for (ReadArray<Integer> attribute : bean.getAttribute1().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
                quality.put(slot.getSlotId(), bean);
            }
        }
        //套装属性
        for (Cfg_SoulArmor_signet_suit_Bean bean : CfgManager.getCfg_SoulArmor_signet_suit_Container().getValuees()) {
            ReadArray<Integer> parts = bean.getPart().get(0);
            int count = suitQualityReachCount(parts, quality, bean);
            //二件套
            if (count >= 2) {
                for (ReadArray<Integer> attribute : bean.getValueOf2().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
            }
            //三件套
            if (count >= 3) {
                for (ReadArray<Integer> attribute : bean.getValueOf3().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
            }
            //四件套
            if (count >= 4) {
                for (ReadArray<Integer> attribute : bean.getValueOf4().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
            }
            //六件套
            if (count >= 6) {
                for (ReadArray<Integer> attribute : bean.getValueOf6().getValuees()) {
                    if (attribute.size() >= 2) {
                        att.addAttribute(attribute.get(0), attribute.get(1));
                    }
                }
            }
        }
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setSoulFightRank(player, power);
        }
        return att;
    }

    //几件套套装
    int suitQualityReachCount(ReadArray<Integer> parts, HashMap<Integer, Cfg_Equip_Bean> quality, Cfg_SoulArmor_signet_suit_Bean target) {
        int count = 0;
        for (int slotId : parts.getValue()) {
            Cfg_Equip_Bean bean = quality.get(slotId);
            if (bean == null){
                continue;
            }
            if (bean.getQuality() >= target.getQuality() || bean.getDiamond_Number() >= target.getStar()) {
                count = count + 1;
            }
        }
        return count;
    }

    /**
     * 获取系统属性
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
        return ScriptEnum.SoulEquipAttributeScript;
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
