package com.gm.project.gmtool.hefu;

import com.gm.GameManagerApplication;
import com.gm.project.gmtool.hefu.tool.command.CommandUtil;
import com.gm.project.gmtool.hefu.domain.Hefu;
import com.gm.project.gmtool.hefu.entiry.DBInfo;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.entiry.LangInfo;
import com.gm.project.gmtool.hefu.tool.HeFuCheck;
import com.gm.project.gmtool.hefu.tool.command.Result;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.gmtool.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 一个合服任务
 * @Auther: gouzhongliang
 * @Date: 2021/9/9 14:05
 */
public class HefuTask implements Runnable{

    private static Logger log = LoggerFactory.getLogger(HefuTask.class);

    private List<String> logs = new ArrayList<>();
    /*合服任务信息*/
    private Hefu hefu;
    /**目标服务器*/
    private HefuServer toServer;
    /**源服务器*/
    private List<HefuServer> fromServer;
    /**临时文件目录*/
    private String tempdir;
    /**平台数据更新链接*/
    private String platUpdateUrl;
    /**语言信息*/
    private LangInfo langInfo;
    /**secret_key*/
    private String secret_key;

    //改变的角色信息,角色ID,[原来的角色名,新的角色名]
    private HashMap<Integer, HashMap<Long,String[]>> roles = new HashMap<>();
    //改变的帮会信息,帮会ID,[原来的帮会名,新的帮会名,帮会的帮主ID]
    private HashMap<Integer, HashMap<Long,String[]>> guilds = new HashMap<>();

    private HefuManager manager;

    public void setManager(HefuManager manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        try {
            //开始合服
            hefu.setStatus(1);
            hefu.setStep(0);
            manager.save(hefu);

            for(int i=0; i< 100; i++){
                if(hefu.getStatus() == 2 || hefu.getStatus() == 4){//成功或者取消
                    break;
                }
                startStep();
            }
        } catch (Exception ex) {
            writeLog(ex.getMessage());
            writeLog("合服失败，请重试");
            log.error(ex.getMessage(), ex);
            hefu.setStatus(3);
        } finally {
            try{
                hefu.setRecord(JsonUtils.toJSONString(logs));
                manager.save(hefu);
            }catch (Exception e){
                log.error("", e);
            }
            //关闭数据库连接
            toServer.getDb().close();
            toServer.getDblog().close();
            for(HefuServer s : fromServer){
                s.getDb().close();
                s.getDblog().close();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //移除任务
            manager.removeTask(this.hefu.getId());
        }

    }

    private void startStep() throws Exception{
        int overStep = hefu.getStep();
        int step = overStep + 1;
        switch (step){
            case 1:
                writeLog("1.开始检测配置");
                checkConfig(this);
                break;
            case 2:
                //服务器状态检测
                writeLog("2.服务器状态检测 跳过");
//                for(int i=5;i>0;i--){
//                    writeLog("请确认源服务器，目标服务器都已经关闭" + i);
//                    Thread.sleep(1000);
//                }
                break;
            case 3:
                break;
            case 4:
                //数据处理----------
                writeLog("4.开始数据处理");
                onPrecess();
                //提交事务 --不然会阻塞后面的任务
                toServer.getDb().commit();
                for(HefuServer server : fromServer){
                    server.getDb().commit();
                }
                break;
            case 5:
                //导出源服数据
                writeLog("5.开始导出源服数据");
                File tempdirfile = new File(tempdir);
                if(!tempdirfile.exists()){
                    tempdirfile.mkdirs();
                }
                for(HefuServer server : fromServer){
                    exportDB(server);
                }
                break;
            case 6:
                //把导出的数据导入到目标数据库
                writeLog("6.源服数据导入到目标服");
                importDB(toServer);
                break;
            case 7:
                //检查
                writeLog("7.检查数据完整性");
                HeFuCheck.check(this);
                break;
            case 8:
                //修改服务器列表中，源服务器指向的ip，端口，支付地址
                writeLog("8.更新源服对应的地址和端口");
                updatePlatInfo(platUpdateUrl);
                break;
            case 9:
                //GM后台合服处理
                writeLog("9.更新GM后台");
                backend();
                writeLog("合服成功");
                hefu.setStatus(2);
                break;
            default:
                hefu.setStatus(2);
                break;
        }
        //保存进度
        hefu.setStep(step);
        hefu.setRecord(JsonUtils.toJSONString(logs));
        manager.save(hefu);
    }

    /**
     * Gm后台数据处理
     */
    private void backend() {
        ITServerService service = manager.getServerService();
        //修改数据表
        for(HefuServer server : fromServer){
            TServer s = server.getServer();
            s.setIsHeFu(1);
            s.setHefuServerID(toServer.getServerId());
            s.setHefuTime(new Date());
            service.updateTServer(s);
        }

    }

    public static void sendHttpReq(String urladdress, String method) throws Exception {
        HttpURLConnection uc = null;
        try
        {
            URL url = new URL(urladdress);

            uc = (HttpURLConnection) url.openConnection();
            uc.setDoInput(true);
            uc.setDoOutput(true);
            uc.setInstanceFollowRedirects(true); // 不允许重定向
            uc.setRequestMethod(method);
            uc.setConnectTimeout(5000); // 五秒连接超时
            uc.setReadTimeout(5000); // 5秒返回超时
            uc.connect();
            int code = 405;
            code = uc.getResponseCode();
            if(code != 200){
                throw new Exception("code " + code + ", req error:" + urladdress);
            }
        }finally {
            if (uc != null){
                uc.disconnect();//释放资源，并有可能影响到持久连接
            }
        }
    }

    /**
     * 修改平台数据 源服ip端口修改为目标服的ip和端口.//TODO（确认是否有支付地址）
     */
    private void updatePlatInfo(String platUpdateUrl) throws Exception {

        String params = "?secret_key=" + secret_key + "&targetId="+ toServer.getServerId() +"&fromIds=";
        //设置源服务器数据
        int i = 0;
        for(HefuServer serverInfo : fromServer){
            params += serverInfo.getServerId();
            i++;
            if(i < fromServer.size()){
                params += ",";
            }
        }
        String reqURL= platUpdateUrl + params;
        writeLog("请求reqURL：" + reqURL);
        sendHttpReq(reqURL, "GET");
    }

    /**
     * 执行sql命令
     * @param db
     * @param sql
     * @return
     */
    private static List<String> exeSqlCommand(DBInfo db, String sql) {
        return exeSqlCommand(db, sql, false);
    }

    private static List<String> exeSqlCommand(DBInfo db, String sql, boolean showProcess) {
        StringBuilder sb = new StringBuilder();
        sb.append("mysql -u").append(db.getUsername())
                .append(" -p").append(db.getPassword())
                .append(" -h").append(db.getIp())
                .append(" --port ").append(db.getPort())
                .append(" ").append(db.getDbname())
                .append(" -e \"").append(sql).append("\" ");
        if(!showProcess){
            sb.append("|tail -n 1");
        }
        Result result = CommandUtil.exeCommand(sb.toString());
        if(!result.isSuccess()){
            throw new RuntimeException("执行命令失败");
        }
        return result.getResult();
    }

    private void importDB(HefuServer to) {
        for(HefuServer serverInfo : fromServer){
            exeImport(to.getDblog(), getFileName(tempdir, hefu.getId() + "_" + serverInfo.getDblog().getDbname() + "_rolestate_insert.sql"));
            exeImport(to.getDblog(), getFileName(tempdir, hefu.getId() + "_" + serverInfo.getDblog().getDbname() + "_roleitems_insert.sql"));
            exeImport(to.getDb(), getFileName(tempdir, hefu.getId() + "_" + serverInfo.getDb().getDbname() + "_insert.sql"));
        }
    }

    public void exeImport(DBInfo db, String filename) {
        File file = new File(filename);
        if(!file.exists()){
            throw new RuntimeException("导入失败，文件不存在：" + filename);
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mysql -u").append(db.getUsername())
                .append(" -p").append(db.getPassword())
                .append(" -h").append(db.getIp())
                .append(" --port ").append(db.getPort())
                .append(" ").append(db.getDbname());
        sb.append(" < ").append(filename);
        exeCommand(sb.toString());
    }

    /**
     * 导出数据
     * @param serverInfo
     */
    private void exportDB(HefuServer serverInfo) {
        exportInsert(serverInfo.getDb(), null, getFileName(tempdir,  hefu.getId() + "_" + serverInfo.getDb().getDbname() + "_insert.sql"));
        exportInsert(serverInfo.getDblog(), "rolestate", getFileName(tempdir, hefu.getId() + "_" + serverInfo.getDblog().getDbname() + "_rolestate_insert.sql"));
        exportInsert(serverInfo.getDblog(), "roleitems", getFileName(tempdir, hefu.getId() + "_" + serverInfo.getDblog().getDbname() + "_roleitems_insert.sql"));
    }

    private static String getFileName(String path, String name) {
        if(path.endsWith("/")){
            if(name.startsWith("/")){
                path = path.substring(0, path.length() - 2);
            }
        }else{
            if(!name.startsWith("/")){
                path += "/";
            }
        }
        return path + name;
    }

    private static void exportInsert(DBInfo db, String table, String filename) {
        File file = new File(filename);
        if(file.exists()){
            file.delete();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("mysqldump -t -u").append(db.getUsername())
                .append(" -p").append(db.getPassword())
                .append(" -h").append(db.getIp())
                .append(" --port ").append(db.getPort())
                .append(" ").append(db.getDbname());
        if(table != null){
            sb.append(" ").append(table);
        }
        sb.append(" > ").append(filename);
        Result result = CommandUtil.exeCommand(sb.toString());
        if(!result.isSuccess()){
            throw new RuntimeException("命令执行失败command:"+sb.toString());
        }
    }

    private void onPrecess() throws Exception {
        manager.onPrecess(this);
    }

    public void writeLog(String s) {
        logs.add(s);
        log.info(s);
    }

    public static void checkConfig(HefuTask task) throws Exception{
        HefuServer toServer = task.getToServer();
        //检测数据库是否存在
        checkDB(toServer.getDb());
        checkDB(toServer.getDblog());
        List<HefuServer> fromServer = task.getFromServer();
        for(HefuServer formServer : fromServer){
            checkDB(formServer.getDb());
            checkDB(formServer.getDblog());
        }
    }

    public static void checkDB(DBInfo db) {
        String sql = "select count(1) from information_schema.SCHEMATA where SCHEMA_NAME = '"+db.getDbname()+"'";
        StringBuilder sb = new StringBuilder();
        sb.append("mysql -u").append(db.getUsername())
                .append(" -p").append(db.getPassword())
                .append(" -h").append(db.getIp())
                .append(" --port ").append(db.getPort())
                .append(" -e \"").append(sql).append("\" |tail -n 1");
        Result res = CommandUtil.exeCommand(sb.toString());
        if(res.isSuccess()){
            List<String> result = res.getResult();
            if(result != null && result.size() > 0 && result.get(result.size()-1).equals("1")){
                return;
            }
        }
        throw new RuntimeException("数据库配置错误：" + db.toString());
    }

    /**
     * 执行命令
     * @param command
     * @return
     */
    private List<String> exeCommand(String command) {
        try{
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec(new String[]{"/bin/bash","-c", command});

            List<String> result = new ArrayList<>();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))){
                String str = null;
                while((str = reader.readLine()) != null){
                    log.info("resultMsg:{}", str);
                    result.add(str);
                }
            }catch (Exception e){
                log.error("", e);
            }

            try(BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()))){
                String str = null;
                while((str = errorReader.readLine()) != null){
                    writeLog("resultErrorMsg:" + str);
                    result.add(str);
                }
            }catch (Exception e){
                log.error("", e);
            }

            int exitValue = process.waitFor();
            process.destroy();
            log.info("exitValue:{}", exitValue);

            if(exitValue != 0){
                throw new Exception("命令执行失败");
            }
            return result;
        }catch (Exception e){
            log.error("exeCommand error:" + command, e);
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 清除数据
     * @param name
     * @throws SQLException
     */
    public void clearFromData(String name) throws SQLException {
        for(HefuServer server : fromServer){
            server.getDb().execute("DELETE FROM " + name);
        }
    }

    public void clearToData(String name) throws SQLException {
        toServer.getDb().execute("DELETE FROM " + name);
    }

    public Hefu getHefu() {
        return hefu;
    }

    public void setHefu(Hefu hefu) {
        this.hefu = hefu;
    }

    public HefuServer getToServer() {
        return toServer;
    }

    public void setToServer(HefuServer toServer) {
        this.toServer = toServer;
    }

    public List<HefuServer> getFromServer() {
        return fromServer;
    }

    public void setFromServer(List<HefuServer> fromServer) {
        this.fromServer = fromServer;
    }

    public HashMap<Integer, HashMap<Long, String[]>> getRoles() {
        return roles;
    }

    public void setRoles(HashMap<Integer, HashMap<Long, String[]>> roles) {
        this.roles = roles;
    }

    public HashMap<Integer, HashMap<Long, String[]>> getGuilds() {
        return guilds;
    }

    public void setGuilds(HashMap<Integer, HashMap<Long, String[]>> guilds) {
        this.guilds = guilds;
    }

    public String getPlatUpdateUrl() {
        return platUpdateUrl;
    }

    public void setPlatUpdateUrl(String platUpdateUrl) {
        this.platUpdateUrl = platUpdateUrl;
    }

    public void setTempdir(String tempdir) {
        this.tempdir = tempdir;
    }

    public LangInfo getLangInfo() {
        return langInfo;
    }

    public void setLangInfo(LangInfo langInfo) {
        this.langInfo = langInfo;
    }

    public List<String> getLogs() {
        return logs;
    }

    public String getSecret_key() {
        return secret_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }
}
