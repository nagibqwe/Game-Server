/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.backgrand.timer;

import com.game.chat.structs.Notify;
import  com.data.MessageString;
import com.game.server.GameServer;
import com.game.utils.MessageUtils;
import game.core.timer.TimerEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author xuchangming <xysoko@qq.com>
 */
public class ServerCloseTimer extends TimerEvent {

    private static final Logger log = LogManager.getLogger("ServerCloseTimer");

    private final int endTime;

    public ServerCloseTimer(int type, int end) {
        super(1, 0,type * 1000);
        endTime = end;
    }

    @Override
    public void action() {
        MessageUtils.notify_allOnlinePlayer(Notify.CHAT_SYS_BULL, MessageString.WANGNENGTISHI, "服务器将在" + endTime + "秒后关闭， 请大家下线保存， 不下线后果自负！");
        log.error("服务器将在" + endTime + "秒后关闭， 请大家下线， 不下线后果自负！");
        int en = endTime - 1;
        if (en < 1) {
            System.exit(2);
        } else {
            int type = 5;
            if (endTime <= 10) {
                type = 1;
            }
            GameServer.getInstance().getMainThread().addTimerEvent(new ServerCloseTimer(type, endTime - type));
        }

    }

}
