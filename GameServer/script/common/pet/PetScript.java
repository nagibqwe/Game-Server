package common.pet;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.attribute.BaseLongAttribute;
import com.game.backpack.manager.BackpackManager;
import com.game.backpack.structs.Item;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.map.structs.MapObject;
import com.game.newfashion.manager.NewFashionManager;
import com.game.pet.log.PetLevelChangeLog;
import com.game.pet.log.PetLog;
import com.game.pet.script.IPetScript;
import com.game.pet.structs.*;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.skill.structs.Skill;
import com.game.structs.AttributeType;
import com.game.structs.ItemChangeAction;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.script.IScript;
import game.core.util.IDConfigUtil;
import game.message.PetMessage;
import game.message.backpackMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class PetScript implements IPetScript, IScript {
    private static final Logger logger = LogManager.getLogger(PetScript.class);

    @Override
    public int getId() {
        return ScriptEnum.PetBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initPet(Player player, int funcId) {
        for (Cfg_Pet_Bean bean : CfgManager.getCfg_Pet_Container().getValuees()) {
            if (bean.getUnlock() == null || bean.getUnlock().isEmpty()) {
                continue;
            }
            if (bean.getUnlock().get(0).get(0) != PetDefine.ACTIVE_TYPE_FUNCTION) {
                continue;
            }
            if (bean.getUnlock().get(0).get(1) != funcId) {
                continue;
            }
            int petId = bean.getId();
            ActivePet activePet = player.getActivePet();
            Pet pet = activePet.getPets().get(petId);
            if (pet != null) {
                logger.error("玩家" + player.getName() + " 该只宠物是已经激活的，不该发生的错误！petId=" + petId);
                continue;
            }

            //判断是否可激活
            Cfg_Pet_Bean petBean = CfgManager.getCfg_Pet_Container().getValueByKey(petId);
            if (petBean == null) {
                logger.error("获取宠物数据失败，请检查pet表，petId=" + petId);
                continue;
            }
            int initStage = PetDefine.INIT_DEGREE;
            Cfg_Pet_rank_Bean rankBean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(petId * 1000 + initStage);
            if (rankBean == null) {
                logger.error("Cfg_Pet_rankBean无法找到指定数据，激活宠物失败，id = " + (petId * 1000 + initStage));
                continue;
            }
            //生成宠物数据
            pet = new Pet();
            pet.setOwnerId(player.getId());
            pet.setOwnerName(player.getName());
            pet.setModelId(petId);
            pet.setName(petBean.getName());
            pet.setStage(initStage);
            pet.setManualSkill(petBean.getPet_skill());
            Skill skill = new Skill();
            skill.setSkillId(petBean.getBaseskill());
            pet.getBaseSkills().put(petBean.getBaseskill(), skill);
            for (ReadArray<Integer> passivitySkillId : rankBean.getPet_skill().getValuees()) {
                pet.getPassivitySkill().add(passivitySkillId.get(1));
            }
            activePet.getPets().put(petId, pet);
            activePet.setFightPet(petId);
            //计算宠物属性
            calPetAttribute(pet);
            //记log
            writePetLog(player, pet, PetDefine.PET_OPERATION_ACTIVE);

            Manager.newFashionManager.deal().huaxingActivateFashion(player, NewFashionManager.PET_TYPE, petId);
        }
        initPetAssistantData(player);
        //计算玩家属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //通知客户端
        online(player, true);
    }

    @Override
    public void finishTaskGetPet(Player player, int taskId) {
        for (Cfg_Pet_Bean bean : CfgManager.getCfg_Pet_Container().getValuees()) {
            if (bean.getUnlock() == null || bean.getUnlock().isEmpty()) {
                continue;
            }
            if (bean.getUnlock().get(0).get(0) != PetDefine.ACTIVE_TYPE_TASK) {
                continue;
            }
            if (bean.getUnlock().get(0).get(1) != taskId) {
                continue;
            }
            activePet(player, bean.getId(), false);
        }
    }

    @Override
    public void offLine(Player player) {
        ActivePet activePet = player.getActivePet();
        int petId = activePet.getFightPet();
        Pet pet = activePet.getPets().get(petId);
        if (pet == null) {
            return;
        }
        //退出地图
        MapObject map = Manager.mapManager.getMap(pet.gainMapId());
        Manager.mapManager.manager().onQuitMap(map, pet, true);
    }

    @Override
    public void online(Player player, boolean funcOpen) {
        ActivePet activePet = player.getActivePet();
        if (activePet.getPets().isEmpty()) {
            //尚无激活的宠物
            return;
        }
        //fixme 修复已出现出战宠物丢失的问题
        if (activePet.getFightPet() <= 0) {
            Pet pet = Utils.findOne(activePet.getPets().values(), p -> true);
            activePet.setFightPet(pet.getModelId());
        }

        sendPetAllInfo(player, funcOpen);
        //推送宠物背包
        backpackMessage.ResPetEquipBagInfos.Builder bagInfo = backpackMessage.ResPetEquipBagInfos.newBuilder();
        player.getPetEquipPackItems().values().forEach(item -> bagInfo.addItemInfoList(Manager.backpackManager.manager().buildItemInfo(item).build()));
        MessageUtils.send_to_player(player, backpackMessage.ResPetEquipBagInfos.MsgID.eMsgID_VALUE, bagInfo.build().toByteArray());
    }

    /**
     * 同步所有宠物数据
     *
     * @param player
     * @param funcOpen
     */
    void sendPetAllInfo(Player player, boolean funcOpen) {
        ActivePet activePet = player.getActivePet();
        PetMessage.ResPetList.Builder resMsg = PetMessage.ResPetList.newBuilder();
        for (Pet pet : activePet.getPets().values()) {
            PetMessage.PetInfo.Builder info = PetMessage.PetInfo.newBuilder();
            info.setModelId(pet.getModelId());
            info.setCurStage(pet.getStage());
            resMsg.addPetList(info);
        }
        resMsg.setCurLevel(activePet.getLevel());
        resMsg.setCurExp(activePet.getExp());
        resMsg.setBattlePetId(activePet.getFightPet());
        resMsg.setFuncOpen(funcOpen);
        resMsg.setAutoSet(activePet.isAutoDecompose());
        //初始化宠物装备，兼容老版本账号
        if(player.getActivePet().getAssistants().size()<=0){
            initPetAssistantData(player);
        }
        player.getActivePet().getAssistants().entrySet().forEach(entry -> resMsg.addAssistantList(entry.getValue().bytesWriteToPb(entry.getKey())));
        MessageUtils.send_to_player(player, PetMessage.ResPetList.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
    }

    @Override
    public Pet getBattlePet(Player player) {
        ActivePet activePet = player.getActivePet();
        int petId = activePet.getFightPet();
        Pet pet = activePet.getPets().get(petId);
        if (pet == null) {
            return null;
        }
        return pet;
    }

    /**
     * 宠物出战
     */
    @Override
    public boolean callPet(Player player, int petId) {

        ActivePet activePet = player.getActivePet();
        if (activePet.getFightPet() == petId) {
            logger.error("宠物出战出错 宠物已出战 pet={} player={}", petId, player);
            return false;
        }
        Pet pet = activePet.getPets().get(petId);
        if (pet == null) {
            logger.error("宠物出战出错！玩家 " + player.getName() + "没有激活这个宠物_" + petId);
            return false;
        }
        //回收旧宠物
        recyclePet(player);
        //玩家宠物手动技能更换
        Manager.skillManager.addSkill(player, pet.getManualSkill());
        PetMessage.ResAddPetSkill.Builder skillMsg = PetMessage.ResAddPetSkill.newBuilder();
        skillMsg.setSkillId(pet.getManualSkill());
        MessageUtils.send_to_player(player, PetMessage.ResAddPetSkill.MsgID.eMsgID_VALUE, skillMsg.build().toByteArray());
        //计算宠物属性
        calPetAttribute(pet);
        //宠物进图
        MapGpsUtil.CopyGPS(player.getCurGps(), pet.getCurGps());
        activePet.setFightPet(petId);
        Manager.mapManager.manager().onEnterMap(pet);
        //向客户端发送出战宠物消息
        PetMessage.ResBattlePet.Builder resMsg = PetMessage.ResBattlePet.newBuilder();
        resMsg.setBattlePetId(petId);
        MessageUtils.send_to_player(player, PetMessage.ResBattlePet.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());

        Manager.newFashionManager.deal().huaxingWearFashion(player, NewFashionManager.PET_TYPE, petId);
        //出战的宠物要换到助阵一位上
        changePetAssiant(player, 1, petId);
        return true;
    }

    @Override
    public boolean recyclePet(Player player) {
        ActivePet activePet = player.getActivePet();
        int petId = activePet.getFightPet();
        Pet pet = activePet.getPets().get(petId);
        if (pet == null) {
            logger.error("宠物休息出错！玩家 " + player.getName() + "没有激活这个宠物_" + petId);
            return false;
        }
        //退出地图
        MapObject map = Manager.mapManager.getMap(pet.gainMapId());
        Manager.mapManager.manager().onQuitMap(map, pet, true);
        pet.reset();
        //删除手动技能
        Manager.skillManager.removeSkill(player, pet.getManualSkill());
        //重置出战id
        activePet.setFightPet(0);
        //向客户端发送出战宠物消息
        PetMessage.ResBattlePet.Builder resMsg = PetMessage.ResBattlePet.newBuilder();
        resMsg.setBattlePetId(0);
        MessageUtils.send_to_player(player, PetMessage.ResBattlePet.MsgID.eMsgID_VALUE, resMsg.build().toByteArray());
        return true;
    }

    /**
     * 激活宠物
     *
     * @param player 玩家
     * @param petId  宠物id
     * @return result
     */
    private boolean activePet(Player player, int petId, boolean isGm) {
        ActivePet activePet = player.getActivePet();
        Pet pet = activePet.getPets().get(petId);
        if (pet != null) {
            logger.error("玩家" + player.getName() + " 该只宠物是已经激活的，不该发生的错误！petId=" + petId);
            return false;
        }

        //判断是否可激活
        Cfg_Pet_Bean petBean = CfgManager.getCfg_Pet_Container().getValueByKey(petId);
        if (petBean == null) {
            logger.error("获取宠物数据失败，请检查pet表，petId=" + petId);
            return false;
        }
        int initStage = PetDefine.INIT_DEGREE;
        Cfg_Pet_rank_Bean rankBean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(petId * 1000 + initStage);
        if (rankBean == null) {
            logger.error("Cfg_Pet_rankBean无法找到指定数据，激活宠物失败，id = " + (petId * 1000 + initStage));
            return false;
        }
        if (!isGm) {
            for (ReadArray<Integer> value : petBean.getUnlock().getValuees()) {
                if (!checkActive(player, value.get(0), value.get(1))) {
                    return false;
                }
            }
        }

        //生成宠物数据
        pet = new Pet();
        pet.setOwnerId(player.getId());
        pet.setOwnerName(player.getName());
        pet.setModelId(petId);
        pet.setName(petBean.getName());
        pet.setStage(initStage);
        pet.setManualSkill(petBean.getPet_skill());
        Skill skill = new Skill();
        skill.setSkillId(petBean.getBaseskill());
        pet.getBaseSkills().put(petBean.getBaseskill(), skill);
        for (ReadArray<Integer> passivitySkillId : rankBean.getPet_skill().getValuees()) {
            pet.getPassivitySkill().add(passivitySkillId.get(1));
            Manager.skillManager.addSkill(player, passivitySkillId.get(1));
        }
        activePet.getPets().put(petId, pet);
        //计算宠物属性
        calPetAttribute(pet);
        //记log
        writePetLog(player, pet, PetDefine.PET_OPERATION_ACTIVE);
        //计算玩家属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);

        Manager.controlManager.operate(player, FunctionVariable.PetID, 0);
        //通知客户端
        PetMessage.ResSyncPet.Builder msg = PetMessage.ResSyncPet.newBuilder();
        PetMessage.PetInfo.Builder info = PetMessage.PetInfo.newBuilder();
        info.setModelId(petId);
        info.setCurStage(initStage);
        msg.setPet(info);
        msg.setFight(player.getActivePet().getNature().getPower());
        MessageUtils.send_to_player(player, PetMessage.ResSyncPet.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.newFashionManager.deal().huaxingActivateFashion(player, NewFashionManager.PET_TYPE, petId);

        RoleGrowLog.create(player, GrowType.pet_active, 0, petId, 0,petId, null);
        return true;
    }

    public void calPetAttribute(Pet pet) {
        BaseLongAttribute attribute = pet.getAttribute();
        attribute.clean();
        //初始属性
        Cfg_Pet_Bean petBean = CfgManager.getCfg_Pet_Container().getValueByKey(pet.getModelId());
        if (petBean == null) {
            logger.error("Cfg_PetBean无法找到指定数据，宠物属性计算失败，id = " + pet.getModelId());
            return;
        }
        for (ReadArray value : petBean.getAttribute().getValuees()) {
            attribute.addAttribute((int) value.get(0), (int) value.get(1));
        }
        //阶级属性
        Cfg_Pet_rank_Bean rankBean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(pet.getModelId() * 1000 + pet.getStage());
        if (rankBean == null) {
            logger.error("Cfg_Pet_rankBean无法找到指定数据，宠物属性计算失败，id = " + (pet.getModelId() * 1000 + pet.getStage()));
            return;
        }
        for (ReadArray value : rankBean.getAttribute().getValuees()) {
            attribute.addAttribute((int) value.get(0), (int) value.get(1));
        }
        //技能属性
        List<Integer> allSkillIds = new ArrayList<>();
        allSkillIds.add(pet.getManualSkill());
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
            for (ReadArray property : bean.getParams().getValuees()) {
                if (property.size() < 3) {
                    continue;
                }
                int func = (int) property.get(0);
                int type = (int) property.get(1);
                int value = (int) property.get(2);
                if (func == 1) {
                    if (type < 1 || type > AttributeType.ATTR_MAX) {
                        continue;
                    }
                    attribute.addAttribute(type, value);
                }
            }
        }
        //计算属性的最终值
        for (int[] types : AttributeType.ATTR_FIX) {
            int type = types[0];
            // 获取属性里具体的值
            long value = attribute.getAdditionValue(type);
            long valuePercent = 0;
            if (types[1] != 0) {
                valuePercent = attribute.getAdditionValue(types[1]);
            }
            attribute.setAttribute(types[0], (long) (value * (1 + valuePercent / 10000.00d)));
        }
        //攻击、速度属性处理
        if (attribute.getAdditionValue(AttributeType.ATTR_Speed) == 0) {
            attribute.setAttribute(AttributeType.ATTR_Speed, 2000);//保证有一个基本的速度值
        }

        long maxHp = attribute.getAdditionValue(AttributeType.ATTR_MaxHp);

        attribute.cleanMaxHP();
        attribute.addMaxHP(maxHp);


        attribute.calFinalAttackSpeed();
        attribute.calFinalMoveSpeed();

        pet.getAttribute().addMaxHP(maxHp);
        pet.setCurHp(attribute.MaxHP());
        pet.setCurHp(pet.getAttribute().MaxHP());
    }

    /**
     * 激活的先决条件是否满足
     *
     * @param type 解锁条件(类型ID_类型参数；0.任务；1.前置满阶；3.道具)
     * @return
     */
    private boolean checkActive(Player player, int type, int value) {
        switch (type) {
            case PetDefine.ACTIVE_TYPE_TASK:
                if (!player.getOverMainTaskIDs().contains(value)) {
                    //没有完成规定的主线任务
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.PET_ACTIVE_TASK);
                    return false;
                }
                break;
            case PetDefine.ACTIVE_TYPE_PRE:
                Pet preposition = player.getActivePet().getPets().get(value);
                if (preposition == null) {
                    //该宠物没有激活
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.NeedPetActived);
                    return false;
                }
                Cfg_Pet_Bean petBean = CfgManager.getCfg_Pet_Container().getValueByKey(value);
                if (petBean == null) {
                    logger.error("Cfg_PetBean无法找到指定数据，激活失败，id = " + value);
                    return false;
                }
                if (preposition.getStage() < petBean.getFull_degress()) {
                    //前置宠物没有满阶
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.PET_ACTIVE_STAGE);
                    return false;
                }
                break;
            case PetDefine.ACTIVE_TYPE_ITEM:
                if (!Manager.backpackManager.manager().onRemoveItem(player, value, 1, ItemChangeReason.PetActiveDec, IDConfigUtil.getLogId())) {
                    //道具不足
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.PET_ACTIVE_ITEM);
                    return false;
                }
                break;
            default:
                logger.error("宠物激活检查先决条件，条件类型错误, type = " + type);
                return false;
        }
        return true;
    }

    @Override
    public boolean petAction(Player player, int actType, int modelId, boolean isGm) {
        boolean result = false;
        switch (actType) {
            case 1: //激活
                result = activePet(player, modelId, isGm);
                break;
            case 2: //强化
                result = strengthenPet(player, modelId);
                break;
            case 3: //出战
                result = callPet(player, modelId);
                break;
            case 4: //TODO 客户端没有召回系统
//                result = recyclePet(player);
                break;
            default:
                logger.error("请求错误的actType, actType=" + actType);
        }
        if (!result) {
            return false;
        }
        return true;
    }

    /**
     * 宠物升阶
     *
     * @param player
     * @param modelId
     * @return
     */
    private boolean strengthenPet(Player player, int modelId) {
        ActivePet pets = player.getActivePet();
        Pet pet = pets.getPets().get(modelId);
        if (pet == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Pet_Attach_This_Pet_Not_Active);
            return false;
        }
        int id = modelId * 1000 + pet.getStage();
        Cfg_Pet_rank_Bean bean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(id);
        if (bean == null) {
            logger.error("宠物升阶失败，无法找到指定Cfg_Pet_rankBean，id = " + id);
            return false;
        }
        int nextStage = pet.getStage() + 1;
        int nextId = modelId * 1000 + nextStage;
        Cfg_Pet_rank_Bean nextBean = CfgManager.getCfg_Pet_rank_Container().getValueByKey(nextId);
        if (nextBean == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PET_STAGE_MAX);
            return false;
        }
        if (bean.getRank_exp().size() < 2) {
            logger.error("宠物升阶失败, Cfg_Pet_rankBean消耗配置的长度小于2，id = " + id);
            return false;
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player, bean.getRank_exp().get(0), bean.getRank_exp().get(1), ItemChangeReason.PetStrengthDec, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            Manager.backpackManager.manager().sendItemNotEnough(player, bean.getRank_exp().get(0));
            return false;
        }
        pet.getPassivitySkill().clear();
        for (ReadArray<Integer> skill : nextBean.getPet_skill().getValuees()) {
            pet.getPassivitySkill().add(skill.get(1));
        }
        pet.setStage(nextStage);
        Manager.countManager.addVariant(player, VariantType.PetUpDegree, 1);
        Manager.controlManager.operate(player, FunctionVariable.PetNum, 1);
        //计算宠物属性
        calPetAttribute(pet);
        //记log
        writePetLog(player, pet, PetDefine.PET_OPERATION_STRNGTH);
        //计算玩家属性
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //通知客户端
        PetMessage.ResSyncPet.Builder msg = PetMessage.ResSyncPet.newBuilder();
        PetMessage.PetInfo.Builder info = PetMessage.PetInfo.newBuilder();
        info.setModelId(modelId);
        info.setCurStage(nextStage);
        msg.setPet(info);
        msg.setFight(player.getActivePet().getNature().getPower());
        MessageUtils.send_to_player(player, PetMessage.ResSyncPet.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        RoleGrowLog.create(player, GrowType.pet_star_up, 0, modelId, nextStage-2, nextStage-1, null);
        return true;
    }

    @Override
    public void eatEquip(Player player, PetMessage.ReqEatEquip messInfo) {
        int itemId = messInfo.getItemId();
        ActivePet pets = player.getActivePet();
        int oldId = pets.getLevel();
        int level = pets.getLevel();
        long exp = pets.getExp();

        Cfg_Pet_level_Bean bean = CfgManager.getCfg_Pet_level_Container().getValueByKey(level);
        if (bean == null) {
            logger.error("Cfg_Pet_level_Bean无法找到指定数据，id = " + level);
            return;
        }
        Cfg_Pet_level_Bean nextBean = CfgManager.getCfg_Pet_level_Container().getValueByKey(level + 1);
        if (nextBean == null && exp >= bean.getExp()) {
            return;
        }

        HashMap<Integer, Integer> exps = new HashMap<>();
        for (ReadArray<Integer> value : Global.Pet_Levelup_Item_Num.getValuees()) {
            if (value.size() < 2) {
                continue;
            }
            exps.put(value.get(0), value.get(1));
        }

        int needExp = bean.getExp();
        boolean isChange = false;
        HashMap<Integer, Integer> itemList = new HashMap<>();
        long totalAddExp = 0;
        if (itemId > 0) {
            //单个材料提升最多消耗升一级的数量
            if (!exps.containsKey(itemId)) {
                return;
            }
            int itemNum = Manager.backpackManager.manager().getItemNum(player, itemId);
            if(itemNum > 1){
                itemNum = 1;
            }
            for (int i = 1; i <= itemNum; i++) {
                exp += exps.get(itemId);
                totalAddExp += exps.get(itemId);
                while (exp >= needExp) {
                    nextBean = CfgManager.getCfg_Pet_level_Container().getValueByKey(level + 1);
                    if (nextBean == null) {
                        exp = Math.min(needExp, exp);
                        break;
                    }
                    isChange = true;
                    level += 1;
                    exp -= needExp;
                    needExp = nextBean.getExp();
                }
                itemList.put(itemId, i);
                if (isChange) {
                    break;
                }
            }
        } else {
            //一键提升
            out:
            for (Integer itemModelId : exps.keySet()) {
                int itemNum = Manager.backpackManager.manager().getItemNum(player, itemModelId);
                if (itemNum == 0) {
                    continue;
                }
                for (int i = 1; i <= itemNum; i++) {
                    exp += exps.get(itemModelId);
                    totalAddExp += exps.get(itemModelId);
                    while (exp >= needExp) {
                        nextBean = CfgManager.getCfg_Pet_level_Container().getValueByKey(level + 1);
                        if (nextBean == null) {
                            exp = Math.min(needExp, exp);
                            itemList.put(itemModelId, i);
                            break out;
                        }
                        isChange = true;
                        level += 1;
                        exp -= needExp;
                        needExp = nextBean.getExp();
                    }
                }
                itemList.put(itemModelId, itemNum);
            }
        }
        if (itemList.isEmpty()) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        for (Map.Entry<Integer, Integer> items : itemList.entrySet()) {
            if (!Manager.backpackManager.manager().onRemoveItem(player, items.getKey(), items.getValue(), ItemChangeReason.PetEatEquipDec, actionId)) {
                logger.error("error : 不应该走到这，正常情况一定会扣成功");
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
                return;
            }
        }

        if (isChange) {
            pets.setLevel(level);
            Manager.controlManager.operate(player, FunctionVariable.PetLevel, 0);
            Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        }
        pets.setExp(exp);
        Manager.countManager.addVariant(player, VariantType.PetEat, 1);
        Manager.controlManager.operate(player, FunctionVariable.PetDevour, 1);

        //记log
        writePetLevelLog(player, totalAddExp, itemList);
        //发消息
        PetMessage.ResEatEquip.Builder msg = PetMessage.ResEatEquip.newBuilder();
        msg.setCurLevel(level);
        msg.setCurExp(exp);
        msg.setFight(player.getActivePet().getNature().getPower());
        MessageUtils.send_to_player(player, PetMessage.ResEatEquip.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        //成长BI
        Manager.biManager.getScript().biGrow(player, GrowType.pet_level_up.getType(), 0, BIDefine.GrowLevelUp, oldId, level, level);
    }

    @Override
    public void eatSoul(Player player, PetMessage.ReqEatSoul messInfo) {
        logger.error("宠物御魂修改。这个接口没用了");
//        int soulId = messInfo.getSoulId();
//        Cfg_Pet_soul_Bean bean = CfgManager.getCfg_Pet_soul_Container().getValueByKey(soulId);
//        if (bean == null) {
//            logger.error("宠物系统，吞噬御魂出错，找不到对应的Cfg_Pet_soulBean数据，id = " + soulId);
//            return;
//        }
//        int itemModelId = bean.getConsume();
//        ActivePet activePet = player.getActivePet();
//        Integer num = activePet.getSoul().get(soulId);
//        if (num == null) {
//            num = 0;
//        }
//        int old = num;
//        if (num >= bean.getConsumption_max()) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PetSoulLimit);
//            return;
//        }
//        int ownerNum = Manager.backpackManager.manager().getItemNum(player, itemModelId);
//        if (ownerNum <= 0) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
//            return;
//        }
//        int canCostNum = bean.getConsumption_max() - num;
//        int needCostNum = canCostNum >= ownerNum ? ownerNum : canCostNum;
//        if (!Manager.backpackManager.manager().onRemoveItem(player, itemModelId, needCostNum, ItemChangeReason.PetSoulRemoveItem, IDConfigUtil.getLogId())) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
//            Manager.backpackManager.manager().sendItemNotEnough(player, itemModelId);
//            return;
//        }
//        num += needCostNum;
//        activePet.getSoul().put(soulId, num);
//        Manager.controlManager.operate(player, FunctionVariable.PetSoul, num);
//        //计算玩家属性
//        Manager.playerAttAttributeManager.calPlayerAttributs(player, PlayerAttributeType.PET);
//        //通知客户端
//        PetMessage.ResEatSoul.Builder msg = PetMessage.ResEatSoul.newBuilder();
//        PetMessage.PetSoulInfo.Builder info = PetMessage.PetSoulInfo.newBuilder();
//        info.setSoulId(soulId);
//        info.setSoulLevel(num);
//        msg.setPetSoulInfo(info);
//        MessageUtils.send_to_player(player, PetMessage.ResEatSoul.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//        Manager.biManager.getScript().biGrow(player, 11, 0, BIDefine.GrowLevelUp, old, num, soulId);
    }

    /**
     * 宠物属性变化记log
     */
    private void writePetLog(Player player, Pet pet, int type) {
        try {
            PetLog log = new PetLog();
            log.setPlayer(player);
            log.setActionType(type);
            log.setPetId(pet.getModelId());
            log.setStage(pet.getStage());
            LogService.getInstance().execute(log);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /**
     * 记等级变化log
     */
    private void writePetLevelLog(Player player, long addExp, HashMap<Integer, Integer> eatItem) {
        try {
            ActivePet pet = player.getActivePet();
            PetLevelChangeLog log = new PetLevelChangeLog();
            log.setPlayer(player);
            log.setLevel(pet.getLevel());
            log.setExp(pet.getExp());
            log.setAddExp(addExp);
            log.setEatEquips(eatItem.toString());
            LogService.getInstance().execute(log);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @Override
    public void wearEquip(Player player, long equipId, int assistantId, int cellId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        PetEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            logger.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        Item equip = player.getPetEquipPackItems().get(equipId);
        if (equip == null) {
            logger.error("宠物装备找不到");
        }
        Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
        if (eqBean == null) {
            logger.error("装备表中没有找到该装备,id=" + equip.getItemModelId());
            return;
        }
        if (eqBean.getLevel() > player.getLevel()) {
            logger.error("等级不足，无法穿戴");
            return;
        }
        if (eqBean.getPart() != cellId) {
            logger.error("该装备不能穿戴到这个位置");
            return;
        }
        long action = IDConfigUtil.getLogId();
        //背包扣除将要穿戴的装备
        Item equipInBag = removePetEquip(player, equipId, ItemChangeReason.DressPetEquipDec, action);
        if (equipInBag == null) {
            logger.error("扣除背包中装备失败");
            return;
        }
        Item oldEquip = part.getPetEquip();
        //该位置已经有装备，旧装备到背包
        if (oldEquip != null) {
            addPetEquip(player,oldEquip,ItemChangeReason.ReplacePetEquipGet);
        }
        part.setPetEquip(equipInBag);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //通知客户端
        PetMessage.ResPetEquipWear.Builder pb = PetMessage.ResPetEquipWear.newBuilder();
        pb.setEquipId(equipId);
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setEquipModelId(equipInBag.getItemModelId());
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipWear.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player,GrowType.pet_equip_wear, eqBean, part.getStrengthLv(), assistantId);
    }

    @Override
    public void unwearEquip(Player player, int assistantId, int cellId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        PetEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            logger.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getPetEquip() == null) {
            logger.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        BackpackManager.getInstance().manager().addItem(player, part.getPetEquip(), ItemChangeReason.ReplacePetEquipGet, IDConfigUtil.getLogId());
        part.setPetEquip(null);
        //通知客户端
        PetMessage.ResPetEquipUnWear.Builder pb = PetMessage.ResPetEquipUnWear.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipUnWear.MsgID.eMsgID_VALUE, pb.build().toByteArray());
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
    }

    /**
     * 初始化宠物助阵数据
     */
    public void initPetAssistantData(Player player) {
        ConcurrentHashMap<Integer, PetAssistant> assistant = player.getActivePet().getAssistants();
        if (!assistant.isEmpty()) {
            logger.error("宠物助阵数据已经初始化过！");
            return;
        }
        //主助阵id
        int mainAssistantId = 0;
        //助战id-每个助战里面的装备格子id
        HashMap<Integer, ArrayList<Integer>> assistant_cells = new HashMap<>();
        for (Cfg_Pet_equip_unlock_Bean cfg : CfgManager.getCfg_Pet_equip_unlock_Container().getValuees()) {
            int assistantId = cfg.getSite();
            int cellId = cfg.getPartId();
            ArrayList<Integer> list = assistant_cells.getOrDefault(assistantId, new ArrayList<>());
            list.add(cellId);
            assistant_cells.put(assistantId, list);

            if (mainAssistantId == 0) {
                mainAssistantId = assistantId;
            }
        }
        //每个助战位id
        int eachAssistantId = mainAssistantId;
        for (Map.Entry<Integer, ArrayList<Integer>> entry : assistant_cells.entrySet()) {
            //每一个助战位
            PetAssistant petAssistant = new PetAssistant();
            //每一个助战位中有4个装备格子
            ConcurrentHashMap<Integer, PetEquipPart> parts = new ConcurrentHashMap<>();
            for (Integer id : entry.getValue()) {
                parts.put(id, new PetEquipPart());
            }
            petAssistant.setParts(parts);
            //初始化每个助战位的全身强化等级、全身附魂等级。id跟pet_equip_inten_class表和pet_equip_soulbound_class表的id对应
            petAssistant.setStrengthActiveId(eachAssistantId * 10000);
            petAssistant.setSoulActiveId(eachAssistantId * 10000);
            eachAssistantId++;
            //助战id-助战位
            assistant.put(entry.getKey(), petAssistant);
        }
        //第一个助战位默认激活
        player.getActivePet().getAssistants().get(mainAssistantId).setActive(true);
        RoleGrowLog.create(player, GrowType.pet_assistant_unlock, 0, mainAssistantId, mainAssistantId,mainAssistantId, null);
        //助阵数据初始化完毕，要默认把主宠放到第一个助战位
        int petId = player.getActivePet().getFightPet();
        assistant.get(mainAssistantId).setPetId(petId);
        //推送客户端第一个助战位有了宠物
        PetMessage.ResChangeAssiPet.Builder pb = PetMessage.ResChangeAssiPet.newBuilder();
        pb.setAssistantId(mainAssistantId);
        pb.setPetModelId(player.getActivePet().getFightPet());
        MessageUtils.send_to_player(player, PetMessage.ResChangeAssiPet.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player, GrowType.pet_assistant_active, 0, mainAssistantId, 0,petId, null);
    }

    @Override
    public boolean addPetEquip(Player player, Item item, int reason) {
        if (item == null) {
            logger.error("传入的道具为空！");
            return false;
        }
        Cfg_Equip_Bean cfg = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
        if (cfg == null) {
            logger.error("道具的模板数据找不到！");
            return false;
        }
        long action = IDConfigUtil.getLogId();

        player.getPetEquipPackItems().put(item.getId(), item);
        backpackMessage.ResPetEquipAdd.Builder builder = backpackMessage.ResPetEquipAdd.newBuilder();
        builder.setItemInfo(Manager.backpackManager.manager().buildItemInfo(item).build());
        builder.setReason(reason);
        MessageUtils.send_to_player(player, backpackMessage.ResPetEquipAdd.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //增加日志
        Manager.backpackManager.manager().writeItemLogAndBI(player,0, item.getNum(), item, reason, action);
        //设置了宠物装备自动分解
        if (player.getActivePet().isAutoDecompose()) {
            //满足自动分解的条件
            if (6 > cfg.getQuality() && 1 >= cfg.getDiamond_Number()) {
                Cfg_Pet_equip_resolve_Bean bean = CfgManager.getCfg_Pet_equip_resolve_Container().getValueByKey(item.getItemModelId());
                if (bean == null) {
                    logger.error("配置数据找不到");
                }else{
                    //增加物品分解日志
                    removePetEquip(player, item.getId(), ItemChangeReason.AutoDecomposePetEquipDec, action);
                    for (ReadArray<Integer> value : bean.getResolve_rewards().getValuees()) {
                        if (RandomUtils.random(10000) < value.get(0)) {
                            BackpackManager.getInstance().manager().addItem(player, Item.createItem(value.get(1), value.get(2), true), ItemChangeReason.AutoDecomposePetEquipGet, action);
                        }
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canAdd(Player player, int petEquipNum) {
        return petEquipNum + player.getPetEquipPackItems().size() <= 200;
    }

    @Override
    public Item removePetEquip(Player player, long itemId, int reason, long action) {
        Item petEquip = player.getPetEquipPackItems().remove(itemId);
        if (petEquip == null) {
            return null;
        }
        backpackMessage.ResPetEquipDelete.Builder builder = backpackMessage.ResPetEquipDelete.newBuilder();
        builder.setItemId(itemId);
        builder.setReason(reason);
        MessageUtils.send_to_player(player, backpackMessage.ResPetEquipDelete.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //宠物装备日志
        Manager.backpackManager.manager().writeItemLogAndBI(player,1,0,petEquip,reason,action);
        return petEquip;
    }

    /**
     * 更换助阵宠物
     */
    @Override
    public void changePetAssiant(Player player, int assistantId, int petId) {
        ActivePet petData = player.getActivePet();
        Pet pet = petData.getPets().get(petId);
        //宠物未激活
        if (pet == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Pet_Attach_This_Pet_Not_Active);
            return;
        }
        PetAssistant operateAssistant = petData.getAssistants().get(assistantId);
        //助阵位找不到
        if (operateAssistant == null || !operateAssistant.isActive()) {
            logger.error("前端传来的助阵位id非法assistantId={} p={}", assistantId, player);
            return;
        }

        //该助阵位上本来就是这个宠物
        if (operateAssistant.getPetId() == petId) {
            return;
        }
        //要操作的阵上之前说不定有宠物
        int oldPet = operateAssistant.getPetId();
        //要操作的宠物说不定之前就在阵上
        int oldAssistant = 0;
        for (Map.Entry<Integer, PetAssistant> entry : petData.getAssistants().entrySet()) {
            if (entry.getValue().getPetId() == petId) {
                oldAssistant = entry.getKey();
                break;
            }
        }
        operateAssistant.setPetId(petId);
        //推送客户端该助战位成功换上了宠物
        PetMessage.ResChangeAssiPet.Builder pb = PetMessage.ResChangeAssiPet.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setPetModelId(petId);
        MessageUtils.send_to_player(player, PetMessage.ResChangeAssiPet.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        //说明是切换两个助阵位的宠物
        if (oldPet != 0 && oldAssistant != 0) {
            petData.getAssistants().get(oldAssistant).setPetId(oldPet);
            //另一个助战位置的宠物变动也推送一次
            PetMessage.ResChangeAssiPet.Builder pb2 = PetMessage.ResChangeAssiPet.newBuilder();
            pb2.setAssistantId(oldAssistant);
            pb2.setPetModelId(oldPet);
            MessageUtils.send_to_player(player, PetMessage.ResChangeAssiPet.MsgID.eMsgID_VALUE, pb2.build().toByteArray());

            RoleGrowLog.create(player, GrowType.pet_assistant_active, 0, oldAssistant, oldPet,oldPet, null);
        }
        //如果第一个助战位的宠物改变。那么出战的宠物要同步
        int mainAssistantId = CfgManager.getCfg_Pet_equip_unlock_Container().getValuees()[0].getSite();
        int mainAssistantPetId = petData.getAssistants().get(mainAssistantId).getPetId();
        if (mainAssistantPetId != petData.getFightPet()) {
            callPet(player, mainAssistantPetId);
        }

        RoleGrowLog.create(player, GrowType.pet_assistant_active, 0, assistantId, operateAssistant.getPetId(),petId, null);
    }


    /**
     * 强化宠物装备
     */
    @Override
    public void intenPetEquip(Player player, int assistantId, int cellId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        PetEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            logger.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getPetEquip() == null) {
            logger.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        int oldIntenLv = part.getStrengthLv();
        //当前强化等级的配置
        Cfg_Pet_equip_inten_Bean nowIntenConfig = CfgManager.getCfg_Pet_equip_inten_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldIntenLv);
        //下一级强化等级的配置
        Cfg_Pet_equip_inten_Bean nextIntenConfig = CfgManager.getCfg_Pet_equip_inten_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldIntenLv + 1);
        if (nextIntenConfig == null) {
            logger.error("已强化到最高等级");
            return;
        }
        if (nowIntenConfig == null) {
            logger.error("强化配置表找不到");
            return;
        }
        //强化所需物品不足
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, nowIntenConfig.getConsume(), IDConfigUtil.getLogId(), ItemChangeReason.IntenPetEquipDec)) {
            return;
        }
        part.setStrengthLv(oldIntenLv + 1);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //返回结果
        PetMessage.ResPetEquipStrength.Builder pb = PetMessage.ResPetEquipStrength.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setStrengthLv(part.getStrengthLv());
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipStrength.MsgID.eMsgID_VALUE, pb.build().toByteArray());
        Manager.controlManager.operate(player, FunctionVariable.Pet_Equip_Strengthen_Level, 1);

        Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getPetEquip().getItemModelId());
        RoleGrowLog.create(player, GrowType.pet_equip_intensify, eqBean, part.getStrengthLv(), assistantId);
    }

    /**
     * 附魂（进阶）宠物装备
     */
    @Override
    public void soulPetEquip(Player player, int assistantId, int cellId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        PetEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            logger.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getPetEquip() == null) {
            logger.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        int oldSoulLv = part.getSoulLv();
        //当前附魂等级的配置
        Cfg_Pet_equip_soulbound_Bean nowSoulConfig = CfgManager.getCfg_Pet_equip_soulbound_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldSoulLv);
        //下一级附魂等级的配置
        Cfg_Pet_equip_soulbound_Bean nextSoulConfig = CfgManager.getCfg_Pet_equip_soulbound_Container().getValueByKey(assistantId * 100000000 + cellId * 10000 + oldSoulLv + 1);
        if (nextSoulConfig == null) {
            logger.error("已附魂到最高等级");
            return;
        }
        if (nowSoulConfig == null) {
            logger.error("附魂消耗数据找不到");
            return;
        }
        //附魂所需物品不足
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, nowSoulConfig.getConsume(), IDConfigUtil.getLogId(), ItemChangeReason.SoulPetEquipDec)) {
            return;
        }
        part.setSoulLv(oldSoulLv + 1);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //返回结果
        PetMessage.ResPetEquipSoul.Builder pb = PetMessage.ResPetEquipSoul.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setCellId(cellId);
        pb.setSoulLv(part.getSoulLv());
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipSoul.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        Cfg_Equip_Bean eqBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getPetEquip().getItemModelId());
        RoleGrowLog.create(player, GrowType.pet_equip_soul, eqBean, part.getSoulLv(), assistantId);
    }

    /**
     * 宠物装备全身强化属性激活
     */
    @Override
    public void petEquipIntenActive(Player player, int assistantId, int levelId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        if (assistant.getStrengthActiveId() >= levelId) {
            logger.error("该等级已经激活了" + player.getId() + ", " + assistantId);
            return;
        }
        Cfg_Pet_equip_inten_class_Bean config = CfgManager.getCfg_Pet_equip_inten_class_Container().getValueByKey(levelId);
        if (config == null) {
            logger.error("配置表找不到" + levelId);
            return;
        }
        int totalLv = 0;
        for (PetEquipPart part : assistant.getParts().values()) {
            totalLv += part.getStrengthLv();
        }
        if (totalLv < config.getSuitLevel()) {
            logger.error("还没有达到全身强等级");
            return;
        }
        int oldLevel = assistant.getStrengthActiveId();
        assistant.setStrengthActiveId(levelId);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //返回结果
        PetMessage.ResPetEquipActiveInten.Builder pb = PetMessage.ResPetEquipActiveInten.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setStrengthActiveId(assistant.getStrengthActiveId());
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipActiveInten.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player,GrowType.pet_equip_intensify_active, 0, assistantId, oldLevel, levelId, null);
    }

    /**
     * 宠物装备全身附魂属性激活
     */
    @Override
    public void petEquipSoulActive(Player player, int assistantId, int levelId) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        if (assistant.getSoulActiveId() >= levelId) {
            logger.error("该等级已经激活了" + player.getId() + ", " + assistantId);
            return;
        }
        Cfg_Pet_equip_soulbound_class_Bean config = CfgManager.getCfg_Pet_equip_soulbound_class_Container().getValueByKey(levelId);
        if (config == null) {
            logger.error("配置表找不到" + levelId);
            return;
        }
        int totalLv = 0;
        for (PetEquipPart part : assistant.getParts().values()) {
            totalLv += part.getSoulLv();
        }
        if (totalLv < config.getSuitLevel()) {
            logger.error("全身附魂等级没有全部达到");
            return;
        }
        int oldLevel = assistant.getSoulActiveId();
        assistant.setSoulActiveId(levelId);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        //返回结果
        PetMessage.ResPetEquipActiveSoul.Builder pb = PetMessage.ResPetEquipActiveSoul.newBuilder();
        pb.setAssistantId(assistantId);
        pb.setSoulActiveId(assistant.getSoulActiveId());
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipActiveSoul.MsgID.eMsgID_VALUE, pb.build().toByteArray());

        RoleGrowLog.create(player,GrowType.pet_equip_soul_active,0,assistantId, oldLevel, levelId, null);
    }

    /**
     * 宠物装备合成
     */
    @Override
    public void petEquipSynthetic(Player player, int assistantId, int cellId, List<Long> eqs) {
        PetAssistant assistant = player.getActivePet().getAssistants().get(assistantId);
        if (assistant == null) {
            logger.error("宠物助战数据竟然没初始化" + player.getId() + ", " + assistantId);
            return;
        }
        PetEquipPart part = assistant.getParts().get(cellId);
        if (part == null) {
            logger.error("装备格子找不到" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }
        if (part.getPetEquip() == null) {
            logger.error("目标位置上没有装备" + player.getId() + ", " + assistantId + ", " + cellId);
            return;
        }

        //升级前装备
        Cfg_Equip_Bean srcBean = CfgManager.getCfg_Equip_Container().getValueByKey(part.getPetEquip().getItemModelId());
        //升级的配置数据
        Cfg_Pet_equip_synthesis_Bean synthesis_Bean = CfgManager.getCfg_Pet_equip_synthesis_Container().getValueByKey(part.getPetEquip().getItemModelId());
        //升级后装备
        Cfg_Equip_Bean targetBean = CfgManager.getCfg_Equip_Container().getValueByKey(synthesis_Bean.getEquip_ID());
        if (srcBean == null || targetBean == null) {
            logger.error("升级前后的装备bean竟然为空");
            return;
        }

        List<Item> equips = new ArrayList<>();
        long totalProbability = 0L;
        int qualityIndex, diamondIndex;
        for (Long eid : eqs) {
            //检测前端传来的即将消耗的装备存不存在
            Item equip = player.getPetEquipPackItems().get(eid);
            if (equip == null) {
                logger.error("宠物装备找不到");
                return;
            }
            //要消耗的装备模板数据
            Cfg_Equip_Bean bean = CfgManager.getCfg_Equip_Container().getValueByKey(equip.getItemModelId());
            if (!synthesis_Bean.getJoin_part().isEmpty() && !synthesis_Bean.getJoin_part().contains(bean.getPart())) {
                logger.error("不能消耗该位置的装备");
                return;
            }
            if (!synthesis_Bean.getQuality().contains(bean.getQuality())) {
                logger.error("不能消耗该品质的装备");
                return;
            }
            if (!synthesis_Bean.getDiamond().contains(bean.getDiamond_Number())) {
                logger.error("不能消耗该星级的装备");
                return;
            }
            qualityIndex = getArrayIndex(synthesis_Bean.getQuality(), bean.getQuality());
            diamondIndex = getArrayIndex(synthesis_Bean.getDiamond(), bean.getDiamond_Number());
            totalProbability += (long) synthesis_Bean.getQuality_Number().get(qualityIndex) * synthesis_Bean.getDiamond_Number().get(diamondIndex) * synthesis_Bean.getJoin_num_probability();
            equips.add(equip);
        }

        long action = IDConfigUtil.getLogId();
        //要扣除的道具
        if (synthesis_Bean.getJoin_item().size() == 2) {
            int itemNumber = synthesis_Bean.getJoin_item().get(1);
            int itemModeId = synthesis_Bean.getJoin_item().get(0);
            if (!Manager.backpackManager.manager().onRemoveItem(player, itemModeId, itemNumber, ItemChangeReason.ComposePetEquipDec, action)) {
                logger.error("要扣除的道具不足");
                return;
            }
        }

        //移除消耗的装备
        for (Long eid : eqs) {
            Item removed = removePetEquip(player, eid, ItemChangeReason.ComposePetEquipDec, action);
            if (removed == null) {
                logger.error("要扣除的装备不存在");
            }
        }

        //概率计算成功与否
        boolean success = RandomUtils.defaultIsGenerate((int) (totalProbability / 10000_0000));

        PetMessage.ResPetEquipSynthesis.Builder res = PetMessage.ResPetEquipSynthesis.newBuilder();
        res.setSuccess(success);
        res.setAssistantId(assistantId);
        res.setCellId(cellId);

        if (success) {
            //合成了新装备，直接替换掉源装备
            Item tarEquip = Item.createItem(targetBean.getId(), 1, true);
            Item srcEquip = part.getPetEquip();
            //物品消耗日志
            Manager.backpackManager.manager().writeItemLogAndBI(player,1,0,srcEquip,ItemChangeReason.ComposePetEquipDec,action);
            part.setPetEquip(tarEquip);
            res.setNewEquip(BackpackManager.getInstance().manager().buildItemInfo(tarEquip));
            //物品添加日志
            Manager.backpackManager.manager().writeItemLogAndBI(player,0,1,tarEquip,ItemChangeReason.ComposePetEquipGet,action);
            if (synthesis_Bean.getNotice() != 0 || synthesis_Bean.getChatchannel() != null) {
                //TODO:公告
               MessageUtils.notify_allOnlinePlayer(synthesis_Bean.getNotice() , synthesis_Bean.getChatchannel(), MessageString.EquipSynthetic,
                       player.getId()+"", player.getName(), Manager.backpackManager.manager().getChatInfo(tarEquip),
                        Utils.makeUrlStr(MessageString.EquipSynthetic));
            }

            RoleGrowLog.create(player, GrowType.pet_equip_synthesis,targetBean, part.getStrengthLv(), assistantId);
        }
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipSynthesis.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 获取指定值在数组中的索引
     */
    private int getArrayIndex(ReadIntegerArray array, int target) {
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == target) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 宠物装备分解
     */
    public void petEquipDecompose(Player player, List<Long> equipId) {
        //奖励id与数量，用于奖励合并
        HashMap<Integer, Integer> map = new HashMap<>();
        long action = IDConfigUtil.getLogId();
        //成功分解的装备数
        int count = 0;
        for (Long id : equipId) {
            //背包扣除将要分解的装备
            Item equipInBag = removePetEquip(player, id, ItemChangeReason.DecomposePetEquipDec, action);
            if (equipInBag == null) {
                logger.error("扣除背包中装备失败");
                continue;
            }
            Cfg_Pet_equip_resolve_Bean bean = CfgManager.getCfg_Pet_equip_resolve_Container().getValueByKey(equipInBag.getItemModelId());
            if (bean == null) {
                logger.error("配置数据找不到");
                continue;
            }
            for (ReadArray<Integer> value : bean.getResolve_rewards().getValuees()) {
                if (RandomUtils.random(10000) < value.get(0)) {
                    Integer num = map.getOrDefault(value.get(1), 0);
                    map.put(value.get(1), num + value.get(2));
                }
            }
            count++;
        }
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            BackpackManager.getInstance().manager().addItem(player, Item.createItem(entry.getKey(), entry.getValue(), true), ItemChangeReason.DecomposePetEquipGet, action);
        }
        Manager.countManager.addCount(player, BaseCountType.Pet_Equip_Resolve, 0, Count.RefreshType.CountType_Forever, count);
        Manager.controlManager.operate(player, FunctionVariable.Pet_Equip_Resolve, count);
    }

    /**
     * 设置宠物装备自动分解
     */
    public void autoEquipDecomposeSet(Player player, boolean set) {
        player.getActivePet().setAutoDecompose(set);
        PetMessage.ResPetEquipDecomposeSetting.Builder pb = PetMessage.ResPetEquipDecomposeSetting.newBuilder();
        pb.setSet(set);
        MessageUtils.send_to_player(player, PetMessage.ResPetEquipDecomposeSetting.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    /**
     * 激活宠物装备槽位
     *
     * @param player
     */
    @Override
    public void activePetEquip(Player player, Integer slotId) {

        if (slotId != null) {
            ActivePet ap = player.getActivePet();
            PetAssistant site = ap.getAssistants().get(slotId);
            if (site == null || site.isActive()) {
                return;
            }
            Map.Entry<Integer, PetEquipPart> cell = Utils.findOne(site.getParts().entrySet(), et -> true);

            Cfg_Pet_equip_unlock_Bean bean = CfgManager.getCfg_Pet_equip_unlock_Container().getValueByKey(slotId * 10000 + cell.getKey());
            if (Manager.backpackManager.manager().onRemoveItem(
                    player, bean.getSiteUnlockItem().get(0), bean.getSiteUnlockItem().get(1), ItemChangeReason.ActivePetEquipSlotDec, IDConfigUtil.getLogId())) {
                site.setActive(true);
                sendPetAllInfo(player, false);
                Manager.controlManager.operate(player, FunctionVariable.Pet_Equip_Inten, 1);
                MessageUtils.notify_player(player, Notify.NORMAL, MessageString.ActiveSuccess);
                logger.info("道具激活宠物槽位 slotId={} P={}", bean.getSite(), player);

                RoleGrowLog.create(player, GrowType.pet_assistant_unlock, 0, slotId, slotId,slotId, null);
            }
            return;
        }
        boolean isActive = false;
        for (Cfg_Pet_equip_unlock_Bean bean : CfgManager.getCfg_Pet_equip_unlock_Container().getValuees()) {
            ActivePet ap = player.getActivePet();
            PetAssistant site = ap.getAssistants().get(bean.getSite());
            if (site == null) {
                continue;
            }
            //检查槽位装备栏
            PetEquipPart part = site.getParts().get(bean.getPartId());
            if (part != null && !part.isActive()) {
                if (Manager.controlManager.deal().checkFuncProgress(player, bean.getPartUnlock())) {
                    part.setActive(true);
                    isActive = true;
                    logger.info("条件激活宠物部位 slotId={} part={} P={}", bean.getSite(), bean.getPartId(), player);
                }
            }
            if (site.isActive()) {
                continue;
            }
            if (bean.getSiteUnlock().size() <= 0) {
                continue;
            }
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getSiteUnlock())) {
                site.setActive(true);
                isActive = true;
                logger.info("条件激活宠物槽位 slotId={} P={}", bean.getSite(), player);
                RoleGrowLog.create(player, GrowType.pet_assistant_unlock, 0, bean.getSite(), bean.getSite(),bean.getSite(), null);
            }
        }
        if (isActive) {
            sendPetAllInfo(player, false);
            Manager.controlManager.operate(player, FunctionVariable.Pet_Equip_Inten, 1);
        }
    }

    /**
     * 统计宠物装备槽激活个数
     *
     * @param player
     * @return
     */
    @Override
    public int calcPetEquipSlotNumber(Player player) {
        ActivePet ap = player.getActivePet();
        int count = 0;
        for (PetAssistant solt : ap.getAssistants().values()) {
            if (solt.isActive()) {
                count = count + 1;
            }
        }
        return count;
    }
}