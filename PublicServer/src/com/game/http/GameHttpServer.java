/**
 * @date 2014/5/14
 * @author ChenLong
 */
package com.game.http;

import game.core.net.server.SocketServer;
import org.apache.logging.log4j.LogManager; import org.apache.logging.log4j.Logger; 

/**
 * 内部Http服务器 用于和后台通信, 目前暂支持Http GET方法
 *
 * @author ChenLong
 */
public class GameHttpServer {

    private static final Logger log = LogManager.getLogger(GameHttpServer.class);

    private SocketServer httpserver = null;

    /**
     * 启动HTTP监听
     *
     * @param port
     * @return
     */
    public int accept(int port) {
        if (httpserver == null) {
            httpserver = new SocketServer("登录HTTP服务", port);
        }

        httpserver.start(new HttpChannelImpl());

        return 0;
    }

    /**
     * 停止服务
     */
    public void stop() {
        if (httpserver != null) {
            httpserver.stop();
        }
    }

    public static GameHttpServer getInstance() {
        return GameHttpServer.Singleton.INSTANCE.getServer();
    }

    private enum Singleton {

        INSTANCE;

        GameHttpServer server;

        Singleton() {
            this.server = new GameHttpServer();
        }

        GameHttpServer getServer() {
            return server;
        }
    }
}
