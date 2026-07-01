package common.dailyactive;

import com.data.*;
import com.data.bean.*;
import com.data.container.Cfg_Daily_Container;
import com.data.container.Cfg_Mapsetting_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.backpack.structs.Item;
import com.game.backpack.structs.ItemCoinType;
import com.game.bravepeak.struct.BravePeakDefine;
import com.game.chat.structs.Notify;
import com.game.copymap.scripts.ICopyReliveScript;
import com.game.count.structs.VariantType;
import com.game.dailyactive.log.EnterDailyActivityLog;
import com.game.dailyactive.log.ReceiveActiveRewardLog;
import com.game.dailyactive.manager.DailyActiveManager;
import com.game.dailyactive.script.IDailyActiveScript;
import com.game.dailyactive.script.IDailyScript;
import com.game.dailyactive.structs.DailyActiveData;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.guildbattle.structs.GuildBattle;
import com.game.manager.Manager;
import com.game.map.manager.MapManager;
import com.game.map.structs.MapObject;
import com.game.ninedaysfocused.manager.NineDaysFocusedManager;
import com.game.peak.timer.PeakCloseEvent;
import com.game.peak.timer.PeakStartEvent;
import com.game.player.structs.Player;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import com.game.vip.structs.VipPower;
import com.game.welfare.struct.RetrieveType;
import game.core.dblog.LogService;
import game.core.map.Position;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.core.script.ScriptManager;
import game.core.util.CrossState;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.CrossHorseBossMessage;
import game.message.DailyactiveMessage;
import game.message.DailyactiveMessage.ResGetActiveReward;
import game.message.MSG_UniverseMessage;
import game.message.WorldBonfireMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DailyActiveScript implements IDailyActiveScript {

    private static final Logger logger = LogManager.getLogger(DailyActiveScript.class);

    final DailyActiveDefine[] BossDailyIds = {
            DailyActiveDefine.HOME_BOSS,
            DailyActiveDefine.WORLD_BOSS,
            DailyActiveDefine.SUIT_BOSS,
            DailyActiveDefine.SOUL_ANIMAL_ISLAND_BOSS
    };
    final int DailyReady = 1;
    final int DailyOpen = 2;
    final int DailyClose = 3;

    @Override
    public int getId() {
        return ScriptEnum.DailyActiveBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void timerCheck(long now, long lastCheckTime) {

        Cfg_Daily_Bean[] beans = Cfg_Daily_Container.GetInstance().getValuees();
        for (Cfg_Daily_Bean bean : beans) {

            if (Manager.dailyActiveManager.getGmControl().containsKey(bean.getId())) {
                continue;
            }

            int[] check = checkOpen(bean);
            if (check.length == 0) {
                if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) == DailyOpen) {
                    activeEnd(bean);
                }
                continue;
            }
            if (check[1] == DailyReady) {
                if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) == DailyClose) {
                    Manager.dailyActiveManager.getDailyOpenState().put(bean.getId(), DailyReady);
                    doPushDailyActiveNotify(bean);
                    doPushStageState(bean, DailyReady);
                    logger.info("日常活动id={}【{}】准备阶段：stage={}", bean.getId(), bean.getName(), check[2]);
                }
                continue;
            }
            if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) != DailyOpen) {
                activeBegin(bean);
                doPushDailyStartNotify(bean);
                doPushStageState(bean, DailyOpen);
            }
        }

        //宗派活动--福地称号，除去特殊称号后宗派内的成员的排名
        Manager.guildActivityManager.deal().rank(lastCheckTime, now, false);

        Manager.questionnaireManager.deal().checkOpenState(lastCheckTime, now);

        Manager.openServerAcManager.deal().checkActEnd(lastCheckTime, now);
    }

    /**
     * 今天是否开放活动
     *
     * @param bean
     * @param time
     * @return
     */
    private boolean checkOpenDay(Cfg_Daily_Bean bean, long time) {
        int curOpenDay = TimeUtils.getOpenServerDay();
        if (bean.getIf_open() == 0) {
            return false;
        }
        //开服天数控制器
        if (bean.getOpenday() != 0 && curOpenDay < bean.getOpenday()) {
            return false;
        }
        //开服天数特殊开启条件
        if (bean.getSpecialOpen() == curOpenDay) {
            return true;
        }
        //强制关闭天数
        if (bean.getCloseTime().size() == 2) {
            if (curOpenDay > bean.getCloseTime().get(0)) {
                return false;
            }
        }
        //开服天数控制器
        boolean isOpenDayTrue = false;
        if (bean.getDelayDays().size() == 1) {
            if (curOpenDay >= bean.getDelayDays().get(0) && bean.getIfGono() == 1) {
                isOpenDayTrue = true;
            }
        }
        //开服天数列表控制器
        for (int openDay : bean.getDelayDays().getValue()) {
            if (openDay == curOpenDay) {
                isOpenDayTrue = true;
                break;
            }
        }
        if (!isOpenDayTrue) {
            return false;
        }
        //周控制器
        int curWeekDay = TimeUtils.getDayOfWeek(time);
        for (int weekDay : bean.getOpenTime().getValue()) {
            if (weekDay == 0) {
                return true;
            }
            if (weekDay == curWeekDay) {
                return true;
            }
        }
        return false;
    }

    /**
     * 活动是否开启了
     *
     * @param bean
     * @return
     */
    int[] checkOpen(Cfg_Daily_Bean bean) {
        long time = TimeUtils.Time();
        if (bean.getIf_open() == 0) {
            return new int[0];
        }
        //开启天检测
        if (!checkOpenDay(bean, time)) {
            return new int[0];
        }
        long todayBeginTime = TimeUtils.getTodayBeginTime();
        long forceEnd = -1; //强制关闭时间
        if (bean.getCloseTime().size() == 2) {
            int curOpenDay = TimeUtils.getOpenServerDay();
            if (curOpenDay == bean.getCloseTime().get(0)) {
                forceEnd = todayBeginTime + bean.getCloseTime().get(1) * 60 * 1000L;
            }
        }
        //开启当天 时间区间检测
        for (int i = 0; i < bean.getTime().size(); i++) {
            ReadArray<Integer> timeBean = bean.getTime().get(i);
            long start = todayBeginTime + timeBean.get(0) * 60 * 1000L;
            long end = todayBeginTime + timeBean.get(1) * 60 * 1000L;
            long startPush = start - bean.getPushAdvance() * 60 * 1000L;
            int stage = -1;
            //TODO 检测活动推送
            if (bean.getStage().size() == 1) {
                ReadArray<Integer> stageArr = bean.getStage().get(0);
                stage = stageArr.get(0);
                startPush = start + stageArr.get(1) * 60 * 1000L;
            }
            if (forceEnd != -1) {
                end = Math.min(end, forceEnd);
            }
            //开始活动开启推送
            if (time >= startPush && time <= end) {
                if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) == DailyClose) {
                    if (time >= start && time <= end) {
                        return new int[]{i, DailyOpen};
                    }
                    return new int[]{i, DailyReady, stage};
                }
            }
            if (time >= start && time <= end) {
                return new int[]{i, DailyOpen};
            }
        }
        return new int[0];
    }

    /**
     * 日常重置
     *
     * @param player
     */
    @Override
    public void checkDailyReset(Player player) {

        long week = Manager.countManager.getVariant(player, VariantType.DailyWeekActive);
        if (week == 0) {
            Manager.countManager.setVariant(player, VariantType.DailyWeekActive, 1);
            DailyActiveData dailyActiveData = player.getDailyActiveData();
            for (Cfg_Daily_Bean bean : CfgManager.getCfg_Daily_Container().getValuees()) {
                if (bean.getRefresh() == 1) {
                    dailyActiveData.getDailyProgress().put(bean.getId(), 0);
                    dailyActiveData.getDailyBuyCount().put(bean.getId(), 0);
                    dailyActiveData.getItemAddCount().put(bean.getId(), 0);
                    logger.info("周日常重置 dailyId={} player={}", bean.getId(), player);
                }
            }
        }

        long value = Manager.countManager.getVariant(player, VariantType.DailyDayActive);
        if (value == 0) {
            Manager.countManager.setVariant(player, VariantType.DailyDayActive, 1);
            //TODO 这两个是什么鬼
            player.getWorshipRoleIdSet().clear();
            DailyActiveData dailyActiveData = player.getDailyActiveData();

            dailyActiveData.getAcRewardList().clear();
            dailyActiveData.setActiveNum(0);

            long activePoint = player.getCurrencys().get(ItemCoinType.ActivePoint);
            if (activePoint > 0 && player.getLevel() > Global.Activities_count_Noclean_Level) {
                Manager.currencyManager.manager().onDecItemCoin(player, activePoint, ItemChangeReason.ActivePointDailyClear, IDConfigUtil.getLogId(), ItemCoinType.ActivePoint);
            }

            for (Cfg_Daily_Bean bean : CfgManager.getCfg_Daily_Container().getValuees()) {
                if (bean.getRefresh() == 0) {
                    dailyActiveData.getDailyProgress().put(bean.getId(), 0);
                    dailyActiveData.getDailyBuyCount().put(bean.getId(), 0);
                    dailyActiveData.getItemAddCount().put(bean.getId(), 0);
//                    logger.info("日常重置 dailyId={} player={}", bean.getId(), player);
                }
            }

            for (Cfg_Daily_Bean bean : CfgManager.getCfg_Daily_Container().getValuees()) {
                if (Manager.dailyActiveManager.deal().checkInBossMap(player, bean.getId())) {
                    MapObject map = Manager.mapManager.getMap(player.gainMapId());
                    IScript script = ScriptManager.getInstance().GetScriptClass(map.getSetting().getIsscript());
                    if (script instanceof IDailyScript) {
                        IDailyScript ids = ((IDailyScript) script);
                        ids.sendBossPanel(player, map);
                        ids.sendBossInfo(player, map);
                        ids.changeCamp(player, map);
                    }
                }
            }
            Manager.dailyActiveManager.deal().sendDailyActivePanelInfo(player);
        }
    }

    public void activeBegin(Cfg_Daily_Bean bean) {
        Manager.dailyActiveManager.getDailyOpenState().put(bean.getId(), DailyOpen);
        sendClientDailyOpenInfo(bean.getId(), true);
        logger.info("日常活动id={}【{}】开始阶段", bean.getId(), bean.getName());

        try {
            DailyActiveDefine dailyActiveDefine = DailyActiveDefine.find(bean.getId());
            if (dailyActiveDefine == null) {
                return;
            }
            switch (dailyActiveDefine) {
                case GUILD_LAST_BATTLE:
                    createDailyMap(bean);
                    break;
                case ACTIVITY_GUILD_BOSS:
                    Manager.guildActivityManager.deal().optGuildBossActivity(true);
                    break;
                case PeakPk:
                    Manager.peakManager.addCommand(new PeakStartEvent());
                    break;
                case ACTIVITY_GUILDBATTLE:
                    Manager.guildBattleManager.manager().guildBattleBegin(bean.getCloneID().get(0));
                    break;
                case NINE_DAYS_FOCUSED:
                    NineDaysFocusedManager.deal().onStart();
                    break;
                case EIGHT_DIAGRAMS:
                    Manager.eightDiagramsManager.eightDiagramsIsOpen = true;
                    break;
                case YZZD:
                    DailyActiveManager.bravePeakMapInfo.clear();
                    break;
                case FUD_ACTIVITY_BOSS:
                    createDailyMap(bean);
                    break;
                case HOME_BOSS:
                    createDailyMap(bean);
                    break;
                case GemBoss:
                    createDailyMap(bean);
                    break;
                case ACTIVITY_LOCAl_SOULANIMALISLAND:
                    createDailyMap(bean);
                    break;
                case ACTIVITY_CROSS_UNIVERSEWAR:
//                    if (GameServer.getInstance().IsFightServer()) {
//                        break;
//                    }
//                    if (TimeUtils.getOpenServerDay() >= Global.UniverseSeverOpenTime) {
////                        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.UniverseActivityStart, Utils.makeUrlStr(MessageString.UniverseActivityStart));
//                        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_URL_MARQUEE, MessageString.UniverseActivityStart, "");
//                    }
                    break;
                case CRAZY_WEEK:
                    //狂欢周活动处理
                    Manager.crazyWeekManager.deal().beginActivity();
                    break;
                case COUPLE_ESCORT:
                    Manager.couplefightManager.getCoupleEscort().onChangeCoupleEscortState(true);
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /**
     * 创建活动地图
     *
     * @param bean
     */
    void createDailyMap(Cfg_Daily_Bean bean) {
        ConcurrentHashMap<Integer, Long> mapList = DailyActiveManager.dailyMap.get(bean.getId());
        if (mapList == null) {
            mapList = new ConcurrentHashMap<>();
            DailyActiveManager.dailyMap.put(bean.getId(), mapList);
        }
        for (int i = 0; i < bean.getCloneID().size(); i++) {
            MapObject mapObject = Manager.mapManager.createCopyMap(bean.getCloneID().get(i), 1, MapManager.CopyMapOwnerSystemId);
            if (mapObject != null) {
                mapList.put(mapObject.getZoneModelId(), mapObject.getId());
            } else {
                logger.error("创建活动地图失败：" + bean.getId());
            }
        }
    }

    public void activeEnd(Cfg_Daily_Bean bean) {
        Manager.dailyActiveManager.getDailyOpenState().put(bean.getId(), DailyClose);
        doPushStageState(bean, DailyClose);
        sendClientDailyOpenInfo(bean.getId(), false);
        logger.info("日常活动id={}【{}】关闭阶段", bean.getId(), bean.getName());
        try {
            DailyActiveDefine dailyActiveDefine = DailyActiveDefine.find(bean.getId());
            if (dailyActiveDefine == null) {
                return;
            }
            switch (dailyActiveDefine) {
                case GUILD_LAST_BATTLE:
                    Manager.guildActivityManager.guildLastBattle().endActive();
                    break;
                case PeakPk:
                    Manager.peakManager.addCommand(new PeakCloseEvent());
                    break;
                case ACTIVITY_GUILD_BOSS:
                    Manager.guildActivityManager.deal().optGuildBossActivity(false);
                    break;
                case ACTIVITY_GUILDBATTLE:
                    Manager.guildBattleManager.manager().guildBattleEnd();
                    break;
                case NINE_DAYS_FOCUSED:
                    NineDaysFocusedManager.deal().onOver();
                    break;
                case EIGHT_DIAGRAMS:
                    Manager.eightDiagramsManager.eightDiagramsIsOpen = false;
                    break;
                case FUD_ACTIVITY_BOSS:
                case HOME_BOSS:
                case GemBoss:
                case ACTIVITY_LOCAl_SOULANIMALISLAND:
                    if (DailyActiveManager.dailyMap.containsKey(bean.getId())) {
                        for (Long mapId : DailyActiveManager.dailyMap.get(bean.getId()).values()) {
                            MapObject map = Manager.mapManager.getMap(mapId);
                            if (map != null) {
                                if (bean.getId() == DailyActiveDefine.HOME_BOSS.getValue()) {
                                    Manager.bossManager.bossHomeScript().call("activityEnd", map);
                                }
                                if (bean.getId() == DailyActiveDefine.FUD_ACTIVITY_BOSS.getValue()) {
                                    Manager.mapManager.base(ScriptEnum.GuildFudScript).call("activityEnd", map);
                                }
                                map.setAutoRemove(true);
                            } else {
                                logger.error("地图不存在！！，" + bean.getId() + ": " + mapId);
                            }
                        }
                        DailyActiveManager.dailyMap.get(bean.getId()).clear();
                    }
                    break;
                case YZZD:
                    DailyActiveManager.bravePeakMapInfo.clear();
                    break;
                case CRAZY_WEEK:
                    //狂欢周活动处理
                    Manager.crazyWeekManager.deal().endActivity();
                    break;

                case COUPLE_ESCORT:
                    Manager.couplefightManager.getCoupleEscort().onChangeCoupleEscortState(false);
                    break;
                case CROSS_Alien_Boss:
                    Manager.alienGemManager.getScript().close();
                    break;
//            case DailyActiveDefine.NINE_DAYS_FOCUSED:
//                NineDaysFocusedManager.deal().onOver();
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    /**
     * 检查活动推送
     *
     * @param bean 活动配置表bean
     */
    private void doPushDailyActiveNotify(Cfg_Daily_Bean bean) {

        if (bean.getId() == DailyActiveDefine.CROSS_FUD_Devil.getValue()) {
//            logger.info("魔王缝隙 准备公告");
            for (Player player : Manager.playerManager.getOnLines()) {
                MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.Cross_devil_Group_Open_Notice);
            }
        }
    }

    /**
     * 检查活动推送
     *
     * @param bean 活动配置表bean
     */
    private void doPushDailyStartNotify(Cfg_Daily_Bean bean) {

        if (bean.getId() == DailyActiveDefine.CROSS_FUD_Devil.getValue()) {
//            logger.info("魔王缝隙 开始公告");
            for (Player player : Manager.playerManager.getOnLines()) {
                MessageUtils.notify_player(player, Notify.CHAT_SYS_MARQUEE, MessageString.Cross_devil_Group_Open);
            }
        }
    }

    /**
     * 活动阶段状态
     */
    private void doPushStageState(Cfg_Daily_Bean bean, int stage) {
        IScript script = Manager.scriptManager.GetScriptClass(bean.getScriptId());
        if (script != null) {
            script.call(stage, "activityChangeCallBack");
        }
    }

    /**
     * 发送日常活动面板信息
     */
    @Override
    public void sendDailyActivePanelInfo(Player player) {
        DailyActiveData dailyActiveData = player.getDailyActiveData();
        int activeBaseMaxValue = getDailyActiveMax(player);

        DailyactiveMessage.ResDailyActivePenel.Builder msg = DailyactiveMessage.ResDailyActivePenel.newBuilder();
        msg.setValue(dailyActiveData.getActiveNum());
        msg.addAllDrawList(dailyActiveData.getAcRewardList());
        msg.setUseItemCount((int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxCount));
        msg.setActiveAdded((int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxNum));
        msg.setActiveMax(activeBaseMaxValue);

        Cfg_Daily_Bean[] list = Cfg_Daily_Container.GetInstance().getValuees();
        for (Cfg_Daily_Bean bean : list) {
            DailyactiveMessage.dailyActiveInfo.Builder builder = DailyactiveMessage.dailyActiveInfo.newBuilder();
            //是否可以显示
//            if (bean.getCanshow() != 1) {
//                continue;
//            }
            builder.setActiveId(bean.getId());

            //是否满足等级开启条件
            boolean conditionOpen = player.getLevel() >= bean.getOpenLevel() && TimeUtils.getOpenServerDay() >= bean.getDelayDays().get(0, 0);
            builder.setConditionOpen(conditionOpen);

            //是否在活动时间内
            builder.setOpen(Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) == DailyOpen);

            int remainCount = getDailyRemainCount(player, bean.getId());
            int canBuyCount = getDailyCanBuyCount(player, bean.getId());
            builder.setRemainCount(remainCount);
            builder.setCanBuyCount(canBuyCount);
            msg.addDailyInfoList(builder);
        }
        MessageUtils.send_to_player(player, DailyactiveMessage.ResDailyActivePenel.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    @Override
    public void addDailyProgress(Player player, DailyActiveDefine daily, int effectTimes) {
        if (effectTimes <= 0) {
            return;
        }
        int dailyId = daily.getValue();
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            logger.error("Cfg_Daily_Bean配置表不存在");
            return;
        }

        //不限次数的活动不加进度
        if (bean.getTimes() == -1) {
            return;
        }
        int dailyRemainCount = getDailyRemainCount(player, dailyId);
        if (dailyRemainCount <= 0) {
            return;
        }
        DailyActiveData data = player.getDailyActiveData();
        int progress = data.getDailyProgress().getOrDefault(dailyId, 0);
        data.getDailyProgress().put(dailyId, progress + effectTimes);
        Manager.bossManager.manager().sendDailyCount(player, daily);
        //最大值活跃度限制
        int addActiveNum = bean.getActiveValue() * effectTimes;
        int maxActivePoint = getDailyActiveMax(player);
        int hasActivePoint = data.getActiveNum();

        addActiveNum = Math.min(addActiveNum, maxActivePoint - hasActivePoint);

        data.setActiveNum(hasActivePoint + addActiveNum);
        player.getCurRRD().setHasActivePoint(hasActivePoint + addActiveNum);//资源找回需要记录当前总共获得
        Manager.currencyManager.manager().onAddItemCoin(player, ItemCoinType.ActivePoint, addActiveNum, ItemChangeReason.CompleteDailyGet, IDConfigUtil.getLogId());
        Manager.countManager.addVariant(player, VariantType.Daily_Active_Value, addActiveNum);

        sendUpdateDailyInfo(player, dailyId);
        Manager.controlManager.operate(player, FunctionVariable.CurDayActiveValue, addActiveNum);
        // 挚友添加活跃点
        Manager.chumManager.getScript().addActiveValue(player, addActiveNum);
    }

    /**
     * 增加活跃次数
     *
     * @param player
     * @param dailyId
     * @param num
     */
    @Override
    public void addCountByItem(Player player, int dailyId, int num) {
        int itemAddCount = player.getDailyActiveData().getItemAddCount().getOrDefault(dailyId, 0);
        player.getDailyActiveData().getItemAddCount().put(dailyId, itemAddCount + num);
    }

    private void sendUpdateDailyInfo(Player player, int dailyId) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            return;
        }
        int activeBaseMaxValue = getDailyActiveMax(player);
        DailyActiveData data = player.getDailyActiveData();
        DailyactiveMessage.ResUpdateDailyActiveInfo.Builder builder = DailyactiveMessage.ResUpdateDailyActiveInfo.newBuilder();
        builder.setValue(data.getActiveNum());
        builder.setUseItemCount((int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxCount));
        builder.setActiveAdded((int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxNum));
        builder.setActiveMax(activeBaseMaxValue + builder.getActiveAdded());
        DailyactiveMessage.dailyActiveInfo.Builder info = DailyactiveMessage.dailyActiveInfo.newBuilder();
        info.setActiveId(dailyId);

        int remainCount = getDailyRemainCount(player, dailyId);
        int canBuyCount = getDailyCanBuyCount(player, dailyId);
        info.setRemainCount(remainCount);
        info.setCanBuyCount(canBuyCount);

        boolean conditionOpen = player.getLevel() >= bean.getOpenLevel();
        info.setConditionOpen(conditionOpen);
        builder.setInfo(info);
        MessageUtils.send_to_player(player, DailyactiveMessage.ResUpdateDailyActiveInfo.MsgID.eMsgID_VALUE, builder.build().toByteArray());

    }

    private int getDailyActiveMax(Player player) {
        Integer activeBaseMaxValue = 0;
        for (int i = 0; i < Global.EveryDay_Activity_Point_Max.size(); i++) {
            ReadArray<Integer> array = Global.EveryDay_Activity_Point_Max.get(i);
            if (player.getLevel() >= array.get(0)) {
                activeBaseMaxValue = array.get(1);
            }
        }
        activeBaseMaxValue += (int) Manager.countManager.getVariant(player, VariantType.AddDailyActiveMaxNum);
        activeBaseMaxValue += Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_9);
        return activeBaseMaxValue;
    }

    /**
     * 领取活跃度奖励宝箱
     */
    @Override
    public void OnReqGetActiveReward(Player player, DailyactiveMessage.ReqGetActiveReward messInfo) {
        DailyActiveData dailyActiveData = player.getDailyActiveData();
        Cfg_Daily_reward_Bean bean = CfgManager.getCfg_Daily_reward_Container().getValueByKey(messInfo.getId());
        if (bean == null) {
            logger.error("Cfg_Active_rewardBean配置表不存在：" + messInfo.getId());
            return;
        }
        //已经领取了
        if (dailyActiveData.getAcRewardList().contains(messInfo.getId())) {
            logger.error("已经领取了");
            return;
        }
        //活跃点不足
        int dailyActiveValue = dailyActiveData.getActiveNum();
        if (dailyActiveValue < bean.getQ_needintegral()) {
            return;
        }
        //发送奖励
        ReadIntegerArrayEs list = bean.getQ_reward_item();
        if (list.isEmpty()) {
            logger.error("日常活跃奖励未配置");
            return;
        }
        long actionId = IDConfigUtil.getLogId();
        List<Item> items = Item.createItems(list);
        if (!Manager.backpackManager.manager().addItems(player, items, ItemChangeReason.ActiveRewardGet, actionId)) {
            Manager.mailManager.sendMailToPlayer(player.getId(), MessageString.System, MessageString.System
                    , MessageString.System, MessageString.BAG_FULL_MAIL_CONTENT, items, ItemChangeReason.ActiveRewardGet, actionId);
        }

        //保存活跃度奖励进度
        dailyActiveData.getAcRewardList().add(messInfo.getId());

        //返回消息
        ResGetActiveReward.Builder msg = ResGetActiveReward.newBuilder();
        msg.setResult(0);
        msg.addAllDrawIdList(dailyActiveData.getAcRewardList());
        MessageUtils.send_to_player(player, ResGetActiveReward.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        //写日志
        writeReceiveActiveRewardLog(player, messInfo.getId(), dailyActiveValue, list.toString(), actionId);
    }

    @Override
    public boolean checkCanEnterActive(Player player, Cfg_Daily_Bean bean) {

        //是否可以显示
        if (bean == null) {
            return false;
        }
        //容错前后端时间tick 间隔
        if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) == DailyClose) {
            checkOpen(bean);
        }

        //是否不在活动时间内
        if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) != DailyOpen) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Daily_Not_In_Time);
            return false;
        }

        //-1表示不限次数
        if (bean.getTimes() == -1 || bean.getTimes_hide() == 1) {
            return true;
        }

        //世界boss特殊处理，不受次数限制
        if (bean.getId() == DailyActiveDefine.WORLD_BOSS.getValue()
                || bean.getId() == DailyActiveDefine.SUIT_BOSS.getValue()) {
            return true;
        }

        //是否还有剩余次数
        int remainCount = getDailyRemainCount(player, bean.getId());

        if (remainCount == 0) {
            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Daily_Count_Not_Enough);
            return false;
        }
        return true;
    }

    @Override
    public void joinDailyActive(Player player, int dailyId, int param, boolean isGm) {
        if (player == null) {
            return;
        }
        if (player.playerCrossData.crossState != CrossState.PCS_LOCAL) {
            return;
        }
        Cfg_Daily_Bean bean = Cfg_Daily_Container.GetInstance().getValueByKey(dailyId);
        if (!isGm && !checkCanEnterActive(player, bean)) {
            return;
        }
        //检查等级是否满足
        Cfg_Clone_map_Bean cloneMapBean = CfgManager.getCfg_Clone_map_Container().getValueByKey(param);
        if (cloneMapBean != null && (player.getLevel() < cloneMapBean.getMin_lv() || player.getLevel() > cloneMapBean.getMax_lv())) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CLONEENTERNEEDLEVEL, cloneMapBean.getMin_lv());
            return;
        }
        if (cloneMapBean != null) {
            Cfg_Mapsetting_Bean mapBean = Cfg_Mapsetting_Container.GetInstance().getValueByKey(cloneMapBean.getMapid());
            if (mapBean != null) {
                if (!Manager.mapManager.base(mapBean.getIsscript()).canEnterMap(player, param, 0)) {
                    return;
                }
            }
        }
        try {
            DailyActiveDefine dailyActiveDefine = DailyActiveDefine.find(dailyId);
            if (dailyActiveDefine == null) {
                return;
            }
            switch (dailyActiveDefine) {
                case GUILD_LAST_BATTLE:
                    Manager.guildActivityManager.guildLastBattle().enterMap(player);
                    break;
                case FUD_ACTIVITY_BOSS:
                case ACTIVITY_LOCAl_SOULANIMALISLAND:
                case HOME_BOSS:
                case GemBoss:
                    enterDailyMap(player, dailyId, param);
                    break;
                case Un_Limit_BOSS:
                    Manager.copyMapManager.manager().onReqCopyMapEnter(player, param, 1);
                    break;
                case SUIT_BOSS:
                case WORLD_BOSS:
                    enterWorldBoss(player, param);
                    break;
                case YZZD:
                    enterYZZD(player, bean);
                    break;
                case GodDevilWar:
                    Manager.copyMapManager.manager().onReqCopyMapEnter(player, bean.getCloneID().getValue()[0], 0);
                    break;
                case GUILD_ACTIVITY_GUARD:
                    enterGuildGuardMap(player, param);
                    break;
                case ACTIVITY_WORLD_BONFIRE:
                    enterWorldBonfire(player);
                    addDailyProgress(player, DailyActiveDefine.ACTIVITY_WORLD_BONFIRE, 1);
                    break;
                case ACTIVITY_CROSS_UNIVERSEWAR:
                    enterUniverseWar(player);
                    break;
                case ACTIVITY_GUILDBATTLE:
                    enterGuildBattle(player);
                    break;
                case CrossHosreBoss:
                    enterCrossHorseBoss(player, param);
                    break;

                default:
                    break;
            }
        } catch (Exception exception) {
            logger.error(exception);
        }
    }


    //进入跨服坐骑BOSS
    private void enterCrossHorseBoss(Player player, int cfgId) {

        Cfg_Bossnew_HorseBoss_Bean bean = CfgManager.getCfg_Bossnew_HorseBoss_Container().getValueByKey(cfgId);
        if (bean == null) {
            logger.error("Cfg_Bossnew_HorseBoss_Bean  is nul  {} ", cfgId);
            return;
        }
        if (player.getHorse().getHorseEquipTotalScore() < bean.getPower()) {
            logger.error("脉轮评分不足   {} ", player.getHorse().getHorseEquipTotalScore());
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.HorseBossNoPower);
            return;
        }
        CrossHorseBossMessage.G2PReqEnterHorseBoss.Builder msg = CrossHorseBossMessage.G2PReqEnterHorseBoss.newBuilder();
        msg.setPlayerId(player.getId());
        msg.setCfgID(cfgId);
        MessageUtils.send_to_public(CrossHorseBossMessage.G2PReqEnterHorseBoss.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    private boolean enterWorldBonfire(Player player) {
        Manager.retrieveResManager.getScript().count(player, RetrieveType.WorldBonfire);
        WorldBonfireMessage.G2PWorldBonfireEnter.Builder msg = WorldBonfireMessage.G2PWorldBonfireEnter.newBuilder();
        msg.setRoleID(player.getId());
        MessageUtils.send_to_public(WorldBonfireMessage.G2PWorldBonfireEnter.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        return true;
    }

    private void enterUniverseWar(Player player) {
        MSG_UniverseMessage.G2PEnterDaily.Builder msg = MSG_UniverseMessage.G2PEnterDaily.newBuilder();
        msg.setRoleId(player.getId());
        MessageUtils.send_to_public(MSG_UniverseMessage.G2PEnterDaily.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * 进入世界boss地图
     *
     * @param mapModelId 地图modelId
     */
    private void enterWorldBoss(Player player, int mapModelId) {
        Manager.mapManager.changeMap(player, mapModelId, null, -1, false);
    }

    private void enterYZZD(Player player, Cfg_Daily_Bean bean) {
        int floor = 0;
        if (DailyActiveManager.bravePeakMapInfo.getPlayerFloor().containsKey(player.getId())) {
            floor = DailyActiveManager.bravePeakMapInfo.getPlayerFloor().get(player.getId());
        }
        if (floor >= BravePeakDefine.BRAVE_PEAK_MAX_FLOOR) {
            MessageUtils.notify_player(player, Notify.ERROR, MessageString.CHALLENGEALLOVER);
            return;
        }
        Manager.copyMapManager.manager().onReqCopyMapEnter(player, bean.getCloneID().getValue()[0], 0);
    }


    /**
     * 进入活动地图
     *
     * @param mapModelId 地图modelId
     */
    private void enterDailyMap(Player player, int dailyId, int mapModelId) {
        ConcurrentHashMap<Integer, Long> dailyMapList = DailyActiveManager.dailyMap.get(dailyId);
        if (dailyMapList == null) {
            logger.error("日常活动地图未创建！！！");
            return;
        }
        Long mapId = dailyMapList.getOrDefault(mapModelId, 0L);
        if (mapId == 0) {
            logger.error("进入活动地图失败，未创建活动地图！！！dailyId:" + dailyId + ", mapModelId:" + mapModelId);
            return;
        }
        Manager.mapManager.changeMap(player, mapId, null, false);
    }

    private void enterGuildGuardMap(Player player, int mapModelId) {
        //TODO 暂时不用，用的时候再做调整
//        if (!player.isHaveGuild()) {
//            MessageUtils.notify_player(player, Notify.NORMAL, MessageString.Not_Join_Guild);
//            return;
//        }
//        Cfg_Clone_map_Bean clone_map_bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(mapModelId);
//
//        List<MapObject> mapObjects = Manager.mapManager.getMaps(clone_map_bean.getMapid());
//        MapObject mapObject = null;
//        if (mapObjects == null || mapObjects.isEmpty()) {
//            mapObject = Manager.mapManager.createCopyMap(mapModelId, player.getGuildId());
//        } else {
//            for (MapObject map : mapObjects) {
//                if (MapParam.getMapAffiliation(map) == player.getGuildId()) {
//                    mapObject = map;
//                    break;
//                }
//            }
//        }
//
//        if (mapObject == null) {
//            return;
//        }
//        Manager.mapManager.changeMap(player, mapObject.getId(), null, false);
    }

    /**
     * 进入仙盟争霸
     */
    private void enterGuildBattle(Player player) {
        if (!player.isHaveGuild()) {
            return;
        }

        GuildBattle guildBattle = Manager.guildBattleManager.getGuildbattles().get(player.getGuildId());
        if (guildBattle == null) {
            logger.error("玩家:" + player + "当前仙盟没有参加争霸赛" + player.getGuildId());
            return;
        }

        MapObject mapObject = Manager.mapManager.getMap(guildBattle.getMapId());
        if (mapObject == null) {
            logger.error("玩家:" + player + "当前仙盟争霸赛地图找不到" + guildBattle.getMapId());
            return;
        }

        ICopyReliveScript iScript = (ICopyReliveScript) Manager.scriptManager.GetScriptClass(ScriptEnum.GuildBatleMapScript);
        Position position = iScript.doCreateRelivePosition(mapObject, player);
        Manager.mapManager.changeMap(player, mapObject.getId(), position, false);
    }

    /**
     * 请求/设置推送列表
     */
    public void onReqDailyPushIds(Player player, DailyactiveMessage.ReqDailyPushIds mess) {
        // List<Integer> focusList = player.getDailyActiveData().getFocusList();
        //默认推送列表
        // if (focusList.isEmpty()) {
        //     for (Cfg_Daily_notice_Bean bean : Cfg_Daily_notice_Container.GetInstance().getValuees()) {
        //         if (bean.getReveal() == 1 && bean.getType() == 1) {
        //             focusList.add(bean.getID());
        //         }
        //     }
        // }
//
        // DailyactiveMessage.ResDailyPushResult.Builder builder = DailyactiveMessage.ResDailyPushResult.newBuilder();
        // if (!mess.getActiveIdListList().isEmpty()) {
        //     Cfg_Daily_notice_Bean[] list = Cfg_Daily_notice_Container.GetInstance().getValuees();
        //     for (Cfg_Daily_notice_Bean bean : list) {
        //         if (bean.getReveal() == 0) {
        //             mess.getActiveIdListList().remove(Integer.valueOf(bean.getID()));
        //         }
        //     }
        //     player.getDailyActiveData().setFocusList(mess.getActiveIdListList());
        // }
        // builder.addAllActiveIdList(player.getDailyActiveData().getFocusList());
        // MessageUtils.send_to_player(player, DailyactiveMessage.ResDailyPushResult.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }

    private void writeReceiveActiveRewardLog(Player player, int id, int dailyActiveValue, String activeReward, long actionId) {
        ReceiveActiveRewardLog log = new ReceiveActiveRewardLog();
        log.setPlayer(player);
        log.setCfgId(id);
        log.setDailyActiveValue(dailyActiveValue);
        log.setActiveReward(activeReward);
        log.setActionId(actionId);
        LogService.getInstance().execute(log);
    }

    private void sendClientDailyOpenInfo(int dailyId, boolean open) {
        DailyactiveMessage.ResDailyActiveOpen.Builder builder = DailyactiveMessage.ResDailyActiveOpen.newBuilder();
        builder.setDailyId(dailyId);
        builder.setOpen(open);
        MessageUtils.send_to_all_player(DailyactiveMessage.ResDailyActiveOpen.MsgID.eMsgID_VALUE, builder.build().toByteArray());
    }


    public void writeEnterDailyActivityLog(int dailyId, int modelId, long roleId, int level) {
        EnterDailyActivityLog dailyLog = new EnterDailyActivityLog();
        dailyLog.setDailyId(dailyId);
        dailyLog.setModelId(modelId);
        dailyLog.setRoleId(roleId);
        dailyLog.setLevel(level);
        dailyLog.setSid(ServerConfig.getServerId());
        dailyLog.setPlat(ServerConfig.getServerPlatform());
        LogService.getInstance().execute(dailyLog);
    }

    public void P2FSendDailyState(DailyactiveMessage.P2FSendDailyState messInfo) {
        if (messInfo.getDailyID() == DailyActiveDefine.EIGHT_DIAGRAMS.getValue()) {
            Manager.eightDiagramsManager.deal().EightDiagramsChangeState(messInfo.getIsOpen());
        }

        logger.info("P2FSendDailyState      " + messInfo.getIsOpen() + " dailyID " + messInfo.getDailyID());
    }

    @Override
    public long getDailyNearlyEndTime(int dailyId) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            return 0;
        }

        long now = TimeUtils.Time();
        if (bean.getIfcross() == 0 && Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) != DailyOpen) {
            return 0;
        }
        int nowMin = TimeUtils.getDayOfHour(now) * 60 + TimeUtils.getDayOfMin(now);
        ReadIntegerArrayEs time = bean.getTime();
        for (int i = 0; i < time.size(); i++) {
            int startTime = time.get(i).get(0);
            int endTime = time.get(i).get(1);
            if (nowMin >= startTime && nowMin <= endTime) {
                return TimeUtils.getTodayBeginTime() + endTime * 60 * 1000;
            }
        }
        return 0;
    }

    @Override
    public int getDailyCanBuyCount(Player player, int dailyId) {
        Cfg_Daily_Bean bean = Cfg_Daily_Container.GetInstance().getValueByKey(dailyId);
        if (bean.getIf_add_count() != 1) {
            return 0;
        }
        if (bean.getTimes() < 0) {
            return 0;
        }
        int vipPowerType = getVipPowerTypeByDailyId(dailyId);
        if (vipPowerType != 0) {
            int times = Manager.vipManager.power().getVipPurNum(player, vipPowerType);
            int buyTimes = player.getDailyActiveData().getDailyBuyCount().getOrDefault(dailyId, 0);
            return Math.max(0, times - buyTimes);
        }
        return 0;
    }

    @Override
    public int getDailyBuyNeedGold(int dailyId, int buyCount) {
        int vipPowerType = getVipPowerTypeByDailyId(dailyId);
        if (vipPowerType != 0) {
            return Manager.vipManager.power().getVipAddNumPrice(buyCount, vipPowerType);
        }
        return 0;
    }

    @Override
    public int getDailyRemainCount(Player player, int dailyId) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            return 0;
        }
        int freeTimes = bean.getTimes();
        if (freeTimes < 0) {
            return freeTimes;
        }
        if (bean.getId() == DailyActiveDefine.JJC.getValue()) {
            freeTimes += Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_11);
        }
        DailyActiveData dailyActiveData = player.getDailyActiveData();
        int joinCount = dailyActiveData.getDailyProgress().getOrDefault(dailyId, 0);
        int buyCount = dailyActiveData.getDailyBuyCount().getOrDefault(dailyId, 0);
        int itemAddCount = dailyActiveData.getItemAddCount().getOrDefault(dailyId, 0);
        return Math.max(freeTimes + buyCount + itemAddCount - joinCount, 0);
    }

    /**
     * 获取日常最大次数
     *
     * @param player
     * @param dailyId
     */
    @Override
    public int getDailyMaxCount(Player player, int dailyId) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            return 0;
        }
        int freeTimes = bean.getTimes();
        if (freeTimes < 0) {
            return freeTimes;
        }
        if (bean.getId() == DailyActiveDefine.JJC.getValue()) {
            freeTimes += Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_11);
        }
        DailyActiveData dailyActiveData = player.getDailyActiveData();
        int buyCount = dailyActiveData.getDailyBuyCount().getOrDefault(dailyId, 0);
        int itemAddCount = dailyActiveData.getItemAddCount().getOrDefault(dailyId, 0);
        return freeTimes + buyCount + itemAddCount;
    }

    /**
     * 活动是否开启
     *
     * @param dailyId
     * @return
     */
    @Override
    public boolean isOpen(int dailyId) {
        return Manager.dailyActiveManager.getDailyOpenState().getOrDefault(dailyId, DailyClose) == DailyOpen;
    }

    @Override
    public boolean isInGuildDaily() {
        for (Map.Entry<Integer, Integer> daily : Manager.dailyActiveManager.getDailyOpenState().entrySet()) {
            Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(daily.getKey());
            int state = daily.getValue();
            if (state == DailyOpen && bean != null && bean.getIsSignOut() == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean checkInBossMap(Player player, int dailyId) {
        DailyActiveDefine daily = Utils.findOne(BossDailyIds, d -> d.getValue() == dailyId);
        if (daily != null) {
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map == null) {
                return false;
            }
            Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
            for (int i = 0; i < bean.getCloneID().size(); i++) {
                if (map.getMapModelId() == bean.getCloneID().get(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    public int getVipPowerTypeByDailyId(int dailyId) {
        int vipPowerType = 0;
        DailyActiveDefine dailyActiveDefine = DailyActiveDefine.find(dailyId);
        if (dailyActiveDefine == null) {
            return vipPowerType;
        }
        switch (dailyActiveDefine) {
            case JJC:
                vipPowerType = VipPower.POWER_23;
                break;
            case ExpCopy:
                vipPowerType = VipPower.POWER_13;
                break;
            case EquipCopy:
                vipPowerType = VipPower.POWER_14;
                break;
            case StrengthenCopy:
                vipPowerType = VipPower.POWER_15;
                break;
            case WORLD_BOSS:
                vipPowerType = VipPower.POWER_16;
                break;
            case SUIT_BOSS:
                vipPowerType = VipPower.POWER_17;
                break;
            case GemBoss:
                vipPowerType = VipPower.POWER_18;
                break;
            case StateBoss:
                vipPowerType = VipPower.POWER_20;
                break;
            case FairyLand:
                vipPowerType = VipPower.POWER_22;
                break;
            default:
                break;
//                logger.error("未找到日常对应的vipPowerType:" + dailyId);
        }
        return vipPowerType;
    }

}
