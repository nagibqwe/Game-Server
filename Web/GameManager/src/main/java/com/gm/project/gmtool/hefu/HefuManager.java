package com.gm.project.gmtool.hefu;

import com.alibaba.fastjson.JSON;
import com.gm.GameManagerApplication;
import com.gm.project.gmtool.hefu.tool.command.CommandUtil;
import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.dbbak.service.IDbbakService;
import com.gm.project.gmtool.hefu.domain.Hefu;
import com.gm.project.gmtool.hefu.entiry.DBInfo;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.service.IHefuService;
import com.gm.project.gmtool.hefu.tool.command.Result;
import com.gm.project.gmtool.hefu.tool.tables.interfaces.ITableHandler;
import com.gm.project.gmtool.server.service.ITServerService;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Auther: gouzhongliang
 * @Date: 2021/9/7 11:24
 */
@Component
public class HefuManager {

    private Logger log = LoggerFactory.getLogger(HefuManager.class);

    private ExecutorService executorService = Executors.newFixedThreadPool(5);

    @Autowired
    private IHefuService hefuService;

    @Autowired
    private IDbbakService dbbakService;

    @Autowired
    private ITServerService serverService;

    public ITServerService getServerService() {
        return serverService;
    }

    public void setServerService(ITServerService serverService) {
        this.serverService = serverService;
    }

    /**合服任务*/
    private Map<Long, HefuTask> tasks = new ConcurrentHashMap<>();
    /**备份任务*/
    private Map<String, Object> baks = new ConcurrentHashMap<>();
    /**还原任务*/
    private Map<String, Object> restores = new ConcurrentHashMap<>();

    /**
     * 添加任务
     * @param task
     */
    public synchronized void addTask(HefuTask task) {
        Long key = task.getHefu().getId();
        HefuTask old = tasks.get(key);
        if(old != null){
            throw new RuntimeException("任务已存在");
        }
        tasks.put(task.getHefu().getId(), task);
        task.setManager(this);
        executorService.execute(task);
    }

    public HefuTask getTask(Long id){
        return tasks.get(id);
    }

    public void removeTask(Long id){
        tasks.remove(id);
    }

    public Map<String, Object> getTaskLog(Long id, Integer index){
        HefuTask task = tasks.get(id);
        if(task != null){
            List<String> logs = new ArrayList<>();
            int size = task.getLogs().size();
            for(; index < size; index++){
                logs.add(task.getLogs().get(index));
            }
            Map<String, Object> map = new HashMap<>();
            map.put("index", size);
            map.put("logs", logs);
            return map;
        }
        return null;
    }

    /**
     * 数据处理
     * @param task
     * @throws Exception
     */
    public void onPrecess(HefuTask task) throws Exception{
        task.getRoles().clear();
        task.getGuilds().clear();
        List<ITableHandler> handlers = new ArrayList<>();
        List<List<String>> rs = task.getToServer().getDb().query("show tables", 1);
        List<String> allTables = new ArrayList<>();
        for(List<String> r : rs){
            String table = r.get(0);
            Class c;
            try {
                c = Class.forName("com.gm.project.gmtool.hefu.tool.tables.game." + table);
            }catch (ClassNotFoundException ex){
                task.writeLog("表:" + table + "不存在处理器!");
                continue;
            }
            allTables.add(table);
            ITableHandler h = (ITableHandler) c.newInstance();
            if (h == null) {
                task.writeLog("表:" + table + "处理器创建实例失败!");
                continue;
            }
            h.setTask(task);
            handlers.add(h);
        }
        task.writeLog("需要处理的游戏表的数量为:"+handlers.size());
        //根据优先级排序
        handlers.sort(Comparator.comparing(ITableHandler::getPriority));
        //对这些表进行处理
        for (ITableHandler h : handlers) {
            h.doBefore(null);
            h.doProcess(null);
        }

        //记录表的信息用于合并后的检查
        RecordTableInfo(task, allTables);
    }

    private void RecordTableInfo(HefuTask task, List<String> tables) throws Exception{
        //记录各个数据表的数据量，用于合服完成后验证合服结果
        Map<String, Integer> toTableMap = new HashMap<>(); //目标合服上数据表的数据量，也要在数据处理完再查
        List<Map<String, Integer>> fromTableMap = new ArrayList<>(); //被合服上数据表的数据量，在其数据处理后再查询记录

        for (String table : tables) {
            List<List<String>> rs = task.getToServer().getDb().query("select count(*) from "+table, 1);
            for(List<String> r : rs) {
                toTableMap.put(table, Integer.valueOf(r.get(0)));
            }
        }

        for(HefuServer server : task.getFromServer()){
            Map<String, Integer> tmp = new HashMap<>();
            for (String table : tables) {
                List<List<String>> rs = server.getDb().query("select count(*) from "+table, 1);
                for(List<String> r : rs) {
                    tmp.put(table, Integer.valueOf(r.get(0)));
                }
            }
            fromTableMap.add(tmp);
        }

        //将toTableMap和fromTableMap数据记录到文件中，数据库合并完成后
        String toTableMapStr = JSON.toJSONString(toTableMap);
        StringBuilder sb = new StringBuilder();
        sb.append(toTableMapStr);
        for(Map<String,Integer> v : fromTableMap){
            sb.append("#").append(JSON.toJSONString(v));
        }
        task.getHefu().setRecord(sb.toString());
    }

    public void save(Hefu hefu) {
        Hefu obj = hefuService.selectHefuById(hefu.getId());
        obj.setStep(hefu.getStep());
        obj.setStatus(hefu.getStatus());
        obj.setRecord(hefu.getRecord());
        hefuService.updateHefu(obj);
    }


    /**
     * 数据备份
     * @param server
     */
    public void dbbak(HefuServer server, int type, String filedir) {
        DBInfo db = null;
        if(type == 1){
            db = server.getDb();
        }else if(type == 2){
            db = server.getDblog();
        }else{
            throw new RuntimeException("类型错误");
        }
        HefuTask.checkDB(db);
        DBInfo finalDb = db;
        executorService.submit(()->{
            String key = server.getServerId() + "_" + type;
            try{
                baks.put(key, new Object());

                String filename = getDbbakName(filedir, finalDb.getDbname());
                File file = new File(filename);
                if(file.exists()){
                    file.delete();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("mysqldump -u").append(finalDb.getUsername())
                        .append(" -p").append(finalDb.getPassword())
                        .append(" -h").append(finalDb.getIp())
                        .append(" --port ").append(finalDb.getPort())
                        .append(" ").append(finalDb.getDbname())
                        .append(" > ").append(filename);
                Result result = CommandUtil.exeCommand(sb.toString());
                if(result.isSuccess()){
                    Dbbak dbbak = new Dbbak();
                    dbbak.setServerId(server.getServerId());
                    dbbak.setCreateTime(new Date());
                    dbbak.setType(type);
                    dbbak.setSize(file.length());
                    dbbak.setUrl(file.getAbsolutePath());
                    dbbakService.insertDbbak(dbbak);
                }else{

                }
            }catch (Exception e){
                log.error("备份数据库失败", e);
            }finally {
                baks.remove(key);
            }
        });
    }

    private String getDbbakName(String filedir, String dbname) {
        if(filedir.endsWith("/")){
            return filedir + dbname + "_" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmss") + ".sql";
        }else{
            return filedir + "/" + dbname + "_" + DateFormatUtils.format(new Date(),"yyyyMMddHHmmss") + ".sql";
        }
    }

    public Object getBaks(Integer serverId, Integer type){
        String key = serverId + "_" + type;
        return baks.get(key);
    }

    /**
     * 数据库还原
     * @param db
     * @param url
     */
    public void dbrestore(DBInfo db, String url) {
//        executorService.execute(()->{
            new HefuTask().exeImport(db, url);
//        });
    }
}
