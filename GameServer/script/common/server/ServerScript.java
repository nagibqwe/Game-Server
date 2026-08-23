package common.server;

import com.data.MessageString;
import com.game.chat.structs.Notify;
import com.game.connectfightserver.manager.ConnectFightManager;
import com.game.connectfightserver.struct.ConnectFightServer;
import com.game.fightserver.manager.FightClientManager;
import com.game.fightserver.struct.FightClient;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.SessionAttribute;
import com.game.script.structs.ScriptEnum;
import com.game.server.GameServer;
import com.game.server.social.SocialServerClient;
import com.game.server.script.IServerScript;
import com.game.structs.GlobalType;
import com.game.structs.ServerStr;
import com.game.utils.MessageUtils;
import game.core.net.Config.ServerConfig;
import game.core.net.Config.ServerEnum;
import game.core.script.IScript;
import game.core.util.CrossState;
import game.core.util.HttpUtils;
import game.core.util.JsonUtils;
import game.core.util.TimeUtils;
import game.message.*;
import game.message.serverMessage.F2GResRegister;
import game.message.serverMessage.G2FReqRegister;
import game.message.serverMessage.P2GResFightServerList;
import game.message.serverMessage.gameServerInfo;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author admin
 */
public class ServerScript implements IScript, IServerScript {

    private static final Logger log = LogManager.getLogger(ServerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.ServerBaseScript;
    }

    @Override
    public Object call(Object... objects) {
        return null;
    }

    @Override
    public String getNotHaveMarkStr(String tableName, int id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " не найдено", "В языковом пакете отсутствует ID для “" + tableName.toLowerCase() + "”+" + id + "!");
            return tableName + "(" + id + ") неизвестно";
        } catch (Exception e) {
            return "Неизвестно " + tableName + ":" + id;
        }
    }

    /**
     * Строки языкового пакета сервера
     *
     * @param tableName
     * @param id
     * @return
     */
    @Override
    public String getStr(String tableName, int id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return "2&_" + ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " не найдено", "В языковом пакете отсутствует ID для “" + tableName.toLowerCase() + "”+" + id + "!");
            return "2&_" + tableName + "(" + id + ") неизвестно";
        } catch (Exception e) {
            return "Неизвестно " + tableName + ":" + id;
        }

    }

    /**
     * Получение названия божественного оружия
     *
     * @param career
     * @param dragoonId
     * @return
     */
    @Override
    public String getDragoonName(int career, int dragoonId) {
        try {
            String key = career + "_" + dragoonId;
            if (ServerStr.getDragoon().containsKey(key)) {
                return ServerStr.getDragoon().get(key);
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " career_dragoonId：" + career + "_" + dragoonId + " не найдено", "Проверьте языковой пакет!");
            return key;
        } catch (Exception e) {
            return "Неизвестно " + career + ":" + dragoonId;
        }
    }

    /**
     * Получение текста на нужном языке
     *
     * @param tableName
     * @param id
     * @param roleId
     * @return
     */
    @Override
    public String getLanguage(String tableName, int id, long roleId) {
        try {
            String key = tableName.toLowerCase() + "_" + id;

            int languageType = Manager.registerManager.getLanguageType(roleId);

            ConcurrentHashMap<String, String> languagePack = ServerStr.getLanguageMap().get(languageType);
            if (languagePack == null) {
                GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("Языковой пакет не найден", "Языковой пакет не найден, languageType=" + languageType);
                return null;
            }
            if (ServerStr.getLanguageNo().containsKey(key)) {
                int no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                return languagePack.get(skey);
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr tableName_id не найдено", "В языковом пакете отсутствует ID для “" + tableName.toLowerCase() + "”+" + id + "!");
            return null;
        } catch (Exception e) {
            log.error("Ошибка getLanguage：" + e);
            return null;
        }
    }

    @Override
    public String getLanguage(String tableName, String id) {
        try {
            String key = tableName.toLowerCase() + "_" + id;
            if (ServerStr.getLanguageNo().containsKey(key)) {
                Integer no = ServerStr.getLanguageNo().get(key);
                String skey = "str" + no;
                if (ServerStr.getServerLang().containsKey(skey)) {
                    return ServerStr.getServerLang().get(skey);
                }
            }
            GameServer.getInstance().getErrorLogThread().pushErrorExcptionLog("ServerStr" + " tableName _id" + tableName.toLowerCase() + "”+" + id + " не найдено", "В языковом пакете отсутствует ID для “" + tableName.toLowerCase() + "”+" + id + "!");
            return "" + tableName + "(" + id + ") неизвестно";
        } catch (Exception e) {
            return "Неизвестно " + tableName + ":" + id;
        }
    }

    //Уведомление об успешном подключении к публичному серверу
    @Override
    public void OnP2GResRegister(ChannelHandlerContext context, serverMessage.P2GResRegister messInfo) {

        log.info("Игровой сервер " + GameServer.getInstance().getServerName() + " зарегистрирован на " + messInfo.getPublicName() + " успешно!");

        log.info("Адрес публичного сервера: " + context.channel() + " содержимое: " + messInfo.getPublicName() + "(" + messInfo.getPublicId() + ")");
        //Запрос данных о боссах Леса Душ для боевого сервера
        if (GameServer.getInstance().IsFightServer()) {
            SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.Builder mess = SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.newBuilder();
            MessageUtils.send_to_public(SoulAnimalForestMessage.F2PReqSoulAnimalForestBossInfo.MsgID.eMsgID_VALUE, mess.build().toByteArray());
        }
        if (GameServer.getInstance().IsFightServer()) {
            GuildCrossFudMessage.G2PSyncRoomInfo.Builder mess = GuildCrossFudMessage.G2PSyncRoomInfo.newBuilder();
            MessageUtils.send_to_public(GuildCrossFudMessage.G2PSyncRoomInfo.MsgID.eMsgID_VALUE, mess.build().toByteArray());
        }
    }

    /**
     * Ответ регистрации боевого сервера
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnF2GResRegister(ChannelHandlerContext context, F2GResRegister mess) {
        ConnectFightManager.GetInstance().Register(mess.getFsId(), context);
        log.info("Получен ответ регистрации от боевого сервера “" + mess.getFsName() + "(" + mess.getFsId() + ")”!");
        ConnectFightServer cfs = ConnectFightManager.GetInstance().getConList().get(mess.getFsId());
        if (cfs == null) {
            log.error("Класс боевого сервера не существует, проверьте ситуацию!");
            return;
        }
        cfs.setFid(mess.getFsId());
        cfs.setLastheartTime(System.currentTimeMillis());
        int id = context.channel().attr(SessionAttribute.CONNECT_SERVER_ID).get();
        String ip = context.channel().attr(SessionAttribute.CONNECT_SERVER_IP).get();
        int sport = context.channel().attr(SessionAttribute.CONNECT_SERVER_PORT).get();
        log.info("Боевой сервер id = " + id + " ip= " + ip + " port=" + sport + " подключён успешно!");
    }

    /**
     * Запрос регистрации боевого сервера
     *
     * @param context
     * @param mess
     */
    @Override
    public void OnG2FReqRegister(ChannelHandlerContext context, G2FReqRegister mess) {
        log.error("Получено соединение от игрового сервера “" + mess + "!");
        FightClient fc = new FightClient();
        gameServerInfo sInfo = mess.getSinfo();
        fc.setIp(sInfo.getServerIP());
        fc.setPlat(sInfo.getPlatformMark());
        fc.setPort(sInfo.getServerPort());
        fc.setSid(sInfo.getServerId());
        fc.setVersion(sInfo.getVersion());
        context.channel().attr(SessionAttribute.FIGHT_CLIENT_INFO).set(fc);
        int sid = sInfo.getServerId();
        String plat = sInfo.getPlatformMark();

        FightClientManager.GetInstance().add(sid, plat, context);
        F2GResRegister.Builder msg = F2GResRegister.newBuilder();
        msg.setFsId(ServerConfig.getServerId());
        msg.setFsName(ServerConfig.getServerName());
        FightClientManager.GetInstance().send_to_game(context, F2GResRegister.MsgID.eMsgID_VALUE, msg.build().toByteArray());

    }

    /**
     * Получение списка боевых серверов
     *
     * @param context
     * @param messInfo
     */
    @Override
    public void OnP2GResFightServerList(ChannelHandlerContext context, P2GResFightServerList messInfo) {

        gameServerInfo social = messInfo.getSocial();
        if (SocialServerClient.getInstance().channel == null && social != null) {

            if (social.getServerId() > 0) {
                //Обновление IP и порта
                SocialServerClient.getInstance().setServerIP(social.getServerIP()).setPort(social.getServerPort());
            }

            //Запуск соединения с социальным сервером
            if (social.getServerId() > 0 && !SocialServerClient.getInstance().init) {
                SocialServerClient.getInstance().init(social.getServerIP(), social.getServerPort());
                new Thread(SocialServerClient.getInstance(), "Соединение с социальным сервером").start();
                log.info("Публичный сервер синхронизирует социальный сервер={}", messInfo);
            }
        }

        if (GameServer.getInstance().IsFightServer()) {
            return;
        }

        for (gameServerInfo sinfo : messInfo.getInfoListList()) {
            int fid = sinfo.getServerId();
            String ip = sinfo.getServerIP();
            int port = sinfo.getServerPort();

            ConnectFightServer cfs = ConnectFightManager.GetInstance().getConList().get(fid);
            if (cfs != null) {
                cfs.getMapIds().clear();
                cfs.getMapIds().addAll(sinfo.getMapIdsList());
                ConnectFightManager.GetInstance().RegisterMapids(fid, sinfo.getMapIdsList());//Регистрация карт
                if (cfs.isTrue(ip, port)) {
                    continue;
                }
                //Обновление IP и порта
                ConnectFightManager.GetInstance().getConList().get(fid).setIPPort(ip, port);

                if (!cfs.isIsConnect() && !cfs.isConnectBool()) {
                    new Thread(cfs).start();//Запуск соединения
                }
                continue;
            }
            log.info("Публичный сервер сообщил о новом боевом сервере ID=" + fid + " , ip=" + ip + ", port=" + port);
            cfs = new ConnectFightServer(fid, ip, port);
            cfs.getMapIds().addAll(sinfo.getMapIdsList());
            ConnectFightManager.GetInstance().getConList().put(fid, cfs);
            ConnectFightManager.GetInstance().RegisterMapids(fid, sinfo.getMapIdsList());
            cfs.setLastheartTime(System.currentTimeMillis() + 30 * 1000);//Добавляем 30 секунд к сердцебиению, чтобы избежать повторного закрытия
            new Thread(cfs).start();//Запуск соединения
        }

        log.info("Публичный сервер синхронизировал " + messInfo.getInfoListCount() + " боевых серверов с игровым сервером");
    }

    /**
     * Соединение с боевым сервером разорвано
     *
     * @param session
     * @param serverId
     */
    @Override
    public void OnFightSessionOut(ChannelHandlerContext session, int serverId) {
        int fitghSid = serverId;
        List<Player> list = new ArrayList<>(Manager.playerManager.getPlayersCache().values());
        for (Player player : list) {
            if (player.playerCrossData.toFightSid != fitghSid) {
                continue;
            }
            player.playerCrossData.setToFightServer(false);//Сброс флага кросс-сервера
            player.playerCrossData.toFightSid = 0;
            player.playerCrossData.toZoneModelId = 0;
            player.playerCrossData.toFightId = 0;
            player.playerCrossData.isReqFight = false;
            player.playerCrossData.crossState = CrossState.PCS_LOCAL;

            //Если игрок онлайн
            if (player.getIsOnline() != 0) {
                //Смена карты
                MessageUtils.notify_player(player, Notify.SHOWBOX, MessageString.FightServerBreak);
                //Возврат на исходную карту
                Manager.copyMapManager.outZone(player);
            } else {
                //Восстановление карты
                player.changeMapId(player.getOld().getMapId());
                player.changeMapModelId(player.getOld().getModelId());
                player.changeLine(player.getOld().getLine());
                player.changeCurPos(player.getOld().getPos());
            }
        }
    }

    /**
     * Получение информации о сервере
     *
     * @return
     */
    gameServerInfo.Builder server() {
        gameServerInfo.Builder server = gameServerInfo.newBuilder();
        server.setPlatformMark(ServerConfig.getServerPlatform());
        server.setServerIP(ServerConfig.getGameServerIp());
        server.setServerId(ServerConfig.getServerId());
        server.setServerPort(ServerConfig.getServerPort());
        server.setServerType(ServerConfig.GetServerType());
        server.setVersion(GameServer.version);
        server.setServerOpentime(ServerConfig.getServerOpenTime());
        server.setServerWorldlv(GlobalType.getWorldLevel());
        return server;
    }

    /**
     * Регистрация на социальном сервере
     *
     * @param context
     */
    @Override
    public void G2SRegisterServer(ChannelHandlerContext context) {

        serverMessage.G2SRegisterServer.Builder message = serverMessage.G2SRegisterServer.newBuilder();
        message.setServer(server());

        MessageUtils.send_to_social(serverMessage.G2SRegisterServer.MsgID.eMsgID_VALUE, message.build().toByteArray());
    }


    @Override
    public void OnP2GResFightServer(ChannelHandlerContext context, serverMessage.P2GResFightServer messInfo) {

    }


    //"cn","en","kor"(Корея),"th"(Таиланд),"tw"(Тайвань),"yn"(Вьетнам)
    @Override
    public String getItemString(int modelId, String lang, String defineValue) {
        if (ServerStr.getLanguageMap().containsKey(modelId)) {
            ConcurrentHashMap<String, String> langMap = ServerStr.getLanguageMap().get(modelId);
            if (langMap.containsKey(lang)) {
                return langMap.get(lang);
            }
        }
        return defineValue;
    }


    @Override
    public void loadMutilLang() {
        //"cn","en","kor"(Корея),"th"(Таиланд),"tw"(Тайвань),"yn"(Вьетнам)
        ServerStr.getLanguageMap().clear();
        HashMap<String, String> langMark = new HashMap<>();
        langMark.put("cn", "cn");
        langMark.put("ros", "en");
        langMark.put("kor", "kor");
        langMark.put("th", "th");
        langMark.put("tw", "tw");
        langMark.put("yn", "yn");
        loadMutilItemLang(langMark.get(ServerConfig.getLangType()));
    }

    private void loadMutilItemLang(String lang) {
        try {
            Properties prop = new Properties();
            String proPath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "lang" + File.separator + "ItemString_" + lang + ".properties";
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));
            prop.load(br);
            Set keyValue = prop.keySet();
            for (Iterator it = keyValue.iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                String Property = prop.getProperty(key);
                String itemKey = key.substring(5).trim();
                try {
                    int itemId = Integer.parseInt(itemKey);
                    ConcurrentHashMap<String, String> langMap = null;
                    if (ServerStr.getLanguageMap().containsKey(itemId)) {
                        langMap = ServerStr.getLanguageMap().get(itemId);
                    } else {
                        langMap = new ConcurrentHashMap<>();
                        ServerStr.getLanguageMap().put(itemId, langMap);
                    }
                    langMap.put(lang, Property);
                } catch (NumberFormatException e) {
                    log.error("Ошибка загрузки языкового пакета для " + lang + ", ключ=" + key, e);
                }
            }
            log.error("Загружено " + prop.size() + " записей языкового пакета для " + lang);
        } catch (Exception e) {
            log.error("Ошибка загрузки языкового пакета для " + lang, e);
        }
    }

    @Override
    public void loadServerIdList() {
        try {
            StringBuilder result = new StringBuilder();
            log.info("Текущий HTTP для получения списка серверов: " + GlobalType.HEART_WEB + String.format(GlobalType.HEART_PARA, "serverIdList", ServerConfig.getServerId(), 1, "serverIdList"));
            int code = HttpUtils.sendPost(GlobalType.HEART_WEB, String.format(GlobalType.HEART_PARA, "serverIdList", ServerConfig.getServerId(), 1, "serverIdList"), result);
            if (code == 200) {
                List<Integer> serverids = ServerConfig.getServerIdList();
                Map map = JsonUtils.parseObject(result.toString(), Map.class);
                String ids = (String) map.get("msg");
                log.info("Получение списка ID серверов успешно: " + ids);
                String[] idArr = ids.split(",");
                for (String idStr : idArr) {
                    int id = Integer.parseInt(idStr);
                    if (!serverids.contains(id)) {
                        serverids.add(id);
                    }
                }
            } else {
                log.error("Ошибка получения списка ID серверов, код: " + code);
            }
        } catch (Exception e) {
            log.error("Ошибка получения списка ID серверов", e);
        }
    }
}