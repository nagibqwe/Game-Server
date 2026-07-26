package com.gm.project.gmtool.hefu.service;

import java.io.File;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.*;

import com.gm.framework.config.GameManagerConfig;
import com.gm.project.gmtool.db.domain.TDb;
import com.gm.project.gmtool.db.service.ITDbService;
import com.gm.project.gmtool.dbbak.domain.Dbbak;
import com.gm.project.gmtool.dbbak.service.IDbbakService;
import com.gm.project.gmtool.hefu.HefuManager;
import com.gm.project.gmtool.hefu.HefuTask;
import com.gm.project.gmtool.hefu.entiry.DBInfo;
import com.gm.project.gmtool.hefu.entiry.HefuServer;
import com.gm.project.gmtool.hefu.entiry.LangInfo;
import com.gm.project.gmtool.server.domain.TServer;
import com.gm.project.gmtool.server.service.ITServerService;
import com.gm.project.system.config.service.IConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.gm.project.gmtool.hefu.mapper.HefuMapper;
import com.gm.project.gmtool.hefu.domain.Hefu;
import com.gm.common.utils.text.Convert;

/**
 * Объединение серверовService业务层处理
 * 
 * @author gm
 * @date 2021-09-08
 */
@Service
public class HefuServiceImpl implements IHefuService {
    private Logger log = LoggerFactory.getLogger(HefuServiceImpl.class);
    @Autowired
    private HefuMapper hefuMapper;

    @Autowired
    private ITServerService serverService;

    @Autowired
    private ITDbService dbService;

//    @Autowired
//    private ITDblogService dblogService;

    @Autowired
    private IConfigService configService;

    @Autowired
    private IDbbakService dbbakService;

    @Autowired
    private HefuManager hefuManager;

    @Autowired
    private GameManagerConfig gameManagerConfig;

    /**
     * 查询Объединение серверов
     * 
     * @param id Объединение серверовID
     * @return Объединение серверов
     */
    @Override
    public Hefu selectHefuById(Long id)
    {
        return hefuMapper.selectHefuById(id);
    }

    /**
     * 查询Список объединения
     * 
     * @param hefu Объединение серверов
     * @return Объединение серверов
     */
    @Override
    public List<Hefu> selectHefuList(Hefu hefu)
    {
        return hefuMapper.selectHefuList(hefu);
    }

    /**
     * ДобавитьОбъединение серверов
     * 
     * @param hefu Объединение серверов
     * @return Результат
     */
    @Override
    public int insertHefu(Hefu hefu)
    {
        hefu.setCreateTime(new Date());
        hefu.setStatus(0);
        hefu.setStep(0);
        if(hefu.getFromServers().contains(hefu.getToServer())){

        }
        return hefuMapper.insertHefu(hefu);
    }

    /**
     * ИзменитьОбъединение серверов
     * 
     * @param hefu Объединение серверов
     * @return Результат
     */
    @Override
    public int updateHefu(Hefu hefu)
    {
        return hefuMapper.updateHefu(hefu);
    }

    /**
     * УдалитьОбъединение серверов对象
     * 
     * @param ids 需要Удалить的ДанныеID
     * @return Результат
     */
    @Override
    public int deleteHefuByIds(String ids)
    {
        return hefuMapper.deleteHefuByIds(Convert.toStrArray(ids));
    }

    /**
     * УдалитьОбъединение серверовИнформация
     * 
     * @param id Объединение серверовID
     * @return Результат
     */
    @Override
    public int deleteHefuById(Long id)
    {
        return hefuMapper.deleteHefuById(id);
    }

    @Override
    public boolean start(Long id) {
        Hefu hefu = hefuMapper.selectHefuById(id);

        HefuTask task = getHefuTask(hefu);

        hefuManager.addTask(task);
        return true;
    }

    @Override
    public boolean stop(Long id) {
        HefuTask task = hefuManager.getTask(id);
        if(task != null){
            task.getHefu().setStatus(4);
        }
        return false;
    }

    private HefuTask getHefuTask(Hefu hefu) {
        HefuTask task = new HefuTask();
        task.setHefu(hefu);

        String platUpdateUrl = gameManagerConfig.getPlatUpdateUrl();
        if(platUpdateUrl == null || platUpdateUrl.isEmpty()){
            throw new RuntimeException("系统配置platUpdateUrl不存在");
        }
        task.setPlatUpdateUrl(platUpdateUrl);

        String filedir = gameManagerConfig.getHefudir();
        if(filedir == null || filedir.isEmpty()){
            throw new RuntimeException("系统配置hefudir不存在");
        }
        task.setTempdir(filedir);

        task.setSecret_key(gameManagerConfig.getRequestServerKey());

        task.setToServer(getHefuServerInfo(hefu.getToServer()));
        List<HefuServer> servers = new ArrayList<>();
        for(Integer serverId : hefu.getFromServers()){
            HefuServer fromServer = getHefuServerInfo(serverId);
            servers.add(fromServer);
        }
        task.setFromServer(servers);

        //载入语言Информация
        String lang = task.getHefu().getLanguage();
        try {
            Properties prop = new Properties();
            prop.load(new InputStreamReader(this.getClass().getResourceAsStream("/hefu/" + lang + ".properties"), Charset.defaultCharset()));
            String roleTitle = prop.getProperty("roleTitle");
            String roleContent = prop.getProperty("roleContent");
            String guildTitle = prop.getProperty("guildTitle");
            String guildContent = prop.getProperty("guildContent");
            String system = prop.getProperty("system");
            LangInfo langInfo = new LangInfo();
            langInfo.setLang(lang);
            langInfo.setSender(system);
            langInfo.setRoleTitle(roleTitle);
            langInfo.setRoleContent(roleContent);
            langInfo.setGuildTitle(guildTitle);
            langInfo.setGuildContent(guildContent);
            task.setLangInfo(langInfo);
        } catch (Exception e) {
            log.error("加载Объединение серверов语言文件Ошибка", e);
            throw new RuntimeException("加载Объединение серверов语言文件Ошибка");
        }
        return task;
    }

    @Override
    public Map<String, Object> getLog(Long id, Integer index) {
        Map<String, Object> map = new HashMap<>();
        map.put("index", index);
        map = hefuManager.getTaskLog(id,index);
        return map;
    }

    @Override
    public void dbbak(Long id, Integer serverId, Integer type) {
        HefuTask task = hefuManager.getTask(id);
        if(task != null){
            throw new RuntimeException("Объединение серверов进行中，不能备份");
        }
        String filedir = configService.selectConfigByKey("hefu.filedir");
        if(filedir.isEmpty()){
            throw new RuntimeException("系统参数hefu.filedir不存在");
        }
        File file = new File(filedir);
        if(!file.exists()){
            file.mkdirs();
        }
        Hefu hefu = selectHefuById(id);
        if(hefu == null){
            throw new RuntimeException("Объединение серверовДанные不存在");
        }
        if(serverId != null){//备份单个СерверДанные
            HefuServer server = getHefuServerInfo(serverId);
            bakServer(server, type, filedir);
        }else{
            bakServer(getHefuServerInfo(hefu.getToServer()), type, filedir);
            for(Integer s : hefu.getFromServers()){
                bakServer(getHefuServerInfo(s), type, filedir);
            }
        }
    }

    @Override
    public List<Dbbak> bakList(Long id) {
        Hefu hefu = selectHefuById(id);
        List<Dbbak> baks = new ArrayList<>();
        baks.add(getBakInfo(hefu.getToServer(), 1));
        baks.add(getBakInfo(hefu.getToServer(), 2));
        for(Integer server : hefu.getFromServers()){
            baks.add(getBakInfo(server, 1));
            baks.add(getBakInfo(server, 2));
        }
        return baks;
    }

    @Override
    public void dbrestore(Long id, Integer serverId, Integer type) {
        HefuTask task = hefuManager.getTask(id);
        if(task != null){
            throw new RuntimeException("Объединение серверов进行中，不能还原");
        }
        Hefu hefu = selectHefuById(id);
        if(hefu == null){
            throw new RuntimeException("Объединение серверовДанные不存在");
        }
        if(serverId != null){//Объединение серверов单个СерверДанные
            HefuServer server = getHefuServerInfo(serverId);
            restoreDB(server, type);
        }else{
            restoreDB(getHefuServerInfo(hefu.getToServer()), 1);
            restoreDB(getHefuServerInfo(hefu.getToServer()), 2);
            for(Integer s : hefu.getFromServers()){
                restoreDB(getHefuServerInfo(s), 1);
                restoreDB(getHefuServerInfo(s), 2);
            }
        }
    }

    @Override
    public boolean check(Long id) throws Exception{
        Hefu hefu = selectHefuById(id);
        HefuTask task = getHefuTask(hefu);
        //Данные库检测
        task.checkConfig(task);

        return true;
    }

    private Dbbak getBakInfo(Integer serverId, Integer type) {
        Dbbak param = new Dbbak();
        param.setServerId(serverId);
        param.setType(type);
        Dbbak bak = dbbakService.selectLatestDbbak(param);
        if(bak == null){
            bak = new Dbbak();
            bak.setServerId(serverId);
            bak.setType(type);
        }else{
            String url = bak.getUrl();
            File file = new File(url);
            if(file.exists()){
                bak.setFileExist(1);
            }
        }
        if(hefuManager.getBaks(serverId,type) != null){
            bak.setBaking(1);
        }
        return bak;
    }

    private void bakServer(HefuServer server, Integer type, String filedir) {
        if(type != null){
            hefuManager.dbbak(server, type, filedir);
        }else{
            hefuManager.dbbak(server, 1, filedir);
            hefuManager.dbbak(server, 2, filedir);
        }
    }

    private void restoreDB(HefuServer server, Integer type) {
        DBInfo db = null;
        if(type == 1){
            db = server.getDb();
        }else if(type == 2){
            db = server.getDblog();
        }else{
            throw new RuntimeException("Данные库Тип错误");
        }
        HefuTask.checkDB(db);
        Dbbak param = new Dbbak();
        param.setServerId(server.getServerId());
        param.setType(type);
        Dbbak dbbak = dbbakService.selectLatestDbbak(param);
        if(dbbak == null){
            throw new RuntimeException("没有找到对应的备份Информация");
        }
        String url = dbbak.getUrl();
        File file = new File(url);
        if(!file.exists()){
            throw new RuntimeException("文件不存在:"+url);
        }
        hefuManager.dbrestore(db, url);
    }

    /**
     * 获取СерверИнформация
     * @param serverId
     * @return
     */
    private HefuServer getHefuServerInfo(Integer serverId) {
        if(serverId == null){
            throw new RuntimeException("СерверId为空");
        }
        //Сервер配置
        TServer server = serverService.selectTServerByServerId(serverId);
        if(server == null){
            throw new RuntimeException("沒有找到СерверИнформация");
        }
        //游戏库配置
        TDb db = dbService.selectTDbByServerId(serverId);
        if(db == null){
            throw new RuntimeException("沒有找到游戏库Информация");
        }
//        //Журнал库配置
//        TServer dblog = serverService.selectTServerByServerId(serverId);
//        if(dblog == null){
//            throw new RuntimeException("沒有找到Журнал库Информация");
//        }
        HefuServer hefuServer = new HefuServer();
        hefuServer.setServerId(serverId);
        hefuServer.setServer(server);
        hefuServer.setDb(new DBInfo(db.getDbIp(), db.getDbPort().toString(), db.getDbname(), db.getDbuser(), db.getDbpassword()));
        hefuServer.setDblog(new DBInfo(server.getDblogIp(), server.getDblogPort().toString(), server.getDblogName(), server.getDblogUser(), server.getDblogPwd()));
        return hefuServer;
    }

    public Map<Integer, Integer> checkIsHefu(Long id){
        Map<Integer, Integer> ss = new HashMap<>();
        Hefu hefu = hefuMapper.selectHefuById(id);
        HefuTask task = getHefuTask(hefu);
        for(HefuServer s : task.getFromServer()){
            if(s.getServer().getHefuServerID() != null && s.getServer().getHefuServerID() > 0){
                ss.put(s.getServerId(), s.getServer().getHefuServerID());
            }
        }
        return ss;
    }

    @Override
    public List<String> logRecord(Long id) {
        Hefu hefu = hefuMapper.selectHefuRecord(id);
        if(hefu == null || hefu.getRecordList() == null){
            return new ArrayList<>();
        }
        return hefu.getRecordList();
    }
}
