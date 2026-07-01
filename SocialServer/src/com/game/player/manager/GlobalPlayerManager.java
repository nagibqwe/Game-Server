package com.game.player.manager;

import com.game.manager.Manager;
import com.game.player.script.IGlobalPlayerScript;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.script.struct.ScriptEnum;
import game.core.script.IScript;
import game.core.util.StringUtils;
import game.message.CommonMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 全局缓存
 */
public class GlobalPlayerManager {
    private final Logger log = LogManager.getLogger(GlobalPlayerManager.class);

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {
        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        GlobalPlayerManager manager;

        Singleton() {
            this.manager = new GlobalPlayerManager();
        }

        GlobalPlayerManager getProcessor() {
            return manager;
        }
    }

    public static GlobalPlayerManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //所有服务器玩家基础信息
    final ConcurrentHashMap<Long, GlobalPlayerWorldInfo> players = new ConcurrentHashMap<>();

    //全服在线玩家基础信息
    final ConcurrentHashMap<Long, GlobalPlayerWorldInfo> onlinePlayers = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Long, GlobalPlayerWorldInfo> getPlayers() {
        return players;
    }

    public ConcurrentHashMap<Long, GlobalPlayerWorldInfo> getOnlinePlayers() {
        return onlinePlayers;
    }

    public IGlobalPlayerScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.GlobalPlayerScript);
        if (is instanceof IGlobalPlayerScript) {
            return (IGlobalPlayerScript) is;
        } else {
            log.error("没有实现玩家管理脚本");
            return null;
        }
    }
    public static CommonMessage.HeadAttribute.Builder getHead(GlobalPlayerWorldInfo player) {
        CommonMessage.HeadAttribute.Builder msg = CommonMessage.HeadAttribute.newBuilder();
        //@todo 头像修改
        msg.setFashionHead(player.getFashionHeadId());
        msg.setFashionFrame(player.getFashionHeadFrameId());
        //自定义头像
        if(!StringUtils.isEmpty(player.getCustomHeadPath())){
            msg.setCustomHeadPath(player.getCustomHeadPath());
        }
        msg.setUseCustomHead(player.isUseCustomHead());
        return msg;
    }
}
