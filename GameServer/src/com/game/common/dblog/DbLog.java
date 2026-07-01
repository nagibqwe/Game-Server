package com.game.common.dblog;

import com.game.player.structs.Player;
import com.game.player.structs.PlayerWorldInfo;
import com.game.serialization.SerialCache;
import game.core.dblog.LogService;
import game.core.dblog.bean.CommonLogBean;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class DbLog extends CommonLogBean{
    
    public static final Logger logger = LogManager.getLogger(LogService.class);
    
    public static void save(int type, Player p, String... param){
        try{
            SerialCache<String> cache = new SerialCache<>();
            cache.addAll(Arrays.asList(param));
            DbLog bean = DbLogParser.parseType(type);
            bean.serial(cache);
            if(p!=null){
                bean.setPlayerInfo(p.getPlatformName(), p.getCreateServerId(), p.getUserId(), p.getId(), p.getName());
            }
            LogService.getInstance().execute(bean);
        }catch(Exception e){
            logger.error("插入数据失败：日志Id="+type,e);
        }
    }
    
    public static void save(int type, PlayerWorldInfo p, String... param) {
        try {
            SerialCache<String> cache = new SerialCache<>();
            cache.addAll(Arrays.asList(param));
            DbLog bean = DbLogParser.parseType(type);
            bean.serial(cache);
            if (p != null) {
                bean.setPlayerInfo(p.getPlat(), p.getCsid(), p.getUserId(), p.getRoleid(), p.getRolename());
            }
            LogService.getInstance().execute(bean);
        } catch (Exception e) {
            logger.error("插入数据失败：日志Id=" + type, e);
        }
    }
    
    public boolean serial(SerialCache cache){
        return true;
    }

    
    
    
}
