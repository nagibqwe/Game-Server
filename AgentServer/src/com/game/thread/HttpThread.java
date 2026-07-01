/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.game.thread;

import com.game.http.GameHttpServer;
import game.core.net.Config.ServerConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author Administrator
 */
public class HttpThread extends Thread {

    //日志

    private static final Logger log = LogManager.getLogger(HttpThread.class);
    //运行标志
    private boolean stop;
    //线程名称
    protected String threadName;

    public HttpThread(String threadName) {
        super(threadName);
        this.threadName = threadName;
    }

    @Override
    public void run() {
        GameHttpServer.getInstance().accept(ServerConfig.getLoginHtttpPort());
    }

    public void stop(boolean flag) {
        stop = flag;
        try {
            synchronized (this) {
                notify();
            }
        } catch (Exception e) {
            log.error("http Thread " + threadName + " Notify Exception:" + e.getMessage());
        }
    }
}
