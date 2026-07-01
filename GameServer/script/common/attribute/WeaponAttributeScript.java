package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.Cfg_HuaxingWeapon_Bean;
import com.data.bean.Cfg_NatureWeapon_Bean;
import com.data.bean.Cfg_Nature_att_Bean;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.manager.Manager;
import com.game.nature.structs.*;
import com.game.attribute.script.IAttributeScript;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.message.NatureMessage;

public class WeaponAttributeScript implements IAttributeScript {

    @Override
    public int getId() {
        return ScriptEnum.WeaponAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.Weapon;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.NatureWeapon)) {
            return att;
        }

        Weapon weapon = player.getWeapon();
        //基础属性
        Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(weapon.getCurrentId());
        if (weaponBean != null) {
            for (ReadArray array : weaponBean.getAttribute().getValuees()) {
                if (array.size() >= 3) {
                    att.addAttribute((int) array.get(0), (int) array.get(1));
                }
            }
        }
        //计算化形
        for (Huaxin huaxin: weapon.getHuaxins().values()){
            int level = huaxin.getLevel();
            Cfg_HuaxingWeapon_Bean huaxingWeapon_bean = CfgManager.getCfg_HuaxingWeapon_Container().getValueByKey(huaxin.getExcelId());
            ReadIntegerArrayEs arrays = huaxingWeapon_bean.getRent_att();
            if (null != arrays) {
                for (ReadArray array : arrays.getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) + (int) array.get(2) * level);
                }
            }
        }
        //计算御魂
        for (Drug drug : player.getWeapon().getDrugs().values()) {
            int belongType = drug.getBelongType();
            int pos = drug.getPos();
            int excelId;
            Cfg_Nature_att_Bean bean;
            for (int i = 0; i < drug.getLevel(); i++) {
                excelId = belongType * 1000 + pos * 100 + i;
                bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
                int levelLimit = bean.getLeve_limit();

                for (ReadArray array : bean.getAttribute().getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) * levelLimit);
                }
            }

            bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(drug.getExcelId());
            if (null != bean) {
                for (ReadArray array : bean.getAttribute().getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) * drug.getUseNumber());
                }
            }
        }

        for(WeaponAttribute weaponAttribute : weapon.getAttributes()) {
            att.addAttribute(weaponAttribute.getAttributeId(), weaponAttribute.getValue());
        }

        int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
        weapon.setPower(power);

        if (sycRank) {
            Manager.rankListManager.deal().setWeaponPower(player, power);
        }

        //同步最新战力到客户端
        NatureMessage.ResPowerChange.Builder build = NatureMessage.ResPowerChange.newBuilder();
        build.setNatureType(NatureType.WEAPON);
        build.setFight(power);
        MessageUtils.send_to_player(player,NatureMessage.ResPowerChange.MsgID.eMsgID_VALUE, build.build().toByteArray());
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

        for (Drug drug : player.getWeapon().getDrugs().values()){
            int excelId = drug.getExcelId();
            Cfg_Nature_att_Bean bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
            if (null != bean) {
                ReadIntegerArray array = bean.getPeiyang_att();
                if (null != array) {
                    att.addSystemAttribute(array.get(0), array.get(1));
                }
            }
        }

        return att;
    }
}
