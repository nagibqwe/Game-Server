package com.game.utils;

import com.game.manager.Manager;
import com.game.player.structs.Player;
import game.core.message.SMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MessageUtils {

    private static final Logger log = LogManager.getLogger(MessageUtils.class);

    /**
     * 发送消息
     * @param playerId
     * @param messageId
     * @param message
     */
    public void sendMsg(long playerId, int messageId, byte[] message) {
        Player player = Manager.playerManager.getPlayers().get(playerId);
        if (player == null) {
            return;
        }
        player.sendMsg(messageId, message);
    }

    /**
     * 发送消息
     * @param player
     * @param messageId
     * @param message
     */
    public static void sendMsg(Player player, int messageId, byte[] message) {
        player.sendMsg(messageId, message);
    }

    /**
     * 发送消息
     * @param playerId
     * @param message
     */
    public void sendMsg(long playerId, SMessage message) {
        Player player = Manager.playerManager.getPlayers().get(playerId);
        if (player == null) {
            return;
        }
        player.sendMsg(message);
    }

    /**
     * 发送消息
     * @param player
     * @param message
     */
    public void sendMsg(Player player, SMessage message) {
        player.sendMsg(message);
    }
}
