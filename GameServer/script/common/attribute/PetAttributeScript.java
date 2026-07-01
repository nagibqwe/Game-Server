package common.attribute;

import com.data.CfgManager;
import com.data.FunctionStart;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.attribute.AttributeUtils;
import com.game.attribute.BaseIntAttribute;
import com.game.attribute.BaseSystemIntAttribute;
import com.game.backpack.structs.Item;
import com.game.manager.Manager;
import com.game.nature.structs.Drug;
import com.game.nature.structs.NatureType;
import com.game.pet.structs.ActivePet;
import com.game.pet.structs.Pet;
import com.game.attribute.script.IAttributeScript;
import com.game.pet.structs.PetAssistant;
import com.game.pet.structs.PetEquipPart;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import game.message.NatureMessage;
import game.message.PetMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.game.structs.AttributeType.*;
import static com.game.structs.AttributeType.ATTR_Def;

/**
 * 宠物属性计算
 */
public class PetAttributeScript implements IAttributeScript {

    private final static Logger log = LogManager.getLogger(PetAttributeScript.class);

    @Override
    public PlayerAttributeType getType() {
        return PlayerAttributeType.PET;
    }

    @Override
    public BaseIntAttribute getPlayerAttribute(Player player, boolean sycRank) {
        BaseIntAttribute att = player.PlayerCalculators().get(getType());
        if (att == null) {
            att = new BaseIntAttribute(AttributeType.ATTR_MAX);
            player.PlayerCalculators().put(getType(), att);
        }
        att.clean();
        //判断系统开放
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Pet)) {
            return att;
        }

        //御魂属性
        calculatePetSoulAttribute(player, att);
        //同步御魂战力
        if (sycRank) {
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
            Manager.rankListManager.deal().setPetSoulPower(player, power);
        }
        //等级属性
        ActivePet pets = player.getActivePet();
        Cfg_Pet_level_Bean levelBean = CfgManager.getCfg_Pet_level_Container().getValueByKey(pets.getLevel());
        if (levelBean != null) {
            for (ReadArray<Integer> value : levelBean.getAttribute().getValuees()) {
                att.addAttribute(value.get(0), value.get(1));
            }
        }


        //宠物装备属性
        calculatePetEquipAttribute(player, att);
        //for (Map.Entry<Integer, Integer> value : pets.getSoul().entrySet()) {
        //    int type = value.getKey();
        //    int num = value.getValue();
        //    Cfg_Pet_soul_Bean soulBean = CfgManager.getCfg_Pet_soul_Container().getValueByKey(type);
        //    if (soulBean == null) {
        //        log.error("御魂属性加到玩家身上时出错，无法找到Cfg_Pet_soulBean数据，id = " + type);
        //        continue;
        //    }
        //    for (ReadArray<Integer> attr : soulBean.getAttribute().getValuees()) {
        //        att.addAttribute(attr.get(0), attr.get(1) * num);
        //    }
        //}
        //宠物属性
        for (Pet pet : pets.getPets().values()) {
            Cfg_Pet_Bean petBean = CfgManager.getCfg_Pet_Container().getValueByKey(pet.getModelId());
            if (petBean == null) {
                log.error("Cfg_PetBean无法找到指定数据，宠物属性加到玩家身上失败，id = " + pet.getModelId());
                continue;
            }
            for (ReadArray<Integer> value : petBean.getAttribute().getValuees()) {
                att.addAttribute(value.get(0), value.get(1));
            }
            //阶级属性
            Cfg_Pet_rank_Bean rankBean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(pet.getModelId() * 1000 + pet.getStage());
            if (rankBean == null) {
                log.error("Cfg_Pet_rankBean无法找到指定数据，宠物属性计算失败，id = " + (pet.getModelId() * 1000 + pet.getStage()));
                continue;
            }
            for (ReadArray<Integer> value : rankBean.getAttribute().getValuees()) {
                att.addAttribute(value.get(0), value.get(1));
            }
            //同步排行榜
            if (sycRank) {
                int power = Manager.playerAttAttributeManager.deal().calcFightPower(att);
                player.getActivePet().getNature().setPower(power);
                Manager.rankListManager.deal().setPetRankData(player, power);
            }
            //技能属性
            List<Integer> allSkillIds = new ArrayList<>();
            allSkillIds.addAll(pet.getBaseSkills().keySet());
            allSkillIds.addAll(pet.getPassivitySkill());
            for (int skillId : allSkillIds) {
                Cfg_Skill_Bean bean = CfgManager.getCfg_Skill_Container().getValueByKey(skillId);
                if (bean == null) {
                    continue;
                }
                if (bean.getParams().size() < 1) {
                    continue;
                }
                for (ReadArray<Integer> property : bean.getParams().getValuees()) {
                    if (property.size() < 3) {
                        continue;
                    }
                    int func = property.get(0);
                    int type = property.get(1);
                    int value = property.get(2);
                    if (func == 1) {
                        if (type < 1 || type > AttributeType.ATTR_MAX) {
                            continue;
                        }
                        att.addAttribute(type, value);
                    }
                }
            }
        }

        //同步最新战力到客户端
        NatureMessage.ResPowerChange.Builder build = NatureMessage.ResPowerChange.newBuilder();
        build.setNatureType(NatureType.PET);
        build.setFight(player.getActivePet().getNature().getPower());
        MessageUtils.send_to_player(player,NatureMessage.ResPowerChange.MsgID.eMsgID_VALUE, build.build().toByteArray());
        return att;
    }

    /**
     * 计算宠物御魂属性
     * */
    private void calculatePetSoulAttribute(Player player, BaseIntAttribute att) {
        BaseSystemIntAttribute systemAtt = player.getSysAttriBute();
        for (int i = 1; i < systemAtt.getLength(); i++) {
            double rate = systemAtt.getAttribute(i) / 10000.0f + 1.0f;
            switch (i) {
                case ATTR_Pet_Attack:
                    AttributeUtils.attributeEnlarge(att, ATTR_Atk, rate);
                    break;
                case ATTR_Pet_Hp:
                    AttributeUtils.attributeEnlarge(att, ATTR_MaxHp, rate);
                    break;
                case ATTR_Pet_DefBreak:
                    AttributeUtils.attributeEnlarge(att, ATTR_DefBreak, rate);
                    break;
                case ATTR_Pet_Defence:
                    AttributeUtils.attributeEnlarge(att, ATTR_Def, rate);
                    break;
                default:
                    break;
            }
        }

        for (Drug drug : player.getActivePet().getNature().getDrugs().values()) {
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
    }

    /**计算宠物装备属性*/
    private void calculatePetEquipAttribute(Player player, BaseIntAttribute att) {
        //遍历所有助战位
        for (Map.Entry<Integer, PetAssistant> assistantEntry : player.getActivePet().getAssistants().entrySet()) {
            //用于计算评分的属性
            BaseIntAttribute attScore = new BaseIntAttribute(AttributeType.ATTR_MAX);
            //当前助战位id
            int assistantid = assistantEntry.getKey();
            PetAssistant pet = assistantEntry.getValue();
            //最新评分
            int score = 0;
            //遍历单个助战位上的所有装备
            for (Map.Entry<Integer, PetEquipPart> equipPartEntry : pet.getParts().entrySet()) {
                //当前装备的位置
                int equipPart = equipPartEntry.getKey();
                //当前装备
                PetEquipPart petEquipPart = equipPartEntry.getValue();

                Item equip = petEquipPart.getPetEquip();
                if (equip == null) {
                    continue;
                }
                //============================装备基础属性生效
                Cfg_Equip_Bean model = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
                if (model == null) {
                    log.error("错误的装备 modelId:" + equip.getItemModelId());
                    continue;
                }
                score += model.getScore();
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
                Cfg_Pet_equip_soulbound_Bean soulboundCfg = CfgManager.getCfg_Pet_equip_soulbound_Container().getValueByKey(assistantid * 100000000 + equipPart * 10000 + petEquipPart.getSoulLv());
                //附魂属性中的绝对属性
                for (ReadArray<Integer> aii : soulboundCfg.getExtraValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                    attScore.addAttribute(aii.get(0), aii.get(1));
                }
                //附魂属性中的百分比属性，只放大装备的基础属性
                double rate = soulboundCfg.getPercentValue() / 10000.0d;
                for (Map.Entry<Integer, Integer> entry : attribute1TempAttribute.entrySet()) {
                    att.addAttribute(entry.getKey(), (int) (entry.getValue() * rate));
                    attScore.addAttribute(entry.getKey(), (int) (entry.getValue() * rate));
                }

                //============================装备强化属性生效
                Cfg_Pet_equip_inten_Bean intenCfg = CfgManager.getCfg_Pet_equip_inten_Container().getValueByKey(assistantid * 100000000 + equipPart * 10000 + petEquipPart.getStrengthLv());
                for (ReadArray<Integer> aii : intenCfg.getValue().getValuees()) {
                    att.addAttribute(aii.get(0), aii.get(1));
                    attScore.addAttribute(aii.get(0), aii.get(1));
                }
            }

            //============================每个助战位的强化目标属性生效
            Cfg_Pet_equip_inten_class_Bean intenClassCfg = CfgManager.getCfg_Pet_equip_inten_class_Container().getValueByKey(assistantEntry.getValue().getStrengthActiveId());
            for (ReadArray<Integer> aii : intenClassCfg.getValue().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
                attScore.addAttribute(aii.get(0), aii.get(1));
            }

            //============================每个助战位的装备全身附魂属性生效
            Cfg_Pet_equip_soulbound_class_Bean soulboundClassCfg = CfgManager.getCfg_Pet_equip_soulbound_class_Container().getValueByKey(assistantEntry.getValue().getSoulActiveId());
            for (ReadArray<Integer> aii : soulboundClassCfg.getValue().getValuees()) {
                att.addAttribute(aii.get(0), aii.get(1));
                attScore.addAttribute(aii.get(0), aii.get(1));
            }

            int old = pet.getScore();
            int power = Manager.playerAttAttributeManager.deal().calcFightPower(attScore);
            score += power;
            if(old != score){
                pet.setScore(score);
                PetMessage.ResPetAssistantScoreUpdate.Builder res = PetMessage.ResPetAssistantScoreUpdate.newBuilder();
                res.setScore(score);
                res.setAssistantId(assistantid);
                MessageUtils.send_to_player(player, PetMessage.ResPetAssistantScoreUpdate.MsgID.eMsgID_VALUE, res.build().toByteArray());
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

        for (Drug drug : player.getActivePet().getNature().getDrugs().values()){
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

    @Override
    public int getId() {
        return ScriptEnum.PetAttributeScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }
}
