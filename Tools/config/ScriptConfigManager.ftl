/**
 * Auto generated, do not edit it
 *
 * 配置表脚本管理
 */
package com.data.script;

import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Timer;
import java.util.TimerTask;
import com.data.ConfigBase;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Set;
import com.data.CfgManager;
import java.util.Map;
import com.data.bean.Cfg_Equip_Bean;
import com.data.bean.Cfg_Item_Bean;
import com.data.container.Cfg_Item_Container;

	
public final class ScriptConfigManager{

    private final String classpath; // javac classpath
    private final String javaFilePath; // .java文件路径
    private final String classFilePath; // javac编译输出路径，内部ClassLoader查找脚本class文件路径

    /**
     * 根据包名获取config容器
     *
     * @param base 传入null返回容易数据，不为null设置数据
     * @param className
     */
    @SuppressWarnings("rawtypes")
    public ConfigBase getScriptConfigBase(String className) {
        switch (className) {
            case "config.Cfg_Global_Load":
                return CfgManager.Global_Container;
            case "config.Cfg_Drop_item_Load":
                return CfgManager.getCfg_Drop_item_Container();
            case "config.Cfg_Drop_package_Load":
                return CfgManager.getCfg_Drop_package_Container();
            case "config.Cfg_MessageString_Load":
                return CfgManager.MessageString_Container;
            <#list list as value>
            case "config.Cfg_${value.filename?cap_first}_Load":
                return CfgManager.getCfg_${value.filename?cap_first}_Container();
            </#list>
            default:
                break;
        }
        return null;
    }
    
    /**
     * 配置初始化
     */
    private ScriptConfigManager() {
        javaFilePath = "script";
        classFilePath = "bin";
        File file = new File(classFilePath);
        if (!file.exists()) {
            file.mkdirs();
        }
        StringBuilder sb = new StringBuilder();
        for (URL url : ((URLClassLoader) this.getClass().getClassLoader()).getURLs()) {
            String p = url.getFile();
            sb.append(p).append(File.pathSeparator);
        }
        sb.append(classFilePath);
        this.classpath = sb.toString();
        load();
    }

    /**
     * 用枚举来实现单例
     */
    private enum Singleton {

        INSTANCE;
        ScriptConfigManager processor;

        Singleton() {
            this.processor = new ScriptConfigManager();
        }

        ScriptConfigManager getProcessor() {
            return processor;
        }
    }

    /**
     * 获取ScriptConfigManager的实例对象.
     *
     * @return
     */
    public static ScriptConfigManager GetInstance() {
        return Singleton.INSTANCE.getProcessor();
    }

    /**
     * 加载配置
     */
    private void load() {
        try {
            initScript();
            String val = System.getProperty("ideDebug");
            boolean isDebug = (val != null && val.equals("true"));
            if (isDebug) {
                timerCheckReload();
            }
        } catch (Exception e) {
            System.out.println("load script fail:" + e);
            System.exit(-4);
        }
    }

    /**
     * 初始化配置脚本数据
     */
    @SuppressWarnings("rawtypes")
    private void initScript() throws Exception {
        String val = System.getProperty("ideDebug");
        boolean isDebug = (val != null && val.equals("true"));
        Set<Class<IScriptConfig>> scriptList = ClassUtil.GetSubClasses("config", IScriptConfig.class);
        for (Class<IScriptConfig> cls : scriptList) {
            IScriptConfig script = cls.newInstance();
            String className = script.getClass().getName();
            ConfigBase config = getScriptConfigBase(className);
            if (config == null) {
                continue;
            }
            config.setJavaFileTimestamp(0);
            String filename = javaFilePath + "/" + className.replace('.', '/') + ".java";
            config.setFileName(filename);
            if (!isDebug) {
                File f = new File(filename);
                if (f.isFile() && f.canRead()) {
                    reloadConfigScript(config, className, f);
                }
            }
        }
    }

    /**
     * 重新加载配置表，传入参数类似为：load.Cfg_Mapsetting_Load
     *
     * @param className
     * @return
     */
    @SuppressWarnings("rawtypes")
    public boolean reloadConfigScript(String className) throws Exception {
        ConfigBase config = getScriptConfigBase(className);
        if (config == null) {
            throw new Exception("配置表：" + className + "reload失败，没有该配置文件");
        }
        File f = new File(config.getFileName());
        if (!f.isFile() || !f.canRead()) {
            throw new Exception("配置表：" + className + "reload失败，配置文件错误");
        }
        return reloadConfigScript(config, className, f);
    }

    @SuppressWarnings({"rawtypes", "deprecation"})
    private boolean reloadConfigScript(ConfigBase config, String className, File f) throws Exception {
        Class<?> clazz;
        try (InputStream inStream = new FileInputStream(f)) {
            byte[] bytes = new byte[(int) f.length()];
            inStream.read(bytes);
            clazz = ClassUtil.JavaCodeToObject(className, new String(bytes, "UTF-8"), classpath, classFilePath);
        }
        if (clazz == null) {
            throw new Exception("配置表：" + className + "reload失败，读取class失败");
        }
        if (!ClassUtil.IsInterface(clazz, IScriptConfig.class)) {
            throw new Exception("配置表：" + className + "reload失败，配置文件load不是继承于IScriptConfig");
        }
        IScriptConfig load = (IScriptConfig) clazz.newInstance();
        Map m = config.newMapValuees();
        load.load(m);
        config.initValue(m);
        config.setJavaFileTimestamp(f.lastModified());
        System.out.println("reload config:" + className);
        return true;

    }
    @SuppressWarnings({"rawtypes", "deprecation"})
   public void reloadCofigItem(){
        Map m = Cfg_Item_Container.GetInstance().newMapValuees();
        for (Cfg_Item_Bean bean : CfgManager.getCfg_Item_Container().getValuees()) {
             m.put(bean.getId(), bean);
        }
        for (Cfg_Equip_Bean bean : CfgManager.getCfg_Equip_Container().getValuees()) {
            int type = 2;
            //400万-500万之间是圣装，类型为14
            if ( bean.getId() >= 4000000 &&  bean.getId() <= 5000000){
                    type = 14;
               }
            //500万-600万之间是仙甲，类型为22
             if ( bean.getId() >= 5000000 &&  bean.getId() <= 6000000){
                    type = 22;
              }
            //700万-800万之间是宠物装备，类型为23
            if ( bean.getId() >= 7000000 &&  bean.getId() <= 8000000){
                    type = 23;
            }
            //魂印
            if ( bean.getPart() >= 211 && bean.getPart() <= 220){
                    type = 24;
            }
            //坐骑装备
            if ( bean.getPart() >= 301 && bean.getPart() <= 304){
                type = 25;
            }
            //魔魂装备
            if ( bean.getPart() >= 305 && bean.getPart() <= 340){
                type = 26;
            }
            //幻装装备
            if ( bean.getPart() >= 441 && bean.getPart() <= 450){
                type = 28;
            }
            Cfg_Item_Bean itemBean = new Cfg_Item_Bean(
            bean.getId(),
            bean.getName(), 0, type, 0, bean.getTrade_recom(), "", bean.getQuality(),
            bean.getLevel(),0,2, bean.getGender().getValueString(), bean.getBind(), 1,
            "", bean.getConfirm(), 0, 0, "", 0,0,
            0, 0, 0, 0, "", 0, 0, 0
            , 0, "", "",
            bean.getDrop_notice(),
			bean.getChatchannel().getValueString(),
			bean.getAuction_price_type(),
            bean.getAuction_use_coin(),
            bean.getAuction_min_price(),
            bean.getAuction_max_price(),
            bean.getAuction_single_type(),
            bean.getAuction_single_price(),
            bean.getAuction_countdown(),
            bean.getAuction_all_time(),
            bean.getAuction_guild_all_time(),
            bean.getDead_time());
            m.put(bean.getId(), itemBean);
        }
        Cfg_Item_Container.GetInstance().initValue(m);
   }


    /**
     * 启动一个timer，每秒自动编译最新脚本，开发版本时才调用
     */
    private void timerCheckReload() {
        new Timer("ScriptCheckReload-Timer").schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    check();
                } catch (Exception e) {

                }
            }

            private void check() {
                try {
                    File dir = new File(javaFilePath + "/config");
                    checkScript(dir, "config");
                } catch (Exception e) {

                }
            }

            /**
             * 检查脚本
             */
            @SuppressWarnings("rawtypes")
            private void checkScript(File dir, String pakckageName) throws IllegalAccessException, IOException, ClassNotFoundException, InstantiationException, Exception {
                for (File f : dir.listFiles()) {
                    if (f.isDirectory()) {
                        if (pakckageName.isEmpty()) {
                            checkScript(f, f.getName());
                        } else {
                            checkScript(f, pakckageName + "." + f.getName());
                        }
                        continue;
                    }
                    if (f.isFile() && f.canRead()) {
                        String className;
                        if (pakckageName.isEmpty()) {
                            className = f.getName();
                        } else {
                            className = pakckageName + "." + f.getName();
                        }
                        className = className.replace(".java", "");
                        ConfigBase config = getScriptConfigBase(className);
                        if (config == null) {
                            continue;
                        }
                        if (config.getJavaFileTimestamp() == 0) {
                            config.setJavaFileTimestamp(f.lastModified());
                            continue;
                        }
                        if (f.lastModified() == config.getJavaFileTimestamp()) {
                            continue;
                        }
                        //重新加载
                        reloadConfigScript(config, className, f);
                    }
                }
            }
        }, 1000, 1000);
    }
}
