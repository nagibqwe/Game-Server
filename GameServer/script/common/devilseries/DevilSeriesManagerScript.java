package common.devilseries;

import com.data.*;
import com.data.bean.*;
import com.data.container.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Equip;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.devilseries.log.DevilCardLog;
import com.game.devilseries.scripts.IDevilSeriesManagerScript;
import com.game.devilseries.structs.*;
import com.game.log.db.RoleGrowLog;
import com.game.log.grow.GrowType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.player.structs.SavePlayerLevel;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.dblog.LogService;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.core.util.WeightCalc;
import game.message.DevilSeriesMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/5/6 18:02
 */
public class DevilSeriesManagerScript implements IDevilSeriesManagerScript {

    private static final Logger log = LogManager.getLogger(DevilSeriesManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.DevilSeriesScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void activeDevilCard(Player player){
        Devil devil = player.getDevil();
        boolean active = false;
        //初始化魔魂阵营九零 一起玩 www.901   75.com
        for(Cfg_Cross_devil_card_Camp_Bean camp_cfg : Cfg_Cross_devil_card_Camp_Container.GetInstance().getValuees()){
            DevilCamp camp = devil.getCamps().get(camp_cfg.getId());
            if(camp == null){
                camp = new DevilCamp();
                camp.setId(camp_cfg.getId());
                devil.getCamps().put(camp_cfg.getId(), camp);
            }
            if(!camp.isActive()){
                if (Manager.controlManager.deal().checkFuncProgress(player, camp_cfg.getCondition())) {
                    camp.setActive(true);
                    active = true;
                }
            }
        }
        //初始化魔魂
        for(Cfg_Cross_devil_card_Main_Bean card_cfg : Cfg_Cross_devil_card_Main_Container.GetInstance().getValuees()){
            DevilCamp camp = devil.getCamps().get(card_cfg.getCamp());
            if(camp != null){
                DevilCard devilCard = camp.getCards().get(card_cfg.getId());
                if(devilCard == null){
                    devilCard = new DevilCard();
                    devilCard.setId(card_cfg.getId());
                    camp.getCards().put(card_cfg.getId(), devilCard);
                }
                if(!devilCard.isActive()){
                    if (Manager.controlManager.deal().checkFuncProgress(player, card_cfg.getCondition())) {
                        devilCard.setActive(true);
                        active = true;
                        //部件
                        Cfg_Cross_devil_card_Camp_Bean camp_cfg = Cfg_Cross_devil_card_Camp_Container.GetInstance().getValueByKey(card_cfg.getCamp());
                        if(camp_cfg == null){
                            log.error("玩家{} 魔魂{}阵营配置不存在", player.getId(), card_cfg.getId());
                        }else{
                            //装备部位初始化
                            for(int partId : camp_cfg.getParts_list().get(0).getValue()){
                                DevilEquipPart part = new DevilEquipPart();
                                part.setId(partId);
                                devilCard.getParts().put(partId, part);
                            }
                        }
                    }
                }
            }else{
                log.error("玩家：{} 初始化魔魂{}错误，没有找到阵营{}",player.getId(), card_cfg.getId(), card_cfg.getCamp());
            }
        }

        if(active){
            //魔魂信息
            sendAllDevilCardInfo(player);
        }
    }

    @Override
    public void online(Player player){
        //魔魂信息
        sendAllDevilCardInfo(player);
        //魔魂 封魔台 抽奖信息
        sendDevilHuntInfo(player);
        //魔魂背包
        DevilSeriesMessage.ResDevilEquipBagInfos.Builder bagInfo = DevilSeriesMessage.ResDevilEquipBagInfos.newBuilder();
        player.getDevilPackItems().values().forEach(item -> bagInfo.addItemInfoList(Manager.backpackManager.manager().buildItemInfo(item).build()));
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilEquipBagInfos.MsgID.eMsgID_VALUE, bagInfo.build().toByteArray());
    }

    @Override
    public void devilCardUp(Player player, int campId, int cardId, int type) {
        log.info("玩家：{}, 请求提升魔魂，阵营{}，魔魂{}", player.getId(), campId, cardId);
        DevilCamp camp = player.getDevil().getCamps().get(campId);
        if(camp == null || !camp.isActive()){
            log.error("玩家{} 阵营{}未激活", player.getId(), camp.getId());
            return;
        }
        DevilCard card = camp.getCards().get(cardId);
        if(card == null || !card.isActive()){
            log.error("玩家{} 魔魂{}未激活", player.getId(), card.getId());
            return;
        }
        /*真实的提升类型 1升级 2升阶 3解锁*/
        int relType = 0;
        int currentKey = camp.getId()*100000+card.getRank()*10000+card.getId()*1000+card.getLevel();
        Cfg_Cross_devil_card_Train_Bean currentCfg = Cfg_Cross_devil_card_Train_Container.GetInstance().getValueByKey(currentKey);
        if(currentCfg == null){
            log.error("玩家{} 魔魂提升{}配置不存在", player.getId(), currentKey);
            return;
        }
        if(card.getRank() == 0 && card.getLevel() == 0){
            //解锁
            relType = 3;
        }else{
            //升级
            int nextKey = camp.getId()*100000+card.getRank()*10000+card.getId()*1000+(card.getLevel() + 1);
            Cfg_Cross_devil_card_Train_Bean nextCfg = Cfg_Cross_devil_card_Train_Container.GetInstance().getValueByKey(nextKey);
            if(nextCfg != null){
                relType = 1;
            }else{
                //升阶
                nextKey = camp.getId()*100000 + (card.getRank() + 1)*10000 + card.getId()*1000 + 1;
                nextCfg = Cfg_Cross_devil_card_Train_Container.GetInstance().getValueByKey(nextKey);
                if(nextCfg == null){
                    log.error("玩家{} 魔魂提升{}配置不存在", player.getId(), nextKey);
                    return;
                }
                relType = 2;
            }
        }
        int itemChangeReason = ItemChangeReason.DevilCardUnlockCost;
        if(relType == 1){
            itemChangeReason = ItemChangeReason.DevilCardLevelUpCost;
        }else if(relType == 2){
            itemChangeReason = ItemChangeReason.DevilCardRankUpCost;
        }
        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, currentCfg.getCondition(), IDConfigUtil.getLogId(), itemChangeReason)) {
            log.error("玩家{} 魔魂提升{}失败，道具不足", player.getId(), currentKey);
            return;
        }
        if(relType == 2){//升阶
            card.setLevel(1);
            card.setRank(card.getRank() + 1);
        }else{
            card.setLevel(card.getLevel() + 1);
        }

        log.info("玩家：{}, 提升魔魂成功，阵营{}，魔魂{}，type:{}", player.getId(), campId, cardId, relType);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.DevilSeries);
        //升级成功返回消息
        DevilSeriesMessage.ResDevilCardUp.Builder res = DevilSeriesMessage.ResDevilCardUp.newBuilder();
        res.setLevel(card.getLevel());
        res.setRank(card.getRank());
        res.setCardId(card.getId());
        res.setCampId(camp.getId());
        res.setType(relType);
        MessageUtils.send_to_player(player,DevilSeriesMessage.ResDevilCardUp.MsgID.eMsgID_VALUE, res.build().toByteArray());

        //同步战斗力
        sendFightPoint(player, campId, cardId, card.getFightPoint());

        if(relType == 3){//解锁
            addDevilCardLog(player, campId, cardId, relType, 1);
            RoleGrowLog.create(player, GrowType.devil_card_unlock, campId, cardId, 0, cardId, null);

            //发送公告
            Cfg_Cross_devil_card_Main_Bean card_main_bean = null;
            for(Cfg_Cross_devil_card_Main_Bean bean : Cfg_Cross_devil_card_Main_Container.GetInstance().getValuees()){
                if(bean.getId() == cardId){
                    card_main_bean = bean;
                }
            }
            if(card_main_bean != null){
                if (card_main_bean.getNotice() != 0 || card_main_bean.getChatchannel() != null) {
                    MessageUtils.notify_allOnlinePlayer(card_main_bean.getNotice() , card_main_bean.getChatchannel(), MessageString.CROSS_DEVIL_NOTICE1,
                            player.getId()+"", player.getName(), card_main_bean.getName(),
                            Utils.makeUrlStr(MessageString.CROSS_DEVIL_NOTICE1));
                }
            }
        }else if(relType == 1){//升级
            addDevilCardLog(player, campId, cardId, relType, card.getLevel());
            RoleGrowLog.create(player, GrowType.devil_card_levelUp, campId, cardId, card.getLevel() - 1, card.getLevel(), null);
        }else if(relType == 2){//升阶
            addDevilCardLog(player, campId, cardId, relType, card.getRank());
            RoleGrowLog.create(player, GrowType.devil_card_stageUp, campId, cardId, card.getRank() - 1, card.getRank(), null);
        }
    }

    @Override
    public void devilCardBreak(Player player, int campId, int cardId, List<Long> equipIds) {
        log.info("玩家：{}, 请求魔魂突破，阵营{}，魔魂{}", player.getId(), campId, cardId);
        DevilCamp camp = player.getDevil().getCamps().get(campId);
        if(camp == null || !camp.isActive()){
            log.error("玩家{} 阵营{}未激活", player.getId(), camp.getId());
            return;
        }
        DevilCard card = camp.getCards().get(cardId);
        if(card == null || !card.isActive()){
            log.error("玩家{} 魔魂{}未激活", player.getId(), card.getId());
            return;
        }
        int nextBreakLevel = card.getBreak_level() + 1;
        int key = card.getId() * 1000 + nextBreakLevel;
        Cfg_Cross_devil_card_Break_Bean breakCfg = Cfg_Cross_devil_card_Break_Container.GetInstance().getValueByKey(key);
        if(breakCfg == null){
            log.error("玩家{} 魔魂突破{}配置不存在", player.getId(), key);
            return;
        }
//        if (!Manager.backpackManager.manager().removeItemOrCurrencies(player, breakCfg.getCondition(), IDConfigUtil.getLogId(), ItemChangeReason.DevilCardBreakCost)) {
//            return;
//        }
        //检测装备是否足够
        List<Equip> equips = new ArrayList<>(4);
        for(Long id : equipIds){
            if(id != null){
                Item item = player.getDevilPackItems().get(id);
                if(item instanceof Equip){
                    equips.add((Equip)item);
                }
            }
        }
        List<Integer> conditions = new ArrayList<>(4);
        if(breakCfg.getCondition() != null) {
            for (ReadArray<Integer> obj : breakCfg.getCondition().getValuees()) {
                if (obj != null) {
                    for (int i = 0; i < obj.get(1); i++) {
                        conditions.add(obj.get(0));
                    }
                }
            }
        }
        for(Equip e : equips){
            conditions.remove(Integer.valueOf(e.getItemModelId()));
        }
        if(conditions.size() != 0){
            log.error("玩家{} 魔魂突破{} 装备不足", player.getId(), key);
            return;
        }

        //删除物品
        for(Equip e : equips){
            removeItem(player, e, ItemChangeReason.DevilCardBreakCost, IDConfigUtil.getLogId());
        }

        //突破等级
        card.setBreak_level(nextBreakLevel);
        //技能
        if(breakCfg.getSkill() != null && breakCfg.getSkill().size() > 0){
            for(int skillId : breakCfg.getSkill().getValue()){
                Manager.skillManager.addSkill(player, skillId);
            }
        }
        log.info("玩家：{}, 魔魂突破成功，阵营{}，魔魂{}", player.getId(), campId, cardId);
        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.DevilSeries);
        //返回消息
        DevilSeriesMessage.ResDevilCardBreak.Builder res = DevilSeriesMessage.ResDevilCardBreak.newBuilder();
        res.setCampId(campId);
        res.setCardId(cardId);
        res.setBreakId(breakCfg.getId());
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilCardBreak.MsgID.eMsgID_VALUE, res.build().toByteArray());

        //同步战斗力
        sendFightPoint(player, campId, cardId, card.getFightPoint());

        addDevilCardLog(player, campId, cardId, 4, nextBreakLevel);
        RoleGrowLog.create(player, GrowType.devil_card_evolution, campId, cardId, nextBreakLevel - 1, nextBreakLevel, null);
    }

    /**
     * 同步战斗力到客户端
     * @param campId
     * @param cardId
     * @param fightPoint
     */
    private void sendFightPoint(Player player,int campId, int cardId, int fightPoint) {
        DevilSeriesMessage.ResDevilCardFightPoint.Builder res = DevilSeriesMessage.ResDevilCardFightPoint.newBuilder();
        res.setFightPoint(fightPoint);
        res.setCampId(campId);
        res.setCardId(cardId);
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilCardFightPoint.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 移除道具
     * @param player 玩家
     * @param item 道具
     */
    private void removeItem(Player player, Item item, int reason, long action) {
        Item removed = player.getDevilPackItems().remove(item.getId());
        if(removed != null){
            Manager.backpackManager.manager().writeItemLogAndBI(player, removed.getNum(), 0, item, reason, action);
            DevilSeriesMessage.ResDevilEquipDelete.Builder resEquipDel = DevilSeriesMessage.ResDevilEquipDelete.newBuilder();
            resEquipDel.setItemId(item.getId());
            resEquipDel.setReason(reason);
            MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilEquipDelete.MsgID.eMsgID_VALUE, resEquipDel.build().toByteArray());
        }else{
            log.error("魔魂装备 玩家：{} 尝试删除不存在的道具：{},{}", player.getId(), item.getId(), item.getItemModelId());
        }
    }

    /**
     * 添加日志
     * @param player 玩家
     * @param campId 阵营
     * @param cardId 魔魂
     * @param type 类型 1升级 2升阶 3解锁 4突破
     * @param nextLevel 操作后等级
     */
    private void addDevilCardLog(Player player, int campId, int cardId, int type, int nextLevel) {
        DevilCardLog log = new DevilCardLog();
        log.setRoleId(player.getId());
        log.setCampId(campId);
        log.setCardId(cardId);
        log.setType(type);
        log.setAfterLevel(nextLevel);
        log.setPlayer(player);
        LogService.getInstance().execute(log);
    }

    @Override
    public void devilEquipWear(Player player, int campId, int cardId, int cellId, long equipId) {
        DevilCamp camp = player.getDevil().getCamps().get(campId);
        if(camp == null || !camp.isActive()){
            log.error("玩家{} 魔魂阵营{}未激活", player.getId(), campId);
            return;
        }
        DevilCard card = camp.getCards().get(cardId);
        if(card == null || !card.isActive()){
            log.error("玩家{} 魔魂{}未激活", player.getId(), cardId);
            return;
        }
        Item item = player.getDevilPackItems().get(equipId);
        if(item == null || !(item instanceof Equip)){
            log.error("玩家{} 装备{}不存在", player.getId(), equipId);
            return;
        }
        Cfg_Equip_Bean equip_cfg = Cfg_Equip_Container.GetInstance().getValueByKey(item.getItemModelId());
        if(equip_cfg == null){
            log.error("玩家{} 装备{}配置不存在", player.getId(), item.getItemModelId());
            return;
        }
        //判断装备阵营是否一致
        int part = equip_cfg.getPart();
        Cfg_Cross_devil_card_Camp_Bean camp_cfg = Cfg_Cross_devil_card_Camp_Container.GetInstance().getValueByKey(campId);
        if(camp_cfg == null){
            log.error("玩家{} 魔魂阵营{}配置不存在", player.getId(), campId);
            return;
        }
        //判断装备阵营
        if(camp_cfg.getParts_list() == null || !camp_cfg.getParts_list().get(0).contains(part)){
            log.error("玩家{} 装备阵营{}不匹配", player.getId(), campId);
            return;
        }

        long action = IDConfigUtil.getLogId();

        //装备穿戴
        player.getDevilPackItems().remove(equipId);
        DevilEquipPart devilPart = card.getParts().get(part);
        Equip oldEquip = devilPart.getEquip();
        devilPart.setEquip((Equip)item);

        //玩家属性变更
        Manager.playerAttAttributeManager.deal().calcAttribute(player, PlayerAttributeType.DevilSeries);

        DevilSeriesMessage.ResDevilEquipWear.Builder resEquipWear = DevilSeriesMessage.ResDevilEquipWear.newBuilder();
        resEquipWear.setCampId(campId);
        resEquipWear.setCardId(cardId);
        resEquipWear.setCellId(cellId);
        resEquipWear.setEquipId(equipId);
        resEquipWear.setEquipModelId(item.getItemModelId());
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilEquipWear.MsgID.eMsgID_VALUE, resEquipWear.build().toByteArray());

        if(oldEquip != null){
            addDevilEquip(player, oldEquip, ItemChangeReason.DevilEquipWearGet, action);
        }

        //同步战斗力
        sendFightPoint(player, campId, cardId, card.getFightPoint());

        RoleGrowLog.create(player, GrowType.devil_equip_wear, equip_cfg,0, 0);
    }

    @Override
    public boolean addDevilEquip(Player player, Item item, int reason, long action) {
        try{
            //道具检测
            if(item == null){
                log.error("传入的道具为空！");
                return false;
            }
            Cfg_Equip_Bean cfg = CfgManager.getCfg_Equip_Container().getValueByKey(item.getItemModelId());
            if (cfg == null) {
                log.error("道具的模板数据找不到！");
                return false;
            }
            //自动分解
//            if(){
//
//            }
            //保存道具
            player.getDevilPackItems().put(item.getId(), item);
            DevilSeriesMessage.ResDevilEquipAdd.Builder resEquipAdd = DevilSeriesMessage.ResDevilEquipAdd.newBuilder();
            resEquipAdd.setItemInfo(Manager.backpackManager.manager().buildItemInfo(item).build());
            resEquipAdd.setReason(reason);
            MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilEquipAdd.MsgID.eMsgID_VALUE, resEquipAdd.build().toByteArray());
            //记录日志
            if(reason != ItemChangeReason.DevilEquipWearGet){
                Manager.backpackManager.manager().writeItemLogAndBI(player,0, item.getNum(), item, reason, action);
            }
            Manager.playerManager.savePlayer(player, SavePlayerLevel.FiveMinLater);
            return true;
        }catch(Exception e){
            log.error("添加魔魂装备异常，玩家{} 物品{} 原因码{} 行为id{}",player.getId(), item.getItemModelId(), reason, action);
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public boolean canAdd(Player player, int needDevilSoulGridNum) {
        return needDevilSoulGridNum + player.getDevilPackItems().size() <= 200;
    }

    @Override
    public void devilEquipSynthesis(Player player, int modelId, List<Long> equipIds) {
        Cfg_Equip_Bean equip_cfg = Cfg_Equip_Container.GetInstance().getValueByKey(modelId);
        if(equip_cfg == null){
            log.error("玩家{} 魔魂装备合成 装备{}配置不存在", player.getId(), modelId);
            return;
        }
        //装备类型 是否为魔魂装备
        Cfg_Cross_devil_card_Camp_Bean camp_cfg = getDevilEquipCamp(equip_cfg);
        if(camp_cfg == null){
            log.error("玩家{} 魔魂装备合成 装备没有找到对应的阵营,req:{}", player.getId(), modelId);
            return;
        }
        if(equipIds.size() != 3){
            log.error("玩家{} 魔魂装备合成 材料装备数量不足,req:{}", player.getId(), equipIds);
            return;
        }
        List<Item> items = new ArrayList<>(3);
        for(Long equipId : equipIds){
            Item item = player.getDevilPackItems().get(equipId);
            if(item == null){
                log.error("玩家{} 魔魂装备合成 材料装备没有找到,req:{}", player.getId(), equipId);
                return;
            }
            Cfg_Equip_Bean c = Cfg_Equip_Container.GetInstance().getValueByKey(item.getItemModelId());
            if(c == null){
                log.error("玩家{} 魔魂装备合成 材料装备配置不存在{}", player.getId(), item.getItemModelId());
                return;
            }
            Cfg_Cross_devil_card_Camp_Bean camp = getDevilEquipCamp(c);
            if(camp == null || camp.getId() != camp_cfg.getId()){
                log.error("玩家{} 魔魂装备合成 材料装备阵营错误{}", player.getId(), item.getItemModelId());
                return;
            }
            items.add(item);
        }
        long action = IDConfigUtil.getLogId();
        //执行合成操作----------
        //扣除材料装备
        for(Item item : items){
            removeItem(player, item, ItemChangeReason.DevilEquipSynthesisCost, action);
        }
        //添加装备
        Item newEquip = Item.createItem(modelId, 1, true);
        addDevilEquip(player, newEquip, ItemChangeReason.DevilEquipSynthesisGet, action);

        DevilSeriesMessage.ResDevilEquipSynthesis.Builder res = DevilSeriesMessage.ResDevilEquipSynthesis.newBuilder();
        res.setSuccess(true);
        res.setNewEquip(Manager.backpackManager.manager().buildItemInfo(newEquip).build());
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilEquipSynthesis.MsgID.eMsgID_VALUE, res.build().toByteArray());
    }

    /**
     * 获取装备对应的阵营
     * @param equip 装备
     * @return 阵营
     */
    private Cfg_Cross_devil_card_Camp_Bean getDevilEquipCamp(Cfg_Equip_Bean equip) {
        if(equip == null){
            return null;
        }
        Cfg_Cross_devil_card_Camp_Bean camp_cfg = null;
        for(Cfg_Cross_devil_card_Camp_Bean cfg : Cfg_Cross_devil_card_Camp_Container.GetInstance().getValuees()){
            if(cfg.getParts_list().get(0).contains(equip.getPart())){
                camp_cfg = cfg;
                break;
            }
        }
        return camp_cfg;
    }

    /**
     * 发送所有魔魂信息
     * @param player 玩家
     */
    private void sendAllDevilCardInfo(Player player) {
        Devil devil = player.getDevil();
        DevilSeriesMessage.ResDevilCardList.Builder builder = DevilSeriesMessage.ResDevilCardList.newBuilder();
        builder.setAutoSet(devil.isAutoSet());
        builder.setAutocolor(devil.getAutoColor());
        builder.setAutoStar(devil.getAutoStar());
        for(DevilCamp camp : devil.getCamps().values()){
            builder.addCamp(camp.toProto());
        }
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilCardList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 计算单次奖励
     * @param player
     * @param itemsRewards
     * @param addHotValue
     */
    private  void calcCfg_Cross_devil_hunt_Pool_List(Player player,List<Item> itemsRewards,int addHotValue,int huntType){
        //热度值列表
        Cfg_Cross_devil_hunt_Hot_Bean[] cfg_Cross_devil_hunt_Hot_BeanList = Cfg_Cross_devil_hunt_Hot_Container.GetInstance().getValuees();
        Cfg_Cross_devil_hunt_Hot_Bean cfg_Cross_devil_hunt_Hot_Bean = null;
        //根据玩家当前热度值 筛选出 热度等级
        if(cfg_Cross_devil_hunt_Hot_BeanList!= null && cfg_Cross_devil_hunt_Hot_BeanList.length>0){
            for(int j = 0;j < cfg_Cross_devil_hunt_Hot_BeanList.length;j++){
                cfg_Cross_devil_hunt_Hot_Bean = cfg_Cross_devil_hunt_Hot_BeanList[j];
                ReadIntegerArray hotArray = cfg_Cross_devil_hunt_Hot_Bean.getHot();
                if(player.getDevil().getDevilHotValue() >= hotArray.get(0) && player.getDevil().getDevilHotValue()<= hotArray.get(1)){
                    break;
                }
            }
        }
        WeightCalc<Integer>  weightCalc = new WeightCalc<>();
        //根据条件筛选对应的 奖池
        Map<Integer,Cfg_Cross_devil_hunt_Pool_Bean> newCfg_Cross_devil_hunt_Pool_List = new HashMap<>();
        //奖池
        Cfg_Cross_devil_hunt_Pool_Bean[] cfg_Cross_devil_hunt_Pool_List =  Cfg_Cross_devil_hunt_Pool_Container.GetInstance().getValuees();
        Cfg_Cross_devil_hunt_Pool_Bean cfg_Cross_devil_hunt_Pool_Bean = null;
        //根据热度值
        if(cfg_Cross_devil_hunt_Pool_List != null && cfg_Cross_devil_hunt_Pool_List.length>0){
            for(int m = 0; m<cfg_Cross_devil_hunt_Pool_List.length ;m++){
                cfg_Cross_devil_hunt_Pool_Bean = cfg_Cross_devil_hunt_Pool_List[m];
                //根据热度等级 筛选对应奖池
                if(cfg_Cross_devil_hunt_Pool_Bean.getHot_Limit() == cfg_Cross_devil_hunt_Hot_Bean.getId()){
                    newCfg_Cross_devil_hunt_Pool_List.put(cfg_Cross_devil_hunt_Pool_Bean.getId(),cfg_Cross_devil_hunt_Pool_Bean);
                    weightCalc.addObject(cfg_Cross_devil_hunt_Pool_Bean.getId(),this.getProbability(cfg_Cross_devil_hunt_Pool_Bean,huntType));
                }
                //公共奖池
                if(cfg_Cross_devil_hunt_Pool_Bean.getHot_Limit() == -1){
                    newCfg_Cross_devil_hunt_Pool_List.put(cfg_Cross_devil_hunt_Pool_Bean.getId(),cfg_Cross_devil_hunt_Pool_Bean);
                    weightCalc.addObject(cfg_Cross_devil_hunt_Pool_Bean.getId(),this.getProbability(cfg_Cross_devil_hunt_Pool_Bean,huntType));
                }
            }
        }
        //单次抽
        int exId =  weightCalc.getObject();
        Cfg_Cross_devil_hunt_Pool_Bean cfg_Cross_devil_hunt_Pool_Bean_Rewards = newCfg_Cross_devil_hunt_Pool_List.get(exId);
        itemsRewards.addAll(Item.createItems(player.getCareer(), cfg_Cross_devil_hunt_Pool_Bean_Rewards.getReward(), 1));
        //刷新热度值
        player.getDevil().setDevilHotValue(player.getDevil().getDevilHotValue() + addHotValue);

    }

    /**
     * 获取权重
     * @param cfg_Cross_devil_hunt_Pool_Bean
     * @param huntType 抽奖类型
     * @return
     */
    public int getProbability(Cfg_Cross_devil_hunt_Pool_Bean cfg_Cross_devil_hunt_Pool_Bean,int huntType){
        if(huntType == 1){ //高级
            return cfg_Cross_devil_hunt_Pool_Bean.getProbability();
        }else if(huntType == 2){ //中级
            return cfg_Cross_devil_hunt_Pool_Bean.getProbabilityMiddle();
        }else if(huntType == 3){ //低级
            return cfg_Cross_devil_hunt_Pool_Bean.getProbabilityLow();
        }
        return 0;
    }

    /**
     * 抽奖
     * @param player 玩家
     * @param huntType 抽奖类型
     * @param consecutiveType 抽奖类型 1 单次 抽奖 2 十连抽
     */
    public void onReqDevilHuntHandler(Player player,int huntType,int consecutiveType){
        //抽奖配置
        Cfg_Cross_devil_hunt_Pop_Bean cfg_Cross_devil_hunt_Pop_Bean = Cfg_Cross_devil_hunt_Pop_Container.GetInstance().getValueByKey(huntType);
        if(cfg_Cross_devil_hunt_Pop_Bean == null){
            return;
        }
        ReadIntegerArrayEs times = cfg_Cross_devil_hunt_Pop_Bean.getTimes();
        Map<Integer, ReadArray> timeMap = new HashMap<>();
        for(int i = 0;i<times.size();i++){
            ReadArray time =  times.get(i);
            timeMap.put((int)time.get(0),time);
        }
        ReadArray time = null;
        int rate = 0;
        //单次抽奖
        if(consecutiveType == 1){
            time = timeMap.get(1);

        }else if(consecutiveType == 2){
            time = timeMap.get(10);

        }
        int modelId = (int)time.get(1);
        int num = (int)time.get(2);
        //判断材料够不
        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, IDConfigUtil.getLogId(), ItemChangeReason.KaoShangLingHorseBuySpecailDec)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }
        //奖励物品
        List<Item> itemsRewards = new ArrayList<>();
        //单次抽奖
        if(consecutiveType == 1){
            calcCfg_Cross_devil_hunt_Pool_List(player,itemsRewards,cfg_Cross_devil_hunt_Pop_Bean.getRate(),huntType);
        }else if(consecutiveType == 2){
            //10连抽
            for(int i = 0;i<10;i++){
                calcCfg_Cross_devil_hunt_Pool_List(player,itemsRewards,cfg_Cross_devil_hunt_Pop_Bean.getRate(),huntType);
            }
        }
        int itemChangeReasonGet = 0;
        //原因码
        if(cfg_Cross_devil_hunt_Pop_Bean.getId() == 1){
            itemChangeReasonGet = ItemChangeReason.DevilHunt1Get;
        }else if(cfg_Cross_devil_hunt_Pop_Bean.getId() == 2){
            itemChangeReasonGet = ItemChangeReason.DevilHunt2Get;
        }else if(cfg_Cross_devil_hunt_Pop_Bean.getId() == 3){
            itemChangeReasonGet = ItemChangeReason.DevilHunt3Get;
        }

        //奖励放入背包
        if (! Manager.backpackManager.manager().addItems(player, itemsRewards, itemChangeReasonGet, IDConfigUtil.getLogId())){
                Manager.mailManager.sendMailToPlayer(player.getId(),
                        MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, itemsRewards, itemChangeReasonGet);
         }
        DevilSeriesMessage.ResDevilHunt.Builder res = DevilSeriesMessage.ResDevilHunt.newBuilder();
        res.setHuntType(huntType);
        res.setConsecutiveType(consecutiveType);
        res.setDevilHotValue(player.getDevil().getDevilHotValue());
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilHunt.MsgID.eMsgID_VALUE, res.build().toByteArray());

        Manager.controlManager.operate(player, FunctionVariable.Daily_DevilHunt_Times, (int)time.get(0));
        Manager.countManager.addVariant(player, VariantType.Daily_DevilHunt_Times, (int)time.get(0));
    }

    public void onReqDevilHuntPanelHandler(Player player){
        this.sendDevilHuntInfo(player);
    }
    /**
     * 发送抽奖初始化信息
     * @param player 玩家
     */
    private void sendDevilHuntInfo(Player player) {
        Devil devil = player.getDevil();
        DevilSeriesMessage.ResDevilHuntPanel.Builder builder = DevilSeriesMessage.ResDevilHuntPanel.newBuilder();
        builder.setDevilHotValue(devil.getDevilHotValue());
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResDevilHuntPanel.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }
    //------------------------除魔团接口 Start-------------------


    /**
     * 请求关注
     * @param player
     */
    @Override
    public void onReqFollowDeviBoss(Player player ,Integer cloneId,boolean followValue) {


        if ( CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValueByKey(cloneId) == null){
            log.info("关注的副本不存在 {}" ,cloneId);
            return;
        }
        List<Integer> folowList =  player.getDevil().getFollowDevilCopyList();
        if (followValue){
            if (!folowList.contains(cloneId)){
                folowList.add(cloneId);
                sendFollowResult(player,cloneId,followValue,0);
            }else {
                sendFollowResult(player,cloneId,followValue,2);
                log.info("已关注该副本 {}" ,cloneId);
                return;
            }
        }else {
            if (folowList.contains(cloneId)){
                sendFollowResult(player,cloneId,followValue,0);
                folowList.remove((Object)cloneId);
            }else {
                sendFollowResult(player,cloneId,followValue,2);
                log.info("取关失败 该副本没有关注 {}" ,cloneId);
                return;
            }
        }
    }
    private void sendFollowResult(Player player,Integer cloneId,boolean followValue ,Integer state) {
        DevilSeriesMessage.ResFollowDeviBoss.Builder msg =  DevilSeriesMessage.ResFollowDeviBoss.newBuilder();
        msg.setCloneId(cloneId);
        msg.setFollowValue(followValue);
        msg.setState(state);
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResFollowDeviBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求打开除魔团面板
     * @param player
     */
    @Override
    public void onReqOpenDeviBossPanel(Player player) {
        ConcurrentHashMap<Long, DevilCopyData>  DevilCopyMap =  Manager.devilSeriesManager.getDevilOpenCopyDataMap();
        List<Integer>  flollowList =  player.getDevil().getFollowDevilCopyList();
        DevilSeriesMessage.ResOpenDeviBossPanel.Builder msg =  DevilSeriesMessage.ResOpenDeviBossPanel.newBuilder();
        DevilSeriesMessage.DeviBossState.Builder builder;
        for (Cfg_Cross_devil_Group_Copy_Bean bean : CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValuees()){
            builder = DevilSeriesMessage.DeviBossState.newBuilder();
            if (flollowList.contains(bean.getId())){
                builder.setFollowValue(true);
            }else {
                builder.setFollowValue(false);
            }
            builder.setCloneId(bean.getId());
            for (Map.Entry<Long, DevilCopyData> entry : DevilCopyMap.entrySet()){
                DevilCopyData devilCopyData =  entry.getValue();
                if (devilCopyData.getCloneId() != bean.getId()){
                    continue;
                }
                DevilSeriesMessage.FollowData.Builder builder1 =  DevilSeriesMessage.FollowData.newBuilder();
                builder1.setHeadName(devilCopyData.getHeadName());
                builder1.setMapId(devilCopyData.getMapId());
                builder1.setEndTime(devilCopyData.getEndTime());
                builder1.setCareer(devilCopyData.getCareer());
//                builder1.setFashionHead(devilCopyData.getFashionHead());
//                builder1.setFashionFrame(devilCopyData.getFashionFrame());
                builder1.setRoleId(devilCopyData.getRoleId());

                builder1.setHead(MapUtils.getHead(devilCopyData.getFashionHead(),devilCopyData.getFashionFrame(),devilCopyData.getCustomHeadPath(),devilCopyData.isUseCustomHead()));

                builder.addFollowDataList(builder1.build());

            }
            msg.addDeviBossList(builder.build());
        }
        MessageUtils.send_to_player(player, DevilSeriesMessage.ResOpenDeviBossPanel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 请求开启除魔团
     */
    @Override
    public void onReqCreateDeviBossMap(Player player, Integer cloneId) {

        Cfg_Cross_devil_Group_Copy_Bean bean = CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValueByKey(cloneId);
        if (bean==null){
            log.error("Cfg_Cross_devil_Group_Copy_Bean  == null {}",cloneId);
            return;
        }

        int itemId = bean.getOpen_Item().get(0);
        int needNum = bean.getOpen_Item().get(1);

        if (! Manager.backpackManager.manager().canDeleteItemNum(player,itemId,needNum)){
            log.error("材料不足开启伏魔团失败 itemId： {}  needNum : {}",itemId,needNum);
            return;
        }
        if ( !Manager.backpackManager.manager().onRemoveItem(player,itemId,needNum,ItemChangeReason.DevilCopyMapCost, IDConfigUtil.getLogId())){
            log.error("扣除材料失败itemId:{}  needNum : {}",itemId,needNum);
            return;
        }

        MapObject map = Manager.mapManager.createCopyMap(cloneId, 1, player.getId(), player.getName());
        if (map ==null){
            log.error("创建地图失败 : {}",cloneId);
            return;
        }
        Manager.mapManager.changeMap(player, map.getId(), map.getBriths().get(0), false);

    }

    /**
     * 进入除魔团
     * @param player
     * @param mapId
     */
    @Override
    public void onReqEnterDeviBossMap(Player player, long mapId) {

        MapObject map =  Manager.mapManager.getMap(mapId);
        if (map == null){
            log.error("地图不存在 : {}",mapId);
            return;
        }
        if (map.isStop()){
            log.error("地图已结束 : {}",mapId);
            return;
        }
        DevilCopyData devilCopyData =  Manager.devilSeriesManager.getDevilOpenCopyDataMap().get(map.getId());
        long curTime = TimeUtils.Time();
        if ( devilCopyData.getEndTime() - curTime < 60000){
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Cross_devil_Group_Copy_timeless_notice);
            return;
        }
        Manager.mapManager.changeMap(player, map.getId(), map.getBriths().get(0), false);
    }

    /**
     * 请求积分面板
     * @param player
     */
    @Override
    public void onReqSynDeviBossIntegral(Player player) {

        MapObject map =  Manager.mapManager.getMap( player.getCurGps().getMapId());
        if (map == null){
            log.error("地图不存在 : {}",player.getCurGps().getMapId());
            return;
        }
        List<Devilintegral> devilintegrals  = Manager.devilSeriesManager.getIntegralsRank().get(player.getCurGps().getMapId());
        if (devilintegrals == null){
            log.error("地图积分不存在 : {}",player.getCurGps().getMapId());
            return;
        }
        DevilSeriesMessage.ResSynDeviBossIntegral.Builder msg = DevilSeriesMessage.ResSynDeviBossIntegral.newBuilder();
        DevilSeriesMessage.DeviBossIntegral.Builder builder;
        //生成前50名的消息数据结构
        int rank = 1;
        int selfInter = 0;
        int selfRank = 0;
        for (Devilintegral devilintegral1 :devilintegrals){
            if (rank > Manager.devilSeriesManager.MaxRank){
                break;
            }
            if (devilintegral1.getRoleId() == player.getId()){
                selfRank = rank;
                selfInter = devilintegral1.getIntegral();
            }
            builder = DevilSeriesMessage.DeviBossIntegral.newBuilder();
            builder.setIntergral(devilintegral1.getIntegral());
            builder.setRank(rank);
            builder.setName(devilintegral1.getName());
            msg.addIntegralList(builder.build());
            rank++;
        }
        msg.setSelfRank(selfRank);
        msg.setSelfIntergral(selfInter);
        MessageUtils.send_to_player(player,  DevilSeriesMessage.ResSynDeviBossIntegral.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    public void followDefaultDeviCopy(Player player){
        if (player.getLevel() == Global.Cross_devil_Group_Attention_Open_Level){
            List<Integer> folowList =  player.getDevil().getFollowDevilCopyList();
            for (Cfg_Cross_devil_Group_Copy_Bean bean : CfgManager.getCfg_Cross_devil_Group_Copy_Container().getValuees()){
                if (!folowList.contains(bean.getId())) {
                    folowList.add(bean.getId());
                }
            }
        }
    }

    public int compareIntegral(Devilintegral i1,Devilintegral i2){
        if (i2.getIntegral() != i1.getIntegral()) {
            return i2.getIntegral() > i1.getIntegral() ? 1 : -1;
        }
        else if (i2.getIntegral() == i1.getIntegral()){
            return i2.getIntegralTime() > i1.getIntegralTime() ? -1 : 1;
        }
        return 0;
    }
    //------------------------除魔团接口 end-------------------
}
