package common.heart;

import com.data.CfgManager;
import com.data.Global;
import com.data.ItemChangeReason;
import com.data.bean.Cfg_Bag_grid_Bean;
import com.game.attribute.BaseIntAttribute;
import com.game.backpack.structs.ItemCoinType;
import com.game.chat.structs.ChatChannel;
import com.game.cooldown.structs.CooldownTypes;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.heart.manager.HeartManager;
import com.game.heart.script.IHeartScript;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.pet.structs.Pet;
import com.game.player.script.IPlayerBattle;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerDefine;
import com.game.player.structs.QuitGameDefine;
import com.game.player.structs.SessionAttribute;
import com.game.player.timer.PlayerHeartTimer;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.skill.structs.SkillDefine;
import com.game.structs.AttributeType;
import com.game.task.structs.Task;
import com.game.vip.structs.VipPower;
import game.core.script.IScript;
import game.core.timer.TimerEvent;
import game.core.util.IDConfigUtil;
import game.core.util.TimeUtils;
import game.message.heartMessage;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * @author admin
 */
public class HeartScript implements IScript, IHeartScript {

    private static final Logger log = LogManager.getLogger(HeartScript.class);

    @Override
    public int getId() {
        return ScriptEnum.HeartBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //加速检查,低于9000毫秒认为加速
    private final int SpeedUpCheck_TIME = 9000;
    //封停时长,单位s,两小时
    private final int FORBID_TIME = 2 * 60 * 60;

    @Override
    public void OnHeartReceive(ChannelHandlerContext session, heartMessage.ReqHeart heartInfo) {
        Validate.notNull(session);
        Validate.notNull(heartInfo);
//        LOGGER.error(session.channel() + " 收到玩家的心跳！");
        try {
            if (session.channel().attr(SessionAttribute.PLAYER).get() == null) {
                log.error("心跳检查  错误数据，为获取到玩家数据" + session.channel());
                Manager.playerManager.iQuitGame().QuitGame(session, QuitGameDefine.SocketClosed, false, true);
                return;
            }
            long time = System.currentTimeMillis();
            session.channel().attr(SessionAttribute.HeartSendTime).set(time);
            Long lastSendTime = session.channel().attr(SessionAttribute.SpeedUpCheckSendTime).get();
            if (lastSendTime == null) {
                return;
            }
            long offTime = time - lastSendTime;
            Player player = (Player) session.channel().attr(SessionAttribute.PLAYER).get();
//            LOGGER.error(player.nameIdString() + " 收到玩家的心跳！");
            //如果是10秒之内就回复了，就认为客户端加速，直接踢掉，记log，封停两小时
            if (offTime < SpeedUpCheck_TIME) {
                if (HeartManager.isForbid) {
                    int forbidTime = (int) (System.currentTimeMillis() / 1000) + FORBID_TIME;
                    player.setForbid(forbidTime);
                }
                Date date = new Date(lastSendTime);
                Date now = new Date(time);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
                String str = " roleId:" + player.getId() + " name:" + player.getName()
                        + " 上次发送的时间：" + simpleDateFormat.format(date) + " 这次接受的时间：" + simpleDateFormat.format(now) + " 结果值为：" + offTime
                        + " 客户端上次发送时间：" + session.channel().attr(SessionAttribute.LASTCLIENTSENDSPEEDUP).get() + " now:" + heartInfo.getTime();
                log.error(session + "-->close [because] HeartTooQuick " + " sessionId:" + session + str);
                Manager.playerManager.iQuitGame().QuitGame(session, QuitGameDefine.HeartTooQuick, false, true);
                Manager.heartManager.writeKickOutLog(player, 0, str);
                return;
            }
            session.channel().attr(SessionAttribute.LASTCLIENTSENDSPEEDUP).set(heartInfo.getTime());
            Manager.heartManager.sendSpeedUpCheckMsg(player);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    @Override
    public void PlayerHeartTimer(TimerEvent event) {
        PlayerHeartTimer timer = (PlayerHeartTimer) event;
        MapObject map = timer.getMap();

        long curTime = TimeUtils.Time();
        for (Player player : map.getPlayers().values()) {
            dealPlayer(map, player, curTime);
        }
    }

    private void dealPlayer(MapObject map, Player player, long curTime) {
        if (player == null) {
            return;
        }
        try {
            //检查天墟战场怒气重置
            Manager.universeManager.deal().checkAnger(player);

            //战斗服不做处理
            if (!GameServer.getInstance().IsFightServer()) {
                //如果玩家地图错误主动释放
                MapObject curMap = Manager.mapManager.getMap(player.gainMapId());
                if (!curMap.equals(map)) {
                    Manager.mapManager.manager().clearPlayer(map, player);
                    log.error("主动释放玩家" + player);
                    return;
                }
            }

            //打坐经验定时发送
            Manager.playerHookManager.deal().sitDownEarning(player);

            //称号过期检测
            Manager.titleManager.deal().titleTimeOutCheck(player);
            //VIP珠宝过期检测
            Manager.vipManager.pearl().vipPearlTimeOutCheck(player);

            //测试坐标
            if (player.isTestPos()) {
                Manager.chatManager.deal().sendChatMessage(player, null, ChatChannel.CHATCHANNEL_SYSTEM, 0, "坐标{" + (int) player.gainX() + "," + (int) player.gainY() + "}", 0);
            }
            if (player.isInBattle()) {
                //如果是战斗状态检测是否脱战
                if (player.getLastFight() > curTime || player.getLastFight() + Global.Off_War_Time < curTime) {
                    player.setLastFight(0);
                    player.setInBattle(false);
                    player.clearHatred();
                    player.setCurAttackTargetId(0);
                    player.setBreakWakanFight(0);
                    Pet pet = Manager.petManager.getBattlePet(player);
                    if (pet != null) {
                        pet.clearHatred();
                    }
                    //计算buff
                    Manager.buffManager.deal().onLiveBattle(player);
                }
            }
            //检测玩家仇恨和被仇恨
            calcHatred(player);

            //自动回血
            if (!player.isInBattle() && !player.isDie()) {
                long roleHp = player.getAttribute().getAdditionValue(AttributeType.ATTR_RoleHp);
                if (roleHp != 0 && player.getCurHp() < player.getAttribute().MaxHP() && !Manager.buffManager.deal().haveJinLiao(player)) {
                    player.setCurHp(player.getCurHp() + (long) (player.getAttribute().MaxHP() * roleHp / 10000F));
                    player.onHpChange(player);
                }
            }
            if (!player.isInBattle()) {
                long roleWakan = player.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan);
                if (player.getCurWakan() != roleWakan) {
                    long curWakan = (long) (player.getCurWakan() + roleWakan * Global.Lingli_nofighting_recovery_num / 10000F);
                    if (curWakan >= player.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan)) {
                        curWakan = player.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan);
                    }
                    player.setCurWakan(curWakan);
                }
            } else {
                if (player.getBreakWakanFight() != 0 && (curTime - player.getBreakWakanFight()) >= Global.Lingli_fighting_recovery_time * 1000L) {
                    long roleWakan = player.getAttribute().getAdditionValue(AttributeType.ATTR_Wakan);
                    player.setCurWakan(roleWakan);
                    player.setBreakWakanFight(0);
                }
            }

            if (Manager.countManager.getVariant(player, VariantType.ZeroClock) == 0) {
                Manager.countManager.setVariant(player, VariantType.ZeroClock, 1);
                zeroClock(player);

                //强制改为0点刷新
                fiveClock(player);
            }
            if (Manager.countManager.getVariant(player, VariantType.FiveClock) == 0) {
                Manager.countManager.setVariant(player, VariantType.FiveClock, 1);
                //fiveClock(player);
            }
            //TODO 日常系统刷新
            Manager.dailyActiveManager.deal().checkDailyReset(player);
            //如果玩家不是在线状态， 则不执行下列逻辑了
            if (!player.isOnline()) {
                return;
            }
            //如果是跨服战，则后面的逻辑不处理了
            if (GameServer.getInstance().IsFightServer()) {
                return;
            }
            //如果玩家行列中已经没有此玩家， 这里先补一刀，我们查一下问题
            Player p = Manager.playerManager.getPlayerCache(player.getId());
            if (p == null) {
                Manager.playerManager.cachePlayer(player);
                log.error(player.nameIdString() + " 玩家已经不在缓存队列了， 是否需要检查一下！");
            }

            //经验倍率时间扣除
            Manager.playerHookManager.deal().decExpItemRateTime(player);
            //经验变化同步到排行榜时间
            player.setExpSyncTime(player.getExpSyncTime() + 1);
            //累计在线总时间(秒)
            player.setAccunonlinetime(player.getAccunonlinetime() + 1);
            //累计今日在线总时间(秒)
            Manager.countManager.addVariant(player,VariantType.Daily_Online_Time, 1);
            //添加背包格子开启时间
            addOpenBagCellTime(player);
            Manager.heartManager.sendReconnectSignInfo(player);
            //个人世界boss复活判断
            Manager.bossManager.manager().calcPersonWorldBossBirth(player);
            int hour = TimeUtils.getDayOfHour(curTime);
            int sec = TimeUtils.getDayOfSecond(curTime);//3秒后再计算发副本信息
            if (hour == 0 && sec > 3) {
                //将副本的所以信息通知一次
                Manager.registerManager.deal().writeRoleLoginLog(player, 2, false);
                //活动的重新通知
                // Manager.activityManager.deal().onReqGetActivityList(player);
            }

            BaseIntAttribute attackStateAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);
            BaseIntAttribute deferStateAtt = new BaseIntAttribute(AttributeType.ATTR_MAX);

            Manager.fightManager.trigger().trigger(map, player, player, deferStateAtt, attackStateAtt, SkillDefine.NORMAL, false);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    //0点刷新
    void zeroClock(Player player) {
        //红包每日清理
        player.getRedPacket().getDayredpacket().clear();
        //开服活动成长之路零点处理
        Manager.openServerAcManager.deal().onReqGrowUpNextProgress(player);
        //特殊开关关闭
        Manager.controlManager.deal().onFuncClose(player);
        //零点刷新寻宝系统
        Manager.treasureHuntManager.deal().treasureHuntBaseInfo(player);
        //重置开服活动预告奖励
        Manager.openServerAcManager.deal().resetFreeDailyReward(player);
        //TODO 零点刷新 仙匣数据
        Manager.marriageManager.manager().sendMarryBox(player);

        //限时支线任务清除
        Manager.taskManager.deal().computeTask(player, Task.BRANCH_TASK, false, true);
        //0点增加vip
        Manager.vipManager.deal().addVipExp(player, Manager.vipManager.power().getVipFreeNum(player, VipPower.POWER_26), ItemChangeReason.VipRechargeReward, IDConfigUtil.getLogId());
        Manager.countManager.setBooleanCountValue(player, BooleanDay.VipDailyExp, true);
    }

    //5点刷新
    void fiveClock(Player player) {
        //任务处理
        Manager.taskManager.deal().zeroClockDeal(player);
        //幸运抽奖刷新
        Manager.luckyDrawManager.deal().zeroClockHandler(player);
        //TODO 5点刷新 情缘副本
        Manager.marriageManager.clone().sendMarryCloneTimes(player);
    }

    //添加背包格子开启时间
    private void addOpenBagCellTime(Player player) {
        int bagCellsNum = player.getBagCellsNum();
        if (bagCellsNum >= Global.Born_Bag_Num.get(1)) {
            return;
        }

        if (bagCellsNum < Global.Born_Bag_Num.get(0)) {
            bagCellsNum = Global.Born_Bag_Num.get(0);
            player.setBagCellsNum(bagCellsNum);
        }

        int bagCellTimeCount = player.getBagCellTimeCount() + 1;

        /**
         * 现在一次开启5个格子
         * 所以定时开启这里也要改
         * */
        int sumTimes = 0;
        int curCell = 0;
        for (int i = (1000 + bagCellsNum + 1); i <= (1000 + bagCellsNum + 5); i++) {
            Cfg_Bag_grid_Bean bean = CfgManager.getCfg_Bag_grid_Container().getValueByKey(i);
            if (bean == null) {
                log.error("包裹格子 配置表错误 cellid:" + i);
                break;
            }
            sumTimes += (int) bean.getTime();
            curCell = i - 1000;
        }

        if (bagCellTimeCount < sumTimes) {
            player.setBagCellTimeCount(bagCellTimeCount);
            return;
        }
        Manager.backpackManager.manager().openBagCellSuccess(player, curCell, (byte) 0, 0, IDConfigUtil.getLogId());
    }

    //检测仇恨
    public void calcHatred(Player player) {
        long curTime = TimeUtils.Time();

        //i hatred somebady
        Iterator<Long> iter = player.getEnemys().keySet().iterator();
        List<Long> ids = new ArrayList<>();
        while (iter.hasNext()) {
            long key = iter.next();
            if (player.getEnemys().get(key) + PlayerDefine.PlayerHatredTime < curTime) {
                ids.add(key);
            }
        }

        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.PlayerBattleBaseScript);
        if (is instanceof IPlayerBattle) {
            for (long rid : ids) {
                ((IPlayerBattle) is).removeHatredPlayer(player, rid);
                log.info(player.nameIdString() + " 清理仇恨对象id:" + rid);
            }
        } else {
            log.error("没有找到IPlayerBattle脚本实例！");
        }

        //somebady hatred me
        Iterator<Long> iter2 = player.getBeEnemys().values().iterator();
        while (iter2.hasNext()) {
            long creatTime = iter2.next();
            if (creatTime + PlayerDefine.PlayerHatredTime < curTime) {
                iter2.remove();
            }
        }

        //5分钟内自卫失效
        Iterator<Long> iter3 = player.getPklist().values().iterator();
        while (iter3.hasNext()) {
            long creatTime = iter3.next();
            if (creatTime + PlayerDefine.PlayerHatredTime < curTime) {
                iter3.remove();
            }
        }

        //如果战斗服就不计算善恶值的减少
        if (GameServer.getInstance().IsFightServer()) {
            return;
        }
        //计算pk值

        if (Manager.currencyManager.manager().getCurrencyNum(player, ItemCoinType.GoodEvil) > 0) {
            if (!Manager.cooldownManager.isCooldowning(player, CooldownTypes.PkCleanCd, null)) {
                Manager.currencyManager.manager().onDecItemCoin(player, PlayerDefine.PkDecValue, ItemChangeReason.PkDec, IDConfigUtil.getLogId(), ItemCoinType.GoodEvil);
                Manager.cooldownManager.addCooldown(player, CooldownTypes.PkCleanCd, null, PlayerDefine.PkCleanTime);
            }
        }
    }

}
