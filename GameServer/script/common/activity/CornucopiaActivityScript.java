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
import com.game.chat.Manager.ChatManager;
import com.game.chat.structs.ChatChannel;
import com.game.chat.structs.Notify;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.player.structs.Player;
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
import game.message.ActivityMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Рог изобилия 300026
 */
public class CornucopiaActivityScript implements IActivityScript, IActivityLucky {

    final transient Logger logger = LogManager.getLogger(CornucopiaActivityScript.class);
    private static final String configData = "configData";
    final transient String totalCount = "totalCount";            //Общее количество розыгрышей
    final transient String lowestDrawCount = "lowestDrawCount";  //Количество розыгрышей до гаранта
    final transient String lowestDrawMap = "lowestDrawMap";      //Карта статуса гаранта
    final transient String lowestGoldCount = "lowestGoldCount";  //Количество розыгрышей до гаранта в пуле юаней
    final transient String totalGold = "totalGold";              //Накопленное количество юаней в пуле
    final transient String roleDailyGoldCount = "roleDailyGoldCount";//Количество выигрышей крупного приза юанями у игрока за день
    final transient String dailyAddGold = "dailyAddGold";        //Количество юаней, добавленных системой за день
    final transient String accReward = "accReward";              //Награда за накопленное количество
    final transient String activeReward = "activeReward";        //Награда за ежедневную активность
    final transient String playerHistory = "playerHistory";      //История розыгрышей игрока
    final transient String serverHistory = "serverHistory";      //История розыгрышей всех игроков на сервере
    final transient String goldHistory = "goldHistory";          //История розыгрышей из пула юаней
    final transient int playerHistoryLen = 10;                   //Длина истории игрока
    final transient int serverHistoryLen = 10;                   //Длина истории сервера
    final transient int goldHistoryLen = 10;                     //Длина истории пула юаней

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity == null) {
            logger.error("Конфигурация для активности Рог изобилия отсутствует");
            return;
        }
        //Важные параметры проверяем здесь
        if (cornucopiaActivity.getOneCostGold() < 1 || cornucopiaActivity.getOneCostItem() < 1) {
            logger.error("В конфигурации Рога изобилия стоимость消耗 установлена в 0");
            return;
        }
        HashMap<String, Integer> msg = JsonUtils.parseObject(dataStr, new TypeReference<HashMap<String, Integer>>() {});
        int operate = msg.get("operate"); //1=розыгрыш 2=получение награды за накопление 3=получение награды за активность
        if (operate == 1) {//Розыгрыш
            Integer once = msg.get("once");
            if (once == null) {
                logger.error("Параметр розыгрыша в Роге изобилия пуст");
                return;
            }

            dealDraw(player, once == 1, cornucopiaActivity, actCfg);
        } else if (operate == 2) {//Получение награды за накопление
            Integer count = msg.get("count");
            if (count == null) {
                logger.error("Параметр получения награды за накопление в Роге изобилия пуст");
                return;
            }
            dealCountReward(player, count, cornucopiaActivity, actCfg);
        } else if (operate == 3) {//Получение ежедневной награды за активность
            Integer count = msg.get("count");
            if (count == null) {
                logger.error("Параметр получения награды за активность в Роге изобилия пуст");
                return;
            }
            dealActiveReward(player, count, cornucopiaActivity, actCfg);
        }
    }

    /**
     * Розыгрыш один раз или десять раз
     */
    private void dealDraw(Player player, boolean once, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        int useItemTimes = 0, useGoldTimes = 0;
        int drawCount = once ? 1 : 10;
        int costItemId = cornucopiaActivity.getItemId();
        int costItemCount = 1; //Количество предметов за один розыгрыш всегда 1
        int costGoldCount = cornucopiaActivity.getOneCostGold();
        if(once){
            //Сначала пытаемся использовать предметы
            if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                //Если предметы не списались, значит их нет, списываем юани
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    //Юаней недостаточно
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                } else {
                    //Юани списались успешно
                    useGoldTimes = 1;
                }
            } else {
                //Предметы использованы успешно
                useItemTimes = 1;
            }
        }else{//Десять раз подряд
            //Сколько предметов есть
            int itemNum = Manager.backpackManager.manager().getItemNum(player, costItemId);
            if (itemNum < costItemCount) {//Даже одного предмета нет
                //Списываем юани за 10 раз
                if (!Manager.currencyManager.manager().onDecItemCoin(player, costGoldCount * 10, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }
                useGoldTimes = 10;
            } else if (itemNum < costItemCount * 10) {//Предметов меньше 10, проверяем хватает ли юаней на разницу
                //Юаней недостаточно
                if (Manager.currencyManager.manager().getCurrencyIntNum(player, ItemCoinType.GemCoin) < (10 - itemNum) * costGoldCount) {
                    MessageUtils.notify_player(player, Notify.ERROR, MessageString.CurrencyNotEnough, Manager.backpackManager.manager().getName(ItemCoinType.GemCoin));
                    return;
                }else {//Юаней достаточно, списываем и юани и все предметы
                    //Для безопасности проверяем ещё раз
                    if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, itemNum, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                        logger.error("1 Не должно было случиться" + player.getId());
                        return;
                    }
                    if (!Manager.currencyManager.manager().onDecItemCoin(player, (10 - itemNum) * costGoldCount, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId(), ItemCoinType.GemCoin)) {
                        logger.error("2 Не должно было случиться" + player.getId());
                        return;
                    }
                    useItemTimes = itemNum;
                    useGoldTimes = 10 - itemNum;
                }
            } else {//Предметов >= 10, списываем 10
                //Для безопасности проверяем ещё раз
                if (!Manager.backpackManager.manager().onRemoveItem(player, costItemId, costItemCount*10, ItemChangeReason.CornucopiaCost, IDConfigUtil.getLogId())) {
                    logger.error("3 Не должно было случиться" + player.getId());
                    return;
                }
                useItemTimes = 10;
            }
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);
        Integer goldCount = (Integer) roleActDataMap.getOrDefault(lowestGoldCount, 0);
        Integer dailyGoldCount = (Integer) roleActDataMap.getOrDefault(roleDailyGoldCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        //Таблицы записей для отправки клиенту
        List<String> playerHistorys;
        List<String> serverHistorys;
        List<String> goldHistorys;
        if(roleActDataMap.get(playerHistory) != null){
            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
        }else{
            playerHistorys = new ArrayList<>();
            roleActDataMap.put(playerHistory, playerHistorys);
        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }
        if(actDataMap.get(goldHistory) != null){
            goldHistorys = (List<String>)actDataMap.get(goldHistory);
        }else{
            goldHistorys = new ArrayList<>();
            actDataMap.put(goldHistory,goldHistorys);
        }

        //Карта гаранта <номер гаранта, статус>
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            //Инициализация карты гаранта
            for (int i = 0; i < cornucopiaActivity.getLowestData().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        HashMap<Integer, LowestData> lowestDatas = cornucopiaActivity.getLowestData();
        HashMap<Integer, LevelWeight> levelWeightMap = cornucopiaActivity.getLevelWeightMap();
        int maxLowestCount = calcLowestMax(lowestDatas);
        //Награда за розыгрыш
        List<RewardData> lastList = new ArrayList<>();
        //Награда из пула юаней
        List<Integer> goldList = new ArrayList<>();
        int bigCount = 0;

        //Приоритет: гарант > удача > лимиты на розыгрыш
        //Начинаем розыгрыш
        for (int times = 0; times < drawCount; times++) {
            boolean useGold = false;
            if(useItemTimes-(times+1)<=0){
                useGold = true;
            }
            //0=начальное состояние 1=гарант 2=удача
            int tag = 0;
            int lv = -1;

            totalDrawCount+=1;
            lowestCount += 1;

            //Сначала проверяем гарант
            List<RewardData> drawReward = null;
            LowestData lowestData = getCurLowestData(lowestCount, lowestDatas);
            if(lowestData != null){
                List<RewardData> lowestRewards = getLowestReward(lowestCount, lowestData, lowestMap);
                if(lowestRewards!=null){
                    tag = 1;
                    drawReward = lowestRewards;
                    //Устанавливаем статус текущего гаранта как использованный
                    lowestMap.put(String.valueOf(lowestData.getIndex()), 1);
                    //Проверяем, все ли гаранты использованы
                    if(lowestCount >= maxLowestCount && checkAllLowest(lowestMap)){
                        lowestCount = 0;//Сбрасываем счётчик гаранта
                        clearLowestMap(cornucopiaActivity.getLowestData(), lowestMap);
                    }
                }
            }

            boolean isTriggerLucky = false;
            if(tag==0){
                //Увеличиваем удачу
                incrLucky(player, cornucopiaActivity);
                isTriggerLucky = isTriggerLucky(player, cornucopiaActivity);
                if(isTriggerLucky){//Удача достигла порога
                    tag=2;
                    lv=0;
                }else{//Обычный розыгрыш

                    //Проверяем исключённые уровни
                    List<Integer> exLv = getExList(cornucopiaActivity, totalDrawCount);
                    //Сначала определяем уровень награды
                    lv = getRandomLevelByWeight(levelWeightMap, useGold, exLv);
                }
            };

            //Выбираем из соответствующего пула
            if(drawReward == null){
                drawReward = getRandomRewardByWeight(cornucopiaActivity.getRewardPoolMap().get(lv), useGold);
            }

            RewardData rewardData = getRewardByCareer(player.getCareer(), drawReward);

            //Если сработала удача, используем награду за удачу
            RewardData lucky = Utils.findOne(cornucopiaActivity.getLuckyAwardList(), i -> i.getC() == 9 || i.getC() == player.getCareer());
            RewardData lastReward = isTriggerLucky ? lucky : rewardData;

            //Для истории
            lastList.add(lastReward);

            List<Item> items = Item.createItems(lastReward.getI(), lastReward.getN(), lastReward.getB() == 1);
            cleanLucky(player, cornucopiaActivity, items);

            //Добавляем историю
            if(playerHistorys.size()>playerHistoryLen){
                playerHistorys.remove(0);
            }
            playerHistorys.add(new StringBuilder(String.valueOf(lastReward.getI())).append("_").append(String.valueOf(lastReward.getN())).toString());

            if(serverHistorys.size()>serverHistoryLen){
                serverHistorys.remove(0);
            }
            serverHistorys.add(new StringBuilder(player.getName()).append("_").append(String.valueOf(lastReward.getI())).append("_").append(String.valueOf(lastReward.getN())).toString());

            if (lv == 0){//Уведомление о крупном выигрыше
                Cfg_Item_Bean itemBean = CfgManager.getCfg_Item_Container().getValueByKey(lastReward.getI());
                String itemName = ServerStr.getChatTableName(itemBean.getName());
                int itemNum = lastReward.getN();
                MessageUtils.notify_AllServer(Notify.EXCLUSIVE_NOTIFY, ChatChannel.CHATCHANNEL_SYSTEM, MessageString.luck_draw_radio_notice4,
                        ServerConfig.getServerId(),
                        player.getId(),
                        player.getName(),
                        actCfg.getName(),
                        itemName,
                        itemNum);
            }

            //Пул юаней
            int curGoldCount = goldCount+1;
            //Потраченные юани добавляем в пул
            if(totalPoolGold+costGoldCount>cornucopiaActivity.getGoldMaxCount()){
                totalPoolGold = cornucopiaActivity.getGoldMaxCount();
            }else{
                totalPoolGold += costGoldCount;
            }

            int min = cornucopiaActivity.getGoldBigMin();
            int max = cornucopiaActivity.getGoldBigMax();
            //Если достигнут дневной лимит
            if(dailyGoldCount>=cornucopiaActivity.getGoldDailyCount()){
                if(curGoldCount<=max){
                    goldCount+=1;
                }
                continue;
            }

            //Проверяем право на крупный выигрыш
            if(curGoldCount<=cornucopiaActivity.getLimitGold()){
                goldCount+=1;
                continue;
            }

            //Вероятность
            int goldPro = useGold?cornucopiaActivity.getGoldPro():cornucopiaActivity.getGoldItemPro();
            boolean bigBomb = false;

            if(min<=curGoldCount&&curGoldCount<=max){//Достигнут гарант
                if(RandomUtils.defaultIsGenerate(getGoldLowestPro(min,max,curGoldCount))){
                    bigBomb = true;
                }else if(curGoldCount == max){//Достигнут максимум гаранта
                    bigBomb = true;
                }
            }else{//Обычный розыгрыш
                bigBomb = RandomUtils.defaultIsGenerate(goldPro);
            }

            if(bigBomb){
                int bigGold = (int)(totalPoolGold*((float)cornucopiaActivity.goldPoolPer/10000.0f));
                //Если награда больше максимальной за один раз, выдаём максимум
                if(bigGold>cornucopiaActivity.getGoldOneMaxCount()){
                    bigGold = cornucopiaActivity.getGoldOneMaxCount();
                }
                totalPoolGold=totalPoolGold-bigGold;
                bigCount += 1;
                dailyGoldCount += 1;
                //Запись в историю
                if(goldHistorys.size()>goldHistoryLen){
                    goldHistorys.remove(0);
                }
                goldHistorys.add(new StringBuilder(new StringBuilder(player.getName()).append("_").append(String.valueOf(bigGold))).toString());
                goldList.add(bigGold);
                goldCount = 0;//Сброс гаранта
            }else{
                goldCount += 1;
            }
        }

        //Отправка наград в рюкзак
        List<Item> items = Item.createItems(player.getCareer(), lastList);
        //Бонус за розыгрыш
        int giftNum = cornucopiaActivity.getGiftData().getN()*useGoldTimes;
        if(giftNum>0){
            Item giftItem = Item.createItem(cornucopiaActivity.getGiftData().getI(), giftNum, cornucopiaActivity.getGiftData().getB() == 1);
            items.add(giftItem);
        }

        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaGet);
        }

        //Награда из пула юаней
        int addGoldReward=0;
        for (int gold:goldList) {
            addGoldReward+=gold;
        }
        if(addGoldReward>0){
            if (!Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.GemCoin, addGoldReward, ItemChangeReason.CornucopiaGoldGet, IDConfigUtil.getLogId())) {
                List<Item> addGoldItem = Item.createItems(ItemCoinType.GemCoin, addGoldReward, true);
                Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                        MessageString.System, MessageString.NoBagCell, addGoldItem, ItemChangeReason.CornucopiaGoldGet);
            }
        }

        //Сохранение данных
        roleActDataMap.put(totalCount, totalDrawCount);
        roleActDataMap.put(lowestDrawCount, lowestCount);
        roleActDataMap.put(lowestDrawMap, lowestMap);
        roleActDataMap.put(lowestGoldCount, goldCount);
        roleActDataMap.put(playerHistory, playerHistorys);
        roleActDataMap.put(roleDailyGoldCount, dailyGoldCount);

        actDataMap.put(totalGold, totalPoolGold);
        actDataMap.put(serverHistory, serverHistorys);
        actDataMap.put(goldHistory, goldHistorys);
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 1);//0 вход 1 розыгрыш 2 получение награды за накопление
        result.put("drawCount", totalDrawCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//Текущий счётчик гаранта
        result.put("gold", totalPoolGold);
        result.put("reward", lastList);
        result.put("goldReward", goldList);
        result.put("selfHistory", playerHistorys.subList(playerHistorys.size()-drawCount, playerHistorys.size()));//Только новые
        result.put("serverHistory", serverHistorys.subList(serverHistorys.size()-drawCount, serverHistorys.size()));//Только новые
        result.put("goldHistory", goldHistorys.subList(goldHistorys.size()-bigCount, goldHistorys.size()));//Только новые
        result.put("countReward", new TreeMap<Integer, Integer>());

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private int calcLowestMax(HashMap<Integer, LowestData> lowestDataMap) {
        int max = 0;
        for (int key : lowestDataMap.keySet()){
            LowestData data = lowestDataMap.get(key);
            max = Math.max(max, data.getMax());
        }
        return max;
    }

    private void clearLowestMap(HashMap<Integer, LowestData> lowestData, LinkedHashMap<String, Integer> lowestMap) {
        lowestMap.clear();
        //Инициализация карты гаранта
        for (int i = 0; i < lowestData.size(); i++) {
            lowestMap.put(String.valueOf(i), 0);
        }
    }

    private boolean checkAllLowest(LinkedHashMap<String, Integer> lowestMap) {
        for (Integer state:lowestMap.values()) {
            if(state == 0){
                return false;
            }
        }
        return true;
    }

    /**
     * 100%/(макс-мин)*(текущий n-мин) [мин >=0] = вероятность гаранта.
     * @param min
     * @param max
     * @param goldCount
     * @return
     */
    private int getGoldLowestPro(int min, int max, Integer goldCount) {
        float base = 1/(float)(max-min)*(float)(goldCount-min<=0?0:goldCount-min);
        return (int) (Math.pow(base, 13)*10000);
    }

    private RewardData getRewardByCareer(byte career, List<RewardData> randomReward) {
        for (RewardData cfg : randomReward) {
            if (cfg.getC() == 9 || cfg.getC() == career) {
                return cfg;
            }
        }
        return randomReward.get(0);
    }

    private List<Integer> getExList(CornucopiaActivity cornucopiaActivity, int totalCount) {
        List<Integer> exList = new ArrayList<>();
        for (int i = 0; i <= 2; i++) {//Третий приз (самый низкий) не исключается
            if(i==0&&totalCount<=cornucopiaActivity.getLimitLv()){
                exList.add(0);
            }else if(i==1&&totalCount<=cornucopiaActivity.getLimitLv1()){
                exList.add(1);
            }else if(i==2&&totalCount<=cornucopiaActivity.getLimitLv2()){
                exList.add(2);
            }
        }
        return exList;
    }

    private int getRandomLevelByWeight(HashMap<Integer, LevelWeight> levelWeightMap, boolean useGold, List<Integer> exLv) {
        int result = 3;
        Integer weightSum = 0;
        for (Map.Entry<Integer, LevelWeight> data : levelWeightMap.entrySet()) {
            if(exLv.contains(data.getKey())){
                continue;
            }
            if(useGold){
                weightSum += data.getValue().getGoldWeight();
            }else{
                weightSum += data.getValue().getItemWeight();
            }
        }

        if (weightSum <= 0) {
            return result;
        }
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        int weight = 0;
        for (Map.Entry<Integer, LevelWeight> data : levelWeightMap.entrySet()) {
            if(exLv.contains(data.getKey())){
                continue;
            }
            if(useGold){
                weight = data.getValue().getGoldWeight();
            }else{
                weight = data.getValue().getItemWeight();
            }
            if (m <= n && n < m + weight) {
                result = data.getKey();
                break;
            }
            m += weight;
        }
        return result;
    }

    private List<RewardData> getRandomRewardByWeight(List<RewardPoolData> list, boolean useGold) {
        List<RewardData> result = null;
        Integer weightSum = 0;
        for (RewardPoolData data : list) {
            if(useGold){
                weightSum += data.getGoldWeight();
            }else{
                weightSum += data.getItemWeight();
            }
        }

        if (weightSum <= 0) {
            return null;
        }
        Integer n = new Random().nextInt(weightSum); // n in [0, weightSum)
        Integer m = 0;
        int weight = 0;
        for (RewardPoolData data : list) {
            if(useGold){
                weight = data.getGoldWeight();
            }else{
                weight = data.getItemWeight();
            }
            if (m <= n && n < m + weight) {
                result = data.getRewardData();
                break;
            }
            m += weight;
        }
        return result;
    }

    private List<RewardData> getLowestReward(Integer count, LowestData lowestData, LinkedHashMap<String, Integer> lowestMap) {
        int state = lowestMap.get(String.valueOf(lowestData.getIndex()));
        if(state != 0){//Уже получена награда этого уровня
            return null;
        }
        LowestPro lowestPro = getLowestPro(count, lowestData.getProList());
        if(lowestPro == null){
            return null;
        }

        int pro = lowestPro.getPro();
        if(count==lowestData.getMax()){
            pro = 10000;
        }

        if(RandomUtils.defaultIsGenerate(pro)){
            return lowestData.getRewardData();
        }
        return null;
    }

    private LowestPro getLowestPro(Integer count, List<LowestPro> proList) {
        for (LowestPro pro:proList) {
            if(pro.getMin()<=count&&count<=pro.getMax()){
                return pro;
            }
        }
        return null;
    }

    private LowestData getCurLowestData(Integer count, HashMap<Integer, LowestData> lowestDatas){
        for (LowestData lowestData:lowestDatas.values()) {
            if(lowestData.getMin()<=count&&count<=lowestData.getMax()){
                return lowestData;
            }
        }
        return null;
    }

    /**
     * Получение награды за накопленное количество
     */
    private void dealCountReward(Player player, int count, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        if (cornucopiaActivity.getAccRewardMap().get(count)==null) {
            logger.error("Некорректный уровень для награды за накопление" + count);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(cornucopiaActivity, roleActDataMap);
        int state = countRewardMap.get(String.valueOf(count));
        if(state == 1){//Уже получено
            logger.error("Ошибка получения награды за накопление, уже получено. count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //Проверяем достаточно ли розыгрышей
        if(totalDrawCount<count){
            logger.error("Ошибка получения награды за накопление, недостаточно розыгрышей. count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //Выдаём награду
        List<RewardData> reward = cornucopiaActivity.getAccRewardMap().get(count);
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaCountGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaCountGet);
        }

        //Устанавливаем статус получения
        countRewardMap.put(String.valueOf(count), 1);
        roleActDataMap.put(accReward, countRewardMap);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //Отправка клиенту
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 2);//0 вход 1 розыгрыш 2 награда за накопление
        result.put("drawCount", totalDrawCount);
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", new ArrayList<>());
        result.put("serverHistory", new ArrayList<>());
        result.put("goldHistory", new ArrayList<>());
        result.put("countReward", countRewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private LinkedHashMap<String, Integer> getCountRewardMap(CornucopiaActivity cornucopiaActivity, ConcurrentHashMap<String, Object> roleActDataMap) {
        LinkedHashMap<String, Integer> countRewardMap;
        if(roleActDataMap.get(accReward)!=null){
            countRewardMap = (LinkedHashMap<String, Integer>)roleActDataMap.get(accReward);
        }else{
            countRewardMap = new LinkedHashMap<>();
            for (Integer accCount:cornucopiaActivity.getAccRewardMap().keySet()) {
                countRewardMap.put(String.valueOf(accCount), 0);
            }
            roleActDataMap.put(accReward, countRewardMap);
        }
        return countRewardMap;
    }

    /**
     * Получение награды за активность
     */
    private void dealActiveReward(Player player, int count, CornucopiaActivity cornucopiaActivity, ActivityConfig actCfg) {
        if (cornucopiaActivity.getFreeGiftMap().get(count)==null) {
            logger.error("Некорректный уровень для награды за активность" + count);
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        LinkedHashMap<String, Integer> activeRewardMap = getActiveRewardMap(cornucopiaActivity, roleActDataMap);
        int state = activeRewardMap.get(String.valueOf(count));
        if(state == 1){//Уже получено
            logger.error("Ошибка получения награды за активность, уже получено. count="+count);
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //Проверяем достаточно ли активности
        if(player.getDailyActiveData().getActiveNum()<count){
            logger.error("Ошибка получения награды за активность, недостаточно очков активности. count="+count+",curCount="+player.getDailyActiveData().getActiveNum());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.TOUZI_GETFAIL);
            return;
        }

        //Выдаём награду
        List<RewardData> reward = cornucopiaActivity.getFreeGiftMap().get(count);
        List<Item> items = Item.createItems(player.getCareer(), reward);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.CornucopiaActiveGet, IDConfigUtil.getLogId())) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System,
                    MessageString.System, MessageString.NoBagCell, items, ItemChangeReason.CornucopiaActiveGet);
        }

        //Устанавливаем статус получения
        activeRewardMap.put(String.valueOf(count), 1);
        roleActDataMap.put(activeReward, activeRewardMap);

        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
        //Отправка клиенту
        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 3);//0 вход 1 розыгрыш 2 награда за накопление 3 награда за активность
        result.put("drawCount", totalDrawCount);
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", new ArrayList<>());
        result.put("serverHistory", new ArrayList<>());
        result.put("goldHistory", new ArrayList<>());
        result.put("countReward", new HashMap<>());
        result.put("activeState", activeRewardMap);

        ActivityMessage.ResActivityDeal.Builder pb = ActivityMessage.ResActivityDeal.newBuilder();
        pb.setData(JsonUtils.toJSONString(result));
        pb.setType(actCfg.getType());
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, pb.build().toByteArray());
    }

    private LinkedHashMap<String, Integer> getActiveRewardMap(CornucopiaActivity cornucopiaActivity, ConcurrentHashMap<String, Object> roleActDataMap) {
        LinkedHashMap<String, Integer> activeRewardMap;
        if(roleActDataMap.get(activeReward)!=null){
            activeRewardMap = (LinkedHashMap<String, Integer>)roleActDataMap.get(activeReward);
        }else{
            activeRewardMap = new LinkedHashMap<>();
            for (Integer count:cornucopiaActivity.getFreeGiftMap().keySet()) {
                activeRewardMap.put(String.valueOf(count), 0);
            }
            roleActDataMap.put(activeReward, activeRewardMap);
        }
        return activeRewardMap;
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        CornucopiaActivity data = JsonUtils.parseObject(customStr, CornucopiaActivity.class);
        actCfg.getCustomCfgMap().put(configData, data);
        actCfg.getCustomCfgMap().put("client", data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {
        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity == null) {
            logger.error("Конфигурация для активности Рог изобилия отсутствует");
            return null;
        }

        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        Integer totalDrawCount = (Integer) roleActDataMap.getOrDefault(totalCount, 0);
        Integer lowestCount = (Integer) roleActDataMap.getOrDefault(lowestDrawCount, 0);

        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());

        //История для клиента
        List<String> playerHistorys;
        List<String> serverHistorys;
        List<String> goldHistorys;
        if(roleActDataMap.get(playerHistory) != null){
            playerHistorys = (List<String>)roleActDataMap.get(playerHistory);
        }else{
            playerHistorys = new ArrayList<>();
            roleActDataMap.put(playerHistory, playerHistorys);
        }
        if(actDataMap.get(serverHistory) != null){
            serverHistorys = (List<String>)actDataMap.get(serverHistory);
        }else{
            serverHistorys = new ArrayList<>();
            actDataMap.put(serverHistory,serverHistorys);
        }
        if(actDataMap.get(goldHistory) != null){
            goldHistorys = (List<String>)actDataMap.get(goldHistory);
        }else{
            goldHistorys = new ArrayList<>();
            actDataMap.put(goldHistory,goldHistorys);
        }

        //Карта гаранта
        LinkedHashMap<String, Integer> lowestMap;
        if(roleActDataMap.get(lowestDrawMap) != null){
            lowestMap = (LinkedHashMap<String, Integer>) roleActDataMap.get(lowestDrawMap);
        }else{
            lowestMap = new LinkedHashMap<>();
            for (int i = 0; i < cornucopiaActivity.getLowestData().size(); i++) {
                lowestMap.put(String.valueOf(i), 0);
            }
            roleActDataMap.put(lowestDrawMap, lowestMap);
        }

        LinkedHashMap<String, Integer> countRewardMap = getCountRewardMap(cornucopiaActivity, roleActDataMap);
        LinkedHashMap<String, Integer> activeRewardMap = getActiveRewardMap(cornucopiaActivity, roleActDataMap);

        HashMap<String, Object> result = new HashMap<>();
        result.put("operate", 0);//0 вход 1 розыгрыш 2 награда за накопление
        result.put("drawCount", totalDrawCount);
        result.put("drawLowestMap", lowestMap);
        result.put("drawLowestCount", lowestCount);//Текущий счётчик гаранта
        result.put("gold", totalPoolGold);
        result.put("reward", new ArrayList<>());
        result.put("goldReward", new ArrayList<>());
        result.put("selfHistory", playerHistorys);
        result.put("serverHistory", serverHistorys);
        result.put("goldHistory", goldHistorys);
        result.put("countReward", countRewardMap);
        result.put("activeState", activeRewardMap);
        return JsonUtils.toJSONString(result);
    }

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }

    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());
        roleActDataMap.put(roleDailyGoldCount, 0);

        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        if (cornucopiaActivity != null) {
            LinkedHashMap<String, Integer> activeRewardMap = new LinkedHashMap<>();
            for (Integer count:cornucopiaActivity.getFreeGiftMap().keySet()) {
                activeRewardMap.put(String.valueOf(count), 0);
            }
            roleActDataMap.put(activeReward, activeRewardMap);
        }
    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        if(actDataMap!=null){
            //Очистка дневного лимита системы
            actDataMap.put(dailyAddGold, 0);
            Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
        }
    }

    @Override
    public void fiveClockDeal(ActivityConfig actCfg) {

    }


    @Override
    public void everyHourDeal(ActivityConfig actCfg) {
        int curHour = TimeUtils.getDayOfHour(TimeUtils.Time());
        if(1<curHour&&curHour<12){//С 12 до 1 ночи
            return;
        }

        CornucopiaActivity cornucopiaActivity = (CornucopiaActivity) actCfg.getCustomCfgMap().get(configData);
        ConcurrentHashMap<String, Object> actDataMap = Manager.activityManager.deal().getActivityData(actCfg.getType());
        Integer totalPoolGold = (Integer)actDataMap.getOrDefault(totalGold, cornucopiaActivity.getGoldInitCount());
        Integer dailySysAddGold = (Integer)actDataMap.getOrDefault(dailyAddGold, 0);

        if(totalPoolGold>=cornucopiaActivity.getSysAddBaseValue()){
            return;
        }
        if(dailySysAddGold>=cornucopiaActivity.getSysAddLimit()){
            return;
        }

        int addGold = cornucopiaActivity.getSysAddCount();
        addGold=addGold+(int)(addGold*((float)RandomUtils.random(1000, 3000)/10000.0f));

        if(totalPoolGold+addGold>cornucopiaActivity.getGoldMaxCount()){
            totalPoolGold = cornucopiaActivity.getGoldMaxCount();
        }else{
            totalPoolGold+=addGold;
        }
        dailySysAddGold+=addGold;

        actDataMap.put(totalGold, totalPoolGold);
        actDataMap.put(dailyAddGold, dailySysAddGold);

        Manager.activityManager.deal().saveActData(actCfg.getType(), actDataMap);
    }

    @Override
    public boolean bossDrop(Player player, int bossId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean boxDrop(Player player, int boxId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public boolean cloneDrop(Player player, int cloneId, ActivityConfig actCfg) {
        return false;
    }

    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum, ActivityConfig actCfg) {

    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.Cornucopia);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object old = activityConfig.getCustomCfgMap().get(configData);
            if (old == null) {
                return;
            }
            String customStr = JsonUtils.toJSONString(old);
            CornucopiaActivity newData = JsonUtils.parseObject(customStr, CornucopiaActivity.class);
            activityConfig.getCustomCfgMap().put(configData, newData);
        }
    }

    @Override
    public int getId() {
        return ScriptEnum.CornucopiaScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

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

    //Вес уровня награды
    static class LevelWeight {
        private int itemWeight;  //Вес для розыгрыша предметами
        private int goldWeight;  //Вес для розыгрыша юанями

        public int getItemWeight() {
            return itemWeight;
        }

        public void setItemWeight(int itemWeight) {
            this.itemWeight = itemWeight;
        }

        public int getGoldWeight() {
            return goldWeight;
        }

        public void setGoldWeight(int goldWeight) {
            this.goldWeight = goldWeight;
        }
    }

    //Вероятность гаранта по сегментам
    static class LowestPro {
        private int min;
        private int max;
        private int pro;

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
    }

    //Данные гаранта
    static class LowestData {
        private int index;//Номер гаранта
        private int min;  //Минимальное количество розыгрышей
        private int max;  //Максимальное количество розыгрышей
        private List<LowestPro> proList;//Вероятность по сегментам
        private List<RewardData> rewardData;//Награда гаранта

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
    }

    //Данные пула наград
    static class RewardPoolData {
        private int itemWeight;  //Вес для предметов
        private int goldWeight; //Вес для юаней
        private List<RewardData> rewardData;

        public int getItemWeight() {
            return itemWeight;
        }

        public void setItemWeight(int itemWeight) {
            this.itemWeight = itemWeight;
        }

        public int getGoldWeight() {
            return goldWeight;
        }

        public void setGoldWeight(int goldWeight) {
            this.goldWeight = goldWeight;
        }

        public List<RewardData> getRewardData() {
            return rewardData;
        }

        public void setRewardData(List<RewardData> rewardData) {
            this.rewardData = rewardData;
        }
    }

    //Данные активности Рог изобилия
    static class CornucopiaActivity extends ActivityLucky {
        private String client;  //Данные для клиента
        //Настройки розыгрыша
        private int limitLv;//После n розыгрышей возможен крупный выигрыш
        private int limitLv1;//После n розыгрышей возможен выигрыш первого уровня
        private int limitLv2;//После n розыгрышей возможен выигрыш второго уровня
        private int limitLv3;//После n розыгрышей возможен выигрыш третьего уровня

        private int itemId;     //ID предмета
        private int oneCostItem;//Стоимость в предметах за 1 раз
        private int tenCostItem;//Стоимость в предметах за 10 раз
        private int oneCostGold;//Стоимость в юанях за 1 раз
        private int tenCostGold;//Стоимость в юанях за 10 раз
        private RewardData giftData;//Бонус за каждый розыгрыш

        //Пул юаней
        private int goldItemPro;    //Вероятность выигрыша юанями при розыгрыше предметами
        private int goldPro;        //Вероятность выигрыша юанями при розыгрыше юанями
        private int goldInitCount;  //Начальное количество в пуле
        private int goldMaxCount;   //Максимальное количество в пуле
        private int goldOneMaxCount;//Максимальный выигрыш за один раз
        private int goldPoolPer;    //Процент от пула при выигрыше
        private int goldDailyCount; //Дневной лимит выигрышей юанями на игрока
        private int goldBigMin;     //Минимальное количество для гаранта юаней
        private int goldBigMax;     //Максимальное количество для гаранта юаней
        private int limitGold;      //После n розыгрышей возможен крупный выигрыш юанями

        private int sysAddBaseValue;//Базовое значение для добавления системой
        private int sysAddCount;    //Количество добавления системой за раз
        private int sysAddLimit;    //Дневной лимит добавления системой

        //Вес уровней наград <уровень, вес>
        private HashMap<Integer, LevelWeight> levelWeightMap;
        //Информация о пулах <уровень, список наград>
        private HashMap<Integer, List<RewardPoolData>> rewardPoolMap;
        //Гарант <номер, данные>
        private HashMap<Integer, LowestData> lowestData;
        //Награда за накопление <количество, награда>
        private HashMap<Integer, List<RewardData>> accRewardMap;
        //Награда за активность <очки активности, награда>
        private HashMap<Integer, List<RewardData>> freeGiftMap;

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public int getLimitLv() {
            return limitLv;
        }

        public void setLimitLv(int limitLv) {
            this.limitLv = limitLv;
        }

        public int getLimitLv1() {
            return limitLv1;
        }

        public void setLimitLv1(int limitLv1) {
            this.limitLv1 = limitLv1;
        }

        public int getLimitLv2() {
            return limitLv2;
        }

        public void setLimitLv2(int limitLv2) {
            this.limitLv2 = limitLv2;
        }

        public int getLimitLv3() {
            return limitLv3;
        }

        public void setLimitLv3(int limitLv3) {
            this.limitLv3 = limitLv3;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getOneCostItem() {
            return oneCostItem;
        }

        public void setOneCostItem(int oneCostItem) {
            this.oneCostItem = oneCostItem;
        }

        public int getTenCostItem() {
            return tenCostItem;
        }

        public void setTenCostItem(int tenCostItem) {
            this.tenCostItem = tenCostItem;
        }

        public int getOneCostGold() {
            return oneCostGold;
        }

        public void setOneCostGold(int oneCostGold) {
            this.oneCostGold = oneCostGold;
        }

        public int getTenCostGold() {
            return tenCostGold;
        }

        public void setTenCostGold(int tenCostGold) {
            this.tenCostGold = tenCostGold;
        }

        public RewardData getGiftData() {
            return giftData;
        }

        public void setGiftData(RewardData giftData) {
            this.giftData = giftData;
        }

        public int getGoldItemPro() {
            return goldItemPro;
        }

        public void setGoldItemPro(int goldItemPro) {
            this.goldItemPro = goldItemPro;
        }

        public int getGoldPro() {
            return goldPro;
        }

        public void setGoldPro(int goldPro) {
            this.goldPro = goldPro;
        }

        public int getGoldInitCount() {
            return goldInitCount;
        }

        public void setGoldInitCount(int goldInitCount) {
            this.goldInitCount = goldInitCount;
        }

        public int getGoldMaxCount() {
            return goldMaxCount;
        }

        public void setGoldMaxCount(int goldMaxCount) {
            this.goldMaxCount = goldMaxCount;
        }

        public int getGoldOneMaxCount() {
            return goldOneMaxCount;
        }

        public void setGoldOneMaxCount(int goldOneMaxCount) {
            this.goldOneMaxCount = goldOneMaxCount;
        }

        public int getGoldDailyCount() {
            return goldDailyCount;
        }

        public void setGoldDailyCount(int goldDailyCount) {
            this.goldDailyCount = goldDailyCount;
        }

        public int getGoldPoolPer() {
            return goldPoolPer;
        }

        public void setGoldPoolPer(int goldPoolPer) {
            this.goldPoolPer = goldPoolPer;
        }

        public int getSysAddBaseValue() {
            return sysAddBaseValue;
        }

        public void setSysAddBaseValue(int sysAddBaseValue) {
            this.sysAddBaseValue = sysAddBaseValue;
        }

        public int getSysAddCount() {
            return sysAddCount;
        }

        public void setSysAddCount(int sysAddCount) {
            this.sysAddCount = sysAddCount;
        }

        public int getSysAddLimit() {
            return sysAddLimit;
        }

        public void setSysAddLimit(int sysAddLimit) {
            this.sysAddLimit = sysAddLimit;
        }

        public int getGoldBigMin() {
            return goldBigMin;
        }

        public void setGoldBigMin(int goldBigMin) {
            this.goldBigMin = goldBigMin;
        }

        public int getGoldBigMax() {
            return goldBigMax;
        }

        public void setGoldBigMax(int goldBigMax) {
            this.goldBigMax = goldBigMax;
        }

        public int getLimitGold() {
            return limitGold;
        }

        public void setLimitGold(int limitGold) {
            this.limitGold = limitGold;
        }

        public HashMap<Integer, LevelWeight> getLevelWeightMap() {
            return levelWeightMap;
        }

        public void setLevelWeightMap(HashMap<Integer, LevelWeight> levelWeightMap) {
            this.levelWeightMap = levelWeightMap;
        }

        public HashMap<Integer, List<RewardPoolData>> getRewardPoolMap() {
            return rewardPoolMap;
        }

        public void setRewardPoolMap(HashMap<Integer, List<RewardPoolData>> rewardPoolMap) {
            this.rewardPoolMap = rewardPoolMap;
        }

        public HashMap<Integer, LowestData> getLowestData() {
            return lowestData;
        }

        public void setLowestData(HashMap<Integer, LowestData> lowestData) {
            this.lowestData = lowestData;
        }

        public HashMap<Integer, List<RewardData>> getAccRewardMap() {
            return accRewardMap;
        }

        public void setAccRewardMap(HashMap<Integer, List<RewardData>> accRewardMap) {
            this.accRewardMap = accRewardMap;
        }

        public HashMap<Integer, List<RewardData>> getFreeGiftMap() {
            return freeGiftMap;
        }

        public void setFreeGiftMap(HashMap<Integer, List<RewardData>> freeGiftMap) {
            this.freeGiftMap = freeGiftMap;
        }

        //endregion
    }
}