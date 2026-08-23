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
 * @Desc Сокровищница 300005
 * @Date 2020/9/9 11:54
 * @Auth ZUncle
 */
public class DrawRewardActivityScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(DrawRewardActivityScript.class);
    final transient String S_Prc = "S_Prc";                      //Прогресс наград сервера
    final transient String P_Big = "P_Big";                      //Прогресс наград игрока по раундам
    final transient String P_Prc = "P_Prc";                      //Прогресс наград игрока
    final transient String P_Lowest = "P_Lowest";                //Прогресс гаранта игрока
    final transient String S_Lowest_Max = "S_Lowest_Max";        //Максимальный прогресс гаранта
    final transient String P_Lowest_State = "P_Lowest_State";    //Состояние гаранта игрока
    final transient String P_Prc_Rewarded = "P_Prc_Rewarded";    //Статус получения наград за прогресс игрока
    final transient String S_Prc_Rewarded = "S_Prc_Rewarded";    //Статус получения наград за прогресс сервера
    final transient String Open_Cells = "Open_Cells";            //Состояние открытых карт
    final transient String Client = "client";
    final transient String ActivityData = "ActivityData";
    final transient String History = "History";
    final transient String RoundDrawCount = "RoundDrawCount";    //Количество розыгрышей в текущем раунде
    final transient int HistoryLen = 10;                         //История розыгрышей
    final transient int ServerHistoryLen = 20;                   //История розыгрышей сервера

    /**
     * Увеличение удачи
     *
     * @param player
     * @param lucky
     */
    @Override
    public void incrLucky(Player player, ActivityLucky lucky) {
        long variant = Manager.countManager.getVariant(player, VariantType.ACTIVITY_LUCKY_VALUE);
        Manager.countManager.setVariant(player, VariantType.ACTIVITY_LUCKY_VALUE, variant + lucky.getLuckyValue());
    }

    /**
     * Проверка срабатывания удачи
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
     * Розыгрыш по гаранту
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
        //Срабатывание по сегментной вероятности
        if (curLowest == lowest.getMax() || ( small != null && RandomUtils.defaultIsGenerate(small.getPro()))){
            triggerState.add(lowest.getIndex());
            activityData.put(P_Lowest_State, triggerState);
            return lowest;
        }
        return null;
    }

    /**
     * Очистка удачи
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
        List<DrawItem> draws;       //Список наград
        List<RoundDrawItem> rounds; //Награды по раундам
        List<PrcDrawItem> prcs;     //Награды за прогресс
        private HashMap<Integer, LowestData> lowestData;  //Гарант <номер, данные>
        int costItem;               //Предмет для розыгрыша
        int gold;                   //Или юани для розыгрыша
        RewardData goldGift;        //Бонус за розыгрыш юанями
        int bigLimit;               //После скольких розыгрышей открывается крупный приз
        String client;              //Данные для клиента
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

    //Вероятность гаранта по сегментам
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
     * Награда
     */
    static class DrawItem {
        int id;     //ID розыгрыша
        int rate;   //Вес
        int big;    //Крупный ли приз
        List<RewardData> item; //Награда
        //region

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
     * Награда за раунд
     */
    static class RoundDrawItem {
        int round;              //Номер раунда
        List<RewardData> item;  //Награда
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
     * Награда за прогресс
     */
    static class PrcDrawItem {
        int p_reach;       //Требование для личного прогресса
        int s_reach;     //Требование для прогресса сервера
        List<RewardData> item; //Награда
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
     * История розыгрыша
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

    //Данные гаранта
    static class LowestData {
        private int index;//Номер гаранта
        private int min;  //Минимальное количество розыгрышей
        private int max;  //Максимальное количество розыгрышей
        private List<LowestPro> proList;    //Вероятность по сегментам
        private List<RewardData> rewardData;//Награда гаранта

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
     * Обработка запроса к активности
     *
     * @param player
     * @param dataStr
     */
    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {

        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {
        });

        int operate = msg.get("operate"); //1=открыть карту 2=получить награду за раунд 3=получить награду за прогресс сервера
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

    //Открытие карты
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
            //Если предметов нет, используем юани
            if (!Manager.currencyManager.manager().decGold(player, draw.getGold(), ItemChangeReason.DailyDrawOpenCardDec, logId)) {
                return;
            }
            useGold = true;
        }
        //Количество розыгрышей в текущем раунде
        int drawCount = (int) activityData.getOrDefault(RoundDrawCount, 0) + 1;

        //Проверка гаранта
        LowestData triggerLowestLucky = isTriggerLowestLucky(activityData, serverActivityData, draw);

        //Увеличение удачи
        incrLucky(player, draw);
        boolean triggerLucky = triggerLowestLucky == null && isTriggerLucky(player, draw);

        //Получение неоткрытых наград
        List<DrawItem> store = Utils.find(draw.getDraws(), o -> {
            for (String str : openCells.values()) {
                DrawItem di = JsonUtils.parseObject(str, new TypeReference<DrawItem>() {
                });
                if (di.getId() == o.getId()) {
                    return false;
                }
            }
            //При удаче выпадает только крупный приз
            if (triggerLucky || triggerLowestLucky != null) {
                return o.getBig() == 1;
            }

            if (o.getBig() == 1 && drawCount <= draw.getBigLimit()) {
                return false;
            }
            return true;
        });

        //Выбор награды
        DrawItem random = random(store);
        //Замена на награду удачи
        if (triggerLucky) {
            int id = random.getId();      //ID розыгрыша
            int rate = random.getRate();  //Вес
            int big = random.getBig();    //Крупный ли приз
            random = new DrawItem();
            random.setId(id);
            random.setRate(rate);
            random.setBig(big);
            random.setItem(draw.getLuckyAwardList());
        }
        //Замена на награду гаранта
        if (triggerLowestLucky != null) {
            int id = random.getId();      //ID розыгрыша
            int rate = random.getRate();  //Вес
            int big = random.getBig();    //Крупный ли приз
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
        //Бонус за розыгрыш юанями
        if (useGold) {
            List<Item> giftItems = Item.createItems(draw.getGoldGift().getI(), draw.getGoldGift().getN(), draw.getGoldGift().getB() == 1);
            if (!Manager.backpackManager.manager().addItems(player, giftItems, ItemChangeReason.DailyDrawOpenCardGet, logId)) {
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, giftItems, ItemChangeReason.DailyDrawOpenCardGet);
            }
        }
        //Увеличение счётчика
        activityData.put(RoundDrawCount, drawCount);
        //Увеличение личного прогресса
        int prc = (int) activityData.getOrDefault(P_Prc, 0);
        activityData.put(P_Prc, prc + 1);
        //Увеличение прогресса сервера
        prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        serverActivityData.put(S_Prc, prc + RandomUtils.random(1, 3));

        if (triggerLucky) {
            cleanLucky(player, draw, items);
        }

        //Выпадение крупного приза, обновление пула
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

            //Уведомление о крупном призе
            HashMap<String, Object> data = new HashMap<>();
            data.put("id", random.getId());
            data.put("rate", random.getRate());
            data.put("big", random.getBig());
            data.put("item", show);
            HashMap<String, Object> message = new HashMap<>();
            message.put("drawLowestMap", lowestMap);
            message.put("drawLowestCount", activityData.getOrDefault(P_Lowest, 0));   //Текущий счётчик гаранта
            message.put("draw", data);

            Manager.activityManager.deal().sendActivityDealMessage(player, actCfg.getType(), JsonUtils.toJSONString(message));

        }
        //Запись истории
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
    }

    //Получение награды за раунд
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
        //Выдача награды

        List<Item> items = Item.createItems(player.getCareer(), roundDrawItem.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DailyDrawRollGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyDrawRollGet);
        }
        logger.info("<Сокровищница> получение награды за прогресс prc={} игрок={}", index, player);

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
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyDraw, ItemChangeReason.DailyDrawRollGet, index, 0);
    }

    //Получение награды за прогресс сервера
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
        //Выдача награды
        List<Item> items = Item.createItems(player.getCareer(), prcDrawItem.getItem());

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.DailyDrawPrcGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.DailyDrawPrcGet);
        }
        logger.info("<Сокровищница> получение награды за прогресс сервера prc={} игрок={}", index, player);

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
        int big = (int) activityData.getOrDefault(P_Big, 0);
        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.DailyDraw, ItemChangeReason.DailyDrawPrcGet, big);
    }

    /**
     * Парсинг пользовательских настроек
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {

        DrawRewardActivity draw = JsonUtils.parseObject(customStr, DrawRewardActivity.class);
        actCfg.getCustomCfgMap().put(ActivityData, draw);

        //Парсинг по профессиям
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
     * Обновление конфигурации активности
     *
     * @param actCfg
     * @param customStr
     */
    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    /**
     * Получение строки данных активности
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
        message.put("server_prc", server_prc);  //Прогресс сервера
        message.put("player_prc", player_prc);  //Личный прогресс
        message.put("player_big", player_big);  //Раунд игрока
        message.put("drawLowestMap", lowestMap);
        message.put("drawLowestCount", lowestCount);//Текущий счётчик гаранта
        message.put("r_prc_rewarded", r_prc_rewarded);  //Полученные награды за раунды
        message.put("s_prc_rewarded", s_prc_rewarded);  //Полученные награды за прогресс сервера
        message.put("open_cells", oc);                  //Открытые карты
        message.put("history", history);                //Личная история
        message.put("sHistory", sHistory);              //История сервера

        return JsonUtils.toJSONString(message);
    }

    /**
     * Обработка пополнения
     *
     * @param player
     * @param rechargeNum
     */
    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * Обработка входа игрока
     *
     * @param player
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    /**
     * Полуночная обработка данных игрока
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
     * Полуночная обработка активности
     */
    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {


        if (actCfg == null) {
            return;
        }

        DrawRewardActivity draw = (DrawRewardActivity) actCfg.getCustomCfgMap().get(ActivityData);

        ConcurrentHashMap<String, Object> serverActivityData = Manager.activityManager.deal().getActivityData(actCfg.getType());
        PrcDrawItem max = Collections.max(draw.getPrcs(), Comparator.comparingInt(PrcDrawItem::getS_reach));
        //Прогресс сервера растёт автоматически: рост = максимальное требование / количество дней активности (с округлением вверх)
        Double day = (actCfg.getEndTime() - actCfg.getBeginTime()) / (24 * 3600 * 1000d);
        Double addPrc = Math.ceil(max.getS_reach() / day);

        //Увеличение прогресса сервера
        int prc = (int) serverActivityData.getOrDefault(S_Prc, 0);
        serverActivityData.put(S_Prc, prc + addPrc.intValue());

        Manager.activityManager.deal().saveActData(actCfg.getType(), serverActivityData);

        logger.info("<Сокровищница> системное добавление прогресса prc={}", addPrc);
    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }

    @Override
    public void everyHourDeal(ActivityConfig actCfg) {

    }

    /**
     * Выпадение с босса
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
     * Выпадение из сундука
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
     * Выпадение в подземелье
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
     * Завершение активности
     */
    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    /**
     * Обработка расхода
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
     * Получение ID скрипта
     *
     * @return
     */
    @Override
    public int getId() {
        return ScriptEnum.DrawRewardActivityScript;
    }

    /**
     * Вызов скрипта
     *
     * @param args
     * @return
     */
    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * Случайный выбор по весу
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