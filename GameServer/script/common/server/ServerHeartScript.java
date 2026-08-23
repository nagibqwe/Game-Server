package common.server;

import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.manager.Manager;
import com.game.peak.timer.PeakZeroTickEvent;
import com.game.player.structs.Player;
import com.game.ranklist.handler.ZeroClearRankHandler;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.thread.ErrorLogThread;
import com.game.structs.IAction;
import game.core.script.IScript;
import game.core.util.TimeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Этот скрипт обрабатывает только системные события, не используйте его для логики очистки на картах
 *
 * @author admin
 */
public class ServerHeartScript implements IScript, IAction {

    final int ZeroClock = 0;    //Обновление в 00:00
    final int FiveClock = 5;    //Обновление в 05:00

    protected Logger log = LogManager.getLogger(ServerHeartScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ServerHeartBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void action() {

        long curTime = TimeUtils.Time();
        int curHour = TimeUtils.getDayOfHour(curTime);
        int curMin = TimeUtils.getDayOfMin(curTime);

        //Очистка логов ошибок в 00:00
        if (curHour == 0) {
            int curDay = TimeUtils.getCurDay(0);
            ErrorLogThread.clearErrorLog(curDay);
            Manager.bossManager.calcBossRebornBaseTime();
        }

        //Если это боевой кросс-сервер
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }

        /*===========================================
         *
         * Логика ниже выполняется только на обычных серверах
         *
         ===========================================*/

        //Обновление в 00:00
        if (Manager.countManager.getServerVariant(VariantType.ZeroClock) == 0) {
            Manager.countManager.setServerVariant(VariantType.ZeroClock, 1);
            log.info("Сервер обновлён в 00:00 day={}", TimeUtils.format2string(curTime));
            zeroClockDeal();

            //Принудительное обновление в 05:00
            fiveClockDeal();
        }
        //Обновление в 05:00
        if (Manager.countManager.getServerVariant(VariantType.FiveClock) == 0) {
            Manager.countManager.setServerVariant(VariantType.FiveClock, 1);
            log.info("Сервер обновлён в 05:00 day={}", TimeUtils.format2string(curTime));

        }

        //Каждый час
        if (curMin % 60 == 0) {
            Manager.activityManager.deal().everyHourDeal();
        }

        //Сохранение данных игроков
        Manager.playerManager.manager().TickSavePlayer();
        //Проверка начала новых событий
        Manager.activityManager.deal().checkAllActivity();
        //Проверка появления мировых боссов
        Manager.bossManager.calcBossBirth();
        //Циклические объявления
        Manager.loopNotifyManager.loopNotifyTick();
        //Передача учения
        Manager.leaderPreachManager.getScript().action();
        //Небесный запрет
        Manager.fallingSkyManager.deal().tick();
        //Помощь v4
        Manager.v4HelpManager.deal().tick();
    }

    //Обновление в 05:00
    public void fiveClockDeal() {
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (player.isOnline()) {
                //Обработка данных игрока для событий в 05:00
                Manager.activityManager.deal().fiveClockPlayerDeal(player);
            }

        }
        //Обработка данных событий в 05:00
        Manager.activityManager.deal().fiveClockDeal();
    }

    //Обновление в 00:00
    private void zeroClockDeal() {
        //Обновление функциональных заданий
        Manager.functionTaskManager.getScript().init();

        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            Player player = entry.getValue();
            if (player.isOnline()) {
                Manager.playerManager.manager().zeroClockPlayerDeal(player);
                //Обработка данных игрока для событий в 00:00
                Manager.activityManager.deal().zeroClockPlayerDeal(player);
                //Установка статуса входа на следующий день
                Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);

                //Обновление ежедневных заданий Небесного запрета
                Manager.fallingSkyManager.deal().onDailyRefreshTask(player);

                //Очистка ежедневных переменных друзей в 00:00
                Manager.friendManager.deal().zeroClockDeal(player);

                //Обновление системы возврата ресурсов
                Manager.retrieveResManager.getScript().switchDay(player);

                //Обновление функциональных заданий
                Manager.functionTaskManager.getScript().online(player);

                //Проверка неполученных привилегий
                Manager.welfareManager.playerOnline(player);

                //События открытия сервера
                Manager.openServerAcManager.deal().zeroClockDeal(player);
            }
        }
        //Пиковая арена
        Manager.peakManager.addCommand(new PeakZeroTickEvent());
        //Боссы Фуди
        Manager.guildActivityManager.deal().checkFudiBossRedPoint();
        //Отправка писем о событии "Фуди"
        Manager.guildActivityManager.guildLastBattle().notifyMail();
        //Очистка просроченных писем
        Manager.mailManager.clearOverTimeMail();

        //Обработка данных событий в 00:00
        Manager.activityManager.deal().zeroClockDeal();
        //Событие "Идеальная судьба"
        Manager.marriageManager.activity().tick();

        Manager.rankListManager.addCommand(new ZeroClearRankHandler());
        //Ежедневные данные о производстве и продаже предметов
        Manager.logManager.crossDay();
        //Дуэль бессмертных
        Manager.couplefightManager.getScript().tick();
        //Обновление в 00:00
        Manager.v4HelpManager.deal().zeroClockDeal();
    }

}

 //TODO
  //  private void computeNewWorldLevel() {
//
  //      int nowDh = TimeUtils.getYearDayHour();
  //      if (GameServer.getWorldLevelCheckTime() == nowDh) {
  //          return;
  //      }
//
  //      try {
  //          ConcurrentHashMap<Integer, Long> rankMap = RankListManager.getTempRankMap().get(RankType.LEVEL_RANK);
  //          if (rankMap == null || rankMap.size() < 1) {
  //              return;
  //          }
  //          long roleId = rankMap.get(1);
  //          RankPlayer rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
  //          if (rankPlayer == null) {
  //              return;
  //          }
//
  //          if (rankPlayer.getLevel() < 110) {
  //              return;
  //          }
//
  //          int total = 0;
  //          int k = 0;
  //          for (Integer i = 1; i <= 10; ++i) {
  //              if (rankMap.containsKey(i)) {
  //                  roleId = rankMap.get(i);
  //                  rankPlayer = RankListManager.getRankPlayerMap().get(roleId);
  //                  if (rankPlayer == null) {
  //                      continue;
  //                  }
  //                  total += rankPlayer.getLevel();
  //                  k += 1;
  //                  continue;
  //              }
  //              break;
  //          }
//
  //          int oldLevel = GlobalType.getWorldLevel();
  //          int jiuLevel = (int) (total * 1f / k + 0.5f);
  //          if (oldLevel != jiuLevel && GlobalType.setWorldLevel(jiuLevel)) {
  //              Manager.playerHookManager.deal().worldLvChange();
  //              //服务器世界等级变化，通知公共服，公共服服务器分组用 世界等级 定义档次
  //              CrossServerMessage.G2PServerWorldLvChange.Builder msg
  //                      = CrossServerMessage.G2PServerWorldLvChange.newBuilder();
  //              msg.setPlat(ServerConfig.getServerPlatform());
  //              msg.setServerId(ServerConfig.getServerId());
  //              msg.setServerWorldLv(jiuLevel);
  //              MessageUtils.send_to_public(CrossServerMessage.
  //                      G2PServerWorldLvChange.MsgID.eMsgID_VALUE, msg.build().toByteArray());
//
  //              log.info("服务器调整新的世界等级为：" + jiuLevel);
  //              WorldLevelChangeLog wlcl = new WorldLevelChangeLog();
  //              wlcl.setNewLevel(jiuLevel);
  //              wlcl.setOldLevel(oldLevel);
  //              wlcl.setPeoples(k);
  //              wlcl.setTotalLevel(total);
  //              LogService.getInstance().execute(wlcl);
//
  //              checkWorldLevelLimit();
  //          }
  //          GameServer.setWorldLevelCheckTime(nowDh);
  //      } catch (Exception exception) {
  //          log.error(exception,exception);
  //      }
  //  }
//
  //  //世界等级改变需要检测是否有 系统功能的开关条件是由世界等级来开启的
  //  private void checkWorldLevelLimit() {
  //      for (Player player : Manager.playerManager.getPlayersCache().values()) {
  //          if (!player.isOnline()) {
  //              continue;
  //          }
  //          Manager.controlManager.operate(player, FunctionVariable.WorldLevelLimit, 1);
  //      }
  //  }
}
