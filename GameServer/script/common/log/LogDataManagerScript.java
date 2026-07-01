package common.log;

import com.game.log.ItemCountLogTask;
import com.game.log.db.ItemCountLog;
import com.game.log.db.ItemCountLogDao;
import com.game.log.script.ILogDataManagerScript;
import com.game.manager.Manager;
import com.game.script.structs.ScriptEnum;
import game.core.dblog.LogService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LogDataManagerScript implements ILogDataManagerScript {

    private static final Logger log = LogManager.getLogger(LogDataManagerScript.class);

    @Override
    public int getId() {
        return ScriptEnum.LogDataManagerScript;
    }

    @Override
    public Object call(Object... args) {
        return null;
    }

    @Override
    public void start() {
        log.info("LogDataManager start...");
        ItemCountLogDao itemCountLogDao = new ItemCountLogDao(LogService.getInstance().getDataSource());
        Manager.logManager.setItemCountLogDao(itemCountLogDao);

        ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 0,
                TimeUnit.MILLISECONDS, Manager.logManager.getQueue());
        Manager.logManager.setExecutorService(executorService);
        //加载已有的数据
        Manager.logManager.getItems().clear();
        List<ItemCountLog> logs = itemCountLogDao.list(new Date());
        if(logs != null && logs.size() > 0){
            log.info("LogDataManager init items:" + logs.size());
            for(ItemCountLog log : logs){
                Manager.logManager.getItems().put(log.getModelId(), log);
            }
        }
        //开始定时器
//        itemCrossDayTask = new ItemCrossDayTask();
//        itemCrossDayTask.executeScheduleAtFixedRate();

        ItemCountLogTask itemCountLogTask = new ItemCountLogTask();
        itemCountLogTask.executeScheduleAtFixedRate();
        Manager.logManager.setItemCountLogTask(itemCountLogTask);
    }

    @Override
    public void stop() {
        //关闭定时器
        //        itemCrossDayTask.cancel();
        Manager.logManager.getItemCountLogTask().cancel();
        //保存所有数据
        saveAll(null);
        LinkedBlockingQueue<Runnable> queue =  Manager.logManager.getQueue();
        while(queue.size() > 0){
            log.info("close LogDataManager queue size:" + queue.size());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Manager.logManager.getExecutorService().shutdown();
        log.info("close LogDataManager success");
    }

    private void saveAll(Collection<ItemCountLog> items){
        int i = 0;
        if(items == null){
            items =  Manager.logManager.getItems().values();
        }
        for(ItemCountLog item : items){
            if(item.update){
                item.update = false;
                Manager.logManager.getExecutorService().submit(()->{
                    Manager.logManager.getItemCountLogDao().replaceInto(item);
                });
                i++;
            }
        }
        log.info("update ItemCountLog count:" + i);
    }

    @Override
    public void onItemChange(int serverId, int modelId, int type, String name, long oldNum, long afterNum) {
        ExecutorService executorService = Manager.logManager.getExecutorService();
        if(executorService != null){
            executorService.submit(()->{
                ItemCountLog log = Manager.logManager.getItems().get(modelId);
                if(log == null){
                    log = new ItemCountLog(serverId, modelId, type, name);
                    Manager.logManager.getItems().put(modelId, log);
                }
                long change = afterNum - oldNum;
                if(change > 0){
                    log.setProduce(log.getProduce() + change);
                }else{
                    log.setConsume(log.getConsume() - change);
                }
                log.setDealTime(System.currentTimeMillis());
                log.update = true;
            });
        }else{
            log.warn("跨服不记录数据,modelId:{} type:{} name:{} oldNum:{} afterNum:{}", modelId, type, name, oldNum, afterNum);
        }
    }

    @Override
    public void crossDay() {
        Manager.logManager.getExecutorService().submit(()->{
            Map<Integer, ItemCountLog> tmp = Manager.logManager.getItems();
            Manager.logManager.setItems(new HashMap<>());
            saveAll(tmp.values());
        });
    }

    public void saveAllByInterval(Collection<ItemCountLog> items){
        long now = System.currentTimeMillis();
        int i = 0;
        long interval = Manager.logManager.getInterval();
        for(ItemCountLog item : items){
            if(item.update && now - item.getDealTime() > interval){
                Manager.logManager.getItemCountLogDao().replaceInto(item);
                item.update = false;
                i++;
            }
        }
        if(i > 0){
            log.info("update ItemCountLog count:" + i);
        }
    }
}
