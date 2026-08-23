package common.server;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.server.GameServer;
import com.game.server.impl.MapServer;
import com.game.server.script.IhandlerScript;
import com.game.script.structs.ScriptEnum;
import com.game.thread.FriendAndRankListProcessor;
import com.game.thread.GuildProcessor;
import com.game.thread.RegisterProcessor;
import com.game.utils.MessageEnum;
import com.game.utils.MessageUtils;
import com.game.utils.Utils;
import game.core.message.MessageNumber;
import game.core.message.MsgSourceEnum;
import game.core.message.RMessage;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.*;
import game.message.heartMessage.ReqHeart;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author admin
 */
public class HandlerScript implements IScript, IhandlerScript {

    private static final Logger log = LogManager.getLogger(HandlerScript.class);

    final Integer[] IgnorePrintIDS = {
            PlayerMessage.ReqGetAccunonlinetime.MsgID.eMsgID_VALUE,
            ReqHeart.MsgID.eMsgID_VALUE,
            MapMessage.ReqDirMove.MsgID.eMsgID_VALUE,
            MapMessage.ReqStopMove.MsgID.eMsgID_VALUE,
            MapMessage.ReqPetMoveTo.MsgID.eMsgID_VALUE,
            MapMessage.ReqPetStopMove.MsgID.eMsgID_VALUE,
            FightMessage.ReqUseSkill.MsgID.eMsgID_VALUE,
            MapMessage.ReqMoveTo.MsgID.eMsgID_VALUE,
    };

    @Override
    public int getId() {
        return ScriptEnum.HandlerScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    //Проверка необходимости отправки на боевой сервер по ID протокола
    @Override
    public boolean CheckFightServerId(int msgId) {
        if (msgId == RegisterMessage.ReqLoadFinish.MsgID.eMsgID_VALUE) {
            return true;
        }
        //При выходе из подземелья на кросс-сервере
        if (msgId == CopyMapMessage.ReqCopyMapOut.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (msgId == npcMessage.ReqClickNpc.MsgID.eMsgID_VALUE) {
            return true;
        }
        if (msgId == PlayerMessage.ReqUpdataPkState.MsgID.eMsgID_VALUE){
            return true;
        }
        if (msgId == ChatMessage.ChatReqCS.MsgID.eMsgID_VALUE) {
            if (ServerConfig.isTestServer()) {
                return true;
            }
        }


        int functionID = MessageNumber.getFunction(msgId);

        if (functionID == 999) {
            return true;
        }

        switch (functionID) {
            case MessageEnum.MSG_FIGHT:
            case MessageEnum.MSG_MAP:
                //Сообщение о воскрешении обрабатывается на локальном сервере
                if (msgId == MapMessage.ReqRelive.MsgID.eMsgID_VALUE) {
                    return false;
                }
            case MessageEnum.MSG_COMMAND:
                return true;

        }


        return false;
    }

    /**
     * Фильтр протоколов, отправляемых с боевого сервера игроку
     *
     * @param msgId
     * @return
     */
    @Override
    public boolean FightSendMsgID_Filter(int msgId) {
        int functionid = MessageNumber.getFunction(msgId);
        return functionid == MessageEnum.MSG_BACKAGE;
    }

    /**
     * Диспетчеризация сообщений
     *
     * @param mess
     */
    @Override
    public void dispatch(RMessage mess) {
        try {
            if (null == mess) {
                return;
            }
            int msgid = mess.getId();

            int sourceId = MessageNumber.getSource(msgid);

            if (sourceId != MsgSourceEnum.ClientToGameServerr) {
                log.error("Получено сообщение, которое не должен обрабатывать GameServer, ID: " + msgid);
                return;
            }
            //Проверка переходных сообщений
            boolean isbl = false;
            try {
                isbl = CheckFilterMsgId(msgid, mess);
            } catch (Exception e) {
                log.error("Ошибка фильтрации ID сообщения", e);
            }
            if (isbl) {
                return;
            }

            ChannelHandlerContext iosession = mess.getContext();
            if (iosession == null) {
                log.info("Соединение уже закрыто!");
                return;
            }

            if (iosession.isRemoved()) {
                log.info("Соединение уже удаляется!");
                return;
            }

            Player player = iosession.channel().attr(SessionAttribute.PLAYER).get();
            if (player != null) {
                if ( Utils.isMessagePrint() && Utils.findOne(IgnorePrintIDS, id -> id == msgid) == null) {
                    log.warn("Игрок[{}] сообщение [ID={} name={}] message={}", player.getName(), msgid, mess.getData().getClass().getSimpleName(), mess.getData());
                }
                mess.setExecutor(player);

                //Если игрок уже на боевом сервере, все протоколы перенаправляются туда
                if ( player.playerCrossData.isToFightServer() && player.playerCrossData.toFightSid > 0) {
                    isbl = false;
                    try {
                        isbl = CheckFightServerId(msgid);
                    } catch (Exception e) {
                        log.error("Ошибка фильтрации ID сообщения для кросс-сервера", e);
                    }
                    if (isbl) {
                        ConnectFightManager.GetInstance().send_to_fight(player.playerCrossData.toFightSid, player.getId(), mess);
                        return;
                    }
                }
            }
            //Переход на кросс-сервер обрабатывается в отдельном потоке
            if (CrossFightMessage.G2FEnterCloneMap.MsgID.eMsgID_VALUE == msgid){
                GameServer.getInstance().getAssistThread().addCommand(mess);
                return;
            }

            int functionID = MessageNumber.getFunction(msgid);
            switch (functionID) {
                case MessageEnum.MSG_HEART:
                    GameServer.getInstance().getMainThread().addCommand(mess);
                    break;
                case MessageEnum.MSG_REGISTER: {
                    //Только сообщение входа обрабатывается асинхронно (с запросом HTTP), остальные выполняются сразу
                    if(mess.getId() == RegisterMessage.ReqLoginGame.MsgID.eMsgID_VALUE){
                        RegisterProcessor.getInstance().addCommand(mess);
                    }
                    else{
                        mess.action();
                    }
                }
                break;
                case MessageEnum.MSG_GUILD:
                    GuildProcessor.getInstance().addCommand(mess);
                    break;
                case MessageEnum.MSG_FRIEND://Друзья
                case MessageEnum.MSG_RANKLIST://Рейтинг
                    FriendAndRankListProcessor.getInstance().addCommand(mess);
                    break;
                case MessageEnum.MSG_Peak: //Пиковая арена
                case MessageEnum.MSG_JJC:  //Арена
                    Manager.peakManager.addCommand(mess);
                    break;
                case MessageEnum.MSG_Activity: //Игровые события
                    Manager.activityManager.addCommand(mess);
                    break;
                case MessageEnum.MSG_LOGIN: {
                    log.error("Получены данные входа, ID сообщения: " + msgid);
                }
                break;
                default: {
                    int state = LogicProcess(msgid, mess);
                    if (state > 0) {
                        //Попытка обработки в главном потоке
                        if (state == 2) {
                            GameServer.getInstance().getMainThread().addCommand(mess);
                        } else {
                            try {
                                OnOtherDealMsg(msgid, mess, iosession);
                                //Отключение клиента при получении более 20 недействительных сообщений
                                if (iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get() > 20){
                                    Manager.registerManager.deal().tickSession(iosession);
                                }
                            } catch (Exception e) {
                                log.error(e, e);
                            }
                        }
                    }
                }
                break;
            }

        } catch (Exception e) {
            log.error(e, e);
        }
    }

    int LogicProcess(int msgId, RMessage mess) {
        //Поиск карты игрока
        ChannelHandlerContext iosession = mess.getContext();
        if (iosession != null) {
            Player player = iosession.channel().attr(SessionAttribute.PLAYER).get();
            if (player == null) {
                if (iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get() == null) {
                    iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(1);
                }else {
                    int count =  iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).get();
                    iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(count + 1);
                }
                log.error("Игрок отсутствует, msgId =" + msgId + " ch=" + iosession.channel());
                return 1;
            }
            iosession.channel().attr(SessionAttribute.INVALIDMSGCOUNT).set(0);
            //Получение потока игрока
            long mapId = player.gainMapId();
            MapServer map = GameServer.getInstance().getMServer(mapId);
            if (map == null) {
                log.error("ID сообщения:" + msgId + " , player =" + player + " поток не найден, карта: " + mapId + " линия: " + player.gainLine() + " m:" + player.gainMapId());
                return 2;
            }

            map.addCommand(mess);
            return 0;
        } else {
            log.error("Неизвестный источник: " + msgId);
        }
        return 3;
    }

    /**
     * Проверка сообщений от клиента к серверу
     *
     * @param msgId
     * @param mess
     * @return
     */
    @Override
    public boolean CheckFilterMsgId(int msgId, RMessage mess) {
        try {
            if (msgId == ReqHeart.MsgID.eMsgID_VALUE) {
                ChannelHandlerContext context = mess.getContext();
            }
        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

    //Сообщения от незарегистрированных игроков отбрасываются
    @Override
    public boolean OnOtherDealMsg(int msgId, RMessage mess, ChannelHandlerContext session) {
        try {
            log.info(session.channel() + " Получено неавторизованное сообщение id:" + msgId + ", протокол: " + mess.getClass().getSimpleName() + ", сообщение отброшено");

        } catch (Exception e) {
            log.error(e, e);
        }
        return false;
    }

    /**
     * Фильтр сетевых протоколов
     *
     * @param mess
     * @return
     */
    @Override
    public boolean Filte_Handler(RMessage mess) {
        return true;
    }
}