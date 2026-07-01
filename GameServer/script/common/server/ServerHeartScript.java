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
 * 此脚本只处理系统事件， 请不要使用来处理地图里面的清除逻辑操作
 *
 * @author admin
 */
public class ServerHeartScript implements IScript, IAction {

    final int ZeroClock = 0;    //0点刷新
    final int FiveClock = 5;    //5点刷新

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

        //TODO 世界等级处理
       //try {
       //    computeNewWorldLevel();
       //} catch (Exception e) {
       //    log.error(e, e);
       //}

        long curTime = TimeUtils.Time();
        int curHour = TimeUtils.getDayOfHour(curTime);
        int curMin = TimeUtils.getDayOfMin(curTime);

        //TODO  0点时清理错误日志
        if (curHour == 0) {
            int curDay = TimeUtils.getCurDay(0);
            ErrorLogThread.clearErrorLog(curDay);
            Manager.bossManager.calcBossRebornBaseTime();
        }

        // 如果是跨服战服务器
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }

        /*===========================================
         *
         * 如果不是跨服才后执行下面的逻辑， 请大家一定要注意
         *
         ===========================================*/

        //0点刷新
        if (Manager.countManager.getServerVariant(VariantType.ZeroClock) == 0) {
            Manager.countManager.setServerVariant(VariantType.ZeroClock, 1);
            log.info("服务器整点0刷新 day={}", TimeUtils.format2string(curTime));
            zeroClockDeal();

            //强制改为0点刷新
            fiveClockDeal();
        }
        //5点刷新
        if (Manager.countManager.getServerVariant(VariantType.FiveClock) == 0) {
            Manager.countManager.setServerVariant(VariantType.FiveClock, 1);
            log.info("服务器整点5刷新 day={}", TimeUtils.format2string(curTime));

        }

        //每隔一小时
        if (curMin % 60 == 0) {
            Manager.activityManager.deal().everyHourDeal();
        }

        //TODO  玩家数据保存
        Manager.playerManager.manager().TickSavePlayer();
        //TODO 是否有新的活动开始
        Manager.activityManager.deal().checkAllActivity();
        //TODO  检查世界boss出生
        Manager.bossManager.calcBossBirth();
        //TODO  循环公告的tick
        Manager.loopNotifyManager.loopNotifyTick();
        //TODO 传道
        Manager.leaderPreachManager.getScript().action();
        //TODO 天禁令
        Manager.fallingSkyManager.deal().tick();
        //TODO v4 助力
        Manager.v4HelpManager.deal().tick();
    }

    //TODO 5点刷新
    public void fiveClockDeal() {
        for (Player player : Manager.playerManager.getPlayersCache().values()) {
            if (player.isOnline()) {
                //Manager.retrieveResManager.getScript().switchDay(player);
                //TODO 五点处理运营活动玩家数据
                Manager.activityManager.deal().fiveClockPlayerDeal(player);
            }

        }
        //TODO 5点处理运营活动数据
        Manager.activityManager.deal().fiveClockDeal();
    }

    //TODO 零点刷新
    private void zeroClockDeal() {
        //功能任务刷新
        Manager.functionTaskManager.getScript().init();

        for (Map.Entry<Long, Player> entry : Manager.playerManager.getPlayersCache().entrySet()) {
            Player player = entry.getValue();
            if (player.isOnline()) {
                Manager.playerManager.manager().zeroClockPlayerDeal(player);
                //TODO 零点处理运营活动玩家数据
                Manager.activityManager.deal().zeroClockPlayerDeal(player);
                //TODO 第二天设置登陆记录状态
                Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);

                //TODO 天禁令每日任务刷新
                Manager.fallingSkyManager.deal().onDailyRefreshTask(player);

                //零点清除好友每天变量
                Manager.friendManager.deal().zeroClockDeal(player);

                //资源找回零点刷新
                Manager.retrieveResManager.getScript().switchDay(player);

                //刷新功能任务
                Manager.functionTaskManager.getScript().online(player);

                //找回未领取的特权卡
                Manager.welfareManager.playerOnline(player);

                //开服活动
                Manager.openServerAcManager.deal().zeroClockDeal(player);
            }
        }
        //TODO 本服巅峰竞技场
        Manager.peakManager.addCommand(new PeakZeroTickEvent());
        //TODO 福地boss
        Manager.guildActivityManager.deal().checkFudiBossRedPoint();
        //TODO 福地论剑活动开启邮件
        Manager.guildActivityManager.guildLastBattle().notifyMail();
        //TODO 清理邮件的
        Manager.mailManager.clearOverTimeMail();

        //TODO 0点处理运营活动数据
        Manager.activityManager.deal().zeroClockDeal();
        //TODO 完美情缘活动
        Manager.marriageManager.activity().tick();

        Manager.rankListManager.addCommand(new ZeroClearRankHandler());
        //每天物品产销数
        Manager.logManager.crossDay();
        //仙侣对决
        Manager.couplefightManager.getScript().tick();
        //零点刷新
        Manager.v4HelpManager.deal().zeroClockDeal();
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
