package com.game.zone.manager;

import com.data.CfgManager;
import com.data.bean.Cfg_Clone_map_Bean;
import com.game.manager.Manager;
import com.game.script.ScriptEnum;
import com.game.utils.ServerParamUtil;
import com.game.zone.script.IZoneHandlerScript;
import com.game.zone.script.IZoneScript;
import com.game.zone.structs.*;
import game.core.script.IScript;
import game.core.util.JsonUtils;
import io.netty.channel.ChannelHandlerContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 跨服副本管理器
 *
 * @author soko <xuchangming@haowan123.com>
 */
public class ZoneManager {

    private static final Logger log = LogManager.getLogger(ZoneManager.class);

    //跨服匹配的时候压入数据<modeId,<人数，队伍>>
    public static final ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentLinkedQueue<ZoneTeam>>> ztCache = new ConcurrentHashMap<>();

    //玩家快速定位
    public static final ConcurrentHashMap<Long, RoleKey> Key = new ConcurrentHashMap<>();

    //玩家创建房间后倒计时结束移除<房间Id，结束时间>
    public static final ConcurrentHashMap<Long, Long> countdown = new ConcurrentHashMap<>();

    private BravePeakInfo bravePeakInfo = new BravePeakInfo();

    public String GetCopymapName(int model) {
        Cfg_Clone_map_Bean bean = CfgManager.getCfg_Clone_map_Container().getValueByKey(model);

        if (bean != null) {
            return bean.getDuplicate_name();
        }
        return "未知";
    }

    public void noticeTime(ChannelHandlerContext context) {
//        Manager.scriptManager.call(ScriptEnum.ZoneManagerScript, "OnNoticeTime", context);
        try {
            manager().OnNoticeTime(context);
        } catch (Exception e) {
            log.error(e, e);
        }
    }

    public IZoneScript manager() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ZoneManagerScript);
        if (is instanceof IZoneScript) {
            return (IZoneScript) is;
        }
        throw new Exception("没有找到具体的实例！IZoneScript");
    }

    public static IZoneHandlerScript deal() throws Exception {
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.ZoneDealHandlerScript);
        if (is instanceof IZoneHandlerScript) {
            return (IZoneHandlerScript) is;
        }
        throw new Exception("没有找到具体的实例！IZoneHandlerScript");
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ZoneManager manager;

        Singleton() {
            this.manager = new ZoneManager();
        }

        ZoneManager getProcessor() {
            return manager;
        }
    }

    //CopyMapManager
    public static ZoneManager getInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    //全服副本的状态机缓存
    private ConcurrentHashMap<Integer, Integer> cloneState = new ConcurrentHashMap<>();

    public ConcurrentHashMap<Integer, Integer> getCloneState() {
        return cloneState;
    }

    public void setCloneState(ConcurrentHashMap<Integer, Integer> cloneState) {
        this.cloneState = cloneState;
    }

    public int GetCloneState(int modelId) {
        if (cloneState.containsKey(modelId)) {
            return cloneState.get(modelId);
        }
        return CloneStateDefine.CLOSE;
    }

    public void setCloneState(int model, int state) {
        int oldstate = GetCloneState(model);
        if (oldstate != state) {
            oldstate = state;
            getCloneState().put(model, oldstate);
            log.info("副本id : (" + model + ")最新状态值为：" + oldstate);
        }
    }


    ///跨服匹配的时候压入数据<modeId,<人数，队伍>>
    public static void put(int modeId, ZoneTeam zt) {
        int size = zt.getPlist().size();
        RoleKey key = new RoleKey(modeId, size);
        if (ztCache.containsKey(modeId)) {
            if (ztCache.get(modeId).containsKey(size)) {
                ztCache.get(modeId).get(size).add(zt);
            } else {
                ConcurrentHashMap<Integer, ConcurrentLinkedQueue<ZoneTeam>> map = ztCache.get(modeId);
                ConcurrentLinkedQueue<ZoneTeam> queue = new ConcurrentLinkedQueue<>();
                queue.add(zt);
                map.put(size, queue);
            }
        } else {
            ConcurrentHashMap<Integer, ConcurrentLinkedQueue<ZoneTeam>> map = new ConcurrentHashMap<>();
            ConcurrentLinkedQueue<ZoneTeam> queue = new ConcurrentLinkedQueue<>();
            queue.add(zt);
            map.put(size, queue);
            ztCache.put(modeId, map);
        }
        //创建roleKey
        for (Long id : zt.getPlist().keySet()) {
            Key.put(id, key);
        }
    }

    public ZoneTeam getAndDelete(long roleId) {
        RoleKey key = null;
        if (Key.containsKey(roleId)) {
            key = Key.get(roleId);
        }
        if (key == null) {
            log.error("玩家不存在队列Key，roleId = " + roleId);
            return null;
        }

        if (!ztCache.containsKey(key.getModeId())) {
            log.error("匹配队列不存在ModeId，ModeId = " + roleId);
            return null;
        }
        if (!ztCache.get(key.getModeId()).containsKey(key.getSize())) {
            log.error("匹配队列不存在Size，size = " + key.getSize());
            return null;
        }
        ConcurrentLinkedQueue<ZoneTeam> zts = ztCache.get(key.getModeId()).get(key.getSize());
        if (zts.isEmpty()) {
            log.error("该匹配队列为空= " + key.getSize());
            return null;
        }
        ZoneTeam z = null;
        for (ZoneTeam zt : zts) {
            if (zt.getPlist().containsKey(roleId)) {//假如这个队有这个玩家s
                z = zt;
                if (!zts.remove(zt)) {
                    log.error("删除匹配不成功，size = " + key.getSize());
                }
                return z;
            }
        }
        return z;
    }

    //删除标记的Key
    public void deleteRoleKey(long roleId) {
        Key.remove(roleId);
    }

    public void deleteRoleKey(ZoneTeam zt) {
        for (TeamPlayerInfo info : zt.getPlist().values()) {
            deleteRoleKey(info.getRoleId());
        }
    }

    public BravePeakInfo getBravePeakInfo() {
        return bravePeakInfo;
    }

    /**
     * 把勇者巅峰数据加入缓存中，第一次会先从ServerParam中获取以往的
     *
     */
    public void addBravePeakData() {
        String newValue = JsonUtils.toJSONString(bravePeakInfo);
        ServerParamUtil.immediateSave(ServerParamUtil.BRAVE_PEAK_MYSQL_SAVE_KEY, newValue);
    }
    
}
