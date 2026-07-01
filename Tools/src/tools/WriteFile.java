/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tools;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import java.io.File;
import java.io.IOException;
import utils.TextFile;

/**
 *
 * @author hewei
 */
public class WriteFile {
    
     /**
     * 默认模板ftl文件位置
     */
    private final static String DEFAULT_FTL_DIR = System.getProperty("user.dir") + File.separator + "config";
    /**
     * 模板数据九 零一起 玩 www.901 75.com
     */
    public static Configuration cfg;

    public static void writeFile(String filePath, String fileContent) throws IOException {
        File file = new File(filePath);
        TextFile.write(fileContent, file);
    }

    /**
     * 获取模板数据
     *
     * @return
     */
    public static Configuration getCfg() {
        if (cfg == null) {
            try {
                cfg = new Configuration();
                File path = new File(DEFAULT_FTL_DIR);
                cfg.setDirectoryForTemplateLoading(path);
                cfg.setObjectWrapper(new DefaultObjectWrapper());
            } catch (IOException e) {
            }
        }
        return cfg;
    }
    
}
