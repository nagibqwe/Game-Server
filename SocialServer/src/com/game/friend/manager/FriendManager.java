package com.game.friend.manager;

import com.game.db.bean.FriendBean;
import com.game.db.dao.FriendDao;
import com.game.friend.script.IFriendScript;
import com.game.friend.struct.*;
import com.game.manager.Manager;
import com.game.player.structs.GlobalPlayerWorldInfo;
import com.game.script.struct.ScriptEnum;
import game.core.json.TypeReference;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import game.core.util.VersionUpdateUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 好友管理器
 */
public class FriendManager {

    private static final Logger log = LogManager.getLogger(FriendManager.class);

    private final FriendDao friendDao = new FriendDao();

    /**
     * 全服好友数据
     */
    private final ConcurrentHashMap<Long, PlayerRelation> allFriendsHashMap = new ConcurrentHashMap<>();


    public static FriendManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    private enum Singleton {

        INSTANCE;
        FriendManager manager;

        Singleton() {
            this.manager = new FriendManager();
        }

        FriendManager getProcessor() {
            return manager;
        }
    }

    public IFriendScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.FriendBaseScript);
        if (is instanceof IFriendScript) {
            return (IFriendScript) is;
        } else {
            log.error("没有实现好友脚本");
            return null;
        }
    }


}
