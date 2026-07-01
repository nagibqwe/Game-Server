package com.game.structs;

import com.data.Global;
import  com.data.MessageString;
import com.game.server.GameServer;
import com.game.utils.Symbol;
import game.core.net.Config.ServerConfig;
import org.apache.commons.lang.SystemUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hewei@haowan123.com
 */
public class ServerStr {

    private static final Logger log = LogManager.getLogger(ServerStr.class);

    /**
     * 此函数只是提供给信鹆系统使用的接口，其它功能请不要使用
     *
     * @param key 传入字符串KEY
     * @return
     */
    public static String ServerLocalString(String key) {
        if (serverString.containsKey(key)) {
            return serverString.get(key);
        }
        return key;
    }


    public static void loadMutil() {
        if (GameServer.getServerScript() != null) {
            GameServer.getServerScript().loadMutilLang();
        }
    }


    public static String getLevelNameHighStr(int level) {
        int HightLevel = Global.PeakLevel_NeedLevel;
        int offsetLevel = level - HightLevel;
        if (offsetLevel < 1) {
            return "";
        }

        return "1&_" + MessageString.HIGHLEVELNAME;
    }

    public static String getLevelNameSlowStr(int level) {
        int HightLevel = Global.PeakLevel_NeedLevel;
        int offsetLevel = level - HightLevel;
        if (offsetLevel < 1) {
            return String.valueOf(level);
        }

        return String.valueOf(offsetLevel);
    }

    private static final ConcurrentHashMap<String, String> serverLang = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, Integer> languageNo = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, String> dragoonMap = new ConcurrentHashMap<>();//神兵名称,

    public static synchronized ConcurrentHashMap<String, String> getServerLang() {
        return serverLang;
    }

    public static synchronized ConcurrentHashMap<String, Integer> getLanguageNo() {
        return languageNo;
    }

    public static synchronized ConcurrentHashMap<String, String> getDragoon() {
        return dragoonMap;
    }
    
    public static String getChatTableName(String name){
        return "2&_" + name;
    }

    /**
     * 增加一个不含有标记的接口函数用来处理直接取名字出来， 不包含标志
     *
     * @param tableName 表名
     * @param id 关键字ID值
     * @return 返回相应字符串
     */
    public static String getNotHaveMarkStr(String tableName, int id) {
        return GameServer.getServerScript().getNotHaveMarkStr(tableName, id);
    }

    //获得物品的名字
    public static String getItemStr(int itemModelId, String lang, String name) {
        return GameServer.getServerScript().getItemString(itemModelId, lang, name);
    }

    //加载服务器语言包
    public static void load(String area) throws IOException {
//        area = "cn";//默认是国语了，不再有其它了
//        log.error("加载当前语言类型为：" + area);
//        Properties prop = new Properties();
//        String proPath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "lang" + File.separator + "language_" + area + ".properties";
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));//
////        InputStream in = new BufferedInputStream(new FileInputStream());
//        prop.load(br);
//        Set keyValue = prop.keySet();
//        for (Iterator it = keyValue.iterator(); it.hasNext();) {
//            String key = (String) it.next();
//            String Property = prop.getProperty(key);
//            serverLang.put(key, Property);
//        }
//        log.error("共加载了语言包条数为：" + prop.size());
//        loadNo();
//        loadDragoon();
        loadOffString();
        loadMutil();
    }

    public static void loadNo() throws IOException {
        Properties prop = new Properties();
        String proPath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "lang" + File.separator + "languageNo.properties";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));
//        InputStream in = new BufferedInputStream(new FileInputStream());
        prop.load(br);
        Set keyValue = prop.keySet();
        for (Iterator it = keyValue.iterator(); it.hasNext();) {
            String key = (String) it.next();
            try {
                Integer Property = Integer.parseInt(prop.getProperty(key));
                languageNo.put(key, Property);
            } catch (Exception e) {
                log.error("请语言条：" + key + "=" + prop.getProperty(key) + "出错了！");
            }
        }
        log.error("共加载了语言包条数为：" + prop.size());
    }

    /**
     * 获取神兵名称
     *
     * @param career 职业标识
     * @param dragoonId 神兵ID
     * @return
     */
    public static String getDragoonName(int career, int dragoonId) {
        return GameServer.getServerScript().getDragoonName(career, dragoonId);
    }

    //多语言相关处理////////////////////////////////////////////////////////////
    private static final ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> languageMap = new ConcurrentHashMap<>(); //<languageType, languagePack>
    /**
     * 服务器特有的语言包
     */
    private static final ConcurrentHashMap<String, String> serverString = new ConcurrentHashMap<>();

    public static ConcurrentHashMap<Integer, ConcurrentHashMap<String, String>> getLanguageMap() {
        return languageMap;
    }

    private static final ConcurrentHashMap<String, Integer> langTypeMap = new ConcurrentHashMap<>(); //<langStrType, langIntType>

    public static ConcurrentHashMap<String, Integer> getLangTypeMap() {
        return langTypeMap;
    }

    private static void loadOffString() throws UnsupportedEncodingException, IOException {
        String area = ServerConfig.getLangType();
        log.error("加载本地语言类型为：" + area);
        Properties prop = new Properties();
        String proPath = SystemUtils.USER_DIR + File.separator + "config" + File.separator + "lang" + File.separator + "ServerString_" + area + ".properties";
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));//
//        InputStream in = new BufferedInputStream(new FileInputStream());
        prop.load(br);
        Set keyValue = prop.keySet();
        for (Iterator it = keyValue.iterator(); it.hasNext();) {
            String key = (String) it.next();
            String Property = prop.getProperty(key);
            serverString.put(key.toLowerCase(), Property);
        }
        log.error("共加载了本地语言包条数为：" + prop.size());
    }

    private void initLangType() {
        if (!langTypeMap.isEmpty()) {
            langTypeMap.clear();
        }
        for (LanguageEnum lang : LanguageEnum.values()) {
            langTypeMap.put(lang.getsType(), lang.getiType());
        }
    }

    //加载语言包
    public void loadLanguagePacks(String langSet) {
        initLangType();

        StringBuilder langFilePath = new StringBuilder();
        langFilePath.append(SystemUtils.USER_DIR).append(File.separator).append("config").append(File.separator).append("lang").append(File.separator);

        log.error("开始加载语言包");
        try {
            String[] arrLangSet = langSet.split(Symbol.DOUHAO);
            for (String lang : arrLangSet) {
                StringBuilder langFileName = new StringBuilder();
                langFileName.append("language_").append(lang).append(".properties");
                String proPath = langFilePath.toString() + langFileName.toString();
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(proPath), "utf-8"));
                Properties prop = new Properties();
                prop.load(br);
                ConcurrentHashMap<String, String> langPack = new ConcurrentHashMap<>();
                for (Entry<Object, Object> entry : prop.entrySet()) {
                    String key = (String) entry.getKey();
                    String value = (String) entry.getValue();
                    langPack.put(key, value);
                }
                languageMap.put(langTypeMap.get(lang), langPack);
                log.error("加载语言包" + langFileName.toString() + "的条数为：" + prop.size());
            }
            loadNo();
            log.error("语言包加载完毕！");
        } catch (UnsupportedEncodingException | FileNotFoundException ex) {
            log.error("加载语言包异常！" + ex);
        } catch (IOException ex) {
            log.error("加载语言包异常！" + ex);
        }
    }

    //获取语言
    public static String getLanguage(int id, long roleId) {
        return getLanguage("serverstr", id, roleId);
    }

    /*
     * 获取语言
     * tableName: 语言所在的数据表明
     * id: 语言Id
     * languageType: manager.registerManager.getLanguageType(roleId);
     */
    public static String getLanguage(String tableName, int id, long roleId) {
        return GameServer.getServerScript().getLanguage(tableName, id, roleId);
    }

    public static String getLanguage(String tableName, String id) {
        return GameServer.getServerScript().getLanguage(tableName, id);
    }
    //多语言相关处理////////////////////////////////////////////////////////////

}
