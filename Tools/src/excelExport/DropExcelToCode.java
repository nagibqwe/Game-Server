/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package excelExport;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.WriteFile;

/**
 * drop表单独导出
 *
 * @author hewei
 */
public class DropExcelToCode {

    public final static Logger logger = Logger.getLogger(DropExcelToCode.class);

    /**
     * 获得excel数据
     *
     * @param excelPath
     * @param javaCodeBasePath
     */
    public static void dropToCode(String excelPath, List<String> javaCodeBasePath) {
        int rowNum = 0;
        int colNum = 0;
        try {
            //输出load文件
            Template loadTemp = WriteFile.getCfg().getTemplate("ConfigScript.ftl", "utf-8"); // 读取Container模板
            //toDrop_GoldLoad(excelPath, javaCodeBasePath, loadTemp);
            toDrop_ItemLoad(excelPath, javaCodeBasePath, loadTemp);
            toDrop_PackageLoad(excelPath, javaCodeBasePath, loadTemp);
            toBeanAndContainer(javaCodeBasePath);
        } catch (Exception e) {
            logger.error("drop表错误行：" + rowNum + ";错误列：" + colNum, e);
        }

    }

    private static void toDrop_GoldLoad(String excelPath, List<String> javaPathes, Template loadTemp) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_gold.xlsm");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_gold.xlsm")) {
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    return;
                }
                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表
                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 2) {
                    logger.error("drop工作表行数格式错误，不得低于2行：");
                    return;
                }
                HashMap<String, String> values = new HashMap<>();
                XSSFRow row;
                String key;
                //取出所有数据值
                for (int i = 3; i <= rows; i++) {
                    rowNum = i + 1;
                    row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }
                    colNum = 0;
                    XSSFCell cell = row.getCell(colNum);
                    if (cell == null) {
                        continue;
                    }
                    key = cell.getRawValue();
                    if (key == null) {
                        continue;
                    }
                    String s = values.get(key);
                    if (s == null) {
                        s = key + ",\"" + getCellStrValue(row, 1) + "," + getCellStrValue(row, 2) + "," + getCellStrValue(row, 3) + "," + getCellStrValue(row, 4)
                                + "," + getCellStrValue(row, 5) + "," + getCellStrValue(row, 6);
                    } else {
                        s += ("]" + getCellStrValue(row, 1) + "," + getCellStrValue(row, 2) + "," + getCellStrValue(row, 3) + "," + getCellStrValue(row, 4)
                                + "," + getCellStrValue(row, 5) + "," + getCellStrValue(row, 6));
                    }
                    values.put(key, s);
                }
                List<Field> listFields = new ArrayList<>();
                Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    Field field = new Field();
                    field.setKey(next.getKey());
                    field.setValue(next.getValue() + "\"");
                    listFields.add(field);
                }
                HashMap<String, Object> root = new HashMap<>();
                //bean的class名
                String classBeanName = ExcelToCode.getJavaClassBeanName("drop_gold");
                //container的class名
                String classContainerName = ExcelToCode.getJavaClassContainerName("drop_gold");
                //获取load的class名
                String classLoadName = ExcelToCode.getJavaClassLoadName("drop_gold");
                root.put("classBeanName", classBeanName);
                root.put("classContainerName", classContainerName);
                root.put("classLoadName", classLoadName);
                root.put("explain", "drop_gold配置表");
                root.put("package", ConfigExcelReader.javaPackageName);
                root.put("values", listFields);
                try (Writer out = new StringWriter()) {
                    loadTemp.process(root, out);
                    for (String str : javaPathes) {
                        WriteFile.writeFile(str + ConfigExcelReader.javaScriptPath + File.separator + classLoadName + ".java", out.toString());
                    }
                }
            }
        } catch (IOException | TemplateException e) {
            logger.error("drop_gold表错误行：" + rowNum + ";错误列：" + colNum, e);
        }
    }

    private static void toDrop_ItemLoad(String excelPath, List<String> javaPathes, Template loadTemp) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_item.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_item.xlsx")) {
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    return;
                }
                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表
                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 5) {
                    logger.error("drop工作表行数格式错误，不得低于5行：");
                    return;
                }
                XSSFRow row = sheet.getRow(1);
                int cellNumber = row.getLastCellNum();
                HashMap<String, String> values = new HashMap<>();
                String key;
                //取出所有数据值
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }
                    colNum = 0;
                    XSSFCell cell = row.getCell(colNum);
                    if (cell == null) {
                        continue;
                    }
                    key = cell.getRawValue();
                    if (key == null) {
                        continue;
                    }
                    String s = values.get(key);
                    if (s == null) {
                        s = key + "," + getCellStrValue(row, 1) + "," + getCellStrValue(row, 2) + ",\"" + getCellStrValue(row, 5) + "," + getCellStrValue(row, 6) + ","
                                + getCellStrValue(row, 7) + "," + getCellStrValue(row, 8) + "," + getCellStrValue(row, 9) + "," + getCellStrValue(row, 10)
                                + "," + getCellStrValue(row, 11) + "," + getCellStrValue(row, 12);
                    } else {
                        s += ("]" + getCellStrValue(row, 5) + "," + getCellStrValue(row, 6) + "," + getCellStrValue(row, 7) + "," + getCellStrValue(row, 8) + ","
                                + getCellStrValue(row, 9) + "," + getCellStrValue(row, 10) + "," + getCellStrValue(row, 11) + "," + getCellStrValue(row, 12));
                    }
                    values.put(key, s);
                }
                List<Field> listFields = new ArrayList<>();
                Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    Field field = new Field();
                    field.setKey(next.getKey());
                    field.setValue(next.getValue() + "\"");
                    listFields.add(field);
                }
                HashMap<String, Object> root = new HashMap<>();
                //bean的class名
                String classBeanName = ExcelToCode.getJavaClassBeanName("drop_item");
                //container的class名
                String classContainerName = ExcelToCode.getJavaClassContainerName("drop_item");
                //获取load的class名
                String classLoadName = ExcelToCode.getJavaClassLoadName("drop_item");
                root.put("classBeanName", classBeanName);
                root.put("classContainerName", classContainerName);
                root.put("classLoadName", classLoadName);
                root.put("explain", "drop_gold配置表");
                root.put("package", ConfigExcelReader.javaPackageName);
                root.put("values", listFields);
                try (Writer out = new StringWriter()) {
                    loadTemp.process(root, out);
                    for (String str : javaPathes) {
                        WriteFile.writeFile(str + ConfigExcelReader.javaScriptPath + File.separator + classLoadName + ".java", out.toString());
                    }
                }
            }
        } catch (IOException | TemplateException e) {
            logger.error("drop_gold表错误行：" + rowNum + ";错误列：" + colNum, e);
        }
    }

    private static void toDrop_PackageLoad(String excelPath, List<String> javaPathes, Template loadTemp) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_package.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "drop_package.xlsx")) {
                XSSFWorkbook wb = new XSSFWorkbook(in);
                int sheetNumber = wb.getNumberOfSheets(); //获得工作表数量
                if (sheetNumber < 1) {
                    return;
                }
                //只取第一个工作表格
                XSSFSheet sheet = wb.getSheetAt(0); //获得工作表
                //数据工作表
                int rows = sheet.getLastRowNum();
                if (rows < 5) {
                    logger.error("drop工作表行数格式错误，不得低于5行：");
                    return;
                }
                HashMap<String, String> values = new HashMap<>();
                HashMap<String, Integer> sum = new HashMap<>();
                XSSFRow row;
                String key;
                String weight;
                //取出所有数据值
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    row = sheet.getRow(i);
                    if (row == null) {
                        break;
                    }
                    colNum = 0;
                    XSSFCell cell = row.getCell(colNum);
                    if (cell == null) {
                        continue;
                    }
                    key = cell.getRawValue();
                    if (key == null) {
                        continue;
                    }
                    String s = values.get(key);
                    weight = getCellStrValue(row, 7);
                    if (s == null) {
                        s = ",\"" + getCellStrValue(row, 4) + "," + getCellStrValue(row, 5) + ","
                                + getCellStrValue(row, 6) + "," + weight;
                        sum.put(key, Integer.parseInt(weight));
                    } else {
                        s += ("]" + getCellStrValue(row, 4) + "," + getCellStrValue(row, 5) + "," + getCellStrValue(row, 6) + "," + weight );
                        sum.put(key, sum.get(key) + Integer.parseInt(weight));
                    }
                    values.put(key, s);
                }
                List<Field> listFields = new ArrayList<>();
                Iterator<Map.Entry<String, String>> iterator = values.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> next = iterator.next();
                    Field field = new Field();
                    field.setKey(next.getKey());
                    field.setValue(next.getKey() + "," + sum.get(next.getKey()) + next.getValue() + "\"");
                    listFields.add(field);
                }
                HashMap<String, Object> root = new HashMap<>();
                //bean的class名
                String classBeanName = ExcelToCode.getJavaClassBeanName("drop_package");
                //container的class名
                String classContainerName = ExcelToCode.getJavaClassContainerName("drop_package");
                //获取load的class名
                String classLoadName = ExcelToCode.getJavaClassLoadName("drop_package");
                root.put("classBeanName", classBeanName);
                root.put("classContainerName", classContainerName);
                root.put("classLoadName", classLoadName);
                root.put("explain", "drop_gold配置表");
                root.put("package", ConfigExcelReader.javaPackageName);
                root.put("values", listFields);
                try (Writer out = new StringWriter()) {
                    loadTemp.process(root, out);
                    for (String str : javaPathes) {
                        WriteFile.writeFile(str + ConfigExcelReader.javaScriptPath + File.separator + classLoadName + ".java", out.toString());
                    }
                }
            }

        } catch (IOException | TemplateException e) {
            logger.error("drop_gold表错误行：" + rowNum + ";错误列：" + colNum, e);
        }
    }

    private static String getCellStrValue(XSSFRow row, int colNum) {
        XSSFCell cell = row.getCell(colNum);
        if (cell == null) {
            return "0";
        }
        String str = cell.getRawValue();
        if (str == null) {
            return "0";
        }
        return str;
    }

    private static void toBeanAndContainer(List<String> javaPathes) {
        for (String javaPath : javaPathes) {
            //toBean(javaPath, "Drop_gold");
            toBean(javaPath, "Drop_item");
            toBean(javaPath, "Drop_package");
            //ExcelToCode.toContainer(javaPath, "drop_gold");
            ExcelToCode.toContainer(javaPath, "drop_item");
            ExcelToCode.toContainer(javaPath, "drop_package");
        }

    }

    private static void toBean(String javeCodeBasePath, String fileName) {
        try {
            HashMap<String, Object> root = new HashMap<>();
            Template beanTemp = WriteFile.cfg.getTemplate("Cfg_" + fileName + "_Bean.ftl", "utf-8"); // 读取bean模板
            //输出bean文件
            try (Writer out = new StringWriter()) {
                beanTemp.process(root, out);
                WriteFile.writeFile(javeCodeBasePath + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "bean" + File.separator
                        + "Cfg_" + fileName + "_Bean.java", out.toString());
            }
        } catch (IOException | TemplateException e) {

        }
    }
}
