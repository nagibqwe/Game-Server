package com.game.alienGem.manager;

import com.game.alienGem.script.IAlienGemScript;
import com.game.boss.struct.Boss;
import com.game.manager.Manager;
import com.game.map.structs.MapObject;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 须弥宝库 九 零一起玩 www.9 0175.com
 * @Auther: gouzhongliang
 * @Date: 2021/11/15 15:12
 */
public enum AlienGemManager {

    instance;

    private Logger log = LogManager.getLogger(AlienGemManager.class);

    //副本信息
    private Map<Integer, Long> maps = new ConcurrentHashMap<>();

    //地图中的BOSS信息
    private Map<Long,ConcurrentHashMap<Long, Boss>> bossMap = new ConcurrentHashMap<>();

    public Map<Integer, Long> getMaps() {
        return maps;
    }

    public void setMaps(Map<Integer, Long> maps) {
        this.maps = maps;
    }

    public Map<Long, ConcurrentHashMap<Long, Boss>> getBossMap() {
        return bossMap;
    }

    public void setBossMap(Map<Long, ConcurrentHashMap<Long, Boss>> bossMap) {
        this.bossMap = bossMap;
    }

    public IAlienGemScript getScript(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.AlienGemScript);
        if (is instanceof IAlienGemScript) {
            return (IAlienGemScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }
}
