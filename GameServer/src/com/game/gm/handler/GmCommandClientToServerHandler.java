package com.game.gm.handler;

import com.game.gm.manager.GmCommandManager;
import com.game.player.structs.Player;

import game.core.command.Handler;
import game.core.message.Message;
import game.core.message.RMessage;
import game.core.util.TimeUtils;
import game.message.GmMessage.GmCommandClientToServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * makehandler v1.1
 * Client -> Game
 * Обработка внутренней GM-команды, отправленной клиентом
 */
@Message(
        id = GmCommandClientToServer.MsgID.eMsgID_VALUE,
        clazz = GmCommandClientToServer.class
)
public class GmCommandClientToServerHandler
        extends Handler<GmCommandClientToServer> {

    private static final Logger logger =
            LogManager.getLogger(GmCommandClientToServerHandler.class);

    @Override
    public void action(
            RMessage session,
            GmCommandClientToServer message
    ) {
        try {
            long start = TimeUtils.Time();

            Player player = (Player) session.getExecutor();

            if (player != null) {

                String command = message.getCommand();

                logger.info(
                        "Получена GM-команда: userId="
                                + player.getUserId()
                                + ", roleId="
                                + player.getId()
                                + ", roleName="
                                + player.getName()
                                + ", command="
                                + command
                );

                GmCommandManager.getInstance()
                        .clientGmDeal(player, command);

            } else {
                logger.error(
                        "Ошибка обработки GM-команды: "
                                + "данные игрока не получены!"
                );
            }

            long dealTime = TimeUtils.Time() - start;

            if (dealTime > 300) {
                logger.warn(
                        "GmCommandClientToServerHandler: "
                                + "длительное время обработки: "
                                + dealTime
                                + " ms"
                );
            }

        } catch (Exception e) {
            logger.error(
                    "Ошибка обработки GM-команды",
                    e
            );
        }
    }
}