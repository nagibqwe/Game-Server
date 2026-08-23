package common.activity;

import com.game.activity.log.ActivityDonateLog;
import com.game.activity.log.ActivityGetLog;
import com.game.activity.manager.ActivityManager;
import com.game.activity.script.IActivityManageScript;
import com.game.activity.script.IActivityScript;
import com.game.activity.struct.ActivityConfig;
import com.game.backgrand.manager.BackGrandServer;
import com.game.backgrand.script.IBackCommandScript;
import com.game.count.structs.BaseCountType;
import com.game.count.structs.Count;
import com.game.db.bean.ActivityConfigBean;
import com.game.db.bean.ActivityDataBean;
import com.game.db.bean.RoleActivityDataBean;
import com.game.db.bean.TagInfoBean;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.script.structs.ScriptEnum;
import com.game.server.DbSqlName;
import com.game.server.thread.SaveServer;
import com.game.utils.MessageUtils;
import game.core.dblog.LogService;
import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.ActivityMessage;
import io.netty.channel.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс управления игровыми событиями (данные событий настраиваются в GM панели)
 */
public class ActivityManageScript implements IActivityManageScript {

    private static final Logger log = LogManager.getLogger(ActivityManageScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ActivityScriptBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void onReqActivityDeal(Player player, int actType, String dataStr) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            log.error("Скрипт не найден. scriptId：" + ScriptEnum.getActivityScriptId(actType / 1000));
            return;
        }
        if (!checkOpen(player, actType)) {
            return;
        }

        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actType);
        if (actCfg == null) {
            log.error("ActivityConfig не найден. type：" + actType);
            return;
        }
        try{
            as.onReqActivityDeal(player, dataStr, actCfg);
        }catch (Exception e){
            log.error(e+", Ошибка выполнения действия события",e);
        }
    }

    @Override
    public void sendActivityDealMessage(Player player, int actType, String dataStr) {
        ActivityMessage.ResActivityDeal.Builder msg = ActivityMessage.ResActivityDeal.newBuilder();
        msg.setType(actType);
        msg.setData(dataStr);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityDeal.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void onReqActivity(Player player, int actType) {
        if (!checkOpen(player, actType)) {
            return;
        }
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = getActivityDataBuilder(player.getId(), actType);
        msg.setAct(actMess);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void playerOnline(Player player) {

        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            as.playerOnline(player, actCfg);
        }

        ActivityMessage.ResActivityList.Builder msg = ActivityMessage.ResActivityList.newBuilder();
        for (ActivityConfig actCfg : Manager.activityManager.getActCfgMap().values()) {
            ActivityMessage.Activity.Builder actMess = getActivityBuilder(player.getId(), actCfg);
            msg.addActList(actMess);
        }
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityList.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private ActivityMessage.Activity.Builder getActivityBuilder(long roleId, ActivityConfig actCfg) {

        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actCfg.getType());
        actMess.setActConfig(getActivityConfigStr(roleId, actCfg));

        String actData = getActivityDataStr(roleId, actCfg.getType());
        if (actData == null || actData.equals("")) {
            actMess.setActData("{}");
        } else {
            actMess.setActData(actData);
        }
        return actMess;
    }

    private ActivityMessage.Activity.Builder getActivityDataBuilder(long roleId, int actType) {
        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actType);

        String actData = getActivityDataStr(roleId, actType);
        if (actData == null || actData.equals("")) {
            actMess.setActData("{}");
        } else {
            actMess.setActData(actData);
        }
        return actMess;
    }

    @Override
    public ConcurrentHashMap<String, Object> getRoleActivityData(long roleId, int actType) {
        ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> roleActDataMap = Manager.activityManager.getRoleActDatas().get(roleId);
        if (roleActDataMap == null) {
            roleActDataMap = new ConcurrentHashMap<>();
            Manager.activityManager.getRoleActDatas().put(roleId, roleActDataMap);
        }
        ConcurrentHashMap<String, Object> roleActData = roleActDataMap.get(actType);
        if (roleActData == null) {
            roleActData = new ConcurrentHashMap<>();
            roleActDataMap.put(actType, roleActData);
        }
        return roleActData;
    }

    @Override
    public ConcurrentHashMap<String, Object> getActivityData(int actType) {
        ConcurrentHashMap<String, Object> ActDataMap = Manager.activityManager.getActDatas().get(actType);
        if (ActDataMap == null) {
            ActDataMap = new ConcurrentHashMap<>();
            Manager.activityManager.getActDatas().put(actType, ActDataMap);
        }
        return ActDataMap;
    }

    @Override
    public boolean checkLevel(int level, ActivityConfig actCfg) {
        return level >= actCfg.getMinLv() && actCfg.getMaxLv() >= level;
    }

    /**
     * Проверка активности события
     * @param player
     * @param type
     * @return
     */
    public boolean checkOpen(Player player, int type) {
        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
        if (actCfg == null) {
            return false;
        }
        if (!actCfg.isActiviting()) {
            return false;
        }
        /// if (player != null) {
        ///     if (!checkLevel(player.getLevel(), actCfg)) {
        ///         return false;
        ///     }
        /// }
        return true;
    }

    private String getActivityConfigStr(long roleId, ActivityConfig actCfg) {
        HashMap<String, Object> resultMap = new HashMap<>();
        resultMap.put("type", actCfg.getType());
        resultMap.put("minLv", actCfg.getMinLv());
        resultMap.put("maxLv", actCfg.getMaxLv());
        resultMap.put("tag", actCfg.getTag());
        resultMap.put("sort", actCfg.getSort());
        resultMap.put("name", actCfg.getName());

        long bTime = actCfg.getBeginTime();
        long eTime = actCfg.getEndTime();

        resultMap.put("beginTime", bTime);
        resultMap.put("endTime", eTime);
        resultMap.put("isDelete", actCfg.getIsDelete());

        //Пользовательские данные для клиента
        PlayerWorldInfo player = Manager.playerManager.getPlayerWorldInfo(roleId);
        String custom = "client";
        if (actCfg.getCustomCfgMap().containsKey(custom)) {
            resultMap.put("custom", actCfg.getCustomCfgMap().get(custom));
        }
        if (actCfg.getCustomCfgMap().containsKey(custom + player.getCareer())) {
            resultMap.put("custom", actCfg.getCustomCfgMap().get(custom + player.getCareer()));
        }
        return JsonUtils.toJSONString(resultMap);
    }

    private String getActivityDataStr(long roleId, int actType) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            return null;
        }
        ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actType);
        if (actCfg == null) {
            log.error("ActivityConfig не найден, type=" + actType);
            return null;
        }
        String dataStr = "";
        try{
            dataStr = as.getActivityDataStr(actCfg, roleId);
        }catch (Exception e){
            log.error("Ошибка получения данных события, actId="+actCfg.getId(),e);
        }

        return dataStr;
    }

    @Override
    public boolean registerActivityBean(ActivityConfigBean actBean) {
        try {
            if (actBean == null) {
                return false;
            }

            if (actBean.getType() <= 0) {
                return false;
            }
            Manager.countManager.setCount(ActivityManager.getInstance(), BaseCountType.Activity, actBean.getId(), Count.RefreshType.CountType_Day, 0);

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(actBean.getType());
            if (actCfg != null) {//Уже зарегистрировано, обновление конфигурации
                //Уже есть активность того же типа, исключаем несовпадающие по времени
                if (!isActiviting(actBean, TimeUtils.Time())) {
                    actBean.setState((byte) 0);
                    addPreMap(actBean);
                    return true;
                }

                IActivityScript as = getScript(actBean.getType());
                if (as == null) {
                    return false;
                }

                ConcurrentHashMap<String, Object> oldCustomMap = actCfg.getCustomCfgMap();

                actCfg.setCustomCfgMap(new ConcurrentHashMap<String, Object>());
                //Проверка парсинга пользовательских настроек
                if (!(as.parseCustomConfig(actCfg, actBean.getCustom()))) {
                    log.error("Ошибка парсинга пользовательских данных события, actId=" + actCfg.getId());
                    actCfg.setCustomCfgMap(oldCustomMap);
                    return false;
                }

                //Завершение предыдущей активности
                as.activityEndDeal(actCfg);

                //Удаление предыдущей активности
                Manager.activityManager.deal().delActConfig(actCfg.getId());

                //Очистка данных предыдущей активности
                cleanActData(actCfg.getType());

                //Обновление базовой информации
                actCfg.beanToActivityBaseConfig(actBean);
                actCfg.setIsDelete((byte) 0);

                //Обработка после изменения конфигурации
                sendActivityOpen(actCfg);
                return true;
            }

            //Проверка возможности добавления активности
            if (!isActiviting(actBean, TimeUtils.Time())) {
                actBean.setState((byte) 0);
                addPreMap(actBean);
                return true;
            }

            //Регистрация новой активности
            actCfg = new ActivityConfig();
            //Регистрация пользовательских данных
            IActivityScript as = getScript(actBean.getType());
            if (as == null) {
                return false;
            }

            //Установка базовой информации
            actCfg.beanToActivityBaseConfig(actBean);

            //Парсинг пользовательских настроек
            if (!as.parseCustomConfig(actCfg, actBean.getCustom())) {
                log.error("Ошибка парсинга пользовательских данных события");
                return false;
            }

            Manager.activityManager.getActCfgMap().put(actBean.getType(), actCfg);

            sendActivityOpen(actCfg);
            return true;
        } catch (Exception e) {
            log.error(actBean, e);
        }
        return false;
    }

    private boolean loadActConfig(ActivityConfigBean actBean) {
        try {
            int actType = actBean.getType();
            if (actBean == null) {
                return false;
            }

            if (actType <= 0) {
                return false;
            }

            if (actBean.getIsDelete() == 1) {
                return false;
            }

            long nowTime = TimeUtils.Time();
            //Активность истекла, удаление
            if (nowTime >= actBean.getEndTime()) {
                delActConfig(actBean.getId());
                return false;
            }

            if (actBean.getState() == 0) {//Предварительная публикация
                addPreMap(actBean);
            } else if (actBean.getState() == 1) {//Активная
                //Если уже загружена активность того же типа, исключаем неактивные по времени
                if (Manager.activityManager.getActCfgMap().containsKey(actType)) {
                    if (!isActiviting(actBean, nowTime)) {
                        addPreMap(actBean);
                        actBean.setState((byte) 0);
                        Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
                        return false;
                    }
                }

                IActivityScript as = getScript(actType);
                if (as == null) {
                    return false;
                }

                //Регистрация новой активности
                ActivityConfig actCfg = new ActivityConfig();

                //Установка базовой информации
                actCfg.beanToActivityBaseConfig(actBean);

                //Проверка парсинга пользовательских настроек
                if (!(as.parseCustomConfig(actCfg, actBean.getCustom()))) {
                    log.error("Ошибка парсинга пользовательских данных события, actId=" + actCfg.getId());
                    return false;
                }

                Manager.activityManager.getActCfgMap().put(actType, actCfg);
            }
            return true;
        } catch (Exception e) {
            log.error(actBean, e);
        }
        return false;
    }

    private boolean isActiviting(ActivityConfigBean actBean, long nowTime) {
        //Условие для новых серверов: в течение 7 дней только активности для новых серверов
        if(TimeUtils.getOpenServerDay() <= 7){
            if(actBean.getIsOpenServer() != 1){
                return false;
            }
        }
        return nowTime >= actBean.getBeginTime() && nowTime <= actBean.getEndTime();
    }

    private void addPreMap(ActivityConfigBean actBean) {
        Map<Integer, ActivityConfigBean> preMap = Manager.activityManager.getPreCfgMap().get(actBean.getType());
        if (preMap == null) {
            preMap = new HashMap<>();
            Manager.activityManager.getPreCfgMap().put(actBean.getType(), preMap);
        }
        preMap.put(actBean.getId(), actBean);
    }

    private void sendActivityOpen(ActivityConfig actCfg) {
        if (Manager.countManager.getCount(ActivityManager.getInstance(), BaseCountType.Activity, actCfg.getId()) <= 0) {

            Manager.countManager.setCount(ActivityManager.getInstance(), BaseCountType.Activity, actCfg.getId(), Count.RefreshType.CountType_Day, 1);

            ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
            for (Player player : Manager.playerManager.getOnLines()) {
                ActivityMessage.Activity.Builder actMess = getActivityBuilder(player.getId(), actCfg);
                msg.setAct(actMess);
                MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
    }

    @Override
    public IActivityScript getScript(int actType) {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.getActivityScriptId(actType / 1000));
        if (is == null) {
            return null;
        }
        if (is instanceof IActivityScript) {
            return (IActivityScript) is;
        }
        return null;
    }

    @Override
    /**
     * Получение списка конфигураций активности по логическому ID типа
     * @param actLogicID
     * @return
     */
    public List<ActivityConfig> getActCfgListByActLogicID(int actLogicID) {
        List<ActivityConfig> result = new ArrayList<>();
        for (Integer typeID : Manager.activityManager.getActCfgMap().keySet()) {
            if (toActLogicID(typeID) == actLogicID) {
                result.add(Manager.activityManager.getActCfgMap().get(typeID));
            }
        }
        return result;
    }

    @Override
    public int toActLogicID(int actType) {
        return actType / 1000;
    }

    @Override
    public int toActType(int logicID, int festivalID) {
        return logicID * 1000 + festivalID;
    }

    @Override
    public void sendActivityConfigChange(Player player, ActivityConfig actCfg) {

    }

    @Override
    public void sendActivityDataChange(Player player, int actType) {
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = getActivityDataBuilder(player.getId(), actType);
        msg.setAct(actMess);
        MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void saveActConfig(ActivityConfig actCfg, int state) {
        ActivityConfigBean bean = makeActivityBean(actCfg);
        bean.setState((byte) state);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
    }

    @Override
    public void delActConfig(int id) {
        ActivityConfigBean bean = new ActivityConfigBean();
        bean.setId(id);
        bean.setWhere(id);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYCONFIG_DELETE, SaveServer.DELETE);
    }

    @Override
    public void saveActData(int type, ConcurrentHashMap<String, Object> actDataMap) {
        ActivityDataBean bean = new ActivityDataBean();
        bean.setType(type);
        String str = JsonUtils.toJSONString(actDataMap);
        bean.setActData(str);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_UPDATE, SaveServer.MERGE);
    }

    @Override
    public void delActData(int type) {
        ActivityDataBean bean = new ActivityDataBean();
        bean.setType(type);
        bean.setWhere(type);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);
    }

    @Override
    public void saveRoleActData(long roleId, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>> actDataMap) {
        RoleActivityDataBean bean = new RoleActivityDataBean();
        bean.setRoleId(roleId);
        String str = JsonUtils.toJSONString(actDataMap);
        bean.setActData(str);
        Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ROLEACTIVITYDATA_UPDATE, SaveServer.MERGE);
    }

    /**
     * Проверка открытия и закрытия активностей
     */
    @Override
    public void checkAllActivity() {
        long now = TimeUtils.Time();
        //Проверка закрытия
        Iterator<Entry<Integer, ActivityConfig>> iterator1 = Manager.activityManager.getActCfgMap().entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<Integer, ActivityConfig> entry = iterator1.next();
            int actType = entry.getKey();
            ActivityConfig actCfg = entry.getValue();
            //Активность истекла, удаление
            if (actCfg.getEndTime() < now) {
                IActivityScript as = getScript(actType);
                if (as == null) {
                    log.error("Ошибка скрипта активности: actType=" + actType);
                    continue;
                }

                try {
                    //Завершение активности
                    as.activityEndDeal(actCfg);
                }catch (Exception e){
                    log.error(e+", Ошибка завершения активности, actId="+actCfg.getId(), e);
                }
                //Очистка конфигурации
                iterator1.remove();
                Manager.activityManager.deal().delActConfig(actCfg.getId());

                //Очистка данных активности
                cleanActData(actType);
                continue;
            }
            if (actCfg.isActiviting()) {
                sendActivityOpen(actCfg);
            }
        }


        //Проверка предварительных активностей на готовность к открытию
        Iterator<Entry<Integer, Map<Integer, ActivityConfigBean>>> its = Manager.activityManager.getPreCfgMap().entrySet().iterator();
        while (its.hasNext()) {
            Entry<Integer, Map<Integer, ActivityConfigBean>> entry = its.next();
            int actType = entry.getKey();

            if (Manager.activityManager.getActCfgMap().containsKey(actType)) {
                //Уже есть активная активность этого типа
                continue;
            }

            //В предварительном списке может быть несколько активностей одного типа
            Iterator<Entry<Integer, ActivityConfigBean>> preIt = entry.getValue().entrySet().iterator();
            while (preIt.hasNext()) {
                Entry<Integer, ActivityConfigBean> pre = preIt.next();
                ActivityConfigBean actBean = pre.getValue();
                //Условие для новых серверов: в течение 7 дней только активности для новых серверов
                if(TimeUtils.getOpenServerDay() <= 7){
                    if(actBean.getIsOpenServer() != 1){
                        continue;
                    }
                }
                //Активность готова к открытию
                if (actBean.getBeginTime() < now && now < actBean.getEndTime()) {
                    IActivityScript as = getScript(actType);
                    if (as == null) {
                        continue;
                    }
                    ActivityConfig actCfg = new ActivityConfig();
                    actCfg.beanToActivityBaseConfig(actBean);
                    //Парсинг пользовательских настроек
                    try{
                        if (!as.parseCustomConfig(actCfg, actBean.getCustom())) {
                            log.error("Ошибка парсинга пользовательских данных активности");
                            continue;
                        }
                    }catch (Exception e){
                        log.error(e+", Ошибка проверки открытия активности, actId="+actCfg.getId(), e);
                        continue;
                    }
                    Manager.activityManager.getActCfgMap().put(actType, actCfg);
                    //Удаление из предварительного списка после успешного открытия
                    preIt.remove();

                    sendActivityOpen(actCfg);

                    actBean.setState((byte) 1);
                    Manager.saveThreadManager.getOtherServerSave().deal(actBean, DbSqlName.ACTIVITYCONFIG_UPDATE, SaveServer.MERGE);
                    break;
                }
            }
        }
    }

    @Override
    public void load() {
        Manager.activityManager.getActCfgMap().clear();
        List<ActivityConfigBean> configList = Manager.activityManager.getConfigDao().selectAll();
        if (configList == null) {
            return;
        }

        for (ActivityConfigBean bean : configList) {
            loadActConfig(bean);
        }

        //Загрузка данных активностей
        Manager.activityManager.getActDatas().clear();
        List<ActivityDataBean> dataList = Manager.activityManager.getDataDao().selectAll();
        if (dataList == null) {
            return;
        }

        for (ActivityDataBean bean : dataList) {
            Manager.activityManager.getActDatas().put(bean.getType(), JsonUtils.parseObject(bean.getActData(), new TypeReference<ConcurrentHashMap<String, Object>>() {
            }));
        }

        //Загрузка данных активностей игроков
        Manager.activityManager.getRoleActDatas().clear();
        List<RoleActivityDataBean> roleDataList = Manager.activityManager.getRoleDataDao().selectAll();
        if (roleDataList == null) {
            return;
        }

        for (RoleActivityDataBean bean : roleDataList) {
            Manager.activityManager.getRoleActDatas().put(bean.getRoleId(), JsonUtils.parseObject(bean.getActData(),
                    new TypeReference<ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>>() {
                    }));
        }

        log.info("Загружено " + configList.size() + " конфигураций активностей");
    }

    @Override
    public int loadTagInfo() {
        Manager.activityManager.getTagInfoList().clear();
        List<TagInfoBean> tagList = Manager.activityManager.getTagDao().selectAll();
        if (tagList != null && !tagList.isEmpty()) {
            Manager.activityManager.getTagInfoList().addAll(tagList);
            return Manager.activityManager.getTagInfoList().size();
        }
        return 0;
    }

    /**
     * Обработка полуночи для игрока
     *
     * @param player
     */
    @Override
    public void zeroClockPlayerDeal(Player player) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            as.zeroClockPlayerDeal(player, actCfg);
        }
    }

    @Override
    public void fiveClockPlayerDeal(Player player) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            as.fiveClockPlayerDeal(player, actCfg);
        }
    }

    @Override
    public void zeroClockDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.zeroClockDeal(actCfg);
        }
    }

    @Override
    public void fiveClockDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.fiveClockDeal(actCfg);
        }
    }

    @Override
    public void everyHourDeal() {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (!actCfg.isActiviting()) {
                continue;
            }

            as.everyHourDeal(actCfg);
        }
    }

    @Override
    public void cleanActivity(int actType) {
        IActivityScript as = getScript(actType);
        if (as == null) {
            return;
        }

        as.activityEndDeal(Manager.activityManager.getActCfgMap().get(actType));

        //Очистка конфигурации
        Manager.activityManager.getActCfgMap().remove(actType);
        Manager.activityManager.deal().delActConfig(actType);

        //Очистка данных активности
        cleanActData(actType);
    }

    @Override
    public void sendActivityTagInfo(Player player) {
        ActivityMessage.ResTagInfoList.Builder builder = ActivityMessage.ResTagInfoList.newBuilder();
        builder.setTag(JsonUtils.toJSONString(Manager.activityManager.getTagInfoList()));
        MessageUtils.send_to_player(player, ActivityMessage.ResTagInfoList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    @Override
    public void sendActivityTagInfo(Player player, String data) {
        ActivityMessage.ResTagInfoList.Builder builder = ActivityMessage.ResTagInfoList.newBuilder();
        builder.setTag(data);
        MessageUtils.send_to_player(player, ActivityMessage.ResTagInfoList.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void cleanActData(int actType) {
        //Очистка данных активности
        if (Manager.activityManager.getActDatas().containsKey(actType)) {
            Manager.activityManager.getActDatas().remove(actType);
            Manager.activityManager.deal().delActData(actType);
        }

        //Очистка данных игроков
        for (Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : Manager.activityManager.getRoleActDatas().entrySet()) {
            long roleId = entry.getKey();
            if (entry.getValue().containsKey(actType)) {
                entry.getValue().remove(actType);
                Manager.activityManager.deal().saveRoleActData(roleId, entry.getValue());
            }
        }
    }

    @Override
    public void rechargeDeal(Player player, int getGoodsCfgId, int rechargeNum) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (!actCfg.isRecordTime()){
                continue;
            }
            as.rechargeDeal(player, getGoodsCfgId, rechargeNum, actCfg);
        }
    }

    @Override
    public void consumeDeal(Player player, int coinType, int consumeNum) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (!actCfg.isRecordTime()){
                continue;
            }
            as.consumeDeal(player, coinType, consumeNum, actCfg);
        }
    }

    @Override
    public List<Long> getRoleIdList(int actType) {
        List<Long> roleIds = new ArrayList<>();
        for (Entry<Long, ConcurrentHashMap<Integer, ConcurrentHashMap<String, Object>>> entry : Manager.activityManager.getRoleActDatas().entrySet()) {
            if (entry.getValue().containsKey(actType)) {
                roleIds.add(entry.getKey());
            }
        }
        return roleIds;
    }

    /**
     * Обработка дропа с босса
     *
     * @param player
     * @return
     */
    @Override
    public void bossDrop(Player player, int bossId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            as.bossDrop(player, bossId, actCfg);
        }
    }

    public boolean boxDrop(Player player, int boxId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (as.boxDrop(player, boxId, actCfg)) {
                return true;
            }

        }
        return false;
    }

    /**
     * Дроп в подземелье
     *
     * @param player
     * @param cloneId
     * @return
     */
    @Override
    public boolean cloneDrop(Player player, int cloneId) {
        for (int type : Manager.activityManager.getActCfgMap().keySet()) {
            IActivityScript as = getScript(type);
            if (as == null) {
                continue;
            }

            if (!checkOpen(player, type)) {
                continue;
            }

            ActivityConfig actCfg = Manager.activityManager.getActCfgMap().get(type);
            if (actCfg == null) {
                log.error("ActivityConfig не найден. type：" + type);
                continue;
            }

            if (as.cloneDrop(player, cloneId, actCfg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Проверка входа
     */
    private boolean checkLogin(Player player, String[] ss) {
        try {
            Date d = TimeUtils.getSdfNoSecond().parse(ss[1]);
            long beginDay = TimeUtils.GetCurTimeInMin(4, d.getTime());
            d = TimeUtils.getSdfNoSecond().parse(ss[2]);
            long endDay = TimeUtils.GetCurTimeInMin(4, d.getTime());

            for (Integer last : player.getLoginDays()) {
                if (beginDay <= last && last < endDay) {
                    return true;
                }
            }
        } catch (Exception e) {
            log.error(e, e);
            log.error("Параметры ss:");
            for (String s : ss) {
                log.error("s=" + s);
            }
            return false;
        }
        return false;
    }

    private void writeActivityDonateLog(long sendId, long receiveId, int itemModelId, int num, long actionId) {
        try {
            ActivityDonateLog donateLog = new ActivityDonateLog();
            donateLog.setActionId(actionId);
            donateLog.setItemModelid(itemModelId);
            donateLog.setNum(num);
            donateLog.setReceiveId(receiveId);
            donateLog.setSendId(sendId);
            donateLog.setSid(Manager.playerManager.getCreateServeId(sendId));
            LogService.getInstance().execute(donateLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    private void writeGetActivityLog(Player player, long activityId, String parms, long actionId) {
        try {
            ActivityGetLog getLog = new ActivityGetLog();
            getLog.setActionId(actionId);
            getLog.setActivityId(activityId);
            getLog.setParms(parms);
            getLog.setRoleId(player.getId());
            getLog.setSid(player.getCreateServerId());
            getLog.setPlatformName(player.getPlatformName());
            LogService.getInstance().execute(getLog);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    /**
     * Проверка самой ранней активности
     */
    private ActivityConfig checkActivity(Map<Long, ActivityConfig> activityMap) {
        long now = TimeUtils.Time();
        ActivityConfig activity = new ActivityConfig();
        ActivityConfigBean bean;
        long minStartTime = 9999999999999L;
        long minEndTime = 0L;
        //Проверка активности
        Iterator<Entry<Long, ActivityConfig>> iterator1 = activityMap.entrySet().iterator();
        while (iterator1.hasNext()) {
            Entry<Long, ActivityConfig> entry = iterator1.next();
            if (entry.getValue().getEndTime() < (now - 5 * 1000)) {
                //Активность истекла, удаление
                bean = new ActivityConfigBean();
                bean.setWhere(entry.getKey());
                Manager.saveThreadManager.getOtherServerSave().deal(bean, DbSqlName.ACTIVITYDATA_DELETE, SaveServer.DELETE);
                iterator1.remove();
            }
            if (entry.getValue().getBeginTime() < minStartTime) {
                minStartTime = entry.getValue().getBeginTime();
                minEndTime = entry.getValue().getEndTime();
                activity = entry.getValue();
            } else if (entry.getValue().getBeginTime() == minStartTime) {
                if (entry.getValue().getEndTime() < minEndTime) {
                    minStartTime = entry.getValue().getBeginTime();
                    minEndTime = entry.getValue().getEndTime();
                    activity = entry.getValue();
                }
            }
        }
        return activity;
    }

    /**
     * Установка активности из панели управления
     *
     * @param actBean    данные активности
     * @param b2wSession сессия
     */
    public void w2gSyncActivity(ActivityConfigBean actBean, Channel b2wSession) {
        boolean result = w2gSyncActivity(actBean);
        Map<String, Object> map = new HashMap<>(16);
        map.put("ok", result);
        map.put("msg", result ? "Публикация успешна!" : "Ошибка публикации!");

        BackGrandServer.Send(b2wSession, JsonUtils.toJSONString(map));
    }

    /**
     * Установка активности из панели управления
     *
     * @param actBean данные активности
     */
    @Override
    public boolean w2gSyncActivity(ActivityConfigBean actBean) {
        log.error("Получена публикация активности activityId:" + actBean.getId() + ", тип:" + actBean.getType() + ", название:" + actBean.getName());
        actBean.setWhere(actBean.getId());
        //Регистрация активности
        boolean isSuccess = false;
        if (actBean.getState() == 1) {//Перезапись активной активности
            isSuccess = Manager.activityManager.deal().registerActivityBean(actBean);
        } else {
            addPreMap(actBean);
            isSuccess = true;
        }
        return isSuccess;
    }

    /**
     * Массовая публикация активностей
     *
     * @param activityBeans список активностей
     */
    @Override
    public List<Integer> w2gBatchSyncActivity(List<ActivityConfigBean> activityBeans) {
        log.error("Получена массовая публикация активностей");
        List<Integer> faultList = new ArrayList<>();
        for (ActivityConfigBean actConfigBean : activityBeans) {
            actConfigBean.setWhere(actConfigBean.getId());
            boolean isSuccess = false;
            if (actConfigBean.getState() == 1) {
                isSuccess = Manager.activityManager.deal().registerActivityBean(actConfigBean);
            } else {
                addPreMap(actConfigBean);
                isSuccess = true;
            }

            if (!isSuccess) {
                faultList.add(actConfigBean.getId());
            }
        }
        return faultList;
    }

    /**
     * Массовая публикация активностей
     *
     * @param activityConfigBeans список активностей
     * @param session             сессия
     */
    public void w2gBatchSyncActivity(List<ActivityConfigBean> activityConfigBeans, Channel session) {
        List<Integer> faultList = w2gBatchSyncActivity(activityConfigBeans);
        Map<String, Object> map = new HashMap<>(16);
        if (faultList.isEmpty()) {
            map.put("ok", true);
            map.put("msg", "Массовая публикация успешна!");
        } else {
            map.put("ok", false);
            map.put("data", faultList);
        }
        BackGrandServer.Send(session, JsonUtils.toJSONString(map));
    }

    /**
     * Удаление активности из панели управления
     *
     * @param b2wSession сессия
     */
    @Override
    public void w2gSyncDeleteActivity(int actType, Channel b2wSession) {
        log.error("Запрос на удаление активности actType:" + actType);
        Map<String, Object> map = new HashMap<>(16);
        map.put("ok", true);
        map.put("msg", "Активность " + actType + " удалена успешно!");
        Manager.activityManager.getActCfgMap().remove(actType);
        BackGrandServer.Send(b2wSession, JsonUtils.toJSONString(map));
    }

    /**
     * Удаление активности
     */
    public void w2gSyncDeleteActivity(int actType) {
        log.error("Удаление активности actType:" + actType);
        Manager.activityManager.getActCfgMap().remove(actType);
    }

    public void batchDelActivity(int actType) {
        log.error("Массовое удаление активности actType:" + actType);

        IActivityScript as = getScript(actType);
        if (as == null) {
            return;
        }
        //Активность уже удалена
        if (!Manager.activityManager.getActCfgMap().containsKey(actType)) {
            return;
        }

        as.activityEndDeal(Manager.activityManager.getActCfgMap().get(actType));

        Manager.activityManager.getActCfgMap().remove(actType);

        cleanActData(actType);

        noticeDelActivityMessage(actType);
    }

    private void noticeDelActivityMessage(int actType) {
        ActivityMessage.ResActivityChange.Builder msg = ActivityMessage.ResActivityChange.newBuilder();
        ActivityMessage.Activity.Builder actMess = ActivityMessage.Activity.newBuilder();
        actMess.setType(actType);

        HashMap<String, Object> actMap = new HashMap<>();
        actMap.put("isDelete", 1);

        actMess.setActConfig(JsonUtils.toJSONString(actMap));
        msg.setAct(actMess);

        for (Player player : Manager.playerManager.getOnLines()) {
            MessageUtils.send_to_player(player, ActivityMessage.ResActivityChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public void gmSetActivity(Map<String, Object> cmdMap) {
        IBackCommandScript script = (IBackCommandScript) Manager.scriptManager.GetScriptClass(ScriptEnum.BackCommandBaseScript);
        script.gmActivitySendMess(cmdMap);
    }

    private ActivityConfigBean makeActivityBean(ActivityConfig actCfg) {
        ActivityConfigBean actBean = new ActivityConfigBean();
        actBean.setId(actCfg.getId());
        actBean.setType(actCfg.getType());
        actBean.setMinLv(actCfg.getMinLv());
        actBean.setMaxLv(actCfg.getMaxLv());
        actBean.setTag(actCfg.getTag());
        actBean.setSort(actCfg.getSort());
        actBean.setName(actCfg.getName());
        actBean.setBeginTime(actCfg.getBeginTime());
        actBean.setEndTime(actCfg.getEndTime());
        actBean.setIsDelete(actCfg.getIsDelete());
        actBean.setCustom(actCfg.getCustom());
        return actBean;
    }

    @Override
    public void reload() {
        if (Manager.activityManager == null) {
            return;
        }
    }
}