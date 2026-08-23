package com.game.gm.manager;

import com.game.db.bean.GameMaster;
import com.game.db.dao.GameMasterDao;
import com.game.gm.log.GmCommandLog;
import com.game.gm.script.IGmScript;
import com.game.manager.Manager;
import com.game.player.structs.Player;
import com.game.player.structs.PlayerAttributeType;
import com.game.script.structs.ScriptEnum;
import com.game.utils.MessageUtils;

import game.core.dblog.LogService;
import game.core.net.Config.ServerConfig;
import game.core.script.IScript;
import game.message.CrossServerMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;


/**
 * GM Command Manager
 *
 * @author Administrator
 */
public class GmCommandManager {

    private static final Logger log =
            LogManager.getLogger(GmCommandManager.class);

    private final GameMasterDao gmDao;

    /*
     * Команды, доступные GM уровня 1.
     *
     * GM level 1:
     * только команды из этого списка.
     *
     * GM level 2 и выше:
     * все GM-команды.
     */
    private static final HashMap<String, Integer> commandLevelMap =
            new HashMap<>();


    static {

        commandLevelMap.put("&jinyan", 1);
        commandLevelMap.put("&kick", 1);
        commandLevelMap.put("&ts", 1);
        commandLevelMap.put("&totask", 1);
        commandLevelMap.put("&changemap", 1);
        commandLevelMap.put("&fangchenmi", 1);
        commandLevelMap.put("&goto", 1);
        commandLevelMap.put("&wudi", 1);
        commandLevelMap.put("&selectline", 1);
        commandLevelMap.put("&whomapid", 1);
        commandLevelMap.put("&dazuo", 1);
        commandLevelMap.put("&mapcount", 1);
        commandLevelMap.put("&script", 1);
        commandLevelMap.put("&worldscript", 1);
        commandLevelMap.put("&loadscript", 1);
        commandLevelMap.put("&reload", 1);
        commandLevelMap.put("&worldreload", 1);
        commandLevelMap.put("&date", 1);
        commandLevelMap.put("&maxlogin", 1);
        commandLevelMap.put("&sd", 1);
        commandLevelMap.put("&worldloadscript", 1);
        commandLevelMap.put("&inspectplayergold", 1);
    }


    private GmCommandManager() {

        gmDao = new GameMasterDao();
    }


    /**
     * Проверяет наличие GM-прав у игрока.
     *
     * Старого player.setGmLevel() в текущей версии Player нет,
     * поэтому выставляем только boolean GM.
     */
    public void setPlayerGmLevel(Player player) {

        if (player == null) {
            return;
        }

        try {

            GameMaster gm =
                    gmDao.selectByUserId(
                            player.getUserId()
                    );

            boolean isGM =
                    gm != null
                            && gm.getGmLevel() > 0;

            player.setGM(isGM);

            if (isGM) {

                log.info(
                        "GM определён: userId="
                                + player.getUserId()
                                + ", roleId="
                                + player.getId()
                                + ", roleName="
                                + player.getName()
                                + ", gmLevel="
                                + gm.getGmLevel()
                );
            }

        } catch (Exception e) {

            player.setGM(false);

            log.error(
                    "Ошибка определения GM статуса. userId="
                            + player.getUserId(),
                    e
            );
        }
    }


    /**
     * Получение GM Script.
     */
    public IGmScript getGM() {

        IScript script =
                Manager.scriptManager.GetScriptClass(
                        ScriptEnum.GmComandBaseScript
                );

        if (script instanceof IGmScript) {

            return (IGmScript) script;
        }

        log.error(
                "Не найден экземпляр GM Script: "
                        + ScriptEnum.GmComandBaseScript
        );

        return null;
    }


    /**
     * Обработка GM-команд, полученных от клиента.
     *
     * @param player  игрок
     * @param command GM-команда
     */
    public void clientGmDeal(
            Player player,
            String command
    ) {

        // =====================================================
        // 1. Проверка входных данных
        // =====================================================

        if (player == null) {

            log.error(
                    "GM команда отклонена: player == null"
            );

            return;
        }


        if (command == null
                || command.trim().isEmpty()) {

            log.error(
                    "GM команда пуста!"
            );

            return;
        }


        command = command.trim();


        /*
         * Не переводим всю команду в lower case,
         * чтобы не испортить аргументы команды.
         *
         * Например:
         *
         * &command PlayerName
         *
         * Имя PlayerName должно остаться без изменений.
         */

        String[] strCommand =
                command.split("\\s+");


        if (strCommand.length == 0) {
            return;
        }


        String commandName =
                strCommand[0]
                        .toLowerCase();


        // =====================================================
        // 2. Проверка GM через БД
        // =====================================================

        GameMaster gm;


        try {

            gm =
                    gmDao.selectByUserId(
                            player.getUserId()
                    );

        } catch (Exception e) {

            /*
             * Если БД недоступна —
             * GM команду запрещаем.
             */

            player.setGM(false);

            log.error(
                    "Ошибка проверки GM прав. userId="
                            + player.getUserId(),
                    e
            );

            return;
        }


        // =====================================================
        // 3. Игрок не является GM
        // =====================================================

        if (gm == null
                || gm.getGmLevel() <= 0) {

            player.setGM(false);

            log.warn(
                    "Попытка GM команды без прав."
                            + " userId="
                            + player.getUserId()
                            + ", roleId="
                            + player.getId()
                            + ", roleName="
                            + player.getName()
                            + ", command="
                            + command
            );

            return;
        }


        int gmLevel =
                gm.getGmLevel();


        // Игрок успешно определён как GM
        player.setGM(true);


        // =====================================================
        // 4. Проверка уровня GM
        // =====================================================

        /*
         * GM Level 1:
         * только команды commandLevelMap.
         *
         * GM Level >= 2:
         * полный доступ.
         */

        if (gmLevel == 1
                && !commandLevelMap.containsKey(
                        commandName
                )) {

            log.warn(
                    "Недостаточно GM прав."
                            + " userId="
                            + player.getUserId()
                            + ", roleName="
                            + player.getName()
                            + ", gmLevel="
                            + gmLevel
                            + ", command="
                            + command
            );

            return;
        }


        // =====================================================
        // 5. Лог разрешённой GM команды
        // =====================================================

        log.info(
                "GM COMMAND:"
                        + " userId="
                        + player.getUserId()
                        + ", roleId="
                        + player.getId()
                        + ", roleName="
                        + player.getName()
                        + ", gmLevel="
                        + gmLevel
                        + ", command="
                        + command
        );


        // =====================================================
        // 6. Запись GM команды в DB Log
        // =====================================================

        if (player.getUserId()
                > 10000000000L) {

            try {

                GmCommandLog gmCommandLog =
                        new GmCommandLog();


                gmCommandLog.setUserId(
                        player.getUserId()
                );


                gmCommandLog.setRoleName(
                        player.getName()
                );


                gmCommandLog.setRoleId(
                        player.getId()
                );


                gmCommandLog.setSid(
                        ServerConfig.getServerId()
                );


                /*
                 * В старом коде было:
                 *
                 * gmCommandLog.setGmLevel(player.getGmLevel());
                 *
                 * Но Player.getGmLevel() больше отсутствует.
                 *
                 * Поэтому пока GM level в DB-log
                 * специально не записываем.
                 */


                gmCommandLog.setCommand(
                        command
                );


                LogService.getInstance()
                        .execute(
                                gmCommandLog
                        );

            } catch (Exception e) {

                log.error(
                        "Ошибка записи GM команды в лог",
                        e
                );
            }
        }


        // =====================================================
        // 7. Локальные специальные команды
        // =====================================================

        switch (commandName) {


            // ---------------------------------------------
            // Перезагрузка таблиц
            // ---------------------------------------------

            case "&reloaddata":

                /*
                 * Старая реализация отсутствует.
                 * Не передаём дальше.
                 */

                break;


            // ---------------------------------------------
            // Старый maxcondition
            // ---------------------------------------------

            case "&maxcondition":

                /*
                 * Старый код был отключён.
                 *
                 * ВАЖНО:
                 * здесь обязательно break.
                 */

                break;


            // ---------------------------------------------
            // Сброс автобоя
            // ---------------------------------------------

            case "&clearhook":

                player.getHookInfo()
                        .setOnHook(false);


                player.getHookInfo()
                        .setHookTime(0);


                player.getHookInfo()
                        .getItemExpAddRateTime()
                        .clear();


                Manager.playerAttAttributeManager
                        .deal()
                        .calcAttribute(
                                player,
                                PlayerAttributeType.MEDICINESATTRIBUTE
                        );


                Manager.playerHookManager
                        .deal()
                        .onReqHookSetInfoHandler(
                                player
                        );


                /*
                 * В старом коде break отсутствовал,
                 * поэтому &clearhook дополнительно
                 * попадал в RunGmCmd().
                 *
                 * Исправлено.
                 */

                break;


            // ---------------------------------------------
            // Все остальные GM команды
            // ---------------------------------------------

            default:

                IGmScript gmScript =
                        getGM();


                if (gmScript == null) {

                    log.error(
                            "GM Script не загружен."
                                    + " Команда не выполнена: "
                                    + command
                    );

                    return;
                }


                try {

                    gmScript.RunGmCmd(
                            player,
                            command
                    );

                } catch (Exception e) {

                    log.error(
                            "Ошибка выполнения GM команды:"
                                    + " userId="
                                    + player.getUserId()
                                    + ", command="
                                    + command,
                            e
                    );
                }

                break;
        }
    }


    // =========================================================
    // Singleton
    // =========================================================

    private enum Singleton {

        INSTANCE;

        private final GmCommandManager manager;


        Singleton() {

            this.manager =
                    new GmCommandManager();
        }


        GmCommandManager getProcessor() {

            return manager;
        }
    }


    public static GmCommandManager getInstance() {

        return Singleton.INSTANCE
                .getProcessor();
    }


    // =========================================================
    // Передача GM команды на Public / Social
    // =========================================================

    public void sendGMToPublic(
            long roleId,
            String str
    ) {

        CrossServerMessage.G2PGMCMD.Builder scriptMsg =
                CrossServerMessage.G2PGMCMD
                        .newBuilder();


        scriptMsg.setRoleId(
                roleId
        );


        scriptMsg.setCmd(
                str
        );


        MessageUtils.send_to_public(
                CrossServerMessage.G2PGMCMD
                        .MsgID
                        .eMsgID_VALUE,
                scriptMsg.build()
                        .toByteArray()
        );


        // Отправка команды также на SocialServer

        MessageUtils.send_to_social(
                CrossServerMessage.G2PGMCMD
                        .MsgID
                        .eMsgID_VALUE,
                scriptMsg.build()
                        .toByteArray()
        );
    }
}