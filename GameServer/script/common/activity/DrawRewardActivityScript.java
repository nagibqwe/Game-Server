package common.activity;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.MessageString;
import com.data.bean.Cfg_Item_Bean;
import com.data.struct.ReadArray;
import com.game.activity.script.IActivityLucky;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityLucky;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
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
 * @Desc TODO 天帝宝库 300005
 * @Date 2020/9/9 11:54
 * @Auth ZUncle
 */
public class DrawRewardActivityScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(DrawRewardActivityScript.class);
    final transient String S_Prc = "S_Prc";                      //服务器奖励进度
    final transient String P_Big = "P_Big";                      //玩家轮次奖励进度
    final transient String P_Prc = "P_Prc";                      //玩家奖励进度
    final transient String P_Lowest = "P_Lowest";                //玩家保底奖励进度
    final transient String S_Lowest_Max = "S_Lowest_Max";        //保底奖励最大进度
    final transient String P_Lowest_State = "P_Lowest_State";    //玩家保底奖励状态
    final transient String P_Prc_Rewarded = "P_Prc_Rewarded";    //玩家进度奖励领取状态
    final transient String S_Prc_Rewarded = "S_Prc_Rewarded";    //服务器进度奖励领取状态
    final transient String Open_Cells = "Open_Cells";            //翻牌状态
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String History = "History";
    final transient String RoundDrawCount = "RoundDrawCount";    //本轮次抽奖次数
    final transient int HistoryLen = 10;                         //抽奖记录
    final transient int ServerHistoryLen = 20;                   //抽奖记录

    /**
     * 增加幸运值
     * 九 零一 起玩 www.9 0175.com
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

    int calcLowestMax(DrawRewardActivity draw) {
        int max = 0;
        for (int key : draw.getLowestData().keySet()){
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
    public LowestData isTriggerLowestLucky(ConcurrentHashMap<String, Object> activityData, ConcurrentHashMap<String, Object> serverActivityData, DrawRewardActivity draw) {

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
        if (curLowest == lowest.getMax() || ( small != null && RandomUtils.defaultIsGenerate(small.getPro()))){
            triggerState.add(lowest.getIndex());
            activityData.put(P_Lowest_State, triggerState);
            return lowest;
        }
        return null;
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

    //////////////////////////////////
    static class DrawRewardActivity extends ActivityLucky {
        List<DrawItem> draws;       //奖池列表
        List<RoundDrawItem> rounds; //轮次奖品
        List<PrcDrawItem> prcs;     //进度奖励
        private HashMap<Integer, LowestData> lowestData;  //保底奖励 <序号，保底信息>
        int costItem;               //抽奖道具
        int gold;                   //或抽奖元宝
        RewardData goldGift;        //元宝抽奖赠送道具
        int bigLimit;               //大奖第几次抽奖之后开放
        String client;              //前端数据
        //region

        public int getBigLimit() {
            return bigLimit;
        }

        public void setBigLimit(int bigLimit) {
            this.bigLimit = bigLimit;
        }

        public RewardData getGoldGift() {
            return goldGift;
        }

        public void setGoldGift(RewardData goldGift) {
            this.goldGift = goldGift;
        }

        public int getGold() {
            return gold;
        }

        public void setGold(int gold) {
            this.gold = gold;
        }

        public int getCostItem() {
            return costItem;
        }

        public void setCostItem(int costItem) {
            this.costItem = costItem;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public List<DrawItem> getDraws() {
            return draws;
        }

        public void setDraws(List<DrawItem> draws) {
            this.draws = draws;
        }

        public List<PrcDrawItem> getPrcs() {
            return prcs;
        }

        public void setPrcs(List<PrcDrawItem> prcs) {
            this.prcs = prcs;
        }

        public List<RoundDrawItem> getRounds() {
            return rounds;
        }

        public void setRounds(List<RoundDrawItem> rounds) {
            this.rounds = rounds;
        }

        public HashMap<Integer, LowestData> getLowestData() {
            return lowestData;
        }

        public void setLowestData(HashMap<Integer, LowestData> lowestData) {
            this.lowestData = lowestData;
        }

        //endregion
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


    /**
     * 奖品
     */
    static class DrawItem {
        int id;     //抽奖ID
        int rate;   //权重
        int big;    //是否大奖
        List<RewardData> item; //奖品
        //region 方法

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
        }

        public int getBig() {
            return big;
        }

        public void setBig(int big) {
            this.big = big;
        }

        public List<RewardData> getItem() {
            return item;
        }

        public void setItem(List<RewardData> item) {
            this.item = item;
        }

        //endregion
    }

    /**
     * 轮次奖品
     */
    static class RoundDrawItem {
        int round;              //轮次
        List<RewardData> item;  //奖品
        //region

        public int getRound() {
            return round;
        }

        public void setRound(int round) {
            this.round = round;
        }

        public List<RewardData> getItem() {
            return item;
        }

        public void setItem(List<RewardData> item) {
            this.item = item;
        }

        //endregion
    }

    /**
     * 进度奖品
     */
    static class PrcDrawItem {
        int p_reach;       //个人进度需求
        int s_reach;     //全服进度需求
        List<RewardData> item; //奖品
        //region


        public int getS_reach() {
            return s_reach;
        }

        public void setS_reach(int s_reach) {
            this.s_reach = s_reach;
        }

        public int getP_reach() {
            return p_reach;
        }

        public void setP_reach(int p_reach) {
            this.p_reach = p_reach;
        }

        public List<RewardData> getItem() {
            return item;
        }

        public void setItem(List<RewardData> item) {
            this.item = item;
        }

        //endregion
    }

    /**
     * 抽奖记录
     */
    static class DrawHistory {
        String name;
        long time;
        RewardData item;
        //region

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public RewardData getItem() {
            return item;
        }

        public void setItem(RewardData item) {
            this.item = item;
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


    /**
     * 请求操作运营活动
     *
     * @param player
     * @param dataStr
     */
    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {
        });

        int operate = msg.get("operate"); //1=翻牌2=领取轮次进度奖励 3=领取全服进度奖励
        int index = msg.get("index");
        if (operate == 1) {
            openCard(player, index, actCfg);
        } else if (operate == 2) {
            openSelfPrc(player, index, actCfg);
        } else if (operate == 3) {
            openServerPrc(player, index, actCfg);
        }
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
    }

    //TODO 翻卡牌
    void openCard(Player player, int index, ActivityConfig actCfg) {

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        HashMap<Integer, String> openCells = (HashMap<Integer, String>) activityData.getOrDefault(Open_Cells, new HashMap<Integer, String>());
        if (openCells.containsKey(index)) {
            return;
        }
        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);

        if (!serverActivityData.containsKey(S_Lowest_Max)) {
            serverActivityData.put(S_Lowest_Max, calcLowestMax(draw));
        }

        boolean useGold = false;
        long logId = IDConfigUtil.getLogId();
        if (!Manager.backpackManager.manager().onRemoveItem(player, draw.getCostItem(), 1, ItemChangeReason.DailyDrawOpenCardDec, logId)) {
            //TODO 道具不足用元宝
            if (!Manager.currencyManager.manager().decGold(player, draw.getGold(), ItemChangeReason.DailyDrawOpenCardDec, logId)) {
                return;
            }
            useGold = true;
        }
        //TODO 本轮次抽奖次数
        int drawCount = (int) activityData.getOrDefault(RoundDrawCount, 0) + 1;

        //TODO 触发保底奖励
        LowestData triggerLowestLucky = isTriggerLowestLucky(activityData, serverActivityData, draw);

        //TODO 增加幸运值
        incrLucky(player, draw);
        boolean triggerLucky = triggerLowestLucky == null && isTriggerLucky(player, draw);

        //TODO 获取未翻牌奖励
        List<DrawItem> store = Utils.find(draw.getDraws(), o -> {
            for (String str : openCells.values()) {
                DrawItem di = JsonUtils.parseObject(str, new TypeReference<DrawItem>() {
                });
                if (di.getId() == o.getId()) {
                    return false;
                }
            }
            //幸运值触发只要大奖
            if (triggerLucky || triggerLowestLucky != null) {
                return o.getBig() == 1;
            }

            if (o.getBig() == 1 && drawCount <= draw.getBigLimit()) {
                return false;
            }
            return true;
        });

        //TODO 翻出奖励
        DrawItem random = random(store);
        //重置为幸运值奖励
        if (triggerLucky) {
            int id = random.getId();      //抽奖ID
            int rate = random.getRate();  //权重
            int big = random.getBig();    //是否大奖
            random = new DrawItem();
            random.setId(id);
            random.setRate(rate);
            random.setBig(big);
            random.setItem(draw.getLuckyAwardList());
        }
        //重置为保底奖励
        if (triggerLowestLucky != null) {
            int id = random.getId();      //抽奖ID
            int rate = random.getRate();  //权重
            int big = random.getBig();    //是否大奖
            random = new DrawItem();
            random.setId(id);
            random.setRate(rate);
            random.setBig(big);
            random.setItem(triggerLowestLucky.getRewardData());
        }

        openCells.put(index, JsonUtils.toJSONString(random));
        activityData.put(Open_Cells, openCells);

        RewardData show = Utils.findOne(random.getItem(), i -> i.getC() == 9 || i.getC() == player.getCareer());

        List<Item> items = Item.createItems(player.getCareer(), random.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DailyDrawOpenCardGet, logId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyDrawOpenCardGet);
        }
        //TODO 元宝抽奖发送额外奖励
        if (useGold) {
            List<Item> giftItems = Item.createItems(draw.getGoldGift().getI(), draw.getGoldGift().getN(), draw.getGoldGift().getB() == 1);
            if (!Manager.backpackManager.manager().addItems(player, giftItems, ItemChangeReason.DailyDrawOpenCardGet, logId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, giftItems, ItemChangeReason.DailyDrawOpenCardGet);
            }
        }
        //TODO 添加次数
        activityData.put(RoundDrawCount, drawCount);
        //TODO 添加抽奖进度
        int prc = (int) activityData.getOrDefault(P_Prc, 0);
        activityData.put(P_Prc, prc + 1);
        //TODO 添加服务器进度
        prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        serverActivityData.put(S_Prc, prc + RandomUtils.random(1, 3));

        if (triggerLucky) {
            cleanLucky(player, draw, items);
        }

        //TODO 抽中大奖，刷新奖池
        int big = (int) activityData.getOrDefault(P_Big, 0);
        if (random.getBig() == 1) {
            openCells.clear();
            activityData.put(P_Big, big + 1);
            activityData.put(RoundDrawCount, 0);


            List<Integer> lowestRecord = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());
            HashMap<Integer, Integer> lowestMap = new HashMap<>();
            for (Integer ii : draw.getLowestData().keySet()) {
                lowestMap.put(ii, lowestRecord.contains(ii) ? 1 : 0);
            }

            //TODO 抽中大奖
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", random.getId());
            data.put("rate", random.getRate());
            data.put("big", random.getBig());
            data.put("item", show);
            HashMap<String, Object> message = new HashMap<>();
            message.put("drawLowestMap", lowestMap);
            message.put("drawLowestCount", activityData.getOrDefault(P_Lowest, 0));   //当前保底时的抽奖次数
            message.put("draw", data);

            Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));

        }
        //TODO 记录
        DrawHistory history = new DrawHistory();
        history.setTime(TimeUtils.Time());
        history.setItem(show);

        List<String> histories = (List<String>) activityData.getOrDefault(History, new ArrayList<String>());
        histories.add(JsonUtils.toJSONString(history));
        if (histories.size() > HistoryLen) {
            histories.remove(0);
        }
        activityData.put(History, histories);

        history.setName(player.getName());
        histories = (List<String>) serverActivityData.getOrDefault(History, new ArrayList<String>());
        histories.add(JsonUtils.toJSONString(history));
        if (histories.size() > ServerHistoryLen) {
            histories.remove(0);
        }
        serverActivityData.put(History, histories);
        Manager.activityManager.deal().saveActData(actCfg.getType(), serverActivityData);

//        logger.info("<天帝宝库> 抽奖index={} player={}", index, player);
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.DailyDrawOpenCard, actCfg.getType(), actCfg.getId());
        if (random.getBig() == 1) {
            Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(show.getI());
            String itemName = ServerStr.getChatTableName(itemBean.getName());
            int itemNum = show.getN();
            MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice1,
                    ServerConfig.getServerId(),
                    player.getId(),
                    player.getName(),
                    actCfg.getName(),
                    itemName,
                    itemNum);
        }
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyDraw, ItemChangeReason.DailyDrawOpenCard, big, 0);
        //测试日志
//        if (ServerConfig.isTestServer()) {
//            long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
//            Manager.chatManager.sendSystemStrToPlayer(player, "当前幸运值=" + variant);
//        }
    }

    //TODO 获取轮次进度奖励
    void openSelfPrc(Player player, Integer index, ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        int prc = (int) activityData.getOrDefault(P_Big, 0);
        if (index > prc) {
            return;
        }
        List<Integer> rewarded = (List<Integer>) activityData.getOrDefault(P_Prc_Rewarded, new ArrayList<Integer>());
        if (rewarded.contains(index)) {
            return;
        }
        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);
        RoundDrawItem roundDrawItem = Utils.findOne(draw.getRounds(), o -> o.getRound() == index);
        if (roundDrawItem == null) {
            return;
        }
        rewarded.add(index);
        activityData.put(P_Prc_Rewarded, rewarded);
        //TODO 开始发奖励

        List<Item> items = Item.createItems(player.getCareer(), roundDrawItem.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DailyDrawRollGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyDrawRollGet);
        }
        logger.info("<天帝宝库>领取个人进度奖励 prc={} player={}", index, player);

        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(items.get(0).getItemModelId());
        String itemName = ServerStr.getChatTableName(itemBean.getName());
        int itemNum = items.get(0).getNum();
        MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice2,
                ServerConfig.getServerId(),
                player.getId(),
                player.getName(),
                actCfg.getName(),
                itemName,
                itemNum);
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.DailyDrawRollGet, actCfg.getType(), actCfg.getId());
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyDraw, ItemChangeReason.DailyDrawRollGet, index, 0);
    }

    //TODO 获取全服进度奖励
    void openServerPrc(Player player, Integer index, ActivityConfig actCfg) {

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        int s_prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        if (index > s_prc) {
            return;
        }
        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        List<Integer> rewarded = (List<Integer>) activityData.getOrDefault(S_Prc_Rewarded, new ArrayList<Integer>());
        if (rewarded.contains(index)) {
            return;
        }
        int prc = (int) activityData.getOrDefault(P_Prc, 0);


        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);
        PrcDrawItem prcDrawItem = Utils.findOne(draw.getPrcs(), o -> o.getS_reach() == index && prc >= o.getP_reach());
        if (prcDrawItem == null) {
            return;
        }
        rewarded.add(index);
        activityData.put(S_Prc_Rewarded, rewarded);
        //TODO 开始发奖励
        List<Item> items = Item.createItems(player.getCareer(), prcDrawItem.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DailyDrawPrcGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyDrawPrcGet);
        }
        logger.info("<天帝宝库>领取全服进度奖励 prc={} player={}", index, player);

        Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(items.get(0).getItemModelId());
        String itemName = ServerStr.getChatTableName(itemBean.getName());
        int itemNum = items.get(0).getNum();
        MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice2,
                ServerConfig.getServerId(),
                player.getId(),
                player.getName(),
                actCfg.getName(),
                itemName,
                itemNum);
//        Manager.biManager.getScript().biActivity(player, ItemChangeReason.DailyDrawPrcGet, actCfg.getType(), actCfg.getId());
        int big = (int) activityData.getOrDefault(P_Big, 0);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyDraw, ItemChangeReason.DailyDrawPrcGet, big);
    }

    /**
     * 解析活动自己的自定义配置
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {

        DrawRewardActivity draw = JsonUtils.parseObject(customStr, DrawRewardActivity.class);
        actCfg.getCustomCfgMap().put(ActivityData, draw);

        //根据职业解析配置表
        for (ReadArray<Integer> job : Global.JobSex.getValuees()) {
            int career = job.get(0);
            DrawRewardActivity draw0 = JsonUtils.parseObject(customStr, DrawRewardActivity.class);
            ArrayList<HashMap<String, Object>> rounds = new ArrayList<>();
            for (RoundDrawItem filter : draw0.getRounds()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("round", filter.getRound());
                data.put("item", Utils.findOne(filter.getItem(), item -> item.getC() == 9 || item.getC() == career));
                rounds.add(data);
            }
            ArrayList<HashMap<String, Object>> prcs = new ArrayList<>();
            for (PrcDrawItem filter : draw0.getPrcs()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("p_reach", filter.getP_reach());
                data.put("s_reach", filter.getS_reach());
                data.put("item", Utils.findOne(filter.getItem(), item -> item.getC() == 9 || item.getC() == career));
                prcs.add(data);
            }
            ArrayList<HashMap<String, Object>> draws = new ArrayList<>();
            for (DrawItem filter : draw0.getDraws()) {
                HashMap<String, Object> data = new HashMap<>();
                data.put("id", filter.getId());
                data.put("rate", filter.getRate());
                data.put("big", filter.getBig());
                data.put("item", Utils.findOne(filter.getItem(), item -> item.getC() == 9 || item.getC() == career));
                draws.add(data);
            }
            HashMap<String, Object> resultMap = new HashMap<>();
            resultMap.put("rounds", rounds);
            resultMap.put("prcs", prcs);
            resultMap.put("draws", draws);
            resultMap.put("costItem", draw.getCostItem());
            resultMap.put("gold", draw.getGold());
            resultMap.put("goldGift", draw.getGoldGift());
            resultMap.put("lowestData", draw.getLowestData());
            actCfg.getCustomCfgMap().put(Client + career, JsonUtils.toJSONString(resultMap));
        }
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
     * @param roleId
     */
    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(roleId);

        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        int server_prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        List<String> sHistory = (List<String>) serverActivityData.getOrDefault(History, new ArrayList<String>());

        ConcurrentHashMap<String, Object> activityData = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        int player_prc = (int) activityData.getOrDefault(P_Prc, 0);
        int player_big = (int) activityData.getOrDefault(P_Big, 0);

        int lowestCount = (int) activityData.getOrDefault(P_Lowest, 0);
        List<Integer> lowestRecord = (List<Integer>) activityData.getOrDefault(P_Lowest_State, new ArrayList<>());

        List<Integer> r_prc_rewarded = (List<Integer>) activityData.getOrDefault(P_Prc_Rewarded, new ArrayList<Integer>());
        List<Integer> s_prc_rewarded = (List<Integer>) activityData.getOrDefault(S_Prc_Rewarded, new ArrayList<Integer>());
        List<String> history = (List<String>) activityData.getOrDefault(History, new ArrayList<String>());

        HashMap<Integer, String> open_cells = (HashMap<Integer, String>) activityData.getOrDefault(Open_Cells, new HashMap<Integer, String>());
        HashMap<Integer, Object> oc = new HashMap<>();
        for (Map.Entry<Integer, String> entry : open_cells.entrySet()) {
            DrawItem filter = JsonUtils.parseObject(entry.getValue(), new TypeReference<DrawItem>() {
            });
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", filter.getId());
            data.put("rate", filter.getRate());
            data.put("big", filter.getBig());
            data.put("item", Utils.findOne(filter.getItem(), item -> item.getC() == 9 || item.getC() == player.getCareer()));
            oc.put(entry.getKey(), data);
        }
        HashMap<Integer, Integer> lowestMap = new HashMap<>();
        for (Integer index : draw.getLowestData().keySet()) {
            lowestMap.put(index, lowestRecord.contains(index) ? 1 : 0);
        }

        HashMap<String, Object> message = new HashMap<>();
        message.put("server_prc", server_prc);  //服务器进度
        message.put("player_prc", player_prc);  //个人进度
        message.put("player_big", player_big);  //个人轮次
        message.put("drawLowestMap", lowestMap);
        message.put("drawLowestCount", lowestCount);//当前保底时的抽奖次数
        message.put("r_prc_rewarded", r_prc_rewarded);  //已领轮次进度奖励
        message.put("s_prc_rewarded", s_prc_rewarded);  //已领全服进度奖励
        message.put("open_cells", oc);                  //已经翻牌
        message.put("history", history);                //个人抽奖记录
        message.put("sHistory", sHistory);              //世界抽奖记录

        return JsonUtils.toJSONString(message);
    }

    /**
     * 充值后的处理
     *
     * @param player
     * @param rechargeNum
     */
    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * 玩家上线处理
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理某个玩家活动数据
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    /**
     * 0点处理运营活动数据
     */
    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {


        if (actCfg == null) {
            return;
        }

        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        PrcDrawItem max = Collections.max(draw.getPrcs(), Comparator.comparingInt(PrcDrawItem::getS_reach));
        //TODO 全服进度会系统自然增长，增长值=奖励最高要求进度/活动天数（向上取整）
        Double day = (actCfg.getEndTime() - actCfg.getBeginTime()) / (24 * 3600 * 1000d);
        Double addPrc = Math.ceil(max.getS_reach() / day);

        //TODO 添加服务器进度
        int prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        serverActivityData.put(S_Prc, prc + addPrc.intValue());

        Manager.activityManager.deal().saveActData(actCfg.getType(), serverActivityData);

        logger.info("<天帝宝库>系统新增抽奖进度 prc={}", addPrc);
    }

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
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    /**
     * 活动结束处理
     */
    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    /**
     * 活动消耗
     *
     * @param player
     * @param coinType
     * @param consumeNum
     */
    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {
        if (Manager.activityManager == null) {
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.DrawReward);
        for (ActivityConfig activityConfig : actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(ActivityData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            DrawRewardActivity newData = JsonUtils.parseObject(customStr, DrawRewardActivity.class);
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
        return ScriptEnum.DrawRewardActivityScript;
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

    /**
     * 随机权重
     *
     * @param params
     * @return
     */
    private DrawItem random(List<DrawItem> params) {

        TreeMap<Float, DrawItem> weightMap = new TreeMap<>();
        for (DrawItem param : params) {
            Float weight = weightMap.size() == 0 ? 0f : weightMap.lastKey();
            weight += param.getRate();
            weightMap.put(weight, param);
        }
        float randomWeight = RandomUtils.randomFloatValue(0f, weightMap.lastKey());
        SortedMap<Float, DrawItem> sort = weightMap.tailMap(randomWeight, false);
        return weightMap.get(sort.firstKey());
    }

}
