package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.backpack.structs.Item;
import com.game.horse.structs.Horse;
import com.game.horse.structs.HorseEquip;
import com.game.horse.structs.HorseEquipPart;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.manager.Manager;
import com.game.nature.structs.Drug;
import com.game.nature.structs.Huaxin;
import com.game.attribute.script.IAttributeScript;
import com.game.nature.structs.NatureType;
import com.game.pet.structs.PetAssistant;
import com.game.pet.structs.PetEquipPart;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.message.NatureMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

import static com.game.structs.AttributeType.*;


public class HorseAttributeScript implements IAttributeScript {

    private final static Logger log = LogManager.getLogger(HorseAttributeScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HorseAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.HORSE;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();

        if(!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Mount)) {
            return att;
        }

        //坐骑御魂
        calculateHorseSoulAttribute(player, att);
        //同步御魂战力
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setHorseSoulPower(player, power);
        }
        //计算坐骑属性
        calculateHorseAttribute(player, att);
        //计算坐骑脉轮属性
        if(Manager.controlManager.deal().isOpenFunction(player, FunctionStart.MountEquip)) {
            BaseIntAttribute equipAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
            calculateHorseEquipAttribute(player, equipAtt);
            //合并到整体中
            for(int i = 0;i<AttributeType.ATTR_MAX;i++){
                att.setAttribute(i, att.getAdditionValue(i) + equipAtt.getAdditionValue(i));
            }
            if(sycRank){
                int power = Manager.playerAttAttributeManager.deal().calcFightPower(equipAtt);
                Manager.rankListManager.deal().setHorseEquipPower(player, power);
            }
        }
        int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
        player.getHorse().getNature().setPower(power);
        //同步排行榜
        if (sycRank) {
            Manager.rankListManager.deal().setHorseRankData(player, power);
        }
        //同步最新战力到客户端
        NatureMessage.ResPowerChange.Builder build = NatureMessage.ResPowerChange.newBuilder();
        build.setNatureType(NatureType.HORSE);
        build.setFight(power);
        MessageUtils.send_to_player(player,NatureMessage.ResPowerChange.MsgID.eMsgID_VALUE, build.build().toByteArray());
        return att;
    }

    /**
     * 计算坐骑装备属性
     * @param player
     * @param att
     */
    private void calculateHorseEquipAttribute(Player player, BaseIntAttribute att) {
        //遍历所有助战位
        for (Map.Entry<Integer, HorseEquip> assistantEntry : player.getHorse().getEquips().entrySet()) {
            if(!assistantEntry.getValue().isActive()){
                continue;
            }
            //当前助战位id
            int assistantid = assistantEntry.getKey();
            //遍历单个助战位上的所有装备
            for (Map.Entry<Integer, HorseEquipPart> equipPartEntry : assistantEntry.getValue().getParts().entrySet()) {
                //当前装备的位置
                int equipPart = equipPartEntry.getKey();
                //当前装备
                HorseEquipPart petEquipPart = equipPartEntry.getValue();

                Item equip = petEquipPart.getItem();
                if (equip == null) {
                    continue;
                }
                //============================装备基础属性生效
                Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
                if (model == null) {
                    log.error("错误的装备 modelId:" + equip.getItemModelId());
                    continue;
                }
                //装备基础属性的attribute1属性。用于待会附魂属性对它的放大（即只会放大装备基础属性的attribute1）
                HashMap<Integer, Integer> attribute1TempAttribute = new HashMap<>();
                //装备基础属性中的Attribute1属性生效
                for (ReadArray<Integer> aii : model.getAttribute1().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));

                    //记录到attribute1TempAttribute中
                    Integer old = attribute1TempAttribute.getOrDefault(aii.get(0), 0);
                    old += aii.get(1);
                    attribute1TempAttribute.put(aii.get(0), old);
                }
                //装备基础属性中的Attribute2属性生效
                for (ReadArray<Integer> aii : model.getAttribute2().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }

                //============================装备附魂属性生效
                Cfg_Horse_equip_soulbound_Bean soulboundCfg = CfgManager.getCfg_Horse_equip_soulbound_Container().getValueByKey(assistantid * 100000000 + equipPart * 10000 + petEquipPart.getSoulLv());
                //附魂属性中的绝对属性
                for (ReadArray<Integer> aii : soulboundCfg.getExtraValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }
                //附魂属性中的百分比属性
                for (ReadArray<Integer> aii : soulboundCfg.getPercentValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }

                //============================装备强化属性生效
                Cfg_Horse_equip_inten_Bean intenCfg = CfgManager.getCfg_Horse_equip_inten_Container().getValueByKey(assistantid * 100000000 + equipPart * 10000 + petEquipPart.getStrengthLv());
                for (ReadArray<Integer> aii : intenCfg.getValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }
            }

            //============================每个助战位的强化目标属性生效
            Cfg_Horse_equip_inten_class_Bean intenClassCfg = CfgManager.getCfg_Horse_equip_inten_class_Container().getValueByKey(assistantEntry.getValue().getStrengthActiveId());
            if(intenClassCfg != null){
                for (ReadArray<Integer> aii : intenClassCfg.getValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }
            }

            //============================每个助战位的装备全身附魂属性生效
            Cfg_Horse_equip_soulbound_class_Bean soulboundClassCfg = CfgManager.getCfg_Horse_equip_soulbound_class_Container().getValueByKey(assistantEntry.getValue().getSoulActiveId());
            if(soulboundClassCfg != null){
                for (ReadArray<Integer> aii : soulboundClassCfg.getValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                }
            }
        }
    }

    /**
     * 坐骑御魂
     * @param player
     * @param att
     */
    private void calculateHorseSoulAttribute(Player player, BaseIntAttribute att){
        Horse horse = player.getHorse();
        BaseSystemIntAttribute systemAtt = player.getSysAttriBute();
        for (int i = 1; i < systemAtt.getLength(); i++) {
            double rate = systemAtt.getAttribute(i) / 10000.0f + 1.0f;
            switch (i) {
                case ATTR_Horse_Attack:
                    AttributeUtils.attributeEnlarge(att, ATTR_Atk, rate);
                    break;
                case ATTR_Horse_Hp:
                    AttributeUtils.attributeEnlarge(att, ATTR_MaxHp, rate);
                    break;
                case ATTR_Horse_DefBreak:
                    AttributeUtils.attributeEnlarge(att, ATTR_DefBreak, rate);
                    break;
                case ATTR_Horse_Defence:
                    AttributeUtils.attributeEnlarge(att, ATTR_Def, rate);
                    break;
                default:
                    break;
            }
        }
        for (Drug drug : horse.getNature().getDrugs().values()) {
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
            if (bean != null) {
                for (ReadArray array : bean.getAttribute().getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) * drug.getUseNumber());
                }
            }
        }
    }

    private void calculateHorseAttribute(Player player, BaseIntAttribute att) {
        Horse horse = player.getHorse();
        Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(horse.getNature().getCurrentId());
        if (horseBean == null) {
            return;
        }
        for (ReadArray array : horseBean.getAttribute().getValuees()) {
            if (array.size() >= 3) {
                if (player.getHorse().getRideState() == HorseRideStateEnum.UnRide && AttributeType.ATTR_Speed == (int) array.get(0)) {
                    continue;
                }
                att.addAttribute((int) array.get(0), (int) array.get(1));
            }
        }
        /*
          化形的属性，要对每个化形进行计算
          属性等于表上rent_att*（星级+1）【已激活】
          */
        for (Huaxin huaxin : horse.getNature().getHuaxins().values()) {
            int level = huaxin.getLevel();
            Cfg_HuaxingHorse_Bean huaxingHorseBean = CfgManager.getCfg_HuaxingHorse_Container().getValueByKey(huaxin.getExcelId());
            ReadIntegerArrayEs arrays = huaxingHorseBean.getRent_att();
            if (null != arrays) {
                for (ReadArray array : arrays.getValuees()) {
                    att.addAttribute((int) array.get(0), (int) array.get(1) + (int) array.get(2) * level);
                }
            }
        }
    }


    @Override
    public BaseSystemIntAttribute getPlayerSystemAttribute(Player player) {
        BaseSystemIntAttribute att = player.PlayerCalSystemCulators().get(getType());
        if (att == null) {
            att = new BaseSystemIntAttribute(AttributeType.SystemAttr_Max);
            player.PlayerCalSystemCulators().put(getType(), att);
        }
        att.clean();

        for (Drug drug : player.getHorse().getNature().getDrugs().values()){
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