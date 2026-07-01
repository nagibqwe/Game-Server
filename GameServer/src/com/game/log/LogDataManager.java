package com.game.log;

import com.game.log.db.ItemCountLog;
import com.game.log.db.ItemCountLogDao;
import com.game.log.script.ILogDataManagerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.script.IScript;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.*;

public enum LogDataManager {
    instance;
    private static final Logger log = LogManager.getLogger(LogDataManager.class);
    private long interval = 10000;
    private volatile Map<Integer, ItemCountLog> items = new HashMap<>();
    private ItemCountLogDao itemCountLogDao;
    private ExecutorService executorService;
    private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(100000);
    /**跨天定时任务*/
//    private ItemCrossDayTask itemCrossDayTask;
    private ItemCountLogTask itemCountLogTask;

    public ILogDataManagerScript getScript(){
        IScript is = Manager.scriptManager.GetScriptClass(ScriptEnum.LogDataManagerScript);
        if (is instanceof ILogDataManagerScript) {
            return (ILogDataManagerScript) is;
        } else {
            log.error("没有找到具体的接口实现类！不会走到这里，请注意实现！");
            return null;
        }
    }

    public void start(){
        getScript().start();
    }

    public void stop(){
        try{
            getScript().stop();
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
    }

    public void saveAllByInterval(){
        executorService.submit(()->{
            getScript().saveAllByInterval(items.values());
        });
    }

    public void onItemChange(int serverId,int modelId, int type, String name, long oldNum, long afterNum){
        getScript().onItemChange(serverId, modelId, type, name, oldNum, afterNum);
    }

    public void crossDay(){
        getScript().crossDay();
    }

    public ItemCountLogDao getItemCountLogDao() {
        return itemCountLogDao;
    }

    public void setItemCountLogDao(ItemCountLogDao itemCountLogDao) {
        this.itemCountLogDao = itemCountLogDao;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }

    public Map<Integer, ItemCountLog> getItems() {
        return items;
    }

    public void setItems(Map<Integer, ItemCountLog> items) {
        this.items = items;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public LinkedBlockingQueue<Runnable> getQueue() {
        return queue;
    }

    public void setQueue(LinkedBlockingQueue<Runnable> queue) {
        this.queue = queue;
    }

    public ItemCountLogTask getItemCountLogTask() {
        return itemCountLogTask;
    }

    public void setItemCountLogTask(ItemCountLogTask itemCountLogTask) {
        this.itemCountLogTask = itemCountLogTask;
    }
}
