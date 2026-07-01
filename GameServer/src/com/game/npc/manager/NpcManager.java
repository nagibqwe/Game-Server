package com.game.npc.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Npc_Bean;
import com.game.manager.Manager;
import com.game.map.structs.*;
import com.game.map.structs.MapObject;
import com.game.monster.script.ITaskEntityIsShow;
import com.game.npc.structs.Npc;
import com.game.script.structs.ScriptEnum;
import com.game.structs.ServerStr;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Administrator
 */
public class NpcManager {
    private static final Logger logger = LogManager.getLogger(NpcManager.class);

    /**
     * 发送给客户端的NPC名字
     * @param npcId
     * @return
     */
    public String getChatNpcName(int npcId) {
        Cfg_Npc_Bean bean = CfgManager.getCfg_Npc_Container().getValueByKey(npcId);
        if( bean == null){
            return "npc"+ npcId;
        }
        return ServerStr.getChatTableName(bean.getName());
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;                   //一个枚举的元素，它就代表了Singleton的一个实例
        NpcManager manager;

        Singleton() {
            this.manager = new NpcManager();
        }

        NpcManager getProcessor() {
            return manager;
        }
    }

    //获取NpcManager的实例对象
    public static NpcManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }


    public ITaskEntityIsShow manager() {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.NpcManagerBaseScript);
        if (is instanceof ITaskEntityIsShow) {
            return (ITaskEntityIsShow) is;
        } else {
            return null;
        }
    }

}
