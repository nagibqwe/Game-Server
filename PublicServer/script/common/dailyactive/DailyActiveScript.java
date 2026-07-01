package common.dailyactive;

import com.data.CfgManager;
import com.data.bean.Cfg_Daily_Bean;
import com.data.container.Cfg_Daily_Container;
import com.data.struct.ReadArray;
import com.data.struct.ReadIntegerArrayEs;
import com.game.dailyactive.scripts.IDailyActiveScript;
import com.game.dailyactive.structs.DailyActiveDefine;
import com.game.eightdiagrams.manager.EightDiagramsManager;
import com.game.gameserver.structs.ServerInfo;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.structs.ServerType;
import com.game.utils.MessageUtils;
import com.game.worldanswer.structs.QuestionDefine;
import game.core.util.TimeUtils;
import game.message.DailyactiveMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DailyActiveScript implements IDailyActiveScript {

    final Logger logger = LogManager.getLogger(DailyActiveScript.class);

    final int DailyReady = 1;
    final int DailyOpen = 2;
    final int DailyClose = 3;

    @Override
    public int getId() {
        return ScriptEnum.DailyActiveScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    /**
     * 今天是否开放活动
     *
     * @param bean
     * @param time
     * @return
     */
    boolean checkOpenDay(Cfg_Daily_Bean bean, long time) {
        if (bean.getIf_open() == 0) {
            return false;
        }
        int curOpenDay = TimeUtils.getOpenServerDay();
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

    @Override
    public boolean isActiveOpen(int dailyId) {
        return Manager.dailyActiveManager.getDailyOpenState().getOrDefault(dailyId, DailyClose) == DailyOpen;
    }

    @Override
    public void timerTicker(long nowTime, long lastTime) {

        Cfg_Daily_Bean[] beans = Cfg_Daily_Container.GetInstance().getValuees();
        for (Cfg_Daily_Bean bean : beans) {

            if (Manager.dailyActiveManager.getGmControl().containsKey(bean.getId())) {
                continue;
            }
            //是否跨服活动
            if (bean.getIfcross() == 0) {
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
                    doDailyReady(bean, DailyReady);
                    logger.info("日常活动id={}【{}】准备阶段 stage={}", bean.getId(), bean.getName(), check[2]);
                }
                continue;
            }
            if (Manager.dailyActiveManager.getDailyOpenState().getOrDefault(bean.getId(), DailyClose) != DailyOpen) {
                activeBegin(bean, check[0]);
            }
        }
    }


    /**
     * 活动准备阶段
     *
     * @param bean
     * @param stage
     */
    private void doDailyReady(Cfg_Daily_Bean bean, int stage) {
        switch (bean.getId()) {
            case DailyActiveDefine.WORLD_ANSWER_QUESTION:
                Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(stage);
                break;
            case DailyActiveDefine.EIGHT_DIAGRAMS:
                EightDiagramsManager.getInstance().deal().preparationPhase();
                break;
            default:
                break;
        }
    }

    @Override
    public void activeBegin(Cfg_Daily_Bean bean, int index) {

        Manager.dailyActiveManager.getDailyOpenState().put(bean.getId(), DailyOpen);
        P2FSendDailyState(bean.getId(), true);
        logger.info("日常活动id={}【{}】开始阶段", bean.getId(), bean.getName());

        try {
            switch (bean.getId()) {
                case DailyActiveDefine.CROSS_Alien_Boss:
                    Manager.fudManager.alien().activeBegin();
                    break;
                case DailyActiveDefine.CROSS_FUD_Devil:
                    Manager.fudManager.devil().activeBegin();
                    break;
                case DailyActiveDefine.CrossFud:
                    Manager.fudManager.deal().activeBegin();
                    break;
                case DailyActiveDefine.PeakPk:
                    Manager.peakManager.deal().start();
                    break;
                case DailyActiveDefine.WORLD_ANSWER_QUESTION:
                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(QuestionDefine.ActivityPhase_AnswerPlay_2);
                    break;
                case DailyActiveDefine.NINE_DAYS_FOCUSED:
                    Manager.nineDaysFocusedManager.deal().onStart();
                    break;
                case DailyActiveDefine.EIGHT_DIAGRAMS:
                    EightDiagramsManager.getInstance().deal().eightDiagramsStart();
                    break;
                case DailyActiveDefine.ACTIVITY_WORLD_BONFIRE:
                    Manager.worldBonfireManager.manager().beginWorldBonfire(index);
                    break;
                case DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR:
                    Manager.universeManager.manager().createRoom(bean, index);
                    break;
                case DailyActiveDefine.COUPLE_FIGHT:
                    Manager.couplefightManager.getScript().start();
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    @Override
    public void activeEnd(Cfg_Daily_Bean bean) {
        Manager.dailyActiveManager.getDailyOpenState().put(bean.getId(), DailyClose);
        P2FSendDailyState(bean.getId(), false);
        logger.info("日常活动id={}【{}】关闭阶段", bean.getId(), bean.getName());

        try {
            switch (bean.getId()) {
                case DailyActiveDefine.CROSS_Alien_Boss:
                    Manager.fudManager.alien().activeEnd();
                    break;
                case DailyActiveDefine.CROSS_FUD_Devil:
                    Manager.fudManager.devil().activeEnd();
                    break;
                case DailyActiveDefine.CrossFud:
                    Manager.fudManager.deal().activeEnd();
                    break;
                case DailyActiveDefine.PeakPk:
                    Manager.peakManager.deal().close();
                    break;
                case DailyActiveDefine.WORLD_ANSWER_QUESTION:
                    Manager.worldAnswerManager.getIWorldAnswer().worldAnswerStageChange(QuestionDefine.ActivityPhase_AnswerPlay_3);
                    break;
                case DailyActiveDefine.NINE_DAYS_FOCUSED:
                    Manager.nineDaysFocusedManager.deal().onOver();
                    break;
                case DailyActiveDefine.EIGHT_DIAGRAMS:
                    EightDiagramsManager.getInstance().deal().eightDiagramsOver();
                    break;
                case DailyActiveDefine.ACTIVITY_WORLD_BONFIRE:
                    Manager.worldBonfireManager.manager().endWorldBonfire();
                    break;
                case DailyActiveDefine.ACTIVITY_CROSS_UNIVERSEWAR:
                    Manager.universeManager.manager().removeRoom(bean);
                    break;
                case DailyActiveDefine.COUPLE_FIGHT:
                    Manager.couplefightManager.getScript().close();
                default:
                    break;
            }
        } catch (Exception e) {
            logger.error(e, e);
        }

    }

    private void P2FSendDailyState(int dailyID, boolean isOpen) {
        DailyactiveMessage.P2FSendDailyState.Builder msg = DailyactiveMessage.P2FSendDailyState.newBuilder();
        msg.setDailyID(dailyID);
        msg.setIsOpen(isOpen);
        List<ServerInfo> list = Manager.gameServerManager.GetType(ServerType.FIGHTSERVER);
        for (ServerInfo serverInfo : list) {
            MessageUtils.send_to_game(serverInfo.getSession(), DailyactiveMessage.P2FSendDailyState.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }
    }

    @Override
    public long getDailyNearlyEndTime(int dailyId) {
        Cfg_Daily_Bean bean = CfgManager.getCfg_Daily_Container().getValueByKey(dailyId);
        if (bean == null) {
            return 0;
        }

        long now = TimeUtils.Time();
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
}
