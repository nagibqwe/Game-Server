package common.nature;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_State_stifle_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.attribute.BaseIntAttribute;
import com.game.bi.manager.BIDefine;
import com.game.chat.structs.Notify;
import com.game.horse.structs.HorseRideStateEnum;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.ActType;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.map.manager.MapGpsUtil;
import com.game.nature.log.*;
import com.game.nature.script.INatureScript;
import com.game.nature.structs.*;
import com.game.newfashion.manager.NewFashionManager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.statestifle.manager.StateStifleManager;
import com.game.structs.AttributeType;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.message.NatureMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NatureScript implements INatureScript {

    private final Logger logger = LogManager.getLogger(NatureScript.class);
    /**
     * 每次附灵，选择的属性的最大个数
     */
    private final static int EACH_AFFILIATED_SPIRIT_NUMBER = 3;

    @Override
    public int getId() {
        return ScriptEnum.NatureBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void initPlayerAllNatureFunction(Player player) {
        //initPlayerNature(player, player.getTalisman(), NatureType.TALISMAN);

       // initPlayerNature(player, player.getMagic(), NatureType.MAGIC);

        // initPlayerWeapon(player, player.getWeapon());
    }

    @Override
    public void initPlayerWing(Player player) {
        player.setWingStatus(1);
        player.setWing(new Nature());
        initPlayerNature(player, player.getWing(), NatureType.WING);
    }

    @Override
    public void initPlayerHorse(Player player) {
        initPlayerNature(player, player.getHorse().getNature(), NatureType.HORSE);
    }

    @Override
    public void initFabaoDrug(Player player) {
        initPlayerNature(player, player.getStifleData().getNature(), NatureType.STIFLEFFABAO);
    }

    @Override
    public void initPetDrug(Player player) {
        initPlayerNature(player, player.getActivePet().getNature(), NatureType.PET);
    }

    @Override
    public void initStifleFabao(Player player) {
        int configId = player.getStifleData().getLevel() * 100;
        Cfg_State_stifle_Bean bean = Cfg_State_stifle_Container.GetInstance().getValueByKey(configId);
        if (bean != null && bean.getModelID() != 0) {
            handleHuaxinStarUp(player.getStifleData().getNature(), player, bean.getModelID(), NatureType.STIFLEFFABAO, true);
            Manager.natureManager.deal().onReqNatureModelSet(player, NatureType.STIFLEFFABAO, bean.getModelID());
        }
        Manager.controlManager.operate(player, FunctionVariable.State_stifleID, 0);
    }

    @Override
    public void onReqNatureInfo(Player player, int type) {
        int functionId = getNatureTypeFunctionId(type);
        if (!Manager.controlManager.deal().isOpenFunction(player, functionId)) {
            return;
        }
        Nature nature = getNatureByNatureType(player, type);
        handleNatureOpenRequest(player, nature, type);
    }

    @Override
    public void onReqNatureDrug(Player player, int type, int itemId) {
        Nature nature = getNatureByNatureType(player, type);
        //神兵没有培养，不需单独处理
        handleNatureBaseUseDrugs(player, itemId, nature, type);
    }

    @Override
    public void onReqNatureModelSet(Player player, int type, int modelId) {
        //神兵单独处理
        //if (type == NatureType.WEAPON) {
        //    handleNatureBaseSkinModelId(player, modelId, player.getWeapon(), type);
        //    return;
        //}
        if (type == NatureType.STIFLEFFABAO) {
            boolean restSkill = StateStifleManager.getInstance().deal().addSkillToPlayer(player, player.getStifleData().getNature().getCurrentModelId(), modelId);
            if (!restSkill) {
                logger.error("技能设置失败!!!!");
                return;
            }
        }
        Nature nature = getNatureByNatureType(player, type);
        handleNatureBaseSkinModelId(player, modelId, nature, type);
    }

    @Override
    public void onReqNatureUpLevel(Player player, int type, int itemId, boolean isOneKeyUp) {
        //神兵单独处理
        //if (type == NatureType.WEAPON) {
        //    handleWeaponUpLevel(player, itemId, isOneKeyUp, player.getWeapon(), type);
        //    return;
        //}
        Nature nature = getNatureByNatureType(player, type);
        handleNatureBaseUpLevel(player, itemId, isOneKeyUp, nature, type);
    }

    @Override
    public void onReqNatureFashionUpLevel(Player player, int type, int id) {
        //神兵单独处理
        NatureBase nature = getNatureByNatureType(player, type);
        //if (type == NatureType.WEAPON) {
        //    nature = player.getWeapon();
        //}
        handleHuaxinStarUp(nature, player, id, type, false);
    }

    /***********************************************private functions******************************************************/
    /**
     * 处理翅膀、神器、阵列初始化
     */
    private void initPlayerNature(Player player, Nature nature, int type) {
        if (nature.getCurrentId() != 0) {
            return;
        }

        nature.setCurrentExp(0);
        nature.setCurrentId(1);
        ReadIntegerArray skill = getSkillByNatureType(type, nature.getCurrentId());
        if (skill != null) {
            addOrUpdateSkill(nature, skill.get(0), skill.get(1), player);
        }

        int modelId = getModelIDByNatureType(type, nature.getCurrentId(), player.getCareer());
        if (modelId != 0) {
            setNatureBaseModelId(player, nature, modelId, true, type);
            handleHuaxinStarUp(nature, player, modelId, type, true);
        }
        for (Cfg_Nature_att_Bean temp : CfgManager.getCfg_Nature_att_Container().getValuees()) {
            if (temp.getType() == type && temp.getLevel() == 0) {
                Drug drug = new Drug();
                drug.setItemId(temp.getItem_id());
                drug.setPos(temp.getPosition());
                drug.setBelongType(temp.getType());
                drug.setUseNumber(0);
                nature.getDrugs().put(drug.getItemId(), drug);
            }
        }
        updatePlayerAttributeByNatureType(type, player);
        updateRankInfo(type, player, nature.getCurrentId());
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);

        //返回客户端消息，同步周围玩家
        sendNatureInfoToClient(player, nature, type);
        sendModelInfoToRoundPlayer(player, modelId, type, nature.getId());

        Manager.controlManager.operate(player, FunctionVariable.NatureWingID, 0);
        Manager.controlManager.operate(player, FunctionVariable.NatureHorseID, 0);

        writeNatureUpLog(0, nature.getCurrentId(), nature.getCurrentExp(),
                nature.getActiveSkillMap().toString(), nature.getHuaxins().keySet().toString(), player.getId(), 0L);
    }

    /**
     * 处理神兵初始化
     */
    // private void initPlayerWeapon(Player player, Weapon weapon) {
    //     if (0 != weapon.getCurrentId()) {
    //         return;
    //     }
//
    //     weapon.setCurrentId(1);
    //     ReadIntegerArray skill = getSkillByNatureType(NatureType.WEAPON, weapon.getCurrentId());
    //     if (null != skill) {
    //         addOrUpdateSkill(weapon, skill.get(0), skill.get(1), player);
    //     }
//
    //     int modelId = getModelIDByNatureType(NatureType.WEAPON, weapon.getCurrentId());
    //     if (0 != modelId) {
    //         setNatureBaseModelId(weapon, modelId, true, NatureType.WEAPON);
    //     }
//
    //     Cfg_NatureWeapon_Bean bean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(weapon.getCurrentId());
//
    //     for (ReadArray array : bean.getAtt().getValuees()) {
    //         WeaponAttribute attribute = new WeaponAttribute();
    //         attribute.setAttributeId((int) array.get(0));
    //         attribute.setValue((int) array.get(1));
    //         weapon.getAttributes().add(attribute);
    //     }
//
    //     //写日志
    //     writeWeaponBreakthroughLog(0, weapon.getCurrentId(), weapon.getAttributes().toString(),
    //             weapon.getActiveSkillMap().toString(), weapon.getHuaxins().keySet().toString(), player.getId(), 0L);
//
   //     Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.Weapon);
   //     updatePlayerAttributeByNatureType(NatureType.WEAPON, player);
   //     Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
   //     handleWeaponOpenRequest(player, weapon);
   // }
    //     Manager.playerAttAttributeManager.calPlayerAttributs(player, PlayerAttributeType.Weapon);
    //     updatePlayerAttributeByNatureType(NatureType.WEAPON, player);
    //     Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
    //     handleWeaponOpenRequest(player, weapon);
    // }

    /**
     * 设置模型id
     */
    private void setNatureBaseModelId(Player player, NatureBase base, int modelId, boolean isActive, int type) {
        int actType = isActive ? 0 : 1;
        writeNatureModelLog(modelId, base.getCurrentModelId(), actType, type);
        base.setCurrentModelId(modelId);
        base.getActiveNatureModelSet().add(modelId);
        //也需要激活时装

        int fashionType = getFashionType(type);
        if (fashionType >0){
            Manager.newFashionManager.deal().huaxingActivateFashion(player,fashionType,modelId);
            Manager.newFashionManager.deal().huaxingWearFashion(player,fashionType,modelId);
        }
    }

    private int getFashionType(int actType){
        switch (actType){
            case NatureType.HORSE:
                return NewFashionManager.HORSE_TYPE;
            case NatureType.WING:
                return NewFashionManager.WING_TYPE;
            case NatureType.STIFLEFFABAO:
                return NewFashionManager.FABAO_TYPE;
            case NatureType.WEAPON:
                return NewFashionManager.WEAPON_TYPE;
        }
        return -1;
    }


    /**
     * 用增加的总共的progress增加等级
     */
    private List<Integer> useAllExp(Nature nature, int maxId, int totalAddProgress, int type) {
        int current = nature.getCurrentExp();
        int tempId = nature.getCurrentId();
        long tempDiff;
        int progress;
        for (; tempId <= maxId; tempId++) {
            progress = getProgressByNatureType(type, tempId);
            tempDiff = progress - current;
            if (tempDiff > totalAddProgress) {
                current += totalAddProgress;
                break;
            }
            current = 0;
            totalAddProgress -= tempDiff;
        }
        if (tempId >= maxId) {
            tempId = maxId;
            current = 0;
        }

        List<Integer> result = new ArrayList<>();
        result.add(tempId);
        result.add(current);
        return result;
    }

    /**
     * 处理坐骑 翅膀 神器 阵法升级
     */
    private void handleNatureBaseUpLevel(Player player, int itemId, boolean isOneKeyUp, Nature nature, int type) {
        int reason = 0;
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                reason = ItemChangeReason.HorseUpDec;
                break;
            case NatureType.WING:
                reason = ItemChangeReason.WingUpDec;
                break;
            case NatureType.TALISMAN:
                reason = ItemChangeReason.TalismanUpDec;
                break;
            case NatureType.MAGIC:
                reason = ItemChangeReason.MagicUpDec;
                break;
            case NatureType.WEAPON:
                reason = ItemChangeReason.GodWeaponUplevelDec;
                break;
        }
        /*
         * 获取当前级别的总经验
         * 因为NatureWing.xlsx中的每一个id代表一级，故直接拿当前id获取相应的配置即可拿到当前级别的总经验
         * */
        int total = getProgressByNatureType(type, nature.getCurrentId());
        if (0 == total) {
            logger.error("nature handleNatureBaseUpLevel 获取当前等级总经验出错 当前等级:" + nature.getCurrentId() + " 类型:" + type);
            return;
        }

        int current = nature.getCurrentExp();
        //当前升级需要的经验
        int needExp = total - current;
        int lastExp = getLastExp(type, nature.getCurrentId());
        if (lastExp == 0) {//满级
            return;
        }
        int maxExcelId = getNatureBaseMaxExcelId(type);
        if (0 == maxExcelId) {
            logger.error("nature handleNatureBaseUpLevel 获取当前类型最大等级出错 类型:" + type);
            return;
        }

        int totalAddProgress = 0;
        ReadIntegerArray upItemArray = getUpItemArray(type, nature.getCurrentId());
        if (null == upItemArray) {
            logger.error("nature handleNatureBaseUpLevel 获取当前类型升级物品出错 类型:" + type);
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        if (isOneKeyUp) {
            int totalNeedProgress = lastExp - current;
            int tempAddProgress;
            for (int i = 0; i < upItemArray.size(); i++) {
                int useItemId = upItemArray.get(i);
                int itemAddProgress = getItemUseEffect(useItemId);
                int itemNumber = Manager.backpackManager.manager().getItemNum(player, useItemId);
                tempAddProgress = itemAddProgress * itemNumber;
                if (totalNeedProgress >= tempAddProgress) {
                    Manager.backpackManager.manager().onRemoveItem(player, useItemId, itemNumber, reason, actionId);
                    totalNeedProgress -= tempAddProgress;
                    totalAddProgress += tempAddProgress;
                } else {
                    itemNumber = totalNeedProgress / itemAddProgress;
                    if (totalNeedProgress % itemAddProgress > 0) {
                        itemNumber++;
                    }
                    tempAddProgress += itemAddProgress * itemNumber;
                    totalNeedProgress -= tempAddProgress;
                    totalAddProgress += tempAddProgress;
                    Manager.backpackManager.manager().onRemoveItem(player, useItemId, itemNumber, reason, actionId);
                }
                if (0 >= totalNeedProgress) {
                    break;
                }
            }
        } else {
            int eachItemProgress = 0;
            for (int i = 0; i < upItemArray.size(); i++) {
                if (upItemArray.get(i) != itemId) {
                    continue;
                }
                eachItemProgress = getItemUseEffect(itemId);
                break;
            }

            if (0 == eachItemProgress) {
                return;
            }

            int itemTotalNumber = Manager.backpackManager.manager().getItemNum(player, itemId);

            int useNumber = 1;
            switch (type) {
                //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
                case NatureType.HORSE:
                case NatureType.WING:
                case NatureType.WEAPON:
                    useNumber = 1;
                    totalAddProgress = eachItemProgress;
                    break;
                case NatureType.TALISMAN:
                case NatureType.MAGIC:
                    if (needExp > eachItemProgress) {
                        totalAddProgress = eachItemProgress * itemTotalNumber;
                        if (needExp > totalAddProgress) {
                            useNumber = itemTotalNumber;
                        } else {
                            useNumber = needExp / eachItemProgress;
                            if (needExp % eachItemProgress > 0) {
                                useNumber++;
                            }
                            totalAddProgress = useNumber * eachItemProgress;
                        }
                    } else {
                        totalAddProgress = eachItemProgress;
                        useNumber = 1;
                    }
                    break;
            }

            Manager.backpackManager.manager().onRemoveItem(player, itemId, useNumber, reason, actionId);
        }

        int oldId = nature.getCurrentId();
        List<Integer> result = useAllExp(nature, maxExcelId, totalAddProgress, type);
        nature.setCurrentId(result.get(0));
        nature.setCurrentExp(result.get(1));
        NatureMessage.ResNatureUpLevel.Builder builder = NatureMessage.ResNatureUpLevel.newBuilder();
        buildResNatureUpLevel(nature, type, builder, oldId, player);
        //在修改了nature的id后，才重新计算属性和战力
        if (oldId != result.get(0)) {
            if (NatureType.HORSE == type) {
                //由于坐骑需要判断阶数的提升，而阶数的提升和等级的提升不是线性关系，故需要单独处理
                int currentSteps = player.getHorse().getHorseSteps();
                Cfg_NatureHorse_Bean oldHorseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(oldId);
                if (null != oldHorseBean && oldHorseBean.getSteps() < currentSteps) {
                    Manager.controlManager.operate(player, FunctionVariable.HorseNum, 0);
                    Manager.controlManager.operate(player, FunctionVariable.HorseStarNumRank, 0);
                }
                Manager.controlManager.operate(player, FunctionVariable.HorseStarNum, 0);
            } else {
                checkControl(player, type);
            }
            updatePlayerAttributeByNatureType(type, player);
        }
        builder.setCurexp(nature.getCurrentExp());
        builder.setFight(nature.getPower());
        MessageUtils.send_to_player(player, NatureMessage.ResNatureUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //记录升级的日志
        writeNatureUpLog(oldId, nature.getCurrentId(), nature.getCurrentExp(),
                nature.getActiveSkillMap().toString(), nature.getHuaxins().keySet().toString(), player.getId(), actionId);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
        //成长BI
        natrueGrowBI(player, type, oldId, nature.getCurrentId());
    }

    /**
     * 构造
     */
    private void buildResNatureUpLevel(NatureBase nature, int type, NatureMessage.ResNatureUpLevel.Builder builder, int oldId, Player player) {
        builder.setNatureType(type);
        builder.setLevel(nature.getCurrentId());

        HashMap<Integer, Integer> updateLevel = new HashMap<>();
        for (int i = oldId + 1; i <= nature.getCurrentId(); i++) {
            ReadIntegerArray skill = getSkillByNatureType(type, i);
            if (0 != skill.size()) {
                addOrUpdateSkill(nature, skill.get(0), skill.get(1), player);
                updateLevel.put(skill.get(0), skill.get(1));
            }
        }

        for (Map.Entry<Integer, Integer> entry : updateLevel.entrySet()) {
            NatureMessage.natureSkillInfo.Builder skillBuilder = NatureMessage.natureSkillInfo.newBuilder();
            skillBuilder.setLevel(entry.getValue());
            skillBuilder.setSkillType(entry.getKey());
            builder.addActiveSkill(skillBuilder);
        }

        for (int i = oldId + 1; i <= nature.getCurrentId(); i++) {
            int modelId = getModelIDByNatureType(type, i, player.getCareer());
            if (0 != modelId) {
                setNatureBaseModelId(player,nature, modelId, false, type);
                handleNatureBaseSkinModelId(player, modelId, nature, type);
                //handleHuaxinStarUp(nature, player, modelId, type, true);
            }
        }
        builder.addAllActiveModel(nature.getActiveNatureModelSet());
    }

    /**
     * 处理神兵升级消息
     */
   // private void handleWeaponUpLevel(Player player, int itemId, boolean isOneKeyUp, Weapon weapon, int type) {
   //     /*
   //      * itemId
   //      * 0 代表附灵， isOneKeyUp代表自动附灵
   //      * 1 代表突破
   //      * */
   //     int oldId = weapon.getCurrentId();
   //     if (0 == itemId) {
   //         handleWeaponAffiliatedSpirit(weapon, isOneKeyUp, player);
   //     } else if (1 == itemId) {
   //         handleWeaponBreakthrough(weapon, player);
   //     } else {
   //         logger.error("handleWeaponUpLevel wrong item id: " + itemId);
   //         return;
   //     }
//
   //     NatureMessage.ResNatureUpLevel.Builder builder = NatureMessage.ResNatureUpLevel.newBuilder();
   //     buildResNatureUpLevel(weapon, type, builder, oldId, player);
   //     for (WeaponAttribute attribute : weapon.getAttributes()){
   //         NatureMessage.natureWeaponsInfo.Builder infoBuilder = NatureMessage.natureWeaponsInfo.newBuilder();
   //         infoBuilder.setId(attribute.getAttributeId());
   //         infoBuilder.setValue(attribute.getValue());
   //         builder.addWeaponsInfo(infoBuilder);
   //     }
   //     builder.setFight(weapon.getPower());
//
   //     MessageUtils.send_to_player(player, NatureMessage.ResNatureUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
   // }

    /**
     * 处理神兵突破
     */
   // private void handleWeaponBreakthrough(Weapon weapon, Player player) {
   //     Cfg_NatureWeapon_Bean bean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(weapon.getCurrentId());
   //     List<WeaponAttribute> list = getNotFullWeaponAttributeList(weapon, bean);
   //     if (0 != list.size()) {
   //         //说明还有属性值没有满，不能突破
   //         logger.error("nature handleWeaponBreakthrough 属性没有升满，不能突破");
   //         return;
   //     }
   //     //查找此次突破需要消耗的道具是否满足
   //     ReadIntegerArrayEs itemList = bean.getLevel_up_cost();
   //     if (!checkItemMatch(itemList, player)) {
   //         logger.error("nature handleWeaponBreakthrough 玩家所拥有的物品和数量不匹配升级所需要的");
   //         return;
   //     }
//
   //     boolean flag = probability(10000, bean.getLevel_up_probability());
   //     //判断此次的突破是否成功，没成功，扣除50%的道具，并且返回
   //     long actionId = IDConfigUtil.getLogId();
   //     removeItemInWeapon(itemList, flag, player, true, actionId);
   //     if (!flag) {
   //         return;
   //     }
//
   //     int nextId = weapon.getCurrentId() + 1;
   //     weapon.setCurrentId(nextId);
   //     Cfg_NatureWeapon_Bean nextBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(nextId);
   //     if (null == nextBean) {
   //         logger.error("nature handleWeaponBreakthrough 没有拿到神兵配置文件  id:" + nextId);
   //         return;
   //     }
//
   //     ReadIntegerArray skill = bean.getSkill();
   //     if (null != skill) {
   //         addOrUpdateSkill(weapon, skill.get(0), skill.get(1), player);
   //     }
//
   //     int modelId = bean.getModel_ID();
   //     if (0 != modelId) {
   //         setNatureBaseModelId(weapon, modelId, false, NatureType.WEAPON);
   //     }
   //     //重设所有属性的初始值
   //     for (int i = 0; i < bean.getAtt().size(); i++) {
   //         ReadArray array = bean.getAtt().get(i);
   //         WeaponAttribute attribute = weapon.getAttributes().get(i);
   //         if (null == attribute) {
   //             continue;
   //         }
   //         attribute.setValue((int) array.get(1));
   //         attribute.setAddValue((int) array.get(3));
   //     }
//
   //     Manager.playerAttAttributeManager.deal().calPlayerAttributs(player, PlayerAttributeType.Weapon);
   //     updatePlayerAttributeByNatureType(NatureType.WEAPON, player);
//
   //     writeWeaponBreakthroughLog(nextId - 1, nextId, weapon.getAttributes().toString(),
   //             weapon.getActiveSkillMap().toString(), weapon.getHuaxins().keySet().toString(),
   //             player.getUserId(), actionId);
   // }

    /**
     * 检查道具是否足够
     * 用于神兵中
     */
    private boolean checkItemMatch(ReadIntegerArrayEs itemList, Player player) {
        for (int i = 0; i < itemList.size(); i++) {
            ReadArray array = itemList.get(i);
            int itemId = (int) array.get(0);
            int needNumber = (int) array.get(1);
            int itemTotalNumber = Manager.backpackManager.manager().getItemNum(player, itemId);
            if (itemTotalNumber < needNumber) {
                //需要保证所有道具的个数都够
                return false;
            }
        }
        return true;
    }

    /**
     * 概率比较函数
     * 用于神兵中
     */
    private boolean probability(int max, int latch) {
        int probability = RandomUtils.random(max);
        boolean flag = true;
        if (probability > latch) {
            flag = false;
        }
        return flag;
    }

    /**
     * 扣除道具
     * 用于神兵中
     */
    private void removeItemInWeapon(ReadIntegerArrayEs itemList, boolean flag, Player player, boolean isBreakthrough, long actionId) {
        for (ReadArray array : itemList.getValuees()) {
            int useNumber = flag ? (int) array.get(1) : (int) Math.ceil((int) array.get(1) / 2);
            int reason = isBreakthrough ? ItemChangeReason.WeaponUpDec : ItemChangeReason.WeaponAffiliatedSpiritDec;
            Manager.backpackManager.manager().onRemoveItem(player, (int) array.get(0), useNumber, reason, actionId);
        }
    }

    /**
     * 获取当前神兵的属性中没有满的属性列表
     */
    private List<WeaponAttribute> getNotFullWeaponAttributeList(Weapon weapon, Cfg_NatureWeapon_Bean bean) {
        List<WeaponAttribute> list = new ArrayList<>();
        //找出没有满的属性
        for (int i = 0; i < bean.getAttribute().size(); i++) {
            ReadArray array = bean.getAttribute().get(i);
            WeaponAttribute attribute = weapon.getAttributes().get(i);
            if (null == attribute) {
                continue;
            }
            if (attribute.getValue() >= (int) array.get(2)) {
                continue;
            }

            attribute.setAddValue((int) array.get(3));
            list.add(attribute);
        }
        return list;
    }

//    /**
//     * 处理神兵附灵
//     */
//    private void handleWeaponAffiliatedSpirit(Weapon weapon, boolean isOneKeyUp, Player player) {
//        if (isOneKeyUp) {
//            while (true) {
//                if (!handleOnceAffiliatedSpirit(weapon, player)) {
//                    return;
//                }
//            }
//        } else {
//            handleOnceAffiliatedSpirit(weapon, player);
//        }
//    }

//    /**
//     * 附灵一次
//     * false 表示附灵失败，要么道具不够，要么所有属性都满了
//     * true 表示附灵成功，但包括概率校验不过扣除道具
//     */
//    private boolean handleOnceAffiliatedSpirit(Weapon weapon, Player player) {
//        //随机选小于等于3个没满的属性
//        Cfg_NatureWeapon_Bean bean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(weapon.getCurrentId());
//        List<WeaponAttribute> list = getNotFullWeaponAttributeList(weapon, bean);
//        int listSize = list.size();
//        //如果list为空，说明所有属性都满了
//        if (0 == listSize) {
//            return false;
//        }
//        //查找此次附灵需要消耗的道具是否满足
//        ReadIntegerArrayEs itemList = bean.getActive_cost();
//        if (!checkItemMatch(itemList, player)) {
//            return false;
//        }
//        //概率比较结果
//        boolean flag = probability(10000, bean.getActive_probability());
//        //判断此次的附灵是否成功，没成功，扣除50%的道具，并且返回
//        long actionId = IDConfigUtil.getLogId();
//        removeItemInWeapon(itemList, flag, player, false, actionId);
//        if (!flag) {
//            MessageUtils.notify_player(player, Notify.ERROR, MessageString.WEAPON_AFFILIATED_SPIRIT_FAILED);
//            return true;
//        }
//        //判断list大小，如果小于等于3，则直接升级这3个属性，大于则随机选择list的下标
//        StringBuilder attributeBuilder = new StringBuilder();
//        if (listSize <= EACH_AFFILIATED_SPIRIT_NUMBER) {
//            list.forEach(
//                    attribute -> {
//                        attribute.addValue(attribute.getAddValue());
//                        appendAttributeStr(attributeBuilder, attribute);
//                    }
//            );
//        } else {
//            //先洗牌
//            Collections.shuffle(list);
//            //只要前三个
//            list.stream().limit(EACH_AFFILIATED_SPIRIT_NUMBER).forEach(
//                    attribute -> {
//                        attribute.addValue(attribute.getAddValue());
//                        appendAttributeStr(attributeBuilder, attribute);
//                    }
//            );
//        }
//
//        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Weapon);
//        updatePlayerAttributeByNatureType(NatureType.WEAPON, player);
//
//        writeWeaponAffiliatedSpiritLog(player.getUserId(), actionId, attributeBuilder.toString());
//        return true;
//    }

    /**
     * 处理翅膀皮肤模型变更
     */
    private void handleNatureBaseSkinModelId(Player player, int modelId, NatureBase base, int type) {
        if (type == NatureType.STIFLEFFABAO) {
            Manager.mapManager.manager().onQuitMap(Manager.mapManager.getMap(player.gainMapId()), base, true);
        }
        writeNatureModelLog(modelId, base.getCurrentModelId(), 1, type);
        base.setOwnerId(player.getId());
        base.setCurrentModelId(modelId);
        if (base.getId() == 0) {
            base.setId(IDConfigUtil.getId());
        }
        NatureMessage.ResNatureModelSet.Builder builder = NatureMessage.ResNatureModelSet.newBuilder();
        builder.setNatureType(type);
        builder.setModelId(modelId);
        MessageUtils.send_to_player(player, NatureMessage.ResNatureModelSet.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        if (type == NatureType.STIFLEFFABAO) {
            MapGpsUtil.CopyGPS(player.getCurGps(), base.getCurGps());
            Manager.mapManager.manager().onEnterMap(base);
            Manager.stateStifleManager.deal().boradFabaoInfo(player);
        } else {
            sendModelInfoToRoundPlayer(player, modelId, type, base.getId());
        }

        //时装穿戴
        int fashionType = getFashionType(type);
        if (fashionType >0){
            Manager.newFashionManager.deal().huaxingWearFashion(player,fashionType,modelId);
        }
    }

    /**
     * 处理吃果子的使用物品
     */
    private void handleNatureBaseUseDrugs(Player player, int itemId, Nature nature, int type) {
        int itemTotalNumber = Manager.backpackManager.manager().getItemNum(player, itemId);
        if (0 >= itemTotalNumber) {
            return;
        }

        ConcurrentHashMap<Integer, Drug> drugs = nature.getDrugs();
        Drug drug = drugs.get(itemId);
        //玩家身上吃果子的数量
        int leftUseNumber = drug.getUseNumber();
        int useNumber = 0;
        //根据drug的useNumber来更新level
        int level = drug.getLevel();
        int belongType = drug.getBelongType();
        int pos = drug.getPos();
        int excelId = drug.getExcelId();
        Cfg_Nature_att_Bean bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
        if (null == bean) {
            logger.error("nature handleNatureBaseUseDrugs 找不到吃药的配置文件 id:" + excelId);
            return;
        }
        int currentUseNumber = 0;
        int tempNextExcelId = caculateNatureAttExcelId(belongType, pos, level + 1);
        if (isMaxLevel(tempNextExcelId, excelId, leftUseNumber)) return;
        int levelLimit = bean.getLeve_limit();
        while (itemTotalNumber > 0) {
            int tempNextLevel = level + 1;
            tempNextExcelId = caculateNatureAttExcelId(belongType, pos, tempNextLevel);
            if (isMaxLevel(tempNextExcelId, excelId, currentUseNumber)) break;
            //不足升一级
            if (levelLimit > itemTotalNumber + leftUseNumber) {
                currentUseNumber = itemTotalNumber + leftUseNumber;
                useNumber += itemTotalNumber;
                break;
            }
            Cfg_Nature_att_Bean tempNextBean = CfgManager.getCfg_Nature_att_Container().getValueByKey(tempNextExcelId);
            //最后一阶了 只能升次数 不能升阶数
            if (tempNextBean == null) {
                int need = bean.getLeve_limit();
                if (itemTotalNumber + leftUseNumber >= need) {
                    int onceUse = need - leftUseNumber;
                    useNumber += onceUse;
                } else {
                    useNumber += itemTotalNumber;
                }
                currentUseNumber = bean.getLeve_limit();
                break;
            } else {
                itemTotalNumber -= levelLimit - leftUseNumber;
                currentUseNumber = 0;
                useNumber += levelLimit - leftUseNumber;
                level = tempNextLevel;
                excelId = tempNextExcelId;
                bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
                levelLimit = bean.getLeve_limit();
                leftUseNumber = 0;
            }
        }

        int oldLevel = drug.getLevel();
        int oldUseNumber = drug.getUseNumber();
        drug.setLevel(level);
        drug.setUseNumber(currentUseNumber);

        //坐骑御魂培养XX次，开启或完成某个其他成就。。。暂时还没有完成法宝御魂、宠物御魂的任务
        if (type == NatureType.HORSE) {
            Manager.controlManager.operate(player, FunctionVariable.HorseSoul, currentUseNumber);
        }

        //先算玩家战力提升，是为了更新玩家的系统属性，防止下面算造化某个子系统的战力的时候，玩家的系统属性没有更新
        updatePlayerAttributeByNatureType(type, player);
        /*
         * 吃药战力提升规则
         * 1、每个药品要提升属性，要根据每个等级来定提升的值，如果跨级提升，要一级一级的遍历，累加
         * 2、每升一级要增加一个百分比属性，换算成战力，替换旧值
         * */
        updatePlayerAttributeByNatureType(type, player);
        //消耗drug
        long actionId = IDConfigUtil.getLogId();
        Manager.backpackManager.manager().onRemoveItem(player, itemId, useNumber, ItemChangeReason.DrugDec, actionId);

        //组织消息并发给客户端
        NatureMessage.ResNatureDrug.Builder builder = NatureMessage.ResNatureDrug.newBuilder();
        builder.setNatureType(type);
        NatureMessage.natureDrugInfo.Builder infoBuilder = NatureMessage.natureDrugInfo.newBuilder();
        infoBuilder.setEatnum(drug.getUseNumber());
        infoBuilder.setLevel(drug.getLevel());
        infoBuilder.setFruitId(drug.getItemId());
        builder.setDruginfo(infoBuilder);
        builder.setFight(nature.getPower());
        MessageUtils.send_to_player(player, NatureMessage.ResNatureDrug.MsgID.eMsgID_VALUE, builder.build().toByteArray());
        //记录日志
        writeNatureDrugLog(drug.toString(), player.getUserId(), actionId);
        Manager.playerManager.savePlayer(player, SavePlayerLevel.OneMinLater);
//        int growType = 0;
        GrowType growType = null;
        if(type==NatureType.HORSE){
            growType = GrowType.horse_soul_up;
        }else if(type==NatureType.WING){
            growType = GrowType.wing_soul_up;
        }else if(type == NatureType.PET){
            growType = GrowType.pet_soul_up;
        }else if(type == NatureType.STIFLEFFABAO){
            //法宝御魂
            growType = GrowType.stifle_soul_up;
        }else if(type == NatureType.WEAPON){
            growType = GrowType.godWeapon_soul_up;
        }
        if(growType == null){
            return;
        }
        if(level > oldLevel){
            //升阶
            Manager.biManager.getScript().biGrow(player,  growType.getType(), bean.getType(), BIDefine.GrowStageUp, oldLevel, level, bean.getPosition());
        }
        if(oldUseNumber > drug.getUseNumber()){
            oldUseNumber = 0;
        }
        //升级
        Manager.biManager.getScript().biGrow(player,  growType.getType(), bean.getType(), BIDefine.GrowLevelUp, oldUseNumber, drug.getUseNumber(), bean.getPosition());
    }

    private boolean isMaxLevel(int tempNextExcelId, int excelId, int leftUseNumber) {
        Cfg_Nature_att_Bean tempNextBean = CfgManager.getCfg_Nature_att_Container().getValueByKey(tempNextExcelId);
        Cfg_Nature_att_Bean bean = CfgManager.getCfg_Nature_att_Container().getValueByKey(excelId);
        return tempNextBean == null && bean.getLeve_limit() <= leftUseNumber;
    }

    /**
     * 处理打开神兵面板
     */
    private void handleWeaponOpenRequest(Player player, Weapon weapon) {
        NatureMessage.ResNatureInfo.Builder builder = NatureMessage.ResNatureInfo.newBuilder();
        builder.setNatureType(NatureType.WEAPON);
        NatureMessage.natureInfo.Builder infoBuilder = NatureMessage.natureInfo.newBuilder();
        addNatureBaseInfoToBuilder(weapon, infoBuilder, NatureType.WEAPON);
        for (WeaponAttribute attribute : weapon.getAttributes()) {
            NatureMessage.natureWeaponsInfo.Builder weaponBuilder = NatureMessage.natureWeaponsInfo.newBuilder();
            weaponBuilder.setId(attribute.getAttributeId());
            weaponBuilder.setValue(attribute.getValue());
            infoBuilder.addWeaponsInfo(weaponBuilder);
        }
        builder.setNatureInfo(infoBuilder);
        MessageUtils.send_to_player(player, NatureMessage.ResNatureInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 处理打开翅膀、神器、阵法面板
     */
    private void handleNatureOpenRequest(Player player, Nature nature, int type) {
        if (nature == null) {
            return;
        }
        sendNatureInfoToClient(player, nature, type);
    }

    private void sendNatureInfoToClient(Player player, Nature nature, int type) {
        NatureMessage.ResNatureInfo.Builder builder = NatureMessage.ResNatureInfo.newBuilder();
        builder.setNatureType(type);
        NatureMessage.natureInfo.Builder infoBuilder = NatureMessage.natureInfo.newBuilder();
        infoBuilder.setCurExp(nature.getCurrentExp());
        addNatureBaseInfoToBuilder(nature, infoBuilder, type);
        addDrugsInfoIntoBuilder(nature.getDrugs(), infoBuilder);
        builder.setNatureInfo(infoBuilder);
        MessageUtils.send_to_player(player, NatureMessage.ResNatureInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 增加造化系统基础信息
     */
    private void addNatureBaseInfoToBuilder(NatureBase base, NatureMessage.natureInfo.Builder infoBuilder, int type) {
        infoBuilder.setCurLevel(base.getCurrentId());
        infoBuilder.setModelId(base.getCurrentModelId());
        for (Map.Entry<Integer, Integer> entry : base.getActiveSkillMap().entrySet()){
            NatureMessage.natureSkillInfo.Builder skillBuilder = NatureMessage.natureSkillInfo.newBuilder();
            skillBuilder.setLevel(entry.getValue());
            skillBuilder.setSkillType(entry.getKey());
            infoBuilder.addHaveActiveSkill(skillBuilder);
        }
        infoBuilder.addAllHaveActiveModel(base.getActiveNatureModelSet());
        addHuaxinsInfoIntoBuilder(base.getHuaxins(), infoBuilder, type);
        infoBuilder.setFight(base.getPower());
    }

    /**
     * 增加吃药信息
     */
    private void addDrugsInfoIntoBuilder(ConcurrentHashMap<Integer, Drug> drugs, NatureMessage.natureInfo.Builder builder) {
        for (Drug drug : drugs.values()){
            NatureMessage.natureDrugInfo.Builder drugBuilder = NatureMessage.natureDrugInfo.newBuilder();
            drugBuilder.setFruitId(drug.getItemId());
            drugBuilder.setLevel(drug.getLevel());
            drugBuilder.setEatnum(drug.getUseNumber());
            builder.addFruitInfo(drugBuilder);
        }
    }

    /**
     * 增加化形信息
     */
    private void addHuaxinsInfoIntoBuilder(ConcurrentHashMap<Integer, Huaxin> huaxins, NatureMessage.natureInfo.Builder builder, int type) {
        for (Huaxin huaxin : huaxins.values()) {
            NatureMessage.natureOutlineInfo.Builder huaxinBuilder = NatureMessage.natureOutlineInfo.newBuilder();
            huaxinBuilder.setId(huaxin.getExcelId());
            huaxinBuilder.setLevel(huaxin.getLevel());
            int power = calculateHuaxinFightPower(huaxin, type);
            huaxinBuilder.setFight(power);
            builder.addOutlineInfo(huaxinBuilder);
        }
    }

    /**
     * 获取化形的战力
     */
    private int calculateHuaxinFightPower(Huaxin huaxin, int type) {
        BaseIntAttribute attribute = new BaseIntAttribute(AttributeType.ATTR_MAX);
        final int level = huaxin.getLevel() + 1;
        ReadIntegerArrayEs arrays = getRentAttArray(huaxin, type);
        if (null != arrays) {
            for (ReadArray array : arrays.getValuees()) {
                attribute.addAttribute((int) array.get(0), (int) array.get(1) * level);
            }
        }
        return Manager.playerAttAttributeManager.deal().calcFightPower(attribute);
    }

    /**
     * 处理化形升星
     */
    @Override
    public void handleHuaxinStarUp(NatureBase base, Player player, int id, int type, boolean auto) {

        Huaxin huaxin = base.getHuaxins().get(id);
        boolean isActive = huaxin == null;
        //是否是时装
        boolean isFashion = false;
        //自动激活的翅膀不消耗材料并且不能手动激活
        boolean freeActive = false;
        if (type == NatureType.WING && isActive) {
            Cfg_HuaxingWing_Bean bean = CfgManager.getCfg_HuaxingWing_Container().getValueByKey(id);
            if (bean == null) {
                return;
            }
            if (bean.getPassive_skill() != 0) {
                Manager.skillManager.addSkill(player, bean.getPassive_skill());
            }
            if (bean.getActive_condition() == 1) {
                freeActive = true;
                if (!auto) {
                    return;
                }
            }
            isFashion = bean.getIf_fashion() == 0;
        }

        if (type == NatureType.WEAPON && isActive) {
            Cfg_HuaxingWeapon_Bean bean = CfgManager.getCfg_HuaxingWeapon_Container().getValueByKey(id);
            if (bean == null) {
                return;
            }
            if (bean.getPassive_skill() != 0) {
                Manager.skillManager.addSkill(player, bean.getPassive_skill());
            }
            if (bean.getActive_condition() == 1) {
                freeActive = true;
                if (!auto) {
                    return;
                }
            }
            isFashion = bean.getIf_fashion() == 0;
        }

        if (type == NatureType.HORSE && isActive) {
            Cfg_HuaxingHorse_Bean bean = CfgManager.getCfg_HuaxingHorse_Container().getValueByKey(id);
            if (bean == null) {
                return;
            }
            if (bean.getPassive_skill() != 0) {
                Manager.skillManager.addSkill(player, bean.getPassive_skill());
            }
            if (bean.getActive_condition() == 1) {
                freeActive = true;
                if (!auto) {
                    return;
                }
            }
            isFashion = bean.getIf_fashion() == 0;
        }

        if (type == NatureType.STIFLEFFABAO && isActive) {
            Cfg_Huaxingfabao_Bean bean = CfgManager.getCfg_Huaxingfabao_Container().getValueByKey(id);
            if (bean == null) {
                return;
            }
            if (bean.getPassive_skill() != 0) {
                Manager.skillManager.addSkill(player, bean.getPassive_skill());
            }
            if (auto) {
                freeActive = true;
            }
            isFashion = bean.getIf_fashion() == 0;
        }

        int upNeedItemNumber = 0;
        int upLevel = huaxin == null ? 0 : huaxin.getLevel();
        ReadIntegerArrayEs list = getStarItemNumArrayByType(type, id);
        if (list == null) {
            logger.info("造化配置表不存在：type:" + type + ",id:" + id);
            return;
        }
        if (upLevel >= list.size()) {
            logger.info(String.format("化形升星已经到最大等级了{%s}!!", type));
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            ReadArray array = list.get(i);
            if (upLevel == (int) array.get(0)) {
                upNeedItemNumber = (int) array.get(1);
                break;
            }
        }

        long actionId = IDConfigUtil.getLogId();
        int itemId = getActiveItemByType(type, id);
        if (!isFashion && !freeActive && !Manager.backpackManager.manager().onRemoveItem(player, itemId, upNeedItemNumber, ItemChangeReason.HuaxingDec, actionId)) {
            logger.error("物品不足");
            return;
        }

        if (huaxin == null) {
            huaxin = new Huaxin(id, 0);
            base.getHuaxins().put(id, huaxin);

            int fashionType = getFashionType(type);
            if (fashionType >0){
                Manager.newFashionManager.deal().huaxingActivateFashion(player,fashionType,id);
            }
        } else {
            huaxin.setLevel(huaxin.getLevel() + 1);
        }
        updatePlayerAttributeByNatureType(type, player);
        sendHuaxinUpResultToClient(huaxin, player, type, base);

        //法宝追击器灵激活化形条件处理
        if (type == NatureType.STIFLEFFABAO) {
            if (isActive) {
                Manager.stateStifleManager.deal().addSoulSpiritProgress(player, 3, 1, 0);
            }
        }
        writeNatureHuaxinUpLog(huaxin.getExcelId(), huaxin.getLevel(), isActive, itemId + "," + upNeedItemNumber, player.getUserId(), actionId);
        if (isActive) {
            Manager.controlManager.operate(player, FunctionVariable.State_stifleID, 0);
            Manager.controlManager.operate(player, FunctionVariable.NatureWingID, 0);
            Manager.controlManager.operate(player, FunctionVariable.NatureHorseID, 0);
        }

//        int growType = 0;
        GrowType growType = null;
        if(type == NatureType.HORSE){
            growType = isActive ? GrowType.horse_active : GrowType.horse_star_up;
        }else if(type == NatureType.WING){
            growType = isActive ? GrowType.wing_active : GrowType.wing_star_up;
        }else if(type == NatureType.STIFLEFFABAO){
            growType = isActive ? GrowType.stifle_active : GrowType.stifle_star_up;
        }else if(type == NatureType.WEAPON){
            growType = isActive ? GrowType.godWeapon_active : GrowType.godWeapon_star_up;
        }
        if(growType == null){
            return;
        }
        if(isActive){
            Manager.biManager.getScript().biGrow(player, growType.getType(), 0, growType.getAct_type(), 0, id, id);
        }else{
            Manager.biManager.getScript().biGrow(player, growType.getType(), 0, growType.getAct_type(), huaxin.getLevel() - 1, huaxin.getLevel(), id);
        }
    }

    @Override
    public void initPlayerWeapon(Player player) {
        Weapon weapon = new Weapon();
        weapon.setStatus(Weapon.status_init);
        player.setWeapon(weapon);
        initPlayerNature(player, player.getWeapon(), NatureType.WEAPON);
    }

    private void sendHuaxinUpResultToClient(Huaxin huaxin, Player player, int type, NatureBase base) {
        NatureMessage.ResNatureFashionUpLevel.Builder builder = NatureMessage.ResNatureFashionUpLevel.newBuilder();
        NatureMessage.natureOutlineInfo.Builder huaxinBuilder = NatureMessage.natureOutlineInfo.newBuilder();
        huaxinBuilder.setId(huaxin.getExcelId());
        huaxinBuilder.setLevel(huaxin.getLevel());
        huaxinBuilder.setFight(base.getPower());
        builder.setInfo(huaxinBuilder);
        builder.setNatureType(type);
        MessageUtils.send_to_player(player, NatureMessage.ResNatureFashionUpLevel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private int caculateNatureAttExcelId(int belongType, int pos, int level) {
        return belongType * 1000 + pos * 100 + level;
    }

    private int getNatureBaseMaxExcelId(int type) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                return CfgManager.getCfg_NatureHorse_Container().size();
            case NatureType.WING:
                return CfgManager.getCfg_NatureWing_Container().size();
            case NatureType.TALISMAN:
                return CfgManager.getCfg_NatureTalisman_Container().size();
            case NatureType.MAGIC:
                return CfgManager.getCfg_NatureMagic_Container().size();
            case NatureType.WEAPON:
                return CfgManager.getCfg_NatureWeapon_Container().size();
            default:
                logger.error("nature getNatureBaseMaxExcelId type: " + type + " is invalid");
                break;
        }
        return 0;
    }

    private ReadIntegerArray getUpItemArray(int type, int excelId) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道，5：神兵
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(excelId);
                if (null == horseBean) {
                    return null;
                }
                return horseBean.getUp_item();
            case NatureType.WING:
                Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(excelId);
                if (null == wingBean) {
                    return null;
                }
                return wingBean.getUp_item();
            case NatureType.TALISMAN:
                Cfg_NatureTalisman_Bean talismanBean = CfgManager.getCfg_NatureTalisman_Container().getValueByKey(excelId);
                if (null == talismanBean) {
                    return null;
                }
                return talismanBean.getUp_item();
            case NatureType.MAGIC:
                Cfg_NatureMagic_Bean arrayBean = CfgManager.getCfg_NatureMagic_Container().getValueByKey(excelId);
                if (null == arrayBean) {
                    return null;
                }
                return arrayBean.getUp_item();
            case NatureType.WEAPON:
                Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(excelId);
                if (null == weaponBean) {
                    return null;
                }
                return weaponBean.getUp_item();
            default:
                logger.error("nature getUpItemArray type: " + type + " is invalid");
                break;
        }
        return null;
    }

    private int getModelIDByNatureType(int type, int excelId, int career) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(excelId);
                return horseBean.getModelID();
            case NatureType.WING:
                Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(excelId);
                return wingBean.getModelID();
            case NatureType.TALISMAN:
                Cfg_NatureTalisman_Bean talismanBean = CfgManager.getCfg_NatureTalisman_Container().getValueByKey(excelId);
                return talismanBean.getModelID();
            case NatureType.MAGIC:
                Cfg_NatureMagic_Bean arrayBean = CfgManager.getCfg_NatureMagic_Container().getValueByKey(excelId);
                return arrayBean.getModelID();
            case NatureType.WEAPON:
                Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(excelId);
                if(weaponBean.getModelID() != null && weaponBean.getModelID().size() > career){
                    return weaponBean.getModelID().get(career);
                }
                return 0;
            case NatureType.STIFLEFFABAO:
                break;
            case NatureType.PET:
                break;
            default:
                logger.error("nature getModelIDByNatureType type: " + type + " is invalid");
                break;
        }
        return 0;
    }

    private ReadIntegerArray getSkillByNatureType(int type, int excelId) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(excelId);
                return horseBean.getSkill();
            case NatureType.WING:
                Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(excelId);
                return wingBean.getSkill();
            case NatureType.TALISMAN:
                Cfg_NatureTalisman_Bean talismanBean = CfgManager.getCfg_NatureTalisman_Container().getValueByKey(excelId);
                return talismanBean.getSkill();
            case NatureType.MAGIC:
                Cfg_NatureMagic_Bean arrayBean = CfgManager.getCfg_NatureMagic_Container().getValueByKey(excelId);
                return arrayBean.getSkill();
            case NatureType.WEAPON:
                Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(excelId);
                return weaponBean.getSkill();
            case NatureType.PET:
                break;
            default:
                logger.error("nature getSkillByNatureType type: " + type + " is invalid");
                break;
        }
        return null;
    }

    private int getProgressByNatureType(int type, int excelId) {
        switch (type) {
            //1：坐骑，2：翅膀 ，3：法器 4:阵道
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(excelId);
                return horseBean.getProgress();
            case NatureType.WING:
                Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(excelId);
                return wingBean.getProgress();
            case NatureType.TALISMAN:
                Cfg_NatureTalisman_Bean talismanBean = CfgManager.getCfg_NatureTalisman_Container().getValueByKey(excelId);
                return talismanBean.getProgress();
            case NatureType.MAGIC:
                Cfg_NatureMagic_Bean arrayBean = CfgManager.getCfg_NatureMagic_Container().getValueByKey(excelId);
                return arrayBean.getProgress();
            case NatureType.WEAPON:
                Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(excelId);
                return weaponBean.getProgress();
            default:
                logger.error("nature getProgressByNatureType type: " + type + " is invalid");
                break;
        }
        return 0;
    }

    private int getActiveItemByType(int type, int id) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                Cfg_HuaxingHorse_Bean horseBean = CfgManager.getCfg_HuaxingHorse_Container().getValueByKey(id);
                return horseBean.getActive_item();
            case NatureType.WING:
                Cfg_HuaxingWing_Bean wingBean = CfgManager.getCfg_HuaxingWing_Container().getValueByKey(id);
                return wingBean.getActive_item();
            case NatureType.TALISMAN:
                Cfg_HuaxingTalisman_Bean talismanBean = CfgManager.getCfg_HuaxingTalisman_Container().getValueByKey(id);
                return talismanBean.getActive_item();
            case NatureType.MAGIC:
                Cfg_HuaxingMagic_Bean magicBean = CfgManager.getCfg_HuaxingMagic_Container().getValueByKey(id);
                return magicBean.getActive_item();
            case NatureType.WEAPON:
                Cfg_HuaxingWeapon_Bean weaponBean = CfgManager.getCfg_HuaxingWeapon_Container().getValueByKey(id);
                return weaponBean.getActive_item();
            case NatureType.STIFLEFFABAO:
                Cfg_Huaxingfabao_Bean fabaoBean = CfgManager.getCfg_Huaxingfabao_Container().getValueByKey(id);
                return fabaoBean.getActive_item();
            default:
                logger.error("nature getActiveItemByType type: " + type + " is invalid");
                break;
        }
        return 0;
    }

    private ReadIntegerArrayEs getStarItemNumArrayByType(int type, int id) {
        switch (type) {
            //1：坐骑 2：翅膀 3：法器 4：阵道 5：神兵
            case NatureType.HORSE:
                Cfg_HuaxingHorse_Bean horseBean = CfgManager.getCfg_HuaxingHorse_Container().getValueByKey(id);
                if (null == horseBean) {
                    return null;
                }
                return horseBean.getStar_itemnum();
            case NatureType.WING:
                Cfg_HuaxingWing_Bean wingBean = CfgManager.getCfg_HuaxingWing_Container().getValueByKey(id);
                if (null == wingBean) {
                    return null;
                }
                return wingBean.getStar_itemnum();
            case NatureType.TALISMAN:
                Cfg_HuaxingTalisman_Bean talismanBean = CfgManager.getCfg_HuaxingTalisman_Container().getValueByKey(id);
                if (null == talismanBean) {
                    return null;
                }
                return talismanBean.getStar_itemnum();
            case NatureType.MAGIC:
                Cfg_HuaxingMagic_Bean magicBean = CfgManager.getCfg_HuaxingMagic_Container().getValueByKey(id);
                if (null == magicBean) {
                    return null;
                }
                return magicBean.getStar_itemnum();
            case NatureType.STIFLEFFABAO:
                Cfg_Huaxingfabao_Bean stifleffabaoBean = CfgManager.getCfg_Huaxingfabao_Container().getValueByKey(id);
                if (null == stifleffabaoBean) {
                    return null;
                }
                if (stifleffabaoBean.getType() == 1) {
                    return null;
                }
                return stifleffabaoBean.getStar_itemnum();
            case NatureType.WEAPON:
                Cfg_HuaxingWeapon_Bean weaponBean = CfgManager.getCfg_HuaxingWeapon_Container().getValueByKey(id);
                if (null == weaponBean) {
                    return null;
                }
                return weaponBean.getStar_itemnum();
            default:
                logger.error("nature getStarItemNumArrayByType type: " + type + " is invalid");
                break;
        }
        return null;
    }

    private int getLastExp(int type, int currentId) {
        switch (type) {
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(currentId);
                if (null == horseBean) {
                    return 0;
                }
                return horseBean.getLast_exp();
            case NatureType.WING:
                Cfg_NatureWing_Bean wingBean = CfgManager.getCfg_NatureWing_Container().getValueByKey(currentId);
                if (null == wingBean) {
                    return 0;
                }
                return wingBean.getLast_exp();
            case NatureType.TALISMAN:
                Cfg_NatureTalisman_Bean talismanBean = CfgManager.getCfg_NatureTalisman_Container().getValueByKey(currentId);
                if (null == talismanBean) {
                    return 0;
                }
                return talismanBean.getLast_exp();
            case NatureType.MAGIC:
                Cfg_NatureMagic_Bean magicBean = CfgManager.getCfg_NatureMagic_Container().getValueByKey(currentId);
                if (null == magicBean) {
                    return 0;
                }
                return magicBean.getLast_exp();
            case NatureType.WEAPON:
                Cfg_NatureWeapon_Bean weaponBean = CfgManager.getCfg_NatureWeapon_Container().getValueByKey(currentId);
                if (null == weaponBean) {
                    return 0;
                }
                return weaponBean.getLast_exp();
            default:
                return 0;
        }
    }

    private ReadIntegerArrayEs getRentAttArray(Huaxin huaxin, int type) {
        switch (type) {
            case NatureType.HORSE:
                Cfg_HuaxingHorse_Bean horseBean = CfgManager.getCfg_HuaxingHorse_Container().getValueByKey(huaxin.getExcelId());
                return horseBean.getRent_att();
            case NatureType.WING:
                Cfg_HuaxingWing_Bean wingBean = CfgManager.getCfg_HuaxingWing_Container().getValueByKey(huaxin.getExcelId());
                return wingBean.getRent_att();
            case NatureType.TALISMAN:
                Cfg_HuaxingTalisman_Bean talismanBean = CfgManager.getCfg_HuaxingTalisman_Container().getValueByKey(huaxin.getExcelId());
                return talismanBean.getRent_att();
            case NatureType.MAGIC:
                Cfg_HuaxingMagic_Bean magicBean = CfgManager.getCfg_HuaxingMagic_Container().getValueByKey(huaxin.getExcelId());
                return magicBean.getRent_att();
            case NatureType.WEAPON:
                Cfg_HuaxingWeapon_Bean weaponBean = CfgManager.getCfg_HuaxingWeapon_Container().getValueByKey(huaxin.getExcelId());
                return weaponBean.getRent_att();
            case NatureType.STIFLEFFABAO:
                Cfg_Huaxingfabao_Bean bean = CfgManager.getCfg_Huaxingfabao_Container().getValueByKey(huaxin.getExcelId());
                return bean.getRent_att();
            default:
                logger.error("nature getRentAttArray type:" + type + " is invalid");
                return null;
        }
    }

    private void writeNatureModelLog(int newId, int oldId, int actType, int type) {
        NatureModelLog modelLog = new NatureModelLog();
        modelLog.setActType(actType);
        modelLog.setNewId(newId);
        modelLog.setOldId(oldId);
        modelLog.setType(type);
        LogService.getInstance().execute(modelLog);
    }

    private void writeNatureUpLog(int oldLevel, int newLevel, int exp, String skill, String model, long playerId, long actionId) {
        NatureUpLog upLog = new NatureUpLog();
        upLog.setOldLevel(oldLevel);
        upLog.setNewLevel(newLevel);
        upLog.setExp(exp);
        upLog.setSkillActivated(skill);
        upLog.setModelActivated(model);
        upLog.setPlayerId(playerId);
        if (0 == actionId) {
            upLog.setActionId(IDConfigUtil.getLogId());
        } else {
            upLog.setActionId(actionId);
        }
        LogService.getInstance().execute(upLog);
    }

    private void appendAttributeStr(StringBuilder attributeBuilder, WeaponAttribute attribute) {
        attributeBuilder.append(attribute.getAttributeId());
        attributeBuilder.append(",");
        attributeBuilder.append(attribute.getValue());
        attributeBuilder.append(";");
    }

    private void appendItemStr(int itemId, int itemNumber, StringBuilder itemStr) {
        itemStr.append(itemId);
        itemStr.append(",");
        itemStr.append(itemNumber);
        itemStr.append(";");
    }

    private void writeNatureHuaxinUpLog(int id, int star, boolean active, String itemStr, long playerId, long actionId) {
        NatureHuaxinUpLog huaxinUpLog = new NatureHuaxinUpLog();
        huaxinUpLog.setPlayerId(playerId);
        huaxinUpLog.setActionId(actionId);
        huaxinUpLog.setHuanxiId(id);
        huaxinUpLog.setStar(star);
        huaxinUpLog.setType(active ? 0 : 1);
        huaxinUpLog.setItemStr(itemStr);
        LogService.getInstance().execute(huaxinUpLog);
    }

    private void writeNatureDrugLog(String info, long playerId, long actionId) {
        NatrueDrugLog drugLog = new NatrueDrugLog();
        drugLog.setPlayerId(playerId);
        drugLog.setActionId(actionId);
        drugLog.setInfo(info);
        LogService.getInstance().execute(drugLog);
    }

    private void writeWeaponBreakthroughLog(int oldLevel, int newLevel, String attribute, String skill, String model, long playerId, long actionId) {
        WeaponBreakthroughLog weaponUpLog = new WeaponBreakthroughLog();
        weaponUpLog.setOldLevel(oldLevel);
        weaponUpLog.setNewLevel(newLevel);
        weaponUpLog.setAttribute(attribute);
        weaponUpLog.setSkillActivated(skill);
        weaponUpLog.setModelActivated(model);
        weaponUpLog.setPlayerId(playerId);
        if (0 == actionId) {
            weaponUpLog.setActionId(IDConfigUtil.getLogId());
        } else {
            weaponUpLog.setActionId(actionId);
        }
        LogService.getInstance().execute(weaponUpLog);
    }

    private void writeWeaponAffiliatedSpiritLog(long playerId, long actionId, String attribute) {
        WeaponAffiliatedSpiritLog log = new WeaponAffiliatedSpiritLog();
        log.setPlayerId(playerId);
        log.setActionId(actionId);
        log.setAttribute(attribute);
        LogService.getInstance().execute(log);
    }

    private int getItemUseEffect(int itemId) {
        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(itemId);
        if (null == itemBean) {
            logger.error("nature getItemUseEffect 物品id不对  id:" + itemId);
            return 0;
        }

        ReadIntegerArrayEs effectList = itemBean.getEffect_num();
        ReadArray effectArray = effectList.get(0);
        if (4 != (int) effectArray.get(0)) {
            logger.error("nature getItemUseEffect 物品没有配置增加数值效果  id:" + itemId);
            return 0;
        }
        return (int) effectArray.get(1);
    }

    private void checkControl(Player player, int type) {
        switch (type) {
            case NatureType.WING:
                Manager.controlManager.operate(player, FunctionVariable.WingNum, 0);
                break;
            case NatureType.TALISMAN:
                Manager.controlManager.operate(player, FunctionVariable.TalismanLevel, 0);
                break;
            case NatureType.MAGIC:
                Manager.controlManager.operate(player, FunctionVariable.MagicLevel, 0);
                break;
            case NatureType.WEAPON:
                Manager.controlManager.operate(player, FunctionVariable.WeaponNum, 0);
                Manager.controlManager.operate(player, FunctionVariable.God_weapon_Strengthen_Level, 0);
                break;
            default:
                break;
        }
    }

    private int getNatureTypeFunctionId(int type) {
        int functionId = 0;
        switch (type) {
            case NatureType.HORSE:
                functionId = FunctionStart.Mount;
                break;
            case NatureType.WING:
                functionId = FunctionStart.NatureWing;
                break;
            case NatureType.TALISMAN:
                functionId = FunctionStart.NatureTalisman;
                break;
            case NatureType.MAGIC:
                functionId = FunctionStart.NatureMagic;
                break;
            case NatureType.WEAPON:
                functionId = FunctionStart.NatureWeapon;
                break;
            case NatureType.STIFLEFFABAO:
                functionId = FunctionStart.FaBaoDrug;
                break;
            case NatureType.PET:
                functionId = FunctionStart.PetProSoul;
            default:
                break;
        }
        return functionId;
    }

    private Nature getNatureByNatureType(Player player, int type) {
        Nature nature = null;
        switch (type) {
            case NatureType.HORSE:
                nature = player.getHorse().getNature();
                break;
            case NatureType.WING:
                nature = player.getWing();
                break;
            case NatureType.TALISMAN:
                nature = player.getTalisman();
                break;
            case NatureType.MAGIC:
                nature = player.getMagic();
                break;
            case NatureType.STIFLEFFABAO:
                nature = player.getStifleData().getNature();
                break;
            case NatureType.PET:
                nature = player.getActivePet().getNature();
                break;
            case NatureType.WEAPON:
                nature = player.getWeapon();
                break;
            default:
                break;
        }
        return nature;
    }

    private void updatePlayerAttributeByNatureType(int type, Player player) {
        switch (type) {
            case NatureType.HORSE:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.HORSE);
                break;
            case NatureType.WING:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.WING);
                break;
            case NatureType.TALISMAN:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Talisman);
                break;
            case NatureType.MAGIC:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Magic);
                break;
            case NatureType.WEAPON:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.Weapon);
                break;
            case NatureType.STIFLEFFABAO:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.StifleFabao);
                break;
            case NatureType.PET:
                Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.PET);
                break;
            default:
                logger.error("nature updatePlayerAttributeByNatureType wrong type: " + type);
        }
    }

    private void updateRankInfo(int type, Player player, int id) {
        switch (type) {
            case NatureType.HORSE:
                Manager.rankListManager.deal().setHorseId(player, id);
                break;
            case NatureType.WING:
                Manager.rankListManager.deal().setWingId(player, id);
                break;
            default:
                break;
        }
    }

    private void sendModelInfoToRoundPlayer(Player player, int modelId, int type, long id) {
        //非上马状态直接return
        if (type == NatureType.HORSE && player.getHorse().getRideState() == HorseRideStateEnum.UnRide) {
            return;
        }
        NatureMessage.ResNatureModelIdChange.Builder changeBuilder = NatureMessage.ResNatureModelIdChange.newBuilder();
        changeBuilder.setPlayerid(player.getId());
        changeBuilder.setModelid(modelId);
        changeBuilder.setType(type);
        changeBuilder.setNatureId(id);
        //MessageUtils.send_to_roundPlayer(player, NatureMessage.ResNatureModelIdChange.MsgID.eMsgID_VALUE, changeBuilder.build().toByteArray());
        MessageUtils.send_to_player(player.getId(), NatureMessage.ResNatureModelIdChange.MsgID.eMsgID_VALUE, changeBuilder.build().toByteArray());
    }

    private void addOrUpdateSkill(NatureBase nature, int skill, int level, Player player) {
        Integer oldLevel = nature.getActiveSkillMap().get(skill);
        if(null != oldLevel) {
            int oldSkillId = getRealSkillId(skill, oldLevel);
            Manager.skillManager.removeSkill(player, oldSkillId);
        }
        nature.getActiveSkillMap().put(skill, level);
        int newSkillId = getRealSkillId(skill, level);
        Manager.skillManager.addSkill(player, newSkillId);
    }

    private int getRealSkillId(int element_type, int level) {
        for (Cfg_Skill_Bean bean : CfgManager.getCfg_Skill_Container().getValuees()) {
            if (bean.getElement_type() == element_type && bean.getLevel() == level) {
                return bean.getId();
            }
        }
        return 0;
    }

    private void natrueGrowBI(Player player, int type, int oldId, int afterId){
        switch (type) {
            //1：坐骑 2：翅膀 5神兵
            case NatureType.HORSE:
                Cfg_NatureHorse_Bean horseBean = CfgManager.getCfg_NatureHorse_Container().getValueByKey(afterId);
                if(horseBean != null){
                    Manager.biManager.getScript().biGrow(player, GrowType.horse_level_up.getType(), 0, BIDefine.GrowLevelUp, oldId, afterId, afterId);
                }
                break;
            case NatureType.WING:
                Manager.biManager.getScript().biGrow(player, GrowType.wing_level_up.getType(), 0, BIDefine.GrowLevelUp, oldId, afterId, afterId);
                break;
            case NatureType.WEAPON:
                Manager.biManager.getScript().biGrow(player, GrowType.godWeapon_level_up.getType(), 0, BIDefine.GrowLevelUp, oldId, afterId, afterId);
                break;
        }
    }
}
