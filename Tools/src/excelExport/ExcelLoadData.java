/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author hewei
 */
public class ExcelLoadData {

    //文件名
    private String filename;
    //生成java的bean名字
    private String beanName;
    //字段数据
    private final HashMap<Integer, ExcelLoadFiled> fileds = new HashMap<>();
    //服务器字段数据列表
    private final List<ExcelLoadFiled> filedsServerUseList = new ArrayList<>();
    //三行及后面行数的服务器用的配置数据
    private final List<Field> serverValues = new ArrayList<>();
    //文件导出结果
    private boolean isSuccess = false;
    //所需要的包名:com.alibaba.fastjson.JSON、java.util.ArrayList、java.util.List、com.game.script.ScriptConfigManager
    private final List<String> imports = new ArrayList<>();
    //语言包字段列表
    private final List<ExcelLoadFiled> languageList = new ArrayList<>();
    //bean初始化参数
    private final StringBuilder beanParms = new StringBuilder();

    /**
     * 获取文件名
     *
     * @return
     */
    public String getFilename() {
        return filename;
    }

    /**
     * 设置文件名
     *
     * @param filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 生成java的bean名字
     *
     * @return
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * 生成java的bean名字
     *
     * @param beanName
     */
    public void setBeanName(String beanName) {
        this.beanName = beanName;
    }

    /**
     * 三行及后面行数的服务器用的配置数据
     *
     * @return
     */
    public List<Field> getServerValues() {
        return serverValues;
    }

    /**
     * 文件导出结果
     *
     * @return
     */
    public boolean isIsSuccess() {
        return isSuccess;
    }

    /**
     * 文件导出结果
     *
     * @param isSuccess
     */
    public void setIsSuccess(boolean isSuccess) {
        this.isSuccess = isSuccess;
    }

    /**
     * fileds
     *
     * @return
     */
    public HashMap<Integer, ExcelLoadFiled> getFileds() {
        return fileds;
    }

    /**
     * 服务器字段数据列表
     *
     * @return
     */
    public List<ExcelLoadFiled> getFiledsServerUseList() {
        return filedsServerUseList;
    }

    /**
     * 所需要的包名:com.alibaba.fastjson.JSON、java.util.ArrayList、java.util.List、java.util.HashMap、com.game.script.ScriptConfigManager
     *
     * @return
     */
    public List<String> getImports() {
        return imports;
    }

    /**
     *
     * @param importStr
     * com.alibaba.fastjson.JSON、java.util.ArrayList、java.util.List、java.util.HashMap、com.game.script.ScriptConfigManager
     */
    public void addImports(String importStr) {
        if (imports.contains(importStr)) {
            return;
        }
        imports.add(importStr);
    }

    /**
     * 语言包字段列表
     *
     * @return
     */
    public List<ExcelLoadFiled> getLanguageList() {
        return languageList;
    }

    /**
     * bean初始化参数
     *
     * @return
     */
    public StringBuilder getBeanParms() {
        return beanParms;
    }

}
