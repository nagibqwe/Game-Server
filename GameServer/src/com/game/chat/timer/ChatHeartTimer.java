
package com.game.chat.timer;

import com.game.manager.Manager;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ChatHeartTimer extends TimerEvent {

    final Logger log = LogManager.getLogger(ChatHeartTimer.class);

    public ChatHeartTimer() {
        super(-1, 0, 60 * 1000);
    }

    @Override
    public void action() {
        try {
            Manager.chatManager.deal().tickMultiMedia();
        } catch (Exception e) {
            log.error(e, e);
        }
    }
}
