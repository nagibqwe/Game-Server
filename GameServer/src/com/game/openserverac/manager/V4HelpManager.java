package com.game.openserverac.manager;

import com.game.manager.Manager;
import com.game.openserverac.scripts.IV4HelpScript;
import com.game.openserverac.structs.V4HelpBean;
import com.game.openserverac.structs.V4HelpRecordLog;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * v4助力 管理
 */
public class V4HelpManager {

    private static final Logger logger = LogManager.getLogger(V4HelpManager.class);

    public ConcurrentHashMap<Long, Long> getV4HelpApplyMap() {
        return v4HelpApplyMap;
    }

    /**
     * 申请列表
     */
    final ConcurrentHashMap<Long,Long> v4HelpApplyMap = new ConcurrentHashMap<>();

    /**
     * 被投资人数据记录 方便离线操作
     */
    final ConcurrentHashMap<Long, V4HelpBean> v4HelpBeanMap = new ConcurrentHashMap<>();
    public ConcurrentHashMap<Long, V4HelpBean> getV4HelpBeanMap() {
        return v4HelpBeanMap;
    }
    /**
     * 日志
     */
    private List<V4HelpRecordLog> v4HelpRecordLog = new ArrayList<>();
    public List<V4HelpRecordLog> getV4HelpRecordLog() {
        return v4HelpRecordLog;
    }
    public IV4HelpScript deal() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.V4HelpScript);
        if (is instanceof IV4HelpScript) {
            return (IV4HelpScript) is;
        } else {
            logger.error("IOpenServerAc接口脚本错误");
            return null;
        }
    }

    private enum Singleton {
        INSTANCE;
        V4HelpManager manager;
        Singleton() {
            this.manager = new V4HelpManager();
        }
        V4HelpManager getProcessor() {
            return manager;
        }
    }

    public static V4HelpManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }
}
