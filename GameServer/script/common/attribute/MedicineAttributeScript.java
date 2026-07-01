package common.attribute;

import com.data.Global;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.count.structs.VariantType;
import com.game.equip.struct.EquipDefine;
import com.game.hook.manager.PlayerHookManager;
import com.game.manager.Manager;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;

/**
 * @author gsj
 * 经验药属性处理
 */
public class MedicineAttributeScript implements IAttributeScript {

    @Override
    public int getId() {
        return ScriptEnum.MedicineAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.MEDICINESATTRIBUTE;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        //药品永久属性
        AttributeUtils.addAttribute(att, player.getMedicinesAttribute());

        //经验、组队、vip、世界等级加成.(符咒经验加成通过技能系统加成)
        int itemRate = Manager.playerHookManager.deal().getCurrentItemRate(player);
        att.addAttribute(AttributeType.ATTR_MonserExp, itemRate * 100);

        int teamRate = Manager.backpackManager.manager().calTeamRate(player);
        att.addAttribute(AttributeType.ATTR_MonserExp, teamRate * 100);

        int addExp = Manager.backpackManager.manager().calMarriageRate(player);
        att.addAttribute(AttributeType.ATTR_MonserExp, addExp * 100);

        int worldLvRate = Manager.backpackManager.manager().calWorldLvRate(player);
        //完成首冲才发
        if (player.getFirstRechargeTime() > 0) {
            att.addAttribute(AttributeType.ATTR_MonserExp, worldLvRate * 100);
        }
        int szRate = Manager.equipManager.deal().calEquipRate(player, EquipDefine.EquipPart_Bracelet);
        int ehRate = Manager.equipManager.deal().calEquipRate(player, EquipDefine.EquipPart_Earring);
        att.addAttribute(AttributeType.ATTR_MonserExp, szRate);
        att.addAttribute(AttributeType.ATTR_MonserExp, ehRate);

        long total = Manager.countManager.getVariant(player, VariantType.RechargeMoney);
        if (total >= Global.First_Recharge_Exp_Add) {
            att.addAttribute(AttributeType.ATTR_MonserExp, Global.First_Rechar_Exp_Add);
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

        //药品永久属性
        AttributeUtils.addAttribute(att, player.getMedicinesAttributeSys());

        return att;
    }
}
