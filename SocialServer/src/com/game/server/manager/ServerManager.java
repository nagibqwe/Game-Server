package com.game.server.manager;

import com.game.count.structs.Count;
import com.game.count.structs.ICount;
import com.game.manager.Manager;
import com.game.script.struct.ScriptEnum;
import com.game.server.script.IServer;
import com.game.server.struct.ServerInfo;
import game.core.script.IScript;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc TODO
 * @Date 2021/6/9 15:55
 * @Auth ZUncle
 */
public class ServerManager implements ICount {

    /**
     * 服务器计数器
     */
    ConcurrentHashMap<String, Count> counts = new ConcurrentHashMap<>();

    /**
     * 服务器缓存
     */
    private final ConcurrentHashMap<String, ServerInfo> servers = new ConcurrentHashMap<>();



    public ConcurrentHashMap<String, ServerInfo> getServers() {
        return servers;
    }

    /**
     * 获取技术数据
     * 九 零一 起玩 www.9 017 5.c om
     * @return
     */
    @Override
    public ConcurrentHashMap<String, Count> getCounts() {
        return counts;
    }

    public void setCounts(ConcurrentHashMap<String, Count> counts) {
        this.counts = counts;
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ServerManager processor;

        Singleton() {
            this.processor = new ServerManager();
        }

        ServerManager getProcessor() {
            return processor;
        }
    }

    public static ServerManager getInstance() {
        return ServerManager.Singleton.INSTANCE.getProcessor();
    }


    public IServer server() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ServerScript);
        if (is == null) {
            throw new NullPointerException("没有找到具体的脚本实例！script=" + ScriptEnum.ServerScript);
        }
        return (IServer) is;
    }

}
