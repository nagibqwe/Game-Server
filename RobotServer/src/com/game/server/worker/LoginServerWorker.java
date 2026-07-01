package com.game.server.worker;

import com.game.client.LoginClient;
import com.game.manager.Manager;
import com.game.structs.Config;
import game.core.concurrent.AbstractWork;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LoginServerWorker extends AbstractWork {
    private static final Logger log = LogManager.getLogger(LoginServerWorker.class);


    int port;
    LoginClient loginClient;
    public LoginServerWorker( LoginClient loginClient,int port) {
        this.port = port;
        this.loginClient = loginClient;
    }


    public String getKey() {
        return "";
    }

    @Override
    public void run() {

        try {
            if (loginClient == null){
                return;
            }
            loginClient.start(Config.getServerIp(),port);
        } catch (Exception e) {
            log.info(e, e);
        }

    }
}
