package common.register;

import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.count.structs.BooleanDay;
import com.game.count.structs.VariantType;
import com.game.equip.struct.EquipDefine;
import com.game.equip.struct.EquipPart;
import com.game.equip.struct.EquipPartBaseType;
import com.game.fightserver.manager.FightClientManager;
import com.game.manager.Manager;
import com.game.map.structs.MapDefine;
import com.game.map.structs.MapObject;
import com.game.map.structs.MapUtils;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.register.script.ILoadScript;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.structs.EntityState;
import com.game.utils.MessageUtils;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.TimeUtils;
import game.message.CrossFightMessage;
import game.message.CrossFightMessage.G2FOnEnterMapAgain;
import game.message.CrossFightMessage.G2PCheckCrossInfo;
import game.message.MapMessage;
import game.message.PlayerMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author admin
 */
public class LoadScript implements IScript, ILoadScript {

    private static final Logger logger = LogManager.getLogger(LoadScript.class);
    private static final Logger log = LogManager.getLogger("com.game.register.manager.RegisterManager");

    @Override
    public int getId() {
        return ScriptEnum.LoadScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public void EnterGameMap(Player player) {
        try {
            logger.info("Начало процесса входа на карту player={}", player);
            if ((Manager.countManager.getVariant(player, VariantType.Today_First_Login_Level) < 1)) {
                Manager.countManager.addVariant(player, VariantType.Today_First_Login_Level, player.getLevel());
            }

            //Если есть флаг кросс-сервера, проверяем возможность входа
            if (player.playerCrossData.toFightId > 0 && player.playerCrossData.toZoneModelId != 0) {
                player.playerCrossData.setToFightServer(false);
                player.playerCrossData.isReqFight = false;
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
                logger.info("Инициализация карты игрока завершена" + player + ", вход на кросс-сервер");
                checkCrossInfo(player);
                return;
            } else if (player.playerCrossData.toFightId != 0) {
                player.playerCrossData.toFightId = 0;
                player.playerCrossData.setToFightServer(false);
                player.playerCrossData.isReqFight = false;
                logger.info("Возврат игрока на локальный сервер" + player + ", вход на кросс-сервер " + player.playerCrossData.toFightId);

            }
            //Корректировка флага входа, если игрок не был перемещён на боевой сервер
            if (player.playerCrossData.isReqFight) {
                long now = TimeUtils.Time();
                if (now - player.playerCrossData.reqFightTime > 150000) {
                    player.playerCrossData.isReqFight = false;
                }
            }

            if (player.playerCrossData.crossState > CrossState.PCS_LOCAL) {
                player.playerCrossData.crossState = CrossState.PCS_LOCAL;
            }
            int mapId = player.gainMapModelId();

            //Если игрок в комнате ожидания кросс-сервера после перезапуска сервера
            if (mapId == 500) {
                MapObject unknowMap = Manager.mapManager.getMap(player.gainMapId());
                logger.info("Инициализация карты игрока завершена, в комнате ожидания кросс-сервера" + player);
                //Выгоняем игрока с боевого сервера
                if (unknowMap != null)
                    Manager.mapManager.manager().onQuitMap(unknowMap, player, false);
                Manager.mapManager.changeMap(player, Manager.playerManager.getBornMapID(), null, -1, true);
                return;
            }
            MapObject map = Manager.mapManager.getMap(player.gainMapId());
            if (map != null && map.getType() != MapDefine.WORLD_MAP && !map.isStop()) {
                Manager.mapManager.changeMap(player, map.getId(), player.gainCurPos(), true);
                return;
            }
            Manager.mapManager.changeMap(player, player.gainMapModelId(), player.gainCurPos(), -1, true);
        } catch (Exception ex) {
            logger.error(ex, ex);
        }
    }


    //Проверка завершения кросс-серверного боя
    public void checkCrossInfo(Player player) {
        G2PCheckCrossInfo.Builder msg = G2PCheckCrossInfo.newBuilder();
        msg.setRoleId(player.getId());
        msg.setRoomId(player.playerCrossData.toFightId);
        MessageUtils.send_to_public(G2PCheckCrossInfo.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    /**
     * Обработка ID гида главного интерфейса от клиента
     *
     * @param player
     * @param lastId
     */
    @Override
    public void OnReqUpdateMainUIGuideID(Player player, int lastId) {
        if (player == null) {
            return;
        }
        if (lastId < 1) {
            logger.error("Ошибка обновления ID гида главного интерфейса!");
            return;
        }

        player.setMainGuide(lastId);
        sendMainGuide(player);
    }

    @Override
    public void OnReqMainUIGuideID(Player player) {
        if (player != null) {
            sendMainGuide(player);
        } else {
            logger.error("OnReqMainUIGuideID: передан неверный параметр!");
        }
    }

    private void sendMainGuide(Player player) {
        PlayerMessage.ResMainUIGuideID.Builder msg = PlayerMessage.ResMainUIGuideID.newBuilder();
        msg.setGid(player.getMainGuide());
        MessageUtils.send_to_player(player, PlayerMessage.ResMainUIGuideID.MsgID.eMsgID_VALUE, msg.build().toByteArray());
    }

    //Загрузка клиентом завершена
    @Override
    public void OnReqLoadFinish(Player player) {
        //Вход на карту
        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (EntityState.ChangeMap.compare(player.getState())) {
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ChangeMap);
            return;
        }
        //При переподключении поддерживается перезагрузка
        if (EntityState.ReConnect.compare(player.getState())) {
            //Вход на карту
            if (player.playerCrossData.toFightId > 0) {
                //Отправка входа на кросс-сервер
                G2FOnEnterMapAgain.Builder entercross = G2FOnEnterMapAgain.newBuilder();
                entercross.setRoleId(player.getId());
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FOnEnterMapAgain.MsgID.eMsgID_VALUE, entercross.build().toByteArray());
                player.playerCrossData.setToFightServer(true);
                player.playerCrossData.isReqFight = false;
                return;
            }
            //Обновление позиции с использованием флага онлайн
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (pwi != null) {
                pwi.setLastOffTime(0);//Игрок онлайн
            } else {
                log.error(player + " не удалось сгенерировать данные офлайн при входе", new NullPointerException());
            }
            log.error(player.nameIdString() + " Переподключение успешно!");
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ReConnect);
            return;
        }

        //Загрузка при входе завершена
        if (EntityState.LoginGame.compare(player.getState())) {

            //Обновление позиции с использованием флага онлайн
            PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
            if (pwi != null) {
                pwi.setLastOffTime(0);//Игрок онлайн
            }
            Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);
            //Проверка и настройка данных
            checkPlayerData(player);
            //Синхронизация всех данных игрока
            Manager.playerManager.manager().OnSendPlayerAllInfo(player, false);
            //Вход на карту
            if (player.playerCrossData.toFightId > 0) {
                //Отправка входа на кросс-сервер
                G2FOnEnterMapAgain.Builder entercross = G2FOnEnterMapAgain.newBuilder();
                entercross.setRoleId(player.getId());
                ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), G2FOnEnterMapAgain.MsgID.eMsgID_VALUE, entercross.build().toByteArray());
                player.playerCrossData.setToFightServer(true);
                player.playerCrossData.isReqFight = false;
            } else {
                //Очистка пути
                player.clearRoads();
                Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            }

            player.removeSate(EntityState.LoginGame);
            logger.info(player + " Вход завершён! mapId=" + player.gainMapId());
            try {
                Manager.redPacketManager.getScript().playerLogin(player);
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }

        //Если не боевой сервер, пропускаем
        if (!GameServer.getInstance().IsFightServer()) {
            return;
        }

        if (map != null) {
            Manager.mapManager.manager().onEnterMap(player, map, player.gainCurPos());
            player.removeSate(EntityState.ChangeMap);
            MapMessage.ResJumpBlock.Builder msg = MapMessage.ResJumpBlock.newBuilder();
            msg.setId(player.getId());
            msg.setTarget(MapUtils.getPos(player.gainCurPos()));
            MessageUtils.send_to_roundPlayer(player, MapMessage.ResJumpBlock.MsgID.eMsgID_VALUE, msg.build().toByteArray(), true);
        } else {
            CrossFightMessage.F2GEnterCloneMapRes.Builder msg = CrossFightMessage.F2GEnterCloneMapRes.newBuilder();
            msg.setRoleId(player.getId());
            msg.setParam(-1);
            msg.setLineId(-1);
            msg.setX(-1);
            msg.setY(-1);
            FightClientManager.GetInstance().send_to_game(player.getIosession(), CrossFightMessage.F2GEnterCloneMapRes.MsgID.eMsgID_VALUE, msg.build().toByteArray());
        }

    }

    /**
     * Проверка и настройка данных
     * @param player
     */
    private void checkPlayerData(Player player) {
        //Проверка частей экипировки
        List<EquipPart> parts = player.getEquipParts();
        //Добавление недостающих частей
        if(parts.size() < EquipDefine.EquipPart_Num){
            for(int i= parts.size();i<EquipDefine.EquipPart_Num;i++){
                EquipPart part = new EquipPart();
                part.setCurrentExp(0);
                part.setType(i);
                part.setLevel(0);
                parts.add(part);
            }
        }

        //Инициализация слотов самоцветов
        Manager.gemManager.deal().initGemInfo(player);
    }

    //Переподключение после обрыва
    @Override
    public void reconnect(Player player) {
        player.resetState();

        Manager.countManager.setBooleanCountValue(player, BooleanDay.DailyLogin, true);
        Manager.playerManager.manager().OnSendPlayerAllInfo(player, true);
        player.removeSate(EntityState.ExitGame);
        player.addState(EntityState.Stand);
        player.addState(EntityState.ReConnect);

        if (player.playerCrossData.toFightId > 0) {
            player.playerCrossData.setToFightServer(true);
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_FIGHT;
            return;
        }

        MapObject map = Manager.mapManager.getMap(player.gainMapId());
        if (map != null && map.getType() != MapDefine.WORLD_MAP && !map.isStop()) {
            Manager.mapManager.changeMap(player, map.getId(), player.gainCurPos(), true);
            return;
        }
        Manager.mapManager.changeMap(player, player.gainMapModelId(), player.gainCurPos(), -1, true);

        PlayerWorldInfo pwi = Manager.playerManager.getPlayerWorldInfo(player.getId());
        if (pwi != null) {
            pwi.setLastOffTime(0);//Игрок онлайн
        }
    }
}