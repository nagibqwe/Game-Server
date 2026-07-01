/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import tools.LoadConfig;
import tools.WriteFile;
import utils.TextFile;

/**
 * 配置表生成代码
 *
 * @author hewei
 */
public class ExcelToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");

    public final static Logger logger = Logger.getLogger(ExcelToCode.class);

    public final static HashMap<String, HashMap<String, Integer>> alllanguages = new HashMap<>();

    public final static String MAIN_VERSION = "Config";

    public static void main(String[] args) {
        try {
            if (args.length > 0){
                String strFilePath = System.getProperty("user.dir") + File.separator   + ConfigExcelReader.excelFileBase + args[0];
                System.out.println(strFilePath);
                File dir = new File(strFilePath);
                String version = args[0];
                if (!dir.exists()){
                    System.out.println("版本文件夹不存在  :"  +strFilePath);
                    version = MAIN_VERSION;
                }
                toAllJava(version);
            }else {
                HashMap<String,String> versionDatas = getVersionDatas();
                if (versionDatas.size()<=0){
                    System.out.println("没有可用的版本目录:");
                    return;
                }
                for (Map.Entry<String,String> entry :versionDatas.entrySet()){
                    System.out.println("输入 :"  + entry.getKey() + " 导出版本 " + entry.getValue() );
                }
                System.out.println("注意！Config目录为基础主版本。其他目录为差异化版本，只放差异化的表格。\n" +
                        "例如输入2，会先导出Config目录的所有文件，然后导出2版本中所有文件，并替换掉同名的基础文件。\n" +
                        "请输入您要导的版本号,Enter键导出默认Config目录:");
                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                String str = br.readLine();
                System.out.println(str);
                if (str.equals("")){
                    toJava(MAIN_VERSION);
                }else {
                    if (!versionDatas.containsKey(str)){
                        System.out.println("输入KEY 不存在: " +str);
                        return;
                    }
                    String version = versionDatas.get(str);
                    toJava(version);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static HashMap<String,String> getVersionDatas(){
            HashMap<String,String> versionDatas = new HashMap<>();
            File dir = new File(ConfigExcelReader.excelFileBase);
            int count = 1;
             for (File f : dir.listFiles()) {
                  if (!f.isDirectory()){
                      continue;
                  }
                  if (!f.getName().contains("Config")){
                      continue;
                  }
                 versionDatas.put(count + "",f.getName());
                 count++;
             }
            return versionDatas;
    }



    public static void toAllJava(String version){
        try {
            StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
            pathBuilder.append("log4j_server.xml");
            DOMConfigurator.configureAndWatch(pathBuilder.toString());
            LoadConfig.load();
            toCode(version);
            System.out.print("导表完成！！");

            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                killProcess();
            }else{
                return;
            }
         } catch (Exception e) {
             error.error(e, e);
          }
    }


    public static void toJava(String version){
      try {
          StringBuilder pathBuilder = new StringBuilder(System.getProperty("user.dir") + File.separator + "config" + File.separator);
          pathBuilder.append("log4j_server.xml");
          DOMConfigurator.configureAndWatch(pathBuilder.toString());
          LoadConfig.load();
          toCode(version);
          scannerInput();
      } catch (Exception e) {
          error.error(e, e);
      }
    }
    private static void scannerInput() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.out.print("按任 Enter 键退出：");
            String a = scanner.nextLine();
            if (a !=null)
                killProcess();
        }
    }

    private static void killProcess() {
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        try {
            rt.exec("cmd.exe /C start wmic process where name='cmd.exe' call terminate");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    /**
     * 获取语言包id
     *
     * @param str
     * @param fileName
     * @param languageType
     * @return
     */
    public static int getLanguageId(String str, String fileName, String languageType) {
        HashMap<String, Integer> languages = alllanguages.get(languageType);
        if (languages == null) {
            return 0;
        }
        Integer a = languages.get(str);
        if (a == null) {
            if (str.isEmpty()) {
                return 0;
            }
            if (isLetterDigitOrChinese(str)) {
                return 0;
            }
            error.error("语言：" + str + "   -------------------没有找到对应的语言包id;fileName:" + fileName);
            return 0;
        }
        return a;
    }

    //\t == 10;\n == 13
    public static String decodeEscape(String str) {
        char[] list = str.toCharArray();
        StringBuilder decodeEscapeSb = new StringBuilder();
        char a;
        for (int i = 0; i < list.length; ++i) {
            a = list[i];
            if (a == 10 || a == 13) {
                decodeEscapeSb.append("\\").append("n");
            } else {
                decodeEscapeSb.append(a);
            }
        }
        return decodeEscapeSb.toString();
    }

    public static boolean isLetterDigitOrChinese(String str) {
        String regex = "^[a-z0-9A-Z]+$";
        return str.matches(regex);
    }

    /**
     * 生成所有代码
     *
     */
    public static void toCode(String version) {
        loadLanguage();
        List<String> javaCodeBasePathes = new ArrayList<>();
        javaCodeBasePathes.add(ConfigExcelReader.javaCodeBase);
        // TODO: 2019/6/13
//        String[] strList = ConfigExcelReader.excelFilePathOther.split(",");
//        for (String str : strList) {
//            javaCodeBasePathes.add(ConfigExcelReader.javaCodeBase + "_" + str);
//        }
        //删除之前生成的文件
        for (String s : javaCodeBasePathes) {
            TextFile.delteFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "bean");
            TextFile.delteFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container");
            TextFile.delteFile(s + ConfigExcelReader.javaScriptPath);
        }

        //不管导的是主版本，还是差异化版本，都先将主版本导一遍
        String filePath = ConfigExcelReader.excelFileBase + MAIN_VERSION;
        //生成基础的公告配置
        List<ExcelLoadData> list = excelPathToCode(filePath, javaCodeBasePathes, "China");
        toScriptConfigManagerCode(list, javaCodeBasePathes);

        //如果导的是差异化版本，再将差异化版本导一遍就可以了
        if (! MAIN_VERSION.equals(version)) {
            logger.info("开始导差异化版本" + version + "...");
            filePath = ConfigExcelReader.excelFileBase + version;
            excelPathToCode(filePath, javaCodeBasePathes, "China");
        }
// TODO: 2019/6/13
        //各个语言特殊配置
//        for (String str : strList) {
//            String javaExcelPath = ConfigExcelReader.excelFilePathOtherBase + str;
//            List<String> javaCodespecPathes = new ArrayList<>();
//            javaCodespecPathes.add(ConfigExcelReader.javaCodeBase + "_" + str);
//            excelPathToCode(javaExcelPath, javaCodespecPathes, str);
//        }
    }

    private static List<ExcelLoadData> excelPathToCode(String javaExcelPath, List<String> javaCodeBasePathes, String languageType) {
        List<ExcelLoadData> list = new ArrayList<>();
        for (String file : ConfigExcelReader.getExcelFileList(javaExcelPath)) {
            ExcelLoadData datas = toCode(javaExcelPath, javaCodeBasePathes, file, languageType);
            if (datas == null) {
                continue;
            }
            if (datas.isIsSuccess()) {
                list.add(datas);
            }
        }
        String serverPath = javaExcelPath + File.separator +  ConfigExcelReader.excelServerFilePath;
        for (String file : ConfigExcelReader.getExcelFileList(serverPath)) {
            ExcelLoadData datas = toCode(serverPath, javaCodeBasePathes, file, languageType);
            if (datas == null) {
                continue;
            }
            if (datas.isIsSuccess()) {
                list.add(datas);
            }
        }

        GlobalExcelToCode.globalToCode(javaExcelPath, javaCodeBasePathes);
        DropExcelToCode.dropToCode(serverPath, javaCodeBasePathes);
        FunctionExcelToCode.functionToCode(javaExcelPath, javaCodeBasePathes);
        ItemChangeReasonExcelToCode.ItemChangeReasonToCode(javaExcelPath, javaCodeBasePathes);
        MessageStringToCode.messageStringToCode(javaExcelPath,javaCodeBasePathes);
        TaskExcelToCode.taskToCode(javaExcelPath, javaCodeBasePathes);
//        StringsToCode.stringsToCode(javaExcelPath, javaCodeBasePathes, languageType);
        return list;
    }

    public static void toScriptConfigManagerCode(List<ExcelLoadData> list, List<String> javaCodeBasePathes) {
        try {
            HashMap<String, Object> root = new HashMap<>();
            root.put("list", list);
            root.put("package", ConfigExcelReader.javaPackageName);
            Template beanTemp = WriteFile.cfg.getTemplate("ScriptConfigManager.ftl", "utf-8"); // 读取ScriptConfigManager模板
            //输出ScriptConfigManager文件
            try (Writer out = new StringWriter()) {
                beanTemp.process(root, out);
                for (String s : javaCodeBasePathes) {
                    WriteFile.writeFile(s + ConfigExcelReader.scriptConfigManagerPath + File.separator + "ScriptConfigManager.java", out.toString());
                }
                logger.info("ScriptConfigManager.java导出成功。"   );
            }
            Template cfgTemp = WriteFile.getCfg().getTemplate("CfgManager.ftl", "utf-8"); // 读取CfgManager模板
            //输出CfgManager文件
            try (Writer out = new StringWriter()) {
                cfgTemp.process(root, out);
                for (String s : javaCodeBasePathes) {
                    WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "CfgManager.java", out.toString());
                }
                logger.info("CfgManager.java导出成功。");
            }
        } catch (IOException | TemplateException e) {
            error.error(e, e);
        }
    }

    /**
     * 生成语言数据
     */
    private static void languageStrToCode() {
//        try {
//            ExcelLoadData datas = new ExcelLoadData();
//            datas.setFilename("languageStr");
//            datas.setIsSuccess(true);
//            datas.getBeanParms().append("int languageId,String languageStr");
//            ExcelLoadFiled fileld1 = new ExcelLoadFiled();
//            fileld1.setFiledDesc("语言包id");
//            fileld1.setFiledName("languageId");
//            fileld1.setFiledType("int");
//            fileld1.setJavaClassName("int");
//            datas.getFiledsServerUseList().add(fileld1);
//            ExcelLoadFiled fileld2 = new ExcelLoadFiled();
//            fileld2.setFiledDesc("语言包内容");
//            fileld2.setFiledName("languageStr");
//            fileld2.setFiledType("char");
//            fileld2.setJavaClassName("String");
//            datas.getFiledsServerUseList().add(fileld2);
//
//            toBean("languageStr", datas);
//            toContainer("languageStr");
//            HashMap<String, Object> root = new HashMap<>();
//            root.put("classBeanName", "Cfg_LanguageStr_Bean");
//            root.put("classLoadName", "Cfg_LanguageStr_Load");
//            root.put("explain", "语言包");
//            root.put("package", ConfigExcelReader.javaPackageName);
//            root.put("values", languageIdList);
//
//            Template temp = WriteFile.cfg.getTemplate("Cfg_LanguageStr_Load.ftl", "utf-8"); // 读取模板
//            try (Writer out = new StringWriter()) {
//                temp.process(root, out);
//                WriteFile.writeFile(ConfigExcelReader.javaScriptPath + File.separator + "Cfg_LanguageStr_Load.java", out.toString());
//            }
//            logger.info("语言包导出成功。");
//        } catch (IOException | TemplateException e) {
//            error.error(e, e);
//        }
    }

    /**
     * 生成代码
     *
     * @param javaExcelPath
     * @param javaPathes
     * @param fileName
     * @param languageType
     * @return
     */
    public static ExcelLoadData toCode(String javaExcelPath, List<String> javaPathes, String fileName, String languageType) {
        WriteFile.getCfg();
        ExcelLoadData datas = ConfigExcelReader.load(javaExcelPath, fileName, languageType);
        for (String s : javaPathes) {
            toJavaCode(s, datas);
        }
        logger.info(fileName + "生成java代码成功");
        return datas;
    }

    private static void toBean(String javaPath, String finalName, ExcelLoadData datas) {
        try {
            //bean的class名
            String classBeanName = getJavaClassBeanName(finalName);
            //container的class名
            String classContainerName = getJavaClassContainerName(finalName);
            //获取load的class名
            String classLoadName = getJavaClassLoadName(finalName);
            HashMap<String, Object> root = new HashMap<>();
            root.put("classBeanName", classBeanName);
            root.put("classContainerName", classContainerName);
            root.put("classLoadName", classLoadName);
            root.put("package", ConfigExcelReader.javaPackageName);
            root.put("explain", finalName + "配置表");
            root.put("imports", datas.getImports());
            root.put("parms", datas.getBeanParms());
            root.put("fields", datas.getFiledsServerUseList());
            root.put("languageList", datas.getLanguageList());
            root.put("values", datas.getServerValues());
            Template beanTemp = WriteFile.cfg.getTemplate("Bean.ftl", "utf-8"); // 读取bean模板
            //输出bean文件
            try (Writer out = new StringWriter()) {
                beanTemp.process(root, out);
                WriteFile.writeFile(javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "bean" + File.separator + classBeanName + ".java", out.toString());
            }

        } catch (IOException | TemplateException e) {
            error.error(e, e);
        }
    }

    public static void toContainer(String javeCodeBasePath, String finalName) {
        try {
            //bean的class名
            String classBeanName = getJavaClassBeanName(finalName);
            //container的class名
            String classContainerName = getJavaClassContainerName(finalName);
            //获取load的class名
            String classLoadName = getJavaClassLoadName(finalName);
            HashMap<String, Object> root = new HashMap<>();
            root.put("package", ConfigExcelReader.javaPackageName);
            root.put("explain", finalName + "配置表");
            root.put("classBeanName", classBeanName);
            root.put("classContainerName", classContainerName);
            root.put("classLoadName", classLoadName);
            //输出container文件
            Template containerTemp = WriteFile.cfg.getTemplate("Container.ftl", "utf-8"); // 读取Container模板
            try (Writer out = new StringWriter()) {
                containerTemp.process(root, out);
                WriteFile.writeFile(javeCodeBasePath + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container"
                        + File.separator + classContainerName + ".java", out.toString());
            }

        } catch (IOException | TemplateException e) {
            error.error(e, e);
        }
    }

    /**
     * 生成java代码
     *
     * @param javaCodeBasePath
     * @param datas
     */
    public static void toJavaCode(String javaCodeBasePath, ExcelLoadData datas) {
        try {
            if (datas == null) {
                return;
            }
            if (!datas.isIsSuccess()) {
                return;
            }

            HashMap<String, Object> root = new HashMap<>();
            //bean的class名
            String classBeanName = getJavaClassBeanName(datas.getBeanName());
            //container的class名
            String classContainerName = getJavaClassContainerName(datas.getFilename());
            //获取load的class名
            String classLoadName = getJavaClassLoadName(datas.getFilename());
            root.put("classBeanName", classBeanName);
            root.put("classContainerName", classContainerName);
            root.put("classLoadName", classLoadName);
            root.put("explain", datas.getFilename() + "配置表");
            root.put("package", ConfigExcelReader.javaPackageName);
            root.put("imports", datas.getImports());
            root.put("parms", datas.getBeanParms());
            root.put("fields", datas.getFiledsServerUseList());
            root.put("languageList", datas.getLanguageList());
            root.put("values", datas.getServerValues());
            Template beanTemp = WriteFile.cfg.getTemplate("Bean.ftl", "utf-8"); // 读取bean模板
            //输出bean文件
            try (Writer out = new StringWriter()) {
                beanTemp.process(root, out);
                WriteFile.writeFile(javaCodeBasePath + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "bean" + File.separator + classBeanName + ".java", out.toString());
            }
            //输出container文件
            Template containerTemp = WriteFile.cfg.getTemplate("Container.ftl", "utf-8"); // 读取Container模板
            try (Writer out = new StringWriter()) {
                containerTemp.process(root, out);
                WriteFile.writeFile(javaCodeBasePath + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container" + File.separator + classContainerName + ".java", out.toString());
            }
            //输出load文件
            Template loadTemp = WriteFile.cfg.getTemplate("ConfigScript.ftl", "utf-8"); // 读取Container模板
            try (Writer out = new StringWriter()) {
                loadTemp.process(root, out);
                WriteFile.writeFile(javaCodeBasePath + ConfigExcelReader.javaScriptPath + File.separator + classLoadName + ".java", out.toString());
            }
        } catch (IOException | TemplateException e) {
            error.error(datas.getFilename() + "生成java代码失败", e);
            return;
        }
    }

    /**
     * 加载语言包
     */
    private static void loadLanguage() {
        loadLanguage("China");
        String[] strList = ConfigExcelReader.excelFilePathOther.split(",");
        for (String str : strList) {
            loadLanguage(str);
        }
    }

    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    private static void loadLanguage(String languageType) {
        int i = 0;
        try {
            String fileName = ConfigExcelReader.languageCsvPath + File.separator + languageType + File.separator + "_LocaleText.csv";
            File f = new File(fileName);
            if (!f.canRead()) {
                return;
            }
            HashMap<String, Integer> languages = new HashMap<>();
            FileInputStream fis = new FileInputStream(fileName);
            byte[] b = new byte[3];
            fis.read(b);
            String code = "GBK";
            if (b[0] == -17 && b[1] == -69 && b[2] == -65) {
                code = "UTF-8";
            }
            InputStreamReader isw = new InputStreamReader(fis, code);
            try (BufferedReader br = new BufferedReader(isw)) {
                boolean bReadNext = true;
                while (bReadNext) {
                    i++;
                    // 一行
                    String strReadLine = br.readLine();
                    if (strReadLine == null) {
                        break;
                    }
                    String[] ss = strReadLine.split(",");
                    String str = ss[0];
                    int id = Integer.parseInt(str);
                    str = strReadLine.replaceFirst(str, "");
                    str = str.replaceFirst(",", "");
                    if (ss.length > 2) {
                        int length = str.length();
                        str = str.substring(1, length - 1);
                    }
                    languages.put(str, id);
                }
            }
            alllanguages.put(languageType, languages);
        } catch (IOException | NumberFormatException e) {
            error.error("语言包csv读取失败:行数：" + i, e);
        }
    }

    public static String getJavaClassBeanName(String sheetName) {
        char oldChar = sheetName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = sheetName.replaceFirst(oldChar + "", newChar + "");
        return "Cfg_" + replace + "_Bean";
    }

    public static String getJavaClassContainerName(String sheetName) {
        char oldChar = sheetName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = sheetName.replaceFirst(oldChar + "", newChar + "");
        return "Cfg_" + replace + "_Container";
    }

    public static String getJavaClassLoadName(String sheetName) {
        char oldChar = sheetName.charAt(0);
        char newChar = (oldChar + "").toUpperCase().charAt(0);
        String replace = sheetName.replaceFirst(oldChar + "", newChar + "");
        return "Cfg_" + replace + "_Load";
    }

}
