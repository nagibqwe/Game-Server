package common.register;

import com.game.behavior.manager.BehaviorManager;
import com.game.bi.biqq.QQLogType;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapDefine;
import com.game.peak.timer.PeakCancelMatchEvent;
import com.game.peak.timer.PeakCloneEvent;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.player.structs.SessionAttribute;
import com.game.register.script.IQuitGame;
import com.game.roleLog.RoleUpdateLogService;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import game.core.message.SMessage;
import game.core.script.IScript;
import game.core.util.SessionUtils;
import game.core.util.TimeUtils;
import game.message.CopyMapMessage.ReqCopyMapOut;
import game.message.CrossServerMessage.G2FSynPlayerOut;
import game.message.PlayerMessage;
import game.message.RegisterMessage;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author admin
 */
public class RoleQuitGame implements IScript, IQuitGame {
    private static final Logger logger = LogManager.getLogger(RoleQuitGame.class);

    @Override
    public int getId() {
        return ScriptEnum.QuitGameBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void QuitGame(ChannelHandlerContext context, int reason, boolean isQuit, boolean isSendMsg) {

        if (isSendMsg) {
            sendQuitGameInfo(context, reason);
        }

        Player player = context.channel().attr(SessionAttribute.PLAYER).get();
        if (player == null) {
            Manager.registerManager.deal().tickSession(context);
            return;
        }
        logger.warn(" Начало выхода из игры player={} sessionId:{} reason={} ", player, context.channel(), reason);
        Manager.registerManager.deal().tickSession(context);
        if (isQuit) {
            logger.warn("Игрок主動 выходит из игры！player={}", player);
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map != null && map.getType() == MapDefine.COPY_MAP) {
                Manager.copyMapManager.outZone(player);//Выход из подземелья
            }
            if (player.playerCrossData.isToFightServer()) {
                ReqCopyMapOut.Builder msg = ReqCopyMapOut.newBuilder();
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), ReqCopyMapOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            }
        }
        try {
            //BI при выходе игрока
            boolean canRes = Manager.retrieveResManager.getScript().canRetrieveRes(player);
            Manager.biManager.getScript().biLogout(player, reason, canRes ? 1 : 0);
            Manager.biManager.getScript().biRole_info(player);

            Manager.biManager.get4399Script().updatePlayer(player);
            Manager.biManager.get4399Script().quitBiTo4399(player, reason, "");
            Manager.biManager.getQQScript().log(player, QQLogType.logout);
            QuitGame(player);

        } catch (Exception e) {
            logger.error("sessionId:" + context.channel() + " ошибка при выходе из игры. Причина: " + reason, e);
        }
    }

    //Отправка сообщения о выходе клиенту
    void sendQuitGameInfo(ChannelHandlerContext context, int reason) {
        try {
            RegisterMessage.ResQuit.Builder msg = RegisterMessage.ResQuit.newBuilder();
            msg.setReason(reason);
            ChannelFuture cf = context.writeAndFlush(new SMessage(RegisterMessage.ResQuit.MsgID.eMsgID_VALUE, msg.build().toByteArray()));
            cf.awaitUninterruptibly(30, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error(e, e);
        }
    }

    void QuitGame(Player player) {
        //Если аккаунт уже вышел, пропускаем эту логику
        if (EntityState.ExitGame.compare(player.getState())) {
            return;
        }
        player.dealOffLine();//Сначала устанавливаем флаг выхода
        //Если это боевой сервер, выходим, не обрабатываем выход
        if (GameServer.getInstance().IsFightServer()) {
            logger.error("Игрок отключился на боевом сервере---" + player.getId());
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());

        player.addState(EntityState.ExitGame);
        //Очистка боевого состояния при выходе
        player.setFightState(0);
        player.getFightEnums().clear();
        player.clearHatred();
        player.getEnemys().clear();
        player.getBeEnemys().clear();
        player.getPklist().clear();

        BehaviorManager.CancelAllBehavior(player);

        //Обработка баффов
        Manager.buffManager.deal().offline(player);
        //Обработка выхода из команды
        Manager.teamManager.playerOffLine(player);
        //Питомец
        Manager.petManager.offLine(player);
        //Выход из гильдии
        Manager.guildsManager.playerOffLine(player);
        //Офлайн-автоматизация
        Manager.playerHookManager.deal().enterOfflineHook(player);
        Manager.worldHelpManager.playerOffline(player);
        Manager.retrieveResManager.getScript().switchDay(player);

        //Обработка при выходе из совместной поездки
        if (player.getHorse().isRideOther() || Manager.horseManager.getMultiPlayerHashMap().containsKey(player.getId())) {
            Manager.horseManager.deal().onReqChangeRideState(player, 0);
        }

        Manager.leaderPreachManager.getScript().offline(player);
        Manager.registerManager.deal().writeRoleLoginLog(player);
        Manager.saveThreadManager.getSavePlayerThread().addRole(Manager.playerManager.manager().makeRoleBeanByPlayer(player));
        //Сохранение данных предметов
        RoleUpdateLogService.getInstance().updateRoleItemData(player.getId());
        //Сохранение актуальных данных в rolestate
        RoleUpdateLogService.getInstance().updateRoleDate(player.getId());

        //Выход с карты
        Manager.mapManager.manager().onQuitMap(map, player, true);
        //Синхронизация с мировым сервером
        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if (pwi != null) {
            pwi.setLastOffTime((int) (TimeUtils.Time() / 1000));
        }
        Manager.playerManager.manager().syncPlayerWorldInfo(player, true);
        Manager.playerManager.manager().changeLoginFight(player.getId(), player.getFightPoint());

        if (player.playerCrossData.isToFightServer()) {
            //Отправка уведомления об отключении на боевой сервер
            G2FSynPlayerOut.Builder msg = G2FSynPlayerOut.newBuilder();
            msg.setRoleId(player.getId());
            MessageUtils.send_to_public(G2FSynPlayerOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            MessageUtils.send_to_fight(player, G2FSynPlayerOut.MsgID.eMsgID_VALUE, msg.build().toByteArray());
            //Выход из матчмейкинга
            Manager.copyMapManager.manager().onReqCancelMatch(player);
            player.playerCrossData.isReqFight = false;
            logger.error("Уведомление об отключении на кросс-сервере отправлено!");
        }
        //Выход из матчмейкинга пиковой арены
        Manager.peakManager.addCommand(new PeakCancelMatchEvent(player));

        //Выход из голосовой комнаты
        Manager.chatManager.deal().playerLogout(player);

        Manager.cooldownManager.cleanAllCooldown(player);
        Manager.cooldownManager.cleanAllCooldown(Manager.petManager.getBattlePet(player));

        //Выход из сопровождения бессмертных
        Manager.couplefightManager.getCoupleEscort().onLeaveGame(player);
        //Синхронизация с социальным сервером
        PlayerWorldInfo playerWorldInfo = Manager.playerManager.getPlayerWorldInfo(player.getId());
        PlayerMessage.G2SSynPlayerSocialInfo.Builder mPlayer = PlayerMessage.G2SSynPlayerSocialInfo.newBuilder();
        mPlayer.setGlobalPlayerWorldInfo(playerWorldInfo.toGlobalPlayerWorldInfo());
        mPlayer.setType(1);
        MessageUtils.send_to_social(PlayerMessage.G2SSynPlayerSocialInfo.MsgID.eMsgID_VALUE, mPlayer.build().toByteArray());
        //Дуэль бессмертных
        Manager.couplefightManager.getScript().playerOffline(player);
        logger.info("Выход из игры завершён player:" + player);
    }

}