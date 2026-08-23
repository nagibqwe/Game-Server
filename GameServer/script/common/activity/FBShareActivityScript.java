package common.activity;

import com.data.ItemChangeReason;
import com.data.MessageString;
import com.game.activity.manager.ActivityManager;

import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.activity.struct.ActivityType;
import com.game.activity.struct.RewardData;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bi.struct.BIActiityTypeEnum;
import com.game.chat.structs.Notify;
import com.game.db.bean.ActivityConfigBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.json.TypeReference;
import game.core.util.IDConfigUtil;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author gaozhaoguang
 * @desc Скрипт активности "Публикация в FB" 300020
 * @date Created on 2020/8/18 18:06
 **/
public class FBShareActivityScript implements IActivityScript {
    private static final Logger logger = LogManager.getLogger(FBShareActivityScript.class);

    //Основной ключ для сохранения в менеджере активностей
    final String CN_MAIN_KEY = "fbShare";
    //Данные для отправки клиенту
    final String CN_RES_CLIENT_KEY = "client";
    //Ключ запроса в данных от клиента
    final String CN_C_REQUEST_KEY = "request";
    //Запрос на получение награды от клиента
    final String CN_C_REQ_AWARD_KEY = "reqAward";
    //Запрос на публикацию от клиента
    final String CN_C_REQ_SHARE_KEY = "reqShare";
    //Ключ статуса получения награды в данных игрока
    final String CN_P_AWARD_IS_GET_KEY = "shareState";

    //Ещё не публиковал
    final int CN_STATE_NONE = 0;
    //Уже опубликовал
    final int CN_STATE_SHARED = 1;
    //Уже получил награду
    final int CN_STATE_AWARDED = 2;

    @Override
    public int getId() {
        return ScriptEnum.FBShareActivityScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void reload() {
        if(Manager.activityManager == null){
            return;
        }
        List<ActivityConfig> actCfgList = Manager.activityManager.deal().getActCfgListByActLogicID(ActivityType.FBShare);
        for (ActivityConfig activityConfig:actCfgList) {
            if (activityConfig == null) {
                return;
            }
            Object obj = activityConfig.getCustomCfgMap().get(CN_MAIN_KEY);
            if (obj != null) {
                String customStr = JsonUtils.toJSONString(obj);
                FBShareChristmasConfig newData = JsonUtils.toJavaObject(customStr, FBShareChristmasConfig.class);
                activityConfig.getCustomCfgMap().put(CN_MAIN_KEY, newData);
            }
        }
    }

    @Override
    public void onReqActivityDeal(Player player, String dataStr, ActivityConfig actCfg) {
        logger.error("onReqActivityDeal:"+dataStr);
        ConcurrentHashMap<String, Object> data = JsonUtils.parseObject(dataStr, new TypeReference<ConcurrentHashMap<String, Object>>() {
        });
        String reqMethod = Utils.getOrDefaultFromMap(data, CN_C_REQUEST_KEY, "");
        if (reqMethod.equals(CN_C_REQ_AWARD_KEY)) {
            onReqAwardHandler(player,actCfg);
        } else if (reqMethod.equals(CN_C_REQ_SHARE_KEY)) {
            onReqShareHandler(player,actCfg);
        } else{
            logger.error("Получен неизвестный запрос:" + reqMethod);
        }
    }

    @Override
    public boolean parseCustomConfig(ActivityConfig actCfg, String customStr) {
        logger.error("parseCustomConfig:"+customStr);
        FBShareChristmasConfig data = JsonUtils.toJavaObject(customStr, FBShareChristmasConfig.class);
        actCfg.getCustomCfgMap().put(CN_MAIN_KEY, data);
        actCfg.getCustomCfgMap().put(CN_RES_CLIENT_KEY, data.getClient());
        return true;
    }

    @Override
    public boolean updateCustomConfig(ActivityConfig actCfg, String customStr) {
        return parseCustomConfig(actCfg, customStr);
    }

    @Override
    public String getActivityDataStr(ActivityConfig actCfg, long roleId) {

        //Проверка, онлайн ли игрок
        Player player = Manager.playerManager.getPlayerOnline(roleId);
        if (player == null) {
            logger.error("getActivityDataStr: игрок уже вышел! roleID:" + roleId);
            return "";
        }
        //Обновление данных игрока
        onUpdateActiveDataHandler(player,actCfg);

        //Получение данных активности игрока для отправки
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(roleId, actCfg.getType());
        HashMap<String, Object> groupData = new HashMap<>();
        groupData.put(CN_P_AWARD_IS_GET_KEY, roleActDataMap.getOrDefault(CN_P_AWARD_IS_GET_KEY,0));
        return JsonUtils.toJSONString(groupData);
    }

    @Override
    public void rechargeDeal(Player player,int getGoodsCfgId, int rechargeNum, ActivityConfig actCfg) {

    }

    /**
     * Загрузка данных при входе
     *
     * @param player игрок
     */
    @Override
    public void playerOnline(Player player, ActivityConfig actCfg) {
        onUpdateActiveDataHandler(player,actCfg);
    }

    /**
     * Обновление в 00:00
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player, ActivityConfig actCfg) {
        onUpdateActiveDataHandler(player,actCfg);
    }

    @Override
    public void fiveClockPlayerDeal(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void zeroClockDeal(ActivityConfig actCfg) {

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

    @Override
    public void activityEndDeal(ActivityConfig actCfg) {

    }
    public void consumeDeal(Player player,int coinType, int consumeNum, ActivityConfig actCfg){

    }

    //region //Внутренние методы

    /**
     * Обновление данных игрока
     *
     * @param player
     */
    private void onUpdateActiveDataHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            return;
        }
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(),actCfg.getType());
        //1.Инициализация всех данных
        Utils.putNoExistInMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
    }
    /**
     * Обработка запроса на публикацию
     *
     * @param player
     */
    private void onReqShareHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.Получение данных активности игрока
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        //2.Проверка статуса
        int shareState = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
        if(shareState != CN_STATE_NONE){
            logger.error("onReqShareHandler: ошибка публикации в активности FB Share! roleID:"+player.getId()+";; shareState:"+shareState);
            return;
        }
        //3.Установка флага публикации
        roleActDataMap.put(CN_P_AWARD_IS_GET_KEY, CN_STATE_SHARED);

        //4.Синхронизация с клиентом
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));
    }

    /**
     * Обработка запроса на получение награды
     *
     * @param player
     */
    private void onReqAwardHandler(Player player, ActivityConfig actCfg) {
        if (!checkActivityValid(actCfg)) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_ACTIVITY_INVALID);
            return;
        }
        //1.Получение данных активности игрока
        ConcurrentHashMap<String, Object> roleActDataMap = Manager.activityManager.deal().getRoleActivityData(player.getId(), actCfg.getType());

        //2.Проверка статуса
        int shareState = Utils.getOrDefaultFromMap(roleActDataMap, CN_P_AWARD_IS_GET_KEY, CN_STATE_NONE);
        if(shareState != CN_STATE_SHARED) {
            logger.error("onReqShareHandler: ошибка получения награды в активности FB Share! roleID:"+player.getId()+";; shareState:"+shareState);
            return;
        }

        //3.Проверка корректности награды в конфигурации
        FBShareChristmasConfig cfg = (FBShareChristmasConfig) actCfg.getCustomCfgMap().get(CN_MAIN_KEY);
        if (cfg == null || cfg.getAwardList() == null || cfg.getAwardList().size() == 0) {
            logger.error("onReqAwardHandler: ошибка в конфигурации награды активности FB Share! cfg.getAwardList() == null || cfg.getAwardList().size() == 0 roleID:"+player.getId());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.S_UNKNOW_ERROR);
            return;
        }

        //4.Формирование списка наград
        List<Item> items = new ArrayList<>();
        for (RewardData rd : cfg.getAwardList()) {
            if (rd.getC() == player.getCareer() || rd.getC() == 9) {
                items.add(Item.createItem(rd.getI(), rd.getN(), rd.getB() > 0));
            }
        }

        //4.1 Проверка корректности награды
        if (items.size() == 0) {
            logger.error("onReqAwardHandler: ошибка в награде для класса! Career:" +player.getCareer());
            return;
        }

        //4.2 Проверка места в рюкзаке
        if (Manager.backpackManager.manager().onHasAddSpaces(player, items) != 0) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.NoBagCell);
            return;
        }

        //5.Установка флага получения награды
        roleActDataMap.put(CN_P_AWARD_IS_GET_KEY, CN_STATE_AWARDED);

        //6.Выдача награды
        List<Item> itemList = Item.clone(items);
        Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.FBShareChristmasActivityGet, IDConfigUtil.getLogId());

        //7.Синхронизация с клиентом
        Manager.activityManager.deal().sendActivityDataChange(player, actCfg.getType());
        Manager.activityManager.deal().saveRoleActData(player.getId(), Manager.activityManager.getRoleActDatas().get(player.getId()));

        Manager.biManager.getScript().biActivity(player, BIActiityTypeEnum.FBShare, ItemChangeReason.FBShareChristmasActivityGet);
    }


    /**
     * Проверка активности на валидность
     *
     * @return
     */
    private boolean checkActivityValid(ActivityConfig actCfg) {
        if (actCfg == null) {
            return false;
        }
        if (!actCfg.isActiviting()) {
            logger.error("Активность остановлена: " + actCfg.getType());
            return false;
        }
        return true;
    }

    //endregion


    //region //Внутренние классы

    /**
     * Конфигурация активности "Публикация в FB"
     */
    private static class FBShareChristmasConfig {
        //Список наград
        private List<RewardData> awardList;
        //Данные для клиента
        private String client ;

        public List<RewardData> getAwardList() {
            return awardList;
        }

        public void setAwardList(List<RewardData> awardList) {
            this.awardList = awardList;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }
    }

    //endregion

    //region //Тестовые методы
    //Тест получения награды
    public static void testAward(Player player) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player,  ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0), "{'request':'reqAward'}");
    }

    //Тест публикации
    public static void testShare(Player player) {
        ActivityManager.getInstance().deal().onReqActivityDeal(player, ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0), "{'request':'reqShare'}");
    }

    //Тест регистрации активности
    public static void testRegisterActivity() {
        ActivityConfigBean acb = new ActivityConfigBean();
        acb.setId(9);
        acb.setType(ActivityManager.getInstance().deal().toActType(ActivityType.FBShare ,0));
        acb.setMinLv(0);
        acb.setMaxLv(801);
        acb.setTag((byte) 1);
        acb.setSort((byte) 1);
        acb.setName("Публикация в FB на праздник");
        acb.setBeginTime(TimeUtils.Time() - 24*60*60*1000);
        acb.setEndTime(TimeUtils.Time() + 24*60*60*1000);
        acb.setIsDelete((byte) 0);

        List<RewardData> list = new ArrayList<>();
        for (int i = 1; i < 3; i++) {
            RewardData item = new RewardData();
            item.setI(12004);
            item.setN(i * 10);
            item.setB(1);
            item.setC(9);
            list.add(item);
        }

        HashMap<String, Object> hm = new HashMap();
        hm.put("id", 111);
        hm.put("awardList", list);
        hm.put("shareState", 0);

        HashMap<String, Object> clientMap = new HashMap<>();
        clientMap.put("awardList", list);

        hm.put("client", JsonUtils.toJSONString(clientMap));

        acb.setCustom(JsonUtils.toJSONString(hm));
        Manager.activityManager.deal().registerActivityBean(acb);
    }

    //endregion
}