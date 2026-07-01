package common.marriage;

import com.data.*;
import com.data.bean.*;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.db.bean.RankPlayer;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.marriage.manager.MarriageManager;
import com.game.marriage.script.IMarryActivityScript;
import com.game.marriage.struct.MarryActivityTask;
import com.game.player.structs.Player;
import com.game.ranklist.manager.RankListManager;
import com.game.script.structs.ScriptEnum;
import com.game.shop.structs.ShopDefine;
import com.game.utils.MessageUtils;
import com.game.utils.ServerParamUtil;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.MarriageMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MarryActivityScript implements IMarryActivityScript {

    static final Logger logger = LogManager.getLogger("MarryActivityScript");

    final int ActivityStateOpen = 0;
    final int ActivityStateClose = 1;


    @Override
    public int getId() {
        return ScriptEnum.MarryActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 活动检测
     */
    @Override
    public void tick() {

        long state = Manager.countManager.getServerVariant(VariantType.MarryActivity);
        if (state == ActivityStateClose) {
            return;
        }
        if (isClose()) {
            /**
             * 活动结束
             */
            Manager.countManager.setServerVariant(VariantType.MarryActivity, ActivityStateClose);
            doActivityEnd();
        }
    }

    boolean isClose() {
        int openServerDay = TimeUtils.getOpenServerDay();
        return openServerDay > Global.Marry_activity_close_time;
    }

    /**
     * 活动结束 处理接口
     */
    @Override
    public void doActivityEnd() {

        //排名奖励结束邮件
        calcRankMail();

        //活动结束回收福袋
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            recoveryMarryActivityItem(player);

            //计算情缘活动结束 任务奖励
            this.calcEndMarryActivityTaskMail(player);
        }
    }

    /**
     * 计数亲密度排序
     */
    @Override
    public void intimacyRank() {

        List<Cfg_Marry_activity_rank_Bean> rankBeans = new ArrayList<>();
        for (Cfg_Marry_activity_rank_Bean bean : CfgManager.getCfg_Marry_activity_rank_Container().getValuees()) {
            if (bean.getRewardType() == 1) {
                rankBeans.add(bean);
            }
        }
        List<RankPlayer> rankPlayers = new ArrayList<>(RankListManager.getRankPlayerMap().values());

        rankBeans.sort(Comparator.comparingInt(Cfg_Marry_activity_rank_Bean::getMinRank));
        rankPlayers.sort(Comparator.comparingInt(RankPlayer::getIntimacy).thenComparingLong(RankPlayer::getFightPower).reversed());
        //TODO 根据达成条件重排
        Manager.marriageManager.getIntimacyRank().clear();
        int rank = 1;
        for (RankPlayer role : rankPlayers) {
            for (Cfg_Marry_activity_rank_Bean bean : rankBeans) {
                if (role.getIntimacy() < bean.getLimit()) {
                    rank = Math.max(rank, bean.getMaxRank() + 1);
                    continue;
                }
                if (rank < bean.getMinRank() || rank > bean.getMaxRank()) {
                    rank = Math.max(rank, bean.getMaxRank() + 1);
                    continue;
                }
                break;
            }
            Manager.marriageManager.getIntimacyRank().put(role.getRoleId(), rank);
            rank = rank + 1;
        }
    }

    /**
     * 结算亲密度排名未领取奖励
     */
    @Override
    public void calcRankMail() {

        HashMap<Integer, Cfg_Marry_activity_rank_Bean> hashBeans = new HashMap<>();
        for (Cfg_Marry_activity_rank_Bean bean : CfgManager.getCfg_Marry_activity_rank_Container().getValuees()) {
            if (bean.getRewardType() == 1) {
                for (int i = bean.getMinRank(); i <= bean.getMaxRank(); i++) {
                    hashBeans.put(i, bean);
                }
            }
        }

        Manager.marriageManager.activity().intimacyRank();
        //TODO 根据达成条件重排
        HashMap<Integer, Long> roleRankReset = new HashMap<>();
        Manager.marriageManager.getIntimacyRank().forEach((key, value) -> roleRankReset.put(value, key));

        for (Map.Entry<Integer, Cfg_Marry_activity_rank_Bean> entry : hashBeans.entrySet()) {
            if (!roleRankReset.containsKey(entry.getKey())) {
                continue;
            }
            int index = entry.getKey();
            long roleId = roleRankReset.get(index);
            Cfg_Marry_activity_rank_Bean bean = entry.getValue();
            Player player = Manager.playerManager.getPlayer(roleId);
            if (player == null) {
                logger.warn("玩家不存在 player={}", roleId);
                continue;
            }
            logger.warn("发放排名奖励 rank={} player={}", index, roleId);

            List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.MARRY_ACTIVITY_MAIL_TITL,
                    MessageString.MARRY_ACTIVITY_RANK_MAIL, items, ItemChangeReason.MarryActivityRankGet);
        }

    }

    /**
     * 回收福袋
     *
     * @param player
     */
    @Override
    public void recoveryMarryActivityItem(Player player) {

        if (!isClose()) {
            return;
        }

        long state = Manager.countManager.getVariant(player, VariantType.MarryActivity);
        if (state == ActivityStateClose) {
            return;
        }
        Manager.countManager.setVariant(player, VariantType.MarryActivity, ActivityStateClose);

        int gift = Global.Marry_activity_bless_goods.get(0);
        int itemId = Global.Marry_activity_bless_goods.get(1);
        int count = Global.Marry_activity_bless_goods.get(2);

        int giftCount = Manager.backpackManager.manager().getItemNum(player, gift);
        if (giftCount <= 0) {
            return;
        }
        List<Item> item = Item.createItems(itemId, count * giftCount, false);
        long action = IDConfigUtil.getLogId();
        Manager.backpackManager.manager().onRemoveItem(player, gift, giftCount, ItemChangeReason.MarryActivityGiftCost, action);
        Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                MessageString.MARRY_ACTIVITY_MAIL_TITL,
                MessageString.MARRY_ACTIVITY_GIFT_MAIL, item, ItemChangeReason.MarryActivityGiftGet);

        logger.info("情缘福袋回收 player={}", player);

    }

    /**
     * 触发活动掉落
     *
     * @param career
     * @param monsterId
     * @param zone
     * @param ber
     * @return
     */
    @Override
    public List<Item> triggerDrop(int career, int monsterId, int zone, int ber) {
        if (isClose()) {
            return new ArrayList<>();
        }
        Cfg_Monster_Bean monster = CfgManager.getCfg_Monster_Container().getValueByKey(monsterId);

        for (Cfg_Marry_activity_bless_Bean bean : CfgManager.getCfg_Marry_activity_bless_Container().getValuees()) {
            if (monster != null && monster.getBOSS_type() == bean.getSubType()) {
                return Item.createItems(career, bean.getReward(), ber);
            }
            if (zone == bean.getSubType()) {
                return Item.createItems(career, bean.getReward(), ber);
            }
        }
        return new ArrayList<>();
    }


    private int getProgress(Player player, ReadArray<Integer> param) {
        return Manager.controlManager.deal().getFuncProgress(player, param);
    }

    /**
     * 情缘商店购买
     *
     * @param player
     */
    @Override
    public void reqMarryActivityShopBuy(Player player, int shopId) {

        Cfg_Marry_activity_shop_Bean cfg_Marry_activity_shop_Bean = CfgManager.getCfg_Marry_activity_shop_Container().getValueByKey(shopId);
        if (cfg_Marry_activity_shop_Bean == null) {
            return;
        }
        ConcurrentHashMap<Integer, Integer> shopMap = null;
        if (!MarriageManager.getInstance().getMarryActivityShopBuyCountMap().containsKey(player.getId())) {
            shopMap = new ConcurrentHashMap<>();
            MarriageManager.getInstance().getMarryActivityShopBuyCountMap().put(player.getId(), shopMap);
        } else {
            shopMap = MarriageManager.getInstance().getMarryActivityShopBuyCountMap().get(player.getId());
        }

        int count = shopMap.getOrDefault(cfg_Marry_activity_shop_Bean.getId(), 0);

        if (count >= cfg_Marry_activity_shop_Bean.getLimitBuy()) {
            return;
        }
     
        //TODO MMOB-11261  需求新增 2021年7月17日
        List<Item> items = Item.createItems(cfg_Marry_activity_shop_Bean.getReward(), 1);
        int msId = Manager.backpackManager.manager().onHasAddSpaces(player, items);
        if (msId != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //判断物品数量
        int modelId = cfg_Marry_activity_shop_Bean.getPrice().get(0);
        int num = cfg_Marry_activity_shop_Bean.getPrice().get(1);
        if (!Manager.backpackManager.manager().removeItemOrCurrency(player, modelId, num, IDConfigUtil.getLogId(), ItemChangeReason.KaoShangLingHorseBuySpecailDec)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.ItemNotEnough);
            return;
        }


        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.MarryActivityShopBuyGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MailType.SysCommonRewardMail
                    , MessageString.System, MessageString.BAGISSPACETOMAIL
                    , MessageString.GetAwardNotEnoughSpaceContent, items, ItemChangeReason.CopyMapGet, IDConfigUtil.getLogId());
        }
        int newCount = count + 1;
        shopMap.put(cfg_Marry_activity_shop_Bean.getId(), newCount);

        ServerParamUtil.saveMarryActivityShopBuy();

        //Manager.countManager.setCount(player, BaseCountType.MarryActivityShopBuyCount, 0, Count.RefreshType.CountType_Forever, newCount);
        //响应
        MarriageMessage.ResMarryActivityShopBuy.Builder builder = MarriageMessage.ResMarryActivityShopBuy.newBuilder();

        MarriageMessage.MarryActivityShopInfo.Builder marryActivityShopInfo = MarriageMessage.MarryActivityShopInfo.newBuilder();
        marryActivityShopInfo.setShopId(shopId);
        marryActivityShopInfo.setBuyCount(newCount);
        builder.addShopInfoList(marryActivityShopInfo);

        MessageUtils.send_to_player(player, MarriageMessage.ResMarryActivityShopBuy.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    /**
     * 获取亲密度信息
     *
     * @param player
     */
    @Override
    public void reqMarryActivityIntimacy(Player player) {

        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());

        MarriageMessage.ResMarryActivityIntimacy.Builder message = MarriageMessage.ResMarryActivityIntimacy.newBuilder();
        message.setIntimacy(rankPlayer == null ? 0 : rankPlayer.getIntimacy());
        message.setRank(Manager.marriageManager.getIntimacyRank().getOrDefault(player.getId(), 0));

        for (Cfg_Marry_activity_rank_Bean bean : CfgManager.getCfg_Marry_activity_rank_Container().getValuees()) {
            if (player.getMarryActivity().checkRankRewardState(bean.getId())) {
                message.addRankRewardEd(bean.getId());
            }
        }
        MessageUtils.send_to_player(player, MarriageMessage.ResMarryActivityIntimacy.MsgID.eMsgID_VALUE, message.build().toByteArray());

    }

    /**
     * 获取亲密度奖励
     *
     * @param player
     * @param id
     */
    @Override
    public void reqMarryActivityIntimacyReward(Player player, int id) {

        RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(player.getId());
        if (rankPlayer == null) {
            return;
        }
        Cfg_Marry_activity_rank_Bean bean = CfgManager.getCfg_Marry_activity_rank_Container().getValueByKey(id);
        if (bean == null || bean.getRewardType() != 0) {
            return;
        }
        if (rankPlayer.getIntimacy() < bean.getLimit()) {
            return;
        }
        if (player.getMarryActivity().checkRankRewardState(id)) {
            return;
        }
        player.getMarryActivity().signRankRewardState(id, true);
        //发奖励
        List<Item> items = Item.createItems(player.getCareer(), bean.getReward(), 1);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.MarryActivityRankGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.MarryActivityRankGet);
        }
        reqMarryActivityIntimacy(player);
    }


    /**
     * 任务奖励领取
     *
     * @param player
     * @param taskID
     */
    @Override
    public void onReqGetMarryActivityTaskReward(Player player, int taskID) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Marry)) {
            return;
        }
        HashMap<Integer, MarryActivityTask> taskHashMap = player.getMarryActivity().getMarryActivityTaskMap();
        if (!taskHashMap.containsKey(taskID)) {
            logger.error("任务未完成 不能领奖  {}", taskID);
            return;
        }
        Cfg_Marry_activity_task_Bean bean = CfgManager.getCfg_Marry_activity_task_Container().getValueByKey(taskID);
        if (bean == null) {
            logger.error("Cfg_Marry_activity_task_Bean is null  {}", taskID);
            return;
        }
        MarryActivityTask marryActivityTask = taskHashMap.get(taskID);
        if (marryActivityTask.isState()) {
            logger.error("已领取  {}", taskID);
            return;
        }
        if (bean.getType() == 1) {
            if (!marryActivityTask.isFinsh()) {
                return;
            }
        } else if (bean.getType() == 0) {
            if (marryActivityTask.getProgress() < bean.getRate()) {
                return;
            }

        }
        //发奖
        sendTaskReward(player, bean.getReward(), false);
        marryActivityTask.setState(true);
        List<MarriageMessage.MarryActivityTaskData> taskDatas = new ArrayList<>();
        taskDatas.add(buildTaskData(marryActivityTask));
        //刷新进度
        onResRefreshMarryActivityTask(player, taskDatas);
//        int type = 0;
//        if(bean.getType() == 1){
//            type = Task.FallingSky_TASK_DAY;
//        }else if(bean.getType() == 2){
//            type = Task.FallingSky_TASK_STAGE;
//        }else if(bean.getType() == 3){
//            type = Task.FallingSky_TASK_ROUND;
//        }
//        if(type != 0){
//            Manager.biManager.getScript().biTask(player, bean.getId(), type, TaskConst.BI_RECEIVE,"",0,0);
//        }
    }

    private MarriageMessage.MarryActivityTaskData buildTaskData(MarryActivityTask task) {
        MarriageMessage.MarryActivityTaskData.Builder taskData = MarriageMessage.MarryActivityTaskData.newBuilder();
        taskData.setState(task.isState());
        taskData.setProgress(task.getProgress());
        taskData.setTaskID(task.getTaskID());
        return taskData.build();
    }

    private List<MarriageMessage.MarryActivityTaskData> buildTaskDatas(Player player) {

        //初始化进度
        Cfg_Marry_activity_task_Bean[] taskBeanList = CfgManager.getCfg_Marry_activity_task_Container().getValuees();
        if (taskBeanList != null && taskBeanList.length > 0) {
            for (int i = 0; i < taskBeanList.length; i++) {

                if (taskBeanList[i].getType() == 1) {
                    this.onRefreshUpProgress(player, taskBeanList[i].getCondition().get(0), false);
                }

            }
        }

        List<MarriageMessage.MarryActivityTaskData> taskDatas = new ArrayList<>();
        for (Map.Entry<Integer, MarryActivityTask> entry : player.getMarryActivity().getMarryActivityTaskMap().entrySet()) {
            MarriageMessage.MarryActivityTaskData.Builder taskData = MarriageMessage.MarryActivityTaskData.newBuilder();
            MarryActivityTask task = entry.getValue();
            taskData.setState(task.isState());
            taskData.setProgress(task.getProgress());
            taskData.setTaskID(task.getTaskID());
            taskDatas.add(taskData.build());
        }
        return taskDatas;
    }

    /**
     * @param player
     */
    private void sendTaskReward(Player player) {
        HashMap<Integer, MarryActivityTask> taskMap = player.getMarryActivity().getMarryActivityTaskMap();
        if (taskMap.size() <= 0) {
            return;
        }
        Iterator<Map.Entry<Integer, MarryActivityTask>> iter = taskMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Integer, MarryActivityTask> entry = iter.next();
            MarryActivityTask marryActivityTask = entry.getValue();
            Cfg_Marry_activity_task_Bean bean = CfgManager.getCfg_Marry_activity_task_Container().getValueByKey(marryActivityTask.getTaskID());
            if (bean == null) {
                logger.error("Cfg_Marry_activity_task_Bean is null {}", marryActivityTask.getTaskID());
                continue;
            }
//            if (bean.getType() != type) {
//                continue;
//            }
            iter.remove();

            if (!marryActivityTask.isState()) {
                if (bean.getType() == 1) {
                    if (marryActivityTask.isFinsh()) {
                        sendTaskReward(player, bean.getReward(), true);
                    }
                } else {
                    if (marryActivityTask.getProgress() >= bean.getRate()) {
                        sendTaskReward(player, bean.getReward(), true);
                    }
                }

            }
        }
    }

    private void sendTaskReward(Player player, ReadIntegerArrayEs reward, boolean isMaill) {
        sendReward(player, reward, isMaill, ItemChangeReason.MarryActivityTaskRewardGet);
        // Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FallingSky_TaskAward, ItemChangeReason.FallingSkyTaskRewardGain);
    }

    /**
     * 发送奖励
     *
     * @param player
     * @param reward
     * @param isMaill
     */
    private void sendReward(Player player, ReadIntegerArrayEs reward, boolean isMaill, int reason) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Marry)) {
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(player.getCareer(), reward, 1);

        if (isMaill) {
            String conext = MessageString.marry_activity_reward_miss_tex + "@_@";
            Manager.mailManager.sendMailToPlayer(player.getId(),
                    MessageString.System, MessageString.System, MessageString.marry_activity_reward_miss_mail, conext, items, reason);
        } else {
            if (!Manager.backpackManager.manager().addItems(player, items, reason, actionId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(),
                        MessageString.System, MessageString.System, MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, reason);
            }
        }
    }

    /**
     * 进度刷新
     *
     * @param player
     * @param type
     */
    @Override
    public void onRefreshUpProgress(Player player, int type, boolean isSendClient) {
//        long state = Manager.countManager.getServerVariant(VariantType.MarryActivity);
//        if (state == ActivityStateClose) {
//            return;
//        }

        //活动关闭 不检测任务条件了
        if (isClose()) {
            return;
        }


        HashMap<Integer, MarryActivityTask> taskHashMap = player.getMarryActivity().getMarryActivityTaskMap();
        List<MarriageMessage.MarryActivityTaskData> taskDatas = new ArrayList<>();
        int totalTaskProgress = 0;
        for (Cfg_Marry_activity_task_Bean bean : CfgManager.getCfg_Marry_activity_task_Container().getValuees()) {
            if (bean.getType() != 1) {
                continue;
            }
            if (bean.getCondition().get(0) != type) {
                continue;
            }
            MarryActivityTask marryActivityTask = taskHashMap.get(bean.getId());
            if (marryActivityTask == null) {
                marryActivityTask = new MarryActivityTask();
                marryActivityTask.setTaskID(bean.getId());
                marryActivityTask.setState(false);
                taskHashMap.put(bean.getId(), marryActivityTask);
            }
            //是否完成
//            if(!marryActivityTask.isFinsh()){
//                marryActivityTask.setProgress(this.getProgress(player, bean.getCondition()));
//            }
            marryActivityTask.setProgress(this.getProgress(player, bean.getCondition()));

            //检测是否完成
            if (Manager.controlManager.deal().checkFuncProgress(player, bean.getCondition())) {
                marryActivityTask.setFinsh(true);
            }
            taskDatas.add(buildTaskData(marryActivityTask));
        }
        //总进度
        for (Cfg_Marry_activity_task_Bean bean : CfgManager.getCfg_Marry_activity_task_Container().getValuees()) {
            if (bean.getType() == 1) {
                MarryActivityTask marryActivityTask = taskHashMap.get(bean.getId());
                if (marryActivityTask != null) {
                    //检测任务是否完成
                    if (marryActivityTask.isFinsh()) {
                        totalTaskProgress += bean.getRate();
                    }
                }

            }
        }

        //检测总任务进度
        for (Cfg_Marry_activity_task_Bean bean : CfgManager.getCfg_Marry_activity_task_Container().getValuees()) {
            if (bean.getType() == 0) {
                MarryActivityTask marryActivityTask = taskHashMap.get(bean.getId());
                if (marryActivityTask == null) {
                    marryActivityTask = new MarryActivityTask();
                    marryActivityTask.setTaskID(bean.getId());
                    marryActivityTask.setState(false);
                    taskHashMap.put(bean.getId(), marryActivityTask);
                }
                marryActivityTask.setProgress(totalTaskProgress);
                taskDatas.add(buildTaskData(marryActivityTask));
            }
        }
        if (taskDatas.size() > 0) {
            if (isSendClient) {
                onResRefreshMarryActivityTask(player, taskDatas);
            }

        }
    }

    //任务进度刷新
    private void onResRefreshMarryActivityTask(Player player, List<MarriageMessage.MarryActivityTaskData> taskDatas) {

        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Marry)) {
            return;
        }
        MarriageMessage.ResRefreshMarryActivityTask.Builder msg = MarriageMessage.ResRefreshMarryActivityTask.newBuilder();
        msg.addAllTaskData(taskDatas);
        MessageUtils.send_to_player(player, MarriageMessage.ResRefreshMarryActivityTask.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 计算情缘活动结束 任务奖励
     */
    public void calcEndMarryActivityTaskMail(Player player) {
        HashMap<Integer, MarryActivityTask> taskHashMap = player.getMarryActivity().getMarryActivityTaskMap();
        if (taskHashMap != null && taskHashMap.size() > 0) {
            this.sendTaskReward(player);
        }
        player.getMarryActivity().getMarryActivityTaskMap().clear();

        ServerParamUtil.saveMarryActivityShopBuy();
    }

    @Override
    public void online(Player player) {
        if (!Manager.controlManager.deal().isOpenFunction(player, FunctionStart.Marry)) {
            return;
        }
        //检测任务刷新
        //上线判断活动关闭 发放奖励邮件
        if (isClose()) {
            calcEndMarryActivityTaskMail(player);
        } else {
            //情缘任务
            MarriageMessage.ResRefreshMarryActivityTask.Builder taskBuilder = MarriageMessage.ResRefreshMarryActivityTask.newBuilder();
            taskBuilder.addAllTaskData(buildTaskDatas(player));
            MessageUtils.send_to_player(player, MarriageMessage.ResRefreshMarryActivityTask.MsgID.eMsgID_VALUE, taskBuilder.build().toByteArray());
        }
        if (MarriageManager.getInstance().getMarryActivityShopBuyCountMap().containsKey(player.getId())) {
            ConcurrentHashMap<Integer, Integer> shopMap = MarriageManager.getInstance().getMarryActivityShopBuyCountMap().get(player.getId());
            MarriageMessage.ResMarryActivityShopBuy.Builder shopBuilder = MarriageMessage.ResMarryActivityShopBuy.newBuilder();
            for (int shopId : shopMap.keySet()) {
                MarriageMessage.MarryActivityShopInfo.Builder marryActivityShopInfo = MarriageMessage.MarryActivityShopInfo.newBuilder();
                marryActivityShopInfo.setShopId(shopId);
                marryActivityShopInfo.setBuyCount(shopMap.get(shopId));
                shopBuilder.addShopInfoList(marryActivityShopInfo);
            }
            MessageUtils.send_to_player(player, MarriageMessage.ResMarryActivityShopBuy.MsgID.eMsgID_VALUE, shopBuilder.build().toByteArray());
        }
    }
}
