package common.home;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.Notify;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.count.structs.VariantType;
import com.game.friend.struct.Relation;
import com.game.home.event.SceneChangeEvent;
import com.game.home.script.IHomeScript;
import com.game.home.structs.Home;
import com.game.home.structs.HomeTask;
import com.data.struct.HomeTaskType;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.server.social.SocialServerClient;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.HomeMessage;
import game.message.ZoneMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @Desc TODO
 * @Date 2021/6/28 20:45
 * @Auth ZUncle
 */
public class HomeScript implements IHomeScript {

    Logger logger = LogManager.getLogger(HomeScript.class);

    //家园地图
    final int HouseCloneId = 65001;

    final int TaskUnOver = 0;   //未完成
    final int TaskReward = 1;   //未领奖
    final int TaskOver = 2;     //完成

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.HomeScript;
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

    @Override
    public void reqAuthHomePemHandler(Player player, HomeMessage.ReqAuthHomePem messInfo) {
        HomeMessage.G2SAuthHomePem.Builder message = HomeMessage.G2SAuthHomePem.newBuilder();
        message.setHelper(messInfo.getHelper());
        message.setAuthUnFriendEnter(messInfo.getAuthUnFriendEnter());
        message.setAuthUnFriendOpt(messInfo.getAuthUnFriendOpt());
        MessageUtils.send_to_social(player, HomeMessage.G2SAuthHomePem.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqEnterHomeHandler(Player player, HomeMessage.ReqEnterHome messInfo) {

        Relation friendRelation = Manager.friendManager.getFriendRelation(player, messInfo.getRoleId());

        HomeMessage.G2SEnterHome.Builder message = HomeMessage.G2SEnterHome.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        message.setFriend(friendRelation.equals(Relation.RelationType_Friend));
        MessageUtils.send_to_social(player, HomeMessage.G2SEnterHome.MsgID.eMsgID_VALUE, message.build().toByteArray());

        if (player.getId() == messInfo.getRoleId()) {
            sendTask(player);
        }
    }

    @Override
    public void reqHomeInfoHandler(Player player, HomeMessage.ReqHomeInfo messInfo) {

        long variant = Manager.countManager.getVariant(player, VariantType.HouseTup);

        HomeMessage.G2SHomeInfo.Builder message = HomeMessage.G2SHomeInfo.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        message.setTupReward(variant == 0);
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeTrimMatchScoreHandler(Player player, HomeMessage.ReqHomeTrimMatchScore messInfo) {
        HomeMessage.G2SHomeTrimMatchScore.Builder message = HomeMessage.G2SHomeTrimMatchScore.newBuilder();
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeTrimMatchScore.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeTrimRankHandler(Player player, HomeMessage.ReqHomeTrimRank messInfo) {
        HomeMessage.G2SHomeTrimRank.Builder message = HomeMessage.G2SHomeTrimRank.newBuilder();
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeTrimRank.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeTrimVoteHandler(Player player, HomeMessage.ReqHomeTrimVote messInfo) {
        HomeMessage.G2SHomeTrimVote.Builder message = HomeMessage.G2SHomeTrimVote.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        message.setType(messInfo.getType());
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeTrimVote.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeVisitorGiftListHandler(Player player, HomeMessage.ReqHomeVisitorGiftList messInfo) {
        HomeMessage.G2SHomeVisitorGiftList.Builder message = HomeMessage.G2SHomeVisitorGiftList.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeVisitorGiftList.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeVisitorNoteHandler(Player player, HomeMessage.ReqHomeVisitorNote messInfo) {
        HomeMessage.G2SHomeVisitorNote.Builder message = HomeMessage.G2SHomeVisitorNote.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeVisitorNote.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqRandomHomeTrimTargetHandler(Player player, HomeMessage.ReqRandomHomeTrimTarget messInfo) {
        HomeMessage.G2SRandomHomeTrimTarget.Builder message = HomeMessage.G2SRandomHomeTrimTarget.newBuilder();
        MessageUtils.send_to_social(player, HomeMessage.G2SRandomHomeTrimTarget.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqSendVisitorGiftHandler(Player player, HomeMessage.ReqSendVisitorGift messInfo) {
        if (messInfo.getRoleId() == player.getId()) {
            return;
        }
        //TODO 检查送礼消耗
        Cfg_Social_house_gift_Bean bean = CfgManager.getCfg_Social_house_gift_Container().getValueByKey(messInfo.getGiftId());
        if (bean == null) {
            return;
        }
        int costItem = bean.getType().get(0);
        int costCount = bean.getType().get(1);

        if (!Manager.backpackManager.manager().onRemoveItem(player, costItem, costCount, ItemChangeReason.HouseGiftCost, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(costItem));
            return;
        }

        Relation friendRelation = Manager.friendManager.getFriendRelation(player, messInfo.getRoleId());

        HomeMessage.G2SSendVisitorGift.Builder message = HomeMessage.G2SSendVisitorGift.newBuilder();
        message.setRoleId(messInfo.getRoleId());
        message.setGiftId(messInfo.getGiftId());
        message.setFriend(friendRelation.equals(Relation.RelationType_Friend));
        MessageUtils.send_to_social(player, HomeMessage.G2SSendVisitorGift.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeDecorate(Player player, HomeMessage.ReqHomeDecorate messInfo) {

        HomeMessage.Vector3.Builder pos = HomeMessage.Vector3.newBuilder();
        pos.setX(messInfo.getX());
        pos.setY(messInfo.getY());
        pos.setZ(messInfo.getZ());

        HomeMessage.FurnitureCell.Builder furniture = HomeMessage.FurnitureCell.newBuilder();
        furniture.setId(messInfo.getId());
        furniture.setModelId(messInfo.getModelId());
        furniture.setDir(messInfo.getDir());
        furniture.setPos(pos);

        HomeMessage.G2SHomeDecorate.Builder message = HomeMessage.G2SHomeDecorate.newBuilder();
        message.setType(messInfo.getType());
        message.setTargetId(messInfo.getTargetId());
        message.setFurniture(furniture);

        MessageUtils.send_to_social(player, HomeMessage.G2SHomeDecorate.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void reqHomeLevelUp(Player player, HomeMessage.ReqHomeLevelUp messInfo) {

        Home home = player.getHome();

        Cfg_Social_house_Bean bean = CfgManager.getCfg_Social_house_Container().getValueByKey(home.getLevel());
        if (bean == null) {
            return;
        }
        Cfg_Social_house_Bean next = CfgManager.getCfg_Social_house_Container().getValueByKey(home.getLevel() + 1);
        if (next == null) {
            return;
        }

        int costItem = bean.getLevelup_pay().get(0);
        int costCount = bean.getLevelup_pay().get(1);

        if (!Manager.backpackManager.manager().onRemoveItem(player, costItem, costCount, ItemChangeReason.HouseLevelUpCost, IDConfigUtil.getLogId())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.Item_Not_Enough, Manager.backpackManager.manager().getName(costItem));
            return;
        }
        home.setLevel(next.getId());
        HomeMessage.G2SHomeLevelUp.Builder message = HomeMessage.G2SHomeLevelUp.newBuilder();
        message.setCurLevel(home.getLevel());
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeLevelUp.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    @Override
    public void S2GEnterHome(Player player, HomeMessage.S2GEnterHome messInfo) {

        if (player.playerCrossData.toFightServer) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CopyIngNotOperate);
            return;
        }

        ZoneMessage.cloneTeamInfo.Builder info = ZoneMessage.cloneTeamInfo.newBuilder();
        info.setCarear(player.getCareer());
        info.setLeader(messInfo.getRoomId() == player.getId());
        info.setReady(true);
        info.setRoleId(player.getId());
        info.setRoleName(player.getName());
        info.setServerId(ServerConfig.getServerId());
        info.setLevel(player.getLevel());

        ZoneMessage.G2PReqEnterZone.Builder msg = ZoneMessage.G2PReqEnterZone.newBuilder();
        msg.setModelId(HouseCloneId);
        msg.setLevel(messInfo.getLevel());
        msg.setRoleId(messInfo.getRoomId());
        msg.setPlat(ServerConfig.getServerPlatform());
        msg.setSid(ServerConfig.getServerId());
        msg.setBirthGroup(0);
        msg.setGuildId(player.getGuildId());
        msg.addList(info);
        MessageUtils.send_to_public(ZoneMessage.G2PReqEnterZone.MsgID.eMsgID_VALUE, msg.build().toByteArray());

        player.playerCrossData.isReqFight = true;
        player.playerCrossData.reqFightTime = TimeUtils.Time();
        player.playerCrossData.crossState = CrossState.PCS_PIPEI;

    }

    @Override
    public void S2GHomeInfo(Player player, HomeMessage.S2GHomeInfo messInfo) {
        if (player == null){
            return;
        }
        Home home = player.getHome();
        home.setTupLevel(messInfo.getTupLevel());
        home.setTupExp(messInfo.getExp());

        sendTupInfo(player);

        logger.info("更新聚宝盆数据 player={}", player);
    }

    /**
     * 添加家具
     *
     * @param player
     * @param furnitureId
     * @param count
     * @param changeReason
     * @return
     */
    @Override
    public boolean addFurniture(Player player, int furnitureId, int count, int changeReason) {
        if (SocialServerClient.getInstance().channel == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PublicServer_Close);
            return false;
        }
        Cfg_Social_house_furniture_Bean bean = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(furnitureId);

        MessageUtils.notify_player(player, Notify.NORMAL, MessageString.C_GET_FURNITURE, ServerStr.getChatTableName(bean.getName()));

        //添加家具
        HomeMessage.G2SHomeAddFurniture.Builder add = HomeMessage.G2SHomeAddFurniture.newBuilder();
        add.setModelId(furnitureId);
        add.setCount(count);
        add.setReason(changeReason);
        MessageUtils.send_to_social(player, HomeMessage.G2SHomeAddFurniture.MsgID.eMsgID_VALUE, add.build().toByteArray());
        return true;
    }

    @Override
    public void reqGetTupRewardHandler(Player player, HomeMessage.ReqGetTupReward messInfo) {

        long variant = Manager.countManager.getVariant(player, VariantType.HouseTup);
        if (variant > 0) {
            return;
        }
        long exp = calcHookExp(player);
        if (exp < 0) {
            return;
        }
        Manager.countManager.setVariant(player, VariantType.HouseTup, 1);

        Manager.currencyManager.manager().addEXP(player, exp, ItemChangeReason.HouseTupGet, IDConfigUtil.getLogId());

        sendTupInfo(player);
    }

    @Override
    public void reqHomeBuy(Player player, HomeMessage.ReqHomeBuy messInfo) {

        if (SocialServerClient.getInstance().channel == null) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.PublicServer_Close);
            return;
        }

        Cfg_Social_house_market_Bean bean = CfgManager.getCfg_Social_house_market_Container().getValueByKey(messInfo.getGoods());
        if (bean == null) {
            return;
        }
        if (messInfo.getCount() <= 0 || (long) messInfo.getCount() * bean.getPrice() >= Integer.MAX_VALUE) {
            return;
        }
        if (bean.getLevel() > player.getHome().getTupLevel()) {
            return;
        }
        long count = Manager.countManager.getCount(player, BaseCountType.HouseShop, bean.getID());

        int remain = bean.getBuyNum() <= 0 ? bean.getBuyNum() : bean.getBuyNum() - (int) count;

        if (remain > 0 && messInfo.getCount() > remain) {
            return;
        }
        int price = messInfo.getCount() * bean.getPrice();
        long action = IDConfigUtil.getLogId();

        if (Manager.currencyManager.manager().onDecItemCoin(player, price, ItemChangeReason.HouseShopCost, action, bean.getCurrencyID())) {

            if (bean.getLimitType() > 0 && bean.getBuyNum() > 0) {
                Count.RefreshType refreshType = Count.RefreshType.convert(bean.getLimitType() - 1);
                Manager.countManager.addCount(player, BaseCountType.HouseShop, bean.getID(), refreshType, messInfo.getCount());
            }
            List<Item> item = Item.createItems(bean.getFurnitureID(), messInfo.getCount(), false);
            if (!Manager.backpackManager.manager().addItems(player, item, ItemChangeReason.HouseShopGet, IDConfigUtil.getLogId())) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, item, ItemChangeReason.HouseShopGet);
            }
            //更新商品数量
            HomeMessage.HomeGoods.Builder goods = toBuild(player, bean);
            HomeMessage.ResUpdateHomeShopGoods.Builder message = HomeMessage.ResUpdateHomeShopGoods.newBuilder();
            message.setGoods(goods);
            MessageUtils.send_to_player(player, HomeMessage.ResUpdateHomeShopGoods.MsgID.eMsgID_VALUE, message.build().toByteArray());
        }
    }


    void sendTupInfo(Player player) {

        Home house = player.getHome();
        long variant = Manager.countManager.getVariant(player, VariantType.HouseTup);

        long exp = calcHookExp(player);

        HomeMessage.ResHomeTupInfo.Builder message = HomeMessage.ResHomeTupInfo.newBuilder();
        message.setTupLevel(house.getTupLevel());
        message.setTupExp((int) house.getTupExp());
        message.setTupReward(variant == 0);
        message.setTupRewardExp(exp);

        MessageUtils.send_to_player(player, HomeMessage.ResHomeTupInfo.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    long calcHookExp(Player player) {

        Cfg_On_hook_Bean hook = CfgManager.getCfg_On_hook_Container().getValueByKey(player.getLevel());
        if (hook == null) {
            return -1;
        }
        Cfg_Social_house_level_Bean tup = CfgManager.getCfg_Social_house_level_Container().getValueByKey(player.getHome().getTupLevel());
        if (tup == null) {
            return -1;
        }
        return (long) (tup.getLevelup_pay() * player.gainExpRate() * hook.getExp());
    }

    /**
     * 领取任务奖励
     *
     * @param player
     * @param id
     */
    @Override
    public void reqTaskRewardHandler(Player player, int id) {

        HomeTask task = player.getHome().getHomeTask().get(id);
        if (task == null) {
            return;
        }
        if (task.getState() != TaskReward) {
            return;
        }

        Cfg_Social_house_task_Bean bean = CfgManager.getCfg_Social_house_task_Container().getValueByKey(id);

        List<Item> items = Item.createItems(bean.getTask_reward(), 1);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.HouseTaskGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.HouseTaskGet);
        }

        task.setState(TaskOver);

        List<HomeTask> update = acceptTask(player);
        update.add(task);

        sendTaskUpdate(player, update);

        logger.info("领取任务奖励 task={} player={}", id, player);
    }

    /**
     * 发送任务列表
     *
     * @param player
     */
    @Override
    public void sendTask(Player player) {

        acceptTask(player);

        long variant = Manager.countManager.getVariant(player, VariantType.Daily_Home_Task);
        HomeMessage.ResHomeTaskList.Builder message = HomeMessage.ResHomeTaskList.newBuilder();
        for (HomeTask task : player.getHome().getHomeTask().values()) {
            Cfg_Social_house_task_Bean bean = CfgManager.getCfg_Social_house_task_Container().getValueByKey(task.getId());
            if (variant == 0 && bean.getDaily() == 0) {
                task.reset();
            }
            HomeMessage.TaskInfo.Builder mTask = toBuild(task);
            message.addTasks(mTask);
        }
        if (variant == 0) {
            Manager.countManager.setVariant(player, VariantType.Daily_Home_Task, 1);
        }
        MessageUtils.send_to_player(player, HomeMessage.ResHomeTaskList.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 发送任务更新
     *
     * @param player
     * @param tasks
     */
    void sendTaskUpdate(Player player, Collection<HomeTask> tasks) {
        if (tasks.isEmpty()) {
            return;
        }
        HomeMessage.ResTaskUpdate.Builder message = HomeMessage.ResTaskUpdate.newBuilder();
        for (HomeTask task : tasks) {
            HomeMessage.TaskInfo.Builder mTask = toBuild(task);
            message.addTasks(mTask);
        }
        MessageUtils.send_to_player(player, HomeMessage.ResTaskUpdate.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }


    HomeMessage.TaskInfo.Builder toBuild(HomeTask task) {
        HomeMessage.TaskInfo.Builder mTask = HomeMessage.TaskInfo.newBuilder();
        mTask.setId(task.getId());
        mTask.setProcess(task.getProcess());
        mTask.setState(task.getState());
        return mTask;
    }

    /**
     * 领取任务
     *
     * @param player
     * @return
     */
    List<HomeTask> acceptTask(Player player) {
        List<HomeTask> list = new ArrayList<>();
        for (Cfg_Social_house_task_Bean bean : CfgManager.getCfg_Social_house_task_Container().getValuees()) {
            if (player.getHome().getHomeTask().containsKey(bean.getId())) {
                continue;
            }
            if (bean.getDemand_value().isEmpty()) {
                continue;
            }
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getConditions_value())) {
                HomeTask task = new HomeTask();
                task.setId(bean.getId());
                task.setState(TaskUnOver);
                player.getHome().getHomeTask().put(task.getId(), task);
                list.add(task);
                if (bean.getType() == HomeTaskType.Popularity.getValue()) {
                    long popularity = player.getHistoryCoin().get(ItemCoinType.Popularity);
                    task.setProcess((int) popularity);
                }
                if (bean.getType() == HomeTaskType.Decorate.getValue()) {
                    task.setProcess(player.getHome().getDecorate());
                }
                if (bean.getType() == HomeTaskType.Tup.getValue()) {
                    task.setProcess(player.getHome().getTupLevel());
                }
                checkOver(task, bean);
            }
        }
        return list;
    }

    /**
     * 更新任务
     *
     * @param player
     * @param type
     * @param params
     */
    @Override
    public void doTaskAction(Player player, int type, List<Integer> params) {
        if (player == null){
            return;
        }
        logger.info("社区任务 doTaskAction type={} params={}", type, params);

        acceptTask(player);

        List<HomeTask> update = new ArrayList<>();

        int action = params.get(0);

        for (HomeTask task : player.getHome().getHomeTask().values()) {
            Cfg_Social_house_task_Bean bean = CfgManager.getCfg_Social_house_task_Container().getValueByKey(task.getId());
            if (bean.getType() != type) {
                continue;
            }
            if (task.getState() == TaskOver || task.getState() == TaskReward) {
                continue;
            }

            update.add(task);
            //TODO 收集家具套装
            if (type == HomeTaskType.CollectionSuit.getValue()) {
                int param = params.get(1);
                int count = task.getParams().getOrDefault(action, 0);
                task.getParams().put(action, count + param);

                ReadArray<Integer> condition = bean.getDemand_value().get(0);
                int max = condition.get(0, 99);
                int suit = condition.get(1, -1);
                int cc = 0;
                //TODO 检测任务完成
                for (Cfg_Social_house_furniture_Bean furniture : CfgManager.getCfg_Social_house_furniture_Container().getValuees()) {
                    if (furniture.getSuit() != suit) {
                        continue;
                    }
                    if (task.getParams().containsKey(furniture.getId())) {
                        cc ++;
                    }
                }
                if (cc >= max) {
                    task.setProcess(max);
                    task.setState(TaskReward);
                }else {
                    task.setProcess(cc);
                }
                continue;
            }

            if (type == HomeTaskType.CollectionType.getValue()) {
                int param = params.get(1);
                int count = task.getParams().getOrDefault(action, 0);
                task.getParams().put(action, count + param);

                boolean over = false;
                //TODO 检测任务完成
                for (ReadArray<Integer> condition : bean.getDemand_value().getValuees()) {
                    int furnitureType = condition.get(0, -1);
                    int furnitureCount = condition.get(1, 0);
                    int total = 0;
                    for (Map.Entry<Integer, Integer> entry : task.getParams().entrySet()) {
                        Cfg_Social_house_furniture_Bean furniture = CfgManager.getCfg_Social_house_furniture_Container().getValueByKey(entry.getKey());
                        if (furniture.getType() == furnitureType) {
                            total = total + entry.getValue();
                        }
                    }
                    if (total < furnitureCount) {
                        over = false;
                        break;
                    }
                    over = true;
                }
                if (over) {
                    task.setState(TaskReward);
                }
                continue;
            }
            if (type == HomeTaskType.Tup.getValue()) {
                task.setProcess(action);
            } else if (type == HomeTaskType.Popularity.getValue()) {
                long popularity = player.getHistoryCoin().get(ItemCoinType.Popularity);
                task.setProcess((int) popularity);
            } else if (type == HomeTaskType.Decorate.getValue()) {
                player.getHome().setDecorate(action);
                task.setProcess(action);
            } else {
                task.setProcess(task.getProcess() + action);
            }
            //TODO 检测任务完成
            checkOver(task, bean);
        }
        sendTaskUpdate(player, update);
    }

    void checkOver(HomeTask task, Cfg_Social_house_task_Bean bean) {
        ReadArray<Integer> condition = bean.getDemand_value().get(0);
        int ct = condition.get(0, 0);
        if (ct != 0 && task.getProcess() >= ct) {
            task.setProcess(ct);
            task.setState(TaskReward);
        }
    }

    @Override
    public void sendHomeShop(Player player) {

        HomeMessage.ResHomeShopGoods.Builder message = HomeMessage.ResHomeShopGoods.newBuilder();

        for (Cfg_Social_house_market_Bean bean : CfgManager.getCfg_Social_house_market_Container().getValuees()) {
            HomeMessage.HomeGoods.Builder goods = toBuild(player, bean);
            message.addGoods(goods);
        }

        MessageUtils.send_to_player(player, HomeMessage.ResHomeShopGoods.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }

    /**
     * 处理场景改变
     *
     * @param mess
     */
    @Override
    public void doSceneChange(HomeMessage.S2FHomeSceneChange mess) {

        MapObject map = Manager.mapManager.getMap(mess.getRoleId());
        if (map == null) {
            return;
        }
        Manager.mapManager.addCommand(new SceneChangeEvent(map, mess.getLevel()));
    }

    HomeMessage.HomeGoods.Builder toBuild(Player player, Cfg_Social_house_market_Bean bean) {

        long count = Manager.countManager.getCount(player, BaseCountType.HouseShop, bean.getID());

        int remain = bean.getBuyNum() <= 0 ? bean.getBuyNum() : bean.getBuyNum() - (int) count;

        HomeMessage.HomeGoods.Builder goods = HomeMessage.HomeGoods.newBuilder();
        goods.setGoodsId(bean.getID());
        goods.setRemain(remain);
        return goods;
    }
}
