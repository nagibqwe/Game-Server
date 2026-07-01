package common.newfashion;

import com.data.*;
import com.data.bean.Cfg_Fashion_Bean;
import com.data.bean.Cfg_Fashion_link_Bean;
import com.data.bean.Cfg_Fashion_total_Bean;
import com.data.container.Cfg_Fashion_total_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.game.chat.structs.Notify;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.nature.structs.NatureType;
import com.game.newfashion.log.FashionLog;
import com.game.newfashion.manager.NewFashionManager;
import com.game.newfashion.script.INewFashionScript;
import com.game.newfashion.structs.FashionData;
import com.game.newfashion.structs.PlayerNewFashion;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.server.structs.SynToFightType;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.message.NewFashionMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 542 on 2020/3/23.
 */
public class NewFashionScript implements INewFashionScript {

    private final static Logger log = LogManager.getLogger(NewFashionScript.class);

    @Override
    public int getId() {
        return ScriptEnum.NewFasionBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }




    /**
     * 序列号时装
     *
     * @param fashionData
     * @return
     */
    public NewFashionMessage.NewFashion buildFashionData(FashionData fashionData) {
        NewFashionMessage.NewFashion.Builder newFashion = NewFashionMessage.NewFashion.newBuilder();
        newFashion.setFashionID(fashionData.getFashionID());
        newFashion.setStar(fashionData.getStar());
        newFashion.setType(fashionData.getType());
        return newFashion.build();
    }

    public void onlineInit(Player player) {
        NewFashionMessage.ResOnlineInitFashionInfo.Builder msg = NewFashionMessage.ResOnlineInitFashionInfo.newBuilder();

        //默认激活头像，头像框,气泡
        onlineWearDefault(player);

        for (FashionData fashionData : player.getNewFashionData().getActivtyFsDatas().values()) {
            msg.addActiveIds(buildFashionData(fashionData));
            if (player.getNewFashionData().getWearDatas().containsKey(fashionData.getType())) {
                FashionData wearData = player.getNewFashionData().getWearDatas().get(fashionData.getType());
                if (wearData.getFashionID() == fashionData.getFashionID()) {
                    wearData.setStar(fashionData.getStar());
                }
            }
        }
        for (FashionData fashionData : player.getNewFashionData().getWearDatas().values()) {
            msg.addWearData(buildFashionData(fashionData));
            setFashionBodyOrWeaponId(player, fashionData);
        }
        for (Map.Entry<Integer, Integer> entry : player.getNewFashionData().getActityTjDatas().entrySet()) {
            NewFashionMessage.TjData.Builder tjdata = NewFashionMessage.TjData.newBuilder();
            tjdata.setStar(entry.getValue());
            tjdata.setTjID(entry.getKey());
            msg.addActiveTjs(tjdata.build());
        }
        MessageUtils.send_to_player(player, NewFashionMessage.ResOnlineInitFashionInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void ReqActiveFashion(Player player, int id) {

        if (player.getNewFashionData().getActivtyFsDatas().containsKey(id)) {
            log.info("已激活  " + id);
            return;
        }
        Cfg_Fashion_Bean fashion_bean = CfgManager.getCfg_Fashion_Container().getValueByKey(id);
        if (fashion_bean == null) {
            log.info("Cfg_Fashion_Bean  null   " + id);
            return;
        }
        int delItemID = 0;
        for (ReadArray<Integer> readArray : fashion_bean.getActive_item().getValuees()) {
            if (readArray.get(0) == player.getCareer()) {
                delItemID = readArray.get(1);
            }
        }
        if (!Manager.backpackManager.manager().onRemoveItem(player, delItemID, 1, ItemChangeReason.ActiveFashionDec, delItemID)) {
            log.info("扣除材料失败");
            return;
        }
        Cfg_Fashion_total_Bean total_bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(id);
        checkFashion(player, total_bean);
    }

    @Override
    public void ReqActiveTj(Player player, int id) {

        Cfg_Fashion_link_Bean fashion_link_bean = CfgManager.getCfg_Fashion_link_Container().getValueByKey(id);
        if (fashion_link_bean == null) {
            log.info("fashion_link_bean  null   " + id);
            return;
        }
        if (player.getNewFashionData().getActityTjDatas().containsKey(id)) {
            log.info("已激活   " + id);
            return;
        }
        for (ReadArray<Integer> readArray : fashion_link_bean.getNeed_fashion_id().getValuees()) {
            if (!player.getNewFashionData().getActivtyFsDatas().containsKey(readArray.get(1))) {
                log.info("激活图鉴失败 请先激活 时装  " + readArray.get(1));
                return;
            }
        }
        player.getNewFashionData().getActityTjDatas().put(id, 0);

        NewFashionMessage.ResActiveTjResult.Builder msg = NewFashionMessage.ResActiveTjResult.newBuilder();
        NewFashionMessage.TjData.Builder tjdata = NewFashionMessage.TjData.newBuilder();
        tjdata.setTjID(id);
        tjdata.setStar(0);
        msg.setTjData(tjdata.build());
        MessageUtils.send_to_player(player, NewFashionMessage.ResActiveTjResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.NewFashion);
        RoleGrowLog.create(player, GrowType.fashion_tj_active, 0, id, 0, id, null);
    }

    @Override
    public void ReqFashionStar(Player player, int id) {

        if (!player.getNewFashionData().getActivtyFsDatas().containsKey(id)) {
            log.info("未激活  " + id);
            return;
        }
        Cfg_Fashion_Bean fashion_bean = CfgManager.getCfg_Fashion_Container().getValueByKey(id);
        if (fashion_bean == null) {
            log.info("Cfg_Fashion_Bean  null   " + id);
            return;
        }

        FashionData fashionData = player.getNewFashionData().getActivtyFsDatas().get(id);
        int curStar = fashionData.getStar();
        if (curStar >= fashion_bean.getStar_itemnum().size()) {
            log.info("已达到最大星级   " + curStar);
            return;
        }

        int needItemId = 0;
        for (ReadArray<Integer> readArray : fashion_bean.getActive_item().getValuees()) {
            if (readArray.get(0) == player.getCareer()) {
                needItemId = readArray.get(1);
            }
        }
        ReadArray<Integer> needItemArr = fashion_bean.getStar_itemnum().get(curStar);
        int needItemNum = needItemArr.get(1);

        if (!Manager.backpackManager.manager().onRemoveItem(player, needItemId, needItemNum, ItemChangeReason.FashionStarUpDec, needItemId)) {
            log.info("扣除材料失败");
            return;
        }
        fashionData.setStar(curStar + 1);

        if (player.getNewFashionData().getWearDatas().containsKey(fashionData.getType())) {
            FashionData wearData = player.getNewFashionData().getWearDatas().get(fashionData.getType());
            if (wearData.getFashionID() == fashionData.getFashionID()) {
                wearData.setStar(fashionData.getStar());
            }
        }
        NewFashionMessage.ResFashionStarResult.Builder msg = NewFashionMessage.ResFashionStarResult.newBuilder();
        msg.setRetData(buildFashionData(fashionData));
        MessageUtils.send_to_player(player, NewFashionMessage.ResFashionStarResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.NewFashion);

        GrowType growType = null;
        if(fashion_bean.getType() == 1){
            growType = GrowType.fashion_body_starUp;
        }else if(fashion_bean.getType() == 2){
            growType = GrowType.fashion_weapon_starUp;
        }else if(fashion_bean.getType() == 4){
            growType = GrowType.fashion_horse_starUp;
        }else if(fashion_bean.getType() == 5){
            growType = GrowType.fashion_pet_starUp;
        }else if(fashion_bean.getType() == 6){
            growType = GrowType.fashion_stifle_starUp;
        }else if(fashion_bean.getType() == 11){
            growType = GrowType.fashion_head_starUp;
        }else if(fashion_bean.getType() == 12){
            growType = GrowType.fashion_headFrame_starUp;
        }else if(fashion_bean.getType() == 13){
            growType = GrowType.fashion_chat_starUp;
        }
        if(growType != null){
            RoleGrowLog.create(player, growType, fashion_bean.getType(), id, curStar, curStar + 1, null);
        }
    }

    @Override
    public void ReqTjStar(Player player, int id) {

        if (!player.getNewFashionData().getActityTjDatas().containsKey(id)) {
            log.info("未激活图鉴   " + id);
            return;
        }
        Cfg_Fashion_link_Bean fashion_link_bean = CfgManager.getCfg_Fashion_link_Container().getValueByKey(id);
        if (fashion_link_bean == null) {
            log.info("fashion_link_bean  null   " + id);
            return;
        }
        int minStar = 999;
        for (ReadArray<Integer> readArray : fashion_link_bean.getNeed_fashion_id().getValuees()) {
            FashionData fashionData = player.getNewFashionData().getActivtyFsDatas().get(readArray.get(1));
            if (fashionData.getStar() < minStar) {
                minStar = fashionData.getStar();
            }
        }

        int tjStar = player.getNewFashionData().getActityTjDatas().get(id);

        if (tjStar >= minStar) {
            log.info("图鉴已升到最高等级   " + tjStar);
            return;
        }
        player.getNewFashionData().getActityTjDatas().put(id, tjStar + 1);

        NewFashionMessage.ResTjStarResult.Builder msg = NewFashionMessage.ResTjStarResult.newBuilder();
        NewFashionMessage.TjData.Builder tjdata = NewFashionMessage.TjData.newBuilder();
        tjdata.setTjID(id);
        tjdata.setStar(tjStar + 1);
        msg.setTjData(tjdata.build());
        MessageUtils.send_to_player(player, NewFashionMessage.ResTjStarResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.NewFashion);

        RoleGrowLog.create(player,GrowType.fashion_tj_starUp, 0, id, tjStar, tjStar + 1, null);
    }

    private boolean checkIsCandress(Player player, int id) {
        if (id > 0) {
            Cfg_Fashion_total_Bean bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(id);
            if (bean == null) {
                log.error("Cfg_Fashion_total_Bean配置表中未找到id=" + id);
                return false;
            }
            if (!player.getNewFashionData().getActivtyFsDatas().containsKey(id)) {
                MessageUtils.notify_player(player, Notify.ERROR, MessageString.QISPRITE_WEIJIHUO);
                return false;
            }
        }
        return true;
    }

    public void activeNewFashion(Player player, int type, int condition) {
        Cfg_Fashion_total_Bean[] cfg_fashion_beans = Cfg_Fashion_total_Container.GetInstance().getValuees();
        for (Cfg_Fashion_total_Bean cfgFashionBean : cfg_fashion_beans) {
            ReadIntegerArray array = cfgFashionBean.getVariable_ID();
            if (array == null || array.size() <= 0)
                continue;
            if (array.get(0) != type)
                continue;
            int index = player.getCareer() + 1;
            int condid = array.get(index);

            if (type == FunctionVariable.ReceiveFirstRechargeReward) {
                if (condition < condid)
                    continue;
            } else if (condid != condition) {
                continue;
            }
            checkFashion(player, cfgFashionBean);
        }
    }

    public void addFashionID(Player player, int id) {

        Cfg_Fashion_total_Bean bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(id);
        if (bean == null) {
            log.error("Cfg_Fashion_total_Bean配置表中未找到id=" + id);
            return;
        }
        checkFashion(player, bean);
    }

    private void checkFashion(Player player, Cfg_Fashion_total_Bean cfgFashionBean) {
        PlayerNewFashion playerFashion = player.getNewFashionData();
        if (playerFashion.getActivtyFsDatas().containsKey(cfgFashionBean.getId()))
            return;

        FashionData fashionData = createFashionData(cfgFashionBean.getId());
        if (fashionData == null) {
            return;
        }
        playerFashion.getActivtyFsDatas().put(fashionData.getFashionID(), fashionData);

        //学习技能
        needLearnSkill(player, cfgFashionBean.getId());

        Cfg_Fashion_total_Bean curBean = null;
        boolean isBroadcast = false;
        FashionData curData = null;
        if (!playerFashion.getWearDatas().containsKey(fashionData.getType())) {
            isBroadcast = true;
            playerFashion.getWearDatas().put(fashionData.getType(), fashionData);
        } else {
            curData = playerFashion.getWearDatas().get(fashionData.getType());
            curBean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(curData.getFashionID());
            if (cfgFashionBean.getOrder() < curBean.getOrder()) {
                playerFashion.getWearDatas().put(fashionData.getType(), fashionData);
                isBroadcast = true;
            }
        }
        if (isBroadcast)
            ResNewFashionBodyBroadcast(player);
        ResSaveFashionResult(player, true, fashionData);
        activateOrWearHuaxing(player, fashionData, true, isBroadcast);
        writeLog(player, cfgFashionBean.getId(), 0);
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.NewFashion);

        GrowType growType = null;
        if(cfgFashionBean.getType() == 1){
            growType = GrowType.fashion_body_active;
        }else if(cfgFashionBean.getType() == 2){
            growType = GrowType.fashion_weapon_active;
        }else if(cfgFashionBean.getType() == 4){
            growType = GrowType.fashion_horse_active;
        }else if(cfgFashionBean.getType() == 5){
            growType = GrowType.fashion_pet_active;
        }else if(cfgFashionBean.getType() == 6){
            growType = GrowType.fashion_stifle_active;
        }else if(cfgFashionBean.getType() == 11){
            growType = GrowType.fashion_head_active;
        }else if(cfgFashionBean.getType() == 12){
            growType = GrowType.fashion_headFrame_active;
        }else if(cfgFashionBean.getType() == 13){
            growType = GrowType.fashion_chat_active;
        }
        if(growType != null){
            RoleGrowLog.create(player, growType, cfgFashionBean.getType(), cfgFashionBean.getId(), 0, cfgFashionBean.getId(), null);
        }
    }


    /**
     * 化形激活时装
     */
    public void huaxingActivateFashion(Player player, int type, int huaxingID) {
        int fashionID = getFashionID(type, huaxingID);
        Cfg_Fashion_total_Bean bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(fashionID);
        if (bean == null) {
            log.error("Cfg_Fashion_total_Bean  null  " + fashionID);
            return;
        }
        PlayerNewFashion playerFashion = player.getNewFashionData();
        if (playerFashion.getActivtyFsDatas().containsKey(fashionID)) {
            log.error("已激活    " + fashionID);
            return;
        }

        FashionData fashionData = createFashionData(bean.getId());
        if (fashionData == null) {
            return;
        }
        playerFashion.getActivtyFsDatas().put(fashionData.getFashionID(), fashionData);

        ResSaveFashionResult(player, true, fashionData);
        writeLog(player, fashionID, 0);
    }


    /**
     * 化形穿戴时装
     *
     * @param player
     * @param type
     * @param huaxingID
     */
    public void huaxingWearFashion(Player player, int type, int huaxingID) {

        int fashionID = getFashionID(type, huaxingID);
        Cfg_Fashion_total_Bean bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(fashionID);
        if (bean == null) {
            log.error("Cfg_Fashion_total_Bean  null  " + fashionID);
            return;
        }
        PlayerNewFashion playerFashion = player.getNewFashionData();
        if (!playerFashion.getActivtyFsDatas().containsKey(fashionID)) {
            log.error("未激活    " + fashionID);
            return;
        }
        FashionData weardata = player.getNewFashionData().getWearDatas().get(bean.getType());
        if (weardata != null) {
            if (weardata.getFashionID() == bean.getId()) {
                return;
            }
        }
        FashionData changeData = player.getNewFashionData().getActivtyFsDatas().get(fashionID);
        player.getNewFashionData().getWearDatas().put(changeData.getType(), changeData);
        ResNewFashionBodyBroadcast(player);
        ResSaveFashionResult(player, false, null);

    }

    /**
     * 穿戴保存时装
     *
     * @param player
     * @param fashionList
     * @param
     */
    public void ReqSaveFashion(Player player, List<Integer> fashionList) {

        for (Integer id : fashionList) {
            if (!checkIsCandress(player, id)) {
                return;
            }
        }
        for (Integer id : fashionList) {
            FashionData changeData = player.getNewFashionData().getActivtyFsDatas().get(id);
            FashionData wearData = player.getNewFashionData().getWearDatas().get(changeData.getType());
            if (wearData != null) {
                if (wearData.getFashionID() == changeData.getFashionID()) {
                    log.info("时装已穿戴 " + id);
                    continue;
                }
            }
            player.getNewFashionData().getWearDatas().put(changeData.getType(), changeData);
            activateOrWearHuaxing(player, changeData, false, true);
        }
        ResNewFashionBodyBroadcast(player);
        ResSaveFashionResult(player, false, null);

    }

    /**
     * 广播其他人
     */
    private void ResNewFashionBodyBroadcast(Player player) {
        NewFashionMessage.ResNewFashionBodyBroadcast.Builder msg = NewFashionMessage.ResNewFashionBodyBroadcast.newBuilder();
        msg.setPlayerId(player.getId());
        for (FashionData fashionData : player.getNewFashionData().getWearDatas().values()) {
            if (fashionData.getType() ==  NewFashionManager.BODY_TYPE
                    || fashionData.getType() ==  NewFashionManager.WEAPON_TYPE
                    ||  fashionData.getType() ==  NewFashionManager.WING_TYPE) {
                msg.addRetData(buildFashionData(fashionData));
            }
        }
        if (player.playerCrossData.isToFightServer()) {
            Map<Integer, Object> map = new HashMap<>();
            map.put(1, player.getNewFashionData().getBodyID());
            map.put(2, player.getNewFashionData().getWeaponID());
            Manager.playerManager.managerExt().noticeSynRoleInfoToFight(player, SynToFightType.FashionChange, map, NewFashionMessage.ResNewFashionBodyBroadcast.MsgID.eMsgID_VALUE, msg.build().toByteString());
        } else {
            MessageUtils.send_to_roundPlayer(player, NewFashionMessage.ResNewFashionBodyBroadcast.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        }
    }


    private void ResSaveFashionResult(Player player, boolean isActivate, FashionData activefashionData) {
        NewFashionMessage.ResSaveFashionResult.Builder msg = NewFashionMessage.ResSaveFashionResult.newBuilder();
        for (FashionData fashionData : player.getNewFashionData().getWearDatas().values()) {
            msg.addRetData(buildFashionData(fashionData));
            setFashionBodyOrWeaponId(player, fashionData);
        }
        msg.setIsActivate(isActivate);
        if (isActivate) {
            msg.setActivateID(buildFashionData(activefashionData));
        }
        MessageUtils.send_to_player(player, NewFashionMessage.ResSaveFashionResult.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        Manager.teamManager.playerOnLine(player);
    }

    private void setFashionBodyOrWeaponId(Player player, FashionData fashionData) {
        if (fashionData.getType() == NewFashionManager.BODY_TYPE) {
            player.getNewFashionData().setBodyID(fashionData.getFashionID());
        } else if (fashionData.getType() == NewFashionManager.WEAPON_TYPE) {
            player.getNewFashionData().setWeaponID(fashionData.getFashionID());
        }else if (fashionData.getType() == NewFashionManager.WING_TYPE){
            player.getNewFashionData().setWingId(fashionData.getFashionID());
        }
    }

    public void gmSetFashionID(Player player, int id) {
        Cfg_Fashion_total_Bean bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }

        checkFashion(player, bean);
    }

    public void clearAllFshionID(Player player) {
        player.getNewFashionData().getActivtyFsDatas().clear();
        player.getNewFashionData().getWearDatas().clear();
        player.getNewFashionData().getActityTjDatas().clear();
        player.getNewFashionData().setBodyID(0);
        player.getNewFashionData().setWeaponID(0);
        player.getNewFashionData().setWingId(0);
        onlineInit(player);
    }

    /**
     * 时装日志
     *
     * @param player
     * @param fashionID
     * @param actType   0激活
     */
    public void writeLog(Player player, int fashionID, int actType) {
        FashionLog fashionLog = new FashionLog();
        fashionLog.setPlayerInfo(player.getPlatformName(), player.getCreateServerId(), player.getUserId(), player.getId(), player.getName());
        fashionLog.setFashionId(fashionID);
        fashionLog.setActType(actType);
        LogService.getInstance().execute(fashionLog);
    }


    private int getHuaXinID(int type1, int type2, int id) {
        int huaxinID = id - (type1 * 100000000 + type2 * 10000000);
        return huaxinID;
    }


    /**
     * 激活或穿戴化形
     */
    private void activateOrWearHuaxing(Player player, FashionData fashionData, boolean isActive, boolean isWear) {

        Cfg_Fashion_total_Bean total_bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(fashionData.getFashionID());
        int huaxinId = getHuaXinID(total_bean.getType(), total_bean.getType_2(), fashionData.getFashionID());
        switch (fashionData.getType()) {
            case NewFashionManager.WING_TYPE:
            case NewFashionManager.HORSE_TYPE:
            case NewFashionManager.WEAPON_TYPE:
            case NewFashionManager.FABAO_TYPE:
                int type = getHuaxingType(fashionData.getType());
                if (isActive) {
                    Manager.natureManager.deal().onReqNatureFashionUpLevel(player, type, huaxinId);
                }
                if (isWear) {
                    Manager.natureManager.deal().onReqNatureModelSet(player, type, huaxinId);
                }
                break;
            case NewFashionManager.PET_TYPE:
                if (isActive) {
                    Manager.petManager.deal().petAction(player, 1, huaxinId, true);
                }
                if (isWear) {
                    Manager.petManager.deal().petAction(player, 3, huaxinId, true);
                }
                break;

            case NewFashionManager.SOUL_TYPE:
                Manager.soulArmorManager.script().wearSoulArmor(player,huaxinId);
                break;
        }
    }

    private int getHuaxingType(int actType) {

        switch (actType) {
            case NewFashionManager.WING_TYPE:
                return NatureType.WING;
            case NewFashionManager.HORSE_TYPE:
                return NatureType.HORSE;
            case NewFashionManager.FABAO_TYPE:
                return NatureType.STIFLEFFABAO;
            case NewFashionManager.WEAPON_TYPE:
                return NatureType.WEAPON;
        }
        return -1;
    }

    private FashionData createFashionData(int id) {
        Cfg_Fashion_total_Bean fashion_total_bean = CfgManager.getCfg_Fashion_total_Container().getValueByKey(id);
        if (fashion_total_bean == null) {
            log.error("fashion_total_bean  == null" + id);
            return null;
        }
        FashionData fashionData = new FashionData();
        fashionData.setFashionID(id);
        fashionData.setType(fashion_total_bean.getType());
        fashionData.setStar(0);

        return fashionData;
    }

    private void needLearnSkill(Player player, int id) {

        Cfg_Fashion_Bean bean = CfgManager.getCfg_Fashion_Container().getValueByKey(id);
        if (bean == null) {
            return;
        }
        if (bean.getType() == NewFashionManager.BODY_TYPE || bean.getType() == NewFashionManager.WEAPON_TYPE) {
            if (bean.getPassive_skill() > 0) {
                Manager.skillManager.addSkill(player, bean.getPassive_skill());
            }
        }
    }


    public int getFashionID(int type1, int huaxinID) {
        //化形时装类型统一为1
        int fashionID = type1 * 100000000 + 1 * 10000000 + huaxinID;
        return fashionID;
    }

    private void onlineWearDefault(Player player) {
        //默认激活头像
        activateDefault(player, NewFashionManager.HEAD_TYPE, Global.Initial_Decorate.get(0));
        //默认激活头像框
        activateDefault(player, NewFashionManager.HEAD_FRAME_TYPE, Global.Initial_Decorate.get(1));
        //默认激活气泡
        activateDefault(player, NewFashionManager.BUBBLE_TYPE, Global.Initial_Decorate.get(2));
    }

    private void activateDefault(Player player, int type, int id) {
        if (!player.getNewFashionData().getWearDatas().containsKey(type)) {
            FashionData fashionData = createFashionData(id);
            if (fashionData != null) {
                player.getNewFashionData().getActivtyFsDatas().put(fashionData.getFashionID(), fashionData);
                player.getNewFashionData().getWearDatas().put(fashionData.getType(), fashionData);
            }
        }
    }


    /**
     * 获取头像
     *
     * @param player
     */
    public int getHead(Player player) {
        FashionData fashionData = player.getNewFashionData().getWearDatas().get(11);
        if (fashionData == null) {
            return 0;
        }
        return fashionData.getFashionID();
    }

    /**
     * 获取头像框
     *
     * @param player
     */
    public int getHeadBox(Player player) {

        FashionData fashionData = player.getNewFashionData().getWearDatas().get(12);
        if (fashionData == null) {
            return 0;
        }
        return fashionData.getFashionID();
    }

    /**
     * 获取气泡
     *
     * @param player
     */
    public int getQiPao(Player player) {
        FashionData fashionData = player.getNewFashionData().getWearDatas().get(13);
        if (fashionData == null) {
            return 0;
        }
        return fashionData.getFashionID();
    }


}
