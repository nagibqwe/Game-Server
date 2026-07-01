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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import tools.WriteFile;

/**
 * ItemChangeReason表单独导出
 * 九 零 一起玩 www.901 75.com
 * @author cxl
 */
public class ItemChangeReasonExcelToCode {

    public final static Logger error = Logger.getLogger("ERRORLOG");
    public final static Logger logger = Logger.getLogger("ItemChangeReasonExcelToCode");

    /**
     * 获得excel数据
     *
     * @param excelPath
     * @param javaCodeBasePathes
     */
    public static void ItemChangeReasonToCode(String excelPath, List<String> javaCodeBasePathes) {
        File f = new File(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "ItemChangeReason.xlsx");
        if (!f.canRead()) {
            return;
        }
        int rowNum = 0;
        int colNum = 0;
        try {
            try (FileInputStream in = new FileInputStream(System.getProperty("user.dir") + File.separator + excelPath + File.separator + "ItemChangeReason.xlsx")) {
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
                    error.error("ItemChangeReason工作表行数格式错误，不得低于5行：");
                    return;
                }
                int index=0;
                List<ExcelLoadFiled> list = new ArrayList<>();
                List<String> imports = new ArrayList<>();
                String allDesc = "";
                for (int i = 5; i <= rows; i++) {
                    rowNum = i + 1;
                    XSSFRow row = sheet.getRow(i);
                    if (row == null) {
                        continue;
                    }
                    if (row.getCell(0) == null || row.getCell(0).getRawValue() == ""
                            || row.getCell(1) == null || row.getCell(1).getRawValue() == "") {
                        continue;
                    }
                    ExcelLoadFiled filedValue = new ExcelLoadFiled();
                    //取出第2列
                    colNum = 2;
                    String str = row.getCell(1).getStringCellValue();
                    String[] definelist =  str.split(";");
                    filedValue.setFiledName(definelist[0]);
                    if (definelist.length<2){
                        error.error("表  ItemChangeReason 行数" + i + row.getCell(0).getRawValue() +" 没用 ; 符号把字段描述分开" );
                        filedValue.setFiledDesc(definelist[0]);
                    }else{
                        filedValue.setFiledDesc(definelist[1]);
                    }
                    if (i < rows){
                        allDesc += definelist[1] + ",";
                    }else{
                        allDesc += definelist[1];
                    }
                    filedValue.setFiledType("int");
                    str = row.getCell(0).getRawValue();
                    filedValue.setJavaClassName(str);
                    list.add(filedValue);
                }
                ExcelLoadFiled filedValue = new ExcelLoadFiled();
                if (!imports.contains("com.data.struct.ReadStringArray")) {
                    imports.add("com.data.struct.ReadStringArray");
                }
                allDesc =  "\"" + allDesc +  "\"";
                filedValue.setFiledDesc("");
                filedValue.setFiledType("[char]");
                filedValue.setJavaClassName(allDesc);
                filedValue.setFiledName("allDescString");
                list.add(filedValue);
                try {
                    HashMap<String, Object> root = new HashMap<>();
                    root.put("list", list);
                    root.put("package", ConfigExcelReader.javaPackageName);
                    root.put("imports", imports);
                    Template beanTemp = WriteFile.getCfg().getTemplate("ItemChangeReason.ftl", "utf-8"); // Global模板
                    //Global文件
                    try (Writer out = new StringWriter()) {
                        beanTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "ItemChangeReason.java", content);
                        }
                        logger.info("ItemChangeReason.java导出成功。");
                    }
                    //输出load文件
                    Template loadTemp = WriteFile.getCfg().getTemplate("Cfg_ItemChangeReason_Load.ftl", "utf-8"); // Cfg_Global_Load模板
                    try (Writer out = new StringWriter()) {
                        loadTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaScriptPath + File.separator + "Cfg_ItemChangeReason_Load.java", content);
                        }
                        logger.info("Cfg_ItemChangeReason_Load.java导出成功。");
                    }
                    //Global文件
                    Template contaTemp = WriteFile.getCfg().getTemplate("Cfg_ItemChangeReason_Container.ftl", "utf-8"); //Cfg_Global_Container模板
                    try (Writer out = new StringWriter()) {
                        contaTemp.process(root, out);
                        String content = out.toString();
                        for (String s : javaCodeBasePathes) {
                            WriteFile.writeFile(s + ConfigExcelReader.javaPath + File.separator + ConfigExcelReader.javaPackageName.replace('.', '/') + File.separator + "container" + File.separator + "Cfg_ItemChangeReason_Container.java", content);
                        }
                        logger.info("Cfg_ItemChangeReason_Container.java导出成功。");
                    }
                } catch (IOException | TemplateException e) {
                    error.error(e, e);
                }
            }
        } catch (Exception e) {
            error.error("ItemChangeReason：" + rowNum + ";错误列：" + colNum, e);
        }

    }

}
