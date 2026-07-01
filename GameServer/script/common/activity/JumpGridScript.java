package common.activity;

import com.data.CfgManager;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Item_Bean;
import com.game.activity.script.IActivityLucky;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityLucky;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.db.bean.ActivityConfigBean;
import com.game.mail.manager.MailManager;
import com.game.mail.structs.MailType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import com.game.utils.RandomUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.net.Config.ServerConfig;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO 掷骰子 300023
 * @Date 2020/12/17 15:29
 * @Auth ZUncle
 */
public class JumpGridScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(JumpGridScript.class);

    //region 数据KEY
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String JumpGridHistory = "J_G_History";                //已经跳过的路径
    final transient String R_ProcessCount = "R_P_Count";                   //个人通关场次
    final transient String R_ProcessRewardHistory = "R_P_R_History";       //个人通关奖励记录
    final transient String S_ProcessCount = "S_P_Count";                   //全服通关场次
    final transient String S_ProcessMailHistory = "S_P_MailHistory";       //全服通关奖励邮件记录
    final transient String DailyGainList = "DailyGainList";                //每日刷新骰子


    final transient String P_Lowest = "P_Lowest";                //玩家保底奖励进度
    final transient String S_Lowest_Max = "S_Lowest_Max";        //保底奖励最大进度
    final transient String P_Lowest_State = "P_Lowest_State";    //玩家保底奖励状态

    final transient int Opt0 = 0;  //摇奖
    final transient int Opt1 = 1;  //领取奖励

    /**
     * 增加幸运值
     *  九零 一起玩 www.901 75.com
     * @param player
     * @param lucky
     */
    @Override
    public void incrLucky(Player player, ActivityLucky lucky) {
        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
        Manager.countManager.setVariant(player, VariantType.ACTIVITY_LUCKY_VALUE, variant + lucky.getLuckyValue());
    }

    /**
     * 是否触发幸运值
     *
     * @param player
     * @param lucky
     */
    @Override
    public boolean isTriggerLucky(Player player, ActivityLucky lucky) {
        int totalLuckyValue = Manager.activityManager.getTotalLuckyValue();
        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
        return variant >= totalLuckyValue && totalLuckyValue > 0;
    }

    /**
     * 清空幸运值
     *
     * @param player
     * @param lucky
     */
    @Override
    public void cleanLucky(Player player, ActivityLucky lucky, List<Item> items) {
        for (RewardData data : lucky.getLuckyAwardList()) {
            Item one = Utils.findOne(items, item -> item.getItemModelId() == data.getI());
            if (one != null) {
                Manager.countManager.setVariant(player, VariantType.ACTIVITY_LUCKY_VALUE, 0);
            }
        }
    }

    //保底分段概率
    static class LowestPro {
        private int min;
        private int max;
        private int pro;

        //region
        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getPro() {
            return pro;
        }

        public void setPro(int pro) {
            this.pro = pro;
        }
        //endregion
    }

    //保底信息
    static class LowestData {
        private int index;//保底序号
        private int min;  //最小保底次数
        private int max;  //最大保底次数
        private List<LowestPro> proList;    //保底分段概率
        private List<RewardData> rewardData;//保底奖励

        //region
        public int getIndex() {
            return index;
        }

        public void setIndex(int index) {
            this.index = index;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public List<LowestPro> getProList() {
            return proList;
        }

        public void setProList(List<LowestPro> proList) {
            this.proList = proList;
        }

        public List<RewardData> getRewardData() {
            return rewardData;
        }

        public void setRewardData(List<RewardData> rewardData) {
            this.rewardData = rewardData;
        }
        //endregion
    }

    //进度奖励
    static class ProcessReward {
        int proc;                                       //进度
        List<RewardData> items = new ArrayList<>();     //奖励

        public int getProc() {
            return proc;
        }

        public void setProc(int proc) {
            this.proc = proc;
        }

        public List<RewardData> getItems() {
            return items;
        }

        public void setItems(List<RewardData> items) {
            this.items = items;
        }
    }

    //大奖
    static class BigGift {
        int bigGiftWeight;
        List<RewardData> gift = new ArrayList<>();

        public int getBigGiftWeight() {
            return bigGiftWeight;
        }

        public void setBigGiftWeight(int bigGiftWeight) {
            this.bigGiftWeight = bigGiftWeight;
        }

        public List<RewardData> getGift() {
            return gift;
        }

        public void setGift(List<RewardData> gift) {
            this.gift = gift;
        }
    }

    static class JumpGridActivity extends ActivityLucky {
        String client;                                                   //前端数据
        int costGold;                                                    //或者消耗元宝
        RewardData cost;                                                 //消耗色子
        List<RewardData> dailyGain;                                     //每日凌晨获得色子
        List<List<RewardData>> grids = new ArrayList<>();               //格子列表
        List<BigGift> bigGiftPool = new ArrayList<>();                  //格子大奖
        HashMap<Integer, ProcessReward> playerTimes = new HashMap<>();      //个人通关进度奖励
        HashMap<Integer, ProcessReward> serverTimes = new HashMap<>();      //全服通关进度奖励

        HashMap<Integer, LowestData> lowestData;  //保底奖励 <序号，保底信息>

        public HashMap<Integer, LowestData> getLowestData() {
            return lowestData;
        }

        public void setLowestData(HashMap<Integer, LowestData> lowestData) {
            this.lowestData = lowestData;
        }

        public List<RewardData> getDailyGain() {
            return dailyGain;
        }

        public void setDailyGain(List<RewardData> dailyGain) {
            this.dailyGain = dailyGain;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<List<RewardData>> getGrids() {
            return grids;
        }

        public void setGrids(List<List<RewardData>> grids) {
            this.grids = grids;
        }

        public HashMap<Integer, ProcessReward> getPlayerTimes() {
            return playerTimes;
        }

        public void setPlayerTimes(HashMap<Integer, ProcessReward> playerTimes) {
            this.playerTimes = playerTimes;
        }

        public HashMap<Integer, ProcessReward> getServerTimes() {
            return serverTimes;
        }

        public void setServerTimes(HashMap<Integer, ProcessReward> serverTimes) {
            this.serverTimes = serverTimes;
        }

        public RewardData getCost() {
            return cost;
        }

        public void setCost(RewardData cost) {
            this.cost = cost;
        }

        public int getCostGold() {
            return costGold;
        }

        public void setCostGold(int costGold) {
            this.costGold = costGold;
        }

        public List<BigGift> getBigGiftPool() {
            return bigGiftPool;
        }

        public void setBigGiftPool(List<BigGift> bigGiftPool) {
            this.bigGiftPool = bigGiftPool;
        }
    }


    /**
     * 请求操作运营活动
     *
     * @param player
     * @param dataStr
     * @param actCfg
     */
    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {
        });

        int opt = msg.get("opt");
        if (opt == Opt0) { //==0
            int isGold = msg.get("isGold");
            int num = msg.get("num");
            reqJumpGrid(player, isGold != 0, num, actCfg);
        }
        if (opt == Opt1) { //==1
            int times = msg.get("times");
            reqProcessReward(player, times, actCfg);
        }
    }

    //领取通关奖励
    void reqProcessReward(Player player, int times, ActivityConfig actCfg) {

        JumpGridActivity activity = (JumpGridActivity) actCfg.getCustomCfgMap().get(ActivityData);
        ProcessReward reward = activity.getPlayerTimes().get(times);
        if (reward == null) {
            return;
        }
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        ArrayList<Integer> history = (ArrayList) activityData.getOrDefault(R_ProcessRewardHistory, new ArrayList<>());
        if (history.contains(times)) {
            return;
        }
        history.add(times);
        activityData.put(R_ProcessRewardHistory, history);
        //TODO 发放道具
        List<Item> items = Item.createItems(player.getCareer(), reward.getItems());
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DicePlayerTimesGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DicePlayerTimesGet);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("times", times);
        message.put("selfHistory", history);
        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        logger.info("掷骰子通关奖励 times={} player={}", times, player);
        //记录BI
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.DicePlayerTimesGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Dice, ItemChangeReason.DicePlayerTimesGet, times, 0);

    }

    void reqJumpGrid(Player player, boolean isGold, int num, ActivityConfig actCfg) {
        JumpGridActivity activity = (JumpGridActivity) actCfg.getCustomCfgMap().get(ActivityData);

        if (activity.getGrids().isEmpty()) {
            return;
        }
        if (num < 1 || num > 10) {
            return;
        }


        int remainCount = Manager.backpackManager.manager().getItemNum(player, activity.getCost().getI());

        long coin = Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GemCoin);
        long remainCoinCount = coin / activity.getCostGold();

        //TODO 检查道具是否足够
        if (isGold) {
            if (remainCount + remainCoinCount < num) {
                return;
            }
        } else {
            if (remainCount < num) {
                return;
            }
        }
        int min = Math.min(remainCount, num);

        long logId = IDConfigUtil.getLogId();

        Manager.backpackManager.manager().onRemoveItem(player, activity.getCost().getI(), min, ItemChangeReason.DiceCost, logId);

        Manager.currencyManager.manager().decGold(player, activity.getCostGold() * (num - min), ItemChangeReason.DiceCost, logId);

        //服务器活动数据
        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        //玩家个人活动数据
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        int oldServerTimes = (int) serverActivityData.getOrDefault(S_ProcessCount, 0);

        List<Integer> jumpList = new ArrayList<>();
        List<Item> rewards = new ArrayList<>();
        List<Item> big = new ArrayList<>();
        int openGrid = 0;

        for (int i = 0; i < num; i++) {
            openGrid = jump(player, activity, serverActivityData, activityData, jumpList, rewards, big);
        }
        List<RewardData> shows = convert(rewards);
        List<RewardData> bigShows = convert(big);
        if (rewards.size() > 0) {
            if (!Manager.backpackManager.manager().addItems(player, rewards, ItemChangeReason.DiceGridGet, logId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, rewards, ItemChangeReason.DiceGridGet);
            }
        }
        if (big.size() > 0) {
            if (!Manager.backpackManager.manager().addItems(player, big, ItemChangeReason.DiceCrossGet, logId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, big, ItemChangeReason.DiceCrossGet);
            }
        }

        int playerTimes = (int) activityData.getOrDefault(R_ProcessCount, 0);
        int serverTimes = (int) serverActivityData.getOrDefault(S_ProcessCount, 0);

        List<Integer> lowestRecord = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());
        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer ii : activity.getLowestData().keySet()) {
            lowestMap.put(ii, lowestRecord.contains(ii) ? 1 : 0);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("jump", jumpList);              //骰子随机列表
        message.put("openGrid", openGrid);          //实际开启的格子
        message.put("rewards", shows);              //普通奖励列表
        message.put("bigRewards", bigShows);        //通关奖励列表
        message.put("playerTimes", playerTimes);    //玩家抽奖次数
        message.put("serverTimes", serverTimes);    //服务器抽奖次数
        message.put("drawLowestMap", lowestMap);    //保底奖励抽取状态
        message.put("drawLowestCount", activityData.getOrDefault(P_Lowest, 0));   //当前保底的抽奖次数

        Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        ArrayList<Integer> mailHistory = (ArrayList) serverActivityData.getOrDefault(S_ProcessMailHistory, new ArrayList<>());
        //TODO 全服通关次数邮件发放全服福利
        for (int check = oldServerTimes; check <= serverTimes; check++) {
            ProcessReward sp = activity.getServerTimes().get(check);
            if (sp != null && !mailHistory.contains(check)) {
                mailHistory.add(check);
                serverActivityData.put(S_ProcessMailHistory, mailHistory);
                sendMail(actCfg, sp.getItems(), false);
                logger.info("掷骰子全服通关奖励发放邮件！！！！times={}", check);
            }
        }
        for (Item i_ : rewards) {
            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Dice, ItemChangeReason.DiceGridGet);
        }
        for (Item item : big) {
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(item.getItemModelId());
            MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice3,
                    ServerConfig.getServerId(),
                    player.getId(),
                    player.getName(),
                    actCfg.getName(),
                    ServerStr.getChatTableName(itemBean.getName()),
                    item.getNum());

            Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.Dice, ItemChangeReason.DiceCrossGet);
        }

        //测试日志
//        if (ServerConfig.isTestServer()) {
//            long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
//            Manager.chatManager.sendSystemStrToPlayer(player, "当前幸运值=" + variant);
//        }
    }

    List<RewardData> convert(List<Item> items) {
        List<RewardData> datas = new ArrayList<>();
        for (Item item : items) {
            RewardData data = new RewardData();
            data.setI(item.getItemModelId())
                    .setN(item.getNum())
                    .setB(item.isBind() ? 1 : 0);
            datas.add(data);
        }
        return datas;
    }

    int jump(Player player, JumpGridActivity activity, ConcurrentHashMap<String, Object> serverActivityData, ConcurrentHashMap<String, Object> activityData,
             List<Integer> jumpList,
             List<Item> rewards,
             List<Item> big) {


        ArrayList<Integer> jg = (ArrayList) activityData.getOrDefault(JumpGridHistory, new ArrayList<>());

        int jump = RandomUtils.random(1, 6); //随机跳跃格子数量
        jumpList.add(jump);

        int openGrid = jg.isEmpty() ? jump : jg.get(jg.size() - 1) + jump;
        openGrid = Math.min(openGrid, activity.getGrids().size());

        jg.add(openGrid);
        activityData.put(JumpGridHistory, jg);

        List<RewardData> gridReward = activity.getGrids().get(openGrid - 1);
        boolean isCross = openGrid == activity.getGrids().size();
        if (isCross) {
            //TODO 是否触发保底奖励
            LowestData triggerLowestLucky = isTriggerLowestLucky(activityData, serverActivityData, activity);
            if (triggerLowestLucky == null) {
                BigGift random = random(activity.getBigGiftPool());
                gridReward = random.getGift();
            } else {
                gridReward = triggerLowestLucky.getRewardData();
            }
        }

        //TODO 增加幸运值
        incrLucky(player, activity);
        if (isTriggerLucky(player, activity)) {

            List<Item> luckyAward = Item.createItems(player.getCareer(), activity.getLuckyAwardList());
            big.addAll(luckyAward);

            cleanLucky(player, activity, luckyAward);
        }

        //TODO 发放道具
        List<Item> items = Item.createItems(player.getCareer(), gridReward);
        //TODO 是否通关
        if (isCross) {
            int playerTimes = (int) activityData.getOrDefault(R_ProcessCount, 0);
            activityData.put(R_ProcessCount, playerTimes + 1);

            int serverTimes = (int) serverActivityData.getOrDefault(S_ProcessCount, 0);
            serverActivityData.put(S_ProcessCount, serverTimes + 1);

            activityData.put(JumpGridHistory, new ArrayList<>());

            big.add(items.get(0));
        } else {
            rewards.add(items.get(0));
        }


        return openGrid;
    }

    int calcLowestMax(JumpGridActivity draw) {
        int max = 0;
        for (int key : draw.getLowestData().keySet()) {
            LowestData bean = draw.getLowestData().get(key);
            max = Math.max(max, bean.getMax());
        }
        return max;
    }

    /**
     * 保底奖励抽奖
     *
     * @param activityData
     * @param serverActivityData
     * @param draw
     * @return
     */
    public LowestData isTriggerLowestLucky(ConcurrentHashMap<String, Object> activityData, ConcurrentHashMap<String, Object> serverActivityData, JumpGridActivity draw) {

        int last = (int) activityData.getOrDefault(P_Lowest, 0);
        int maxLowest = (int) serverActivityData.getOrDefault(S_Lowest_Max, calcLowestMax(draw));

        List<Integer> triggerState = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());
        if (triggerState.size() >= draw.getLowestData().size() && last >= maxLowest) {
            last = 0;
            activityData.put(P_Lowest, 0);
            triggerState.clear();
        }

        int curLowest = last + 1;
        activityData.put(P_Lowest, curLowest);

        LowestData lowest = Utils.findOne(draw.getLowestData().values(), o -> curLowest >= o.getMin() && curLowest <= o.getMax());
        if (lowest == null) {
            return null;
        }
        if (triggerState.contains(lowest.getIndex())) {
            return null;
        }

        LowestPro small = Utils.findOne(lowest.getProList(), o -> curLowest >= o.getMin() && curLowest <= o.getMax());
        //分段概率触发
        if (curLowest == lowest.getMax() || (small != null && RandomUtils.defaultIsGenerate(small.getPro()))) {
            triggerState.add(lowest.getIndex());
            activityData.put(P_Lowest_State, triggerState);
            return lowest;
        }
        return null;
    }

    /**
     * 随机权重
     *
     * @param params
     * @return
     */
    private BigGift random(List<BigGift> params) {

        TreeMap<Float, BigGift> weightMap = new TreeMap<>();
        for (BigGift param : params) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.getBigGiftWeight();
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, BigGift> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

    /**
     * 全服邮件
     *
     * @param rewards
     */
    void sendMail(ActivityConfig actCfg, List<RewardData> rewards, boolean isDaily) {

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        serverActivityData.putIfAbsent(DailyGainList, new HashMap<>());
        HashMap<Long, Boolean> dailyHash = (HashMap) serverActivityData.get(DailyGainList);
        int serverTimes = (int) serverActivityData.getOrDefault(S_ProcessCount, 0);

        for (PlayerWorldInfo player : Manager.playerManager.getAllPlayerWorldInfo().values()) {
            if (player.getLevel() < actCfg.getMinLv()) {
                continue;
            }
            if (isDaily) {
                dailyHash.put(player.getRoleid(), true);
            }
            List<Item> items = Item.createItems(player.getCareer(), rewards);

            String context = isDaily ? MailManager.linkContext(MessageString.Dice_Daily_Mail) : MailManager.linkContext(MessageString.Dice_ServerTimes_Mail, serverTimes);

            Manager.mailManager.sendMailToPlayer(player.getRoleid(), MailType.SysCommonRewardMail,
                    MessageString.System,
                    MessageString.Dice_Mail_Title,
                    context,
                    items, ItemChangeReason.DiceRewardGain);
        }
    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        JumpGridActivity draw = JsonUtils.parseObject(customStr, JumpGridActivity.class);
        actCfg.getCustomCfgMap().put(ActivityData, draw);
        actCfg.getCustomCfgMap().put(Client, draw.getClient());
        return true;
    }

    /**
     * 活动配置更新处理
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    /**
     * 生成活动数据字符串
     *
     * @param actCfg
     * @param roleId
     */
    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        JumpGridActivity activity = (JumpGridActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        int serverTimes = (int) serverActivityData.getOrDefault(S_ProcessCount, 0);
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        int playerTimes = (int) activityData.getOrDefault(R_ProcessCount, 0);
        List<Integer> rewardHistory = (ArrayList<Integer>) activityData.getOrDefault(R_ProcessRewardHistory, new ArrayList<>());

        ArrayList<Integer> jumpGrids = (ArrayList) activityData.getOrDefault(JumpGridHistory, new ArrayList<>());

        int lowestCount = (int) activityData.getOrDefault(P_Lowest, 0);
        List<Integer> lowestRecord = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());

        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer index : activity.getLowestData().keySet()) {
            lowestMap.put(index, lowestRecord.contains(index) ? 1 : 0);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("jumpGrids", jumpGrids);            //已跳格子
        message.put("serverTimes", serverTimes);        //全服通关次数
        message.put("playerTimes", playerTimes);        //个人通关次数
        message.put("rewardHistory", rewardHistory);    //个人通关次数奖励领取记录

        message.put("drawLowestMap", lowestMap);
        message.put("drawLowestCount", lowestCount);//当前保底时的抽奖次数

        return JsonUtils.toJSONString(message);
    }

    /**
     * 活动结束时每个活动的特殊处理
     *
     * @param actCfg
     */
    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    /**
     * 玩家上线处理
     *
     * @param player
     * @param actCfg
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理某个玩家活动数据
     *
     * @param player
     * @param actCfg
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    /**
     * 5点处理某个玩家活动数据
     *
     * @param player
     * @param actCfg
     */
    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理运营活动数据
     *
     * @param actCfg
     */
    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

        if (!actCfg.isActiviting()) {
            return;
        }
        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        serverActivityData.putIfAbsent(DailyGainList, new HashMap<>());
        HashMap<Long, Boolean> dailyHash = (HashMap) serverActivityData.get(DailyGainList);
        dailyHash.clear();

        JumpGridActivity activity = (JumpGridActivity) actCfg.getCustomCfgMap().get(ActivityData);
        sendMail(actCfg, activity.getDailyGain(), true);

        logger.info("掷骰子凌晨发放邮件！！！！");
    }

    /**
     * 5点处理运营活动数据
     *
     * @param actCfg
     */
    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    /**
     * 活动掉落
     *
     * @param player
     * @param bossId
     * @param actCfg
     * @return
     */
    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 活动掉落
     *
     * @param player
     * @param boxId
     * @param actCfg
     * @return
     */
    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 副本掉落
     *
     * @param player
     * @param cloneId
     * @param actCfg
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 充值后的处理
     *
     * @param player
     * @param getGoodsCfgId
     * @param rechargeNum
     * @param actCfg
     */
    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * 活动消耗
     *
     * @param player
     * @param coinType
     * @param consumeNum
     * @param actCfg
     */
    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {
        if (Manager.activityManager == null) {
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.ZhiTouzi);
        for (ActivityConfig activityConfig : actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            JumpGridActivity newData = JsonUtils.parseObject(customStr, JumpGridActivity.class);
            activityConfig.getCustomCfgMap().put(ActivityData, newData);
        }
    }

    /**
     * 获取scriptId
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.JumpGridScript;
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


    public static void testBuild() {

        //每日奖励
        List<RewardData> dailyGain = new ArrayList<>();
        dailyGain.add(new RewardData().setI(60020).setN(5).setB(0).setC(9));

        //掷骰子格子奖励
        List<List<RewardData>> grids = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            List<RewardData> grid = new ArrayList<>();
            if (i % 5 == 4) {
                grid.add(new RewardData().setI(21001).setN(1).setB(0).setC(0));
                grid.add(new RewardData().setI(20001).setN(1).setB(0).setC(1));
            } else {
                grid.add(new RewardData().setI(22001).setN(1).setB(0).setC(9));
            }
            grids.add(grid);
        }
        List<RewardData> end = new ArrayList<>();
        end.add(new RewardData().setI(81002).setN(1).setB(0).setC(9));
        grids.add(end);

        //玩家进度奖励
        HashMap<Integer, Object> playerTimes = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            int proc = i * 10 + 10;

            List<RewardData> items = new ArrayList<>();
            items.add(new RewardData().setI(21001).setN(1).setB(0).setC(9));
            items.add(new RewardData().setI(20001).setN(1).setB(0).setC(9));

            HashMap<String, Object> times = new HashMap<>();
            times.put("proc", proc);
            times.put("items", items);

            playerTimes.put(proc, times);
        }
        //全服进度奖励
        HashMap<Integer, Object> serverTimes = new HashMap<>();
        for (int i = 0; i < 5; i++) {
            int proc = i * 10 + 10;
            List<RewardData> items = new ArrayList<>();
            items.add(new RewardData().setI(60020).setN(3).setB(0).setC(9));

            HashMap<String, Object> times = new HashMap<>();
            times.put("proc", proc);
            times.put("items", items);

            serverTimes.put(proc, times);
        }

        HashMap<String, Object> client = new HashMap();
        client.put("costGold", 10);         //骰子消耗元宝
        client.put("cost", new RewardData().setI(60020).setN(1).setB(0).setC(9));  //骰子
        client.put("grids", grids);             //格子配置
        client.put("playerTimes", playerTimes); //个人进度
        client.put("serverTimes", serverTimes); //全服进度

        HashMap<String, Object> custom = new HashMap();
        custom.put("costGold", 10);
        custom.put("cost", new RewardData().setI(60020).setN(1).setB(0).setC(9));
        custom.put("dailyGain", dailyGain);
        custom.put("grids", grids);
        custom.put("playerTimes", playerTimes);
        custom.put("serverTimes", serverTimes);
        custom.put("client", JsonUtils.toJSONString(client));

        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(Manager.activityManager.deal().toActType(ActivityType.ZhiTouzi, 9));
        acb.setType(Manager.activityManager.deal().toActType(ActivityType.ZhiTouzi, 9));
        acb.setState((byte) 1);
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("掷骰子");
        acb.setBeginTime(TimeUtils.Time() - 24 * 60 * 60 * 1000L);
        acb.setEndTime(TimeUtils.Time() + 30 * 24 * 60 * 60 * 1000L);
        acb.setIsDelete((byte) 0);
        acb.setCustom(JsonUtils.toJSONString(custom));
        Manager.activityManager.deal().registerActivityBean(acb);
        if (Manager.activityManager.getConfigDao().isExitActivity(acb.getId())) {
            Manager.activityManager.getConfigDao().update(DbSqlName.ACTIVITYCONFIG_UPDATE.getName(), acb);
        } else {
            Manager.activityManager.getConfigDao().insert(DbSqlName.ACTIVITYCONFIG_INSERT.getName(), acb);
        }
    }

}
